package com.king.tooth.sys.code.resource;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.sys.controller.cfg.CfgColumnController;
import com.king.tooth.sys.controller.cfg.CfgDatabaseController;
import com.king.tooth.sys.controller.cfg.CfgProjectController;
import com.king.tooth.sys.controller.cfg.CfgProjectModuleController;
import com.king.tooth.sys.controller.cfg.CfgSqlController;
import com.king.tooth.sys.controller.cfg.CfgSqlParameterController;
import com.king.tooth.sys.controller.cfg.CfgTableController;
import com.king.tooth.sys.controller.sys.SysAccountController;
import com.king.tooth.sys.controller.sys.SysExcelController;
import com.king.tooth.sys.controller.sys.SysPermissionController;
import com.king.tooth.sys.controller.sys.SysPushMessageInfoController;
import com.king.tooth.sys.controller.sys.SysUserController;
import com.king.tooth.sys.controller.tools.SystemToolsController;

/**
 * 代码资源映射
 * @author DougLei
 */
public class CodeResourceMapping {
	static final Map<String, CodeResourceEntity> codeResourceMapping = new HashMap<String, CodeResourceEntity>(60); 
	/**
	 * 是否没有初始化代码资源映射
	 * 防止被多次初始化
	 */
	private static boolean unInitCodeResourceMapping = true;
	
	@SuppressWarnings("rawtypes")
	public static void put(String key, Class clz, String methodName){
		codeResourceMapping.put(key.toLowerCase(), new CodeResourceEntity(clz, methodName));
	}
	
	/**
	 * 初始化代码资源
	 */
	private static void initCodeResource() {
		// normal code
		initNormalCodeResource();
		// action code
		initActionCodeResource();
		// import data code
		initImportDataCodeResource();
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>简单的增删改查</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("/CfgDatabase_post", CfgDatabaseController.class, "add");
		put("/CfgDatabase_put", CfgDatabaseController.class, "update");
		put("/CfgDatabase_delete", CfgDatabaseController.class, "delete");
		put("/database/add/post", CfgDatabaseController.class, "add");
		put("/database/update/put", CfgDatabaseController.class, "update");
		put("/database/delete/delete", CfgDatabaseController.class, "delete");
		
		// 项目操作
		put("/CfgProject_post", CfgProjectController.class, "add");
		put("/CfgProject_put", CfgProjectController.class, "update");
		put("/CfgProject_delete", CfgProjectController.class, "delete");
		put("/project/add/post", CfgProjectController.class, "add");
		put("/project/update/put", CfgProjectController.class, "update");
		put("/project/delete/delete", CfgProjectController.class, "delete");
		
		// 项目模块操作
		put("/CfgProjectModule_post", CfgProjectModuleController.class, "add");
		put("/CfgProjectModule_put", CfgProjectModuleController.class, "update");
		put("/CfgProjectModule_delete", CfgProjectModuleController.class, "delete");
		put("/project_module/add/post", CfgProjectModuleController.class, "add");
		put("/project_module/update/put", CfgProjectModuleController.class, "update");
		put("/project_module/delete/delete", CfgProjectModuleController.class, "delete");
		
		// 表操作
		put("/CfgTable_post", CfgTableController.class, "add");
		put("/CfgTable_put", CfgTableController.class, "update");
		put("/CfgTable_delete", CfgTableController.class, "delete");
		put("/table/add/post", CfgTableController.class, "add");
		put("/table/update/put", CfgTableController.class, "update");
		put("/table/delete/delete", CfgTableController.class, "delete");
		
		// 列操作
		put("/CfgColumn_post", CfgColumnController.class, "add");
		put("/CfgColumn_put", CfgColumnController.class, "update");
		put("/CfgColumn_delete", CfgColumnController.class, "delete");
		put("/column/add/post", CfgColumnController.class, "add");
		put("/column/update/put", CfgColumnController.class, "update");
		put("/column/delete/delete", CfgColumnController.class, "delete");
		
		// sql脚本操作
		put("/CfgSql_post", CfgSqlController.class, "add");
		put("/CfgSql_put", CfgSqlController.class, "update");
		put("/CfgSql_delete", CfgSqlController.class, "delete");
		put("/sql/add/post", CfgSqlController.class, "add");
		put("/sql/update/put", CfgSqlController.class, "update");
		put("/sql/delete/delete", CfgSqlController.class, "delete");
		
		// sql脚本参数操作
		put("/CfgSqlParameter_post", CfgSqlParameterController.class, "add");
		put("/CfgSqlParameter_put", CfgSqlParameterController.class, "update");
		put("/CfgSqlParameter_delete", CfgSqlParameterController.class, "delete");
		put("/sql_parameter/add/post", CfgSqlParameterController.class, "add");
		put("/sql_parameter/update/put", CfgSqlParameterController.class, "update");
		put("/sql_parameter/delete/delete", CfgSqlParameterController.class, "delete");
		
		// 用户操作
		put("/SysUser_post", SysUserController.class, "add");
		put("/SysUser_put", SysUserController.class, "update");
		put("/SysUser_delete", SysUserController.class, "delete");
		put("/user/add/post", SysUserController.class, "add");
		put("/user/update/put", SysUserController.class, "update");
		put("/user/delete/delete", SysUserController.class, "delete");
		
		// 账户操作
		put("/SysAccount_post", SysAccountController.class, "add");
		put("/SysAccount_put", SysAccountController.class, "update");
		put("/SysAccount_delete", SysAccountController.class, "delete");
		put("/account/add/post", SysAccountController.class, "add");
		put("/account/update/put", SysAccountController.class, "update");
		put("/account/delete/delete", SysAccountController.class, "delete");
	}
	
	/**
	 * 初始化action代码资源
	 * <p>类似建模，开通账户等功能</p>
	 */
	private static void initActionCodeResource() {
		// 数据库操作
		put("/database/test/link/post", CfgDatabaseController.class, "testLink");
		
		// 表操作
		put("/table/model/create/post", CfgTableController.class, "buildModel");
		put("/table/model/drop/post", CfgTableController.class, "cancelBuildModel");
		put("/project/table/relation/add/post", CfgTableController.class, "addProjTableRelation");
		put("/project/table/relation/cancel/post", CfgTableController.class, "cancelProjTableRelation");
		
		// sql脚本操作
		put("/sql/object/create/post", CfgSqlController.class, "createSqlObject");
		put("/sql/object/drop/post", CfgSqlController.class, "dropSqlObject");
		put("/project/sql/relation/add/post", CfgSqlController.class, "addProjSqlScriptRelation");
		put("/project/sql/relation/cancel/post", CfgSqlController.class, "cancelProjSqlScriptRelation");
		
		// 用户操作
		put("/user/account/open/post", SysUserController.class, "openAccount");
		put("/user/account/close/post", SysUserController.class, "closeAccount");
		put("/user/account/reset/put", SysUserController.class, "resetAccount");
		put("/user/account/pwd/update/put", SysUserController.class, "updatePassword");
		put("/user/account/pwd/reset/put", SysUserController.class, "resetPassword");
		
		// 账户操作
		put("/login/post", SysAccountController.class, "login");
		put("/login_out/post", SysAccountController.class, "loginOut");
		
		// 权限操作
		put("/permission/get", SysPermissionController.class, "calcPermissionByCode");
		
		// 工具接口
		put("/hibernate/classmetadata/monitor/get", SystemToolsController.class, "monitorHibernateClassMetadata");// 监听hibernate类元数据
		put("/resource/info/search/get", SystemToolsController.class, "getResourceInfo");// 获取指定资源信息
		
		// 消息接口
		put("/message/push/post", SysPushMessageInfoController.class, "pushMessage");// 消息推送
		put("/message/read/get", SysPushMessageInfoController.class, "readMessage");// 消息阅读
		put("/message/read_status/update/put", SysPushMessageInfoController.class, "updateMessageReadStatus");// 修改消息的阅读状态
		
		// excel操作接口
		put("/excel/import/post", SysExcelController.class, "importExcel");// 导入excel
		put("/excel/import_template/create/post", SysExcelController.class, "createImportExcelTemplate");// 生成excel导入模版
	}
	
	/**
	 * 初始化import data代码资源
	 * <p>即资源的保存的代码方法</p>
	 */
	private static void initImportDataCodeResource() {
		put("/CfgDatabase"+IMPORT_DATA_KEY_SUFFIX, CfgDatabaseController.class, "add");// 数据库信息导入操作
		put("/CfgProject"+IMPORT_DATA_KEY_SUFFIX, CfgProjectController.class, "add");// 项目信息导入操作
		put("/CfgProjectModule"+IMPORT_DATA_KEY_SUFFIX, CfgProjectModuleController.class, "add");// 项目模块信息导入操作
		put("/ComTabledata"+IMPORT_DATA_KEY_SUFFIX, CfgTableController.class, "add");// 表信息导入操作
		put("/ComColumndata"+IMPORT_DATA_KEY_SUFFIX, CfgColumnController.class, "add");// 列信息导入操作
		put("/CfgSql"+IMPORT_DATA_KEY_SUFFIX, CfgSqlController.class, "add");// sql脚本信息导入操作
		put("/CfgSqlParameter"+IMPORT_DATA_KEY_SUFFIX, CfgSqlParameterController.class, "add");// sql脚本参数信息导入操作
		put("/SysUser"+IMPORT_DATA_KEY_SUFFIX, SysUserController.class, "add");// 用户信息导入操作
		put("/SysAccount"+IMPORT_DATA_KEY_SUFFIX, SysAccountController.class, "add");// 账户信息导入操作
	}
	/** 保存导入数据的代码，映射的key值后缀 */
	static final String IMPORT_DATA_KEY_SUFFIX = "/data/import";

	/**
	 * 系统启动时，初始化系统代码资源
	 */
	public static void initCodeResourceMapping(){
		if(unInitCodeResourceMapping){
			initCodeResource();
			unInitCodeResourceMapping = false;
		}
	}
}
