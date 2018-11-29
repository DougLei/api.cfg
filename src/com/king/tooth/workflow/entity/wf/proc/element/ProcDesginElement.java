package com.king.tooth.workflow.entity.wf.proc.element;

import java.io.Serializable;

/**
 * 流程设计元素的抽象父类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class ProcDesginElement implements Serializable{
	// id和key的值是完全一样的，只是表现形式不一样
	// id是在配置文件中的标识
	// key实在数据库字段中的标识
	
	/**
	 * 配置文件中的id
	 * <p>数据库字段中是xxx_key与之对应，即数据库字段用key来存储配置文件中的id属性的值</p>
	 */
	protected String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return id;
	}
}
