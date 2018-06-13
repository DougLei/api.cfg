package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * 操作日志资源对象
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
	private Integer operResultIsSuccess;
	/**
	 * 操作失败的异常信息
	 */
	private String errorMsg;
	
	// ------------------------------------------------
	
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
	public Integer getOperResultIsSuccess() {
		return operResultIsSuccess;
	}
	public void setOperResultIsSuccess(Integer operResultIsSuccess) {
		this.operResultIsSuccess = operResultIsSuccess;
	}
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_OPER_LOG", 0);
		table.setName("操作日志资源对象表");
		table.setComments("操作日志资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10);
		
		ComColumndata reqLogIdColumn = new ComColumndata("req_log_id", DataTypeConstants.STRING, 32);
		reqLogIdColumn.setName("请求的日志信息主键");
		reqLogIdColumn.setComments("请求的日志信息主键：一次请求中，可能有多个操作");
		reqLogIdColumn.setOrderCode(1);
		columns.add(reqLogIdColumn);
		
		ComColumndata operTypeColumn = new ComColumndata("oper_type", DataTypeConstants.STRING, 10);
		operTypeColumn.setName("操作的类型");
		operTypeColumn.setComments("操作的类型:目前是针对数据库的[增/删/改/查]");
		operTypeColumn.setOrderCode(2);
		columns.add(operTypeColumn);
		
		ComColumndata operDataColumn = new ComColumndata("oper_data", DataTypeConstants.CLOB, 0);
		operDataColumn.setName("操作的数据");
		operDataColumn.setComments("操作的数据:json串");
		operDataColumn.setOrderCode(3);
		columns.add(operDataColumn);
		
		ComColumndata operResultIsSuccessColumn = new ComColumndata("oper_result_is_success", DataTypeConstants.INTEGER, 1);
		operResultIsSuccessColumn.setName("操作结果是否成功");
		operResultIsSuccessColumn.setComments("操作结果是否成功");
		operResultIsSuccessColumn.setOrderCode(4);
		columns.add(operResultIsSuccessColumn);
		
		ComColumndata errorMsgColumn = new ComColumndata("error_msg", DataTypeConstants.STRING, 300);
		errorMsgColumn.setName("操作失败的异常信息");
		errorMsgColumn.setComments("操作失败的异常信息");
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
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("operResultIsSuccess", operResultIsSuccess);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
