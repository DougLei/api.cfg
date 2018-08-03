package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 操作sql日志信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysOperSqlLog extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 关联的请求日志主键
	 */
	private String reqLogId;
	/**
	 * 操作的sql语句
	 */
	private String sqlScript;
	/**
	 * 对应的参数
	 */
	private String sqlParams;
	/**
	 * 排序
	 * <p>发出sql的顺序</p>
	 */
	private Integer orderCode;
	
	// ------------------------------------------------

	public String getReqLogId() {
		return reqLogId;
	}
	public void setReqLogId(String reqLogId) {
		this.reqLogId = reqLogId;
	}
	public String getSqlScript() {
		return sqlScript;
	}
	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}
	public String getSqlParams() {
		return sqlParams;
	}
	public void setSqlParams(String sqlParams) {
		this.sqlParams = sqlParams;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_OPER_SQL_LOG", 0);
		table.setName("操作sql日志信息表");
		table.setComments("操作sql日志信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(13);
		
		ComColumndata reqLogIdColumn = new ComColumndata("req_log_id", BuiltinCodeDataType.STRING, 32);
		reqLogIdColumn.setName("关联的请求日志主键");
		reqLogIdColumn.setComments("关联的请求日志主键");
		reqLogIdColumn.setOrderCode(1);
		columns.add(reqLogIdColumn);
		
		ComColumndata sqlScriptColumn = new ComColumndata("sql_script", BuiltinCodeDataType.STRING, 2000);
		sqlScriptColumn.setName("操作的sql语句");
		sqlScriptColumn.setComments("操作的sql语句");
		sqlScriptColumn.setOrderCode(2);
		columns.add(sqlScriptColumn);
		
		ComColumndata sqlParamsColumn = new ComColumndata("sql_params", BuiltinCodeDataType.STRING, 1200);
		sqlParamsColumn.setName("对应的参数");
		sqlParamsColumn.setComments("对应的参数");
		sqlParamsColumn.setOrderCode(3);
		columns.add(sqlParamsColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 2);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("发出sql的顺序");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_OPER_SQL_LOG";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysOperSqlLog";
	}
}
