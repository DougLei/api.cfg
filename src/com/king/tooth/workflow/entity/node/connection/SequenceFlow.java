package com.king.tooth.workflow.entity.node.connection;

import com.king.tooth.workflow.entity.node.ProcessNode;

/**
 * 顺序流节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class SequenceFlow extends ProcessNode{
	public String getNodeName() {
		return "sequenceFlow";
	}
	
	/**
	 * 进入顺序流的(活动)id
	 */
	private String sourceRefId;
	/**
	 * 离开顺序流的(活动)id
	 */
	private String targetRefId;
	
	public String getSourceRefId() {
		return sourceRefId;
	}
	public void setSourceRefId(String sourceRefId) {
		this.sourceRefId = sourceRefId;
	}
	public String getTargetRefId() {
		return targetRefId;
	}
	public void setTargetRefId(String targetRefId) {
		this.targetRefId = targetRefId;
	}
}
