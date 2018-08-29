package com.king.tooth.sys.service.other;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.annotation.Service;
import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRuleDetail;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComProjectModule;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectHbmLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectSqlLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectTableLinks;
import com.king.tooth.sys.entity.dm.DmPublishBasicData;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysUserPermissionCache;
import com.king.tooth.sys.entity.sys.SysDataDictionary;
import com.king.tooth.sys.entity.sys.SysDept;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.sys.SysHibernateHbm;
import com.king.tooth.sys.entity.sys.SysOperSqlLog;
import com.king.tooth.sys.entity.sys.SysOrg;
import com.king.tooth.sys.entity.sys.SysPermission;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.entity.sys.SysPosition;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.sys.SysRole;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.SysUserGroup;
import com.king.tooth.sys.entity.sys.SysUserGroupDetail;
import com.king.tooth.sys.entity.sys.datalinks.SysUserRoleLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysDataLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserDeptLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserPositionLinks;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateHbmUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 初始化系统的Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Service
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
			Log4jUtil.info("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.error("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg("InitCfgSystemService", "loadSysBasicDatasBySysFirstStart", e));
			System.exit(0);
		}
	}
	
	/**
	 * 处理本系统和本数据库的关系
	 */
	private void processCurrentSysOfPorjDatabaseRelation() {
		// 添加本系统和本数据库的映射关系
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(
				BuiltinObjectInstance.currentSysBuiltinProjectInstance.getId(), 
				BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId());
	}
	
	/**
	 * 获取系统涉及到的所有表
	 * @return
	 */
	private List<ComTabledata> getAllTables(){
		List<ComTabledata> tables = new ArrayList<ComTabledata>(50);
		tables.add(new CfgDatabase().toCreateTable());
		tables.add(new ComProject().toCreateTable());
		tables.add(new ComProjectModule().toCreateTable());
		tables.add(new SysHibernateHbm().toCreateTable());
		tables.add(new ComSqlScript().toCreateTable());
		tables.add(new CfgProjectSqlLinks().toCreateTable());
		tables.add(new CfgProjectHbmLinks().toCreateTable());
		tables.add(new SysAccount().toCreateTable());
		tables.add(new SysDataDictionary().toCreateTable());
		tables.add(new SysResource().toCreateTable());
		tables.add(new SysDataLinks().toCreateTable());
		tables.add(new SysReqLog().toCreateTable());
		tables.add(new SysOperSqlLog().toCreateTable());
		tables.add(new SysAccountOnlineStatus().toCreateTable());
		tables.add(new SysUser().toCreateTable());
		tables.add(new ComColumndata().toCreateTable());
		tables.add(new ComTabledata().toCreateTable());
		tables.add(new DmPublishInfo().toCreateTable());
		tables.add(new DmPublishBasicData().toCreateTable());
		tables.add(new ComSqlScriptParameter().toCreateTable());
		tables.add(new CfgProjectTableLinks().toCreateTable());
		tables.add(new SysRole().toCreateTable());
		tables.add(new SysPermission().toCreateTable());
		tables.add(new SysPermissionPriority().toCreateTable());
		tables.add(new SysFile().toCreateTable());
		tables.add(new SysOrg().toCreateTable());
		tables.add(new SysDept().toCreateTable());
		tables.add(new SysPosition().toCreateTable());
		tables.add(new SysUserRoleLinks().toCreateTable());
		tables.add(new SysUserDeptLinks().toCreateTable());
		tables.add(new SysUserPositionLinks().toCreateTable());
		tables.add(new SysUserPermissionCache().toCreateTable());
		tables.add(new SysUserGroup().toCreateTable());
		tables.add(new SysUserGroupDetail().toCreateTable());
		tables.add(new CfgColumnCodeRule().toCreateTable());
		tables.add(new CfgColumnCodeRuleDetail().toCreateTable());
		return tables;
	}
	
	/**
	 * 初始化数据库信息
	 */
	private void initDatabaseInfo() {
		try {
			// 设置当前操作的项目，获得对应的sessionFactory
			CurrentThreadContext.setProjectId(BuiltinObjectInstance.currentSysBuiltinProjectInstance.getId());
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
		DBTableHandler dbHandler = new DBTableHandler(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance);
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
		List<String> hbmContents = new ArrayList<String>(tables.size());
		for (ComTabledata table : tables) {
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			hbmContents.add(HibernateHbmUtil.createHbmMappingContent(table, true));// 记录hbm内容
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
		SysAccount admin = new SysAccount();
		admin.setId("16ed21bd7a7a41f5bea2ebaa258908cf");/* 在同步数据的时候，为了和服务器数据库中的id一致，这里写成固定值，即服务器数据库中，账户admin的id */
		admin.setType(1);
		admin.setLoginName("admin");
		admin.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		admin.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(BuiltinObjectInstance.validDate);
		String adminAccountId = HibernateUtil.saveObject(admin, null).getString(ResourcePropNameConstants.ID);
	
		// 添加普通账户【2.普通账户】
		SysAccount normal = new SysAccount();
		normal.setId("59c2c378b845447d8f675ef29b55cb63");
		normal.setType(2);
		normal.setLoginName("normal");
		normal.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		normal.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), normal.getLoginPwdKey()));
		normal.setValidDate(BuiltinObjectInstance.validDate);
		String normalAccountId = HibernateUtil.saveObject(normal, adminAccountId).getString(ResourcePropNameConstants.ID);
		
		// 添加平台开发账户【3.平台开发账户】
		SysAccount developer = new SysAccount();
		developer.setId("93d02915eb764d978e3cae6987b5fc7a");
		developer.setType(3);
		developer.setLoginName("developer");
		developer.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		developer.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), developer.getLoginPwdKey()));
		developer.setValidDate(BuiltinObjectInstance.validDate);
		HibernateUtil.saveObject(developer, adminAccountId).getString(ResourcePropNameConstants.ID);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据库信息【运行平台数据库信息】
		CfgDatabase appDatabase = new CfgDatabase();
		appDatabase.setId("05fb6ef9c3ackfccb91b00add666odb9");
		appDatabase.setDisplayName("运行系统通用数据库(内置)");
		appDatabase.setType(SysConfig.getSystemConfig("jdbc.dbType"));
		appDatabase.setInstanceName("SmartOneApp");
		appDatabase.setLoginUserName("SmartOneApp");
		appDatabase.setLoginPassword(SysConfig.getSystemConfig("db.default.password"));
		appDatabase.setIp(SysConfig.getSystemConfig("db.default.ip"));
		appDatabase.setPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
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
		HibernateUtil.saveObject(project, normalAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 根据表创建hbm文件，并将其加入到CfgHibernateHbm表中
		insertHbm(adminAccountId);
		// 添加数据字典基础数据
		insertBasicDataDictionary(adminAccountId);
		// 添加要发布的基础数据
		insertPublishBasicData(adminAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 清空用户在线数据表
		HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, "delete SysAccountOnlineStatus", null);
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到CfgHibernateHbm表中
	 * @param adminAccountId 
	 */
	private void insertHbm(String adminAccountId) {
		List<ComTabledata> tables = getAllTables();
		SysHibernateHbm hbm;
		SysResource resource;
		for (ComTabledata table : tables) {
			if(table.getBelongPlatformType() == ISysResource.APP_PLATFORM){
				continue;
			}
			// 创建对应的hbm文件，并保存
			hbm = new SysHibernateHbm();
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.tableTurnToHbm(table);
			hbm.setHbmContent(HibernateHbmUtil.createHbmMappingContent(table, true));
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
		// CfgDatabase.dbType 数据库类型
		insertDataDictionary(adminAccountId, "comcolumndata.dbtype", "oracle", "oracle", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comcolumndata.dbtype", "sqlserver", "sqlserver", 2, ISysResource.CONFIG_PLATFORM);
		
		// ComTabledata.tableType 表类型
		insertDataDictionary(adminAccountId, "comcolumndata.tabletype", "单表", "1", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comcolumndata.tabletype", "树表", "2", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comcolumndata.tabletype", "主子表", "3", 3, ISysResource.CONFIG_PLATFORM);
		
		// ComTabledata.dbType 数据库类型
		insertDataDictionary(adminAccountId, "comcolumndata.dbtype", "oracle", "oracle", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comcolumndata.dbtype", "sqlserver", "sqlserver", 2, ISysResource.CONFIG_PLATFORM);
		
		// SysReqLog.operType 操作的类型
		insertDataDictionary(adminAccountId, "comoperlog.opertype", "查询", "select", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comoperlog.opertype", "增加", "insert", 2, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comoperlog.opertype", "修改", "update", 3, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comoperlog.opertype", "删除", "delete", 4, ISysResource.COMMON_PLATFORM);
		
		// SysAccount.accountType 账户类型
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "管理员", "1", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "普通账户", "2", 2, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysaccount.accounttype", "平台开发账户", "3", 3, ISysResource.COMMON_PLATFORM);
		
		// SysAccount.accountStatus 账户状态
		insertDataDictionary(adminAccountId, "comsysaccount.accountstatus", "启用", "1", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysaccount.accountstatus", "禁用", "2", 2, ISysResource.COMMON_PLATFORM);
		
		// SysResource.resourceType 资源类型
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "表资源", "1", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "sql脚本资源", "2", 2, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "代码资源", "3", 3, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "数据库资源", "4", 4, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "项目资源", "5", 5, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "项目模块资源", "6", 6, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "comsysresource.resourcetype", "基础数据资源", "7", 7, ISysResource.COMMON_PLATFORM);
		
		// SqlScriptParameter.parameterFrom sql脚本参数的来源
		insertDataDictionary(adminAccountId, "comSqlScriptParameter.parameterFrom", "用户输入", "0", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comSqlScriptParameter.parameterFrom", "系统内置", "1", 2, ISysResource.CONFIG_PLATFORM);
		
		// SqlScriptParameter.inOut sql脚本参数in/out类型
		insertDataDictionary(adminAccountId, "comSqlScriptParameter.inOut", "输入参数(in)", "1", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comSqlScriptParameter.inOut", "输出参数(out)", "2", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "comSqlScriptParameter.inOut", "输入输出参数(in_out)", "3", 3, ISysResource.CONFIG_PLATFORM);
		
		// SysReqLog.type 请求日志的请求类型
		insertDataDictionary(adminAccountId, "SysReqLog.type", "login", "1", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "SysReqLog.type", "loginOut", "2", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "SysReqLog.type", "sql", "3", 3, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "SysReqLog.type", "file", "4", 4, ISysResource.CONFIG_PLATFORM);
		
		// System.builtinQueryParameter 系统内置查询参数
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前系统时间", "_currentDate", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前租户id", "_currentCustomerId", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前项目id", "_currentProjectId", 3, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前账户id", "_accountId", 4, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前账户名", "_accountName", 5, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前用户id", "_currentUserId", 6, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前用户所属组织id", "_currentOrgId", 7, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前用户所属部门id", "_currentDeptId", 8, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "System.builtinQueryParameter", "当前用户所属岗位id", "_currentPositionId", 9, ISysResource.CONFIG_PLATFORM);
		
		// system.dataType 系统内置数据类型
		insertDataDictionary(adminAccountId, "system.dataType", "字符串", "string", 1, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "system.dataType", "布尔值", "boolean", 2, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "system.dataType", "整型", "integer", 3, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "system.dataType", "浮点型", "double", 4, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "system.dataType", "日期", "date", 5, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "system.dataType", "字符大字段", "clob", 6, ISysResource.CONFIG_PLATFORM);
		insertDataDictionary(adminAccountId, "system.dataType", "二进制大字段", "blob", 7, ISysResource.CONFIG_PLATFORM);
		
		// system.dataType 系统内置权限类型
		insertDataDictionary(adminAccountId, "system.permissionType", "角色", "role", 1, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "system.permissionType", "帐号", "account", 2, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "system.permissionType", "部门", "dept", 3, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "system.permissionType", "岗位", "position", 4, ISysResource.COMMON_PLATFORM);
		insertDataDictionary(adminAccountId, "system.permissionType", "帐号组", "accountGroup", 5, ISysResource.COMMON_PLATFORM);
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
		SysDataDictionary dataDictionary = new SysDataDictionary();
		dataDictionary.setCode(code.toLowerCase());
		dataDictionary.setCaption(codeCaption);
		dataDictionary.setVal(codeValue);
		dataDictionary.setOrderCode(orderCode);
		HibernateUtil.saveObject(dataDictionary, adminAccountId);
		
		// 同时，不是配置系统的数据，则添加要发布的基础的数据字典数据
		if(belongPlatformType != ISysResource.CONFIG_PLATFORM){
			dataDictionary.setId(ResourceHandlerUtil.getIdentity());
			HibernateUtil.saveObject(dataDictionary.turnToPublishBasicData(belongPlatformType), adminAccountId);
		}
	}
	
	/**
	 * 添加要发布的基础数据
	 * @param adminAccountId
	 */
	private void insertPublishBasicData(String adminAccountId) {
		// 添加一条要发布的管理员账户信息
		SysAccount admin = new SysAccount();
		admin.setId(ResourceHandlerUtil.getIdentity());
		admin.setType(1);
		admin.setLoginName("admin");
		admin.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(BuiltinObjectInstance.validDate);
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
			List<CfgDatabase> databases = HibernateUtil.extendExecuteListQueryByHqlArr(CfgDatabase.class, null, null, "from CfgDatabase where isEnabled = 1 and belongPlatformType = 2 and isCreated =1");
			if(databases != null && databases.size()> 0){
				// 查询获取核心表的hbm资源
				List<String> coreTableHbmContents = HibernateUtil.executeListQueryByHql(null, null, "select h.hbmContent from SysHibernateHbm h, ComTabledata t where h.refTableId = t."+ResourcePropNameConstants.ID+" and t.isCore=1 and t.isEnabled=1 and h.isEnabled=1", null);
				HibernateUtil.closeCurrentThreadSession();
				if(coreTableHbmContents == null || coreTableHbmContents.size() == 0){
					throw new NullPointerException("没有查询到核心表的hbm资源，请检查配置系统数据库中的数据是否正确");
				}
				addOtherCoreTableHbmContents(coreTableHbmContents);
				
				String projDatabaseRelationQueryHql = "select "+ResourcePropNameConstants.ID+" from ComProject where isEnabled=1 and isCreated=1 and refDatabaseId= ?";
				String testLinkResult;
				for (CfgDatabase database : databases) {
					database.analysisResourceProp();
					testLinkResult = database.testDbLink();
					if(testLinkResult.startsWith("err")){
						Log4jUtil.error(testLinkResult);
						continue;
					}
					Log4jUtil.info("测试连接数据库[dbType="+database.getType()+" ， dbInstanceName="+database.getInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getIp()+" ， dbPort="+database.getPort()+"]：" + testLinkResult);
					Log4jUtil.info("给该数据库的连接数据源，添加核心的hbm内容");
					DynamicDBUtil.addDataSource(database);// 创建对应的动态数据源和sessionFactory
					loadCoreHbmContentsToDatabase(database, coreTableHbmContents);// 加载当前数据库中的hbm到sessionFactory中
					loadProjIdWithDatabaseIdRelation(projDatabaseRelationQueryHql, database);
				}
				coreTableHbmContents.clear();
			}else{
				HibernateUtil.closeCurrentThreadSession();
			}
		} catch (Exception e) {
			Log4jUtil.error("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg("InitCfgSystemService", "loadHbmsByStart", e));
			System.exit(0);
		}
	}
	
	/**
	 * 加载项目id和数据库id的关联关系
	 * @param projDatabaseRelationQueryHql
	 * @param database
	 * @return
	 */
	private void loadProjIdWithDatabaseIdRelation(String projDatabaseRelationQueryHql, CfgDatabase database) {
		// 加载数据库和项目的关联关系映射
		CurrentThreadContext.setDatabaseId(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId());// 设置当前操作的项目，获得对应的sessionFactory，即配置系统
		boolean isExists = loadProjIdWithDatabaseIdRelation(projDatabaseRelationQueryHql, database.getId());
		HibernateUtil.closeCurrentThreadSession();
		Log4jUtil.info("数据库[dbType="+database.getType()+" ， dbInstanceName="+database.getInstanceName()+" ， loginUserName="+database.getLoginUserName()+" ， loginPassword="+database.getLoginPassword()+" ， dbIp="+database.getIp()+" ， dbPort="+database.getPort()+"]的数据库，是否存在发布的项目："+ isExists );
	}
	/**
	 * 加载项目id和数据库id的关联关系
	 * @param projDatabaseRelationQueryHql
	 * @param databaseId
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
	 * 加载当前系统数据库的所有hbm映射文件
	 * @param database 指定数据库的id
	 * @throws SQLException 
	 * @throws IOException 
	 */
	private void loadCurrentSysDatabaseHbms() throws SQLException, IOException {
		CfgDatabase database = BuiltinObjectInstance.currentSysBuiltinDatabaseInstance;
		
		CurrentThreadContext.setDatabaseId(database.getId());
		// 获取当前系统的SysHibernateHbm映射文件对象
		String sql = "select hbm_content from sys_hibernate_hbm where ref_database_id = '"+database.getId()+"' and hbm_resource_name = 'SysHibernateHbm' and is_enabled = 1";
		String hbmContent = null;
		if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(SysConfig.getSystemConfig("jdbc.dbType"))){
			hbmContent = ((String) HibernateUtil.executeUniqueQueryBySql(sql, null)).trim();
		}else if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(SysConfig.getSystemConfig("jdbc.dbType"))){
			Clob clob = (Clob) HibernateUtil.executeUniqueQueryBySql(sql, null);
			if(clob == null){
				throw new NullPointerException("数据库名为["+database.getDisplayName()+"]，实例名为["+database.getInstanceName()+"]，ip为["+database.getIp()+"]，端口为["+database.getPort()+"]，用户名为["+database.getLoginUserName()+"]，密码为["+database.getLoginPassword()+"]，的数据库中，没有查询到SysHibernateHbm的hbm文件内容，请检查：[" + sql + "]");
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
		int count = ((Long) HibernateUtil.executeUniqueQueryByHql("select count("+ResourcePropNameConstants.ID+") from SysHibernateHbm where isEnabled = 1 and hbmResourceName != 'SysHibernateHbm' and refDatabaseId = '"+database.getId()+"'", null)).intValue();
		if(count == 0){
			return;
		}
		int loopCount = count/100 + 1;
		List<Object> hbmContents = null;
		List<String> hcs = null;
		for(int i=0;i<loopCount;i++){
			hbmContents = HibernateUtil.executeListQueryByHql("100", (i+1)+"", "select hbmContent from SysHibernateHbm where isEnabled = 1 and hbmResourceName !='SysHibernateHbm' and refDatabaseId = '"+database.getId()+"'", null);
			hcs = new ArrayList<String>(hbmContents.size());
			for (Object obj : hbmContents) {
				hcs.add(obj+"");
			}
			HibernateUtil.appendNewConfig(hcs);
			hbmContents.clear();
			hcs.clear();
		}
		
		// 初始化日志表
		initLogTables();
		
		// 关闭session
		HibernateUtil.closeCurrentThreadSession();
	}
	
	/**
	 * 初始化日志表
	 * <p>如果不存在日志表，则要创建</p>
	 * <p>这个要判断4个东西，reqLog表、reqLog的hbm、operSqlLog表、operSqlLog的hbm</p>
	 */
	private void initLogTables() {
		// 判断是否存在日志表table
		DBTableHandler tableHandler = new DBTableHandler(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance);
		List<String> logTableNames = tableHandler.filterTable(false, BuiltinObjectInstance.sysReqLog.toDropTable(), BuiltinObjectInstance.sysOperSqlLog.toDropTable());
		if(logTableNames != null && logTableNames.size() > 0){
			// 不存在，则create
			for (String logTableName : logTableNames) {
				if(logTableName.equals(BuiltinObjectInstance.sysReqLog.toDropTable())){
					tableHandler.createTable(BuiltinObjectInstance.sysReqLog.toCreateTable(), true);
				}else if(logTableName.equals(BuiltinObjectInstance.sysOperSqlLog.toDropTable())){
					tableHandler.createTable(BuiltinObjectInstance.sysOperSqlLog.toCreateTable(), true);
				}
			}
		}
		
		// 判断是否存在日志表的hbm
		if(!HibernateUtil.hbmConfigIsExists(BuiltinObjectInstance.sysReqLog.getEntityName())){
			createHbm(BuiltinObjectInstance.sysReqLog.toCreateTable());
		}
		if(!HibernateUtil.hbmConfigIsExists(BuiltinObjectInstance.sysOperSqlLog.getEntityName())){
			createHbm(BuiltinObjectInstance.sysOperSqlLog.toCreateTable());
		}
	}
	
	/**
	 * 创建hbm对象
	 * @param table
	 */
	private void createHbm(ComTabledata table){
		// 插入hbm
		SysHibernateHbm hbm = new SysHibernateHbm(); 
		hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
		hbm.tableTurnToHbm(table);
		hbm.setHbmContent(HibernateHbmUtil.createHbmMappingContent(table, true));
		HibernateUtil.saveObject(hbm, null);
		
		// 插入资源数据
		BuiltinObjectInstance.resourceService.saveSysResource(table);
		
		// 将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbm.getHbmContent());
		
		// 清空缓存
		table.clear();
	}
	
	/**
	 * 添加其他核心表的hbm资源
	 * <p>主要针对只在运行系统中会存在的资源，例如ComProjectSysHibernateHbmLinks关系表等</p>
	 * @param coreTableHbmContents
	 */
	private void addOtherCoreTableHbmContents(List<String> coreTableHbmContents) {
		ComTabledata comProjectSysHibernateHbmLinksTable = new CfgProjectHbmLinks().toCreateTable();
		coreTableHbmContents.add(HibernateHbmUtil.createHbmMappingContent(comProjectSysHibernateHbmLinksTable, true));
	}
	
	/**
	 * 给指定数据库加载核心hbm映射文件
	 * @param database 指定数据库
	 * @param coreTableHbmContents
	 */
	private void loadCoreHbmContentsToDatabase(CfgDatabase database, List<String> coreTableHbmContents){
		CurrentThreadContext.setDatabaseId(database.getId());
		HibernateUtil.appendNewConfig(coreTableHbmContents);
	}
}
