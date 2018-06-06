package com.king.tooth.sys.entity.common.datalinks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.TableConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * [通用的]数据库和sql脚本的关联关系资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComDatabaseComSqlScriptLinks implements Serializable, ITable {
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_DATABASE_SQLSCRIPT_LINKS");
		table.setResourceName("ComDatabaseComSqlScriptLinks");
		table.setName("[配置系统]数据库和sql脚本的关联关系资源对象");
		table.setComments("[配置系统]数据库和sql脚本的关联关系资源对象");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(4);
		
		CfgColumndata leftIdColumn = new CfgColumndata("left_id");
		leftIdColumn.setName("左资源id");
		leftIdColumn.setComments("左资源id(默认即主表、主资源)");
		leftIdColumn.setColumnType(DataTypeConstants.STRING);
		leftIdColumn.setLength(32);
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		CfgColumndata rightIdColumn = new CfgColumndata("right_id");
		rightIdColumn.setName("右资源id");
		rightIdColumn.setComments("右资源id(默认即子表、子资源)");
		rightIdColumn.setColumnType(DataTypeConstants.STRING);
		rightIdColumn.setLength(32);
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		table.setColumns(columns);
		table.setIsBuiltin(1);
		table.setReqResourceMethod(ISysResource.NONE);
		table.setPlatformType(TableConstants.IS_COMMON_PLATFORM_TYPE);
		table.setIsCreateHbm(1);
		return table;
	}

	public String toDropTable() {
		return "COM_DATABASE_SQLSCRIPT_LINKS";
	}
}
