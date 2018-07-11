package com.king.tooth.cache;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.cache.entity.CodeResourceEntity;
import com.king.tooth.sys.controller.cfg.ComColumndataController;
import com.king.tooth.sys.controller.cfg.ComTabledataController;
import com.king.tooth.sys.controller.common.ComDatabaseController;
import com.king.tooth.sys.controller.common.ComModuleOperationController;
import com.king.tooth.sys.controller.common.ComProjectController;
import com.king.tooth.sys.controller.common.ComProjectModuleController;
import com.king.tooth.sys.controller.common.ComSqlScriptController;
import com.king.tooth.sys.controller.common.ComSqlScriptParameterController;
import com.king.tooth.sys.controller.common.ComSysAccountController;
import com.king.tooth.sys.controller.common.ComUserController;
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
	
	@SuppressWarnings("rawtypes")
	private static void put(String key, Class clz, String methodName){
		codeResourceMapping.put(key.toLowerCase(), new CodeResourceEntity(clz, methodName));
	}
	
	/**
	 * 初始化代码资源
	 */
	private static void intCodeResource() {
		// action
		initActionCodeResource();
		// normal
		initNormalCodeResource();
	}
	
	/**
	 * 初始化action代码资源
	 * <p>例如：ComTabledata post buildModel就是建模的操作，类似的还有发布操作等</p>
	 */
	private static void initActionCodeResource() {
		// 账户操作
		put("ComSysAccount_post_login", ComSysAccountController.class, "login");
		put("ComSysAccount_post_loginOut", ComSysAccountController.class, "loginOut");
		// 数据库操作
		put("ComDatabase_post_linkTest", ComTabledataController.class, "linkTest");
		put("ComDatabase_post_publish", ComTabledataController.class, "publish");
		put("ComDatabase_post_cancelPublish", ComTabledataController.class, "cancelPublish");
		// 项目操作
		put("ComProject_post_cancelRelation", ComProjectController.class, "cancelRelation");
		put("ComProject_post_publish", ComProjectController.class, "publish");
		put("ComProject_post_cancelPublish", ComProjectController.class, "cancelPublish");
		// 项目模块操作
		put("ComProjectModule_post_publish", ComProjectModuleController.class, "publish");
		put("ComProjectModule_post_cancelPublish", ComProjectModuleController.class, "cancelPublish");
		// 模块功能操作
		put("ComModuleOperation_post_publish", ComModuleOperationController.class, "publish");
		put("ComModuleOperation_post_cancelPublish", ComModuleOperationController.class, "cancelPublish");
		// 表操作
		put("ComTabledata_post_buildModel", ComTabledataController.class, "buildModel");
		put("ComTabledata_post_cancelBuildModel", ComTabledataController.class, "cancelBuildModel");
		put("ComTabledata_post_addProjTableRelation", ComTabledataController.class, "addProjTableRelation");
		put("ComTabledata_post_cancelProjTableRelation", ComTabledataController.class, "cancelProjTableRelation");
		put("ComTabledata_post_publish", ComTabledataController.class, "publish");
		put("ComTabledata_post_cancelPublish", ComTabledataController.class, "cancelPublish");
		// sql脚本操作
		put("ComSqlScript_post_addProjSqlScriptRelation", ComSqlScriptController.class, "addProjSqlScriptRelation");
		put("ComSqlScript_post_cancelProjSqlScriptRelation", ComSqlScriptController.class, "cancelProjSqlScriptRelation");
		put("ComSqlScript_post_publish", ComSqlScriptController.class, "publish");
		put("ComSqlScript_post_cancelPublish", ComSqlScriptController.class, "cancelPublish");
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>例如：ComTabledata post就是添加表的操作</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("ComDatabase_post", ComDatabaseController.class, "add");
		put("ComDatabase_put", ComDatabaseController.class, "update");
		put("ComDatabase_delete", ComDatabaseController.class, "delete");
		// 项目操作
		put("ComProject_post", ComProjectController.class, "add");
		put("ComProject_put", ComProjectController.class, "update");
		put("ComProject_delete", ComProjectController.class, "delete");
		// 项目模块操作
		put("ComProjectModule_post", ComProjectModuleController.class, "add");
		put("ComProjectModule_put", ComProjectModuleController.class, "update");
		put("ComProjectModule_delete", ComProjectModuleController.class, "delete");
		// 模块功能操作
		put("ComModuleOperation_post", ComModuleOperationController.class, "add");
		put("ComModuleOperation_put", ComModuleOperationController.class, "update");
		put("ComModuleOperation_delete", ComModuleOperationController.class, "delete");
		// 表操作
		put("ComTabledata_post", ComTabledataController.class, "add");
		put("ComTabledata_put", ComTabledataController.class, "update");
		put("ComTabledata_delete", ComTabledataController.class, "delete");
		// 列操作
		put("ComColumndata_post", ComColumndataController.class, "add");
		put("ComColumndata_put", ComColumndataController.class, "update");
		put("ComColumndata_delete", ComColumndataController.class, "delete");
		// sql脚本操作
		put("ComSqlScript_post", ComSqlScriptController.class, "add");
		put("ComSqlScript_put", ComSqlScriptController.class, "update");
		put("ComSqlScript_delete", ComSqlScriptController.class, "delete");
		// sql脚本参数操作
		put("ComSqlScriptParameter_post", ComSqlScriptParameterController.class, "add");
		put("ComSqlScriptParameter_put", ComSqlScriptParameterController.class, "update");
		put("ComSqlScriptParameter_delete", ComSqlScriptParameterController.class, "delete");
		
		
		// 用户操作
		put("ComComUser_post", ComUserController.class, "add");
		put("ComComUser_put", ComUserController.class, "update");
		put("ComComUser_delete", ComUserController.class, "delete");
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
	 * @param formData
	 * @return 
	 */
	public static Object invokeCodeResource(String codeResourceKey, HttpServletRequest request, Object formData){
		CodeResourceEntity codeResource = codeResourceMapping.get(codeResourceKey);
		if(codeResource == null){
			Log4jUtil.warn("没有找到codeResourceKey值为["+codeResourceKey+"]的代码资源对象实例");
			return null;
		}
		Log4jUtil.debug(" ========================> 此次请求调用的代码资源key为：{}", codeResourceKey);
		if(formData == null){
			formData = "";
		}
		Object object = codeResource.invokeMethodForCodeResource(request, formData.toString());
		if(object == null){
			throw new NullPointerException("系统在调用codeResourceKey为["+codeResourceKey+"]的代码资源时，返回的结果为null，请联系开发人员");
		}
		return object;
	}
}
