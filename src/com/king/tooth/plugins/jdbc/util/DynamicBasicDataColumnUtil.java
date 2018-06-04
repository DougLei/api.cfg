package com.king.tooth.plugins.jdbc.util;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * 动态的基础数据字段的工具类
 * <pre>
 * 	 目前，数据包括的基础字段有：
 * 		id
 * 		create_time
 * 		last_update_time
 * 		create_user_id
 * 		last_updated_user_id
 * </pre>
 * @author DougLei
 */
public class DynamicBasicDataColumnUtil {
	
	/**
	 * 给动态表对象，添加基础的字段
	 * <pre>
	 * 	 目前，数据包括的基础字段有：
	 * 		id、create_time、last_update_time
	 * </pre>
	 * @param table
	 */
	public static void initBasicColumnToTable(CfgTabledata table){
		// id
		CfgColumndata idColumn = new CfgColumndata("id");
		idColumn.setIsKey(1);
		idColumn.setIsNullabled(0);
		idColumn.setColumnType(DataTypeConstants.STRING);
		idColumn.setLength(32);
		idColumn.setComments("主键");
		idColumn.setName("主键");
		idColumn.setOrderCode(9995);
		table.getColumns().add(idColumn);
		
		if(table.getIsDatalinkTable() == 0 && !ResourceNameConstants.COMMON_DATALINK_TABLENAME.equals(table.getTableName())){// 不是关系表，才要这些字段
			// create_time
			CfgColumndata createTimeColumn = new CfgColumndata("create_time");
			createTimeColumn.setColumnType(DataTypeConstants.DATE);
			createTimeColumn.setName("创建时间");
			createTimeColumn.setComments("创建时间");
			createTimeColumn.setOrderCode(9996);
			// last_update_time
			CfgColumndata lastUpdateTimeColumn = new CfgColumndata("last_update_time");
			lastUpdateTimeColumn.setColumnType(DataTypeConstants.DATE);
			lastUpdateTimeColumn.setComments("最后修改时间");
			lastUpdateTimeColumn.setName("最后修改时间");
			lastUpdateTimeColumn.setOrderCode(9997);
			// create_user_id
			CfgColumndata createUserIdColumn = new CfgColumndata("create_user_id");
			createUserIdColumn.setColumnType(DataTypeConstants.STRING);
			createUserIdColumn.setComments("创建人主键");
			createUserIdColumn.setName("创建人主键");
			createUserIdColumn.setLength(32);
			createUserIdColumn.setOrderCode(9998);
			// last_updated_user_id
			CfgColumndata lastUpdatedUserIdColumn = new CfgColumndata("last_updated_user_id");
			lastUpdatedUserIdColumn.setColumnType(DataTypeConstants.STRING);
			lastUpdatedUserIdColumn.setComments("最后修改人主键");
			lastUpdatedUserIdColumn.setName("最后修改人主键");
			lastUpdatedUserIdColumn.setLength(32);
			lastUpdatedUserIdColumn.setOrderCode(9999);
			
			table.getColumns().add(createTimeColumn);
			table.getColumns().add(lastUpdateTimeColumn);
			table.getColumns().add(createUserIdColumn);
			table.getColumns().add(lastUpdatedUserIdColumn);
		}
	}
}
