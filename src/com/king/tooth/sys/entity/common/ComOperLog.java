package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]操作日志资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComOperLog extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 请求的日志信息主键
	 * <p>一次请求中，可能有多个操作</p>
	 */
	private String reqLogId;
	/**
	 * 操作的类型
	 * <p>目前是针对数据库的[增/删/改/查]</p>
	 */
	private String operType;
	/**
	 * 操作的数据
	 * <p>json串</p>
	 */
	private Object operData;
	/**
	 * 操作结果是否成功
	 */
	private int operResultIsSuccess;
	/**
	 * 操作失败的异常信息
	 */
	private String errorMsg;
	
	// ------------------------------------------------
	
	public ComOperLog() {
	}
	public String getReqLogId() {
		return reqLogId;
	}
	public void setReqLogId(String reqLogId) {
		this.reqLogId = reqLogId;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getOperData() {
		return JsonUtil.toJsonString(operData, false);
	}
	public void setOperData(String operData) {
		this.operData = operData;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int getOperResultIsSuccess() {
		return operResultIsSuccess;
	}
	public void setOperResultIsSuccess(int operResultIsSuccess) {
		this.operResultIsSuccess = operResultIsSuccess;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_OPER_LOG", 0);
		table.setName("[通用的]操作日志资源对象表");
		table.setComments("[通用的]操作日志资源对象表");
		table.setIsBuiltin(1);
		table.setPlatformType(ISysResource.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(10);
		
		CfgColumndata reqLogIdColumn = new CfgColumndata("req_log_id");
		reqLogIdColumn.setName("请求的日志信息主键");
		reqLogIdColumn.setComments("请求的日志信息主键：一次请求中，可能有多个操作");
		reqLogIdColumn.setColumnType(DataTypeConstants.STRING);
		reqLogIdColumn.setLength(32);
		reqLogIdColumn.setOrderCode(1);
		columns.add(reqLogIdColumn);
		
		CfgColumndata operTypeColumn = new CfgColumndata("oper_type");
		operTypeColumn.setName("操作的类型");
		operTypeColumn.setComments("操作的类型:目前是针对数据库的[增/删/改/查]");
		operTypeColumn.setColumnType(DataTypeConstants.STRING);
		operTypeColumn.setLength(8);
		operTypeColumn.setOrderCode(2);
		columns.add(operTypeColumn);
		
		CfgColumndata operDataColumn = new CfgColumndata("oper_data");
		operDataColumn.setName("操作的数据");
		operDataColumn.setComments("操作的数据:json串");
		operDataColumn.setColumnType(DataTypeConstants.CLOB);
		operDataColumn.setOrderCode(3);
		columns.add(operDataColumn);
		
		CfgColumndata operResultIsSuccessColumn = new CfgColumndata("oper_result_is_success");
		operResultIsSuccessColumn.setName("操作结果是否成功");
		operResultIsSuccessColumn.setComments("操作结果是否成功");
		operResultIsSuccessColumn.setColumnType(DataTypeConstants.INTEGER);
		operResultIsSuccessColumn.setLength(1);
		operResultIsSuccessColumn.setOrderCode(4);
		columns.add(operResultIsSuccessColumn);
		
		CfgColumndata errorMsgColumn = new CfgColumndata("error_msg");
		errorMsgColumn.setName("操作失败的异常信息");
		errorMsgColumn.setComments("操作失败的异常信息");
		errorMsgColumn.setColumnType(DataTypeConstants.STRING);
		errorMsgColumn.setLength(300);
		errorMsgColumn.setOrderCode(5);
		columns.add(errorMsgColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_OPER_LOG";
	}
	
	public String getEntityName() {
		return "ComOperLog";
	}
	
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("operResultIsSuccess", operResultIsSuccess+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
}
