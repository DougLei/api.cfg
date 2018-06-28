package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComProjectModule;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目模块信息资源对象处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComProjectModuleService extends AbstractPublishService {

	/**
	 * 验证模块编码是否存在
	 * <p>项目唯一</p>
	 * @param projectModule
	 * @return operResult
	 */
	private String validProjectModuleCodeIsExists(ComProjectModule projectModule) {
		String hql = "select count("+ResourceNameConstants.ID+") from ComProjectModule where code = ? and refProjectId = ?";
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
	public String saveProjectModule(ComProjectModule projectModule) {
		String operResult = validProjectModuleCodeIsExists(projectModule);
		if(operResult == null){
			projectModule.setRefProjectId(CurrentThreadContext.getConfProjectId());
			HibernateUtil.saveObject(projectModule, null);
		}
		return operResult;
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
		if(oldProjectModule.getIsCreated() == 1){
			return "["+oldProjectModule.getName()+"]模块已经发布，不能修改模块信息，请先取消发布";
		}
		
		String operResult = null;
		if(!oldProjectModule.getCode().equals(projectModule.getCode())){
			operResult = validProjectModuleCodeIsExists(projectModule);
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
		if(oldProjectModule.getIsCreated() == 1){
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
		
		// 发布模块功能
		String hql = "select "+ResourceNameConstants.ID +  " from ComModuleOperation where isEnabled =1 and isNeedDeploy=1 and moduleId = ?";
		List<Object> publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, hql, projectModuleId);
		if(publishDataIds != null && publishDataIds.size() > 0){
			new ComModuleOperationService().batchPublishModuleOperation(project.getRefDatabaseId(), project.getId(), publishDataIds);
			publishDataIds.clear();
		}
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
		if(projectModule.getIsCreated() == 0){
			return "["+projectModule.getName()+"]模块未发布，无法取消发布";
		}
		
		executeRemoteUpdate(null, projectModule.getRefProjectId(), 
				"delete " + projectModule.getEntityName() + " where refDataId='"+projectModuleId+"' and projectId='"+projectModule.getRefProjectId()+"'");
		publishInfoService.deletePublishedData(null, projectModuleId);
		
		modifyIsCreatedPropVal(projectModule.getEntityName(), 0, projectModule.getId());
		
		// 取消发布模块功能
		String hql = "select "+ResourceNameConstants.ID +  " from ComModuleOperation where moduleId = ?";
		List<Object> publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, hql, projectModuleId);
		if(publishDataIds != null && publishDataIds.size() > 0){
			ComProject project = getObjectById(projectModule.getRefProjectId(), ComProject.class);
			new ComModuleOperationService().batchCancelPublishModuleOperation(project.getRefDatabaseId(), project.getId(), publishDataIds, true);
			publishDataIds.clear();
		}
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
		
		// 发布模块功能
		StringBuilder tmpHql = new StringBuilder(" from ComModuleOperation where isEnabled =1 and isNeedDeploy=1 and moduleId in (");
		int len = projectModuleIds.size();
		for (int i=0;i<len;i++) {
			tmpHql.append("?,");
		}
		tmpHql.setLength(tmpHql.length()-1);
		tmpHql.append(")");
		String hql = tmpHql.toString();
		tmpHql.setLength(0);
		
		List<Object> projectModuleIdsCopy = new ArrayList<Object>(projectModuleIds.size());
		projectModuleIdsCopy.addAll(projectModuleIds);
		long count = (long) HibernateUtil.executeUniqueQueryByHql("select count("+ResourceNameConstants.ID+") "+hql, projectModuleIdsCopy);
		if(count > 0){
			long loopCount = count/200 + 1;
			hql = "select "+ResourceNameConstants.ID + hql;
			for(int i=0;i<loopCount;i++){
				List<Object> publishDataIds = HibernateUtil.executeListQueryByHql("200", (i+1)+"", hql, projectModuleIds);
				if(publishDataIds != null && publishDataIds.size() > 0){
					new ComModuleOperationService().batchPublishModuleOperation(databaseId, projectId, publishDataIds);
					publishDataIds.clear();
				}
			}
		}
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
		
		// 取消发布模块功能
		StringBuilder hql = new StringBuilder("select "+ResourceNameConstants.ID+" from ComModuleOperation where moduleId in (");
		int len = projectModuleIds.size();
		for (int i=0;i<len;i++) {
			hql.append("?,");
		}
		hql.setLength(hql.length()-1);
		hql.append(")");
		List<Object> publishDataIds = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), projectModuleIds);
		if(publishDataIds != null && publishDataIds.size() > 0){
			new ComModuleOperationService().batchCancelPublishModuleOperation(databaseId, projectId, publishDataIds, false);
			publishDataIds.clear();
		}
	}
}
