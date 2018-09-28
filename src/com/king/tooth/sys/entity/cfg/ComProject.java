package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.StrUtils;

/**
 * 项目信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class ComProject extends BasicEntity implements ITable, IEntityPropAnalysis, IEntity{
	
	/**
	 * 关联的数据库主键
	 * <p>目前系统只支持，一个项目对一个数据库</p>
	 */
	private String refDatabaseId;
	/**
	 * 项目名称
	 */
	private String projName;
	/**
	 * 项目编码
	 */
	private String projCode;
	/**
	 * 项目描述
	 */
	private String descs;
	
	//-----------------------------------------------------------
	
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getProjName() {
		if(StrUtils.isEmpty(projName)){
			projName = projCode;
		}
		return projName;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public String getRefDatabaseId() {
		return refDatabaseId;
	}
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata refDatabaseIdColumn = new ComColumndata("ref_database_id", BuiltinDataType.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：目前系统只支持，一个项目对一个数据库");
		refDatabaseIdColumn.setIsNullabled(0);
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		ComColumndata projNameColumn = new ComColumndata("proj_name", BuiltinDataType.STRING, 200);
		projNameColumn.setName("项目名称");
		projNameColumn.setComments("项目名称");
		projNameColumn.setOrderCode(2);
		columns.add(projNameColumn);
		
		ComColumndata projCodeColumn = new ComColumndata("proj_code", BuiltinDataType.STRING, 100);
		projCodeColumn.setName("项目编码");
		projCodeColumn.setComments("项目编码");
		projCodeColumn.setIsNullabled(0);
		projCodeColumn.setOrderCode(3);
		columns.add(projCodeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", BuiltinDataType.STRING, 800);
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		descsColumn.setOrderCode(4);
		columns.add(descsColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("项目信息表表");
		table.setComments("项目信息表表");
		
		table.setColumns(getColumnList());
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComProject";
	}

	public String validNotNullProps() {
		if(StrUtils.isEmpty(refDatabaseId)){
			return "项目关联的数据库id不能为空";
		}
		if(StrUtils.isEmpty(projCode)){
			return "项目编码不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
}
