package com.king.tooth.sys.entity.common.sqlscript;

import java.io.Serializable;
import java.sql.Types;

import oracle.jdbc.OracleTypes;

import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.util.DateUtil;

/**
 * 存储过程sql脚本的参数对象
 * @see ComSqlScript使用到
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProcedureSqlScriptParameter implements Serializable{
	/**
	 * 标识调用的存储过程，第几个参数，下标从1开始
	 */
	private int index;
	/**
	 * 数据库类型
	 */
	private String dbType;
	/**
	 * 参数名
	 */
	private String parameterName;
	/**
	 * 参数的数据类型
	 */
	private String parameterDataType;
	/**
	 * 参数的in/out类型
	 */
	private int inOut;
	/**
	 * 实际在使用时，传递的值
	 */
	private Object actualValue;
	/**
	 * 实际在使用时，out的值
	 */
	private Object outValue;
	
	
	public ProcedureSqlScriptParameter() {
	}
	public ProcedureSqlScriptParameter(String dbType, int index, String parameterName, String parameterDataType, int inOut) {
		this.dbType = dbType;
		this.index = index;
		this.parameterName = parameterName;
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
	 * 获取对应的数据库类型
	 * @return
	 */
	public int getTypes(){
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
	public Object getActualValue() {
		if("date".equalsIgnoreCase(parameterDataType) 
				|| "datetime".equalsIgnoreCase(parameterDataType)){
			actualValue = DateUtil.parseDate(actualValue+"");
		}
		return actualValue;
	}
	public int getInOut() {
		return inOut;
	}
	public void setInOut(int inOut) {
		this.inOut = inOut;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getDbType() {
		return dbType;
	}
	public Object getOutValue() {
		return outValue;
	}
	public void setOutValue(Object outValue) {
		this.outValue = outValue;
	}
	public void setActualValue(Object actualValue) {
		this.actualValue = actualValue;
	}
	public int getIndex() {
		return index;
	}
}
