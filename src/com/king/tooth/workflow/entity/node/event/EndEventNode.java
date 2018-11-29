package com.king.tooth.workflow.entity.node.event;

import com.king.tooth.workflow.entity.node.ProcessNode;

/**
 * 结束事件节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class EndEventNode extends ProcessNode{
	public String getNodeName() {
		return "endEvent";
	}
}
