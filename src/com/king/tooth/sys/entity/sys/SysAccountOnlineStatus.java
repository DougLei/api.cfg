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
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;

/**
 * 账户在线状态信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysAccountOnlineStatus extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 当前账户id
	 */
	private String accountId;
	/**
	 * 当前账户名
	 */
	private String accountName;
	/**
	 * 当前账户类型
	 * <p>冗余SysAccount表的accountType值</p>
	 */
	private Integer accountType;
	/**
	 * 当前用户id
	 */
	private String userId;
	/**
	 * 当前用户所属组织id
	 */
	private String orgId;
	/**
	 * 当前用户所属部门id
	 */
	private String deptId;
	/**
	 * 当前用户所属岗位id
	 */
	private String positionId;
	/**
	 * 当前用户所属角色id
	 */
	private String roleId;
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
	 * 权限信息对象
	 */
	@JSONField(serialize = false)
	private SysPermissionExtend permission;
	
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
	public Integer getAccountType() {
		return accountType;
	}
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public SysPermissionExtend getPermission() {
		return permission;
	}
	public void setPermission(SysPermissionExtend permission) {
		this.permission = permission;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_ACCOUNT_ONLINE_STATUS", 0);
		table.setName("账户在线状态信息表");
		table.setComments("账户在线状态信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(22);
		
		ComColumndata accountIdColumn = new ComColumndata("account_id", BuiltinCodeDataType.STRING, 32);
		accountIdColumn.setName("当前账户id");
		accountIdColumn.setComments("当前账户id");
		columns.add(accountIdColumn);
		
		ComColumndata accountNameColumn = new ComColumndata("account_name", BuiltinCodeDataType.STRING, 32);
		accountNameColumn.setName("当前账户名");
		accountNameColumn.setComments("当前账户名");
		columns.add(accountNameColumn);
		
		ComColumndata accountTypeColumn = new ComColumndata("account_type", BuiltinCodeDataType.INTEGER, 1);
		accountTypeColumn.setName("当前账户类型");
		accountTypeColumn.setComments("当前账户类型：冗余SysAccount表的accountType值");
		columns.add(accountTypeColumn);
		
		ComColumndata userIdColumn = new ComColumndata("user_id", BuiltinCodeDataType.STRING, 32);
		userIdColumn.setName("当前用户id");
		userIdColumn.setComments("当前用户id");
		columns.add(userIdColumn);
		
		ComColumndata orgIdColumn = new ComColumndata("org_id", BuiltinCodeDataType.STRING, 200);
		orgIdColumn.setName("当前用户所属组织id");
		orgIdColumn.setComments("当前用户所属组织id，可以有多个，用,隔开");
		columns.add(orgIdColumn);
		
		ComColumndata deptIdColumn = new ComColumndata("dept_id", BuiltinCodeDataType.STRING, 200);
		deptIdColumn.setName("当前用户所属部门id");
		deptIdColumn.setComments("当前用户所属部门id，可以有多个，用,隔开");
		columns.add(deptIdColumn);
		
		ComColumndata positionIdColumn = new ComColumndata("position_id", BuiltinCodeDataType.STRING, 200);
		positionIdColumn.setName("当前用户所属岗位id");
		positionIdColumn.setComments("当前用户所属岗位id，可以有多个，用,隔开");
		columns.add(positionIdColumn);
		
		ComColumndata roleIdColumn = new ComColumndata("role_id", BuiltinCodeDataType.STRING, 400);
		roleIdColumn.setName("当前用户所属角色id");
		roleIdColumn.setComments("当前用户所属角色id，可以有多个，用,隔开");
		columns.add(roleIdColumn);
		
		ComColumndata tokenColumn = new ComColumndata("token", BuiltinCodeDataType.STRING, 32);
		tokenColumn.setName("token值");
		tokenColumn.setComments("token值");
		columns.add(tokenColumn);
		
		ComColumndata loginIpColumn = new ComColumndata("login_ip", BuiltinCodeDataType.STRING, 20);
		loginIpColumn.setName("登录的客户端ip");
		loginIpColumn.setComments("登录的客户端ip");
		columns.add(loginIpColumn);
		
		ComColumndata loginMacColumn = new ComColumndata("login_mac", BuiltinCodeDataType.STRING, 50);
		loginMacColumn.setName("登录的客户端mac");
		loginMacColumn.setComments("登录的客户端mac");
		columns.add(loginMacColumn);
		
		ComColumndata loginDateColumn = new ComColumndata("login_date", BuiltinCodeDataType.DATE, 0);
		loginDateColumn.setName("登录的时间");
		loginDateColumn.setComments("登录的时间");
		columns.add(loginDateColumn);
		
		ComColumndata tryLoginTimesColumn = new ComColumndata("try_login_times", BuiltinCodeDataType.INTEGER, 3);
		tryLoginTimesColumn.setName("本次尝试登录的次数");
		tryLoginTimesColumn.setComments("本次尝试登录的次数");
		tryLoginTimesColumn.setDefaultValue("0");
		columns.add(tryLoginTimesColumn);
		
		ComColumndata lastOperDateColumn = new ComColumndata("last_oper_date", BuiltinCodeDataType.DATE, 0);
		lastOperDateColumn.setName("最后一次操作的时间");
		lastOperDateColumn.setComments("最后一次操作的时间");
		columns.add(lastOperDateColumn);
		
		ComColumndata confProjectIdColumn = new ComColumndata("conf_project_id", BuiltinCodeDataType.STRING, 32);
		confProjectIdColumn.setName("配置的项目id");
		confProjectIdColumn.setComments("配置的项目id：配置系统使用");
		columns.add(confProjectIdColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_ACCOUNT_ONLINE_STATUS";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysAccountOnlineStatus";
	}

	/**
	 * 是否是管理账户
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isAdministrator(){
		return (accountType == 1);
	}
	/**
	 * 是否是普通账户
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isNormal(){
		return (accountType == 2);
	}
	/**
	 * 是否是平台开发者账户
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isDeveloper(){
		return (accountType == 3);
	}
}
