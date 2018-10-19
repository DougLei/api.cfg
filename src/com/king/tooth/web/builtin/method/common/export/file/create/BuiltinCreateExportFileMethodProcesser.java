package com.king.tooth.web.builtin.method.common.export.file.create;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 内置创建导出文件的函数处理器
 * <p>配合BuiltinPagerMethodProcesser中的，_rows或_limit参数使用，这两个中的任意一个参数指定一次导出的数据数量，提高系统性能</p>
 * <p>_rows或_limit参数的搭配参数(_page或_start)可以随便传值，但是必须传值，建议传值都为0即可</p>
 * @author DougLei
 */
public class BuiltinCreateExportFileMethodProcesser extends AbstractBuiltinCommonMethod{
	
	/**
	 * 请求的资源名
	 */
	private String resourceName;
	private String parentResourceName;
	/**
	 * 是否创建导出文件
	 */
	private boolean isCreateExport;
	/**
	 * 导出文件的后缀
	 */
	private String exportFileSuffix;
	
	public BuiltinCreateExportFileMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinCreateExportFileMethodProcesser内置方法处理器");
	}
	public BuiltinCreateExportFileMethodProcesser(String resourceName, String parentResourceName, String isCreateExport, String exportFileSuffix) {
		if(!"true".equals(isCreateExport)){
			Log4jUtil.debug("此次请求，没有使用到BuiltinCreateExportFileMethodProcesser内置方法处理器");
			return;
		}
		this.resourceName = resourceName;
		this.parentResourceName = parentResourceName;
		this.isCreateExport = true;
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
	public String getParentResourceName() {
		return parentResourceName;
	}
}
