package com.king.tooth.sys.entity.wf.hi;

import java.util.ArrayList;
import java.util.Date;
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
 * 流程活动实例历史表
 * <p>包括user task类型的任务数据，以及其他各种类型的任务数据，例如start event，end event，script task等</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfHiActivityinst extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的流程实例id
	 */
	private String refPropInstId;
	/**
	 * 关联的执行实例id
	 */
	private String refExecutionInstId;
	/**
	 * 关联的任务实例id
	 */
	private String refTaskInstId;
	/**
	 * 活动类型
	 * <p>startEvent、endEvent、userTask等</p>
	 */
	private String actType;
	/**
	 * 活动key
	 * <p>配置文件中的id值</p>
	 */
	private String actKey;
	/**
	 * 活动名称
	 * <p>配置文件中的name值</p>
	 */
	private String name;
	/**
	 * 开始办理时间
	 */
	private Date startDate;
	/**
	 * 结束办理时间
	 */
	private Date endDate;
	/**
	 * 办理持续的时间
	 * <p>单位为毫秒</p>
	 */
	private Integer durationTimes;
	
	//-------------------------------------------------------------------------
	
	public String getRefPropInstId() {
		return refPropInstId;
	}
	public void setRefPropInstId(String refPropInstId) {
		this.refPropInstId = refPropInstId;
	}
	public String getRefExecutionInstId() {
		return refExecutionInstId;
	}
	public void setRefExecutionInstId(String refExecutionInstId) {
		this.refExecutionInstId = refExecutionInstId;
	}
	public String getRefTaskInstId() {
		return refTaskInstId;
	}
	public void setRefTaskInstId(String refTaskInstId) {
		this.refTaskInstId = refTaskInstId;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getActKey() {
		return actKey;
	}
	public void setActKey(String actKey) {
		this.actKey = actKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getDurationTimes() {
		return durationTimes;
	}
	public void setDurationTimes(Integer durationTimes) {
		this.durationTimes = durationTimes;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn refPropInstIdColumn = new CfgColumn("ref_proc_inst_id", DataTypeConstants.STRING, 32);
		refPropInstIdColumn.setName("关联的流程实例id");
		refPropInstIdColumn.setComments("关联的流程实例id");
		columns.add(refPropInstIdColumn);
		
		CfgColumn refExecutionInstIdColumn = new CfgColumn("ref_execution_inst_id", DataTypeConstants.STRING, 32);
		refExecutionInstIdColumn.setName("关联的执行实例id");
		refExecutionInstIdColumn.setComments("关联的执行实例id");
		columns.add(refExecutionInstIdColumn);
		
		CfgColumn refTaskInstIdColumn = new CfgColumn("ref_task_inst_id", DataTypeConstants.STRING, 32);
		refTaskInstIdColumn.setName("关联的任务实例id");
		refTaskInstIdColumn.setComments("关联的任务实例id");
		columns.add(refTaskInstIdColumn);
		
		CfgColumn actTypeColumn = new CfgColumn("act_type", DataTypeConstants.STRING, 30);
		actTypeColumn.setName("活动类型");
		actTypeColumn.setComments("startEvent、endEvent、userTask等");
		columns.add(actTypeColumn);
		
		CfgColumn actKeyColumn = new CfgColumn("act_key", DataTypeConstants.STRING, 150);
		actKeyColumn.setName("活动key");
		actKeyColumn.setComments("配置文件中的id值");
		columns.add(actKeyColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 150);
		nameColumn.setName("活动名称");
		nameColumn.setComments("配置文件中的name值");
		columns.add(nameColumn);
		
		CfgColumn startDateColumn = new CfgColumn("start_date", DataTypeConstants.DATE, 0);
		startDateColumn.setName("开始办理时间");
		startDateColumn.setComments("开始办理时间");
		columns.add(startDateColumn);
		
		CfgColumn endDateColumn = new CfgColumn("end_date", DataTypeConstants.DATE, 0);
		endDateColumn.setName("结束办理时间");
		endDateColumn.setComments("结束办理时间");
		columns.add(endDateColumn);
		
		CfgColumn durationTimesColumn = new CfgColumn("duration_times", DataTypeConstants.INTEGER, 38);
		durationTimesColumn.setName("办理持续的时间");
		durationTimesColumn.setComments("单位为毫秒");
		columns.add(durationTimesColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程活动实例历史表");
		table.setRemark("包括user task类型的任务数据，以及其他各种类型的任务数据，例如start event，end event，script task等");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_HI_ACTIVITYINST";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfHiActivityinst";
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
