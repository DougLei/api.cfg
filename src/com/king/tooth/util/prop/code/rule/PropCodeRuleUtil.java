package com.king.tooth.util.prop.code.rule;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.sys.entity.cfg.CfgPropCodeRule;
import com.king.tooth.web.entity.request.ResourcePropCodeRule;

/**
 * 属性编码规则的工具类
 * @author DougLei
 */
public class PropCodeRuleUtil {
	
	/**
	 * 给对象的属性设置最终的编码值
	 * <p>添加表资源时使用</p>
	 * @see com.king.tooth.web.processer.tableresource.post.SingleResourceProcesser
	 * @param data
	 * @param index
	 * @param resourcePropCodeRule
	 */
	public static void setFinalCodeVal(JSONObject data, int index, ResourcePropCodeRule resourcePropCodeRule) {
		if(resourcePropCodeRule == null){
			return;
		}
		
		List<CfgPropCodeRule> rules = resourcePropCodeRule.getRules();
		if(rules != null && rules.size() > 0){
			for (CfgPropCodeRule rule : rules) {
				data.put(rule.getRefPropName(), rule.getFinalCodeVal(index));
			}
		}
	}
	
	/**
	 * 获取对象属性的最终编码值
	 * <p>insert sql资源使用</p>
	 * @see com.king.tooth.web.entity.request.valid.data.SqlResourceVerifier.analysisActualInValue()
	 * @param sqlParamName sql参数名
	 * @param paramIndex 第几个参数下标，即每个sql脚本参数的下标
	 * @param resourcePropCodeRule
	 */
	public static Object getFinalCodeVal(String sqlParamName, int paramIndex, ResourcePropCodeRule resourcePropCodeRule) {
		if(resourcePropCodeRule == null){
			return null;
		}
		
		List<CfgPropCodeRule> rules = resourcePropCodeRule.getRules();
		if(rules == null || rules.size() == 0){
			return null;
		}
		for (CfgPropCodeRule rule : rules) {
			if(rule.getRefPropName().equals(sqlParamName)){
				return rule.getFinalCodeVal(paramIndex);
			}
		}
		throw new IllegalArgumentException("调用sql脚本，自动生成编码值时，rules中没有refPropName=["+sqlParamName+"]的编码值，请检查属性编码规则的配置，或联系后端系统开发人员");
	}
	
	// ----------------------------------------------------------------
	/**
	 * 根据编码规则id，获取对应的锁对象
	 * @param codeRuleId
	 * @return
	 */
	public static Lock getPropCodeRuleLock(String codeRuleId){
		Lock lock = PropCodeRuleLockMapping.propCodeRuleLockMapping.get(codeRuleId);
		if(lock == null){
			lock = new ReentrantLock();
			PropCodeRuleLockMapping.propCodeRuleLockMapping.put(codeRuleId, lock);
		}
		return lock;
	}
	
	/**
	 * 根据编码规则id，删除对应的锁对象
	 * @param codeRuleId
	 * @return
	 */
	public static void removePropCodeRuleLock(String codeRuleId){
		PropCodeRuleLockMapping.propCodeRuleLockMapping.remove(codeRuleId);
	}
}