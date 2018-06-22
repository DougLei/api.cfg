package com.king.tooth.sys.service.cfg;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
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
			if(databaseId != null || projectId != null){
				hql += " and";
			}
			hql += " publishResourceId=?";
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

	/**
	 * 删除发布的数据
	 * @param publishProjectId 如果是操作表、sql脚本的时候，这个字段绝对不能为空，因为表、sql脚本和项目的关系是多对多的关系
	 * @param publishResourceId 
	 */
	public void deletePublishedData(String publishProjectId, String publishResourceId) {
		String hql = "delete ComPublishInfo where";
		List<Object> params = new ArrayList<Object>(2);
		if(publishProjectId != null){
			hql += " publishProjectId = ?";
			params.add(publishProjectId);
		}
		if(publishResourceId != null){
			if(publishProjectId != null){
				hql += " and ";
			}
			hql += " publishResourceId = ?";
			params.add(publishResourceId);
		}
		HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, hql, params);
	}
	
	/**
	 * 批量删除发布的数据
	 * @param publishProjectId 如果是操作表、sql脚本的时候，这个字段绝对不能为空，因为表、sql脚本和项目的关系是多对多的关系
	 * @param publishResourceIds 
	 */
	public void batchDeletePublishedData(String publishProjectId, List<Object> publishResourceIds) {
		StringBuilder hql = new StringBuilder("delete ComPublishInfo where");
		List<Object> params = new ArrayList<Object>(publishResourceIds.size()+1);
		if(publishProjectId != null){
			hql.append(" publishProjectId = ?");
			params.add(publishProjectId);
		}
		if(publishResourceIds != null && publishResourceIds.size() > 0){
			if(publishProjectId != null){
				hql.append(" and ");
			}
			hql.append("publishResourceId in(");
			for (Object resourceId : publishResourceIds) {
				hql.append("?,");
				params.add(resourceId);
			}
			hql.setLength(hql.length()-1);
			hql.append(")");
		}
		HibernateUtil.executeUpdateByHql(SqlStatementType.DELETE, hql.toString(), params);
	}
}