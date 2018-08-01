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
 * 操作日志信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysOperLog extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 操作的类型
	 * <p>操作的类型：login/sql</p>
	 */
	private String operType;
	/**
	 * 操作的sql语句
	 */
	private String sql;
	/**
	 * 对应的参数json串
	 */
	private String paramJson;
	/**
	 * 操作结果是否成功
	 */
	private Integer operResult;
	/**
	 * 操作失败的异常信息
	 */
	private String errorMsg;
	
	// ------------------------------------------------
	
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getParamJson() {
		return paramJson;
	}
	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}
	public Integer getOperResult() {
		return operResult;
	}
	public void setOperResult(Integer operResult) {
		this.operResult = operResult;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_OPER_LOG", 0);
		table.setName("操作日志信息表");
		table.setComments("操作日志信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(12);
		
		ComColumndata operTypeColumn = new ComColumndata("oper_type", BuiltinCodeDataType.STRING, 10);
		operTypeColumn.setName("操作的类型");
		operTypeColumn.setComments("操作的类型:login/sql");
		operTypeColumn.setDefaultValue(sqlOperType);
		operTypeColumn.setOrderCode(1);
		columns.add(operTypeColumn);
		
		ComColumndata sqlColumn = new ComColumndata("sql", BuiltinCodeDataType.CLOB, 0);
		sqlColumn.setName("操作的sql语句");
		sqlColumn.setComments("操作的sql语句");
		sqlColumn.setOrderCode(2);
		columns.add(sqlColumn);
		
		ComColumndata paramJsonColumn = new ComColumndata("param_json", BuiltinCodeDataType.STRING, 9999);
		paramJsonColumn.setName("对应的参数json串");
		paramJsonColumn.setComments("对应的参数json串");
		paramJsonColumn.setOrderCode(3);
		columns.add(paramJsonColumn);
		
		ComColumndata operResultColumn = new ComColumndata("oper_result", BuiltinCodeDataType.INTEGER, 1);
		operResultColumn.setName("操作结果");
		operResultColumn.setComments("操作结果是否成功");
		operResultColumn.setOrderCode(4);
		columns.add(operResultColumn);
		
		ComColumndata errorMsgColumn = new ComColumndata("error_msg", BuiltinCodeDataType.STRING, 1000);
		errorMsgColumn.setName("操作失败的异常信息");
		errorMsgColumn.setComments("操作失败的异常信息");
		errorMsgColumn.setOrderCode(5);
		columns.add(errorMsgColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_OPER_LOG";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysOperLog";
	}
	
	/**
	 * 操作类型：login
	 */
	public static final String loginOperType = "login";
	/**
	 * 操作类型：sql
	 */
	public static final String sqlOperType = "sql";
}
