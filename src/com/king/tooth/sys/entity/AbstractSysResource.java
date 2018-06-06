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
	protected int isDeploymentApp;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 */
	protected String reqResourceMethod;
	/**
	 * 是否内置
	 * <p>如果不是内置，则需要发布出去</>
	 * <p>如果是内置，且platformType=2或3，则也需要发布出去</>
	 * <p>如果是内置，且platformType=1，则不需要发布出去</>
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
