package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 权限优先级资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPermissionPriority extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 权限类型
	 * <p>权限类型：比如角色role，帐号account，部门dept，岗位position，帐号组accountGroup等</p>
	 * @see BuiltinPermissionType
	 */
	private String permissionType;
	/**
	 * 优先等级
	 * <p>优先等级，越高越优先</p>
	 */
	private Integer lv;
	/**
	 * 相同权限类型的优先级
	 * <p>相同权限类型的优先级：这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高；如果这个字段没有值，则默认以对应权限类型的orderCode来决定优先级，orderCode越低的，优先级越高</p>
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
	
	public ComPermissionPriority() {
	}
	public ComPermissionPriority(String permissionType, Integer lv) {
		this.permissionType = permissionType;
		this.lv = lv;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PERMISSION_PRIORITY", 0);
		table.setName("权限优先级资源对象表");
		table.setComments("权限优先级资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10);
		
		ComColumndata permissionTypeColumn = new ComColumndata("permission_type", BuiltinCodeDataType.STRING, 20);
		permissionTypeColumn.setName("优先等级");
		permissionTypeColumn.setComments("优先等级，越高越优先");
		permissionTypeColumn.setOrderCode(1);
		columns.add(permissionTypeColumn);
		
		ComColumndata lvColumn = new ComColumndata("lv", BuiltinCodeDataType.INTEGER, 2);
		lvColumn.setName("关联的数据id");
		lvColumn.setComments("关联的数据id：比如角色id，帐号id，部门id，岗位id，帐号组id等");
		lvColumn.setOrderCode(2);
		columns.add(lvColumn);
		
		ComColumndata samePermissionTypeLvColumn = new ComColumndata("same_permission_type_lv", BuiltinCodeDataType.CLOB, 0);
		samePermissionTypeLvColumn.setName("相同权限类型的优先级");
		samePermissionTypeLvColumn.setComments("相同权限类型的优先级：这里可以按照等级顺序，存储多个id，用,分割，越前面的，优先级越高；第一个优先级最高；如果这个字段没有值，则默认以对应权限类型的orderCode来决定优先级，orderCode越低的，优先级越高");
		samePermissionTypeLvColumn.setOrderCode(3);
		columns.add(samePermissionTypeLvColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_PERMISSION_PRIORITY";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComPermissionPriority";
	}
}
