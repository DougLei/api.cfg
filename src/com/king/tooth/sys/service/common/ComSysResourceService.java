package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [通用的]系统资源服务处理器
 * @author DougLei
 */
public class ComSysResourceService extends AbstractResourceService{

	/**
	 * 添加一条新资源
	 * @param databaseId
	 * @param resource
	 */
	public void insertSysResource(String databaseId, ISysResource iresource){
		ComSysResource resource = new ComSysResource();
		resource.setDatabaseId(databaseId);
		resource.setIsEnabled(iresource.getIsEnabled());
		resource.setRefResourceId(iresource.getRefResourceId());
		resource.setResourceName(iresource.getResourceName());
		resource.setResourceType(iresource.getResourceType());
		HibernateUtil.saveObject(resource, "添加一条新资源");
	}
	
	/**
	 * 删除指定的资源
	 * @param refResourceIds
	 */
	public void deleteSysResource(List<Object> refResourceIds) {
		int len = refResourceIds.size();
		if(len > 0){
			StringBuilder hql = new StringBuilder("delete ComSysResource where refResourceId in (");
			for(int i=0;i<len ;i++){
				hql.append("?,");
			}
			hql.setLength(hql.length()-1);
			hql.append(")");
			HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, hql.toString(), refResourceIds);
		}
	}
	
	/**
	 * 根据资源名，查询资源对象
	 * @param resourceName
	 * @return
	 */
	public ComSysResource findResourceByResourceName(String resourceName) {
		if(StrUtils.isEmpty(resourceName)){
			throw new NullPointerException("请求的资源名不能为空");
		}
		
		String queryHql = "from ComSysResource where resourceName = ?";
		List<Object> parameters = new ArrayList<Object>(1);
		parameters.add(resourceName);
		ComSysResource resource = (ComSysResource) HibernateUtil.executeUniqueQueryByHql(queryHql, parameters);
		if(resource == null){
			throw new IllegalArgumentException("不存在请求的资源：" + resourceName);
		}
		if(resource.getIsEnabled() == ISysResource.UNENABLED_RESOURCE_STATUS){
			throw new IllegalArgumentException("请求的资源被禁用，请联系管理员：" + resourceName);
		}
		return resource;
	}
}
