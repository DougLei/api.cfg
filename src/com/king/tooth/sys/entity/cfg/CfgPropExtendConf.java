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
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.StrUtils;

/**
 * 属性扩展配置信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgPropExtendConf extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
	
	/** 
	 * 关联的资源id
	 */
	private String refResourceId;
	/** 
	 * 关联的属性id
	 * <p>CfgColumn的id</p>
	 */
	private String refPropId;
	/**
	 * 数据字典id
	 */
	private String dataDictionaryId;
	/** 
	 * 关联的表id/表资源名
	 * <p>如果是内置表，存储的是内置表资源名；如果是业务表，存储的是表id</p> 
	 */
	private String refTable;
	/**
	 * 关联表中，关联的value列id/列属性名
	 * <p>value列为提交值，存值方式同ref_table</p>
	 */
	private String refValueColumn;
	
	//-------------------------------------------------------------------------

	public String getRefPropId() {
		return refPropId;
	}
	public void setRefPropId(String refPropId) {
		this.refPropId = refPropId;
	}
	public String getDataDictionaryId() {
		return dataDictionaryId;
	}
	public void setDataDictionaryId(String dataDictionaryId) {
		this.dataDictionaryId = dataDictionaryId;
	}
	public String getRefTable() {
		return refTable;
	}
	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public String getRefValueColumn() {
		return refValueColumn;
	}
	public void setRefValueColumn(String refValueColumn) {
		this.refValueColumn = refValueColumn;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("关联的资源id");
		columns.add(refResourceIdColumn);
		
		CfgColumn refPropIdColumn = new CfgColumn("ref_prop_id", DataTypeConstants.STRING, 32);
		refPropIdColumn.setName("关联的属性id");
		refPropIdColumn.setComments("CfgColumn的id，或CfgSqlResultset的id");
		columns.add(refPropIdColumn);
		
		CfgColumn dataDictionaryIdColumn = new CfgColumn("data_dictionary_id", DataTypeConstants.STRING, 50);
		dataDictionaryIdColumn.setName("数据字典id");
		dataDictionaryIdColumn.setComments("数据字典id");
		columns.add(dataDictionaryIdColumn);
		
		CfgColumn refTableColumn = new CfgColumn("ref_table", DataTypeConstants.STRING, 60);
		refTableColumn.setName("关联的表id/表资源名");
		refTableColumn.setComments("如果是内置表，存储的是内置表资源名；如果是业务表，存储的是表id");
		columns.add(refTableColumn);
		
		CfgColumn refValueColumnColumn = new CfgColumn("ref_value_column", DataTypeConstants.STRING, 40);
		refValueColumnColumn.setName("关联表中，关联的value列id/列属性名");
		refValueColumnColumn.setComments("value列为提交值，存值方式同ref_table");
		columns.add(refValueColumnColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setResourceName(getEntityName());
		table.setName("属性扩展配置信息表");
		table.setComments("属性扩展配置信息表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_PROP_EXTEND_CONF";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgPropExtendConf";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(refResourceId)){
			return "关联的资源id不能为空";
		}
		if(StrUtils.isEmpty(refPropId)){
			return "关联的属性id不能为空";
		}
		if(StrUtils.isEmpty(dataDictionaryId) && StrUtils.isEmpty(refTable) && StrUtils.isEmpty(refValueColumn)){
			return "配置属性的扩展信息，必须关联数据字典，或关联其他表的字段，且只能关联一种";
		}
		if(StrUtils.notEmpty(dataDictionaryId) && (StrUtils.notEmpty(refTable) || StrUtils.notEmpty(refValueColumn))){
			return "配置属性的扩展信息时，已经配置了关联的数据字典，无法再配置关联其他表";
		}
		if(StrUtils.isEmpty(dataDictionaryId) && (StrUtils.isEmpty(refTable) || StrUtils.isEmpty(refValueColumn))){
			return "配置属性的扩展信息时，关联其他表的字段，关联表(refTable)和value字段(refValueColumn)的值都不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	/**
	 * 是否是内置的表资源
	 * @return
	 */
	public boolean isBuiltinTableResource(){
		return ResourceInfoConstants.BUILTIN_RESOURCE.equals(refResourceId);
	}
}
