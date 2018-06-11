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
		idColumn.setName("主键");
		idColumn.setComments("主键");
		idColumn.setOrderCode(9901);
		table.getColumns().add(idColumn);
		
		// projectId
		ComColumndata projectIdColumn = new ComColumndata("project_id", DataTypeConstants.STRING, 32);
		projectIdColumn.setName("关联的项目主键");
		projectIdColumn.setComments("关联的项目主键");
		projectIdColumn.setOrderCode(9902);
		table.getColumns().add(projectIdColumn);
		
		// belongPlatformType
		ComColumndata belongPlatformTypeColumn = new ComColumndata("belong_platform_type", DataTypeConstants.INTEGER, 1);
		belongPlatformTypeColumn.setName("所属的平台类型");
		belongPlatformTypeColumn.setComments("所属的平台类型:1：配置平台、2：运行平台、3：通用(这个类型由后端开发者控制)");
		belongPlatformTypeColumn.setDefaultValue("2");
		belongPlatformTypeColumn.setOrderCode(9903);
		table.getColumns().add(belongPlatformTypeColumn);
		
		if(table.getIsResource() == 1){
			ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
			isEnabledColumn.setName("资源是否有效");
			isEnabledColumn.setComments("资源是否有效");
			isEnabledColumn.setDefaultValue("1");
			isEnabledColumn.setOrderCode(9904);
			table.getColumns().add(isEnabledColumn);
			
			ComColumndata reqResourceMethodColumn = new ComColumndata("req_resource_method", DataTypeConstants.STRING, 20);
			reqResourceMethodColumn.setName("请求资源的方法");
			reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
			reqResourceMethodColumn.setDefaultValue(ISysResource.ALL);
			reqResourceMethodColumn.setOrderCode(9905);
			table.getColumns().add(reqResourceMethodColumn);

			ComColumndata isBuiltinColumn = new ComColumndata("is_builtin", DataTypeConstants.INTEGER, 1);
			isBuiltinColumn.setName("是否内置资源");
			isBuiltinColumn.setComments("是否内置资源:这个字段由开发人员控制，不开放给用户");
			isBuiltinColumn.setDefaultValue("0");
			isBuiltinColumn.setOrderCode(9906);
			table.getColumns().add(isBuiltinColumn);

			ComColumndata isNeedDeployColumn = new ComColumndata("is_need_deploy", DataTypeConstants.INTEGER, 1);
			isNeedDeployColumn.setName("资源是否需要发布");
			isNeedDeployColumn.setComments("资源是否需要发布");
			isNeedDeployColumn.setDefaultValue("1");
			isNeedDeployColumn.setOrderCode(9907);
			table.getColumns().add(isNeedDeployColumn);
			
			ComColumndata isCreatedColumn = new ComColumndata("is_created", DataTypeConstants.INTEGER, 1);
			isCreatedColumn.setName("资源是否被创建");
			isCreatedColumn.setComments("资源是否被创建：在配置平台中，主要是给平台开发人员使用，也是标识表资源是否被加载到sessionFactory中；在运行平台中，这个字段标识资源是否被加载，主要是指表资源是否被加载到sessionFactory中");
			isCreatedColumn.setDefaultValue("0");
			isCreatedColumn.setOrderCode(9908);
			table.getColumns().add(isCreatedColumn);
		}
		
		if(table.getIsDatalinkTable() == 0 && !table.getTableName().endsWith(ResourceNameConstants.DATALINK_TABLENAME_SUFFIX)){// 不是关系表，才要这些字段
			ComColumndata createTimeColumn = new ComColumndata("create_time", DataTypeConstants.DATE, 0);
			createTimeColumn.setName("创建时间");
			createTimeColumn.setComments("创建时间");
			createTimeColumn.setOrderCode(9909);
			table.getColumns().add(createTimeColumn);
			
			ComColumndata lastUpdateTimeColumn = new ComColumndata("last_update_time", DataTypeConstants.DATE, 0);
			lastUpdateTimeColumn.setComments("最后修改时间");
			lastUpdateTimeColumn.setName("最后修改时间");
			lastUpdateTimeColumn.setOrderCode(9910);
			table.getColumns().add(lastUpdateTimeColumn);
			
			ComColumndata createUserIdColumn = new ComColumndata("create_user_id", DataTypeConstants.STRING, 32);
			createUserIdColumn.setComments("创建人主键");
			createUserIdColumn.setName("创建人主键");
			createUserIdColumn.setOrderCode(9911);
			table.getColumns().add(createUserIdColumn);
			
			ComColumndata lastUpdatedUserIdColumn = new ComColumndata("last_updated_user_id", DataTypeConstants.STRING, 32);
			lastUpdatedUserIdColumn.setComments("最后修改人主键");
			lastUpdatedUserIdColumn.setName("最后修改人主键");
			lastUpdatedUserIdColumn.setOrderCode(9912);
			table.getColumns().add(lastUpdatedUserIdColumn);
		}
	}
}
