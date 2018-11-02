package com.king.tooth.web.builtin.method.common.util.querycondfunc.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinQueryParameters;

/**
 * hql查询函数参数实体类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class HqlQueryCondFuncEntity extends AbstractQueryCondFuncEntity implements Serializable{
	
	public HqlQueryCondFuncEntity(String propName, String value, String dataType) {
		this.propName = propName;
		this.dataType = dataType;
		analysisQueryParams(value);
	}
	
	/**
	 * 解析请求查询参数的方法名、值数组、是否取反
	 * @param value 
	 */
	private void analysisQueryParams(String value) {
		value = removeComments(value);// 移除value中的注释
		this.isInversion = value.startsWith("!");// 判断是否取反
		setValues(value);// 解析出值数组
		setMethodName(value);// 解析出方法名
		processSpecialThings();// 处理一些特殊的内容
	}
	
	/**
	 * 解析出值数组
	 * @param value
	 * @return
	 */
	private void setValues(String value) {
		commonMatcher = VALUES_PATTERN.matcher(value);
		Object[] tmp = null;
		if(commonMatcher.find()){
			// 匹配到了，证明是propName=eq(xxx,xxx)等内置函数，提取出函数中的值，再用,分割
			tmp = commonMatcher.group().split(",");
		}else{
			// 没有匹配，证明使用的是propName=value 的默认规则，直接用,分割
			tmp = value.split(",");
		}
		
		int len = tmp.length;
		List<Object> result = new ArrayList<Object>(len);
		
		// 处理每个值最外层的单引号或双引号
		String tmpVal = null;
		Object builtinQuerParamValue;
		String[] bqpvTmp;
		for (int i = 0; i < len; i++) {
			tmpVal = (tmp[i]+"").trim();
			if(tmpVal.startsWith("'") || tmpVal.startsWith("\"")){
				tmpVal = tmpVal.substring(1, tmpVal.length()-1);
			}
			
			if(BuiltinQueryParameters.isBuiltinQueryParams(tmpVal)){
				builtinQuerParamValue = BuiltinQueryParameters.getBuiltinQueryParamValue(tmpVal);
				if(builtinQuerParamValue instanceof String){
					bqpvTmp = ((String)builtinQuerParamValue).split(",");
					for (String bt : bqpvTmp) {
						result.add(bt);
					}
				}else{
					result.add(builtinQuerParamValue);
				}
			}else{
				result.add(tmpVal);
			}
		}
		this.values = processValuesByDataType(result, true);
		result.clear();
	}

	/**
	 * 处理一些特殊的内容
	 * 有什么其他，可以自行向里面添加
	 */
	private void processSpecialThings() {
		/* 
		   1.如果propName为_ids，则必须把propName改为SystemConstants.ID
		   key=_ids是客户端请求传递过来的，属于平台内置处理的功能
		
		   2.如果propName为_resourceid，则必须把propName改为SystemConstants.ID
		      这个key值来自      @see PlatformServlet.processSpecialData()
		*/
		if(this.propName.equals(BuiltinParameterKeys._IDS) || this.propName.equals(BuiltinParameterKeys.RESOURCE_ID)){
			this.propName = ResourcePropNameConstants.ID;
		}
		
		// 3.如果ne方法，有多个值，则改为调用!in的方法，这个可以提高效率
		if(this.methodName.equals("ne") && this.values.length > 1){
			this.methodName = "in";
			this.isInversion = true;
		}
		
		// 4.如果eq方法，有多个值，则改为调用in的方法，这个可以提高效率
		if(this.methodName.equals("eq") && this.values.length > 1){
			this.methodName = "in";
		}
		
		// 5.如果in方法，但只有一个值，则改为调用eq/ne的方法，这个可以提高效率
		if(this.methodName.equals("in") && this.values.length == 1){
			if(isInversion){ // 如果取反
				this.methodName = "ne";
			}else{
				this.methodName = "eq";
			}
		}
	}	
}
