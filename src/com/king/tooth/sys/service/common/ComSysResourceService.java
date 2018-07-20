package com.king.tooth.sys.service.common;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
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
	 * <p>***保存的时候，确保传进来iresource的id有值***</p>
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
		HibernateUtil.executeUpdateBySqlArr(BuiltinDatabaseData.DELETE, "delete com_sys_resource where ref_resource_id = ? and project_id = ?", resourceId, CurrentThreadContext.getProjectId());
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
		
		ComSysResource resource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysResource.class, "from ComSysResource where resourceName = ? and projectId = ? and customerId = ?", resourceName, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCurrentAccountOnlineStatus().getCurrentCustomerId());
		if(resource == null){
			throw new IllegalArgumentException("不存在请求的资源：" + resourceName);
		}
		if(resource.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的资源被禁用，请联系管理员：" + resourceName);
		}
		return resource;
	}

	/**
	 * 修改资源名
	 * @param refResourceId
	 * @param resourceName
	 */
	public void updateResourceName(String refResourceId, String resourceName) {
		HibernateUtil.executeUpdateBySqlArr(BuiltinDatabaseData.UPDATE, "update com_sys_resource set resource_name = ? where ref_resource_id = ? and project_id = ?", resourceName, refResourceId, CurrentThreadContext.getProjectId());
	}
}
