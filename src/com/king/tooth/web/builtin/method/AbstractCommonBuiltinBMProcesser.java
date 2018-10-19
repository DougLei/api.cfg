package com.king.tooth.web.builtin.method;

import java.util.Map;

import com.king.tooth.util.DataValidUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.web.builtin.method.common.export.file.create.BuiltinCreateExportFileMethodProcesser;
import com.king.tooth.web.builtin.method.common.focusedid.BuiltinFocusedIdMethodProcesser;
import com.king.tooth.web.builtin.method.common.pager.BuiltinPagerMethodProcesser;

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
	
	/**
	 * 内置聚焦函数处理器实例
	 */
	protected BuiltinFocusedIdMethodProcesser focusedIdProcesser;
	/**
	 * 内置分页函数处理器实例
	 */
	protected BuiltinPagerMethodProcesser pagerProcesser;
	/**
	 * 内置创建导出文件的函数处理器
	 */
	protected BuiltinCreateExportFileMethodProcesser createExportFileProcesser;
	
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
	 * 内置分页函数处理器实例
	 * @param requestBuiltinParams
	 */
	protected void setPagerProcesser(Map<String, String> requestBuiltinParams) {
		String limit = requestBuiltinParams.remove("_limit");
		String start = requestBuiltinParams.remove("_start");
		
		String rows = requestBuiltinParams.remove("_rows");
		String page = requestBuiltinParams.remove("_page");// 这四个参数的内容，需要理清楚
		if((StrUtils.notEmpty(limit) && DataValidUtil.isInteger(limit) && StrUtils.notEmpty(start)) && DataValidUtil.isInteger(start)
				|| (StrUtils.notEmpty(rows) && DataValidUtil.isInteger(rows) && StrUtils.notEmpty(page) && DataValidUtil.isInteger(page))){
			pagerProcesser = new BuiltinPagerMethodProcesser(limit, start, rows, page);
		}
	}
	/**
	 * 内置创建导出文件的函数处理器实例
	 * 
	 */
	public void setCreateExportFileProcesser(Map<String, String> requestBuiltinParams) {
		String isCreateExport = requestBuiltinParams.remove("_isCreateExport");
		String exportFileSuffix = requestBuiltinParams.remove("_exportFileSuffix");
		if(StrUtils.notEmpty(isCreateExport) && StrUtils.notEmpty(exportFileSuffix)){
			createExportFileProcesser = new BuiltinCreateExportFileMethodProcesser(resourceName, parentResourceName, isCreateExport, exportFileSuffix);
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
