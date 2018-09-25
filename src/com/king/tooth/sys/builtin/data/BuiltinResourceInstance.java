package com.king.tooth.sys.builtin.data;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.sys.controller.cfg.CfgDatabaseController;
import com.king.tooth.sys.controller.cfg.CfgColumnController;
import com.king.tooth.sys.controller.cfg.CfgProjectController;
import com.king.tooth.sys.controller.cfg.CfgProjectModuleController;
import com.king.tooth.sys.controller.cfg.CfgSqlController;
import com.king.tooth.sys.controller.cfg.CfgSqlParameterController;
import com.king.tooth.sys.controller.cfg.CfgTableController;
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
import com.king.tooth.sys.entity.sys.datalinks.SysUserRoleLinks;
import com.king.tooth.sys.service.cfg.CfgDatabaseService;
import com.king.tooth.sys.service.cfg.CfgColumnService;
import com.king.tooth.sys.service.cfg.CfgProjectModuleService;
import com.king.tooth.sys.service.cfg.CfgProjectService;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.sys.service.cfg.CfgTableService;
import com.king.tooth.sys.service.dm.DmPublishInfoService;
import com.king.tooth.sys.service.other.SystemToolsService;
import com.king.tooth.sys.service.sys.SysAccountOnlineStatusService;
import com.king.tooth.sys.service.sys.SysAccountService;
import com.king.tooth.sys.service.sys.SysFileService;
import com.king.tooth.sys.service.sys.SysPermissionService;
import com.king.tooth.sys.service.sys.SysPushMessageInfoService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.sys.service.sys.SysUserService;

/**
 * 系统内置的资源实例
 * @author DougLei
 */
public class BuiltinResourceInstance {
	
	/**
	 * controller类的map缓存
	 */
	private static final Map<String, Object> controllerCache = new HashMap<String, Object>(12); 
	static{
		controllerCache.put("CfgTableController", new CfgTableController());
		controllerCache.put("CfgColumnController", new CfgColumnController());
		controllerCache.put("CfgDatabaseController", new CfgDatabaseController());
		controllerCache.put("CfgProjectController", new CfgProjectController());
		controllerCache.put("CfgProjectModuleController", new CfgProjectModuleController());
		controllerCache.put("CfgSqlController", new CfgSqlController());
		controllerCache.put("CfgSqlParameterController", new CfgSqlParameterController());
		controllerCache.put("SysAccountController", new SysAccountController());
		controllerCache.put("SysUserController", new SysUserController());
		controllerCache.put("SysPermissionController", new SysPermissionController());
		controllerCache.put("SystemToolsController", new SystemToolsController());
		controllerCache.put("SysPushMessageInfoController", new SysPushMessageInfoController());
	}
	
	/**
	 * 获得controller实例
	 * @param name
	 * @return
	 */
	public static Object getControllerInstance(String name){
		return controllerCache.get(name);
	}
	/**
	 * 获得controller实例
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getControllerInstance(String name, Class<T> clz){
		return (T) controllerCache.get(name);
	}
	
	// --------------------------------------------------------------------
	/**
	 * service层类的map缓存
	 */
	private static final Map<String, Object> serviceCache = new HashMap<String, Object>(15); 
	static{
		serviceCache.put("CfgTableService", new CfgTableService());
		serviceCache.put("CfgColumnService", new CfgColumnService());
		serviceCache.put("CfgDatabaseService", new CfgDatabaseService());
		serviceCache.put("CfgProjectService", new CfgProjectService());
		serviceCache.put("CfgProjectModuleService", new CfgProjectModuleService());
		serviceCache.put("CfgSqlService", new CfgSqlService());
		serviceCache.put("DmPublishInfoService", new DmPublishInfoService());
		serviceCache.put("SysAccountOnlineStatusService", new SysAccountOnlineStatusService());
		serviceCache.put("SysAccountService", new SysAccountService());
		serviceCache.put("SysResourceService", new SysResourceService());
		serviceCache.put("SysUserService", new SysUserService());
		serviceCache.put("SysPermissionService", new SysPermissionService());
		serviceCache.put("SysFileService", new SysFileService());
		serviceCache.put("SystemToolsService", new SystemToolsService());
		serviceCache.put("SysPushMessageInfoService", new SysPushMessageInfoService());
	}
	
	/**
	 * 获得service实例
	 * @param name
	 * @return
	 */
	public static Object getServiceInstance(String name){
		return serviceCache.get(name);
	}
	/**
	 * 获得service实例
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getServiceInstance(String name, Class<T> clz){
		return (T) serviceCache.get(name);
	}
	
	// --------------------------------------------------------------------
	/**
	 * entity类的map缓存
	 */
	private static final Map<String, Object> entityCache = new HashMap<String, Object>(39); 
	static{
		entityCache.put("CfgDatabase", new CfgDatabase());
		entityCache.put("CfgTable", new ComTabledata());
		entityCache.put("CfgColumn", new ComColumndata());
		entityCache.put("CfgProject", new ComProject());
		entityCache.put("CfgProjectModule", new ComProjectModule());
		entityCache.put("CfgSql", new ComSqlScript());
		entityCache.put("CfgSqlParameter", new ComSqlScriptParameter());
		entityCache.put("CfgSqlResultset", new CfgSqlResultset());
		entityCache.put("CfgColumnCodeRule", new CfgColumnCodeRule());
		entityCache.put("CfgColumnCodeRuleDetail", new CfgColumnCodeRuleDetail());
		entityCache.put("CfgProjectHbmLinks", new CfgProjectHbmLinks());
		entityCache.put("CfgProjectSqlLinks", new CfgProjectSqlLinks());
		entityCache.put("CfgProjectTableLinks", new CfgProjectTableLinks());
		entityCache.put("DmPublishBasicData", new DmPublishBasicData());
		entityCache.put("DmPublishInfo", new DmPublishInfo());
		entityCache.put("SysUser", new SysUser());
		entityCache.put("SysAccount", new SysAccount());
		entityCache.put("SysAccountOnlineStatus", new SysAccountOnlineStatus());
		entityCache.put("SysDataDictionary", new SysDataDictionary());
		entityCache.put("SysOrg", new SysOrg());
		entityCache.put("SysDept", new SysDept());
		entityCache.put("SysPosition", new SysPosition());
		entityCache.put("SysRole", new SysRole());
		entityCache.put("SysPermission", new SysPermission());
		entityCache.put("SysUserPermissionCache", new SysUserPermissionCache());
		entityCache.put("SysPermissionPriority", new SysPermissionPriority());
		entityCache.put("SysReqLog", new SysReqLog());
		entityCache.put("SysOperSqlLog", new SysOperSqlLog());
		entityCache.put("SysResource", new SysResource());
		entityCache.put("SysFile", new SysFile());
		entityCache.put("SysHibernateHbm", new SysHibernateHbm());
		entityCache.put("SysUserGroup", new SysUserGroup());
		entityCache.put("SysUserGroupDetail", new SysUserGroupDetail());
		entityCache.put("SysPushMessageInfo", new SysPushMessageInfo());
		entityCache.put("SysDataPrivS", new SysDataPrivS());
		entityCache.put("SysDataLinks", new SysDataLinks());
		entityCache.put("SysUserRoleLinks", new SysUserRoleLinks());
		entityCache.put("SysUserDeptLinks", new SysUserDeptLinks());
		entityCache.put("SysUserPositionLinks", new SysUserDeptLinks());
	}
	
	/**
	 * 获得entity实例
	 * @param name
	 * @return
	 */
	public static Object getEntityInstance(String name){
		return entityCache.get(name);
	}
	/**
	 * 获得entity实例
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getEntityInstance(String name, Class<T> clz){
		return (T) entityCache.get(name);
	}
}
