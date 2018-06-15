package com.king.tooth.sys.service.common;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;

import com.king.tooth.cache.ProjectIdRefDatabaseIdMapping;
import com.king.tooth.constants.CurrentSysInstanceConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComPublishBasicData;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.database.DynamicDBUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目信息资源对象处理器
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
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComDatabase where id = ?", project.getRefDatabaseId());
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
		String hql = "select count("+ResourceNameConstants.ID+") from ComProject where projCode = ?";
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
	public String saveProject(ComProject project) {
		String operResult = validProjectRefDatabaseIsExists(project);
		if(operResult == null){
			operResult = validProjectCodeIsExists(project);
		}
		if(operResult == null){
			HibernateUtil.saveObject(project, null);
		}
		return operResult;
	}

	/**
	 * 修改项目
	 * @param project
	 * @return
	 */
	public String updateProject(ComProject project) {
		ComProject oldProject = getObjectById(project.getId(), ComProject.class);
		if(oldProject == null){
			return "没有找到id为["+project.getId()+"]的项目对象信息";
		}
		if(publishInfoService.validResourceIsPublished(oldProject.getRefDatabaseId(), oldProject.getId(), null, null)){
			return "该项目已经发布，不能修改项目信息，或取消发布后再修改";
		}
		
		String operResult = null;
		if(!oldProject.getProjCode().equals(project.getProjCode())){
			operResult = validProjectCodeIsExists(project);
		}
		
		if(operResult == null){
			operResult = validProjectRefDatabaseIsExists(project);
		}
		if(operResult == null){
			HibernateUtil.updateObjectByHql(project, null);
		}
		return operResult;
	}

	/**
	 * 删除项目
	 * @param projectId
	 * @return
	 */
	public String deleteProject(String projectId) {
		ComProject oldProject = getObjectById(projectId, ComProject.class);
		if(oldProject == null){
			return "没有找到id为["+projectId+"]的项目对象信息";
		}
		if(publishInfoService.validResourceIsPublished(oldProject.getRefDatabaseId(), oldProject.getId(), null, null)){
			return "["+oldProject.getProjName()+"]项目已经发布，无法删除，请先取消发布";
		}
		
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProjectComTabledataLinks where leftId = ?", projectId);
		if(count > 0){
			return "该项目下还关联着[表信息]，无法删除，请先取消他们的关联信息";
		}
		count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComProjectComSqlScriptLinks where leftId = ?", projectId);
		if(count > 0){
			return "该项目下还关联着[脚本信息]，无法删除，请先取消他们的关联信息";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProject where id = '"+projectId+"'");
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
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProjectComTabledataLinks where leftId = ?", projectId);
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProjectComSqlScriptLinks where leftId = ?", projectId);
		}else if("table".equals(relationType)){
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProjectComTabledataLinks where leftId = ?", projectId);
		}else if("sql".equals(relationType)){
			HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComProjectComSqlScriptLinks where leftId = ?", projectId);
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
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "没有找到id为["+projectId+"]的项目对象信息";
		}
		if(project.getIsCreated() == 1){
			return "id为["+projectId+"]的项目已经被发布，不能重复发布";
		}
		if(project.getIsNeedDeploy() == 0){
			return "id为["+projectId+"]的项目不该被发布，如需发布，请联系管理员";
		}
		if(project.getIsEnabled() == 0){
			return "id为["+projectId+"]的项目信息无效，请联系管理员";
		}
		if(publishInfoService.validResourceIsPublished(project.getRefDatabaseId(), null, null, null)){
			return "["+project.getProjName()+"]项目所属的数据库还未发布，请先发布数据库";
		}
		if(publishInfoService.validResourceIsPublished(null, project.getId(), null, null)){
			return "["+project.getProjName()+"]项目已经发布，无需再次发布，或取消发布后重新发布";
		}
		
		// 将项目id和数据库id映射起来
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(projectId, project.getRefDatabaseId());
		
		publishInfoService.deletePublishedData(null, projectId);
		executeRemotePublish(getAppSysDatabaseId(null), project.getId(), project, 0, null);
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
			
			project.setIsCreated(1);
			HibernateUtil.updateObject(project, null);
			
			// 给远程系统的内置表中插入基础数据
			executeRemoteSaveBasicData(project.getRefDatabaseId(), projectId);
			
			// 记录要发布的数据id集合
			List<Object> publishDataIds;
			// 发布项目模块
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select "+ResourceNameConstants.ID+" from ComProjectModule where isEnabled =1 and isNeedDeploy=1 and refProjectId = '"+projectId+"'");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComProjectModuleService().batchPublishProjectModule(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			// 发布表
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select table."+ResourceNameConstants.ID+" from ComTabledata table left join ComProjectComTabledataLinks pt on(pt.rightId = table.id)" +
							"where table.isEnabled =1 and table.isNeedDeploy=1 and table.isBuiltin=0" +
							" and pt.leftId='"+projectId+"'");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComTabledataService().batchPublishTable(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			// 发布sql脚本
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select sqlScript."+ResourceNameConstants.ID+" from ComSqlScript sqlScript left join ComProjectComSqlScriptLinks ps on(ps.rightId = sqlScript.id)" +
							"where sqlScript.isEnabled =1 and sqlScript.isNeedDeploy=1 and sqlScript.isBuiltin=0" +
							" and (ps.leftId='"+projectId+"' or sqlScript.belongPlatformType="+ISysResource.COMMON_PLATFORM );
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComSqlScriptService().batchPublishSqlScript(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			result = useLoadPublishApi(projectId, projectId, "project", "1", 
					CurrentSysInstanceConstants.currentSysBuiltinProjectInstance.getId());
		}
		return result;
	}

	/**
	 * 取消发布项目
	 * @param projectId
	 * @return
	 */
	private String cancelPublishProject(String projectId){
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "没有找到id为["+projectId+"]的项目对象信息";
		}
		if(project.getIsCreated() == 1){
			return "["+project.getProjName()+"]项目未发布，无法取消发布";
		}
		// 将项目id和数据库id取消映射
		ProjectIdRefDatabaseIdMapping.removeMapping(projectId);
		
		// 远程删除运行系统中的数据库信息
		executeRemoteUpdate(getAppSysDatabaseId(null), null, "delete " + project.getEntityName() + " where id = '"+projectId+"'");
		publishInfoService.deletePublishedData(null, projectId);
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
			
			project.setIsCreated(0);
			HibernateUtil.updateObject(project, null);
			
			// 删除远程系统内置表中的所有和项目相关的数据，即projectId=project.getId()的所有数据
			executeRemoteDeleteBasicData(project.getRefDatabaseId(), projectId);
			
			// 记录要发布的数据id集合
			List<Object> publishDataIds;
			// 取消发布项目模块
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select "+ResourceNameConstants.ID+" from ComProjectModule where refProjectId = '"+projectId+"'");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComProjectModuleService().batchCancelPublishProjectModule(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			// 取消发布表
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select table."+ResourceNameConstants.ID+" from ComTabledata table left join ComProjectComTabledataLinks pt on(pt.rightId = table.id)" +
							"where table.isEnabled =1 and table.isNeedDeploy=1 and table.isBuiltin=0" +
							" and pt.leftId='"+projectId+"'");
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComTabledataService().batchCancelPublishTable(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			// 取消发布sql脚本
			publishDataIds = HibernateUtil.executeListQueryByHqlArr(null, null, 
					"select sqlScript."+ResourceNameConstants.ID+" from ComSqlScript sqlScript left join ComProjectComSqlScriptLinks ps on(ps.rightId = sqlScript.id)" +
							"where sqlScript.isEnabled =1 and sqlScript.isNeedDeploy=1 and sqlScript.isBuiltin=0" +
							" and (ps.leftId='"+projectId+"' or sqlScript.belongPlatformType="+ISysResource.COMMON_PLATFORM );
			if(publishDataIds != null && publishDataIds.size() > 0){
				new ComSqlScriptService().batchCancelPublishSqlScript(project.getRefDatabaseId(), projectId, publishDataIds);
				publishDataIds.clear();
			}
			
			result = useLoadPublishApi(projectId, projectId, "project", "-1", 
					CurrentSysInstanceConstants.currentSysBuiltinProjectInstance.getId());
		}
		return result;
	}
	
	/**
	 * 执行远程保存基础数据操作
	 * @param databaseId 
	 * @param projectId  
	 */
	private void executeRemoteSaveBasicData(String databaseId, String projectId){
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			// 最后可以根据实际开发的结果，决定这里到底需不需要分页
			int count = (int) HibernateUtil.executeUniqueQueryBySql(
					"select count(1) from com_publish_basic_data where is_enabled=1 and is_need_deploy=1 and belong_platform_type!="+ISysResource.CONFIG_PLATFORM, null);
			int loopCount = count/50 + 1;
			List<ComPublishBasicData> basicDatas;// 获取要发布的基础信息集合
			for(int i=0;i<loopCount;i++){
				basicDatas = HibernateUtil.extendExecuteListQueryByHqlArr(ComPublishBasicData.class, "50", (i+1)+"", 
						"from ComPublishBasicData where isEnabled=1 and isNeedDeploy=1 and belongPlatformType != "+ISysResource.CONFIG_PLATFORM);
				for (ComPublishBasicData basicData : basicDatas) {
					session.save(basicData.getBasicDataResourceName(), basicData.getBasicDataJsonObject(projectId));
				}
				basicDatas.clear();
			}
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
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
		StringBuilder hql = new StringBuilder();
		for (Object builtinTableTableName : builtinTableTableNames) {
			hql.append("delete ").append(builtinTableTableName).append(" where project_id = '"+projectId+"'");
		}
		builtinTableTableNames.clear();
		
		SessionFactoryImpl sessionFactory = DynamicDBUtil.getSessionFactory(databaseId);
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createSQLQuery(hql.toString()).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		}finally{
			if(session != null){
				session.flush();
				session.close();
			}
			hql.setLength(0);
		}
	}

	//--------------------------------------------------------------------------------------------------------
	protected String loadPublishData(String projectId, String publishDataId) {
		ComProject project = getObjectById(projectId, ComProject.class);
		if(project == null){
			return "没有找到id为["+projectId+"]的项目对象信息，运行系统无法加载";
		}
		ProjectIdRefDatabaseIdMapping.setProjRefDbMapping(projectId, project.getRefDatabaseId());
		return "success";
	}

	protected String unloadPublishData(String projectId, String publishDataId) {
		ProjectIdRefDatabaseIdMapping.removeMapping(projectId);
		return "success";
	}
}
