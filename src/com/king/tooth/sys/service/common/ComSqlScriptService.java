package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]sql脚本资源服务处理器
 * @author DougLei
 */
public class ComSqlScriptService extends AbstractResourceService {

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
		List<Object> parameters = new ArrayList<Object>(1);
		parameters.add(resourceName);
		ComSqlScript sqlScriptResource = (ComSqlScript) HibernateUtil.executeUniqueQueryByHql(queryHql, parameters);
		if(sqlScriptResource == null){
			throw new IllegalArgumentException("不存在请求的sql脚本资源：" + resourceName);
		}
		if(sqlScriptResource.getIsCreated() == 0){
			throw new IllegalArgumentException("请求的sql脚本资源未被创建，请联系管理员：" + resourceName);
		}
		if(sqlScriptResource.getIsEnabled() == ISysResource.UNENABLED_RESOURCE_STATUS){
			throw new IllegalArgumentException("请求的sql脚本资源被禁用，请联系管理员：" + resourceName);
		}
		return sqlScriptResource;
	}
}
