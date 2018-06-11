package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;

/**
 * 数据字典资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDataDictionary extends BasicEntity implements ITable, IEntity{
	/**
	 * 编码
	 */
	private String code;
	/**
	 * 父编码编号(可为空)
	 */
	private String parentCodeId;
	/**
	 * 显示的文本
	 */
	private String codeCaption;
	/**
	 * 操作的值(value)
	 */
	private String codeValue;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 备注
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getParentCodeId() {
		return parentCodeId;
	}
	public void setParentCodeId(String parentCodeId) {
		this.parentCodeId = parentCodeId;
	}
	public String getCodeCaption() {
		return codeCaption;
	}
	public void setCodeCaption(String codeCaption) {
		this.codeCaption = codeCaption;
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
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_DATA_DICTIONARY", 0);
		table.setName("数据字典资源对象表");
		table.setComments("数据字典资源对象表");
		table.setVersion(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(12);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 50);
		codeColumn.setName("编码");
		codeColumn.setComments("编码");
		codeColumn.setOrderCode(1);
		columns.add(codeColumn);
		
		ComColumndata parentCodeIdColumn = new ComColumndata("parent_code_id", DataTypeConstants.STRING, 32);
		parentCodeIdColumn.setName("父编码编号(可为空)");
		parentCodeIdColumn.setComments("父编码编号(可为空)");
		parentCodeIdColumn.setOrderCode(2);
		columns.add(parentCodeIdColumn);
		
		ComColumndata codeCaptionColumn = new ComColumndata("code_caption", DataTypeConstants.STRING, 50);
		codeCaptionColumn.setName("显示的文本");
		codeCaptionColumn.setComments("显示的文本");
		codeCaptionColumn.setOrderCode(3);
		columns.add(codeCaptionColumn);
		
		ComColumndata codeValueColumn = new ComColumndata("code_value", DataTypeConstants.STRING, 30);
		codeValueColumn.setName("操作的值(value)");
		codeValueColumn.setComments("操作的值(value)");
		codeValueColumn.setOrderCode(4);
		columns.add(codeValueColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 150);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setOrderCode(6);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_DATA_DICTIONARY";
	}
	
	public String getEntityName() {
		return "ComDataDictionary";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("orderCode", orderCode);
		entityJson.put("belongPlatformType", belongPlatformType);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
}
