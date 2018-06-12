package com.king.tooth.sys.service.common;

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
	 * @param projectModule
	 * @return operResult
	 */
	private String validProjectModuleRefProjectIsExists(ComProjectModule projectModule) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProject where id = ?", projectModule.getRefProjectId());
		if(count != 1){
			return "关联的id=["+projectModule.getRefProjectId()+"]的项目信息不存在";
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
		String operResult = validProjectModuleRefProjectIsExists(projectModule);
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
			operResult = validProjectModuleRefProjectIsExists(projectModule);
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
		if(!publishInfoService.validResourceIsPublished(null, projectModule.getRefProjectId(), null, null)){
			return "["+projectModule.getName()+"]模块所属的项目还未发布，请先发布项目";
		}
		if(publishInfoService.validResourceIsPublished(null, null, projectModule.getId(), null)){
			return "["+projectModule.getName()+"]模块已经发布，无法再次发布";
		}
		
		publishInfoService.deletePublishedData(projectModuleId);
		
		ComProject project = getObjectById(projectModule.getRefProjectId(), ComProject.class);
		projectModule.setRefDatabaseId(project.getRefDatabaseId());
		executeRemotePublish(null, projectModule.getProjectId(), projectModule);
		
		// 后续还要加入发布功能，放到这里
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
		
		executeRemoteUpdate(null, projectModule.getRefProjectId(), "");
		publishInfoService.deletePublishedData(projectModuleId);
		return null;
	}
}
