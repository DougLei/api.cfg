package com.king.tooth.sys.entity.cfg.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 项目和hbm的关系表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgProjectHbmLinks implements Serializable, ITable {
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("CFG_PROJECT_HBM_LINKS", 0);
		table.setResourceName("CfgProjectHbmLinks");
		table.setName("项目和hbm的关系表");
		table.setComments("项目和hbm的关系表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(6);
		
		ComColumndata leftIdColumn = new ComColumndata("left_id", BuiltinDataType.STRING, 32);
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		ComColumndata rightIdColumn = new ComColumndata("right_id", BuiltinDataType.STRING, 32);
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinDataType.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "CFG_PROJECT_HBM_LINKS";
	}
}
