package com.king.tooth.sys.service.common;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.service.AbstractPublishService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目信息资源对象处理器
 * @author DougLei
 */
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
		
		String operResult = null;
		if(!oldProject.getProjCode().equals(project.getProjCode())){
			if(publishInfoService.validResourceIsPublished(oldProject.getRefDatabaseId(), oldProject.getId(), null)){
				return "该项目已经发布，不能修改项目编码，或取消发布后再修改";
			}
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
		if(publishInfoService.validResourceIsPublished(oldProject.getRefDatabaseId(), oldProject.getId(), null)){
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
}
