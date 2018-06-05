package com.king.tooth.sys.service.common;

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
	 * @param resource
	 */
	public void insertSysResource(ISysResource iresource){
		ComSysResource resource = new ComSysResource();
		resource.setRefResourceId(iresource.getResourceId());
		resource.setResourceName(iresource.getResourceName());
		resource.setResourceType(iresource.getResourceType());
		resource.setReqResourceMethod(iresource.getReqResourceMethod());
		resource.setIsEnabled(1);
		HibernateUtil.saveObject(resource , "保存资源");
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
		
		ComSysResource resource = (ComSysResource) HibernateUtil.executeUniqueQueryByHqlArr("from ComSysResource where resourceName = ?", resourceName);
		if(resource == null){
			throw new IllegalArgumentException("不存在请求的资源：" + resourceName);
		}
		if(resource.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的资源被禁用，请联系管理员：" + resourceName);
		}
		return resource;
	}
}
