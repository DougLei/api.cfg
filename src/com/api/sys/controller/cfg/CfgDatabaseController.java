package com.api.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.constants.ResourcePropNameConstants;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.sys.service.cfg.CfgDatabaseService;
import com.api.util.StrUtils;

/**
 * 数据库信息表Controller
 * @author DougLei
 */
@Controller
public class CfgDatabaseController extends AController{
	
	/**
	 * 添加数据库
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgDatabase> databases = getDataInstanceList(ijson, CfgDatabase.class, true);
		analysisResourceProp(databases, false);
		if(analysisResult == null){
			for (CfgDatabase database : databases) {
				resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).saveDatabase(database);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgDatabase对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(databases, null);
	}
	
	/**
	 * 修改数据库
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgDatabase> databases = getDataInstanceList(ijson, CfgDatabase.class, true);
		analysisResourceProp(databases, true);
		if(analysisResult == null){
			for (CfgDatabase database : databases) {
				resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).updateDatabase(database);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgDatabase对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(databases, null);
	}
	
	/**
	 * 删除数据库
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String databaseIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(databaseIds)){
			return "要删除的数据库id不能为空";
		}
		
		String[] databaseIdArr = databaseIds.split(",");
		for (String databaseId : databaseIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).deleteDatabase(databaseId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, databaseIds);
		return getResultObject(null, null);
	}
	
	/**
	 * 测试数据库连接
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object testLink(HttpServletRequest request, IJson ijson){
		JSONObject jsonObject = getJSONObject(ijson);
		if(StrUtils.isEmpty(jsonObject.getString(ResourcePropNameConstants.ID))){
			return "测试连接的数据库id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).databaseLinkTest(jsonObject.getString(ResourcePropNameConstants.ID));
		
		String linkMsg = resultObject.toString();
		resultObject = null;
		processResultObject("linkMsg", linkMsg);
		return getResultObject(null, null);
	}
}
