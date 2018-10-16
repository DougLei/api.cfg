package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 数据字典表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class SysDataDictionary extends BasicEntity implements ITable, IEntity{
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 父编码编号(可为空)
	 */
	private String parentId;
	/**
	 * 显示的文本
	 */
	private String caption;
	/**
	 * 后台操作的值(value)
	 */
	private String val;
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
	/**
	 * 数据标识，默认值为1
	 * <p>1:系统内置</p>
	 * <p>2:用户定义</p>
	 */
	private Integer dataFlag;
	/**
	 * 是否删除
	 */
	private Integer isDelete;
	
	//-------------------------------------------------------------------------
	public SysDataDictionary() {
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
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
	public Integer getDataFlag() {
		return dataFlag;
	}
	public void setDataFlag(Integer dataFlag) {
		this.dataFlag = dataFlag;
	}
	public Integer getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(9+7);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 50);
		codeColumn.setName("编码");
		codeColumn.setComments("编码");
		columns.add(codeColumn);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父编码主键");
		parentIdColumn.setComments("父编码主键(可为空)");
		columns.add(parentIdColumn);
		
		ComColumndata captionColumn = new ComColumndata("caption", DataTypeConstants.STRING, 200);
		captionColumn.setName("显示的文本");
		captionColumn.setComments("显示的文本");
		columns.add(captionColumn);
		
		ComColumndata valColumn = new ComColumndata("val", DataTypeConstants.STRING, 400);
		valColumn.setName("后台操作的值");
		valColumn.setComments("后台操作的值(value)");
		columns.add(valColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		columns.add(orderCodeColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效：默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 150);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		columns.add(commentsColumn);
		
		ComColumndata dataFlagColumn = new ComColumndata("data_flag", DataTypeConstants.INTEGER, 2);
		dataFlagColumn.setName("数据标识");
		dataFlagColumn.setComments("默认值为1，1:系统内置，2:用户定义");
		dataFlagColumn.setDefaultValue("1");
		columns.add(dataFlagColumn);
		
		ComColumndata isDeleteColumn = new ComColumndata("is_delete", DataTypeConstants.INTEGER, 1);
		isDeleteColumn.setName("是否删除");
		isDeleteColumn.setComments("是否删除");
		isDeleteColumn.setDefaultValue("0");
		columns.add(isDeleteColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("数据字典表");
		table.setComments("数据字典表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "SYS_DATA_DICTIONARY";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysDataDictionary";
	}
}
