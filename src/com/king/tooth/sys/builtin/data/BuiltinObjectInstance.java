package com.king.tooth.sys.builtin.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.PermissionConstants;
import com.king.tooth.sys.controller.cfg.CfgDatabaseController;
import com.king.tooth.sys.controller.cfg.ComColumndataController;
import com.king.tooth.sys.controller.cfg.ComProjectController;
import com.king.tooth.sys.controller.cfg.ComProjectModuleController;
import com.king.tooth.sys.controller.cfg.ComSqlScriptController;
import com.king.tooth.sys.controller.cfg.ComSqlScriptParameterController;
import com.king.tooth.sys.controller.cfg.ComTabledataController;
import com.king.tooth.sys.controller.other.SystemToolsController;
import com.king.tooth.sys.controller.sys.SysAccountController;
import com.king.tooth.sys.controller.sys.SysPermissionController;
import com.king.tooth.sys.controller.sys.SysPushMessageInfoController;
import com.king.tooth.sys.controller.sys.SysUserController;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRuleDetail;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComProjectModule;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectHbmLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectSqlLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectTableLinks;
import com.king.tooth.sys.entity.dm.DmPublishBasicData;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysDataDictionary;
import com.king.tooth.sys.entity.sys.SysDataPrivS;
import com.king.tooth.sys.entity.sys.SysDept;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.sys.SysHibernateHbm;
import com.king.tooth.sys.entity.sys.SysOperSqlLog;
import com.king.tooth.sys.entity.sys.SysOrg;
import com.king.tooth.sys.entity.sys.SysPermission;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.entity.sys.SysPosition;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.sys.SysRole;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.SysUserGroup;
import com.king.tooth.sys.entity.sys.SysUserGroupDetail;
import com.king.tooth.sys.entity.sys.SysUserPermissionCache;
import com.king.tooth.sys.entity.sys.datalinks.SysDataLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserDeptLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserPositionLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserRoleLinks;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.sys.service.cfg.CfgDatabaseService;
import com.king.tooth.sys.service.cfg.ComColumndataService;
import com.king.tooth.sys.service.cfg.ComProjectModuleService;
import com.king.tooth.sys.service.cfg.ComProjectService;
import com.king.tooth.sys.service.cfg.ComSqlScriptService;
import com.king.tooth.sys.service.cfg.ComTabledataService;
import com.king.tooth.sys.service.dm.DmPublishInfoService;
import com.king.tooth.sys.service.other.SystemToolsService;
import com.king.tooth.sys.service.sys.SysAccountOnlineStatusService;
import com.king.tooth.sys.service.sys.SysAccountService;
import com.king.tooth.sys.service.sys.SysFileService;
import com.king.tooth.sys.service.sys.SysPermissionService;
import com.king.tooth.sys.service.sys.SysPushMessageInfoService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.sys.service.sys.SysUserService;
import com.king.tooth.util.DateUtil;

/**
 * 系统内置的对象实例
 * @author DougLei
 */
public class BuiltinObjectInstance {
	
	/**
	 * 数据的有效期
	 */
	public transient static final Date validDate = DateUtil.parseDate("2099-12-31 23:59:59");
	
	// -------------------------------------------------------
	/**
	 * 当前系统的数据库对象实例
	 */
	public transient static final CfgDatabase currentSysBuiltinDatabaseInstance = new CfgDatabase();
	static{
		currentSysBuiltinDatabaseInstance.setId(SysConfig.getSystemConfig("current.sys.database.id"));
		currentSysBuiltinDatabaseInstance.setType(SysConfig.getSystemConfig("jdbc.dbType"));
		currentSysBuiltinDatabaseInstance.setInstanceName(SysConfig.getSystemConfig("db.default.instancename"));
		currentSysBuiltinDatabaseInstance.setLoginUserName(SysConfig.getSystemConfig("jdbc.username"));
		currentSysBuiltinDatabaseInstance.setLoginPassword(SysConfig.getSystemConfig("jdbc.password"));
		currentSysBuiltinDatabaseInstance.setIp(SysConfig.getSystemConfig("db.default.ip"));
		currentSysBuiltinDatabaseInstance.setPort(Integer.valueOf(SysConfig.getSystemConfig("db.default.port")));
	}
	
	/**
	 * 当前系统项目对象实例
	 */
	public transient static final ComProject currentSysBuiltinProjectInstance = new ComProject(); 
	static{
		currentSysBuiltinProjectInstance.setId(SysConfig.getSystemConfig("current.sys.project.id"));
	}
	
	// -------------------------------------------------------
	/**
	 * 权限优先级集合
	 */
	public static final List<SysPermissionPriority> permissionPriorities = new ArrayList<SysPermissionPriority>(6); 
	static{
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_USER, 1));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_ACCOUNT, 2));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_ROLE, 3));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_DEPT, 4));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_POSITION, 5));
		permissionPriorities.add(new SysPermissionPriority(PermissionConstants.OBJ_TYPE_USERGROUP, 6));
	}
	
	/**
	 * 所有权限的对象实例
	 */
	public static final SysPermissionExtend allPermission = new SysPermissionExtend();
	static{
		allPermission.setRefResourceCode("ALL");
		allPermission.setRefResourceId("ALL");
		allPermission.setRefResourceType("ALL");
	}
	
	// -------------------------------------------------------
	//controller
	public static final ComTabledataController tableController = new ComTabledataController();
	public static final ComColumndataController columnController = new ComColumndataController();
	public static final CfgDatabaseController databaseController = new CfgDatabaseController();
	public static final ComProjectController projectController = new ComProjectController();
	public static final ComProjectModuleController projectModuleController = new ComProjectModuleController();
	public static final ComSqlScriptController sqlController = new ComSqlScriptController();
	public static final ComSqlScriptParameterController sqlParamController = new ComSqlScriptParameterController();
	public static final SysAccountController accountController = new SysAccountController();
	public static final SysUserController userController = new SysUserController();
	public static final SysPermissionController permissionController = new SysPermissionController();
	public static final SystemToolsController systemToolsController = new SystemToolsController();
	public static final SysPushMessageInfoController sysPushMessageInfoController = new SysPushMessageInfoController();
	
	//service
	public static final ComColumndataService columnService = new ComColumndataService();
	public static final DmPublishInfoService publishInfoService = new DmPublishInfoService();
	public static final ComTabledataService tableService = new ComTabledataService();
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
	public static final SystemToolsService systemToolsService = new SystemToolsService();
	public static final SysPushMessageInfoService sysPushMessageInfoService = new SysPushMessageInfoService();
	
	// -------------------------------------------------------
	/* 内置表对象，用来建表建模用到的 */
	
	// cfg
	public static final CfgDatabase cfgDatabase = new CfgDatabase();
	public static final ComTabledata cfgTable = new ComTabledata();
	public static final ComColumndata cfgColumn = new ComColumndata();
	public static final ComProject cfgProject = new ComProject();
	public static final ComProjectModule cfgProjectModule = new ComProjectModule();
	public static final ComSqlScript cfgSqlScript = new ComSqlScript();
	public static final ComSqlScriptParameter cfgSqlScriptParameter = new ComSqlScriptParameter();
	public static final CfgSqlResultset cfgSqlResultset = new CfgSqlResultset();
	public static final CfgColumnCodeRule cfgColumnCodeRule = new CfgColumnCodeRule();
	public static final CfgColumnCodeRuleDetail cfgColumnCodeRuleDetail = new CfgColumnCodeRuleDetail();
	
	public static final CfgProjectHbmLinks cfgProjectHbmLinks = new CfgProjectHbmLinks();
	public static final CfgProjectSqlLinks cfgProjectSqlLinks = new CfgProjectSqlLinks();
	public static final CfgProjectTableLinks cfgProjectTableLinks = new CfgProjectTableLinks();
	
	// dm
	public static final DmPublishBasicData dmPublishBasicData = new DmPublishBasicData();
	public static final DmPublishInfo dmPublishInfo = new DmPublishInfo();
	
	// sys
	public static final SysUser sysUser = new SysUser();
	public static final SysAccount sysAccount = new SysAccount();
	public static final SysAccountOnlineStatus sysAccountOnlineStatus = new SysAccountOnlineStatus();
	public static final SysDataDictionary sysDataDictionary = new SysDataDictionary();
	public static final SysOrg sysOrg = new SysOrg();
	public static final SysDept sysDept = new SysDept();
	public static final SysPosition sysPosition = new SysPosition();
	public static final SysRole sysRole = new SysRole();
	public static final SysPermission sysPermission = new SysPermission();
	public static final SysUserPermissionCache sysUserPermissionCache = new SysUserPermissionCache();
	public static final SysPermissionPriority sysPermissionPriority = new SysPermissionPriority();
	public static final SysReqLog sysReqLog = new SysReqLog();
	public static final SysOperSqlLog sysOperSqlLog = new SysOperSqlLog();
	public static final SysResource sysResource = new SysResource();
	public static final SysFile sysFile = new SysFile();
	public static final SysHibernateHbm sysHibernateHbm = new SysHibernateHbm();
	public static final SysUserGroup sysUserGroup = new SysUserGroup();
	public static final SysUserGroupDetail sysUserGroupDetail = new SysUserGroupDetail();
	public static final SysPushMessageInfo sysPushMessageInfo = new SysPushMessageInfo();
	public static final SysDataPrivS sysDataPrivS = new SysDataPrivS();
	
	public static final SysDataLinks sysDataLinks = new SysDataLinks();
	public static final SysUserRoleLinks sysUserRoleLinks = new SysUserRoleLinks();
	public static final SysUserDeptLinks sysUserDeptLinks = new SysUserDeptLinks();
	public static final SysUserPositionLinks sysUserPositionLinks = new SysUserPositionLinks();
}
