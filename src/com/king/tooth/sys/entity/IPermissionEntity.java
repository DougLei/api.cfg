package com.king.tooth.sys.entity;

/**
 * 权限实体接口
 * @author DougLei
 */
public interface IPermissionEntity {
	
	String getRefResourceId();
	String getRefResourceCode();
	
	String getRefParentResourceId();
	String getRefParentResourceCode();

	/**
	 * 是否修改了权限信息
	 * @param oldPermissionEntity
	 * @return
	 */
	boolean isChangePermissionInfo(IPermissionEntity oldPermissionEntity);
}
