package com.king.tooth.web.entity.request;

import com.king.tooth.cache.CodeResourceMapping;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.thread.CurrentThreadContext;
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
				throw new IllegalArgumentException("平台目前不支持处理[主子/递归]方式调用code资源");
			}
			resourceType = ISysResource.CODE;
		}else{
			SysResource resource = BuiltinObjectInstance.resourceService.findResourceByResourceName(routeBody.getResourceName());
			resourceType = resource.getResourceType();
			
			// 如果是sql脚本资源，则要去查询sql脚本实例
			if(ISysResource.SQLSCRIPT == resourceType){
				sqlScriptResource = BuiltinObjectInstance.sqlScriptService.findSqlScriptResourceById(resource.getRefResourceId());
			}
			
			// 如果请求包括父资源，则验证父资源是否可以调用
			if(StrUtils.notEmpty(routeBody.getParentResourceName())){
				resource = BuiltinObjectInstance.resourceService.findResourceByResourceName(routeBody.getParentResourceName());
				
				if(resource.getResourceType() != resourceType){
					throw new IllegalArgumentException("平台目前不支持处理不同类型的资源混合调用");
				}
				if(resource.getResourceType() == ISysResource.SQLSCRIPT && !routeBody.getParentResourceName().equals(routeBody.getResourceName())){
					throw new IllegalArgumentException("平台目前不支持处理[主子]方式调用sql资源");
				}
			}
		}
		
		// 记录日志，请求的资源类型
		CurrentThreadContext.getReqLogData().getReqLog().setResourceType(resourceType);
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
