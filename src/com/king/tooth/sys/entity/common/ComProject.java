package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgCustomer;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]项目信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProject extends BasicEntity implements ITable{
	
	/**
	 * 所属的客户主键
	 * <p>只有所属的客户，才能对项目进行修改，其他的都只能查看和使用</p>
	 */
	private String ownerCustomerId;
	/**
	 * 关联的数据库主键
	 */
	private String databaseId;
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 项目描述
	 */
	private String descs;
	/**
	 * 项目进度
	 * 	1.调研、2.设计、3.开发、4.测试、5.试运行、6.上线、7.验收...
	 */
	private int progressStatus;
	/**
	 * 启项时间
	 */
	private Date startDate;
	/**
	 * 预计结项时间
	 */
	private Date planEndDate;
	/**
	 * 实际结项时间
	 */
	private Date endDate;
	/**
	 * 版本
	 */
	private int version;
	/**
	 * 是否测试
	 * <p>到测试平台</p>
	 */
	private int isTest;
	/**
	 * 是否部署
	 * <p>到运行平台</p>
	 */
	private int isDeployment;
	
	//-----------------------------------------------------------
	
	/**
	 * 所属客户
	 */
	private CfgCustomer customer;
	/**
	 * 所包含的模块集合
	 */
	private List<ComProjectModule> projectModules;
	/**
	 * 所属的数据库
	 */
	private ComDatabase database;
	
	
	public ComProject() {
		this.version = 1;
	}
	public ComProject(String id, String databaseId) {
		this.id = id;
		this.databaseId = databaseId;
	}

	public List<ComProjectModule> getProjectModules() {
		return projectModules;
	}
	public void setProjectModules(List<ComProjectModule> projectModules) {
		this.projectModules = projectModules;
	}
	public CfgCustomer getCustomer() {
		return customer;
	}
	public int getIsDeployment() {
		return isDeployment;
	}
	public void setIsDeployment(int isDeployment) {
		this.isDeployment = isDeployment;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
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
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setCustomer(CfgCustomer customer) {
		this.customer = customer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	public String getDescs() {
		return descs;
	}
	public ComDatabase getDatabase() {
		return database;
	}
	public void setDatabase(ComDatabase database) {
		this.database = database;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public int getProgressStatus() {
		return progressStatus;
	}
	public void setProgressStatus(int progressStatus) {
		this.progressStatus = progressStatus;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getPlanEndDate() {
		return planEndDate;
	}
	public int getIsTest() {
		return isTest;
	}
	public void setIsTest(int isTest) {
		this.isTest = isTest;
	}
	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public String getOwnerCustomerId() {
		return ownerCustomerId;
	}
	public void setOwnerCustomerId(String ownerCustomerId) {
		this.ownerCustomerId = ownerCustomerId;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT");
		table.setName("[通用的]项目信息资源对象表");
		table.setComments("[通用的]项目信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(16);
		
		CfgColumndata ownerCustomerIdColumn = new CfgColumndata("owner_customer_id");
		ownerCustomerIdColumn.setName("所属的客户主键");
		ownerCustomerIdColumn.setComments("所属的客户主键：只有所属的客户，才能对项目进行修改，其他的都只能查看和使用");
		ownerCustomerIdColumn.setColumnType(DataTypeConstants.STRING);
		ownerCustomerIdColumn.setLength(32);
		ownerCustomerIdColumn.setOrderCode(1);
		columns.add(ownerCustomerIdColumn);
		
		CfgColumndata databaseIdColumn = new CfgColumndata("database_id");
		databaseIdColumn.setName("关联的数据库主键");
		databaseIdColumn.setComments("关联的数据库主键");
		databaseIdColumn.setColumnType(DataTypeConstants.STRING);
		databaseIdColumn.setLength(32);
		databaseIdColumn.setOrderCode(2);
		columns.add(databaseIdColumn);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("项目名称");
		nameColumn.setComments("项目名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(200);
		nameColumn.setOrderCode(3);
		columns.add(nameColumn);
		
		CfgColumndata descsColumn = new CfgColumndata("descs");
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(800);
		descsColumn.setOrderCode(4);
		columns.add(descsColumn);
		
		CfgColumndata progressStatusColumn = new CfgColumndata("progress_status");
		progressStatusColumn.setName("项目进度");
		progressStatusColumn.setComments("项目进度：1.调研、2.设计、3.开发、4.测试、5.试运行、6.上线、7.验收...");
		progressStatusColumn.setColumnType(DataTypeConstants.INTEGER);
		progressStatusColumn.setLength(1);
		progressStatusColumn.setOrderCode(5);
		columns.add(progressStatusColumn);
		
		CfgColumndata startDateColumn = new CfgColumndata("start_date");
		startDateColumn.setName("启项时间");
		startDateColumn.setComments("启项时间");
		startDateColumn.setColumnType(DataTypeConstants.DATE);
		startDateColumn.setOrderCode(6);
		columns.add(startDateColumn);
		
		CfgColumndata planEndDateColumn = new CfgColumndata("plan_end_date");
		planEndDateColumn.setName("预计结项时间");
		planEndDateColumn.setComments("预计结项时间");
		planEndDateColumn.setColumnType(DataTypeConstants.DATE);
		planEndDateColumn.setOrderCode(7);
		columns.add(planEndDateColumn);
		
		CfgColumndata endDateColumn = new CfgColumndata("end_date");
		endDateColumn.setName("实际结项时间");
		endDateColumn.setComments("实际结项时间");
		endDateColumn.setColumnType(DataTypeConstants.DATE);
		endDateColumn.setOrderCode(8);
		columns.add(endDateColumn);
		
		CfgColumndata isTestColumn = new CfgColumndata("is_test");
		isTestColumn.setName("是否测试");
		isTestColumn.setComments("是否测试：到测试平台");
		isTestColumn.setColumnType(DataTypeConstants.INTEGER);
		isTestColumn.setLength(1);
		isTestColumn.setOrderCode(9);
		columns.add(isTestColumn);
		
		CfgColumndata isDeploymentColumn = new CfgColumndata("is_deployment");
		isDeploymentColumn.setName("是否部署");
		isDeploymentColumn.setComments("是否部署：到运行平台");
		isDeploymentColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentColumn.setLength(1);
		isDeploymentColumn.setOrderCode(10);
		columns.add(isDeploymentColumn);
		
		CfgColumndata versionColumn = new CfgColumndata("version");
		versionColumn.setName("版本");
		versionColumn.setComments("版本");
		versionColumn.setColumnType(DataTypeConstants.INTEGER);
		versionColumn.setLength(3);
		versionColumn.setOrderCode(11);
		columns.add(versionColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT";
	}
}
