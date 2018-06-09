package com.king.tooth.sys.service.common;

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
	 * 保存sql脚本
	 * @param sqlScript
	 * @return
	 */
	public String saveSqlScript(ComSqlScript sqlScript) {
		return null;
	}

	/**
	 * 修改sql脚本
	 * @param sqlScript
	 * @return
	 */
	public String updateSqlScript(ComSqlScript sqlScript) {
		return null;
	}

	/**
	 * 删除sql脚本
	 * @param sqlScriptId
	 * @return
	 */
	public String deleteSqlScript(String sqlScriptId) {
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
