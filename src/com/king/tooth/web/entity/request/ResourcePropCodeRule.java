package com.king.tooth.web.entity.request;

import java.util.List;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;

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
		if(requestBody.getRouteBody().getParentResourceName() == null && requestBody.isPostRequest() && !requestBody.getResourceInfo().getReqResource().isCodeResource() 
				&& !requestBody.getResourceInfo().getReqResource().isBusinessModelResource()){// 业务资源有自己的处理
			// 内置的资源，不需要处理
			if(ResourceInfoConstants.BUILTIN_RESOURCE.equals(requestBody.getResourceInfo().getReqResource().getRefResourceId())){
				return;
			}
			// sql资源，如果不是insert语句，则不需要处理
			if(requestBody.getResourceInfo().getReqResource().isSqlResource() && !SqlStatementTypeConstants.INSERT.equals(requestBody.getResourceInfo().getSql().getConfType())){
				return;
			}
			rules = PropCodeRuleUtil.analyzeRules(requestBody.getResourceInfo().getReqResource().getRefResourceId(), requestBody.getResourceInfo().getReqResource().getResourceName(), requestBody.getFormData());
		}
	}
	
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
