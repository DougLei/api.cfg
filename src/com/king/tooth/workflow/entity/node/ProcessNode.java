package com.king.tooth.workflow.entity.node;

import java.util.List;

import com.king.tooth.workflow.entity.node.connection.SequenceFlow;

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
	 * 进入的顺序流对象集合
	 */
	protected List<SequenceFlow> inSequenceFlows;
	/**
	 * 出去的顺序流对象集合
	 */
	protected List<SequenceFlow> outSequenceFlows;
	
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
	public List<SequenceFlow> getInSequenceFlows() {
		return inSequenceFlows;
	}
	public void setInSequenceFlows(List<SequenceFlow> inSequenceFlows) {
		this.inSequenceFlows = inSequenceFlows;
	}
	public List<SequenceFlow> getOutSequenceFlows() {
		return outSequenceFlows;
	}
	public void setOutSequenceFlows(List<SequenceFlow> outSequenceFlows) {
		this.outSequenceFlows = outSequenceFlows;
	}
}
