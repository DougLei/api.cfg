package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

/**
 * 权限优先级信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysPermissionPriority extends BasicEntity implements IEntity{
	
	/**
	 * 权限类型
	 * <p>比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等</p>
	 * @see BuiltinPermissionType
	 */
	private String permissionType;
	/**
	 * 优先等级
	 * <p>越低越优先</p>
	 */
	private Integer lv;
	/**
	 * 相同权限类型的优先级
	 * <p>这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高；如果这个字段没有值，则默认以对应权限类型的orderCode来决定优先级，orderCode越低的，优先级越高</p>
	 */
	private String samePermissionTypeLv;
	
	// ---------------------------------------------------------------------------
	public String getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
	}
	public String getSamePermissionTypeLv() {
		return samePermissionTypeLv;
	}
	public void setSamePermissionTypeLv(String samePermissionTypeLv) {
		this.samePermissionTypeLv = samePermissionTypeLv;
	}
	
	public SysPermissionPriority() {
	}
	public SysPermissionPriority(String permissionType, Integer lv) {
		this.permissionType = permissionType;
		this.lv = lv;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(10);
		
		CfgColumn permissionTypeColumn = new CfgColumn("permission_type", DataTypeConstants.STRING, 20);
		permissionTypeColumn.setName("权限类型");
		permissionTypeColumn.setComments("比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等");
		permissionTypeColumn.setOrderCode(1);
		columns.add(permissionTypeColumn);
		
		CfgColumn lvColumn = new CfgColumn("lv", DataTypeConstants.INTEGER, 2);
		lvColumn.setName("优先等级");
		lvColumn.setComments("越低越优先");
		lvColumn.setOrderCode(2);
		columns.add(lvColumn);
		
		CfgColumn samePermissionTypeLvColumn = new CfgColumn("same_permission_type_lv", DataTypeConstants.CLOB, 0);
		samePermissionTypeLvColumn.setName("相同权限类型的优先级");
		samePermissionTypeLvColumn.setComments("这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高；如果这个字段没有值，则默认以对应权限类型的orderCode来决定优先级，orderCode越低的，优先级越高");
		samePermissionTypeLvColumn.setOrderCode(3);
		columns.add(samePermissionTypeLvColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("权限优先级信息表");
		table.setRemark("权限优先级信息表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_PERMISSION_PRIORITY";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysPermissionPriority";
	}
}
