package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;

/**
 * 资源模型表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgResourceModelRelations extends BasicEntity implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 关联的资源模型id
	 */
	private String refResourceModelId;
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
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	private Integer isEnabled;
	/**
	 * 关联父资源的方式
	 * <p>默认值为1，1:子资源中有属性关联存储父资源的id、2:通过关系表，关联主子资源的数据</p>
	 */
	private Integer refParentResourceMethod;
	/**
	 * 主资源中关联子资源的key名
	 * <p>默认值为children，这个是在主资源对象中，用指定的key值存储子资源对象数组</p>
	 */
	private String pRefSubResourceKeyName;
	/**
	 * 关联父资源的属性id
	 * <p>默认值为parentId，当ref_parent_resource_method=1时，需要指定子资源的哪个属性，存储父资源的id值</p>
	 */
	private String refParentResourcePropId;
	
	//-------------------------------------------------------------------------
	public String getRefResourceModelId() {
		return refResourceModelId;
	}
	public void setRefResourceModelId(String refResourceModelId) {
		this.refResourceModelId = refResourceModelId;
	}
	public String getParentId() {
		return parentId;
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
	public Integer getRefParentResourceMethod() {
		return refParentResourceMethod;
	}
	public void setRefParentResourceMethod(Integer refParentResourceMethod) {
		this.refParentResourceMethod = refParentResourceMethod;
	}
	public String getpRefSubResourceKeyName() {
		return pRefSubResourceKeyName;
	}
	public void setpRefSubResourceKeyName(String pRefSubResourceKeyName) {
		this.pRefSubResourceKeyName = pRefSubResourceKeyName;
	}
	public String getRefParentResourcePropId() {
		return refParentResourcePropId;
	}
	public void setRefParentResourcePropId(String refParentResourcePropId) {
		this.refParentResourcePropId = refParentResourcePropId;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(8+7);
		
		CfgColumn refResourceModelIdColumn = new CfgColumn("ref_resource_model_id", DataTypeConstants.STRING, 32);
		refResourceModelIdColumn.setName("关联的资源模型id");
		refResourceModelIdColumn.setComments("关联的资源模型id");
		columns.add(refResourceModelIdColumn);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父id");
		parentIdColumn.setComments("父id");
		columns.add(parentIdColumn);
		
		CfgColumn refResourceIdColumn = new CfgColumn("ref_resource_id", DataTypeConstants.STRING, 32);
		refResourceIdColumn.setName("关联的资源id");
		refResourceIdColumn.setComments("CfgTable或CfgSql资源的id");
		columns.add(refResourceIdColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		CfgColumn refParentResourceMethodColumn = new CfgColumn("ref_parent_resource_method", DataTypeConstants.INTEGER, 1);
		refParentResourceMethodColumn.setName("关联父资源的方式");
		refParentResourceMethodColumn.setComments("默认值为1，1:子资源中有属性关联存储父资源的id、2:通过关系表，关联主子资源的数据");
		refParentResourceMethodColumn.setDefaultValue("1");
		columns.add(refParentResourceMethodColumn);
		
		CfgColumn pRefSubResourceKeyNameColumn = new CfgColumn("p_ref_sub_resource_key_name", DataTypeConstants.STRING, 60);
		pRefSubResourceKeyNameColumn.setName("主资源中关联子资源的key名");
		pRefSubResourceKeyNameColumn.setComments("默认值为children，这个是在主资源对象中，用指定的key值存储子资源对象数组");
		pRefSubResourceKeyNameColumn.setDefaultValue("children");
		columns.add(pRefSubResourceKeyNameColumn);
		
		CfgColumn refParentResourcePropIdColumn = new CfgColumn("ref_parent_resource_prop_id", DataTypeConstants.STRING, 32);
		refParentResourcePropIdColumn.setName("关联父资源的属性id");
		refParentResourcePropIdColumn.setComments("默认值为parentId，当ref_parent_resource_method=1时，需要指定子资源的哪个属性，存储父资源的id值");
		refParentResourcePropIdColumn.setDefaultValue("parentId");
		columns.add(refParentResourcePropIdColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("资源模型关系表");
		table.setComments("资源模型关系表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_RESOURCE_MODEL_RELATIONS";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgResourceModelRelations";
	}
	
	public String validNotNullProps() {
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
}
