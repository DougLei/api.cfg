package com.king.tooth.sys.entity;

/**
 * 系统资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class AbstractSysResourceEntity extends BasicEntity implements ISysResource{
	
	/**
	 * 是否启用
	 */
	protected int isEnabled;
	
	/**
	 * 是否部署到测试平台
	 */
	protected int isDeploymentTest;
	
	/**
	 * 是否部署到运行平台
	 */
	protected int isDeploymentRun;
}
