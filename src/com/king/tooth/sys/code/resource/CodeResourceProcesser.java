package com.king.tooth.sys.code.resource;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.util.Log4jUtil;

/**
 * 代码资源的处理器
 * @author DougLei
 */
public class CodeResourceProcesser {

	/**
	 * 获取导入数据的代码资源的key值
	 * @param resourceName
	 * @return
	 */
	public static String getImportDataCodeResourceKey(String resourceName){
		return "/" + resourceName + CodeResourceMapping.IMPORT_DATA_KEY_SUFFIX;
	}
	
	// ---------------------------------------------------------------------------------------------------------------------
	/**
	 * 判断是否是代码资源类型
	 * @param codeResourceKey
	 * @return
	 */
	public static boolean isCodeResource(String codeResourceKey){
		return CodeResourceMapping.codeResourceMapping.containsKey(codeResourceKey);
	}
	
	/**
	 * 调用代码资源
	 * <p>在调用前，最好先调用isCodeResource()方法，判断是否是代码资源，结果为true再调用该方法，防止出错</p>
	 * @param codeResourceKey
	 * @param request
	 * @param ijson
	 * @param urlParams
	 * @return 
	 */
	public static Object invokeCodeResource(String codeResourceKey, HttpServletRequest request, IJson ijson){
		CodeResourceEntity codeResource = CodeResourceMapping.codeResourceMapping.get(codeResourceKey);
		if(codeResource == null){
			return "没有找到codeResourceKey值为["+codeResourceKey+"]的代码资源对象实例";
		}
		Log4jUtil.debug(" ========================> 此次请求调用的代码资源key为：{}", codeResourceKey);
		
		Object object = codeResource.invokeMethodForCodeResource(request, ijson);
		if(object == null){
			return "系统在调用codeResourceKey为["+codeResourceKey+"]的代码资源时，返回的结果为null，请联系后端系统开发人员";
		}
		return object;
	}
	
}
