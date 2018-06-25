package com.king.tooth.sys.service.init.app;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 初始化运行系统的服务器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class InitAppSystemService extends AbstractService{

	/**
	 * 处理本系统和本数据库的关系
	 */
	private void processCurrentSysOfPorjDatabaseRelation() {
		// 添加本系统和本数据库的映射关系
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(
				CurrentSysInstanceConstants.currentSysBuiltinProjectInstance.getId(), 
				CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance.getId());
	}
	
	/**
	 * 系统每次启动时，加载hbm的配置信息
	 * 主要是hbm内容
	 */
	public void loadHbmsByStart() {
		Log4jUtil.info("loadHbmsByStart..........");
		processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
		try {
			// 先加载当前系统的所有hbm映射文件
			loadHbmContentsByDatabase(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
			
			// 再加载系统中所有数据库信息，创建动态数据源，动态sessionFactory，以及将各个数据库中的hbm加载进自己的sessionFactory中
			List<ComDatabase> databases = HibernateUtil.extendExecuteListQueryByHqlArr(ComDatabase.class, null, null, "from ComDatabase where isEnabled = 1 and belongPlatformType = 2");
			if(databases != null && databases.size()> 0){
				String projDatabaseRelationQueryHql = "select "+ResourceNameConstants.ID+" from ComProject where isEnabled = 1 and refDatabaseId = ?";
				for (ComDatabase database : databases) {
					if(existsPublishedProjects(projDatabaseRelationQueryHql, database)){
						database.analysisResourceProp();
						String testLinkResult = database.testDbLink();
						if(testLinkResult.startsWith("err")){
							throw new Exception(testLinkResult);
						}
						Log4jUtil.debug("测试连接数据库[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]：" + testLinkResult);
						
						DynamicDBUtil.addDataSource(database);// 创建对应的动态数据源和sessionFactory
						loadHbmContentsByDatabase(database);// 加载当前数据库中的hbm到sessionFactory中
					}
				}
			}else{
				HibernateUtil.closeCurrentThreadSession();
			}
		} catch (Exception e) {
			Log4jUtil.debug("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		}
	}
	
	/**
	 * 验证数据库下是否有发布的项目
	 * @param projDatabaseRelationQueryHql
	 * @param database
	 * @return
	 */
	private boolean existsPublishedProjects(String projDatabaseRelationQueryHql, ComDatabase database) {
		// 加载数据库和项目的关联关系映射
		CurrentThreadContext.setDatabaseId(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance.getId());// 设置当前操作的项目，获得对应的sessionFactory，即运行系统
		boolean isExists = loadProjIdWithDatabaseIdRelation(projDatabaseRelationQueryHql, database.getId());
		HibernateUtil.closeCurrentThreadSession();
		Log4jUtil.debug("数据库[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]的数据库，是否存在发布的项目："+ isExists );
		return isExists;
	}
	
	/**
	 * 加载项目id和数据库id的关联关系
	 * @param projDatabaseRelationQueryHql
	 * @param databaseId
	 * @return 指定的数据库下，是否存在发布的项目
	 */
	private boolean loadProjIdWithDatabaseIdRelation(String projDatabaseRelationQueryHql, String databaseId) {
		List<Object> projIds = HibernateUtil.executeListQueryByHqlArr(null, null, projDatabaseRelationQueryHql, databaseId);
		if(projIds != null && projIds.size() > 0){
			for (Object projId : projIds) {
				if(StrUtils.isEmpty(projId)){
					continue;
				}
				ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(projId.toString(), databaseId);
			}
			projIds.clear();
			return true;
		}
		return false;
	}
	
	/**
	 * 加载指定数据库的hbm映射文件
	 * @param database 指定数据库的id
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void loadHbmContentsByDatabase(ComDatabase database) throws SQLException, IOException {
		CurrentThreadContext.setDatabaseId(database.getId());
		
		// 获取当前系统的ComHibernateHbm映射文件对象
		String sql = "select hbm_content from com_hibernate_hbm where ref_database_id = '"+database.getId()+"' and hbm_resource_name = 'ComHibernateHbm' and is_enabled = 1";
		String hbmContent = null;
		if(DynamicDataConstants.DB_TYPE_SQLSERVER.equals(SysConfig.getSystemConfig("jdbc.dbType"))){
			hbmContent = ((String) HibernateUtil.executeUniqueQueryBySql(sql, null)).trim();
		}else if(DynamicDataConstants.DB_TYPE_ORACLE.equals(SysConfig.getSystemConfig("jdbc.dbType"))){
			Clob clob = (Clob) HibernateUtil.executeUniqueQueryBySql(sql, null);
			if(clob == null){
				throw new NullPointerException("数据库名为["+database.getDbDisplayName()+"]，实例名为["+database.getDbInstanceName()+"]，ip为["+database.getDbIp()+"]，端口为["+database.getDbPort()+"]，用户名为["+database.getLoginUserName()+"]，密码为["+database.getLoginPassword()+"]，的数据库中，没有查询到ComHibernateHbm的hbm文件内容，请检查：[" + sql + "]");
			}
			
			Reader reader = clob.getCharacterStream();
			StringBuilder hbmContentSB = new StringBuilder();
			char[] cr = new char[500];
			while(reader.read(cr) != -1){
				hbmContentSB.append(cr);
				cr = new char[500];
			}
			
			hbmContent = hbmContentSB.toString().trim();
			hbmContentSB.setLength(0);
		}
		// 将其加载到当前系统的sessionFactory中
		HibernateUtil.appendNewConfig(hbmContent);
		
		// 查询databaseId指定的库下有多少hbm数据，分页查询并加载到sessionFactory中
		int count = ((Long)HibernateUtil.executeUniqueQueryBySql("select count("+ResourceNameConstants.ID+") from ComHibernateHbm where isEnabled = 1 and hbmResourceName != 'ComHibernateHbm' and refDatabaseId = '"+database.getId()+"'", null)).intValue();
		if(count == 0){
			return;
		}
		int loopCount = count/100 + 1;
		List<Object> hbmContents = null;
		List<String> hcs = null;
		for(int i=0;i<loopCount;i++){
			hbmContents = HibernateUtil.executeListQueryByHql("100", (i+1)+"", "select hbmContent from ComHibernateHbm where isEnabled = 1 and hbmResourceName !='ComHibernateHbm' and refDatabaseId = '"+database.getId()+"'", null);
			hcs = new ArrayList<String>(hbmContents.size());
			for (Object obj : hbmContents) {
				hcs.add(obj+"");
			}
			HibernateUtil.appendNewConfig(hcs);
			hcs.clear();
			hbmContents.clear();
		}
		// 关闭session
		HibernateUtil.closeCurrentThreadSession();
	}
}
