package com.king.tooth.web.builtin.method.sqlresource.sqlscript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.sqlresource.AbstractSqlResourceBuiltinMethodProcesser;

/**
 * 内置sql脚本处理器
 * @author DougLei
 */
public class BuiltinSqlScriptMethodProcesser extends AbstractSqlResourceBuiltinMethodProcesser{

	/**
	 * sql脚本的参数集合
	 * [解析后，该属性就清空]
	 */
	private Map<String, String> sqlScriptParams;
	
	/**
	 * 请求体
	 */
	private IJson formData;
	
	/**
	 * sql脚本资源
	 */
	private ComSqlScript sqlScriptResource;

	public BuiltinSqlScriptMethodProcesser(ComSqlScript reqSqlScriptResource, Map<String, String> sqlScriptParams, IJson formData) {
		super.isUsed = true;
		this.sqlScriptResource = reqSqlScriptResource;
		this.sqlScriptParams = sqlScriptParams;
		this.formData = formData;
	}

	public BuiltinSqlScriptMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinSqlScriptMethodProcesser内置方法处理器");
	}
	
	/**
	 * 根据实际值的map集合，创建对应的sql脚本参数集合
	 * @return
	 */
	private List<ComSqlScriptParameter> getActualParameters() {
		List<ComSqlScriptParameter> sqlScriptActualParameters = null;
		ComSqlScriptParameter ssp = null;
		
		// 请求体为空，那么是从url传参，则是get请求select sql资源
		if((formData == null || formData.size() == 0) && sqlScriptParams != null && sqlScriptParams.size() > 0){
			// 解析sql脚本的参数
			sqlScriptActualParameters = new ArrayList<ComSqlScriptParameter>(sqlScriptParams.size());
			
			Set<String> parameterNames = sqlScriptParams.keySet();
			for (String parameterName : parameterNames) {
				ssp = new ComSqlScriptParameter(parameterName, null, 0, -1, false);
				ssp.setActualInValue(processActualValue(sqlScriptParams.get(parameterName).trim()));
				sqlScriptActualParameters.add(ssp);
			}
			
			sqlScriptParams.clear();
		}
		// 否则就是通过请求体传参，则是post/put/delete insert/update/delete 等sql资源
		else{
			JSONObject json = formData.get(0);
			if(json != null && json.size()>0){
				sqlScriptActualParameters = new ArrayList<ComSqlScriptParameter>(json.size());
				
				Set<String> parameterNames = json.keySet();
				for (String parameterName : parameterNames) {
					ssp = new ComSqlScriptParameter(parameterName, null, 0, -1, false);
					ssp.setActualInValue(json.getString(parameterName).trim());
					sqlScriptActualParameters.add(ssp);
				}
			}
		}
		return sqlScriptActualParameters;
	}
	
	/**
	 * 处理每个值最外层的单引号或双引号
	 * <p>主要针对url参数的处理</p>
	 * @param actualValue
	 * @return
	 */
	private String processActualValue(String actualValue){
		if(actualValue.startsWith("'") || actualValue.startsWith("\"")){
			actualValue = actualValue.substring(1, actualValue.length()-1);
		}
		return actualValue;
	}
	
	
	protected void execAnalysisParam() {
		// 获取从调用方传过来的脚本参数对象，通过url传入的，现在默认是第一个sql语句的参数，即sqlIndex=1
		// 现在考虑是能通过url传值的，应该都是get请求，调用的select sql资源
		List<ComSqlScriptParameter> sqlScriptActualParameters = getActualParameters();
		// 获取sql脚本资源对象
		sqlScriptResource.setActualParams(sqlScriptActualParameters);
		sqlScriptResource.analysisFinalSqlScript(sqlScriptResource, sqlParameterValues);
		
		if(sqlScriptActualParameters != null && sqlScriptActualParameters.size() > 0){
			sqlScriptActualParameters.clear();
		}
	}

	/**
	 * 获取sql脚本资源
	 * @return
	 */
	public ComSqlScript getSqlScriptResource() {
		execAnalysisParams();
		return sqlScriptResource;
	}

	public int getProcesserType() {
		return BuiltinMethodProcesserType.SQL_SCRIPT;
	}

	public StringBuilder getSql() {
		execAnalysisParams();
		return sql;
	}
	
	public void clearInvalidMemory() {
		if(sqlScriptParams != null && sqlScriptParams.size() > 0){
			sqlScriptParams.clear();
		}
		if(sqlScriptResource != null){
			if(sqlScriptResource.getSqlQueryResultColumnList() != null && sqlScriptResource.getSqlQueryResultColumnList().size() > 0){
				sqlScriptResource.getSqlQueryResultColumnList().clear();
			}
			if(sqlScriptResource.getSqlScriptParameterList() != null && sqlScriptResource.getSqlScriptParameterList().size() > 0){
				sqlScriptResource.getSqlScriptParameterList().clear();
			}
			if(sqlScriptResource.getParameterNameRecordList() != null && sqlScriptResource.getParameterNameRecordList().size() > 0){
				sqlScriptResource.getParameterNameRecordList().clear();
			}
		}
	}
}
