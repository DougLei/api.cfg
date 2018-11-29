package com.king.tooth.workflow.entity.node.event;

import com.king.tooth.workflow.entity.node.ProcessNode;

/**
 * 开始事件节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class StartEventNode extends ProcessNode{
	public String getNodeName() {
		return "startEvent";
	}
}
