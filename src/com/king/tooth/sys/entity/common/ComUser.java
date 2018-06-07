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
 * [通用的]人员资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComUser extends BasicEntity implements ITable, IEntity{
	/**
	 * 账户主键
	 */
	private String accountId;
	/**
	 * 昵称
	 */
	private String nikeName;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 年龄 
	 */
	private int age;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 办公电话
	 */
	private String officePhone;
	/**
	 * 手机号
	 */
	private String tel;
	/**
	 * 邮箱
	 */
	private String userEmail;
	/**
	 * 办公地点
	 */
	private String wrokAddr;
	/**
	 * 居住地点
	 */
	private String liveAddr;
	/**
	 * 身份证号码
	 */
	private String idCardNo;
	/**
	 * 入职时间
	 */
	private Date employedDate;
	/**
	 * 人员状态
	 * <p>1.在职、2.离职、3.休假</p>
	 */
	private int userStatus;
	/**
	 * 月薪
	 */
	private String monthSalar;
	/**
	 * 工号
	 */
	private String workNo;
	/**
	 * 密级
	 */
	private int secretLevel;
	/**
	 * 描述
	 */
	private String descs;
	
	//-------------------------------------------------------------------------
	
	public ComUser() {
		this.userStatus = 1;
	}
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(int secretLevel) {
		this.secretLevel = secretLevel;
	}
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSex() {
		return sex;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	public String getMonthSalar() {
		return monthSalar;
	}
	public void setMonthSalar(String monthSalar) {
		this.monthSalar = monthSalar;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getOfficePhone() {
		return officePhone;
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
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getWrokAddr() {
		return wrokAddr;
	}
	public void setWrokAddr(String wrokAddr) {
		this.wrokAddr = wrokAddr;
	}
	public String getLiveAddr() {
		return liveAddr;
	}
	public void setLiveAddr(String liveAddr) {
		this.liveAddr = liveAddr;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public Date getEmployedDate() {
		return employedDate;
	}
	public void setEmployedDate(Date employedDate) {
		this.employedDate = employedDate;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_USER");
		table.setName("[通用的]人员资源对象表");
		table.setComments("[通用的]人员资源对象表");
		table.setIsBuiltin(1);
		table.setPlatformType(ISysResource.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(23);
		
		CfgColumndata accountIdColumn = new CfgColumndata("account_id");
		accountIdColumn.setName("账户主键");
		accountIdColumn.setComments("账户主键");
		accountIdColumn.setColumnType(DataTypeConstants.STRING);
		accountIdColumn.setLength(32);
		accountIdColumn.setOrderCode(1);
		columns.add(accountIdColumn);
		
		CfgColumndata nikeNameColumn = new CfgColumndata("nike_name");
		nikeNameColumn.setName("昵称");
		nikeNameColumn.setComments("昵称");
		nikeNameColumn.setColumnType(DataTypeConstants.STRING);
		nikeNameColumn.setLength(50);
		nikeNameColumn.setOrderCode(2);
		columns.add(nikeNameColumn);
		
		CfgColumndata realNameColumn = new CfgColumndata("real_name");
		realNameColumn.setName("真实姓名");
		realNameColumn.setComments("真实姓名");
		realNameColumn.setColumnType(DataTypeConstants.STRING);
		realNameColumn.setLength(100);
		realNameColumn.setOrderCode(3);
		columns.add(realNameColumn);
		
		CfgColumndata ageColumn = new CfgColumndata("age");
		ageColumn.setName("年龄");
		ageColumn.setComments("年龄");
		ageColumn.setColumnType(DataTypeConstants.INTEGER);
		ageColumn.setLength(3);
		ageColumn.setOrderCode(4);
		columns.add(ageColumn);
		
		CfgColumndata sexColumn = new CfgColumndata("sex");
		sexColumn.setName("性别");
		sexColumn.setComments("性别");
		sexColumn.setColumnType(DataTypeConstants.INTEGER);
		sexColumn.setLength(1);
		sexColumn.setOrderCode(5);
		columns.add(sexColumn);
		
		CfgColumndata officePhoneColumn = new CfgColumndata("office_phone");
		officePhoneColumn.setName("办公电话");
		officePhoneColumn.setComments("办公电话");
		officePhoneColumn.setColumnType(DataTypeConstants.STRING);
		officePhoneColumn.setLength(30);
		officePhoneColumn.setOrderCode(6);
		columns.add(officePhoneColumn);
		
		CfgColumndata telColumn = new CfgColumndata("tel");
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		telColumn.setColumnType(DataTypeConstants.STRING);
		telColumn.setLength(20);
		telColumn.setOrderCode(7);
		columns.add(telColumn);
		
		CfgColumndata userEmailDescColumn = new CfgColumndata("user_email");
		userEmailDescColumn.setName("邮箱");
		userEmailDescColumn.setComments("邮箱");
		userEmailDescColumn.setColumnType(DataTypeConstants.STRING);
		userEmailDescColumn.setLength(80);
		userEmailDescColumn.setOrderCode(8);
		columns.add(userEmailDescColumn);
		
		CfgColumndata wrokAddrColumn = new CfgColumndata("wrok_addr");
		wrokAddrColumn.setName("办公地点");
		wrokAddrColumn.setComments("办公地点");
		wrokAddrColumn.setColumnType(DataTypeConstants.STRING);
		wrokAddrColumn.setLength(200);
		wrokAddrColumn.setOrderCode(9);
		columns.add(wrokAddrColumn);
		
		CfgColumndata liveAddrColumn = new CfgColumndata("live_addr");
		liveAddrColumn.setName("居住地点");
		liveAddrColumn.setComments("居住地点");
		liveAddrColumn.setColumnType(DataTypeConstants.STRING);
		liveAddrColumn.setLength(200);
		liveAddrColumn.setOrderCode(10);
		columns.add(liveAddrColumn);
		
		CfgColumndata idCardNoColumn = new CfgColumndata("id_card_no");
		idCardNoColumn.setName("身份证号码");
		idCardNoColumn.setComments("身份证号码");
		idCardNoColumn.setColumnType(DataTypeConstants.STRING);
		idCardNoColumn.setLength(40);
		idCardNoColumn.setOrderCode(11);
		columns.add(idCardNoColumn);
		
		CfgColumndata employedDateColumn = new CfgColumndata("employed_date");
		employedDateColumn.setName("入职时间");
		employedDateColumn.setComments("入职时间");
		employedDateColumn.setColumnType(DataTypeConstants.DATE);
		employedDateColumn.setOrderCode(12);
		columns.add(employedDateColumn);
		
		CfgColumndata userStatusColumn = new CfgColumndata("user_status");
		userStatusColumn.setName("人员状态");
		userStatusColumn.setComments("人员状态:1.在职、2.离职、3.休假");
		userStatusColumn.setColumnType(DataTypeConstants.INTEGER);
		userStatusColumn.setLength(1);
		userStatusColumn.setOrderCode(13);
		columns.add(userStatusColumn);
		
		CfgColumndata monthSalarColumn = new CfgColumndata("month_salar");
		monthSalarColumn.setName("月薪");
		monthSalarColumn.setComments("月薪");
		monthSalarColumn.setColumnType(DataTypeConstants.DOUBLE);
		monthSalarColumn.setLength(6);
		monthSalarColumn.setPrecision(2);
		monthSalarColumn.setOrderCode(14);
		columns.add(monthSalarColumn);
		
		CfgColumndata workNoColumn = new CfgColumndata("work_no");
		workNoColumn.setName("工号");
		workNoColumn.setComments("工号");
		workNoColumn.setColumnType(DataTypeConstants.STRING);
		workNoColumn.setLength(6);
		workNoColumn.setOrderCode(15);
		columns.add(workNoColumn);
		
		CfgColumndata secretLevelColumn = new CfgColumndata("secret_level");
		secretLevelColumn.setName("密级");
		secretLevelColumn.setComments("密级");
		secretLevelColumn.setColumnType(DataTypeConstants.INTEGER);
		secretLevelColumn.setLength(1);
		secretLevelColumn.setOrderCode(16);
		columns.add(secretLevelColumn);
		
		CfgColumndata descsColumn = new CfgColumndata("descs");
		descsColumn.setName("描述");
		descsColumn.setComments("描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(2000);
		descsColumn.setOrderCode(17);
		columns.add(descsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_USER";
	}
	
	public String getEntityName() {
		return "ComUser";
	}
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("employedDate", employedDate);
		json.put("age", age+"");
		json.put("sex", sex+"");
		json.put("userStatus", userStatus+"");
		json.put("secretLevel", secretLevel+"");
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
