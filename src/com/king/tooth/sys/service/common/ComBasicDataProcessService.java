package com.king.tooth.sys.service.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
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
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]基础数据处理器
 * @author DougLei
 */
public class ComBasicDataProcessService extends AbstractResourceService{
	
	/**
	 * 系统首次启动时，初始化系统的基础数据
	 */
	public void loadSysBasicDatasBySysFirstStart() {
		try {
			initCfgDatabaseInfo();
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
			
			
			ComDatabase database = new ComDatabase();
			database.setDbDisplayName("配置平台数据库");
			database.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
			// 开始创建配置表
			DBTableHandler dbHandler = new DBTableHandler(database);
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
		// 添加数据字典数据
		insertDataDictionary();
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
	 */
	private void insertDataDictionary() {
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
}
