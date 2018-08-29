package com.king.tooth.sys.entity.sys;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 用户组表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
public class SysUserGroup extends BasicEntity implements ITable, IEntity{

	/**
	 * 用户组名称
	 */
	private String name;
	/**
	 * 用户组编码
	 */
	private String code;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	/**
	 * 描述
	 */
	private String descs;
	/**
	 * 是否有效
	 */
	private Integer isEnabled;
	
	//-------------------------------------------------------------------------

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
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("SYS_USER_GROUP", 0);
		table.setName("用户组表");
		table.setComments("用户组表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(12);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinCodeDataType.STRING, 60);
		nameColumn.setName("用户组名称");
		nameColumn.setComments("用户组名称");
		columns.add(nameColumn);
		
		ComColumndata codeColumn = new ComColumndata("code", BuiltinCodeDataType.STRING, 32);
		codeColumn.setName("用户组编码");
		codeColumn.setComments("用户组编码");
		columns.add(codeColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 4);
		orderCodeColumn.setName("排序值");
		orderCodeColumn.setComments("排序值");
		columns.add(orderCodeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", BuiltinCodeDataType.STRING, 200);
		descsColumn.setName("描述");
		descsColumn.setComments("描述");
		columns.add(descsColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", BuiltinCodeDataType.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("是否有效");
		columns.add(isEnabledColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "SYS_USER_GROUP";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "SysUserGroup";
	}
}
