package com.api.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.cfg.CfgBusiModel;
import com.api.sys.service.cfg.CfgBusiModelService;

/**
 * 业务模型表Controller
 * @author DougLei
 */
@Controller
public class CfgBusiModelController extends AController{
	
	/**
	 * 添加业务模型
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgBusiModel> busiModels = getDataInstanceList(ijson, CfgBusiModel.class, true);
		analysisResourceProp(busiModels, false);
		if(analysisResult == null){
			for (CfgBusiModel busiModel : busiModels) {
				resultObject = BuiltinResourceInstance.getInstance("CfgBusiModelService", CfgBusiModelService.class).saveBusiModel(busiModel);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgBusiModel对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(busiModels, null);
	}
	
	/**
	 * 修改业务模型
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgBusiModel> busiModels = getDataInstanceList(ijson, CfgBusiModel.class, true);
		analysisResourceProp(busiModels, true);
		if(analysisResult == null){
			for (CfgBusiModel busiModel : busiModels) {
				resultObject = BuiltinResourceInstance.getInstance("CfgBusiModelService", CfgBusiModelService.class).updateBusiModel(busiModel);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgBusiModel对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(busiModels, null);
	}
	
	/**
	 * 删除业务模型
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String busiModelIds = request.getParameter(BuiltinParameterKeys._IDS);
		String[] busiModelIdArr = busiModelIds.split(",");
		for (String busiModelId : busiModelIdArr) {
			resultObject = BuiltinResourceInstance.getInstance("CfgBusiModelService", CfgBusiModelService.class).deleteBusiModel(busiModelId);
			if(resultObject != null){
				break;
			}
		}
		processResultObject(BuiltinParameterKeys._IDS, busiModelIds);
		return getResultObject(null, null);
	}
}
