package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.StrUtils;

/**
 * 业务资源模型表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgBusiResModel extends ACfgResource implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 资源模型的描述
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 业务资源模型包含的所有关系集合
	 */
	@JSONField(serialize = false)
	private List<CfgBusiResModelRelations> busiResModelRelationsList;
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<CfgBusiResModelRelations> getBusiResModelRelationsList() {
		return busiResModelRelationsList;
	}
	public void setBusiResModelRelationsList(List<CfgBusiResModelRelations> busiResModelRelationsList) {
		this.busiResModelRelationsList = busiResModelRelationsList;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		columns.add(BuiltinObjectInstance.resourceNameColumn);
		
		CfgColumn commentsColumn = new CfgColumn("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("业务资源模型的描述");
		commentsColumn.setComments("业务资源模型的描述");
		columns.add(commentsColumn);
		
		columns.add(BuiltinObjectInstance.isCreatedColumn);
		columns.add(BuiltinObjectInstance.isEnabledColumn);
		columns.add(BuiltinObjectInstance.requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("业务资源模型表");
		table.setComments("业务资源模型表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_BUSI_RES_MODEL";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgBusiResModel";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(resourceName)){
			return "业务资源模型的资源名不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	public CfgResource turnToResource() {
		CfgResource resource = super.turnToResource();
		resource.setResourceType(ResourceInfoConstants.BUSINESS_MODEL);
		return resource;
	}
	
	public void clear() {
		if(busiResModelRelationsList != null && busiResModelRelationsList.size() > 0){
			busiResModelRelationsList.clear();
		}
	}
}
