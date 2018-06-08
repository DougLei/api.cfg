package com.king.tooth.plugins.jdbc.util;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.common.ComColumndata;
import com.king.tooth.sys.entity.common.ComTabledata;

/**
 * 动态的基础数据字段的工具类
 * @author DougLei
 */
public class DynamicBasicDataColumnUtil {
	
	/**
	 * 给动态表对象，添加基础的字段
	 * @param table
	 */
	public static void initBasicColumnToTable(ComTabledata table){
		// id
		ComColumndata idColumn = new ComColumndata("id", DataTypeConstants.STRING, 32);
		idColumn.setIsPrimaryKey(1);
		idColumn.setIsNullabled(0);
		idColumn.setComments("主键");
		idColumn.setName("主键");
		idColumn.setOrderCode(9901);
		table.getColumns().add(idColumn);
		
		// projectId
		ComColumndata projectIdColumn = new ComColumndata("project_id", DataTypeConstants.STRING, 32);
		projectIdColumn.setComments("关联的项目主键");
		projectIdColumn.setName("关联的项目主键");
		projectIdColumn.setOrderCode(9902);
		table.getColumns().add(projectIdColumn);
		
		if(table.getIsResource() == 1){
			ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
			isEnabledColumn.setName("资源是否有效");
			isEnabledColumn.setComments("资源是否有效");
			isEnabledColumn.setDefaultValue("1");
			isEnabledColumn.setOrderCode(9903);
			table.getColumns().add(isEnabledColumn);
			
			ComColumndata reqResourceMethodColumn = new ComColumndata("req_resource_method", DataTypeConstants.STRING, 20);
			reqResourceMethodColumn.setName("请求资源的方法");
			reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
			reqResourceMethodColumn.setDefaultValue(ISysResource.ALL);
			reqResourceMethodColumn.setOrderCode(9904);
			table.getColumns().add(reqResourceMethodColumn);

			ComColumndata isBuiltinColumn = new ComColumndata("is_builtin", DataTypeConstants.INTEGER, 1);
			isBuiltinColumn.setName("是否内置资源");
			isBuiltinColumn.setComments("是否内置资源:这个字段由开发人员控制，不开放给用户");
			isBuiltinColumn.setDefaultValue("0");
			isBuiltinColumn.setOrderCode(9905);
			table.getColumns().add(isBuiltinColumn);

			ComColumndata isNeedDeployColumn = new ComColumndata("is_need_deploy", DataTypeConstants.INTEGER, 1);
			isNeedDeployColumn.setName("资源是否需要发布");
			isNeedDeployColumn.setComments("资源是否需要发布");
			isNeedDeployColumn.setDefaultValue("1");
			isNeedDeployColumn.setOrderCode(9906);
			table.getColumns().add(isNeedDeployColumn);
			
			ComColumndata isDeployedColumn = new ComColumndata("is_deployed", DataTypeConstants.INTEGER, 1);
			isDeployedColumn.setName("资源是否发布");
			isDeployedColumn.setComments("资源是否发布");
			isDeployedColumn.setDefaultValue("0");
			isDeployedColumn.setOrderCode(9907);
			table.getColumns().add(isDeployedColumn);
		}
		
		if(table.getIsDatalinkTable() == 0 && !ResourceNameConstants.COMMON_DATALINK_TABLENAME.equals(table.getTableName())){// 不是关系表，才要这些字段
			ComColumndata createTimeColumn = new ComColumndata("create_time", DataTypeConstants.DATE, 0);
			createTimeColumn.setName("创建时间");
			createTimeColumn.setComments("创建时间");
			createTimeColumn.setOrderCode(9908);
			table.getColumns().add(createTimeColumn);
			
			ComColumndata lastUpdateTimeColumn = new ComColumndata("last_update_time", DataTypeConstants.DATE, 0);
			lastUpdateTimeColumn.setComments("最后修改时间");
			lastUpdateTimeColumn.setName("最后修改时间");
			lastUpdateTimeColumn.setOrderCode(9909);
			table.getColumns().add(lastUpdateTimeColumn);
			
			ComColumndata createUserIdColumn = new ComColumndata("create_user_id", DataTypeConstants.STRING, 32);
			createUserIdColumn.setComments("创建人主键");
			createUserIdColumn.setName("创建人主键");
			createUserIdColumn.setOrderCode(9910);
			table.getColumns().add(createUserIdColumn);
			
			ComColumndata lastUpdatedUserIdColumn = new ComColumndata("last_updated_user_id", DataTypeConstants.STRING, 32);
			lastUpdatedUserIdColumn.setComments("最后修改人主键");
			lastUpdatedUserIdColumn.setName("最后修改人主键");
			lastUpdatedUserIdColumn.setOrderCode(9911);
			table.getColumns().add(lastUpdatedUserIdColumn);
		}
	}
}
