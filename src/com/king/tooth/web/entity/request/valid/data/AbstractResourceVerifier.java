package com.king.tooth.web.entity.request.valid.data;

import java.math.BigDecimal;
import java.util.List;

import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.other.ResourceMetadataInfo;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.StrUtils;
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
			if(propName.equals(rmi.getName())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证数据是否合法
	 * @param resourceTypeDesc
	 * @param dataValue
	 * @param rmi
	 * @param index
	 * @return
	 */
	protected String validDataIsLegal(Object dataValue, ResourceMetadataInfo rmi, int index){
		// 验证数据类型、数据长度、数据精度
		if(BuiltinDataType.BOOLEAN.equals(rmi.getDataType())){
			if(!(dataValue instanceof Boolean)){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
			}
		}else if(BuiltinDataType.INTEGER.equals(rmi.getDataType())){
			if(!(dataValue instanceof Integer)){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为整数类型";
			}
			if(dataValue.toString().length() > rmi.getLength()){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
		}else if(BuiltinDataType.DOUBLE.equals(rmi.getDataType())){
			if(!(dataValue instanceof BigDecimal)){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为浮点类型";
			}
			dataValueStr = dataValue.toString();
			if((dataValueStr.length()-1) > rmi.getLength()){
				return "第"+index+"个对象，["+rmi.getDescName()+"]的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
			if(dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值精度，大于实际配置的精度("+rmi.getPrecision()+")";
			}
		}else if(BuiltinDataType.DATE.equals(rmi.getDataType())){
			if(!DateUtil.valueIsDateFormat(dataValue)){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为日期类型";
			}
		}else if(BuiltinDataType.STRING.equals(rmi.getDataType())){
			if(StrUtils.calcStrLength(dataValue.toString()) > rmi.getLength()){
				return "第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
			}
		}else{
			return "第"+index+"个对象，["+rmi.getDescName()+"]，系统目前不支持["+rmi.getDataType()+"]数据类型，请联系后端开发人员";
		}
		return null;
	}
	private String dataValueStr;
	
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
