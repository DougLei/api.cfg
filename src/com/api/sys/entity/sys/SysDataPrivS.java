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
 * 数据权限信息表（简单）
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysDataPrivS extends BasicEntity implements IEntity{
	
	/**
	 * 主体id
	 * <p>主体id：比如用户id，账户id，角色id，部门id，岗位id，用户组id等</p>
	 */
	private String refDataId;
	/**
	 * 主体类型
	 * <p>主体类型：比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等</p>
	 */
	private String refDataType;
	/**
	 * 关联的数据id
	 * <p>关联的数据id：比如部门id</p>
	 */
	private String refId;
	/**
	 * 关联的数据类型
	 * <p>关联的数据类型：比如部门dept</p>
	 */
	private String refType;
	
	//-------------------------------------------------------------------------
	
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
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getRefType() {
		return refType;
	}
	public void setRefType(String refType) {
		this.refType = refType;
	}

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(11);
		
		CfgColumn refDataIdColumn = new CfgColumn("ref_data_id", DataTypeConstants.STRING, 32);
		refDataIdColumn.setName("主体id");
		refDataIdColumn.setComments("主体id：比如用户id，账户id，角色id，部门id，岗位id，用户组id等");
		columns.add(refDataIdColumn);
		
		CfgColumn refDataTypeColumn = new CfgColumn("ref_data_type", DataTypeConstants.STRING, 20);
		refDataTypeColumn.setName("主体类型");
		refDataTypeColumn.setComments("主体类型：比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等");
		columns.add(refDataTypeColumn);
		
		CfgColumn refIdColumn = new CfgColumn("ref_id", DataTypeConstants.STRING, 32);
		refIdColumn.setName("关联的数据id");
		refIdColumn.setComments("关联的数据id：比如部门id");
		columns.add(refIdColumn);
		
		CfgColumn refTypeColumn = new CfgColumn("ref_type", DataTypeConstants.STRING, 20);
		refTypeColumn.setName("关联的数据类型");
		refTypeColumn.setComments("关联的数据类型：比如部门dept");
		columns.add(refTypeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("数据权限信息表（简单）");
		table.setRemark("数据权限信息表（简单）");
		
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_DATA_PRIV_S";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysDataPrivS";
	}
}
