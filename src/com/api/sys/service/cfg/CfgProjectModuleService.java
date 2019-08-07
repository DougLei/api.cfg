package com.api.sys.service.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.api.annotation.Service;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.entity.cfg.CfgProjectModule;
import com.api.sys.entity.cfg.projectmodule.ProjectModule;
import com.api.sys.entity.cfg.projectmodule.ProjectModuleExtend;
import com.api.sys.entity.sys.permission.SysPermissionExtend;
import com.api.sys.service.AService;
import com.api.sys.service.sys.SysPermissionService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 项目模块信息表Service
 * @author DougLei
 */
@Service
public class CfgProjectModuleService extends AService {

	/**
	 * 验证模块编码是否存在
	 * <p>项目唯一</p>
	 * @param projectModule
	 * @return operResult
	 */
	private String validProjectModuleCodeIsExists(CfgProjectModule projectModule) {
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgProjectModule where code = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, projectModule.getCode());
		
		// 临时不要refProjectId条件
//		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgProjectModule where code = ? and refProjectId = ?";
//		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, projectModule.getCode(), CurrentThreadContext.getConfProjectId());
		if(count > 0){
			return "编码为["+projectModule.getCode()+"]的模块信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存项目模块
	 * @param projectModule
	 * @return
	 */
	public Object saveProjectModule(CfgProjectModule projectModule) {
		String operResult = validProjectModuleCodeIsExists(projectModule);
		if(operResult == null){
			// TODO 临时修改refProjectId
			if(StrUtils.isEmpty(projectModule.getRefProjectId())){
				projectModule.setRefProjectId(CurrentThreadContext.getConfProjectId());
			}
			// TODO 临时修改projectId
			if(StrUtils.notEmpty(projectModule.getProjectId())){
				if("7fe971700f21d3a796d2017398812dcd".equals(projectModule.getProjectId())){// 这个是pc端项目的id，改为默认的o1bb id值
					projectModule.setProjectId("90621e37b806o6fe8538c5eb782901bb");
				}
			}
			
			return HibernateUtil.saveObject(projectModule, null);
		}
		return operResult;
	}

	/**
	 * 修改项目模块
	 * @param projectModule
	 * @return
	 */
	public Object updateProjectModule(CfgProjectModule projectModule) {
		CfgProjectModule oldProjectModule = getObjectById(projectModule.getId(), CfgProjectModule.class);
		
		String operResult = null;
		if(!oldProjectModule.getCode().equals(projectModule.getCode())){
			operResult = validProjectModuleCodeIsExists(projectModule);
			BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).updatePermissionCodeInfo(projectModule);
		}
		
		if(operResult == null){
			if(projectModule.isChangePermissionInfo(oldProjectModule)){
				BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).updatePermissionInfo(projectModule);
			}
			projectModule.setProjectId(oldProjectModule.getProjectId());
			return HibernateUtil.updateEntityObject(projectModule, null);
		}
		return operResult;
	}

	/**
	 * 删除项目模块
	 * @param projectModuleId
	 * @return
	 */
	public String deleteProjectModule(String projectModuleId) {
		CfgProjectModule module = getObjectById(projectModuleId, CfgProjectModule.class);
		
		// 判断有没有子模块，如果有子模块，不能删除
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgProjectModule where parentId = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, projectModuleId);
		if(count > 0){
			return "["+module.getName()+"]模块存在"+count+"条子模块，无法直接删除，请先删除所有子模块";
		}
		
		// 删除模块信息
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgProjectModule where "+ResourcePropNameConstants.ID+" = ?", projectModuleId);
		// 删除权限信息
		BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).deletePermissionInfoByResourceId(projectModuleId);
		// 删除权限缓存信息
		BuiltinResourceInstance.getInstance("SysPermissionService", SysPermissionService.class).deletePermissionCache();
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/** 查询模块集合的selectHql语句的select头 */
	private static final String queryModulesSelectHqlHead = "select new map(" + ResourcePropNameConstants.ID + " as id, name as text, url as link, appUrl as appUrl, icon as icon, isEnabled as hide, orderCode as orderCode) ";
	
	/**
	 * 获取当前项目所有有效的模块信息
	 * <p>该方法目前只在登陆的时候用到，给不受权限约束的用户使用，比如管理员</p>
	 * @return
	 */
	public List<ProjectModuleExtend> getCurrentProjectOfModules(){
		List<ProjectModuleExtend> modules = installProjectModuleInstanceList(queryRootModulesHql, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(modules != null && modules.size() > 0){
			for (ProjectModuleExtend module : modules) {
				module.setChildren(recursiveGetCurrentProjectOfSubModules(module));
			}
		}
		return modules;
	}
	/**
	 * 递归获取当前项目所有有效的模块的子模块
	 * @param projectModule
	 * @return
	 */
	private List<ProjectModuleExtend> recursiveGetCurrentProjectOfSubModules(ProjectModuleExtend projectModule) {
		List<ProjectModuleExtend> subProjectModules = installProjectModuleInstanceList(queryModulesHql, projectModule.getId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(subProjectModules != null && subProjectModules.size() > 0){
			for (ProjectModuleExtend subModule : subProjectModules) {
				subModule.setChildren(recursiveGetCurrentProjectOfSubModules(subModule));
			}
		}
		return subProjectModules;
	}
	/** 查询根模块集合的hql */
	private static final String queryRootModulesHql = queryModulesSelectHqlHead + "from CfgProjectModule where (parentId is null or parentId = '') and projectId=? and customerId=? and isEnabled = 1 order by orderCode asc";
	/** 查询子模块集合的hql */
	private static final String queryModulesHql = queryModulesSelectHqlHead + "from CfgProjectModule where parentId=? and projectId=? and customerId=? and isEnabled = 1 order by orderCode asc";
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 根据权限获取对应的模块集合
	 * <p>该方法目前只在登陆的时候用到，给收到权限约束的一般用户使用</p>
	 * @param permission
	 * @return
	 */
	public List<ProjectModuleExtend> getProjectModulesByPermission(SysPermissionExtend permission) {
		List<SysPermissionExtend> permissions = permission.getChildren();
		List<ProjectModuleExtend> modules = new ArrayList<ProjectModuleExtend>(permissions.size());
		
		ProjectModuleExtend ex = null;
		for (SysPermissionExtend sysPermissionExtend : permissions) {
			ex = getProjectModuleByPermission(sysPermissionExtend);
			if(ex == null){
				continue;
			}
			modules.add(ex);
		}
		sortProjectModules(modules, true);
		return modules;
	}
	
	/**
	 * 查询权限对应的模块信息
	 * @param permission
	 * @return
	 */
	private ProjectModuleExtend getProjectModuleByPermission(SysPermissionExtend permission) {
		ProjectModuleExtend module = installProjectModuleInstance(queryProjectModuleByIdHql, permission.getRefResourceId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(module == null){
			return null;
		}
		module.setChildren(recursiveGetProjectModuleByPermission(permission.getChildren()));
		return module;
	}
	/**
	 * 递归查询权限对应的模块信息
	 * @param permissions
	 * @return
	 */
	private List<ProjectModuleExtend> recursiveGetProjectModuleByPermission(List<SysPermissionExtend> permissions) {
		List<ProjectModuleExtend> modules = null;
		if(permissions != null && permissions.size() > 0){
			modules = new ArrayList<ProjectModuleExtend>(permissions.size());
			ProjectModuleExtend module;
			for (SysPermissionExtend permission : permissions) {
				module = installProjectModuleInstance(queryProjectModuleByIdHql, permission.getRefResourceId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
				if(module == null){
					continue;
				}
				modules.add(module);
				module.setChildren(recursiveGetProjectModuleByPermission(permission.getChildren()));
			}
		}
		return modules;
	}
	/** 根据id查询模块信息集合的hql */
	private final static String queryProjectModuleByIdHql = queryModulesSelectHqlHead + "from CfgProjectModule where "+ResourcePropNameConstants.ID+"=? and projectId=? and customerId=? and isEnabled = 1 order by orderCode asc";
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 组装projectModule实例对象
	 * @param hql
	 * @param parameterArray
	 * @return
	 */
	private ProjectModuleExtend installProjectModuleInstance(String hql, Object... parameterArray){
		return HibernateUtil.extendExecuteUniqueQueryByHqlArr(ProjectModuleExtend.class, hql, parameterArray);
	}
	
	/**
	 * 组装projectModule实例集合
	 * @param hql
	 * @param parameterArray
	 * @return
	 */
	private List<ProjectModuleExtend> installProjectModuleInstanceList(String hql, Object... parameterArray){
		return HibernateUtil.extendExecuteListQueryByHqlArr(ProjectModuleExtend.class, null, null, hql, parameterArray);
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 对模块信息进行排序
	 * @param projectModules
	 * @param isAsc 是否正序，false为倒叙
	 * @return
	 */
	private List<ProjectModuleExtend> sortProjectModules(List<ProjectModuleExtend> projectModules, boolean isAsc) {
		if(projectModules == null || projectModules.size() == 0){
			return projectModules;
		}
		for (ProjectModuleExtend projectModule : projectModules) {
			sortProjectModules(projectModule.getChildren(), isAsc);
		}
		if(isAsc){
			Collections.sort(projectModules);
		}else{
			Collections.sort(projectModules, comparator);
		}
		return projectModules;
	}
	/** 倒叙排列模块的比较对象实例 */
	private static Comparator<ProjectModule> comparator = Collections.reverseOrder();
}
