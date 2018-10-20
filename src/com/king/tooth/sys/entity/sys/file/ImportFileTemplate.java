package com.king.tooth.sys.entity.sys.file;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

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
	
	// --------------------------------------------------
	public String getFileSuffix() {
		return fileSuffix;
	}
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
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
			resourceMetadataInfos = (List<ResourceMetadataInfo>) obj;
			this.fileId = ResourceHandlerUtil.getIdentity();
		}
		return result;
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ImportFileTemplate";
	}
}
