package com.king.tooth.workflow.entity.wf.hi;

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
 * 流程处理意见表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfHiSuggestion extends WfBasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的任务实例id
	 */
	private String refTaskInstId;
	/**
	 * 提出处理意见的用户id
	 */
	private String userId;
	/**
	 * 意见的内容
	 */
	private String message;
	/**
	 * 态度
	 * <p>默认值为0，提出处理意见的用户态度：0.不表态、1.同意、2.不同意</p>
	 */
	private int attitude;
	
	//-------------------------------------------------------------------------
	
	
	public String getRefTaskInstId() {
		return refTaskInstId;
	}
	public void setRefTaskInstId(String refTaskInstId) {
		this.refTaskInstId = refTaskInstId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getAttitude() {
		return attitude;
	}
	public void setAttitude(int attitude) {
		this.attitude = attitude;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn refTaskInstIdColumn = new CfgColumn("ref_task_inst_id", DataTypeConstants.STRING, 32);
		refTaskInstIdColumn.setName("关联的任务实例id");
		refTaskInstIdColumn.setComments("关联的任务实例id");
		columns.add(refTaskInstIdColumn);
		
		CfgColumn userIdColumn = new CfgColumn("user_id", DataTypeConstants.STRING, 150);
		userIdColumn.setName("提出处理意见的用户id");
		userIdColumn.setComments("提出处理意见的用户id");
		columns.add(userIdColumn);
		
		CfgColumn messageColumn = new CfgColumn("message", DataTypeConstants.STRING, 1000);
		messageColumn.setName("意见的内容");
		messageColumn.setComments("意见的内容");
		columns.add(messageColumn);
		
		CfgColumn attitudeColumn = new CfgColumn("attitude", DataTypeConstants.INTEGER, 1);
		attitudeColumn.setName("态度");
		attitudeColumn.setComments("默认值为0，提出处理意见的用户态度：0.不表态、1.同意、2.不同意");
		attitudeColumn.setDefaultValue("0");
		columns.add(attitudeColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程处理意见表");
		table.setRemark("流程处理意见表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_HI_SUGGESTION";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfHiSuggestion";
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
