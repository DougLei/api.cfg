package com.king.tooth.web.processer.coderesource.get;

import com.king.tooth.web.processer.coderesource.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	public String getProcesserName() {
		return "【Get-CodeResource】SingleResourceProcesser";
	}

	protected boolean doProcess() {
		invokeMethod();
		return true;
	}
}
