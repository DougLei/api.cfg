package com.king.tooth.cache;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.entity.CodeResourceEntity;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.util.Log4jUtil;

/**
 * 代码资源映射
 * @author DougLei
 */
public class CodeResourceMapping {
	private static final Map<String, CodeResourceEntity> codeResourceMapping = new HashMap<String, CodeResourceEntity>(); 
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
		put("CfgDatabase_post_testLink", BuiltinInstance.databaseController, "testLink");
		put("CfgDatabase_post_publish", BuiltinInstance.databaseController, "publish");
		put("CfgDatabase_post_cancelPublish", BuiltinInstance.databaseController, "cancelPublish");
		put("/database/test_link/post", BuiltinInstance.databaseController, "testLink");
		put("/database/publish/add/post", BuiltinInstance.databaseController, "publish");
		put("/database/publish/cancel/post", BuiltinInstance.databaseController, "cancelPublish");
		
		// 项目操作
		put("ComProject_post_publish", BuiltinInstance.projectController, "publish");
		put("ComProject_post_cancelPublish", BuiltinInstance.projectController, "cancelPublish");
		put("/project/publish/add/post", BuiltinInstance.projectController, "publish");
		put("/project/publish/cancel/post", BuiltinInstance.projectController, "cancelPublish");
		
		// 项目模块操作
		put("ComProjectModule_post_publish", BuiltinInstance.projectModuleController, "publish");
		put("ComProjectModule_post_cancelPublish", BuiltinInstance.projectModuleController, "cancelPublish");
		put("/project_module/publish/add/post", BuiltinInstance.projectModuleController, "publish");
		put("/project_module/publish/cancel/post", BuiltinInstance.projectModuleController, "cancelPublish");
		
		// 表操作
		put("ComTabledata_post_buildModel", BuiltinInstance.tabledataController, "buildModel");
		put("ComTabledata_post_addProjTableRelation", BuiltinInstance.tabledataController, "addProjTableRelation");
		put("ComTabledata_post_cancelProjTableRelation", BuiltinInstance.tabledataController, "cancelProjTableRelation");
		put("ComTabledata_post_publish", BuiltinInstance.tabledataController, "publish");
		put("ComTabledata_post_cancelPublish", BuiltinInstance.tabledataController, "cancelPublish");
		put("/table/build_model/post", BuiltinInstance.tabledataController, "buildModel");
		put("/table/publish/add/post", BuiltinInstance.tabledataController, "publish");
		put("/table/publish/cancel/post", BuiltinInstance.tabledataController, "cancelPublish");
		put("/project/table/relation/add/post", BuiltinInstance.tabledataController, "addProjTableRelation");
		put("/project/table/relation/cancel/post", BuiltinInstance.tabledataController, "cancelProjTableRelation");
		
		// sql脚本操作
		put("ComSqlScript_post_addProjSqlScriptRelation", BuiltinInstance.sqlController, "addProjSqlScriptRelation");
		put("ComSqlScript_post_cancelProjSqlScriptRelation", BuiltinInstance.sqlController, "cancelProjSqlScriptRelation");
		put("ComSqlScript_post_publish", BuiltinInstance.sqlController, "publish");
		put("ComSqlScript_post_cancelPublish", BuiltinInstance.sqlController, "cancelPublish");
		put("/project/sql/relation/add/post", BuiltinInstance.sqlController, "addProjSqlScriptRelation");
		put("/project/sql/relation/cancel/post", BuiltinInstance.sqlController, "cancelProjSqlScriptRelation");
		put("/sql/publish/add/post", BuiltinInstance.sqlController, "publish");
		put("/sql/publish/cancel/post", BuiltinInstance.sqlController, "cancelPublish");
		
		// 用户操作
		put("SysUser_post_openAccount", BuiltinInstance.userController, "openAccount");
		put("SysUser_post_updatePassword", BuiltinInstance.userController, "updatePassword");
		put("/user/open_account/post", BuiltinInstance.userController, "openAccount");
		put("/user/update_pwd/post", BuiltinInstance.userController, "updatePassword");
		
		// 账户操作
		put("SysAccount_post_login", BuiltinInstance.accountController, "login");
		put("SysAccount_post_loginOut", BuiltinInstance.accountController, "loginOut");
		put("SysAccount_post_updatePassword", BuiltinInstance.accountController, "updatePassword");
		put("/login/post", BuiltinInstance.accountController, "login");
		put("/login_out/post", BuiltinInstance.accountController, "loginOut");
		put("/account/update_pwd/post", BuiltinInstance.accountController, "updatePassword");
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>例如：ComTabledata post就是添加表的操作</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("CfgDatabase_post", BuiltinInstance.databaseController, "add");
		put("CfgDatabase_put", BuiltinInstance.databaseController, "update");
		put("CfgDatabase_delete", BuiltinInstance.databaseController, "delete");
		put("/database/add/post", BuiltinInstance.databaseController, "add");
		put("/database/update/put", BuiltinInstance.databaseController, "update");
		put("/database/delete/delete", BuiltinInstance.databaseController, "delete");
		
		// 项目操作
		put("ComProject_post", BuiltinInstance.projectController, "add");
		put("ComProject_put", BuiltinInstance.projectController, "update");
		put("ComProject_delete", BuiltinInstance.projectController, "delete");
		put("/project/add/post", BuiltinInstance.projectController, "add");
		put("/project/update/put", BuiltinInstance.projectController, "update");
		put("/project/delete/delete", BuiltinInstance.projectController, "delete");
		
		// 项目模块操作
		put("ComProjectModule_post", BuiltinInstance.projectModuleController, "add");
		put("ComProjectModule_put", BuiltinInstance.projectModuleController, "update");
		put("ComProjectModule_delete", BuiltinInstance.projectModuleController, "delete");
		put("/project_module/add/post", BuiltinInstance.projectModuleController, "add");
		put("/project_module/update/put", BuiltinInstance.projectModuleController, "update");
		put("/project_module/delete/delete", BuiltinInstance.projectModuleController, "delete");
		
		// 表操作
		put("ComTabledata_post", BuiltinInstance.tabledataController, "add");
		put("ComTabledata_put", BuiltinInstance.tabledataController, "update");
		put("ComTabledata_delete", BuiltinInstance.tabledataController, "delete");
		put("/table/add/post", BuiltinInstance.tabledataController, "add");
		put("/table/update/put", BuiltinInstance.tabledataController, "update");
		put("/table/delete/delete", BuiltinInstance.tabledataController, "delete");
		
		// 列操作
		put("ComColumndata_post", BuiltinInstance.columndataController, "add");
		put("ComColumndata_put", BuiltinInstance.columndataController, "update");
		put("ComColumndata_delete", BuiltinInstance.columndataController, "delete");
		put("/column/add/post", BuiltinInstance.columndataController, "add");
		put("/column/update/put", BuiltinInstance.columndataController, "update");
		put("/column/delete/delete", BuiltinInstance.columndataController, "delete");
		
		// sql脚本操作
		put("ComSqlScript_post", BuiltinInstance.sqlController, "add");
		put("ComSqlScript_put", BuiltinInstance.sqlController, "update");
		put("ComSqlScript_delete", BuiltinInstance.sqlController, "delete");
		put("/sql/add/post", BuiltinInstance.sqlController, "add");
		put("/sql/update/put", BuiltinInstance.sqlController, "update");
		put("/sql/delete/delete", BuiltinInstance.sqlController, "delete");
		
		// sql脚本参数操作
		put("ComSqlScriptParameter_post", BuiltinInstance.sqlParamController, "add");
		put("ComSqlScriptParameter_put", BuiltinInstance.sqlParamController, "update");
		put("ComSqlScriptParameter_delete", BuiltinInstance.sqlParamController, "delete");
		put("/sql_parameter/add/post", BuiltinInstance.sqlParamController, "add");
		put("/sql_parameter/update/put", BuiltinInstance.sqlParamController, "update");
		put("/sql_parameter/delete/delete", BuiltinInstance.sqlParamController, "delete");
		
		// 用户操作
		put("SysUser_post", BuiltinInstance.userController, "add");
		put("SysUser_put", BuiltinInstance.userController, "update");
		put("SysUser_delete", BuiltinInstance.userController, "delete");
		put("/user/add/post", BuiltinInstance.userController, "add");
		put("/user/update/put", BuiltinInstance.userController, "update");
		put("/user/delete/delete", BuiltinInstance.userController, "delete");
		
		// 账户操作
		put("SysAccount_post", BuiltinInstance.accountController, "add");
		put("SysAccount_put", BuiltinInstance.accountController, "update");
		put("SysAccount_delete", BuiltinInstance.accountController, "delete");
		put("/account/add/post", BuiltinInstance.accountController, "add");
		put("/account/update/put", BuiltinInstance.accountController, "update");
		put("/account/delete/delete", BuiltinInstance.accountController, "delete");
	}
	

	/**
	 * 初始化系统内置查询条件函数配置
	 */
	public static void initBuiltinQueryCondFuncConfig(){
		if(unInitCodeResourceMapping){
			intCodeResource();
			unInitCodeResourceMapping = false;
		}
	}

	/**
	 * 获取代码资源的key值
	 * @param resourceName
	 * @param reqMethod
	 * @param actionName
	 * @return
	 */
	public static String getCodeResourceKey(String resourceName, String reqMethod, String actionName){
		if(resourceName.indexOf("/") != -1){
			return resourceName;
		}
		return resourceName.toLowerCase() + "_" + reqMethod + (actionName == null?"":"_"+actionName.toLowerCase());
	}
	
	/**
	 * 判断是否是代码资源类型
	 * @param codeResourceKey
	 * @return
	 */
	public static boolean isCodeResource(String codeResourceKey){
		return codeResourceMapping.containsKey(codeResourceKey);
	}
	
	/**
	 * 调用代码资源
	 * <p>在调用前，最好先调用isCodeResource()方法，判断是否是代码资源，结果为true再调用该方法，防止出错</p>
	 * @param codeResourceKey
	 * @param request
	 * @param ijson
	 * @param urlParams
	 * @return 
	 */
	public static Object invokeCodeResource(String codeResourceKey, HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		CodeResourceEntity codeResource = codeResourceMapping.get(codeResourceKey);
		if(codeResource == null){
			return "没有找到codeResourceKey值为["+codeResourceKey+"]的代码资源对象实例";
		}
		Log4jUtil.debug(" ========================> 此次请求调用的代码资源key为：{}", codeResourceKey);
		
		Object object = codeResource.invokeMethodForCodeResource(request, ijson, urlParams);
		if(object == null){
			return "系统在调用codeResourceKey为["+codeResourceKey+"]的代码资源时，返回的结果为null，请联系开发人员";
		}
		return object;
	}
}
