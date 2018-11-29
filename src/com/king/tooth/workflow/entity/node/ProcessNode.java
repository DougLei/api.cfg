package com.king.tooth.workflow.entity.node;

/**
 * process节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProcessNode extends ProcDesginNode{
	public String getNodeName() {
		return "process";
	}
	
	/**
	 * 备注
	 */
	private String remark;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
