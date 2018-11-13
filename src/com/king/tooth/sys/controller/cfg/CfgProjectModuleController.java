package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.CfgProjectModule;
import com.king.tooth.sys.service.cfg.CfgProjectModuleService;
import com.king.tooth.util.StrUtils;

/**
 * 项目模块信息表Controller
 * @author DougLei
 */
@Controller
public class CfgProjectModuleController extends AController{
	
	/**
	 * 添加项目模块
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson) {
		List<CfgProjectModule> projectModules = getDataInstanceList(ijson, CfgProjectModule.class, true);
		analysisResourceProp(projectModules, false);
		if(analysisResult == null){
			for (CfgProjectModule projectModule : projectModules) {
				resultObject = BuiltinResourceInstance.getInstance("CfgProjectModuleService", CfgProjectModuleService.class).saveProjectModule(projectModule);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgProjectModule对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(projectModules, null);
	}
	
	/**
	 * 修改项目模块
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson) {
		List<CfgProjectModule> projectModules = getDataInstanceList(ijson, CfgProjectModule.class, true);
		analysisResourceProp(projectModules, true);
		if(analysisResult == null){
			for (CfgProjectModule projectModule : projectModules) {
				resultObject = BuiltinResourceInstance.getInstance("CfgProjectModuleService", CfgProjectModuleService.class).updateProjectModule(projectModule);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgProjectModule对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(projectModules, null);
	}
	
	/**
	 * 删除项目模块
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String projectModuleIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(projectModuleIds)){
			return "要删除的项目模块id不能为空";
		}
		
		String[] projectModuleIdArr = projectModuleIds.split(",");
		for (String projectModuleId : projectModuleIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("CfgProjectModuleService", CfgProjectModuleService.class).deleteProjectModule(projectModuleId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, projectModuleIds);
		return getResultObject(null, null);
	}
}
