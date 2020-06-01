package com.api.web.entity.request.valid.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.api.constants.SqlStatementTypeConstants;
import com.api.sys.entity.cfg.CfgSql;
import com.api.sys.entity.cfg.CfgSqlParameter;
import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.api.util.NamingProcessUtil;
import com.api.web.entity.request.RequestBody;
import com.api.web.entity.request.valid.data.AbstractResourceVerifier;
import com.api.web.entity.request.valid.data.util.SqlResourceValidUtil;
import com.api.web.entity.request.valid.data.util.TableResourceValidUtil;

/**
 * sql资源的数据校验类
 * @author DougLei
 */
public class SqlResourceVerifier extends AbstractResourceVerifier{

	private CfgSql sql;
	private List<CfgSqlParameter> sqlParams;
	
	/**
	 * sql脚本实际传入的参数集合
	 */
	private List<List<CfgSqlParameter>> actualParamsList;
	/**
	 * 针对select sql查询结果集的元数据信息集合
	 */
	private List<ResourceMetadataInfo> outSqlResultSetMetadataInfos;
	/**
	 * 针对procedure sql传入表对象的元数据信息集合
	 */
	private List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList;
	
	public SqlResourceVerifier(RequestBody requestBody) {
		super(requestBody);
		sql = requestBody.getResourceInfo().getSql();
		sqlParams = sql.getSqlParams();
	}
	
	public void clearValidData(){
		super.clearValidData();
//		if(inSqlResultSetMetadataInfoList != null && inSqlResultSetMetadataInfoList.size() > 0){
//			for (List<ResourceMetadataInfo> inSqlResultSetMetadataInfos : inSqlResultSetMetadataInfoList) {
//				if(inSqlResultSetMetadataInfos != null && inSqlResultSetMetadataInfos.size() > 0){
//					inSqlResultSetMetadataInfos.clear();
//				}
//			}
//			inSqlResultSetMetadataInfoList.clear();
//		}
		
		if(!SqlStatementTypeConstants.SELECT.equals(sql.getType())){// select语句不清空的原因是，后续还要使用元数据信息中的数据类型，对值进行数据类型转换操作
			if(outSqlResultSetMetadataInfos != null && outSqlResultSetMetadataInfos.size() > 0){
				outSqlResultSetMetadataInfos.clear();
			}
		}
		
		if(actualParamsList != null && actualParamsList.size() > 0){
			for (List<CfgSqlParameter> actualParams : actualParamsList) {
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
		actualParamsList = SqlResourceValidUtil.initActualParamsList(analysisInSqlParams(), requestBody.getFormData());
		
		String validResult = SqlResourceValidUtil.doValidAndSetActualParams(sql, requestBody.isGetRequest(), actualParamsList, resourceMetadataInfos, inSqlResultSetMetadataInfoList);
		if(validResult != null){
			return validResult;
		}
		
		if(requestBody.isGetRequest()){
			return validGetSqlResourceMetadata();
		}else if(requestBody.isPostRequest() || requestBody.isPutRequest() || requestBody.isDeleteRequest()){
			return validPostSqlResourceMetadata();
		}
		return "sql资源，只支持[get、post、put、delete]四种请求方式";
	}
	
	/**
	 * 初始化sql资源元数据信息集合
	 * @return
	 */
	private void initSqlResourceMetadataInfos() {
		resourceMetadataInfos = SqlResourceValidUtil.getSqlResourceParamsMetadataInfos(sql);
		if(requestBody.isParentSubResourceQuery() && requestBody.isRecursiveQuery()){
			parentResourceMetadataInfos = resourceMetadataInfos;
		}
		inSqlResultSetMetadataInfoList = SqlResourceValidUtil.getSqlInResultSetMetadataInfoList(sql);
		outSqlResultSetMetadataInfos = SqlResourceValidUtil.getSqlOutResultSetMetadataInfos(sql);
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
			for (CfgSqlParameter sqlParam : sqlParams) {
				for (String key : keys) {
					if(key.equalsIgnoreCase(sqlParam.getName())){
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
	 * 验证get请求的sql资源数据
	 * <p>主要验证请求的select sql，筛选的列名是否存在</p>
	 * @return
	 */
	private String validGetSqlResourceMetadata() {
		Set<String> requestResourcePropNames = requestBody.getRequestResourceParams().keySet();
		for (String propName : requestResourcePropNames) {
			if(TableResourceValidUtil.validPropUnExists(true, propName, outSqlResultSetMetadataInfos)){
				return "执行selec sql资源["+resourceName+"]时，查询结果集不存在名为["+NamingProcessUtil.propNameTurnColumnName(propName)+"]的列";
			}
		}
		
		if(requestBody.isParentSubResourceQuery()){
			requestResourcePropNames = requestBody.getRequestParentResourceParams().keySet();
			for (String propName : requestResourcePropNames) {
				if(TableResourceValidUtil.validPropUnExists(true, propName, outSqlResultSetMetadataInfos)){
					return "执行selec sql资源["+parentResourceName+"]时，查询结果集不存在名为["+NamingProcessUtil.propNameTurnColumnName(propName)+"]的列";
				}
			}
		}
		
		// 记录请求的查询资源的元数据信息集合
		requestBody.setQueryResourceMetadataInfos(outSqlResultSetMetadataInfos);
		requestBody.setQueryParentResourceMetadataInfos(outSqlResultSetMetadataInfos);
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
