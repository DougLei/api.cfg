package com.api.util.build.model;

import java.util.List;

import com.api.sys.builtin.data.BuiltinObjectInstance;
import com.api.sys.entity.cfg.CfgTable;
import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;

/**
 * 动态的基础字段的工具类
 * @author DougLei
 */
public class DynamicBasicColumnUtil {
	
	/**
	 * 给动态表对象，添加基础的字段
	 * @param table
	 */
	public static void initBasicColumnToTable(CfgTable table){
		if(table.getIsBuildIn()==1){
			table.getColumns().add(BuiltinObjectInstance.idColumn32);
		}else{
			table.getColumns().add(BuiltinObjectInstance.idColumn50);
		}
		table.getColumns().add(BuiltinObjectInstance.customerIdColumn);
		table.getColumns().add(BuiltinObjectInstance.projectIdColumn);
		
		if(!table.getTableName().endsWith("_LINKS")){// 不是关系表，才要这些字段
			table.getColumns().add(BuiltinObjectInstance.createDateColumn);
			table.getColumns().add(BuiltinObjectInstance.lastUpdateDateColumn);
			table.getColumns().add(BuiltinObjectInstance.createUserIdColumn);
			table.getColumns().add(BuiltinObjectInstance.lastUpdateUserIdColumn);
		}
	}
	
	/**
	 * 初始化表资源基础的元数据信息
	 * @param isBuildIn
	 * @param resourceName
	 * @param resourceMetadataInfos
	 */
	public static void initBasicMetadataInfos(int isBuildIn, String resourceName, List<ResourceMetadataInfo> resourceMetadataInfos) {
		if(isBuildIn==1){
			resourceMetadataInfos.add(BuiltinObjectInstance.idColumn32.toTableResourceMetadataInfo());
		}else{
			resourceMetadataInfos.add(BuiltinObjectInstance.idColumn50.toTableResourceMetadataInfo());
		}
		resourceMetadataInfos.add(BuiltinObjectInstance.customerIdColumn.toTableResourceMetadataInfo());
		resourceMetadataInfos.add(BuiltinObjectInstance.projectIdColumn.toTableResourceMetadataInfo());
		if(!resourceName.endsWith("Links")){// 不是关系表，才要这些字段
			resourceMetadataInfos.add(BuiltinObjectInstance.createDateColumn.toTableResourceMetadataInfo());
			resourceMetadataInfos.add(BuiltinObjectInstance.lastUpdateDateColumn.toTableResourceMetadataInfo());
			resourceMetadataInfos.add(BuiltinObjectInstance.createUserIdColumn.toTableResourceMetadataInfo());
			resourceMetadataInfos.add(BuiltinObjectInstance.lastUpdateUserIdColumn.toTableResourceMetadataInfo());
		}
	}
}
