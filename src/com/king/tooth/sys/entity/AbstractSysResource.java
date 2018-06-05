package com.king.tooth.sys.entity;

/**
 * 系统资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class AbstractSysResource extends BasicEntity implements ISysResource{
	/**
	 * 是否部署到正式环境
	 */
	protected int isDeploymentRun;
	
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none</p>
	 */
	protected String reqResourceMethod;
}