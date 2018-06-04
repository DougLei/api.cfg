package com.king.tooth.web.builtin.method.common.util.querycondfunc;

import java.util.List;

import com.king.tooth.util.StrUtils;

/**
 * 内置的查询条件函数抽象类
 * @author DougLei
 */
public abstract class AbstractBuiltinQueryCondFunc{
	
	/**
	 * 记录请求的值中，是否有"null"，如果有，则要增加is null/is not null的查询条件
	 */
	protected boolean valueIsNullStr = false;
	
	/**
	 * 是否取反
	 */
	protected boolean isInversion = false;
	/**
	 * not 操作符
	 * <p>值为" not "，或为空字符串；默认为空字符串</p>
	 * <p>和isInversion属性相对应：</p>
	 * <p>isInversion = true，notOperator = "not"</p>
	 * <p>isInversion = false，notOperator = ""</p>
	 */
	protected String notOperator = "";
	
	/**
	 * set是否取反
	 * @param isInversion
	 */
	private void setIsInversion(boolean isInversion){
		this.isInversion = isInversion;
		if(isInversion){
			notOperator = " not ";
		}
	}
	
	/**
	 * 根据属性名，以及属性值，拼装hql语句
	 * <p>由子类实现</p>
	 * @param propName 属性名
	 * @param values 值的数组
	 * @return
	 */
	protected abstract String toDBScriptStatement(String propName, Object[] values);
	
	/**
	 * 获取内置方法的名称
	 * @return
	 */
	protected abstract String getMethodName();
	
	/**
	 * 预处理请求的值
	 * @param values
	 * @param parameterValues
	 */
	private void preProcessValues(Object[] values, List<Object> parameterValues) {
		boolean isLikeQuery = "like".equals(getMethodName());
		
		for (Object val : values) {
			if(StrUtils.isNullStr((val+""))){// 如果查询的值是null，则查询条件为(is null/is not null)
				valueIsNullStr = true;
				continue;
			}
			
			if(isLikeQuery){
				parameterValues.add("%"+val+"%");
			}else{
				parameterValues.add(val);
			}
		}
	}
	
	/**
	 * 根据参数，转换成数据库脚本语句
	 * <p>对外处理的统一接口</p>
	 * @param funcName 方法名，方法名第一个字符是!的，标识取反
	 * @param propName 属性名
	 * @param values 值数组
	 * @param parameterValues 参数集合，将值数组的数据按顺序存储进来，后续通过hibernate的query.setParameter调用.即hql语句中?对应的值.这里用引用传递，减少内存开销
	 * @param alias 别名
	 * @return
	 */
	String toDBScriptStatement(boolean isInversion, String propName, Object[] values, List<Object> parameterValues, String alias){
		setIsInversion(isInversion);
		preProcessValues(values, parameterValues);
		propName = alias + propName;// 加上别名
		return toDBScriptStatement(propName, values);
	}
}
