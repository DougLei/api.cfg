package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;

/**
 * 请求日志资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComReqLog extends BasicEntity implements ITable, IEntity{
	/**
	 * 请求的账户信息主键
	 * <p>记录是哪个用户请求的，可为空</p>
	 */
	private String reqAccountId;
	/**
	 * 请求的方式
	 */
	private String reqMethod;
	/**
	 * 请求的客户端ip
	 */
	private String reqIp;
	/**
	 * 请求的客户端mac
	 * <p>可为空</p>
	 */
	private String reqMac;
	/**
	 * 请求的url
	 */
	private String reqUrl;
	/**
	 * 请求的url参数
	 */
	private String reqUrlParams;
	/**
	 * 请求体
	 */
	private String reqBody;
	/**
	 * 响应体
	 */
	private String respBody; 
	/**
	 * 发起请求的时间
	 */
	private Date reqDate; 
	/**
	 * 完成响应的时间
	 */
	private Date respDate; 
	/**
	 * 请求携带的token值
	 * <p>可为空</p>
	 */
	private String reqToken;
	
	//-------------------------------------------------------------------------
	
	public String getReqAccountId() {
		return reqAccountId;
	}
	public void setReqAccountId(String reqAccountId) {
		this.reqAccountId = reqAccountId;
	}
	public String getReqMethod() {
		return reqMethod;
	}
	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}
	public String getReqIp() {
		return reqIp;
	}
	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}
	public String getReqUrlParams() {
		return reqUrlParams;
	}
	public void setReqUrlParams(String reqUrlParams) {
		this.reqUrlParams = reqUrlParams;
	}
	public String getReqUrl() {
		return reqUrl;
	}
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	public String getReqBody() {
		return reqBody;
	}
	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}
	public String getRespBody() {
		return respBody;
	}
	public void setRespBody(String respBody) {
		this.respBody = respBody;
	}
	public Date getReqDate() {
		return reqDate;
	}
	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}
	public Date getRespDate() {
		return respDate;
	}
	public void setRespDate(Date respDate) {
		this.respDate = respDate;
	}
	public String getReqMac() {
		return reqMac;
	}
	public void setReqMac(String reqMac) {
		this.reqMac = reqMac;
	}
	public String getReqToken() {
		return reqToken;
	}
	public void setReqToken(String reqToken) {
		this.reqToken = reqToken;
	}
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_REQ_LOG", 0);
		table.setName("请求日志资源对象表");
		table.setComments("请求日志资源对象表");
		table.setVersion(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata reqAccountIdColumn = new ComColumndata("req_account_id", DataTypeConstants.STRING, 32);
		reqAccountIdColumn.setName("请求的账户信息主键");
		reqAccountIdColumn.setComments("请求的账户信息主键:记录是哪个用户请求的，可为空");
		reqAccountIdColumn.setOrderCode(1);
		columns.add(reqAccountIdColumn);
		
		ComColumndata reqMethodColumn = new ComColumndata("req_method", DataTypeConstants.STRING, 10);
		reqMethodColumn.setName("请求的方式");
		reqMethodColumn.setComments("请求的方式");
		reqMethodColumn.setOrderCode(2);
		columns.add(reqMethodColumn);
		
		ComColumndata reqIpColumn = new ComColumndata("req_ip", DataTypeConstants.STRING, 20);
		reqIpColumn.setName("请求的客户端ip");
		reqIpColumn.setComments("请求的客户端ip");
		reqIpColumn.setOrderCode(3);
		columns.add(reqIpColumn);
		
		ComColumndata reqMacColumn = new ComColumndata("req_mac", DataTypeConstants.STRING, 50);
		reqMacColumn.setName("请求的客户端mac");
		reqMacColumn.setComments("请求的客户端mac：可为空");
		reqMacColumn.setOrderCode(4);
		columns.add(reqMacColumn);
		
		ComColumndata reqUrlColumn = new ComColumndata("req_url", DataTypeConstants.STRING, 160);
		reqUrlColumn.setName("请求的url");
		reqUrlColumn.setComments("请求的url");
		reqUrlColumn.setOrderCode(5);
		columns.add(reqUrlColumn);
		
		ComColumndata reqUrlParamsColumn = new ComColumndata("req_url_params", DataTypeConstants.STRING, 1000);
		reqUrlParamsColumn.setName("请求的url参数");
		reqUrlParamsColumn.setComments("请求的url参数");
		reqUrlParamsColumn.setOrderCode(6);
		columns.add(reqUrlParamsColumn);
		
		ComColumndata reqBodyColumn = new ComColumndata("req_body", DataTypeConstants.CLOB, 0);
		reqBodyColumn.setName("请求体");
		reqBodyColumn.setComments("请求体");
		reqBodyColumn.setOrderCode(7);
		columns.add(reqBodyColumn);
		
		ComColumndata respBodyColumn = new ComColumndata("resp_body", DataTypeConstants.CLOB, 0);
		respBodyColumn.setName("响应体");
		respBodyColumn.setComments("响应体");
		respBodyColumn.setOrderCode(8);
		columns.add(respBodyColumn);
		
		ComColumndata reqDateColumn = new ComColumndata("req_date", DataTypeConstants.DATE, 32);
		reqDateColumn.setName("发起请求的时间");
		reqDateColumn.setComments("发起请求的时间");
		reqDateColumn.setOrderCode(9);
		columns.add(reqDateColumn);
		
		ComColumndata respDateColumn = new ComColumndata("resp_date", DataTypeConstants.DATE, 32);
		respDateColumn.setName("完成响应的时间");
		respDateColumn.setComments("完成响应的时间");
		respDateColumn.setOrderCode(10);
		columns.add(respDateColumn);
		
		ComColumndata reqTokenColumn = new ComColumndata("req_token", DataTypeConstants.STRING, 32);
		reqTokenColumn.setName("请求携带的token值");
		reqTokenColumn.setComments("请求携带的token值：可为空");
		reqTokenColumn.setOrderCode(11);
		columns.add(reqTokenColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_REQ_LOG";
	}
	
	public String getEntityName() {
		return "ComReqLog";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("reqDate", reqDate);
		entityJson.put("respDate", reqDate);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
}
