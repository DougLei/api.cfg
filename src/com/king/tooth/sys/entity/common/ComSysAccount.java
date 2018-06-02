package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.cache.SysConfig;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgCustomer;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.CryptographyUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

/**
 * [通用的]系统账户资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSysAccount extends BasicEntity implements ITable{

	/**
	 * 登录名
	 */
	private String loginName;
	/**
	 * 登录密码
	 */
	private String loginPwd;
	/**
	 * 登录密码的密钥
	 * <p>和loginPwd结合，得到每个账户独有的密码</p>
	 */
	private String loginPwdKey;
	/**
	 * 手机号
	 */
	private String tel;
	/**
	 * 邮箱
	 */
	private String emails;
	/**
	 * 账户类型
	 * 		0:超级管理员(和用户表有对应)，只有一个，而且只是在配置平台中有
	 * 		1.游客：虚拟账号(和用户表没有对应)
	 * 		2.客户(企业)：实体帐号(和用户表有对应)
	 * 		3.普通账户：实体帐号(和用户表有对应)
	 * 		4.普通虚拟账户：(和用户表没有对应)
	 * 		
	 * 只有超级管理员才能修改这个字段的值，其他地方，这个值只能通过系统指定的硬编码去管理
	 */
	private int accountType;
	/**
	 * 账户状态
	 * 		1.启用
	 * 		2.禁用
	 * 		3.过期
	 */
	private int accountStatus;
	/**
	 * 账户状态的描述
	 */
	private String accountStatusDes;
	/**
	 * 账户有效期限
	 */
	private Date validDate;
	/**
	 * 是否不能删除
	 */
	private int isUnDelete;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 对应的用户对象
	 */
	private ComUser user;
	/**
	 * 对应的客户对象
	 */
	private CfgCustomer customer;
	/**
	 * 登录和验证登录时，用来传递信息
	 */
	private String message;
	/**
	 * 所具有的角色集合
	 */
	private List<ComRole> roles;
	
	public ComSysAccount() {
		this.accountStatus = ISysResource.ENABLED_RESOURCE_STATUS;
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		if(StrUtils.isEmpty(loginPwd)){
			loginPwd = CryptographyUtil.encodeMd5AccountPassword(SysConfig.getSystemConfig("account.default.pwd"), getLoginPwdKey());
		}
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		if(StrUtils.notEmpty(loginPwd)){
			loginPwd = CryptographyUtil.encodeMd5AccountPassword(loginPwd, getLoginPwdKey());
		}
		this.loginPwd = loginPwd;
	}
	public String getTel() {
		return tel;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public CfgCustomer getCustomer() {
		return customer;
	}
	public String getAccountStatusDes() {
		return accountStatusDes;
	}
	public List<ComRole> getRoles() {
		return roles;
	}
	public void setRoles(List<ComRole> roles) {
		this.roles = roles;
	}
	public void setAccountStatusDes(String accountStatusDes) {
		this.accountStatusDes = accountStatusDes;
	}
	public void setCustomer(CfgCustomer customer) {
		this.customer = customer;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Date getValidDate() {
		return validDate;
	}
	public ComUser getUser() {
		return user;
	}
	public void setUser(ComUser user) {
		this.user = user;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public String getEmails() {
		return emails;
	}
	public String getLoginPwdKey() {
		if(StrUtils.isEmpty(loginPwdKey)){
			loginPwdKey = ResourceHandlerUtil.getLoginPwdKey();
		}
		return loginPwdKey;
	}
	public void setLoginPwdKey(String loginPwdKey) {
		this.loginPwdKey = loginPwdKey;
	}
	public void setEmails(String emails) {
		this.emails = emails;
	}
	public int getAccountType() {
		return accountType;
	}
	public int getIsUnDelete() {
		return isUnDelete;
	}
	public void setIsUnDelete(int isUnDelete) {
		this.isUnDelete = isUnDelete;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_SYS_ACCOUNT");
		table.setName("[通用的]系统账户资源对象表");
		table.setComments("[通用的]系统账户资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(16);
		
		CfgColumndata loginNameColumn = new CfgColumndata("login_name");
		loginNameColumn.setName("登录名");
		loginNameColumn.setComments("登录名");
		loginNameColumn.setColumnType(DataTypeConstants.STRING);
		loginNameColumn.setLength(50);
		loginNameColumn.setOrderCode(1);
		columns.add(loginNameColumn);
		
		CfgColumndata loginPwdColumn = new CfgColumndata("login_pwd");
		loginPwdColumn.setName("登录密码");
		loginPwdColumn.setComments("登录密码");
		loginPwdColumn.setColumnType(DataTypeConstants.STRING);
		loginPwdColumn.setLength(32);
		loginPwdColumn.setOrderCode(2);
		columns.add(loginPwdColumn);
		
		CfgColumndata loginPwdKeyColumn = new CfgColumndata("login_pwd_key");
		loginPwdKeyColumn.setName("登录密码的密钥");
		loginPwdKeyColumn.setComments("登录密码的密钥：和loginPwd结合，得到每个账户独有的密码");
		loginPwdKeyColumn.setColumnType(DataTypeConstants.STRING);
		loginPwdKeyColumn.setLength(32);
		loginPwdKeyColumn.setOrderCode(3);
		columns.add(loginPwdKeyColumn);
		
		CfgColumndata telColumn = new CfgColumndata("tel");
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		telColumn.setColumnType(DataTypeConstants.STRING);
		telColumn.setLength(20);
		telColumn.setOrderCode(4);
		columns.add(telColumn);
		
		CfgColumndata emailsColumn = new CfgColumndata("emails");
		emailsColumn.setName("邮箱");
		emailsColumn.setComments("邮箱");
		emailsColumn.setColumnType(DataTypeConstants.STRING);
		emailsColumn.setLength(80);
		emailsColumn.setOrderCode(5);
		columns.add(emailsColumn);
		
		CfgColumndata accountTypeColumn = new CfgColumndata("account_type");
		accountTypeColumn.setName("账户类型");
		accountTypeColumn.setComments("账户类型:0:超级管理员(和用户表有对应)，只有一个，而且只是在配置平台中有、1.游客：虚拟账号(和用户表没有对应)、2.客户(企业)：实体帐号(和用户表有对应)、3.普通账户：(和用户表有对应)、4.普通虚拟账户：(和用户表没有对应)。只有超级管理员才能修改这个字段的值，其他地方，这个值只能通过系统指定的硬编码去管理");
		accountTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		accountTypeColumn.setLength(1);
		accountTypeColumn.setOrderCode(6);
		columns.add(accountTypeColumn);
		
		CfgColumndata accountStatusColumn = new CfgColumndata("account_status");
		accountStatusColumn.setName("账户状态");
		accountStatusColumn.setComments("账户状态:1.启用、2.禁用、3.过期");
		accountStatusColumn.setColumnType(DataTypeConstants.INTEGER);
		accountStatusColumn.setLength(1);
		accountStatusColumn.setOrderCode(7);
		columns.add(accountStatusColumn);
		
		CfgColumndata accountStatusDesColumn = new CfgColumndata("account_status_des");
		accountStatusDesColumn.setName("账户状态的描述");
		accountStatusDesColumn.setComments("账户状态的描述");
		accountStatusDesColumn.setColumnType(DataTypeConstants.STRING);
		accountStatusDesColumn.setLength(200);
		accountStatusDesColumn.setOrderCode(8);
		columns.add(accountStatusDesColumn);
		
		CfgColumndata validDateColumn = new CfgColumndata("valid_date");
		validDateColumn.setName("账户有效期限");
		validDateColumn.setComments("账户有效期限");
		validDateColumn.setColumnType(DataTypeConstants.DATE);
		validDateColumn.setOrderCode(9);
		columns.add(validDateColumn);
		
		CfgColumndata isUnDeleteColumn = new CfgColumndata("is_un_delete");
		isUnDeleteColumn.setName("是否不能删除");
		isUnDeleteColumn.setComments("是否不能删除");
		isUnDeleteColumn.setColumnType(DataTypeConstants.INTEGER);
		isUnDeleteColumn.setLength(1);
		isUnDeleteColumn.setOrderCode(10);
		columns.add(isUnDeleteColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_ACCOUNT";
	}
}
