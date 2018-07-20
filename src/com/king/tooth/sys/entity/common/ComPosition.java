package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

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
	
	private Integer booleanVar;
	private Double doubleVar;
	private Date dateVar;
	
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
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(11);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinCodeDataType.STRING, 60);
		nameColumn.setName("职务名称");
		nameColumn.setComments("职务名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinCodeDataType.STRING, 32);
		codeColumn.setName("职务编码");
		codeColumn.setComments("职务编码");
		codeColumn.setOrderCode(2);
		columns.add(codeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", BuiltinCodeDataType.STRING, 100);
		descsColumn.setName("职务描述");
		descsColumn.setComments("职务描述");
		descsColumn.setOrderCode(3);
		columns.add(descsColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 3);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		orderCodeColumn.setOrderCode(4);
		columns.add(orderCodeColumn);
		
		ComColumndata booleanVarColumn = new ComColumndata("boolean_var", BuiltinCodeDataType.BOOLEAN, 0);
		booleanVarColumn.setName("布尔值测试");
		booleanVarColumn.setComments("布尔值测试");
		booleanVarColumn.setOrderCode(5);
		columns.add(booleanVarColumn);
		
		ComColumndata doubleVarColumn = new ComColumndata("double_var", BuiltinCodeDataType.DOUBLE, 6);
		doubleVarColumn.setName("浮点型测试");
		doubleVarColumn.setComments("浮点型测试");
		doubleVarColumn.setPrecision(2);
		doubleVarColumn.setOrderCode(6);
		columns.add(doubleVarColumn);
		
		ComColumndata dateVarColumn = new ComColumndata("date_var", BuiltinCodeDataType.DATE, 4);
		dateVarColumn.setName("日期型测试");
		dateVarColumn.setComments("日期型测试");
		dateVarColumn.setOrderCode(7);
		columns.add(dateVarColumn);
		
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
	
	public Integer getBooleanVar() {
		return booleanVar;
	}
	public void setBooleanVar(Integer booleanVar) {
		this.booleanVar = booleanVar;
	}
	public Double getDoubleVar() {
		return doubleVar;
	}
	public void setDoubleVar(Double doubleVar) {
		this.doubleVar = doubleVar;
	}
	public Date getDateVar() {
		return dateVar;
	}
	public void setDateVar(Date dateVar) {
		this.dateVar = dateVar;
	}
}
