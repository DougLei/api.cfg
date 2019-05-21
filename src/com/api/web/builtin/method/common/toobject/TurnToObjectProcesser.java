package com.api.web.builtin.method.common.toobject;

import com.api.util.Log4jUtil;
import com.api.util.StrUtils;
import com.api.web.builtin.method.BuiltinMethodProcesserType;
import com.api.web.builtin.method.common.AbstractBuiltinCommonMethod;

/**
 * 转换成object
 * @author DougLei
 */
public class TurnToObjectProcesser extends AbstractBuiltinCommonMethod{
	
	private boolean turnToObject;
	
	public TurnToObjectProcesser() {
		Log4jUtil.debug("此次请求，没有使用到TurnToObjectProcesser内置方法处理器");
	}
	public TurnToObjectProcesser(String turnToObject) {
		if(StrUtils.notEmpty(turnToObject)) {
			this.turnToObject = Boolean.parseBoolean(turnToObject);
		}
	}
	
	public boolean isTurnToObject() {
		return turnToObject;
	}
	
	public int getProcesserType() {
		return BuiltinMethodProcesserType.TURN_TO_OBJECT;
	}
}
