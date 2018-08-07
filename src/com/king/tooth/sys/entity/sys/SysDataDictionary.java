package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.dm.DmPublishBasicData;

/**
 * 数据字典表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SysDataDictionary extends BasicEntity implements ITable, IEntity{
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_DATA_DICTIONARY", 0);
		table.setName("数据字典表");
		table.setComments("数据字典表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(14);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinCodeDataType.STRING, 50);
		codeColumn.setName("编码");
		codeColumn.setComments("编码");
		codeColumn.setOrderCode(1);
		columns.add(codeColumn);
		
		ComColumndata parentCodeIdColumn = new ComColumndata("parent_code_id", BuiltinCodeDataType.STRING, 32);
		parentCodeIdColumn.setName("父编码编号(可为空)");
		parentCodeIdColumn.setComments("父编码编号(可为空)");
		parentCodeIdColumn.setOrderCode(2);
		columns.add(parentCodeIdColumn);
		
		ComColumndata codeCaptionColumn = new ComColumndata("code_caption", BuiltinCodeDataType.STRING, 50);
		codeCaptionColumn.setName("显示的文本");
		codeCaptionColumn.setComments("显示的文本");
		codeCaptionColumn.setOrderCode(3);
		columns.add(codeCaptionColumn);
		
		ComColumndata codeValueColumn = new ComColumndata("code_value", BuiltinCodeDataType.STRING, 30);
		codeValueColumn.setName("操作的值(value)");
		codeValueColumn.setComments("操作的值(value)");
		codeValueColumn.setOrderCode(4);
		columns.add(codeValueColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", BuiltinCodeDataType.STRING, 150);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效：默认值为1");
		isEnabledColumn.setDefaultValue("1");
		isEnabledColumn.setOrderCode(6);
		columns.add(isEnabledColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinCodeDataType.STRING, 150);
		commentsColumn.setName("备注");
		commentsColumn.setComments("备注");
		commentsColumn.setOrderCode(7);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_DATA_DICTIONARY";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysDataDictionary";
	}
	
	/**
	 * 转换为要发布的基础数据资源对象
	 * @return
	 */
	public DmPublishBasicData turnToPublishBasicData(Integer belongPlatformType){
		DmPublishBasicData publishBasicData = new DmPublishBasicData();
		publishBasicData.setBasicDataResourceName(getEntityName());
		publishBasicData.setBasicDataJsonStr(JSONObject.toJSONString(this));
		publishBasicData.setBelongPlatformType(belongPlatformType);
		return publishBasicData;
	}
}
