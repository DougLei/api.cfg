package com.king.tooth.sys.service.common;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql脚本资源服务处理器
 * @author DougLei
 */
public class ComSqlScriptService extends AbstractService {

	/**
	 * 验证sql脚本资源名是否存在
	 * @param table
	 * @return operResult
	 */
	private String validsqlScriptResourceNameIsExists(ComSqlScript sqlScript) {
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourceNameConstants.ID+") from ComSqlScript where sqlScriptResourceName = ? and createUserId = ?", sqlScript.getSqlScriptResourceName(), CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(count > 0){
			return "sql脚本资源名为["+sqlScript.getSqlScriptResourceName()+"]的已存在";
		}
		return null;
	}
	
	/**
	 * 保存sql脚本
	 * @param sqlScript
	 * @return
	 */
	public String saveSqlScript(ComSqlScript sqlScript) {
		String operResult = validsqlScriptResourceNameIsExists(sqlScript);
		if(operResult == null){
			if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper()){
				/* 属于直接就建模，不需要用户再进行一次建模的操作(表是因为，添加了表信息后，还要添加列信息，然后才能建模) */
				// 如果是平台开发者，则要解析出sql脚本内容，再保存
				sqlScript.analysisResourceProp();
				sqlScript.setIsCreated(1);
				// 保存到资源表中
				new ComSysResourceService().saveSysResource(sqlScript);
			}
			HibernateUtil.saveObject(sqlScript, null);
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
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		
		String operResult = null;
		if(!isPlatformDeveloper && !oldSqlScript.getSqlScriptResourceName().equals(sqlScript.getSqlScriptResourceName())){
			if(oldSqlScript.getIsDeployed() == 1){
				return "该sql资源已经发布，不能修改sql资源名，或取消发布后再修改";
			}
			operResult = validsqlScriptResourceNameIsExists(sqlScript);
		}
		if(operResult == null){
			if(isPlatformDeveloper){
				sqlScript.analysisResourceProp();
				sqlScript.setIsCreated(1);
			}
			HibernateUtil.updateObjectByHql(sqlScript, null);
		}
		return operResult;
	}

	/**
	 * 删除sql脚本
	 * @param sqlScriptId
	 * @return
	 */
	public String deleteSqlScript(String sqlScriptId) {
		ComSqlScript oldSqlScript = getObjectById(sqlScriptId, ComSqlScript.class);
		if(oldSqlScript == null){
			return "没有找到id为["+sqlScriptId+"]的sql脚本对象信息";
		}
		boolean isPlatformDeveloper = CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().isPlatformDeveloper();
		if(!isPlatformDeveloper && oldSqlScript.getIsDeployed() == 1){
			return "该sql脚本已经发布，无法删除，请先取消发布";
		}
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComSqlScript where id = '"+sqlScriptId+"'");
		
		// 如果是平台开发者账户，还要删除资源信息
		if(isPlatformDeveloper){
			new ComSysResourceService().deleteSysResource(sqlScriptId);
		}
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
		
		String queryHql = "from ComSqlScript where sqlScriptResourceName = ?";
		ComSqlScript sqlScriptResource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSqlScript.class, queryHql, resourceName);
		if(sqlScriptResource == null){
			throw new IllegalArgumentException("不存在请求的sql脚本资源：" + resourceName);
		}
		return sqlScriptResource;
	}
}
