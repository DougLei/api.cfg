package com.king.tooth.sys.service.common;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql脚本资源服务处理器
 * @author DougLei
 */
public class ComSqlScriptService extends AbstractService {

//	private ComSysResourceService comSysResourceService = new ComSysResourceService();
	
	/**
	 * 添加sql脚本
	 * @param sqlScript
	 */
	public void saveSqlScript(ComSqlScript sqlScript) {
		try {
//			ComSqlScript sql = new ComSqlScript(sqlScript.getSqlScriptCaption(), sqlScript.getSqlScriptResourceName(), sqlScript.getSqlScriptContent());
//			if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().getAccountType() == 0){
//				sql.setIsBuiltin(1);
//				sql.setIsCreatedResource(1);
//			}
//			sql.setComments(sqlScript.getComments());
//			HibernateUtil.saveObject(sql, null);
//			
//			if(CurrentThreadContext.getCurrentAccountOnlineStatus().getAccount().getAccountType() == 0){
//				createSqlScriptModel(sql);
//			}
		} catch (Exception e) {
			Log4jUtil.debug(ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 修改sql脚本
	 * @param sqlScript
	 */
	public void updateSqlScript(ComSqlScript sqlScript) {
		try {
//			ComSqlScript sql = new ComSqlScript(sqlScript.getSqlScriptCaption(), sqlScript.getSqlScriptResourceName(), sqlScript.getSqlScriptContent());
//			sql.setId(sqlScript.getId());
//			
//			sqlScript = findSqlScriptResourceById(sqlScript.getId());
//			sql.setIsBuiltin(sqlScript.getIsBuiltin());
//			sql.setCreateTime(sqlScript.getCreateTime());
//			sql.setCreateUserId(sqlScript.getCreateUserId());
//			sql.setComments(sqlScript.getComments());
//			sql.setIsCreateBuiltinResource(sqlScript.getIsCreateBuiltinResource());
//			sql.setIsDeploymentApp(sqlScript.getIsDeploymentApp());
//			HibernateUtil.updateObject(sql, null);
		} catch (Exception e) {
			Log4jUtil.debug(ExceptionUtil.getErrMsg(e));
		}
	}
	
	/**
	 * 删除sql脚本
	 * @param sqlScriptIdArr
	 */
	public void deleteSqlScript(Object[] sqlScriptIdArr) {
		int len = sqlScriptIdArr.length;
		StringBuilder in = new StringBuilder("");
		if(len == 1){
			in.append(" = ?");
		}else{
			in.append(" in (");
			for (int i=0;i<len;i++) {
				in.append("?").append(",");
			}
			in.setLength(in.length()-1);
			in.append(")");
		}
		
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.DELETE, "delete ComSysResource where refResourceId " + in, sqlScriptIdArr);
		HibernateUtil.executeUpdateByHqlArr(SqlStatementType.UPDATE, "delete ComSqlScript where id " + in, sqlScriptIdArr);
	}
	
//	/**
//	 * 创建sql脚本数据模型
//	 * @param sqlScript
//	 */
//	private void createSqlScriptModel(ComSqlScript sqlScript) {
//		comSysResourceService.insertSysResource(sqlScript);// 将sql脚本资源加入到资源表中
//	}

	//--------------------------------------------------------
	
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
	
	/**
	 * 根据id，查询对应的通用sql脚本资源对象
	 * @param sqlScriptId
	 * @return
	 */
	public ComSqlScript findSqlScriptResourceById(String sqlScriptId) {
		if(StrUtils.isEmpty(sqlScriptId)){
			throw new NullPointerException("请求的资源主键不能为空");
		}
		
		String queryHql = "from ComSqlScript where id = ?";
		ComSqlScript sqlScriptResource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSqlScript.class, queryHql, sqlScriptId);
		if(sqlScriptResource == null){
			throw new IllegalArgumentException("不存在请求的sql脚本资源：" + sqlScriptId);
		}
		return sqlScriptResource;
	}

	//--------------------------------------------------------
	
	/**
	 * 发布sql脚本
	 * @return
	 */
	public void deployingSqlScript(String[] sqlScriptIdArr) {
		
	}

	/**
	 * 删除发布sql脚本
	 * @return
	 */
	public void cancelDeployingSqlScript(String[] sqlScriptIdArr) {
		
	}
}
