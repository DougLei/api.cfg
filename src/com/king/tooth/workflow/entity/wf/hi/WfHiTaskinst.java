package com.king.tooth.workflow.entity.wf.hi;

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
 * 流程任务实例历史表
 * <p>只记录user task类型的任务数据</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfHiTaskinst extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的流程实例id
	 */
	private String refPropInstId;
	/**
	 * 关联的执行实例id
	 */
	private String refExecutionInstId;
	/**
	 * 任务key
	 * <p>配置文件中的id值</p>
	 */
	private String tKey;
	/**
	 * 任务名称
	 * <p>配置文件中的name值</p>
	 */
	private String name;
	/**
	 * 最晚办理期限
	 * <p>办理的最后期限时间，在配置文件中配置办理的时限，在进入到该任务的时候，会自动计算，得出该值</p>
	 */
	private Date latestDeadline;
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
	/**
	 * 结束原因
	 * <p>如果任务结束，则会从WF_RU_TASKINST表中删除对应的数据，这里记录删除原因，比如完成，终止等</p>
	 */
	private String finishReason;
	/**
	 * 业务key
	 * <p>在配置文件中配置，关联实际业务的信息，例如业务的url，这个是当前任务的配置，会覆盖全局配置</p>
	 */
	private String businessKey;
	
	
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
	public String gettKey() {
		return tKey;
	}
	public void settKey(String tKey) {
		this.tKey = tKey;
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
	public String getFinishReason() {
		return finishReason;
	}
	public void setFinishReason(String finishReason) {
		this.finishReason = finishReason;
	}
	public Date getLatestDeadline() {
		return latestDeadline;
	}
	public void setLatestDeadline(Date latestDeadline) {
		this.latestDeadline = latestDeadline;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(10+7);
		
		CfgColumn refPropInstIdColumn = new CfgColumn("ref_proc_inst_id", DataTypeConstants.STRING, 32);
		refPropInstIdColumn.setName("关联的流程实例id");
		refPropInstIdColumn.setComments("关联的流程实例id");
		columns.add(refPropInstIdColumn);
		
		CfgColumn refExecutionInstIdColumn = new CfgColumn("ref_execution_inst_id", DataTypeConstants.STRING, 32);
		refExecutionInstIdColumn.setName("关联的执行实例id");
		refExecutionInstIdColumn.setComments("关联的执行实例id");
		columns.add(refExecutionInstIdColumn);
		
		CfgColumn tKeyColumn = new CfgColumn("t_key", DataTypeConstants.STRING, 150);
		tKeyColumn.setName("任务key");
		tKeyColumn.setComments("配置文件中的id值");
		columns.add(tKeyColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 150);
		nameColumn.setName("任务名称");
		nameColumn.setComments("配置文件中的name值");
		columns.add(nameColumn);
		
		CfgColumn latestDeadlineColumn = new CfgColumn("latest_deadline", DataTypeConstants.DATE, 0);
		latestDeadlineColumn.setName("最晚办理期限");
		latestDeadlineColumn.setComments("办理的最后期限时间，在配置文件中配置办理的时限，在进入到该任务的时候，会自动计算，得出该值");
		columns.add(latestDeadlineColumn);
		
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
		
		CfgColumn finishReasonColumn = new CfgColumn("finish_reason", DataTypeConstants.STRING, 200);
		finishReasonColumn.setName("结束原因");
		finishReasonColumn.setComments("如果任务结束，则会从WF_RU_EXECUTIONINST表中删除对应的数据，这里记录删除原因，比如完成，终止等");
		columns.add(finishReasonColumn);
		
		CfgColumn businessKeyColumn = new CfgColumn("business_key", DataTypeConstants.STRING, 500);
		businessKeyColumn.setName("业务key");
		businessKeyColumn.setComments("在配置文件中配置，关联实际业务的信息，例如业务的url，这个是当前任务的配置，会覆盖全局配置");
		columns.add(businessKeyColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程任务实例历史表");
		table.setRemark("只记录user task类型的任务数据");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_HI_TASKINST";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfHiTaskinst";
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
