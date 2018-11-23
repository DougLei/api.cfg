package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;

/**
 * 序列信息表表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgSeqInfo extends BasicEntity implements IEntity, IEntityPropAnalysis{
	
	/**
	 * 关联的属性编码规则明细id
	 */
	private String refPropCodeRuleDetailId;
	/**
	 * 序列初始化的时间
	 */
	private Date initDate;
	/**
	 * 序列的当前值
	 */
	private Integer currentVal;
	/**
	 * 父序列值
	 * <p>实现递归序列，该字段存值例如：1.1，那么该序列的值就为1.1.1、1.1.2等</p>
	 */
	private String parentSeqVal;
	
	//-------------------------------------------------------------------------

	public String getRefPropCodeRuleDetailId() {
		return refPropCodeRuleDetailId;
	}
	public void setRefPropCodeRuleDetailId(String refPropCodeRuleDetailId) {
		this.refPropCodeRuleDetailId = refPropCodeRuleDetailId;
	}
	public Date getInitDate() {
		return initDate;
	}
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}
	public Integer getCurrentVal() {
		return currentVal;
	}
	public void setCurrentVal(Integer currentVal) {
		this.currentVal = currentVal;
	}
	public String getParentSeqVal() {
		return parentSeqVal;
	}
	public void setParentSeqVal(String parentSeqVal) {
		this.parentSeqVal = parentSeqVal;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(4+7);
		
		CfgColumn refPropCodeRuleDetailIdColumn = new CfgColumn("ref_prop_code_rule_detail_id", DataTypeConstants.STRING, 32);
		refPropCodeRuleDetailIdColumn.setName("关联的属性编码规则明细id");
		refPropCodeRuleDetailIdColumn.setComments("关联的属性编码规则明细id");
		columns.add(refPropCodeRuleDetailIdColumn);
		
		CfgColumn initDateColumn = new CfgColumn("init_date", DataTypeConstants.DATE, 0);
		initDateColumn.setName("序列初始化的时间");
		initDateColumn.setComments("序列初始化的时间");
		columns.add(initDateColumn);
		
		CfgColumn currentValColumn = new CfgColumn("current_val", DataTypeConstants.INTEGER, 10);
		currentValColumn.setName("序列的当前值");
		currentValColumn.setComments("序列的当前值");
		columns.add(currentValColumn);
		
		CfgColumn parentSeqValColumn = new CfgColumn("parent_seq_val", DataTypeConstants.STRING, 20);
		parentSeqValColumn.setName("父序列值");
		parentSeqValColumn.setComments("实现递归序列，该字段存值例如：1.1，那么该序列的值就为1.1.1、1.1.2等");
		columns.add(parentSeqValColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("序列信息表");
		table.setRemark("序列信息表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_SEQ_INFO";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgSeqInfo";
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
