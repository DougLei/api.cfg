package com.king.tooth.sys.entity.sys.permission;

import java.util.List;

import com.king.tooth.sys.entity.sys.SysPermission;

/**
 * 权限信息扩展对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysPermissionExtend extends SysPermission {
	
	/**
	 * 子权限集合
	 */
	private List<SysPermissionExtend> children;

	public List<SysPermissionExtend> getChildren() {
		return children;
	}
	public void setChildren(List<SysPermissionExtend> children) {
		this.children = children;
	}
}
