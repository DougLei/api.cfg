package com.king.tooth.sys.builtin.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.controller.app.ComDeptController;
import com.king.tooth.sys.controller.cfg.ComColumndataController;
import com.king.tooth.sys.controller.cfg.ComTabledataController;
import com.king.tooth.sys.controller.common.ComDatabaseController;
import com.king.tooth.sys.controller.common.ComProjectController;
import com.king.tooth.sys.controller.common.ComProjectModuleController;
import com.king.tooth.sys.controller.common.ComSqlScriptController;
import com.king.tooth.sys.controller.common.ComSqlScriptParameterController;
import com.king.tooth.sys.controller.common.ComSysAccountController;
import com.king.tooth.sys.controller.common.ComUserController;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComPermissionPriority;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.service.app.ComDeptService;
import com.king.tooth.sys.service.cfg.ComColumndataService;
import com.king.tooth.sys.service.cfg.ComPublishInfoService;
import com.king.tooth.sys.service.cfg.ComTabledataService;
import com.king.tooth.sys.service.common.ComDatabaseService;
import com.king.tooth.sys.service.common.ComPermissionService;
import com.king.tooth.sys.service.common.ComProjectModuleService;
import com.king.tooth.sys.service.common.ComProjectService;
import com.king.tooth.sys.service.common.ComReqLogService;
import com.king.tooth.sys.service.common.ComSqlScriptService;
import com.king.tooth.sys.service.common.ComSysAccountOnlineStatusService;
import com.king.tooth.sys.service.common.ComSysAccountService;
import com.king.tooth.sys.service.common.ComSysResourceService;
import com.king.tooth.sys.service.common.ComUserService;
import com.king.tooth.sys.service.sys.SysFileService;
import com.king.tooth.util.DateUtil;

/**
 * 系统内置的对象实例
 * @author DougLei
 */
public class BuiltinInstance {
	
	/**
	 * 当前系统的数据库对象实例
	 */
	public transient static final ComDatabase currentSysBuiltinDatabaseInstance = new ComDatabase();
	static{
		currentSysBuiltinDatabaseInstance.setId(SysConfig.getSystemConfig("current.sys.database.id"));
		currentSysBuiltinDatabaseInstance.setDbType(SysConfig.getSystemConfig("jdbc.dbType"));
		currentSysBuiltinDatabaseInstance.setDbInstanceName(SysConfig.getSystemConfig("db.default.instancename"));
		currentSysBuiltinDatabaseInstance.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
		currentSysBuiltinDatabaseInstance.setLoginPassword(SysConfig.getSystemConfig("jdbc.password"));
		currentSysBuiltinDatabaseInstance.setDbIp(SysConfig.getSystemConfig("db.default.ip"));
		currentSysBuiltinDatabaseInstance.setDbPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
	}
	
	/**
	 * 当前系统项目对象实例
	 */
	public transient static final ComProject currentSysBuiltinProjectInstance = new ComProject(); 
	static{
		currentSysBuiltinProjectInstance.setId(SysConfig.getSystemConfig("current.sys.project.id"));
	}
	
	/**
	 * 数据的有效期
	 */
	public transient static final Date validDate = DateUtil.parseDate("2099-12-31 23:59:59");
	
	//---------------------------------------------------------
	/**
	 * 权限优先级集合
	 */
	public static final List<ComPermissionPriority> permissionPriorities = new ArrayList<ComPermissionPriority>(4); 
	static{
		permissionPriorities.add(new ComPermissionPriority(BuiltinPermissionType.ACCOUNT, 4));
		permissionPriorities.add(new ComPermissionPriority(BuiltinPermissionType.ROLE, 3));
		permissionPriorities.add(new ComPermissionPriority(BuiltinPermissionType.DEPT, 2));
		permissionPriorities.add(new ComPermissionPriority(BuiltinPermissionType.POSITION, 1));
	}
	
	//---------------------------------------------------------
	//controller
	public static final ComDeptController deptController = new ComDeptController();
	public static final ComTabledataController tabledataController = new ComTabledataController();
	public static final ComColumndataController columndataController = new ComColumndataController();
	public static final ComDatabaseController databaseController = new ComDatabaseController();
	public static final ComProjectController projectController = new ComProjectController();
	public static final ComProjectModuleController projectModuleController = new ComProjectModuleController();
	public static final ComSqlScriptController sqlController = new ComSqlScriptController();
	public static final ComSqlScriptParameterController sqlParamController = new ComSqlScriptParameterController();
	public static final ComSysAccountController accountController = new ComSysAccountController();
	public static final ComUserController userController = new ComUserController();
	
	//service
	public static final ComDeptService deptService = new ComDeptService();
	public static final ComColumndataService columndataService = new ComColumndataService();
	public static final ComPublishInfoService publishInfoService = new ComPublishInfoService();
	public static final ComTabledataService tabledataService = new ComTabledataService();
	public static final ComDatabaseService databaseService = new ComDatabaseService();
	public static final ComProjectService projectService = new ComProjectService();
	public static final ComProjectModuleService projectModuleService = new ComProjectModuleService();
	public static final ComReqLogService reqLogService = new ComReqLogService();
	public static final ComSqlScriptService sqlService = new ComSqlScriptService();
	public static final ComSysAccountOnlineStatusService accountOnlineStatusService = new ComSysAccountOnlineStatusService();
	public static final ComSysAccountService accountService = new ComSysAccountService();
	public static final ComSysResourceService resourceService = new ComSysResourceService();
	public static final ComUserService userService = new ComUserService();
	public static final ComPermissionService permissionService = new ComPermissionService();
	public static final SysFileService fileService = new SysFileService();
	
}
