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
 * 数据权限信息表（简单）
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysDataPrivS extends BasicEntity implements ITable, IEntity{
	
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
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(11);
		
		ComColumndata refDataIdColumn = new ComColumndata("ref_data_id", BuiltinDataType.STRING, 32);
		refDataIdColumn.setName("主体id");
		refDataIdColumn.setComments("主体id：比如用户id，账户id，角色id，部门id，岗位id，用户组id等");
		columns.add(refDataIdColumn);
		
		ComColumndata refDataTypeColumn = new ComColumndata("ref_data_type", BuiltinDataType.STRING, 20);
		refDataTypeColumn.setName("主体类型");
		refDataTypeColumn.setComments("主体类型：比如用户user，账户account，角色role，部门dept，岗位position，用户组userGroup等");
		columns.add(refDataTypeColumn);
		
		ComColumndata refIdColumn = new ComColumndata("ref_id", BuiltinDataType.STRING, 32);
		refIdColumn.setName("关联的数据id");
		refIdColumn.setComments("关联的数据id：比如部门id");
		columns.add(refIdColumn);
		
		ComColumndata refTypeColumn = new ComColumndata("ref_type", BuiltinDataType.STRING, 20);
		refTypeColumn.setName("关联的数据类型");
		refTypeColumn.setComments("关联的数据类型：比如部门dept");
		columns.add(refTypeColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("数据权限信息表（简单）");
		table.setComments("数据权限信息表（简单）");
		
		
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
