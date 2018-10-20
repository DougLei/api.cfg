package com.king.tooth.sys.entity.sys.file;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.tools.resource.ResourceMetadataInfo;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.entity.resulttype.PageResultEntity;

/**
 * 导出文件类
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ExportFile extends AIEFile implements Serializable, IEntityPropAnalysis{
	
	/**
	 * 要导出文件的后缀
	 */
	private String exportFileSuffix;
	/**
	 * 导出数据时分页查询的对象
	 */
	private PageResultEntity pageResultEntity;
	/**
	 * 导出数据时，执行查询的对象
	 */
	private Query query;
	/**
	 * 是否是表资源
	 * <p>如果不是表资源，就是sql资源</p>
	 */
	private boolean isTableResource;
	
	public ExportFile() {
	}
	public ExportFile(String fileId, String resourceName, boolean isTableResource, String exportFileSuffix, PageResultEntity pageResultEntity, Query query) {
		this.fileId = fileId;
		this.resourceName = resourceName;
		this.isTableResource = isTableResource;
		this.exportFileSuffix = exportFileSuffix;
		this.pageResultEntity = pageResultEntity;
		this.query = query;
	}
	
	public String getExportFileSuffix() {
		return exportFileSuffix;
	}
	public void setExportFileSuffix(String exportFileSuffix) {
		this.exportFileSuffix = exportFileSuffix;
	}
	public PageResultEntity getPageResultEntity() {
		return pageResultEntity;
	}
	public void setPageResultEntity(PageResultEntity pageResultEntity) {
		this.pageResultEntity = pageResultEntity;
	}
	public boolean getIsTableResource() {
		return isTableResource;
	}
	public void setIsTableResource(boolean isTableResource) {
		this.isTableResource = isTableResource;
	}
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}

	public String validNotNullProps() {
		if(pageResultEntity == null){
			return "生成导出文件时，分页查询的对象[pageResultEntity]不能为空"; 
		}
		if(pageResultEntity.getTotalCount() < 1){
			return "该查询条件下没有任何数据，无法导出文件";
		}
		if(query == null){
			return "生成导出文件时，执行查询的对象[query]不能为空"; 
		}
		if(StrUtils.isEmpty(exportFileSuffix)){
			return "生成导出文件的后缀不能为空";
		}
		if(StrUtils.isEmpty(resourceName)){
			return "生成导出文件，操作的资源名不能为空";
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			exportFileSuffix = exportFileSuffix.toLowerCase();
			if(!isSupportFileSuffix(exportFileSuffix)){
				return "系统不支持导出后缀为["+exportFileSuffix+"]的文件，系统支持导出的文件后缀包括：" +Arrays.toString(supportFileSuffixArray);
			}
			
			Object obj = getIEResourceMetadataInfos(resourceName, 0);
			if(obj instanceof String){
				return obj.toString();
			}
			resourceMetadataInfos = (List<ResourceMetadataInfo>) obj;
		}
		return result;
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ExportFile";
	}
}
