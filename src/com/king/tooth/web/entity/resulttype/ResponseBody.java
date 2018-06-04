package com.king.tooth.web.entity.resulttype;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;

/**
 * 响应体，返回给客户端对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ResponseBody implements Serializable{
	
	/**
	 * 响应的状态码，默认200
	 * <p>构造函数没有提供该参数，如果需要修改，请使用set方法修改</p>
	 */
	@JSONField(name = "status")
	private int status = 200;
	
	/**
	 * 响应的描述信息
	 */
	@JSONField(name = "message")
	private String message;
	
	/**
	 * 要响应的具体对象内容
	 */
	@JSONField(name = "data")
	private Object data;
	
	public ResponseBody() {
	}
	public ResponseBody(String message, Object data) {
		this.message = message;
		this.data = data;
	}
	public ResponseBody(Object data) {
		this.data = data;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 根据不同的响应类型，进行处理，默认是处理成json串
	 */
	public String toStrings(){
		String str = null;
		if("true".equals(SysConfig.getSystemConfig("output.result.format"))){
			str = JsonUtil.toJsonString(this, true);
		}else{
			str = JsonUtil.toJsonString(this, false);
		}
		Log4jUtil.debug("[ResponseBody响应给客户端的结果为]：\n" + str);
		return str;
	}
}
