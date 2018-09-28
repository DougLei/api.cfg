package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class ComTabledata extends AbstractSysResource implements ITable, IEntityPropAnalysis{
	/**
	 * 显示的汉字名称
	 */
	private String name;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 旧表名
	 * <p>如果修改了表名，这里记录之前的表名</p>
	 */
	private String oldTableName;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 表类型：1:单表、2、表类型/游标类型
	 * <p>默认值:1</p>
	 */
	private Integer tableType;
	/**
	 * 注释
	 */
	private String comments;
	/**
	 * 是否是核心表
	 * <p>这个由后端开发人员控制，在发布时会用到</p>
	 */
	private Integer isCore;
	/**
	 * 是否是资源
	 * <p>这个字段由开发人员控制，不开放给用户</p>
	 */
	private Integer isResource;
	/**
	 * 是否建模
	 */
	private Integer isBuildModel;
	
	//-----------------------------------------------------------------------
	
	/**
	 * 列集合
	 */
	@JSONField(serialize = false)
	private List<ComColumndata> columns;
	/**
	 * 数据库类型
	 * 目前主要是判断，如果是oracle数据库时，要判断表名长度不能超过30个字符
	 */
	@JSONField(serialize = false)
	private String dbType;
	/**
	 * 关联的数据库id
	 * 该字段在发布的时候用到
	 * @see turnToPublish()
	 */
	@JSONField(serialize = false)
	private String refDatabaseId;

	/**
	 * 表对应的hbmContent
	 * 在发布数据库时使用
	 */
	@JSONField(serialize = false)
	private String hbmContent;
	
	public ComTabledata() {
	}
	public ComTabledata(String tableName) {
		this.tableName = tableName;
		analysisResourceProp();
	}
	
	//-------------------------------------------------------------------------
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = resourceName;
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getTableName() {
		return tableName;
	}
	public String getOldTableName() {
		return oldTableName;
	}
	public void setOldTableName(String oldTableName) {
		this.oldTableName = oldTableName;
	}
	public List<ComColumndata> getColumns() {
		return columns;
	}
	public void setColumns(List<ComColumndata> columns) {
		this.columns = columns;
	}
	public Integer getTableType() {
		return tableType;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResourceName() {
		if(StrUtils.isEmpty(resourceName)){
			resourceName = NamingProcessUtil.tableNameTurnClassName(tableName);
		}
		return resourceName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getIsResource() {
		return isResource;
	}
	public void setIsResource(Integer isResource) {
		this.isResource = isResource;
	}
	public void clear(){
		if(columns != null && columns.size()>0){
			columns.clear();
		}
	}
	public void setRefDatabaseId(String refDatabaseId) {
		this.refDatabaseId = refDatabaseId;
	}
	public Integer getIsCore() {
		return isCore;
	}
	public void setIsCore(Integer isCore) {
		this.isCore = isCore;
	}
	public String getHbmContent() {
		return hbmContent;
	}
	public void setHbmContent(String hbmContent) {
		this.hbmContent = hbmContent;
	}
	public Integer getIsBuildModel() {
		return isBuildModel;
	}
	public void setIsBuildModel(Integer isBuildModel) {
		this.isBuildModel = isBuildModel;
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(23);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinDataType.STRING, 100);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		ComColumndata tableNameColumn = new ComColumndata("table_name", BuiltinDataType.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			tableNameColumn.setLength(30);
		}
		tableNameColumn.setName("表名");
		tableNameColumn.setIsNullabled(0);
		tableNameColumn.setComments("表名");
		tableNameColumn.setOrderCode(2);
		columns.add(tableNameColumn);
		
		ComColumndata oldTableNameColumn = new ComColumndata("old_table_name", BuiltinDataType.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			oldTableNameColumn.setLength(30);
		}
		oldTableNameColumn.setName("旧表名");
		oldTableNameColumn.setComments("如果修改了表名，这里记录之前的表名");
		oldTableNameColumn.setOrderCode(2);
		columns.add(oldTableNameColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", BuiltinDataType.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setOrderCode(3);
		columns.add(resourceNameColumn);
		
		ComColumndata tableTypeColumn = new ComColumndata("table_type", BuiltinDataType.INTEGER, 1);
		tableTypeColumn.setName("表类型");
		tableTypeColumn.setComments("1:单表、2、表类型/游标类型");
		tableTypeColumn.setDefaultValue("1");
		tableTypeColumn.setOrderCode(4);
		columns.add(tableTypeColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinDataType.STRING, 200);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setOrderCode(10);
		columns.add(commentsColumn);
		
		ComColumndata isCoreColumn = new ComColumndata("is_core", BuiltinDataType.INTEGER, 1);
		isCoreColumn.setName("是否是核心表");
		isCoreColumn.setComments("是否是核心表:这个由后端开发人员控制，在发布时会用到");
		isCoreColumn.setDefaultValue("0");
		isCoreColumn.setOrderCode(11);
		columns.add(isCoreColumn);
		
		ComColumndata isResourceColumn = new ComColumndata("is_resource", BuiltinDataType.INTEGER, 1);
		isResourceColumn.setName("是否是资源");
		isResourceColumn.setComments("是否是资源:这个字段由开发人员控制，不开放给用户");
		isResourceColumn.setDefaultValue("0");
		isResourceColumn.setOrderCode(12);
		columns.add(isResourceColumn);
		
		ComColumndata isBuildModelColumn = new ComColumndata("is_build_model", BuiltinDataType.INTEGER, 1);
		isBuildModelColumn.setName("是否建模");
		isBuildModelColumn.setComments("默认值为0");
		isBuildModelColumn.setDefaultValue("0");
		columns.add(isBuildModelColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("表信息表");
		table.setComments("表信息表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "COM_TABLEDATA";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComTabledata";
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(tableName)){
			return "表名不能为空！";
		}
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType) && this.tableName.length() > 30){
			return "oracle数据库的表名长度不能超过30个字符！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			this.tableName = tableName.trim().toUpperCase();
			this.resourceName = NamingProcessUtil.tableNameTurnClassName(tableName);
		}
		return result;
	}
	
	public SysResource turnToResource() {
		SysResource resource = super.turnToResource();
		resource.setResourceType(TABLE);
		resource.setResourceName(resourceName);
		return resource;
	}
	
	public SysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	@JSONField(serialize = false)
	public Integer getResourceType() {
		return TABLE;
	}
	
	/**
	 * 目前只在ComTabledataService类的batchPublishTable()中用到
	 */
	public DmPublishInfo turnToPublish() {
		DmPublishInfo publish = new DmPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(projectId);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(resourceName);
		publish.setResourceType(TABLE);
		return publish;
	}
}
