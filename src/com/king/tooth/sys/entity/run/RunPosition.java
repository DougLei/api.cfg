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
 * [运行系统]职务资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class RunPosition extends BasicEntity implements ITable{

	/**
	 * 职务名称
	 */
	private String name;
	/**
	 * 职务编码
	 */
	private String code;
	/**
	 * 职务描述
	 */
	private String descs;
	/**
	 * 排序值
	 */
	private int orderCode;
	
	//-----------------------------------------------------------
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
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
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public String getDescs() {
		return descs;
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
	public void setDescs(String descs) {
		this.descs = descs;
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
		CfgTabledata table = new CfgTabledata(dbType, "RUN_POSITION");
		table.setName("[运行系统]职务资源对象表");
		table.setComments("[运行系统]职务资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(10);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("职务名称");
		nameColumn.setComments("职务名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(30);
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumndata codeColumn = new CfgColumndata("code");
		codeColumn.setName("职务编码");
		codeColumn.setComments("职务编码");
		codeColumn.setColumnType(DataTypeConstants.STRING);
		codeColumn.setLength(20);
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		CfgColumndata descsColumn = new CfgColumndata("descs");
		descsColumn.setName("职务描述");
		descsColumn.setComments("职务描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(40);
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "RUN_POSITION";
	}
}
