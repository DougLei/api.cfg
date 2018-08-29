package com.king.tooth.sys.entity.sys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;

/**
 * 请求日志信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
public class SysReqLog extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 请求类型
	 * <p>1：login、2：loginOut、3：sql、4：file...</p>
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
	 * 请求的时间
	 */
	private Date reqDate;
	/**
	 * 响应的时间
	 */
	private Date respDate;
	/**
	 * 请求的资源类型
	 */
	private Integer resourceType;
	/**
	 * 请求的资源名
	 */
	private String resourceName;
	/**
	 * 请求的父资源名
	 */
	private String parentResourceName;
	
	// ------------------------------------------------
	
	/**
	 * 操作sql的日志集合
	 */
	@JSONField(serialize = false)
	private List<SysOperSqlLog> operSqlLogs;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
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
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getParentResourceName() {
		return parentResourceName;
	}
	public void setParentResourceName(String parentResourceName) {
		this.parentResourceName = parentResourceName;
	}
	public List<SysOperSqlLog> getOperSqlLogs() {
		return operSqlLogs;
	}
	
	public SysReqLog() {
	}
	public SysReqLog(HttpServletRequest request) {
		this.id = ResourceHandlerUtil.getIdentity();
		this.method = request.getMethod().toLowerCase();
		this.apiAddr = request.getRequestURI();
		this.clientIp = request.getAttribute(BuiltinParameterKeys._CLIENT_IP).toString();
		this.reqDate = new Date();
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_REQ_LOG" + "_" + yyyyMM, 0);
		table.setName("请求日志信息表");
		table.setComments("请求日志信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(19);
		
		ComColumndata typeColumn = new ComColumndata("type", BuiltinCodeDataType.INTEGER, 1);
		typeColumn.setName("请求类型");
		typeColumn.setComments("1：login、2：loginOut、3：sql、4：file...");
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
		
		ComColumndata reqDateColumn = new ComColumndata("req_date", BuiltinCodeDataType.DATE, 0);
		reqDateColumn.setName("响应的时间");
		reqDateColumn.setComments("响应的时间");
		reqDateColumn.setOrderCode(8);
		columns.add(reqDateColumn);
		
		ComColumndata respDateColumn = new ComColumndata("resp_date", BuiltinCodeDataType.DATE, 0);
		respDateColumn.setName("响应的时间");
		respDateColumn.setComments("响应的时间");
		respDateColumn.setOrderCode(9);
		columns.add(respDateColumn);
		
		ComColumndata resourceTypeColumn = new ComColumndata("resource_type", BuiltinCodeDataType.INTEGER, 1);
		resourceTypeColumn.setName("请求的资源类型");
		resourceTypeColumn.setComments("请求的资源类型");
		resourceTypeColumn.setOrderCode(10);
		columns.add(resourceTypeColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", BuiltinCodeDataType.STRING, 60);
		resourceNameColumn.setName("请求的资源名");
		resourceNameColumn.setComments("请求的资源名");
		resourceNameColumn.setOrderCode(11);
		columns.add(resourceNameColumn);
		
		ComColumndata parentResourceNameColumn = new ComColumndata("parent_resource_name", BuiltinCodeDataType.STRING, 60);
		parentResourceNameColumn.setName("请求的父资源名");
		parentResourceNameColumn.setComments("请求的父资源名");
		parentResourceNameColumn.setOrderCode(12);
		columns.add(parentResourceNameColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_REQ_LOG" +"_"+ yyyyMM;
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysReqLog" + yyyyMM;
	}
	
	/**
	 * 添加一条操作sql的日志
	 * @param sqlScript
	 * @param sqlParams
	 */
	public void addOperSqlLog(String sqlScript, Object sqlParams) {
		if(operSqlLogs == null){
			operSqlLogs = new ArrayList<SysOperSqlLog>();
		}
		SysOperSqlLog operSqlLog = new SysOperSqlLog();
		operSqlLog.setReqLogId(this.id);
		operSqlLog.setSqlScript(sqlScript);
		operSqlLog.setSqlParams(JsonUtil.toJsonString(sqlParams, false));
		operSqlLogs.add(operSqlLog);
	}
	
	// ------------------------------------------------------------------------------------
	/**
	 * 格式化日期中的年月
	 */
	private transient static final SimpleDateFormat ymSdf = new SimpleDateFormat("yyyyMM");
	
	/**
	 * 日志表的年月后缀
	 */
	public static String yyyyMM = getYearMonth(new Date());
	
	/**
	 * 获取日期中的年月
	 * @param currentDate
	 * @return
	 */
	public static String getYearMonth(Date currentDate){
		return DateUtil.formatDate(currentDate, ymSdf);
	}
}
