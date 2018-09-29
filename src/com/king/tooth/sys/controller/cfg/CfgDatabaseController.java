package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.service.cfg.CfgDatabaseService;
import com.king.tooth.util.StrUtils;

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
		analysisResourceProp(databases);
		if(analysisResult == null){
			if(databases.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).saveDatabase(databases.get(0));
			}else{
				for (CfgDatabase database : databases) {
					resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).saveDatabase(database);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			databases.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改数据库
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgDatabase> databases = getDataInstanceList(ijson, CfgDatabase.class, true);
		analysisResourceProp(databases);
		if(analysisResult == null){
			if(databases.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).updateDatabase(databases.get(0));
			}else{
				for (CfgDatabase database : databases) {
					resultObject = BuiltinResourceInstance.getInstance("CfgDatabaseService", CfgDatabaseService.class).updateDatabase(database);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			databases.clear();
		}
		return getResultObject();
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
		return getResultObject();
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
		if(linkMsg.startsWith("ok:")){
			jsonObject.put("linkMsg", linkMsg.replaceAll("ok:", ""));
			resultObject = jsonObject;
			return getResultObject();
		}else{
			return resultObject;
		}
	}
}
