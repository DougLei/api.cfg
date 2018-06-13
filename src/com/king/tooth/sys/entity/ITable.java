package com.king.tooth.sys.entity;

import com.king.tooth.sys.entity.cfg.ComTabledata;

/**
 * 表接口
 * @author DougLei
 */
public interface ITable {
	
	/**
	 * 获取表对象
	 * <p>包括列</p>
	 * @return
	 */
	ComTabledata toCreateTable();
	
	/**
	 * 删除表
	 * <p>返回表名</p>
	 * @return
	 */
	String toDropTable();
	
	
	/**
	 * 所属的平台类型
	 * <p>1：配置平台</p>
	 */
	public static final int CONFIG_PLATFORM = 1;
	/**
	 * 所属的平台类型
	 * <p>2：运行平台</p>
	 */
	public static final int APP_PLATFORM = 2;
	/**
	 * 所属的平台类型
	 * <p>3：通用(这个类型由后端开发者控制)</p>
	 */
	public static final int COMMON_PLATFORM = 3;
}
