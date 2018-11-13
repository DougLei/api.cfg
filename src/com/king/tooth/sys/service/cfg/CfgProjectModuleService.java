package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.entity.cfg.CfgProjectModule;
import com.king.tooth.sys.entity.cfg.projectmodule.ProjectModule;
import com.king.tooth.sys.entity.cfg.projectmodule.ProjectModuleExtend;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.sys.service.AService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.hibernate.HibernateUtil;

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
		String hql = "select count("+ResourcePropNameConstants.ID+") from CfgProjectModule where code = ? and refProjectId = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, projectModule.getCode(), CurrentThreadContext.getConfProjectId());
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
			projectModule.setRefProjectId(CurrentThreadContext.getConfProjectId());
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
		}
		
		if(operResult == null){
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
		getObjectById(projectModuleId, CfgProjectModule.class);
		
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgProjectModule where "+ResourcePropNameConstants.ID+" = '"+projectModuleId+"'");
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/** 查询模块集合的selectHql语句的select头 */
	private static final String queryModulesSelectHqlHead = "select new map(" + ResourcePropNameConstants.ID + " as id, name as text, url as link, icon as icon, isEnabled as hide, orderCode as orderCode) ";
	
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
		for (SysPermissionExtend sysPermissionExtend : permissions) {
			modules.add(getProjectModuleByPermission(sysPermissionExtend));
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
