package com.king.tooth.sys.service.sys;

import com.king.tooth.annotation.Service;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 资源信息表Service
 * @author DougLei
 */
@Service
public class SysResourceService extends AbstractService{
	
	/**
	 * 保存资源信息
	 * <p>***保存的时候，确保传进来iresource的id有值***</p>
	 * @param resource
	 */
	public void saveSysResource(ISysResource iresource){
		SysResource resource = iresource.turnToResource();
		HibernateUtil.saveObject(resource , null);
	}

	/**
	 * 删除资源信息
	 * @param resourceId
	 */
	public void deleteSysResource(String resourceId){
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.DELETE, "delete SysResource where refResourceId = ? and projectId = ?", resourceId, CurrentThreadContext.getProjectId());
	}
	
	/**
	 * 根据资源名，查询资源对象
	 * @param resourceName
	 * @return
	 */
	public SysResource findResourceByResourceName(String resourceName) {
		if(StrUtils.isEmpty(resourceName)){
			throw new NullPointerException("请求的资源名不能为空");
		}
		
		SysResource resource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(SysResource.class, "from SysResource where resourceName = ? and projectId = ? and customerId = ?", resourceName, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
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
		HibernateUtil.executeUpdateByHqlArr(BuiltinDatabaseData.UPDATE, "update SysResource set resourceName = ? where refResourceId = ? and projectId = ?", resourceName, refResourceId, CurrentThreadContext.getProjectId());
	}
}
