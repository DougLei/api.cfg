package com.api.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

/**
 * 权限信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysPermission extends BasicEntity implements IEntity{
	
	/**
	 * 主体id：比如用户id，账户id，角色id，部门id，岗位id，用户组id等
	 */
	private String objId;
	/**
	 * 主体类型：比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等
	 */
	private String objType;
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
	 * 关联的资源类型：模块module、页签tab、功能oper、字段field  等
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
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
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
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(17);
		
		CfgColumn objIdColumn = new CfgColumn("obj_id", DataTypeConstants.STRING, 32);
		objIdColumn.setName("主体id");
		objIdColumn.setComments("比如用户id，账户id，角色id，部门id，岗位id，用户组id等");
		columns.add(objIdColumn);
		
		CfgColumn objTypeColumn = new CfgColumn("obj_type", DataTypeConstants.STRING, 12);
		objTypeColumn.setName("主体类型");
		objTypeColumn.setComments("比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等");
		columns.add(objTypeColumn);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("关联的资源id，比如某个模块的id，某个功能的id等");
		refResourceIdColumn.setOrderCode(3);
		columns.add(refResourceIdColumn);
		
		CfgColumn refResourceCodeColumn = new CfgColumn("ref_resource_code", DataTypeConstants.STRING, 100);
		refResourceCodeColumn.setName("关联的资源编码");
		refResourceCodeColumn.setComments("关联的资源code，全项目唯一");
		refResourceCodeColumn.setOrderCode(4);
		columns.add(refResourceCodeColumn);
		
		CfgColumn refParentResourceIdColumn = new CfgColumn("ref_parent_resource_id", DataTypeConstants.STRING, 32);
		refParentResourceIdColumn.setName("关联的父资源id");
		refParentResourceIdColumn.setComments("关联的父资源id");
		refParentResourceIdColumn.setOrderCode(5);
		columns.add(refParentResourceIdColumn);
		
		CfgColumn refParentResourceCodeColumn = new CfgColumn("ref_parent_resource_code", DataTypeConstants.STRING, 100);
		refParentResourceCodeColumn.setName("关联的父资源编码");
		refParentResourceCodeColumn.setComments("关联的父资源code");
		refParentResourceCodeColumn.setOrderCode(6);
		columns.add(refParentResourceCodeColumn);
		
		CfgColumn refResourceTypeColumn = new CfgColumn("ref_resource_type", DataTypeConstants.STRING, 20);
		refResourceTypeColumn.setName("关联的资源类型");
		refResourceTypeColumn.setComments("关联的资源类型：模块module、页签tab、功能oper、字段field  等");
		refResourceTypeColumn.setOrderCode(7);
		columns.add(refResourceTypeColumn);
		
		CfgColumn isVisibilityColumn = new CfgColumn("is_visibility", DataTypeConstants.INTEGER, 1);
		isVisibilityColumn.setName("是否可见(是否可读)");
		isVisibilityColumn.setComments("是否可见(是否可读)");
		isVisibilityColumn.setOrderCode(8);
		columns.add(isVisibilityColumn);
		
		CfgColumn isOperColumn = new CfgColumn("is_oper", DataTypeConstants.INTEGER, 1);
		isOperColumn.setName("是否可操作(是否可写)");
		isOperColumn.setComments("是否可操作(是否可写)");
		isOperColumn.setOrderCode(9);
		columns.add(isOperColumn);
		
		CfgColumn secretLevelsColumn = new CfgColumn("secret_levels", DataTypeConstants.STRING, 32);
		secretLevelsColumn.setName("密级");
		secretLevelsColumn.setComments("密级：多个用,隔开");
		secretLevelsColumn.setOrderCode(10);
		columns.add(secretLevelsColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("权限信息表");
		table.setRemark("权限信息表");
		
		
		table.setColumns(getColumnList());
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
