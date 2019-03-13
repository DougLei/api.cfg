package com.api.sys.builtin.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.api.sys.controller.cfg.CfgBusiModelController;
import com.api.sys.controller.cfg.CfgBusiModelResRelationsController;
import com.api.sys.controller.cfg.CfgColumnController;
import com.api.sys.controller.cfg.CfgDatabaseController;
import com.api.sys.controller.cfg.CfgProjectController;
import com.api.sys.controller.cfg.CfgProjectModuleController;
import com.api.sys.controller.cfg.CfgPropExtendConfController;
import com.api.sys.controller.cfg.CfgSqlController;
import com.api.sys.controller.cfg.CfgSqlParameterController;
import com.api.sys.controller.cfg.CfgTableController;
import com.api.sys.controller.sys.SysAccountCardController;
import com.api.sys.controller.sys.SysAccountController;
import com.api.sys.controller.sys.SysExcelController;
import com.api.sys.controller.sys.SysPermissionController;
import com.api.sys.controller.sys.SysPushMessageInfoController;
import com.api.sys.controller.sys.SysUserController;
import com.api.sys.controller.tools.SystemToolsController;
import com.api.sys.entity.ITable;
import com.api.sys.entity.cfg.CfgBusiModel;
import com.api.sys.entity.cfg.CfgBusiModelResRelations;
import com.api.sys.entity.cfg.CfgCodeDataDictionary;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.sys.entity.cfg.CfgHibernateHbm;
import com.api.sys.entity.cfg.CfgProject;
import com.api.sys.entity.cfg.CfgProjectModule;
import com.api.sys.entity.cfg.CfgPropCodeRule;
import com.api.sys.entity.cfg.CfgPropCodeRuleDetail;
import com.api.sys.entity.cfg.CfgPropExtendConf;
import com.api.sys.entity.cfg.CfgResource;
import com.api.sys.entity.cfg.CfgSeqInfo;
import com.api.sys.entity.cfg.CfgSql;
import com.api.sys.entity.cfg.CfgSqlParameter;
import com.api.sys.entity.cfg.CfgSqlResultset;
import com.api.sys.entity.cfg.CfgTable;
import com.api.sys.entity.cfg.datalinks.CfgProjectSqlLinks;
import com.api.sys.entity.cfg.datalinks.CfgProjectTableLinks;
import com.api.sys.entity.sys.SysAccount;
import com.api.sys.entity.sys.SysAccountCard;
import com.api.sys.entity.sys.SysAccountOnlineStatus;
import com.api.sys.entity.sys.SysDataDictionary;
import com.api.sys.entity.sys.SysDataPrivS;
import com.api.sys.entity.sys.SysDept;
import com.api.sys.entity.sys.SysFile;
import com.api.sys.entity.sys.SysFileIELog;
import com.api.sys.entity.sys.SysOperSqlLog;
import com.api.sys.entity.sys.SysOperationLog;
import com.api.sys.entity.sys.SysOrg;
import com.api.sys.entity.sys.SysPermission;
import com.api.sys.entity.sys.SysPermissionPriority;
import com.api.sys.entity.sys.SysPosition;
import com.api.sys.entity.sys.SysPushMessageInfo;
import com.api.sys.entity.sys.SysReqLog;
import com.api.sys.entity.sys.SysRole;
import com.api.sys.entity.sys.SysUser;
import com.api.sys.entity.sys.SysUserGroup;
import com.api.sys.entity.sys.SysUserGroupDetail;
import com.api.sys.entity.sys.SysUserPermissionCache;
import com.api.sys.entity.sys.datalinks.SysDataLinks;
import com.api.sys.entity.sys.datalinks.SysUserDeptLinks;
import com.api.sys.entity.sys.datalinks.SysUserPositionLinks;
import com.api.sys.entity.sys.datalinks.SysUserRoleLinks;
import com.api.sys.service.cfg.CfgBusiModelResRelationsService;
import com.api.sys.service.cfg.CfgBusiModelService;
import com.api.sys.service.cfg.CfgColumnService;
import com.api.sys.service.cfg.CfgDatabaseService;
import com.api.sys.service.cfg.CfgProjectModuleService;
import com.api.sys.service.cfg.CfgProjectService;
import com.api.sys.service.cfg.CfgPropExtendConfService;
import com.api.sys.service.cfg.CfgResourceService;
import com.api.sys.service.cfg.CfgSqlService;
import com.api.sys.service.cfg.CfgTableService;
import com.api.sys.service.sys.SysAccountCardService;
import com.api.sys.service.sys.SysAccountOnlineStatusService;
import com.api.sys.service.sys.SysAccountService;
import com.api.sys.service.sys.SysExcelService;
import com.api.sys.service.sys.SysFileService;
import com.api.sys.service.sys.SysPermissionService;
import com.api.sys.service.sys.SysPushMessageInfoService;
import com.api.sys.service.sys.SysUserService;
import com.api.sys.service.tools.SystemToolsService;

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
		instanceCache.put("SysAccountCardController", new SysAccountCardController());
		instanceCache.put("SysUserController", new SysUserController());
		instanceCache.put("SysPermissionController", new SysPermissionController());
		instanceCache.put("SystemToolsController", new SystemToolsController());
		instanceCache.put("SysPushMessageInfoController", new SysPushMessageInfoController());
		instanceCache.put("SysExcelController", new SysExcelController());
		instanceCache.put("CfgPropExtendConfController", new CfgPropExtendConfController());
		instanceCache.put("CfgBusiModelController", new CfgBusiModelController());
		instanceCache.put("CfgBusiModelResRelationsController", new CfgBusiModelResRelationsController());
		
		instanceCache.put("CfgTableService", new CfgTableService());
		instanceCache.put("CfgColumnService", new CfgColumnService());
		instanceCache.put("CfgDatabaseService", new CfgDatabaseService());
		instanceCache.put("CfgProjectService", new CfgProjectService());
		instanceCache.put("CfgProjectModuleService", new CfgProjectModuleService());
		instanceCache.put("CfgSqlService", new CfgSqlService());
		instanceCache.put("SysAccountOnlineStatusService", new SysAccountOnlineStatusService());
		instanceCache.put("SysAccountService", new SysAccountService());
		instanceCache.put("SysAccountCardService", new SysAccountCardService());
		instanceCache.put("CfgResourceService", new CfgResourceService());
		instanceCache.put("SysUserService", new SysUserService());
		instanceCache.put("SysPermissionService", new SysPermissionService());
		instanceCache.put("SysFileService", new SysFileService());
		instanceCache.put("SystemToolsService", new SystemToolsService());
		instanceCache.put("SysPushMessageInfoService", new SysPushMessageInfoService());
		instanceCache.put("SysExcelService", new SysExcelService());
		instanceCache.put("CfgPropExtendConfService", new CfgPropExtendConfService());
		instanceCache.put("CfgBusiModelService", new CfgBusiModelService());
		instanceCache.put("CfgBusiModelResRelationsService", new CfgBusiModelResRelationsService());
		
		instanceCache.put("CfgDatabase", new CfgDatabase());
		instanceCache.put("CfgTable", new CfgTable());
		instanceCache.put("CfgColumn", new CfgColumn());
		instanceCache.put("CfgHibernateHbm", new CfgHibernateHbm());
		instanceCache.put("CfgProject", new CfgProject());
		instanceCache.put("CfgProjectModule", new CfgProjectModule());
		instanceCache.put("CfgSql", new CfgSql());
		instanceCache.put("CfgSqlParameter", new CfgSqlParameter());
		instanceCache.put("CfgSqlResultset", new CfgSqlResultset());
		instanceCache.put("CfgPropCodeRule", new CfgPropCodeRule());
		instanceCache.put("CfgPropCodeRuleDetail", new CfgPropCodeRuleDetail());
		instanceCache.put("CfgSeqInfo", new CfgSeqInfo());
		instanceCache.put("CfgCodeDataDictionary", new CfgCodeDataDictionary());
		instanceCache.put("CfgProjectSqlLinks", new CfgProjectSqlLinks());
		instanceCache.put("CfgProjectTableLinks", new CfgProjectTableLinks());
		instanceCache.put("CfgPropExtendConf", new CfgPropExtendConf());
		instanceCache.put("CfgBusiModel", new CfgBusiModel());
		instanceCache.put("CfgBusiModelResRelations", new CfgBusiModelResRelations());
		instanceCache.put("CfgResource", new CfgResource());
		instanceCache.put("SysUser", new SysUser());
		instanceCache.put("SysAccount", new SysAccount());
		instanceCache.put("SysAccountCard", new SysAccountCard());
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
		instanceCache.put("SysOperationLog", new SysOperationLog());
		instanceCache.put("SysFile", new SysFile());
		instanceCache.put("SysUserGroup", new SysUserGroup());
		instanceCache.put("SysUserGroupDetail", new SysUserGroupDetail());
		instanceCache.put("SysPushMessageInfo", new SysPushMessageInfo());
		instanceCache.put("SysDataPrivS", new SysDataPrivS());
		instanceCache.put("SysDataLinks", new SysDataLinks());
		instanceCache.put("SysUserRoleLinks", new SysUserRoleLinks());
		instanceCache.put("SysUserDeptLinks", new SysUserDeptLinks());
		instanceCache.put("SysUserPositionLinks", new SysUserPositionLinks());
		instanceCache.put("SysFileIELog", new SysFileIELog());
	}
	
	/**
	 * 获得实例
	 * @param name
	 * @return
	 */
	public static Object getInstance(String name){
		Object obj = instanceCache.get(name);
		if(obj == null){
			obj = instanceCache.get(name.substring(0, name.length() - 6));
		}
		if(obj == null){
			throw new NullPointerException("没有在实例缓存中查询到name=["+name+"]的资源实例，请联系后端系统开发人员");
		}
		return obj;
	}
	/**
	 * 获得实例
	 * @param name
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String name, Class<T> clz){
		return (T) getInstance(name);
	}
	
	/**
	 * 获得所有tables
	 * @return
	 */
	public static List<CfgTable> getTables(){
		List<CfgTable> tables = new ArrayList<CfgTable>(tableCount);
		Collection<Object> instances = instanceCache.values();
		for (Object object : instances) {
			if(object instanceof ITable){
				tables.add(((ITable)object).toCreateTable());
			}
		}
		return tables;
	}
	private static final int tableCount = 50;
}
