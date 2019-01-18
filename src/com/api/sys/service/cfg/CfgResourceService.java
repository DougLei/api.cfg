package com.api.sys.service.cfg;

import com.api.annotation.Service;
import com.api.constants.ResourcePropNameConstants;
import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.cfg.CfgResource;
import com.api.sys.entity.cfg.ICfgResource;
import com.api.sys.service.AService;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;

/**
 * 资源信息表Service
 * @author DougLei
 */
@Service
public class CfgResourceService extends AService{
	
	/**
	 * 保存资源信息
	 * <p>***保存的时候，确保传进来iresource的id有值***</p>
	 * @param resource
	 */
	public void saveCfgResource(ICfgResource iresource){
		CfgResource resource = iresource.turnToResource();
		HibernateUtil.saveObject(resource , null);
	}

	/**
	 * 删除资源信息
	 * @param resourceId
	 */
	public void deleteCfgResource(String resourceId){
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.DELETE, "delete CfgResource where refResourceId = ? and projectId = ? and customerId=?", resourceId, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
	}
	
	/**
	 * 根据资源名，查询资源对象
	 * @param resourceName
	 * @return
	 */
	public CfgResource findResourceByResourceName(String resourceName) {
		if(StrUtils.isEmpty(resourceName)){
			throw new NullPointerException("请求的资源名不能为空");
		}
		
		CfgResource resource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgResource.class, "from CfgResource where resourceName = ? and projectId = ? and customerId = ?", resourceName, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(resource == null){
			throw new IllegalArgumentException("不存在资源名为["+resourceName+"]的资源");
		}
		if(resource.getIsEnabled() == 0){
			throw new IllegalArgumentException("请求的资源["+resourceName+"]被禁用，请联系管理员");
		}
		return resource;
	}

	/**
	 * 更新资源信息
	 * @param refResourceId
	 * @param resourceName
	 * @param resourceRequestMethod
	 * @param isEnabled 
	 */
	public void updateResourceInfo(String refResourceId, String resourceName, String resourceRequestMethod, Integer isEnabled) {
		HibernateUtil.executeUpdateByHqlArr(SqlStatementTypeConstants.UPDATE, 
				"update CfgResource set resourceName=?, requestMethod=?, isEnabled=? where refResourceId=? and projectId=?", resourceName, resourceRequestMethod, isEnabled, refResourceId, CurrentThreadContext.getProjectId());
	}
	
	/**
	 * 根据关联的资源名，判断资源是否存在
	 * @param resourceName
	 * @return
	 */
	public boolean resourceIsExistByRefResourceName(String resourceName){
		long count = (long) HibernateUtil.executeUniqueQueryByHqlArr(queryResourceIsExistsHql, resourceName, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		return count > 0;
	}
	private static final String queryResourceIsExistsHql = "select count("+ResourcePropNameConstants.ID+") from CfgResource where resourceName=? and projectId=? and customerId=?";
}
