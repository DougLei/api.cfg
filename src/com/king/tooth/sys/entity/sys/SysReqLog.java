package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
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
 * 请求日志信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysReqLog extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 请求类型
	 * <p>1：login、2：loginOut、3：sql</p>
	 */
	private Integer type;
	/**
	 * 请求方式
	 * <p>get/post/delete/update</p>
	 */
	private String method;
	/**
	 * 请求的接口地址
	 */
	private String apiAddr;
	/**
	 * 请求的客户端ip
	 */
	private String clientIp;
	/**
	 * 请求的客户端mac
	 */
	private String clientMac;
	/**
	 * 请求的数据
	 */
	private String reqData;
	/**
	 * 响应的数据
	 */
	private String respData;
	/**
	 * 响应的时间
	 */
	private Date respDate;
	/**
	 * 是否成功
	 */
	private Integer isSuccess;
	/**
	 * 错误信息
	 * <p>如果失败，则记录错误信息</p>
	 */
	private String errMsg;
	
	// ------------------------------------------------
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getApiAddr() {
		return apiAddr;
	}
	public void setApiAddr(String apiAddr) {
		this.apiAddr = apiAddr;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getClientMac() {
		return clientMac;
	}
	public void setClientMac(String clientMac) {
		this.clientMac = clientMac;
	}
	public String getReqData() {
		return reqData;
	}
	public void setReqData(String reqData) {
		this.reqData = reqData;
	}
	public String getRespData() {
		return respData;
	}
	public void setRespData(String respData) {
		this.respData = respData;
	}
	public Date getRespDate() {
		return respDate;
	}
	public void setRespDate(Date respDate) {
		this.respDate = respDate;
	}
	public Integer getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_REQ_LOG", 0);
		table.setName("请求日志信息表");
		table.setComments("请求日志信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata typeColumn = new ComColumndata("type", BuiltinCodeDataType.INTEGER, 1);
		typeColumn.setName("请求类型");
		typeColumn.setComments("1：login、2：loginOut、3：sql");
		typeColumn.setDefaultValue("3");
		typeColumn.setOrderCode(1);
		columns.add(typeColumn);
		
		ComColumndata methodColumn = new ComColumndata("method", BuiltinCodeDataType.STRING, 8);
		methodColumn.setName("请求方式");
		methodColumn.setComments("get/post/delete/update");
		methodColumn.setOrderCode(2);
		columns.add(methodColumn);
		
		ComColumndata apiAddrColumn = new ComColumndata("api_addr", BuiltinCodeDataType.STRING, 300);
		apiAddrColumn.setName("请求的接口地址");
		apiAddrColumn.setComments("请求的接口地址");
		apiAddrColumn.setOrderCode(3);
		columns.add(apiAddrColumn);
		
		ComColumndata clientIpColumn = new ComColumndata("client_ip", BuiltinCodeDataType.STRING, 20);
		clientIpColumn.setName("请求的客户端ip");
		clientIpColumn.setComments("请求的客户端ip");
		clientIpColumn.setOrderCode(4);
		columns.add(clientIpColumn);
		
		ComColumndata clientMacColumn = new ComColumndata("client_mac", BuiltinCodeDataType.STRING, 50);
		clientMacColumn.setName("请求的客户端mac");
		clientMacColumn.setComments("请求的客户端max");
		clientMacColumn.setOrderCode(5);
		columns.add(clientMacColumn);
		
		ComColumndata reqDataColumn = new ComColumndata("req_data", BuiltinCodeDataType.CLOB, 0);
		reqDataColumn.setName("请求的数据");
		reqDataColumn.setComments("请求的数据");
		reqDataColumn.setOrderCode(6);
		columns.add(reqDataColumn);
		
		ComColumndata respDataColumn = new ComColumndata("resp_data", BuiltinCodeDataType.CLOB, 0);
		respDataColumn.setName("响应的数据");
		respDataColumn.setComments("响应的数据");
		respDataColumn.setOrderCode(7);
		columns.add(respDataColumn);
		
		ComColumndata respDateColumn = new ComColumndata("resp_date", BuiltinCodeDataType.DATE, 0);
		respDateColumn.setName("响应的时间");
		respDateColumn.setComments("响应的时间");
		respDateColumn.setOrderCode(8);
		columns.add(respDateColumn);
		
		ComColumndata isSuccessColumn = new ComColumndata("is_success", BuiltinCodeDataType.INTEGER, 1);
		isSuccessColumn.setName("是否成功");
		isSuccessColumn.setComments("是否成功");
		isSuccessColumn.setOrderCode(9);
		columns.add(isSuccessColumn);
		
		ComColumndata errMsgColumn = new ComColumndata("err_msg", BuiltinCodeDataType.STRING, 600);
		errMsgColumn.setName("错误信息");
		errMsgColumn.setComments("如果失败，则记录错误信息");
		errMsgColumn.setOrderCode(10);
		columns.add(errMsgColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_REQ_LOG";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysReqLog";
	}
}
