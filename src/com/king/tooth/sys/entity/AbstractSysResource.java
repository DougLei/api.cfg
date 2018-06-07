package com.king.tooth.sys.entity;

import java.util.Date;

/**
 * 系统资源抽象类
 * @author DougLei
 */
@SuppressWarnings("serial")
public abstract class AbstractSysResource extends BasicEntity implements ISysResource{
	/**
	 * 是否有效
	 */
	protected int isEnabled;
	/**
	 * 有效期
	 */
	protected Date validDate;
	/**
	 * 是否需要发布
	 */
	protected int isNeedDeploy;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 */
	protected String reqResourceMethod;
	/**
	 * 是否内置
	 */
	protected int isBuiltin;
	/**
	 * 所属于的平台类型
	 * <p>1:配置平台、2:运行平台、3:公用</p>
	 */
	protected int platformType;
	/**
	 * 是否已经创建资源
	 */
	protected int isCreatedResource;
	
	
	public String getReqResourceMethod() {
		if(reqResourceMethod == null){
			return ALL;
		}
		return reqResourceMethod;
	}
}
