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
 * 用户信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUser extends BasicEntity implements IEntity, IEntityPropAnalysis{
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
	 * <p>默认值为1，1.在职、2.离职、3.休假，4.其他</p>
	 */
	private Integer status;
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
	 * 主要所属的角色id
	 * <p>关系表中，isMain=1的角色id</p>
	 */
	private String roleId;
	/**
	 * 是否被删除
	 * <p>逻辑删除，默认值为0</p>
	 */
	private Integer isDelete;
	/**
	 * 备用字段01
	 */
	private String backup01;
	/**
	 * 备用字段02
	 */
	private String backup02;
	
	// ---------------------------------------------------------------------------

	/**
	 * 是否创建账户
	 */
	@JSONField(serialize = false)
	private int isCreateAccount;
	
	/**
	 * 如果修改了用户工号，是否同步修改账户的登录名，这个要配合上面的isCreateAccount一起使用，即当isCreateAccount=1时，该值=1才有效，1是0否
	 */
	@JSONField(serialize = false)
	private int isSyncLoginName;
	
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public void setIsCreateAccount(int isCreateAccount) {
		this.isCreateAccount = isCreateAccount;
	}
	public int getIsSyncLoginName() {
		return isSyncLoginName;
	}
	public void setIsSyncLoginName(int isSyncLoginName) {
		this.isSyncLoginName = isSyncLoginName;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public String getBackup01() {
		return backup01;
	}
	public void setBackup01(String backup01) {
		this.backup01 = backup01;
	}
	public String getBackup02() {
		return backup02;
	}
	public void setBackup02(String backup02) {
		this.backup02 = backup02;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(23+7);
		
		CfgColumn nikeNameColumn = new CfgColumn("nike_name", DataTypeConstants.STRING, 50);
		nikeNameColumn.setName("昵称");
		nikeNameColumn.setComments("昵称");
		columns.add(nikeNameColumn);
		
		CfgColumn realNameColumn = new CfgColumn("real_name", DataTypeConstants.STRING, 100);
		realNameColumn.setName("真实姓名");
		realNameColumn.setComments("真实姓名");
		columns.add(realNameColumn);
		
		CfgColumn ageColumn = new CfgColumn("age", DataTypeConstants.INTEGER, 1);
		ageColumn.setName("年龄");
		ageColumn.setComments("年龄");
		columns.add(ageColumn);
		
		CfgColumn sexColumn = new CfgColumn("sex", DataTypeConstants.INTEGER, 1);
		sexColumn.setName("性别");
		sexColumn.setComments("性别");
		columns.add(sexColumn);
		
		CfgColumn officePhoneColumn = new CfgColumn("office_phone", DataTypeConstants.STRING, 32);
		officePhoneColumn.setName("办公电话");
		officePhoneColumn.setComments("办公电话");
		columns.add(officePhoneColumn);
		
		CfgColumn telColumn = new CfgColumn("tel", DataTypeConstants.STRING, 20);
		telColumn.setName("手机号");
		telColumn.setComments("手机号");
		columns.add(telColumn);
		
		CfgColumn emailColumn = new CfgColumn("email", DataTypeConstants.STRING, 80);
		emailColumn.setName("邮箱");
		emailColumn.setComments("邮箱");
		columns.add(emailColumn);
		
		CfgColumn workAddrColumn = new CfgColumn("work_addr", DataTypeConstants.STRING, 200);
		workAddrColumn.setName("办公地点");
		workAddrColumn.setComments("办公地点");
		columns.add(workAddrColumn);
		
		CfgColumn liveAddrColumn = new CfgColumn("live_addr", DataTypeConstants.STRING, 200);
		liveAddrColumn.setName("居住地点");
		liveAddrColumn.setComments("居住地点");
		columns.add(liveAddrColumn);
		
		CfgColumn idCardNoColumn = new CfgColumn("id_card_no", DataTypeConstants.STRING, 40);
		idCardNoColumn.setName("身份证号码");
		idCardNoColumn.setComments("身份证号码");
		idCardNoColumn.setOrderCode(11);
		columns.add(idCardNoColumn);
		
		CfgColumn employedDateColumn = new CfgColumn("employed_date", DataTypeConstants.DATE, 0);
		employedDateColumn.setName("入职时间");
		employedDateColumn.setComments("入职时间");
		columns.add(employedDateColumn);
		
		CfgColumn statusColumn = new CfgColumn("status", DataTypeConstants.INTEGER, 1);
		statusColumn.setName("人员状态");
		statusColumn.setComments("默认值为1，1.在职、2.离职、3.休假，4.其他");
		statusColumn.setDefaultValue("1");
		columns.add(statusColumn);
		
		CfgColumn monthSalarColumn = new CfgColumn("month_salar", DataTypeConstants.DOUBLE, 10);
		monthSalarColumn.setName("月薪");
		monthSalarColumn.setComments("月薪");
		monthSalarColumn.setPrecision(2);
		columns.add(monthSalarColumn);
		
		CfgColumn workNoColumn = new CfgColumn("work_no", DataTypeConstants.STRING, 64);
		workNoColumn.setName("工号");
		workNoColumn.setComments("工号");
		workNoColumn.setIsNullabled(0);
		columns.add(workNoColumn);
		
		CfgColumn secretLevelColumn = new CfgColumn("secret_level", DataTypeConstants.INTEGER, 1);
		secretLevelColumn.setName("密级");
		secretLevelColumn.setComments("密级");
		columns.add(secretLevelColumn);
		
		CfgColumn descsColumn = new CfgColumn("descs", DataTypeConstants.STRING, 2000);
		descsColumn.setName("描述");
		descsColumn.setComments("描述");
		columns.add(descsColumn);
		
		CfgColumn deptIdColumn = new CfgColumn("dept_id", DataTypeConstants.STRING, 32);
		deptIdColumn.setName("主要所属的部门id");
		deptIdColumn.setComments("主要所属的部门id：关系表中，isMain=1的部门id");
		columns.add(deptIdColumn);
		
		CfgColumn positionIdColumn = new CfgColumn("position_id", DataTypeConstants.STRING, 32);
		positionIdColumn.setName("主要所属的职务id");
		positionIdColumn.setComments("主要所属的岗位id：关系表中，isMain=1的职务id");
		columns.add(positionIdColumn);
		
		CfgColumn orgIdColumn = new CfgColumn("org_id", DataTypeConstants.STRING, 32);
		orgIdColumn.setName("主要所属的组织id");
		orgIdColumn.setComments("即所属主要部门关联的组织id");
		columns.add(orgIdColumn);
		
		CfgColumn roleIdColumn = new CfgColumn("role_id", DataTypeConstants.STRING, 32);
		roleIdColumn.setName("主要所属的角色id");
		roleIdColumn.setComments("关系表中，isMain=1的角色id");
		columns.add(roleIdColumn);
		
		CfgColumn isDeleteColumn = new CfgColumn("is_delete", DataTypeConstants.INTEGER, 1);
		isDeleteColumn.setName("是否被删除");
		isDeleteColumn.setComments("逻辑删除，默认值为0");
		isDeleteColumn.setDefaultValue("0");
		columns.add(isDeleteColumn);
		
		CfgColumn backup01Column = new CfgColumn("backup01", DataTypeConstants.STRING, 200);
		backup01Column.setName("备用字段01");
		backup01Column.setComments("备用字段01");
		columns.add(backup01Column);
		
		CfgColumn backup02Column = new CfgColumn("backup02", DataTypeConstants.STRING, 500);
		backup02Column.setName("备用字段02");
		backup02Column.setComments("备用字段02");
		columns.add(backup02Column);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("用户信息表");
		table.setRemark("用户信息表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
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
	
	/**
	 * 获取用户状态
	 * @return 1启用，2禁用
	 */
	@JSONField(serialize = false)
	public int getUserStatus(){
		if(status == 1){
			return 1;
		}else{
			return 2;
		}
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
