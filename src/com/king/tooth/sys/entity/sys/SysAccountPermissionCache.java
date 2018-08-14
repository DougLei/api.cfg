package com.king.tooth.sys.entity.sys;

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
 * 账户权限缓存表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysAccountPermissionCache extends BasicEntity implements ITable, IEntity{

	/**
	 * 账户主键
	 */
	private String accountId;
	
	/**
	 * 账户所拥有的权限json
	 */
	private String permission;
	
	//-------------------------------------------------------------------------
	
	public String getPermission() {
		return permission;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_ACCOUNT_PERMISSION_CACHE", 0);
		table.setName("账户权限缓存表");
		table.setComments("账户权限缓存表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9);
		
		ComColumndata accountIdColumn = new ComColumndata("account_id", BuiltinCodeDataType.STRING, 32);
		accountIdColumn.setName("账户主键");
		accountIdColumn.setComments("账户主键");
		accountIdColumn.setOrderCode(1);
		columns.add(accountIdColumn);
		
		ComColumndata permissionColumn = new ComColumndata("permission", BuiltinCodeDataType.CLOB, 0);
		permissionColumn.setName("账户所拥有的权限json");
		permissionColumn.setComments("账户所拥有的权限json");
		permissionColumn.setOrderCode(2);
		columns.add(permissionColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_ACCOUNT_PERMISSION_CACHE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysAccountPermissionCache";
	}
}