package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql脚本资源服务处理器
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComSqlScriptService extends AbstractPublishService {
	
	/**
	 * 验证sql脚本资源名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validSqlScriptResourceNameIsExists(ComSqlScript sqlScript) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComSqlScript where sqlScriptResourceName = ? and createUserId = ?", sqlScript.getSqlScriptResourceName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(count > 0){
			return "您已经创建过相同sql脚本资源名["+sqlScript.getSqlScriptResourceName()+"]的数据";
		}
		return null;
	}
	
	/**
	 * 验证sql脚本关联的项目是否存在
	 * @param project
	 * @return operResult
	 */
	private String validSqlScriptRefProjIsExists(String projectId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProject where id = ?", projectId);
		if(count != 1){
			return "sql脚本关联的，id为["+projectId+"]的项目信息不存在";
		}
		return null;
	}
	
	/**
	 * 保存sql脚本
	 * @param sqlScript
	 * @return
	 */
	public String saveSqlScript(ComSqlScript sqlScript) {
		String operResult = validSqlScriptResourceNameIsExists(sqlScript);
		if(operResult == null){
			boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
			String projectId = sqlScript.getProjectId();
			sqlScript.setProjectId(null);
			if(!isPlatformDeveloper){// 非平台开发者，建的sql脚本一开始，一定要和一个项目关联起来
				if(StrUtils.isEmpty(projectId)){
					return "sql脚本关联的项目id不能为空！";
				}
				operResult = validSqlScriptRefProjIsExists(projectId);
			}
			
			if(operResult == null){
				if(isPlatformDeveloper){
					/* 属于直接就建模，不需要用户再进行一次建模的操作(表是因为，添加了表信息后，还要添加列信息，然后才能建模) */
					// 如果是平台开发者，则要解析出sql脚本内容，再保存
					operResult = sqlScript.analysisResourceProp();
					if(operResult == null){
						sqlScript.setIsCreated(1);
					}
				}
				if(operResult == null){
					String sqlScriptId = HibernateUtil.saveObject(sqlScript, null);
					
					if(isPlatformDeveloper){
						// 因为保存资源数据的时候，需要sqlScript对象的id，所以放到最后
						sqlScript.setId(sqlScriptId);
						new ComSysResourceService().saveSysResource(sqlScript);
					}
					
					// 保存sql脚本和项目的关联关系
					if(isPlatformDeveloper){
						HibernateUtil.saveDataLinks("ComProjectComSqlScriptLinks", CurrentThreadContext.getProjectId(), sqlScriptId);
					}else{
						HibernateUtil.saveDataLinks("ComProjectComSqlScriptLinks", projectId, sqlScriptId);
					}
				}
			}
		}
		return operResult;
	}

	/**
	 * 修改sql脚本
	 * @param sqlScript
	 * @return
	 */
	public String updateSqlScript(ComSqlScript sqlScript) {
		ComSqlScript oldSqlScript = getObjectById(sqlScript.getId(), ComSqlScript.class);
		if(oldSqlScript == null){
			return "没有找到id为["+sqlScript.getId()+"]的sql脚本对象信息";
		}
		String operResult = null;
		if(!oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
			operResult = validSqlScriptResourceNameIsExists(sqlScript);
		}
		if(operResult == null){
			boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
			if(!isPlatformDeveloper){
				if(StrUtils.isEmpty(sqlScript.getProjectId())){
					return "表关联的项目id不能为空！";
				}
				String projectId = sqlScript.getProjectId();
				sqlScript.setProjectId(null);
				
				if(publishInfoService.validResourceIsPublished(null, projectId, oldSqlScript.getId(), null)){
					return "该sql脚本已经发布，不能修改sql脚本信息，或取消发布后再修改";
				}
			}
			
			if(isPlatformDeveloper){
				operResult = sqlScript.analysisResourceProp();
				sqlScript.setIsCreated(1);
				
				if(!oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
					// 如果修改了sql脚本的资源名，也要同步修改ComSysResource表中的资源名
					new ComSysResourceService().updateResourceName(sqlScript.getId(), sqlScript.getSqlScriptResourceName());
				}
			}
			if(operResult == null){
				HibernateUtil.updateObjectByHql(sqlScript, null);
			}
		}
		return operResult;
	}

	/**
	 * 删除sql脚本
	 * @param sqlScriptId
	 * @param projectId
	 * @return
	 */
	public String deleteSqlScript(String sqlScriptId, String projectId) {
		ComSqlScript oldSqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(oldSqlScript == null){
			return "没有找到id为["+sqlScriptId+"]的sql脚本对象信息";
		}
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		if(!isPlatformDeveloper){
			if(StrUtils.isEmpty(projectId)){
				return "要删除的sql脚本，关联的项目id不能为空";
			}
			if(publishInfoService.validResourceIsPublished(null, projectId, oldSqlScript.getId(), null)){
				return "该sql脚本已经发布，无法删除，请先取消发布";
			}
		}
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks("ComProjectComSqlScriptLinks", null, sqlScriptId);
		if(datalinks.size() > 1){
			List<Object> projectIds = new ArrayList<Object>(datalinks.size());
			StringBuilder hql = new StringBuilder("select projName from ComProject where id in (");
			for (JSONObject json : datalinks) {
				projectIds.add(json.getString("leftId"));
				hql.append("?,");
			}
			hql.setLength(hql.length() - 1);
			hql.append(")");
			
			List<Object> projNames = HibernateUtil.executeListQueryByHql(null, null, hql.toString(), projectIds);
			projectIds.clear();
			return "该sql脚本关联多个项目，无法删除，请先取消和其他项目的关联，关联的项目包括：" + projNames;
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComSqlScript where id = '"+sqlScriptId+"'");
		HibernateUtil.deleteDataLinks("ComProjectComSqlScriptLinks", null, sqlScriptId);
		
		// 如果是平台开发者账户，还要删除资源信息
		if(isPlatformDeveloper){
			new ComSysResourceService().deleteSysResource(sqlScriptId);
		}
		return null;
	}
	
	/**
	 * 建立项目和sql脚本的关联关系
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String addProjSqlScriptRelation(String projectId, String sqlScriptId) {
		HibernateUtil.saveDataLinks("ComProjectComSqlScriptLinks", projectId, sqlScriptId);
		return null;
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String cancelProjSqlScriptRelation(String projectId, String sqlScriptId) {
		HibernateUtil.deleteDataLinks("ComProjectComSqlScriptLinks", projectId, sqlScriptId);
		return null;
	}
	
	/**
	 * 根据资源名，查询对应的通用sql脚本资源对象
	 * @param resourceName
	 * @return
	 */
	public ComSqlScript findSqlScriptResourceByName(String resourceName) {
		if(StrUtils.isEmpty(resourceName)){
			throw new NullPointerException("请求的资源名不能为空");
		}
		
		String queryHql = "from ComSqlScript where isEnabled=1 and sqlScriptResourceName = ?";
		ComSqlScript sqlScriptResource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSqlScript.class, queryHql, resourceName);
		if(sqlScriptResource == null){
			throw new IllegalArgumentException("不存在请求的sql脚本资源：" + resourceName);
		}
		return sqlScriptResource;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布sql脚本
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String publishSqlScript(String projectId, String sqlScriptId) {
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(sqlScript == null){
			return "没有找到id为["+sqlScriptId+"]的sql脚本对象信息";
		}
		if(sqlScript.getIsNeedDeploy() == 0){
			return "id为["+sqlScriptId+"]的sql脚本不该被发布，如需发布，请联系管理员";
		}
		if(sqlScript.getIsEnabled() == 0){
			return "id为["+sqlScriptId+"]的sql脚本信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(null, projectId, sqlScriptId, null)){
			return "["+sqlScript.getSqlScriptResourceName()+"]sql脚本所属的项目还未发布，请先发布项目";
		}
		if(publishInfoService.validResourceIsPublished(null, projectId, sqlScriptId, null)){
			return "["+sqlScript.getSqlScriptResourceName()+"]sql脚本已经发布，无需再次发布，或取消发布后重新发布";
		}
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "sql脚本关联的，id为["+projectId+"]的项目信息不存在";
		}
		
		publishInfoService.deletePublishedData(projectId, sqlScriptId);
		sqlScript.setRefDatabaseId(project.getRefDatabaseId());
		sqlScript.setProjectId(projectId);
		executeRemotePublish(project.getRefDatabaseId(), projectId, sqlScript, 1, "ComProjectComSqlScriptLinks");
		
		return useLoadPublishApi(sqlScriptId, projectId, "sql", "1", projectId);
	}
	
	/**
	 * 取消发布sql脚本
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String cancelPublishSqlScript(String projectId, String sqlScriptId) {
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(sqlScript == null){
			return "没有找到id为["+sqlScriptId+"]的sql脚本对象信息";
		}
		if(!publishInfoService.validResourceIsPublished(null, projectId, sqlScriptId, null)){
			return "["+sqlScript.getSqlScriptResourceName()+"]sql脚本未发布，无法取消发布";
		}
		String result = validSqlScriptRefProjIsExists(projectId);
		if(result != null){
			return result;
		}
		
		executeRemoteUpdate(null, projectId, 
				"delete ComProjectComSqlScriptLinks where projectId='"+projectId+"' and leftId='"+projectId+"' and rightId in (select "+ResourceNameConstants.ID+" from "+sqlScript.getEntityName()+" where projectId='"+projectId+"' and refDataId = '"+sqlScriptId+"')",
				"delete " + sqlScript.getEntityName() + " where projectId='"+projectId+"' and refDataId='"+sqlScriptId+"'",
				"delete ComSysResource where projectId='"+projectId+"' and refDataId = '"+sqlScriptId+"'");
		publishInfoService.deletePublishedData(projectId, sqlScriptId);
		return null;
	}

	/**
	 * 批量发布sql脚本
	 * @param databaseId
	 * @param projectId
	 * @param sqlScriptIds
	 */
	public void batchPublishSqlScript(String databaseId, String projectId, List<Object> sqlScriptIds) {
		List<ComSqlScript> sqlScripts = new ArrayList<ComSqlScript>(sqlScriptIds.size());
		ComSqlScript sqlScript;
		StringBuilder sqlScriptIdStr = new StringBuilder();
		for (Object sqlScriptId : sqlScriptIds) {
			sqlScript = getObjectById(sqlScriptId.toString(), ComSqlScript.class);
			
			if(sqlScript.getIsNeedDeploy() == 0){
				sqlScript.setBatchPublishMsg("id为["+sqlScriptId+"]的sql脚本不该被发布，如需发布，请联系管理员");
			}else if(sqlScript.getIsEnabled() == 0){
				sqlScript.setBatchPublishMsg("id为["+sqlScriptId+"]的sql脚本信息无效，请联系管理员");
			}else if(publishInfoService.validResourceIsPublished(null, projectId, sqlScript.getId(), null)){
				sqlScript.setBatchPublishMsg("["+sqlScript.getSqlScriptResourceName()+"]sql脚本已经发布，无需再次发布，或取消发布后重新发布");
			}
			
			sqlScript.setRefDatabaseId(databaseId);
			sqlScript.setProjectId(projectId);
			sqlScripts.add(sqlScript);
			
			if(sqlScript.getBatchPublishMsg() == null){
				sqlScriptIdStr.append(sqlScriptId).append(",");
			}
		}
		
		publishInfoService.batchDeletePublishedData(null, sqlScriptIds);
		executeRemoteBatchPublish(databaseId, projectId, sqlScripts, 1, "ComProjectComSqlScriptLinks");
		sqlScripts.clear();
		sqlScriptIds.clear();
		
		sqlScriptIdStr.setLength(sqlScriptIdStr.length()-1);
		useLoadPublishApi(sqlScriptIdStr.toString(), projectId, "sql", "1", projectId);
		sqlScriptIdStr.setLength(0);
	}
	
	/**
	 * 批量取消发布sql脚本
	 * @param databaseId
	 * @param projectId
	 * @param sqlScriptIds
	 * @return
	 */
	public void batchCancelPublishSqlScript(String databaseId, String projectId, List<Object> sqlScriptIds) {
		publishInfoService.batchDeletePublishedData(projectId, sqlScriptIds);
	}

	//--------------------------------------------------------------------------------------------------------
	protected String loadPublishData(String projectId, String publishDataId) {
		String[] sqlScriptIds = publishDataId.split(",");
		ComSqlScript sqlScript;
		String hql = "from ComSqlScript where isCreated=0 and refDataId = ? and projectId='"+projectId+"'";
		for (String sqlScriptId : sqlScriptIds) {
			sqlScript = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSqlScript.class, hql, sqlScriptId.trim());
			sqlScript.analysisResourceProp();
			sqlScript.setIsCreated(1);
			HibernateUtil.updateObjectByHql(sqlScript, null);
		}
		return "success";
	}

	protected String unloadPublishData(String projectId, String publishDataId) {
		return null;
	}
}
