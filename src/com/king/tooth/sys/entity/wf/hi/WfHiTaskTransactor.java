package com.king.tooth.sys.entity.wf.hi;

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
 * 流程任务处理人历史表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfHiTaskTransactor extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的任务实例id
	 */
	private String refTaskInstId;
	/**
	 * 处理人类型
	 * <p>0:发起者、1:参与者、2:候选者、3:办理人</p>
	 */
	private Integer tType;
	/**
	 * 处理人id
	 */
	private String tId;
	/**
	 * 排序值
	 */
	private Integer orderCode;
	
	//-------------------------------------------------------------------------
	
	public String getRefTaskInstId() {
		return refTaskInstId;
	}
	public void setRefTaskInstId(String refTaskInstId) {
		this.refTaskInstId = refTaskInstId;
	}
	public Integer gettType() {
		return tType;
	}
	public void settType(Integer tType) {
		this.tType = tType;
	}
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn refTaskInstIdColumn = new CfgColumn("ref_task_inst_id", DataTypeConstants.STRING, 32);
		refTaskInstIdColumn.setName("关联的任务实例id");
		refTaskInstIdColumn.setComments("关联的任务实例id");
		columns.add(refTaskInstIdColumn);
		
		CfgColumn tTypeColumn = new CfgColumn("t_type", DataTypeConstants.INTEGER, 1);
		tTypeColumn.setName("处理人类型");
		tTypeColumn.setComments("0:发起者、1:参与者、2:候选者、3:办理人");
		columns.add(tTypeColumn);
		
		CfgColumn tIdColumn = new CfgColumn("t_id", DataTypeConstants.STRING, 150);
		tIdColumn.setName("处理人id");
		tIdColumn.setComments("处理人id");
		columns.add(tIdColumn);
		
		CfgColumn orderCodeColumn = new CfgColumn("order_code", DataTypeConstants.INTEGER, 4);
		orderCodeColumn.setName("排序");
		orderCodeColumn.setComments("排序");
		columns.add(orderCodeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程任务处理人历史表");
		table.setRemark("流程任务处理人历史表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_HI_TASK_TRANSACTOR";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfHiTaskTransactor";
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
