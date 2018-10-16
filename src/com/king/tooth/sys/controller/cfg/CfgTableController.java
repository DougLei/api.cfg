package com.king.tooth.sys.controller.cfg;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.jdbc.table.DBTableHandler;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.service.cfg.CfgTableService;
import com.king.tooth.thread.current.CurrentThreadContext;
import com.king.tooth.util.StrUtils;

/**
 * 表信息表Controller
 * @author DougLei
 */
@Controller
public class CfgTableController extends AController{
	
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
			for (ComTabledata table : tables) {
				resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).saveTable(table);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(tables, OperDataTypeConstants.ADD);
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
			for (ComTabledata table : tables) {
				resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).updateTable(table);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(tables, OperDataTypeConstants.EDIT);
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
			resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).deleteTable(tableId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, tableIds);
		return getResultObject(null, null);
	}

	/**
	 * 建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object buildModel(HttpServletRequest request, IJson ijson){
		// 获取数据库连接对象，准备进行create表操作
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		List<ComTabledata> tables = getDataInstanceList(ijson, ComTabledata.class, true);
		for (ComTabledata table : tables) {
			if(StrUtils.isEmpty(table.getId())){
				return "要建模的表id不能为空";
			}
		}
		
		List<String> deleteTableIds = new ArrayList<String>(tables.size());// 记录每个建模的表id
		if(analysisResult == null){
			for (ComTabledata table : tables) {
				resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).buildModel(table.getId(), deleteTableIds, dbTableHandler);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		deleteTableIds.clear();
		return getResultObject(tables, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 取消建模
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object cancelBuildModel(HttpServletRequest request, IJson ijson){
		// 获取数据库连接对象，准备进行create表、drop表的操作
		DBTableHandler dbTableHandler = new DBTableHandler(CurrentThreadContext.getDatabaseInstance());
		List<ComTabledata> tables = getDataInstanceList(ijson, ComTabledata.class, true);
		for (ComTabledata table : tables) {
			if(StrUtils.isEmpty(table.getId())){
				return "要取消建模的表id不能为空";
			}
		}
		
		if(analysisResult == null){
			for (ComTabledata table : tables) {
				resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).cancelBuildModel(dbTableHandler, null, table.getId(), true);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(tables, OperDataTypeConstants.EDIT);
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
		resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).addProjTableRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject(null, null);
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
		resultObject = BuiltinResourceInstance.getInstance("CfgTableService", CfgTableService.class).cancelProjTableRelation(jsonObject.getString("projectId"), jsonObject.getString(ResourcePropNameConstants.ID));
		if(resultObject == null){
			resultObject = jsonObject;
		}
		return getResultObject(null, null);
	}
}
