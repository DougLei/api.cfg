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
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.app.ComAttachment;
import com.king.tooth.sys.entity.app.ComDept;
import com.king.tooth.sys.entity.app.ComOrg;
import com.king.tooth.sys.entity.app.ComPermission;
import com.king.tooth.sys.entity.app.ComPosition;
import com.king.tooth.sys.entity.app.ComRole;
import com.king.tooth.sys.entity.app.datalinks.ComProjectComHibernateHbmLinks;
import com.king.tooth.sys.entity.app.datalinks.ComSysAccountComRoleLinks;
import com.king.tooth.sys.entity.app.datalinks.ComUserComDeptLinks;
import com.king.tooth.sys.entity.app.datalinks.ComUserComPositionLinks;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishBasicData;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.ComProjectComTabledataLinks;
import com.king.tooth.sys.entity.common.ComCode;
import com.king.tooth.sys.entity.common.ComDataDictionary;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbm;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.entity.common.ComOperLog;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.common.ComUser;
import com.king.tooth.sys.entity.common.ComVerifyCode;
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
public class InitCfgSystemService extends AbstractService{

	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void loadSysBasicDatasBySysFirstStart() {
		Log4jUtil.info("loadSysBasicDatasBySysFirstStart..........");
		try {
			processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
			initDatabaseInfo();// 初始化数据库信息
			updateInitConfig();
			Log4jUtil.debug("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.debug("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		}
	}
	
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
	 * 获取系统涉及到的所有表
	 * @return
	 */
	private List<ComTabledata> getAllTables(){
		List<ComTabledata> tables = new ArrayList<ComTabledata>(33);
		// 核心表
		tables.add(new ComDatabase().toCreateTable());
		tables.add(new ComProject().toCreateTable());
		tables.add(new ComProjectModule().toCreateTable());
		tables.add(new ComModuleOperation().toCreateTable());
		tables.add(new ComHibernateHbm().toCreateTable());
		tables.add(new ComSqlScript().toCreateTable());
		tables.add(new ComCode().toCreateTable());
		tables.add(new ComProjectComSqlScriptLinks().toCreateTable());
		tables.add(new ComProjectComHibernateHbmLinks().toCreateTable());
		tables.add(new ComProjectComCodeLinks().toCreateTable());
		tables.add(new ComSysAccount().toCreateTable());
		tables.add(new ComDataDictionary().toCreateTable());
		// 通用表
		tables.add(new ComSysResource().toCreateTable());
		tables.add(new ComDataLinks().toCreateTable());
		tables.add(new ComOperLog().toCreateTable());
		tables.add(new ComReqLog().toCreateTable());
		tables.add(new ComSysAccountOnlineStatus().toCreateTable());
		tables.add(new ComUser().toCreateTable());
		tables.add(new ComVerifyCode().toCreateTable());
		// 配置系统表
		tables.add(new ComColumndata().toCreateTable());
		tables.add(new ComTabledata().toCreateTable());
		tables.add(new ComPublishInfo().toCreateTable());
		tables.add(new ComPublishBasicData().toCreateTable());
		tables.add(new ComProjectComTabledataLinks().toCreateTable());
		// 运行系统表
		tables.add(new ComRole().toCreateTable());
		tables.add(new ComPermission().toCreateTable());
		tables.add(new ComAttachment().toCreateTable());
		tables.add(new ComOrg().toCreateTable());
		tables.add(new ComDept().toCreateTable());
		tables.add(new ComPosition().toCreateTable());
		tables.add(new ComSysAccountComRoleLinks().toCreateTable());
		tables.add(new ComUserComDeptLinks().toCreateTable());
		tables.add(new ComUserComPositionLinks().toCreateTable());
		return tables;
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
		List<ComTabledata> tables = getAllTables();
		List<ComTabledata> tmpTables = new ArrayList<ComTabledata>();
		DBTableHandler dbHandler = new DBTableHandler(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance);
		for (ComTabledata table : tables) {
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			tmpTables.add(table);
		}
		dbHandler.dropTable(tmpTables);// 尝试先删除表
		dbHandler.createTable(tmpTables, true);// 开始创建表
		ResourceHandlerUtil.clearTables(tmpTables);
		ResourceHandlerUtil.clearTables(tables);
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到SessionFactory中
	 */
	private void insertHbmContentsToSessionFactory() {
		List<ComTabledata> tables = getAllTables();
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		List<String> hbmContents = new ArrayList<String>(tables.size());
		for (ComTabledata table : tables) {
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			hbmContents.add(hibernateHbmHandler.createHbmMappingContent(table, true));// 记录hbm内容
		}
		// 将hbmContents加入到hibernate sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
		ResourceHandlerUtil.clearTables(tables);
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
		appDatabase.setId("05fb6ef9c3ackfccb91b00add666odb9");
		appDatabase.setDbDisplayName("运行系统通用数据库(内置)");
		appDatabase.setDbType(SysConfig.getSystemConfig("jdbc.dbType"));
		appDatabase.setDbInstanceName("SmartOneApp");
		appDatabase.setLoginUserName("SmartOneApp");
		appDatabase.setLoginPassword("1");
		appDatabase.setDbIp(SysConfig.getSystemConfig("db.default.ip"));
		appDatabase.setDbPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
		appDatabase.analysisResourceProp();
		appDatabase.setIsBuiltin(1);
		appDatabase.setIsNeedDeploy(1);
		HibernateUtil.saveObject(appDatabase, null);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加项目信息【运行平台数据库中的一个项目】
		ComProject project = new ComProject();
		project.setId("7fe971700f21d3a796d2017398812dcd");
		project.setRefDatabaseId("05fb6ef9c3ackfccb91b00add666odb9");
		project.setProjName("自动化配置项目(内置)");
		project.setProjCode("AutoConfigProj");
		project.analysisResourceProp();
		project.setIsBuiltin(1);
		project.setIsNeedDeploy(1);
		HibernateUtil.saveObject(project, null);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 将表信息插入的cfgTabledata表中，同时把列的信息插入到cfgColumndata表中
		insertAllTables(adminAccountId);
		// 添加数据字典基础数据
		insertBasicDataDictionary(adminAccountId);
		// 添加代码资源
		insertCodeResources(adminAccountId);
		// 添加要发布的基础数据
		insertPublishBasicData(adminAccountId);
	}
	
	/**
	 * 将表信息插入的cfgTabledata表中
	 * <p>同时把列的信息插入到cfgColumndata表中</p>
	 * <p>再根据表创建hbm文件，并将其加入到CfgHibernateHbm表中</p>
	 * @param adminAccountId 
	 */
	private void insertAllTables(String adminAccountId) {
		List<ComTabledata> tables = getAllTables();
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
			hbm.setHbmContent(hibernateHbmHandler.createHbmMappingContent(table, true));
			HibernateUtil.saveObject(hbm, adminAccountId);
			
			// 保存到资源表中
			resource = table.turnToResource();
			HibernateUtil.saveObject(resource, adminAccountId);
		}
		ResourceHandlerUtil.clearTables(tables);
	}
	
	/**
	 * 添加数据字典的基础数据
	 * @param adminAccountId 
	 */
	private void insertBasicDataDictionary(String adminAccountId) {
		// ComColumndata.columnType 字段数据类型
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "字符串", "string", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "布尔值", "boolean", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "整型", "integer", 3, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "浮点型", "double", 4, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "日期", "date", 5, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "字符大字段", "clob", 6, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgcolumndata.columntype", "二进制大字段", "blob", 7, ISysResource.CONFIG_PLATFORM);
		
		// ComDatabase.dbType 数据库类型
		insertDataDictionary(adminAccountId, "cfgdatabase.dbtype", "oracle", "oracle", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgdatabase.dbtype", "sqlserver", "sqlserver", 2, ISysResource.CONFIG_PLATFORM);
		
		// ComTabledata.tableType 表类型
		insertDataDictionary(adminAccountId, "cfgtabledata.tabletype", "单表", "1", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgtabledata.tabletype", "树表", "2", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgtabledata.tabletype", "主子表", "3", 3, ISysResource.CONFIG_PLATFORM);
		
		// ComTabledata.dbType 数据库类型
		insertDataDictionary(adminAccountId, "cfgtabledata.dbtype", "oracle", "oracle", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "cfgtabledata.dbtype", "sqlserver", "sqlserver", 2, ISysResource.CONFIG_PLATFORM);
		
		// ComOperLog.operType 操作的类型
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "查询", "select", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "增加", "insert", 2, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "修改", "update", 3, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comoperLog.opertype", "删除", "delete", 4, ISysResource.COMMON_PLATFORM);
		
		// ComSysAccount.accountType 账户类型
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "管理账户", "0", 0, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "普通账户", "1", 1, ISysResource.COMMON_PLATFORM);
		
		// ComSysAccount.accountStatus 账户状态
		insertDataDictionary(adminAccountId, "comsysaccount.accountstatus", "启用", "1", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysaccount.accountstatus", "禁用", "2", 2, ISysResource.COMMON_PLATFORM);
		
		// ComSysResource.resourceType 资源类型
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "表资源", "1", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "sql脚本资源", "2", 2, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "代码资源", "3", 3, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "数据库资源", "4", 4, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "项目资源", "5", 5, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "项目模块资源", "6", 6, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "基础数据资源", "7", 7, ISysResource.COMMON_PLATFORM);
	}
	/**
	 * 添加数据字典
	 * @param code
	 * @param codeCaption
	 * @param codeValue
	 * @param orderCode
	 * @param belongPlatformType
	 * @return
	 */
	private void insertDataDictionary(String adminAccountId, String code, String codeCaption, String codeValue, int orderCode, int belongPlatformType){
		ComDataDictionary dataDictionary = new ComDataDictionary();
		dataDictionary.setCode(code);
		dataDictionary.setCodeCaption(codeCaption);
		dataDictionary.setCodeValue(codeValue);
		dataDictionary.setOrderCode(orderCode);
		HibernateUtil.saveObject(dataDictionary, adminAccountId);
		
		// 同时，不是配置系统的数据，则添加要发布的基础的数据字典数据
		if(belongPlatformType != ISysResource.CONFIG_PLATFORM){
			dataDictionary.setId(ResourceHandlerUtil.getIdentity());
			HibernateUtil.saveObject(dataDictionary.turnToPublishBasicData(belongPlatformType), adminAccountId);
		}
	}
	
	/**
	 * 添加代码资源
	 * @param adminAccountId
	 */
	private void insertCodeResources(String adminAccountId) {
		insertCodeResource(adminAccountId, "ResourceDescTable", "com.king.tooth.sys.controller.cfg.ResourceDescController", "table", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ResourceDescTableJson", "com.king.tooth.sys.controller.cfg.ResourceDescController", "tableJson", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "ColumnAdd", "com.king.tooth.sys.controller.cfg.ComColumndataController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ColumnUpdate", "com.king.tooth.sys.controller.cfg.ComColumndataController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ColumnDelete", "com.king.tooth.sys.controller.cfg.ComColumndataController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "TableAdd", "com.king.tooth.sys.controller.cfg.ComTabledataController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableUpdate", "com.king.tooth.sys.controller.cfg.ComTabledataController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableDelete", "com.king.tooth.sys.controller.cfg.ComTabledataController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableBuildModel", "com.king.tooth.sys.controller.cfg.ComTabledataController", "buildModel", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableCancelBuildModel", "com.king.tooth.sys.controller.cfg.ComTabledataController", "cancelBuildModel", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableAddProjTableRelation", "com.king.tooth.sys.controller.cfg.ComTabledataController", "addProjTableRelation", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableCancelProjTableRelation", "com.king.tooth.sys.controller.cfg.ComTabledataController", "cancelProjTableRelation", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TablePublish", "com.king.tooth.sys.controller.cfg.ComTabledataController", "publish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "TableCancelPublish", "com.king.tooth.sys.controller.cfg.ComTabledataController", "cancelPublish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "DatabaseAdd", "com.king.tooth.sys.controller.common.ComDatabaseController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "DatabaseUpdate", "com.king.tooth.sys.controller.common.ComDatabaseController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "DatabaseDelete", "com.king.tooth.sys.controller.common.ComDatabaseController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "DatabaseLinkTest", "com.king.tooth.sys.controller.common.ComDatabaseController", "linkTest", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "DatabasePublish", "com.king.tooth.sys.controller.common.ComDatabaseController", "publish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "DatabaseCancelPublish", "com.king.tooth.sys.controller.common.ComDatabaseController", "cancelPublish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "ModuleOperationAdd", "com.king.tooth.sys.controller.common.ComModuleOperationController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ModuleOperationUpdate", "com.king.tooth.sys.controller.common.ComModuleOperationController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ModuleOperationDelete", "com.king.tooth.sys.controller.common.ComModuleOperationController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ModuleOperationPublish", "com.king.tooth.sys.controller.common.ComModuleOperationController", "publish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ModuleOperationCancelPublish", "com.king.tooth.sys.controller.common.ComModuleOperationController", "cancelPublish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "ProjectAdd", "com.king.tooth.sys.controller.common.ComProjectController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectUpdate", "com.king.tooth.sys.controller.common.ComProjectController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectDelete", "com.king.tooth.sys.controller.common.ComProjectController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectCancelRelation", "com.king.tooth.sys.controller.common.ComProjectController", "cancelRelation", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectPublish", "com.king.tooth.sys.controller.common.ComProjectController", "publish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectCancelPublish", "com.king.tooth.sys.controller.common.ComProjectController", "cancelPublish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "ProjectModuleAdd", "com.king.tooth.sys.controller.common.ComProjectModuleController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectModuleUpdate", "com.king.tooth.sys.controller.common.ComProjectModuleController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectModuleDelete", "com.king.tooth.sys.controller.common.ComProjectModuleController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectModulePublish", "com.king.tooth.sys.controller.common.ComProjectModuleController", "publish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "ProjectModuleCancelPublish", "com.king.tooth.sys.controller.common.ComProjectModuleController", "cancelPublish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "SqlScriptAdd", "com.king.tooth.sys.controller.common.ComSqlScriptController", "add", ISysResource.POST, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "SqlScriptUpdate", "com.king.tooth.sys.controller.common.ComSqlScriptController", "update", ISysResource.PUT, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "SqlScriptDelete", "com.king.tooth.sys.controller.common.ComSqlScriptController", "delete", ISysResource.DELETE, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "SqlScriptAddProjSqlScriptRelation", "com.king.tooth.sys.controller.common.ComSqlScriptController", "addProjSqlScriptRelation", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "SqlScriptCancelProjSqlScriptRelation", "com.king.tooth.sys.controller.common.ComSqlScriptController", "cancelProjSqlScriptRelation", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "SqlScriptPublish", "com.king.tooth.sys.controller.common.ComSqlScriptController", "publish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		insertCodeResource(adminAccountId, "SqlScriptCancelPublish", "com.king.tooth.sys.controller.common.ComSqlScriptController", "cancelPublish", ISysResource.GET, 0, ISysResource.CONFIG_PLATFORM);
		
		insertCodeResource(adminAccountId, "login", "com.king.tooth.sys.controller.common.ComSysAccountController", "login", ISysResource.POST, 1, ISysResource.COMMON_PLATFORM);
	}
	private void insertCodeResource(String adminAccountId, String codeResourceName, String classPath, String methodName, String reqResourceMethod, Integer isNeedDeploy, Integer belongPlatformType){
		ComCode code = new ComCode();
		code.setIsCreated(1);
		code.setIsBuiltin(1);
		
		code.setCodeResourceName(codeResourceName);
		code.setClassPath(classPath);
		code.setMethodName(methodName);
		code.setReqResourceMethod(reqResourceMethod);
		code.setIsNeedDeploy(isNeedDeploy);
		code.setBelongPlatformType(belongPlatformType);
		code.setId(HibernateUtil.saveObject(code, adminAccountId));
		
		if(belongPlatformType != ISysResource.APP_PLATFORM){
			HibernateUtil.saveObject(code.turnToResource(), adminAccountId);
		}
		
		if(belongPlatformType != ISysResource.CONFIG_PLATFORM){
			code.setId(ResourceHandlerUtil.getIdentity());
			HibernateUtil.saveObject(code.turnToPublishBasicData(belongPlatformType), adminAccountId);
			
			ComSysResource resource = code.turnToResource();
			resource.setId(ResourceHandlerUtil.getIdentity());
			resource.setRefResourceId(code.getId());
			HibernateUtil.saveObject(resource.turnToPublishBasicData(belongPlatformType), adminAccountId);
		}
	}
	
	/**
	 * 添加要发布的基础数据
	 * @param adminAccountId
	 */
	private void insertPublishBasicData(String adminAccountId) {
		// 添加一条要发布的管理员账户信息
		ComSysAccount admin = new ComSysAccount();
		admin.setId(ResourceHandlerUtil.getIdentity());
		admin.setAccountType(1);
		admin.setLoginName("admin");
		admin.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		HibernateUtil.saveObject(admin.turnToPublishBasicData(ISysResource.APP_PLATFORM), adminAccountId);
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
	public void loadHbmsByStart() {
		Log4jUtil.info("loadHbmsByStart..........");
		processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
		try {
			// 先加载当前系统数据库的所有hbm映射文件
			loadCurrentSysDatabaseHbms();
			
			// 再加载系统中所有数据库信息，创建动态数据源，动态sessionFactory，以及将各个数据库中的核心hbm加载进对应的sessionFactory中
			// 同时建立数据库和项目的关联关系，为之后的发布操作做准备
			List<ComDatabase> databases = HibernateUtil.extendExecuteListQueryByHqlArr(ComDatabase.class, null, null, "from ComDatabase where isEnabled = 1 and belongPlatformType = 2 and isCreated =1");
			
			if(databases != null && databases.size()> 0){
				// 查询获取核心表资源名
				List<Object> coreTableResourceNames = HibernateUtil.executeListQueryByHql(null, null, "select resourceName from ComTabledata where isCore=1 and isEnabled=1 and resourceName != 'ComHibernateHbm'", null);
				
				if(coreTableResourceNames == null || coreTableResourceNames.size() == 0){
					throw new NullPointerException("没有查询到核心的表资源名称，请检查配置系统数据库中的数据是否正确");
				}
				StringBuilder sp = new StringBuilder("(");
				int len = coreTableResourceNames.size();
				for(int i=0;i<len ;i++){
					sp.append("?,");
				}
				sp.setLength(sp.length()-1);
				sp.append(")");
				String placholders = sp.toString();
				sp.setLength(0);
				
				String projDatabaseRelationQueryHql = "select "+ResourceNameConstants.ID+" from ComProject where isEnabled = 1 and isCreated =1 and refDatabaseId = ?";
				for (ComDatabase database : databases) {
					if(existsPublishedProjects(projDatabaseRelationQueryHql, database)){
						database.analysisResourceProp();
						String testLinkResult = database.testDbLink();
						if(testLinkResult.startsWith("err")){
							throw new Exception(testLinkResult);
						}
						Log4jUtil.debug("测试连接数据库[dbType="+database.getDbType()+" ， dbInstanceName="+database.getDbInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getDbIp()+" ， dbPort="+database.getDbPort()+"]：" + testLinkResult);
						
						DynamicDBUtil.addDataSource(database);// 创建对应的动态数据源和sessionFactory
						loadCoreHbmContentsByDatabaseId(database, coreTableResourceNames, placholders);// 加载当前数据库中的hbm到sessionFactory中
					}
				}
				coreTableResourceNames.clear();
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
		CurrentThreadContext.setDatabaseId(CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance.getId());// 设置当前操作的项目，获得对应的sessionFactory，即配置系统
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
	 * 加载当前系统数据库的hbm映射文件
	 * @param database 指定数据库的id
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void loadCurrentSysDatabaseHbms() throws SQLException, IOException {
		ComDatabase database = CurrentSysInstanceConstants.currentSysBuiltinDatabaseInstance;
		loadComHibernateHbmContent(database);
		
		// 查询databaseId指定的库下有多少hbm数据，分页查询并加载到sessionFactory中
		int count = ((Long) HibernateUtil.executeUniqueQueryByHql("select count("+ResourceNameConstants.ID+") from ComHibernateHbm where isEnabled = 1 and hbmResourceName != 'ComHibernateHbm' and refDatabaseId = '"+database.getId()+"'", null)).intValue();
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
			hbmContents.clear();
			hcs.clear();
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
	}
}
