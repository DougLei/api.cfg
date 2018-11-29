package com.king.tooth.workflow.entity.node;

import java.io.Serializable;

/**
 * 流程设计节点
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProcDesginNode implements Serializable, INode{
	public String getNodeName() {
		return "procDesign";
	}
	
	/**
	 * 配置文件中的id
	 * <p>数据库字段中是xxx_key与之对应，即数据库字段用key来存储配置文件中的id属性的值</p>
	 * <pre>
	 * 	id和key的值是完全一样的，只是表现形式不一样
	 * 	id是在配置文件中的标识
	 * 	key实在数据库字段中的标识
	 * </pre>
	 */
	protected String id;
	/**
	 * 节点名称
	 * <p>同id一样，和数据库中对应，值默认和id一致</p>
	 */
	protected String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
