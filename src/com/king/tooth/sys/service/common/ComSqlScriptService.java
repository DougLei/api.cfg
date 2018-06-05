package com.king.tooth.sys.service.common;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]sql脚本资源服务处理器
 * @author DougLei
 */
public class ComSqlScriptService extends AbstractResourceService {

	private ComSysResourceService comSysResourceService = new ComSysResourceService();
	
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

	/**
	 * 创建sql脚本数据模型
	 * @param sqlScriptIdArr
	 */
	public void createSqlScriptModel(String[] sqlScriptIdArr) {
		ComSqlScript sqlScript = null;
		for (String sqlScriptId : sqlScriptIdArr) {
			sqlScript = findSqlScriptResourceById(sqlScriptId);
			if(sqlScript != null){
				sqlScript.setIsCreateBuiltinResource(1);
				HibernateUtil.updateObject(sqlScript, null);
				comSysResourceService.insertSysResource(sqlScript);// 将sql脚本资源加入到资源表中
			}
		}
	}

	/**
	 * 删除sql脚本数据模型
	 * @param sqlScriptIdArr
	 */
	public void dropSqlScriptModel(Object[] sqlScriptIdArr) {
		int len = sqlScriptIdArr.length;
		StringBuilder in = new StringBuilder(" in (");
		for (int i=0;i<len;i++) {
			in.append("?").append(",");
		}
		in.setLength(in.length()-1);
		in.append(")");
		
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete com_sys_resource where ref_resource_id " + in, sqlScriptIdArr);
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.UPDATE, "update ComSqlScript set isCreateBuiltinResource=0 where id " + in, sqlScriptIdArr);
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
