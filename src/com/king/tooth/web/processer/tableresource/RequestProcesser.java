package com.king.tooth.web.processer.tableresource;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.tableresource.BuiltinTableResourceBMProcesser;
import com.king.tooth.web.entity.resulttype.ResponseBody;
import com.king.tooth.web.processer.IRequestProcesser;

/**
 * 请求处理器
 * @author DougLei
 */
public abstract class RequestProcesser extends RequestProcesserCommon implements IRequestProcesser{

	/**
	 * 处理方法
	 * <p>RequestProcesser抽象类定义，由各个子类去实现</p>
	 * <p>最后各个子类通过调用super.setResponseBody(..)方法，给ResponseBody响应体属性赋予操作结果</p>
	 * @return 是否继续向下执行
	 */
	protected abstract boolean doProcess();
	
	/**
	 * 处理方法
	 * <p>对外的统一接口</p>
	 * @return ResponseEntity
	 */
	public final ResponseBody doRequestProcess(){
		Log4jUtil.debug("请求的内置url参数为：{}", requestBody.getRequestBuiltinParams());
		Log4jUtil.debug("请求的资源参数为：{}", requestBody.getRequestResourceParams());
		Log4jUtil.debug("请求的父资源参数为：{}", requestBody.getRequestParentResourceParams());
		Log4jUtil.debug("请求的请求体值为：{}", requestBody.getFormDataStr());
		
		// 初始化内置函数处理器的类实例
		builtinTableResourceBMProcesser = new BuiltinTableResourceBMProcesser(requestBody.getRequestBuiltinParams(), requestBody.getRequestResourceParams(), requestBody.getRequestParentResourceParams(), hqlParameterValues);
		
		doProcess();// 进行实际的业务处理，由子类实现
		releaseInvalidMemory();
		return responseBody;
	}
}