package com.api.sys.service.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.annotation.Service;
import com.api.cache.ProjectIdRefDatabaseIdMapping;
import com.api.cache.SysContext;
import com.api.constants.ResourceInfoConstants;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.plugins.jdbc.table.DBTableHandler;
import com.api.sys.builtin.data.BuiltinDatabaseData;
import com.api.sys.builtin.data.BuiltinObjectInstance;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.sys.entity.cfg.CfgHibernateHbm;
import com.api.sys.entity.cfg.CfgProject;
import com.api.sys.entity.cfg.CfgResource;
import com.api.sys.entity.cfg.CfgTable;
import com.api.sys.entity.sys.SysAccount;
import com.api.sys.entity.sys.SysOperSqlLog;
import com.api.sys.entity.sys.SysOperationLog;
import com.api.sys.entity.sys.SysReqLog;
import com.api.sys.service.AService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.CloseUtil;
import com.api.util.CryptographyUtil;
import com.api.util.ExceptionUtil;
import com.api.util.Log4jUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.hibernate.HibernateHbmUtil;
import com.api.util.hibernate.HibernateUtil;

/**
 * 初始化系统的Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
@Service
public class InitSystemService extends AService{
	private static final Logger logger = LoggerFactory.getLogger(InitSystemService.class);

	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void firstStart() {
		Log4jUtil.info("firstStart..........");
		try {
			processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
			initDatabaseInfo();// 初始化数据库信息
			updateInitConfig();
			Log4jUtil.info("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.error("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(1);
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
		
		// TODO 临时添加，为了适应触摸屏系统，7fe971700f21d3a796d2017398812dcf为cfg_project中id=7fe971700f21d3a796d2017398812dcf的数据
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(
				"7fe971700f21d3a796d2017398812dcf", 
				BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId());
	}
	
	/**
	 * 获取系统涉及到的所有表
	 * @return
	 */
	private List<CfgTable> getAllTables(){
		return BuiltinResourceInstance.getTables();
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
		List<CfgTable> tables = getAllTables();
		List<CfgTable> tmpTables = new ArrayList<CfgTable>();
		DBTableHandler dbHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		for (CfgTable table : tables) {
			tmpTables.add(table);
		}
		dbHandler.batchDropTable(tmpTables);// 尝试先删除表
		dbHandler.batchCreateTable(tmpTables, true);// 开始创建表
		clearTables(tmpTables);
		clearTables(tables);
	}
	
	/**
	 * 清除表信息
	 * @param tables
	 */
	private void clearTables(List<CfgTable> tables){
		if(tables != null && tables.size() > 0){
			for (CfgTable table : tables) {
				table.clear();
			}
			tables.clear();
		}
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到SessionFactory中
	 */
	private void insertHbmContentsToSessionFactory() {
		List<CfgTable> tables = getAllTables();
		List<String> hbmContents = new ArrayList<String>(tables.size());
		for (CfgTable table : tables) {
			hbmContents.add(HibernateHbmUtil.createHbmMappingContent(table, true));// 记录hbm内容
		}
		// 将hbmContents加入到hibernate sessionFactory中
		HibernateUtil.appendNewConfig(hbmContents);
		hbmContents.clear();
		clearTables(tables);
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
		admin.setLoginPwd(CryptographyUtil.encodeMd5(SysContext.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(BuiltinObjectInstance.validDate);
		String adminAccountId = HibernateUtil.saveObject(admin, null).getString(ResourcePropNameConstants.ID);
		
		// 添加用户管理账户【2.用户管理账户】，可以对系统用户和账户进行物理删除
		SysAccount userAdmin = new SysAccount();
		userAdmin.setId("62414f34367147729595cf815d33fe3f");/* 同上原因 */
		userAdmin.setType(SysAccount.ADMIN);
		userAdmin.setLoginName("user_admin");
		userAdmin.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		userAdmin.setLoginPwd(CryptographyUtil.encodeMd5(SysContext.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		userAdmin.setValidDate(BuiltinObjectInstance.validDate);
		HibernateUtil.saveObject(userAdmin, adminAccountId).getString(ResourcePropNameConstants.ID);
	
		// 添加平台开发账户【3.平台开发账户】
		SysAccount developer = new SysAccount();
		developer.setId("93d02915eb764d978e3cae6987b5fc7a");/* 同上原因 */
		developer.setType(SysAccount.DEVELOPER);
		developer.setLoginName("developer");
		developer.setLoginPwdKey(ResourceHandlerUtil.getLoginPwdKey());
		developer.setLoginPwd(CryptographyUtil.encodeMd5(SysContext.getSystemConfig("account.default.pwd"), developer.getLoginPwdKey()));
		developer.setValidDate(BuiltinObjectInstance.validDate);
		HibernateUtil.saveObject(developer, adminAccountId).getString(ResourcePropNameConstants.ID);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据库信息【运行平台数据库信息】
		CfgDatabase appDatabase = new CfgDatabase();
		appDatabase.setId("05fb6ef9c3ackfccb91b00add666odb9");
		appDatabase.setDisplayName("运行系统通用数据库(内置)");
		appDatabase.setType(SysContext.getSystemConfig("jdbc.dbType"));
		appDatabase.setInstanceName("SmartOneApp");
		appDatabase.setLoginUserName("SmartOneApp");
		appDatabase.setLoginPassword(SysContext.getSystemConfig("db.default.password"));
		appDatabase.setIp(SysContext.getSystemConfig("db.default.ip"));
		appDatabase.setPort(Integer.valueOf(SysContext.getSystemConfig("db.default.port")));
		appDatabase.analysisResourceProp();
		HibernateUtil.saveObject(appDatabase, adminAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加项目信息【运行平台数据库中的一个项目】
		CfgProject project = new CfgProject();
		project.setId("7fe971700f21d3a796d2017398812dcd");
		project.setRefDatabaseId("05fb6ef9c3ackfccb91b00add666odb9");
		project.setName("自动化配置项目(内置)");
		project.setCode("AutoConfigProj");
		project.analysisResourceProp();
		HibernateUtil.saveObject(project, adminAccountId);
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 根据表创建hbm文件，并将其加入到CfgHibernateHbm表中
		insertHbm(adminAccountId);
	}
	
	/**
	 * 根据表创建hbm文件，并将其加入到CfgHibernateHbm表中
	 * @param adminAccountId 
	 */
	private void insertHbm(String adminAccountId) {
		List<CfgTable> tables = getAllTables();
		CfgHibernateHbm hbm;
		CfgResource resource;
		for (CfgTable table : tables) {
			// 创建对应的hbm文件，并保存
			hbm = new CfgHibernateHbm(table);
			hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
			hbm.setRefTableId(ResourceInfoConstants.BUILTIN_RESOURCE);
			hbm.setContent(HibernateHbmUtil.createHbmMappingContent(table, true));
			HibernateUtil.saveObject(hbm, adminAccountId);
			
			// 保存到资源表中
			resource = table.turnToResource();
			resource.setRefResourceId(ResourceInfoConstants.BUILTIN_RESOURCE);
			HibernateUtil.saveObject(resource, adminAccountId);
		}
		clearTables(tables);
	}
	
	/**
	 * 修改初始化的配置文件内容
	 */
	private void updateInitConfig() {
		if("false".equals(SysContext.getSystemConfig("is.develop"))){
			// 如果不是开发模式的话，在进行了初始化操作后，系统自动去修改api.platform.init.properties配置文件的内容，将true值改为false
			File file = new File(SysContext.WEB_SYSTEM_CONTEXT_REALPATH + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "api.platform.init.properties");
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
		Log4jUtil.info("start..........");
		processCurrentSysOfPorjDatabaseRelation();// 处理本系统和本数据库的关系
		try {
			CurrentThreadContext.setDatabaseId(BuiltinObjectInstance.currentSysBuiltinDatabaseInstance.getId());
			
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			
			// 先加载当前系统数据库的所有hbm映射文件
			loadCurrentSysDatabaseHbms();
			// 清空用户在线数据表
			HibernateUtil.executeUpdateBySql(SqlStatementTypeConstants.DELETE, "truncate table sys_account_online_status", null);
			
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error(getExceptionDetailMessage(e));
			HibernateUtil.rollbackTransaction();
			Log4jUtil.error("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		} finally{
			HibernateUtil.closeCurrentThreadSession();
		}
	}
	
	/**
	 * 获取异常的详细信息
	 * <p>错在哪个类，哪一行</p>
	 * @param t
	 * @return
	 */
	private String getExceptionDetailMessage(Throwable t){
		PrintWriter pw = null;
		try {
			StringWriter sw = new StringWriter();
			pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			pw.close();
			pw = null;
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
		
		// 获取当前系统的CfgHibernateHbm映射文件对象
		String sql = "select content from cfg_hibernate_hbm where ref_database_id = '"+database.getId()+"' and resource_name = 'CfgHibernateHbm'";
		String hbmContent = null;
		if(BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(SysContext.getSystemConfig("jdbc.dbType"))){
			hbmContent = ((String) HibernateUtil.executeUniqueQueryBySql(sql, null)).trim();
		}else if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(SysContext.getSystemConfig("jdbc.dbType"))){
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
				if(obj != null){
					hcs.add(obj.toString());
				}
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
	 * <p>这个要判断6个东西，reqLog表、reqLog的hbm、operSqlLog表、operSqlLog的hbm、reqOperationLog表、reqOperationLog的hbm</p>
	 */
	private void initLogTables() {
		// 判断是否存在日志表table
		DBTableHandler tableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		List<String> logTableNames = tableHandler.filterTable(false, BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toDropTable(), BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toDropTable(), BuiltinResourceInstance.getInstance("SysOperationLog", SysOperationLog.class).toDropTable());
		if(logTableNames != null && logTableNames.size() > 0){
			// 不存在，则create
			for (String logTableName : logTableNames) {
				if(logTableName.equals(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toDropTable())){
					tableHandler.createTable(BuiltinResourceInstance.getInstance("SysReqLog", SysReqLog.class).toCreateTable(), true);
				}else if(logTableName.equals(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toDropTable())){
					tableHandler.createTable(BuiltinResourceInstance.getInstance("SysOperSqlLog", SysOperSqlLog.class).toCreateTable(), true);
				}else if(logTableName.equals(BuiltinResourceInstance.getInstance("SysOperationLog", SysOperationLog.class).toDropTable())){
					tableHandler.createTable(BuiltinResourceInstance.getInstance("SysOperationLog", SysOperationLog.class).toCreateTable(), true);
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
		if(!HibernateUtil.hbmConfigIsExists(BuiltinResourceInstance.getInstance("SysOperationLog", SysOperationLog.class).getEntityName())){
			createHbm(BuiltinResourceInstance.getInstance("SysOperationLog", SysOperationLog.class).toCreateTable());
		}
	}
	
	/**
	 * 创建hbm对象
	 * @param table
	 */
	private void createHbm(CfgTable table){
		// 插入hbm
		CfgHibernateHbm hbm = new CfgHibernateHbm(table); 
		hbm.setRefTableId(ResourceInfoConstants.BUILTIN_RESOURCE);
		hbm.setRefDatabaseId(CurrentThreadContext.getDatabaseId());
		hbm.setContent(HibernateHbmUtil.createHbmMappingContent(table, true));
		HibernateUtil.saveObject(hbm, null);
		
		// 插入资源数据
		CfgResource resource = table.turnToResource();
		resource.setRefResourceId(ResourceInfoConstants.BUILTIN_RESOURCE);
		HibernateUtil.saveObject(resource, null);
		
		// 将hbm配置内容，加入到sessionFactory中
		HibernateUtil.appendNewConfig(hbm.getContent());
		
		// 清空缓存
		table.clear();
	}
}
