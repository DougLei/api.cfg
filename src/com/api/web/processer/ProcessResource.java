package com.api.web.processer;

/**
 * 处理的资源对象
 * @author DougLei
 */
public class ProcessResource {
	/**
	 * 处理资源的方式
	 * <p>get、post、put、delete</p>
	 */
	private String processResourceMethod;
	/**
	 * 处理资源的类型
	 * 	1：表资源类型
	 * 	2：sql脚本资源类型
	 * 	3：代码资源类型
	 */
	private int[] processResourceTypes;
	
	
	public String getProcessResourceMethod() {
		return processResourceMethod;
	}
	public void setProcessResourceMethod(String processResourceMethod) {
		this.processResourceMethod = processResourceMethod;
	}
	public int[] getProcessResourceTypes() {
		return processResourceTypes;
	}
	public void setProcessResourceTypes(int[] processResourceTypes) {
		this.processResourceTypes = processResourceTypes;
	}
}
