package com.king.tooth.web.servlet.route;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 资源路由映射类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RouteMapping implements Serializable{
	
	/**
	 * 资源映射
	 * <p>缓存的parentResourceName、parentTableName、resourceName、tableName</p>
	 * <p>Key=resourceType或parentResourceType		Value=resourceName或parentResourceName</p>
	 * <p>Key=resourceName或parentResourceName	    Value=tableName或parentTableName</p>
	 * <p>Key=propName							    Value=columnName</p>
	 */
	private transient static final Map<String, String> routeResourceMapping = new HashMap<String, String>();
	
	/**
	 * 存储路由资源
	 * @param resourceName
	 * @param resourceValue
	 */
	static void setRouteResource(String resourceName, String resourceValue){
		routeResourceMapping.put(resourceName, resourceValue);
	}
	
	/**
	 * 获取路由资源
	 * @param resourceName
	 */
	static String getRouteResource(String resourceName){
		return routeResourceMapping.get(resourceName);
	}
}
