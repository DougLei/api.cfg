package com.king.tooth.sys.entity.cfg.projectmodule;

import java.io.Serializable;

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
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setName(String name) {
		this.text = name;
	}
	public String getLink() {
		return link;
	}
	public void setUrl(String url) {
		this.link = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Boolean getHide() {
		return hide;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.hide = (isEnabled==1) ? false : true;
	}
}
