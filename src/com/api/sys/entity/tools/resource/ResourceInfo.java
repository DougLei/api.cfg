package com.api.sys.entity.tools.resource;

import java.io.Serializable;
import java.util.Map;

import com.api.constants.ResourceInfoConstants;
import com.api.util.JsonUtil;

/**
 * 资源信息对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ResourceInfo implements Serializable{
	
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 资源类型
	 */
	private String resourceType;
	/**
	 * 请求的方法
	 */
	private String reqResourceMethod;
	/**
	 * 资源结构
	 */
	private Map<String, String> struct;
	/**
	 * 资源结构的json字符串
	 */
	private String structJsonStr;
	/**
	 * 消息
	 */
	private String msg;
	
	public void setResourceType(Integer resourceType) {
		if(resourceType == ResourceInfoConstants.TABLE){
			this.resourceType = "表资源";
		}else if(resourceType == ResourceInfoConstants.SQL){
			this.resourceType = "sql脚本资源";
		}else{
			this.resourceType = resourceType.toString();
		}
	}
	
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Map<String, String> getStruct() {
		return struct;
	}
	public void setStruct(Map<String, String> struct) {
		this.struct = struct;
		this.structJsonStr = JsonUtil.toJsonString(struct, true);
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStructJsonStr() {
		return structJsonStr;
	}
	public void setStructJsonStr(String structJsonStr) {
		this.structJsonStr = structJsonStr;
	}
	public String getReqResourceMethod() {
		return reqResourceMethod;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
}
