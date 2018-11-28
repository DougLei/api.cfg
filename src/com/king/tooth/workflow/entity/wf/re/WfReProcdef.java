package com.king.tooth.workflow.entity.wf.re;

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
 * 流程定义表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfReProcdef extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的部署id
	 */
	private String refDeployId;
	/**
	 * 流程key
	 * <p>配置文件中的id值</p>
	 */
	private String pKey;
	/**
	 * 流程名称
	 * <p>配置文件中的name值</p>
	 */
	private String name;
	/**
	 * 流程版本
	 */
	private int version;
	/**
	 * 是否激活
	 * <p>默认值为1，如果没有激活，则无法启动该流程</p>
	 */
	private Integer isActivate;
	/**
	 * 备注
	 */
	private String remark;
	
	//-------------------------------------------------------------------------
	
	public String getRefDeployId() {
		return refDeployId;
	}
	public void setRefDeployId(String refDeployId) {
		this.refDeployId = refDeployId;
	}
	public String getpKey() {
		return pKey;
	}
	public void setpKey(String pKey) {
		this.pKey = pKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Integer getIsActivate() {
		return isActivate;
	}
	public void setIsActivate(Integer isActivate) {
		this.isActivate = isActivate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(6+7);
		
		CfgColumn refDeployIdColumn = new CfgColumn("ref_deploy_id", DataTypeConstants.STRING, 32);
		refDeployIdColumn.setName("关联的部署id");
		refDeployIdColumn.setComments("关联的部署id");
		columns.add(refDeployIdColumn);
		
		CfgColumn pKeyColumn = new CfgColumn("p_key", DataTypeConstants.STRING, 150);
		pKeyColumn.setName("流程key");
		pKeyColumn.setComments("配置文件中的id值");
		columns.add(pKeyColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 200);
		nameColumn.setName("流程名称");
		nameColumn.setComments("配置文件中的name值");
		columns.add(nameColumn);

		CfgColumn versionColumn = new CfgColumn("version", DataTypeConstants.INTEGER, 4);
		versionColumn.setName("流程版本");
		versionColumn.setComments("流程版本");
		columns.add(versionColumn);
		
		CfgColumn isActivateColumn = new CfgColumn("is_activate", DataTypeConstants.INTEGER, 1);
		isActivateColumn.setName("是否激活");
		isActivateColumn.setComments("默认值为1，如果没有激活，则无法启动该流程");
		isActivateColumn.setDefaultValue("1");
		columns.add(isActivateColumn);
		
		CfgColumn remarkColumn = new CfgColumn("remark", DataTypeConstants.STRING, 200);
		remarkColumn.setName("备注");
		remarkColumn.setComments("备注");
		columns.add(remarkColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程定义表");
		table.setRemark("流程定义表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_RE_PROCDEF";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfReProcdef";
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
