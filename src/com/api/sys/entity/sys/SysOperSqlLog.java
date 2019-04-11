package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

/**
 * 操作sql日志信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysOperSqlLog extends BasicEntity implements IEntity{
	
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
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(13);
		
		CfgColumn reqLogIdColumn = new CfgColumn("req_log_id", DataTypeConstants.STRING, 32);
		reqLogIdColumn.setName("关联的请求日志主键");
		reqLogIdColumn.setComments("关联的请求日志主键");
		reqLogIdColumn.setOrderCode(1);
		columns.add(reqLogIdColumn);
		
		CfgColumn sqlScriptColumn = new CfgColumn("sql_script", DataTypeConstants.CLOB, 0);
		sqlScriptColumn.setName("操作的sql语句");
		sqlScriptColumn.setComments("操作的sql语句");
		sqlScriptColumn.setOrderCode(2);
		columns.add(sqlScriptColumn);
		
		CfgColumn sqlParamsColumn = new CfgColumn("sql_params", DataTypeConstants.CLOB, 0);
		sqlParamsColumn.setName("对应的参数");
		sqlParamsColumn.setComments("对应的参数");
		sqlParamsColumn.setOrderCode(3);
		columns.add(sqlParamsColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("发出sql的顺序");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("操作sql日志信息表");
		table.setRemark("操作sql日志信息表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_OPER_SQL_LOG";
//		return "SYS_OPER_SQL_LOG" + "_" + SysReqLog.yyyyMM;
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysOperSqlLog";
//		return "SysOperSqlLog" + SysReqLog.yyyyMM;
	}
}
