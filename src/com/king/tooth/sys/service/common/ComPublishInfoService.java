package com.king.tooth.sys.service.common;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.entity.common.ComPublishInfo;
import com.king.tooth.sys.service.AbstractService;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 系统发布信息资源对象处理器
 * @author DougLei
 */
public class ComPublishInfoService extends AbstractService{
	
	/**
	 * 验证指定的资源是否发布
	 * @param databaseId
	 * @param projectId
	 * @param resourceId
	 * @return
	 */
	public boolean validResourceIsPublished(String databaseId, String projectId, String resourceId){
		String hql = "from ComPublishInfo where";
		List<Object> params = new ArrayList<Object>(3);
		if(databaseId != null){
			hql += " publishDatabaseId=?";
			params.add(databaseId);
		}
		if(projectId != null){
			if(databaseId != null){
				hql += " and";
			}
			hql += " publishProjectId=?";
			params.add(projectId);
		}
		if(resourceId != null){
			hql += " and publishResourceId=?";
			params.add(resourceId);
		}
		
		ComPublishInfo publishInfo = HibernateUtil.extendExecuteUniqueQueryByHql(ComPublishInfo.class, hql, params);
		params.clear();
		if(publishInfo == null){
			return false;
		}
		if(publishInfo.getIsSuccess() == 0){
			return false;
		}
		return true;
	}
	
	//--------------------------------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------
	
}