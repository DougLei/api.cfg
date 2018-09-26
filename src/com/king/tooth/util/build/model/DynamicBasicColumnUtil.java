package com.king.tooth.util.build.model;

import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 动态的基础字段的工具类
 * @author DougLei
 */
public class DynamicBasicColumnUtil {
	
	/**
	 * 给动态表对象，添加基础的字段
	 * @param table
	 */
	public static void initBasicColumnToTable(ComTabledata table){
		// id
		ComColumndata idColumn = new ComColumndata(ResourcePropNameConstants.ID, BuiltinDataType.STRING, 32);
		idColumn.setIsPrimaryKey(1);
		idColumn.setIsNullabled(0);
		idColumn.setName("主键");
		idColumn.setComments("主键");
		table.getColumns().add(idColumn);
		
		// customerId
		ComColumndata customerIdColumn = new ComColumndata("customer_id", BuiltinDataType.STRING, 32);
		customerIdColumn.setName("所属租户主键");
		customerIdColumn.setComments("所属租户主键");
		table.getColumns().add(customerIdColumn);
		
		// projectId
		ComColumndata projectIdColumn = new ComColumndata("project_id", BuiltinDataType.STRING, 32);
		projectIdColumn.setName("关联的项目主键");
		projectIdColumn.setComments("关联的项目主键");
		table.getColumns().add(projectIdColumn);
		
		if(table.getIsResource() != null && table.getIsResource() == 1){
			ComColumndata isEnabledColumn = new ComColumndata("is_enabled", BuiltinDataType.INTEGER, 1);
			isEnabledColumn.setName("资源是否有效");
			isEnabledColumn.setComments("资源是否有效");
			isEnabledColumn.setDefaultValue("1");
			table.getColumns().add(isEnabledColumn);
			
			ComColumndata reqResourceMethodColumn = new ComColumndata("req_resource_method", BuiltinDataType.STRING, 20);
			reqResourceMethodColumn.setName("请求资源的方法");
			reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
			reqResourceMethodColumn.setDefaultValue(ISysResource.ALL);
			table.getColumns().add(reqResourceMethodColumn);

			ComColumndata isBuiltinColumn = new ComColumndata("is_builtin", BuiltinDataType.INTEGER, 1);
			isBuiltinColumn.setName("是否内置资源");
			isBuiltinColumn.setComments("是否内置资源:这个字段由后端开发人员控制，不开放给用户和前端开发");
			isBuiltinColumn.setDefaultValue("0");
			table.getColumns().add(isBuiltinColumn);

			ComColumndata isNeedDeployColumn = new ComColumndata("is_need_deploy", BuiltinDataType.INTEGER, 1);
			isNeedDeployColumn.setName("资源是否需要发布");
			isNeedDeployColumn.setComments("资源是否需要发布");
			isNeedDeployColumn.setDefaultValue("1");
			table.getColumns().add(isNeedDeployColumn);
			
			ComColumndata belongPlatformTypeColumn = new ComColumndata("belong_platform_type", BuiltinDataType.INTEGER, 1);
			belongPlatformTypeColumn.setName("资源所属的平台类型");
			belongPlatformTypeColumn.setComments("资源所属的平台类型:1：配置平台、2：运行平台、3：通用(这个类型由开发者控制)；后期开发的功能，如果是每个项目都需要的(基础功能)，则用这个字段控制是否要发布；和isBuiltin有类似的作用，开放给前端开发使用，但还是不开放给用户；isBuiltin控制的是系统内置的资源，belongPlatformType控制的是系统外置的资源");
			belongPlatformTypeColumn.setDefaultValue("2");
			table.getColumns().add(belongPlatformTypeColumn);
			
			ComColumndata isCreatedColumn = new ComColumndata("is_created", BuiltinDataType.INTEGER, 1);
			isCreatedColumn.setName("资源是否被创建");
			isCreatedColumn.setComments("资源是否被创建：在配置平台中，主要是给平台开发人员使用，也是标识表资源是否被加载到sessionFactory中；在运行平台中，这个字段标识资源是否被加载，主要是指表资源是否被加载到sessionFactory中；针对说明：数据库/项目，在配置平台为0，发布后，值改为1，取消发布后，值改回0，而像表资源这种多对多的关系，则无法通过一个字段去判断是否被发布，所以不用这个字段去判断是否被发布；即在系统启动的时候用来判断该资源是否需要加载");
			isCreatedColumn.setDefaultValue("0");
			table.getColumns().add(isCreatedColumn);
			
			ComColumndata refDataIdColumn = new ComColumndata("ref_data_id", BuiltinDataType.STRING, 32);
			refDataIdColumn.setName("引用的数据主键");
			refDataIdColumn.setComments("引用的数据主键：发布数据到运行平台时，将发布的数据id(在配置平台中的)保存到这个字段中，然后在运行平台重新创建一个新的id去保存数据");
			table.getColumns().add(refDataIdColumn);
		}
		
		if(table.getIsDatalinkTable() == 0 && !table.getTableName().endsWith("_LINKS")){// 不是关系表，才要这些字段
			ComColumndata createDateColumn = new ComColumndata("create_date", BuiltinDataType.DATE, 0);
			createDateColumn.setName("创建时间");
			createDateColumn.setComments("创建时间");
			table.getColumns().add(createDateColumn);
			
			ComColumndata lastUpdateDateColumn = new ComColumndata("last_update_date", BuiltinDataType.DATE, 0);
			lastUpdateDateColumn.setComments("最后修改时间");
			lastUpdateDateColumn.setName("最后修改时间");
			table.getColumns().add(lastUpdateDateColumn);
			
			ComColumndata createUserIdColumn = new ComColumndata("create_user_id", BuiltinDataType.STRING, 32);
			createUserIdColumn.setComments("创建人主键");
			createUserIdColumn.setName("创建人主键");
			table.getColumns().add(createUserIdColumn);
			
			ComColumndata lastUpdateUserIdColumn = new ComColumndata("last_update_user_id", BuiltinDataType.STRING, 32);
			lastUpdateUserIdColumn.setComments("最后修改人主键");
			lastUpdateUserIdColumn.setName("最后修改人主键");
			table.getColumns().add(lastUpdateUserIdColumn);
		}
	}
}