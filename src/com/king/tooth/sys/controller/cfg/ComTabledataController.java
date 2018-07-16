package com.king.tooth.sys.controller.cfg;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.service.cfg.ComTabledataService;
import com.king.tooth.util.StrUtils;

/**
 * 表数据信息资源对象控制器
 * @author DougLei
 */
public class ComTabledataController extends AbstractPublishController{
	
	private ComTabledataService tabledataService = new ComTabledataService();
	
	/**
	 * 添加表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object add(HttpServletRequest request, String json){
		List<ComTabledata> tables = getDataInstanceList(json, ComTabledata.class);
		analysisResourceProp(tables);
		if(analysisResult == null){
			if(tables.size() == 1){
				resultObject = tabledataService.saveTable(tables.get(0));
			}else{
				for (ComTabledata table : tables) {
					resultObject = tabledataService.saveTable(table);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 修改表
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	public Object update(HttpServletRequest request, String json){
		List<ComTabledata> tables = getDataInstanceList(json, ComTabledata.class);
		analysisResourceProp(tables);
		if(analysisResult == null){
			if(tables.size() == 1){
				resultObject = tabledataService.updateTable(tables.get(0));
			}else{
				for (ComTabledata table : tables) {
					resultObject = tabledataService.updateTable(table);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
		}
		return getResultObject();
	}
	
	/**
	 * 删除表
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	public Object delete(HttpServletRequest request, String json){
		String tableIds = request.getParameter(ResourceNameConstants.IDS);
		if(StrUtils.isEmpty(tableIds)){
			return "要删除的表id不能为空";
		}
		
		String[] tableIdArr = tableIds.split(",");
		for (String tableId : tableIdArr) {
			resultObject = tabledataService.deleteTable(tableId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(ResourceNameConstants.IDS, tableIds);
		return getResultObject();
	}
	

	/**
	 * 建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object buildModel(HttpServletRequest request, String json){
		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "建模功能目前只提供给平台开发人员使用";
		}
		
		IJson ijson = getIJson(json);
		int len = ijson.size();
		List<String> deleteTableIds = new ArrayList<String>(len);// 记录每个建模的表id
		String tableId;
		if(len == 1){
			tableId = ijson.get(0).getString(ResourceNameConstants.ID);
			if(StrUtils.isEmpty(tableId)){
				return "要建模的表id不能为空";
			}
			deleteTableIds.add(tableId);
			resultObject = tabledataService.buildModel(tableId);
		}else{
			for(int i=0;i<len ;i++){
				tableId = ijson.get(i).getString(ResourceNameConstants.ID);
				if(StrUtils.isEmpty(tableId)){
					resultObject = "要建模的表id不能为空";
					continue;
				}
				deleteTableIds.add(tableId);
				resultObject = tabledataService.buildModel(tableId);
				if(resultObject != null){
					break;
				}
			}
		}
		if(resultObject == null){
			resultObject = ijson.getJson();
		}else{
			// 如果建模出现异常，要将一起建模操作且成功的表都删除掉
			tabledataService.batchCancelBuildModel(deleteTableIds);
		}
		return getResultObject();
	}
	
	/**
	 * 建立项目和表的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object addProjTableRelation(HttpServletRequest request, String json){
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要操作的表id不能为空";
		}
		resultObject = tabledataService.addProjTableRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消项目和表的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelProjTableRelation(HttpServletRequest request, String json){
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要操作的表id不能为空";
		}
		resultObject = tabledataService.cancelProjTableRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	//--------------------------------------------------------------------------------------------------------
	/**
	 * 发布表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object publish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要发布的表id不能为空";
		}
		resultObject = tabledataService.publishTable(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
	
	/**
	 * 取消发布表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	public Object cancelPublish(HttpServletRequest request, String json){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isPlatformDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(json);
		if(StrUtils.isEmpty(jsonObject.getString(ResourceNameConstants.ID))){
			return "要取消发布的表id不能为空";
		}
		resultObject = tabledataService.cancelPublishTable(jsonObject.getString(ResourceNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
