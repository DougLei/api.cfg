package com.king.tooth.web.builtin.method.tableresource.parentsub;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.util.querycondfunc.BuiltinQueryCondFuncUtil;
import com.king.tooth.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置父子资源链接查询函数处理器
 * @author DougLei
 */
public class BuiltinParentsubQueryMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
	/**
	 * 父资源的查询条件参数集合
	 */
	private Map<String, String> parentResourceQueryCond;
	
	/**
	 * 是否是简单的主子表查询模式
	 * <p>不是通过关系表查询，而是通过子表的parentID字段查询</p>
	 * <p>默认值是false，即默认是通过关系表查询</p>
	 */
	private boolean isSimpleParentSubQueryModel;
	
	/**
	 * 子资源中，关联主子源对应的属性名
	 * 如果请求的url中没有指定，则默认为parentId
	 */
	private String refPropName;
	
	public BuiltinParentsubQueryMethodProcesser(Map<String, String> parentResourceQueryCond, String isSimpleParentSubQueryModel, String refPropName, String resourceName) {
		super.isUsed = true;
		this.parentResourceQueryCond = parentResourceQueryCond;
		this.resourceName = resourceName;
		
		if(StrUtils.isEmpty(isSimpleParentSubQueryModel)){
			isSimpleParentSubQueryModel = "false";
		}
		this.isSimpleParentSubQueryModel = Boolean.valueOf(isSimpleParentSubQueryModel);
		
		if(StrUtils.isEmpty(refPropName)){
			refPropName = "parentId";
		}
		this.refPropName = refPropName;
	}
	
	public BuiltinParentsubQueryMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinParentsubQueryMethodProcesser内置方法处理器");
	}
	
	public StringBuilder getHql() {
		execAnalysisParams();
		return hql;
	}

	protected void execAnalysisParam() {
		if(isSimpleParentSubQueryModel){
			hql.append(" where s_.").append(refPropName);
			if(parentResourceQueryCond.size() > 0){ // 如果有查询主表的条件集合
				hql.append(" in( select p_.").append(ResourceNameConstants.ID)
				   .append(" from ").append(parentResourceName).append(" p_ where ");
				Set<Entry<String, String>> queryCondParamsSet = parentResourceQueryCond.entrySet();
				BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ISysResource.TABLE, parentResourceName, queryCondParamsSet , hqlParameterValues, hql, "p_");
				hql.append(" ) ");
			}else{
				hql.append(" =? ");
				hqlParameterValues.add(parentResourceId);
			}
		}else{
			String dataLinkResourceName = HibernateUtil.getDataLinkResourceName(parentResourceName, resourceName);
			hql.append(" where s_.").append(ResourceNameConstants.ID).append(" in ( select d_.rightId from ")
			   .append(dataLinkResourceName).append(" d_,")
			   .append(parentResourceName).append(" p_ where p_.").append(ResourceNameConstants.ID)
			   .append("=d_.leftId and ");
			
			if(parentResourceQueryCond.size() > 0){ // 如果有查询主表的条件集合
				Set<Entry<String, String>> queryCondParamsSet = parentResourceQueryCond.entrySet();
				BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ISysResource.TABLE, parentResourceName, queryCondParamsSet , hqlParameterValues, hql, "p_");
			}else{ // 否则就直接查询
				hql.append("p_.")
				   .append(ResourceNameConstants.ID)
				   .append(" =?");
				hqlParameterValues.add(parentResourceId);
			}
			hql.append(" ) ");
		}
		
		Log4jUtil.debug("[BuiltinParentsubQueryMethodProcesser.execAnalysisParam]解析出来，要执行的父子资源查询的hql语句为：{}", hql.toString());
		Log4jUtil.debug("[BuiltinParentsubQueryMethodProcesser.execAnalysisParam]解析出来，要执行的执子表查询的hql参数值为：ParentId={}", parentResourceId);
	}
	
	/**
	 * 获取查询主资源的条件hql语句
	 * 用在【目前只主子表删除时】 
	 * @see ParentResourceByIdToSubResourceProcesser.getDeleteHql()
	 * @param parentResourceId 由路由传入的父资源id
	 * @param alias 别名
	 * @return
	 */
	public StringBuilder getQueryParentResourceCondHql(String parentResourceId, String alias){
		StringBuilder hql = new StringBuilder();
		if(parentResourceQueryCond.size() > 0){ // 如果有查询主表的条件集合
			Set<Entry<String, String>> queryCondParamsSet = parentResourceQueryCond.entrySet();
			BuiltinQueryCondFuncUtil.installQueryCondOfDBScriptStatement(ISysResource.TABLE, parentResourceName, queryCondParamsSet , hqlParameterValues, hql, alias);
		}else{// 否则就直接查询
			hql.append(alias).append(".").append(ResourceNameConstants.ID).append(" = ?");
			hqlParameterValues.add(parentResourceId);
		}
		return hql;
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.PARENT_SUB_QUERY;
	}

	public boolean getIsSimpleParentSubQueryModel() {
		return isSimpleParentSubQueryModel;
	}

	public String getRefPropName() {
		return refPropName;
	}
	
	public void clearInvalidMemory() {
		if(parentResourceQueryCond != null && parentResourceQueryCond.size() > 0){
			parentResourceQueryCond.clear();
		}
	}
}
