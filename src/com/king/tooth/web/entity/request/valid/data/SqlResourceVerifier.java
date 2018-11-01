package com.king.tooth.web.entity.request.valid.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;
import com.king.tooth.sys.builtin.data.BuiltinQueryParameters;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.SqlResourceMetadataInfo;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ExceptionUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.datatype.DataTypeValidUtil;
import com.king.tooth.util.prop.code.rule.PropCodeRuleUtil;
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
				if(actualParams != null && actualParams.size() > 0){
					actualParams.clear();
				}
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
		return "sql资源，系统只支持[get、post]两种请求方式";
	}
	
	/**
	 * 初始化sql资源元数据信息集合
	 * @return
	 */
	private void initSqlResourceMetadataInfos() {
		resourceMetadataInfos = getSqlResourceParamsMetadataInfos(sqlParams);
		if(requestBody.isParentSubResourceQuery() && requestBody.isRecursiveQuery()){
			parentResourceMetadataInfos = resourceMetadataInfos;
		}
		inSqlResultSetMetadataInfoList = getSqlInResultSetMetadataInfoList(sql);
		outSqlResultSetMetadataInfos = getSqlOutResultSetMetadataInfos(sql);
	}
	
	/**
	 * 获取sql资源的参数元数据信息集合
	 * @param sqlParams
	 * @return
	 */
	private List<ResourceMetadataInfo> getSqlResourceParamsMetadataInfos(List<ComSqlScriptParameter> sqlParams){
		List<ResourceMetadataInfo> metadataInfos = null;
		if(sqlParams != null && sqlParams.size() > 0){
			metadataInfos = new ArrayList<ResourceMetadataInfo>(sqlParams.size());
			for (ComSqlScriptParameter sqlParam : sqlParams) {
				metadataInfos.add(new SqlResourceMetadataInfo(
						null,
						sqlParam.getParameterDataType(),
						sqlParam.getLength(),
						sqlParam.getPrecision(),
						0, // sql脚本参数不需要唯一约束
						0, // sql脚本参数不能为空
						0, // 不能忽略检查
						sqlParam.getParameterName(),
						sqlParam.getRemark()));
			}
		}
		return metadataInfos;
	}
	
	/**
	 * 获取sql资源的传入表对象参数的元数据信息集合
	 * <p>主要针对procedure</p>
	 * @param sql
	 * @return
	 */
	private List<List<ResourceMetadataInfo>> getSqlInResultSetMetadataInfoList(ComSqlScript sql){
		if(SqlStatementTypeConstants.PROCEDURE.equals(sql.getSqlScriptType())){
			List<CfgSqlResultset> inSqlResultsets = sql.getInSqlResultsets();
			if(inSqlResultsets != null && inSqlResultsets.size() > 0){
				List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList = new ArrayList<List<ResourceMetadataInfo>>(inSqlResultsets.size());
				for (CfgSqlResultset cfgSqlResultset : inSqlResultsets) {
					inSqlResultSetMetadataInfoList.add(cfgSqlResultset.getInSqlResultSetMetadataInfos());
				}
				return inSqlResultSetMetadataInfoList;
			}
		}
		return null;
	}
	
	/**
	 * 获取sql资源传出的结果集元数据信息集合
	 * <p>主要针对select</p>
	 * @param sql
	 * @return
	 */
	private List<ResourceMetadataInfo> getSqlOutResultSetMetadataInfos(ComSqlScript sql){
		if(SqlStatementTypeConstants.SELECT.equals(sql.getSqlScriptType())){
			List<CfgSqlResultset> outSqlResultSet = sql.getOutSqlResultsetsList().get(0);
			List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(outSqlResultSet.size());
			for (CfgSqlResultset csr : outSqlResultSet) {
				metadataInfos.add(new SqlResourceMetadataInfo(csr.getPropName()));
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
		
		// 请求体为空，那么判断是否从url传参
		if((formData == null || formData.size() == 0)){
			if(inSqlParams != null && inSqlParams.size() > 0){
				actualParamsList = new ArrayList<List<ComSqlScriptParameter>>(1);
				// 解析sql脚本的参数
				sqlScriptActualParameters = new ArrayList<ComSqlScriptParameter>(inSqlParams.size());
				actualParamsList.add(sqlScriptActualParameters);
				
				Set<String> parameterNames = inSqlParams.keySet();
				for (String parameterName : parameterNames) {
					ssp = new ComSqlScriptParameter(parameterName, null, false, 0, -1, false, true);
					ssp.setActualInValue(processActualValue(inSqlParams.get(parameterName).trim()));
					sqlScriptActualParameters.add(ssp);
				}
				inSqlParams.clear();
			}
		}
		// 否则就是通过请求体传参
		else{
			int len = formData.size();
			actualParamsList = new ArrayList<List<ComSqlScriptParameter>>(len);
			
			JSONObject json = null;
			for(int i=0;i<len;i++){
				json = formData.get(i);
				if(json != null && json.size()>0){
					sqlScriptActualParameters = new ArrayList<ComSqlScriptParameter>(json.size());
					actualParamsList.add(sqlScriptActualParameters);
					
					Set<String> parameterNames = json.keySet();
					for (String parameterName : parameterNames) {
						ssp = new ComSqlScriptParameter(parameterName, null, false, 0, -1, false, true);
						ssp.setActualInValue(json.get(parameterName));
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
		// 如果没有传入任何参数，还能进入到这里，说明配置的参数要么是系统内置，要么是自动生成，要么是有默认值的
		if((actualParamsList == null || actualParamsList.size() == 0)){
			// 如果配置了sql参数，但是实际调用的时候没有传入任何参数
			for (ComSqlScriptParameter sqlParam : this.sqlParams) {
				if(sqlParam.getParameterFrom() == ComSqlScriptParameter.USER_INPUT && StrUtils.isEmpty(sqlParam.getDefaultValue())){
					return "在调用sql资源时，必须传入名为"+sqlParam.getParameterName()+"的参数值";
				}
			}
			
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
						if(ssp.getParameterName().equals(rmi.getPropName())){
							if(ssp.getParameterFrom() == ComSqlScriptParameter.SYSTEM_BUILTIN){// 参数值来源为系统内置
								analysisActualInValueResult = analysisActualInValue(ssp, requestBody.isGetRequest(), null, index);
							}else if(ssp.getParameterFrom() == ComSqlScriptParameter.USER_INPUT){// 参数值来源为用户输入
								if(actualParams != null && actualParams.size() > 0){
									for (ComSqlScriptParameter ssap : actualParams) {
										if(ssp.getParameterName().equals(ssap.getParameterName())){
											ssp.setActualInValue(ssap.getActualInValue());
											break;
										}
									}
								}
								analysisActualInValueResult = analysisActualInValue(ssp, requestBody.isGetRequest(), rmi, index);
							}
							if(analysisActualInValueResult != null){
								return analysisActualInValueResult;
							}
							break;
						}
					}	
				}
				index++;
				paramIndex = 0;
				inSqlResultSetMetadataInfoIndex = 0;
			}
			this.sqlParams.clear();
		}
		sql.setSqlParamsList(sqlParamsList);
		return null;
	}
	/** 用来自动生成编码的下标 */
	private int paramIndex;
	
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
		String desc = "操作sql资源时，";
		if(rmi != null && !ssp.getParameterDataType().equals(rmi.getDataType())){
			return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 参数，配置的数据类型("+ssp.getParameterDataType()+")和实际加载的数据类型("+rmi.getDataType()+")不一致，请联系后端系统开发人员";
		}
		
		if(ssp.getIsNullabled() == 1){
			return null;
		}
		if(ssp.getParameterFrom() == ComSqlScriptParameter.USER_INPUT){
			if(ssp.getActualInValue() == null){
				if(ssp.getDefaultValue() == null){
					return desc+"第"+index+"个对象，必须传入名为"+ssp.getParameterName()+"的参数值";
				}
				ssp.setActualInValue(ssp.getDefaultValue());
			}
			if(rmi != null){
				actualInValue = ssp.getActualInValue();
				if(actualInValue == null){
					return desc+"必须要传入的参数["+ssp.getParameterName()+"]，请修改调用方式，传入该参数值";
				}
				if(ssp.getIsPlaceholder() == 1){
					dataValueStr = actualInValue.toString();
					
					// 无论是什么类型的请求，日期类型都是string类型，都要进行转换
					if(DataTypeConstants.DATE.equals(ssp.getParameterDataType())){
						if(DataTypeValidUtil.isDate(actualInValue)){
							actualInValue = DateUtil.parseTimestamp(dataValueStr);
						}else{
							return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为日期类型";
						}
					}else{
						if(isGetRequest){// get请求，值都是string类型，需要进行转换
							if(DataTypeConstants.INTEGER.equals(ssp.getParameterDataType())){
								if(DataTypeValidUtil.isInteger(dataValueStr)){
									actualInValue = Integer.valueOf(dataValueStr);
									if(rmi.getLength() != -1 && dataValueStr.length() > rmi.getLength()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
									}
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为整数类型";
								}
							}else if(DataTypeConstants.DOUBLE.equals(ssp.getParameterDataType())){
								if(DataTypeValidUtil.isNumber(dataValueStr)){
									actualInValue = BigDecimal.valueOf(Double.valueOf(dataValueStr));
									if(rmi.getLength() != -1 && (dataValueStr.length()-1) > rmi.getLength()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"]的值长度，大于实际配置的长度("+rmi.getLength()+")";
									}
									if(rmi.getPrecision() != -1 && dataValueStr.indexOf(".")!=-1 &&  dataValueStr.substring(dataValueStr.indexOf(".")+1).length() > rmi.getPrecision()){
										return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值精度，大于实际配置的精度("+rmi.getPrecision()+")";
									}
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为浮点类型[或数字类型]";
								}
							}else if(DataTypeConstants.BOOLEAN.equals(ssp.getParameterDataType())){
								if(DataTypeValidUtil.isBoolean(dataValueStr)){
									actualInValue = ("true".equals(dataValueStr))? "1":"0";
								}else{
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值不合法，应为布尔值类型";
								}
							}else if(DataTypeConstants.STRING.equals(ssp.getParameterDataType())){
								if(rmi.getLength() != -1 && StrUtils.calcStrLength(dataValueStr) > rmi.getLength()){
									return desc+"第"+index+"个对象，["+rmi.getDescName()+"] 的值长度，大于实际配置的长度("+rmi.getLength()+")";
								}
							}else{
								return desc+"第"+index+"个对象，["+rmi.getDescName()+"]，系统目前不支持["+ssp.getParameterDataType()+"]数据类型，请联系后端开发人员";
							}
						}else{// 否则就是post请求，直接判断，不需要转换
							if(ssp.getIsTableType() == 1){
								if(inSqlResultSetMetadataInfoList == null || inSqlResultSetMetadataInfoList.size() == 0){
									return "["+ssp.getParameterName()+"] 参数关联的表类型，没有查询到对应的列的元数据信息集合，请联系后端开发人员";
								}
								
								actualInValue = IJsonUtil.getIJson(dataValueStr);
								return validProcedureSqlInResultSet(desc, ssp, index, (IJson)actualInValue);
							}else{
								validDataIsLegalResult = ResourceHandlerUtil.validDataIsLegal(actualInValue, rmi);
								if(validDataIsLegalResult != null){
									return desc + "第"+index+"个对象，" + validDataIsLegalResult;
								}
								return null;
							}
						}
					}
				}else{
					actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
				}
			}
		}else if(ssp.getParameterFrom() == ComSqlScriptParameter.SYSTEM_BUILTIN){
			actualInValue = BuiltinQueryParameters.getBuiltinQueryParamValue(ssp.getParameterName());
			if(actualInValue == null){
				return desc+"内置参数["+ssp.getParameterName()+"]的值为空，请联系后端系统开发人员";
			}
			if(ssp.getIsPlaceholder() == 0){
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else if(ssp.getParameterFrom() == ComSqlScriptParameter.AUTO_CODE){
			actualInValue = PropCodeRuleUtil.getFinalCodeVal(ssp.getParameterName(), paramIndex++, requestBody.getResourcePropCodeRule());
			if(actualInValue == null){
				return desc+"自动编码参数["+ssp.getParameterName()+"]的值为空，请联系后端系统开发人员";
			}
			if(ssp.getIsPlaceholder() == 0){
				actualInValue = getSimpleSqlParameterValue(ssp, actualInValue);
			}
		}else{
			return "parameterFrom的值，仅限于：[0(用户输入)、1(系统内置)、2(自动编码)]，请联系后端系统开发人员";
		}
		ssp.setActualInValue(actualInValue);
		return null;
	}
	private Object actualInValue;
	private String dataValueStr;
	private String validDataIsLegalResult;
	
	/**
	 * 验证存储过程传入的结果集
	 * @param desc 
	 * @param ssp
	 * @param index
	 * @param ijson
	 * @return
	 */
	private String validProcedureSqlInResultSet(String desc, ComSqlScriptParameter ssp, int index, IJson ijson) {
		List<ResourceMetadataInfo> inSqlResultSetMetadataInfos = inSqlResultSetMetadataInfoList.get(inSqlResultSetMetadataInfoIndex++);
		if(inSqlResultSetMetadataInfos == null || inSqlResultSetMetadataInfos.size() == 0){
			return desc+"第"+index+"个对象，["+ssp.getParameterName()+"] 参数关联的表类型，没有查询到对应的列的元数据信息集合，请联系后端开发人员";
		}
		if(ijson != null && ijson.size() > 0){
			return validTableResourceMetadata(desc+"操作第"+index+"个对象，["+ssp.getParameterName()+"] 参数关联的表对象中，", ijson, false, false);
		}
		return null;
	}
	/** 如果调用存储过程，传入表参数类型，这里记录表参数元数据集合的下标，用来取对应的元数据集合来做数据验证 */
	private int inSqlResultSetMetadataInfoIndex = 0;
	
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
		return ssp.getValuePackStart()+sqlParameterValue.toString()+ssp.getValuePackEnd();
	}
	
	/**
	 * 验证get请求的sql资源数据
	 * <p>主要验证请求的select sql，筛选的列名是否存在</p>
	 * @return
	 */
	private String validGetSqlResourceMetadata() {
		Set<String> requestResourcePropNames = requestBody.getRequestResourceParams().keySet();
		for (String propName : requestResourcePropNames) {
			if(validPropUnExists(true, propName, outSqlResultSetMetadataInfos)){
				return "执行selec sql资源["+resourceName+"]时，查询结果集不存在名为["+NamingProcessUtil.propNameTurnColumnName(propName)+"]的列";
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			requestResourcePropNames = requestBody.getRequestParentResourceParams().keySet();
			for (String propName : requestResourcePropNames) {
				if(validPropUnExists(true, propName, outSqlResultSetMetadataInfos)){
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
