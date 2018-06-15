package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.common.ComModuleOperation;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 模块功能资源对象处理器
 * @author DougLei
 */
public class ComModuleOperationService extends AbstractPublishService {

	/**
	 * 验证功能关联的模块是否存在
	 * @param moduleId
	 * @return 
	 */
	private String validModuleOperationRefModuleIsExists(String moduleId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProjectModule where id = ?", moduleId);
		if(count != 1){
			return "功能关联的，id为["+moduleId+"]的模块信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证功能编码是否存在
	 * @param projectId
	 * @param moduleOperation
	 * @return 
	 */
	private String validModuleOperationCodeIsExists(String projectId, ComModuleOperation moduleOperation) {
		String hql = "select count(mo."+ResourceNameConstants.ID+") from ComModuleOperation mo, ComProjectModule pm " +
				"where mo.moduleId = pm.id and mo.code = ? and pm.refProjectId = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, moduleOperation.getCode(), projectId);
		if(count > 0){
			return "编码为["+moduleOperation.getCode()+"]的功能信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存模块功能
	 * @return
	 */
	public String saveModuleOperation(ComModuleOperation moduleOperation) {
		String operResult = validModuleOperationRefModuleIsExists(moduleOperation.getModuleId());
		if(operResult == null){
			ComProjectModule projectModule = getObjectById(moduleOperation.getModuleId(), ComProjectModule.class);
			operResult = validModuleOperationCodeIsExists(projectModule.getRefProjectId(), moduleOperation);
		}
		if(operResult == null){
			HibernateUtil.saveObject(moduleOperation, null);
		}
		return null;
	}

	/**
	 * 修改模块功能
	 * @return
	 */
	public String updateModuleOperation(ComModuleOperation moduleOperation) {
		ComModuleOperation oldModuleOperation = getObjectById(moduleOperation.getId(), ComModuleOperation.class);
		if(oldModuleOperation == null){
			return "没有找到id为["+moduleOperation.getId()+"]的功能对象信息";
		}
		
		ComProjectModule projectModule = getObjectById(oldModuleOperation.getModuleId(), ComProjectModule.class);
		if(projectModule == null){
			return "功能关联的，id为["+moduleOperation.getModuleId()+"]的模块信息不存在";
		}
		if(publishInfoService.validResourceIsPublished(null, projectModule.getRefProjectId(), oldModuleOperation.getId(), null)){
			return "该功能已经发布，不能修改功能信息，或取消发布后再修改";
		}
		
		String operResult = null;
		if(!oldModuleOperation.getCode().equals(moduleOperation.getCode())){
			operResult = validModuleOperationCodeIsExists(projectModule.getRefProjectId(), moduleOperation);
		}
		
		if(operResult == null){
			operResult = validModuleOperationRefModuleIsExists(moduleOperation.getModuleId());
		}
		if(operResult == null){
			HibernateUtil.updateObjectByHql(moduleOperation, null);
		}
		return operResult;
	}

	/**
	 * 删除模块功能
	 * @return
	 */
	public String deleteModuleOperation(String moduleOperationId) {
		ComModuleOperation oldModuleOperation = getObjectById(moduleOperationId, ComModuleOperation.class);
		if(oldModuleOperation == null){
			return "没有找到id为["+moduleOperationId+"]的功能对象信息";
		}
		if(publishInfoService.validResourceIsPublished(null, null, moduleOperationId, null)){
			return "["+oldModuleOperation.getName()+"]功能已经发布，无法删除，请先取消发布";
		}
		
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComModuleOperation where id = '"+moduleOperationId+"'");
		return null;
	}

	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布模块功能
	 * @return
	 */
	public String publishModuleOperation(String moduleOperationId) {
		ComModuleOperation moduleOperation = getObjectById(moduleOperationId, ComModuleOperation.class);
		if(moduleOperation == null){
			return "没有找到id为["+moduleOperationId+"]的功能对象信息";
		}
		
		if(moduleOperation.getIsNeedDeploy() == 0){
			return "id为["+moduleOperationId+"]的功能不该被发布，如需发布，请联系管理员";
		}
		if(moduleOperation.getIsEnabled() == 0){
			return "id为["+moduleOperationId+"]的功能信息无效，请联系管理员";
		}
		if(publishInfoService.validResourceIsPublished(null, null, moduleOperationId, null)){
			return "["+moduleOperation.getName()+"]功能已经发布，无需再次发布，或取消发布后重新发布";
		}
		
		ComProjectModule projectModule = getObjectById(moduleOperation.getModuleId(), ComProjectModule.class);
		if(projectModule == null){
			return "功能关联的，id为["+moduleOperation.getModuleId()+"]的模块信息不存在";
		}
		if(!publishInfoService.validResourceIsPublished(null, null, projectModule.getId(), null)){
			return "["+moduleOperation.getName()+"]功能所属的模块还未发布，请先发布模块";
		}
		
		ComProject project = getObjectById(projectModule.getRefProjectId(), ComProject.class);
		
		publishInfoService.deletePublishedData(null, moduleOperationId);
		moduleOperation.setRefDatabaseId(project.getRefDatabaseId());
		moduleOperation.setRefProjectId(project.getId());
		executeRemotePublish(project.getRefDatabaseId(), project.getId(), moduleOperation, 0, null);
		return null;
	}

	/**
	 * 取消发布模块功能
	 * @return
	 */
	public String cancelPublishModuleOperation(String moduleOperationId) {
		ComModuleOperation moduleOperation = getObjectById(moduleOperationId, ComModuleOperation.class);
		if(moduleOperation == null){
			return "没有找到id为["+moduleOperationId+"]的功能对象信息";
		}
		if(!publishInfoService.validResourceIsPublished(null, null, moduleOperationId, null)){
			return "["+moduleOperation.getName()+"]功能未发布，无法取消发布";
		}
		ComProjectModule projectModule = getObjectById(moduleOperation.getModuleId(), ComProjectModule.class);
		if(projectModule == null){
			return "功能关联的，id为["+moduleOperation.getModuleId()+"]的模块信息不存在";
		}
		
		executeRemoteUpdate(null, projectModule.getRefProjectId(), 
				"delete " + moduleOperation.getEntityName() + " where refDataId='"+moduleOperationId+"' and projectId='"+projectModule.getRefProjectId()+"'");
		publishInfoService.deletePublishedData(projectModule.getRefProjectId(), moduleOperationId);
		return null;
	}

	/**
	 * 批量发布模块功能
	 * @param databaseId
	 * @param projectId
	 * @param publishDataIds
	 */
	public void batchPublishModuleOperation(String databaseId, String projectId, List<Object> publishDataIds) {
		List<ComModuleOperation> comModuleOperations = new ArrayList<ComModuleOperation>(publishDataIds.size());
		ComModuleOperation comModuleOperation;
		for (Object publishDataId : publishDataIds) {
			comModuleOperation = getObjectById(publishDataId.toString(), ComModuleOperation.class);
			
			if(comModuleOperation.getIsNeedDeploy() == 0){
				comModuleOperation.setBatchPublishMsg("id为["+publishDataId+"]的功能不该被发布，如需发布，请联系管理员");
			}else if(comModuleOperation.getIsEnabled() == 0){
				comModuleOperation.setBatchPublishMsg("id为["+publishDataId+"]的功能信息无效，请联系管理员");
			}else if(publishInfoService.validResourceIsPublished(null, null, comModuleOperation.getId(), null)){
				comModuleOperation.setBatchPublishMsg("["+comModuleOperation.getName()+"]功能已经发布，无需再次发布，或取消发布后重新发布");
			}
			comModuleOperation.setRefDatabaseId(databaseId);
			comModuleOperation.setProjectId(projectId);
			comModuleOperations.add(comModuleOperation);
		}
		
		publishInfoService.batchDeletePublishedData(null, publishDataIds);
		executeRemoteBatchPublish(databaseId, projectId, comModuleOperations, 0, null);
		comModuleOperations.clear();
	}
	
	/**
	 * 批量取消发布模块功能
	 * @param databaseId
	 * @param projectId
	 * @param publishDataIds
	 */
	public void batchCancelPublishModuleOperation(String databaseId, String projectId, List<Object> publishDataIds) {
		publishInfoService.batchDeletePublishedData(projectId, publishDataIds);		
	}

	//--------------------------------------------------------------------------------------------------------
	protected String loadPublishData(String porjectId, String publishDataId) {
		return null;
	}

	protected String unloadPublishData(String projectId, String publishDataId) {
		return null;
	}
}
