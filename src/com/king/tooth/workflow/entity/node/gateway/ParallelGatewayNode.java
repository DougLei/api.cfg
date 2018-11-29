package com.king.tooth.workflow.entity.node.gateway;

import com.king.tooth.workflow.entity.node.ProcessNode;

/**
 * 并行网关节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ParallelGatewayNode extends ProcessNode{
	public String getNodeName() {
		return "parallelGateway";
	}
}
