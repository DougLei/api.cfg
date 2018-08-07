package com.king.tooth.web.processer.sqlresource;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.sqlresource.BuiltinSqlResourceBMProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sqlscript.BuiltinSqlScriptMethodProcesser;
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
	protected final List<List<Object>> sqlParameterValues = new ArrayList<List<Object>>();
	
	/**
	 * 内置sql脚本处理器
	 */
	protected BuiltinSqlScriptMethodProcesser builtinSqlScriptMethodProcesser;
	
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
		ComSqlScript sqlScript = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		
		if(sqlScript.getSqlScriptType().equals(BuiltinDatabaseData.PROCEDURE)){// 是存储过程
			JSONObject json = HibernateUtil.executeProcedure(sqlScript.getDbType(), sqlScript.getProcedureName(), sqlScript.getSqlScriptParameterList());
			setResponseBody(new ResponseBody(json, true));
			return;
		}
		
		String[] modifySqlArr = sqlScript.getFinalSqlScript().getFinalModifySqlArr();
		int len = modifySqlArr.length;
		Query query;
		for (int i = 0; i < len; i++) {
			query = createQuery(i, modifySqlArr[i].replace(";", ""));
			query.executeUpdate();
		}
		
		if(requestBody.getFormData() == null || requestBody.getFormData().size() == 0){
			setResponseBody(new ResponseBody(null, true));
		}else{
			setResponseBody(new ResponseBody(requestBody.getFormData().get(0), true));
		}
	}
	
	/**
	 * 释放不用的内存
	 */
	protected final void releaseInvalidMemory() {
		// 清除sql语句中的参数值集合
		if(sqlParameterValues.size() > 0){
			for(List<Object> list : sqlParameterValues){
				if(list.size() > 0){
					list.clear();
				}
			}
			sqlParameterValues.clear();
		}
		// 清除内置函数处理器的无效数据
		builtinSqlResourceBMProcesser.releaseInvalidMemory();
	}
}
