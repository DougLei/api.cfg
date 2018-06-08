package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
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
	
	/**
	 * 构造函数
	 * @param req
	 */
	public ComReqLog() {
	}

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
		table.setIsBuiltin(1);
		table.setPlatformType(ISysResource.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata reqAccountIdColumn = new ComColumndata("req_account_id");
		reqAccountIdColumn.setName("请求的账户信息主键");
		reqAccountIdColumn.setComments("请求的账户信息主键:记录是哪个用户请求的，可为空");
		reqAccountIdColumn.setColumnType(DataTypeConstants.STRING);
		reqAccountIdColumn.setLength(32);
		reqAccountIdColumn.setOrderCode(1);
		columns.add(reqAccountIdColumn);
		
		ComColumndata reqMethodColumn = new ComColumndata("req_method");
		reqMethodColumn.setName("请求的方式");
		reqMethodColumn.setComments("请求的方式");
		reqMethodColumn.setColumnType(DataTypeConstants.STRING);
		reqMethodColumn.setLength(8);
		reqMethodColumn.setOrderCode(2);
		columns.add(reqMethodColumn);
		
		ComColumndata reqIpColumn = new ComColumndata("req_ip");
		reqIpColumn.setName("请求的客户端ip");
		reqIpColumn.setComments("请求的客户端ip");
		reqIpColumn.setColumnType(DataTypeConstants.STRING);
		reqIpColumn.setLength(20);
		reqIpColumn.setOrderCode(3);
		columns.add(reqIpColumn);
		
		ComColumndata reqMacColumn = new ComColumndata("req_mac");
		reqMacColumn.setName("请求的客户端mac");
		reqMacColumn.setComments("请求的客户端mac：可为空");
		reqMacColumn.setColumnType(DataTypeConstants.STRING);
		reqMacColumn.setLength(50);
		reqMacColumn.setOrderCode(4);
		columns.add(reqMacColumn);
		
		ComColumndata reqUrlColumn = new ComColumndata("req_url");
		reqUrlColumn.setName("请求的url");
		reqUrlColumn.setComments("请求的url");
		reqUrlColumn.setColumnType(DataTypeConstants.STRING);
		reqUrlColumn.setLength(150);
		reqUrlColumn.setOrderCode(5);
		columns.add(reqUrlColumn);
		
		ComColumndata reqUrlParamsColumn = new ComColumndata("req_url_params");
		reqUrlParamsColumn.setName("请求的url参数");
		reqUrlParamsColumn.setComments("请求的url参数");
		reqUrlParamsColumn.setColumnType(DataTypeConstants.STRING);
		reqUrlParamsColumn.setLength(1000);
		reqUrlParamsColumn.setOrderCode(6);
		columns.add(reqUrlParamsColumn);
		
		ComColumndata reqBodyColumn = new ComColumndata("req_body");
		reqBodyColumn.setName("请求体");
		reqBodyColumn.setComments("请求体");
		reqBodyColumn.setColumnType(DataTypeConstants.CLOB);
		reqBodyColumn.setOrderCode(7);
		columns.add(reqBodyColumn);
		
		ComColumndata respBodyColumn = new ComColumndata("resp_body");
		respBodyColumn.setName("响应体");
		respBodyColumn.setComments("响应体");
		respBodyColumn.setColumnType(DataTypeConstants.CLOB);
		respBodyColumn.setOrderCode(8);
		columns.add(respBodyColumn);
		
		ComColumndata reqDateColumn = new ComColumndata("req_date");
		reqDateColumn.setName("发起请求的时间");
		reqDateColumn.setComments("发起请求的时间");
		reqDateColumn.setColumnType(DataTypeConstants.DATE);
		reqDateColumn.setOrderCode(9);
		columns.add(reqDateColumn);
		
		ComColumndata respDateColumn = new ComColumndata("resp_date");
		respDateColumn.setName("完成响应的时间");
		respDateColumn.setComments("完成响应的时间");
		respDateColumn.setColumnType(DataTypeConstants.DATE);
		respDateColumn.setOrderCode(10);
		columns.add(respDateColumn);
		
		ComColumndata reqTokenColumn = new ComColumndata("req_token");
		reqTokenColumn.setName("请求携带的token值");
		reqTokenColumn.setComments("请求携带的token值：可为空");
		reqTokenColumn.setColumnType(DataTypeConstants.STRING);
		reqTokenColumn.setLength(32);
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
	
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("reqDate", reqDate);
		json.put("respDate", reqDate);
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
}
