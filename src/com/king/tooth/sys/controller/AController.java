package com.king.tooth.sys.controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.ijson.IJson;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * 控制器的父类
 * @author DougLei
 */
public abstract class AController {
	
	/**
	 * 解析的结果
	 * <p>包括解析的结果</p>
	 */
	protected String analysisResult;
	/**
	 * 处理到的第几个对象
	 */
	protected int index;
	/**
	 * 处理结果对象
	 */
	protected Object resultObject;
	/**
	 * 结果jsonArray对象
	 */
	protected JSONArray resultJsonArray;
	/**
	 * 请求的json数据是否是数组
	 * <p>不是数组，就是对象</p>
	 */
	protected boolean ijsonIsArray;
	
	/**
	 * 验证ijson参数是否为空
	 * @param ijson
	 */
	protected void vaildIJsonNotNull(IJson ijson){
		if(ijson == null || ijson.size() == 0){
			throw new NullPointerException("提交的数据不能为空");
		}
	}
	
	/**
	 * 根据json串，获取对应数据类型实例集合
	 * @param ijson
	 * @param targetClass
	 * @param clearIJsonData 是否清除ijson中的数据
	 * @return
	 */
	protected <T> List<T> getDataInstanceList(IJson ijson, Class<T> targetClass, boolean clearIJsonData){
		vaildIJsonNotNull(ijson);
		ijsonIsArray = ijson.isArray();
		resultJsonArray = new JSONArray(ijson.size());
		
		List<T> list = new ArrayList<T>(ijson.size());
		for(int i=0;i<ijson.size();i++){
			list.add(JsonUtil.parseObject(ijson.get(i).toJSONString(), targetClass));
		}
		if(clearIJsonData){
			ijson.clear();
		}
		return list;
	}
	
	/**
	 * 统一解析数据资源属性
	 * @param list
	 * @param isUpdate 是否是更新，如果是更新，要验证id字段不能为空
	 */
	protected void analysisResourceProp(List<? extends IEntityPropAnalysis> list, boolean isUpdate){
		int index = 1;
		for (IEntityPropAnalysis iEntityPropAnalysis : list) {
			analysisResult = iEntityPropAnalysis.analysisResourceProp();
			if(analysisResult != null){
				analysisResult = "传入的第"+index+"个["+iEntityPropAnalysis.getEntityName()+"]对象，" + analysisResult;
				break;
			}
			if(isUpdate && StrUtils.isEmpty(iEntityPropAnalysis.getId())){
				analysisResult = "传入的第"+index+"个["+iEntityPropAnalysis.getEntityName()+"]对象，"+ResourcePropNameConstants.ID+"(主键)属性值不能为空";
				break;
			}
			index++;
		}
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
	 * 处理ResultObject
	 * <p>单个操作的时候，或删除数据的时候</p>
	 * @param key
	 * @param value
	 */
	protected void processResultObject(String key, Object value){
		if(resultObject == null){
			if(resultJsonArray == null){
				resultJsonArray = new JSONArray(1);
			}
			JSONObject json = new JSONObject(1);
			json.put(key, value);
			resultJsonArray.add(json);
		}
	}
	
	/**
	 * 获得最后的结果对象
	 * @param clearList 会清空该list
	 * @param operDataType 操作数据类型 @see OperDataTypeConstants，用作定位，如果传递null，则不会处理
	 * @return
	 */
	protected Object getResultObject(List<? extends Object> clearList, String operDataType){
		if(clearList != null && clearList.size() > 0){
			clearList.clear();
		}
		
		if(analysisResult != null){ 
			return analysisResult;
		}
		if(resultObject instanceof String){
			return resultObject;
		}
		
		if(operDataType != null){
			if(!ijsonIsArray && resultObject instanceof JSONObject){
				JSONObject json = (JSONObject) resultObject;
				if(json.get(ResourcePropNameConstants.ID) != null){
					json.put(ResourcePropNameConstants.FOCUSED_OPER, json.get(ResourcePropNameConstants.ID) + "_" + operDataType);
				}
			}else if(resultJsonArray != null && resultJsonArray.size() > 0){
				Object object;
				JSONObject json;
				int size = resultJsonArray.size();
				for(int i=0;i<size;i++){
					object = resultJsonArray.get(i);
					if(object instanceof JSONObject){
						json = (JSONObject) object;
						if(json.get(ResourcePropNameConstants.ID) != null){
							json.put(ResourcePropNameConstants.FOCUSED_OPER, json.get(ResourcePropNameConstants.ID) + "_" + operDataType);
						}
					}
				}
			}
		}
		
		if(ijsonIsArray){
			return resultJsonArray;
		}else{
			if(resultObject != null && !(resultObject instanceof String)){
				return resultObject;
			}
			return resultJsonArray.get(0);
		}
	}
}
