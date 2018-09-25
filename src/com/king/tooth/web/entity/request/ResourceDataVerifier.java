package com.king.tooth.web.entity.request;

import java.math.BigDecimal;
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
import com.king.tooth.util.DateUtil;
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
	 * 资源的元数据信息集合
	 */
	private List<ResourceMetadataInfo> resourceMetadataInfos;
	/**
	 * 父资源的元数据信息集合
	 */
	private List<ResourceMetadataInfo> parentResourceMetadataInfos;
	
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
		if(requestBody.getResourceInfo().getReqResource().isBuiltinResource()){
			resourceMetadataInfos = getBuiltinTableResourceMetadataInfos(requestBody.getResourceInfo().getReqResource().getResourceName());
		}else{
			resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, "select new map(propName as name,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled) from ComColumndata where tableId=? and operStatus=? order by orderCode asc", requestBody.getResourceInfo().getReqResource().getRefResourceId(), ComColumndata.CREATED);
			if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
				throw new NullPointerException("没有查询到表资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			if(requestBody.isRecursiveQuery()){
				parentResourceMetadataInfos = resourceMetadataInfos;
			}else{
				if(requestBody.getResourceInfo().getReqParentResource().isBuiltinResource()){
					parentResourceMetadataInfos = getBuiltinTableResourceMetadataInfos(requestBody.getResourceInfo().getReqParentResource().getResourceName());
				}else{
					parentResourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, "select new map(propName as name,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled) from ComColumndata where tableId=? and operStatus=? order by orderCode asc", requestBody.getResourceInfo().getReqParentResource().getRefResourceId(), ComColumndata.CREATED);
					if(parentResourceMetadataInfos == null || parentResourceMetadataInfos.size() == 0){
						throw new NullPointerException("没有查询到父表资源["+parentResourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
					}
				}
			}
		}
	}
	
	/**
	 * 获取内置表资源的元数据信息集合
	 * @param tableResourceName
	 * @return
	 */
	private List<ResourceMetadataInfo> getBuiltinTableResourceMetadataInfos(String tableResourceName){
//		BuiltinResourceInstance
		return null;
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
			return validGetTableResourceMetadata();
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
	 * 验证get请求的表资源数据
	 * @return
	 */
	private String validGetTableResourceMetadata() {
		Set<String> requestResourcePropNames = requestBody.getRequestResourceParams().keySet();
		for (String propName : requestResourcePropNames) {
			if(validPropUnExists(true, propName, resourceMetadataInfos)){
				return "操作表资源["+resourceName+"]时，不存在名为["+propName+"]的属性";
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			requestResourcePropNames = requestBody.getRequestParentResourceParams().keySet();
			for (String propName : requestResourcePropNames) {
				if(validPropUnExists(true, propName, parentResourceMetadataInfos)){
					return "操作父表资源["+parentResourceName+"]时，不存在名为["+propName+"]的属性";
				}
			}
		}
		return null;
	}
	
	/**
	 * 验证属性是否不存在
	 * @param validBuiltinParams 是否验证内置参数，是get请求的时候才需要为true，其他请求都是false
	 * @param propName
	 * @param resourceMetadataInfos
	 * @return
	 */
	private boolean validPropUnExists(boolean validBuiltinParams, String propName, List<ResourceMetadataInfo> resourceMetadataInfos){
		if(validBuiltinParams){
			for (String builtinParams : BuiltinParameterKeys.BUILTIN_PARAMS) { // 内置的参数不做是否存在的验证，因为肯定不存在，是后台使用的一些参数
				if(propName.equals(builtinParams)){
					return false;
				}
			}
		}
		
		for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
			if(propName.equals(rmi.getName())){
				return false;
			}
		}
		return true;
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
		Set<String> propKeys = null;
		Object dataValue = null;
		String validDataIsLegalResult = null;
		for(int i=0;i<size;i++){
			data = ijson.get(i);
			if(isValidIdPropIsNull && StrUtils.isEmpty(data.get(ResourcePropNameConstants.ID))){
				return "操作表资源["+resourceName+"]时，第"+(i+1)+"个对象，"+ResourcePropNameConstants.ID+"属性值不能为空";
			}
			
			// 验证每个对象的属性，是否存在
			propKeys = data.keySet();
			for (String propName : propKeys) {
				if(validPropUnExists(false, propName, resourceMetadataInfos)){
					return "操作表资源["+resourceName+"]时，第"+(i+1)+"个对象，不存在名为["+propName+"]的属性";
				}
			}
			
			
			for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
				dataValue = data.get(rmi.getName());
				dataValueIsNull = StrUtils.isEmpty(dataValue);
				
				// 验证不能为空
				if(rmi.getIsNullabled() == 0 && dataValueIsNull){
					return "操作表资源["+resourceName+"]时，第"+(i+1)+"个对象，属性名为["+rmi.getName()+"]的值不能为空";
				}
				
				if(!dataValueIsNull){
					// 两个大字段类型不用检查
					if(BuiltinDataType.CLOB.equals(rmi.getDataType()) || BuiltinDataType.BLOB.equals(rmi.getDataType())){
						continue;
					}
					validDataIsLegalResult = validDataIsLegal("表", dataValue, rmi, (i+1));
					if(validDataIsLegalResult != null){
						return validDataIsLegalResult;
					}
					
					// 验证唯一约束
					if(rmi.getIsUnique() == 1){
						uniqueConstraintPropName.add(rmi.getName());
						if(validDataIsExists(rmi.getName(), dataValue)){
							return "操作表资源["+resourceName+"]时，第"+(i+1)+"个对象，属性名为["+rmi.getName()+"]的值["+dataValue+"]已经存在，不能重复添加";
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
	 * 验证数据是否合法
	 * @param resourceTypeDesc
	 * @param dataValue
	 * @param rmi
	 * @param index
	 * @return
	 */
	private String validDataIsLegal(String resourceTypeDesc, Object dataValue, ResourceMetadataInfo rmi, int index){
		// 验证数据类型、数据长度、数据精度
		if(BuiltinDataType.BOOLEAN.equals(rmi.getDataType())){
			if(!(dataValue instanceof Boolean)){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值不合法，应为布尔值类型";
			}
		}else if(BuiltinDataType.INTEGER.equals(rmi.getDataType())){
			if(!(dataValue instanceof Integer)){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值不合法，应为整数类型";
			}
			if(dataValue.toString().length() > rmi.getLength()){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值长度，大于实际配置的长度限制";
			}
		}else if(BuiltinDataType.DOUBLE.equals(rmi.getDataType())){
			if(!(dataValue instanceof BigDecimal)){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值不合法，应为浮点类型";
			}
			dataValueStr = dataValue.toString();
			if((dataValueStr.length()-1) > rmi.getLength()){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值长度，大于实际配置的长度限制";
			}
			if(dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值的精度，大于实际配置的精度限制";
			}
		}else if(BuiltinDataType.DATE.equals(rmi.getDataType())){
			if(!DateUtil.valueIsDateFormat(dataValue)){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值不合法，应为日期类型";
			}
		}else if(BuiltinDataType.STRING.equals(rmi.getDataType())){
			if(StrUtils.calcStrLength(dataValue.toString()) > rmi.getLength()){
				return "操作"+resourceTypeDesc+"资源["+resourceName+"]时，第"+index+"个对象，属性名为["+rmi.getName()+"]的值长度，大于实际配置的长度限制";
			}
		}
		return null;
	}
	private String dataValueStr;
	
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
		if(StrUtils.isEmpty(requestBody.getRequestResourceParams().get(BuiltinParameterKeys._IDS))){
			return "要删除["+resourceName+"]资源时，"+BuiltinParameterKeys._IDS+"参数值不能为空";
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
			// TODO
			return null;
		}else if(requestBody.isPostRequest()){
			// TODO
			return null;
		}else if(requestBody.isPutRequest()){
			// TODO
			return null;
		}else if(requestBody.isDeleteRequest()){
			// TODO
			return null;
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
	
	// ------------------------------------------------------------------------------------------------
	/**
	 * 验证代码资源的元数据
	 * <p>**代码资源的元数据验证，放到各个代码资源中去自行验证，这里不做统一处理**</p>
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
