package com.king.tooth.sys.controller.cfg;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.controller.AbstractPublishController;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * 表信息表Controller
 * @author DougLei
 */
@Controller
public class CfgTableController extends AbstractPublishController{
	
	/**
	 * 添加表
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComTabledata> tables = getDataInstanceList(ijson, ComTabledata.class, true);
		analysisResourceProp(tables);
		if(analysisResult == null){
			if(tables.size() == 1){
				resultObject = BuiltinObjectInstance.tableService.saveTable(tables.get(0));
			}else{
				for (ComTabledata table : tables) {
					resultObject = BuiltinObjectInstance.tableService.saveTable(table);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			tables.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改表
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComTabledata> tables = getDataInstanceList(ijson, ComTabledata.class, true);
		analysisResourceProp(tables);
		if(analysisResult == null){
			if(tables.size() == 1){
				resultObject = BuiltinObjectInstance.tableService.updateTable(tables.get(0));
			}else{
				for (ComTabledata table : tables) {
					resultObject = BuiltinObjectInstance.tableService.updateTable(table);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			tables.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 删除表
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String tableIds = request.getParameter(BuiltinParameterKeys._IDS);
		String[] tableIdArr = tableIds.split(",");
		for (String tableId : tableIdArr) {
			resultObject = BuiltinObjectInstance.tableService.deleteTable(tableId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, tableIds);
		return getResultObject();
	}
	

	/**
	 * 建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object buildModel(HttpServletRequest request, IJson ijson){
//		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
//			return "建模功能目前只提供给平台开发人员使用";
//		}
		
		// 获取数据库连接对象，准备进行create表、drop表的操作
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		
		int len = ijson.size();
		List<String> deleteTableIds = new ArrayList<String>(len);// 记录每个建模的表id
		String tableId;
		if(len == 1){
			tableId = ijson.get(0).getString(ResourcePropNameConstants.ID);
			if(StrUtils.isEmpty(tableId)){
				return "要建模的表id不能为空";
			}
			resultObject = BuiltinObjectInstance.tableService.buildModel(tableId, deleteTableIds, dbTableHandler);
		}else{
			for(int i=0;i<len ;i++){
				tableId = ijson.get(i).getString(ResourcePropNameConstants.ID);
				if(StrUtils.isEmpty(tableId)){
					resultObject = "要建模的表id不能为空";
					continue;
				}
				resultObject = BuiltinObjectInstance.tableService.buildModel(tableId, deleteTableIds, dbTableHandler);
				if(resultObject != null){
					break;
				}
			}
		}
		if(resultObject == null){
			resultObject = ijson.getJson();
		}
		deleteTableIds.clear();
		return getResultObject();
	}
	
	/**
	 * 取消建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object cancelBuildModel(HttpServletRequest request, IJson ijson){
//		if(!CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
//			return "建模功能目前只提供给平台开发人员使用";
//		}
		
		// 获取数据库连接对象，准备进行create表、drop表的操作
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		
		int len = ijson.size();
		String tableId;
		if(len == 1){
			tableId = ijson.get(0).getString(ResourcePropNameConstants.ID);
			if(StrUtils.isEmpty(tableId)){
				return "要取消建模的表id不能为空";
			}
			resultObject = BuiltinObjectInstance.tableService.cancelBuildModel(dbTableHandler, null, tableId, true);
		}else{
			for(int i=0;i<len ;i++){
				tableId = ijson.get(i).getString(ResourcePropNameConstants.ID);
				if(StrUtils.isEmpty(tableId)){
					resultObject = "要取消建模的表id不能为空";
					continue;
				}
				resultObject = BuiltinObjectInstance.tableService.cancelBuildModel(dbTableHandler, null, tableId, true);
				if(resultObject != null){
					break;
				}
			}
		}
		if(resultObject == null){
			resultObject = ijson.getJson();
		}
		return getResultObject();
	}
	
	
	/**
	 * 建立项目和表的关联关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object addProjTableRelation(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的表id不能为空";
		}
		resultObject = BuiltinObjectInstance.tableService.addProjTableRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
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
	@RequestMapping
	public Object cancelProjTableRelation(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString("projectId"))){
			return "要操作的项目id不能为空";
		}
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要操作的表id不能为空";
		}
		resultObject = BuiltinObjectInstance.tableService.cancelProjTableRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object publish(HttpServletRequest request, IJson ijson){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要发布的表id不能为空";
		}
		resultObject = BuiltinObjectInstance.tableService.publishTable(jsonObject.getString(ResourcePropNameConstants.ID));
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
	public Object cancelPublish(HttpServletRequest request, IJson ijson){
		if(CurrentThreadContext.getCurrentAccountOnlineStatus().isDeveloper()){
			return "取消发布功能，目前只提供给一般开发账户使用";
		}
		
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "要取消发布的表id不能为空";
		}
		resultObject = BuiltinObjectInstance.tableService.cancelPublishTable(jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject();
	}
}
