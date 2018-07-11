package com.king.tooth.web.processer.tableresource.delete;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.util.hibernate.HibernateUtil;


/**
 * 处理这种请求路径格式的处理器：/{parentResourceType}/{parentId}/{resourceType}
 * @author DougLei
 */
public final class ParentResourceByIdToSubResourceProcesser extends DeleteProcesser {

	public String getProcesserName() {
		return "【Delete-TableResource】ParentResourceByIdToSubResourceProcesser";
	}
	
	protected boolean doDeleteProcess() {
		String deleteQueryCondHql = getDeleteHql().toString();
		String deleteParentResourceHql = getDeleteParentResourceHql(deleteQueryCondHql).toString();// 删除主表资源hql
		
//		boolean isCodeResource = ISysResource.CODE.equals(requestBody.getRequestResourceType());
//		boolean isParentCodeResource = ISysResource.CODE.equals(requestBody.getRequestParentResourceType());
		
		List<Object> parameters;
		if(builtinParentsubQueryMethodProcesser.getIsSimpleParentSubQueryModel()){
			String deleteResourceHql = getDeleteResourceHql(deleteQueryCondHql, null).toString();// 删除子表资源hql
			
			// 删除主资源
			parameters = new ArrayList<Object>(hqlParameterValues);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, deleteParentResourceHql, parameters);
			
			// 删除子资源
			parameters = new ArrayList<Object>(hqlParameterValues);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, deleteResourceHql, parameters);
		}else{
			// 获取关联关系资源名
			String dataLinkResourceName = HibernateUtil.getDataLinkResourceName(requestBody.getRouteBody().getParentResourceName(), requestBody.getRouteBody().getResourceName());
			
			String deleteDatalinkHql = getDeleteDatalinkHql(deleteQueryCondHql, dataLinkResourceName).toString();// 删除数据关联关系hql
			String deleteResourceHql = getDeleteResourceHql(deleteQueryCondHql, dataLinkResourceName).toString();// 删除子表资源hql
			
			// 删除子资源
			parameters = new ArrayList<Object>(hqlParameterValues);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, deleteResourceHql, parameters);
			
			// 删除数据关联关系数据
			parameters = new ArrayList<Object>(hqlParameterValues);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, deleteDatalinkHql, parameters);
			
			// 删除主资源
			parameters = new ArrayList<Object>(hqlParameterValues);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.DELETE, deleteParentResourceHql, parameters);
		}
		installResponseBodyForDeleteData(hqlParameterValues, true);
		return true;
	}

	/**
	 * 获取删除子表资源hql
	 * @param deleteQueryCondHql
	 * @param dataLinkResourceName 
	 * @return
	 */
	private StringBuilder getDeleteResourceHql(String deleteQueryCondHql, String dataLinkResourceName){
		StringBuilder hql = new StringBuilder();
		if(dataLinkResourceName == null){
			hql.append(" delete ").append(requestBody.getRouteBody().getResourceName())
			   .append(" where ")
			   .append(builtinParentsubQueryMethodProcesser.getRefParentSubPropName())
			   .append(" in (select p_.").append(ResourceNameConstants.ID)
			   .append(" from ")
			   .append(requestBody.getRouteBody().getParentResourceName()).append(" p_ where ")
			   .append(deleteQueryCondHql);
		}else{
			hql.append(" delete ").append(requestBody.getRouteBody().getResourceName())
			   .append(" where ").append(ResourceNameConstants.ID)
			   .append(" in ( select d_.rightId from ")
			   .append(dataLinkResourceName).append(" d_,")
			   .append(requestBody.getRouteBody().getParentResourceName()).append(" p_ where p_.").append(ResourceNameConstants.ID)
			   .append("=d_.leftId and ")
			   .append(deleteQueryCondHql)
			   .append(")");
		}
		return hql;
	}
	
	/**
	 * 获取删除数据关联关系hql
	 * @param deleteQueryCondHql
	 * @param dataLinkResourceName 
	 * @return
	 */
	private StringBuilder getDeleteDatalinkHql(String deleteQueryCondHql, String dataLinkResourceName){
		StringBuilder hql = new StringBuilder();
		hql.append(" delete from ").append(dataLinkResourceName)
		   .append(" where leftId in ( select p_.").append(ResourceNameConstants.ID)
		   .append(" from ")
		   .append(requestBody.getRouteBody().getParentResourceName())
		   .append(" p_ where ")
		   .append(deleteQueryCondHql)
		   .append(")");
		return hql;
	}
	
	/**
	 * 获取删除主表数据的hql
	 * @param deleteQueryCondHql
	 * @return
	 */
	private StringBuilder getDeleteParentResourceHql(String deleteQueryCondHql){
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ")
		   .append(requestBody.getRouteBody().getParentResourceName())
		   .append(" p_ where ")
		   .append(deleteQueryCondHql);
		return hql;
	}
	
	protected StringBuilder getDeleteHql() {
		return builtinParentsubQueryMethodProcesser.getQueryParentResourceCondHql(requestBody.getRouteBody().getParentId(), "p_");
	}
}
