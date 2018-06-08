package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

/**
 * 系统账户资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSysAccount extends BasicEntity implements ITable, IEntity{

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
	 * 		1.平台开发账户
	 * 		2.一般开发账户
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
	
	//-------------------------------------------------------------------------
	
	/**
	 * 登录和验证登录时，用来传递信息
	 */
	private String message;
	/**
	 * 验证码的值
	 */
	private String verifyCode;
	
	public ComSysAccount() {
		this.accountStatus = 1;
	}
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public String getTel() {
		return tel;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAccountStatusDes() {
		return accountStatusDes;
	}
	public void setAccountStatusDes(String accountStatusDes) {
		this.accountStatusDes = accountStatusDes;
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
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
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
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_SYS_ACCOUNT", 0);
		table.setName("系统账户资源对象表");
		table.setComments("系统账户资源对象表");
		table.setIsBuiltin(1);
		
		table.setIsNeedDeploy(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(15);
		
		ComColumndata loginNameColumn = new ComColumndata("login_name");
		loginNameColumn.setName("登录名");
		loginNameColumn.setComments("登录名");
		loginNameColumn.setColumnType(DataTypeConstants.STRING);
		loginNameColumn.setLength(50);
		loginNameColumn.setOrderCode(1);
		columns.add(loginNameColumn);
		
		ComColumndata loginPwdColumn = new ComColumndata("login_pwd");
		loginPwdColumn.setName("登录密码");
		loginPwdColumn.setComments("登录密码");
		loginPwdColumn.setColumnType(DataTypeConstants.STRING);
		loginPwdColumn.setLength(32);
		loginPwdColumn.setOrderCode(2);
		columns.add(loginPwdColumn);
		
		ComColumndata loginPwdKeyColumn = new ComColumndata("login_pwd_key");
		loginPwdKeyColumn.setName("登录密码的密钥");
		loginPwdKeyColumn.setComments("登录密码的密钥：和loginPwd结合，得到每个账户独有的密码");
		loginPwdKeyColumn.setColumnType(DataTypeConstants.STRING);
		loginPwdKeyColumn.setLength(32);
		loginPwdKeyColumn.setOrderCode(3);
		columns.add(loginPwdKeyColumn);
		
		ComColumndata telColumn = new ComColumndata("tel");
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		telColumn.setColumnType(DataTypeConstants.STRING);
		telColumn.setLength(20);
		telColumn.setOrderCode(4);
		columns.add(telColumn);
		
		ComColumndata emailsColumn = new ComColumndata("emails");
		emailsColumn.setName("邮箱");
		emailsColumn.setComments("邮箱");
		emailsColumn.setColumnType(DataTypeConstants.STRING);
		emailsColumn.setLength(80);
		emailsColumn.setOrderCode(5);
		columns.add(emailsColumn);
		
		ComColumndata accountTypeColumn = new ComColumndata("account_type");
		accountTypeColumn.setName("账户类型");
		accountTypeColumn.setComments("账户类型:1.平台开发账户、2.一般开发账户");
		accountTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		accountTypeColumn.setLength(1);
		accountTypeColumn.setOrderCode(6);
		columns.add(accountTypeColumn);
		
		ComColumndata accountStatusColumn = new ComColumndata("account_status");
		accountStatusColumn.setName("账户状态");
		accountStatusColumn.setComments("账户状态:1.启用、2.禁用、3.过期");
		accountStatusColumn.setColumnType(DataTypeConstants.INTEGER);
		accountStatusColumn.setLength(1);
		accountStatusColumn.setOrderCode(7);
		columns.add(accountStatusColumn);
		
		ComColumndata accountStatusDesColumn = new ComColumndata("account_status_des");
		accountStatusDesColumn.setName("账户状态的描述");
		accountStatusDesColumn.setComments("账户状态的描述");
		accountStatusDesColumn.setColumnType(DataTypeConstants.STRING);
		accountStatusDesColumn.setLength(200);
		accountStatusDesColumn.setOrderCode(8);
		columns.add(accountStatusDesColumn);
		
		ComColumndata validDateColumn = new ComColumndata("valid_date");
		validDateColumn.setName("账户有效期限");
		validDateColumn.setComments("账户有效期限");
		validDateColumn.setColumnType(DataTypeConstants.DATE);
		validDateColumn.setOrderCode(9);
		columns.add(validDateColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_SYS_ACCOUNT";
	}
	
	public String getEntityName() {
		return "ComSysAccount";
	}
	
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("validDate", validDate);
		json.put("accountType", accountType+"");
		json.put("accountStatus", accountStatus+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
}
