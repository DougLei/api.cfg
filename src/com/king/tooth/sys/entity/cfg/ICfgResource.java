package com.king.tooth.sys.entity.cfg;


/**
 * 资源接口
 * @author DougLei
 */
public interface ICfgResource {
	
	/**
	 * 转换为资源对象
	 * @return
	 */
	public CfgResource turnToResource();
	
	/**
	 * 是否更新资源信息
	 * <p>如果修改了[表资源/sql资源]资源名、请求方式、是否有效，也要同步修CfgResource表中的资源名、请求方式、是否有效</p>
	 * @param oldResource
	 * @return
	 */
	public boolean isUpdateResourceInfo(ICfgResource oldResource);
}