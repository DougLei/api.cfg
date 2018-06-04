package com.king.tooth.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class BasicEntity implements Serializable{
	/**
	 * 主键
	 */
	protected String id;
	/**
	 * 创建人主键
	 */
	protected String createUserId;
	/**
	 * 最后修改人主键
	 */
	protected String lastUpdatedUserId;
	/**
	 * 创建时间
	 */
	protected Date createTime;
	/**
	 * 最后修改时间
	 */
	protected Date lastUpdateTime;
	
	/**
	 * 所属的项目主键
	 * <p>用来区分不同项目的数据</p>
	 */
	protected String projectId;
}
