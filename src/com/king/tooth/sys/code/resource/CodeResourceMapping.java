package com.king.tooth.sys.code.resource;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;

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
	}
	
	/**
	 * 初始化action代码资源
	 * <p>例如：ComTabledata post buildModel就是建模的操作，类似的还有发布操作等</p>
	 */
	private static void initActionCodeResource() {
		// 数据库操作
		put("/database/test_link/post", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "testLink");
		put("/database/publish/add/post", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "publish");
		put("/database/publish/cancel/post", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "cancelPublish");
		
		// 项目操作
		put("/project/publish/add/post", BuiltinResourceInstance.getInstance("CfgProjectController"), "publish");
		put("/project/publish/cancel/post", BuiltinResourceInstance.getInstance("CfgProjectController"), "cancelPublish");
		
		// 项目模块操作
		put("/project_module/publish/add/post", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "publish");
		put("/project_module/publish/cancel/post", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "cancelPublish");
		
		// 表操作
		put("ComTabledata_post_buildModel", BuiltinResourceInstance.getInstance("CfgTableController"), "buildModel");
		put("/table/model/create/post", BuiltinResourceInstance.getInstance("CfgTableController"), "buildModel");
		put("/table/model/drop/post", BuiltinResourceInstance.getInstance("CfgTableController"), "cancelBuildModel");
		put("/table/publish/add/post", BuiltinResourceInstance.getInstance("CfgTableController"), "publish");
		put("/table/publish/cancel/post", BuiltinResourceInstance.getInstance("CfgTableController"), "cancelPublish");
		put("/project/table/relation/add/post", BuiltinResourceInstance.getInstance("CfgTableController"), "addProjTableRelation");
		put("/project/table/relation/cancel/post", BuiltinResourceInstance.getInstance("CfgTableController"), "cancelProjTableRelation");
		
		// sql脚本操作
		put("/sql/object/create/post", BuiltinResourceInstance.getInstance("CfgSqlController"), "immediateCreate");
		put("/project/sql/relation/add/post", BuiltinResourceInstance.getInstance("CfgSqlController"), "addProjSqlScriptRelation");
		put("/project/sql/relation/cancel/post", BuiltinResourceInstance.getInstance("CfgSqlController"), "cancelProjSqlScriptRelation");
		put("/sql/publish/add/post", BuiltinResourceInstance.getInstance("CfgSqlController"), "publish");
		put("/sql/publish/cancel/post", BuiltinResourceInstance.getInstance("CfgSqlController"), "cancelPublish");
		
		// 用户操作
		put("/user/open_account/post", BuiltinResourceInstance.getInstance("SysUserController"), "openAccount");
		put("/user/update_pwd/post", BuiltinResourceInstance.getInstance("SysUserController"), "updatePassword");
		
		// 账户操作
		put("/login/post", BuiltinResourceInstance.getInstance("SysAccountController"), "login");
		put("/login_out/post", BuiltinResourceInstance.getInstance("SysAccountController"), "loginOut");
		put("/account/update_pwd/post", BuiltinResourceInstance.getInstance("SysAccountController"), "updatePassword");
		
		// 权限操作
		put("/permission/get", BuiltinResourceInstance.getInstance("SysPermissionController"), "calcPermissionByCode");
		
		// 监听hibernate类元数据
		put("/hibernate_classmetadata/monitor/get", BuiltinResourceInstance.getInstance("SystemToolsController"), "monitorHibernateClassMetadata");
		// 获取指定资源信息
		put("/resource_info/get/get", BuiltinResourceInstance.getInstance("SystemToolsController"), "getResourceInfo");
		
		// 消息推送
		put("/message/push/post", BuiltinResourceInstance.getInstance("SysPushMessageInfoController"), "pushMessage");
		// 消息阅读
		put("/message/read/get", BuiltinResourceInstance.getInstance("SysPushMessageInfoController"), "readMessage");
		// 修改消息的阅读状态
		put("/message/read_status/update/put", BuiltinResourceInstance.getInstance("SysPushMessageInfoController"), "updateMessageReadStatus");
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>例如：ComTabledata post就是添加表的操作</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("CfgDatabase_post", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "add");
		put("CfgDatabase_put", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "update");
		put("CfgDatabase_delete", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "delete");
		put("/database/add/post", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "add");
		put("/database/update/put", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "update");
		put("/database/delete/delete", BuiltinResourceInstance.getInstance("CfgDatabaseController"), "delete");
		
		// 项目操作
		put("ComProject_post", BuiltinResourceInstance.getInstance("CfgProjectController"), "add");
		put("ComProject_put", BuiltinResourceInstance.getInstance("CfgProjectController"), "update");
		put("ComProject_delete", BuiltinResourceInstance.getInstance("CfgProjectController"), "delete");
		put("/project/add/post", BuiltinResourceInstance.getInstance("CfgProjectController"), "add");
		put("/project/update/put", BuiltinResourceInstance.getInstance("CfgProjectController"), "update");
		put("/project/delete/delete", BuiltinResourceInstance.getInstance("CfgProjectController"), "delete");
		
		// 项目模块操作
		put("ComProjectModule_post", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "add");
		put("ComProjectModule_put", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "update");
		put("ComProjectModule_delete", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "delete");
		put("/project_module/add/post", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "add");
		put("/project_module/update/put", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "update");
		put("/project_module/delete/delete", BuiltinResourceInstance.getInstance("CfgProjectModuleController"), "delete");
		
		// 表操作
		put("ComTabledata_post", BuiltinResourceInstance.getInstance("CfgTableController"), "add");
		put("ComTabledata_put", BuiltinResourceInstance.getInstance("CfgTableController"), "update");
		put("ComTabledata_delete", BuiltinResourceInstance.getInstance("CfgTableController"), "delete");
		put("/table/add/post", BuiltinResourceInstance.getInstance("CfgTableController"), "add");
		put("/table/update/put", BuiltinResourceInstance.getInstance("CfgTableController"), "update");
		put("/table/delete/delete", BuiltinResourceInstance.getInstance("CfgTableController"), "delete");
		
		// 列操作
		put("ComColumndata_post", BuiltinResourceInstance.getInstance("CfgColumnController"), "add");
		put("ComColumndata_put", BuiltinResourceInstance.getInstance("CfgColumnController"), "update");
		put("ComColumndata_delete", BuiltinResourceInstance.getInstance("CfgColumnController"), "delete");
		put("/column/add/post", BuiltinResourceInstance.getInstance("CfgColumnController"), "add");
		put("/column/update/put", BuiltinResourceInstance.getInstance("CfgColumnController"), "update");
		put("/column/delete/delete", BuiltinResourceInstance.getInstance("CfgColumnController"), "delete");
		
		// sql脚本操作
		put("ComSqlScript_post", BuiltinResourceInstance.getInstance("CfgSqlController"), "add");
		put("ComSqlScript_put", BuiltinResourceInstance.getInstance("CfgSqlController"), "update");
		put("ComSqlScript_delete", BuiltinResourceInstance.getInstance("CfgSqlController"), "delete");
		put("/sql/add/post", BuiltinResourceInstance.getInstance("CfgSqlController"), "add");
		put("/sql/update/put", BuiltinResourceInstance.getInstance("CfgSqlController"), "update");
		put("/sql/delete/delete", BuiltinResourceInstance.getInstance("CfgSqlController"), "delete");
		
		// sql脚本参数操作
		put("ComSqlScriptParameter_post", BuiltinResourceInstance.getInstance("CfgSqlParameterController"), "add");
		put("ComSqlScriptParameter_put", BuiltinResourceInstance.getInstance("CfgSqlParameterController"), "update");
		put("ComSqlScriptParameter_delete", BuiltinResourceInstance.getInstance("CfgSqlParameterController"), "delete");
		put("/sql_parameter/add/post", BuiltinResourceInstance.getInstance("CfgSqlParameterController"), "add");
		put("/sql_parameter/update/put", BuiltinResourceInstance.getInstance("CfgSqlParameterController"), "update");
		put("/sql_parameter/delete/delete", BuiltinResourceInstance.getInstance("CfgSqlParameterController"), "delete");
		
		// 用户操作
		put("SysUser_post", BuiltinResourceInstance.getInstance("SysUserController"), "add");
		put("SysUser_put", BuiltinResourceInstance.getInstance("SysUserController"), "update");
		put("SysUser_delete", BuiltinResourceInstance.getInstance("SysUserController"), "delete");
		put("/user/add/post", BuiltinResourceInstance.getInstance("SysUserController"), "add");
		put("/user/update/put", BuiltinResourceInstance.getInstance("SysUserController"), "update");
		put("/user/delete/delete", BuiltinResourceInstance.getInstance("SysUserController"), "delete");
		
		// 账户操作
		put("SysAccount_post", BuiltinResourceInstance.getInstance("SysAccountController"), "add");
		put("SysAccount_put", BuiltinResourceInstance.getInstance("SysAccountController"), "update");
		put("SysAccount_delete", BuiltinResourceInstance.getInstance("SysAccountController"), "delete");
		put("/account/add/post", BuiltinResourceInstance.getInstance("SysAccountController"), "add");
		put("/account/update/put", BuiltinResourceInstance.getInstance("SysAccountController"), "update");
		put("/account/delete/delete", BuiltinResourceInstance.getInstance("SysAccountController"), "delete");
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
