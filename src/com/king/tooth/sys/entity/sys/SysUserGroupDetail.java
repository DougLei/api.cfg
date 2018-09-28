package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 用户组明细表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUserGroupDetail extends BasicEntity implements ITable, IEntity{

	/**
	 * 用户组主键
	 */
	private String userGroupId;
	/**
	 * 人员主键
	 */
	private String userId;
	
	//-------------------------------------------------------------------------
	public String getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9);
		
		ComColumndata userGroupIdColumn = new ComColumndata("user_group_id", BuiltinDataType.STRING, 32);
		userGroupIdColumn.setName("用户组主键");
		userGroupIdColumn.setComments("用户组主键");
		columns.add(userGroupIdColumn);
		
		ComColumndata userIdColumn = new ComColumndata("user_id", BuiltinDataType.STRING, 32);
		userIdColumn.setName("人员主键");
		userIdColumn.setComments("人员主键");
		columns.add(userIdColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("用户组明细表");
		table.setComments("用户组明细表");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_USER_GROUP_DETAIL";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysUserGroupDetail";
	}
}
