package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.util.StrUtils;

/**
 * 账户表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysAccount extends BasicEntity implements IEntity, IEntityPropAnalysis{

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
	private String email;
	/**
	 * 账户类型
	 * 		1.管理员
	 * 		2.普通账户
	 * 		3.平台开发账户(配置系统使用)
	 * <p>默认值是：2</p>
	 */
	private Integer type;
	/**
	 * 账户状态
	 * 		1.启用
	 * 		2.禁用
	 * <p>默认值是：1</p>
	 */
	private Integer status;
	/**
	 * 账户有效期限
	 */
	private Date validDate;
	/**
	 * 是否被删除
	 * <p>逻辑删除，默认值为0</p>
	 */
	private Integer isDelete;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 登录和验证登录时，用来传递信息
	 */
	@JSONField(serialize = false)
	private String message;
	/**
	 * 验证码的值
	 */
	@JSONField(serialize = false)
	private String verifyCode;
	
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
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public String getLoginPwdKey() {
		return loginPwdKey;
	}
	public void setLoginPwdKey(String loginPwdKey) {
		this.loginPwdKey = loginPwdKey;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	public SysAccount(String id) {
		this.id = id;
	}
	public SysAccount() {
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(16);
		
		CfgColumn loginNameColumn = new CfgColumn("login_name", DataTypeConstants.STRING, 30);
		loginNameColumn.setName("登录名");
		loginNameColumn.setComments("登录名");
		columns.add(loginNameColumn);
		
		CfgColumn loginPwdColumn = new CfgColumn("login_pwd", DataTypeConstants.STRING, 32);
		loginPwdColumn.setName("登录密码");
		loginPwdColumn.setComments("登录密码");
		columns.add(loginPwdColumn);
		
		CfgColumn loginPwdKeyColumn = new CfgColumn("login_pwd_key", DataTypeConstants.STRING, 32);
		loginPwdKeyColumn.setName("登录密码的密钥");
		loginPwdKeyColumn.setComments("登录密码的密钥：和loginPwd结合，得到每个账户独有的密码");
		columns.add(loginPwdKeyColumn);
		
		CfgColumn telColumn = new CfgColumn("tel", DataTypeConstants.STRING, 20);
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		columns.add(telColumn);
		
		CfgColumn emailColumn = new CfgColumn("email", DataTypeConstants.STRING, 80);
		emailColumn.setName("邮箱");
		emailColumn.setComments("邮箱");
		columns.add(emailColumn);

		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.INTEGER, 1);
		typeColumn.setName("账户类型");
		typeColumn.setComments("账户类型:1.管理员、2.普通账户、3.平台开发账户(配置系统使用)，默认值是：2");
		typeColumn.setDefaultValue("2");
		columns.add(typeColumn);
		
		CfgColumn statusColumn = new CfgColumn("status", DataTypeConstants.INTEGER, 1);
		statusColumn.setName("账户状态");
		statusColumn.setComments("账户状态:1.启用、2.禁用，默认值是：1");
		statusColumn.setDefaultValue("1");
		columns.add(statusColumn);
		
		CfgColumn validDateColumn = new CfgColumn("valid_date", DataTypeConstants.DATE, 0);
		validDateColumn.setName("账户有效期限");
		validDateColumn.setComments("账户有效期限");
		columns.add(validDateColumn);
		
		CfgColumn isDeleteColumn = new CfgColumn("is_delete", DataTypeConstants.INTEGER, 1);
		isDeleteColumn.setName("是否被删除");
		isDeleteColumn.setComments("逻辑删除，默认值为0");
		isDeleteColumn.setDefaultValue("0");
		columns.add(isDeleteColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("账户表");
		table.setRemark("账户表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_ACCOUNT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysAccount";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(loginName)){
			return "登陆名不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	/**
	 * 账户类型:1.管理员
	 */
	public static final Integer ADMIN = 1;
	/**
	 * 账户类型:2.普通账户
	 */
	public static final Integer NORMAL = 2;
	/**
	 * 账户类型:3.平台开发账户(配置系统使用)
	 */
	public static final Integer DEVELOPER = 3;
}
