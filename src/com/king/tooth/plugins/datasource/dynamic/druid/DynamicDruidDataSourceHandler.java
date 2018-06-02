package com.king.tooth.plugins.datasource.dynamic.druid;

import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.king.tooth.cache.SysConfig;
import com.king.tooth.plugins.datasource.dynamic.DynamicDataSourceHolder;
import com.king.tooth.plugins.datasource.dynamic.IDynamicDataSourceHandler;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.util.StrUtils;

/**
 * 动态的druid数据源操作者
 * @author DougLei
 */
public class DynamicDruidDataSourceHandler implements IDynamicDataSourceHandler {

	/**
	 * 动态数据源的持有者对象
	 */
	private DynamicDataSourceHolder dataSourceHolder;
	
	public void setDataSourceHolder(DynamicDataSourceHolder dataSourceHolder) {
		this.dataSourceHolder = dataSourceHolder;
	}

	/**
	 * 根据数据库对象，创建数据源对象
	 * @param database
	 * @return
	 */
	private DruidDataSource getDruidDataSource(ComDatabase database){
		DruidDataSource dataSource = new DruidDataSource();
		
		dataSource.setUrl(database.getUrl());
		dataSource.setUsername(database.getLoginUserName());
		dataSource.setPassword(database.getLoginPassword());
		dataSource.setDriverClassName(database.getDriverClassName());
		
		// @see jdbc.properties
		String maxActive = SysConfig.getSystemConfig("druid.datasource.maxActive");
		if(StrUtils.notEmpty(maxActive)){
			dataSource.setMaxActive(Integer.valueOf(maxActive));
		}
		String minIdle = SysConfig.getSystemConfig("druid.datasource.minIdle");
		if(StrUtils.notEmpty(minIdle)){
			dataSource.setMinIdle(Integer.valueOf(minIdle));
		}
		String maxWait = SysConfig.getSystemConfig("druid.datasource.maxWait");
		if(StrUtils.notEmpty(maxWait)){
			dataSource.setMaxWait(Integer.valueOf(maxWait));
		}
		String timeBetweenEvictionRunsMillis = SysConfig.getSystemConfig("druid.datasource.timeBetweenEvictionRunsMillis");
		if(StrUtils.notEmpty(timeBetweenEvictionRunsMillis)){
			dataSource.setTimeBetweenEvictionRunsMillis(Integer.valueOf(timeBetweenEvictionRunsMillis));
		}
		String poolPreparedStatements = SysConfig.getSystemConfig("druid.datasource.poolPreparedStatements");
		if(StrUtils.notEmpty(poolPreparedStatements)){
			dataSource.setPoolPreparedStatements(Boolean.valueOf(poolPreparedStatements));
		}
		String maxPoolPreparedStatementPerConnectionSize = SysConfig.getSystemConfig("druid.datasource.maxPoolPreparedStatementPerConnectionSize");
		if(StrUtils.notEmpty(maxPoolPreparedStatementPerConnectionSize)){
			dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(maxPoolPreparedStatementPerConnectionSize));
		}
		
		return dataSource;
	}
	
	public DataSource addDataSource(ComDatabase database) {
		DruidDataSource dataSource = getDruidDataSource(database);
		dataSourceHolder.addDataSource(database.getId(), dataSource);
		return dataSource;
	}

	public void removeDataSource(String databaseId) {
		if(SysConfig.getSystemConfig("cfg.database.id").equals(databaseId)
				|| SysConfig.getSystemConfig("test.database.id").equals(databaseId)){
			throw new IllegalArgumentException("不能删除系统内置的数据源");
		}
		dataSourceHolder.removeDataSource(databaseId);
	}

	public DataSource getDataSource() {
		return dataSourceHolder.getDataSource();
	}
	
	public DataSource getDataSource(String databaseId) {
		return dataSourceHolder.getDataSource(databaseId);
	}

	public Map<String, DataSource> getAllDynamicDataSources() {
		return dataSourceHolder.getAllDynamicDataSources();
	}
}
