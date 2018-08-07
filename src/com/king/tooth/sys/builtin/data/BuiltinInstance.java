package com.king.tooth.sys.builtin.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.sys.controller.cfg.ComColumndataController;
import com.king.tooth.sys.controller.cfg.CfgDatabaseController;
import com.king.tooth.sys.controller.cfg.ComProjectController;
import com.king.tooth.sys.controller.cfg.ComProjectModuleController;
import com.king.tooth.sys.controller.cfg.ComSqlScriptController;
import com.king.tooth.sys.controller.cfg.ComSqlScriptParameterController;
import com.king.tooth.sys.controller.cfg.ComTabledataController;
import com.king.tooth.sys.controller.sys.SysAccountController;
import com.king.tooth.sys.controller.sys.SysUserController;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.service.cfg.ComColumndataService;
import com.king.tooth.sys.service.cfg.CfgDatabaseService;
import com.king.tooth.sys.service.cfg.ComProjectModuleService;
import com.king.tooth.sys.service.cfg.ComProjectService;
import com.king.tooth.sys.service.cfg.ComSqlScriptService;
import com.king.tooth.sys.service.cfg.ComTabledataService;
import com.king.tooth.sys.service.dm.DmPublishInfoService;
import com.king.tooth.sys.service.sys.SysPermissionService;
import com.king.tooth.sys.service.sys.SysAccountOnlineStatusService;
import com.king.tooth.sys.service.sys.SysAccountService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.sys.service.sys.SysUserService;
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
	public transient static final CfgDatabase currentSysBuiltinDatabaseInstance = new CfgDatabase();
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
	public static final List<SysPermissionPriority> permissionPriorities = new ArrayList<SysPermissionPriority>(4); 
	static{
		permissionPriorities.add(new SysPermissionPriority(BuiltinPermissionType.ACCOUNT, 4));
		permissionPriorities.add(new SysPermissionPriority(BuiltinPermissionType.ROLE, 3));
		permissionPriorities.add(new SysPermissionPriority(BuiltinPermissionType.DEPT, 2));
		permissionPriorities.add(new SysPermissionPriority(BuiltinPermissionType.POSITION, 1));
	}
	
	//---------------------------------------------------------
	//controller
	public static final ComTabledataController tabledataController = new ComTabledataController();
	public static final ComColumndataController columndataController = new ComColumndataController();
	public static final CfgDatabaseController databaseController = new CfgDatabaseController();
	public static final ComProjectController projectController = new ComProjectController();
	public static final ComProjectModuleController projectModuleController = new ComProjectModuleController();
	public static final ComSqlScriptController sqlController = new ComSqlScriptController();
	public static final ComSqlScriptParameterController sqlParamController = new ComSqlScriptParameterController();
	public static final SysAccountController accountController = new SysAccountController();
	public static final SysUserController userController = new SysUserController();
	
	//service
	public static final ComColumndataService columndataService = new ComColumndataService();
	public static final DmPublishInfoService publishInfoService = new DmPublishInfoService();
	public static final ComTabledataService tabledataService = new ComTabledataService();
	public static final CfgDatabaseService databaseService = new CfgDatabaseService();
	public static final ComProjectService projectService = new ComProjectService();
	public static final ComProjectModuleService projectModuleService = new ComProjectModuleService();
	public static final ComSqlScriptService sqlScriptService = new ComSqlScriptService();
	public static final SysAccountOnlineStatusService accountOnlineStatusService = new SysAccountOnlineStatusService();
	public static final SysAccountService accountService = new SysAccountService();
	public static final SysResourceService resourceService = new SysResourceService();
	public static final SysUserService userService = new SysUserService();
	public static final SysPermissionService permissionService = new SysPermissionService();
	public static final SysFileService fileService = new SysFileService();
	
}
