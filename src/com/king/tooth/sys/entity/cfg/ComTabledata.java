package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.sys.builtin.data.BuiltinCodeDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.common.ComSysResource;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
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
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 表类型：1:单表、2:树表、3:父子关系表
	 * <p>默认值:1</p>
	 */
	private Integer tableType;
	/**
	 * 父表id，只有Table_Type=3的时候，才有效
	 * 主表该字段没有值，子表字段存储的是其父表的id
	 * 子表才会配置该字段的值
	 */
	private String parentTableId;
	/**
	 * 父表名，只有Table_Type=3的时候，才有效
	 * 冗余字段，在create table的时候，根据这个值，自动创建一张关系表，表名需要用到这个值，关系表的表名规则是：[父表名+"_"+子表名+"_"+Link]，先提取父子表名第一个"_"前的值，如果这个值长度还大于5，则只提取前5个
	 * 子表才会配置该字段的值
	 */
	private String parentTableName;
	/**
	 * 是否存在关系表，只有Table_Type=3的时候，才有效
	 * 默认值为0
	 * 如果主子表是一对一的关系，则不需要关系表，子表中有一个parentId关联主表即可，值=0
	 * 如果主子表是多对多关系，则需要关系表，值=1
	 * 子表才会配置该字段的值
	 * <p>默认值:0</p>
	 */
	private Integer isHavaDatalink;
	/**
	 * 子表指向父表的(子表)字段编号
	 * 存储的是子表的字段编号
	 */
	private String subRefParentColumnId;
	/**
	 * 子表指向父表的(子表)字段名
	 * 冗余字段，配置的是子表的字段名，例如parentId
	 * 只有Table_Type=3，isHavaDatalink=0的时候，才有效
	 * 子表才会存储该字段的值
	 */
	private String subRefParentColumnName;
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
	
	//-----------------------------------------------------------------------
	
	/**
	 * 是否是关系表
	 */
	@JSONField(serialize = false)
	private int isDatalinkTable;
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
	public ComTabledata(String tableName, int isDatalinkTable) {
		this.tableName = tableName;
		this.isDatalinkTable = isDatalinkTable;
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
	public String getParentTableId() {
		return parentTableId;
	}
	public void setParentTableId(String parentTableId) {
		this.parentTableId = parentTableId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResourceName() {
		if(StrUtils.isEmpty(resourceName)){
			resourceName = NamingTurnUtil.tableNameTurnClassName(tableName);
		}
		return resourceName;
	}
	public String getParentTableName() {
		return parentTableName;
	}
	public void setParentTableName(String parentTableName) {
		this.parentTableName = parentTableName;
	}
	public int getIsDatalinkTable() {
		return isDatalinkTable;
	}
	public void setIsDatalinkTable(int isDatalinkTable) {
		this.isDatalinkTable = isDatalinkTable;
	}
	public Integer getIsHavaDatalink() {
		return isHavaDatalink;
	}
	public String getSubRefParentColumnId() {
		return subRefParentColumnId;
	}
	public void setSubRefParentColumnId(String subRefParentColumnId) {
		this.subRefParentColumnId = subRefParentColumnId;
	}
	public void setIsHavaDatalink(Integer isHavaDatalink) {
		this.isHavaDatalink = isHavaDatalink;
	}
	public String getSubRefParentColumnName() {
		return subRefParentColumnName;
	}
	public void setSubRefParentColumnName(String subRefParentColumnName) {
		this.subRefParentColumnName = subRefParentColumnName;
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
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_TABLEDATA", 0);
		table.setName("表数据信息资源对象表");
		table.setComments("表数据信息资源对象表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0);
		table.setIsCreated(1);
		table.setBelongPlatformType(CONFIG_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(26);
		
		ComColumndata nameColumn = new ComColumndata("name", BuiltinCodeDataType.STRING, 100);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		ComColumndata tableNameColumn = new ComColumndata("table_name", BuiltinCodeDataType.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			tableNameColumn.setLength(30);
		}
		tableNameColumn.setName("表名");
		tableNameColumn.setIsNullabled(0);
		tableNameColumn.setComments("表名");
		tableNameColumn.setOrderCode(2);
		columns.add(tableNameColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", BuiltinCodeDataType.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setOrderCode(3);
		columns.add(resourceNameColumn);
		
		ComColumndata tableTypeColumn = new ComColumndata("table_type", BuiltinCodeDataType.INTEGER, 1);
		tableTypeColumn.setName("表类型");
		tableTypeColumn.setComments("表类型：1:单表、2:树表、3:父子关系表");
		tableTypeColumn.setDefaultValue("1");
		tableTypeColumn.setOrderCode(4);
		columns.add(tableTypeColumn);
		
		ComColumndata parentTableIdColumn = new ComColumndata("parent_table_id", BuiltinCodeDataType.STRING, 32);
		parentTableIdColumn.setName("父表id");
		parentTableIdColumn.setComments("父表id，只有Table_Type=3的时候，才有效，主表该字段没有值，子表字段存储的是其父表的id，子表才会配置该字段的值");
		parentTableIdColumn.setOrderCode(5);
		columns.add(parentTableIdColumn);
		
		ComColumndata parentTableNameColumn = new ComColumndata("parent_table_name", BuiltinCodeDataType.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			parentTableNameColumn.setLength(30);
		}
		parentTableNameColumn.setName("父表名(冗余字段)");
		parentTableNameColumn.setComments("父表名，只有Table_Type=3的时候，才有效，冗余字段，在create table的时候，根据这个值，自动创建一张关系表，表名需要用到这个值，关系表的表名规则是：[父表名+\"_\"+子表名+\"_\"+Link]，先提取父子表名第一个\"_\"前的值，如果这个值长度还大于5，则只提取前5个，子表才会配置该字段的值");
		parentTableNameColumn.setOrderCode(6);
		columns.add(parentTableNameColumn);
		
		ComColumndata isHavaDatalinkColumn = new ComColumndata("is_hava_datalink", BuiltinCodeDataType.INTEGER, 1);
		isHavaDatalinkColumn.setName("是否是关系表");
		isHavaDatalinkColumn.setComments("是否是关系表，默认是0");
		isHavaDatalinkColumn.setDefaultValue("0");
		isHavaDatalinkColumn.setOrderCode(7);
		columns.add(isHavaDatalinkColumn);
		
		ComColumndata subRefParentColumnIdColumn = new ComColumndata("sub_ref_parent_column_id", BuiltinCodeDataType.STRING, 32);
		subRefParentColumnIdColumn.setName("子表指向父表的(子表)字段编号");
		subRefParentColumnIdColumn.setComments("子表指向父表的(子表)字段编号，存储的是子表的字段编号");
		subRefParentColumnIdColumn.setOrderCode(8);
		columns.add(subRefParentColumnIdColumn);
		
		ComColumndata subRefParentColumnNameColumn = new ComColumndata("sub_ref_parent_column_name", BuiltinCodeDataType.STRING, 40);
		subRefParentColumnNameColumn.setName("子表指向父表的(子表)字段名(冗余字段)");
		subRefParentColumnNameColumn.setComments("子表指向父表的(子表)字段名，冗余字段，配置的是子表的字段名，例如parentId，只有Table_Type=3，isHavaDatalink=0的时候，才有效，子表才会配置该字段的值");
		subRefParentColumnNameColumn.setOrderCode(9);
		columns.add(subRefParentColumnNameColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", BuiltinCodeDataType.STRING, 200);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setOrderCode(10);
		columns.add(commentsColumn);
		
		ComColumndata isCoreColumn = new ComColumndata("is_core", BuiltinCodeDataType.INTEGER, 1);
		isCoreColumn.setName("是否是核心表");
		isCoreColumn.setComments("是否是核心表:这个由后端开发人员控制，在发布时会用到");
		isCoreColumn.setDefaultValue("0");
		isCoreColumn.setOrderCode(11);
		columns.add(isCoreColumn);
		
		ComColumndata isResourceColumn = new ComColumndata("is_resource", BuiltinCodeDataType.INTEGER, 1);
		isResourceColumn.setName("是否是资源");
		isResourceColumn.setComments("是否是资源:这个字段由开发人员控制，不开放给用户");
		isResourceColumn.setDefaultValue("0");
		isResourceColumn.setOrderCode(12);
		columns.add(isResourceColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_TABLEDATA";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "ComTabledata";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put("tableType", tableType);
		entityJson.put("isHavaDatalink", isHavaDatalink);
		entityJson.put("isCore", isCore);
		entityJson.put("isResource", isResource);
		super.processSysResourceProps(entityJson);
		return entityJson.getEntityJson();
	}
	
	public String validNotNullProps() {
		if(StrUtils.isEmpty(tableName)){
			return "表名不能为空！";
		}
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType) && isDatalinkTable == 0 && this.tableName.length() > 30){
			return "oracle数据库的表名长度不能超过30个字符！";
		}
		return null;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			this.tableName = tableName.trim();
			this.resourceName = NamingTurnUtil.tableNameTurnClassName(tableName);
			
			if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType) && isDatalinkTable == 1 && this.tableName.length() > 30){
				// oracle的表名长度不能超过30个字符，所以这里对关系表的表名做处理：前缀+'_'+表名substring(5, 16)+'_'+后缀
				Log4jUtil.info("在oracle数据库中，解析关系表[{}]时，因关系表名长度超过30个字符，系统自动处理",  tableName);
				this.tableName = "DL_" + this.tableName.substring(5, 16) + "_LINKS";
				Log4jUtil.info("自动处理的新表名为：{}",  tableName);
			}
		}
		return result;
	}
	
	public ComSysResource turnToResource() {
		ComSysResource resource = super.turnToResource();
		resource.setResourceType(TABLE);
		resource.setResourceName(resourceName);
		if(isDatalinkTable == 1){
			resource.setRefResourceId(parentTableId);
		}
		return resource;
	}
	
	public ComSysResource turnToPublishResource(String projectId, String refResourceId) {
		throw new IllegalArgumentException("该资源目前不支持turnToPublishResource功能");
	}
	
	@JSONField(serialize = false)
	public Integer getResourceType() {
		return TABLE;
	}
	
	/**
	 * 目前只在ComTabledataService类的batchPublishTable()中用到
	 */
	public ComPublishInfo turnToPublish() {
		ComPublishInfo publish = new ComPublishInfo();
		publish.setPublishDatabaseId(refDatabaseId);
		publish.setPublishProjectId(projectId);
		publish.setPublishResourceId(id);
		publish.setPublishResourceName(resourceName);
		publish.setResourceType(TABLE);
		return publish;
	}
}
