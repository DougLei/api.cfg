package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.dm.DmPublishBasicData;
import com.king.tooth.util.StrUtils;

/**
 * 账户表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysAccount extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{

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

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_ACCOUNT", 0);
		table.setName("账户表");
		table.setComments("账户表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(15);
		
		ComColumndata loginNameColumn = new ComColumndata("login_name", BuiltinDataType.STRING, 30);
		loginNameColumn.setName("登录名");
		loginNameColumn.setComments("登录名");
		loginNameColumn.setOrderCode(1);
		columns.add(loginNameColumn);
		
		ComColumndata loginPwdColumn = new ComColumndata("login_pwd", BuiltinDataType.STRING, 32);
		loginPwdColumn.setName("登录密码");
		loginPwdColumn.setComments("登录密码");
		loginPwdColumn.setOrderCode(2);
		columns.add(loginPwdColumn);
		
		ComColumndata loginPwdKeyColumn = new ComColumndata("login_pwd_key", BuiltinDataType.STRING, 32);
		loginPwdKeyColumn.setName("登录密码的密钥");
		loginPwdKeyColumn.setComments("登录密码的密钥：和loginPwd结合，得到每个账户独有的密码");
		loginPwdKeyColumn.setOrderCode(3);
		columns.add(loginPwdKeyColumn);
		
		ComColumndata telColumn = new ComColumndata("tel", BuiltinDataType.STRING, 20);
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		telColumn.setOrderCode(4);
		columns.add(telColumn);
		
		ComColumndata emailColumn = new ComColumndata("email", BuiltinDataType.STRING, 80);
		emailColumn.setName("邮箱");
		emailColumn.setComments("邮箱");
		emailColumn.setOrderCode(5);
		columns.add(emailColumn);

		ComColumndata typeColumn = new ComColumndata("type", BuiltinDataType.INTEGER, 1);
		typeColumn.setName("账户类型");
		typeColumn.setComments("账户类型:1.管理员、2.普通账户、3.平台开发账户(配置系统使用)，默认值是：2");
		typeColumn.setDefaultValue("2");
		typeColumn.setOrderCode(6);
		columns.add(typeColumn);
		
		ComColumndata statusColumn = new ComColumndata("status", BuiltinDataType.INTEGER, 1);
		statusColumn.setName("账户状态");
		statusColumn.setComments("账户状态:1.启用、2.禁用，默认值是：1");
		statusColumn.setDefaultValue("1");
		statusColumn.setOrderCode(7);
		columns.add(statusColumn);
		
		ComColumndata validDateColumn = new ComColumndata("valid_date", BuiltinDataType.DATE, 0);
		validDateColumn.setName("账户有效期限");
		validDateColumn.setComments("账户有效期限");
		validDateColumn.setOrderCode(8);
		columns.add(validDateColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_ACCOUNT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysAccount";
	}
	
	/**
	 * 转换为要发布的基础数据资源对象
	 * @return
	 */
	public DmPublishBasicData turnToPublishBasicData(Integer belongPlatformType){
		DmPublishBasicData publishBasicData = new DmPublishBasicData();
		publishBasicData.setBasicDataResourceName(getEntityName());
		publishBasicData.setBasicDataJsonStr(JSONObject.toJSONString(this));
		publishBasicData.setBelongPlatformType(belongPlatformType);
		return publishBasicData;
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
