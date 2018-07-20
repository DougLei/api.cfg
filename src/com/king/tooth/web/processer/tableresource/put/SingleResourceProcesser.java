package com.king.tooth.web.processer.tableresource.put;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends PutProcesser {

	public String getProcesserName() {
		return "【Put-TableResource】SingleResourceProcesser";
	}
	
	protected boolean doPutProcess() {
		int tmpCount = -1;
		String updateHql = null;
		JSONObject updatedJsonObj = null;
		// 记录set以及where id条件的值集合
		List<Object> params = new ArrayList<Object>();
		
		List<Object> tmpParameters;
		// 遍历提交的数据，拼装update语句，更新数据
		for(int i=0; i < json.size(); i++){
			updatedJsonObj = json.get(i);
			updateHql = getUpdateHql(updatedJsonObj, params).toString();
			hqlParameterValues.addAll(0, params);// 将set的值，以及where id条件的值一并存储到参数集合，统一调用
			
			tmpParameters = new ArrayList<Object>(hqlParameterValues);
			HibernateUtil.executeUpdateByHql(BuiltinDatabaseData.UPDATE, updateHql, tmpParameters);
			
			// 将set的值，以及where id条件的值从hql参数集合移除
			tmpCount = params.size();
			for(int j= 0 ;j < tmpCount; j++){
				hqlParameterValues.remove(0);
			}
			params.clear();
		}
		installResponseBodyForUpdateData(null, json.getJson(), true);
		return true;
	}

	protected StringBuilder getUpdateHql(JSONObject updatedJsonObj, List<Object> params) {
		ResourceHandlerUtil.initBasicPropValsForUpdate(requestBody.getRouteBody().getResourceName(), updatedJsonObj, null);
		
		updateHql.setLength(0);
		updateHql.append(" update ")
		   .append(requestBody.getRouteBody().getResourceName())
		   .append(" set ");
		
		String queryCondHql = builtinQueryCondMethodProcesser.getHql().toString();// builtinQueryCondMethodProcesser的hql语句
		String whereIdHql = null;// 记录id的where条件，这个可有可无。还能根据builtinQueryCondMethodProcesser查询参数做为条件，更新数据
		String idValue = null;// 记录id的值
		
		Set<String> propsNames = updatedJsonObj.keySet();
		
		for (String pn : propsNames) {
			if(pn.equalsIgnoreCase(ResourceNameConstants.ID)){
				whereIdHql = " where " + ResourceNameConstants.ID + " = ?";
				idValue = updatedJsonObj.getString(pn);
				continue;
			}
			if(updatedJsonObj.get(pn) == null){
				continue;
			}
			
			updateHql.append(pn);
			updateHql.append(" = ?").append(",");
			params.add(updatedJsonObj.get(pn));
		}
		updateHql.setLength(updateHql.length()-1);
		
		if(whereIdHql != null){
			params.add(idValue);
			updateHql.append(whereIdHql);
			queryCondHql = queryCondHql.replace("where", "and");// 去掉builtinQueryCondMethodProcesser的hql语句的where，换成and
		}
		updateHql.append(queryCondHql);
		return updateHql;
	}
}
