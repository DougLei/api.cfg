package com.king.tooth.sys.entity.cfg.sql;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.database.ProcedureUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * sql执行者
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SqlExecutor implements Serializable{
	
	private List<List<Object>> sqlParameterValues;
	
	/**
	 * 执行修改的sql语句
	 * <p>增删改</p>
	 * @param sql
	 * @param data
	 * @return
	 */
	public Object doExecuteModifySql(CfgSql sql, List<List<Object>> sqlParameterValues, IJson data){
		this.sqlParameterValues = sqlParameterValues;
		
		List<FinalSqlScriptStatement> finalSqlScriptList = sql.getFinalSqlScriptList();
		
		String operDataType = null;
		if(SqlStatementTypeConstants.INSERT.equals(sql.getConfType())){
			operDataType = OperDataTypeConstants.ADD;
		}else if(SqlStatementTypeConstants.UPDATE.equals(sql.getConfType())){
			operDataType = OperDataTypeConstants.EDIT;
		}else if(SqlStatementTypeConstants.DELETE.equals(sql.getConfType())){
			operDataType = OperDataTypeConstants.DELETE;
		}
		
		if(SqlStatementTypeConstants.PROCEDURE.equals(sql.getType())){// 是存储过程
			JSONArray jsonArray = ProcedureUtil.executeProcedureOnDataFocused(sql, operDataType, data);
			if(jsonArray != null && jsonArray.size() > 0){
				if(jsonArray.size() == 1){
					return jsonArray.getJSONObject(0);
				}else{
					return jsonArray;
				}
			}else{
				JSONObject json = new JSONObject(1);
				json.put("execProcedure", "成功执行名为["+sql.getObjectName()+"]的存储过程");
				return json;
			}
		}else{
			String[] modifySqlArr;
			Query query;
			int index = 0;
			for (FinalSqlScriptStatement finalSqlScript : finalSqlScriptList) {
				modifySqlArr = finalSqlScript.getFinalModifySqlArr();
				int len = modifySqlArr.length;
				for (int i = 0; i < len; i++) {
					query = createQuery(index++, modifySqlArr[i].replace(";", ""));
					query.executeUpdate();
				}
			}
			
			if(operDataType != null && data != null && data.size() > 0){
				int size = data.size();
				JSONObject jsonObject;
				for(int i=0;i<size;i++){
					jsonObject = data.get(i);
					if(jsonObject.get(ResourcePropNameConstants.ID) != null){
						jsonObject.put(ResourcePropNameConstants.FOCUSED_OPER, jsonObject.getString(ResourcePropNameConstants.ID) + "_" + operDataType);
					}
				}
			}
			
			if(data != null){
				return data.getJson();
			}else{
				return sql.getSqlParamsListJson();
			}
		}
	}
	
	/**
	 * 创建查询对象
	 * @param index
	 * @param sql
	 * @param
	 */
	protected final Query createQuery(int index, String sql){
		Query query = HibernateUtil.getCurrentThreadSession().createSQLQuery(sql);
		setQueryCondParamters(index, query);
		
		Log4jUtil.debug("【最后执行的sql语句为：{}】", sql);
		Log4jUtil.debug("【最后执行的sql语句对应的条件值集合为：{}】", sqlParameterValues.size()>0?sqlParameterValues.get(index):null);
		
		// 日志记录执行的sql语句和参数
		CurrentThreadContext.toReqLogDataAddOperSqlLog(sql, sqlParameterValues.size()>0?sqlParameterValues.get(index):null);
		return query;
	}
	
	/**
	 * 给查询对象query设置各个条件的值
	 * @param index
	 * @param query
	 */
	private void setQueryCondParamters(int index, Query query){
		if(sqlParameterValues.size() > 0){
			List<Object> querySqlParameterValues = sqlParameterValues.get(index);
			if(querySqlParameterValues != null && querySqlParameterValues.size() > 0){
				int i = 0;
				for (Object val : querySqlParameterValues) {
					query.setParameter(i++, val);
				}
			}
		}
	}
}
