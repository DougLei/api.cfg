package com.king.tooth.workflow.entity.wf.re;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.workflow.entity.WfBasicEntity;

/**
 * 流程部署表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfReDeployment extends WfBasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 部署名称
	 */
	private String name;
	/**
	 * 部署时间
	 */
	private String deployDate;
	/**
	 * 备注
	 */
	private String remark;
	
	//-------------------------------------------------------------------------
	
	public String getDeployDate() {
		return deployDate;
	}
	public void setDeployDate(String deployDate) {
		this.deployDate = deployDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(3+7);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 200);
		nameColumn.setName("名称");
		nameColumn.setComments("名称");
		columns.add(nameColumn);
		
		CfgColumn deployDateColumn = new CfgColumn("deploy_date", DataTypeConstants.DATE, 0);
		deployDateColumn.setName("部署时间");
		deployDateColumn.setComments("部署时间");
		columns.add(deployDateColumn);
		
		CfgColumn remarkColumn = new CfgColumn("remark", DataTypeConstants.STRING, 200);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程部署表");
		table.setRemark("流程部署表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_RE_DEPLOYMENT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfReDeployment";
	}
	
	public String validNotNullProps() {
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
		}
		return result;
	}
}
