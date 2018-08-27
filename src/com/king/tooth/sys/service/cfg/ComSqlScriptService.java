package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.thread.CurrentThreadContext;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql脚本信息表Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComSqlScriptService extends AbstractPublishService {
	
	/**
	 * 根据id，获取sql脚本资源对象
	 * @param sqlScriptId
	 * @return
	 */
	public ComSqlScript findSqlScriptResourceById(String sqlScriptId){
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(sqlScript == null){
			throw new NullPointerException("不存在请求的sql脚本资源，请联系管理员");
		}
		if(sqlScript.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的sql脚本资源被禁用，请联系管理员");
		}
		if(StrUtils.isEmpty(sqlScript.getSqlScriptParameters())){
			sqlScript.setSqlScriptParameterList(findSqlScriptParameters(sqlScriptId));
		}
		return sqlScript;
	}
	
	/**
	 * 验证sql脚本资源名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validSqlScriptResourceNameIsExists(ComSqlScript sqlScript) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComSqlScript where sqlScriptResourceName = ? and createUserId = ? and customerId = ?", sqlScript.getSqlScriptResourceName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId(), CurrentThreadContext.getCustomerId());
		if(count > 0){
			return "您已经创建过相同sql脚本资源名["+sqlScript.getSqlScriptResourceName()+"]的数据";
		}
		count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from SysResource where resourceName = ? and projectId = ? and customerId = ?", sqlScript.getSqlScriptResourceName(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(count > 0){
			return "系统中已经存在相同的资源名["+sqlScript.getSqlScriptResourceName()+"]的数据，请修sql脚本资源名";
		}
		return null;
	}
	
	/**
	 * 验证sql脚本关联的项目是否存在
	 * @param project
	 * @return operResult
	 */
	private String validSqlScriptRefProjIsExists(String projectId) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from ComProject where id = ?", projectId);
		if(count != 1){
			return "sql脚本关联的，id为["+projectId+"]的项目信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证一个项目中，是否存在同名的sql脚本资源名
	 * @param sqlScriptResourceName
	 * @param projectId
	 * @return
	 */
	private String validSameResourceNameSqlScriptInProject(String sqlScriptResourceName, String projectId) {
		String hql = "select count(cs."+ResourcePropNameConstants.ID+") from " +
				"ComProject p, CfgProjectSqlLinks ps, ComSqlScript cs " +
				"where p.id = '"+projectId+"' and p.id = ps.leftId and cs.id = ps.rightId and cs.sqlScriptResourceName = '"+sqlScriptResourceName+"'";
		long count = (long) HibernateUtil.executeUniqueQueryByHql(hql, null);
		if(count > 0){
			return "项目关联的sql脚本中已经存在sql脚本资源名为["+sqlScriptResourceName+"]的数据";
		}
		return null;
	}
	
	/**
	 * 保存sql脚本对象时，解析出参数集合，并保存到sqlScriptParameter字段中
	 * @param isDeleteBeforeSqlScriptParameterDatas 是否清空之前的sql脚本参数数据
	 * @param sqlScriptParameterList
	 * @param sqlScriptId
	 */
	private void saveSqlScriptParameter(boolean isDeleteBeforeSqlScriptParameterDatas, List<ComSqlScriptParameter> sqlScriptParameterList, String sqlScriptId){
		if(isDeleteBeforeSqlScriptParameterDatas){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComSqlScriptParameter where sqlScriptId = ?", sqlScriptId);
		}
		
		for (ComSqlScriptParameter sqlScriptParameter : sqlScriptParameterList) {
			sqlScriptParameter.setSqlScriptId(sqlScriptId);
			HibernateUtil.saveObject(sqlScriptParameter, null);
		}
	}
	
	/**
	 * 保存sql脚本
	 * @param sqlScript
	 * @return
	 */
	public Object saveSqlScript(ComSqlScript sqlScript) {
		String operResult = validSqlScriptResourceNameIsExists(sqlScript);
		if(operResult == null){
			// TODO 单项目，取消是否平台开发者的判断
//			boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
			
			String projectId = CurrentThreadContext.getConfProjectId();
			
			// TODO 单项目，取消是否平台开发者的判断
//			if(!isDeveloper){// 非平台开发者，建的sql脚本一开始，一定要和一个项目关联起来
				if(StrUtils.isEmpty(projectId)){
					return "sql脚本关联的项目id不能为空！";
				}
				operResult = validSqlScriptRefProjIsExists(projectId);
				if(operResult == null){
					operResult = validSameResourceNameSqlScriptInProject(sqlScript.getSqlScriptResourceName(), projectId);
				}
//			}
			
			if(operResult == null){
				JSONObject sqlScriptJsonObject = HibernateUtil.saveObject(sqlScript, null);
				
				String sqlScriptId = sqlScriptJsonObject.getString(ResourcePropNameConstants.ID);
				if(sqlScript.getSqlScriptParameterList() != null && sqlScript.getSqlScriptParameterList().size() >0 ){
					saveSqlScriptParameter(false, sqlScript.getSqlScriptParameterList(), sqlScriptId);
				}
				
				// TODO 单项目，取消是否平台开发者的判断
//				if(isDeveloper){
					// 因为保存资源数据的时候，需要sqlScript对象的id，所以放到最后
					sqlScript.setId(sqlScriptId);
					new SysResourceService().saveSysResource(sqlScript);
//				}
				
				// TODO 单项目，取消是否平台开发者的判断
				// 保存sql脚本和项目的关联关系
//				if(isDeveloper){
//					HibernateUtil.saveDataLinks("CfgProjectSqlLinks", CurrentThreadContext.getProjectId(), sqlScriptId);
//				}else{
					HibernateUtil.saveDataLinks("CfgProjectSqlLinks", projectId, sqlScriptId);
//				}
				return sqlScriptJsonObject;
			}
		}
		return operResult;
	}

	/**
	 * 修改sql脚本
	 * @param sqlScript
	 * @return
	 */
	public Object updateSqlScript(ComSqlScript sqlScript) {
		ComSqlScript oldSqlScript = getObjectById(sqlScript.getId(), ComSqlScript.class);
		String operResult = null;
		if(!oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
			operResult = validSqlScriptResourceNameIsExists(sqlScript);
		}
		
		if(operResult == null){
			// TODO 单项目，取消是否平台开发者的判断
//			boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
			
			String projectId = CurrentThreadContext.getConfProjectId();
			
			// TODO 单项目，取消是否平台开发者的判断
//			if(!isDeveloper){
				if(StrUtils.isEmpty(projectId)){
					return "sql脚本关联的项目id不能为空！";
				}
				operResult = validSqlScriptRefProjIsExists(projectId);
				if(operResult == null && !oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
					operResult = validSameResourceNameSqlScriptInProject(sqlScript.getSqlScriptResourceName(), projectId);
				}
				
				// TODO 单项目，取消是否平台开发者的判断
//				if(operResult == null && publishInfoService.validResourceIsPublished(null, projectId, oldSqlScript.getId())){
//					return "该sql脚本已经发布，不能修改sql脚本信息，或取消发布后再修改";
//				}
//			}
			
			// TODO 单项目，取消是否平台开发者的判断
//			if(isDeveloper && !oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
			if(!oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
				// 如果修改了sql脚本的资源名，也要同步修改SysResource表中的资源名
				new SysResourceService().updateResourceName(sqlScript.getId(), sqlScript.getSqlScriptResourceName());
			}
			if(operResult == null){
				if(sqlScript.getIsAnalysisParameters() == 1 && sqlScript.getSqlScriptParameterList() != null && sqlScript.getSqlScriptParameterList().size() >0){
					String sqlScriptId = sqlScript.getId(); 
					saveSqlScriptParameter(true, sqlScript.getSqlScriptParameterList(), sqlScriptId);
				}
				sqlScript.setSqlQueryResultColumns("");
				return HibernateUtil.updateObject(sqlScript, null);
			}
		}
		return operResult;
	}
	
	/**
	 * 删除sql脚本
	 * @param sqlScriptId
	 * @return
	 */
	public String deleteSqlScript(String sqlScriptId) {
		ComSqlScript sql = getObjectById(sqlScriptId, ComSqlScript.class);
		
		// TODO 单项目，取消是否平台开发者的判断
//		ComSqlScript oldSqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
//		boolean isDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper();
//		if(!isDeveloper){
//			if(publishInfoService.validResourceIsPublished(null, CurrentThreadContext.getConfProjectId(), oldSqlScript.getId())){
//				return "该sql脚本已经发布，无法删除，请先取消发布";
//			}
//		}
		
		List<JSONObject> datalinks = HibernateUtil.queryDataLinks("CfgProjectSqlLinks", null, sqlScriptId);
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
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComSqlScript where "+ResourcePropNameConstants.ID+" = ?", sql.getId());
		HibernateUtil.deleteDataLinks("CfgProjectSqlLinks", null, sqlScriptId);
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComSqlScriptParameter where sqlScriptId = ?", sql.getId());
		
		// 如果是平台开发者账户，还要删除资源信息
		// TODO 单项目，取消是否平台开发者的判断
//		if(isDeveloper){
			new SysResourceService().deleteSysResource(sqlScriptId);
//		}
			
		// 删除sql脚本资源时，如果是视图、存储过程等，还需要drop对应的对象【删除数据库对象】
		HibernateUtil.dropObject(sql);
			
		sql.clear();
		return null;
	}

	/**
	 * 创建sql脚本对象
	 * <p>存储过程、视图等</p>
	 * @param ijson
	 * @return
	 */
	public Object immediateCreate(IJson ijson) {
		int len = ijson.size();
		List<ComSqlScript> sqls = new ArrayList<ComSqlScript>(len);
		
		StringBuilder updateHql = new StringBuilder("update ComSqlScript set isCreated=1 where id in (");
		ComSqlScript tmpSql;
		for(int i=0;i<len ;i++){
			tmpSql = getObjectById(ijson.get(i).getString(ResourcePropNameConstants.ID), ComSqlScript.class);
			if(BuiltinDatabaseData.PROCEDURE.equals(tmpSql.getSqlScriptType()) || BuiltinDatabaseData.VIEW.equals(tmpSql.getSqlScriptType())){
				sqls.add(tmpSql);
				updateHql.append("'").append(tmpSql.getId()).append("',");
			}else{
				return "创建资源名为["+tmpSql.getSqlScriptResourceName()+"]的sql对象出错，系统目前只支持在数据库中创建 [存储过程] 和 [视图]";
			}
		}
		updateHql.setLength(updateHql.length()-1);
		updateHql.append(")");
		
		try {
			HibernateUtil.createObjects(sqls);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.UPDATE, updateHql.toString(), null);
		} catch (Exception e) {
			return ExceptionUtil.getErrMsg("ComSqlScriptService", "immediateCreate", e);
		} finally {
			for (ComSqlScript sql : sqls) {
				sql.clear();
			}
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
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		String operResult = validSameResourceNameSqlScriptInProject(sqlScript.getSqlScriptResourceName(), projectId);
		if(operResult == null){
			HibernateUtil.saveDataLinks("CfgProjectSqlLinks", projectId, sqlScriptId);
		}
		return operResult;
	}
	
	/**
	 * 取消项目和sql脚本的关联关系
	 * @param projectId
	 * @param sqlScriptId
	 * @return
	 */
	public String cancelProjSqlScriptRelation(String projectId, String sqlScriptId) {
		HibernateUtil.deleteDataLinks("CfgProjectSqlLinks", projectId, sqlScriptId);
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布sql脚本
	 * @param sqlScriptId
	 * @return
	 */
	public String publishSqlScript(String sqlScriptId) {
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(sqlScript.getIsNeedDeploy() == 0){
			return "id为["+sqlScriptId+"]的sql脚本不该被发布，如需发布，请联系管理员";
		}
		if(sqlScript.getIsEnabled() == 0){
			return "id为["+sqlScriptId+"]的sql脚本信息无效，请联系管理员";
		}
		String projectId = CurrentThreadContext.getConfProjectId();
		if(publishInfoService.validResourceIsPublished(null, projectId, sqlScriptId)){
			return "["+sqlScript.getSqlScriptResourceName()+"]sql脚本已经发布，无需再次发布，或取消发布后重新发布";
		}
		
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "sql脚本关联的，id为["+projectId+"]的项目信息不存在";
		}
		if(project.getIsCreated() == 0){
			return "["+sqlScript.getSqlScriptResourceName()+"]sql脚本所属的项目还未发布，请先发布项目";
		}
		sqlScript.doSetSqlScriptParameterList(findSqlScriptParameters(sqlScriptId));
		
		publishInfoService.deletePublishedData(projectId, sqlScriptId);
		sqlScript.setRefDatabaseId(project.getRefDatabaseId());
		sqlScript.setProjectId(projectId);
		executeRemotePublish(project.getRefDatabaseId(), projectId, sqlScript, 1, "CfgProjectSqlLinks");
		
		return null;
	}
	
	/**
	 * 取消发布sql脚本
	 * @param sqlScriptId
	 * @return
	 */
	public String cancelPublishSqlScript(String sqlScriptId) {
		ComSqlScript sqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		String projectId = CurrentThreadContext.getConfProjectId();
		if(!publishInfoService.validResourceIsPublished(null, projectId, sqlScriptId)){
			return "["+sqlScript.getSqlScriptResourceName()+"]sql脚本未发布，无法取消发布";
		}
		String result = validSqlScriptRefProjIsExists(projectId);
		if(result == null){
			executeRemoteUpdate(null, projectId, 
					"delete CfgProjectSqlLinks where projectId='"+projectId+"' and leftId='"+projectId+"' and rightId in (select "+ResourcePropNameConstants.ID+" from "+sqlScript.getEntityName()+" where projectId='"+projectId+"' and refDataId = '"+sqlScriptId+"')",
					"delete " + sqlScript.getEntityName() + " where projectId='"+projectId+"' and refDataId='"+sqlScriptId+"'",
					"delete SysResource where projectId='"+projectId+"' and refDataId = '"+sqlScriptId+"'");
			publishInfoService.deletePublishedData(projectId, sqlScriptId);
		}
		return result;
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
			
			if(publishInfoService.validResourceIsPublished(null, projectId, sqlScript.getId())){
				Log4jUtil.info("["+sqlScript.getSqlScriptResourceName()+"]sql脚本已经发布，无需再次发布，或取消发布后重新发布");
				continue;
			}
			sqlScriptIdStr.append(sqlScriptId).append(",");
			
			sqlScript.doSetSqlScriptParameterList(findSqlScriptParameters(sqlScript.getId()));
			sqlScript.setRefDatabaseId(databaseId);
			sqlScript.setProjectId(projectId);
			sqlScripts.add(sqlScript);
		}
		
		publishInfoService.batchDeletePublishedData(null, sqlScriptIds);
		executeRemoteBatchPublish(databaseId, projectId, sqlScripts, 1, "CfgProjectSqlLinks");
		sqlScripts.clear();
		
		sqlScriptIdStr.setLength(sqlScriptIdStr.length()-1);
		usePublishResourceApi(sqlScriptIdStr.toString(), projectId, "sql", "1", projectId);
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
	/**
	 * 根据sql脚本id，查询对应的参数集合
	 * @param sqlScriptId
	 * @return
	 */
	private List<ComSqlScriptParameter> findSqlScriptParameters(String sqlScriptId) {
		return HibernateUtil.extendExecuteListQueryByHqlArr(
				ComSqlScriptParameter.class, null, null, "from ComSqlScriptParameter where sqlScriptId = ? order by orderCode asc", sqlScriptId);
	}
	
	/**
	 * 添加sql脚本参数
	 * @param sqlScriptParameters
	 * @return
	 */
	public Object saveSqlScriptParameter(List<ComSqlScriptParameter> sqlScriptParameters) {
		String sqlScriptId = sqlScriptParameters.get(0).getSqlScriptId();
		getObjectById(sqlScriptId, ComSqlScript.class);
		
		JSONArray jsonArray = new JSONArray(sqlScriptParameters.size());
		for (ComSqlScriptParameter comSqlScriptParameter : sqlScriptParameters) {
			jsonArray.add(HibernateUtil.saveObject(comSqlScriptParameter, null));
		}
		return jsonArray;
	}

	/**
	 * 修改sql脚本参数
	 * @param sqlScriptParameters
	 * @return
	 */
	public Object updateSqlScriptParameter(List<ComSqlScriptParameter> sqlScriptParameters) {
		String sqlScriptId = sqlScriptParameters.get(0).getSqlScriptId();
		getObjectById(sqlScriptId, ComSqlScript.class);
		
		JSONArray jsonArray = new JSONArray(sqlScriptParameters.size());
		for (ComSqlScriptParameter comSqlScriptParameter : sqlScriptParameters) {
			jsonArray.add(HibernateUtil.updateObject(comSqlScriptParameter, null));
		}
		return jsonArray;
	}

	/**
	 * 删除sql脚本参数
	 * @param sqlScriptParameterIds
	 * @return
	 */
	public String deleteSqlScriptParameter(String sqlScriptParameterIds) {
		deleteDataById("ComSqlScriptParameter", sqlScriptParameterIds);
		return null;
	}
}
