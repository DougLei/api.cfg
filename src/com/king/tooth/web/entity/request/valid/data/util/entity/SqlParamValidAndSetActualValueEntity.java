package com.king.tooth.web.entity.request.valid.data.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.builtin.data.BuiltinQueryParameters;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.datatype.DataTypeValidUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;
import com.king.tooth.web.entity.request.ResourcePropCodeRule;
import com.king.tooth.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * sql参数的验证和set实际值
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlParamValidAndSetActualValueEntity implements Serializable{
	
	public SqlParamValidAndSetActualValueEntity() {
	}
	public SqlParamValidAndSetActualValueEntity(ComSqlScript sql, boolean isGetRequest, List<List<ComSqlScriptParameter>> actualParamsList, List<ResourceMetadataInfo> resourceMetadataInfos, List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList, ResourcePropCodeRule resourcePropCodeRule) {
		this.sql = sql;
		this.isGetRequest = isGetRequest;
		this.actualParamsList = actualParamsList;
		this.resourceMetadataInfos = resourceMetadataInfos;
		this.inSqlResultSetMetadataInfoList = inSqlResultSetMetadataInfoList;
		this.resourcePropCodeRule = resourcePropCodeRule;
		
		sqlParams = sql.getSqlParams();
		resourceName = sql.getResourceName();
	}
	
	/**
	 * sql脚本实际传入的参数集合
	 */
	private List<List<ComSqlScriptParameter>> actualParamsList;
	/**
	 * 资源的元数据信息集合
	 */
	protected List<ResourceMetadataInfo> resourceMetadataInfos;
	/**
	 * 请求资源的属性(字段、列)值编码规范
	 */
	private ResourcePropCodeRule resourcePropCodeRule;
	/**
	 * 是否是get请求
	 */
	private boolean isGetRequest;
	
	/**
	 * 要操作的sql对象
	 */
	private ComSqlScript sql;
	/**
	 * 针对procedure sql传入表对象的元数据信息集合
	 */
	private List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList;
	private List<ComSqlScriptParameter> sqlParams;
	private String resourceName;
	
	public ComSqlScript getSql() {
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
		
		List<List<ComSqlScriptParameter>> sqlParamsList = null;// set实际传入的参数值的集合
		List<ComSqlScriptParameter> sqlParams = null;
		String analysisActualInValueResult = null;
		
		int index = 1;
		// 如果没有传入任何参数，还能进入到这里，说明配置的参数要么是系统内置，要么是自动生成，要么是有默认值的
		if((actualParamsList == null || actualParamsList.size() == 0)){
			// 如果配置了sql参数，但是实际调用的时候没有传入任何参数
			for (ComSqlScriptParameter sqlParam : this.sqlParams) {
				if(sqlParam.getParameterFrom() == ComSqlScriptParameter.USER_INPUT && StrUtils.isEmpty(sqlParam.getDefaultValue())){
					return "在调用sql资源时，必须传入名为"+sqlParam.getParameterName()+"的参数值";
				}
			}
			
			sqlParamsList = new ArrayList<List<ComSqlScriptParameter>>(1);
			sqlParams = this.sqlParams;
			sqlParamsList.add(sqlParams);
			
			for (ComSqlScriptParameter ssp : sqlParams) {
				analysisActualInValueResult = analysisActualInValue(ssp, isGetRequest, null, index++);
				if(analysisActualInValueResult != null){
					return analysisActualInValueResult;
				}
			}
		}
		// 否则就去解析实际的参数，将其和配置的参数进行匹配
		else{
			sqlParamsList = new ArrayList<List<ComSqlScriptParameter>>(actualParamsList.size());
			
			for (List<ComSqlScriptParameter> actualParams : actualParamsList) {
				sqlParams = cloneSqlParams();
				sqlParamsList.add(sqlParams);
				
				for (ComSqlScriptParameter ssp : sqlParams) {
					for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
						if(ssp.getParameterName().equals(rmi.getPropName())){
							if(ssp.getParameterFrom() == ComSqlScriptParameter.SYSTEM_BUILTIN){// 参数值来源为系统内置
								analysisActualInValueResult = analysisActualInValue(ssp, isGetRequest, null, index);
							}else if(ssp.getParameterFrom() == ComSqlScriptParameter.USER_INPUT){// 参数值来源为用户输入
								if(actualParams != null && actualParams.size() > 0){
									for (ComSqlScriptParameter ssap : actualParams) {
										if(ssp.getParameterName().equals(ssap.getParameterName())){
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
				paramIndex = 0;
				inSqlResultSetMetadataInfoIndex = 0;
			}
			this.sqlParams.clear();
		}
		sql.setSqlParamsList(sqlParamsList);
		return null;
	}
	/** 用来自动生成编码的下标 */
	private int paramIndex;
	
	/**
	 * 克隆sql参数集合
	 * @param sqlScriptParameterList
	 * @return
	 */
	private List<ComSqlScriptParameter> cloneSqlParams() {
		List<ComSqlScriptParameter> sqlParams = new ArrayList<ComSqlScriptParameter>(this.sqlParams.size());
		try {
			for (ComSqlScriptParameter sqlParam : this.sqlParams) {
				sqlParams.add((ComSqlScriptParameter)sqlParam.clone());
			}
		} catch (CloneNotSupportedException e) {
			throw new IllegalArgumentException(ExceptionUtil.getErrMsg(e));
		}
		return sqlParams;
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
	private String analysisActualInValue(ComSqlScriptParameter ssp, boolean isGetRequest, ResourceMetadataInfo rmi, int index) {
		String desc = "操作sql资源时，";
		if(rmi != null && !ssp.getParameterDataType().equals(rmi.getDataType())){
			return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 参数，配置的数据类型("+ssp.getParameterDataType()+")和实际加载的数据类型("+rmi.getDataType()+")不一致，请联系后端系统开发人员";
		}
		
		if(ssp.getIsNullabled() == 1){
			return null;
		}
		if(ssp.getParameterFrom() == ComSqlScriptParameter.USER_INPUT){
			if(ssp.getActualInValue() == null){
				if(ssp.getDefaultValue() == null){
					return desc+"第"+index+"个对象，必须传入名为"+ssp.getParameterName()+"的参数值";
				}
				ssp.setActualInValue(ssp.getDefaultValue());
			}
			if(rmi != null){
				actualInValue = ssp.getActualInValue();
				if(actualInValue == null){
					return desc+"必须要传入的参数["+ssp.getParameterName()+"]，请修改调用方式，传入该参数值";
				}
				if(ssp.getIsPlaceholder() == 1){
					dataValueStr = actualInValue.toString();
					
					// 无论是什么类型的请求，日期类型都是string类型，都要进行转换
					if(DataTypeConstants.DATE.equals(ssp.getParameterDataType())){
						if(DataTypeValidUtil.isDate(actualInValue)){
							actualInValue = DateUtil.parseSqlTimestamp(dataValueStr);
						}else{
							return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为日期类型";
						}
					}else{
						if(isGetRequest){// get请求，值都是string类型，需要进行转换
							if(DataTypeConstants.INTEGER.equals(ssp.getParameterDataType())){
								if(DataTypeValidUtil.isInteger(dataValueStr)){
									actualInValue = Integer.valueOf(dataValueStr);
									if(rmi.getLength() != -1 && dataValueStr.length() > rmi.getLength()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
									}
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为整数类型";
								}
							}else if(DataTypeConstants.DOUBLE.equals(ssp.getParameterDataType())){
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
							}else if(DataTypeConstants.BOOLEAN.equals(ssp.getParameterDataType())){
								if(DataTypeValidUtil.isBoolean(dataValueStr)){
									actualInValue = ("true".equals(dataValueStr))? "1":"0";
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
								}
							}else if(DataTypeConstants.STRING.equals(ssp.getParameterDataType())){
								if(rmi.getLength() != -1 && StrUtils.calcStrLength(dataValueStr) > rmi.getLength()){
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
								}
							}else{
								return desc+"第"+index+"个对象，["+rmi.getDescName()+"]，系统目前不支持["+ssp.getParameterDataType()+"]数据类型，请联系后端开发人员";
							}
						}else{// 否则就是post请求，直接判断，不需要转换
							if(ssp.getIsTableType() == 1){
								if(inSqlResultSetMetadataInfoList == null || inSqlResultSetMetadataInfoList.size() == 0){
									return "["+ssp.getParameterName()+"] 参数关联的表类型，没有查询到对应的列的元数据信息集合，请联系后端开发人员";
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
		}else if(ssp.getParameterFrom() == ComSqlScriptParameter.SYSTEM_BUILTIN){
			actualInValue = BuiltinQueryParameters.getBuiltinQueryParamValue(ssp.getParameterName());
			if(actualInValue == null){
				return desc+"内置参数["+ssp.getParameterName()+"]的值为空，请联系后端系统开发人员";
			}
			if(ssp.getIsPlaceholder() == 0){
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else if(ssp.getParameterFrom() == ComSqlScriptParameter.AUTO_CODE){
			actualInValue = PropCodeRuleUtil.getFinalCodeVal(ssp.getParameterName(), paramIndex++, resourcePropCodeRule);
			if(actualInValue == null){
				return desc+"自动编码参数["+ssp.getParameterName()+"]的值为空，请联系后端系统开发人员";
			}
			if(ssp.getIsPlaceholder() == 0){
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else{
			return "parameterFrom的值，仅限于：[0(用户输入)、1(系统内置)、2(自动编码)]，请联系后端系统开发人员";
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
	private String validProcedureSqlInResultSet(String desc, ComSqlScriptParameter ssp, int index, IJson ijson) {
		List<ResourceMetadataInfo> inSqlResultSetMetadataInfos = inSqlResultSetMetadataInfoList.get(inSqlResultSetMetadataInfoIndex++);
		if(inSqlResultSetMetadataInfos == null || inSqlResultSetMetadataInfos.size() == 0){
			return desc+"第"+index+"个对象，["+ssp.getParameterName()+"] 参数关联的表类型，没有查询到对应的列的元数据信息集合，请联系后端开发人员";
		}
		if(ijson != null && ijson.size() > 0){
			return TableResourceValidUtil.validTableResourceMetadata(desc+"操作第"+index+"个对象，["+ssp.getParameterName()+"] 参数关联的表对象中，", null, inSqlResultSetMetadataInfos, ijson, false, false);
		}
		return null;
	}
	/** 如果调用存储过程，传入表参数类型，这里记录表参数元数据集合的下标，用来取对应的元数据集合来做数据验证 */
	private int inSqlResultSetMetadataInfoIndex = 0;
	
	/**
	 * 获取简单的sql参数值
	 * <p>目前就是对值加上''</p>
	 * @param ssp
	 * @param sqlParameterValue
	 * @return
	 */
	private String getSimpleSqlParameterValue(ComSqlScriptParameter ssp, Object sqlParameterValue){
		if(sqlParameterValue == null){
			Log4jUtil.warn(ComSqlScriptParameter.class, "getSimpleSqlParameterValue", "在获取简单的sql参数值时，传入的sqlParameterValue参数值为null【目前就是对值加上''】");
			return "''";
		}
		return ssp.getValuePackStart()+sqlParameterValue.toString()+ssp.getValuePackEnd();
	}
}
