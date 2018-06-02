package com.king.tooth.plugins.datasource.dynamic;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.util.Log4jUtil;

/**
 * 动态数据源持有者
 * @author DougLei
 */
public class DynamicDataSourceHolder extends AbstractRoutingDataSource{

	/**
	 * 数据源集合，用于动态操作
	 */
	private transient static Map<String, DataSource> dataSources;
	public static void setDataSources(Map<String, DataSource> dataSources) {
		DynamicDataSourceHolder.dataSources = dataSources;
	}

	protected Object determineCurrentLookupKey(){
		return CurrentThreadContext.getDatabaseId();
	}
	
	/**
	 * spring配置文件加载完成后，检查dataSources属性是否有值
	 */
	public void afterPropertiesSet() {
	}
	
	/**
	 * 获取数据源
	 */
	protected DataSource determineTargetDataSource() {
		Object obj = determineCurrentLookupKey();
		if(obj == null){
			throw new NullPointerException("要获取dataSource的databaseId值为null");
		}
		
		String databaseId = obj.toString();
		if(dataSourceIsExists(databaseId)){
			return dataSources.get(databaseId);
		}else{
			throw new IllegalArgumentException("不存在databaseId值为 ["+databaseId+"]的dataSource！");
		}
	}
	
	/**
	 * 添加数据源
	 * <p>如果已经存在，则不再覆盖添加</p>
	 * @param databaseId
	 * @param dataSource
	 */
	public synchronized void addDataSource(String databaseId, DataSource dataSource){
		if(dataSourceIsExists(databaseId)){
			Log4jUtil.debug("databaseId值为 [{}] 的数据源，已经存在于this.dataSources集合中！", databaseId);
			return;
		}
		dataSources.put(databaseId, dataSource);
	}
	
	/**
	 * 删除数据源
	 * @param databaseId
	 */
	public synchronized void removeDataSource(String databaseId){
		if(!dataSourceIsExists(databaseId)){
			throw new NullPointerException("databaseId值为 [{"+databaseId+"}] 的数据源，不存在于this.dataSources集合中，删除数据源失败！");
		}
		dataSources.remove(databaseId);
	}
	
	/**
	 * 获取数据源
	 * @return
	 */
	public DataSource getDataSource(){
		return determineTargetDataSource();
	}
	
	/**
	 * 指定databaseId的dataSource是否存在
	 * @param databaseId
	 * @return
	 */
	private boolean dataSourceIsExists(String databaseId){
		return dataSources.containsKey(databaseId);
	}

	/**
	 * 根据databaseId，获取对应的数据源
	 * @param databaseId
	 * @return
	 */
	public DataSource getDataSource(String databaseId) {
		return dataSources.get(databaseId);
	}

	/**
	 * 获取动态的数据源对象集合
	 * @return
	 */
	public Map<String, DataSource> getAllDynamicDataSources() {
		return dataSources;
	}
}
