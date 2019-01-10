package com.api.web.processer.sqlresource.post;

import com.api.web.processer.sqlresource.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	protected boolean doProcess() {
		doModifyProcess();
		return true;
	}

	public String getProcesserName() {
		return "【Post-SqlResource】SingleResourceProcesser";
	}
}
