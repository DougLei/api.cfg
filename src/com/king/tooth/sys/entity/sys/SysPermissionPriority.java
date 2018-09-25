package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 权限优先级信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysPermissionPriority extends BasicEntity implements ITable, IEntity{
	
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
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10);
		
		ComColumndata permissionTypeColumn = new ComColumndata("permission_type", BuiltinDataType.STRING, 20);
		permissionTypeColumn.setName("权限类型");
		permissionTypeColumn.setComments("比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等");
		permissionTypeColumn.setOrderCode(1);
		columns.add(permissionTypeColumn);
		
		ComColumndata lvColumn = new ComColumndata("lv", BuiltinDataType.INTEGER, 2);
		lvColumn.setName("优先等级");
		lvColumn.setComments("越低越优先");
		lvColumn.setOrderCode(2);
		columns.add(lvColumn);
		
		ComColumndata samePermissionTypeLvColumn = new ComColumndata("same_permission_type_lv", BuiltinDataType.CLOB, 0);
		samePermissionTypeLvColumn.setName("相同权限类型的优先级");
		samePermissionTypeLvColumn.setComments("这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高；如果这个字段没有值，则默认以对应权限类型的orderCode来决定优先级，orderCode越低的，优先级越高");
		samePermissionTypeLvColumn.setOrderCode(3);
		columns.add(samePermissionTypeLvColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_PERMISSION_PRIORITY", 0);
		table.setName("权限优先级信息表");
		table.setComments("权限优先级信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
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
