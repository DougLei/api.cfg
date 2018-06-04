package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.StrUtils;

/**
 * [通用的]项目信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProject extends AbstractSysResource implements ITable{
	
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
	
	//-----------------------------------------------------------
	
	public ComProject() {
	}
	public ComProject(String id, String databaseId) {
		this.id = id;
		this.databaseId = databaseId;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(StrUtils.isEmpty(name)){
			throw new NullPointerException("项目名称不能为空");
		}
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
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public String getOwnerCustomerId() {
		return ownerCustomerId;
	}
	public void setOwnerCustomerId(String ownerCustomerId) {
		this.ownerCustomerId = ownerCustomerId;
	}
	public int getIsDeploymentTest() {
		return isDeploymentTest;
	}
	public void setIsDeploymentTest(int isDeploymentTest) {
		this.isDeploymentTest = isDeploymentTest;
	}
	public int getIsDeploymentRun() {
		return isDeploymentRun;
	}
	public void setIsDeploymentRun(int isDeploymentRun) {
		this.isDeploymentRun = isDeploymentRun;
	}

	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT");
		table.setName("[通用的]项目信息资源对象表");
		table.setComments("[通用的]项目信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(11);
		
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
		
		CfgColumndata isDeploymentTestColumn = new CfgColumndata("is_deployment_test");
		isDeploymentTestColumn.setName("是否部署到测试环境");
		isDeploymentTestColumn.setComments("是否部署到测试环境");
		isDeploymentTestColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentTestColumn.setLength(1);
		isDeploymentTestColumn.setOrderCode(5);
		columns.add(isDeploymentTestColumn);

		CfgColumndata isDeploymentRunColumn = new CfgColumndata("is_deployment_run");
		isDeploymentRunColumn.setName("是否部署到正式环境");
		isDeploymentRunColumn.setComments("是否部署到正式环境");
		isDeploymentRunColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentRunColumn.setLength(1);
		isDeploymentRunColumn.setOrderCode(6);
		columns.add(isDeploymentRunColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT";
	}
	
	public int getResourceType() {
		return PROJECT;
	}
	
	public String getResourceName() {
		return name;
	}
}
