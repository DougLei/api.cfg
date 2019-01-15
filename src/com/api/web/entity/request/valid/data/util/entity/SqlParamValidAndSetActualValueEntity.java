package com.api.web.entity.request.valid.data.util.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.api.constants.DataTypeConstants;
import com.api.plugins.ijson.IJson;
import com.api.plugins.ijson.IJsonUtil;
import com.api.sys.builtin.data.BuiltinParameters;
import com.api.sys.entity.cfg.CfgSql;
import com.api.sys.entity.cfg.CfgSqlParameter;
import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.api.util.DateUtil;
import com.api.util.JsonUtil;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;
import com.api.util.datatype.DataTypeValidUtil;
import com.api.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * sql参数的验证和set实际值
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlParamValidAndSetActualValueEntity extends SqlParamSetActualValueEntity{
	
	public SqlParamValidAndSetActualValueEntity() {
	}
	public SqlParamValidAndSetActualValueEntity(CfgSql sql, boolean isGetRequest, List<List<CfgSqlParameter>> actualParamsList, List<ResourceMetadataInfo> resourceMetadataInfos, List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList) {
		this.sql = sql;
		this.isGetRequest = isGetRequest;
		this.actualParamsList = actualParamsList;
		this.resourceMetadataInfos = resourceMetadataInfos;
		this.inSqlResultSetMetadataInfoList = inSqlResultSetMetadataInfoList;
		
		sqlParams = sql.getSqlParams();
		resourceName = sql.getResourceName();
	}
	
	/**
	 * sql脚本实际传入的参数集合
	 */
	private List<List<CfgSqlParameter>> actualParamsList;
	/**
	 * 资源的元数据信息集合
	 */
	protected List<ResourceMetadataInfo> resourceMetadataInfos;
	/**
	 * 是否是get请求
	 */
	private boolean isGetRequest;
	
	/**
	 * 要操作的sql对象
	 */
	private CfgSql sql;
	/**
	 * 针对procedure sql传入表对象的元数据信息集合
	 */
	private List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList;
	private List<CfgSqlParameter> sqlParams;
	private String resourceName;
	
	public CfgSql getSql() {
		return sql;
	}
	
	/**
	 * 对每个值进行验证
	 * <p>同时将调用sql资源时，实际传过来的值存到参数集合中(sql参数集合/procedure sql参数集合)</p>
	 * @return 
	 */
	public String doValidAndSetActualParams() {
		if((actualParamsList == null || actualParamsList.size() == 0) && (this.sqlParams == null || this.sqlParams.size() == 0)){
			return null;
		}
		// 如果实际传入了参数，但是配置的sql却没有任何参数，也是有问题的
		if(this.sqlParams == null || this.sqlParams.size() == 0){
			return "在调用sql资源时，传入的实际参数信息为：["+JsonUtil.toJsonString(actualParamsList, false)+"]，但是被调用的sql资源["+resourceName+"]却不存在任何参数信息。请检查该sql资源是否确实有参数配置，并确认是否合法调用sql资源";
		}
		
		List<List<CfgSqlParameter>> sqlParamsList = null;// set实际传入的参数值的集合
		List<CfgSqlParameter> sqlParams = null;
		String analysisActualInValueResult = null;
		
		int index = 1;
		// 如果没有传入任何参数，还能进入到这里，说明配置的参数要么是系统内置，要么是自动生成，要么是有默认值的
		if((actualParamsList == null || actualParamsList.size() == 0)){
			// 如果配置了sql参数，但是实际调用的时候没有传入任何参数
			for (CfgSqlParameter sqlParam : this.sqlParams) {
				if(sqlParam.getValueFrom() == CfgSqlParameter.USER_INPUT && StrUtils.isEmpty(sqlParam.getDefaultValue())){
					return "在调用sql资源时，必须传入名为"+sqlParam.getName()+"的参数值";
				}
			}
			
			sqlParamsList = new ArrayList<List<CfgSqlParameter>>(1);
			sqlParams = this.sqlParams;
			sqlParamsList.add(sqlParams);
			
			for (CfgSqlParameter ssp : sqlParams) {
				analysisActualInValueResult = analysisActualInValue(ssp, isGetRequest, null, index++);
				if(analysisActualInValueResult != null){
					return analysisActualInValueResult;
				}
			}
		}
		// 否则就去解析实际的参数，将其和配置的参数进行匹配
		else{
			sqlParamsList = new ArrayList<List<CfgSqlParameter>>(actualParamsList.size());
			
			for (List<CfgSqlParameter> actualParams : actualParamsList) {
				sqlParams = sql.cloneSqlParams();
				sqlParamsList.add(sqlParams);
				
				for (CfgSqlParameter ssp : sqlParams) {
					for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
						if(ssp.getName().equals(rmi.getPropName())){
							if(ssp.getValueFrom() == CfgSqlParameter.SYSTEM_BUILTIN){// 参数值来源为系统内置
								analysisActualInValueResult = analysisActualInValue(ssp, isGetRequest, null, index);
							}else if(ssp.getValueFrom() == CfgSqlParameter.USER_INPUT){// 参数值来源为用户输入
								if(actualParams != null && actualParams.size() > 0){
									for (CfgSqlParameter ssap : actualParams) {
										if(ssp.getName().equals(ssap.getName())){
											ssp.setActualInValue(ssap.getActualInValue());
											break;
										}
									}
								}
								analysisActualInValueResult = analysisActualInValue(ssp, isGetRequest, rmi, index);
							}
							if(analysisActualInValueResult != null){
								return analysisActualInValueResult;
							}
							break;
						}
					}	
				}
				index++;
				inSqlResultSetMetadataInfoIndex = 0;
			}
			this.sqlParams.clear();
		}
		sql.setSqlParamsList(sqlParamsList);
		return null;
	}
	
	/**
	 * 解析实际传入的参数值，同时进行校验
	 * @param ssp
	 * @param isGetRequest 是否是get请求
	 * @param rmi 对应的元数据信息对象，用来做校验
	 *            同时，该验证只针对用户输入的参数值，内置的参数值则不做校验
	 *            同时，该验证只针对占位符参数进行验证
	 * @param index
	 * @return
	 */
	private String analysisActualInValue(CfgSqlParameter ssp, boolean isGetRequest, ResourceMetadataInfo rmi, int index) {
		String desc = "操作sql资源时，";
		if(rmi != null && !ssp.getDataType().equals(rmi.getDataType())){
			return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 参数，配置的数据类型("+ssp.getDataType()+")和实际加载的数据类型("+rmi.getDataType()+")不一致，请联系后端系统开发人员";
		}
		
		if(ssp.getIsNullabled() == 1){
			return null;
		}
		if(ssp.getValueFrom() == CfgSqlParameter.USER_INPUT){
			if(ssp.getActualInValue() == null){
				if(ssp.getDefaultValue() == null){
					return desc+"第"+index+"个对象，必须传入名为"+ssp.getName()+"的参数值";
				}
				ssp.setActualInValue(ssp.getDefaultValue());
			}
			if(rmi != null){
				actualInValue = ssp.getActualInValue();
				if(actualInValue == null){
					return desc+"必须要传入的参数["+ssp.getName()+"]，请修改调用方式，传入该参数值";
				}
				
				dataValueStr = actualInValue.toString();
				if(BuiltinParameters.isBuiltinParams(dataValueStr)){
					actualInValue = BuiltinParameters.getBuiltinQueryParamValue(dataValueStr);
				}
				
				if(ssp.getIsPlaceholder() == 1){
					// 无论是什么类型的请求，日期类型都是string类型，都要进行转换
					if(DataTypeConstants.DATE.equals(ssp.getDataType())){
						if(DataTypeValidUtil.isDate(actualInValue)){
							actualInValue = DateUtil.parseSqlTimestamp(dataValueStr);
						}else{
							return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为日期类型";
						}
					}else{
						if(isGetRequest){// get请求，值都是string类型，需要进行转换
							if(DataTypeConstants.INTEGER.equals(ssp.getDataType())){
								if(DataTypeValidUtil.isInteger(dataValueStr)){
									actualInValue = Integer.valueOf(dataValueStr);
									if(rmi.getLength() != -1 && dataValueStr.length() > rmi.getLength()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
									}
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为整数类型";
								}
							}else if(DataTypeConstants.DOUBLE.equals(ssp.getDataType())){
								if(DataTypeValidUtil.isNumber(dataValueStr)){
									actualInValue = BigDecimal.valueOf(Double.valueOf(dataValueStr));
									if(rmi.getLength() != -1 && (dataValueStr.length()-1) > rmi.getLength()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"]的值长度，大于实际配置的长度("+rmi.getLength()+")";
									}
									if(rmi.getPrecision() != -1 && dataValueStr.indexOf(".")!=-1 &&  dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值精度，大于实际配置的精度("+rmi.getPrecision()+")";
									}
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为浮点类型[或数字类型]";
								}
							}else if(DataTypeConstants.BOOLEAN.equals(ssp.getDataType())){
								if(DataTypeValidUtil.isBoolean(dataValueStr)){
									actualInValue = ("true".equals(dataValueStr))? "1":"0";
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
								}
							}else if(DataTypeConstants.STRING.equals(ssp.getDataType())){
								if(rmi.getLength() != -1 && StrUtils.calcStrLength(dataValueStr) > rmi.getLength()){
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
								}
							}else{
								return desc+"第"+index+"个对象，["+rmi.getDescName()+"]，系统目前不支持["+ssp.getDataType()+"]数据类型，请联系后端开发人员";
							}
						}else{// 否则就是post请求，直接判断，不需要转换
							if(ssp.getIsTableType() == 1){
								if(inSqlResultSetMetadataInfoList == null || inSqlResultSetMetadataInfoList.size() == 0){
									return "["+ssp.getName()+"] 参数关联的表类型，没有查询到对应的列的元数据信息集合，请联系后端开发人员";
								}
								
								actualInValue = IJsonUtil.getIJson(dataValueStr);
								return validProcedureSqlInResultSet(desc, ssp, index, (IJson)actualInValue);
							}else{
								validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(actualInValue, rmi);
								if(validDataIsLegalResult != null){
									return desc + "第"+index+"个对象，" + validDataIsLegalResult;
								}
								return null;
							}
						}
					}
				}else{
					actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
				}
			}
		}else if(ssp.getValueFrom() == CfgSqlParameter.SYSTEM_BUILTIN){
			actualInValue = BuiltinParameters.getBuiltinQueryParamValue(ssp.getName());
			if(actualInValue == null){
				return desc+"内置参数["+ssp.getName()+"]的值为空，请联系后端系统开发人员";
			}
			if(ssp.getIsPlaceholder() == 0){
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else if(ssp.getValueFrom() == CfgSqlParameter.AUTO_CODE){
			// 自动编码的值，在操作的时候再赋值
		}else{
			return "valueFrom的值，仅限于：[0(用户输入)、1(系统内置)、2(自动编码)]，本次处理的值为["+ssp.getValueFrom()+"]，请联系后端系统开发人员";
		}
		ssp.setActualInValue(actualInValue);
		return null;
	}
	private Object actualInValue;
	private String dataValueStr;
	private String validDataIsLegalResult;
	
	/**
	 * 验证存储过程传入的结果集
	 * @param desc 
	 * @param ssp
	 * @param index
	 * @param ijson
	 * @return
	 */
	private String validProcedureSqlInResultSet(String desc, CfgSqlParameter ssp, int index, IJson ijson) {
		List<ResourceMetadataInfo> inSqlResultSetMetadataInfos = inSqlResultSetMetadataInfoList.get(inSqlResultSetMetadataInfoIndex++);
		if(inSqlResultSetMetadataInfos == null || inSqlResultSetMetadataInfos.size() == 0){
			return desc+"第"+index+"个对象，["+ssp.getName()+"] 参数关联的表类型，没有查询到对应的列的元数据信息集合，请联系后端开发人员";
		}
		if(ijson != null && ijson.size() > 0){
			return TableResourceValidUtil.validTableResourceMetadata(desc+"操作第"+index+"个对象，["+ssp.getName()+"] 参数关联的表对象中，", null, inSqlResultSetMetadataInfos, ijson, false, false);
		}
		return null;
	}
	/** 如果调用存储过程，传入表参数类型，这里记录表参数元数据集合的下标，用来取对应的元数据集合来做数据验证 */
	private int inSqlResultSetMetadataInfoIndex = 0;
}
