package com.api.sys.entity.cfg.projectmodule;

import java.io.Serializable;

import com.api.util.StrUtils;

/**
 * 前端对应的项目模块对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProjectModule implements Serializable, Comparable<ProjectModule>{
	private String id;
	private String text;
	private String link;
	private String appUrl;
	private String icon;
	private boolean hide;
	private int orderCode;
	
	public String getId() {
		return id;
	}
	public String getText() {
		return text;
	}
	public String getLink() {
		return link;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getIcon() {
		return icon;
	}
	public boolean getHide() {
		return hide;
	}
	public int getOrderCode() {
		return orderCode;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setHide(Object hide) {
		if(StrUtils.notEmpty(hide)){
			this.hide = hide.toString().equals("0");
		}
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}
	
	public int compareTo(ProjectModule pm) {
		if(this.orderCode > pm.getOrderCode()){
			return 1;
		}else if(this.orderCode < pm.getOrderCode()){
			return -1;
		}else{
			return 0;
		}
	}
}
