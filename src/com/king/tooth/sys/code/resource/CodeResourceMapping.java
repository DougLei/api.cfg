package com.king.tooth.sys.code.resource;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;

/**
 * 代码资源映射
 * @author DougLei
 */
public class CodeResourceMapping {
	static final Map<String, CodeResourceEntity> codeResourceMapping = new HashMap<String, CodeResourceEntity>(); 
	/**
	 * 是否没有初始化代码资源映射
	 * 防止被多次初始化
	 */
	private static boolean unInitCodeResourceMapping = true;
	
	private static void put(String key, Object instance, String methodName){
		codeResourceMapping.put(key.toLowerCase(), new CodeResourceEntity(instance, methodName));
	}
	
	/**
	 * 初始化代码资源
	 */
	private static void intCodeResource() {
		// code
		initActionCodeResource();
		// normal
		initNormalCodeResource();
		// webapi
		initWebApiResource();
	}
	
	/**
	 * 初始化webapi代码资源
	 * <p>例如：消息推送系统对外提供的接口</p>
	 */
	private static void initWebApiResource() {
		// 消息推送
		put("/message/single_push/post", BuiltinObjectInstance.pushMessageWebApi, "pushMessage");
		put("/message/batch_push//post", BuiltinObjectInstance.pushMessageWebApi, "batchPushMessage");
		put("/message/batch_individuality_push/post", BuiltinObjectInstance.pushMessageWebApi, "batchPushIndividualityMessage");
	}
	
	/**
	 * 初始化action代码资源
	 * <p>例如：ComTabledata post buildModel就是建模的操作，类似的还有发布操作等</p>
	 */
	private static void initActionCodeResource() {
		// 数据库操作
		put("/database/test_link/post", BuiltinObjectInstance.databaseController, "testLink");
		put("/database/publish/add/post", BuiltinObjectInstance.databaseController, "publish");
		put("/database/publish/cancel/post", BuiltinObjectInstance.databaseController, "cancelPublish");
		
		// 项目操作
		put("/project/publish/add/post", BuiltinObjectInstance.projectController, "publish");
		put("/project/publish/cancel/post", BuiltinObjectInstance.projectController, "cancelPublish");
		
		// 项目模块操作
		put("/project_module/publish/add/post", BuiltinObjectInstance.projectModuleController, "publish");
		put("/project_module/publish/cancel/post", BuiltinObjectInstance.projectModuleController, "cancelPublish");
		
		// 表操作
		put("ComTabledata_post_buildModel", BuiltinObjectInstance.tableController, "buildModel");
		put("/table/model/create/post", BuiltinObjectInstance.tableController, "buildModel");
		put("/table/publish/add/post", BuiltinObjectInstance.tableController, "publish");
		put("/table/publish/cancel/post", BuiltinObjectInstance.tableController, "cancelPublish");
		put("/project/table/relation/add/post", BuiltinObjectInstance.tableController, "addProjTableRelation");
		put("/project/table/relation/cancel/post", BuiltinObjectInstance.tableController, "cancelProjTableRelation");
		
		// sql脚本操作
		put("/sql/object/create/post", BuiltinObjectInstance.sqlController, "immediateCreate");
		put("/project/sql/relation/add/post", BuiltinObjectInstance.sqlController, "addProjSqlScriptRelation");
		put("/project/sql/relation/cancel/post", BuiltinObjectInstance.sqlController, "cancelProjSqlScriptRelation");
		put("/sql/publish/add/post", BuiltinObjectInstance.sqlController, "publish");
		put("/sql/publish/cancel/post", BuiltinObjectInstance.sqlController, "cancelPublish");
		
		// 用户操作
		put("/user/open_account/post", BuiltinObjectInstance.userController, "openAccount");
		put("/user/update_pwd/post", BuiltinObjectInstance.userController, "updatePassword");
		
		// 账户操作
		put("/login/post", BuiltinObjectInstance.accountController, "login");
		put("/login_out/post", BuiltinObjectInstance.accountController, "loginOut");
		put("/account/update_pwd/post", BuiltinObjectInstance.accountController, "updatePassword");
		
		// 账户操作
		put("/permission/get", BuiltinObjectInstance.permissionController, "calcPermissionByCode");
		
		// 监听hibernate类元数据
		put("/hibernate_classmetadata/monitor/get", BuiltinObjectInstance.systemToolsController, "monitorHibernateClassMetadata");
		// 获取指定资源信息
		put("/resource_info/get/get", BuiltinObjectInstance.systemToolsController, "getResourceInfo");
		
		// 消息推送
		put("/message/push/post", BuiltinObjectInstance.sysPushMessageInfoController, "pushMessage");
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>例如：ComTabledata post就是添加表的操作</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("CfgDatabase_post", BuiltinObjectInstance.databaseController, "add");
		put("CfgDatabase_put", BuiltinObjectInstance.databaseController, "update");
		put("CfgDatabase_delete", BuiltinObjectInstance.databaseController, "delete");
		put("/database/add/post", BuiltinObjectInstance.databaseController, "add");
		put("/database/update/put", BuiltinObjectInstance.databaseController, "update");
		put("/database/delete/delete", BuiltinObjectInstance.databaseController, "delete");
		
		// 项目操作
		put("ComProject_post", BuiltinObjectInstance.projectController, "add");
		put("ComProject_put", BuiltinObjectInstance.projectController, "update");
		put("ComProject_delete", BuiltinObjectInstance.projectController, "delete");
		put("/project/add/post", BuiltinObjectInstance.projectController, "add");
		put("/project/update/put", BuiltinObjectInstance.projectController, "update");
		put("/project/delete/delete", BuiltinObjectInstance.projectController, "delete");
		
		// 项目模块操作
		put("ComProjectModule_post", BuiltinObjectInstance.projectModuleController, "add");
		put("ComProjectModule_put", BuiltinObjectInstance.projectModuleController, "update");
		put("ComProjectModule_delete", BuiltinObjectInstance.projectModuleController, "delete");
		put("/project_module/add/post", BuiltinObjectInstance.projectModuleController, "add");
		put("/project_module/update/put", BuiltinObjectInstance.projectModuleController, "update");
		put("/project_module/delete/delete", BuiltinObjectInstance.projectModuleController, "delete");
		
		// 表操作
		put("ComTabledata_post", BuiltinObjectInstance.tableController, "add");
		put("ComTabledata_put", BuiltinObjectInstance.tableController, "update");
		put("ComTabledata_delete", BuiltinObjectInstance.tableController, "delete");
		put("/table/add/post", BuiltinObjectInstance.tableController, "add");
		put("/table/update/put", BuiltinObjectInstance.tableController, "update");
		put("/table/delete/delete", BuiltinObjectInstance.tableController, "delete");
		
		// 列操作
		put("ComColumndata_post", BuiltinObjectInstance.columnController, "add");
		put("ComColumndata_put", BuiltinObjectInstance.columnController, "update");
		put("ComColumndata_delete", BuiltinObjectInstance.columnController, "delete");
		put("/column/add/post", BuiltinObjectInstance.columnController, "add");
		put("/column/update/put", BuiltinObjectInstance.columnController, "update");
		put("/column/delete/delete", BuiltinObjectInstance.columnController, "delete");
		
		// sql脚本操作
		put("ComSqlScript_post", BuiltinObjectInstance.sqlController, "add");
		put("ComSqlScript_put", BuiltinObjectInstance.sqlController, "update");
		put("ComSqlScript_delete", BuiltinObjectInstance.sqlController, "delete");
		put("/sql/add/post", BuiltinObjectInstance.sqlController, "add");
		put("/sql/update/put", BuiltinObjectInstance.sqlController, "update");
		put("/sql/delete/delete", BuiltinObjectInstance.sqlController, "delete");
		
		// sql脚本参数操作
		put("ComSqlScriptParameter_post", BuiltinObjectInstance.sqlParamController, "add");
		put("ComSqlScriptParameter_put", BuiltinObjectInstance.sqlParamController, "update");
		put("ComSqlScriptParameter_delete", BuiltinObjectInstance.sqlParamController, "delete");
		put("/sql_parameter/add/post", BuiltinObjectInstance.sqlParamController, "add");
		put("/sql_parameter/update/put", BuiltinObjectInstance.sqlParamController, "update");
		put("/sql_parameter/delete/delete", BuiltinObjectInstance.sqlParamController, "delete");
		
		// 用户操作
		put("SysUser_post", BuiltinObjectInstance.userController, "add");
		put("SysUser_put", BuiltinObjectInstance.userController, "update");
		put("SysUser_delete", BuiltinObjectInstance.userController, "delete");
		put("/user/add/post", BuiltinObjectInstance.userController, "add");
		put("/user/update/put", BuiltinObjectInstance.userController, "update");
		put("/user/delete/delete", BuiltinObjectInstance.userController, "delete");
		
		// 账户操作
		put("SysAccount_post", BuiltinObjectInstance.accountController, "add");
		put("SysAccount_put", BuiltinObjectInstance.accountController, "update");
		put("SysAccount_delete", BuiltinObjectInstance.accountController, "delete");
		put("/account/add/post", BuiltinObjectInstance.accountController, "add");
		put("/account/update/put", BuiltinObjectInstance.accountController, "update");
		put("/account/delete/delete", BuiltinObjectInstance.accountController, "delete");
	}
	

	/**
	 * 初始化系统代码资源
	 */
	public static void initCodeResourceMapping(){
		if(unInitCodeResourceMapping){
			intCodeResource();
			unInitCodeResourceMapping = false;
		}
	}
}
