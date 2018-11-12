package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.StrUtils;

/**
 * 项目信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgProject extends BasicEntity implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 关联的数据库主键
	 * <p>目前系统只支持，一个项目对一个数据库</p>
	 */
	private String refDatabaseId;
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 项目编码
	 */
	private String code;
	/**
	 * 项目描述
	 */
	private String descs;
	
	//-----------------------------------------------------------
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = code;
		}
		return name;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn refDatabaseIdColumn = new CfgColumn("ref_database_id", DataTypeConstants.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：目前系统只支持，一个项目对一个数据库");
		refDatabaseIdColumn.setIsNullabled(0);
		columns.add(refDatabaseIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 200);
		nameColumn.setName("项目名称");
		nameColumn.setComments("项目名称");
		columns.add(nameColumn);
		
		CfgColumn codeColumn = new CfgColumn("code", DataTypeConstants.STRING, 100);
		codeColumn.setName("项目编码");
		codeColumn.setComments("项目编码");
		codeColumn.setIsNullabled(0);
		columns.add(codeColumn);
		
		CfgColumn descsColumn = new CfgColumn("descs", DataTypeConstants.STRING, 800);
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		columns.add(descsColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("项目信息表表");
		table.setComments("项目信息表表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}
	public String toDropTable() {
		return "CFG_PROJECT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgProject";
	}

	public String validNotNullProps() {
		if(StrUtils.isEmpty(refDatabaseId)){
			return "项目关联的数据库id不能为空";
		}
		if(StrUtils.isEmpty(code)){
			return "项目编码不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
}
