package com.king.tooth.sys.controller.other;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.controller.AbstractController;

/**
 * 系统工具类的Controller
 * @author DougLei
 */
public class SystemToolsController extends AbstractController{
	
	/**
	 * 监听hibernate类元数据
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object monitorHibernateClassMetadata(HttpServletRequest request, IJson ijson, Map<String, String> urlParams){
		String[] resourceNameArr = null;
		if(ijson != null && ijson.size() > 0){
			int length = ijson.size();
			resourceNameArr = new String[length];
			for(int i=0;i<length;i++){
				resourceNameArr[i] = ijson.get(i).getString("resourceName");
			}
		}
		resultObject = BuiltinObjectInstance.systemToolsService.monitorHibernateClassMetadata(resourceNameArr);
		return getResultObject();
	}
}
