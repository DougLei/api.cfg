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
import com.king.tooth.sys.controller.sys.SysPermissionController;
import com.king.tooth.sys.controller.sys.SysPushMessageInfoController;
import com.king.tooth.sys.controller.sys.SysUserController;
import com.king.tooth.sys.controller.tools.SystemToolsController;

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
	
	@SuppressWarnings("rawtypes")
	private static void put(String key, Class clz, String methodName){
		codeResourceMapping.put(key.toLowerCase(), new CodeResourceEntity(clz, methodName));
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
		put("/database/test_link/post", CfgDatabaseController.class, "testLink");
		put("/database/publish/add/post", CfgDatabaseController.class, "publish");
		put("/database/publish/cancel/post", CfgDatabaseController.class, "cancelPublish");
		
		// 项目操作
		put("/project/publish/add/post", CfgProjectController.class, "publish");
		put("/project/publish/cancel/post", CfgProjectController.class, "cancelPublish");
		
		// 项目模块操作
		put("/project_module/publish/add/post", CfgProjectModuleController.class, "publish");
		put("/project_module/publish/cancel/post", CfgProjectModuleController.class, "cancelPublish");
		
		// 表操作
		put("ComTabledata_post_buildModel", CfgTableController.class, "buildModel");
		put("/table/model/create/post", CfgTableController.class, "buildModel");
		put("/table/model/drop/post", CfgTableController.class, "cancelBuildModel");
		put("/table/publish/add/post", CfgTableController.class, "publish");
		put("/table/publish/cancel/post", CfgTableController.class, "cancelPublish");
		put("/project/table/relation/add/post", CfgTableController.class, "addProjTableRelation");
		put("/project/table/relation/cancel/post", CfgTableController.class, "cancelProjTableRelation");
		
		// sql脚本操作
		put("/sql/object/create/post", CfgSqlController.class, "immediateCreate");
		put("/project/sql/relation/add/post", CfgSqlController.class, "addProjSqlScriptRelation");
		put("/project/sql/relation/cancel/post", CfgSqlController.class, "cancelProjSqlScriptRelation");
		put("/sql/publish/add/post", CfgSqlController.class, "publish");
		put("/sql/publish/cancel/post", CfgSqlController.class, "cancelPublish");
		
		// 用户操作
		put("/user/open_account/post", SysUserController.class, "openAccount");
		put("/user/update_pwd/post", SysUserController.class, "updatePassword");
		put("/user/pwd/reset/put", SysUserController.class, "resetPassword");
		put("/user/account/reset/put", SysUserController.class, "resetAccount");
		
		// 账户操作
		put("/login/post", SysAccountController.class, "login");
		put("/login_out/post", SysAccountController.class, "loginOut");
		put("/account/update_pwd/post", SysAccountController.class, "updatePassword");
		put("/account/pwd/reset/put", SysAccountController.class, "resetPassword");
		
		// 权限操作
		put("/permission/get", SysPermissionController.class, "calcPermissionByCode");
		
		// 监听hibernate类元数据
		put("/hibernate_classmetadata/monitor/get", SystemToolsController.class, "monitorHibernateClassMetadata");
		// 获取指定资源信息
		put("/resource_info/get/get", SystemToolsController.class, "getResourceInfo");
		
		// 消息推送
		put("/message/push/post", SysPushMessageInfoController.class, "pushMessage");
		// 消息阅读
		put("/message/read/get", SysPushMessageInfoController.class, "readMessage");
		// 修改消息的阅读状态
		put("/message/read_status/update/put", SysPushMessageInfoController.class, "updateMessageReadStatus");
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>例如：ComTabledata post就是添加表的操作</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("CfgDatabase_post", CfgDatabaseController.class, "add");
		put("CfgDatabase_put", CfgDatabaseController.class, "update");
		put("CfgDatabase_delete", CfgDatabaseController.class, "delete");
		put("/database/add/post", CfgDatabaseController.class, "add");
		put("/database/update/put", CfgDatabaseController.class, "update");
		put("/database/delete/delete", CfgDatabaseController.class, "delete");
		
		// 项目操作
		put("ComProject_post", CfgProjectController.class, "add");
		put("ComProject_put", CfgProjectController.class, "update");
		put("ComProject_delete", CfgProjectController.class, "delete");
		put("/project/add/post", CfgProjectController.class, "add");
		put("/project/update/put", CfgProjectController.class, "update");
		put("/project/delete/delete", CfgProjectController.class, "delete");
		
		// 项目模块操作
		put("ComProjectModule_post", CfgProjectModuleController.class, "add");
		put("ComProjectModule_put", CfgProjectModuleController.class, "update");
		put("ComProjectModule_delete", CfgProjectModuleController.class, "delete");
		put("/project_module/add/post", CfgProjectModuleController.class, "add");
		put("/project_module/update/put", CfgProjectModuleController.class, "update");
		put("/project_module/delete/delete", CfgProjectModuleController.class, "delete");
		
		// 表操作
		put("ComTabledata_post", CfgTableController.class, "add");
		put("ComTabledata_put", CfgTableController.class, "update");
		put("ComTabledata_delete", CfgTableController.class, "delete");
		put("/table/add/post", CfgTableController.class, "add");
		put("/table/update/put", CfgTableController.class, "update");
		put("/table/delete/delete", CfgTableController.class, "delete");
		
		// 列操作
		put("ComColumndata_post", CfgColumnController.class, "add");
		put("ComColumndata_put", CfgColumnController.class, "update");
		put("ComColumndata_delete", CfgColumnController.class, "delete");
		put("/column/add/post", CfgColumnController.class, "add");
		put("/column/update/put", CfgColumnController.class, "update");
		put("/column/delete/delete", CfgColumnController.class, "delete");
		
		// sql脚本操作
		put("ComSqlScript_post", CfgSqlController.class, "add");
		put("ComSqlScript_put", CfgSqlController.class, "update");
		put("ComSqlScript_delete", CfgSqlController.class, "delete");
		put("/sql/add/post", CfgSqlController.class, "add");
		put("/sql/update/put", CfgSqlController.class, "update");
		put("/sql/delete/delete", CfgSqlController.class, "delete");
		
		// sql脚本参数操作
		put("ComSqlScriptParameter_post", CfgSqlParameterController.class, "add");
		put("ComSqlScriptParameter_put", CfgSqlParameterController.class, "update");
		put("ComSqlScriptParameter_delete", CfgSqlParameterController.class, "delete");
		put("/sql_parameter/add/post", CfgSqlParameterController.class, "add");
		put("/sql_parameter/update/put", CfgSqlParameterController.class, "update");
		put("/sql_parameter/delete/delete", CfgSqlParameterController.class, "delete");
		
		// 用户操作
		put("SysUser_post", SysUserController.class, "add");
		put("SysUser_put", SysUserController.class, "update");
		put("SysUser_delete", SysUserController.class, "delete");
		put("/user/add/post", SysUserController.class, "add");
		put("/user/update/put", SysUserController.class, "update");
		put("/user/delete/delete", SysUserController.class, "delete");
		
		// 账户操作
		put("SysAccount_post", SysAccountController.class, "add");
		put("SysAccount_put", SysAccountController.class, "update");
		put("SysAccount_delete", SysAccountController.class, "delete");
		put("/account/add/post", SysAccountController.class, "add");
		put("/account/update/put", SysAccountController.class, "update");
		put("/account/delete/delete", SysAccountController.class, "delete");
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
