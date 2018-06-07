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
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]系统账户在线状态资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSysAccountOnlineStatus extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 关联的账户主键
	 */
	private String accountId;
	/**
	 * 账户名
	 * <p>冗余字段，保存登录时的账户名，可以是loginName，可以是tel，也可以是emails</p>
	 */
	private String accountName;
	/**
	 * token值
	 */
	private String token;
	/**
	 * 登录的客户端ip
	 */
	private String loginIp;
	/**
	 * 登录的客户端mac
	 */
	private String loginMac;
	/**
	 * 登录的时间
	 */
	private Date loginDate;
	/**
	 * 本次尝试登录的次数
	 */
	private int tryLoginTimes;
	/**
	 * 最后一次操作的时间
	 */
	private Date lastOperDate;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 关联的帐号对象
	 */
	private ComSysAccount account;
	/**
	 * 登录或验证登录时，传递的信息
	 */
	private String message;
	/**
	 * 是否有异常错误
	 * <p>例如，登录失败，账户密码错误等，该属性的值都为1，如果一切正常，则是0</p>
	 */
	private int isError;
	/**
	 * 标识这个对象实例，是否要保存，如果不是保存，则是update
	 */
	private boolean isSave;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public ComSysAccount getAccount() {
		return account;
	}
	public void setAccount(ComSysAccount account) {
		this.account = account;
	}
	public String getToken() {
		return token;
	}
	public boolean getIsSave() {
		return isSave;
	}
	public void setIsSave(boolean isSave) {
		this.isSave = isSave;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public int getTryLoginTimes() {
		return tryLoginTimes;
	}
	public String getLoginMac() {
		return loginMac;
	}
	public void setLoginMac(String loginMac) {
		this.loginMac = loginMac;
	}
	public void setTryLoginTimes(int tryLoginTimes) {
		this.tryLoginTimes = tryLoginTimes;
	}
	public int getIsError() {
		return isError;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public void setIsError(int isError) {
		this.isError = isError;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public Date getLastOperDate() {
		return lastOperDate;
	}
	public void setLastOperDate(Date lastOperDate) {
		this.lastOperDate = lastOperDate;
	}
	
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_SYS_ACCOUNT_ONLINE_STATUS");
		table.setName("[通用的]系统账户在线状态资源对象表");
		table.setComments("[通用的]系统账户在线状态资源对象表");
		table.setIsBuiltin(1);
		table.setPlatformType(ISysResource.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(14);
		
		CfgColumndata accountIdColumn = new CfgColumndata("account_id");
		accountIdColumn.setName("关联的账户主键");
		accountIdColumn.setComments("关联的账户主键");
		accountIdColumn.setColumnType(DataTypeConstants.STRING);
		accountIdColumn.setLength(32);
		accountIdColumn.setOrderCode(1);
		columns.add(accountIdColumn);
		
		CfgColumndata accountNameColumn = new CfgColumndata("account_name");
		accountNameColumn.setName("账户名");
		accountNameColumn.setComments("账户名:冗余字段，保存登录时的账户名，可以是loginName，可以是tel，也可以是emails");
		accountNameColumn.setColumnType(DataTypeConstants.STRING);
		accountNameColumn.setLength(50);
		accountNameColumn.setOrderCode(2);
		columns.add(accountNameColumn);
		
		CfgColumndata tokenColumn = new CfgColumndata("token");
		tokenColumn.setName("token值");
		tokenColumn.setComments("token值");
		tokenColumn.setColumnType(DataTypeConstants.STRING);
		tokenColumn.setLength(32);
		tokenColumn.setOrderCode(3);
		columns.add(tokenColumn);
		
		CfgColumndata loginIpColumn = new CfgColumndata("login_ip");
		loginIpColumn.setName("登录的客户端ip");
		loginIpColumn.setComments("登录的客户端ip");
		loginIpColumn.setColumnType(DataTypeConstants.STRING);
		loginIpColumn.setLength(20);
		loginIpColumn.setOrderCode(4);
		columns.add(loginIpColumn);
		
		CfgColumndata loginMacColumn = new CfgColumndata("login_mac");
		loginMacColumn.setName("登录的客户端mac");
		loginMacColumn.setComments("登录的客户端mac");
		loginMacColumn.setColumnType(DataTypeConstants.STRING);
		loginMacColumn.setLength(50);
		loginMacColumn.setOrderCode(5);
		columns.add(loginMacColumn);
		
		CfgColumndata loginDateColumn = new CfgColumndata("login_date");
		loginDateColumn.setName("登录的时间");
		loginDateColumn.setComments("登录的时间");
		loginDateColumn.setColumnType(DataTypeConstants.DATE);
		loginDateColumn.setOrderCode(6);
		columns.add(loginDateColumn);
		
		CfgColumndata tryLoginTimesColumn = new CfgColumndata("try_login_times");
		tryLoginTimesColumn.setName("本次尝试登录的次数");
		tryLoginTimesColumn.setComments("本次尝试登录的次数");
		tryLoginTimesColumn.setColumnType(DataTypeConstants.INTEGER);
		tryLoginTimesColumn.setLength(3);
		tryLoginTimesColumn.setOrderCode(7);
		columns.add(tryLoginTimesColumn);
		
		CfgColumndata lastOperDateColumn = new CfgColumndata("last_oper_date");
		lastOperDateColumn.setName("最后一次操作的时间");
		lastOperDateColumn.setComments("最后一次操作的时间");
		lastOperDateColumn.setColumnType(DataTypeConstants.DATE);
		lastOperDateColumn.setOrderCode(8);
		columns.add(lastOperDateColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_ACCOUNT_ONLINE_STATUS";
	}
	
	public String getEntityName() {
		return "ComSysAccountOnlineStatus";
	}

	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("loginDate", loginDate);
		json.put("lastOperDate", lastOperDate);
		json.put("tryLoginTimes", tryLoginTimes+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
}
