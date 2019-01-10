package com.api.web.builtin.method.tableresource.query;

import java.util.List;
import java.util.Map;

import com.api.util.Log4jUtil;
import com.api.util.StrUtils;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.common.util.resulttype.IResultType;
import com.api.web.builtin.method.common.util.resulttype.ResultTypeUtil;
import com.api.web.builtin.method.tableresource.AbstractTableResourceBuiltinMethodProcesser;

/**
 * 内置查询函数处理器
 * <p>select ... </p>
 * @author StoneKing
 */
public class BuiltinQueryMethodProcesser extends AbstractTableResourceBuiltinMethodProcesser{
	
	/**
	 * 查询的属性
	 * <p>例如：Name as 姓名, Age...</p>
	 */
	private String select;
	
	/**
	 * 字段分隔符
	 */
	private String split;
	
	/**
	 * _select的属性(列名)数组
	 */
	private String[] propArr;
	
	/**
	 * _select的属性(列名)数组拷贝
	 * <p>propArr中的数据，可以包含 "as xxx" 的别名内容，propArrCopy中只保留_select中查询的字段属性</p>
	 */
	private String[] propArrCopyOnlyPropName;
	
	/**
	 * _resultType参数值
	 */
	private String resultType;
	
	/**
	 * _resultType处理类的实例
	 */
	private IResultType iResultType;
	
	/**
	 * 是否需要二次处理获取的数据集合
	 * <p>例如，Text类型，需要将查询到的数据结果集合，组装成一个大的字符串  @see TextResultType</p>
	 */
	private boolean needProcessDataCollection;
	
	/**
	 * 解析查询的返回结果
	 * <p>根据用户输入的类型值，处理select后的属性(列名)拼接，并使用_split的拼接符。</p>
	 * <p>若类型为_resultType=Text，则记录下来，在程序执行到最后，获取到所有查询结果后，再将结果的对象集合，通过\n，拼接成一整条字符串</p>
	 * <p></p>
	 * <p>1.KeyValues: 键值对列表</p>
	 * <p>2.Strings：字符串列表</p>
	 * <p>3.Text: 文本</p>
	 * <p>4.（匿名类型），由 _select 指定的一个或多个属性组成的匿名类型</p>
	 * <p></p>
	 */
	private void analysisResultType() {
		this.iResultType = ResultTypeUtil.getResultTypeInstance(this.resultType);
		this.needProcessDataCollection = iResultType.toHql(propArr, propArrCopyOnlyPropName, split, hql);
	}

	/**
	 * 解析_select参数指定的属性名(列名)，例如：Name,Age
	 * <p>属性名(列名)可能会有别名，例如： Name as 姓名,Age as 年龄</p>
	 * @param props
	 */
	private void analysisSelect() {
		if(StrUtils.notEmpty(this.select)){
			propArr = this.select.split(",");
			propArrCopyOnlyPropName = new String[propArr.length];
			
			int len = propArr.length;
			int asIndex = -1;// 记录as的下标
			String queryPropName = null;// 记录_select参数中指定的属性名称
			for (int i = 0; i < len ; i++) {
				asIndex = propArr[i].toLowerCase().indexOf(" as ");
				if(asIndex != -1){
					queryPropName = propArr[i].substring(0, asIndex).trim();
				}else{
					queryPropName = propArr[i].trim();
					propArr[i] = queryPropName + " as " + queryPropName;
				}
				propArrCopyOnlyPropName[i] = queryPropName ;
			}
		}
	}
	
	public BuiltinQueryMethodProcesser(String resultType, String select, String split) {
		super.isUsed = true;
		this.hql.append("select ");
		this.resultType = resultType;
		this.select = select;
		this.split = split;
	}
	public BuiltinQueryMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinQueryMethodProcesser内置方法处理器");
	}

	/**
	 * 是否需要二次处理获取的数据集合
	 * @return
	 */
	public boolean getIsNeedProcessDataCollection() {
		return needProcessDataCollection;
	}
	
	/**
	 * 二次处理查询的数据结果集合
	 * <p>当needProcessDataCollection属性的值为true时</p>
	 * @param dataList
	 * @return
	 */
	public List<Map<String, Object>> doProcessDataCollection(List<Map<String, Object>> dataList){
		return iResultType.doProcessDataCollection(dataList, this.split);
	}

	protected void execAnalysisParam() {
		analysisSelect();
		analysisResultType();
		Log4jUtil.debug("[BuiltinQueryMethodProcesser.execAnalysisParam]解析出来，要执行的查询hql语句为：{}", hql.toString());
		Log4jUtil.debug("[BuiltinQueryMethodProcesser.execAnalysisParam]解析完成后，后续是否还要处理最后的数据集合：{}", needProcessDataCollection);
	}
	
	public StringBuilder getHql() {
		execAnalysisParams();
		return hql;
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.QUERY;
	}
	
	public void clearInvalidMemory() {
	}
}
