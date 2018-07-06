package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishBasicData;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.ReflectUtil;

/**
 * 代码资源对象
 * <p>由开发人员维护，不开放给用户</p>
 * @author DougLei
 */
@SuppressWarnings({"serial"})
public class ComCode extends AbstractSysResource implements ITable{
	/**
	 * 代码资源名
	 */
	private String codeResourceName;
	/**
	 * 类的全路径
	 */
	private String classPath;
	/**
	 * 方法名
	 */
	private String methodName;
	/**
	 * 描述
	 */
	private String comments;
	
	//-------------------------------------------------------------------------

	/**
	 * 获取代码类的实例
	 */
	@JSONField(serialize = false)
	public Object getCodeClassInstance(){
		return ReflectUtil.newInstance(classPath);
	}
	
	public String getCodeResourceName() {
		return codeResourceName;
	}
	public void setCodeResourceName(String codeResourceName) {
		this.codeResourceName = codeResourceName;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_CODE", 0);
		table.setName("代码资源对象表");
		table.setComments("代码资源对象表:由开发人员维护，不开放给用户");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		table.setIsCore(1);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata codeResourceNameColumn = new ComColumndata("code_resource_name", BuiltinCodeDataType.STRING, 60);
		codeResourceNameColumn.setName("代码资源名");
		codeResourceNameColumn.setComments("代码资源名");
		codeResourceNameColumn.setOrderCode(1);
		columns.add(codeResourceNameColumn);
		
		ComColumndata classPathColumn = new ComColumndata("class_path", BuiltinCodeDataType.STRING, 120);
		classPathColumn.setName("类的全路径");
		classPathColumn.setComments("类的全路径");
		classPathColumn.setOrderCode(2);
		columns.add(classPathColumn);
		
		ComColumndata methodNameColumn = new ComColumndata("method_name", BuiltinCodeDataType.STRING, 50);
		methodNameColumn.setName("方法名");
		methodNameColumn.setComments("方法名");
		methodNameColumn.setOrderCode(3);
		columns.add(methodNameColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinCodeDataType.STRING, 200);
		commentsColumn.setName("描述");
		commentsColumn.setComments("描述");
		commentsColumn.setOrderCode(4);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_CODE";
	}

	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComCode";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	@JSONField(serialize = false)
	public Integer getResourceType() {
		return CODE;
	}
	
	public ComSysResource turnToResource() {
		ComSysResource resource = super.turnToResource();
		resource.setResourceType(CODE);
		resource.setResourceName(codeResourceName);
		return resource;
	}
	
	public ComSysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	public ComPublishInfo turnToPublish() {
		throw new IllegalArgumentException("该资源目前不支持turnToPublish功能");
	}
	
	/**
	 * 转换为要发布的基础数据资源对象
	 * @return
	 */
	public ComPublishBasicData turnToPublishBasicData(Integer belongPlatformType){
		ComPublishBasicData publishBasicData = new ComPublishBasicData();
		publishBasicData.setBasicDataResourceName(getEntityName());
		publishBasicData.setBasicDataJsonStr(JSONObject.toJSONString(this));
		publishBasicData.setBelongPlatformType(belongPlatformType);
		return publishBasicData;
	}
}
