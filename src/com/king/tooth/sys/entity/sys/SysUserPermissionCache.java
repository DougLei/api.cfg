package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.sys.entity.sys.permission.SysPermissionExtend;

/**
 * 系统用户权限缓存表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUserPermissionCache extends BasicEntity implements ITable, IEntity{

	/**
	 * 用户主键
	 */
	private String userId;
	
	/**
	 * 用户所拥有的权限json
	 */
	private String permission;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 权限对象
	 */
	@JSONField(serialize = false)
	private SysPermissionExtend permissionObject;
	
	public String getPermission() {
		return permission;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public SysPermissionExtend getPermissionObject() {
		return permissionObject;
	}
	public void setPermissionObject(SysPermissionExtend permissionObject) {
		this.permissionObject = permissionObject;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9);
		
		CfgColumn userIdColumn = new CfgColumn("user_id", DataTypeConstants.STRING, 32);
		userIdColumn.setName("用户主键");
		userIdColumn.setComments("用户主键");
		columns.add(userIdColumn);
		
		CfgColumn permissionColumn = new CfgColumn("permission", DataTypeConstants.CLOB, 0);
		permissionColumn.setName("用户所拥有的权限json");
		permissionColumn.setComments("用户所拥有的权限json");
		columns.add(permissionColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("系统用户权限缓存表");
		table.setComments("系统用户权限缓存表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_USER_PERMISSION_CACHE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysUserPermissionCache";
	}
}