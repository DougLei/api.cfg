package com.king.tooth.sys.entity.app;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * 组织机构资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComOrg extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 父组织主键
	 */
	private String parentId;
	/**
	 * 组织名称
	 */
	private String name;
	/**
	 * 组织简称
	 */
	private String shortName;
	/**
	 * 组织编码
	 */
	private String code;
	/**
	 * 排序值
	 */
	private Integer orderCode;

	// ---------------------------------------------------------------------------

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
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}

	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_ORG", 0);
		table.setName("组织机构资源对象表");
		table.setComments("组织机构资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setBelongPlatformType(ISysResource.APP_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(12);
		
		ComColumndata parentIdColumn = new ComColumndata("parent_id", DataTypeConstants.STRING, 32);
		parentIdColumn.setName("父组织主键");
		parentIdColumn.setComments("父组织主键");
		parentIdColumn.setOrderCode(1);
		columns.add(parentIdColumn);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 100);
		nameColumn.setName("组织名称");
		nameColumn.setComments("组织名称");
		nameColumn.setOrderCode(2);
		columns.add(nameColumn);
		
		ComColumndata shortNameColumn = new ComColumndata("short_name", DataTypeConstants.STRING, 50);
		shortNameColumn.setName("组织简称");
		shortNameColumn.setComments("组织简称");
		shortNameColumn.setOrderCode(3);
		columns.add(shortNameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 32);
		codeColumn.setName("组织编码");
		codeColumn.setComments("组织编码");
		codeColumn.setOrderCode(4);
		columns.add(codeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(5);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_ORG";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComOrg";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("orderCode", orderCode);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
