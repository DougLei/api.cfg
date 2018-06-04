package com.king.tooth.web.processer.sqlresource.post;

import com.king.tooth.constants.SqlStatementType;
import com.king.tooth.web.processer.sqlresource.RequestProcesser;

/**
 * 处理这种请求路径格式的处理器：/{resourceType}
 * @author DougLei
 */
public final class SingleResourceProcesser extends RequestProcesser {

	protected boolean doProcess() {
		doModifyProcess(SqlStatementType.INSERT);
		return true;
	}

	public String getProcesserName() {
		return "【Post-SqlResource】SingleResourceProcesser";
	}
}
