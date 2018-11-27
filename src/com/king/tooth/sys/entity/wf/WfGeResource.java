package com.king.tooth.sys.entity.wf;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;

/**
 * 流程资源表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfGeResource extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的部署id
	 */
	private String refDeployId;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 内容
	 */
	private byte[] contents;
	
	//-------------------------------------------------------------------------
	public String getRefDeployId() {
		return refDeployId;
	}
	public void setRefDeployId(String refDeployId) {
		this.refDeployId = refDeployId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getContents() {
		return contents;
	}
	public void setContents(byte[] contents) {
		this.contents = contents;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(16);
		
		CfgColumn refDeployIdColumn = new CfgColumn("ref_deploy_id", DataTypeConstants.STRING, 32);
		refDeployIdColumn.setName("关联的部署id");
		refDeployIdColumn.setComments("关联的部署id");
		columns.add(refDeployIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 300);
		nameColumn.setName("名称");
		nameColumn.setComments("名称");
		columns.add(nameColumn);
		
		CfgColumn contentsColumn = new CfgColumn("contents", DataTypeConstants.BLOB, 0);
		contentsColumn.setName("内容");
		contentsColumn.setComments("内容");
		columns.add(contentsColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程资源表");
		table.setRemark("流程资源表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_GE_RESOURCE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfGeResource";
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
