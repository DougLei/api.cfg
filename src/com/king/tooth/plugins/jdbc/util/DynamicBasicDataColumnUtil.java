package com.king.tooth.plugins.jdbc.util;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;

/**
 * 动态的基础数据字段的工具类
 * @author DougLei
 */
public class DynamicBasicDataColumnUtil {
	
	/**
	 * 给动态表对象，添加基础的字段
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
		idColumn.setOrderCode(9901);
		table.getColumns().add(idColumn);
		
		// projectId
		CfgColumndata projectIdColumn = new CfgColumndata("project_id");
		projectIdColumn.setColumnType(DataTypeConstants.STRING);
		projectIdColumn.setLength(32);
		projectIdColumn.setComments("关联的项目主键");
		projectIdColumn.setName("关联的项目主键");
		projectIdColumn.setOrderCode(9902);
		table.getColumns().add(projectIdColumn);
		
		if(table.getIsResource() == 1){
			CfgColumndata isEnabledColumn = new CfgColumndata("is_enabled");
			isEnabledColumn.setName("资源是否有效");
			isEnabledColumn.setComments("资源是否有效");
			isEnabledColumn.setColumnType(DataTypeConstants.INTEGER);
			isEnabledColumn.setLength(1);
			isEnabledColumn.setOrderCode(9903);
			table.getColumns().add(isEnabledColumn);
			
			CfgColumndata validDateColumn = new CfgColumndata("valid_date");
			validDateColumn.setName("资源有效期");
			validDateColumn.setComments("资源有效期");
			validDateColumn.setColumnType(DataTypeConstants.DATE);
			validDateColumn.setOrderCode(9904);
			table.getColumns().add(validDateColumn);
			
			CfgColumndata isNeedDeployColumn = new CfgColumndata("is_need_deploy");
			isNeedDeployColumn.setName("资源是否需要发布");
			isNeedDeployColumn.setComments("资源是否需要发布");
			isNeedDeployColumn.setColumnType(DataTypeConstants.INTEGER);
			isNeedDeployColumn.setLength(1);
			isNeedDeployColumn.setOrderCode(9905);
			table.getColumns().add(isNeedDeployColumn);

			CfgColumndata reqResourceMethodColumn = new CfgColumndata("req_resource_method");
			reqResourceMethodColumn.setName("请求资源的方法");
			reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
			reqResourceMethodColumn.setColumnType(DataTypeConstants.STRING);
			reqResourceMethodColumn.setLength(20);
			reqResourceMethodColumn.setOrderCode(9906);
			table.getColumns().add(reqResourceMethodColumn);

			CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
			isBuiltinColumn.setName("是否内置资源");
			isBuiltinColumn.setComments("是否内置资源");
			isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
			isBuiltinColumn.setLength(1);
			isBuiltinColumn.setOrderCode(9907);
			table.getColumns().add(isBuiltinColumn);

			CfgColumndata platformTypeColumn = new CfgColumndata("platform_type");
			platformTypeColumn.setName("资源所属于的平台类型");
			platformTypeColumn.setComments("资源所属于的平台类型:1:配置平台、2:运行平台、3:公用");
			platformTypeColumn.setColumnType(DataTypeConstants.INTEGER);
			platformTypeColumn.setLength(1);
			platformTypeColumn.setOrderCode(9908);
			table.getColumns().add(platformTypeColumn);

			CfgColumndata isCreatedResourceColumn = new CfgColumndata("is_created_resource");
			isCreatedResourceColumn.setName("是否已经创建资源");
			isCreatedResourceColumn.setComments("是否已经创建资源");
			isCreatedResourceColumn.setColumnType(DataTypeConstants.INTEGER);
			isCreatedResourceColumn.setLength(1);
			isCreatedResourceColumn.setOrderCode(9909);
			table.getColumns().add(isCreatedResourceColumn);
		}
		
		if(table.getIsDatalinkTable() == 0 && !ResourceNameConstants.COMMON_DATALINK_TABLENAME.equals(table.getTableName())){// 不是关系表，才要这些字段
			CfgColumndata createTimeColumn = new CfgColumndata("create_time");
			createTimeColumn.setColumnType(DataTypeConstants.DATE);
			createTimeColumn.setName("创建时间");
			createTimeColumn.setComments("创建时间");
			createTimeColumn.setOrderCode(9910);
			table.getColumns().add(createTimeColumn);
			
			CfgColumndata lastUpdateTimeColumn = new CfgColumndata("last_update_time");
			lastUpdateTimeColumn.setColumnType(DataTypeConstants.DATE);
			lastUpdateTimeColumn.setComments("最后修改时间");
			lastUpdateTimeColumn.setName("最后修改时间");
			lastUpdateTimeColumn.setOrderCode(9911);
			table.getColumns().add(lastUpdateTimeColumn);
			
			CfgColumndata createUserIdColumn = new CfgColumndata("create_user_id");
			createUserIdColumn.setColumnType(DataTypeConstants.STRING);
			createUserIdColumn.setComments("创建人主键");
			createUserIdColumn.setName("创建人主键");
			createUserIdColumn.setLength(32);
			createUserIdColumn.setOrderCode(9912);
			table.getColumns().add(createUserIdColumn);
			
			CfgColumndata lastUpdatedUserIdColumn = new CfgColumndata("last_updated_user_id");
			lastUpdatedUserIdColumn.setColumnType(DataTypeConstants.STRING);
			lastUpdatedUserIdColumn.setComments("最后修改人主键");
			lastUpdatedUserIdColumn.setName("最后修改人主键");
			lastUpdatedUserIdColumn.setLength(32);
			lastUpdatedUserIdColumn.setOrderCode(9913);
			table.getColumns().add(lastUpdatedUserIdColumn);
		}
	}
}
