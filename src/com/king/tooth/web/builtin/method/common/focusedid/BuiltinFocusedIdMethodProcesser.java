package com.king.tooth.web.builtin.method.common.focusedid;

import com.king.tooth.util.Log4jUtil;
import com.king.tooth.web.builtin.method.BuiltinMethodProcesserType;
import com.king.tooth.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 内置聚焦函数处理器
 * @author DougLei
 */
public class BuiltinFocusedIdMethodProcesser extends AbstractBuiltinCommonMethod{
	
	/**
	 * 聚焦的数据id
	 */
	private String focusedId;
	
	public BuiltinFocusedIdMethodProcesser() {
		Log4jUtil.debug("此次请求，没有使用到BuiltinFocusedIdMethodProcesser内置方法处理器");
	}
	public BuiltinFocusedIdMethodProcesser(String focusedId) {
		isUsed = true;
		this.focusedId = focusedId.trim();
	}

	public String getFocusedId() {
		return focusedId;
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.FOCUSED_ID;
	}
}
