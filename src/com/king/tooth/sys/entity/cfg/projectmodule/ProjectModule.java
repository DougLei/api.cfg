package com.king.tooth.sys.entity.cfg.projectmodule;

import java.io.Serializable;

import com.king.tooth.util.StrUtils;

/**
 * 前端对应的项目模块对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProjectModule implements Serializable{
	private String id;
	private String text;
	private String link;
	private String icon;
	private Boolean hide;
	
	public String getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public String getLink() {
		return link;
	}
	public String getIcon() {
		return icon;
	}
	public Boolean getHide() {
		return hide;
	}
	
	public void setId(Object id) {
		if(StrUtils.notEmpty(id)){
			this.id = id.toString();
		}
	}
	public void setText(Object text) {
		if(StrUtils.notEmpty(text)){
			this.text = text.toString();
		}
	}
	public void setLink(Object link) {
		if(StrUtils.notEmpty(link)){
			this.link = link.toString();
		}
	}
	public void setIcon(Object icon) {
		if(StrUtils.notEmpty(icon)){
			this.icon = icon.toString();
		}
	}
	public void setHide(Object hide) {
		if(StrUtils.notEmpty(hide)){
			this.hide = hide.toString().equals("0");
		}
	}
}
