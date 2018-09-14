package com.king.tooth.websocket;

import java.util.ArrayList;
import java.util.List;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.util.JsonUtil;

/**
 * webapi的父类
 * @author DougLei
 */
public abstract class AbstractWebApi {
	
	/**
	 * 验证ijson参数是否为空
	 * @param ijson
	 */
	private boolean vaildIJsonNotNull(IJson ijson){
		if(ijson == null || ijson.size() == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据json串，获取对应数据类型实例对象
	 * @param ijson
	 * @param targetClass
	 * @param clearIJsonData 是否清除ijson中的数据
	 * @return
	 */
	protected <T> T getDataInstance(IJson ijson, Class<T> targetClass, boolean clearIJsonData){
		T entity = null;
		if(vaildIJsonNotNull(ijson)){
			entity = JsonUtil.parseObject(ijson.get(0).toJSONString(), targetClass);
			if(clearIJsonData){
				ijson.clear();
			}
		}
		return entity;
	}
	
	/**
	 * 根据json串，获取对应数据类型实例集合
	 * @param ijson
	 * @param targetClass
	 * @param clearIJsonData 是否清除ijson中的数据
	 * @return
	 */
	protected <T> List<T> getDataInstanceList(IJson ijson, Class<T> targetClass, boolean clearIJsonData){
		List<T> list = null;
		if(vaildIJsonNotNull(ijson)){
			list = new ArrayList<T>(ijson.size());
			for(int i=0;i<ijson.size();i++){
				list.add(JsonUtil.parseObject(ijson.get(i).toJSONString(), targetClass));
			}
			if(clearIJsonData){
				ijson.clear();
			}
		}
		return list;
	}
}
