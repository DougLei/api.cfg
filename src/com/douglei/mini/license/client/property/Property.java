package com.douglei.mini.license.client.property;

/**
 * 
 * @author DougLei
 */
abstract class Property {
	protected String name;
	protected String value;
	
	protected Property(String name, String value){
		this.name = name;
		this.value = value;
	}
	
	/**
	 * 获取属性的配置内容: name=value
	 * @return
	 */
	public String getContent() {
		return name + '=' + value;
	}
}
