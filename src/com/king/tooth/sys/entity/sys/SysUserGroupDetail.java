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
	/**
	 * 账户主键
	 */
	private String accountId;
	
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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_USER_GROUP_DETAIL", 0);
		table.setName("用户组明细表");
		table.setComments("用户组明细表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10);
		
		ComColumndata userGroupIdColumn = new ComColumndata("user_group_id", BuiltinDataType.STRING, 32);
		userGroupIdColumn.setName("用户组主键");
		userGroupIdColumn.setComments("用户组主键");
		columns.add(userGroupIdColumn);
		
		ComColumndata userIdColumn = new ComColumndata("user_id", BuiltinDataType.STRING, 32);
		userIdColumn.setName("人员主键");
		userIdColumn.setComments("人员主键");
		columns.add(userIdColumn);
		
		ComColumndata accountIdColumn = new ComColumndata("account_id", BuiltinDataType.STRING, 32);
		accountIdColumn.setName("账户主键");
		accountIdColumn.setComments("账户主键");
		columns.add(accountIdColumn);
		
		table.setColumns(columns);
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
