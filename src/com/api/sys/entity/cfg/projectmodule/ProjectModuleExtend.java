package com.api.sys.entity.cfg.projectmodule;

import java.util.List;

/**
 * 前端对应的项目模块扩展对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ProjectModuleExtend extends ProjectModule{
	/**
	 * 子模块集合
	 */
	private List<ProjectModuleExtend> children;

	public List<ProjectModuleExtend> getChildren() {
		return children;
	}
	public void setChildren(List<ProjectModuleExtend> children) {
		this.children = children;
	}
}
