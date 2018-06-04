package com.king.tooth.sys.entity.run;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [运行系统]组织机构资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RunOrg extends BasicEntity implements ITable{
	/**
	 * 父组织主键
	 */
	private String parentId;
	/**
	 * 组织名称
	 */
	private String name;
	/**
	 * 组织简称
	 */
	private String shortName;
	/**
	 * 组织编码
	 */
	private String code;
	/**
	 * 排序值
	 */
	private int orderCode;
	
	//-------------------------------------------------------------------------
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "RUN_ORG");
		table.setName("[运行系统]组织机构资源对象表");
		table.setComments("[运行系统]组织机构资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(11);
		
		CfgColumndata parentIdColumn = new CfgColumndata("parent_id");
		parentIdColumn.setName("父组织主键");
		parentIdColumn.setComments("父组织主键");
		parentIdColumn.setColumnType(DataTypeConstants.STRING);
		parentIdColumn.setLength(32);
		parentIdColumn.setOrderCode(1);
		columns.add(parentIdColumn);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("组织名称");
		nameColumn.setComments("组织名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(60);
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		CfgColumndata shortNameColumn = new CfgColumndata("short_name");
		shortNameColumn.setName("组织简称");
		shortNameColumn.setComments("组织简称");
		shortNameColumn.setColumnType(DataTypeConstants.STRING);
		shortNameColumn.setLength(30);
		shortNameColumn.setOrderCode(3);
		columns.add(shortNameColumn);
		
		CfgColumndata codeColumn = new CfgColumndata("code");
		codeColumn.setName("组织编码");
		codeColumn.setComments("组织编码");
		codeColumn.setColumnType(DataTypeConstants.STRING);
		codeColumn.setLength(20);
		codeColumn.setOrderCode(4);
		columns.add(codeColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "RUN_ROLE";
	}
}
