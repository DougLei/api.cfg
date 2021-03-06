package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;
import com.api.sys.entity.cfg.projectmodule.ProjectModuleExtend;
import com.api.util.StrUtils;

/**
 * 账户在线状态信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysAccountOnlineStatus extends BasicEntity implements IEntity{
	
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
	 * 当前用户密级
	 */
	private Integer userSecretLevel;
	/**
	 * 当前用户所属组织id
	 * <p>多个用,隔开</p>
	 */
	private String orgId;
	@JSONField(serialize = false)
	private List<Object> orgIds;
	/**
	 * 当前用户所属部门id
	 * <p>多个用,隔开</p>
	 */
	private String deptId;
	@JSONField(serialize = false)
	private List<Object> deptIds;
	/**
	 * 当前用户所属岗位id
	 * <p>多个用,隔开</p>
	 */
	private String positionId;
	@JSONField(serialize = false)
	private List<Object> positionIds;
	/**
	 * 当前用户所属角色id
	 * <p>多个用,隔开</p>
	 */
	private String roleId;
	@JSONField(serialize = false)
	private List<Object> roleIds;
	/**
	 * 当前用户所属用户组id
	 * <p>多个用,隔开</p>
	 */
	private String userGroupId;
	@JSONField(serialize = false)
	private List<Object> userGroupIds;
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
	
	/**
	 * 登陆类型
	 * <p>0：用户名密码登陆、1：刷卡登陆</p>
	 */
	private int loginType = -1;

	//-------------------------------------------------------------------------
	public SysAccountOnlineStatus() {
	}
	public SysAccountOnlineStatus(String message) {
		this.isError = 1;
		this.message = message;
	}

	/**
	 * 是否修改账户在线状态信息
	 * @see VarifyReqValidFilter
	 * @see UpdateAccountOnlineStatusThread
	 * <p>目前用在登陆后，每次请求都记录最后一次操作时间</p>
	 */
	@JSONField(serialize = false)
	private boolean isUpdate;
	
	/**
	 * 模块信息对象集合
	 */
	@JSONField(serialize = false)
	private List<ProjectModuleExtend> projectModules;
	
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
	/**
	 * 标识是否是登陆操作
	 * <p>如果是登陆操作，无论如何都要提交事务，即主要是【保存/修改】账户在线状态信息</p>
	 */
	@JSONField(serialize = false)
	private boolean isDoLogin;
	
	/**
	 * 是否有存在关联的用户对象
	 */
	@JSONField(serialize = false)
	private boolean isExistsUserObj;
	
	public String getToken() {
		return token;
	}
	public boolean getIsSave() {
		return isSave;
	}
	public void setIsSave(boolean isSave) {
		this.isSave = isSave;
	}
	public boolean getIsDoLogin() {
		return isDoLogin;
	}
	public void setIsDoLogin(boolean isDoLogin) {
		this.isDoLogin = isDoLogin;
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
	public boolean getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
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
	public Integer getUserSecretLevel() {
		return userSecretLevel;
	}
	public void setUserSecretLevel(Integer userSecretLevel) {
		this.userSecretLevel = userSecretLevel;
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
	public String getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}
	public List<Object> getUserGroupIds() {
		return userGroupIds;
	}
	public void setUserGroupIds(List<Object> userGroupIds) {
		this.userGroupIds = userGroupIds;
	}
	public List<ProjectModuleExtend> getProjectModules() {
		return projectModules;
	}
	public void setProjectModules(List<ProjectModuleExtend> projectModules) {
		this.projectModules = projectModules;
	}
	public List<Object> getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(List<Object> orgIds) {
		this.orgIds = orgIds;
	}
	public List<Object> getDeptIds() {
		return deptIds;
	}
	public void setDeptIds(List<Object> deptIds) {
		this.deptIds = deptIds;
	}
	public List<Object> getPositionIds() {
		return positionIds;
	}
	public void setPositionIds(List<Object> positionIds) {
		this.positionIds = positionIds;
	}
	public List<Object> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(List<Object> roleIds) {
		this.roleIds = roleIds;
	}
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		if(this.loginType == -1){
			this.loginType = loginType;
		}
	}
	public boolean getIsExistsUserObj() {
		return isExistsUserObj;
	}
	public void setIsExistsUserObj(boolean isExistsUserObj) {
		this.isExistsUserObj = isExistsUserObj;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(18+7);
		
		CfgColumn accountIdColumn = new CfgColumn("account_id", DataTypeConstants.STRING, 32);
		accountIdColumn.setName("当前账户id");
		accountIdColumn.setComments("当前账户id");
		columns.add(accountIdColumn);
		
		CfgColumn accountNameColumn = new CfgColumn("account_name", DataTypeConstants.STRING, 32);
		accountNameColumn.setName("当前账户名");
		accountNameColumn.setComments("当前账户名");
		columns.add(accountNameColumn);
		
		CfgColumn accountTypeColumn = new CfgColumn("account_type", DataTypeConstants.INTEGER, 1);
		accountTypeColumn.setName("当前账户类型");
		accountTypeColumn.setComments("当前账户类型：冗余SysAccount表的accountType值");
		columns.add(accountTypeColumn);
		
		CfgColumn userIdColumn = new CfgColumn("user_id", DataTypeConstants.STRING, 32);
		userIdColumn.setName("当前用户id");
		userIdColumn.setComments("当前用户id");
		columns.add(userIdColumn);
		
		CfgColumn userSecretLevelColumn = new CfgColumn("user_secret_level", DataTypeConstants.INTEGER, 1);
		userSecretLevelColumn.setName("当前用户密级");
		userSecretLevelColumn.setComments("当前用户密级");
		columns.add(userSecretLevelColumn);
		
		CfgColumn orgIdColumn = new CfgColumn("org_id", DataTypeConstants.STRING, 200);
		orgIdColumn.setName("当前用户所属组织id");
		orgIdColumn.setComments("当前用户所属组织id，可以有多个，用,隔开");
		columns.add(orgIdColumn);
		
		CfgColumn deptIdColumn = new CfgColumn("dept_id", DataTypeConstants.STRING, 200);
		deptIdColumn.setName("当前用户所属部门id");
		deptIdColumn.setComments("当前用户所属部门id，可以有多个，用,隔开");
		columns.add(deptIdColumn);
		
		CfgColumn positionIdColumn = new CfgColumn("position_id", DataTypeConstants.STRING, 200);
		positionIdColumn.setName("当前用户所属岗位id");
		positionIdColumn.setComments("当前用户所属岗位id，可以有多个，用,隔开");
		columns.add(positionIdColumn);
		
		CfgColumn roleIdColumn = new CfgColumn("role_id", DataTypeConstants.STRING, 400);
		roleIdColumn.setName("当前用户所属角色id");
		roleIdColumn.setComments("当前用户所属角色id，可以有多个，用,隔开");
		columns.add(roleIdColumn);
		
		CfgColumn userGroupIdColumn = new CfgColumn("user_group_id", DataTypeConstants.STRING, 400);
		userGroupIdColumn.setName("当前用户所属用户组id");
		userGroupIdColumn.setComments("当前用户所属用户组id，可以有多个，用,隔开");
		columns.add(userGroupIdColumn);
		
		CfgColumn tokenColumn = new CfgColumn("token", DataTypeConstants.STRING, 32);
		tokenColumn.setName("token值");
		tokenColumn.setComments("token值");
		columns.add(tokenColumn);
		
		CfgColumn loginIpColumn = new CfgColumn("login_ip", DataTypeConstants.STRING, 20);
		loginIpColumn.setName("登录的客户端ip");
		loginIpColumn.setComments("登录的客户端ip");
		columns.add(loginIpColumn);
		
		CfgColumn loginMacColumn = new CfgColumn("login_mac", DataTypeConstants.STRING, 50);
		loginMacColumn.setName("登录的客户端mac");
		loginMacColumn.setComments("登录的客户端mac");
		columns.add(loginMacColumn);
		
		CfgColumn loginDateColumn = new CfgColumn("login_date", DataTypeConstants.DATE, 0);
		loginDateColumn.setName("登录的时间");
		loginDateColumn.setComments("登录的时间");
		columns.add(loginDateColumn);
		
		CfgColumn tryLoginTimesColumn = new CfgColumn("try_login_times", DataTypeConstants.INTEGER, 3);
		tryLoginTimesColumn.setName("本次尝试登录的次数");
		tryLoginTimesColumn.setComments("本次尝试登录的次数");
		tryLoginTimesColumn.setDefaultValue("0");
		columns.add(tryLoginTimesColumn);
		
		CfgColumn lastOperDateColumn = new CfgColumn("last_oper_date", DataTypeConstants.DATE, 0);
		lastOperDateColumn.setName("最后一次操作的时间");
		lastOperDateColumn.setComments("最后一次操作的时间");
		columns.add(lastOperDateColumn);
		
		CfgColumn confProjectIdColumn = new CfgColumn("conf_project_id", DataTypeConstants.STRING, 32);
		confProjectIdColumn.setName("配置的项目id");
		confProjectIdColumn.setComments("配置的项目id：配置系统使用");
		columns.add(confProjectIdColumn);
		
		CfgColumn loginTypeColumn = new CfgColumn("login_type", DataTypeConstants.INTEGER, 1);
		loginTypeColumn.setName("登陆类型");
		loginTypeColumn.setComments("0：用户名密码登陆、1：刷卡登陆");
		columns.add(loginTypeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("账户在线状态信息表");
		table.setRemark("账户在线状态信息表");
		
		
		table.setColumns(getColumnList());
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
	 * 是否是用户管理账户
	 * <p>可以对系统用户和账户进行物理删除</p>
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isUserAdministrator(){
		return "62414f34367147729595cf815d33fe3f".equals(accountId) && isAdministrator(); 
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
	
	/**0：用户名密码登陆*/
	public SysAccountOnlineStatus loginByUserNameAndPassword(){
		this.setLoginType(0);
		return this;
	}
	/**1：刷卡登陆*/
	public SysAccountOnlineStatus loginByCard(){
		this.setLoginType(1);
		return this;
	}
	
	/**
	 * 是否是内置账户
	 * @param accountId
	 * @return 如果是，返回内置的账户名
	 */
	@JSONField(serialize = false)
	public String isBuiltinAccount(String accountId){
		if(StrUtils.isEmpty(accountId)){
			return null;
		}
		if("16ed21bd7a7a41f5bea2ebaa258908cf".equals(accountId)){
			return "admin";
		}
		if("62414f34367147729595cf815d33fe3f".equals(accountId)){
			return "user_admin";
		}
		if("93d02915eb764d978e3cae6987b5fc7a".equals(accountId)){
			return "developer";
		}
		return null;
	}
}
