package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.AbstractResourceService;
import com.king.tooth.util.ResourceHandlerUtil;
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
		String sql = "insert into com_sys_resource values(?,?,?,?,?,?,?,?)";
		List<Object> parameters = new ArrayList<Object>(8);
		parameters.add(iresource.getResourceName());
		parameters.add(iresource.getResourceType());
		parameters.add(1);
		parameters.addAll(ResourceHandlerUtil.getBasicPropVals(null));
		HibernateUtil.executeUpdateBySql(SqlStatementType.INSERT, sql, parameters);
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
		
		String sql = "select resource_type, is_enabled from com_sys_resource where resource_name = ?";
		Object[] objArr = (Object[]) HibernateUtil.executeUniqueQueryBySqlArr(sql, resourceName);
		if(objArr == null){
			throw new IllegalArgumentException("不存在请求的资源：" + resourceName);
		}
		if(Integer.valueOf(objArr[1].toString()) == 0){
			throw new IllegalArgumentException("请求的资源被禁用，请联系管理员：" + resourceName);
		}
		ComSysResource resource = new ComSysResource();
		resource.setResourceName(resourceName);
		resource.setResourceType(Integer.valueOf(objArr[0].toString()));
		resource.setIsEnabled(Integer.valueOf(objArr[1].toString()));
		return resource;
	}
}
