package com.king.tooth.sys.deploy;

import java.io.Serializable;
import java.util.List;

import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.sys.entity.common.ComProject;
import com.king.tooth.sys.entity.common.ComSqlScript;
import com.king.tooth.sys.entity.common.ComTabledata;

/**
 * 发布实体
 * <p>发布/取消发布时用到</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
public class DeployEntity implements Serializable{
	/**
	 * 要发布的数据库对象
	 */
	private ComDatabase database;
	/**
	 * 要发布的项目对象
	 */
	private ComProject project;
	/**
	 * 要发布的表集合对象
	 */
	private List<ComTabledata> tables;
	/**
	 * 要发布的sql脚本集合对象
	 */
	private List<ComSqlScript> sqlScripts;
	
	// ------------------------------------------------------------------------------------
	
	public ComDatabase getDatabase() {
		return database;
	}
	public void setDatabase(ComDatabase database) {
		this.database = database;
	}
	public ComProject getProject() {
		return project;
	}
	public void setProject(ComProject project) {
		this.project = project;
	}
	public List<ComTabledata> getTables() {
		return tables;
	}
	public void setTables(List<ComTabledata> tables) {
		this.tables = tables;
	}
	public List<ComSqlScript> getSqlScripts() {
		return sqlScripts;
	}
	public void setSqlScripts(List<ComSqlScript> sqlScripts) {
		this.sqlScripts = sqlScripts;
	}
}
