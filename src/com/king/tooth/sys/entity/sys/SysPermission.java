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
 * 权限信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysPermission extends BasicEntity implements ITable, IEntity{
	
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
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(17);
		
		ComColumndata objIdColumn = new ComColumndata("obj_id", BuiltinDataType.STRING, 32);
		objIdColumn.setName("主体id");
		objIdColumn.setComments("比如用户id，账户id，角色id，部门id，岗位id，用户组id等");
		columns.add(objIdColumn);
		
		ComColumndata objTypeColumn = new ComColumndata("obj_type", BuiltinDataType.STRING, 12);
		objTypeColumn.setName("主体类型");
		objTypeColumn.setComments("比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等");
		columns.add(objTypeColumn);
		
		ComColumndata refResourceIdColumn = new ComColumndata("ref_resource_id", BuiltinDataType.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("关联的资源id，比如某个模块的id，某个功能的id等");
		refResourceIdColumn.setOrderCode(3);
		columns.add(refResourceIdColumn);
		
		ComColumndata refResourceCodeColumn = new ComColumndata("ref_resource_code", BuiltinDataType.STRING, 100);
		refResourceCodeColumn.setName("关联的资源编码");
		refResourceCodeColumn.setComments("关联的资源code，全项目唯一");
		refResourceCodeColumn.setOrderCode(4);
		columns.add(refResourceCodeColumn);
		
		ComColumndata refParentResourceIdColumn = new ComColumndata("ref_parent_resource_id", BuiltinDataType.STRING, 32);
		refParentResourceIdColumn.setName("关联的父资源id");
		refParentResourceIdColumn.setComments("关联的父资源id");
		refParentResourceIdColumn.setOrderCode(5);
		columns.add(refParentResourceIdColumn);
		
		ComColumndata refParentResourceCodeColumn = new ComColumndata("ref_parent_resource_code", BuiltinDataType.STRING, 100);
		refParentResourceCodeColumn.setName("关联的父资源编码");
		refParentResourceCodeColumn.setComments("关联的父资源code");
		refParentResourceCodeColumn.setOrderCode(6);
		columns.add(refParentResourceCodeColumn);
		
		ComColumndata refResourceTypeColumn = new ComColumndata("ref_resource_type", BuiltinDataType.STRING, 20);
		refResourceTypeColumn.setName("关联的资源类型");
		refResourceTypeColumn.setComments("关联的资源类型：模块module、页签tab、功能oper、字段field  等");
		refResourceTypeColumn.setOrderCode(7);
		columns.add(refResourceTypeColumn);
		
		ComColumndata isVisibilityColumn = new ComColumndata("is_visibility", BuiltinDataType.INTEGER, 1);
		isVisibilityColumn.setName("是否可见(是否可读)");
		isVisibilityColumn.setComments("是否可见(是否可读)");
		isVisibilityColumn.setOrderCode(8);
		columns.add(isVisibilityColumn);
		
		ComColumndata isOperColumn = new ComColumndata("is_oper", BuiltinDataType.INTEGER, 1);
		isOperColumn.setName("是否可操作(是否可写)");
		isOperColumn.setComments("是否可操作(是否可写)");
		isOperColumn.setOrderCode(9);
		columns.add(isOperColumn);
		
		ComColumndata secretLevelsColumn = new ComColumndata("secret_levels", BuiltinDataType.STRING, 32);
		secretLevelsColumn.setName("密级");
		secretLevelsColumn.setComments("密级：多个用,隔开");
		secretLevelsColumn.setOrderCode(10);
		columns.add(secretLevelsColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("权限信息表");
		table.setComments("权限信息表");
		
		
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
