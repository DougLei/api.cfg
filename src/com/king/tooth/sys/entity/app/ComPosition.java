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
 * 职务资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComPosition extends BasicEntity implements ITable, IEntity{
	
	/**
	 * 职务名称
	 */
	private String name;
	/**
	 * 职务编码
	 */
	private String code;
	/**
	 * 职务描述
	 */
	private String descs;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	
	// ---------------------------------------------------------------------------

	public String getName() {
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

	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_POSITION", 0);
		table.setName("职务资源对象表");
		table.setComments("职务资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setBelongPlatformType(ISysResource.APP_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(11);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 60);
		nameColumn.setName("职务名称");
		nameColumn.setComments("职务名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", DataTypeConstants.STRING, 32);
		codeColumn.setName("职务编码");
		codeColumn.setComments("职务编码");
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", DataTypeConstants.STRING, 100);
		descsColumn.setName("职务描述");
		descsColumn.setComments("职务描述");
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_POSITION";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComPosition";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("orderCode", orderCode);
		super.processBasicEntityProps(entityJson);
		return entityJson.getEntityJson();
	}
}
