package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComPublishInfo;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.JsonUtil;

/**
 * 代码资源对象
 * <p>由开发人员维护，不开放给用户</p>
 * @author DougLei
 */
@SuppressWarnings("serial")
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
	 * 参数类型
	 * <p>多个用,隔开，按方法声明的顺序配置，类型均为全路径</p>
	 */
	private String paramTypes;
	/**
	 * 描述
	 */
	private String comments;
	
	//-------------------------------------------------------------------------

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
	public String getParamTypes() {
		return paramTypes;
	}
	public void setParamTypes(String paramTypes) {
		this.paramTypes = paramTypes;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_CODE", 0);
		table.setIsResource(1);
		table.setVersion(1);
		table.setName("代码资源对象表");
		table.setComments("代码资源对象表:由开发人员维护，不开放给用户");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET);
		table.setIsCreated(1);
		table.setBelongPlatformType(COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(18);
		
		ComColumndata codeResourceNameColumn = new ComColumndata("code_resource_name", DataTypeConstants.STRING, 60);
		codeResourceNameColumn.setName("代码资源名");
		codeResourceNameColumn.setComments("代码资源名");
		codeResourceNameColumn.setOrderCode(1);
		columns.add(codeResourceNameColumn);
		
		ComColumndata classPathColumn = new ComColumndata("class_path", DataTypeConstants.STRING, 150);
		classPathColumn.setName("类的全路径");
		classPathColumn.setComments("类的全路径");
		classPathColumn.setOrderCode(2);
		columns.add(classPathColumn);
		
		ComColumndata methodNameColumn = new ComColumndata("method_name", DataTypeConstants.STRING, 50);
		methodNameColumn.setName("方法名");
		methodNameColumn.setComments("方法名");
		methodNameColumn.setOrderCode(3);
		columns.add(methodNameColumn);
		
		ComColumndata paramTypesColumn = new ComColumndata("param_types", DataTypeConstants.STRING, 100);
		paramTypesColumn.setName("参数类型");
		paramTypesColumn.setComments("参数类型:多个用,隔开，按方法声明的顺序配置，类型均为全路径");
		paramTypesColumn.setOrderCode(4);
		columns.add(paramTypesColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("描述");
		commentsColumn.setComments("描述");
		commentsColumn.setOrderCode(5);
		columns.add(commentsColumn);
		
		table.setColumns(columns);
		return table;
	}
	
	public String toDropTable() {
		return "COM_CODE";
	}
	
	public String getEntityName() {
		return "ComCode";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	public Integer getResourceType() {
		return CODE;
	}
	
	public ComSysResource turnToResource() {
		throw new IllegalArgumentException("该资源目前不支持turnToResource功能");
	}
	
	public ComSysResource turnToPublishResource() {
		ComSysResource resource = super.turnToResource();
		return resource;
	}
	
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		return publish;
	}
}
