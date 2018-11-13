package com.king.tooth.sys.controller.cfg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.king.tooth.annotation.Controller;
import com.king.tooth.annotation.RequestMapping;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.sys.builtin.data.BuiltinParameterKeys;
import com.king.tooth.sys.builtin.data.BuiltinResourceInstance;
import com.king.tooth.sys.controller.AController;
import com.king.tooth.sys.entity.cfg.CfgPropExtendConf;
import com.king.tooth.sys.service.cfg.CfgPropExtendConfService;
import com.king.tooth.util.StrUtils;

/**
 * 属性扩展配置信息表Controller
 * @author DougLei
 */
@Controller
public class CfgPropExtendConfController extends AController{
	
	/**
	 * 添加属性导入导出的扩展配置
	 * <p>请求方式：POST</p>
	 * @return
	 */
	@RequestMapping
	public Object add(HttpServletRequest request, IJson ijson){
		List<CfgPropExtendConf> propExtendConfs = getDataInstanceList(ijson, CfgPropExtendConf.class, true);
		analysisResourceProp(propExtendConfs, false);
		if(analysisResult == null){
			for (CfgPropExtendConf propExtendConf : propExtendConfs) {
				resultObject = BuiltinResourceInstance.getInstance("CfgPropExtendConfService", CfgPropExtendConfService.class).savePropExtendConf(propExtendConf);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgPropExtendConf对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(propExtendConfs, null);
	}
	
	/**
	 * 修改属性导入导出的扩展配置
	 * <p>请求方式：PUT</p>
	 * @return
	 */
	@RequestMapping
	public Object update(HttpServletRequest request, IJson ijson){
		List<CfgPropExtendConf> propExtendConfs = getDataInstanceList(ijson, CfgPropExtendConf.class, true);
		analysisResourceProp(propExtendConfs, true);
		if(analysisResult == null){
			for (CfgPropExtendConf propExtendConf : propExtendConfs) {
				resultObject = BuiltinResourceInstance.getInstance("CfgPropExtendConfService", CfgPropExtendConfService.class).updatePropExtendConf(propExtendConf);
				if(resultObject instanceof String){
					index++;
					resultObject = "第"+index+"个CfgPropExtendConf对象，" + resultObject;
					break;
				}
				resultJsonArray.add(resultObject);
			}
		}
		return getResultObject(propExtendConfs, null);
	}
	
	/**
	 * 删除属性导入导出的扩展配置
	 * <p>请求方式：DELETE</p>
	 * @return
	 */
	@RequestMapping
	public Object delete(HttpServletRequest request, IJson ijson){
		String propExtendConfIds = request.getParameter(BuiltinParameterKeys._IDS);
		if(StrUtils.isEmpty(propExtendConfIds)){
			return "要删除的属性导入导出扩展配置id不能为空";
		}
		resultObject = BuiltinResourceInstance.getInstance("CfgPropExtendConfService", CfgPropExtendConfService.class).deletePropExtendConfExtend(propExtendConfIds);
		processResultObject(BuiltinParameterKeys._IDS, propExtendConfIds);
		return getResultObject(null, null);
	}
}
