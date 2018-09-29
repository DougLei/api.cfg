package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.StrUtils;

/**
 * 用户信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUser extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
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
	private Integer age;
	/**
	 * 性别
	 */
	private Integer sex;
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
	private String email;
	/**
	 * 办公地点
	 */
	private String workAddr;
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
	 * <p>0:其他(默认)、1.在职、2.离职、3.休假</p>
	 */
	private Integer userStatus;
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
	private Integer secretLevel;
	/**
	 * 描述
	 */
	private String descs;
	/**
	 * 主要所属的部门id
	 * <p>关系表中，isMain=1的部门id</p>
	 */
	private String deptId;
	/**
	 * 主要所属的职务id
	 * <p>关系表中，isMain=1的职务id</p>
	 */
	private String positionId;
	/**
	 * 主要所属的组织id
	 * <p>即所属主要部门关联的组织id</p>
	 */
	private String orgId;
	/**
	 * 是否被删除
	 * <p>逻辑删除，默认值为0</p>
	 */
	private Integer isDelete;
	
	// ---------------------------------------------------------------------------

	/**
	 * 是否创建账户
	 */
	@JSONField(serialize = false)
	private int isCreateAccount;
	
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
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getOfficePhone() {
		return officePhone;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWorkAddr() {
		return workAddr;
	}
	public void setWorkAddr(String workAddr) {
		this.workAddr = workAddr;
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
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public String getMonthSalar() {
		return monthSalar;
	}
	public void setMonthSalar(String monthSalar) {
		this.monthSalar = monthSalar;
	}
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	public Integer getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public int getIsCreateAccount() {
		return isCreateAccount;
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
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public void setIsCreateAccount(int isCreateAccount) {
		this.isCreateAccount = isCreateAccount;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(27);
		
		ComColumndata nikeNameColumn = new ComColumndata("nike_name", DataTypeConstants.STRING, 50);
		nikeNameColumn.setName("昵称");
		nikeNameColumn.setComments("昵称");
		nikeNameColumn.setOrderCode(2);
		columns.add(nikeNameColumn);
		
		ComColumndata realNameColumn = new ComColumndata("real_name", DataTypeConstants.STRING, 100);
		realNameColumn.setName("真实姓名");
		realNameColumn.setComments("真实姓名");
		realNameColumn.setOrderCode(3);
		columns.add(realNameColumn);
		
		ComColumndata ageColumn = new ComColumndata("age", DataTypeConstants.INTEGER, 1);
		ageColumn.setName("年龄");
		ageColumn.setComments("年龄");
		ageColumn.setOrderCode(4);
		columns.add(ageColumn);
		
		ComColumndata sexColumn = new ComColumndata("sex", DataTypeConstants.INTEGER, 1);
		sexColumn.setName("性别");
		sexColumn.setComments("性别");
		sexColumn.setOrderCode(5);
		columns.add(sexColumn);
		
		ComColumndata officePhoneColumn = new ComColumndata("office_phone", DataTypeConstants.STRING, 32);
		officePhoneColumn.setName("办公电话");
		officePhoneColumn.setComments("办公电话");
		officePhoneColumn.setOrderCode(6);
		columns.add(officePhoneColumn);
		
		ComColumndata telColumn = new ComColumndata("tel", DataTypeConstants.STRING, 20);
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		telColumn.setOrderCode(7);
		columns.add(telColumn);
		
		ComColumndata emailColumn = new ComColumndata("email", DataTypeConstants.STRING, 80);
		emailColumn.setName("邮箱");
		emailColumn.setComments("邮箱");
		emailColumn.setOrderCode(8);
		columns.add(emailColumn);
		
		ComColumndata workAddrColumn = new ComColumndata("work_addr", DataTypeConstants.STRING, 200);
		workAddrColumn.setName("办公地点");
		workAddrColumn.setComments("办公地点");
		workAddrColumn.setOrderCode(9);
		columns.add(workAddrColumn);
		
		ComColumndata liveAddrColumn = new ComColumndata("live_addr", DataTypeConstants.STRING, 200);
		liveAddrColumn.setName("居住地点");
		liveAddrColumn.setComments("居住地点");
		liveAddrColumn.setOrderCode(10);
		columns.add(liveAddrColumn);
		
		ComColumndata idCardNoColumn = new ComColumndata("id_card_no", DataTypeConstants.STRING, 40);
		idCardNoColumn.setName("身份证号码");
		idCardNoColumn.setComments("身份证号码");
		idCardNoColumn.setOrderCode(11);
		columns.add(idCardNoColumn);
		
		ComColumndata employedDateColumn = new ComColumndata("employed_date", DataTypeConstants.DATE, 0);
		employedDateColumn.setName("入职时间");
		employedDateColumn.setComments("入职时间");
		employedDateColumn.setOrderCode(12);
		columns.add(employedDateColumn);
		
		ComColumndata userStatusColumn = new ComColumndata("user_status", DataTypeConstants.INTEGER, 1);
		userStatusColumn.setName("人员状态");
		userStatusColumn.setComments("人员状态:0:其他(默认)、1.在职、2.离职、3.休假");
		userStatusColumn.setDefaultValue("0");
		userStatusColumn.setOrderCode(13);
		columns.add(userStatusColumn);
		
		ComColumndata monthSalarColumn = new ComColumndata("month_salar", DataTypeConstants.DOUBLE, 10);
		monthSalarColumn.setName("月薪");
		monthSalarColumn.setComments("月薪");
		monthSalarColumn.setPrecision(2);
		monthSalarColumn.setOrderCode(14);
		columns.add(monthSalarColumn);
		
		ComColumndata workNoColumn = new ComColumndata("work_no", DataTypeConstants.STRING, 32);
		workNoColumn.setName("工号");
		workNoColumn.setComments("工号");
		workNoColumn.setIsNullabled(0);
		workNoColumn.setOrderCode(15);
		columns.add(workNoColumn);
		
		ComColumndata secretLevelColumn = new ComColumndata("secret_level", DataTypeConstants.INTEGER, 1);
		secretLevelColumn.setName("密级");
		secretLevelColumn.setComments("密级");
		secretLevelColumn.setOrderCode(16);
		columns.add(secretLevelColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", DataTypeConstants.STRING, 2000);
		descsColumn.setName("描述");
		descsColumn.setComments("描述");
		descsColumn.setOrderCode(17);
		columns.add(descsColumn);
		
		ComColumndata deptIdColumn = new ComColumndata("dept_id", DataTypeConstants.STRING, 32);
		deptIdColumn.setName("主要所属的部门id");
		deptIdColumn.setComments("主要所属的部门id：关系表中，isMain=1的部门id");
		deptIdColumn.setOrderCode(18);
		columns.add(deptIdColumn);
		
		ComColumndata positionIdColumn = new ComColumndata("position_id", DataTypeConstants.STRING, 32);
		positionIdColumn.setName("主要所属的职务id");
		positionIdColumn.setComments("主要所属的岗位id：关系表中，isMain=1的职务id");
		positionIdColumn.setOrderCode(19);
		columns.add(positionIdColumn);
		
		ComColumndata orgIdColumn = new ComColumndata("org_id", DataTypeConstants.STRING, 32);
		orgIdColumn.setName("主要所属的组织id");
		orgIdColumn.setComments("即所属主要部门关联的组织id");
		orgIdColumn.setOrderCode(20);
		columns.add(orgIdColumn);
		
		ComColumndata isDeleteColumn = new ComColumndata("is_delete", DataTypeConstants.INTEGER, 1);
		isDeleteColumn.setName("是否被删除");
		isDeleteColumn.setComments("逻辑删除，默认值为0");
		isDeleteColumn.setDefaultValue("0");
		columns.add(isDeleteColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("用户信息表");
		table.setComments("用户信息表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_USER";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysUser";
	}
	
	public String validNotNullProps() {
		if(isCreateAccount == 1 && StrUtils.isEmpty(workNo)){
			return "工号不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	@JSONField(serialize = false)
	public String getName(){
		if(StrUtils.notEmpty(realName)){
			return realName;
		}
		if(StrUtils.notEmpty(workNo)){
			return workNo;
		}
		if(StrUtils.notEmpty(nikeName)){
			return nikeName;
		}
		if(StrUtils.notEmpty(email)){
			return email;
		}
		return null;
	}
}
