package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 编码数据字典表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgCodeDataDictionary extends BasicEntity implements IEntity{
	
	/**
	 * 父id
	 * <p>可为空</p>
	 */
	private String parentId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 属性值
	 * <p>传入属性值，根据属性值，返回对应的编码值</p>
	 */
	private String propValue;
	/**
	 * 编码值
	 */
	private String codeValue;
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
	 * 备注
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(7+7);
		
		CfgColumn parentIdColumn = new CfgColumn("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父id");
		parentIdColumn.setComments("可为空");
		columns.add(parentIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 50);
		nameColumn.setName("名称");
		nameColumn.setComments("名称");
		columns.add(nameColumn);
		
		CfgColumn propValueColumn = new CfgColumn("prop_value", DataTypeConstants.STRING, 50);
		propValueColumn.setName("属性值");
		propValueColumn.setComments("传入属性值，根据属性值，返回对应的编码值");
		columns.add(propValueColumn);
		
		CfgColumn codeValueColumn = new CfgColumn("code_value", DataTypeConstants.STRING, 50);
		codeValueColumn.setName("编码值");
		codeValueColumn.setComments("编码值");
		columns.add(codeValueColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 2);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		CfgColumn isEnabledColumn = new CfgColumn("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效：默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		CfgColumn commentsColumn = new CfgColumn("comments", DataTypeConstants.STRING, 150);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		columns.add(commentsColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("编码数据字典表");
		table.setRemark("编码数据字典表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_CODE_DATA_DICTIONARY";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgCodeDataDictionary";
	}
}
