package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 属性配置扩展表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgPropConfExtend extends BasicEntity implements ITable, IEntity, IEntityPropAnalysis{
	
	/** 
	 * 关联的属性id
	 * <p>CfgColumn的id，或CfgSqlResultset的id</p>
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
	
	/**
	 * 是否引用的内置表
	 * <p>默认值为0</p>
	 */
	private Integer isRefBuiltinTable;
	/** 
	 * 关联的表id/表资源名
	 * <p>如果是内置表，存储的是内置表资源名；如果是业务表，存储的是表id</p> 
	 */
	private String refTable;
	/**
	 * 关联表中，关联的key列id/列属性名
	 * <p>key列为显示值，存值方式同ref_table</p>
	 */
	private String refKeyColumn;
	/**
	 * 关联表中，关联的value列id/列属性名
	 * <p>value列为提交值，存值方式同ref_table</p>
	 */
	private String refValueColumn;
	/**
	 * 关联表中，关联的order by列id/列属性名
	 * <p>为排序字段，存值方式同ref_table</p>
	 */
	private String refOrderByColumn;
	/**
	 * 排序顺序，asc/desc
	 * <p>默认为desc</p>
	 */
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
	public Integer getIsRefBuiltinTable() {
		return isRefBuiltinTable;
	}
	public void setIsRefBuiltinTable(Integer isRefBuiltinTable) {
		this.isRefBuiltinTable = isRefBuiltinTable;
	}
	public String getRefTable() {
		return refTable;
	}
	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}
	public String getRefKeyColumn() {
		return refKeyColumn;
	}
	public void setRefKeyColumn(String refKeyColumn) {
		this.refKeyColumn = refKeyColumn;
	}
	public String getRefValueColumn() {
		return refValueColumn;
	}
	public void setRefValueColumn(String refValueColumn) {
		this.refValueColumn = refValueColumn;
	}
	public String getRefOrderByColumn() {
		return refOrderByColumn;
	}
	public void setRefOrderByColumn(String refOrderByColumn) {
		this.refOrderByColumn = refOrderByColumn;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn refPropIdColumn = new CfgColumn("ref_prop_id", DataTypeConstants.STRING, 32);
		refPropIdColumn.setName("关联的属性id");
		refPropIdColumn.setComments("CfgColumn的id，或CfgSqlResultset的id");
		columns.add(refPropIdColumn);
		
		CfgColumn refPropTypeColumn = new CfgColumn("ref_prop_type", DataTypeConstants.INTEGER, 1);
		refPropTypeColumn.setName("关联的属性类型");
		refPropTypeColumn.setComments("1、column，2、sqlResultset");
		columns.add(refPropTypeColumn);
		
		CfgColumn dataDictionaryIdColumn = new CfgColumn("data_dictionary_id", DataTypeConstants.STRING, 50);
		dataDictionaryIdColumn.setName("数据字典编码id");
		dataDictionaryIdColumn.setComments("数据字典编码id");
		columns.add(dataDictionaryIdColumn);
		
		CfgColumn isRefBuiltinTableColumn = new CfgColumn("is_ref_builtin_table", DataTypeConstants.INTEGER, 1);
		isRefBuiltinTableColumn.setName("是否引用的内置表");
		isRefBuiltinTableColumn.setComments("默认值为0");
		isRefBuiltinTableColumn.setDefaultValue("0");
		columns.add(isRefBuiltinTableColumn);
		
		CfgColumn refTableColumn = new CfgColumn("ref_table", DataTypeConstants.STRING, 60);
		refTableColumn.setName("关联的表id/表资源名");
		refTableColumn.setComments("如果是内置表，存储的是内置表资源名；如果是业务表，存储的是表id");
		columns.add(refTableColumn);
		
		CfgColumn refKeyColumnColumn = new CfgColumn("ref_key_column", DataTypeConstants.STRING, 40);
		refKeyColumnColumn.setName("关联表中，关联的key列id/列属性名");
		refKeyColumnColumn.setComments("key列为显示值，存值方式同ref_table");
		columns.add(refKeyColumnColumn);
		
		CfgColumn refValueColumnColumn = new CfgColumn("ref_value_column", DataTypeConstants.STRING, 40);
		refValueColumnColumn.setName("关联表中，关联的value列id/列属性名");
		refValueColumnColumn.setComments("value列为提交值，存值方式同ref_table");
		columns.add(refValueColumnColumn);
		
		CfgColumn refOrderByColumnColumn = new CfgColumn("ref_order_by_column", DataTypeConstants.STRING, 40);
		refOrderByColumnColumn.setName("关联表中，关联的order by列id/列属性名");
		refOrderByColumnColumn.setComments("为排序字段，存值方式同ref_table");
		columns.add(refOrderByColumnColumn);
		
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
				&& (StrUtils.notEmpty(refTable) || StrUtils.notEmpty(refKeyColumn) || StrUtils.notEmpty(refValueColumn) || StrUtils.notEmpty(refOrderByColumn))){
			return "扩展信息已经配置了关联的数据字典，无法再配置关联其他表的字段";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDataList() {
		/**
		 * 注意：
		 * (1).dataDictionaryCode
		 * (2).refTable、refKeyColumn、refValueColumn、refOrderByColumn
		 * (3)......
		 * 以上是多组扩展配置，系统按照以上配置顺序解析，如果第一个有值了，dataList就用dataDictionaryCode的结果集合；依次类推
		 */
		if(StrUtils.notEmpty(dataDictionaryId)){
			dataList = HibernateUtil.executeListQueryByHqlArr(null, null, queryDataDictionaryHql, dataDictionaryId);
		}else if(StrUtils.notEmpty(refTable) && StrUtils.notEmpty(refKeyColumn) && StrUtils.notEmpty(refValueColumn)){
			String tableResourceName = null;
			String keyColumnPropName = null;
			String valueColumnPropName = null;
			String orderByColumnPropName = null;
			
			if(isRefBuiltinTable == 1){
				tableResourceName = refTable;
				keyColumnPropName = refKeyColumn;
				valueColumnPropName = refValueColumn;
				orderByColumnPropName = refOrderByColumn;
			}else{
				if(StrUtils.notEmpty(refTable) && StrUtils.notEmpty(refKeyColumn) && StrUtils.notEmpty(refValueColumn)){
					Object obj = HibernateUtil.executeUniqueQueryByHqlArr(queryTableResourceNameByIdHql, refTable);
					if(obj == null){
						throw new NullPointerException("id为["+id+"]的属性扩展配置信息，配置的refTable值，无法查询到相应的表信息，请检查配置");
					}
					tableResourceName = obj.toString();
					
					obj = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropResourceNameByIdHql, refKeyColumn);
					if(obj == null){
						throw new NullPointerException("id为["+id+"]的属性扩展配置信息，配置的refKeyColumn值，无法查询到相应的列信息，请检查配置");
					}
					keyColumnPropName = obj.toString();
					
					obj = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropResourceNameByIdHql, refValueColumn);
					if(obj == null){
						throw new NullPointerException("id为["+id+"]的属性扩展配置信息，配置的refValueColumn值，无法查询到相应的列信息，请检查配置");
					}
					valueColumnPropName = obj.toString();
					
					if(StrUtils.notEmpty(refOrderByColumn)){
						obj = HibernateUtil.executeUniqueQueryByHqlArr(queryColumnPropResourceNameByIdHql, refOrderByColumn);
						if(obj == null){
							throw new NullPointerException("id为["+id+"]的属性扩展配置信息，配置的refOrderByColumn值，无法查询到相应的列信息，请检查配置");
						}
						orderByColumnPropName = obj.toString();
					}
				}
			}
			
			StringBuilder hql = new StringBuilder("select ");
			hql.append(valueColumnPropName).append(",").append(keyColumnPropName).append(" from ").append(tableResourceName);
			if(StrUtils.notEmpty(orderByColumnPropName)){
				hql.append(" order by ").append(orderByColumnPropName).append(" ").append(orderBy);
			}
			dataList = HibernateUtil.executeListQueryByHqlArr(null, null, hql.toString());
			hql.setLength(0);
		}
		return dataList;
	}
	/** 查询数据字典的hql */
	private static final String queryDataDictionaryHql = "select val, caption from SysDataDictionary where parentId=? and isEnabled=1 and isDelete=0 order by orderCode asc";
	/** 根据id查询表资源名的hql */
	private static final String queryTableResourceNameByIdHql = "select resourceName from CfgTable where "+ResourcePropNameConstants.ID+"=? and isEnabled=1 and isCreated=1 and isBuildModel=1";
	/** 根据id查询列属性名的hql */
	private static final String queryColumnPropResourceNameByIdHql = "select propName CfgColumn where "+ResourcePropNameConstants.ID+"=? and isEnabled=1 and operStatus="+CfgColumn.CREATED;
}
