package org.hibernate.entity;

import java.io.Serializable;

/**
 * hibernate的类元数据对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class HibernateClassMetadata implements Serializable{
	
	/**
	 * 传进来的资源名
	 */
	private String toResourceName;
	/**
	 * 类名
	 * <p>逻辑上即资源名</p>
	 */
	private String className;
	/**
	 * 所有的属性名集合
	 */
	private String[] propNames;

	public HibernateClassMetadata() {
	}
	public HibernateClassMetadata(String toResourceName) {
		this.toResourceName = toResourceName;
	}
	public HibernateClassMetadata(String className, String[] propNames) {
		this.className = className;
		this.propNames = propNames;
	}
	public HibernateClassMetadata(String toResourceName, String className, String[] propNames) {
		this.toResourceName = toResourceName;
		this.className = className;
		this.propNames = propNames;
	}

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String[] getPropNames() {
		return propNames;
	}
	public void setPropNames(String[] propNames) {
		this.propNames = propNames;
	}
	public String getToResourceName() {
		return toResourceName;
	}
	public void setToResourceName(String toResourceName) {
		this.toResourceName = toResourceName;
	}
}
