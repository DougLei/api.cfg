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
	 * <p>例如：CfgTable post buildModel就是建模的操作，类似的还有发布操作等</p>
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
		put("/excel/export_file/create/post", SysExcelController.class, "createExportExcelFile");// 生成导出excel文件
	}
	
	/**
	 * 初始化一般代码资源
	 * <p>例如：CfgTable post就是添加表的操作</p>
	 */
	private static void initNormalCodeResource() {
		// 数据库操作
		put("/database/add/post", CfgDatabaseController.class, "add");
		put("/database/update/put", CfgDatabaseController.class, "update");
		put("/database/delete/delete", CfgDatabaseController.class, "delete");
		
		// 项目操作
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
		put("/table/add/post", CfgTableController.class, "add");
		put("/table/update/put", CfgTableController.class, "update");
		put("/table/delete/delete", CfgTableController.class, "delete");
		
		// 列操作
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
		put("/sql_parameter/add/post", CfgSqlParameterController.class, "add");
		put("/sql_parameter/update/put", CfgSqlParameterController.class, "update");
		put("/sql_parameter/delete/delete", CfgSqlParameterController.class, "delete");
		
		// 用户操作
		put("/user/add/post", SysUserController.class, "add");
		put("/user/update/put", SysUserController.class, "update");
		put("/user/delete/delete", SysUserController.class, "delete");
		
		// 账户操作
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
