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
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRuleDetail;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.CfgHibernateHbm;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComProjectModule;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectSqlLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectTableLinks;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysDataDictionary;
import com.king.tooth.sys.entity.sys.SysDataPrivS;
import com.king.tooth.sys.entity.sys.SysDept;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.sys.SysOperSqlLog;
import com.king.tooth.sys.entity.sys.SysOrg;
import com.king.tooth.sys.entity.sys.SysPermission;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.entity.sys.SysPosition;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.sys.SysRole;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.SysUserGroup;
import com.king.tooth.sys.entity.sys.SysUserGroupDetail;
import com.king.tooth.sys.entity.sys.SysUserPermissionCache;
import com.king.tooth.sys.entity.sys.datalinks.SysDataLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserDeptLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserPositionLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserRoleLinks;
import com.king.tooth.sys.service.AService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateHbmUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 初始化系统的Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Service
public class InitCfgSystemService extends AService{

	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void firstStart() {
		Log4jUtil.info("loadSysBasicDatasBySysFirstStart..........");
		try {
			processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
			initDatabaseInfo();// 初始化数据库信息
			updateInitConfig();
			Log4jUtil.info("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.error("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
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
		tables.add(new CfgHibernateHbm().toCreateTable());
		tables.add(new ComSqlScript().toCreateTable());
		tables.add(new CfgProjectSqlLinks().toCreateTable());
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
		tables.add(new CfgSqlResultset().toCreateTable());
		tables.add(new SysPushMessageInfo().toCreateTable());
		tables.add(new SysDataPrivS().toCreateTable());
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
		DBTableHandler dbHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		for (ComTabledata table : tables) {
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
		admin.setType(SysAccount.ADMIN);
		admin.setLoginName("admin");
		admin.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		admin.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(BuiltinObjectInstance.validDate);
		String adminAccountId = HibernateUtil.saveObject(admin, null).getString(ResourcePropNameConstants.ID);
	
		// 添加普通账户【2.普通账户】
		SysAccount normal = new SysAccount();
		normal.setId("59c2c378b845447d8f675ef29b55cb63");/* 同上原因 */
		normal.setType(SysAccount.NORMAL);
		normal.setLoginName("normal");
		normal.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		normal.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), normal.getLoginPwdKey()));
		normal.setValidDate(BuiltinObjectInstance.validDate);
		String normalAccountId = HibernateUtil.saveObject(normal, adminAccountId).getString(ResourcePropNameConstants.ID);
		
		// 添加平台开发账户【3.平台开发账户】
		SysAccount developer = new SysAccount();
		developer.setId("93d02915eb764d978e3cae6987b5fc7a");/* 同上原因 */
		developer.setType(SysAccount.DEVELOPER);
		developer.setLoginName("developer");
		developer.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		developer.setLoginPwd(CryptographyUtil.encodeMd5(SysConfig.getSystemConfig("account.default.pwd"), developer.getLoginPwdKey()));
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
		HibernateUtil.saveObject(appDatabase, null);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加项目信息【运行平台数据库中的一个项目】
		ComProject project = new ComProject();
		project.setId("7fe971700f21d3a796d2017398812dcd");
		project.setRefDatabaseId("05fb6ef9c3ackfccb91b00add666odb9");
		project.setProjName("自动化配置项目(内置)");
		project.setProjCode("AutoConfigProj");
		project.analysisResourceProp();
		HibernateUtil.saveObject(project, normalAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 根据表创建hbm文件，并将其加入到CfgHibernateHbm表中
		insertHbm(adminAccountId);
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到CfgHibernateHbm表中
	 * @param adminAccountId 
	 */
	private void insertHbm(String adminAccountId) {
		List<ComTabledata> tables = getAllTables();
		CfgHibernateHbm hbm;
		SysResource resource;
		for (ComTabledata table : tables) {
			// 创建对应的hbm文件，并保存
			hbm = new CfgHibernateHbm(table);
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.setRefTableId("builtinResource");
			hbm.setContent(HibernateHbmUtil.createHbmMappingContent(table, true));
			HibernateUtil.saveObject(hbm, adminAccountId);
			
			// 保存到资源表中
			resource = table.turnToResource();
			resource.setRefResourceId("builtinResource");
			HibernateUtil.saveObject(resource, adminAccountId);
		}
		ResourceHandlerUtil.clearTables(tables);
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
	public void start() {
		Log4jUtil.info("loadHbmsByStart..........");
		processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
		try {
			// 先加载当前系统数据库的所有hbm映射文件
			loadCurrentSysDatabaseHbms();
			// 清空用户在线数据表
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, "delete SysAccountOnlineStatus", null);
		} catch (Exception e) {
			Log4jUtil.error("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		} finally{
			HibernateUtil.closeCurrentThreadSession();
		}
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
		// 获取当前系统的CfgHibernateHbm映射文件对象
		String sql = "select content from cfg_hibernate_hbm where ref_database_id = '"+database.getId()+"' and resource_name = 'CfgHibernateHbm'";
		String hbmContent = null;
		if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(SysConfig.getSystemConfig("jdbc.dbType"))){
			hbmContent = ((String) HibernateUtil.executeUniqueQueryBySql(sql, null)).trim();
		}else if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(SysConfig.getSystemConfig("jdbc.dbType"))){
			Clob clob = (Clob) HibernateUtil.executeUniqueQueryBySql(sql, null);
			if(clob == null){
				throw new NullPointerException("数据库名为["+database.getDisplayName()+"]，实例名为["+database.getInstanceName()+"]，ip为["+database.getIp()+"]，端口为["+database.getPort()+"]，用户名为["+database.getLoginUserName()+"]，密码为["+database.getLoginPassword()+"]，的数据库中，没有查询到CfgHibernateHbm的hbm文件内容，请检查：[" + sql + "]");
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgHibernateHbm where resourceName != 'CfgHibernateHbm' and refDatabaseId = ?", database.getId());
		if(count == 0){
			return;
		}
		long loopCount = count/100 + 1;
		List<Object> hbmContents = null;
		List<String> hcs = null;
		for(int i=0;i<loopCount;i++){
			hbmContents = HibernateUtil.executeListQueryByHqlArr("100", (i+1)+"", "select content from CfgHibernateHbm where resourceName !='CfgHibernateHbm' and refDatabaseId = ?", database.getId());
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
	}
	
	/**
	 * 初始化日志表
	 * <p>如果不存在日志表，则要创建</p>
	 * <p>这个要判断4个东西，reqLog表、reqLog的hbm、operSqlLog表、operSqlLog的hbm</p>
	 */
	private void initLogTables() {
		// 判断是否存在日志表table
		DBTableHandler tableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		List<String> logTableNames = tableHandler.filterTable(false, BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toDropTable(), BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toDropTable());
		if(logTableNames != null && logTableNames.size() > 0){
			// 不存在，则create
			for (String logTableName : logTableNames) {
				if(logTableName.equals(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toDropTable())){
					tableHandler.createTable(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toCreateTable(), true);
				}else if(logTableName.equals(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toDropTable())){
					tableHandler.createTable(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toCreateTable(), true);
				}
			}
		}
		
		// 判断是否存在日志表的hbm
		if(!HibernateUtil.hbmConfigIsExists(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).getEntityName())){
			createHbm(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toCreateTable());
		}
		if(!HibernateUtil.hbmConfigIsExists(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).getEntityName())){
			createHbm(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toCreateTable());
		}
	}
	
	/**
	 * 创建hbm对象
	 * @param table
	 */
	private void createHbm(ComTabledata table){
		// 插入hbm
		CfgHibernateHbm hbm = new CfgHibernateHbm(table); 
		hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
		hbm.setContent(HibernateHbmUtil.createHbmMappingContent(table, true));
		HibernateUtil.saveObject(hbm, null);
		
		// 插入资源数据
		BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).saveSysResource(table);
		
		// 将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbm.getContent());
		
		// 清空缓存
		table.clear();
	}
}
