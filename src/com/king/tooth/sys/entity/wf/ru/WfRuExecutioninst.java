package com.king.tooth.sys.entity.wf.ru;

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
 * 流程运行时，执行实例表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfRuExecutioninst extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的流程定义id
	 */
	private String refProcDefId;
	/**
	 * 流程实例id
	 * <p>如果是单线流程，则该值和id值一样</p>
	 */
	private String procInstId;
	/**
	 * 是否活跃
	 * <p>默认值为1，表示当前执行对象，是否正常，是否在流程中可执行</p>
	 */
	private Integer isActive;
	/**
	 * 是否在并行
	 * <p>默认值为0，表示当前执行对象，是否有和其他执行对象在同时执行</p>
	 */
	private Integer isConcurrent;
	/**
	 * 业务key
	 * <p>在配置文件中配置，关联实际业务的信息，例如业务的url，这个是全局的配置</p>
	 */
	private String businessKey;
	
	
	//-------------------------------------------------------------------------
	
	public String getRefProcDefId() {
		return refProcDefId;
	}
	public void setRefProcDefId(String refProcDefId) {
		this.refProcDefId = refProcDefId;
	}
	public String getProcInstId() {
		return procInstId;
	}
	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public Integer getIsConcurrent() {
		return isConcurrent;
	}
	public void setIsConcurrent(Integer isConcurrent) {
		this.isConcurrent = isConcurrent;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
		CfgColumn refProcDefIdColumn = new CfgColumn("ref_proc_def_id", DataTypeConstants.STRING, 32);
		refProcDefIdColumn.setName("关联的流程定义id");
		refProcDefIdColumn.setComments("关联的流程定义id");
		columns.add(refProcDefIdColumn);
		
		CfgColumn procInstIdColumn = new CfgColumn("proc_inst_id", DataTypeConstants.STRING, 32);
		procInstIdColumn.setName("流程实例id");
		procInstIdColumn.setComments("如果是单线流程，则该值和id值一样");
		columns.add(procInstIdColumn);
		
		CfgColumn isActiveColumn = new CfgColumn("is_active", DataTypeConstants.INTEGER, 1);
		isActiveColumn.setName("是否活跃");
		isActiveColumn.setComments("默认值为1，表示当前执行对象，是否正常，是否在流程中可执行");
		isActiveColumn.setDefaultValue("1");
		columns.add(isActiveColumn);
		
		CfgColumn isConcurrentColumn = new CfgColumn("is_concurrent", DataTypeConstants.INTEGER, 1);
		isConcurrentColumn.setName("是否在并行");
		isConcurrentColumn.setComments("默认值为0，表示当前执行对象，是否有和其他执行对象在同时执行");
		isConcurrentColumn.setDefaultValue("0");
		columns.add(isConcurrentColumn);
		
		CfgColumn businessKeyColumn = new CfgColumn("business_key", DataTypeConstants.STRING, 500);
		businessKeyColumn.setName("业务key");
		businessKeyColumn.setComments("在配置文件中配置，关联实际业务的信息，例如业务的url，这个是全局的配置");
		columns.add(businessKeyColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程运行时，执行实例表");
		table.setRemark("流程运行时，执行实例表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_RU_EXECUTIONINST";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfRuExecutioninst";
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
