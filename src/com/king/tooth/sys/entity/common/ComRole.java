package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]角色资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComRole extends BasicEntity implements ITable{
	
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色编码
	 */
	private String code;
	/**
	 * 角色描述
	 */
	private String descs;
	/**
	 * 排序值
	 */
	private int orderCode;
	/**
	 * 是否启用
	 */
	private int isEnabled;

	// ---------------------------------------------------------------------------
	
	/**
	 * 角色拥有的权限集合
	 */
	private List<ComPermission> permissions;
	
	public ComRole(String id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}
	public ComRole() {
		this.isEnabled = 1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
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
	public int getOrderCode() {
		return orderCode;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<ComPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<ComPermission> permissions) {
		this.permissions = permissions;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}

	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_ROLE");
		table.setName("[通用的]角色资源对象表");
		table.setComments("[通用的]角色资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(11);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("角色名称");
		nameColumn.setComments("角色名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(40);
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumndata codeColumn = new CfgColumndata("code");
		codeColumn.setName("角色编码");
		codeColumn.setComments("角色编码");
		codeColumn.setColumnType(DataTypeConstants.STRING);
		codeColumn.setLength(20);
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		CfgColumndata descsColumn = new CfgColumndata("descs");
		descsColumn.setName("角色描述");
		descsColumn.setComments("角色描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(100);
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(1);
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(5);
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_ROLE";
	}
}
