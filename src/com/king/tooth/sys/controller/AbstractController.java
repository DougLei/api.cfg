package com.king.tooth.sys.controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.JsonUtil;

/**
 * 控制器的抽象父类
 * @author DougLei
 */
public abstract class AbstractController {
	
	/**
	 * 解析的结果
	 * <p>包括解析的结果</p>
	 */
	protected String analysisResult;
	
	/**
	 * 处理结果对象
	 * <p>
	 * 	要么是string类型，记录操作失败的原因
	 *  要么是jsonObject类型，记录操作结果
	 *  要么是jsonArray类型
	 *  只能是这三种类型中的任意一种
	 * </p>
	 */
	protected Object resultObject;
	
	/**
	 * 结果jsonArray对象
	 */
	protected JSONArray resultJsonArray;
	
	private void vaildIJsonNotNull(IJson ijson){
		if(ijson == null || ijson.size() == 0){
			throw new NullPointerException("操作的数据不能为空");
		}
	}
	
	/**
	 * 根据json串，获取对应数据类型实例集合
	 * @param ijson
	 * @param targetClass
	 * @return
	 */
	protected <T> List<T> getDataInstanceList(IJson ijson, Class<T> targetClass){
		vaildIJsonNotNull(ijson);
		if(ijson.isArray()){
			resultJsonArray = new JSONArray(ijson.size());
		}
		
		List<T> list = new ArrayList<T>(ijson.size());
		for(int i=0;i<ijson.size();i++){
			list.add(JsonUtil.parseObject(ijson.get(i).toJSONString(), targetClass));
		}
		ijson.clear();
		return list;
	}
	
	/**
	 * 根据json串，获取ijson对象昂
	 * @param ijson
	 * @return
	 */
	protected IJson getIJson(IJson ijson){
		vaildIJsonNotNull(ijson);
		return ijson;
	}
	
	/**
	 * 根据json串，获取JSONObject对象
	 * @param ijson
	 * @return
	 */
	protected JSONObject getJSONObject(IJson ijson){
		vaildIJsonNotNull(ijson);
		return ijson.get(0);
	}
	
	/**
	 * 统一解析数据资源属性
	 * @param list
	 */
	protected void analysisResourceProp(List<? extends IEntityPropAnalysis> list){
		for (IEntityPropAnalysis iEntityPropAnalysis : list) {
			analysisResult = iEntityPropAnalysis.analysisResourceProp();
			if(analysisResult != null){
				break;
			}
		}
	}
	
	/**
	 * 获得最后的结果对象
	 * @return
	 */
	protected Object getResultObject(){
		if(analysisResult != null){
			return analysisResult;
		}
		if(resultObject == null){
			return "系统异常，操作结果记录对象[resultObject]为空，请联系开发人员";
		}
//		if(resultObject instanceof String){
//			return resultObject.toString();
//		}
		
		if(resultJsonArray != null && resultJsonArray.size() > 0){
			return resultJsonArray;
		}
//		if(resultObject instanceof JSONArray){
//			return (JSONArray) resultObject;
//		}
//		return (JSONObject) resultObject;
		return resultObject;
	}
	
	/**
	 * 处理ResultObject
	 * @param key
	 * @param value
	 */
	protected void processResultObject(String key, Object value){
		if(resultObject == null){
			JSONObject jsonObject = new JSONObject(1);
			jsonObject.put(key, value);
			resultObject = jsonObject;
		}
	}
}
