package com.king.tooth.web.entity.request;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.sys.builtin.data.BuiltinInstance;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.servlet.route.RouteBody;

/**
 * 请求的资源信息
 * @author DougLei
 */
public class ResourceInfo {
	/**
	 * 请求的代码资源key
	 */
	private String codeResourceKey;
	
	/**
	 * 请求的资源类型
	 */
	private int resourceType;
	
	/**
	 * 请求的sql脚本资源实例
	 */
	private ComSqlScript sqlScriptResource;
	
	//------------------------------------------------------------------
	public ResourceInfo() {
	}
	public ResourceInfo(RequestBody requestBody) {
		analysisResource(requestBody);
	}

	//------------------------------------------------------------------
	/**
	 * 解析请求的资源
	 * <p>如果是代码资源，或sql脚本资源，顺便获得其资源对象实例</p>
	 * @param requestBody
	 */
	private void analysisResource(RequestBody requestBody) {
		RouteBody routeBody = requestBody.getRouteBody();
		String requestMethod = requestBody.getRequestMethod();
		
		codeResourceKey = CodeResourceMapping.getCodeResourceKey(routeBody.getResourceName(), requestMethod, routeBody.getActionName());
		
		if(routeBody.isAction() || CodeResourceMapping.isCodeResource(codeResourceKey)){
			if(StrUtils.notEmpty(routeBody.getParentResourceName())){
				throw new IllegalArgumentException("平台目前不支持处理主子action code资源");
			}
			resourceType = ISysResource.CODE;
			return;
		}
		
		ComSysResource resource = BuiltinInstance.resourceService.findResourceByResourceName(routeBody.getResourceName());
		resourceType = resource.getResourceType();
		
		// 如果是sql脚本资源，则要去查询sql脚本实例
		if(ISysResource.SQLSCRIPT == resourceType){
			sqlScriptResource = BuiltinInstance.sqlService.findSqlScriptResourceById(resource.getRefResourceId());
		}
		
		// 如果请求包括父资源，则验证父资源是否可以调用
		if(StrUtils.notEmpty(routeBody.getParentResourceName())){
			resource = BuiltinInstance.resourceService.findResourceByResourceName(routeBody.getParentResourceName());
		}
	}
	
	/**
	 * 是否是表资源
	 * @return
	 */
	public boolean isTableResource(){
		return resourceType == ISysResource.TABLE;
	}
	
	/**
	 * 是否是sql资源
	 * @return
	 */
	public boolean isSqlResource(){
		return resourceType == ISysResource.SQLSCRIPT;
	}
	
	/**
	 * 是否是code资源
	 * @return
	 */
	public boolean isCodeResource(){
		return resourceType == ISysResource.CODE;
	}
	
	//------------------------------------------------------------------
	public int getResourceType() {
		return resourceType;
	}
	public String getCodeResourceKey() {
		return codeResourceKey;
	}
	public ComSqlScript getSqlScriptResource() {
		return sqlScriptResource;
	}
}
