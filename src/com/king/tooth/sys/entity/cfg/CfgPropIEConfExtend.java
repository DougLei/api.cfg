package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.StrUtils;

/**
 * 属性导入导出配置扩展表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgPropIEConfExtend extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
	
	/**
	 * 配置类型
	 * <p>1导入，2导出</p>
	 */
	private Integer confType;
	/**
	 * 关联的属性类型
	 * <p>1、column，2、sqlResultset</p>
	 */
	private Integer refPropType;
	/** 
	 * 关联的属性id
	 * <p>CfgColumn的id，或CfgSqlResultset的id，即对某一个字段的导入导出的扩展配置</p>
	 */
	private String refPropId;
	
	/** 这三个字段，处理某个列的值，引用某张表中某个字段的值。导入时，key列用来显示值，提交的时候，提交value列的值 */
	/** 关联的表id */
	private String refTableId;
	/** 关联表中，关联的key列id */
	private String refKeyColumnId;
	/** 关联表中，关联的value列id */
	private String refValueColumnId;
	
	/** 这三个字段，同上，用来处理内置表，因为内置表和属性没有id */
	/** 关联的表资源名 */
	private String refTableResourceName;
	/** 关联表中，关联的key列属性名 */
	private String refKeyColumnPropName;
	/** 关联表中，关联的value列属性名 */
	private String refValueColumnPropName;
	
	//-------------------------------------------------------------------------
	public Integer getConfType() {
		return confType;
	}
	public void setConfType(Integer confType) {
		this.confType = confType;
	}
	public Integer getRefPropType() {
		return refPropType;
	}
	public void setRefPropType(Integer refPropType) {
		this.refPropType = refPropType;
	}
	public String getRefPropId() {
		return refPropId;
	}
	public void setRefPropId(String refPropId) {
		this.refPropId = refPropId;
	}
	public String getRefTableId() {
		return refTableId;
	}
	public void setRefTableId(String refTableId) {
		this.refTableId = refTableId;
	}
	public String getRefKeyColumnId() {
		return refKeyColumnId;
	}
	public void setRefKeyColumnId(String refKeyColumnId) {
		this.refKeyColumnId = refKeyColumnId;
	}
	public String getRefValueColumnId() {
		return refValueColumnId;
	}
	public void setRefValueColumnId(String refValueColumnId) {
		this.refValueColumnId = refValueColumnId;
	}
	public String getRefTableResourceName() {
		return refTableResourceName;
	}
	public void setRefTableResourceName(String refTableResourceName) {
		this.refTableResourceName = refTableResourceName;
	}
	public String getRefKeyColumnPropName() {
		return refKeyColumnPropName;
	}
	public void setRefKeyColumnPropName(String refKeyColumnPropName) {
		this.refKeyColumnPropName = refKeyColumnPropName;
	}
	public String getRefValueColumnPropName() {
		return refValueColumnPropName;
	}
	public void setRefValueColumnPropName(String refValueColumnPropName) {
		this.refValueColumnPropName = refValueColumnPropName;
	}

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn confTypeColumn = new CfgColumn("conf_type", DataTypeConstants.INTEGER, 1);
		confTypeColumn.setName("配置类型");
		confTypeColumn.setComments("1导入，2导出");
		columns.add(confTypeColumn);
		
		CfgColumn refPropTypeColumn = new CfgColumn("ref_prop_type", DataTypeConstants.INTEGER, 1);
		refPropTypeColumn.setName("关联的属性类型");
		refPropTypeColumn.setComments("1、column，2、sqlResultset");
		columns.add(refPropTypeColumn);
		
		CfgColumn refPropIdColumn = new CfgColumn("ref_prop_id", DataTypeConstants.STRING, 32);
		refPropIdColumn.setName("关联的属性id");
		refPropIdColumn.setComments("CfgColumn的id，或CfgSqlResultset的id，即对某一个字段的导入导出的扩展配置");
		columns.add(refPropIdColumn);
		
		CfgColumn refTableIdColumn = new CfgColumn("ref_table_id", DataTypeConstants.STRING, 32);
		refTableIdColumn.setName("关联的表id");
		refTableIdColumn.setComments("这三个字段，处理某个列的值，引用某张表中某个字段的值。导入时，key列用来显示值，提交的时候，提交value列的值");
		columns.add(refTableIdColumn);
		
		CfgColumn refKeyColumnIdColumn = new CfgColumn("ref_key_column_id", DataTypeConstants.STRING, 32);
		refKeyColumnIdColumn.setName("关联表中，关联的key列id");
		refKeyColumnIdColumn.setComments("关联表中，关联的key列id");
		columns.add(refKeyColumnIdColumn);
		
		CfgColumn refValueColumnIdColumn = new CfgColumn("ref_value_column_id", DataTypeConstants.STRING, 32);
		refValueColumnIdColumn.setName("关联表中，关联的value列id");
		refValueColumnIdColumn.setComments("关联表中，关联的value列id");
		columns.add(refValueColumnIdColumn);
		
		CfgColumn refTableResourceNameColumn = new CfgColumn("ref_table_resource_name", DataTypeConstants.STRING, 60);
		refTableResourceNameColumn.setName("关联的表资源名");
		refTableResourceNameColumn.setComments("这三个字段，同上，用来处理内置表，因为内置表和属性没有id");
		columns.add(refTableResourceNameColumn);
		
		CfgColumn refKeyColumnPropNameColumn = new CfgColumn("ref_key_column_prop_name", DataTypeConstants.STRING, 40);
		refKeyColumnPropNameColumn.setName("关联表中，关联的key列属性名");
		refKeyColumnPropNameColumn.setComments("关联表中，关联的key列属性名");
		columns.add(refKeyColumnPropNameColumn);
		
		CfgColumn refValueColumnPropNameColumn = new CfgColumn("ref_value_column_prop_name", DataTypeConstants.STRING, 40);
		refValueColumnPropNameColumn.setName("关联表中，关联的value列属性名");
		refValueColumnPropNameColumn.setComments("关联表中，关联的value列属性名");
		columns.add(refValueColumnPropNameColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setResourceName(getEntityName());
		table.setName("属性导入导出配置扩展表");
		table.setComments("属性导入导出配置扩展表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_PROP_IE_CONF_EXTEND";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgPropIEConfExtend";
	}
	
	public String validNotNullProps() {
		if(refPropType == null){
			return "关联的属性类型不能为空";
		}
		if(refPropType != REF_PROP_TYPE_COLUMN && refPropType != REF_PROP_TYPE_SQL_RESULTSET){
			return "关联的属性类型值，只能为1(column)或2(sqlResultset)";
		}
		if(confType == null){
			return "配置类型不能为空！";
		}
		if(confType != CONF_TYPE_IMPORT && confType != CONF_TYPE_EXPORT){
			return "配置类型的值必须为1(导入)或2(导出)";
		}
		if(StrUtils.isEmpty(refPropId)){
			return "关联的属性id不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	// ---------------------------------------------------------------------------
	/** 关联的属性类型:1、column */
	public static final Integer REF_PROP_TYPE_COLUMN = 1;
	/** 关联的属性类型:2、sqlResultset */
	public static final Integer REF_PROP_TYPE_SQL_RESULTSET = 2;
	
	/** 配置类型:1、导入 */
	public static final Integer CONF_TYPE_IMPORT = 1;
	/** 配置类型:2、导出 */
	public static final Integer CONF_TYPE_EXPORT = 2;
}
