package com.king.tooth.plugins.jdbc.util;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.StrUtils;

/**
 * 动态的数据关系表的工具类
 * @author DougLei
 */
public class DynamicDataLinkTableUtil {
	
	/**
	 * 处理父子表，如果有父子表数据，则要获得对应的关系表tabledata实例，并存储到tabledatas集合中，统一进行创建表/删除表的操作
	 * @param tabledatas
	 * @param isNeedColumns 是否需要列集合，比如添加的时候需要，但是删除的时候不需要
	 */
	public static void processParentSubTable(List<ComTabledata> tabledatas, boolean isNeedColumns) {
		List<ComTabledata> datalinkTables = new ArrayList<ComTabledata>();
		for (ComTabledata tabledata : tabledatas) {
			if(tabledata.getTableType()!=null  
					&& BuiltinDatabaseData.PARENT_SUB_TABLE == tabledata.getTableType()
					&& StrUtils.notEmpty(tabledata.getParentTableName())
					&& tabledata.getIsHavaDatalink() != null
					&& tabledata.getIsHavaDatalink() == 1){// 判断标示是需要主子表的
				datalinkTables.add(getDataLinkTabledata(tabledata.getDbType(), tabledata.getParentTableId(), tabledata.getParentTableName(), tabledata.getTableName(), isNeedColumns));
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
	 * @param isNeedColumns
	 * @return
	 */
	private static ComTabledata getDataLinkTabledata(String dbType, String parentId, String parentTableName, String subTableName, boolean isNeedColumns){
		ComTabledata dataLinkTable = new ComTabledata(getDataLinkTableName(parentTableName, subTableName), 1);
		dataLinkTable.setDbType(dbType);
		dataLinkTable.setParentTableId(parentId);
		dataLinkTable.setComments("父表" + parentTableName + "和子表" + subTableName + "的关系表");
		dataLinkTable.setReqResourceMethod(ISysResource.NONE);
		String result = dataLinkTable.analysisResourceProp();
		if(result != null){
			throw new IllegalArgumentException(result);
		}
		
		if(isNeedColumns){
			List<ComColumndata> columns = new ArrayList<ComColumndata>(3);
			
			ComColumndata leftIdColumn = new ComColumndata("left_id", BuiltinCodeDataType.STRING, 32);
			leftIdColumn.setIsNullabled(0);
			leftIdColumn.setOrderCode(1);
			columns.add(leftIdColumn);
			
			ComColumndata rightIdColumn = new ComColumndata("right_id", BuiltinCodeDataType.STRING, 32);
			rightIdColumn.setIsNullabled(0);
			rightIdColumn.setOrderCode(2);
			columns.add(rightIdColumn);
			
			ComColumndata orderCodeColumn = new ComColumndata("order_code", BuiltinCodeDataType.INTEGER, 4);
			orderCodeColumn.setOrderCode(3);
			orderCodeColumn.setDefaultValue("0");
			columns.add(orderCodeColumn);
			
			dataLinkTable.setColumns(columns);
		}
		return dataLinkTable;
	}
	
	/**
	 * 根据父子表名，获取关联关系的表名
	 * @param parentTableName
	 * @param subTableName
	 * @return
	 */
	private static String getDataLinkTableName(String parentTableName, String subTableName){
		return parentTableName + "_" + subTableName + "_LINKS";
	}
}
