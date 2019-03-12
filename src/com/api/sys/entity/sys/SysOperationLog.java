package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

/**
 * 操作日志信息表
 * <p>前端提出的操作日志表</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysOperationLog extends BasicEntity implements IEntity{
	
	private String categoryId;
	private String eventId;
	private String eventResult;
	private String funcId;
	private String description;
	private String instanceId;
	private Date loginDate;
	private String userId;
	private String userIp;
	
	// ------------------------------------------------

	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getEventResult() {
		return eventResult;
	}
	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn CATEGORY_IDColumn = new CfgColumn("CATEGORY_ID", DataTypeConstants.STRING, 50);
		CATEGORY_IDColumn.setName("CATEGORY_ID");
		CATEGORY_IDColumn.setComments("CATEGORY_ID");
		columns.add(CATEGORY_IDColumn);
		
		CfgColumn EVENT_IDColumn = new CfgColumn("EVENT_ID", DataTypeConstants.STRING, 50);
		EVENT_IDColumn.setName("EVENT_ID");
		EVENT_IDColumn.setComments("EVENT_ID");
		columns.add(EVENT_IDColumn);
		
		CfgColumn EVENT_RESULTColumn = new CfgColumn("EVENT_RESULT", DataTypeConstants.STRING, 500);
		EVENT_RESULTColumn.setName("EVENT_RESULT");
		EVENT_RESULTColumn.setComments("EVENT_RESULT");
		columns.add(EVENT_RESULTColumn);
		
		CfgColumn FUNC_IDColumn = new CfgColumn("FUNC_ID", DataTypeConstants.STRING, 50);
		FUNC_IDColumn.setName("FUNC_ID");
		FUNC_IDColumn.setComments("FUNC_ID");
		columns.add(FUNC_IDColumn);
		
		CfgColumn DESCRIPTIONColumn = new CfgColumn("DESCRIPTION", DataTypeConstants.STRING, 500);
		DESCRIPTIONColumn.setName("DESCRIPTION");
		DESCRIPTIONColumn.setComments("DESCRIPTION");
		columns.add(DESCRIPTIONColumn);
		
		CfgColumn INSTANCE_IDColumn = new CfgColumn("INSTANCE_ID", DataTypeConstants.STRING, 50);
		INSTANCE_IDColumn.setName("INSTANCE_ID");
		INSTANCE_IDColumn.setComments("INSTANCE_ID");
		columns.add(INSTANCE_IDColumn);
		
		CfgColumn LOGIN_DATEColumn = new CfgColumn("LOGIN_DATE", DataTypeConstants.DATE, 0);
		LOGIN_DATEColumn.setName("LOGIN_DATE");
		LOGIN_DATEColumn.setComments("LOGIN_DATE");
		columns.add(LOGIN_DATEColumn);
		
		CfgColumn USER_IDColumn = new CfgColumn("USER_ID", DataTypeConstants.STRING, 32);
		USER_IDColumn.setName("USER_ID");
		USER_IDColumn.setComments("USER_ID");
		columns.add(USER_IDColumn);
		
		CfgColumn USER_IPColumn = new CfgColumn("USER_IP", DataTypeConstants.STRING, 13);
		USER_IPColumn.setName("USER_IP");
		USER_IPColumn.setComments("USER_IP");
		columns.add(USER_IPColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("操作日志信息表");
		table.setRemark("前端提出的操作日志表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_OPERATION_LOG" + "_" + SysReqLog.yyyyMM;
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysOperationLog" + SysReqLog.yyyyMM;
	}
}
