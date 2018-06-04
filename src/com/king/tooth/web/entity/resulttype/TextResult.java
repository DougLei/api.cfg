package com.king.tooth.web.entity.resulttype;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 文本结果实体类
 * <p>给前端返回文字类型的数据</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class TextResult implements Serializable {
	
	@JSONField(name = "result")
	private Object result;

	public TextResult(Object result) {
		this.result = result;
	}
	public TextResult() {
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
