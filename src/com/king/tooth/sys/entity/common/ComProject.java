package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.CfgColumndata;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.StrUtils;

/**
 * [通用的]项目信息资源对象
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
	 * 项目描述
	 */
	private String descs;
	
	//-----------------------------------------------------------
	
	public ComProject() {
	}

	public void setProjName(String projName) {
		if(StrUtils.isEmpty(projName)){
			throw new NullPointerException("项目名称不能为空");
		}
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

	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "COM_PROJECT");
		table.setIsResource(1);
		table.setName("[通用的]项目信息资源对象表");
		table.setComments("[通用的]项目信息资源对象表");
		table.setIsBuiltin(1);
		table.setPlatformType(IS_COMMON_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		table.setIsNeedDeploy(1);
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(13);
		
		CfgColumndata refDatabaseIdColumn = new CfgColumndata("ref_database_id");
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：目前系统只支持，一个项目对一个数据库");
		refDatabaseIdColumn.setColumnType(DataTypeConstants.STRING);
		refDatabaseIdColumn.setLength(32);
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		CfgColumndata projNameColumn = new CfgColumndata("proj_name");
		projNameColumn.setName("项目名称");
		projNameColumn.setComments("项目名称");
		projNameColumn.setColumnType(DataTypeConstants.STRING);
		projNameColumn.setLength(200);
		projNameColumn.setOrderCode(2);
		columns.add(projNameColumn);
		
		CfgColumndata descsColumn = new CfgColumndata("descs");
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		descsColumn.setColumnType(DataTypeConstants.STRING);
		descsColumn.setLength(800);
		descsColumn.setOrderCode(3);
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
		json.put("validDate", validDate);
		json.put("isNeedDeploy", isNeedDeploy+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("platformType", platformType+"");
		json.put("isCreatedResource", isCreatedResource+"");
		json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		return json;
	}
	
	public void analysisResourceData() {
	}
	
	public ComSysResource turnToResource() {
		analysisResourceData();
		ComSysResource resource = super.turnToResource();
		resource.setRefResourceId(id);
		resource.setResourceType(PROJECT);
		resource.setResourceName(projName);
		return resource;
	}
}
