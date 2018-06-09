package com.king.tooth.sys.service.common;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 系统资源服务处理器
 * @author DougLei
 */
public class ComSysResourceService extends AbstractService{
	
	/**
	 * 保存资源信息
	 * @param resource
	 */
	public void saveSysResource(ISysResource iresource){
		ComSysResource resource = iresource.turnToResource();
		HibernateUtil.saveObject(resource , null);
	}

	/**
	 * 删除资源信息
	 * @param resourceId
	 */
	public void deleteSysResource(String resourceId){
		HibernateUtil.executeUpdateBySqlArr(SqlStatementType.DELETE, "delete com_sys_resource where ref_resource_id = ?", resourceId);
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
		
		ComSysResource resource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysResource.class, "from ComSysResource where resourceName = ?", resourceName);
		if(resource == null){
			throw new IllegalArgumentException("不存在请求的资源：" + resourceName);
		}
		if(resource.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的资源被禁用，请联系管理员：" + resourceName);
		}
		if(resource.getValidDate() == null || ((resource.getValidDate().getTime() - System.currentTimeMillis()) < 0)){
			throw new IllegalArgumentException("请求的资源已过期，请联系管理员：" + resourceName);
		}
		return resource;
	}
}
