package com.api.sys.entity.sys.file.ie;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.api.sys.entity.IEntityPropAnalysis;
import com.api.sys.entity.cfg.propextend.query.data.param.QueryPropExtendConfDataParam;
import com.api.sys.entity.tools.resource.metadatainfo.ie.IEResourceMetadataInfo;
import com.api.util.ResourceHandlerUtil;
import com.api.util.StrUtils;

/**
 * 导入文件模版类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ImportFileTemplate extends AIEFile implements Serializable, IEntityPropAnalysis{

	/**
	 * 导入模版文件的后缀
	 */
	private String fileSuffix;
	/**
	 * 查询属性扩展配置的对应数据列表用参数集合
	 * <p>key为每个属性的属性名</p>
	 * <p>value为每个属性的查询用参数实例</p>
	 */
	private Map<String, QueryPropExtendConfDataParam> queryPropExtendConfDataParams;
	
	// --------------------------------------------------
	public String getFileSuffix() {
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	public Map<String, QueryPropExtendConfDataParam> getQueryPropExtendConfDataParams() {
		return queryPropExtendConfDataParams;
	}
	public void setQueryPropExtendConfDataParams(Map<String, QueryPropExtendConfDataParam> queryPropExtendConfDataParams) {
		this.queryPropExtendConfDataParams = queryPropExtendConfDataParams;
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(fileSuffix)){
			return "导入模版文件的后缀不能为空";
		}
		if(StrUtils.isEmpty(resourceName)){
			return "生成导入模版文件，对应的资源名不能为空";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			fileSuffix = fileSuffix.toLowerCase();
			if(!isSupportFileSuffix(fileSuffix)){
				return "系统不支持后缀为["+fileSuffix+"]的导入模版文件，系统支持的导入模版文件后缀包括：" +Arrays.toString(supportFileSuffixArray);
			}
			
			Object obj = getIEResourceMetadataInfos(null, resourceName, 1);
			if(obj instanceof String){
				return obj.toString();
			}
			ieResourceMetadataInfos = (List<IEResourceMetadataInfo>) obj;
			setResourceMetadataExtendInfo(ieResourceMetadataInfos);
			this.fileId = ResourceHandlerUtil.getIdentity();
		}
		return result;
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ImportFileTemplate";
	}
	
	/**
	 * 获取指定属性名的查询属性扩展配置的对应数据列表用参数对象 实例
	 * @param propName
	 * @return
	 */
	public QueryPropExtendConfDataParam getQueryPropExtendConfDataParam(String propName) {
		QueryPropExtendConfDataParam queryPropExtendConfDataParam = null;
		if(queryPropExtendConfDataParams != null && queryPropExtendConfDataParams.size() >0){
			queryPropExtendConfDataParam = queryPropExtendConfDataParams.get(propName);
		}
		if(queryPropExtendConfDataParam == null){
			queryPropExtendConfDataParam = new  QueryPropExtendConfDataParam();
		}
		return queryPropExtendConfDataParam;
	}
}
