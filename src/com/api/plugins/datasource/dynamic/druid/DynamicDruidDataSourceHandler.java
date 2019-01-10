package com.api.plugins.datasource.dynamic.druid;

import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.api.cache.SysContext;
import com.api.plugins.datasource.dynamic.DynamicDataSourceHolder;
import com.api.plugins.datasource.dynamic.IDynamicDataSourceHandler;
import com.api.sys.entity.cfg.CfgDatabase;
import com.api.util.StrUtils;

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
	private DruidDataSource getDruidDataSource(CfgDatabase database){
		DruidDataSource dataSource = new DruidDataSource();
		
		dataSource.setUrl(database.getUrl());
		dataSource.setUsername(database.getLoginUserName());
		dataSource.setPassword(database.getLoginPassword());
		dataSource.setDriverClassName(database.getDriverClass());
		
		// @see jdbc.properties
		String maxActive = SysContext.getSystemConfig("druid.datasource.maxActive");
		if(StrUtils.notEmpty(maxActive)){
			dataSource.setMaxActive(Integer.valueOf(maxActive));
		}
		String minIdle = SysContext.getSystemConfig("druid.datasource.minIdle");
		if(StrUtils.notEmpty(minIdle)){
			dataSource.setMinIdle(Integer.valueOf(minIdle));
		}
		String maxWait = SysContext.getSystemConfig("druid.datasource.maxWait");
		if(StrUtils.notEmpty(maxWait)){
			dataSource.setMaxWait(Integer.valueOf(maxWait));
		}
		String timeBetweenEvictionRunsMillis = SysContext.getSystemConfig("druid.datasource.timeBetweenEvictionRunsMillis");
		if(StrUtils.notEmpty(timeBetweenEvictionRunsMillis)){
			dataSource.setTimeBetweenEvictionRunsMillis(Integer.valueOf(timeBetweenEvictionRunsMillis));
		}
		String poolPreparedStatements = SysContext.getSystemConfig("druid.datasource.poolPreparedStatements");
		if(StrUtils.notEmpty(poolPreparedStatements)){
			dataSource.setPoolPreparedStatements(Boolean.valueOf(poolPreparedStatements));
		}
		String maxPoolPreparedStatementPerConnectionSize = SysContext.getSystemConfig("druid.datasource.maxPoolPreparedStatementPerConnectionSize");
		if(StrUtils.notEmpty(maxPoolPreparedStatementPerConnectionSize)){
			dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(maxPoolPreparedStatementPerConnectionSize));
		}
		
		return dataSource;
	}
	
	public DataSource addDataSource(CfgDatabase database) {
		DruidDataSource dataSource = getDruidDataSource(database);
		dataSourceHolder.addDataSource(database.getId(), dataSource);
		return dataSource;
	}

	public DataSource removeDataSource(String databaseId) {
		if(SysContext.getSystemConfig("current.sys.database.id").equals(databaseId)){
			throw new IllegalArgumentException("不能删除系统内置的数据源");
		}
		return dataSourceHolder.removeDataSource(databaseId);
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
