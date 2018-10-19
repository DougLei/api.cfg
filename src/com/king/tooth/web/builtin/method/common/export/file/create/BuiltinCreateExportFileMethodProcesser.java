package com.king.tooth.web.builtin.method.common.export.file.create;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 内置创建导出文件的函数处理器
 * @author DougLei
 */
public class BuiltinCreateExportFileMethodProcesser extends AbstractBuiltinCommonMethod{
	
	/**
	 * 是否创建导出文件
	 */
	private boolean isCreateExport;
	/**
	 * 导出文件的后缀
	 */
	private String exportFileSuffix;
	/**
	 * 请求的资源名
	 */
	private String resourceName;
	
	public BuiltinCreateExportFileMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinCreateExportFileMethodProcesser内置方法处理器");
	}
	public BuiltinCreateExportFileMethodProcesser(String resourceName, String isCreateExport, String exportFileSuffix) {
		if(!"true".equals(isCreateExport)){
			this.isUsed = false;
			Log4jUtil.debug("此次请求，没有使用到BuiltinCreateExportFileMethodProcesser内置方法处理器");
			return;
		}
		this.isCreateExport = true;
		this.resourceName = resourceName;
		this.exportFileSuffix = exportFileSuffix;
	}

	
	public boolean getIsCreateExport() {
		return isCreateExport;
	}
	public String getExportFileSuffix() {
		return exportFileSuffix;
	}
	public String getResourceName() {
		return resourceName;
	}
}
