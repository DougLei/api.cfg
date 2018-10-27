package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 用户组明细表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysUserGroupDetail extends BasicEntity implements IEntity{

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
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9);
		
		CfgColumn userGroupIdColumn = new CfgColumn("user_group_id", DataTypeConstants.STRING, 32);
		userGroupIdColumn.setName("用户组主键");
		userGroupIdColumn.setComments("用户组主键");
		columns.add(userGroupIdColumn);
		
		CfgColumn userIdColumn = new CfgColumn("user_id", DataTypeConstants.STRING, 32);
		userIdColumn.setName("人员主键");
		userIdColumn.setComments("人员主键");
		columns.add(userIdColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
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
