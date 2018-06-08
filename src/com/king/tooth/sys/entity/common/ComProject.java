package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;

/**
 * 项目信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProject extends AbstractSysResource implements ITable, IEntity{
	
	/**
	 * 关联的数据库主键
	 * <p>目前系统只支持，一个项目对一个数据库</p>
	 */
	private String refDatabaseId;
	/**
	 * 项目名称
	 */
	private String projName;
	/**
	 * 项目编码
	 */
	private String projCode;
	/**
	 * 项目描述
	 */
	private String descs;
	
	//-----------------------------------------------------------
	
	public ComProject() {
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getProjName() {
		return projName;
	}
	public String getDescs() {
		return descs;
	}
	public void setDescs(String descs) {
		this.descs = descs;
	}
	public String getRefDatabaseId() {
		return refDatabaseId;
	}
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
	

	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_PROJECT", 0);
		table.setIsResource(1);
		table.setName("项目信息资源对象表");
		table.setComments("项目信息资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET+","+DELETE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(15);
		
		ComColumndata refDatabaseIdColumn = new ComColumndata("ref_database_id");
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：目前系统只支持，一个项目对一个数据库");
		refDatabaseIdColumn.setColumnType(DataTypeConstants.STRING);
		refDatabaseIdColumn.setLength(32);
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		ComColumndata projNameColumn = new ComColumndata("proj_name");
		projNameColumn.setName("项目名称");
		projNameColumn.setComments("项目名称");
		projNameColumn.setColumnType(DataTypeConstants.STRING);
		projNameColumn.setLength(200);
		projNameColumn.setOrderCode(2);
		columns.add(projNameColumn);
		
		ComColumndata projCodeColumn = new ComColumndata("proj_code");
		projCodeColumn.setName("项目编码");
		projCodeColumn.setComments("项目编码");
		projCodeColumn.setColumnType(DataTypeConstants.STRING);
		projCodeColumn.setLength(100);
		projCodeColumn.setOrderCode(3);
		columns.add(projCodeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs");
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(800);
		descsColumn.setOrderCode(4);
		columns.add(descsColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT";
	}
	
	public String getEntityName() {
		return "ComProject";
	}

	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("isEnabled", isEnabled+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isDeployed", isDeployed+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
	
	public void analysisResourceData() {
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
}
