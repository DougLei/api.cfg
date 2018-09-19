package com.king.tooth.util.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.database.SQLServerDataTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.IExecute;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.CfgSqlResultset;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

/**
 * 存储过程工具类
 * @author DougLei
 */
public class ProcedureUtil {
	
	/**
	 * 执行存储过程
	 * @param sqlScript
	 * @return
	 */
	public static JSONObject executeProcedure(final ComSqlScript sqlScript) {
		final List<List<ComSqlScriptParameter>> sqlParamsList = sqlScript.getSqlParamsList();
		final boolean sqlScriptHavaParams = (sqlParamsList != null && sqlParamsList.size() > 0);
		if(sqlScriptHavaParams && sqlParamsList.size() >1){
			throw new IllegalArgumentException("系统目前不支持批量处理存储过程，如有需要，请联系后台系统开发人员");
		}
		
		final String sqlScriptId = sqlScript.getId();
		final boolean isOracle = BuiltinDatabaseData.DB_TYPE_ORACLE.equals(sqlScript.getDbType());
		final boolean isSqlServer = BuiltinDatabaseData.DB_TYPE_SQLSERVER.equals(sqlScript.getDbType());
		final String procedureName = sqlScript.getObjectName();
		
		final JSONObject json = new JSONObject(10);
		final List<ComSqlScriptParameter> sqlParams = sqlScriptHavaParams?sqlParamsList.get(0):null;
		final List<List<CfgSqlResultset>> inSqlResultSetsList = sqlScript.getInSqlResultsetsList()==null?new ArrayList<List<CfgSqlResultset>>(5):sqlScript.getInSqlResultsetsList();
		final List<List<CfgSqlResultset>> outSqlResultSetsList = sqlScript.getOutSqlResultsetsList()==null?new ArrayList<List<CfgSqlResultset>>(5):sqlScript.getOutSqlResultsetsList();
		
		new DBLink(CurrentThreadContext.getDatabaseInstance()).doExecute(new IExecute() {
			private int inSqlResultsetIndex = 0;
			
			public void execute(Connection connection) throws SQLException {
				String procedure = callProcedure(procedureName, sqlParams);
				CallableStatement cs = null;
				ResultSet rs = null;
				try {
					cs = connection.prepareCall(procedure);
					setParameters(cs, sqlParams);
					cs.execute();
					putOutputValues(cs, rs, sqlParams);
				} finally {
					CloseUtil.closeDBConn(rs, cs, connection);
					if(sqlParams != null && sqlParams.size() > 0){
						sqlParams.clear();
					}
				}
			}
			
			/**
			 * 设置参数
			 * @param cs
			 * @param sqlParams
			 * @throws SQLException 
			 */
			private void setParameters(CallableStatement cs, List<ComSqlScriptParameter> sqlParams) throws SQLException {
				if(sqlParams != null && sqlParams.size() > 0){
					for (ComSqlScriptParameter parameter : sqlParams) {
						if(parameter.getInOut() == 1){//in
							setParameter(cs, parameter, parameter.getActualInValue());
						}else if(parameter.getInOut() == 2){//out
							cs.registerOutParameter(parameter.getOrderCode(), DBUtil.getDatabaseDataTypeCode(parameter.getParameterDataType(), parameter.getIsTableType(), isOracle, isSqlServer));
						}else if(parameter.getInOut() == 3){//in out
							setParameter(cs, parameter, parameter.getActualInValue());
							cs.registerOutParameter(parameter.getOrderCode(), DBUtil.getDatabaseDataTypeCode(parameter.getParameterDataType(), parameter.getIsTableType(), isOracle, isSqlServer));
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
			private void setParameter(CallableStatement cs, ComSqlScriptParameter parameter, Object actualInValue) throws SQLException {
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
			private void setOracleParameter(CallableStatement cs, ComSqlScriptParameter parameter, Object actualInValue) throws SQLException {
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
			private void setSqlServerParameter(CallableStatement cs, ComSqlScriptParameter parameter, Object actualInValue) throws SQLException {
				if(parameter.getIsTableType() == 1){
					List<CfgSqlResultset> inSqlResultSets = inSqlResultSetsList.get(inSqlResultsetIndex);
					
					SQLServerDataTable table = new SQLServerDataTable();
					for (CfgSqlResultset inSqlResultSet : inSqlResultSets) {
						// 添加列信息：列名，列类型
						table.addColumnMetadata(inSqlResultSet.getColumnName(), DBUtil.getDatabaseDataTypeCode(inSqlResultSet.getDataType(), 0, isOracle, isSqlServer));
					}
					
					if(actualInValue != null){
						IJson ijson = (IJson) actualInValue;
						int arrLength = ijson.size();
						if(arrLength > 0){
							int objLength = inSqlResultSets.size();
							JSONObject json;
							Object[] valueArr;
							for(int i =0; i<arrLength; i++){
								json = ijson.get(i);
								if(json != null && json.size()>0){
									valueArr = new Object[json.size()];
									for(int j=0; j<objLength; j++){
										if(SQLServerDataTypeConstants.DATETIME.equals(inSqlResultSets.get(j).getDataType())){
											valueArr[j] = DateUtil.parseTimestamp(json.getString(inSqlResultSets.get(j).getPropName()));
										}else{
											valueArr[j] = json.get(inSqlResultSets.get(j).getPropName());
										}
									}
									table.addRow(valueArr);
								}
							}
							ijson.clear();
						}
					}
					SQLServerCallableStatement scs = (SQLServerCallableStatement) cs;
					scs.setStructured(parameter.getOrderCode(), parameter.getParameterDataType(), table);
					
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
			private void putOutputValues(CallableStatement cs, ResultSet rs, List<ComSqlScriptParameter> sqlParams) throws SQLException {
				if(isOracle){
					if(!sqlScriptHavaParams){
						return;
					}
					if(sqlParams != null && sqlParams.size() > 0){
						int outSqlResultsetIndex = 0;
						for (ComSqlScriptParameter sp : sqlParams) {
							if(sp.getInOut() == 2 || sp.getInOut() == 3){
								if(sp.getIsTableType() == 1){
									json.put(sp.getParameterName(), getOracleCursorDataSet(cs, rs, sp.getOrderCode(), outSqlResultsetIndex, sp.getId()));
									outSqlResultsetIndex++;
								}else{
									json.put(sp.getParameterName(), cs.getObject(sp.getOrderCode()));
								}
							}
						}
					}
				}else if(isSqlServer){
					putSqlServerDataSet(cs, rs);
					if(sqlParams != null && sqlParams.size() > 0){
						for (ComSqlScriptParameter sp : sqlParams) {
							if(sp.getInOut() == 2 || sp.getInOut() == 3){
								json.put(sp.getParameterName(), cs.getObject(sp.getOrderCode()));
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
			 * @return
			 */
			private List<Map<String, Object>> getOracleCursorDataSet(CallableStatement cs, ResultSet rs, Integer orderCode, int outSqlResultsetIndex, String sqlParameterId) throws SQLException {
				try {
					rs = (ResultSet) cs.getObject(orderCode);
					processResultSetList(rs, outSqlResultsetIndex, sqlParameterId);
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
				while(rs != null){
					processResultSetList(rs, outSqlResultsetIndex, null);
					json.put(outSqlResultSetsList.get(outSqlResultsetIndex).get(0).getName(outSqlResultsetIndex), sqlQueryResultToMap(rs, outSqlResultSetsList.get(outSqlResultsetIndex)));
					outSqlResultsetIndex++;
					
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
			 * @throws SQLException 
			 */
			private void processResultSetList(ResultSet rs, int outSqlResultsetIndex, String sqlParameterId) throws SQLException {
				if(outSqlResultSetsList.size() == outSqlResultsetIndex){
					ResultSetMetaData rsmd = rs.getMetaData();
					int len = rsmd.getColumnCount();
					
					List<CfgSqlResultset> sqlResultSets = new ArrayList<CfgSqlResultset>(len);
					CfgSqlResultset csr = null;
					for(int i=1;i<=len;i++){
						csr = new CfgSqlResultset(rsmd.getColumnName(i), i, 2);
						csr.setSqlScriptId(sqlScriptId);
						
						if(isSqlServer){
							csr.setBatchOrder(outSqlResultsetIndex);
							csr.setName("dataSet"+(outSqlResultsetIndex+1));
						}else if(isOracle){
							csr.setSqlParameterId(sqlParameterId);
						}
						HibernateUtil.saveObject(csr, null);// 保存结果集信息
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
		Log4jUtil.debug("执行procedure名为：{}", procedureName);
		Log4jUtil.debug("执行procedure的条件参数集合为：{}", JsonUtil.toJsonString(sqlParams, false));
		
		if(sqlScriptHavaParams){
			for (List<ComSqlScriptParameter> sp : sqlParamsList) {
				if(sp != null){
					sp.clear();
				}
			}
			sqlParamsList.clear();
		}
		return json;
	}
	
	/**
	 * 组装调用存储过程的语句
	 * @param procedureName
	 * @param sqlScriptParameterList
	 * @return
	 */
	private static String callProcedure(final String procedureName, final List<ComSqlScriptParameter> sqlParams) {
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
		
		// 日志记录发出的hql/sql语句
		CurrentThreadContext.toReqLogDataAddOperSqlLog(procedure.toString(), sqlParams);
		
		return procedure.toString();
	}
	
}
