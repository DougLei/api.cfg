package com.king.tooth.web.processer.sqlresource.put;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.web.processer.sqlresource.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	public String getProcesserName() {
		return "【Put-SqlResource】SingleResourceProcesser";
	}
	
	protected boolean doProcess() {
		doModifyProcess(SqlStatementType.UPDATE);
		return true;
	}
}
