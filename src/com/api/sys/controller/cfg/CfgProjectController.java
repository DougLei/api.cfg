package com.api.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.cfg.CfgProject;
import com.api.sys.service.cfg.CfgProjectService;
import com.api.util.StrUtils;

/**
 * 项目信息表Controller
 * @author DougLei
 */
@Controller
public class CfgProjectController extends AController{
	
	/**
	 * 添加项目
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgProject> projects = getDataInstanceList(ijson, CfgProject.class, true);
		analysisResourceProp(projects, false);
		if(analysisResult == null){
			for (CfgProject project : projects) {
				resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).saveProject(project);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgProject对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(projects, null);
	}
	
	/**
	 * 修改项目
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgProject> projects = getDataInstanceList(ijson, CfgProject.class, true);
		analysisResourceProp(projects, true);
		if(analysisResult == null){
			for (CfgProject project : projects) {
				resultObject = BuiltinResourceInstance.getInstance("CfgProjectService", CfgProjectService.class).updateProject(project);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgProject对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(projects, null);
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
		return getResultObject(null, null);
	}
}
