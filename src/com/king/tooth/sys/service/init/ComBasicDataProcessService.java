package com.king.tooth.sys.service.init;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComDataDictionary;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComOperLog;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.entity.common.ComProjectModuleBody;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.common.ComTabledata;
import com.king.tooth.sys.entity.datalinks.ComDataLinks;
import com.king.tooth.sys.entity.datalinks.ComProjectComSqlScriptLinks;
import com.king.tooth.sys.entity.datalinks.ComProjectComTabledataLinks;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 基础数据处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComBasicDataProcessService extends AbstractService{

	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void loadSysBasicDatasBySysFirstStart() {
		try {
			initDatabaseInfo();
			updateInitConfig();
			Log4jUtil.debug("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.debug("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		}
	}
	
	/**
	 * 初始化数据库信息
	 */
	private void initDatabaseInfo() {
		try {
			processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
			// 设置当前操作的项目，获得对应的sessionFactory
			CurrentThreadContext.setProjectId(CurrentSysInstanceConstants.currentSysProjectInstance.getId());
			
			createTables();
			
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			
			insertHbmContentsToSessionFactory();// 根据表创建hbm文件，并将其加入到SessionFactory中
			insertDatabaseOfBasicDatas();// 插入配置库的基础数据
			
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		}finally{
			HibernateUtil.closeCurrentThreadSession();
		}
	}
	
	/**
	 * 获取要初始化的表集合
	 * @return
	 */
	private List<ComTabledata> getInitTables(){
		List<ComTabledata> tables = new ArrayList<ComTabledata>(17);
		String dbType = CurrentSysInstanceConstants.currentSysDatabaseInstance.getDbType();
		
		tables.add(new ComSysResource().toCreateTable(dbType));
		tables.add(new ComHibernateHbm().toCreateTable(dbType));
		tables.add(new ComColumndata().toCreateTable(dbType));
		tables.add(new ComTabledata().toCreateTable(dbType));
		tables.add(new ComDatabase().toCreateTable(dbType));
		tables.add(new ComDataDictionary().toCreateTable(dbType));
		tables.add(new ComDataLinks().toCreateTable(dbType));
		tables.add(new ComOperLog().toCreateTable(dbType));
		tables.add(new ComProject().toCreateTable(dbType));
		tables.add(new ComProjectModule().toCreateTable(dbType));
		tables.add(new ComProjectModuleBody().toCreateTable(dbType));
		tables.add(new ComReqLog().toCreateTable(dbType));
		tables.add(new ComSqlScript().toCreateTable(dbType));
		tables.add(new ComSysAccount().toCreateTable(dbType));
		tables.add(new ComSysAccountOnlineStatus().toCreateTable(dbType));
		
		tables.add(new ComProjectComSqlScriptLinks().toCreateTable(dbType));
		tables.add(new ComProjectComTabledataLinks().toCreateTable(dbType));
		
		return tables;
	}
	
	/**
	 * 清除表信息
	 * @param tables
	 */
	private void clearTables(List<ComTabledata> tables){
		for (ComTabledata table : tables) {
			table.clear();
		}
		tables.clear();
	}
	
	/**
	 * 创建表
	 * @return 
	 */
	private void createTables(){
		List<ComTabledata> tables = getInitTables();
		DBTableHandler dbHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysDatabaseInstance);
		try {
			dbHandler.dropTable(tables);
		} catch (Exception e) {
			Log4jUtil.debug("*********表不存在，不需要删除");
		}
		// 开始创建表
		dbHandler.createTable(tables);
		clearTables(tables);
	}
	
	/**
	 * 插入数据库库的基础数据
	 */
	private void insertDatabaseOfBasicDatas() {
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加平台开发账户【1.平台开发账户】
		ComSysAccount admin = new ComSysAccount();
		admin.setAccountType(1);
		admin.setLoginName("admin");
		admin.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		String adminAccountId = HibernateUtil.saveObject(admin, null);
		
		
		// 添加一般开发账户【2.一般开发账户】
		ComSysAccount normal = new ComSysAccount();
		normal.setAccountType(2);
		normal.setLoginName("normal");
		normal.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), normal.getLoginPwdKey()));
		normal.setValidDate(DateUtil.parseDate("2019-12-31 23:59:59"));
		HibernateUtil.saveObject(normal, adminAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		insertAllTables(adminAccountId);// 将表信息插入的cfgTabledata表中，同时把列的信息插入到cfgColumndata表中；创建者是平台开发账户
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据字典数据
		insertDataDictionary(adminAccountId);
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到SessionFactory中
	 */
	private void insertHbmContentsToSessionFactory() {
		List<ComTabledata> tables = getInitTables();
		
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		List<String> hbmContents = new ArrayList<String>(tables.size());
		for (ComTabledata table : tables) {
			hbmContents.add(hibernateHbmHandler.createHbmMappingContent(table));// 记录hbm内容
		}
		
		// 将hbmContents加入到hibernate sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		
		hbmContents.clear();
		clearTables(tables);
	}
	
	/**
	 * 将表信息插入的cfgTabledata表中
	 * <p>同时把列的信息插入到cfgColumndata表中</p>
	 * <p>再根据表创建hbm文件，并将其加入到CfgHibernateHbm表中</p>
	 * @param adminAccountId 
	 */
	private void insertAllTables(String adminAccountId) {
		List<ComTabledata> tables = getInitTables();
		
		String tableId;
		List<ComColumndata> columns = null;
		ComHibernateHbm hbm;
		ComSysResource resource;
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		for (ComTabledata table : tables) {
			// 插入表和列信息
			tableId = HibernateUtil.saveObject(table, adminAccountId);
			table.setId(tableId);
			columns = table.getColumns();
			for (ComColumndata column : columns) {
				column.setTableId(tableId);
				HibernateUtil.saveObject(column, adminAccountId);
			}
			
			// 创建对应的hbm文件，并保存
			hbm = new ComHibernateHbm();
			hbm.tableTurnToHbm(table);
			hbm.setHbmContent(hibernateHbmHandler.createHbmMappingContent(table));
			HibernateUtil.saveObject(hbm, adminAccountId);
			
			// 保存到资源表中
			resource = table.turnToResource();
			HibernateUtil.saveObject(resource, adminAccountId);
		}
		clearTables(tables);
	}
	
	/**
	 * 修改初始化的配置文件内容
	 */
	private void updateInitConfig() {
		if("false".equals(SysConfig.getSystemConfig("is.develop"))){
			// 如果不是开发模式的话，在进行了初始化操作后，系统自动去修改api.platform.init.properties配置文件的内容，将true值改为false
			File file = new File(SysConfig.WEB_SYSTEM_CONTEXT_REALPATH + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "api.platform.init.properties");
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write("is.init.baisc.data=false");
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				CloseUtil.closeIO(bw);// 关闭了bw的话，fw也会被关闭，因为bw引用了bw，所以这里只要关闭bw即可，如果再强行关闭fw，会报错，提示流被关闭
			}
		}
	}
	
	/**
	 * 处理本系统和本数据库的关系
	 * 并将
	 */
	private void processCurrentSysOfPorjDatabaseRelation() {
		// 添加本系统和本数据库的映射关系
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(
				CurrentSysInstanceConstants.currentSysProjectInstance.getId(), 
				CurrentSysInstanceConstants.currentSysDatabaseInstance.getId());
	}
	
	//---------------------------------------------------------------------------------------------------
	/**
	 * 添加数据字典的基础数据
	 * @param adminAccountId 
	 */
	private void insertDataDictionary(String adminAccountId) {
		// ComColumndata.columnType 字段数据类型
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "字符串", "string", 1);
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "布尔值", "boolean", 2);
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "整型", "integer", 3);
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "浮点型", "double", 4);
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "日期", "date", 5);
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "字符大字段", "clob", 6);
		insertDataDictionary(adminAccountId, null, "cfgcolumndata.columntype", "二进制大字段", "blob", 7);
		
		// ComDatabase.dbType 数据库类型
		insertDataDictionary(adminAccountId, null, "cfgdatabase.dbtype", "oracle", "oracle", 1);
		insertDataDictionary(adminAccountId, null, "cfgdatabase.dbtype", "sqlserver", "sqlserver", 2);
		
		// ComTabledata.tableType 表类型
		insertDataDictionary(adminAccountId, null, "cfgtabledata.tabletype", "单表", "1", 1);
		insertDataDictionary(adminAccountId, null, "cfgtabledata.tabletype", "树表", "2", 2);
		insertDataDictionary(adminAccountId, null, "cfgtabledata.tabletype", "主子表", "3", 3);
		
		// ComTabledata.dbType 数据库类型
		insertDataDictionary(adminAccountId, null, "cfgtabledata.dbtype", "oracle", "oracle", 1);
		insertDataDictionary(adminAccountId, null, "cfgtabledata.dbtype", "sqlserver", "sqlserver", 2);
		
		// ComOperLog.operType 操作的类型
		insertDataDictionary(adminAccountId, null, "comoperLog.opertype", "查询", "select", 1);
		insertDataDictionary(adminAccountId, null, "comoperLog.opertype", "增加", "insert", 2);
		insertDataDictionary(adminAccountId, null, "comoperLog.opertype", "修改", "update", 3);
		insertDataDictionary(adminAccountId, null, "comoperLog.opertype", "删除", "delete", 4);
		
		// ComPermission.permissionType 权限的类型
		insertDataDictionary(adminAccountId, null, "compermission.permissiontype", "模块", "1", 1);
		insertDataDictionary(adminAccountId, null, "compermission.permissiontype", "页面操作", "2", 2);
		
		// ComSysAccount.accountType 账户类型
		insertDataDictionary(adminAccountId, null, "comsysaccount.accounttype", "超级管理员", "0", 0);
		insertDataDictionary(adminAccountId, null, "comsysaccount.accounttype", "游客", "1", 1);
		insertDataDictionary(adminAccountId, null, "comsysaccount.accounttype", "客户", "2", 2);
		insertDataDictionary(adminAccountId, null, "comsysaccount.accounttype", "普通账户", "3", 3);
		insertDataDictionary(adminAccountId, null, "comsysaccount.accounttype", "普通虚拟账户", "4", 4);
		
		// ComSysAccount.accountStatus 账户状态
		insertDataDictionary(adminAccountId, null, "comsysaccount.accountstatus", "启用", "1", 1);
		insertDataDictionary(adminAccountId, null, "comsysaccount.accountstatus", "禁用", "2", 2);
		insertDataDictionary(adminAccountId, null, "comsysaccount.accountstatus", "过期", "3", 3);
		
		// ComSysResource.resourceType 账户状态
		insertDataDictionary(adminAccountId, null, "comsysresource.resourcetype", "表资源", "1", 1);
		insertDataDictionary(adminAccountId, null, "comsysresource.resourcetype", "sql脚本资源", "2", 2);
		insertDataDictionary(adminAccountId, null, "comsysresource.resourcetype", "代码资源", "2", 2);
		insertDataDictionary(adminAccountId, null, "comsysresource.resourcetype", "数据库资源", "2", 2);
		insertDataDictionary(adminAccountId, null, "comsysresource.resourcetype", "项目资源", "2", 2);
		
		// ComUser.userStatus 账户状态
		insertDataDictionary(adminAccountId, null, "comuser.userstatus", "在职", "1", 1);
		insertDataDictionary(adminAccountId, null, "comuser.userstatus", "离职", "2", 2);
		insertDataDictionary(adminAccountId, null, "comuser.userstatus", "休假", "3", 3);
	}
	
	/**
	 * 初始化添加数据字典
	 * @param parentId
	 * @param code
	 * @param codeCaption
	 * @param codeValue
	 * @param orderCode
	 * @return
	 */
	private String insertDataDictionary(String adminAccountId, String parentId, String code, String codeCaption, String codeValue, int orderCode){
		ComDataDictionary dataDictionary = new ComDataDictionary();
		dataDictionary.setParentCodeId(parentId);
		dataDictionary.setCode(code);
		dataDictionary.setCodeCaption(codeCaption);
		dataDictionary.setCodeValue(codeValue);
		dataDictionary.setOrderCode(orderCode);
		return HibernateUtil.saveObject(dataDictionary, adminAccountId);
	}
	
	//------------------------------------------------------------------------------------
	
	/**
	 * 系统每次启动时，加载hbm的配置信息
	 * 主要是hbm内容
	 */
	public void loadSysBasicDatasBySysStart() {
		processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
		try {
			// 先加载当前系统的所有hbm映射文件
			loadHbmContentsByDatabaseId(CurrentSysInstanceConstants.currentSysDatabaseInstance);
			
			// 再加载系统中所有数据库信息，创建动态数据源，动态sessionFactory，以及将各个数据库中的hbm加载进自己的sessionFactory中
			List<ComDatabase> databases = HibernateUtil.extendExecuteListQueryByHqlArr(ComDatabase.class, null, null, "from ComDatabase where isEnabled = 1");
			HibernateUtil.closeCurrentThreadSession();
			
			if(databases != null && databases.size()> 0){
				for (ComDatabase database : databases) {
					database.analysisResourceProp();
					DynamicDBUtil.addDataSource(database);// 创建对应的动态数据源和sessionFactory
					loadHbmContentsByDatabaseId(database);// 加载当前数据库中的hbm到sessionFactory中
				}
				
				// 加载数据库和项目的关联关系映射
				CurrentThreadContext.setProjectId(CurrentSysInstanceConstants.currentSysProjectInstance.getId());// 设置当前操作的项目，获得对应的sessionFactory
				String projDatabaseRelationQueryHql = "select "+ResourceNameConstants.ID+" from ComProject where isEnabled = 1 and refDatabaseId = ?";
				for (ComDatabase database : databases) {
					loadProjIdWithDatabaseIdRelation(projDatabaseRelationQueryHql, database.getId());
				}
				HibernateUtil.closeCurrentThreadSession();
			}
		} catch (Exception e) {
			Log4jUtil.debug("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		}
	}
	
	/**
	 * 加载项目id和数据库id的关联关系
	 * @param projDatabaseRelationQueryHql
	 * @param databaseId
	 */
	private void loadProjIdWithDatabaseIdRelation(String projDatabaseRelationQueryHql, String databaseId) {
		List<Object> projIds = HibernateUtil.executeListQueryByHqlArr(null, null, projDatabaseRelationQueryHql, databaseId);
		if(projIds != null && projIds.size() > 0){
			for (Object projId : projIds) {
				if(StrUtils.isEmpty(projId)){
					continue;
				}
				ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(projId.toString(), databaseId);
			}
			projIds.clear();
		}
	}

	/**
	 * 加载指定数据库的hbm映射文件
	 * @param database 指定数据库的id
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void loadHbmContentsByDatabaseId(ComDatabase database) throws SQLException, IOException {
		CurrentThreadContext.setDatabaseId(database.getId());
		// 获取当前系统的ComHibernateHbm映射文件对象
		String sql = "select hbm_content from com_hibernate_hbm where ref_database_id = '"+database.getId()+"' and hbm_resource_name = 'ComHibernateHbm' and is_enabled = 1";
		Clob clob = (Clob) HibernateUtil.executeUniqueQueryBySql(sql, null);
		if(clob == null){
			throw new NullPointerException("数据库名为["+database.getDbDisplayName()+"]，实例名为["+database.getDbInstanceName()+"]，ip为["+database.getDbIp()+"]，端口为["+database.getDbPort()+"]，用户名为["+database.getLoginUserName()+"]，密码为["+database.getLoginPassword()+"]，的数据库中，没有查询到ComHibernateHbm的hbm文件内容，请检查：[" + sql + "]");
		}
		
		Reader reader = clob.getCharacterStream();
		StringBuilder hbmContent = new StringBuilder();
		char[] cr = new char[500];
		while(reader.read(cr) != -1){
			hbmContent.append(cr);
			cr = new char[500];
		}
		// 将其加载到当前系统的sessionFactory中
		HibernateUtil.appendNewConfig(hbmContent.toString().trim());
		
		// 查询databaseId指定的库下有多少hbm数据，分页查询并加载到sessionFactory中
		int count = Integer.valueOf(HibernateUtil.executeUniqueQueryBySql("select count(1) from com_hibernate_hbm where is_enabled = 1 and hbm_resource_name != 'ComHibernateHbm' and ref_database_id = '"+database.getId()+"'", null)+"");
		if(count == 0){
			return;
		}
		int loopCount = count/100;
		if(loopCount == 0){
			loopCount = 1;
		}else{
			loopCount++;
		}
		List<Object> hbmContents = null;
		List<String> hcs = null;
		for(int i=0;i<loopCount;i++){
			hbmContents = HibernateUtil.executeListQueryByHql("100", i+"", "select hbmContent from ComHibernateHbm where isEnabled = 1", null);
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
