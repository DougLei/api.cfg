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
		if(requestBody.getRouteBody().getParentResourceName() == null && requestBody.isPostRequest() && !requestBody.getResourceInfo().getReqResource().isCodeResource()){
			// 内置的资源，不需要处理
			if(ResourceInfoConstants.BUILTIN_RESOURCE.equals(requestBody.getResourceInfo().getReqResource().getRefResourceId())){
				return;
			}
			// sql资源，如果不是insert语句，则不需要处理
			if(requestBody.getResourceInfo().getReqResource().isSqlResource() && !SqlStatementTypeConstants.INSERT.equals(requestBody.getResourceInfo().getSql().getConfType())){
				return;
			}
			
			rules = HibernateUtil.extendExecuteListQueryByHqlArr(CfgPropCodeRule.class, null, null, queryPropCodeRuleHql, requestBody.getResourceInfo().getReqResource().getRefResourceId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
			if(rules == null || rules.size() == 0){
				return;
			}
			
			// 判读是否同一个属性，有多个有效的自动编码规则
			int size = rules.size();
			int actSize = size-1;
			String refPropId = null;
			for (int i=0;i<actSize;i++) {
				refPropId = rules.get(i).getRefPropId();
				for(int j=1;j<size;j++){
					if(i != j && refPropId.equals(rules.get(j).getRefPropId())){
						throw new IllegalArgumentException("在生成自动编码值时，属性["+rules.get(j).getRefPropName()+"]出现多次有效的编码规则配置，请检查");
					}
				}
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
