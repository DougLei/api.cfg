package com.king.tooth.sys.entity.sys.file;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.king.tooth.sys.entity.tools.resource.metadatainfo.ie.IEResourceMetadataInfo;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.ResourceHandlerUtil;
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
	 * 导出文件中的标题
	 */
	private String exportTitle;
	private String exportBasicPropNames;
	/**
	 * 要导出的基础字段元数据信息集合
	 */
	@JSONField(serialize = false)
	private List<ResourceMetadataInfo> exportBasicPropMetadataInfos;
	/**
	 * 导出数据时分页查询的对象
	 */
	private PageResultEntity pageResultEntity;
	/**
	 * 导出数据时，执行查询的对象
	 */
	private Query query;
	/**
	 * 要生成导出文件的资源对象
	 */
	private SysResource resource;
	
	public ExportFile() {
	}
	public ExportFile(String fileId, SysResource resource, String exportFileSuffix, String exportTitle, String exportBasicPropNames, PageResultEntity pageResultEntity, Query query) {
		this.fileId = fileId;
		this.resource = resource;
		this.exportFileSuffix = exportFileSuffix;
		this.exportTitle = exportTitle;
		this.exportBasicPropNames = exportBasicPropNames;
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
	public List<ResourceMetadataInfo> getExportBasicPropMetadataInfos() {
		return exportBasicPropMetadataInfos;
	}
	public SysResource getResource() {
		return resource;
	}
	public void setResource(SysResource resource) {
		this.resource = resource;
	}
	public String getExportTitle() {
		return exportTitle;
	}
	public void setExportTitle(String exportTitle) {
		this.exportTitle = exportTitle;
	}
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}

	public String validNotNullProps() {
		if(resource == null){
			return "生成导出文件时，资源对象[resource]不能为空"; 
		}
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
		if(StrUtils.isEmpty(exportTitle)){
			return "生成导出文件中的标题不能为空";
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
			
			Object obj = getIEResourceMetadataInfos(resource, null, 0);
			if(obj instanceof String){
				return obj.toString();
			}
			ieResourceMetadataInfos = (List<IEResourceMetadataInfo>) obj;
			
			if(StrUtils.notEmpty(exportBasicPropNames)){
				String[] exportBasicPropNameArray = exportBasicPropNames.split(",");
				for (String exportBasicPropName : exportBasicPropNameArray) {
					if(ResourcePropNameConstants.ID.equals(exportBasicPropName)){
						ieResourceMetadataInfos.add(0, ResourceHandlerUtil.getBasicPropIEMetadataInfo(exportBasicPropName, resource.isBuiltinResource()));
					}else{
						ieResourceMetadataInfos.add(ResourceHandlerUtil.getBasicPropIEMetadataInfo(exportBasicPropName, resource.isBuiltinResource()));
					}
				}
			}
		}
		return result;
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ExportFile";
	}
	
	/**
	 * 解析导出文件中的标题值
	 * @return
	 */
	public String analyzeExportTitle() {
		Date date = null;
		if(exportTitle.indexOf("${yyyy}")!=-1){
			if(date == null){date = new Date();}
			exportTitle = exportTitle.replace("${yyyy}", DateUtil.formatDate(date, yyyySdf));
		}
		if(exportTitle.indexOf("${numberSeason}")!=-1){
			if(date == null){date = new Date();}
			exportTitle = exportTitle.replace("${numberSeason}", DateUtil.getSeason(date, 1));
		}
		if(exportTitle.indexOf("${CNCharSeason}")!=-1){
			if(date == null){date = new Date();}
			exportTitle = exportTitle.replace("${CNCharSeason}", DateUtil.getSeason(date, 0));
		}
		if(exportTitle.indexOf("${yyyyMM}")!=-1){
			if(date == null){date = new Date();}
			exportTitle = exportTitle.replace("${yyyyMM}", DateUtil.formatDate(date, yyyyMMSdf));
		}
		return exportTitle;
	}
	/**	xxxx年 */
	private static final SimpleDateFormat yyyySdf = new SimpleDateFormat("yyyy年");
	/**	xxxx年xx月 */
	private static final SimpleDateFormat yyyyMMSdf = new SimpleDateFormat("yyyy年MM月");
}
