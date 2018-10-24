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
 * 属性配置扩展表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgPropConfExtend extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
	
	/** 
	 * 关联的属性id
	 * <p>CfgColumn的id，或CfgSqlResultset的id，即对某一个字段的导入导出的扩展配置</p>
	 */
	private String refPropId;
	/**
	 * 关联的属性类型
	 * <p>1、column，2、sqlResultset</p>
	 */
	private Integer refPropType;
	
	/**
	 * 数据字典编码id
	 */
	private String dataDictionaryId;
	
	/** 这四个字段，处理某个列的值，引用某张表中某个字段的值。导入时，key列用来显示值，提交的时候，提交value列的值 */
	/** 关联的表id */
	private String refTableId;
	/** 关联表中，关联的key列id */
	private String refKeyColumnId;
	/** 关联表中，关联的value列id */
	private String refValueColumnId;
	/** 关联表中，关联的order by列id */
	private String refOrderByColumnId;
	
	/** 这四个字段，同上，用来处理内置表，因为内置表和属性没有id */
	/** 关联的内置表资源名 */
	private String refTableResourceName;
	/** 关联的内置表中，关联的key列属性名 */
	private String refKeyColumnPropName;
	/** 关联的内置表中，关联的value列属性名 */
	private String refValueColumnPropName;
	/** 关联的内置表中，关联的order by列属性名 */
	private String refOrderByColumnPropName;
	
	/** 排序顺序，asc/desc，默认为desc */
	private String orderBy;
	
	//-------------------------------------------------------------------------
	/**
	 * 数据列表
	 * <p>数组的长度就为2或3，下标为0的是实际存储的值，下标为1的是展示名称，如果下标为2有值，则标识是子数据集合，依次类推</p>
	 */
	@JSONField(serialize = false)
	private List<Object[]> dataList;
	
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
	public String getDataDictionaryId() {
		return dataDictionaryId;
	}
	public void setDataDictionaryId(String dataDictionaryId) {
		this.dataDictionaryId = dataDictionaryId;
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
	public String getRefOrderByColumnId() {
		return refOrderByColumnId;
	}
	public void setRefOrderByColumnId(String refOrderByColumnId) {
		this.refOrderByColumnId = refOrderByColumnId;
	}
	public String getRefOrderByColumnPropName() {
		return refOrderByColumnPropName;
	}
	public void setRefOrderByColumnPropName(String refOrderByColumnPropName) {
		this.refOrderByColumnPropName = refOrderByColumnPropName;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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
		List<CfgColumn> columns = new ArrayList<CfgColumn>(12+7);
		
		CfgColumn refPropIdColumn = new CfgColumn("ref_prop_id", DataTypeConstants.STRING, 32);
		refPropIdColumn.setName("关联的属性id");
		refPropIdColumn.setComments("CfgColumn的id，或CfgSqlResultset的id，即对某一个字段的导入导出的扩展配置");
		columns.add(refPropIdColumn);
		
		CfgColumn refPropTypeColumn = new CfgColumn("ref_prop_type", DataTypeConstants.INTEGER, 1);
		refPropTypeColumn.setName("关联的属性类型");
		refPropTypeColumn.setComments("1、column，2、sqlResultset");
		columns.add(refPropTypeColumn);
		
		CfgColumn dataDictionaryIdColumn = new CfgColumn("data_dictionary_id", DataTypeConstants.STRING, 50);
		dataDictionaryIdColumn.setName("数据字典编码id");
		dataDictionaryIdColumn.setComments("数据字典编码id");
		columns.add(dataDictionaryIdColumn);
		
		
		
		CfgColumn refTableIdColumn = new CfgColumn("ref_table_id", DataTypeConstants.STRING, 32);
		refTableIdColumn.setName("关联的表id");
		refTableIdColumn.setComments("这四个字段，处理某个列的值，引用某张表中某个字段的值。导入时，key列用来显示值，提交的时候，提交value列的值");
		columns.add(refTableIdColumn);
		
		CfgColumn refKeyColumnIdColumn = new CfgColumn("ref_key_column_id", DataTypeConstants.STRING, 32);
		refKeyColumnIdColumn.setName("关联表中，关联的key列id");
		refKeyColumnIdColumn.setComments("关联表中，关联的key列id");
		columns.add(refKeyColumnIdColumn);
		
		CfgColumn refValueColumnIdColumn = new CfgColumn("ref_value_column_id", DataTypeConstants.STRING, 32);
		refValueColumnIdColumn.setName("关联表中，关联的value列id");
		refValueColumnIdColumn.setComments("关联表中，关联的value列id");
		columns.add(refValueColumnIdColumn);
		
		CfgColumn refOrderByColumnIdColumn = new CfgColumn("ref_order_by_column_id", DataTypeConstants.STRING, 32);
		refOrderByColumnIdColumn.setName("关联表中，关联的order by列id");
		refOrderByColumnIdColumn.setComments("关联表中，关联的order by列id");
		columns.add(refOrderByColumnIdColumn);
		
		CfgColumn refTableResourceNameColumn = new CfgColumn("ref_table_resource_name", DataTypeConstants.STRING, 60);
		refTableResourceNameColumn.setName("关联的内置表资源名");
		refTableResourceNameColumn.setComments("这四个字段，同上，用来处理内置表，因为内置表和属性没有id");
		columns.add(refTableResourceNameColumn);
		
		CfgColumn refKeyColumnPropNameColumn = new CfgColumn("ref_key_column_prop_name", DataTypeConstants.STRING, 40);
		refKeyColumnPropNameColumn.setName("关联的内置表中，关联的key列属性名");
		refKeyColumnPropNameColumn.setComments("关联的内置表中，关联的key列属性名");
		columns.add(refKeyColumnPropNameColumn);
		
		CfgColumn refValueColumnPropNameColumn = new CfgColumn("ref_value_column_prop_name", DataTypeConstants.STRING, 40);
		refValueColumnPropNameColumn.setName("关联的内置表中，关联的value列属性名");
		refValueColumnPropNameColumn.setComments("关联的内置表中，关联的value列属性名");
		columns.add(refValueColumnPropNameColumn);
		
		CfgColumn refOrderByColumnPropNameColumn = new CfgColumn("ref_order_by_column_prop_name", DataTypeConstants.STRING, 40);
		refOrderByColumnPropNameColumn.setName("关联的内置表中，关联的order by列属性名");
		refOrderByColumnPropNameColumn.setComments("关联的内置表中，关联的order by列属性名");
		columns.add(refOrderByColumnPropNameColumn);
		
		CfgColumn orderByColumn = new CfgColumn("order_by", DataTypeConstants.STRING, 4);
		orderByColumn.setName("排序顺序");
		orderByColumn.setComments("asc/desc，默认为desc");
		orderByColumn.setDefaultValue("desc");
		columns.add(orderByColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setResourceName(getEntityName());
		table.setName("属性配置扩展表");
		table.setComments("属性配置扩展表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_PROP_CONF_EXTEND";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgPropConfExtend";
	}
	
	public String validNotNullProps() {
		if(refPropType == null){
			return "关联的属性类型不能为空";
		}
		if(refPropType != 1 && refPropType != 2){
			return "关联的属性类型值，只能为1(column)或2(sqlResultset)";
		}
		if(StrUtils.isEmpty(refPropId)){
			return "关联的属性id不能为空";
		}
		if(StrUtils.notEmpty(dataDictionaryId) 
				&& (StrUtils.notEmpty(refTableId) || StrUtils.notEmpty(refKeyColumnId) || StrUtils.notEmpty(refValueColumnId) || StrUtils.notEmpty(refOrderByColumnId) || StrUtils.notEmpty(refTableResourceName) || StrUtils.notEmpty(refKeyColumnPropName) || StrUtils.notEmpty(refValueColumnPropName) || StrUtils.notEmpty(refOrderByColumnPropName))){
			return "扩展信息已经配置了关联的数据字典，无法再配置关联其他表的字段";
		}
		if((StrUtils.notEmpty(refTableId) && StrUtils.notEmpty(refKeyColumnId) && StrUtils.notEmpty(refValueColumnId)) 
				&& (StrUtils.notEmpty(refTableResourceName) || StrUtils.notEmpty(refKeyColumnPropName) || StrUtils.notEmpty(refValueColumnPropName))){
			return "扩展信息已经配置了关联业务表的字段，无法再配置关联内置表的字段";
		}
		if((StrUtils.notEmpty(refTableResourceName) && StrUtils.notEmpty(refKeyColumnPropName) && StrUtils.notEmpty(refValueColumnPropName)) 
				&& (StrUtils.notEmpty(refTableId) || StrUtils.notEmpty(refKeyColumnId) || StrUtils.notEmpty(refValueColumnId))){
			return "扩展信息已经配置了关联内置表的字段，无法再配置关联业务表的字段";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	public List<Object[]> getDataList() {
		/**
		 * 注意：
		 * (1).dataDictionaryCode
		 * (2).refTableId、refKeyColumnId、refValueColumnId、refOrderByColumnId
		 * (3).refTableResourceName、refKeyColumnPropName、refValueColumnPropName、refOrderByColumnPropName
		 * 以上是三组扩展配置，系统按照以上配置解析，如果第一个有值了，dataList就用dataDictionaryCode的结果集合；依次类推
		 */
		if(StrUtils.notEmpty(dataDictionaryId)){
			// TODO
			
			
		}else if(StrUtils.notEmpty(refTableId) && StrUtils.notEmpty(refKeyColumnId) && StrUtils.notEmpty(refValueColumnId)){
			// TODO
			
			
		}else if(StrUtils.notEmpty(refTableResourceName) && StrUtils.notEmpty(refKeyColumnPropName) && StrUtils.notEmpty(refValueColumnPropName)){
			// TODO
			
			
		}
		return dataList;
	}
}
