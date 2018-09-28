package com.king.tooth.web.entity.request.valid.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.other.ResourceMetadataInfo;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.build.model.DynamicBasicColumnUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.request.RequestBody;

/**
 * 表资源的数据校验类
 * @author DougLei
 */
public class TableResourceVerifier extends AbstractResourceVerifier{
	
	public TableResourceVerifier(RequestBody requestBody, String resourceName, String parentResourceName) {
		super(requestBody, resourceName, parentResourceName);
	}

	public String doValid(){
		return doValidTableResourceMetadata();
	}
	
	/**
	 * 验证表资源的元数据
	 * @return
	 */
	private String doValidTableResourceMetadata() {
		initTableResourceMetadataInfos();
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
	 * 初始化表资源元数据信息集合
	 * @return
	 */
	private void initTableResourceMetadataInfos() {
		if(requestBody.getResourceInfo().getReqResource().isBuiltinResource()){
			resourceMetadataInfos = getBuiltinTableResourceMetadataInfos(resourceName);
		}else{
			resourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, queryTableMetadataInfosHql , requestBody.getResourceInfo().getReqResource().getRefResourceId());
			if(resourceMetadataInfos == null || resourceMetadataInfos.size() == 0){
				throw new NullPointerException("没有查询到表资源["+resourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
			}
			DynamicBasicColumnUtil.initBasicMetadataInfos(resourceName, resourceMetadataInfos);
		}
		
		if(requestBody.isParentSubResourceQuery()){
			if(requestBody.isRecursiveQuery()){
				parentResourceMetadataInfos = resourceMetadataInfos;
			}else{
				if(requestBody.getResourceInfo().getReqParentResource().isBuiltinResource()){
					parentResourceMetadataInfos = getBuiltinTableResourceMetadataInfos(parentResourceName);
				}else{
					parentResourceMetadataInfos = HibernateUtil.extendExecuteListQueryByHqlArr(ResourceMetadataInfo.class, null, null, queryTableMetadataInfosHql, requestBody.getResourceInfo().getReqParentResource().getRefResourceId());
					if(parentResourceMetadataInfos == null || parentResourceMetadataInfos.size() == 0){
						throw new NullPointerException("没有查询到父表资源["+parentResourceName+"]的元数据信息，请检查配置，或联系后台系统开发人员");
					}
					DynamicBasicColumnUtil.initBasicMetadataInfos(parentResourceName, parentResourceMetadataInfos);
				}
			}
		}
	}
	/** 查询表资源元数据信息集合的hql */
	private static final String queryTableMetadataInfosHql = "select new map(propName as name,columnType as dataType,length as length,precision as precision,isUnique as isUnique,isNullabled as isNullabled, name as descName) from ComColumndata where tableId=? and operStatus="+ComColumndata.CREATED+" order by orderCode asc";
	
	/**
	 * 获取内置表资源的元数据信息集合
	 * @param tableResourceName
	 * @return
	 */
	private List<ResourceMetadataInfo> getBuiltinTableResourceMetadataInfos(String tableResourceName){
		ITable itable = BuiltinResourceInstance.getInstance(tableResourceName, ITable.class);
		List<ComColumndata> columns = itable.getColumnList();
		List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(columns.size());
		for (ComColumndata column : columns) {
			metadataInfos.add(new ResourceMetadataInfo(
					column.getPropName(),
					column.getColumnType(),
					column.getLength(),
					column.getPrecision(),
					0, // column.getIsUnique()，基础字段，不需要验证是否唯一，所以设置为0
					1, // column.getIsNullabled()，基础字段，不需要验证是否可为空，所以设置为1
					column.getName()));
		}
		DynamicBasicColumnUtil.initBasicMetadataInfos(tableResourceName, metadataInfos);
		columns.clear();
		return metadataInfos;
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
	 * 验证post请求的表资源数据
	 * @param isValidIdPropIsNull 是否验证id属性为空
	 * @return
	 */
	private String validPostTableResourceMetadata(boolean isValidIdPropIsNull) {
		IJson ijson = requestBody.getFormData();
		int size = ijson.size();
		
		Set<ResourceMetadataInfo> uniqueConstraintProps = new HashSet<ResourceMetadataInfo>(resourceMetadataInfos.size());
		JSONObject data = null;
		boolean dataValueIsNull;
		Set<String> propKeys = null;
		Object dataValue = null;
		String validDataIsLegalResult = null;
		for(int i=0;i<size;i++){
			data = ijson.get(i);
			if(isValidIdPropIsNull && StrUtils.isEmpty(data.get(ResourcePropNameConstants.ID))){
				return "操作表资源["+resourceName+"]时，第"+(i+1)+"个对象，"+ResourcePropNameConstants.ID+"(主键)属性值不能为空";
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
					return "第"+(i+1)+"个对象，["+rmi.getDescName()+"] 的值不能为空";
				}
				
				if(!dataValueIsNull){
					// 两个大字段类型不用检查
					if(BuiltinDataType.CLOB.equals(rmi.getDataType()) || BuiltinDataType.BLOB.equals(rmi.getDataType())){
						continue;
					}
					validDataIsLegalResult = validDataIsLegal(dataValue, rmi, (i+1));
					if(validDataIsLegalResult != null){
						return validDataIsLegalResult;
					}
					
					// 验证唯一约束
					if(rmi.getIsUnique() == 1){
						uniqueConstraintProps.add(rmi);
						if(validDataIsExists(rmi.getName(), dataValue)){
							return "第"+(i+1)+"个对象，["+rmi.getDescName()+"] 的值["+dataValue+"]已经存在，不能重复添加";
						}
					}
				}
			}
		}
		
		// 验证一次提交的数组中，是否有重复的值，违反了唯一约束
		if(size > 1 && uniqueConstraintProps.size()>0){
			for (ResourceMetadataInfo uniqueConstraintProp : uniqueConstraintProps) {
				for(int i=0;i<size-1;i++){
					dataValue = ijson.get(i).get(uniqueConstraintProp.getName());
					if(StrUtils.notEmpty(dataValue)){
						for(int j=i+1;j<size;j++){
							if(dataValue.equals(ijson.get(j).get(uniqueConstraintProp.getName()))){
								return "第"+(i+1)+"个对象和第"+(j+1)+"个对象，["+uniqueConstraintProp.getDescName()+"] 的值重复，操作失败";
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 验证数据是否已经存在
	 * @param propName
	 * @param dataValue
	 * @return
	 */
	private boolean validDataIsExists(String propName, Object dataValue) {
		long count = (long)HibernateUtil.executeUniqueQueryByHqlArr("select count("+ResourcePropNameConstants.ID+") from " + resourceName + " where " + propName + "=? and projectId=? and customerId=?", dataValue, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
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
}
