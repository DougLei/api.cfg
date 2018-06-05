package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * [通用的]数据字典资源对象
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
	private int orderCode;
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
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setCodeCaption(String codeCaption) {
		this.codeCaption = codeCaption;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public int getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_DATA_DICTIONARY");
		table.setName("[通用的]数据字典资源对象表");
		table.setComments("[通用的]数据字典资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(12);
		
		CfgColumndata codeColumn = new CfgColumndata("code");
		codeColumn.setName("编码");
		codeColumn.setComments("编码");
		codeColumn.setColumnType(DataTypeConstants.STRING);
		codeColumn.setLength(70);
		codeColumn.setOrderCode(1);
		columns.add(codeColumn);
		
		CfgColumndata parentCodeIdColumn = new CfgColumndata("parent_code_id");
		parentCodeIdColumn.setName("父编码编号(可为空)");
		parentCodeIdColumn.setComments("父编码编号(可为空)");
		parentCodeIdColumn.setColumnType(DataTypeConstants.STRING);
		parentCodeIdColumn.setLength(32);
		parentCodeIdColumn.setOrderCode(2);
		columns.add(parentCodeIdColumn);
		
		CfgColumndata codeCaptionColumn = new CfgColumndata("code_caption");
		codeCaptionColumn.setName("显示的文本");
		codeCaptionColumn.setComments("显示的文本");
		codeCaptionColumn.setColumnType(DataTypeConstants.STRING);
		codeCaptionColumn.setLength(50);
		codeCaptionColumn.setOrderCode(3);
		columns.add(codeCaptionColumn);
		
		CfgColumndata codeValueColumn = new CfgColumndata("code_value");
		codeValueColumn.setName("操作的值(value)");
		codeValueColumn.setComments("操作的值(value)");
		codeValueColumn.setColumnType(DataTypeConstants.STRING);
		codeValueColumn.setLength(30);
		codeValueColumn.setOrderCode(4);
		columns.add(codeValueColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		CfgColumndata commentsColumn = new CfgColumndata("comments");
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setColumnType(DataTypeConstants.STRING);
		commentsColumn.setLength(200);
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
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
