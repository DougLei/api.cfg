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
 * 业务模型表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgBusiModel extends ACfgResource implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 资源模型的描述
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	/**
	 * 业务模型包含的所有关系集合
	 */
	@JSONField(serialize = false)
	private List<CfgBusiModelResRelations> busiModelResRelationsList;
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<CfgBusiModelResRelations> getBusiModelResRelationsList() {
		return busiModelResRelationsList;
	}
	public void setBusiModelResRelationsList(List<CfgBusiModelResRelations> busiModelResRelationsList) {
		this.busiModelResRelationsList = busiModelResRelationsList;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		columns.add(BuiltinObjectInstance.resourceNameColumn);
		
		CfgColumn commentsColumn = new CfgColumn("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("描述");
		commentsColumn.setComments("描述");
		columns.add(commentsColumn);
		
		columns.add(BuiltinObjectInstance.isCreatedColumn);
		columns.add(BuiltinObjectInstance.isEnabledColumn);
		columns.add(BuiltinObjectInstance.requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("业务模型表");
		table.setComments("业务模型表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_BUSI_MODEL";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgBusiModel";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(resourceName)){
			return "业务模型的资源名不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			requestMethod = ResourceInfoConstants.POST;
		}
		return result;
	}
	
	public CfgResource turnToResource() {
		CfgResource resource = super.turnToResource();
		resource.setResourceType(ResourceInfoConstants.BUSINESS_MODEL);
		return resource;
	}
	
	public void clear() {
		if(busiModelResRelationsList != null && busiModelResRelationsList.size() > 0){
			busiModelResRelationsList.clear();
		}
	}
}