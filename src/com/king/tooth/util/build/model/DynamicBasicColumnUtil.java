package com.king.tooth.util.build.model;

import java.util.List;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.other.ResourceMetadataInfo;

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
		ComColumndata idColumn = new ComColumndata(ResourcePropNameConstants.ID, DataTypeConstants.STRING, 32);
		idColumn.setIsPrimaryKey(1);
		idColumn.setIsNullabled(0);
		idColumn.setName("主键");
		idColumn.setComments("主键");
		table.getColumns().add(idColumn);
		
		ComColumndata customerIdColumn = new ComColumndata("customer_id", DataTypeConstants.STRING, 32);
		customerIdColumn.setName("所属租户主键");
		customerIdColumn.setComments("所属租户主键");
		table.getColumns().add(customerIdColumn);
		
		ComColumndata projectIdColumn = new ComColumndata("project_id", DataTypeConstants.STRING, 32);
		projectIdColumn.setName("所属项目主键");
		projectIdColumn.setComments("所属项目主键");
		table.getColumns().add(projectIdColumn);
		
		if(!table.getTableName().endsWith("_LINKS")){// 不是关系表，才要这些字段
			ComColumndata createDateColumn = new ComColumndata("create_date", DataTypeConstants.DATE, 0);
			createDateColumn.setName("创建时间");
			createDateColumn.setComments("创建时间");
			table.getColumns().add(createDateColumn);
			
			ComColumndata lastUpdateDateColumn = new ComColumndata("last_update_date", DataTypeConstants.DATE, 0);
			lastUpdateDateColumn.setComments("最后修改时间");
			lastUpdateDateColumn.setName("最后修改时间");
			table.getColumns().add(lastUpdateDateColumn);
			
			ComColumndata createUserIdColumn = new ComColumndata("create_user_id", DataTypeConstants.STRING, 32);
			createUserIdColumn.setComments("创建人主键");
			createUserIdColumn.setName("创建人主键");
			table.getColumns().add(createUserIdColumn);
			
			ComColumndata lastUpdateUserIdColumn = new ComColumndata("last_update_user_id", DataTypeConstants.STRING, 32);
			lastUpdateUserIdColumn.setComments("最后修改人主键");
			lastUpdateUserIdColumn.setName("最后修改人主键");
			table.getColumns().add(lastUpdateUserIdColumn);
		}
	}
	
	/**
	 * 初始化表资源基础的元数据信息
	 * @param resourceName
	 * @param resourceMetadataInfos
	 */
	public static void initBasicMetadataInfos(String resourceName, List<ResourceMetadataInfo> resourceMetadataInfos) {
		resourceMetadataInfos.add(new ResourceMetadataInfo("ID", ResourcePropNameConstants.ID, DataTypeConstants.STRING, 32, 0, 0, 1, "主键"));
		resourceMetadataInfos.add(new ResourceMetadataInfo("CUSTOMER_ID", "customerId", DataTypeConstants.STRING, 32, 0, 0, 1, "所属租户主键"));
		resourceMetadataInfos.add(new ResourceMetadataInfo("PROJECT_ID", "projectId", DataTypeConstants.STRING, 32, 0, 0, 1, "所属项目主键"));
		if(!resourceName.endsWith("Links")){// 不是关系表，才要这些字段
			resourceMetadataInfos.add(new ResourceMetadataInfo("CREATE_DATE", "createDate", DataTypeConstants.DATE, 0, 0, 0, 1, "创建时间"));
			resourceMetadataInfos.add(new ResourceMetadataInfo("LAST_UPDATE_DATE", "lastUpdateDate", DataTypeConstants.DATE, 0, 0, 0, 1, "最后修改时间"));
			resourceMetadataInfos.add(new ResourceMetadataInfo("CREATE_USER_ID", "createUserId", DataTypeConstants.STRING, 32, 0, 0, 1, "创建人主键"));
			resourceMetadataInfos.add(new ResourceMetadataInfo("LAST_UPDATE_USER_ID", "lastUpdateUserId", DataTypeConstants.STRING, 32, 0, 0, 1, "最后修改人主键"));
		}
	}
}
