package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComProjectModule;
import com.king.tooth.sys.entity.cfg.projectmodule.ProjectModuleExtend;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目模块信息表Service
 * @author DougLei
 */
@Service
public class CfgProjectModuleService extends AbstractPublishService {

	/**
	 * 验证模块编码是否存在
	 * <p>项目唯一</p>
	 * @param projectModule
	 * @return operResult
	 */
	private String validProjectModuleCodeIsExists(ComProjectModule projectModule) {
		String hql = "select count("+ResourcePropNameConstants.ID+") from ComProjectModule where code = ? and refProjectId = ?";
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
	public Object saveProjectModule(ComProjectModule projectModule) {
		String operResult = validProjectModuleCodeIsExists(projectModule);
		if(operResult == null){
			// TODO 单项目，取消是否平台开发者的判断
//			boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
			
//			if(isDeveloper){
//				projectModule.setRefProjectId(CurrentThreadContext.getProjectId());
//			}else{
				projectModule.setRefProjectId(CurrentThreadContext.getConfProjectId());
//			}
			return HibernateUtil.saveObject(projectModule, null);
		}
		return operResult;
	}

	/**
	 * 修改项目模块
	 * @param projectModule
	 * @return
	 */
	public Object updateProjectModule(ComProjectModule projectModule) {
		ComProjectModule oldProjectModule = getObjectById(projectModule.getId(), ComProjectModule.class);
		if(oldProjectModule.getIsCreated() == 1){
			return "["+oldProjectModule.getName()+"]模块已经发布，不能修改模块信息，请先取消发布";
		}
		
		String operResult = null;
		if(!oldProjectModule.getCode().equals(projectModule.getCode())){
			operResult = validProjectModuleCodeIsExists(projectModule);
		}
		
		if(operResult == null){
			return HibernateUtil.updateObject(projectModule, null);
		}
		return operResult;
	}

	/**
	 * 删除项目模块
	 * @param projectModuleId
	 * @return
	 */
	public String deleteProjectModule(String projectModuleId) {
		ComProjectModule oldProjectModule = getObjectById(projectModuleId, ComProjectModule.class);
		if(oldProjectModule.getIsCreated() == 1){
			return "["+oldProjectModule.getName()+"]模块已经发布，无法删除，请先取消发布";
		}
		
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComProjectModule where "+ResourcePropNameConstants.ID+" = '"+projectModuleId+"'");
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/** 查询模块集合的selectHql语句的select头 */
	private static final String queryModulesSelectHqlHeader = "select new map(" + ResourcePropNameConstants.ID + " as id, name as text, url as link, icon as icon, isEnabled as hide) ";
	
	/**
	 * 获取当前项目所有有效的模块信息
	 * <p>该方法目前只在登陆的时候用到</p>
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
	private static final String queryRootModulesHql = queryModulesSelectHqlHeader + "from ComProjectModule where (parentId is null or parentId = '') and projectId=? and customerId=? and isEnabled = 1 order by orderCode asc";
	/** 查询子模块集合的hql */
	private static final String queryModulesHql = queryModulesSelectHqlHeader + "from ComProjectModule where parentId=? and projectId=? and customerId=? and isEnabled = 1 order by orderCode asc";
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 根据权限获取对应的模块集合
	 * <p>该方法目前只在登陆的时候用到</p>
	 * @param permission
	 * @return
	 */
	public List<ProjectModuleExtend> getProjectModulesByPermission(SysPermissionExtend permission) {
		List<SysPermissionExtend> permissions = permission.getChildren();
		List<ProjectModuleExtend> modules = new ArrayList<ProjectModuleExtend>(permissions.size());
		for (SysPermissionExtend sysPermissionExtend : permissions) {
			modules.add(getProjectModuleByPermission(sysPermissionExtend));
		}
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
	private final static String queryProjectModuleByIdHql = queryModulesSelectHqlHeader + "from ComProjectModule where "+ResourcePropNameConstants.ID+"=? and projectId=? and customerId=? and isEnabled = 1 order by orderCode asc";
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
	 * 发布项目模块
	 * @param projectModuleId
	 * @return
	 */
	public String publishProjectModule(String projectModuleId){
		ComProjectModule projectModule = getObjectById(projectModuleId, ComProjectModule.class);
		if(projectModule.getIsCreated() == 1){
			return "["+projectModule.getName()+"]模块已经发布，无需再次发布，或取消发布后重新发布";
		}
		if(projectModule.getIsNeedDeploy() == 0){
			return "id为["+projectModuleId+"]的模块不该被发布，如需发布，请联系管理员";
		}
		if(projectModule.getIsEnabled() == 0){
			return "id为["+projectModuleId+"]的模块信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(null, projectModule.getRefProjectId(), projectModule.getRefProjectId())){
			return "["+projectModule.getName()+"]模块所属的项目还未发布，请先发布项目";
		}
		ComProject project = getObjectById(projectModule.getRefProjectId(), ComProject.class);
		
		publishInfoService.deletePublishedData(null, projectModuleId);
		projectModule.setRefDatabaseId(project.getRefDatabaseId());
		projectModule.setProjectId(projectModule.getRefProjectId());
		executeRemotePublish(project.getRefDatabaseId(), projectModule.getProjectId(), projectModule, 0, null);

		modifyIsCreatedPropVal(projectModule.getEntityName(), 1, projectModule.getId());
		return null;
	}
	
	/**
	 * 取消发布项目模块
	 * @param projectModuleId
	 * @return
	 */
	public String cancelPublishProjectModule(String projectModuleId){
		ComProjectModule projectModule = getObjectById(projectModuleId, ComProjectModule.class);
		if(projectModule.getIsCreated() == 0){
			return "["+projectModule.getName()+"]模块未发布，无法取消发布";
		}
		
		executeRemoteUpdate(null, projectModule.getRefProjectId(), 
				"delete " + projectModule.getEntityName() + " where refDataId='"+projectModuleId+"' and projectId='"+projectModule.getRefProjectId()+"'");
		publishInfoService.deletePublishedData(null, projectModuleId);
		
		modifyIsCreatedPropVal(projectModule.getEntityName(), 0, projectModule.getId());
		return null;
	}
	
	/**
	 * 批量发布项目模块
	 * @param databaseId
	 * @param projectId
	 * @param projectModuleIds
	 * @return
	 */
	public void batchPublishProjectModule(String databaseId, String projectId, List<Object> projectModuleIds) {
		List<ComProjectModule> projectModules = new ArrayList<ComProjectModule>(projectModuleIds.size());
		ComProjectModule projectModule = null;
		for (Object projectModuleId : projectModuleIds) {
			projectModule = getObjectById(projectModuleId.toString(), ComProjectModule.class);
			
			if(projectModule.getIsNeedDeploy() == 0){
				Log4jUtil.info("id为["+projectModuleId+"]的模块不该被发布，如需发布，请联系管理员");
				continue;
			}else if(projectModule.getIsEnabled() == 0){
				Log4jUtil.info("id为["+projectModuleId+"]的模块信息无效，请联系管理员");
				continue;
			}else if(projectModule.getIsCreated() == 1){
				Log4jUtil.info("["+projectModule.getName()+"]模块已经发布，无需再次发布，或取消发布后重新发布");
				continue;
			}
			projectModule.setRefDatabaseId(databaseId);
			projectModule.setProjectId(projectId);
			projectModules.add(projectModule);
		}
		batchModifyIsCreatedPropVal(projectModule.getEntityName(), 1, projectModuleIds);
		
		publishInfoService.batchDeletePublishedData(null, projectModuleIds);
		executeRemoteBatchPublish(databaseId, projectId, projectModules, 0, null);
	}

	/**
	 * 批量取消发布项目模块
	 * @param databaseId
	 * @param projectId
	 * @param projectModuleIds
	 */
	public void batchCancelPublishProjectModule(String databaseId, String projectId, List<Object> projectModuleIds) {
		publishInfoService.batchDeletePublishedData(projectId, projectModuleIds);
		ComProjectModule projectModule = new ComProjectModule();
		batchModifyIsCreatedPropVal(projectModule.getEntityName(), 0, projectModuleIds);
	}
}
