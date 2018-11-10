package com.king.tooth.web.entity.request;

import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.cfg.CfgBusiModel;
import com.king.tooth.sys.entity.cfg.CfgResource;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.service.cfg.CfgBusiModelService;
import com.king.tooth.sys.service.cfg.CfgResourceService;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.servlet.route.RouteBody;

/**
 * 请求的资源信息
 * @author DougLei
 */
public class ResourceInfo {
	/**
	 * 请求的资源类型
	 */
	private int resourceType;
	
	/**
	 * 请求的sql脚本资源
	 */
	private ComSqlScript sql;
	
	/**
	 * 请求的业务模型资源
	 */
	private CfgBusiModel busiModel;
	
	/**
	 * 请求的资源对象
	 */
	private CfgResource reqResource;
	/**
	 * 请求的父资源对象
	 */
	private CfgResource reqParentResource;
	
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
		
		if(routeBody.getIsCode()){
			resourceType = ResourceInfoConstants.CODE;
			reqResource = new CfgResource(resourceType);
		}else{
			reqResource = BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).findResourceByResourceName(routeBody.getResourceName());
			validIsSupportRequestMethod(requestMethod, reqResource.getRequestMethod(), reqResource.getResourceName(), reqResource.getResourceTypeDesc());
			
			resourceType = reqResource.getResourceType();
			
			// 如果是sql脚本资源，则要去查询sql脚本实例
			if(ResourceInfoConstants.SQL == resourceType){
				sql = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).findSqlScriptResourceById(reqResource.getRefResourceId());
				
				if(SqlStatementTypeConstants.VIEW.equals(sql.getSqlScriptType())){
					throw new IllegalArgumentException("系统目前不支持直接处理视图类型的sql资源");
				}
			}
			// 如果是业务模型资源，则要去查询相关的信息
			else if(ResourceInfoConstants.BUSINESS_MODEL == resourceType){
				if(!"post".equals(requestMethod)){
					throw new IllegalArgumentException("系统目前不支持通过[非post]方式的请求，调用业务模型资源");
				}
				busiModel = BuiltinResourceInstance.getInstance("CfgBusiModelService", CfgBusiModelService.class).findBusiModel(reqResource.getRefResourceId());
			}
			
			// 如果请求包括父资源，则验证父资源是否可以调用
			if(StrUtils.notEmpty(routeBody.getParentResourceName())){
				if(!"get".equals(requestMethod)){
					throw new IllegalArgumentException("系统目前不支持[非get]方式的请求，调用资源的[主子/递归]");
				}
				if(resourceType == ResourceInfoConstants.BUSINESS_MODEL){
					throw new IllegalArgumentException("系统目前不支持[主子/递归]方式调用业务模型资源");
				}
				
				reqParentResource = BuiltinResourceInstance.getInstance("CfgResourceService", CfgResourceService.class).findResourceByResourceName(routeBody.getParentResourceName());
				validIsSupportRequestMethod(requestMethod, reqParentResource.getRequestMethod(), reqParentResource.getResourceName(), reqParentResource.getResourceTypeDesc());
				
				if(reqParentResource.getResourceType() != resourceType){
					throw new IllegalArgumentException("系统目前不支持不同类型的资源混合调用");
				}
				if(reqParentResource.getResourceType() == ResourceInfoConstants.SQL && !routeBody.getParentResourceName().equals(routeBody.getResourceName())){
					throw new IllegalArgumentException("系统目前不支持[主子]方式调用sql资源");
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
		if(ResourceInfoConstants.NONE.equals(resourceSupportRequestMethod) || resourceSupportRequestMethod.contains(ResourceInfoConstants.NONE)){
			throw new IllegalArgumentException("请求的名为["+resourceName+"]的"+resourceType+"，不支持任何方式的请求");
		}
		if(ResourceInfoConstants.ALL.equals(resourceSupportRequestMethod) || resourceSupportRequestMethod.contains(ResourceInfoConstants.ALL)){
			return;
		}
		String[] supportRequestMethodArr = resourceSupportRequestMethod.split(",");
		for (String srm : supportRequestMethodArr) {
			if(srm.equals(actualRequestMethod)){
				return;
			}
		}
		throw new IllegalArgumentException("请求的名为["+resourceName+"]的"+resourceType+"，只支持["+resourceSupportRequestMethod+"]方式的请求");
	}
	
	//------------------------------------------------------------------
	public int getResourceType() {
		return resourceType;
	}
	public ComSqlScript getSql() {
		return sql;
	}
	public CfgBusiModel getBusiModel() {
		return busiModel;
	}
	public CfgResource getReqResource() {
		return reqResource;
	}
	public CfgResource getReqParentResource() {
		return reqParentResource;
	}
	
	public void clear() {
		if(sql != null){
			sql.clear();
		}
		if(busiModel != null){
			busiModel.clear();
		}
	}
}
