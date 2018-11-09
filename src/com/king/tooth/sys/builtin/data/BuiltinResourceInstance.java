package com.king.tooth.sys.builtin.data;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.sys.controller.cfg.CfgBusiResModelController;
import com.king.tooth.sys.controller.cfg.CfgBusiResModelRelationsController;
import com.king.tooth.sys.controller.cfg.CfgColumnController;
import com.king.tooth.sys.controller.cfg.CfgDatabaseController;
import com.king.tooth.sys.controller.cfg.CfgProjectController;
import com.king.tooth.sys.controller.cfg.CfgProjectModuleController;
import com.king.tooth.sys.controller.cfg.CfgPropExtendConfController;
import com.king.tooth.sys.controller.cfg.CfgSqlController;
import com.king.tooth.sys.controller.cfg.CfgSqlParameterController;
import com.king.tooth.sys.controller.cfg.CfgTableController;
import com.king.tooth.sys.controller.sys.SysAccountController;
import com.king.tooth.sys.controller.sys.SysExcelController;
import com.king.tooth.sys.controller.sys.SysPermissionController;
import com.king.tooth.sys.controller.sys.SysPushMessageInfoController;
import com.king.tooth.sys.controller.sys.SysUserController;
import com.king.tooth.sys.controller.tools.SystemToolsController;
import com.king.tooth.sys.entity.cfg.CfgBusiResModel;
import com.king.tooth.sys.entity.cfg.CfgBusiResModelRelations;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.CfgHibernateHbm;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRuleDetail;
import com.king.tooth.sys.entity.cfg.CfgPropExtendConf;
import com.king.tooth.sys.entity.cfg.CfgResource;
import com.king.tooth.sys.entity.cfg.CfgSeqInfo;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComProjectModule;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectSqlLinks;
import com.king.tooth.sys.entity.cfg.datalinks.CfgProjectTableLinks;
import com.king.tooth.sys.entity.sys.SysAccount;
import com.king.tooth.sys.entity.sys.SysAccountOnlineStatus;
import com.king.tooth.sys.entity.sys.SysDataDictionary;
import com.king.tooth.sys.entity.sys.SysDataPrivS;
import com.king.tooth.sys.entity.sys.SysDept;
import com.king.tooth.sys.entity.sys.SysFile;
import com.king.tooth.sys.entity.sys.SysFileIELog;
import com.king.tooth.sys.entity.sys.SysOperSqlLog;
import com.king.tooth.sys.entity.sys.SysOrg;
import com.king.tooth.sys.entity.sys.SysPermission;
import com.king.tooth.sys.entity.sys.SysPermissionPriority;
import com.king.tooth.sys.entity.sys.SysPosition;
import com.king.tooth.sys.entity.sys.SysPushMessageInfo;
import com.king.tooth.sys.entity.sys.SysReqLog;
import com.king.tooth.sys.entity.sys.SysRole;
import com.king.tooth.sys.entity.sys.SysUser;
import com.king.tooth.sys.entity.sys.SysUserGroup;
import com.king.tooth.sys.entity.sys.SysUserGroupDetail;
import com.king.tooth.sys.entity.sys.SysUserPermissionCache;
import com.king.tooth.sys.entity.sys.datalinks.SysDataLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserDeptLinks;
import com.king.tooth.sys.entity.sys.datalinks.SysUserRoleLinks;
import com.king.tooth.sys.service.cfg.CfgBusiResModelRelationsService;
import com.king.tooth.sys.service.cfg.CfgBusiResModelService;
import com.king.tooth.sys.service.cfg.CfgColumnService;
import com.king.tooth.sys.service.cfg.CfgDatabaseService;
import com.king.tooth.sys.service.cfg.CfgProjectModuleService;
import com.king.tooth.sys.service.cfg.CfgProjectService;
import com.king.tooth.sys.service.cfg.CfgPropExtendConfService;
import com.king.tooth.sys.service.cfg.CfgResourceService;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.sys.service.cfg.CfgTableService;
import com.king.tooth.sys.service.sys.SysAccountOnlineStatusService;
import com.king.tooth.sys.service.sys.SysAccountService;
import com.king.tooth.sys.service.sys.SysExcelService;
import com.king.tooth.sys.service.sys.SysFileService;
import com.king.tooth.sys.service.sys.SysPermissionService;
import com.king.tooth.sys.service.sys.SysPushMessageInfoService;
import com.king.tooth.sys.service.sys.SysUserService;
import com.king.tooth.sys.service.tools.SystemToolsService;

/**
 * 系统内置的资源实例
 * @author DougLei
 */
public class BuiltinResourceInstance {
	
	/**
	 * 类实例的map缓存
	 */
	private static final Map<String, Object> instanceCache = new HashMap<String, Object>(70); 
	static{
		instanceCache.put("CfgDatabaseController", new CfgDatabaseController());
		instanceCache.put("CfgProjectController", new CfgProjectController());
		instanceCache.put("CfgProjectModuleController", new CfgProjectModuleController());
		instanceCache.put("CfgTableController", new CfgTableController());
		instanceCache.put("CfgColumnController", new CfgColumnController());
		instanceCache.put("CfgSqlController", new CfgSqlController());
		instanceCache.put("CfgSqlParameterController", new CfgSqlParameterController());
		instanceCache.put("SysAccountController", new SysAccountController());
		instanceCache.put("SysUserController", new SysUserController());
		instanceCache.put("SysPermissionController", new SysPermissionController());
		instanceCache.put("SystemToolsController", new SystemToolsController());
		instanceCache.put("SysPushMessageInfoController", new SysPushMessageInfoController());
		instanceCache.put("SysExcelController", new SysExcelController());
		instanceCache.put("CfgPropExtendConfController", new CfgPropExtendConfController());
		instanceCache.put("CfgBusiResModelController", new CfgBusiResModelController());
		instanceCache.put("CfgBusiResModelRelationsController", new CfgBusiResModelRelationsController());
		
		instanceCache.put("CfgTableService", new CfgTableService());
		instanceCache.put("CfgColumnService", new CfgColumnService());
		instanceCache.put("CfgDatabaseService", new CfgDatabaseService());
		instanceCache.put("CfgProjectService", new CfgProjectService());
		instanceCache.put("CfgProjectModuleService", new CfgProjectModuleService());
		instanceCache.put("CfgSqlService", new CfgSqlService());
		instanceCache.put("SysAccountOnlineStatusService", new SysAccountOnlineStatusService());
		instanceCache.put("SysAccountService", new SysAccountService());
		instanceCache.put("CfgResourceService", new CfgResourceService());
		instanceCache.put("SysUserService", new SysUserService());
		instanceCache.put("SysPermissionService", new SysPermissionService());
		instanceCache.put("SysFileService", new SysFileService());
		instanceCache.put("SystemToolsService", new SystemToolsService());
		instanceCache.put("SysPushMessageInfoService", new SysPushMessageInfoService());
		instanceCache.put("SysExcelService", new SysExcelService());
		instanceCache.put("CfgPropExtendConfService", new CfgPropExtendConfService());
		instanceCache.put("CfgBusiResModelService", new CfgBusiResModelService());
		instanceCache.put("CfgBusiResModelRelationsService", new CfgBusiResModelRelationsService());
		
		instanceCache.put("CfgDatabase", new CfgDatabase());
		instanceCache.put("CfgTable", new CfgTable());
		instanceCache.put("CfgColumn", new CfgColumn());
		instanceCache.put("CfgHibernateHbm", new CfgHibernateHbm());
		instanceCache.put("CfgProject", new ComProject());
		instanceCache.put("CfgProjectModule", new ComProjectModule());
		instanceCache.put("CfgSql", new ComSqlScript());
		instanceCache.put("CfgSqlParameter", new ComSqlScriptParameter());
		instanceCache.put("CfgSqlResultset", new CfgSqlResultset());
		instanceCache.put("CfgPropCodeRule", new CfgPropCodeRule());
		instanceCache.put("CfgPropCodeRuleDetail", new CfgPropCodeRuleDetail());
		instanceCache.put("CfgSeqInfo", new CfgSeqInfo());
		instanceCache.put("CfgProjectSqlLinks", new CfgProjectSqlLinks());
		instanceCache.put("CfgProjectTableLinks", new CfgProjectTableLinks());
		instanceCache.put("SysUser", new SysUser());
		instanceCache.put("SysAccount", new SysAccount());
		instanceCache.put("SysAccountOnlineStatus", new SysAccountOnlineStatus());
		instanceCache.put("SysDataDictionary", new SysDataDictionary());
		instanceCache.put("SysOrg", new SysOrg());
		instanceCache.put("SysDept", new SysDept());
		instanceCache.put("SysPosition", new SysPosition());
		instanceCache.put("SysRole", new SysRole());
		instanceCache.put("SysPermission", new SysPermission());
		instanceCache.put("SysUserPermissionCache", new SysUserPermissionCache());
		instanceCache.put("SysPermissionPriority", new SysPermissionPriority());
		instanceCache.put("SysReqLog", new SysReqLog());
		instanceCache.put("SysOperSqlLog", new SysOperSqlLog());
		instanceCache.put("CfgResource", new CfgResource());
		instanceCache.put("SysFile", new SysFile());
		instanceCache.put("SysUserGroup", new SysUserGroup());
		instanceCache.put("SysUserGroupDetail", new SysUserGroupDetail());
		instanceCache.put("SysPushMessageInfo", new SysPushMessageInfo());
		instanceCache.put("SysDataPrivS", new SysDataPrivS());
		instanceCache.put("SysDataLinks", new SysDataLinks());
		instanceCache.put("SysUserRoleLinks", new SysUserRoleLinks());
		instanceCache.put("SysUserDeptLinks", new SysUserDeptLinks());
		instanceCache.put("SysUserPositionLinks", new SysUserDeptLinks());
		instanceCache.put("SysFileIELog", new SysFileIELog());
		instanceCache.put("CfgPropExtendConf", new CfgPropExtendConf());
		instanceCache.put("CfgBusiResModel", new CfgBusiResModel());
		instanceCache.put("CfgBusiResModelRelations", new CfgBusiResModelRelations());
		
		
		// 以下是因为之前命名不规范造成的遗留key值，后续要处理掉
		instanceCache.put("ComProject", new ComProject());
		instanceCache.put("ComProjectModule", new ComProjectModule());
		instanceCache.put("ComSqlScript", new ComSqlScript());
		instanceCache.put("ComSqlScriptParameter", new ComSqlScriptParameter());
	}
	
	/**
	 * 获得实例
	 * @param name
	 * @return
	 */
	public static Object getInstance(String name){
		return instanceCache.get(name);
	}
	/**
	 * 获得实例
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String name, Class<T> clz){
		return (T) instanceCache.get(name);
	}
}
