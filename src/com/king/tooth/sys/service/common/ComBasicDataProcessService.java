package com.king.tooth.sys.service.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.plugins.orm.hibernate.hbm.HibernateHbmHandler;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgCustomer;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.CfgCustomerCfgDatabaseLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgCustomerComProjectLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgCustomerComSysAccountLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgDatabaseCfgTabledataLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgDatabaseComSqlScriptLinks;
import com.king.tooth.sys.entity.cfg.datalinks.ComProjectCfgTabledataLinks;
import com.king.tooth.sys.entity.common.ComDataDictionary;
import com.king.tooth.sys.entity.common.ComDataLinks;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComHibernateHbmConfdata;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.entity.common.ComOperLog;
import com.king.tooth.sys.entity.common.ComPermission;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.entity.common.ComProjectModuleBody;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.entity.common.ComRole;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.common.ComUser;
import com.king.tooth.sys.entity.common.ComVerifyCode;
import com.king.tooth.sys.entity.common.datalinks.ComProjectComProjectModuleLinks;
import com.king.tooth.sys.entity.common.datalinks.ComProjectComSqlScriptLinks;
import com.king.tooth.sys.entity.common.datalinks.ComRoleComPermissionLinks;
import com.king.tooth.sys.entity.common.datalinks.ComSysAccountComRoleLinks;
import com.king.tooth.sys.entity.run.RunDept;
import com.king.tooth.sys.entity.run.RunOrg;
import com.king.tooth.sys.entity.run.RunPosition;
import com.king.tooth.sys.entity.run.datalinks.ComUserRunDeptLinks;
import com.king.tooth.sys.entity.run.datalinks.ComUserRunPositionLinks;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]基础数据处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComBasicDataProcessService extends AbstractResourceService{

	/**
	 * 测试项目主键
	 * <p>在配置库添加了一个测试项目，记录测试项目的id，在初始化测试库的时候，会用到该id</p>
	 */
	private String testProjectId;
	
	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void loadSysBasicDatasBySysFirstStart() {
		try {
			initCfgDatabaseInfo();
			initTestDatabaseInfo();
			updateInitConfig();
			Log4jUtil.debug("系统初始化完成！");
		} catch (Exception e) {
			Log4jUtil.debug("系统初始化出现异常，异常信息为:{}", ExceptionUtil.getErrMsg(e));
			System.exit(0);
		}
	}
	
	/**
	 * 初始化配置库信息
	 */
	private void initCfgDatabaseInfo() {
		List<CfgTabledata> cfgTables = new ArrayList<CfgTabledata>(31);
		try {
			CurrentThreadContext.setProjectId(SysConfig.getSystemConfig("cfg.project.id"));
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			
			String dbType = SysConfig.getSystemConfig("jdbc.dbType");
			// 3个配置表
			cfgTables.add(new CfgTabledata().toCreateTable(dbType));
			cfgTables.add(new CfgColumndata().toCreateTable(dbType));
			cfgTables.add(new CfgCustomer().toCreateTable(dbType));
			// 6个关系表
			cfgTables.add(new CfgCustomerCfgDatabaseLinks().toCreateTable(dbType));
			cfgTables.add(new CfgCustomerComProjectLinks().toCreateTable(dbType));
			cfgTables.add(new CfgCustomerComSysAccountLinks().toCreateTable(dbType));
			cfgTables.add(new CfgDatabaseCfgTabledataLinks().toCreateTable(dbType));
			cfgTables.add(new CfgDatabaseComSqlScriptLinks().toCreateTable(dbType));
			cfgTables.add(new ComProjectCfgTabledataLinks().toCreateTable(dbType));
			addCommonTables(cfgTables, dbType);
			// 开始创建配置表
			DBTableHandler dbHandler = new DBTableHandler();
			dbHandler.createTable(cfgTables);
			
			insertResources(SysConfig.getSystemConfig("cfg.database.id"), cfgTables);// 将这些表资源插入到资源表
			insertCfgDatabaseOfBasicDatas();// 插入配置库的基础数据
			
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		}finally{
			cfgTables.clear();// 清空表集合
			HibernateUtil.closeCurrentThreadSession();
			CurrentThreadContext.clearCurrentThreadData();
		}
	}
	
	/**
	 * 插入配置库的基础数据
	 */
	private void insertCfgDatabaseOfBasicDatas() {
		ComSysAccountService comSysAccountService = new ComSysAccountService();
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加公司配置平台管理员和对应的用户【不需要角色，这个是超级管理员】
		ComSysAccount admin = new ComSysAccount();
		admin.setLoginName("admin");
		admin.setAccountType(0);
		admin.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		admin.setIsUnDelete(1);
		String adminAccountId = comSysAccountService.saveComSysAccount(admin, "系统初始化配置平台管理员");
		
		ComUser adminUser = new ComUser();
		adminUser.setAccountId(adminAccountId);
		adminUser.setNikeName("administrator");
		adminUser.setRealName("配置平台管理员");
		adminUser.setIsUnDelete(1);
		HibernateUtil.saveObject(adminUser, "系统初始化配置平台管理员");
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加客户信息(初始化是本公司：博道工业)
		CfgCustomer sinoforceCustomer = new CfgCustomer();
		sinoforceCustomer.setName("西安博道工业科技有限公司");
		sinoforceCustomer.setShortName("博道工业");
		sinoforceCustomer.setIsUnDelete(1);
		String sinoforceCustomerId = HibernateUtil.saveObject(sinoforceCustomer, "系统初始化博道工业客户信息");
		
		// 建立账户和客户的关联关系
		Map<String, Object> customerComSysAccountDataLink = ResourceHandlerUtil.getDataLinksObject(sinoforceCustomerId, adminAccountId, 2, null, null);
		HibernateUtil.saveObject("CfgCustomerComSysAccountLinks", customerComSysAccountDataLink , "系统初始化配置客户和帐号关系");
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 给本公司(西安博道工业科技有限公司)添加一个测试项目
		ComProject testProject = new ComProject();
		testProject.setDatabaseId(SysConfig.getSystemConfig("test.database.id"));
		testProject.setName("SmartOne-测试项目");
		testProject.setOwnerCustomerId(sinoforceCustomerId);
		testProject.setIsTest(1);
		testProjectId = HibernateUtil.saveObject(testProject, "系统初始化测试项目");
		
		// 记录项目id和数据库id的映射
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(testProjectId, testProject.getDatabaseId());
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据字典数据
		insertDataDictionary(true);
	}
	
	/**
	 * 初始化测试库信息
	 * @param dbType
	 */
	private void initTestDatabaseInfo(){
		List<CfgTabledata> testTables = new ArrayList<CfgTabledata>(27);
		try {
			CurrentThreadContext.setProjectId(testProjectId);
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			
			String dbType = SysConfig.getSystemConfig("jdbc.dbType");
			// 3个基础表
			testTables.add(new RunDept().toCreateTable(dbType));
			testTables.add(new RunOrg().toCreateTable(dbType));
			testTables.add(new RunPosition().toCreateTable(dbType));
			// 2个关系表
			testTables.add(new ComUserRunDeptLinks().toCreateTable(dbType));
			testTables.add(new ComUserRunPositionLinks().toCreateTable(dbType));
			addCommonTables(testTables, dbType);
			// 开始创建测试表
			DBTableHandler dbHandler = new DBTableHandler();
			dbHandler.createTable(testTables);
			
			insertResources(SysConfig.getSystemConfig("test.database.id"), testTables);// 将这些表资源插入到资源表
			insertTestDatabaseOfBasicDatas();// 插入测试库的基础数据
			
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			throw e;
		}finally{
			testTables.clear();// 清空表集合
			HibernateUtil.closeCurrentThreadSession();
			CurrentThreadContext.clearCurrentThreadData();
		}
	}
	
	/**
	 * 插入测试库的基础数据
	 */
	private void insertTestDatabaseOfBasicDatas() {
		ComSysAccountService comSysAccountService = new ComSysAccountService();
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加系统管理员和对应的用户
		ComSysAccount admin = new ComSysAccount();
		admin.setLoginName("admin");
		admin.setAccountType(3);
		admin.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		admin.setIsUnDelete(1);
		String adminAccountId = comSysAccountService.saveComSysAccount(admin, "系统初始化系统管理员");
		
		ComUser adminUser = new ComUser();
		adminUser.setAccountId(adminAccountId);
		adminUser.setNikeName("sysadmin");
		adminUser.setRealName("系统管理员");
		adminUser.setIsUnDelete(1);
		HibernateUtil.saveObject(adminUser, "系统初始化系统管理员");
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据字段数据
		insertDataDictionary(false);
	}
	
	/**
	 * 将表资源添加到资源表中
	 * @param databaseId
	 * @param tables
	 */
	private void insertResources(String databaseId, List<CfgTabledata> tables) {
		ComSysResourceService comSysResourceService = new ComSysResourceService();
		for (CfgTabledata table : tables) {
			comSysResourceService.insertSysResource(databaseId, table);
		}
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
	 * 添加通用表
	 * <p>目前有22个</p>
	 * @param commonTables
	 * @param dbType
	 */
	private void addCommonTables(List<CfgTabledata> commonTables, String dbType){
		commonTables.add(new ComDatabase().toCreateTable(dbType));
		commonTables.add(new ComHibernateHbmConfdata().toCreateTable(dbType));
		commonTables.add(new ComDataDictionary().toCreateTable(dbType));
		commonTables.add(new ComDataLinks().toCreateTable(dbType));
		commonTables.add(new ComModuleOperation().toCreateTable(dbType));
		commonTables.add(new ComPermission().toCreateTable(dbType));
		commonTables.add(new ComProject().toCreateTable(dbType));
		commonTables.add(new ComProjectModule().toCreateTable(dbType));
		commonTables.add(new ComProjectModuleBody().toCreateTable(dbType));
		commonTables.add(new ComReqLog().toCreateTable(dbType));
		commonTables.add(new ComOperLog().toCreateTable(dbType));
		commonTables.add(new ComRole().toCreateTable(dbType));
		commonTables.add(new ComSqlScript().toCreateTable(dbType));
		commonTables.add(new ComSysAccount().toCreateTable(dbType));
		commonTables.add(new ComSysAccountOnlineStatus().toCreateTable(dbType));
		commonTables.add(new ComSysResource().toCreateTable(dbType));
		commonTables.add(new ComUser().toCreateTable(dbType));
		commonTables.add(new ComVerifyCode().toCreateTable(dbType));
		commonTables.add(new ComProjectComProjectModuleLinks().toCreateTable(dbType));
		commonTables.add(new ComRoleComPermissionLinks().toCreateTable(dbType));
		commonTables.add(new ComSysAccountComRoleLinks().toCreateTable(dbType));
		commonTables.add(new ComProjectComSqlScriptLinks().toCreateTable(dbType));
	}
	
	//---------------------------------------------------------------------------------------------------
	/**
	 * 添加数据字典的基础数据
	 * @param isCfgProject 是否是配置平台项目，如果是配置平台项目，要添加一些专属的数据字典信息
	 */
	private void insertDataDictionary(boolean isCfgProject) {
		if(isCfgProject){
			// CfgColumndata.columnType 字段数据类型
			insertDataDictionary(null, "cfgcolumndata.columntype", "字符串", "string", 1);
			insertDataDictionary(null, "cfgcolumndata.columntype", "布尔值", "boolean", 2);
			insertDataDictionary(null, "cfgcolumndata.columntype", "整型", "integer", 3);
			insertDataDictionary(null, "cfgcolumndata.columntype", "浮点型", "double", 4);
			insertDataDictionary(null, "cfgcolumndata.columntype", "日期", "date", 5);
			insertDataDictionary(null, "cfgcolumndata.columntype", "字符大字段", "clob", 6);
			insertDataDictionary(null, "cfgcolumndata.columntype", "二进制大字段", "blob", 7);
			
			// ComDatabase.dbType 数据库类型
			insertDataDictionary(null, "cfgdatabase.dbtype", "oracle", "oracle", 1);
			insertDataDictionary(null, "cfgdatabase.dbtype", "sqlserver", "sqlserver", 2);
			
			// CfgTabledata.tableType 表类型
			insertDataDictionary(null, "cfgtabledata.tabletype", "单表", "1", 1);
			insertDataDictionary(null, "cfgtabledata.tabletype", "树表", "2", 2);
			insertDataDictionary(null, "cfgtabledata.tabletype", "父子关系表", "3", 3);
			// CfgTabledata.dbType 数据库类型
			insertDataDictionary(null, "cfgtabledata.dbtype", "oracle", "oracle", 1);
			insertDataDictionary(null, "cfgtabledata.dbtype", "sqlserver", "sqlserver", 2);
		}
		
		// ComOperLog.operType 操作的类型
		insertDataDictionary(null, "comoperLog.opertype", "查询", "select", 1);
		insertDataDictionary(null, "comoperLog.opertype", "增加", "insert", 2);
		insertDataDictionary(null, "comoperLog.opertype", "修改", "update", 3);
		insertDataDictionary(null, "comoperLog.opertype", "删除", "delete", 4);
		
		// ComPermission.permissionType 权限的类型
		insertDataDictionary(null, "compermission.permissiontype", "模块", "1", 1);
		insertDataDictionary(null, "compermission.permissiontype", "页面操作", "2", 2);
		
		// ComProject.progressStatus 项目进度
		insertDataDictionary(null, "comproject.progressstatus", "调研", "1", 1);
		insertDataDictionary(null, "comproject.progressstatus", "设计", "2", 2);
		insertDataDictionary(null, "comproject.progressstatus", "开发", "3", 3);
		insertDataDictionary(null, "comproject.progressstatus", "测试", "4", 4);
		insertDataDictionary(null, "comproject.progressstatus", "试运行", "5", 5);
		insertDataDictionary(null, "comproject.progressstatus", "上线", "6", 6);
		insertDataDictionary(null, "comproject.progressstatus", "验收", "7", 7);
		
		// ComSysAccount.accountType 账户类型
		insertDataDictionary(null, "comsysaccount.accounttype", "超级管理员", "0", 0);
		insertDataDictionary(null, "comsysaccount.accounttype", "游客", "1", 1);
		insertDataDictionary(null, "comsysaccount.accounttype", "客户", "2", 2);
		insertDataDictionary(null, "comsysaccount.accounttype", "普通账户", "3", 3);
		insertDataDictionary(null, "comsysaccount.accounttype", "普通虚拟账户", "4", 4);
		
		// ComSysAccount.accountStatus 账户状态
		insertDataDictionary(null, "comsysaccount.accountstatus", "启用", "1", 1);
		insertDataDictionary(null, "comsysaccount.accountstatus", "禁用", "2", 2);
		insertDataDictionary(null, "comsysaccount.accountstatus", "过期", "3", 3);
		
		// ComSysResource.resourceType 账户状态
		insertDataDictionary(null, "comsysresource.resourcetype", "表资源", "1", 1);
		insertDataDictionary(null, "comsysresource.resourcetype", "sql脚本资源", "2", 2);
		
		// ComUser.userStatus 账户状态
		insertDataDictionary(null, "comuser.userstatus", "在职", "1", 1);
		insertDataDictionary(null, "comuser.userstatus", "离职", "2", 2);
		insertDataDictionary(null, "comuser.userstatus", "休假", "3", 3);
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
	private String insertDataDictionary(String parentId, String code, String codeCaption, String codeValue, int orderCode){
		ComDataDictionary dataDictionary = new ComDataDictionary();
		dataDictionary.setParentCodeId(parentId);
		dataDictionary.setCode(code);
		dataDictionary.setCodeCaption(codeCaption);
		dataDictionary.setCodeValue(codeValue);
		dataDictionary.setOrderCode(orderCode);
		return HibernateUtil.saveObject(dataDictionary, "系统初始化添加内置数据字典");
	}

	//---------------------------------------------------------------------------------------------------
	/**
	 * 系统启动时，加载所有配置数据
	 */
	public void loadSysConfDatasBySysStart() {
		// 加载数据库、项目、资源
		CurrentThreadContext.setProjectId(SysConfig.getSystemConfig("cfg.project.id"));
		HibernateUtil.openSessionToCurrentThread();
		
		String hql = "from ComDatabase where isEnabled=1 and isCreated=1";
		List<ComDatabase> databases = HibernateUtil.executeListQueryByHql(hql, null);

		// 动态添加数据源和sessionFactory
		if(databases.size() > 0){
			for (ComDatabase database : databases) {
				DynamicDBUtil.addDataSource(database);
			}
		}
		
		// 将测试库也加入进来，因为他内置到系统中，没有存储到数据库中【配置库是系统库，这里不做处理】
		// 下来要关联项目id和数据库id的映射
		databases.add(new ComDatabase(SysConfig.getSystemConfig("test.database.id")));
		
		// 关联项目id和数据库id的映射
		hql = "select new ComProject(id, databaseId) from ComProject where isDeployment=1";
		List<ComProject> projects = HibernateUtil.executeListQueryByHql(hql, null);
		if(projects != null && projects.size() > 0){
			for (ComDatabase database : databases) {
				for (ComProject project : projects) {
					if(project.getDatabaseId().equals(database.getId())){
						ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(project.getId(), database.getId());
						break;
					}
				}
			}
			projects.clear();
		}
		
		// 加载各个数据库的hbm映射文件
		List<Object> hbms = null;
		SessionFactoryImpl sessionFactoryImpl = null;
		hql = "select fileContent from ComHibernateHbmConfdata where id in (select refResourceId from ComSysResource where isEnabled=1 and resourceType = 1 and databaseId = ?)";
		HibernateHbmHandler hibernateHbmHandler = new HibernateHbmHandler();
		for (ComDatabase database : databases) {
			hbms = HibernateUtil.executeListQueryByHqlArr(hql, database.getId());
			if(hbms!=null && hbms.size()>0){
				sessionFactoryImpl = DynamicDBUtil.getSessionFactory(database.getId());
				if(sessionFactoryImpl == null){
					throw new NullPointerException("系统无法获取databaseId为["+database.getId()+"]的sessionFactory！");
				}
				hibernateHbmHandler.appendNewHbmConfig(hbms, sessionFactoryImpl);
				hbms.clear();
			}
		}
		databases.clear();
		
		HibernateUtil.closeCurrentThreadSession();
		CurrentThreadContext.clearCurrentThreadData();
	}
}
