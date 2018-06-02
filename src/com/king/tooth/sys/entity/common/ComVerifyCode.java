package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]验证码资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComVerifyCode extends BasicEntity implements ITable{
	
	/**
	 * 验证码值
	 */
	private String verifyCode;
	/**
	 * 请求的账户信息主键
	 */
	private String reqAccountId;
	/**
	 * 请求的客户端ip
	 */
	private String reqIp;
	
	//-------------------------------------------------------------------------
	
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getReqAccountId() {
		return reqAccountId;
	}
	public void setReqAccountId(String reqAccountId) {
		this.reqAccountId = reqAccountId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public String getReqIp() {
		return reqIp;
	}
	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}

	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_VERIFY_CODE");
		table.setName("[通用的]验证码资源对象表");
		table.setComments("[通用的]验证码资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(9);
		
		CfgColumndata verifyCodeColumn = new CfgColumndata("verify_code");
		verifyCodeColumn.setName("验证码值");
		verifyCodeColumn.setComments("验证码值");
		verifyCodeColumn.setColumnType(DataTypeConstants.STRING);
		verifyCodeColumn.setLength(8);
		verifyCodeColumn.setOrderCode(1);
		columns.add(verifyCodeColumn);
		
		CfgColumndata reqAccountIdColumn = new CfgColumndata("req_account_id");
		reqAccountIdColumn.setName("请求的账户信息主键");
		reqAccountIdColumn.setComments("请求的账户信息主键");
		reqAccountIdColumn.setColumnType(DataTypeConstants.STRING);
		reqAccountIdColumn.setLength(32);
		reqAccountIdColumn.setOrderCode(2);
		columns.add(reqAccountIdColumn);
		
		CfgColumndata reqIpColumn = new CfgColumndata("req_ip");
		reqIpColumn.setName("请求的客户端ip");
		reqIpColumn.setComments("请求的客户端ip");
		reqIpColumn.setColumnType(DataTypeConstants.STRING);
		reqIpColumn.setLength(20);
		reqIpColumn.setOrderCode(3);
		columns.add(reqIpColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_VERIFY_CODE";
	}
}
