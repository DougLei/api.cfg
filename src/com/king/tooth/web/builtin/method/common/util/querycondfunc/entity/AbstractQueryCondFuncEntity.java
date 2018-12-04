package com.king.tooth.web.builtin.method.common.util.querycondfunc.entity;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.StrUtils;
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
		Object[] values = null;
		
		if(DataTypeConstants.DATE.equals(dataType)){
			// 如果是日期类型，都用区间查询的方式，所以长度只能为2
			values = new Object[2];
			String date;
			if(valueList.size() == 1){
				// 如果日期的值数量是1，则区间值为：btn(传入的日期值，传入的日期值+1天)
				date = DateUtil.addAndSubtractDay(valueList.get(0).toString(), 1);
			}else if(valueList.size() == 2){
				// 如果日期的值数量是2，则区间值为：btn(传入的第一个日期值，传入的第二个日期值+1天)
				date = DateUtil.addAndSubtractDay(valueList.remove(1).toString(), 1);
			}else{
				throw new IllegalArgumentException("查询条件为日期时，值得长度目前只支持1个或2个，请联系后端系统开发人员");
			}
			valueList.add(date);
		}else{
			values = new Object[valueList.size()];
		}
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
			// 没有匹配到方法名，证明使用的是 propName=value 的默认规则
			// 如果是日期类型，则也用btn
			if(DataTypeConstants.DATE.equals(dataType)){
				this.methodName = "btn";
			}else{
				// 否则就用eq
				this.methodName = "eq";
			}
		}
	}
	
	/**
	 * 处理一些特殊的内容
	 * 有什么其他，可以自行向里面添加
	 */
	protected void processSpecialThings() {
		/* 
		   1.如果propName为_ids，则必须把propName改为ResourcePropNameConstants.ID
		   key=_ids是客户端请求传递过来的，属于平台内置处理的功能
		
		   2.如果propName为_resource_id，则必须把propName改为ResourcePropNameConstants.ID
		*/
		if(this.propName.equals(BuiltinParameterKeys._IDS) || this.propName.equals(BuiltinParameterKeys.RESOURCE_ID)){
			this.propName = ResourcePropNameConstants.ID;
		}
		
		// 3.如果ne方法，有多个值，则改为调用!in的方法，这个可以提高效率
		if(this.methodName.equals("ne") && this.values.length > 1){
			this.methodName = "in";
			this.isInversion = true;
		}
		
		// 4.如果ne方法，且是!，则改为eq方法
		if(this.methodName.equals("ne") && isInversion){
			this.methodName = "eq";
			isInversion = false;
		}
		
		// 5.如果eq方法，有多个值，则改为调用in的方法，这个可以提高效率
		if(this.methodName.equals("eq") && this.values.length > 1){
			this.methodName = "in";
		}
		
		// 6.如果eq方法，且是!，则改为ne方法
		if(this.methodName.equals("eq") && isInversion){
			this.methodName = "ne";
			isInversion = false;
		}
		
		// 7.如果in方法，但只有一个值，则改为调用eq/ne的方法，这个可以提高效率
		if(this.methodName.equals("in") && this.values.length == 1){
			if(isInversion){ // 如果取反
				this.methodName = "ne";
			}else{
				this.methodName = "eq";
			}
		}
		
		// 8.如果是btn方法，但是传入的值，有一个是null，例如btn(,2)、btn(4,)，则对应的改用le(2)、ge(4)
		if((this.methodName.equals("btn") || this.methodName.equals("between"))){
			if(StrUtils.isEmpty(this.values[0])){
				this.methodName = "le";
				this.values = new Object[]{this.values[1]};
			}else if(StrUtils.isEmpty(this.values[1])){
				this.methodName = "ge";
				this.values = new Object[]{this.values[0]};
			}
		}
	}	
	
	public String toString(){
		return "【propName】：\t" + getPropName() + "\n" + 
			   "【methodName】：\t" + this.methodName + "\n" + 
			   "【isInversion】：\t" + this.isInversion + "\n" + 
			   "【values】：\t" + Arrays.toString(this.values);
	}
}
