package com.king.tooth.sys.controller;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.ProcessStringTypeJsonExtend;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.ResponseBody;

/**
 * 控制器的抽象父类
 * @author DougLei
 */
public abstract class AbstractController {
	
	/**
	 * 根据json串，获取对应数据类型实例集合
	 * @param json
	 * @param targetClass
	 * @return
	 */
	protected <T> List<T> getDataInstanceList(String json, Class<T> targetClass){
		if(StrUtils.isEmpty(json)){
			throw new NullPointerException("操作的数据不能为空");
		}
		IJson ijson = ProcessStringTypeJsonExtend.getIJson(json);
		List<T> list = new ArrayList<T>(ijson.size());
		for(int i=0;i<ijson.size();i++){
			list.add(JsonUtil.parseObject(ijson.get(i).toJSONString(), targetClass));
		}
		ijson.clear();
		return list;
	}
	
	/**
	 * 统一解析数据资源属性
	 * @param list
	 * @return
	 */
	protected String analysisResourceProp(List<? extends IEntityPropAnalysis> list){
		String result = null;
		for (IEntityPropAnalysis iEntityPropAnalysis : list) {
			result = iEntityPropAnalysis.analysisResourceProp();
			if(result != null){
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 组装操作responseBody对象，主要用于返回操作结果内容
	 * @param operResult 操作结果内容
	 * @param definedMessage 自定义的消息内容
	 * @return
	 */
	protected ResponseBody installOperResponseBody(String operResult, String definedMessage){
		ResponseBody responseBody = new ResponseBody();
		if(operResult == null){
//			if(definedMessage == null){
//				responseBody.setMessage("操作成功");
//			}else{
				responseBody.setMessage(definedMessage);
//			}
		}else{
			responseBody.setMessage(operResult);
		}
		return responseBody;
	}
	
	/**
	 * 组装responseBody对象，返回结果内容和对象
	 * @param message 内容
	 * @param data 对象
	 * @return
	 */
	protected ResponseBody installResponseBody(String message, Object data){
		ResponseBody responseBody = new ResponseBody(message, data);
		return responseBody;
	}
}
