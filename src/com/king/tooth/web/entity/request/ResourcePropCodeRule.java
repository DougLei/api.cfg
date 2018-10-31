package com.king.tooth.web.entity.request;

import java.util.List;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 请求资源的属性(字段、列)值编码规范信息
 * @author DougLei
 */
public class ResourcePropCodeRule {
	
	/**
	 * 字段编码规则对象集合
	 */
	private List<CfgPropCodeRule> rules;
	
	//------------------------------------------------------------------
	public ResourcePropCodeRule() {
	}
	public ResourcePropCodeRule(RequestBody requestBody) {
		analysisResourcePropCodeRule(requestBody);
	}

	//------------------------------------------------------------------
	/**
	 * 解析请求资源的属性(字段、列)值编码规范信息
	 * @param requestBody
	 */
	private void analysisResourcePropCodeRule(RequestBody requestBody) {
		if(requestBody.getRouteBody().getParentResourceName() == null && requestBody.isPostRequest() && !requestBody.getResourceInfo().isCodeResource()){
			// 内置的资源，不需要处理
			if(ResourceInfoConstants.BUILTIN_RESOURCE.equals(requestBody.getResourceInfo().getReqResource().getRefResourceId())){
				return;
			}
			// sql资源，如果不是insert语句，则不需要处理
			if(requestBody.getResourceInfo().isSqlResource() && !SqlStatementTypeConstants.INSERT.equals(requestBody.getResourceInfo().getSqlScriptResource().getConfType())){
				return;
			}
			
			rules = HibernateUtil.extendExecuteListQueryByHqlArr(CfgPropCodeRule.class, null, null, queryPropCodeRuleHql, requestBody.getResourceInfo().getReqResource().getRefResourceId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
			if(rules == null || rules.size() == 0){
				return;
			}
			
			IJson ijson = requestBody.getFormData();
			String resourceName = requestBody.getResourceInfo().getReqResource().getResourceName();
			for (CfgPropCodeRule rule : rules) {
				rule.doProcessFinalCodeVal(ijson, resourceName);
			}
		}
	}
	// 查询属性编码规则集合的hql
	private static final String queryPropCodeRuleHql = "from CfgPropCodeRule where refResourceId=? and isEnabled=1 and projectId=? and customerId=? order by orderCode asc";

	/**
	 * 清空
	 */
	public void clear(){
		if(rules != null && rules.size() > 0){
			for (CfgPropCodeRule rule : rules) {
				rule.clear();
			}
			rules.clear();
		}
	}
	
	//------------------------------------------------------------------
	public List<CfgPropCodeRule> getRules() {
		return rules;
	}
}
