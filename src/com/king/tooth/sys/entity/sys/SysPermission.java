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
 * 权限信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysPermission extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 关联的数据id：比如角色id，帐号id，部门id，岗位id，帐号组id等
	 */
	private String refDataId;
	/**
	 * 关联的数据类型：比如角色role，帐号account，部门dept，岗位position，帐号组accountGroup等
	 */
	private String refDataType;
	/**
	 * 关联的资源id，比如某个模块的id，某个功能的id等
	 */
	private String refResourceId;
	/**
	 * 关联的资源code，全项目唯一
	 */
	private String refResourceCode;
	/**
	 * 关联的父资源id
	 */
	private String refParentResourceId;
	/**
	 * 关联的父资源code
	 */
	private String refParentResourceCode;
	/**
	 * 关联的资源类型：1:模块module、2:tab、3:功能oper  等
	 */
	private String refResourceType;
	/**
	 * 是否可见(是否可读)
	 */
	private Integer isVisibility;
	/**
	 * 是否可操作(是否可写)
	 */
	private Integer isOper;
	/**
	 * 密级：多个用,隔开
	 */
	private String secretLevels;
	
	// ---------------------------------------------------------------------------
	/**
	 * 子权限集合
	 */
	private List<SysPermission> children;
	
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public Integer getIsVisibility() {
		return isVisibility;
	}
	public void setIsVisibility(Integer isVisibility) {
		this.isVisibility = isVisibility;
	}
	public Integer getIsOper() {
		return isOper;
	}
	public void setIsOper(Integer isOper) {
		this.isOper = isOper;
	}
	public String getSecretLevels() {
		return secretLevels;
	}
	public void setSecretLevels(String secretLevels) {
		this.secretLevels = secretLevels;
	}
	public String getRefDataId() {
		return refDataId;
	}
	public void setRefDataId(String refDataId) {
		this.refDataId = refDataId;
	}
	public String getRefDataType() {
		return refDataType;
	}
	public void setRefDataType(String refDataType) {
		this.refDataType = refDataType;
	}
	public String getRefResourceCode() {
		return refResourceCode;
	}
	public void setRefResourceCode(String refResourceCode) {
		this.refResourceCode = refResourceCode;
	}
	public String getRefParentResourceId() {
		return refParentResourceId;
	}
	public void setRefParentResourceId(String refParentResourceId) {
		this.refParentResourceId = refParentResourceId;
	}
	public String getRefParentResourceCode() {
		return refParentResourceCode;
	}
	public void setRefParentResourceCode(String refParentResourceCode) {
		this.refParentResourceCode = refParentResourceCode;
	}
	public String getRefResourceType() {
		return refResourceType;
	}
	public void setRefResourceType(String refResourceType) {
		this.refResourceType = refResourceType;
	}
	public List<SysPermission> gainChildren() {
		return children;
	}
	public void setChildren(List<SysPermission> children) {
		this.children = children;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_PERMISSION", 0);
		table.setName("权限信息表");
		table.setComments("权限信息表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata refDataIdColumn = new ComColumndata("ref_data_id", BuiltinCodeDataType.STRING, 32);
		refDataIdColumn.setName("关联的数据id");
		refDataIdColumn.setComments("关联的数据id：比如角色id，帐号id，部门id，岗位id，帐号组id等");
		refDataIdColumn.setOrderCode(1);
		columns.add(refDataIdColumn);
		
		ComColumndata refDataTypeColumn = new ComColumndata("ref_data_type", BuiltinCodeDataType.STRING, 20);
		refDataTypeColumn.setName("关联的数据类型");
		refDataTypeColumn.setComments("关联的数据类型：比如角色role，帐号account，部门dept，岗位position，帐号组accountGroup等");
		refDataTypeColumn.setOrderCode(2);
		columns.add(refDataTypeColumn);
		
		ComColumndata refResourceIdColumn = new ComColumndata("ref_resource_id", BuiltinCodeDataType.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("关联的资源id，比如某个模块的id，某个功能的id等");
		refResourceIdColumn.setOrderCode(3);
		columns.add(refResourceIdColumn);
		
		ComColumndata refResourceCodeColumn = new ComColumndata("ref_resource_code", BuiltinCodeDataType.STRING, 100);
		refResourceCodeColumn.setName("关联的资源编码");
		refResourceCodeColumn.setComments("关联的资源code，全项目唯一");
		refResourceCodeColumn.setOrderCode(4);
		columns.add(refResourceCodeColumn);
		
		ComColumndata refParentResourceIdColumn = new ComColumndata("ref_parent_resource_id", BuiltinCodeDataType.STRING, 32);
		refParentResourceIdColumn.setName("关联的父资源id");
		refParentResourceIdColumn.setComments("关联的父资源id");
		refParentResourceIdColumn.setOrderCode(5);
		columns.add(refParentResourceIdColumn);
		
		ComColumndata refParentResourceCodeColumn = new ComColumndata("ref_parent_resource_code", BuiltinCodeDataType.STRING, 100);
		refParentResourceCodeColumn.setName("关联的父资源编码");
		refParentResourceCodeColumn.setComments("关联的父资源code");
		refParentResourceCodeColumn.setOrderCode(6);
		columns.add(refParentResourceCodeColumn);
		
		ComColumndata refResourceTypeColumn = new ComColumndata("ref_resource_type", BuiltinCodeDataType.STRING, 20);
		refResourceTypeColumn.setName("关联的资源类型");
		refResourceTypeColumn.setComments("关联的资源类型：1:模块module、2:tab、3:功能oper  等");
		refResourceTypeColumn.setOrderCode(7);
		columns.add(refResourceTypeColumn);
		
		ComColumndata isVisibilityColumn = new ComColumndata("is_visibility", BuiltinCodeDataType.INTEGER, 1);
		isVisibilityColumn.setName("是否可见(是否可读)");
		isVisibilityColumn.setComments("是否可见(是否可读)");
		isVisibilityColumn.setOrderCode(8);
		columns.add(isVisibilityColumn);
		
		ComColumndata isOperColumn = new ComColumndata("is_oper", BuiltinCodeDataType.INTEGER, 1);
		isOperColumn.setName("是否可操作(是否可写)");
		isOperColumn.setComments("是否可操作(是否可写)");
		isOperColumn.setOrderCode(9);
		columns.add(isOperColumn);
		
		ComColumndata secretLevelsColumn = new ComColumndata("secret_levels", BuiltinCodeDataType.STRING, 32);
		secretLevelsColumn.setName("密级");
		secretLevelsColumn.setComments("密级：多个用,隔开");
		secretLevelsColumn.setOrderCode(10);
		columns.add(secretLevelsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_PERMISSION";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysPermission";
	}
}