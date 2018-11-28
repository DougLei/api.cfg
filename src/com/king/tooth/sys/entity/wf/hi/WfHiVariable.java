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
 * 流程参数历史表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class WfHiVariable extends BasicEntity implements IEntity, IEntityPropAnalysis{

	/**
	 * 关联的流程实例id
	 */
	private String refProcInstId;
	/**
	 * 关联的执行实例id
	 */
	private String refExecutionInstId;
	/**
	 * 关联的活动实例id
	 */
	private String refActInstId;
	/**
	 * 参数名
	 */
	private String name;
	/**
	 * 参数的数据类型
	 */
	private String dataType;
	/**
	 * 字符值
	 */
	private String strVal;
	/**
	 * 整型值
	 */
	private Integer intVal;
	/**
	 * 浮点值
	 */
	private Double doubleVal;
	/**
	 * 二进制流值
	 */
	private byte[] byteArrayVal;
	
	//-------------------------------------------------------------------------
	
	public String getRefProcInstId() {
		return refProcInstId;
	}
	public void setRefProcInstId(String refProcInstId) {
		this.refProcInstId = refProcInstId;
	}
	public String getRefExecutionInstId() {
		return refExecutionInstId;
	}
	public void setRefExecutionInstId(String refExecutionInstId) {
		this.refExecutionInstId = refExecutionInstId;
	}
	public String getRefActInstId() {
		return refActInstId;
	}
	public void setRefActInstId(String refActInstId) {
		this.refActInstId = refActInstId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getStrVal() {
		return strVal;
	}
	public void setStrVal(String strVal) {
		this.strVal = strVal;
	}
	public Integer getIntVal() {
		return intVal;
	}
	public void setIntVal(Integer intVal) {
		this.intVal = intVal;
	}
	public Double getDoubleVal() {
		return doubleVal;
	}
	public void setDoubleVal(Double doubleVal) {
		this.doubleVal = doubleVal;
	}
	public byte[] getByteArrayVal() {
		return byteArrayVal;
	}
	public void setByteArrayVal(byte[] byteArrayVal) {
		this.byteArrayVal = byteArrayVal;
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(9+7);
		
		CfgColumn refProcInstIdColumn = new CfgColumn("ref_proc_inst_id", DataTypeConstants.STRING, 32);
		refProcInstIdColumn.setName("关联的流程实例id");
		refProcInstIdColumn.setComments("关联的流程实例id");
		columns.add(refProcInstIdColumn);
		
		CfgColumn refExecutionInstIdColumn = new CfgColumn("ref_execution_inst_id", DataTypeConstants.STRING, 32);
		refExecutionInstIdColumn.setName("关联的执行实例id");
		refExecutionInstIdColumn.setComments("关联的执行实例id");
		columns.add(refExecutionInstIdColumn);
		
		CfgColumn refActInstIdColumn = new CfgColumn("ref_act_inst_id", DataTypeConstants.STRING, 32);
		refActInstIdColumn.setName("关联的活动实例id");
		refActInstIdColumn.setComments("关联的活动实例id");
		columns.add(refActInstIdColumn);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 200);
		nameColumn.setName("参数名");
		nameColumn.setComments("参数名");
		columns.add(nameColumn);
		
		CfgColumn dataTypeColumn = new CfgColumn("data_type", DataTypeConstants.STRING, 20);
		dataTypeColumn.setName("参数的数据类型");
		dataTypeColumn.setComments("参数的数据类型");
		columns.add(dataTypeColumn);
		
		CfgColumn strValColumn = new CfgColumn("str_val", DataTypeConstants.STRING, 2000);
		strValColumn.setName("字符值");
		strValColumn.setComments("字符值");
		columns.add(strValColumn);
		
		CfgColumn intValColumn = new CfgColumn("int_val", DataTypeConstants.INTEGER, 20);
		intValColumn.setName("整型值");
		intValColumn.setComments("整型值");
		columns.add(intValColumn);
		
		CfgColumn doubleValColumn = new CfgColumn("double_val", DataTypeConstants.DOUBLE, 38);
		doubleValColumn.setName("浮点值");
		doubleValColumn.setComments("浮点值");
		doubleValColumn.setPrecision(10);
		columns.add(doubleValColumn);
		
		CfgColumn byteArrayValColumn = new CfgColumn("byte_array_val", DataTypeConstants.BLOB, 0);
		byteArrayValColumn.setName("二进制流值");
		byteArrayValColumn.setComments("二进制流值");
		columns.add(byteArrayValColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("流程参数历史表");
		table.setRemark("流程参数历史表");
		table.setRequestMethod(ResourceInfoConstants.GET);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "WF_HI_VARIABLE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "WfHiVariable";
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
