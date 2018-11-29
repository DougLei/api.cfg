package com.king.tooth.workflow.entity.node.task;

import com.king.tooth.workflow.entity.node.ProcessNode;

/**
 * 用户任务节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class UserTaskNode extends ProcessNode{
	public String getNodeName() {
		return "userTask";
	}
}
