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
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.StrUtils;

/**
 * 资源模型表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgResourceModel extends ASysResource implements IEntityPropAnalysis, IEntity{
	
	/**
	 * 资源模型的描述
	 */
	private String comments;
	
	//-------------------------------------------------------------------------
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		columns.add(BuiltinObjectInstance.resourceNameColumn);
		
		CfgColumn commentsColumn = new CfgColumn("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("资源模型的描述");
		commentsColumn.setComments("资源模型的描述");
		columns.add(commentsColumn);
		
		columns.add(BuiltinObjectInstance.isCreatedColumn);
		columns.add(BuiltinObjectInstance.isEnabledColumn);
		columns.add(BuiltinObjectInstance.requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("资源模型表");
		table.setComments("资源模型表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_RESOURCE_MODEL";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgResourceModel";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(resourceName)){
			return "模型的资源名不能为空！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
	
	public SysResource turnToResource() {
		SysResource resource = super.turnToResource();
		resource.setResourceType(ResourceInfoConstants.BUSINESS_MODEL);
		return resource;
	}
}
