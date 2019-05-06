package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;
import com.api.util.JsonUtil;
import com.api.util.ResourceHandlerUtil;

/**
 * 请求日志信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysReqLog extends BasicEntity implements IEntity{
	
	/**
	 * 请求类型
	 * <p>-2：退出,-1：登录,1：表资源,2：sql资源,3：代码资源,4：业务模型资源,5：文件操作</p>
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

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(19);
		
		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.INTEGER, 1);
		typeColumn.setName("请求类型");
		typeColumn.setComments("-2：退出,-1：登录,1：表资源,2：sql资源,3：代码资源,4：业务模型资源,5：文件操作");
		typeColumn.setDefaultValue("3");
		typeColumn.setOrderCode(1);
		columns.add(typeColumn);
		
		CfgColumn methodColumn = new CfgColumn("method", DataTypeConstants.STRING, 8);
		methodColumn.setName("请求方式");
		methodColumn.setComments("get/post/delete/update");
		methodColumn.setOrderCode(2);
		columns.add(methodColumn);
		
		CfgColumn apiAddrColumn = new CfgColumn("api_addr", DataTypeConstants.STRING, 500);
		apiAddrColumn.setName("请求的接口地址");
		apiAddrColumn.setComments("请求的接口地址");
		apiAddrColumn.setOrderCode(3);
		columns.add(apiAddrColumn);
		
		CfgColumn clientIpColumn = new CfgColumn("client_ip", DataTypeConstants.STRING, 20);
		clientIpColumn.setName("请求的客户端ip");
		clientIpColumn.setComments("请求的客户端ip");
		clientIpColumn.setOrderCode(4);
		columns.add(clientIpColumn);
		
		CfgColumn clientMacColumn = new CfgColumn("client_mac", DataTypeConstants.STRING, 50);
		clientMacColumn.setName("请求的客户端mac");
		clientMacColumn.setComments("请求的客户端max");
		clientMacColumn.setOrderCode(5);
		columns.add(clientMacColumn);
		
		CfgColumn reqDataColumn = new CfgColumn("req_data", DataTypeConstants.CLOB, 0);
		reqDataColumn.setName("请求的数据");
		reqDataColumn.setComments("请求的数据");
		reqDataColumn.setOrderCode(6);
		columns.add(reqDataColumn);
		
		CfgColumn respDataColumn = new CfgColumn("resp_data", DataTypeConstants.CLOB, 0);
		respDataColumn.setName("响应的数据");
		respDataColumn.setComments("响应的数据");
		respDataColumn.setOrderCode(7);
		columns.add(respDataColumn);
		
		CfgColumn reqDateColumn = new CfgColumn("req_date", DataTypeConstants.DATE, 0);
		reqDateColumn.setName("响应的时间");
		reqDateColumn.setComments("响应的时间");
		reqDateColumn.setOrderCode(8);
		columns.add(reqDateColumn);
		
		CfgColumn respDateColumn = new CfgColumn("resp_date", DataTypeConstants.DATE, 0);
		respDateColumn.setName("响应的时间");
		respDateColumn.setComments("响应的时间");
		respDateColumn.setOrderCode(9);
		columns.add(respDateColumn);
		
		CfgColumn resourceTypeColumn = new CfgColumn("resource_type", DataTypeConstants.INTEGER, 1);
		resourceTypeColumn.setName("请求的资源类型");
		resourceTypeColumn.setComments("请求的资源类型");
		resourceTypeColumn.setOrderCode(10);
		columns.add(resourceTypeColumn);
		
		CfgColumn resourceNameColumn = new CfgColumn("resource_name", DataTypeConstants.STRING, 60);
		resourceNameColumn.setName("请求的资源名");
		resourceNameColumn.setComments("请求的资源名");
		resourceNameColumn.setOrderCode(11);
		columns.add(resourceNameColumn);
		
		CfgColumn parentResourceNameColumn = new CfgColumn("parent_resource_name", DataTypeConstants.STRING, 60);
		parentResourceNameColumn.setName("请求的父资源名");
		parentResourceNameColumn.setComments("请求的父资源名");
		parentResourceNameColumn.setOrderCode(12);
		columns.add(parentResourceNameColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("请求日志信息表");
		table.setRemark("请求日志信息表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_REQ_LOG";
//		return "SYS_REQ_LOG" +"_"+ yyyyMM;
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysReqLog";
//		return "SysReqLog" + yyyyMM;
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
		operSqlLog.setReqLogId(id);
		operSqlLog.setSqlScript(sqlScript);
		operSqlLog.setSqlParams(JsonUtil.toJsonString(sqlParams, false));
		operSqlLogs.add(operSqlLog);
	}
	
	// ------------------------------------------------------------------------------------
//	/**
//	 * 格式化日期中的年月
//	 */
//	private transient static final SimpleDateFormat ymSdf = new SimpleDateFormat("yyyyMM");
	
//	/**
//	 * 日志表的年月后缀
//	 */
//	public static String yyyyMM = getYearMonth(new Date());
	
//	/**
//	 * 获取日期中的年月
//	 * @param currentDate
//	 * @return
//	 */
//	public static String getYearMonth(Date currentDate){
//		return DateUtil.formatDate(currentDate, ymSdf);
//	}
	
	public static final Integer LOGIN = -1;
	public static final Integer LOGIN_OUT = -2;
	public static final Integer FILE = 5;
	public static final Integer TOOLS = 6;
}
