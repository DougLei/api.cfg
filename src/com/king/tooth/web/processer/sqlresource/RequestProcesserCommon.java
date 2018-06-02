package com.king.tooth.web.processer.sqlresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.hibernate.HibernateUtil;
import com.king.tooth.web.builtin.method.sqlresource.BuiltinSqlResourceBMProcesser;
import com.king.tooth.web.builtin.method.sqlresource.sqlscript.BuiltinSqlScriptMethodProcesser;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.common.CommonProcesser;

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
	protected final List<List<Object>> sqlParameterValues = new ArrayList<List<Object>>(5);
	
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
		Query query = HibernateUtil.getCurrentThreadSession().createSQLQuery(sql.replace(";", ""));
		setQueryCondParamters(index, query);
		
		Log4jUtil.debug("【最后执行的sql语句为：{}】", sql);
		Log4jUtil.debug("【最后执行的sql语句对应的条件值集合为：{}】", sqlParameterValues);
		return query;
	}
	
	/**
	 * 给查询对象query设置各个条件的值
	 * @param index
	 * @param query
	 */
	private void setQueryCondParamters(int index, Query query){
		if(sqlParameterValues.size() > 0){
			List<Object> querySqlParameterValues = sqlParameterValues.get(index-1);
			if(querySqlParameterValues != null && querySqlParameterValues.size() > 0){
				int i = 0;
				for (Object val : querySqlParameterValues) {
					query.setParameter(i++, val);
				}
			}
		}
	}
	
	/**
	 * modify数据后，组装ResponseBody对象
	 * @param sqlDes 传入的sql语句的描述，例如传入"insert"或"update"或"delete"或"添加"或"修改"或"删除"等简短描述  @see SqlStatementType
	 * @param modifyRows 删除的数据行数
	 * @param data
	 */
	protected final void installResponseBodyForModifyData(String sqlDes, int modifyRows, Object data){
		Log4jUtil.debug("{}了{}条数据", sqlDes, modifyRows);
		String message = sqlDes + "了" + modifyRows + "条数据";
		ResponseBody responseBody = new ResponseBody(message, data);
		setResponseBody(responseBody);
	}
	
	/**
	 * 根据ID，查询单个数据对象时，组装ResponseBody对象
	 * @param dataList
	 */
	protected final void installResponseBodyForDataObject(Map<String, Object> data) {
		ResponseBody responseBody = new ResponseBody(data);
		setResponseBody(responseBody);
	}
	
	/**
	 * 执行修改的处理
	 * <pre>
	 * 	包括
	 * 		insert(post)
	 * 		update(put)
	 * 		delete(delete)
	 * </pre>
	 * @param sqlDesc @see SqlStatementType
	 */
	protected final void doModifyProcess(String sqlDesc){
		ComSqlScript sqlScript = builtinSqlScriptMethodProcesser.getSqlScriptResource();
		if(sqlScript.getSqlScriptType().equals(SqlStatementType.PROCEDURE)){// 是存储过程
			Map<String, Object> data = HibernateUtil.executeProcedure(sqlScript.getProcedureName(), sqlScript.getProcedureParameterList());
			installResponseBodyForDataObject(data);
		}else{
			String[] modifySqlArr = sqlScript.getFinalSqlScript().getFinalModifySqlArr();
			int modifyRows = 0;
			int len = modifySqlArr.length;
			Query query;
			for (int i = 0; i < len; i++) {
				query = createQuery((i+1), modifySqlArr[i]);
				modifyRows += query.executeUpdate();
			}
			installResponseBodyForModifyData(sqlDesc, modifyRows, null);
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
