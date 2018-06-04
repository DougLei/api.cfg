package com.king.tooth.web.builtin.method.common.util.querycondfunc.entity;

import java.util.Arrays;
import java.util.regex.Matcher;


/**
 * 查询函数参数抽象类
 * @author DougLei
 */
public abstract class AbstractQueryCondFuncEntity implements IQueryCondFuncEntity{
	
	/**
	 * 通用的正则表达匹配器
	 */
	protected Matcher commonMatcher;
	
	/**
	 * 内置方法名
	 */
	protected String methodName;
	
	/**
	 * 要查询的属性值数组
	 */
	protected Object[] values;
	/**
	 * 是否取反
	 */
	protected boolean isInversion;
	
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
