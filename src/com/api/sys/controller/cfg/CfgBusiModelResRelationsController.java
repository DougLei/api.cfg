package com.api.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.api.annotation.Controller;
import com.api.annotation.RequestMapping;
import com.api.plugins.ijson.IJson;
import com.api.sys.builtin.data.BuiltinParameterKeys;
import com.api.sys.builtin.data.BuiltinResourceInstance;
import com.api.sys.controller.AController;
import com.api.sys.entity.cfg.CfgBusiModelResRelations;
import com.api.sys.service.cfg.CfgBusiModelResRelationsService;
import com.api.util.StrUtils;

/**
 * 业务模型资源关系表Controller
 * @author DougLei
 */
@Controller
public class CfgBusiModelResRelationsController extends AController{

	/**
	 * 添加业务模型资源关系
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgBusiModelResRelations> busiModelResRelationses = getDataInstanceList(ijson, CfgBusiModelResRelations.class, true);
		analysisResourceProp(busiModelResRelationses, false);
		if(analysisResult == null){
			for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationses) {
				resultObject = BuiltinResourceInstance.getInstance("CfgBusiModelResRelationsService", CfgBusiModelResRelationsService.class).saveBusiModelResRelations(busiModelResRelations);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgBusiModelResRelations对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(busiModelResRelationses, null);
	}
	
	/**
	 * 修改业务模型资源关系
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgBusiModelResRelations> busiModelResRelationses = getDataInstanceList(ijson, CfgBusiModelResRelations.class, true);
		analysisResourceProp(busiModelResRelationses, false);
		if(analysisResult == null){
			for (CfgBusiModelResRelations busiModelResRelations : busiModelResRelationses) {
				resultObject = BuiltinResourceInstance.getInstance("CfgBusiModelResRelationsService", CfgBusiModelResRelationsService.class).updateBusiModelResRelations(busiModelResRelations);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgBusiModelResRelations对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(busiModelResRelationses, null);
	}
	
	/**
	 * 删除业务模型资源关系
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String busiModelResRelationsIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(busiModelResRelationsIds)){
			return "要删除的业务模型资源关系id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgBusiModelResRelationsService", CfgBusiModelResRelationsService.class).deleteBusiModelResRelations(busiModelResRelationsIds);
		processResultObject(BuiltinParameterKeys._IDS, busiModelResRelationsIds);
		return getResultObject(null, null);
	}
}
