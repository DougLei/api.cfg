package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目模块信息资源对象处理器
 * @author DougLei
 */
public class ComProjectModuleService extends AbstractPublishService {

	/**
	 * 验证模块关联的项目是否存在
	 * @param projectId
	 * @return operResult
	 */
	private String validProjectModuleRefProjectIsExists(String projectId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProject where id = ?", projectId);
		if(count != 1){
			return "模块关联的，id为["+projectId+"]的项目信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证模块编码是否存在
	 * @param projectModule
	 * @return operResult
	 */
	private String validProjectModuleCodeIsExists(ComProjectModule projectModule) {
		String hql = "select count("+ResourceNameConstants.ID+") from ComProjectModule where projCode = ? and refProjectId = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, projectModule.getCode(), projectModule.getRefProjectId());
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
	public String saveProjectModule(ComProjectModule projectModule) {
		String operResult = validProjectModuleRefProjectIsExists(projectModule.getRefProjectId());
		if(operResult == null){
			operResult = validProjectModuleCodeIsExists(projectModule);
		}
		if(operResult == null){
			HibernateUtil.saveObject(projectModule, operResult);
		}
		return null;
	}

	/**
	 * 修改项目模块
	 * @param projectModule
	 * @return
	 */
	public String updateProjectModule(ComProjectModule projectModule) {
		ComProjectModule oldProjectModule = getObjectById(projectModule.getId(), ComProjectModule.class);
		if(oldProjectModule == null){
			return "没有找到id为["+projectModule.getId()+"]的模块对象信息";
		}
		
		String operResult = null;
		if(!oldProjectModule.getCode().equals(projectModule.getCode())){
			if(publishInfoService.validResourceIsPublished(null, oldProjectModule.getProjectId(), oldProjectModule.getId(), null)){
				return "该模块已经发布，不能修改模块编码，或取消发布后再修改";
			}
			operResult = validProjectModuleCodeIsExists(projectModule);
		}
		
		if(operResult == null){
			operResult = validProjectModuleRefProjectIsExists(projectModule.getRefProjectId());
		}
		if(operResult == null){
			HibernateUtil.updateObjectByHql(projectModule, null);
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
		if(oldProjectModule == null){
			return "没有找到id为["+projectModuleId+"]的模块对象信息";
		}
		if(publishInfoService.validResourceIsPublished(null, oldProjectModule.getProjectId(), oldProjectModule.getId(), null)){
			return "["+oldProjectModule.getName()+"]模块已经发布，无法删除，请先取消发布";
		}
		
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProjectModule where id = '"+projectModuleId+"'");
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProjectModuleBody where moduleId = '"+projectModuleId+"'");
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目模块
	 * @param projectModuleId
	 * @return
	 */
	public String publishProjectModule(String projectModuleId){
		ComProjectModule projectModule = getObjectById(projectModuleId, ComProjectModule.class);
		if(projectModule == null){
			return "没有找到id为["+projectModuleId+"]的模块对象信息";
		}
		if(projectModule.getIsNeedDeploy() == 0){
			return "id为["+projectModuleId+"]的模块不该被发布，如需发布，请联系管理员";
		}
		if(projectModule.getIsEnabled() == 0){
			return "id为["+projectModuleId+"]的模块信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(null, projectModule.getRefProjectId(), null, null)){
			return "["+projectModule.getName()+"]模块所属的项目还未发布，请先发布项目";
		}
		if(publishInfoService.validResourceIsPublished(null, null, projectModule.getId(), null)){
			return "["+projectModule.getName()+"]模块已经发布，无需再次发布，或取消发布后重新发布";
		}
		ComProject project = getObjectById(projectModule.getRefProjectId(), ComProject.class);
		if(project == null){
			return "模块关联的，id为["+projectModule.getRefProjectId()+"]的项目信息不存在";
		}
		
		publishInfoService.deletePublishedData(null, projectModuleId);
		projectModule.setRefDatabaseId(project.getRefDatabaseId());
		projectModule.setProjectId(projectModule.getRefProjectId());
		executeRemotePublish(null, projectModule.getProjectId(), projectModule, null, null);
		return null;
	}
	
	/**
	 * 取消发布项目模块
	 * @param projectModuleId
	 * @return
	 */
	public String cancelPublishProjectModule(String projectModuleId){
		ComProjectModule projectModule = getObjectById(projectModuleId, ComProjectModule.class);
		if(projectModule == null){
			return "没有找到id为["+projectModuleId+"]的模块对象信息";
		}
		if(!publishInfoService.validResourceIsPublished(null, null, projectModule.getId(), null)){
			return "["+projectModule.getName()+"]模块未发布，无法取消发布";
		}
		String result = validProjectModuleRefProjectIsExists(projectModule.getRefProjectId());
		if(result != null){
			return result;
		}
		
		executeRemoteUpdate(null, projectModule.getRefProjectId(), "delete " + projectModule.getEntityName() + " where " + ResourceNameConstants.ID + "='"+projectModuleId+"'");
		publishInfoService.deletePublishedData(null, projectModuleId);
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
		ComProjectModule projectModule;
		for (Object projectModuleId : projectModuleIds) {
			projectModule = getObjectById(projectModuleId.toString(), ComProjectModule.class);
			
			if(projectModule.getIsNeedDeploy() == 0){
				projectModule.setBatchPublishMsg("id为["+projectModuleId+"]的模块不该被发布，如需发布，请联系管理员");
			}else if(projectModule.getIsEnabled() == 0){
				projectModule.setBatchPublishMsg("id为["+projectModuleId+"]的模块信息无效，请联系管理员");
			}else if(publishInfoService.validResourceIsPublished(null, null, projectModule.getId(), null)){
				projectModule.setBatchPublishMsg("["+projectModule.getName()+"]模块已经发布，无需再次发布，或取消发布后重新发布");
			}
			projectModule.setRefDatabaseId(databaseId);
			projectModule.setProjectId(projectId);
			projectModules.add(projectModule);
		}
		
		publishInfoService.batchDeletePublishedData(null, projectModuleIds);
		executeRemoteBatchPublish(databaseId, null, projectModules, null, null);
		projectModules.clear();
	}

	/**
	 * 批量取消发布项目模块
	 * @param databaseId
	 * @param projectId
	 * @param projectModuleIds
	 */
	public void batchCancelPublishProjectModule(String databaseId, String projectId, List<Object> projectModuleIds) {
		ComProjectModule projectModule = new ComProjectModule();
		StringBuilder hql = new StringBuilder("delete " + projectModule.getEntityName() + " where " + ResourceNameConstants.ID + "in (");
		for (Object projectModuleId : projectModuleIds) {
			projectModule = getObjectById(projectModuleId.toString(), ComProjectModule.class);
			if(!publishInfoService.validResourceIsPublished(null, null, projectModule.getId(), null)){
//				projectModule.setBatchPublishMsg("["+projectModule.getName()+"]模块未发布，无法取消发布");
				continue;
			}
			hql.append("'").append(projectModuleId).append("',");
		}
		hql.setLength(hql.length()-1);
		hql.append(")");
		
		executeRemoteUpdate(databaseId, null, hql.toString());
		publishInfoService.batchDeletePublishedData(null, projectModuleIds);
	}
}
