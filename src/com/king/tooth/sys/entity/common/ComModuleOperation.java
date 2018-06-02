package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.AbstractSysResourceEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]模块操作功能资源对象
 * <p>理解为模块下，可点击操作的按钮(或超链接)</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComModuleOperation extends BasicEntity implements ITable{
	
	/**
	 * 所属模块主键
	 */
	private String moduleId;
	/**
	 * 功能名称
	 * <p>例如：添加</p>
	 */
	private String name;
	/**
	 * 功能编码
	 * <p>这个编码的命名，要整个项目唯一，命名规则可以考虑：(模块名+add)等方式，来确保唯一性</p>
	 */
	private String code;
	/**
	 * url
	 */
	private String url;
	/**
	 * 功能图标
	 */
	private String icon;
	/**
	 * 功能是否隐藏
	 * <p>例如查看明细这个功能，没有特定的按钮，是在每条数据的第一列可以打开；或者例如登录功能</p>
	 */
	private int isHide;
	/**
	 * 排序值
	 */
	private int orderCode;
	/**
	 * 是否启用
	 */
	private int isEnabled;
	/**
	 * 是否受到权限约束
	 * <p>例如登录的功能，就不受到权限约束，但是添加的功能，会受到权限约束</p>
	 * <p>默认会受到约束</p>
	 */
	private int isPermissionConstraint;
	
	public ComModuleOperation() {
		this.isPermissionConstraint = 1;
		this.isEnabled = AbstractSysResourceEntity.ENABLED_RESOURCE_STATUS;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsPermissionConstraint() {
		return isPermissionConstraint;
	}
	public void setIsPermissionConstraint(int isPermissionConstraint) {
		this.isPermissionConstraint = isPermissionConstraint;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public int getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}
	public String getModuleId() {
		return moduleId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIsHide() {
		return isHide;
	}
	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_MODULE_OPERATION");
		table.setName("[通用的]模块操作功能资源对象表");
		table.setComments("[通用的]模块操作功能资源对象表：理解为模块下，可点击操作的按钮(或超链接)");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(14);
		
		CfgColumndata moduleIdColumn = new CfgColumndata("module_id");
		moduleIdColumn.setName("所属模块主键");
		moduleIdColumn.setComments("所属模块主键");
		moduleIdColumn.setColumnType(DataTypeConstants.STRING);
		moduleIdColumn.setLength(32);
		moduleIdColumn.setOrderCode(1);
		columns.add(moduleIdColumn);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("功能名称");
		nameColumn.setComments("功能名称：例如：添加");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(20);
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		CfgColumndata codeColumn = new CfgColumndata("code");
		codeColumn.setName("功能编码");
		codeColumn.setComments("功能编码：这个编码的命名，要整个项目唯一，命名规则可以考虑：(模块名+add)等方式，来确保唯一性");
		codeColumn.setColumnType(DataTypeConstants.STRING);
		codeColumn.setLength(100);
		codeColumn.setOrderCode(3);
		columns.add(codeColumn);
		
		CfgColumndata urlColumn = new CfgColumndata("url");
		urlColumn.setName("url");
		urlColumn.setComments("url");
		urlColumn.setColumnType(DataTypeConstants.STRING);
		urlColumn.setLength(50);
		urlColumn.setOrderCode(4);
		columns.add(urlColumn);
		
		CfgColumndata iconColumn = new CfgColumndata("icon");
		iconColumn.setName("功能图标");
		iconColumn.setComments("功能图标");
		iconColumn.setColumnType(DataTypeConstants.STRING);
		iconColumn.setLength(30);
		iconColumn.setOrderCode(5);
		columns.add(iconColumn);
		
		CfgColumndata isHideColumn = new CfgColumndata("is_hide");
		isHideColumn.setName("功能是否隐藏");
		isHideColumn.setComments("功能是否隐藏:例如查看明细这个功能，没有特定的按钮，是在每条数据的第一列可以打开；或者例如登录功能");
		isHideColumn.setColumnType(DataTypeConstants.INTEGER);
		isHideColumn.setLength(1);
		isHideColumn.setOrderCode(6);
		columns.add(isHideColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(2);
		orderCodeColumn.setOrderCode(7);
		columns.add(orderCodeColumn);
		
		CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
		isEnabledColumn.setLength(1);
		isEnabledColumn.setOrderCode(8);
		columns.add(isEnabledColumn);
		
		CfgColumndata isPermissionConstraintColumn = new CfgColumndata("is_permission_constraint");
		isPermissionConstraintColumn.setName("是否受到权限约束");
		isPermissionConstraintColumn.setComments("是否受到权限约束：例如登录的功能，就不受到权限约束，但是添加的功能，会受到权限约束；默认会受到约束");
		isPermissionConstraintColumn.setColumnType(DataTypeConstants.INTEGER);
		isPermissionConstraintColumn.setLength(1);
		isPermissionConstraintColumn.setDefaultValue("1");
		isPermissionConstraintColumn.setOrderCode(9);
		columns.add(isPermissionConstraintColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_MODULE_OPERATION";
	}
}
