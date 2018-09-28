package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
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
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(14);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinDataType.STRING, 50);
		codeColumn.setName("编码");
		codeColumn.setComments("编码");
		codeColumn.setOrderCode(1);
		columns.add(codeColumn);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", BuiltinDataType.STRING, 32);
		parentIdColumn.setName("父编码主键");
		parentIdColumn.setComments("父编码主键(可为空)");
		parentIdColumn.setOrderCode(2);
		columns.add(parentIdColumn);
		
		ComColumndata captionColumn = new ComColumndata("caption", BuiltinDataType.STRING, 200);
		captionColumn.setName("显示的文本");
		captionColumn.setComments("显示的文本");
		captionColumn.setOrderCode(3);
		columns.add(captionColumn);
		
		ComColumndata valColumn = new ComColumndata("val", BuiltinDataType.STRING, 400);
		valColumn.setName("后台操作的值");
		valColumn.setComments("后台操作的值(value)");
		valColumn.setOrderCode(4);
		columns.add(valColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", BuiltinDataType.STRING, 150);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效：默认值为1");
		isEnabledColumn.setDefaultValue("1");
		isEnabledColumn.setOrderCode(6);
		columns.add(isEnabledColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinDataType.STRING, 150);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setOrderCode(7);
		columns.add(commentsColumn);
		
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
