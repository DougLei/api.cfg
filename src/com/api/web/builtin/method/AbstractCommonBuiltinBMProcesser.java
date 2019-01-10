package com.api.web.builtin.method;

import java.util.List;
import java.util.Map;

import com.api.sys.entity.tools.resource.metadatainfo.ResourceMetadataInfo;
import com.api.util.StrUtils;
import com.api.util.datatype.DataTypeValidUtil;
import com.api.web.builtin.method.common.create.exportfile.BuiltinCreateExportFileMethodProcesser;
import com.api.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.api.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;

/**
 * 公共抽象的内置函数的处理器对外接口
 * <p>目的在与提取BuiltinTableResourceBMProcesser和BuiltinSqlResourceBMProcesser公共的代码</p>
 * @author DougLei
 */
public abstract class AbstractCommonBuiltinBMProcesser {
	
	/**
	 * 请求的资源名
	 */
	protected String resourceName;
	/**
	 * 请求的父亲资源名
	 */
	protected String parentResourceName;
	/**
	 * 请求的父亲资源Id
	 */
	protected String parentResourceId;
	
	protected Map<String, String> requestBuiltinParams;
	protected Map<String, String> requestResourceParams;
	protected Map<String, String> requestParentResourceParams;
	protected List<ResourceMetadataInfo> queryResourceMetadataInfos;
	protected List<ResourceMetadataInfo> queryParentResourceMetadataInfos;
	
	/**
	 * 内置聚焦函数处理器实例
	 */
	protected BuiltinFocusedIdMethodProcesser focusedIdProcesser;
	/**
	 * 内置创建导出文件的函数处理器
	 */
	protected BuiltinCreateExportFileMethodProcesser createExportFileProcesser;
	/**
	 * 内置分页函数处理器实例
	 */
	protected BuiltinPagerMethodProcesser pagerProcesser;
	
	/**
	 * 内置聚焦函数处理器实例
	 * @param requestBuiltinParams
	 */
	protected void setFocusedIdProcesser(Map<String, String> requestBuiltinParams) {
		String focusedId = requestBuiltinParams.remove("_focusedId");
		if(StrUtils.notEmpty(focusedId)){
			focusedIdProcesser = new BuiltinFocusedIdMethodProcesser(focusedId);
		}
	}
	/**
	 * 内置创建导出文件的函数处理器实例
	 */
	public void setCreateExportFileProcesser(Map<String, String> requestBuiltinParams) {
		String isCreateExport = requestBuiltinParams.remove("_isCreateExport");
		String exportFileSuffix = requestBuiltinParams.remove("_exportFileSuffix");
		if(StrUtils.notEmpty(isCreateExport) && StrUtils.notEmpty(exportFileSuffix)){
			createExportFileProcesser = new BuiltinCreateExportFileMethodProcesser(resourceName, parentResourceName, isCreateExport, exportFileSuffix, requestBuiltinParams.remove("_exportTitle"), requestBuiltinParams.remove("_exportBasicPropNames"));
			this.isCreateExport = createExportFileProcesser.getIsUsed();
			this.exportSelectPropNames = createExportFileProcesser.getExportSelectPropNames();
		}
	}
	/** 
	 * 是否生成导出文件，如果要的话，则必须要有BuiltinPagerMethodProcesser处理器
	 * 		如果用户传入了，则用用户传入的值
	 * 		如果用户没有传入，则用默认值
	 * 		主要控制传入的_start或_page值必须为1，如果_limit或_rows有传入值，则用传入的值，否则用默认值300
	 */
	protected boolean isCreateExport;
	/**
	 * 在生成导出文件时，查询数据的源信息，必须是配置了isExport=1的
	 * BuiltinCreateExportFileMethodProcesser处理器会先处理获得所有要导出的字段信息名，并赋给BuiltinQueryMethodProcesser函数的select参数，保证查询出来的数据列，和配置的isExport=1的元数据集合长度一致
	 * 多个用,隔开
	 */
	protected String exportSelectPropNames;
	
	/**
	 * 内置分页函数处理器实例
	 * @param requestBuiltinParams
	 */
	protected void setPagerProcesser(Map<String, String> requestBuiltinParams) {
		String limit = requestBuiltinParams.remove("_limit");
		String start = requestBuiltinParams.remove("_start");
		
		String rows = requestBuiltinParams.remove("_rows");
		String page = requestBuiltinParams.remove("_page");// 这四个参数的内容，需要理清楚
		
		boolean useLimitStart = (StrUtils.notEmpty(limit) && DataTypeValidUtil.isInteger(limit) && StrUtils.notEmpty(start)) && DataTypeValidUtil.isInteger(start);
		boolean useRowsPage = (StrUtils.notEmpty(rows) && DataTypeValidUtil.isInteger(rows) && StrUtils.notEmpty(page) && DataTypeValidUtil.isInteger(page));
		if(isCreateExport || useLimitStart || useRowsPage){
			if(isCreateExport){
				if(useLimitStart){
					start = "0";
				}else if(useRowsPage){
					page = "0";
				}else{
					rows = "300";
					page = "0";
				}
			}
			pagerProcesser = new BuiltinPagerMethodProcesser(limit, start, rows, page);
		}
	}
	
	public BuiltinFocusedIdMethodProcesser getFocusedIdProcesser() {
		if(focusedIdProcesser == null){
			focusedIdProcesser = new BuiltinFocusedIdMethodProcesser();
		}
		return focusedIdProcesser;
	}
	public BuiltinPagerMethodProcesser getPagerProcesser() {
		if(pagerProcesser == null){
			pagerProcesser = new BuiltinPagerMethodProcesser();
		}
		return pagerProcesser;
	}
	public BuiltinCreateExportFileMethodProcesser getCreateExportFileProcesser() {
		if(createExportFileProcesser == null){
			createExportFileProcesser = new BuiltinCreateExportFileMethodProcesser();
		}
		return createExportFileProcesser;
	}
}
