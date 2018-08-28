package com.king.tooth.web.servlet.route;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.util.ReflectUtil;

/**
 * 路由资源体的解析器
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RouteBodyAnalysis implements Serializable{
	
	/**
	 * 特殊标示符
	 */
	private transient static final String[] specialWords = {"counter", "values", "action"};
	
	/**
	 * 路由解析规则map集合
	 * <p>配置文件</p>
	 * <p>Key=(长度_特殊字符[如果特殊字符为空，则为null])</p>
	 * <p>Value=某一个规则对象</p>
	 */
	private transient static final Map<String, RouteRule> ruleMap = new HashMap<String, RouteRule>();
	/**
	 * 是否没有初始化路由解析规则配置
	 * 防止被多次初始化
	 */
	private static boolean unInitRouteRuleConfig = true;
	
	/**
	 * 初始化路由解析规则配置
	 */
	public static void initRouteRuleConfig(){
		if(unInitRouteRuleConfig){
			JSONArray jsonArray = JSONArray.parseArray(SysConfig.getSystemConfig("route.rule.json"));
			JSONObject tmp = null;
			for (int i = 0; i < jsonArray.size(); i++) {
				tmp = jsonArray.getJSONObject(i);
				ruleMap.put(tmp.getString("key"), tmp.getObject("routeRule", RouteRule.class));
			}	
			jsonArray.clear();
			unInitRouteRuleConfig = false;
		}
	}
	
	/**
	 * 开始解析
	 * @param requestUri
	 * @param routeBody
	 */
	public void doAnalysis(String requestUri, RouteBody routeBody) {
		String[] routeArr = requestUri.split("/");
		int routeArrLen = routeArr.length; 
		int index = 0;
		String specialWord = routeArr[index];// 下标为0的:或为特殊标示符，或为第一个路由属性值
		RouteRule rule = getRouteRule(specialWord, routeArrLen, requestUri);
		if(rule.getSpecialWord() != null){
			index++;// 若是特殊标示符，则值要从数组下标为1开始提取
		}
		String[] propSetMethodNameArr = rule.getPropSetMethodNameArr();
		if(propSetMethodNameArr != null){
			for (String setMethod : propSetMethodNameArr) {
				ReflectUtil.invokeMethod(routeBody, setMethod, new Class[]{String.class}, new Object[]{routeArr[index++]});
			}
		}
		routeBody.setIsAction(rule.getIsAction());
		routeBody.setRouteRuleIdentity(rule.getRouteRuleIdentity());
	}
	
	/**
	 * 解析获得路由规则对象
	 * @param specialWord
	 * @param routeArrLen
	 * @param requestUri
	 * @return 
	 */
	private RouteRule getRouteRule(String specialWord, int routeArrLen, String requestUri) {
		boolean includeSpecialWord = false;
		for (String sw : specialWords) {
			if(sw.equalsIgnoreCase(specialWord)){
				specialWord = sw;
				includeSpecialWord = true;
				break;
			}
		}
		
		String key = routeArrLen + "_";
		if(includeSpecialWord){
			key += specialWord;
		}else{
			key += "null";
		}
		
		// 如果不存在配置的内容
		if(!ruleMap.containsKey(key)){
			throw new IllegalArgumentException("平台目前不支持您请求的路由格式：" + requestUri);
		}
		return ruleMap.get(key);
	}
}
 
