package com.king.tooth.workflow.entity.node.gateway;

import com.king.tooth.workflow.entity.node.ProcessNode;

/**
 * 排他网关节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ExclusiveGatewayNode extends ProcessNode{
	public String getNodeName() {
		return "exclusiveGateway";
	}
}
