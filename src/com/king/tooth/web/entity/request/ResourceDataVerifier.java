package com.king.tooth.web.entity.request;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 资源的数据校验类
 * @author DougLei
 */
public class ResourceDataVerifier {
	
	private RequestBody requestBody;
	private String resourceName;
	private String parentResourceName;
	
	/**
	 * 父资源的元数据信息集合
	 */
	private List<ResourceMetadataInfo> parentResourceMetadataInfos;
	/**
	 * 资源的元数据信息集合
	 */
	private List<ResourceMetadataInfo> resourceMetadataInfos;
	
	public ResourceDataVerifier(RequestBody requestBody) {
		this.requestBody = requestBody;
		resourceName = requestBody.getRouteBody().getResourceName();
		parentResourceName = requestBody.getRouteBody().getParentResourceName();
	}

	//------------------------------------------------------------------
	public void clear(){
		if(parentResourceMetadataInfos != null && parentResourceMetadataInfos.size() > 0){
			parentResourceMetadataInfos.clear();
		}
		if(resourceMetadataInfos != null && resourceMetadataInfos.size() > 0){
			resourceMetadataInfos.clear();
		}
	}
	
	/**
	 * 校验请求资源的数据
	 * @param requestBody
	 * @return
	 */
	public String doValidResourceData() {
		if(requestBody.getResourceInfo().isTableResource()){
			initTableResourceMetadataInfos();
			return validTableResourceMetadata();
		}
		if(requestBody.getResourceInfo().isSqlResource()){
			initSqlResourceMetadataInfos();
			return validSqlResourceMetadata();
		}
		if(requestBody.getResourceInfo().isCodeResource()){
			return validCodeResourceMetadata();
		}
		return "系统目前只存在[表、sql脚本、代码]三种资源类型，本次请求的资源类型为["+requestBody.getResourceInfo().getResourceType()+"]，请联系后台系统开发人员";
	}
	
	/**
	 * 初始化表资源元数据信息集合
	 * @return
	 */
	private void initTableResourceMetadataInfos() {
		resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, "select new map(propName as name,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled) from ComColumndata where tableId=? and operStatus=? order by orderCode asc", requestBody.getResourceInfo().getReqResource().getRefResourceId(), ComColumndata.CREATED);
		if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
			throw new NullPointerException("没有查询到表资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
		}
	}
	
	/**
	 * 初始化sql资源元数据信息集合
	 * @return
	 */
	private void initSqlResourceMetadataInfos() {
		resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, "select new map(parameterName as name,parameterDataType as dataType,length as length,precision as precision) from ComSqlScriptParameter where sqlScriptId=? order by orderCode asc", requestBody.getResourceInfo().getReqResource().getRefResourceId(), ComColumndata.CREATED);
		if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
			throw new NullPointerException("没有查询到sql资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
		}
	}

	// ------------------------------------------------------------------------------------------------
	/**
	 * 验证表资源的元数据
	 * @return
	 */
	private String validTableResourceMetadata() {
		if(requestBody.isGetRequest()){
			return null;
		}else if(requestBody.isPostRequest()){
			return validPostTableResourceMetadata(false);
		}else if(requestBody.isPutRequest()){
			return validPutTableResourceMetadata();
		}else if(requestBody.isDeleteRequest()){
			return validDeleteTableResourceMetadata();
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
	
	/**
	 * 验证post请求的表资源数据
	 * @param isValidIdPropIsNull 是否验证id属性为空
	 * @return
	 */
	private String validPostTableResourceMetadata(boolean isValidIdPropIsNull) {
		IJson ijson = requestBody.getFormData();
		int size = ijson.size();
		
		Set<String> uniqueConstraintPropName = new HashSet<String>(resourceMetadataInfos.size());
		JSONObject data = null;
		boolean dataValueIsNull;
		Object dataValue = null;
		for(int i=0;i<size;i++){
			data = ijson.get(i);
			if(isValidIdPropIsNull && StrUtils.isEmpty(data.get(ResourcePropNameConstants.ID))){
				return "操作表资源["+resourceName+"]时，第"+(i+1)+"个对象，"+ResourcePropNameConstants.ID+"属性值不能为空";
			}
			
			for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
//				if(BuiltinDataType.CLOB.equals(rmi.getDataType()) || BuiltinDataType.BLOB.equals(rmi.getDataType())){
//					continue;
//				}
				dataValue = data.get(rmi.getName());
				dataValueIsNull = StrUtils.isEmpty(dataValue);
				
				// 验证不能为空
				if(rmi.getIsNullabled() == 0 && dataValueIsNull){
					return "保存表资源["+resourceName+"]时，第"+(i+1)+"个对象，属性名为["+rmi.getName()+"]的值不能为空";
				}
				
				if(!dataValueIsNull){
					// 验证数据类型
					rmi.getDataType();
					
					// 验证数据长度
					rmi.getLength();
					
					// 验证数据精度
					rmi.getPrecision();
					
					// 验证唯一约束
					if(rmi.getIsUnique() == 1){
						uniqueConstraintPropName.add(rmi.getName());
						if(validDataIsExists(rmi.getName(), dataValue)){
							return "保存表资源["+resourceName+"]时，第"+(i+1)+"个对象，属性名为["+rmi.getName()+"]的值["+dataValue+"]已经存在，不能重复添加";
						}
					}
				}
			}
		}
		
		// 验证一次提交的数组中，是否有重复的值，违反了唯一约束
		if(size > 1 && uniqueConstraintPropName.size()>0){
			for (String propName : uniqueConstraintPropName) {
				for(int i=0;i<size-1;i++){
					dataValue = ijson.get(i).get(propName);
					if(StrUtils.notEmpty(dataValue)){
						for(int j=i+1;j<size;j++){
							if(dataValue.equals(ijson.get(j).get(propName))){
								uniqueConstraintPropName.clear();
								return "保存表资源["+resourceName+"]时，第"+(i+1)+"个对象和第"+(j+1)+"个对象，属性名为["+propName+"]的值相同，不能重复添加";
							}
						}
					}
				}
			}
		}
		uniqueConstraintPropName.clear();
		return null;
	}
	
	/**
	 * 验证数据是否已经存在
	 * @param propName
	 * @param dataValue
	 * @return
	 */
	private boolean validDataIsExists(String propName, Object dataValue) {
		long count = (long)HibernateUtil.executeUniqueQueryByHqlArr("select count(1) from " + resourceName + " where " + propName + "=? and projectId=? and customerId=?", dataValue, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		return (count > 0);
	}

	/**
	 * 验证put请求的表资源数据
	 * @return
	 */
	private String validPutTableResourceMetadata() {
		return validPostTableResourceMetadata(true);
	}
	
	/**
	 * 验证delete请求的表资源数据
	 * @return
	 */
	private String validDeleteTableResourceMetadata() {
		if(StrUtils.isEmpty(requestBody.getRequestUrlParams().get(BuiltinParameterKeys._IDS))){
			return "要删除["+resourceName+"]资源时，_ids参数值不能为空";
		}
		return null;
	}
	// ------------------------------------------------------------------------------------------------
	/**
	 * 验证sql资源的元数据
	 * @return
	 */
	private String validSqlResourceMetadata() {
		if(requestBody.isGetRequest()){
			return null;
		}else if(requestBody.isPostRequest()){
			return null;
		}else if(requestBody.isPutRequest()){
			return null;
		}else if(requestBody.isDeleteRequest()){
			return null;
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
	
	// ------------------------------------------------------------------------------------------------
	/**
	 * 验证代码资源的元数据
	 * @return
	 */
	private String validCodeResourceMetadata() {
		if(requestBody.isGetRequest()){
			return null;
		}else if(requestBody.isPostRequest()){
			return null;
		}else if(requestBody.isPutRequest()){
			return null;
		}else if(requestBody.isDeleteRequest()){
			return null;
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
}
