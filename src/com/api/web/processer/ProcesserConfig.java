package com.api.web.processer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.api.cache.SysContext;
import com.api.util.Log4jUtil;
import com.api.util.StrUtils;
import com.api.web.entity.request.RequestBody;

/**
 * 请求处理器的配置和管理
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProcesserConfig implements Serializable{
	/**
	 * 路由和请求处理器的适配器映射集合
	 * <p>Key=路由的唯一标示，例如：1_null、2_Counter</p>
	 * <p>Value=对应的路由和请求处理器的适配器对象</p>
	 */
	private transient static final Map<String, RouteProcesserAdapter> routeProcesserAdapterMapping = new HashMap<String, RouteProcesserAdapter>();
	/**
	 * 是否没有初始化资源处理器配置
	 * 防止被多次初始化
	 */
	private static boolean unInitResourceProcesserConfig = true;
	
	/**
	 * 初始化资源处理器配置
	 */
	public static void initResourceProcesserConfig(){
		if(unInitResourceProcesserConfig){
			JSONArray jsonArray = JSONArray.parseArray(SysContext.getSystemConfig("route.processer.adapter.json"));
			List<RouteProcesserAdapter> routeProcesserAdapterList = null;
			for (int i = 0; i < jsonArray.size(); i++) {
				routeProcesserAdapterList = jsonArray.getObject(i, RouteProcesserAdapter.class).getRouteProcesserAdapterList();
				for (RouteProcesserAdapter rpa : routeProcesserAdapterList) {
					routeProcesserAdapterMapping.put(rpa.getAdapterIdentity(), rpa);
					Log4jUtil.debug("load processer , processName is :{}", rpa.getProcesser().getProcesserName());
				}
				routeProcesserAdapterList.clear();
			}
			jsonArray.clear();
			unInitResourceProcesserConfig = false;
		}
	}
	
	/**
	 * 按照规则，通过请求体中的数据，获得对应的适配器
	 * <p>adapterIdentity = routeIdentity_requestMethod(小写)</p>
	 * @param requestBody
	 * @return
	 */
	public static IRequestProcesser getProcess(RequestBody requestBody){
		String adapterIdentity = getAdapterIdentity(requestBody);
		
		RouteProcesserAdapter adapter = routeProcesserAdapterMapping.get(adapterIdentity);
		if(adapter == null){
			throw new IllegalArgumentException("平台目前不支持您请求的资源调用方式：["+requestBody.getRequestMethod()+"："+requestBody.getRequestUri()+"]，其adapterIdentity的值为：["+adapterIdentity+"]");
		}
		IRequestProcesser processer = adapter.getProcesser();
		
		Log4jUtil.debug("【请求的uri是 ==================> ：{}】", requestBody.getRequestUri());
		Log4jUtil.debug("【获得的请求处理器是 ==================> ：{}】", processer.getProcesserName());
		processer.setRequestBody(requestBody);// 把请求体代入到处理器中
		return processer;
	}
	
	/**
	 * 获取适配器的唯一标示
	 * @param requestBody
	 * @return
	 */
	private static String getAdapterIdentity(RequestBody requestBody){
		String adapterIdentity = requestBody.getRouteBody().getRouteRuleIdentity();
		
		// 判断，如果主资源名和子资源名一致，则是调用递归查询的处理器
		if(StrUtils.notEmpty(requestBody.getRouteBody().getParentResourceName())
				&& requestBody.getRouteBody().getParentResourceName().equals(requestBody.getRouteBody().getResourceName())){
			adapterIdentity += "_recursive";
		}
		
		adapterIdentity += "_" + requestBody.getRequestMethod();
		adapterIdentity += "_" + requestBody.getResourceInfo().getResourceType();
		return adapterIdentity.toString();
	}
}
