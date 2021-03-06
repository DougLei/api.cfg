package com.api.web.entity.resulttype;

import java.io.Serializable;

import com.api.cache.SysContext;
import com.api.util.JsonUtil;
import com.api.util.Log4jUtil;
import com.api.util.StrUtils;

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
	private int status = 200;
	
	/**
	 * 响应的描述信息
	 */
	private String message;
	
	/**
	 * 要响应的具体对象内容
	 */
	private Object data;
	
	/**
	 * 是否成功
	 */
	private boolean isSuccess;
	
	public ResponseBody() {
	}
	public ResponseBody(String message, Object data) {
		this.message = message;
		this.data = data;
		if(StrUtils.isEmpty(message)){
			this.isSuccess = true;
		}
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
	public boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	/**
	 * 根据不同的响应类型，进行处理，默认是处理成json串
	 */
	public String toStrings(){
		String str = null;
		if("true".equals(SysContext.getSystemConfig("output.result.format"))){
			str = JsonUtil.toJsonString(this, true);
		}else{
			str = JsonUtil.toJsonString(this, false);
		}
		Log4jUtil.debug("[ResponseBody响应给客户端的结果为]：\n" + str);
		return str;
	}
}
