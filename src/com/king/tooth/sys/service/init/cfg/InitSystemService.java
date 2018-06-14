package com.king.tooth.sys.service.init.cfg;

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
import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.app.ComRole;
import com.king.tooth.sys.entity.app.datalinks.ComProjectComHibernateHbmLinks;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishBasicData;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.ComProjectComTabledataLinks;
import com.king.tooth.sys.entity.common.ComCode;
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
import com.king.tooth.sys.entity.common.datalinks.ComDataLinks;
import com.king.tooth.sys.entity.common.datalinks.ComProjectComCodeLinks;
import com.king.tooth.sys.entity.common.datalinks.ComProjectComSqlScriptLinks;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 初始化配置系统的服务器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class InitSystemService extends AbstractService{

	/**
	 * 处理本系统和本数据库的关系
	 */
	private void processCurrentSysOfPorjDatabaseRelation() {
		// 添加本系统和本数据库的映射关系
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(
				CurrentSysInstanceConstants.currentSysBuiltinProjectInstance.getId(), 
				CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance.getId());
	}
	
	private List<ComTabledata> tables = new ArrayList<ComTabledata>(22);
	/**
	 * 初始化系统涉及到的所有表
	 * <p>包括每个表中的基础列</p>
	 * @return
	 */
	private void initAllTables(){
		// 核心表
		tables.add(new ComDatabase().toCreateTable());
		tables.add(new ComProject().toCreateTable());
		tables.add(new ComProjectModule().toCreateTable());
		tables.add(new ComHibernateHbm().toCreateTable());
		tables.add(new ComSqlScript().toCreateTable());
		tables.add(new ComCode().toCreateTable());
		tables.add(new ComProjectComSqlScriptLinks().toCreateTable());
		tables.add(new ComProjectComHibernateHbmLinks().toCreateTable());
		tables.add(new ComProjectComCodeLinks().toCreateTable());
		// 通用表
		tables.add(new ComSysResource().toCreateTable());
		tables.add(new ComDataDictionary().toCreateTable());
		tables.add(new ComDataLinks().toCreateTable());
		tables.add(new ComOperLog().toCreateTable());
		tables.add(new ComProjectModuleBody().toCreateTable());
		tables.add(new ComReqLog().toCreateTable());
		tables.add(new ComSysAccount().toCreateTable());
		tables.add(new ComSysAccountOnlineStatus().toCreateTable());
		// 配置系统表
		tables.add(new ComColumndata().toCreateTable());
		tables.add(new ComTabledata().toCreateTable());
		tables.add(new ComPublishInfo().toCreateTable());
		tables.add(new ComPublishBasicData().toCreateTable());
		tables.add(new ComProjectComTabledataLinks().toCreateTable());
		// 运行系统表
		tables.add(new ComRole().toCreateTable());
		// 初始化基础列
		for (ComTabledata table : tables) {
			DynamicBasicDataColumnUtil.initBasicColumnToTable(table);
		}
	}
	
	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void loadSysBasicDatasBySysFirstStart() {
		try {
			processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
			initAllTables();// 初始化系统涉及到的所有表
			initDatabaseInfo();// 初始化数据库信息
			updateInitConfig();
			Log4jUtil.debug("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.debug("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		}finally{
			ResourceHandlerUtil.clearTables(tables);
		}
	}
	
	/**
	 * 初始化数据库信息
	 */
	private void initDatabaseInfo() {
		try {
			// 设置当前操作的项目，获得对应的sessionFactory
			CurrentThreadContext.setProjectId(CurrentSysInstanceConstants.currentSysBuiltinProjectInstance.getId());
			createTables();
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			insertHbmContentsToSessionFactory();// 根据表创建hbm文件，并将其加入到SessionFactory中
			insertBasicDatas();// 插入基础数据
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		}finally{
			HibernateUtil.closeCurrentThreadSession();
		}
	}
	
	/**
	 * 创建表
	 * @return 
	 */
	private void createTables(){
		List<ComTabledata> tmpTables = new ArrayList<ComTabledata>();
		DBTableHandler dbHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
		for (ComTabledata table : tables) {
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			tmpTables.add(table);
		}
		dbHandler.dropTable(tmpTables);// 尝试先删除表
		dbHandler.createTable(tmpTables, false);// 开始创建表
		tmpTables.clear();
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到SessionFactory中
	 */
	private void insertHbmContentsToSessionFactory() {
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		List<String> hbmContents = new ArrayList<String>(tables.size());
		for (ComTabledata table : tables) {
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			hbmContents.add(hibernateHbmHandler.createHbmMappingContent(table, false));// 记录hbm内容
		}
		// 将hbmContents加入到hibernate sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
	}
	
	/**
	 * 插入基础数据
	 */
	private void insertBasicDatas() {
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加管理账户【1.管理账户】
		ComSysAccount admin = new ComSysAccount();
		admin.setAccountType(1);
		admin.setLoginName("admin");
		admin.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		String adminAccountId = HibernateUtil.saveObject(admin, null);
	
		// 添加普通账户【2.普通账户】
		ComSysAccount normal = new ComSysAccount();
		normal.setAccountType(2);
		normal.setLoginName("normal");
		normal.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), normal.getLoginPwdKey()));
		normal.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		HibernateUtil.saveObject(normal, adminAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据库信息【运行平台数据库信息】
		ComDatabase appDatabase = new ComDatabase();
		appDatabase.setDbType(SysConfig.getSystemConfig("jdbc.dbType"));
		appDatabase.setDbInstanceName("SmartOneApp");
		appDatabase.setLoginUserName("SmartOneApp");
		appDatabase.setLoginPassword("1");
		appDatabase.setDbIp(SysConfig.getSystemConfig("db.default.ip"));
		appDatabase.setDbPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
		appDatabase.analysisResourceProp();
		appDatabase.setIsBuiltin(1);
		appDatabase.setIsNeedDeploy(1);
		String appDatabaseId = HibernateUtil.saveObject(appDatabase, null);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加项目信息【运行平台测试项目】
		ComProject testProject = new ComProject();
		testProject.setRefDatabaseId(appDatabaseId);
		testProject.setProjName("运行系统测试用项目");
		testProject.setProjCode("appTestProject");
		testProject.analysisResourceProp();
		HibernateUtil.saveObject(testProject, null);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		insertAllTables(adminAccountId);// 将表信息插入的cfgTabledata表中，同时把列的信息插入到cfgColumndata表中
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据字典基础数据
		insertBasicDataDictionary(adminAccountId);
	}
	
	/**
	 * 将表信息插入的cfgTabledata表中
	 * <p>同时把列的信息插入到cfgColumndata表中</p>
	 * <p>再根据表创建hbm文件，并将其加入到CfgHibernateHbm表中</p>
	 * @param adminAccountId 
	 */
	private void insertAllTables(String adminAccountId) {
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
			
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			// 创建对应的hbm文件，并保存
			hbm = new ComHibernateHbm();
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.tableTurnToHbm(table);
			hbm.setHbmContent(hibernateHbmHandler.createHbmMappingContent(table, false));
			HibernateUtil.saveObject(hbm, adminAccountId);
			
			// 保存到资源表中
			resource = table.turnToResource();
			HibernateUtil.saveObject(resource, adminAccountId);
		}
	}
	
	/**
	 * 添加数据字典的基础数据
	 * @param adminAccountId 
	 */
	private void insertBasicDataDictionary(String adminAccountId) {
		// ComColumndata.columnType 字段数据类型
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "字符串", "string", 1);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "布尔值", "boolean", 2);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "整型", "integer", 3);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "浮点型", "double", 4);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "日期", "date", 5);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "字符大字段", "clob", 6);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "二进制大字段", "blob", 7);
		
		// ComDatabase.dbType 数据库类型
		insertDataDictionary(adminAccountId, "cfgdatabase.dbtype", "oracle", "oracle", 1);
		insertDataDictionary(adminAccountId, "cfgdatabase.dbtype", "sqlserver", "sqlserver", 2);
		
		// ComTabledata.tableType 表类型
		insertDataDictionary(adminAccountId, "cfgtabledata.tabletype", "单表", "1", 1);
		insertDataDictionary(adminAccountId, "cfgtabledata.tabletype", "树表", "2", 2);
		insertDataDictionary(adminAccountId, "cfgtabledata.tabletype", "主子表", "3", 3);
		
		// ComTabledata.dbType 数据库类型
		insertDataDictionary(adminAccountId, "cfgtabledata.dbtype", "oracle", "oracle", 1);
		insertDataDictionary(adminAccountId, "cfgtabledata.dbtype", "sqlserver", "sqlserver", 2);
		
		// ComOperLog.operType 操作的类型
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "查询", "select", 1);
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "增加", "insert", 2);
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "修改", "update", 3);
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "删除", "delete", 4);
		
		// ComSysAccount.accountType 账户类型
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "管理账户", "0", 0);
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "普通账户", "1", 1);
		
		// ComSysAccount.accountStatus 账户状态
		insertDataDictionary(adminAccountId, "comsysaccount.accountstatus", "启用", "1", 1);
		insertDataDictionary(adminAccountId, "comsysaccount.accountstatus", "禁用", "2", 2);
		
		// ComSysResource.resourceType 资源类型
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "表资源", "1", 1);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "sql脚本资源", "2", 2);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "代码资源", "3", 3);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "数据库资源", "4", 4);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "项目资源", "5", 5);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "项目模块资源", "6", 6);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "基础数据资源", "7", 7);
	}
	/**
	 * 添加数据字典
	 * @param code
	 * @param codeCaption
	 * @param codeValue
	 * @param orderCode
	 * @return
	 */
	private String insertDataDictionary(String adminAccountId, String code, String codeCaption, String codeValue, int orderCode){
		ComDataDictionary dataDictionary = new ComDataDictionary();
		dataDictionary.setCode(code);
		dataDictionary.setCodeCaption(codeCaption);
		dataDictionary.setCodeValue(codeValue);
		dataDictionary.setOrderCode(orderCode);
		dataDictionary.setIsCore(1);
		return HibernateUtil.saveObject(dataDictionary, adminAccountId);
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
	
	//------------------------------------------------------------------------------------
	
	/**
	 * 系统每次启动时，加载hbm的配置信息
	 * 主要是hbm内容
	 */
	public void loadSysBasicDatasByStart() {
		processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
		try {
			// 先加载当前系统的所有hbm映射文件
			loadHbmContentsByDatabaseId(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
			
			// 再加载系统中所有数据库信息，创建动态数据源，动态sessionFactory，以及将各个数据库中的核心hbm加载进对应的sessionFactory中
			// 同时建立数据库和项目的关联关系，为之后的发布操作做准备
			List<ComDatabase> databases = HibernateUtil.extendExecuteListQueryByHqlArr(ComDatabase.class, null, null, "from ComDatabase where isEnabled = 1 and belongPlatformType = 2 and isCreated =1");
			
			if(databases != null && databases.size()> 0){
				// 查询获取核心表资源名都有哪些
				List<Object> coreTableResourceNames = HibernateUtil.executeListQueryByHql(null, null, 
						"select resourceName from ComTabledata where isCore=1 and isEnabled=1", null);
				HibernateUtil.closeCurrentThreadSession();
				if(coreTableResourceNames == null || coreTableResourceNames.size() == 0){
					throw new NullPointerException("没有查询到核心的表资源名称，请检查配置系统数据库中的数据是否正确");
				}
				StringBuilder sp = new StringBuilder("(");
				for (Object object : coreTableResourceNames) {
					if("ComHibernateHbm".equals(object.toString())){
						continue;
					}
					sp.append("?,");
				}
				sp.setLength(sp.length()-1);
				sp.append(")");
				String placholders = sp.toString();
				sp.setLength(0);
				
				for (ComDatabase database : databases) {
					database.analysisResourceProp();
					String testLinkResult = database.testDbLink();
					if(testLinkResult.startsWith("err")){
						throw new Exception(testLinkResult);
					}
					Log4jUtil.debug("测试连接数据库[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]：" + testLinkResult);
					
					DynamicDBUtil.addDataSource(database);// 创建对应的动态数据源和sessionFactory
					loadCoreHbmContentsByDatabaseId(database, coreTableResourceNames, placholders);// 加载当前数据库中的hbm到sessionFactory中
				}
				
				coreTableResourceNames.clear();
				
				// 加载数据库和项目的关联关系映射
				CurrentThreadContext.setProjectId(CurrentSysInstanceConstants.currentSysBuiltinProjectInstance.getId());// 设置当前操作的项目，获得对应的sessionFactory，即配置系统
				String projDatabaseRelationQueryHql = "select "+ResourceNameConstants.ID+" from ComProject where isEnabled = 1 and isCreated =1 and refDatabaseId = ?";
				for (ComDatabase database : databases) {
					loadProjIdWithDatabaseIdRelation(projDatabaseRelationQueryHql, database.getId());
				}
				HibernateUtil.closeCurrentThreadSession();
			}else{
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
		loadComHibernateHbmContent(database);
		
		// 查询databaseId指定的库下有多少hbm数据，分页查询并加载到sessionFactory中
		int count = (int) HibernateUtil.executeUniqueQueryBySql("select count(1) from com_hibernate_hbm where is_enabled = 1 and hbm_resource_name != 'ComHibernateHbm' and ref_database_id = '"+database.getId()+"'", null);
		if(count == 0){
			return;
		}
		int loopCount = count/100 + 1;
		List<Object> hbmContents = null;
		List<String> hcs = null;
		for(int i=0;i<loopCount;i++){
			hbmContents = HibernateUtil.executeListQueryByHql("100", i+"", "select hbmContent from ComHibernateHbm where isEnabled = 1 and hbmResourceName !='ComHibernateHbm' and refDatabaseId = '"+database.getId()+"'", null);
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
	
	/**
	 * 加载指定数据库的核心hbm映射文件
	 * @param database 指定数据库的id
	 * @param coreTableResourceNames
	 * @param placholders
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void loadCoreHbmContentsByDatabaseId(ComDatabase database, List<Object> coreTableResourceNames, String placholders) throws SQLException, IOException {
		loadComHibernateHbmContent(database);
		
		List<Object> coreHbmContents = HibernateUtil.executeListQueryByHql(null, null, 
				"select hbmContent from ComHibernateHbm where isEnabled = 1 and refDatabaseId = '"+database.getId()+"' and hbmResourceName in " + placholders, coreTableResourceNames);
		List<String> hcs = new ArrayList<String>(coreHbmContents.size());
		for (Object obj : coreHbmContents) {
			hcs.add(obj+"");
		}
		HibernateUtil.appendNewConfig(hcs);
		
		hcs.clear();
		coreHbmContents.clear();
		
		// 关闭session
		HibernateUtil.closeCurrentThreadSession();
	}
	
	/**
	 * 加载指定数据库中的ComHibernateHbm内容
	 * @param database
	 * @throws SQLException
	 * @throws IOException
	 */
	private void loadComHibernateHbmContent(ComDatabase database) throws SQLException, IOException{
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
		hbmContent.setLength(0);
	}
}
