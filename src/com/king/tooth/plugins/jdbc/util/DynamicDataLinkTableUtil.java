package com.king.tooth.plugins.jdbc.util;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.StrUtils;

/**
 * 动态的数据关系表的工具类
 * @author DougLei
 */
public class DynamicDataLinkTableUtil {
	
	/**
	 * 处理父子表，如果有父子表数据，则要获得对应的关系表tabledata实例，并存储到tabledatas集合中，统一进行创建表/删除表的操作
	 * @param tabledatas
	 */
	public static void processParentSubTable(List<CfgTabledata> tabledatas) {
		List<CfgTabledata> datalinkTables = new ArrayList<CfgTabledata>();
		for (CfgTabledata tabledata : tabledatas) {
			if(DynamicDataConstants.PARENT_SUB_TABLE == tabledata.getTableType()
					&& StrUtils.notEmpty(tabledata.getParentTableName())
					&& tabledata.getIsHavaDatalink() == 1){// 判断标示是需要主子表的
				datalinkTables.add(getDataLinkTabledata(tabledata.getDbType(), tabledata.getParentTableId(), tabledata.getParentTableName(), tabledata.getTableName()));
			}
		}
		
		if(datalinkTables.size() > 0){
			tabledatas.addAll(datalinkTables);
			datalinkTables.clear();
		}
	}
	
	/**
	 * 获取父子表关联关系表对象
	 * @param dbType
	 * @param parentId
	 * @param parentTableName
	 * @param subTableName
	 * @return
	 */
	private static CfgTabledata getDataLinkTabledata(String dbType, String parentId, String parentTableName, String subTableName){
		CfgTabledata dataLinkTable = new CfgTabledata(dbType, getDataLinkTableName(parentTableName, subTableName), 1);
		dataLinkTable.setId(parentId);// 关系表关联的tableId的值，就是父表的id   这里set到id中，在HibernateHbmHandler.createHbmMappingContent()方法中，从id取hbm文件对应的表主键。关系表对应的表主键，就是父表的id，所以这里这么存储
		dataLinkTable.setComments("父表" + parentTableName + "和子表" + subTableName + "的关系表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(3);
		
		CfgColumndata leftIdColumn = new CfgColumndata("left_id");
		leftIdColumn.setIsNullabled(0);
		leftIdColumn.setColumnType(DataTypeConstants.STRING);
		leftIdColumn.setLength(32);
		leftIdColumn.setOrderCode(1);
		columns.add(leftIdColumn);
		
		CfgColumndata rightIdColumn = new CfgColumndata("right_id");
		rightIdColumn.setIsNullabled(0);
		rightIdColumn.setColumnType(DataTypeConstants.STRING);
		rightIdColumn.setLength(32);
		rightIdColumn.setOrderCode(2);
		columns.add(rightIdColumn);
		
		CfgColumndata orderCodeColumn = new CfgColumndata("order_code");
		orderCodeColumn.setColumnType(DataTypeConstants.INTEGER);
		orderCodeColumn.setLength(4);
		orderCodeColumn.setOrderCode(3);
		columns.add(orderCodeColumn);
		
		dataLinkTable.setColumns(columns);
		return dataLinkTable;
	}
	
	/**
	 * 根据父子表名，获取关联关系的表名
	 * @param parentTableName
	 * @param subTableName
	 * @return
	 */
	private static String getDataLinkTableName(String parentTableName, String subTableName){
		return parentTableName + "_" + subTableName + ResourceNameConstants.DATALINK_TABLENAME_SUFFIX;
	}
}
