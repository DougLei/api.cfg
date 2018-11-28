package com.king.tooth.workflow.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.util.StrUtils;

/**
 * 流程对象的基础实体类资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class WfBasicEntity extends BasicEntity{
	/**
	 * 是否在处理中出现异常
	 */
	@JSONField(serialize = false)
	protected boolean isException;
	/**
	 * 异常的信息
	 */
	@JSONField(serialize = false)
	protected String exceptionMessage;
	
	public boolean isException() {
		return isException;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		if(StrUtils.notEmpty(exceptionMessage)){
			isException = true;
			this.exceptionMessage = exceptionMessage;
		}else{
			isException = false;
		}
	}
}
