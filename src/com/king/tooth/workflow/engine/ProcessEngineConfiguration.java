package com.king.tooth.workflow.engine;

import javax.sql.DataSource;

/**
 * 流程引擎配置对象
 * @author DougLei
 */
public class ProcessEngineConfiguration {
	
	private String driverClass;
	private String url;
	private String userName;
	private String password;
	
	private DataSource dataSource;

	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
