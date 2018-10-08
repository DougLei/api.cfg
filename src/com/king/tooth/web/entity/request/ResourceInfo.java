package com.king.tooth.web.entity.request;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.code.resource.CodeResourceProcesser;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.sys.service.sys.SysResourceService;
import com.king.tooth.thread.current.CurrentThreadContext;
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
	
	/**
	 * 请求的资源对象
	 */
	private SysResource reqResource;
	/**
	 * 请求的父资源对象
	 */
	private SysResource reqParentResource;
	
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
		
		codeResourceKey = CodeResourceProcesser.getCodeResourceKey(routeBody.getResourceName(), requestMethod, routeBody.getActionName());
		if(routeBody.isAction() || CodeResourceProcesser.isCodeResource(codeResourceKey)){
			if(StrUtils.notEmpty(routeBody.getParentResourceName())){
				throw new IllegalArgumentException("系统目前不支持处理[主子/递归]方式调用code资源");
			}
			resourceType = ResourceInfoConstants.CODE;
		}else{
			reqResource = BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).findResourceByResourceName(routeBody.getResourceName());
			validIsSupportRequestMethod(requestMethod, reqResource.getRequestMethod(), reqResource.getResourceName(), reqResource.getResourceTypeDesc());
			
			resourceType = reqResource.getResourceType();
			
			// 如果是sql脚本资源，则要去查询sql脚本实例
			if(ResourceInfoConstants.SQL == resourceType){
				sqlScriptResource = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).findSqlScriptResourceById(reqResource.getRefResourceId());
				
				if(SqlStatementTypeConstants.VIEW.equals(sqlScriptResource.getSqlScriptType())){
					throw new IllegalArgumentException("系统目前不支持直接处理视图类型的sql资源");
				}
			}
			
			// 如果请求包括父资源，则验证父资源是否可以调用
			if(StrUtils.notEmpty(routeBody.getParentResourceName())){
				reqParentResource = BuiltinResourceInstance.getInstance("SysResourceService", SysResourceService.class).findResourceByResourceName(routeBody.getParentResourceName());
				validIsSupportRequestMethod(requestMethod, reqParentResource.getRequestMethod(), reqParentResource.getResourceName(), reqParentResource.getResourceTypeDesc());
				
				if(reqParentResource.getResourceType() != resourceType){
					throw new IllegalArgumentException("系统目前不支持处理不同类型的资源混合调用");
				}
				if(reqParentResource.getResourceType() == ResourceInfoConstants.SQL && !routeBody.getParentResourceName().equals(routeBody.getResourceName())){
					throw new IllegalArgumentException("系统目前不支持处理[主子]方式调用sql资源");
				}
			}
		}
		
		// 记录日志，请求的资源类型
		CurrentThreadContext.getReqLogData().getReqLog().setType(resourceType);
		CurrentThreadContext.getReqLogData().getReqLog().setResourceType(resourceType);
	}
	
	/**
	 * 验证是否支持请求的方式
	 * @param actualRequestMethod
	 * @param resourceSupportRequestMethod
	 * @param resourceName
	 * @param resourceType
	 */
	private void validIsSupportRequestMethod(String actualRequestMethod, String resourceSupportRequestMethod, String resourceName, String resourceType) {
		if("all".equals(resourceSupportRequestMethod)){
			return;
		}
		if("none".equals(resourceSupportRequestMethod)){
			throw new IllegalArgumentException("请求的名为["+resourceName+"]的"+resourceType+"，不支持任何方式的请求");
		}
		String[] supportRequestMethodArr = resourceSupportRequestMethod.split(",");
		for (String srm : supportRequestMethodArr) {
			if(srm.equals(actualRequestMethod)){
				return;
			}
		}
		throw new IllegalArgumentException("请求的名为["+resourceName+"]的"+resourceType+"，只支持["+resourceSupportRequestMethod+"]方式的请求");
	}
	
	/**
	 * 是否是表资源
	 * @return
	 */
	public boolean isTableResource(){
		return resourceType == ResourceInfoConstants.TABLE;
	}
	
	/**
	 * 是否是sql资源
	 * @return
	 */
	public boolean isSqlResource(){
		return resourceType == ResourceInfoConstants.SQL;
	}
	
	/**
	 * 是否是code资源
	 * @return
	 */
	public boolean isCodeResource(){
		return resourceType == ResourceInfoConstants.CODE;
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
	public SysResource getReqResource() {
		return reqResource;
	}
	public SysResource getReqParentResource() {
		return reqParentResource;
	}
}
