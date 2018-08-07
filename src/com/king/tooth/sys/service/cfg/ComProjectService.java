package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.entity.dm.DmPublishBasicData;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目信息表Service
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ComProjectService extends AbstractPublishService {
	
	/**
	 * 验证项目关联的数据库是否存在
	 * @param project
	 * @return operResult
	 */
	private String validProjectRefDatabaseIsExists(ComProject project) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgDatabase where id = ?", project.getRefDatabaseId());
		if(count != 1){
			return "关联的id=["+project.getRefDatabaseId()+"]的数据库信息不存在";
		}
		return null;
	}
	
	/**
	 * 验证项目编码是否存在
	 * @param project
	 * @return operResult
	 */
	private String validProjectCodeIsExists(ComProject project) {
		String hql = "select count("+ResourcePropNameConstants.ID+") from ComProject where projCode = ?";
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(hql, project.getProjCode());
		if(count > 0){
			return "编码为["+project.getProjCode()+"]项目信息已存在";
		}
		return null;
	}
	
	/**
	 * 保存项目
	 * @param project
	 * @return
	 */
	public Object saveProject(ComProject project) {
		String operResult = validProjectRefDatabaseIsExists(project);
		if(operResult == null){
			operResult = validProjectCodeIsExists(project);
		}
		if(operResult == null){
			return HibernateUtil.saveObject(project, null);
		}
		return operResult;
	}

	/**
	 * 修改项目
	 * @param project
	 * @return
	 */
	public Object updateProject(ComProject project) {
		ComProject oldProject = getObjectById(project.getId(), ComProject.class);
		if(oldProject.getIsCreated() == 1){
			return "["+oldProject.getProjName()+"]项目已经发布，不能修改，请先取消发布";
		}
		
		String operResult = null;
		if(!oldProject.getProjCode().equals(project.getProjCode())){
			operResult = validProjectCodeIsExists(project);
		}
		
		if(operResult == null){
			operResult = validProjectRefDatabaseIsExists(project);
		}
		if(operResult == null){
			return HibernateUtil.updateObjectByHql(project, null);
		}
		return operResult;
	}

	/**
	 * 删除项目
	 * @param projectId
	 * @return
	 */
	public String deleteProject(String projectId) {
		if(BuiltinInstance.currentSysBuiltinProjectInstance.getId().equals(projectId)){
			return "禁止删除内置的项目信息";
		}
		ComProject oldProject = getObjectById(projectId, ComProject.class);
		if(oldProject.getIsCreated() == 1){
			return "["+oldProject.getProjName()+"]项目已经发布，无法删除，请先取消发布";
		}
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgProjectTableLinks where leftId = ?", projectId);
		if(count > 0){
			return "该项目下还关联着[表信息]，无法删除，请先取消他们的关联信息";
		}
		count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from CfgProjectSqlLinks where leftId = ?", projectId);
		if(count > 0){
			return "该项目下还关联着[脚本信息]，无法删除，请先取消他们的关联信息";
		}
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete ComProject where "+ResourcePropNameConstants.ID+" = '"+projectId+"'");
		return null;
	}

	/**
	 * 取消项目和[表/sql脚本]的关联信息
	 * @param projectId
	 * @param relationType relationType的值目前包括：table、sql、all
	 * @return
	 */
	public String cancelRelation(String projectId, String relationType) {
		if("all".equals(relationType)){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete CfgProjectTableLinks where leftId = ?", projectId);
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete CfgProjectSqlLinks where leftId = ?", projectId);
		}else if("table".equals(relationType)){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete CfgProjectTableLinks where leftId = ?", projectId);
		}else if("sql".equals(relationType)){
			HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete CfgProjectSqlLinks where leftId = ?", projectId);
		}else{
			return "请传入正确的realtionType";
		}
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布项目
	 * @param projectId
	 * @return
	 */
	private String publishProject(String projectId){
		if(BuiltinInstance.currentSysBuiltinProjectInstance.getId().equals(projectId)){
			return "无法发布配置系统项目";
		}
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project.getIsCreated() == 1){
			return "id为["+projectId+"]的项目已经被发布，无需再次发布，或取消发布后重新发布";
		}
		if(project.getIsNeedDeploy() == 0){
			return "id为["+projectId+"]的项目不该被发布，如需发布，请联系管理员";
		}
		if(project.getIsEnabled() == 0){
			return "id为["+projectId+"]的项目信息无效，请联系管理员";
		}
		if(!publishInfoService.validResourceIsPublished(project.getRefDatabaseId(), null, null)){
			return "["+project.getProjName()+"]项目所属的数据库还未发布，请先发布数据库";
		}
		
		// 将项目id和数据库id映射起来
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(projectId, project.getRefDatabaseId());
		
		publishInfoService.deletePublishedData(null, projectId);
		executeRemotePublish(getAppSysDatabaseId(null), project.getId(), project, 0, null);
		
		modifyIsCreatedPropVal(project.getEntityName(), 1, project.getId());
		return null;
	}
	/**
	 * (All)发布项目
	 * <p>【发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * @param projectId
	 */
	public String publishProjectAll(String projectId) {
		// 发布项目
		String result = publishProject(projectId);
		if(result == null){
			ComProject project = getObjectById(projectId, ComProject.class);
			
			// 给远程系统的内置表中插入基础数据
			result = executeRemoteSaveBasicData(project.getRefDatabaseId(), projectId);
			if(result == null){
				// 记录要发布的数据id集合
				List<Object> publishDataIds;
				// 发布项目模块
				publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
						"select "+ResourcePropNameConstants.ID+" from ComProjectModule where isEnabled =1 and isNeedDeploy=1 and refProjectId = '"+projectId+"'");
				if(publishDataIds != null && publishDataIds.size() > 0){
					new ComProjectModuleService().batchPublishProjectModule(project.getRefDatabaseId(), projectId, publishDataIds);
					publishDataIds.clear();
				}
				
				// 发布公用的表
				ComTabledataService tableService = new ComTabledataService();
				tableService.publishCommonTableResource(project.getRefDatabaseId(), projectId);
				
				// 发布项目关联的表
				publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
						"select table."+ResourcePropNameConstants.ID+" from ComTabledata table, CfgProjectTableLinks pt where pt.rightId = table.id" +
								" and table.isEnabled =1 and table.isNeedDeploy=1" +
								" and pt.leftId='"+projectId+"'");
				if(publishDataIds != null && publishDataIds.size() > 0){
					tableService.batchPublishTable(project.getRefDatabaseId(), projectId, publishDataIds);
					publishDataIds.clear();
				}
				
				// 发布项目关联的sql脚本或通用的sql脚本
				publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
						"select sqlScript."+ResourcePropNameConstants.ID+" from ComSqlScript sqlScript, CfgProjectSqlLinks ps where ps.rightId = sqlScript."+ResourcePropNameConstants.ID +
								" and sqlScript.isEnabled =1 and sqlScript.isNeedDeploy=1" +
								" and (ps.leftId='"+projectId+"' or sqlScript.belongPlatformType="+ISysResource.COMMON_PLATFORM +")");
				if(publishDataIds != null && publishDataIds.size() > 0){
					new ComSqlScriptService().batchPublishSqlScript(project.getRefDatabaseId(), projectId, publishDataIds);
					publishDataIds.clear();
				}
				
				result = usePublishResourceApi(projectId, projectId, "project", "1", 
						BuiltinInstance.currentSysBuiltinProjectInstance.getId());
			}
		}
		return result;
	}

	/**
	 * 取消发布项目
	 * @param projectId
	 * @return
	 */
	private String cancelPublishProject(String projectId){
		if(BuiltinInstance.currentSysBuiltinProjectInstance.getId().equals(projectId)){
			return "无法取消发布配置系统项目";
		}
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project.getIsCreated() == 0){
			return "["+project.getProjName()+"]项目未发布，无法取消发布";
		}
		// 将项目id和数据库id取消映射
		ProjectIdRefDatabaseIdMapping.removeMapping(projectId);
		
		// 远程删除运行系统中的数据库信息
		executeRemoteUpdate(getAppSysDatabaseId(null), null, "delete " + project.getEntityName() + " where "+ResourcePropNameConstants.ID+" = '"+projectId+"'");
		publishInfoService.deletePublishedData(null, projectId);
		
		modifyIsCreatedPropVal(project.getEntityName(), 0, project.getId());
		return null;
	}
	/**
	 * (All)取消发布项目
	 * <p>【取消发布项目的所有信息，包括项目信息，模块信息，表信息，sql脚本信息等】</p>
	 * @param projectId
	 */
	public String cancelPublishProjectAll(String projectId) {
		// 取消发布项目
		String result = cancelPublishProject(projectId);
		if(result == null){
			ComProject project = getObjectById(projectId, ComProject.class);
			
			// 删除远程系统内置表中的所有和项目相关的数据，即projectId=project.getId()的所有数据
			executeRemoteDeleteBasicData(project.getRefDatabaseId(), projectId);
			
			// 记录要发布的数据id集合
			List<Object> publishDataIds;
			// 取消发布项目模块
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select "+ResourcePropNameConstants.ID+" from ComProjectModule where refProjectId = '"+projectId+"'");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComProjectModuleService().batchCancelPublishProjectModule(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			// 取消发布表
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select table."+ResourcePropNameConstants.ID+" from ComTabledata table, CfgProjectTableLinks pt where pt.rightId = table.id" +
							" and table.isNeedDeploy=1 and table.isBuiltin=0" +
							" and pt.leftId='"+projectId+"'");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComTabledataService().batchCancelPublishTable(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			// 取消发布sql脚本
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select sqlScript."+ResourcePropNameConstants.ID+" from ComSqlScript sqlScript, CfgProjectSqlLinks ps where ps.rightId = sqlScript."+ResourcePropNameConstants.ID +
							" and sqlScript.isNeedDeploy=1 and sqlScript.isBuiltin=0" +
							" and (ps.leftId='"+projectId+"' or sqlScript.belongPlatformType="+ISysResource.COMMON_PLATFORM +")");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComSqlScriptService().batchCancelPublishSqlScript(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			result = usePublishResourceApi(projectId, projectId, "project", "-1", 
					BuiltinInstance.currentSysBuiltinProjectInstance.getId());
		}
		return result;
	}
	
	/**
	 * 执行远程保存基础数据操作
	 * @param databaseId 
	 * @param projectId  
	 */
	private String executeRemoteSaveBasicData(String databaseId, String projectId){
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			
			String currentUserId = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId();
			Date currentDate = new Date();
			List<DmPublishBasicData> basicDatas = HibernateUtil.extendExecuteListQueryByHqlArr(DmPublishBasicData.class, null, null, "from DmPublishBasicData where belongPlatformType != "+ISysResource.CONFIG_PLATFORM);// 获取要发布的基础信息集合
			for (DmPublishBasicData basicData : basicDatas) {
				session.save(basicData.getBasicDataResourceName(), basicData.getBasicDataJsonObject(projectId, currentUserId, currentDate));
			}
			basicDatas.clear();
			session.getTransaction().commit();
			return null;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			return "发布基础数据时出现异常：" + ExceptionUtil.getErrMsg("ComProjectService", "executeRemoteSaveBasicData", e);
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
		}
	}
	
	/**
	 * 执行远程删除基础数据操作
	 * @param databaseId 
	 * @param projectId  
	 */
	private void executeRemoteDeleteBasicData(String databaseId, String projectId){
		List<Object> builtinTableTableNames = HibernateUtil.executeListQueryByHqlArr(null, null, 
				"select tableName from ComTabledata where isEnabled =1 and isNeedDeploy=1 and belongPlatformType!="+ISysResource.CONFIG_PLATFORM);
		List<String> deleteHql = new ArrayList<String>(builtinTableTableNames.size());
		for (Object builtinTableTableName : builtinTableTableNames) {
			deleteHql.add("delete " + builtinTableTableName + " where project_id = '"+projectId+"'");
		}
		builtinTableTableNames.clear();
		
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			for (String hql : deleteHql) {
				session.createSQLQuery(hql).executeUpdate();
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
			deleteHql.clear();
		}
	}
}