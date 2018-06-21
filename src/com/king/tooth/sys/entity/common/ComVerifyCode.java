package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * 验证码资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComVerifyCode extends BasicEntity implements ITable, IEntity{
	
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

	// ---------------------------------------------------------------------------

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
	public String getReqIp() {
		return reqIp;
	}
	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_VERIFY_CODE", 0);
		table.setName("验证码资源对象表");
		table.setComments("验证码资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setReqResourceMethod(ISysResource.GET);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9);
		
		ComColumndata verifyCodeColumn = new ComColumndata("verify_code", DataTypeConstants.STRING, 12);
		verifyCodeColumn.setName("验证码值");
		verifyCodeColumn.setComments("验证码值");
		verifyCodeColumn.setOrderCode(1);
		columns.add(verifyCodeColumn);
		
		ComColumndata reqAccountIdColumn = new ComColumndata("req_account_id", DataTypeConstants.STRING, 32);
		reqAccountIdColumn.setName("请求的账户信息主键");
		reqAccountIdColumn.setComments("请求的账户信息主键");
		reqAccountIdColumn.setOrderCode(2);
		columns.add(reqAccountIdColumn);
		
		ComColumndata reqIpColumn = new ComColumndata("req_ip", DataTypeConstants.STRING, 20);
		reqIpColumn.setName("请求的客户端ip");
		reqIpColumn.setComments("请求的客户端ip");
		reqIpColumn.setOrderCode(3);
		columns.add(reqIpColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_VERIFY_CODE";
	}

	public String getEntityName() {
		return "ComVerifyCode";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
