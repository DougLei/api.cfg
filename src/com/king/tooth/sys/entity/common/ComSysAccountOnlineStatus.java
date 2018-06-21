package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
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
 * 系统账户在线状态资源对象
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
	 * <p>冗余字段，保存登录时的账户名，可以是loginName，可以是tel，也可以是emails，如果有对应的ComUser，则保存realName</p>
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
	private Integer tryLoginTimes;
	/**
	 * 最后一次操作的时间
	 */
	private Date lastOperDate;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 关联的帐号对象
	 */
	@JSONField(serialize = false)
	private ComSysAccount account;
	/**
	 * 登录或验证登录时，传递的信息
	 */
	@JSONField(serialize = false)
	private String message;
	/**
	 * 是否有异常错误
	 * <p>例如，登录失败，账户密码错误等，该属性的值都为1，如果一切正常，则是0</p>
	 */
	@JSONField(serialize = false)
	private int isError;
	/**
	 * 标识这个对象实例，是否要保存，如果不是保存，则是update
	 */
	@JSONField(serialize = false)
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
	public Integer getTryLoginTimes() {
		return tryLoginTimes;
	}
	public String getLoginMac() {
		return loginMac;
	}
	public void setLoginMac(String loginMac) {
		this.loginMac = loginMac;
	}
	public void setTryLoginTimes(Integer tryLoginTimes) {
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
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_SYS_ACCOUNT_ONLINE_STATUS", 0);
		table.setName("系统账户在线状态资源对象表");
		table.setComments("系统账户在线状态资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setReqResourceMethod(ISysResource.NONE);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(13);
		
		ComColumndata accountIdColumn = new ComColumndata("account_id", DataTypeConstants.STRING, 32);
		accountIdColumn.setName("关联的账户主键");
		accountIdColumn.setComments("关联的账户主键");
		accountIdColumn.setOrderCode(1);
		columns.add(accountIdColumn);
		
		ComColumndata accountNameColumn = new ComColumndata("account_name", DataTypeConstants.STRING, 30);
		accountNameColumn.setName("账户名");
		accountNameColumn.setComments("账户名:冗余字段，保存登录时的账户名，可以是loginName，可以是tel，也可以是emails冗余字段，保存登录时的账户名，可以是loginName，可以是tel，也可以是emails，如果有对应的ComUser，则保存realName");
		accountNameColumn.setOrderCode(2);
		columns.add(accountNameColumn);
		
		ComColumndata tokenColumn = new ComColumndata("token", DataTypeConstants.STRING, 32);
		tokenColumn.setName("token值");
		tokenColumn.setComments("token值");
		tokenColumn.setOrderCode(3);
		columns.add(tokenColumn);
		
		ComColumndata loginIpColumn = new ComColumndata("login_ip", DataTypeConstants.STRING, 20);
		loginIpColumn.setName("登录的客户端ip");
		loginIpColumn.setComments("登录的客户端ip");
		loginIpColumn.setOrderCode(4);
		columns.add(loginIpColumn);
		
		ComColumndata loginMacColumn = new ComColumndata("login_mac", DataTypeConstants.STRING, 50);
		loginMacColumn.setName("登录的客户端mac");
		loginMacColumn.setComments("登录的客户端mac");
		loginMacColumn.setOrderCode(5);
		columns.add(loginMacColumn);
		
		ComColumndata loginDateColumn = new ComColumndata("login_date", DataTypeConstants.DATE, 0);
		loginDateColumn.setName("登录的时间");
		loginDateColumn.setComments("登录的时间");
		loginDateColumn.setOrderCode(6);
		columns.add(loginDateColumn);
		
		ComColumndata tryLoginTimesColumn = new ComColumndata("try_login_times", DataTypeConstants.INTEGER, 3);
		tryLoginTimesColumn.setName("本次尝试登录的次数");
		tryLoginTimesColumn.setComments("本次尝试登录的次数");
		tryLoginTimesColumn.setDefaultValue("0");
		tryLoginTimesColumn.setOrderCode(7);
		columns.add(tryLoginTimesColumn);
		
		ComColumndata lastOperDateColumn = new ComColumndata("last_oper_date", DataTypeConstants.DATE, 0);
		lastOperDateColumn.setName("最后一次操作的时间");
		lastOperDateColumn.setComments("最后一次操作的时间");
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

	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("loginDate", loginDate);
		entityJson.put("lastOperDate", lastOperDate);
		entityJson.put("tryLoginTimes", tryLoginTimes);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
