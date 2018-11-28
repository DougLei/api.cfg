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
 * 流程执行实例历史表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfHiExecutioninst extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的流程实例id
	 */
	private String refProcInstId;
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
	 * 开始活动的key值
	 * <p>配置内容中，活动的id值</p>
	 */
	private String startActKey;
	/**
	 * 结束活动的key值
	 * <p>配置内容中，活动的id值</p>
	 */
	private String endActKey;
	/**
	 * 结束原因
	 * <p>如果任务结束，则会从WF_RU_EXECUTIONINST表中删除对应的数据，这里记录删除原因，比如完成，终止等</p>
	 */
	private String finishReason;
	
	//-------------------------------------------------------------------------
	
	public String getRefProcInstId() {
		return refProcInstId;
	}
	public void setRefProcInstId(String refProcInstId) {
		this.refProcInstId = refProcInstId;
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
	public String getStartActKey() {
		return startActKey;
	}
	public void setStartActKey(String startActKey) {
		this.startActKey = startActKey;
	}
	public String getEndActKey() {
		return endActKey;
	}
	public void setEndActKey(String endActKey) {
		this.endActKey = endActKey;
	}
	public String getFinishReason() {
		return finishReason;
	}
	public void setFinishReason(String finishReason) {
		this.finishReason = finishReason;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(7+7);
		
		CfgColumn refProcInstIdColumn = new CfgColumn("ref_proc_inst_id", DataTypeConstants.STRING, 32);
		refProcInstIdColumn.setName("关联的流程实例id");
		refProcInstIdColumn.setComments("关联的流程实例id");
		columns.add(refProcInstIdColumn);
		
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
		
		CfgColumn startActKeyColumn = new CfgColumn("start_act_key", DataTypeConstants.STRING, 150);
		startActKeyColumn.setName("开始活动的key值");
		startActKeyColumn.setComments("配置内容中，活动的id值");
		columns.add(startActKeyColumn);
		
		CfgColumn endActKeyColumn = new CfgColumn("end_act_key", DataTypeConstants.STRING, 150);
		endActKeyColumn.setName("结束活动的key值");
		endActKeyColumn.setComments("配置内容中，活动的id值");
		columns.add(endActKeyColumn);
		
		CfgColumn finishReasonColumn = new CfgColumn("finish_reason", DataTypeConstants.STRING, 200);
		finishReasonColumn.setName("结束原因");
		finishReasonColumn.setComments("如果任务结束，则会从WF_RU_EXECUTIONINST表中删除对应的数据，这里记录删除原因，比如完成，终止等");
		columns.add(finishReasonColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程执行实例历史表");
		table.setRemark("流程执行实例历史表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_HI_EXECUTIONINST";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfHiExecutioninst";
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
