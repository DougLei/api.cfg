package com.king.tooth.sys.entity.common.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 人员和部门的关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComUserComDeptLinks implements Serializable, ITable {
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_USER_DEPT_LINKS", 0);
		table.setResourceName("ComUserComDeptLinks");
		table.setName("人员和部门的关联关系资源对象表");
		table.setComments("人员和部门的关联关系资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(7);
		
		ComColumndata leftIdColumn = new ComColumndata("left_id", BuiltinCodeDataType.STRING, 32);
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		ComColumndata rightIdColumn = new ComColumndata("right_id", BuiltinCodeDataType.STRING, 32);
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		ComColumndata isMainColumn = new ComColumndata("is_main", BuiltinCodeDataType.INTEGER, 1);
		isMainColumn.setName("是否是主要部门");
		isMainColumn.setComments("是否是主要部门：即默认部门，其他的都属于兼职部门，默认值是0");
		isMainColumn.setDefaultValue("0");
		isMainColumn.setOrderCode(4);
		columns.add(isMainColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_USER_DEPT_LINKS";
	}
}
