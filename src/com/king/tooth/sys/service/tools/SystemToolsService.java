package com.king.tooth.sys.service.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.annotation.Service;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.cfg.CfgResource;
import com.king.tooth.sys.entity.tools.resource.ResourceInfo;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 系统工具类的Service
 * @author DougLei
 */
@Service
public class SystemToolsService {
	
	/**
	 * 监听hibernate类元数据
	 * @param resourceNameArr
	 * @return
	 */
	public Object monitorHibernateClassMetadata(String[] resourceNameArr){
		return HibernateUtil.getHibernateClassMetadatas(resourceNameArr);
	}
	
	/**
	 * 获取指定资源的信息
	 * @param name 表名 或 资源名
	 * @return
	 */
	public Object getResourceInfo(String name){
		CfgResource resource = HibernateUtil.extendExecuteUniqueQueryByHqlArr(CfgResource.class, "from CfgResource where (resourceName=? or resourceName=?) and projectId=? and customerId=?", name, NamingProcessUtil.tableNameTurnClassName(name), CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(resource == null){
			return "系统中没有查询到指定名称["+name+"]的资源";
		}
		Integer resourceType = resource.getResourceType();
		ResourceInfo resourceInfo = new ResourceInfo();
		resourceInfo.setResourceName(resource.getResourceName());
		resourceInfo.setResourceType(resourceType);
		resourceInfo.setReqResourceMethod(resource.getRequestMethod().toUpperCase());
		
		if(resourceType == ResourceInfoConstants.TABLE){
			setTableResourceStruct(resource.getRefResourceId(), resourceInfo);
		}else if(resourceType == ResourceInfoConstants.SQL){
			setSqlResourceStruct(resource.getRefResourceId(), resourceInfo);
		}else{
			resourceInfo.setMsg("系统目前不存在类型为["+resourceType+"]的资源，请联系系统开发人员");
		}
		return resourceInfo;
	}

	/**
	 * 设置表资源的结构
	 * @param tableResourceId
	 * @param resourceInfo
	 */
	@SuppressWarnings("unchecked")
	private void setTableResourceStruct(String tableResourceId, ResourceInfo resourceInfo) {
		List<Object[]> columns = HibernateUtil.executeListQueryByHqlArr(null, null, "select propName,columnType,name,comments  from CfgColumn where tableId=?", tableResourceId);
		if(columns == null || columns.size() == 0){
			resourceInfo.setMsg("名为["+resourceInfo.getResourceName()+"]的表资源不存在结构信息，请检查配置的列信息");
			return;
		}
		Map<String, String> struct = new HashMap<String, String>(columns.size());
		for (Object[] column : columns) {
			struct.put(column[0]+"", "--"+column[1]+"："+column[2]+(column[3]==null?"":"："+column[3]));
		}
		columns.clear();
		resourceInfo.setStruct(struct);
	}
	/**
	 * 设置sql脚本资源的结构
	 * @param sqlResourceId
	 * @param resourceInfo
	 */
	@SuppressWarnings("unchecked")
	private void setSqlResourceStruct(String sqlResourceId, ResourceInfo resourceInfo) {
		List<Object[]> sqlParams = HibernateUtil.executeListQueryByHqlArr(null, null, "select parameterName,parameterDataType from ComSqlScriptParameter where sqlScriptId=? and parameterFrom=0", sqlResourceId);
		if(sqlParams == null || sqlParams.size() == 0){
			resourceInfo.setMsg("名为["+resourceInfo.getResourceName()+"]的sql脚本资源不存在结构信息，没有任何参数，可以直接调用");
			return;
		}
		Map<String, String> struct = new HashMap<String, String>(sqlParams.size());
		for (Object[] sqlParam : sqlParams) {
			struct.put(sqlParam[0]+"", "--"+sqlParam[1]);
		}
		sqlParams.clear();
		resourceInfo.setStruct(struct);
	}
}