package com.king.tooth.sys.entity.cfg.projectmodule;

import java.util.List;

import com.king.tooth.sys.entity.cfg.ComProjectModule;

/**
 * 模块信息扩展对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CfgProjectModuleExtend extends ComProjectModule{
	
	/**
	 * 子模块集合
	 */
	private List<CfgProjectModuleExtend> children;

	public List<CfgProjectModuleExtend> getChildren() {
		return children;
	}
	public void setChildren(List<CfgProjectModuleExtend> children) {
		this.children = children;
	}
}
