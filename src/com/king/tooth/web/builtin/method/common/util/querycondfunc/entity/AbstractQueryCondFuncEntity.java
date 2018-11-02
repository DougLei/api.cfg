package com.king.tooth.web.builtin.method.common.util.querycondfunc.entity;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.king.tooth.util.datatype.DataTypeTurnUtil;


/**
 * 查询函数参数抽象类
 * @author DougLei
 */
public abstract class AbstractQueryCondFuncEntity implements IQueryCondFuncEntity{
	
	/**
	 * 正则表达式获取值
	 * <p>ne(xxxx)的时候，获取xxxx</p>
	 * <p>xxxx的时候，直接获取xxxx</p>
	 */
	protected static final Pattern VALUES_PATTERN = Pattern.compile("(?<=\\().*(?=\\))");
	/**
	 * 正则表达式获取注释
	 */
	protected static final Pattern COMMENTS_PATTERN = Pattern.compile("(/\\*([^*]|[\r\n]|(\\*+([^*/]|[\r\n])))*\\*+/)|(//.*)");
	/**
	 * 正则表达式获取方法名
	 */
	protected static final Pattern METHOD_PATTERN = Pattern.compile("(?<=!?)\\w+(?=\\()");
	
	/**
	 * 通用的正则表达匹配器
	 */
	protected Matcher commonMatcher;
	
	/**
	 * 内置方法名
	 */
	protected String methodName;
	
	/**
	 * 要查询的属性
	 */
	protected String propName;
	
	/**
	 * 属性对应的数据类型
	 */
	protected String dataType;
	
	/**
	 * 要查询的属性值数组
	 */
	protected Object[] values;
	/**
	 * 是否取反
	 */
	protected boolean isInversion;
	
	public String getPropName() {
		return propName;
	}
	public String getMethodName() {
		return methodName;
	}
	public Object[] getValues() {
		return values;
	}
	public boolean isInversion() {
		return isInversion;
	}
	
	/**
	 * 移除注释
	 * @param value
	 * @return
	 */
	protected String removeComments(String value) {
		commonMatcher = COMMENTS_PATTERN.matcher(value);
		if(commonMatcher.find()){
			return commonMatcher.replaceAll("");
		}
		return value;
	}
	
	/**
	 * 根据数据类型，对实际值进行类型转换等操作，最终获取的值数组
	 * @param valueList
	 * @param isTableResource 
	 * @return
	 */
	protected Object[] processValuesByDataType(List<Object> valueList, boolean isTableResource) {
		int index = 0;
		Object[] values = new Object[valueList.size()];
		for (Object vl : valueList) {
			values[index++] = DataTypeTurnUtil.turnValueDataType(vl, dataType, true, isTableResource, false);
		}
		valueList.clear();
		return values;
	}
	
	/**
	 * 解析出方法名
	 * @param value
	 */
	protected void setMethodName(String value) {
		commonMatcher = METHOD_PATTERN.matcher(value);
		if(commonMatcher.find()){
			this.methodName = commonMatcher.group().toLowerCase();
		}else{
			// 没有匹配到方法名，证明使用的是 propName=value 的默认规则，则方法名为eq
			this.methodName = "eq";
		}
	}
	
	public String toString(){
		return "【propName】：\t" + getPropName() + "\n" + 
			   "【methodName】：\t" + this.methodName + "\n" + 
			   "【isInversion】：\t" + this.isInversion + "\n" + 
			   "【values】：\t" + Arrays.toString(this.values);
	}
}
