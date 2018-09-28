package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AbstractController;
import com.king.tooth.sys.entity.cfg.ComProject;
import com.king.tooth.sys.service.cfg.CfgProjectService;
import com.king.tooth.util.StrUtils;

/**
 * 项目信息表Controller
 * @author DougLei
 */
@Controller
public class CfgProjectController extends AbstractController{
	
	/**
	 * 添加项目
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<ComProject> projects = getDataInstanceList(ijson, ComProject.class, true);
		analysisResourceProp(projects);
		if(analysisResult == null){
			if(projects.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).saveProject(projects.get(0));
			}else{
				for (ComProject project : projects) {
					resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).saveProject(project);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			projects.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 修改项目
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<ComProject> projects = getDataInstanceList(ijson, ComProject.class, true);
		analysisResourceProp(projects);
		if(analysisResult == null){
			if(projects.size() == 1){
				resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).updateProject(projects.get(0));
			}else{
				for (ComProject project : projects) {
					resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).updateProject(project);
					if(resultObject instanceof String){
						break;
					}
					resultJsonArray.add((JSONObject) resultObject);
				}
			}
			projects.clear();
		}
		return getResultObject();
	}
	
	/**
	 * 删除项目
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String projectIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(projectIds)){
			return "要删除的项目id不能为空";
		}

		String[] projectIdArr = projectIds.split(",");
		for (String projectId : projectIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).deleteProject(projectId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, projectIds);
		return getResultObject();
	}
}
