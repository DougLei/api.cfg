package com.king.tooth.web.processer.sqlresource;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.constants.SqlStatementTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.cfg.CfgSql;
import com.king.tooth.sys.entity.cfg.sql.FinalSqlScriptStatement;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.database.ProcedureUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.sqlresource.BuiltinSqlResourceBMProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sqlscript.BuiltinSqlMethodProcesser;
import com.king.tooth.web.entity.request.valid.data.util.entity.SqlParamSetActualValueEntity;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.CommonProcesser;

/**
 * 通用的请求处理器类
 * <p>主要是一些通用的方法</p>
 * @author DougLei
 */
public class RequestProcesserCommon extends CommonProcesser{
	
	/**
	 * 内置sql资源函数处理器
	 */
	protected BuiltinSqlResourceBMProcesser builtinSqlResourceBMProcesser;
	
	/**
	 * sql语句中的参数值集合
	 * <p>可能有多个sql语句，所有用集合的集合封装参数</p>
	 */
	protected final List<List<Object>> sqlParameterValues = new ArrayList<List<Object>>(20);
	
	/**
	 * 内置sql脚本处理器
	 */
	protected BuiltinSqlMethodProcesser builtinSqlScriptMethodProcesser;
	
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
		
		// 日志记录发出的hql/sql语句
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
	
	/**
	 * 执行修改的处理
	 * <pre>
	 * 	包括
	 * 		insert(post)
	 * 		update(put)
	 * 		delete(delete)
	 * </pre>
	 * @param sqlDesc @see BuiltinDatabaseData
	 */
	protected final void doModifyProcess(String sqlDesc){
		CfgSql sqlScript = builtinSqlScriptMethodProcesser.getReqSql();
		List<FinalSqlScriptStatement> finalSqlScriptList = sqlScript.getFinalSqlScriptList();
		new SqlParamSetActualValueEntity().setFinalCodeVals(sqlScript, requestBody.getResourcePropCodeRule());
		
		if(SqlStatementTypeConstants.PROCEDURE.equals(sqlScript.getType())){// 是存储过程
			JSONArray jsonArray = ProcedureUtil.executeProcedure(sqlScript);
			
			if(jsonArray != null && jsonArray.size() > 0){
				if(jsonArray.size() == 1){
					setResponseBody(new ResponseBody(null, jsonArray.getJSONObject(0)));
				}else{
					setResponseBody(new ResponseBody(null, jsonArray));
				}
			}else{
				setResponseBody(new ResponseBody("成功执行名为["+sqlScript.getObjectName()+"]的存储过程", true));
			}
			return;
		}
		
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
		
		if(requestBody.getFormData() == null || requestBody.getFormData().size() == 0){
			setResponseBody(new ResponseBody(null, requestBody.installAllUrlParams()));
		}else{
			String operType = null;
			if(SqlStatementTypeConstants.INSERT.equals(sqlScript.getConfType())){
				operType = "_" + OperDataTypeConstants.ADD;
			}else if(SqlStatementTypeConstants.UPDATE.equals(sqlScript.getConfType())){
				operType = "_" + OperDataTypeConstants.EDIT;
			}else if(SqlStatementTypeConstants.DELETE.equals(sqlScript.getConfType())){
				operType = "_" + OperDataTypeConstants.DELETE;
			}
			
			IJson ijson = requestBody.getFormData();
			if(operType != null){
				int size = ijson.size();
				JSONObject jsonObject;
				for(int i=0;i<size;i++){
					jsonObject = ijson.get(i);
					if(jsonObject.get(ResourcePropNameConstants.ID) != null){
						jsonObject.put(ResourcePropNameConstants.FOCUSED_OPER, jsonObject.getString(ResourcePropNameConstants.ID) + operType);
					}
				}
			}
			setResponseBody(new ResponseBody(null, ijson.getJson()));
		}
	}
	
	/**
	 * 释放不用的内存
	 */
	protected final void releaseInvalidMemory() {
		// 清除sql语句中的参数值集合
		if(sqlParameterValues.size() > 0){
			for(List<Object> list : sqlParameterValues){
				if(list != null){
					list.clear();
				}
			}
			sqlParameterValues.clear();
		}
		// 清除内置函数处理器的无效数据
		builtinSqlResourceBMProcesser.releaseInvalidMemory();
	}
}
