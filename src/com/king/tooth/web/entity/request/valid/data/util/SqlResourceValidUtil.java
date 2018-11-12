package com.king.tooth.web.entity.request.valid.data.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.CfgSqlParameter;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.SqlResourceMetadataInfo;
import com.king.tooth.web.entity.request.ResourcePropCodeRule;
import com.king.tooth.web.entity.request.valid.data.util.entity.SqlParamValidAndSetActualValueEntity;

/**
 * sql资源验证的工具类
 * @author DougLei
 */
public class SqlResourceValidUtil {

	/**
	 * 获取sql资源的参数元数据信息集合
	 * @param sqlParams
	 * @return
	 */
	public static List<ResourceMetadataInfo> getSqlResourceParamsMetadataInfos(CfgSql sql){
		List<CfgSqlParameter> sqlParams = sql.getSqlParams();
		List<ResourceMetadataInfo> metadataInfos = null;
		if(sqlParams != null && sqlParams.size() > 0){
			metadataInfos = new ArrayList<ResourceMetadataInfo>(sqlParams.size());
			for (CfgSqlParameter sqlParam : sqlParams) {
				metadataInfos.add(new SqlResourceMetadataInfo(
						null,
						sqlParam.getDataType(),
						sqlParam.getLength(),
						sqlParam.getPrecision(),
						0, // sql脚本参数不需要唯一约束
						0, // sql脚本参数不能为空
						0, // 不能忽略检查
						sqlParam.getName(),
						sqlParam.getRemark()));
			}
		}
		return metadataInfos;
	}
	
	// --------------------------------------------------------------------------------
	/**
	 * 获取sql资源的传入表对象参数的元数据信息集合
	 * <p>主要针对procedure</p>
	 * @param sql
	 * @return
	 */
	public static List<List<ResourceMetadataInfo>> getSqlInResultSetMetadataInfoList(CfgSql sql){
		if(SqlStatementTypeConstants.PROCEDURE.equals(sql.getType())){
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
	
	// --------------------------------------------------------------------------------
	/**
	 * 获取sql资源传出的结果集元数据信息集合
	 * <p>主要针对select</p>
	 * @param sql
	 * @return
	 */
	public static List<ResourceMetadataInfo> getSqlOutResultSetMetadataInfos(CfgSql sql){
		if(SqlStatementTypeConstants.SELECT.equals(sql.getType())){
			List<CfgSqlResultset> outSqlResultSet = sql.getOutSqlResultsetsList().get(0);
			List<ResourceMetadataInfo> metadataInfos = new ArrayList<ResourceMetadataInfo>(outSqlResultSet.size());
			for (CfgSqlResultset csr : outSqlResultSet) {
				metadataInfos.add(new SqlResourceMetadataInfo(csr.getPropName(), csr.getDataType()));
			}
			return metadataInfos;
		}
		return null;
	}
	
	// --------------------------------------------------------------------------------
	/**
	 * 初始化sql脚本实际传入的参数集合
	 * @param inSqlParams
	 * @param formData
	 * @return
	 */
	public static List<List<CfgSqlParameter>> initActualParamsList(Map<String, String> inSqlParams, IJson formData) {
		List<List<CfgSqlParameter>> actualParamsList = null;
		
		List<CfgSqlParameter> sqlScriptActualParameters = null;
		CfgSqlParameter ssp = null;
		
		// 请求体为空，那么判断是否从url传参
		if((formData == null || formData.size() == 0)){
			if(inSqlParams != null && inSqlParams.size() > 0){
				actualParamsList = new ArrayList<List<CfgSqlParameter>>(1);
				// 解析sql脚本的参数
				sqlScriptActualParameters = new ArrayList<CfgSqlParameter>(inSqlParams.size());
				actualParamsList.add(sqlScriptActualParameters);
				
				Set<String> parameterNames = inSqlParams.keySet();
				for (String parameterName : parameterNames) {
					ssp = new CfgSqlParameter(parameterName, null, false, 0, -1, false, true);
					ssp.setActualInValue(processActualValue(inSqlParams.get(parameterName).trim()));
					sqlScriptActualParameters.add(ssp);
				}
				inSqlParams.clear();
			}
		}
		// 否则就是通过请求体传参
		else{
			int len = formData.size();
			actualParamsList = new ArrayList<List<CfgSqlParameter>>(len);
			
			JSONObject json = null;
			for(int i=0;i<len;i++){
				json = formData.get(i);
				if(json != null && json.size()>0){
					sqlScriptActualParameters = new ArrayList<CfgSqlParameter>(json.size());
					actualParamsList.add(sqlScriptActualParameters);
					
					Set<String> parameterNames = json.keySet();
					for (String parameterName : parameterNames) {
						ssp = new CfgSqlParameter(parameterName, null, false, 0, -1, false, true);
						ssp.setActualInValue(json.get(parameterName));
						sqlScriptActualParameters.add(ssp);
					}
				}else{
					actualParamsList.add(null);
				}
			}
		}
		return actualParamsList;
	}
	
	/**
	 * 处理每个值最外层的单引号或双引号
	 * <p>主要针对url参数的处理</p>
	 * @param actualValue
	 * @return
	 */
	private static String processActualValue(String actualValue){
		if(actualValue.startsWith("'") || actualValue.startsWith("\"")){
			actualValue = actualValue.substring(1, actualValue.length()-1);
		}
		return actualValue;
	}
	
	// --------------------------------------------------------------------------------
	/**
	 * 对每个值进行验证
	 * <p>同时将调用sql资源时，实际传过来的值存到参数集合中(sql参数集合/procedure sql参数集合)</p>
	 * @param sql
	 * @param isGetRequest
	 * @param actualParamsList
	 * @param resourceMetadataInfos
	 * @param inSqlResultSetMetadataInfoList
	 * @param resourcePropCodeRule
	 * @return
	 */
	public static String doValidAndSetActualParams(CfgSql sql, boolean isGetRequest, List<List<CfgSqlParameter>> actualParamsList, List<ResourceMetadataInfo> resourceMetadataInfos, List<List<ResourceMetadataInfo>> inSqlResultSetMetadataInfoList, ResourcePropCodeRule resourcePropCodeRule) {
		SqlParamValidAndSetActualValueEntity sqlParamValidAndSetActualValueEntity = 
				new SqlParamValidAndSetActualValueEntity(sql, isGetRequest, actualParamsList, resourceMetadataInfos, inSqlResultSetMetadataInfoList, resourcePropCodeRule);
		return sqlParamValidAndSetActualValueEntity.doValidAndSetActualParams();
	}
}
