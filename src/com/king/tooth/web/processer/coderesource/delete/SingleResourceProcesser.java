package com.king.tooth.web.processer.coderesource.delete;

import com.king.tooth.web.processer.coderesource.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	public String getProcesserName() {
		return "【Delete-CodeResource】SingleResourceProcesser";
	}

	protected boolean doProcess() {
		
		return false;
	}
}
