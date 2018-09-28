package com.king.tooth.sys.service.cfg;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.service.AService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 项目信息表Service
 * @author DougLei
 */
@Service
public class CfgProjectService extends AService {
	
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
		
		String operResult = null;
		if(!oldProject.getProjCode().equals(project.getProjCode())){
			operResult = validProjectCodeIsExists(project);
		}
		
		if(operResult == null){
			operResult = validProjectRefDatabaseIsExists(project);
		}
		if(operResult == null){
			return HibernateUtil.updateObject(project, null);
		}
		return operResult;
	}

	/**
	 * 删除项目
	 * @param projectId
	 * @return
	 */
	public String deleteProject(String projectId) {
		if(BuiltinObjectInstance.currentSysBuiltinProjectInstance.getId().equals(projectId)){
			return "禁止删除内置的项目信息";
		}
		getObjectById(projectId, ComProject.class);
		
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
}
