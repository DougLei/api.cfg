package com.api.sys.entity.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.annotation.Table;
import com.api.constants.DataTypeConstants;
import com.api.sys.entity.BasicEntity;
import com.api.sys.entity.IEntity;
import com.api.sys.entity.IEntityPropAnalysis;
import com.api.util.StrUtils;

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
	/**
	 * 字段组值
	 * <p>实现根据列值的不同，从1开始重新生成序列值</p>
	 */
	private String columnGroupValue;
	
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
	public String getColumnGroupValue() {
		return columnGroupValue;
	}
	public void setColumnGroupValue(String columnGroupValue) {
		this.columnGroupValue = columnGroupValue;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(5+7);
		
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
		
		CfgColumn columnGroupValueColumn = new CfgColumn("column_group_value", DataTypeConstants.STRING, 80);
		columnGroupValueColumn.setName("字段组值");
		columnGroupValueColumn.setComments("实现根据列值的不同，从1开始重新生成序列值");
		columns.add(columnGroupValueColumn);
		
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
	
	public Object getCurrentVal(String parentSeqValue, String recSeqLinkSymbol) {
		if(StrUtils.isEmpty(parentSeqValue)){
			return currentVal;
		}else{
			return parentSeqValue + recSeqLinkSymbol + currentVal;
		}
	}
}
