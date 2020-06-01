package com.api.util.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.constants.ResourcePropNameConstants;
import com.api.plugins.ijson.IJson;
import com.api.plugins.ijson.IJsonUtil;
import com.api.plugins.jdbc.DBLink;
import com.api.plugins.jdbc.IExecute;
import com.api.sys.builtin.data.BuiltinDatabaseData;
import com.api.sys.entity.cfg.CfgSql;
import com.api.sys.entity.cfg.CfgSqlParameter;
import com.api.sys.entity.cfg.CfgSqlResultset;
import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.api.thread.current.CurrentThreadContext;
import com.api.util.CloseUtil;
import com.api.util.JsonUtil;
import com.api.util.Log4jUtil;
import com.api.util.StrUtils;
import com.api.util.datatype.DataTypeTurnUtil;
import com.api.util.hibernate.HibernateUtil;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

/**
 * 存储过程工具类
 * @author DougLei
 */
public class ProcedureUtil {
	
	/**
	 * 带数据定位的执行存储过程
	 * @param sqlScript
	 * @param operDataType
	 * @param ijson
	 * @return
	 */
	public static JSONArray executeProcedureOnDataFocused(CfgSql sqlScript, String operDataType, IJson ijson) {
		JSONArray jsonArray = executeProcedure(sqlScript, ijson);
		if(operDataType != null && ijson != null && ijson.size() > 0){
			int size = ijson.size();
			String focusedDataId = null;
			for(int i=0;i<size;i++){
				focusedDataId = ijson.get(i).getString(ResourcePropNameConstants.ID);
				if(StrUtils.notEmpty(focusedDataId)){
					if(jsonArray.get(i) == null){
						jsonArray.add(new JSONObject(1));
					}
					if(focusedDataId.contains(",")){
						focusedDataId = focusedDataId.replace(",", "_"+operDataType+",");
					}
					jsonArray.getJSONObject(i).put(ResourcePropNameConstants.FOCUSED_OPER, focusedDataId + "_" + operDataType);
				}
			}
		}
		return jsonArray;
	}
	
	/**
	 * 执行存储过程
	 * @param sqlScript
	 * @param ijson
	 * @return
	 */
	public static JSONArray executeProcedure(CfgSql sqlScript, IJson ijson) {
		JSONArray jsonArray = null;
		
		boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqlScript.getDbType());
		boolean isSqlServer = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqlScript.getDbType());
		String sqlScriptId = sqlScript.getId();
		String sqlScriptType = sqlScript.getType();
		
		List<CfgSqlResultset> inSqlResultSets = sqlScript.getInSqlResultsets()==null?new ArrayList<CfgSqlResultset>(5):sqlScript.getInSqlResultsets();
		List<List<CfgSqlResultset>> outSqlResultSetsList = sqlScript.getOutSqlResultsetsList()==null?new ArrayList<List<CfgSqlResultset>>():sqlScript.getOutSqlResultsetsList();
		
		List<List<CfgSqlParameter>> sqlParamsList = sqlScript.getSqlParamsList();
		boolean sqlScriptHavaParams = (sqlParamsList != null && sqlParamsList.size() > 0);
		
		boolean isRecordResultset = isRecordResultset(ijson);
		
		String procedureName = sqlScript.getObjectName();
		String callProcedure = null;
		JSONObject json = null;
		
		if(sqlScriptHavaParams){
			jsonArray = new JSONArray(sqlParamsList.size());
			callProcedure = callProcedure(procedureName, sqlParamsList.get(0));
			int count = 1;
			for (List<CfgSqlParameter> sqlParams : sqlParamsList) {
				json = new JSONObject(10);
				execProcedure(procedureName, sqlScriptHavaParams, isOracle, isSqlServer, sqlScriptId, sqlScriptType, inSqlResultSets, outSqlResultSetsList, sqlParams, callProcedure, json, isRecordResultset);
				jsonArray.add(json);
				
				Log4jUtil.debug("第{}次执行procedure名为：{}", count, procedureName);
				Log4jUtil.debug("第{}次执行procedure的条件参数集合为：{}", count, JsonUtil.toJsonString(sqlParams, false));
				count++;
			}
		}else{
			jsonArray = new JSONArray(1);
			callProcedure = callProcedure(procedureName, null);
			json = new JSONObject(10);
			execProcedure(procedureName, sqlScriptHavaParams, isOracle, isSqlServer, sqlScriptId, sqlScriptType, inSqlResultSets, outSqlResultSetsList, null, callProcedure, json, isRecordResultset);
			jsonArray.add(json);
			
			Log4jUtil.debug("执行procedure名为：{}", procedureName);
			Log4jUtil.debug("执行procedure的没有参数");
		}
		
		if(sqlScriptHavaParams){
			for (List<CfgSqlParameter> sp : sqlParamsList) {
				if(sp != null){
					sp.clear();
				}
			}
			sqlParamsList.clear();
		}
		return jsonArray;
	}
	
	/**
	 * 组装调用存储过程的语句
	 * @param procedureName
	 * @param sqlScriptParameterList
	 * @return
	 */
	private static String callProcedure(final String procedureName, final List<CfgSqlParameter> sqlParams) {
		StringBuilder procedure = new StringBuilder();
		procedure.append("{call ").append(procedureName).append("(");
		if(sqlParams != null && sqlParams.size() > 0){
			int len = sqlParams.size();
			for (int i=0;i<len ;i++) {
				procedure.append("?,");
			}
			procedure.setLength(procedure.length() - 1);
		}
		procedure.append(")}");
		Log4jUtil.debug("调用的procedure为：{}", procedure);
		
		// 日志记录执行的存储过程和参数
		CurrentThreadContext.toReqLogDataAddOperSqlLog(procedure.toString(), sqlParams);
		
		return procedure.toString();
	}
	
	/**
	 * 是否记录存储过程返回的结果集元数据信息
	 * @param ijson
	 * @return
	 */
	private static boolean isRecordResultset(IJson ijson) {
		if(ijson != null && ijson.size() > 0){
			JSONObject json = null;
			for(int i=0;i<ijson.size();i++){
				json = ijson.get(i);
				if(json.get(ResourcePropNameConstants.RECORD_RESULTSET) != null && json.getString(ResourcePropNameConstants.RECORD_RESULTSET).equals("true")){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 执行存储过程
	 * <p>输出参数，返回结果，按顺序存储到json中</p>
	 * @param procedureName 
	 * @param sqlScriptHavaParams
	 * @param isOracle
	 * @param isSqlServer
	 * @param sqlScriptId
	 * @param sqlScriptType
	 * @param inSqlResultSets
	 * @param outSqlResultSetsList
	 * @param sqlParams
	 * @param callProcedure
	 * @param json
	 * @param isRecordResultset
	 */
	private static void execProcedure(final String procedureName, final boolean sqlScriptHavaParams, final boolean isOracle, final boolean isSqlServer, final String sqlScriptId, final String sqlScriptType, final List<CfgSqlResultset> inSqlResultSets, final List<List<CfgSqlResultset>> outSqlResultSetsList, final List<CfgSqlParameter> sqlParams, final String callProcedure, final JSONObject json, final boolean isRecordResultset){
		new DBLink(CurrentThreadContext.getDatabaseInstance(), false).doExecute(new IExecute() {
			private int inSqlResultsetIndex = 0;
			
			public void execute(Connection connection) throws SQLException {
				CallableStatement cs = null;
				ResultSet rs = null;
				try {
					cs = connection.prepareCall(callProcedure);
					setParameters(cs, sqlParams);
					cs.execute();
					putOutputValues(cs, rs, sqlParams);
				} finally {
					CloseUtil.closeDBConn(rs, cs, connection);
				}
			}
			
			/**
			 * 设置参数
			 * @param cs
			 * @param sqlParams
			 * @throws SQLException 
			 */
			private void setParameters(CallableStatement cs, List<CfgSqlParameter> sqlParams) throws SQLException {
				if(sqlParams != null && sqlParams.size() > 0){
					for (CfgSqlParameter parameter : sqlParams) {
						if(parameter.getInOut() == 1){//in
							setParameter(cs, parameter, parameter.getActualInValue());
						}else if(parameter.getInOut() == 2){//out
							cs.registerOutParameter(parameter.getOrderCode(), DBUtil.getDatabaseDataTypeCode(parameter.getDataType(), parameter.getIsTableType(), isOracle, isSqlServer));
						}else if(parameter.getInOut() == 3){//in out
							setParameter(cs, parameter, parameter.getActualInValue());
							cs.registerOutParameter(parameter.getOrderCode(), DBUtil.getDatabaseDataTypeCode(parameter.getDataType(), parameter.getIsTableType(), isOracle, isSqlServer));
						}
					}
				}
			}
			
			/**
			 * 设置参数
			 * @param cs
			 * @param parameter
			 * @param actualInValue
			 * @throws SQLException
			 */
			private void setParameter(CallableStatement cs, CfgSqlParameter parameter, Object actualInValue) throws SQLException {
				if(isOracle){
					setOracleParameter(cs, parameter, actualInValue);
				}else if(isSqlServer){
					setSqlServerParameter(cs, parameter, actualInValue);
				}
			}

			/**
			 * 设置oracle的参数
			 * @param cs
			 * @param parameter
			 * @param actualInValue
			 * @throws SQLException
			 */
			private void setOracleParameter(CallableStatement cs, CfgSqlParameter parameter, Object actualInValue) throws SQLException {
				if(parameter.getIsTableType() == 1){
					// TODO 处理oracle游标类型的参数
					inSqlResultsetIndex++;
				}else{
					cs.setObject(parameter.getOrderCode(), actualInValue);
				}
			}
			
			/**
			 * 设置sqlserver的参数
			 * @param cs
			 * @param parameter
			 * @param actualInValue
			 * @throws SQLException 
			 */
			private void setSqlServerParameter(CallableStatement cs, CfgSqlParameter parameter, Object actualInValue) throws SQLException {
				if(parameter.getIsTableType() == 1){
					List<ResourceMetadataInfo> inSqlResultSetMetadataInfos = inSqlResultSets.get(inSqlResultsetIndex).getInSqlResultSetMetadataInfos();
					
					SQLServerDataTable table = new SQLServerDataTable();
					for (ResourceMetadataInfo inSqlResultSetMetadataInfo : inSqlResultSetMetadataInfos) {
						// 添加列信息：列名，列类型
						table.addColumnMetadata(inSqlResultSetMetadataInfo.getColumnName(), DBUtil.getDatabaseDataTypeCode(inSqlResultSetMetadataInfo.getDataType(), 0, isOracle, isSqlServer));
					}
					
					if(actualInValue != null){
						IJson ijson = IJsonUtil.getIJson(actualInValue);
						int arrLength = ijson.size();
						if(arrLength > 0){
							int objLength = inSqlResultSetMetadataInfos.size();
							JSONObject json;
							Object[] valueArr;
							for(int i =0; i<arrLength; i++){
								json = ijson.get(i);
								if(json != null && json.size()>0){
									valueArr = new Object[json.size()];
									for(int j=0; j<objLength; j++){
										valueArr[j] = DataTypeTurnUtil.turnValueDataType(json.get(inSqlResultSetMetadataInfos.get(j).getPropName()), inSqlResultSetMetadataInfos.get(j).getDataType(), false, false, true);
									}
									table.addRow(valueArr);
								}
							}
							ijson.clear();
						}
					}
					SQLServerCallableStatement scs = (SQLServerCallableStatement) cs;
					scs.setStructured(parameter.getOrderCode(), parameter.getDataType(), table);
					
					inSqlResultsetIndex++;
				}else{
					cs.setObject(parameter.getOrderCode(), actualInValue);
				}
			}

			/**
			 * 存储output类型的值
			 * @param cs
			 * @param rs 
			 * @param sqlParams
			 * @throws SQLException 
			 */
			private void putOutputValues(CallableStatement cs, ResultSet rs, List<CfgSqlParameter> sqlParams) throws SQLException {
				if(isOracle){
					if(!sqlScriptHavaParams){
						return;
					}
					if(sqlParams != null && sqlParams.size() > 0){
						int outSqlResultsetIndex = 0;
						for (CfgSqlParameter sp : sqlParams) {
							if(sp.getInOut() == 2 || sp.getInOut() == 3){
								if(sp.getIsTableType() == 1){
									json.put(sp.getName(), getOracleCursorDataSet(cs, rs, sp.getOrderCode(), outSqlResultsetIndex, sp.getId(), sp.getName()));
									outSqlResultsetIndex++;
								}else{
									json.put(sp.getName(), cs.getObject(sp.getOrderCode()));
								}
							}
						}
					}
				}else if(isSqlServer){
					putSqlServerDataSet(cs, rs);
					if(sqlParams != null && sqlParams.size() > 0){
						for (CfgSqlParameter sp : sqlParams) {
							if(sp.getInOut() == 2 || sp.getInOut() == 3){
								json.put(sp.getName(), cs.getObject(sp.getOrderCode()));
							}
						}
					}
				}
			}
			
			/**
			 * 获取oracle的游标数据集
			 * @param rs 
			 * @param cs 
			 * @param orderCode
			 * @param outSqlResultsetIndex
			 * @param sqlParameterId
			 * @param sqlParameterName
			 * @return
			 */
			private List<Map<String, Object>> getOracleCursorDataSet(CallableStatement cs, ResultSet rs, Integer orderCode, int outSqlResultsetIndex, String sqlParameterId, String sqlParameterName) throws SQLException {
				try {
					rs = (ResultSet) cs.getObject(orderCode);
					processResultSetList(rs, outSqlResultsetIndex, sqlParameterId, sqlParameterName);
					return sqlQueryResultToMap(rs, outSqlResultSetsList.get(outSqlResultsetIndex));
				} finally{
					CloseUtil.closeDBConn(rs);
				}
			}

			/**
			 * 存储sqlserver的数据集
			 * @param cs
			 * @param rs 
			 * @throws SQLException 
			 */
			private void putSqlServerDataSet(CallableStatement cs, ResultSet rs) throws SQLException {
				int outSqlResultsetIndex = 0;
				rs = cs.getResultSet();
				while(rs != null || cs.getUpdateCount() > -1){
					if(rs != null){
						processResultSetList(rs, outSqlResultsetIndex, null, null);
						json.put(outSqlResultSetsList.get(outSqlResultsetIndex).get(0).getName(outSqlResultsetIndex), sqlQueryResultToMap(rs, outSqlResultSetsList.get(outSqlResultsetIndex)));
						outSqlResultsetIndex++;
					}
					cs.getMoreResults();
					rs = cs.getResultSet();
				}
			}
			
			/**
			 * 处理结果集
			 * <p>如果没有结果集，则要获取结果集信息并保存到数据库</p>
			 * @param rs
			 * @param outSqlResultsetIndex
			 * @param sqlParameterId
			 * @param sqlParameterName 
			 * @throws SQLException 
			 */
			private void processResultSetList(ResultSet rs, int outSqlResultsetIndex, String sqlParameterId, String sqlParameterName) throws SQLException {
				if(outSqlResultSetsList.size() == outSqlResultsetIndex){
					ResultSetMetaData rsmd = rs.getMetaData();
					int len = rsmd.getColumnCount();
					
					String columnName = null;
					for(int i=1;i<=len-1;i++){
						columnName = rsmd.getColumnName(i);
						for(int j=i+1;j<=len;j++){
							if(columnName.equals(rsmd.getColumnName(j))){
								if(isOracle){
									throw new IllegalArgumentException("调用名为["+procedureName+"]的存储过程，返回参数名为["+sqlParameterName+"]的结果集中，出现重复列名["+columnName+"]，位置分别位于["+i+","+j+"]，请检查");
								}else if(isSqlServer){
									throw new IllegalArgumentException("调用名为["+procedureName+"]的存储过程，返回的第"+(outSqlResultsetIndex+1)+"个结果集中，出现重复列名["+columnName+"]，位置分别位于["+i+","+j+"]，请检查");
								}
							}
						}
					}
					
					List<CfgSqlResultset> sqlResultSets = new ArrayList<CfgSqlResultset>(len);
					CfgSqlResultset csr = null;
					for(int i=1;i<=len;i++){
						csr = new CfgSqlResultset(sqlScriptType, rsmd.getColumnName(i), i, CfgSqlResultset.OUT);
						csr.setSqlScriptId(sqlScriptId);
						
						if(isSqlServer){
							csr.setBatchOrder(outSqlResultsetIndex);
							csr.setName("dataSet"+(outSqlResultsetIndex+1));
						}else if(isOracle){
							csr.setSqlParameterId(sqlParameterId);
						}
						if(isRecordResultset){
							HibernateUtil.saveObject(csr, null);// 保存结果集信息
						}
						sqlResultSets.add(csr);
					}
					outSqlResultSetsList.add(sqlResultSets);
				}
			}

			/**
			 * sql查询结果集转为list<map>
			 * <p>将列名，转换为属性名，作为key</p>
			 * <p>将值作为value显示</p>
			 * @param rs
			 * @param sqlResultSets
			 * @return
			 * @throws SQLException 
			 */
			private List<Map<String, Object>> sqlQueryResultToMap(ResultSet rs, List<CfgSqlResultset> sqlResultSets) throws SQLException{
				List<Object[]> queryResultSetList = getQueryResultSetList(rs, sqlResultSets);
				List<Map<String, Object>> dataList = null;
				if(queryResultSetList != null && queryResultSetList.size() > 0){
					dataList = new ArrayList<Map<String, Object>>(queryResultSetList.size());
					Map<String, Object> data = null;

					for(Object[] object : queryResultSetList){
						data = new HashMap<String, Object>(object.length);
						int i = 0;
						for(CfgSqlResultset csr : sqlResultSets){
							data.put(csr.getPropName(), object[i++]);
						}
						dataList.add(data);
					}
					queryResultSetList.clear();
				}else{
					Log4jUtil.debug("将sql查询结果集转为list<Map>时，sql查询的结果集为空，转换结果为空");
				}
				return dataList;
			}
			
			/**
			 * 从ResultSet中，获取结果集集合
			 * @param rs
			 * @param sqlResultSets
			 * @return
			 * @throws SQLException 
			 */
			private List<Object[]> getQueryResultSetList(ResultSet rs, List<CfgSqlResultset> sqlResultSets) throws SQLException{
				if(sqlResultSets == null || sqlResultSets.size() == 0){
					throw new NullPointerException("将sql查询结果转为map时，要参照的结果集列信息集合不能为空[sqlResultsets]");
				}
				List<Object[]> queryResultSetList = null;
				
				int flag = 0;
				int size = sqlResultSets.size();
				Object[] objects;
				while(rs.next()){
					if(flag == 0){
						queryResultSetList = new ArrayList<Object[]>();
						flag++;
					}
					objects = new Object[size];
					for(int i=0;i<size;i++){
						objects[i] = rs.getObject(i+1);
					}
					queryResultSetList.add(objects);
				}
				return queryResultSetList;
			}
		});
	}
}
