package com.king.tooth.sys.entity.common.sqlscript;

import java.io.Serializable;
import java.sql.Types;

import oracle.jdbc.OracleTypes;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.plugins.builtin.params.BuiltinQueryParameters;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * sql脚本的参数对象
 * @see ComSqlScript使用到
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlScriptParameter implements Serializable{
	/**
	 * id
	 */
	private String id;
	/**
	 * 记录是第几个sql语句的参数
	 */
	private int index;
	/**
	 * 参数名称
	 */
	private String parameterName;
	/**
	 * 参数长度
	 */
	private int length;
	/**
	 * 参数数据类型
	 */
	private String parameterDataType;
	/**
	 * 默认值
	 */
	private Object defaultValue;
	/**
	 * 参数来源
	 * <p>0.用户输入、1.系统内置</p>
	 */
	private int parameterFrom;
	/**
	 * 是否是需要占位符的参数
	 * <p>即是否是需要用?代替的</p>
	 * <p>目前全部都是1</p>
	 */
	private int isPlaceholder = 1;
	/**
	 * 参数的in/out类型
	 * <p>in=1、out=2、inOut=3</p>
	 * <p>增删改查sql语句都是in类型</p>
	 */
	private int inOut;
	
	//------------------------------------------------------------------------------
	/**
	 * 关联的sql脚本id
	 */
	@JSONField(serialize = false)
	private String sqlScriptId;
	
	/**
	 * 实际在使用时，传递in的值
	 */
	@JSONField(serialize = false)
	private Object actualInValue;
	
	/**
	 * 实际在使用中，返回out的值
	 */
	@JSONField(serialize = false)
	private Object acutalOutValue;
	
	public SqlScriptParameter() {
	}
	public SqlScriptParameter(int index, String parameterName, String parameterDataType, int inOut) {
		this.id = ResourceHandlerUtil.getIdentity();
		this.index = index;
		this.parameterName = parameterName;
		if(BuiltinQueryParameters.isBuiltinQueryParams(parameterName)){
			this.parameterFrom = 1;
		}
		
		if(parameterDataType == null){
			this.parameterDataType = DataTypeConstants.STRING;
		}
		this.parameterDataType = parameterDataType;
		
		if(inOut == 0 || inOut == 1){// in
			this.inOut = 1;
		}else if(inOut == 2 || inOut == 4){// out
			this.inOut = 2;
		}else{// in out
			this.inOut = 3;
		}
	}
	
	/**
	 * 获取存储过程参数对应的数据类型
	 * <p>在调用存储过程时使用到该方法：将数据库类型，转换为对应的代码类型</p>
	 * @param dbType
	 * @return
	 */
	public int getProcedureParamsMappingDataTypes(String dbType){
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)){
			if("varchar2".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.VARCHAR;
			}else if("char".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.CHAR;
			}else if("number".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.NUMBER;
			}else if("date".equalsIgnoreCase(parameterDataType)){
				return OracleTypes.DATE;
			}
			throw new IllegalArgumentException("系统目前不支持[oracle]数据库的["+parameterDataType+"]数据类型转换，请联系管理员，目前支持的数据类型为：[varchar2、char、number、date]");
		}else if(DynamicDataConstants.DB_TYPE_SQLSERVER.equals(dbType)){
			if("varchar".equalsIgnoreCase(parameterDataType)){
				return Types.VARCHAR;
			}else if("char".equalsIgnoreCase(parameterDataType)){
				return Types.CHAR;
			}else if("int".equalsIgnoreCase(parameterDataType)){
				return Types.INTEGER;
			}else if("decimal".equalsIgnoreCase(parameterDataType)){
				return Types.DECIMAL;
			}else if("datetime".equalsIgnoreCase(parameterDataType)){
				return Types.DATE;
			}
			throw new IllegalArgumentException("系统目前不支持[sqlserver]数据库的["+parameterDataType+"]数据类型转换，请联系管理员");
		}
		throw new IllegalArgumentException("系统目前不支持["+dbType+"]数据库的数据类型转换，请联系管理员");
	}
	
	/**
	 * 解析实际传入的参数值
	 * @return
	 */
	public Object analysisActualInValue() {
		if(parameterFrom == 0){
			if(actualInValue == null){
				actualInValue = defaultValue;
			}
			if(actualInValue == null){
				throw new NullPointerException("调用sql脚本时，参数["+parameterName+"]的值不能为空");
			}
			if(isPlaceholder == 1){
				if(DataTypeConstants.INTEGER.equals(parameterDataType)){
					return Integer.valueOf(actualInValue+"");
				}else if(DataTypeConstants.DOUBLE.equals(parameterDataType)){
					return Double.valueOf(actualInValue+"");
				}else if(DataTypeConstants.DATE.equals(parameterDataType)){
					return DateUtil.parseDate(actualInValue+"");
				}else{
					return actualInValue+"";
				}
			}else{
				return "'"+actualInValue+"'";
			}
		}else if(parameterFrom == 1){
			actualInValue = BuiltinQueryParameters.getBuiltinQueryParamValue(parameterName);
			if(actualInValue == null){
				throw new NullPointerException("调用sql脚本时，内置参数["+parameterName+"]的值为空，请检查配置，或程序代码");
			}
			return actualInValue;
		}else{
			throw new IllegalArgumentException("parameterFrom的值，仅限于：[0(用户输入)、1(系统内置)]");
		}
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterDataType() {
		return parameterDataType;
	}
	public void setParameterDataType(String parameterDataType) {
		this.parameterDataType = parameterDataType;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public int getIsPlaceholder() {
		return isPlaceholder;
	}
	public void setIsPlaceholder(int isPlaceholder) {
		this.isPlaceholder = isPlaceholder;
	}
	public void setParameterFrom(int parameterFrom) {
		this.parameterFrom = parameterFrom;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Integer getParameterFrom() {
		return parameterFrom;
	}
	public void setParameterFrom(Integer parameterFrom) {
		this.parameterFrom = parameterFrom;
	}
	public int getInOut() {
		return inOut;
	}
	public void setInOut(int inOut) {
		this.inOut = inOut;
	}
	public Object getActualInValue() {
		return actualInValue;
	}
	public void setActualInValue(Object actualInValue) {
		this.actualInValue = actualInValue;
	}
	public Object getAcutalOutValue() {
		return acutalOutValue;
	}
	public void setAcutalOutValue(Object acutalOutValue) {
		this.acutalOutValue = acutalOutValue;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSqlScriptId() {
		return sqlScriptId;
	}
	public void setSqlScriptId(String sqlScriptId) {
		this.sqlScriptId = sqlScriptId;
	}
}
