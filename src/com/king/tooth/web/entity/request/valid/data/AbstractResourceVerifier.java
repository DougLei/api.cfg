package com.king.tooth.web.entity.request.valid.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.other.AResourceMetadataInfo;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.DataValidUtil;
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
	protected List<AResourceMetadataInfo> resourceMetadataInfos;
	/**
	 * 父资源的元数据信息集合
	 */
	protected List<AResourceMetadataInfo> parentResourceMetadataInfos;
	
	/**
	 * 验证属性是否不存在
	 * @param validBuiltinParams 是否验证内置参数，是get请求的时候才需要为true，其他请求都是false
	 * @param propName
	 * @param resourceMetadataInfos
	 * @return
	 */
	protected boolean validPropUnExists(boolean validBuiltinParams, String propName, List<AResourceMetadataInfo> resourceMetadataInfos){
		if(validBuiltinParams){
			for (String builtinParams : BuiltinParameterKeys.BUILTIN_PARAMS) { // 内置的参数不做是否存在的验证，因为肯定不存在，是后台使用的一些参数
				if(propName.equals(builtinParams)){
					return false;
				}
			}
		}
		for (AResourceMetadataInfo rmi : resourceMetadataInfos) {
			if(propName.equals(rmi.getPropName())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证数据是否合法
	 * @param desc
	 * @param dataValue
	 * @param rmi
	 * @param index
	 * @return
	 */
	protected String validDataIsLegal(String desc, Object dataValue, AResourceMetadataInfo rmi, int index){
		// 验证数据类型、数据长度、数据精度
		if(DataTypeConstants.BOOLEAN.equals(rmi.getDataType())){
			if(!DataValidUtil.isBoolean(dataValue)){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
			}
		}else if(DataTypeConstants.INTEGER.equals(rmi.getDataType())){
			if(!DataValidUtil.isInteger(dataValue)){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为整数类型";
			}
			if(rmi.getLength() != -1 && dataValue.toString().length() > rmi.getLength()){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
		}else if(DataTypeConstants.DOUBLE.equals(rmi.getDataType())){
			if(!DataValidUtil.isBigDecimal(dataValue)){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为浮点类型";
			}
			dataValueStr = dataValue.toString();
			if(rmi.getLength() != -1 && (dataValueStr.length()-1) > rmi.getLength()){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"]的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
			if(rmi.getPrecision() != -1 && dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值精度，大于实际配置的精度("+rmi.getPrecision()+")";
			}
		}else if(DataTypeConstants.DATE.equals(rmi.getDataType())){
			if(!DataValidUtil.isDate(dataValue)){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为日期类型";
			}
		}else if(DataTypeConstants.STRING.equals(rmi.getDataType())){
			if(rmi.getLength() != -1 && StrUtils.calcStrLength(dataValue.toString()) > rmi.getLength()){
				return desc + "第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
		}else{
			return desc + "第"+index+"个对象，["+rmi.getDescName()+"]，系统目前不支持["+rmi.getDataType()+"]数据类型，请联系后端开发人员";
		}
		return null;
	}
	private String dataValueStr;
	
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
		
		Set<AResourceMetadataInfo> uniqueConstraintProps = new HashSet<AResourceMetadataInfo>(resourceMetadataInfos.size());
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
			
			for (AResourceMetadataInfo rmi : resourceMetadataInfos) {
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
					validDataIsLegalResult = validDataIsLegal(desc, dataValue, rmi, (i+1));
					if(validDataIsLegalResult != null){
						return validDataIsLegalResult;
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
			for (AResourceMetadataInfo uniqueConstraintProp : uniqueConstraintProps) {
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

	/**
	 * 验证
	 * @return
	 */
	public abstract String doValid();
}
