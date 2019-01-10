package com.api.web.processer.sqlresource.put;

import com.api.web.processer.sqlresource.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	public String getProcesserName() {
		return "【Put-SqlResource】SingleResourceProcesser";
	}
	
	protected boolean doProcess() {
		doModifyProcess();
		return true;
	}
}
