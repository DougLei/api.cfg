package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.service.cfg.CfgSqlService;
import com.king.tooth.sys.service.cfg.CfgTableService;
import com.king.tooth.util.StrUtils;

/**
 * 业务模型资源关系表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgBusiModelResRelations extends BasicEntity implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 关联的业务模型id
	 */
	private String refBusiModelId;
	/**
	 * 父id
	 * <p>形成树表结构</p>
	 */
	private String parentId;
	/**
	 * 关联的资源id
	 * <p>CfgTable或CfgSql资源的id</p>
	 */
	private String refResourceId;
	/**
	 * 关联的资源类型
	 * <p>1.CfgTable、2.CfgSql</p>
	 */
	private Integer refResourceType;
	
	/**
	 * 主资源中关联子资源的key名
	 * <p>默认值为children，这个是在主资源对象中，用指定的key值存储子资源对象数组</p>
	 */
	private String refSubResourceKeyName;
	/**
	 * 子资源中关联父资源的属性id
	 * <p>指定子资源的哪个属性，存储父资源的id值</p>
	 */
	private String refParentResourcePropId;
	
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	private Integer isEnabled;
	/**
	 * 是否可为空
	 * <p>默认为0</p>
	 */
	private Integer isNullabled;
	
	//-------------------------------------------------------------------------
	/**
	 * 业务模型子资源关系集合
	 */
	@JSONField(serialize = false)
	private List<CfgBusiModelResRelations> subBusiModelResRelationsList;
	
	/**
	 * 关联的表资源对象
	 */
	@JSONField(serialize = false)
	private CfgTable refTable;
	
	/**
	 * 关联的sql资源对象
	 */
	@JSONField(serialize = false)
	private CfgSql refSql;
	
	public String getParentId() {
		return parentId;
	}
	public String getRefBusiModelId() {
		return refBusiModelId;
	}
	public void setRefBusiModelId(String refBusiModelId) {
		this.refBusiModelId = refBusiModelId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRefResourceId() {
		return refResourceId;
	}
	public void setRefResourceId(String refResourceId) {
		this.refResourceId = refResourceId;
	}
	public Integer getRefResourceType() {
		return refResourceType;
	}
	public void setRefResourceType(Integer refResourceType) {
		this.refResourceType = refResourceType;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getRefSubResourceKeyName() {
		return refSubResourceKeyName;
	}
	public void setRefSubResourceKeyName(String refSubResourceKeyName) {
		this.refSubResourceKeyName = refSubResourceKeyName;
	}
	public void setSubBusiModelResRelationsList(List<CfgBusiModelResRelations> subBusiModelResRelationsList) {
		this.subBusiModelResRelationsList = subBusiModelResRelationsList;
	}
	public String getRefParentResourcePropId() {
		return refParentResourcePropId;
	}
	public Integer getIsNullabled() {
		return isNullabled;
	}
	public void setIsNullabled(Integer isNullabled) {
		this.isNullabled = isNullabled;
	}
	public void setRefParentResourcePropId(String refParentResourcePropId) {
		this.refParentResourcePropId = refParentResourcePropId;
	}
	public List<CfgBusiModelResRelations> getSubBusiModelResRelationsList() {
		return subBusiModelResRelationsList;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn refBusiModelIdColumn = new CfgColumn("ref_busi_model_id", DataTypeConstants.STRING, 32);
		refBusiModelIdColumn.setName("关联的业务模型id");
		refBusiModelIdColumn.setComments("关联的业务模型id");
		columns.add(refBusiModelIdColumn);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父id");
		parentIdColumn.setComments("父id");
		columns.add(parentIdColumn);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("CfgTable或CfgSql资源的id");
		columns.add(refResourceIdColumn);
		
		CfgColumn refResourceTypeColumn = new CfgColumn("ref_resource_type", DataTypeConstants.INTEGER, 1);
		refResourceTypeColumn.setName("关联的资源类型");
		refResourceTypeColumn.setComments("1.CfgTable、2.CfgSql");
		columns.add(refResourceTypeColumn);
		
		CfgColumn refSubResourceKeyNameColumn = new CfgColumn("ref_sub_resource_key_name", DataTypeConstants.STRING, 60);
		refSubResourceKeyNameColumn.setName("主资源中关联子资源的key名");
		refSubResourceKeyNameColumn.setComments("默认值为children，这个是在主资源对象中，用指定的key值存储子资源对象数组");
		refSubResourceKeyNameColumn.setDefaultValue("children");
		columns.add(refSubResourceKeyNameColumn);
		
		CfgColumn refParentResourcePropIdColumn = new CfgColumn("ref_parent_resource_prop_id", DataTypeConstants.STRING, 32);
		refParentResourcePropIdColumn.setName("子资源中关联父资源的属性id");
		refParentResourcePropIdColumn.setComments("指定子资源的哪个属性，存储父资源的id值");
		columns.add(refParentResourcePropIdColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		CfgColumn isNullabledColumn = new CfgColumn("is_nullabled", DataTypeConstants.INTEGER, 1);
		isNullabledColumn.setName("是否可为空");
		isNullabledColumn.setComments("默认为0");
		isNullabledColumn.setDefaultValue("0");
		columns.add(isNullabledColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("业务模型资源关系表");
		table.setComments("业务模型资源关系表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_BUSI_MODEL_RES_RELATIONS";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgBusiModelResRelations";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(refBusiModelId)){
			return "关联的业务模型id值不能为空";
		}
		if(StrUtils.isEmpty(refResourceId)){
			return "关联的资源id值不能为空";
		}
		if(refResourceType == null || (refResourceType != REF_RESOURCE_TYPE_CFG_TABLE && refResourceType != REF_RESOURCE_TYPE_CFG_SQL)){
			return "关联的资源类型值不能为空，且值目前只能为[1.CfgTable]或[2.CfgSql]";
		}
		if(StrUtils.isEmpty(refParentResourcePropId)){
			return "子资源中关联父资源的属性id值不能为空";
		}
		if(orderCode == null || orderCode < 1){
			return "排序值不能为空，且必须大于0！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	// --------------------------------------------------------
	/**
	 * 关联的资源类型
	 * <p>1.CfgTable</p>
	 */
	public static final Integer REF_RESOURCE_TYPE_CFG_TABLE = 1;
	/**
	 * 关联的资源类型
	 * <p>2.CfgSql</p>
	 */
	public static final Integer REF_RESOURCE_TYPE_CFG_SQL = 2;
	
	/**
	 * 添加业务模型资源关系的子关系
	 * @param busiModelResRelations
	 */
	public void addSubBusiModelResRelations(CfgBusiModelResRelations busiModelResRelations) {
		if(subBusiModelResRelationsList == null){
			subBusiModelResRelationsList = new ArrayList<CfgBusiModelResRelations>();
		}
		subBusiModelResRelationsList.add(busiModelResRelations);
	}
	
	/**
	 * 获取引用的资源名
	 * @return
	 */
	public String getRefResourceName() {
		getRefResource();
		if(refResourceType == REF_RESOURCE_TYPE_CFG_TABLE){
			return refTable.getResourceName();
		}else{
			return refSql.getResourceName();
		}
	}
	
	/**
	 * 获取引用的资源对象
	 * @return
	 */
	public Object getRefResource() {
		if(refResourceType == REF_RESOURCE_TYPE_CFG_TABLE){
			if(refTable == null){
				refTable = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).findTableResourceById(refResourceId);
			}
			return refTable;
		}else if(refResourceType == REF_RESOURCE_TYPE_CFG_SQL){
			if(refSql == null){
				refSql = BuiltinResourceInstance.getInstance("CfgSqlService", CfgSqlService.class).findSqlScriptResourceAllInfoById(refResourceId);
			}
			return refSql;
		}
		throw new IllegalArgumentException("关联的资源类型[refResourceType]值目前只能为[1.CfgTable]或[2.CfgSql]");
	}
	public CfgTable getRefTable() {
		return refTable;
	}
	public CfgSql getRefSql() {
		return refSql;
	}
	
	public void clear(){
		if(refSql != null){
			refSql.clear();
		}
	}
}
