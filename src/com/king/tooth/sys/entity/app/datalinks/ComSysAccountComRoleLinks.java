package com.king.tooth.sys.entity.app.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 系统账户和角色的关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComSysAccountComRoleLinks implements Serializable, ITable {
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_ACCOUNT_ROLE_LINKS", 0);
		table.setResourceName("ComSysAccountComRoleLinks");
		table.setName("系统账户和角色的关联关系资源对象表");
		table.setComments("系统账户和角色的关联关系资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setBelongPlatformType(ISysResource.APP_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(6);
		
		ComColumndata leftIdColumn = new ComColumndata("left_id", DataTypeConstants.STRING, 32);
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		ComColumndata rightIdColumn = new ComColumndata("right_id", DataTypeConstants.STRING, 32);
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		ComColumndata orderCodeColumn = new ComColumndata("order_code", DataTypeConstants.INTEGER, 3);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setDefaultValue("0");
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_ACCOUNT_ROLE_LINKS";
	}
}
