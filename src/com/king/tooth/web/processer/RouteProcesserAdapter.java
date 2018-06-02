package com.king.tooth.web.processer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.entity.AbstractSysResourceEntity;
import com.king.tooth.util.ReflectUtil;

/**
 * 路由和请求处理器的适配器
 * @author DougLei
 */
@SuppressWarnings({"serial", "unchecked"})
public class RouteProcesserAdapter implements Serializable {
	
	/**
	 * 处理器的基础目录(包)
	 * <p>com.king.tooth.web.processer.</p>
	 */
	private static final String PROCESSER_BASIC_PACKAGE = "com.king.tooth.web.processer.";
	/**
	 * 表资源的处理器包名
	 * <p>tableresource.</p>
	 */
	private static final String TABLE_RESOURCE_PROCESSER_PACKAGE_NAME = "tableresource.";
	/**
	 * sql资源的处理器包名
	 * <p>sqlresource.</p>
	 */
	private static final String SQL_RESOURCE_PROCESSER_PACKAGE_NAME = "sqlresource.";
	
	
	/**
	 * 路由规则的全局唯一标示
	 */
	private String routeRuleIdentity;
	/**
	 * 处理器的类名
	 */
	private String processClassName;
	/**
	 * 可处理的资源对象数组
	 */
	private ProcessResource[] processResources;
	
	/**
	 * 适配器的全局唯一标示
	 * adapterIdentity = routeIdentity_requestMethod(小写)
	 */
	private String adapterIdentity;
	/**
	 * 处理器类对象
	 */
	private Class<IRequestProcesser> processerClass;
	
	public RouteProcesserAdapter() {
	}
	
	/**
	 * @param requestMethod 请求的方法(包名)
	 * @param processResourceType 处理的资源类型
	 * @param routeRuleIdentity
	 * @param processClassName
	 */
	private RouteProcesserAdapter(String requestMethod, int processResourceType, String routeRuleIdentity, String processClassName) {
		this.routeRuleIdentity = routeRuleIdentity;
		this.processClassName = processClassName;
		this.adapterIdentity = this.routeRuleIdentity + "_" + requestMethod + "_" + processResourceType;
		String processClassPath = PROCESSER_BASIC_PACKAGE + getResourceProcessPackageName(processResourceType) + requestMethod + "." + this.processClassName;
		this.processerClass = ReflectUtil.getClass(processClassPath);
	}
	
	
	public void setRouteRuleIdentity(String routeRuleIdentity) {
		this.routeRuleIdentity = routeRuleIdentity;
	}
	public void setProcessClassName(String processClassName) {
		this.processClassName = processClassName;
	}
	public void setProcessResources(ProcessResource[] processResources) {
		this.processResources = processResources;
	}
	public String getAdapterIdentity() {
		return adapterIdentity;
	}
	
	/**
	 * 根据请求的不同方式，获取路由和处理器的适配器集合
	 * <p>配置的json串，一种处理器名可能对应多种请求方式(get.post.put.delete)</p>
	 * <p>将读取的每条配置信息，都转换成RouteProcesserAdapter对象</p>
	 * @see routeProcesserAdapterList
	 * @return
	 */
	public List<RouteProcesserAdapter> getRouteProcesserAdapterList() {
		List<RouteProcesserAdapter> routeProcesserAdapterList = new ArrayList<RouteProcesserAdapter>();
		
		RouteProcesserAdapter adapter = null;
		// 根据不同的处理请求方法，创建不同处理器的适配器对象
		int[] resourceTypes;
		for (ProcessResource pr : processResources) {
			resourceTypes = pr.getProcessResourceTypes();
			for (int rt : resourceTypes) {
				adapter = new RouteProcesserAdapter(pr.getProcessResourceMethod(), rt, this.routeRuleIdentity, this.processClassName);
				routeProcesserAdapterList.add(adapter);
			}
		}
		return routeProcesserAdapterList;
	}
	
	/**
	 * 根据处理的资源类型，获取对应资源处理器的包名
	 * @param processResourceType
	 * @return
	 */
	private String getResourceProcessPackageName(int processResourceType) {
		if(processResourceType == AbstractSysResourceEntity.TABLE_RESOURCE_TYPE){
			return TABLE_RESOURCE_PROCESSER_PACKAGE_NAME;
		}else if(processResourceType == AbstractSysResourceEntity.SQLSCRIPT_RESOURCE_TYPE){
			return SQL_RESOURCE_PROCESSER_PACKAGE_NAME;
		}
		throw new IllegalArgumentException("[processResourceType="+processResourceType+"]，没有匹配到对应的资源类型处理器，请检查[api.platform.processer.properties]配置文件中，key为route.processer.adapter.json的数据中，processResourceType的值是否配置正确");
	}

	public IRequestProcesser getProcesser() {
		IRequestProcesser processer = ReflectUtil.newInstance(this.processerClass);
		return processer;
	}
}
