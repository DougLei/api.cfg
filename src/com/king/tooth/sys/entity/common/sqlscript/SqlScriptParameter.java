package com.king.tooth.sys.entity.common.sqlscript;

import java.io.Serializable;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.sqlparser.SqlStatementParserUtil;

/**
 * sql脚本的参数对象
 * @see ComSqlScript使用到
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlScriptParameter implements Serializable{
	/**
	 * 记录是第几个sql语句的参数
	 */
	private int index;
	/**
	 * 参数名称
	 */
	private String parameterName;
	/**
	 * 参数数据类型
	 */
	private String parameterDataType;
	/**
	 * 默认值
	 */
	private Object defaultValue;
	/**
	 * 实际在使用时，传递的值
	 */
	private Object actualValue;
	/**
	 * 是否是需要占位符的参数
	 * 即是否是需要用?代替的
	 */
	private boolean isPlaceholderParameter;
	/**
	 * 是否被处理过
	 * <p>该属性，属于性能优化属性</p>
	 * <p>在第一次调用sql时，会加载对应的参数对象集合，会去判断哪些参数属于条件子句，修改其isWhereClauseParameter的值。最后处理完成后，将每个参数对象的isProcessed改为true，并保存到数据库</p>
	 * <p>二次调用sql时，根据之前的结果，不需要解析sql，就可以直接进行替换，得到最终的sql语句和queryCondParameters集合</p>
	 * @see SqlStatementParserUtil.selectSqlToInvoke
	 */
	private boolean isProcessed;
	/**
	 * 是否被更新
	 * <p>该属性，属于性能优化属性</p>
	 * <p>在isProcessed被改为true后，并将该属性的值也改为true，最后将新的数据更新到数据库</p>
	 * <p>下次在处理到这块时，因为isUpdated也为true，则不再做更新操作，除非用户去修改了sql配置</p>
	 */
	private boolean isUpdated;
	
	public SqlScriptParameter(int index, String parameterName) {
		this.index = index;
		this.parameterName = parameterName;
	}
	public SqlScriptParameter() {
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getParameterDataType() {
		if(StrUtils.isEmpty(parameterDataType)){
			parameterDataType = DataTypeConstants.STRING;
		}
		return parameterDataType;
	}
	public void setParameterDataType(String parameterDataType) {
		if(StrUtils.isEmpty(parameterDataType)){
			parameterDataType = DataTypeConstants.STRING;
		}
		this.parameterDataType = parameterDataType;
	}
	public Object getDefaultValue() {
		if(StrUtils.isEmpty(defaultValue)){
			defaultValue = parameterName;
		}
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public Object getActualValue() {
		if(StrUtils.isEmpty(actualValue)){
			actualValue = getDefaultValue();
		}
		if(isPlaceholderParameter){
			if(DataTypeConstants.DATE.equals(getParameterDataType())){
				actualValue = DateUtil.parseDate(actualValue+"");
			}
			return actualValue;
		}else{
			return "'"+actualValue+"'";
		}
	}
	public void setActualValue(Object actualValue) {
		this.actualValue = actualValue;
	}
	public boolean getIsProcessed() {
		return isProcessed;
	}
	public boolean getIsUpdated() {
		return isUpdated;
	}
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	public boolean getIsPlaceholderParameter() {
		return isPlaceholderParameter;
	}
	public void setIsPlaceholderParameter(boolean isPlaceholderParameter) {
		this.isPlaceholderParameter = isPlaceholderParameter;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
