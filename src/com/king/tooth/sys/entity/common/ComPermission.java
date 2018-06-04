package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]权限资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPermission extends BasicEntity implements ITable{

	/**
	 * 关联的资源id
	 * <p>可以是模块id(菜单id)，也可以是模块下的操作功能id...</p>
	 */
	private String refResourceId;
	/**
	 * 权限的类型
	 * <p>1.模块(菜单)、2.页面操作(每个页面上的按钮/超链接)...</p>
	 */
	private int permissionType;
	/**
	 * 是否可见(是否可读)
	 */
	private int isVisibility;
	/**
	 * 是否可操作(是否可写)
	 */
	private int isOper;
	/**
	 * 密级
	 * <p>多个用,隔开</p>
	 */
	private String secretLevels;

	// ---------------------------------------------------------------------------
	
	/**
	 * 权限对应的模块资源对象
	 * <p>permissionType=1</p>
	 */
	private ComProjectModule module;
	/**
	 * 权限对应的操作资源对象
	 * <p>permissionType=2</p>
	 */
	private ComModuleOperation operation;
	
	
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public String getSecretLevels() {
		return secretLevels;
	}
	public void setSecretLevels(String secretLevels) {
		this.secretLevels = secretLevels;
	}
	public int getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}
	public int getIsVisibility() {
		return isVisibility;
	}
	public void setIsVisibility(int isVisibility) {
		this.isVisibility = isVisibility;
	}
	public int getIsOper() {
		return isOper;
	}
	public void setIsOper(int isOper) {
		this.isOper = isOper;
	}
	public ComProjectModule getModule() {
		return module;
	}
	public void setModule(ComProjectModule module) {
		this.module = module;
	}
	public ComModuleOperation getOperation() {
		return operation;
	}
	public void setOperation(ComModuleOperation operation) {
		this.operation = operation;
	}

	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PERMISSION");
		table.setName("[通用的]权限资源对象表");
		table.setComments("[通用的]权限资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(10);
		
		CfgColumndata refResourceIdColumn = new CfgColumndata("ref_resource_id");
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("关联的资源id：可以是模块id(菜单id)，也可以是模块下的操作功能id...");
		refResourceIdColumn.setColumnType(DataTypeConstants.STRING);
		refResourceIdColumn.setLength(32);
		refResourceIdColumn.setOrderCode(1);
		columns.add(refResourceIdColumn);
		
		CfgColumndata permissionTypeColumn = new CfgColumndata("permission_type");
		permissionTypeColumn.setName("权限的类型");
		permissionTypeColumn.setComments("权限的类型：1.模块(菜单)、2.页面操作(每个页面上的按钮/超链接)...");
		permissionTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		permissionTypeColumn.setLength(1);
		permissionTypeColumn.setOrderCode(2);
		columns.add(permissionTypeColumn);
		
		CfgColumndata isVisibilityColumn = new CfgColumndata("is_visibility");
		isVisibilityColumn.setName("是否可见(是否可读)");
		isVisibilityColumn.setComments("是否可见(是否可读)");
		isVisibilityColumn.setColumnType(DataTypeConstants.INTEGER);
		isVisibilityColumn.setLength(1);
		isVisibilityColumn.setOrderCode(3);
		columns.add(isVisibilityColumn);
		
		CfgColumndata isOperColumn = new CfgColumndata("is_oper");
		isOperColumn.setName("是否可操作(是否可写)");
		isOperColumn.setComments("是否可操作(是否可写)");
		isOperColumn.setColumnType(DataTypeConstants.INTEGER);
		isOperColumn.setLength(1);
		isOperColumn.setOrderCode(4);
		columns.add(isOperColumn);
		
		CfgColumndata secretLevelsColumn = new CfgColumndata("secret_levels");
		secretLevelsColumn.setName("密级");
		secretLevelsColumn.setComments("密级：多个用,隔开");
		secretLevelsColumn.setColumnType(DataTypeConstants.STRING);
		secretLevelsColumn.setLength(10);
		secretLevelsColumn.setOrderCode(5);
		columns.add(secretLevelsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_PERMISSION";
	}
}
