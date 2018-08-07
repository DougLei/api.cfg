package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.ResourcePropNameConstants;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.IPublish;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.StrUtils;

/**
 * 项目信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComProject extends AbstractSysResource implements ITable, IEntityPropAnalysis, IPublish{
	
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
	
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getProjName() {
		if(StrUtils.isEmpty(projName)){
			projName = projCode;
		}
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_PROJECT", 0);
		table.setName("项目信息表表");
		table.setComments("项目信息表表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata refDatabaseIdColumn = new ComColumndata("ref_database_id", BuiltinCodeDataType.STRING, 32);
		refDatabaseIdColumn.setName("关联的数据库主键");
		refDatabaseIdColumn.setComments("关联的数据库主键：目前系统只支持，一个项目对一个数据库");
		refDatabaseIdColumn.setIsNullabled(0);
		refDatabaseIdColumn.setOrderCode(1);
		columns.add(refDatabaseIdColumn);
		
		ComColumndata projNameColumn = new ComColumndata("proj_name", BuiltinCodeDataType.STRING, 200);
		projNameColumn.setName("项目名称");
		projNameColumn.setComments("项目名称");
		projNameColumn.setOrderCode(2);
		columns.add(projNameColumn);
		
		ComColumndata projCodeColumn = new ComColumndata("proj_code", BuiltinCodeDataType.STRING, 100);
		projCodeColumn.setName("项目编码");
		projCodeColumn.setComments("项目编码");
		projCodeColumn.setIsNullabled(0);
		projCodeColumn.setOrderCode(3);
		columns.add(projCodeColumn);
		
		ComColumndata descsColumn = new ComColumndata("descs", BuiltinCodeDataType.STRING, 800);
		descsColumn.setName("项目描述");
		descsColumn.setComments("项目描述");
		descsColumn.setOrderCode(4);
		columns.add(descsColumn);
		
		table.setColumns(columns);
		return table;
	}
	public String toDropTable() {
		return "COM_PROJECT";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComProject";
	}

	public SysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(refDatabaseId)){
			return "项目关联的数据库id不能为空";
		}
		if(StrUtils.isEmpty(projCode)){
			return "项目编码不能为空";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		return validNotNullProps();
	}
	
	@JSONField(serialize = false)
	public Integer getResourceType() {
		return PROJECT;
	}
	
	public DmPublishInfo turnToPublish() {
		DmPublishInfo publish = new DmPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(id);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(projCode);
		publish.setResourceType(PROJECT);
		return publish;
	}
	
	public JSONObject toPublishEntityJson(String projectId) {
		JSONObject json = toEntityJson();
		json.put("refDataId", json.getString(ResourcePropNameConstants.ID));
		processPublishEntityJson(json);
		return json;
	}
}
