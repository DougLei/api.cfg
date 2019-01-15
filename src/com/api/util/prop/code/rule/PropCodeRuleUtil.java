package com.api.util.prop.code.rule;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSONObject;
import com.api.plugins.ijson.IJson;
import com.api.plugins.ijson.IJsonUtil;
import com.api.sys.entity.ITable;
import com.api.sys.entity.cfg.CfgPropCodeRule;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.CryptographyUtil;
import com.api.util.StrUtils;
import com.api.util.hibernate.HibernateUtil;
import com.api.web.entity.request.ResourcePropCodeRule;

/**
 * 属性编码规则的工具类
 * @author DougLei
 */
public class PropCodeRuleUtil {
	
	/**
	 * 解析出指定字段编码规则对象集合
	 * @param resourceId
	 * @param resourceName
	 * @param ijson
	 * @return
	 */
	public static List<CfgPropCodeRule> analyzeRules(String resourceId, String resourceName, IJson ijson){
		List<CfgPropCodeRule> rules = HibernateUtil.extendExecuteListQueryByHqlArr(CfgPropCodeRule.class, null, null, queryPropCodeRuleHql, resourceId, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(rules == null || rules.size() == 0){
			return null;
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
		
		for(int i=0;i<rules.size();i++){
			if(StrUtils.notEmpty(rules.get(i).getRefId())){
				rules.remove(i);
				rules.add(i, HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgPropCodeRule.class, querySinglePropCodeRuleHql, rules.get(i).getRefId(), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId()));
			}
		}
		
		for (CfgPropCodeRule rule : rules) {
			rule.doProcessFinalCodeVal(ijson, resourceName);
		}
		return rules;
	}
	/** 查询属性编码规则集合的hql */
	private static final String queryPropCodeRuleHql = "from CfgPropCodeRule where refResourceId=? and isEnabled=1 and projectId=? and customerId=? order by orderCode asc";
	private static final String querySinglePropCodeRuleHql = "from CfgPropCodeRule where refPropId=? and isEnabled=1 and projectId=? and customerId=?";
	
	// ----------------------------------------------------------------
	/**
	 * 给table资源对象的属性设置最终的编码值
	 * <p>添加表资源时使用</p>
	 * @param data
	 * @param index
	 * @param resourcePropCodeRule
	 */
	public static void setTableResourceFinalCodeVal(JSONObject data, int index, ResourcePropCodeRule resourcePropCodeRule) {
		if(resourcePropCodeRule == null){
			return;
		}
		setTableResourceFinalCodeVal(data, index, resourcePropCodeRule.getRules());
	}
	
	/**
	 * 给table资源对象的属性设置最终的编码值
	 * <p>添加表资源时使用</p>
	 * @param data
	 * @param index
	 * @param rules
	 */
	public static void setTableResourceFinalCodeVal(JSONObject data, int index, List<CfgPropCodeRule> rules) {
		if(rules != null && rules.size() > 0){
			for (CfgPropCodeRule rule : rules) {
				data.put(rule.getRefPropName(), rule.getFinalCodeVal(index));
			}
		}
	}
	
	// ----------------------------------------------------------------
	/**
	 * 获取sql资源对象属性的最终编码值
	 * <p>insert sql资源使用</p>
	 * @param sqlParamName sql参数名
	 * @param paramIndex 第几个参数下标，即每个sql脚本参数的下标
	 * @param resourcePropCodeRule
	 */
	public static Object getSqlResourceFinalCodeVal(String sqlParamName, int paramIndex, ResourcePropCodeRule resourcePropCodeRule) {
		if(resourcePropCodeRule == null){
			return null;
		}
		return getSqlResourceFinalCodeVal(sqlParamName, paramIndex, resourcePropCodeRule.getRules());
		
	}
	
	/**
	 * 获取sql资源对象属性的最终编码值
	 * <p>insert sql资源使用</p>
	 * @param sqlParamName
	 * @param paramIndex
	 * @param rules
	 * @return
	 */
	public static Object getSqlResourceFinalCodeVal(String sqlParamName, int paramIndex, List<CfgPropCodeRule> rules) {
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
	
	// ----------------------------------------------------------------
	/**
	 * 处理内置表资源的编码规则值
	 * @param builtinTableResource
	 * @return 返回附上规则值的JsonObject实例
	 */
	public static IJson processBuiltinTableResourceCodeRuleValue(ITable builtinTableResource){
		if(builtinTableResource == null){
			throw new NullPointerException("处理内置表资源的编码规则值是，传入的内置表资源对象不能为空");
		}
		IJson tmp = IJsonUtil.getIJson(builtinTableResource);
		List<CfgPropCodeRule> rules = PropCodeRuleUtil.analyzeRules(CryptographyUtil.encodeMd5(builtinTableResource.getTableResourceName()), builtinTableResource.getTableResourceName(), tmp);
		if(rules == null || rules.size() == 0){
			throw new NullPointerException("没有给"+builtinTableResource.getTableResourceName()+"资源中配置任何编码生成规则");
		}
		for(int i=0;i<tmp.size();i++){
			PropCodeRuleUtil.setTableResourceFinalCodeVal(tmp.get(i), i, rules);
		}
		return tmp;
	}
	
}
