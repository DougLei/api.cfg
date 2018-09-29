package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.BasicEntity;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class ComTabledata extends BasicEntity implements ITable, IEntityPropAnalysis, IEntity, ISysResource{
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
	private Integer type;
	/**
	 * 注释
	 */
	private String comments;
	/**
	 * 是否被创建
	 * <p>默认值为0</p>
	 * <p>该字段在建模时，值改为1，后续修改字段信息等，该值均不变，只有在取消建模时，才会改为0</p>
	 */
	private Integer isCreated;
	/**
	 * 是否建模
	 * <p>默认值为0</p>
	 * <p>该字段在建模时，值改为1，后续修改字段信息等，该值改为0，用来标识是否建模，是否需要alter table xxx</p>
	 */
	private Integer isBuildModel;
	/**
	 * 是否有效
	 * <p>默认值为1</p>
	 */
	private Integer isEnabled;
	/**
	 * 请求资源的方法
	 * <p>get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持</p>
	 * <p>默认值：all</p>
	 */
	private String requestMethod;
	
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

	public ComTabledata() {
	}
	public ComTabledata(String tableName) {
		this.tableName = tableName;
		this.isCreated = 1;
		analysisResourceProp();
	}
	
	//-------------------------------------------------------------------------
	public String getName() {
		if(StrUtils.isEmpty(name)){
			name = resourceName;
		}
		return name;
	}
	public Integer getIsCreated() {
		return isCreated;
	}
	public void setIsCreated(Integer isCreated) {
		this.isCreated = isCreated;
	}
	public Integer getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
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
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public Integer getIsBuildModel() {
		return isBuildModel;
	}
	public void setIsBuildModel(Integer isBuildModel) {
		this.isBuildModel = isBuildModel;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public void clear(){
		if(columns != null && columns.size()>0){
			columns.clear();
		}
	}
	
	@JSONField(serialize = false)
	public List<ComColumndata> getColumnList() {
		List<ComColumndata> columns = new ArrayList<ComColumndata>(10+7);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 100);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		columns.add(nameColumn);
		
		ComColumndata tableNameColumn = new ComColumndata("table_name", DataTypeConstants.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			tableNameColumn.setLength(30);
		}
		tableNameColumn.setName("表名");
		tableNameColumn.setIsNullabled(0);
		tableNameColumn.setComments("表名");
		columns.add(tableNameColumn);
		
		ComColumndata oldTableNameColumn = new ComColumndata("old_table_name", DataTypeConstants.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			oldTableNameColumn.setLength(30);
		}
		oldTableNameColumn.setName("旧表名");
		oldTableNameColumn.setComments("如果修改了表名，这里记录之前的表名");
		columns.add(oldTableNameColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", DataTypeConstants.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		columns.add(resourceNameColumn);
		
		ComColumndata typeColumn = new ComColumndata("type", DataTypeConstants.INTEGER, 1);
		typeColumn.setName("表类型");
		typeColumn.setComments("1:单表、2、表类型/游标类型");
		typeColumn.setDefaultValue("1");
		columns.add(typeColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		columns.add(commentsColumn);
		
		ComColumndata isCreatedColumn = new ComColumndata("is_created", DataTypeConstants.INTEGER, 1);
		isCreatedColumn.setName("是否被创建");
		isCreatedColumn.setComments("默认值为0，该字段在建模时，值改为1，后续修改字段信息等，该值均不变，只有在取消建模时，才会改为0");
		isCreatedColumn.setDefaultValue("0");
		columns.add(isCreatedColumn);
		
		ComColumndata isBuildModelColumn = new ComColumndata("is_build_model", DataTypeConstants.INTEGER, 1);
		isBuildModelColumn.setName("是否建模");
		isBuildModelColumn.setComments("默认值为0，该字段在建模时，值改为1，后续修改字段信息等，该值改为0，用来标识是否建模，是否需要alter table xxx");
		isBuildModelColumn.setDefaultValue("0");
		columns.add(isBuildModelColumn);
		
		ComColumndata isEnabledColumn = new ComColumndata("is_enabled", DataTypeConstants.INTEGER, 1);
		isEnabledColumn.setName("是否有效");
		isEnabledColumn.setComments("默认值为1");
		isEnabledColumn.setDefaultValue("1");
		columns.add(isEnabledColumn);
		
		ComColumndata requestMethodColumn = new ComColumndata("request_method", DataTypeConstants.STRING, 30);
		requestMethodColumn.setName("请求资源的方法");
		requestMethodColumn.setComments("默认值：all，get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
		requestMethodColumn.setDefaultValue("all");
		columns.add(requestMethodColumn);
		
		return columns;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata(toDropTable());
		table.setName("表信息表");
		table.setComments("表信息表");
		
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
		SysResource resource = new SysResource();
		resource.setRefResourceId(id);
		resource.setResourceType(ResourceInfoConstants.TABLE);
		resource.setResourceName(resourceName);
		resource.setIsEnabled(isEnabled);
		resource.setRequestMethod(requestMethod);
		return resource;
	}
	
	// ---------------------------------------------------------
	/**
	 * 单表
	 * <p>@see type属性</p>
	 */
	public static final Integer SINGLE_TABLE = 1;
	/**
	 * 表数据类型
	 * <p>@see type属性</p>
	 */
	public static final Integer TABLE_DATATYPE = 2;
}
