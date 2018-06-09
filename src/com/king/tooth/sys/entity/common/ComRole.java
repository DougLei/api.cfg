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
import com.king.tooth.util.StrUtils;

/**
 * 角色资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComRole extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色编码
	 */
	private String code;
	/**
	 * 角色描述
	 */
	private String descs;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 是否启用
	 */
	private Integer isEnabled;

	// ---------------------------------------------------------------------------
	
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = code;
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
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
	
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_ROLE", 0);
		table.setName("[通用的]角色资源对象表");
		table.setComments("[通用的]角色资源对象表");
		table.setVersion(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(11);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 30);
		nameColumn.setName("角色名称");
		nameColumn.setComments("角色名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 20);
		codeColumn.setName("角色编码");
		codeColumn.setComments("角色编码");
		codeColumn.setIsNullabled(0);
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", DataTypeConstants.STRING, 100);
		descsColumn.setName("角色描述");
		descsColumn.setComments("角色描述");
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否启用");
		isEnabledColumn.setComments("是否启用");
		isEnabledColumn.setDefaultValue("1");
		isEnabledColumn.setOrderCode(5);
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_ROLE";
	}
	
	public String getEntityName() {
		return "ComRole";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("orderCode", orderCode);
		entityJson.put("isEnabled", isEnabled);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
}
