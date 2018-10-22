package com.king.tooth.web.entity.request.valid.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.entity.request.RequestBody;

/**
 * 资源的数据校验父类
 * @author DougLei
 */
public abstract class AbstractResourceVerifier {

	protected RequestBody requestBody;
	protected String resourceName;
	protected String parentResourceName;
	
	public AbstractResourceVerifier(RequestBody requestBody, String resourceName, String parentResourceName) {
		this.requestBody = requestBody;
		this.resourceName = resourceName;
		this.parentResourceName = parentResourceName;
	}

	/**
	 * 资源的元数据信息集合
	 */
	protected List<ResourceMetadataInfo> resourceMetadataInfos;
	/**
	 * 父资源的元数据信息集合
	 */
	protected List<ResourceMetadataInfo> parentResourceMetadataInfos;
	
	/**
	 * 验证属性是否不存在
	 * @param validBuiltinParams 是否验证内置参数，是get请求的时候才需要为true，其他请求都是false
	 * @param propName
	 * @param resourceMetadataInfos
	 * @return
	 */
	protected boolean validPropUnExists(boolean validBuiltinParams, String propName, List<ResourceMetadataInfo> resourceMetadataInfos){
		if(validBuiltinParams){
			for (String builtinParams : BuiltinParameterKeys.BUILTIN_PARAMS) { // 内置的参数不做是否存在的验证，因为肯定不存在，是后台使用的一些参数
				if(propName.equals(builtinParams)){
					return false;
				}
			}
		}
		for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
			if(propName.equals(rmi.getPropName())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证表资源的元数据
	 * @param desc
	 * @param ijson
	 * @param isUpdate 是否是修改，如果是修改，则要验证id属性为空
	 * @param isValidUniqueInDb 是否在数据库中验证值唯一(如果是表类型，则不需要去数据库的实际表中验证是否唯一，因为目前也无法验证)
	 * @return
	 */
	protected String validTableResourceMetadata(String desc, IJson ijson, boolean isUpdate, boolean isValidUniqueInDb){
		int size = ijson.size();
		
		Set<ResourceMetadataInfo> uniqueConstraintProps = new HashSet<ResourceMetadataInfo>(resourceMetadataInfos.size());
		JSONObject data = null;
		Object dataIdValue = null;
		boolean dataValueIsNull;
		Set<String> propKeys = null;
		Object dataValue = null;
		String validDataIsLegalResult = null;
		for(int i=0;i<size;i++){
			data = ijson.get(i);
			dataIdValue = data.get(ResourcePropNameConstants.ID);
			if(isUpdate && StrUtils.isEmpty(dataIdValue)){
				return desc + "第"+(i+1)+"个对象，"+ResourcePropNameConstants.ID+"(主键)属性值不能为空";
			}
			
			// 验证每个对象的属性，是否存在
			propKeys = data.keySet();
			for (String propName : propKeys) {
				if(validPropUnExists(false, propName, resourceMetadataInfos)){
					return desc + "第"+(i+1)+"个对象，不存在名为["+propName+"]的属性";
				}
			}
			
			for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
				dataValue = data.get(rmi.getPropName());
				dataValueIsNull = StrUtils.isEmpty(dataValue);
				
				// 验证不能为空
				if(rmi.getIsNullabled() == 0 && dataValueIsNull){
					return desc + "第"+(i+1)+"个对象，["+rmi.getDescName()+"] 的值不能为空";
				}
				
				if(!dataValueIsNull){
					// 两个大字段类型不用检查
					if(DataTypeConstants.CLOB.equals(rmi.getDataType()) || DataTypeConstants.BLOB.equals(rmi.getDataType())){
						continue;
					}
					validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(dataValue, rmi);
					if(validDataIsLegalResult != null){
						return desc + "第"+(i+1)+"个对象，" + validDataIsLegalResult;
					}
					
					// 验证唯一约束
					if(rmi.getIsUnique() == 1){
						uniqueConstraintProps.add(rmi);
						if(isValidUniqueInDb && validDataIsExists(rmi.getPropName(), dataValue, isUpdate, dataIdValue)){
							return desc + "第"+(i+1)+"个对象，["+rmi.getDescName()+"] 的值["+dataValue+"]已经存在，不能重复添加";
						}
					}
				}
			}
		}
		
		// 验证一次提交的数组中，是否有重复的值，违反了唯一约束
		if(size > 1 && uniqueConstraintProps.size()>0){
			for (ResourceMetadataInfo uniqueConstraintProp : uniqueConstraintProps) {
				for(int i=0;i<size-1;i++){
					dataValue = ijson.get(i).get(uniqueConstraintProp.getPropName());
					if(StrUtils.notEmpty(dataValue)){
						for(int j=i+1;j<size;j++){
							if(dataValue.equals(ijson.get(j).get(uniqueConstraintProp.getPropName()))){
								return desc + "第"+(i+1)+"个对象和第"+(j+1)+"个对象，["+uniqueConstraintProp.getDescName()+"] 的值重复，操作失败";
							}
						}
					}
				}
			}
		}
		
		if(uniqueConstraintProps.size() > 0){
			uniqueConstraintProps.clear();
		}
		return null;
	}
	
	/**
	 * 验证数据是否已经存在
	 * @param propName
	 * @param dataValue
	 * @param isUpdate 是否是修改
	 * @param dataIdValue 
	 * @return
	 */
	private boolean validDataIsExists(String propName, Object dataValue, boolean isUpdate, Object dataIdValue) {
		Object id = HibernateUtil.executeUniqueQueryByHqlArr("select "+ResourcePropNameConstants.ID+" from " + resourceName + " where " + propName + "=? and projectId=? and customerId=?", dataValue, CurrentThreadContext.getProjectId(), CurrentThreadContext.getCustomerId());
		if(isUpdate){
			// 如果是修改的话，要先判断是否能查询到数据，如果查询不到数据，则证明不存在
			if(id == null){
				return false;
			}
			// 如果查询到数据，再判断查询到的数据id是否和当前操作的数据id一致，如果一致，忽略唯一性验证
			if(id.toString().equals(dataIdValue.toString())){
				return false;
			}
			// 否则就是数据出现重复
			return true;
		}else{
			// 如果是添加的话，则只要查询到数据，就是已存在
			return (id != null);
		}
	}
	
	public void clear(){
		if(parentResourceMetadataInfos != null && parentResourceMetadataInfos.size() > 0){
			parentResourceMetadataInfos.clear();
		}
		if(resourceMetadataInfos != null && resourceMetadataInfos.size() > 0){
			resourceMetadataInfos.clear();
		}
	}

	// ------------------------------------------------------------------------------------------
	/**
	 * 验证
	 * @return
	 */
	public abstract String doValid();
}
