package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.constants.OperDataTypeConstants;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.CfgPropIEConfExtend;
import com.king.tooth.sys.service.cfg.CfgPropIEConfExtendService;
import com.king.tooth.util.StrUtils;

/**
 * 属性导入导出配置扩展表Controller
 * @author DougLei
 */
@Controller
public class CfgPropIEConfExtendController extends AController{
	
	/**
	 * 添加属性导入导出的扩展配置
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgPropIEConfExtend> propIEConfExtends = getDataInstanceList(ijson, CfgPropIEConfExtend.class, true);
		analysisResourceProp(propIEConfExtends);
		if(analysisResult == null){
			for (CfgPropIEConfExtend propIEConfExtend : propIEConfExtends) {
				resultObject = BuiltinResourceInstance.getInstance("CfgPropIEConfExtendService", CfgPropIEConfExtendService.class).savePropIEConfExtend(propIEConfExtend);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(propIEConfExtends, OperDataTypeConstants.ADD);
	}
	
	/**
	 * 修改属性导入导出的扩展配置
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgPropIEConfExtend> propIEConfExtends = getDataInstanceList(ijson, CfgPropIEConfExtend.class, true);
		analysisResourceProp(propIEConfExtends);
		if(analysisResult == null){
			for (CfgPropIEConfExtend propIEConfExtend : propIEConfExtends) {
				resultObject = BuiltinResourceInstance.getInstance("CfgPropIEConfExtendService", CfgPropIEConfExtendService.class).updatePropIEConfExtend(propIEConfExtend);
				if(resultObject instanceof String){
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(propIEConfExtends, OperDataTypeConstants.EDIT);
	}
	
	/**
	 * 删除属性导入导出的扩展配置
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String propIEConfExtendIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(propIEConfExtendIds)){
			return "要删除的属性导入导出扩展配置id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgPropIEConfExtendService", CfgPropIEConfExtendService.class).deletePropIEConfExtend(propIEConfExtendIds);
		processResultObject(BuiltinParameterKeys._IDS, propIEConfExtendIds);
		return getResultObject(null, null);
	}
}
