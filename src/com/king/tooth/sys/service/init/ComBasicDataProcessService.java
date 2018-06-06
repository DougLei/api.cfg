package com.king.tooth.sys.service.init;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.SysDatabaseInstanceConstants;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgCustomer;
import com.king.tooth.sys.entity.cfg.CfgHibernateHbm;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.ComDatabaseCfgTabledataLinks;
import com.king.tooth.sys.entity.common.ComDataDictionary;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.entity.common.ComOperLog;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.entity.common.ComProjectModuleBody;
import com.king.tooth.sys.entity.common.ComReqLog;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysAccount;
import com.king.tooth.sys.entity.common.ComSysAccountOnlineStatus;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.common.ComUser;
import com.king.tooth.sys.entity.common.datalinks.ComDataLinks;
import com.king.tooth.sys.entity.common.datalinks.ComDatabaseComSqlScriptLinks;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]基础数据处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
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
		try {
			createTables();
			
			HibernateUtil.openSessionToCurrentThread();
			HibernateUtil.beginTransaction();
			
			insertAllTables();// 将表信息插入的cfgTabledata表中，同时把列的信息插入到cfgColumndata表中
			insertTableToResources();// 将这些表资源插入到资源表
			insertCfgDatabaseOfBasicDatas();// 插入配置库的基础数据
			
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
	private List<CfgTabledata> getInitTables(){
		List<CfgTabledata> tables = new ArrayList<CfgTabledata>(19);
		String dbType = SysConfig.getSystemConfig("jdbc.dbType");
		
		tables.add(new ComSysResource().toCreateTable(dbType));
		tables.add(new CfgHibernateHbm().toCreateTable(dbType));
		tables.add(new CfgColumndata().toCreateTable(dbType));
		tables.add(new CfgCustomer().toCreateTable(dbType));
		tables.add(new CfgTabledata().toCreateTable(dbType));
		tables.add(new ComDatabase().toCreateTable(dbType));
		tables.add(new ComDataDictionary().toCreateTable(dbType));
		tables.add(new ComDataLinks().toCreateTable(dbType));
		tables.add(new ComModuleOperation().toCreateTable(dbType));
		tables.add(new ComOperLog().toCreateTable(dbType));
		tables.add(new ComProject().toCreateTable(dbType));
		tables.add(new ComProjectModule().toCreateTable(dbType));
		tables.add(new ComProjectModuleBody().toCreateTable(dbType));
		tables.add(new ComReqLog().toCreateTable(dbType));
		tables.add(new ComSqlScript().toCreateTable(dbType));
		tables.add(new ComSysAccount().toCreateTable(dbType));
		tables.add(new ComSysAccountOnlineStatus().toCreateTable(dbType));
		tables.add(new ComUser().toCreateTable(dbType));
		tables.add(new ComDatabaseCfgTabledataLinks().toCreateTable(dbType));
		tables.add(new ComDatabaseComSqlScriptLinks().toCreateTable(dbType));
		
		return tables;
	}
	
	/**
	 * 清除表信息
	 * @param tables
	 */
	private void clearTables(List<CfgTabledata> tables){
		for (CfgTabledata table : tables) {
			table.clear();
		}
		tables.clear();
	}
	
	/**
	 * 创建表
	 * @return 
	 */
	private void createTables(){
		List<CfgTabledata> tables = getInitTables();
		DBTableHandler dbHandler = new DBTableHandler(SysDatabaseInstanceConstants.CFG_DATABASE);
		try {
			dbHandler.dropTable(tables);
		} catch (Exception e) {
			Log4jUtil.debug("表不存在，不需要删除");
		}
		// 开始创建表
		dbHandler.createTable(tables);
		clearTables(tables);
	}
	
	/**
	 * 插入配置库的基础数据
	 */
	private void insertCfgDatabaseOfBasicDatas() {
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加公司配置平台管理员和对应的用户【这个是超级管理员】
		ComSysAccount admin = new ComSysAccount();
		admin.setAccountType(0);
		admin.setLoginName("administrator");
		admin.setLoginPwd(CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), admin.getLoginPwdKey()));
		admin.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		String adminAccountId = HibernateUtil.saveObject(admin, null);
		
		ComUser adminUser = new ComUser();
		adminUser.setAccountId(adminAccountId);
		adminUser.setNikeName("administrator");
		adminUser.setRealName("配置平台管理员");
		HibernateUtil.saveObject(adminUser, null);

		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		
		
		//----------------------------------------------------------------------------------------------------------------------------------------------------------
		// 添加数据字典数据
		insertDataDictionary();
	}
	
	/**
	 * 将表信息插入的cfgTabledata表中
	 * 同时把列的信息插入到cfgColumndata表中
	 */
	private void insertAllTables() {
		List<CfgTabledata> tables = getInitTables();
		
		String tableId;
		List<CfgColumndata> columns = null;
		for (CfgTabledata table : tables) {
			tableId = HibernateUtil.saveObject(table, "初始化插入内置表");
			columns = table.getColumns();
			for (CfgColumndata column : columns) {
				column.setTableId(tableId);
				HibernateUtil.saveObject(column, "初始化插入内置表的列");
			}
		}
		clearTables(tables);
	}
	
	/**
	 * 将表资源添加到资源表中
	 */
	private void insertTableToResources() {
		List<CfgTabledata> tables = getInitTables();
		
		ComSysResourceService comSysResourceService = new ComSysResourceService();
		for (CfgTabledata table : tables) {
			comSysResourceService.insertSysResource(table);
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
		insertDataDictionary(null, "cfgtabledata.tabletype", "主子表", "3", 3);
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
		insertDataDictionary(null, "comsysresource.resourcetype", "代码资源", "2", 2);
		insertDataDictionary(null, "comsysresource.resourcetype", "数据库资源", "2", 2);
		insertDataDictionary(null, "comsysresource.resourcetype", "项目资源", "2", 2);
		
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
	
	//------------------------------------------------------------------------------------
	/**
	 * 系统每次启动时，加载hbm的配置信息
	 * 主要是hbm内容
	 */
	public void loadSysBasicDatasBySysStart() {
		List<Object> hbmContents = HibernateUtil.executeListQueryByHql("select hbmContent from CfgHibernateHbm", null);
		if(hbmContents != null && hbmContents.size() > 0){
			List<String> hcs = new ArrayList<String>(hbmContents.size());
			for (Object obj : hbmContents) {
				hcs.add(obj+"");
			}
			
			HibernateUtil.appendNewConfig(hcs);
			hcs.clear();
			hbmContents.clear();
		}
	}
}
