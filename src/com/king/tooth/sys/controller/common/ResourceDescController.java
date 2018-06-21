package com.king.tooth.sys.controller.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.sys.entity.desc.resource.ResourceDescEntity;
import com.king.tooth.sys.entity.desc.resource.table.ColumnResource;
import com.king.tooth.sys.entity.desc.resource.table.TableResource;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 资源信息描述控制器
 * @author DougLei
 */
public class ResourceDescController extends AbstractController{
	
	/**
	 * 获取指定表资源的资源描述信息
	 * <p>请求方式：GET</p>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResponseBody table(HttpServletRequest request, String json){
		String tableResourceName = request.getParameter("resourceName");
		if(StrUtils.isEmpty(tableResourceName)){
			return installOperResponseBody("resourceName参数值不能为空", null);
		}
		
		ResourceDescEntity resourceDescEntity = new ResourceDescEntity();
		resourceDescEntity.setResourceName(tableResourceName);
		
		ComSysResource tableResource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysResource.class, 
				"from ComSysResource where resourceName = ? and createUserId = ?", 
				tableResourceName, 
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(tableResource == null){
			return installOperResponseBody("无法查询到resourceName为["+tableResourceName+"]的表资源描述信息，请联系管理员", null);
		}
		if(tableResource.getIsEnabled() == 0){
			return installOperResponseBody("resourceName为["+tableResourceName+"]的表资源被禁用，请联系管理员", null);
		}
		
		resourceDescEntity.setReqResourceMethods(tableResource.getReqResourceMethod());
		
		TableResource table = new TableResource();
		table.setTableName(HibernateUtil.executeUniqueQueryByHql("select tableName from ComTabledata where id = '"+tableResource.getRefResourceId()+"'", null));
		table.setResourceName(tableResourceName);
		
		List<Object[]> columnMaps = HibernateUtil.executeListQueryByHqlArr(null, null, 
				"select columnName, propName, comments from ComColumndata where tableId ='"+tableResource.getRefResourceId()+"' order by isEnabled desc");
		
		ColumnResource column = null;
		List<ColumnResource> columns = new ArrayList<ColumnResource>(columnMaps.size());
		for (Object[] cm : columnMaps) {
			column = new ColumnResource();
			column.setColumnName(cm[0]);
			column.setPropName(cm[1]);
			column.setComments(cm[2]);
			columns.add(column);
		}
		columnMaps.clear();
		table.setColumns(columns);
		resourceDescEntity.setTable(table);
		
		return installResponseBody(null, resourceDescEntity);
	}
	
	/**
	 * 获取指定表资源的json串
	 * <p>请求方式：GET</p>
	 * @return
	 */
	public ResponseBody tableJson(HttpServletRequest request, String jsonstr){
		String tableResourceName = request.getParameter("resourceName");
		if(StrUtils.isEmpty(tableResourceName)){
			return installOperResponseBody("resourceName参数值不能为空", null);
		}
		
		ComSysResource tableResource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComSysResource.class, 
				"from ComSysResource where resourceName = ? and createUserId = ?", 
				tableResourceName, 
				CurrentThreadContext.getCurrentAccountOnlineStatus().getAccountId());
		if(tableResource == null){
			return installOperResponseBody("无法查询到resourceName为["+tableResourceName+"]的表资源描述信息，请联系管理员", null);
		}
		if(tableResource.getIsEnabled() == 0){
			return installOperResponseBody("resourceName为["+tableResourceName+"]的表资源被禁用，请联系管理员", null);
		}
		
		
		ComTabledata table = HibernateUtil.extendExecuteUniqueQueryByHqlArr(ComTabledata.class, "from ComTabledata where id = '"+tableResource.getRefResourceId()+"'");
		table.setColumns(HibernateUtil.extendExecuteListQueryByHqlArr(ComColumndata.class, null, null, "from ComColumndata where isEnabled =1 and tableId =?", tableResource.getRefResourceId()));
		initBasicColumnToTable(table);
		
		boolean hc = false;
		String havaComments = request.getParameter("havaComments");
		if("true".equals(havaComments)){
			hc = true;
		}
		
		List<ComColumndata> columns = table.getColumns();
		Map<Object, Object> json = new HashMap<Object, Object>(columns.size());
		for (ComColumndata column : columns) {
			if(hc){
				json.put(column.getPropName(), column.getComments());
			}else{
				json.put(column.getPropName(), "");
			}
		}
		
		table.clear();
		return installResponseBody(null, json);
	}
	
	/**
	 * 给动态表对象，添加基础的字段
	 * @param table
	 */
	private void initBasicColumnToTable(ComTabledata table){
		// projectId
		ComColumndata projectIdColumn = new ComColumndata("project_id", DataTypeConstants.STRING, 32);
		projectIdColumn.setName("关联的项目主键");
		projectIdColumn.setComments("关联的项目主键");
		projectIdColumn.setOrderCode(9902);
		table.getColumns().add(projectIdColumn);
		
		// belongPlatformType
		ComColumndata belongPlatformTypeColumn = new ComColumndata("belong_platform_type", DataTypeConstants.INTEGER, 1);
		belongPlatformTypeColumn.setName("所属的平台类型");
		belongPlatformTypeColumn.setComments("所属的平台类型:1：配置平台、2：运行平台、3：通用(这个类型由后端开发者控制)");
		belongPlatformTypeColumn.setDefaultValue("2");
		belongPlatformTypeColumn.setOrderCode(9903);
		table.getColumns().add(belongPlatformTypeColumn);
		
		if(table.getIsResource() == 1){
			ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
			isEnabledColumn.setName("资源是否有效");
			isEnabledColumn.setComments("资源是否有效");
			isEnabledColumn.setDefaultValue("1");
			isEnabledColumn.setOrderCode(9904);
			table.getColumns().add(isEnabledColumn);
			
			ComColumndata reqResourceMethodColumn = new ComColumndata("req_resource_method", DataTypeConstants.STRING, 20);
			reqResourceMethodColumn.setName("请求资源的方法");
			reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
			reqResourceMethodColumn.setDefaultValue(ISysResource.ALL);
			reqResourceMethodColumn.setOrderCode(9905);
			table.getColumns().add(reqResourceMethodColumn);

			ComColumndata isBuiltinColumn = new ComColumndata("is_builtin", DataTypeConstants.INTEGER, 1);
			isBuiltinColumn.setName("是否内置资源");
			isBuiltinColumn.setComments("是否内置资源:这个字段由开发人员控制，不开放给用户");
			isBuiltinColumn.setDefaultValue("0");
			isBuiltinColumn.setOrderCode(9906);
			table.getColumns().add(isBuiltinColumn);

			ComColumndata isNeedDeployColumn = new ComColumndata("is_need_deploy", DataTypeConstants.INTEGER, 1);
			isNeedDeployColumn.setName("资源是否需要发布");
			isNeedDeployColumn.setComments("资源是否需要发布");
			isNeedDeployColumn.setDefaultValue("1");
			isNeedDeployColumn.setOrderCode(9907);
			table.getColumns().add(isNeedDeployColumn);
			
			ComColumndata isNeedRedeployColumn = new ComColumndata("is_need_redeploy", DataTypeConstants.INTEGER, 1);
			isNeedRedeployColumn.setName("资源是否需要补发布");
			isNeedRedeployColumn.setComments("资源是否需要补发布:例如，当配置平台开发出来一个新的运行平台通用功能时，将这个字段值改为1，去给所有已经发布的项目，补发增加新的功能");
			isNeedRedeployColumn.setDefaultValue("0");
			isNeedRedeployColumn.setOrderCode(9908);
			table.getColumns().add(isNeedRedeployColumn);
			
			ComColumndata isCreatedColumn = new ComColumndata("is_created", DataTypeConstants.INTEGER, 1);
			isCreatedColumn.setName("资源是否被创建");
			isCreatedColumn.setComments("资源是否被创建：在配置平台中，主要是给平台开发人员使用，也是标识表资源是否被加载到sessionFactory中；在运行平台中，这个字段标识资源是否被加载，主要是指表资源是否被加载到sessionFactory中");
			isCreatedColumn.setDefaultValue("0");
			isCreatedColumn.setOrderCode(9909);
			table.getColumns().add(isCreatedColumn);
		}
	}
}
