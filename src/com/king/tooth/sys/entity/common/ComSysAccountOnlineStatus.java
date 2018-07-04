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
	 * 当前租户id
	 */
	private String currentCustomerId;
	/**
	 * 当前的项目id
	 */
	private String currentProjectId;
	/**
	 * 当前的账户id
	 */
	private String accountId;
	/**
	 * 当前账户名
	 */
	private String accountName;
	/**
	 * 当前用户id
	 */
	private String currentUserId;
	/**
	 * 当前用户所属组织id
	 */
	private String currentOrgId;
	/**
	 * 当前用户所属部门id
	 */
	private String currentDeptId;
	/**
	 * 当前用户所属岗位id
	 */
	private String currentPositionId;
	/**
	 * 是否是管理员
	 */
	private Integer isAdministrator;
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
	/**
	 * 配置的项目id
	 * <p>配置系统使用</p>
	 */
	private String confProjectId;
	
	//-------------------------------------------------------------------------
	
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
	public String getConfProjectId() {
		return confProjectId;
	}
	public void setConfProjectId(String confProjectId) {
		this.confProjectId = confProjectId;
	}
	public String getCurrentUserId() {
		return currentUserId;
	}
	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}
	public String getCurrentOrgId() {
		return currentOrgId;
	}
	public void setCurrentOrgId(String currentOrgId) {
		this.currentOrgId = currentOrgId;
	}
	public String getCurrentDeptId() {
		return currentDeptId;
	}
	public void setCurrentDeptId(String currentDeptId) {
		this.currentDeptId = currentDeptId;
	}
	public String getCurrentPositionId() {
		return currentPositionId;
	}
	public void setCurrentPositionId(String currentPositionId) {
		this.currentPositionId = currentPositionId;
	}
	public Integer getIsAdministrator() {
		return isAdministrator;
	}
	public void setIsAdministrator(Integer isAdministrator) {
		this.isAdministrator = isAdministrator;
	}
	public String getCurrentCustomerId() {
		return currentCustomerId;
	}
	public void setCurrentCustomerId(String currentCustomerId) {
		this.currentCustomerId = currentCustomerId;
	}
	public String getCurrentProjectId() {
		return currentProjectId;
	}
	public void setCurrentProjectId(String currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public boolean isAdministrator(){
		return (isAdministrator == 1);
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_SYS_ACCOUNT_ONLINE_STATUS", 0);
		table.setName("系统账户在线状态资源对象表");
		table.setComments("系统账户在线状态资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(21);
		
		ComColumndata currentCustomerIdColumn = new ComColumndata("current_customer_id", DataTypeConstants.STRING, 32);
		currentCustomerIdColumn.setName("当前租户id");
		currentCustomerIdColumn.setComments("当前租户id");
		currentCustomerIdColumn.setOrderCode(1);
		columns.add(currentCustomerIdColumn);
		
		ComColumndata currentProjectIdColumn = new ComColumndata("current_project_id", DataTypeConstants.STRING, 32);
		currentProjectIdColumn.setName("当前项目id");
		currentProjectIdColumn.setComments("当前项目id");
		currentProjectIdColumn.setOrderCode(2);
		columns.add(currentProjectIdColumn);
		
		ComColumndata accountIdColumn = new ComColumndata("account_id", DataTypeConstants.STRING, 32);
		accountIdColumn.setName("当前的账户id");
		accountIdColumn.setComments("当前的账户id");
		accountIdColumn.setOrderCode(3);
		columns.add(accountIdColumn);
		
		ComColumndata accountNameColumn = new ComColumndata("account_name", DataTypeConstants.STRING, 32);
		accountNameColumn.setName("当前账户名");
		accountNameColumn.setComments("当前账户名");
		accountNameColumn.setOrderCode(4);
		columns.add(accountNameColumn);
		
		ComColumndata currentUserIdColumn = new ComColumndata("current_user_id", DataTypeConstants.STRING, 32);
		currentUserIdColumn.setName("当前用户id");
		currentUserIdColumn.setComments("当前用户id");
		currentUserIdColumn.setOrderCode(5);
		columns.add(currentUserIdColumn);
		
		ComColumndata currentOrgIdColumn = new ComColumndata("current_org_id", DataTypeConstants.STRING, 32);
		currentOrgIdColumn.setName("当前用户所属组织id");
		currentOrgIdColumn.setComments("当前用户所属组织id");
		currentOrgIdColumn.setOrderCode(6);
		columns.add(currentOrgIdColumn);
		
		ComColumndata currentDeptIdColumn = new ComColumndata("current_dept_id", DataTypeConstants.STRING, 32);
		currentDeptIdColumn.setName("当前用户所属部门id");
		currentDeptIdColumn.setComments("当前用户所属部门id");
		currentDeptIdColumn.setOrderCode(7);
		columns.add(currentDeptIdColumn);
		
		ComColumndata currentPositionIdColumn = new ComColumndata("current_position_id", DataTypeConstants.STRING, 32);
		currentPositionIdColumn.setName("当前用户所属岗位id");
		currentPositionIdColumn.setComments("当前用户所属岗位id");
		currentPositionIdColumn.setOrderCode(8);
		columns.add(currentPositionIdColumn);
		
		ComColumndata isAdministratorColumn = new ComColumndata("is_administrator", DataTypeConstants.STRING, 32);
		isAdministratorColumn.setName("token值");
		isAdministratorColumn.setComments("token值");
		isAdministratorColumn.setOrderCode(9);
		columns.add(isAdministratorColumn);
		
		ComColumndata tokenColumn = new ComColumndata("token", DataTypeConstants.STRING, 32);
		tokenColumn.setName("token值");
		tokenColumn.setComments("token值");
		tokenColumn.setOrderCode(10);
		columns.add(tokenColumn);
		
		ComColumndata loginIpColumn = new ComColumndata("login_ip", DataTypeConstants.STRING, 20);
		loginIpColumn.setName("登录的客户端ip");
		loginIpColumn.setComments("登录的客户端ip");
		loginIpColumn.setOrderCode(11);
		columns.add(loginIpColumn);
		
		ComColumndata loginMacColumn = new ComColumndata("login_mac", DataTypeConstants.STRING, 50);
		loginMacColumn.setName("登录的客户端mac");
		loginMacColumn.setComments("登录的客户端mac");
		loginMacColumn.setOrderCode(12);
		columns.add(loginMacColumn);
		
		ComColumndata loginDateColumn = new ComColumndata("login_date", DataTypeConstants.DATE, 0);
		loginDateColumn.setName("登录的时间");
		loginDateColumn.setComments("登录的时间");
		loginDateColumn.setOrderCode(13);
		columns.add(loginDateColumn);
		
		ComColumndata tryLoginTimesColumn = new ComColumndata("try_login_times", DataTypeConstants.INTEGER, 3);
		tryLoginTimesColumn.setName("本次尝试登录的次数");
		tryLoginTimesColumn.setComments("本次尝试登录的次数");
		tryLoginTimesColumn.setDefaultValue("0");
		tryLoginTimesColumn.setOrderCode(14);
		columns.add(tryLoginTimesColumn);
		
		ComColumndata lastOperDateColumn = new ComColumndata("last_oper_date", DataTypeConstants.DATE, 0);
		lastOperDateColumn.setName("最后一次操作的时间");
		lastOperDateColumn.setComments("最后一次操作的时间");
		lastOperDateColumn.setOrderCode(15);
		columns.add(lastOperDateColumn);
		
		ComColumndata confProjectIdColumn = new ComColumndata("conf_project_id", DataTypeConstants.STRING, 32);
		confProjectIdColumn.setName("配置的项目id");
		confProjectIdColumn.setComments("配置的项目id：配置系统使用");
		confProjectIdColumn.setOrderCode(16);
		columns.add(confProjectIdColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_ACCOUNT_ONLINE_STATUS";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComSysAccountOnlineStatus";
	}

	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("isAdministrator", isAdministrator);
		entityJson.put("loginDate", loginDate);
		entityJson.put("lastOperDate", lastOperDate);
		entityJson.put("tryLoginTimes", tryLoginTimes);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
