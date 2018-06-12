package com.king.tooth.plugins.datasource.dynamic;

import java.util.Map;

import javax.sql.DataSource;

import com.king.tooth.sys.entity.common.ComDatabase;

/**
 * 动态数据源操作接口
 * @author DougLei
 */
public interface IDynamicDataSourceHandler {
	
	/**
	 * @param dataSourceHandler
	 */
	public void setDataSourceHolder(DynamicDataSourceHolder dataSourceHolder);
	
	/**
	 * 动态添加数据源
	 * @param database
	 * @return 添加的数据源对象
	 */
	public DataSource addDataSource(ComDatabase database);
	
	/**
	 * 动态删除数据源
	 * @param databaseId
	 */
	public DataSource removeDataSource(String databaseId);
	
	/**
	 * 动态获取数据源
	 */
	public DataSource getDataSource();
	
	/**
	 * 根据databaseId，获取对应的数据源
	 */
	public DataSource getDataSource(String databaseId);
	
	/**
	 * 获取动态的数据源对象集合
	 * @return
	 */
	public Map<String, DataSource> getAllDynamicDataSources();
}
