package com.king.tooth.web.entity.request;

import java.util.List;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgColumnCodeRule;
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
	private List<CfgColumnCodeRule> rules;
	
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
		if(requestBody.getRouteBody().getParentResourceName() == null && requestBody.getResourceInfo().isTableResource() 
				&& requestBody.isPostRequest()){
			
			// 内置的资源，不需要处理
			if(ResourceInfoConstants.BUILTIN_RESOURCE.equals(requestBody.getResourceInfo().getReqResource().getRefResourceId())){
				return;
			}
			
			rules = HibernateUtil.extendExecuteListQueryByHqlArr(CfgColumnCodeRule.class, null, null, queryColumnCodeRuleHql, requestBody.getResourceInfo().getReqResource().getRefResourceId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
			if(rules == null || rules.size() == 0){
				return;
			}
			
			IJson ijson = requestBody.getFormData();
			String resourceName = requestBody.getResourceInfo().getReqResource().getResourceName();
			for (CfgColumnCodeRule rule : rules) {
				rule.doProcessFinalCodeVal(ijson, resourceName);
			}
		}
	}
	// 查询字段编码规则集合的hql
	private static final String queryColumnCodeRuleHql = "from CfgColumnCodeRule where refTableId=? and isEnabled=1 and projectId=? and customerId=?";

	/**
	 * 清空
	 */
	public void clear(){
		if(rules != null && rules.size() > 0){
			for (CfgColumnCodeRule rule : rules) {
				rule.clear();
			}
			rules.clear();
		}
	}
	
	//------------------------------------------------------------------
	public List<CfgColumnCodeRule> getRules() {
		return rules;
	}
}
