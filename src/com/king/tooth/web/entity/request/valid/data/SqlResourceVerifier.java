package com.king.tooth.web.entity.request.valid.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinQueryParameters;
import com.king.tooth.sys.entity.ResourceMetadataInfo;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.request.RequestBody;

/**
 * sql资源的数据校验类
 * @author DougLei
 */
public class SqlResourceVerifier extends AbstractResourceVerifier{

	private ComSqlScript sql;
	private List<ComSqlScriptParameter> sqlParams;
	
	/**
	 * sql脚本实际传入的参数集合
	 */
	private List<List<ComSqlScriptParameter>> actualParamsList;
	/**
	 * 针对select sql查询结果集的元数据信息集合
	 */
	private List<ResourceMetadataInfo> outSqlResultSetMetadataInfos;
	/**
	 * 针对procedure sql传入表对象的元数据信息集合
	 */
	private List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList;
	
	public SqlResourceVerifier(RequestBody requestBody, String resourceName, String parentResourceName) {
		super(requestBody, resourceName, parentResourceName);
		sql = requestBody.getResourceInfo().getSqlScriptResource();
		sqlParams = sql.getSqlParams();
	}
	
	public void clear(){
		super.clear();
		if(inSqlResultSetMetadataInfoList != null && inSqlResultSetMetadataInfoList.size() > 0){
			for (List<ResourceMetadataInfo> inSqlResultSetMetadataInfos : inSqlResultSetMetadataInfoList) {
				if(inSqlResultSetMetadataInfos != null && inSqlResultSetMetadataInfos.size() > 0){
					inSqlResultSetMetadataInfos.clear();
				}
			}
			inSqlResultSetMetadataInfoList.clear();
		}
		if(outSqlResultSetMetadataInfos != null && outSqlResultSetMetadataInfos.size() > 0){
			outSqlResultSetMetadataInfos.clear();
		}
		if(actualParamsList != null && actualParamsList.size() > 0){
			for (List<ComSqlScriptParameter> actualParams : actualParamsList) {
				actualParams.clear();
			}
			actualParamsList.clear();
		}
	}
	
	public String doValid(){
		return doValidSqlResourceMetadata();
	}

	/**
	 * 验证sql资源的元数据
	 * @return
	 */
	private String doValidSqlResourceMetadata() {
		initSqlResourceMetadataInfos();
		initActualParamsList();
		String validResult = validAndSetActualParams();
		if(validResult != null){
			return validResult;
		}
		
		if(requestBody.isGetRequest()){
			return validGetSqlResourceMetadata();
		}else if(requestBody.isPostRequest() || requestBody.isPutRequest() || requestBody.isDeleteRequest()){
			return validPostSqlResourceMetadata();
		}
		return "系统只支持[get、post、put、delete]四种请求方式";
	}
	
	/**
	 * 初始化sql资源元数据信息集合
	 * @return
	 */
	private void initSqlResourceMetadataInfos() {
		resourceMetadataInfos = analysisSqlMetadataInfos();
		if(requestBody.isParentSubResourceQuery() && requestBody.isRecursiveQuery()){
			parentResourceMetadataInfos = resourceMetadataInfos;
		}
		inSqlResultSetMetadataInfoList = analysisProcedureSqlInResultSetMetadataInfoList();
		outSqlResultSetMetadataInfos = analysisSelectSqlOutResultSetMetadataInfos();
	}
	
	/**
	 * 解析出sql参数的元数据信息集合
	 * @param sqlParams
	 * @return
	 */
	private List<ResourceMetadataInfo> analysisSqlMetadataInfos(){
		List<ResourceMetadataInfo> metadataInfos = null;
		if(sqlParams != null && sqlParams.size() > 0){
			metadataInfos = new ArrayList<ResourceMetadataInfo>(sqlParams.size());
			for (ComSqlScriptParameter sqlParam : sqlParams) {
				metadataInfos.add(new ResourceMetadataInfo(
						sqlParam.getParameterName(),
						sqlParam.getParameterDataType(),
						sqlParam.getLength(),
						sqlParam.getPrecision(),
						0, // sql脚本参数不需要唯一约束
						0, // sql脚本参数不能为空
						sqlParam.getRemark()));
			}
		}
		return metadataInfos;
	}
	
	/**
	 * 解析出procedure sql传入表对象的元数据信息集合
	 * @return
	 */
	private List<List<ResourceMetadataInfo>> analysisProcedureSqlInResultSetMetadataInfoList() {
		if(BuiltinDatabaseData.PROCEDURE.equals(sql.getSqlScriptType())){
			List<List<CfgSqlResultset>> inSqlResultsetsList = sql.getInSqlResultsetsList();
			if(inSqlResultsetsList != null && inSqlResultsetsList.size() > 0){
				List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList = new ArrayList<List<ResourceMetadataInfo>>(inSqlResultsetsList.size());
				
				List<ResourceMetadataInfo> inSqlResultSetMetadataInfos = null;
				for (List<CfgSqlResultset> inSqlResultsets : inSqlResultsetsList) {
					if(inSqlResultsets != null && inSqlResultsets.size() > 0){
						inSqlResultSetMetadataInfos = new ArrayList<ResourceMetadataInfo>(inSqlResultsets.size());
						for (CfgSqlResultset crs : inSqlResultsets) {
							// TODO
							inSqlResultSetMetadataInfos.add(new ResourceMetadataInfo(
												crs.getPropName(),
												crs.getDataType(),
												0, // 系统还未实现解析该值  sqlParam.getLength()
												0, // 系统还未实现解析该值  sqlParam.getPrecision() 
												0, // 不需要唯一约束
												0, // 不需要是否不能为空约束
												null));
						}
					}
					inSqlResultSetMetadataInfoList.add(inSqlResultSetMetadataInfos);
				}
				return inSqlResultSetMetadataInfoList;
			}
		}
		return null;
	}
	
	/**
	 * 解析出select sql输出查询结果集的元数据信息集合
	 * @param sqlParams
	 * @return
	 */
	private List<ResourceMetadataInfo> analysisSelectSqlOutResultSetMetadataInfos() {
		if(BuiltinDatabaseData.SELECT.equals(sql.getSqlScriptType())){
			List<CfgSqlResultset> outSqlResultSet = sql.getOutSqlResultsetsList().get(0);
			List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(outSqlResultSet.size());
			for (CfgSqlResultset csr : outSqlResultSet) {
				metadataInfos.add(new ResourceMetadataInfo(csr.getPropName()));
			}
			return metadataInfos;
		}
		return null;
	}
	
	/**
	 * 初始化sql脚本实际传入的参数集合
	 */
	private void initActualParamsList() {
		Map<String, String> inSqlParams = analysisInSqlParams();
		IJson formData = requestBody.getFormData();
		
		List<ComSqlScriptParameter> sqlScriptActualParameters = null;
		ComSqlScriptParameter ssp = null;
		
		// 请求体为空，那么是从url传参，则是get请求select sql资源
		if((formData == null || formData.size() == 0)){
			if(inSqlParams != null && inSqlParams.size() > 0){
				actualParamsList = new ArrayList<List<ComSqlScriptParameter>>(1);
				// 解析sql脚本的参数
				sqlScriptActualParameters = new ArrayList<ComSqlScriptParameter>(inSqlParams.size());
				actualParamsList.add(sqlScriptActualParameters);
				
				Set<String> parameterNames = inSqlParams.keySet();
				for (String parameterName : parameterNames) {
					ssp = new ComSqlScriptParameter(parameterName, null, 0, -1, false);
					ssp.setActualInValue(processActualValue(inSqlParams.get(parameterName).trim()));
					sqlScriptActualParameters.add(ssp);
				}
				inSqlParams.clear();
			}
		}
		// 否则就是通过请求体传参，则是post/put/delete insert/update/delete 等sql资源
		else{
			int len = formData.size();
			actualParamsList = new ArrayList<List<ComSqlScriptParameter>>(len);
			
			JSONObject json;
			for(int i=0;i<len;i++){
				json = formData.get(i);
				if(json != null && json.size()>0){
					sqlScriptActualParameters = new ArrayList<ComSqlScriptParameter>(json.size());
					actualParamsList.add(sqlScriptActualParameters);
					
					Set<String> parameterNames = json.keySet();
					for (String parameterName : parameterNames) {
						ssp = new ComSqlScriptParameter(parameterName, null, 0, -1, false);
						ssp.setActualInValue(json.getString(parameterName).trim());
						sqlScriptActualParameters.add(ssp);
					}
				}else{
					actualParamsList.add(null);
				}
			}
		}
	}
	
	/**
	 * 解析传入的sql脚本参数
	 * @return
	 */
	private Map<String, String> analysisInSqlParams() {
		Map<String, String> requestResourceParams = requestBody.getRequestResourceParams();
		Map<String, String> inSqlParams = null;
		if(sqlParams != null && sqlParams.size() > 0 && requestResourceParams.size() > 0){
			inSqlParams = new HashMap<String, String>(16);// 默认初始长度为16
			
			Set<String> keys = requestResourceParams.keySet();
			for (ComSqlScriptParameter sqlParam : sqlParams) {
				for (String key : keys) {
					if(key.equalsIgnoreCase(sqlParam.getParameterName())){
						inSqlParams.put(key, requestResourceParams.get(key));
						break;
					}
				}
			}
			
			if(inSqlParams.size() > 0){
				keys = inSqlParams.keySet();
				for (String key : keys) {
					requestResourceParams.remove(key);
				}
			}
		}
		return inSqlParams;
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
	
	/**
	 * 对每个值进行验证
	 * <p>同时将调用sql资源时，实际传过来的值存到参数集合中(sql参数集合/procedure sql参数集合)</p>
	 * @return 
	 */
	private String validAndSetActualParams() {
		if((actualParamsList == null || actualParamsList.size() == 0) && (this.sqlParams == null || this.sqlParams.size() == 0)){
			return null;
		}
		// 如果实际传入了参数，但是配置的sql却没有任何参数，也是有问题的
		if(this.sqlParams == null || this.sqlParams.size() == 0){
			return "在调用sql资源时，传入的实际参数信息为：["+JsonUtil.toJsonString(actualParamsList, false)+"]，但是被调用的sql资源["+resourceName+"]却不存在任何参数信息。请检查该sql资源是否确实有参数配置，并确认是否合法调用sql资源";
		}
		
		List<List<ComSqlScriptParameter>> sqlParamsList = null;// set实际传入的参数值的集合
		List<ComSqlScriptParameter> sqlParams = null;
		String analysisActualInValueResult = null;
		
		int index = 1;
		// 如果没有传入任何参数，还能进入到这里，说明配置的参数要么是系统内置，要么是有默认值的
		if((actualParamsList == null || actualParamsList.size() == 0)){
			sqlParamsList = new ArrayList<List<ComSqlScriptParameter>>(1);
			sqlParams = this.sqlParams;
			sqlParamsList.add(sqlParams);
			
			for (ComSqlScriptParameter ssp : sqlParams) {
				analysisActualInValueResult = analysisActualInValue(ssp, requestBody.isGetRequest(), null, index++);
				if(analysisActualInValueResult != null){
					return analysisActualInValueResult;
				}
			}
		}
		// 否则就去解析实际的参数，将其和配置的参数进行匹配
		else{
			sqlParamsList = new ArrayList<List<ComSqlScriptParameter>>(actualParamsList.size());
			
			for (List<ComSqlScriptParameter> actualParams : actualParamsList) {
				sqlParams = cloneSqlParams();
				sqlParamsList.add(sqlParams);
				
				for (ComSqlScriptParameter ssp : sqlParams) {
					for (ResourceMetadataInfo rmi : resourceMetadataInfos) {
						if(ssp.getParameterName().equals(rmi.getName())){
							if(ssp.getParameterFrom() == ComSqlScriptParameter.SYSTEM_BUILTIN){// 参数值来源为系统内置
								analysisActualInValueResult = analysisActualInValue(ssp, requestBody.isGetRequest(), null, index++);
							}else if(ssp.getParameterFrom() == ComSqlScriptParameter.USER_INPUT){// 参数值来源为用户输入
								for (ComSqlScriptParameter ssap : actualParams) {
									if(ssp.getParameterName().equals(ssap.getParameterName())){
										ssp.setActualInValue(ssap.getActualInValue());
										break;
									}
								}
								analysisActualInValueResult = analysisActualInValue(ssp, requestBody.isGetRequest(), rmi, index++);
							}
							if(analysisActualInValueResult != null){
								return analysisActualInValueResult;
							}
							break;
						}
					}	
				}
				index=1;
			}
			this.sqlParams.clear();
		}
		sql.setSqlParamsList(sqlParamsList);
		return null;
	}
	
	/**
	 * 克隆sql参数集合
	 * @param sqlScriptParameterList
	 * @return
	 */
	private List<ComSqlScriptParameter> cloneSqlParams() {
		List<ComSqlScriptParameter> sqlParams = new ArrayList<ComSqlScriptParameter>(this.sqlParams.size());
		try {
			for (ComSqlScriptParameter sqlParam : this.sqlParams) {
				sqlParams.add((ComSqlScriptParameter)sqlParam.clone());
			}
		} catch (CloneNotSupportedException e) {
			throw new IllegalArgumentException(ExceptionUtil.getErrMsg(e));
		}
		return sqlParams;
	}
	
	
	/**
	 * 解析实际传入的参数值，同时进行校验
	 * @param ssp
	 * @param isGetRequest 是否是get请求
	 * @param rmi 对应的元数据信息对象，用来做校验
	 *            同时，该验证只针对用户输入的参数值，内置的参数值则不做校验
	 *            同时，该验证只针对占位符参数进行验证
	 * @param index
	 * @return
	 */
	public String analysisActualInValue(ComSqlScriptParameter ssp, boolean isGetRequest, ResourceMetadataInfo rmi, int index) {
		if(!ssp.getParameterDataType().equals(rmi.getDataType())){
			return "第"+index+"个对象，["+rmi.getDescName()+"] 参数，配置的数据类型("+ssp.getParameterDataType()+")和实际加载的数据类型("+rmi.getDataType()+")不一致，请联系后端系统开发人员";
		}
		
		if(ssp.getParameterFrom() == ComSqlScriptParameter.USER_INPUT){
			if(ssp.getActualInValue() == null){
				ssp.setActualInValue(ssp.getDefaultValue());
			}
			actualInValue = ssp.getActualInValue();
			if(actualInValue == null){
				return "在调用sql资源时，必须要传入的参数["+ssp.getParameterName()+"]，请修改调用方式，传入该参数值";
			}
			if(ssp.getIsPlaceholder() == 1){
				dataValueStr = actualInValue.toString();
				
				// 无论是什么类型的请求，日期类型都是string类型，都要进行转换
				if(BuiltinDataType.DATE.equals(ssp.getParameterDataType())){
					if(!DateUtil.valueIsDateFormat(actualInValue)){
						return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为日期类型";
					}
					actualInValue = DateUtil.parseTimestamp(dataValueStr);
				}else{
					if(isGetRequest){// get请求，值都是string类型，需要进行转换
						if(BuiltinDataType.INTEGER.equals(ssp.getParameterDataType())){
							try {
								actualInValue = Integer.valueOf(dataValueStr);
								if(dataValueStr.length() > rmi.getLength()){
									return "第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
								}
							} catch (NumberFormatException e) {
								return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为整数类型";
							}
						}else if(BuiltinDataType.DOUBLE.equals(ssp.getParameterDataType())){
							try {
								actualInValue = BigDecimal.valueOf(Double.valueOf(dataValueStr));
								if((dataValueStr.length()-1) > rmi.getLength()){
									return "第"+index+"个对象，["+rmi.getDescName()+"]的值长度，大于实际配置的长度("+rmi.getLength()+")";
								}
								if(dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
									return "第"+index+"个对象，["+rmi.getDescName()+"] 的值精度，大于实际配置的精度("+rmi.getPrecision()+")";
								}
							} catch (NumberFormatException e) {
								return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为浮点类型";
							}
						}else if(BuiltinDataType.BOOLEAN.equals(ssp.getParameterDataType())){
							if(!"true".equals(dataValueStr) && !"false".equals(dataValueStr)){
								return "第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
							}
							actualInValue = ("true".equals(dataValueStr))? "1":"0";
						}else if(BuiltinDataType.STRING.equals(ssp.getParameterDataType())){
							if(StrUtils.calcStrLength(dataValueStr) > rmi.getLength()){
								return "第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
							}
						}else{
							return "第"+index+"个对象，["+rmi.getDescName()+"]，系统目前不支持["+ssp.getParameterDataType()+"]数据类型，请联系后端开发人员";
						}
					}else{// 否则就是post请求，直接判断，不需要转换
						if(ssp.getIsTableType() == 1){
							actualInValue = IJsonUtil.getIJson(dataValueStr);
							return validProcedureSqlInResultSet(ssp, rmi, index, (IJson)actualInValue);
						}else{
							return validDataIsLegal(actualInValue, rmi, index);
						}
					}
				}
			}else{
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else if(ssp.getParameterFrom() == ComSqlScriptParameter.SYSTEM_BUILTIN){
			actualInValue = BuiltinQueryParameters.getBuiltinQueryParamValue(ssp.getParameterName());
			if(actualInValue == null){
				return "调用sql脚本时，内置参数["+ssp.getParameterName()+"]的值为空，请联系后台系统开发人员";
			}
			if(ssp.getIsPlaceholder() == 0){
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else{
			return "parameterFrom的值，仅限于：[0(用户输入)、1(系统内置)]";
		}
		return null;
	}
	private Object actualInValue;
	private String dataValueStr;
	
	/**
	 * 验证存储过程传入的结果集
	 * @param ssp
	 * @param rmi
	 * @param index
	 * @param ijson
	 * @return
	 */
	private String validProcedureSqlInResultSet(ComSqlScriptParameter ssp, ResourceMetadataInfo rmi, int index, IJson ijson) {
		// TODO
		return null;
	}
	
	/**
	 * 获取简单的sql参数值
	 * <p>目前就是对值加上''</p>
	 * @param ssp
	 * @param sqlParameterValue
	 * @return
	 */
	private String getSimpleSqlParameterValue(ComSqlScriptParameter ssp, Object sqlParameterValue){
		if(sqlParameterValue == null){
			Log4jUtil.warn(ComSqlScriptParameter.class, "getSimpleSqlParameterValue", "在获取简单的sql参数值时，传入的sqlParameterValue参数值为null【目前就是对值加上''】");
			return "''";
		}
		return "'"+sqlParameterValue.toString()+"'";
	}
	
	/**
	 * 验证get请求的sql资源数据
	 * <p>主要验证请求的select sql，筛选的列名是否存在</p>
	 * @return
	 */
	private String validGetSqlResourceMetadata() {
		Set<String> requestResourcePropNames = requestBody.getRequestResourceParams().keySet();
		for (String propName : requestResourcePropNames) {
			if(validPropUnExists(propName, outSqlResultSetMetadataInfos)){
				return "执行selec sql资源["+resourceName+"]时，查询结果集不存在名为["+NamingProcessUtil.propNameTurnColumnName(propName)+"]的列";
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			requestResourcePropNames = requestBody.getRequestParentResourceParams().keySet();
			for (String propName : requestResourcePropNames) {
				if(validPropUnExists(propName, outSqlResultSetMetadataInfos)){
					return "执行selec sql资源["+parentResourceName+"]时，查询结果集不存在名为["+NamingProcessUtil.propNameTurnColumnName(propName)+"]的列";
				}
			}
		}
		return null;
	}
	
	/**
	 * 验证post请求的sql资源数据
	 * <p>sql参数验证完，目前post请求不需要再验证其他数据</p>
	 * @return
	 */
	private String validPostSqlResourceMetadata() {
		return null;
	}
}
