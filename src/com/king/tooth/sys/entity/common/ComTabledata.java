package com.king.tooth.sys.entity.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.EntityJson;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.DateUtil;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class ComTabledata extends AbstractSysResource implements ITable, IEntity, IEntityPropAnalysis{
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
	 * 版本
	 */
	private Integer version;
	/**
	 * 注释
	 */
	private String comments;
	
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
	 * 是否是资源
	 * <p>这个字段由开发人员控制，不开放给用户</p>
	 * <p>和数据库没有对应的映射</p>
	 */
	@JSONField(serialize = false)
	private int isResource;
	
	
	public ComTabledata() {
	}
	public ComTabledata(String dbType, String tableName, int isDatalinkTable) {
		this.dbType = dbType;
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResourceName() {
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
	public int getIsResource() {
		return isResource;
	}
	public void setIsResource(int isResource) {
		this.isResource = isResource;
	}
	public void clear(){
		if(columns != null && columns.size()>0){
			columns.clear();
		}
	}
	
	public ComTabledata toCreateTable(String dbType) {
		ComTabledata table = new ComTabledata(dbType, "COM_TABLEDATA", 0);
		table.setIsResource(1);
		table.setVersion(1);
		table.setName("表数据信息资源对象表");
		table.setComments("表数据信息资源对象表");
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(1);
		table.setReqResourceMethod(GET+","+DELETE);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(23);
		
		ComColumndata nameColumn = new ComColumndata("name", DataTypeConstants.STRING, 100);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		ComColumndata tableNameColumn = new ComColumndata("table_name", DataTypeConstants.STRING, 80);
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)){
			tableNameColumn.setLength(30);
		}
		tableNameColumn.setName("表名");
		tableNameColumn.setComments("表名");
		tableNameColumn.setOrderCode(2);
		columns.add(tableNameColumn);
		
		ComColumndata resourceNameColumn = new ComColumndata("resource_name", DataTypeConstants.STRING, 60);
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setOrderCode(3);
		columns.add(resourceNameColumn);
		
		ComColumndata tableTypeColumn = new ComColumndata("table_type", DataTypeConstants.INTEGER, 1);
		tableTypeColumn.setName("表类型");
		tableTypeColumn.setComments("表类型：1:单表、2:树表、3:父子关系表");
		tableTypeColumn.setDefaultValue("1");
		tableTypeColumn.setOrderCode(4);
		columns.add(tableTypeColumn);
		
		ComColumndata parentTableIdColumn = new ComColumndata("parent_table_id", DataTypeConstants.STRING, 32);
		parentTableIdColumn.setName("父表id");
		parentTableIdColumn.setComments("父表id，只有Table_Type=3的时候，才有效，主表该字段没有值，子表字段存储的是其父表的id，子表才会配置该字段的值");
		parentTableIdColumn.setOrderCode(5);
		columns.add(parentTableIdColumn);
		
		ComColumndata parentTableNameColumn = new ComColumndata("parent_table_name", DataTypeConstants.STRING, 80);
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType)){
			parentTableNameColumn.setLength(30);
		}
		parentTableNameColumn.setName("父表名(冗余字段)");
		parentTableNameColumn.setComments("父表名，只有Table_Type=3的时候，才有效，冗余字段，在create table的时候，根据这个值，自动创建一张关系表，表名需要用到这个值，关系表的表名规则是：[父表名+\"_\"+子表名+\"_\"+Link]，先提取父子表名第一个\"_\"前的值，如果这个值长度还大于5，则只提取前5个，子表才会配置该字段的值");
		parentTableNameColumn.setOrderCode(6);
		columns.add(parentTableNameColumn);
		
		ComColumndata isHavaDatalinkColumn = new ComColumndata("is_hava_datalink", DataTypeConstants.INTEGER, 1);
		isHavaDatalinkColumn.setName("是否是关系表");
		isHavaDatalinkColumn.setComments("是否是关系表，默认是0");
		isHavaDatalinkColumn.setDefaultValue("0");
		isHavaDatalinkColumn.setOrderCode(7);
		columns.add(isHavaDatalinkColumn);
		
		ComColumndata subRefParentColumnIdColumn = new ComColumndata("sub_ref_parent_column_id", DataTypeConstants.STRING, 32);
		subRefParentColumnIdColumn.setName("子表指向父表的(子表)字段编号");
		subRefParentColumnIdColumn.setComments("子表指向父表的(子表)字段编号，存储的是子表的字段编号");
		subRefParentColumnIdColumn.setOrderCode(8);
		columns.add(subRefParentColumnIdColumn);
		
		ComColumndata subRefParentColumnNameColumn = new ComColumndata("sub_ref_parent_column_name", DataTypeConstants.STRING, 40);
		subRefParentColumnNameColumn.setName("子表指向父表的(子表)字段名(冗余字段)");
		subRefParentColumnNameColumn.setComments("子表指向父表的(子表)字段名，冗余字段，配置的是子表的字段名，例如parentId，只有Table_Type=3，isHavaDatalink=0的时候，才有效，子表才会配置该字段的值");
		subRefParentColumnNameColumn.setOrderCode(9);
		columns.add(subRefParentColumnNameColumn);
		
		ComColumndata commentsColumn = new ComColumndata("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setOrderCode(10);
		columns.add(commentsColumn);
		
		ComColumndata versionColumn = new ComColumndata("version", DataTypeConstants.INTEGER, 3);
		versionColumn.setName("版本");
		versionColumn.setComments("版本");
		versionColumn.setDefaultValue("1");
		versionColumn.setOrderCode(11);
		columns.add(versionColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "COM_TABLEDATA";
	}
	
	public String getEntityName() {
		return "ComTabledata";
	}
	
	public JSONObject toEntityJson() {
		EntityJson entityJson = new EntityJson(JsonUtil.toJsonObject(this));
		entityJson.put(ResourceNameConstants.ID, id);
		entityJson.put("tableType", tableType);
		entityJson.put("isHavaDatalink", isHavaDatalink);
		entityJson.put("version", version);
		entityJson.put("isDatalinkTable", isDatalinkTable);
		entityJson.put("isEnabled", isEnabled);
		entityJson.put("isBuiltin", isBuiltin);
		entityJson.put("isNeedDeploy", isNeedDeploy);
		entityJson.put("isDeployed", isDeployed);
		entityJson.put(ResourceNameConstants.CREATE_TIME, createTime);
		return entityJson.getEntityJson();
	}
	
	public String validNotNullProps() {
		if(!isValidNotNullProps){
			if(StrUtils.isEmpty(tableName)){
				validNotNullPropsResult = "表名不能为空！";
			}
			if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType) && isDatalinkTable == 0 && this.tableName.length() > 30){
				validNotNullPropsResult = "oracle数据库的表名长度不能超过30个字符！";
			}
			isValidNotNullProps = true;
		}
		return validNotNullPropsResult;
	}
	
	public String analysisResourceProp() {
		String result = validNotNullProps();
		if(result == null){
			this.tableName = tableName.trim();
			this.resourceName = NamingTurnUtil.tableNameTurnClassName(tableName);
			
			if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType) && isDatalinkTable == 1 && this.tableName.length() > 30){
				// oracle的表名长度不能超过30个字符，所以这里对关系表的表名做处理：前缀+'_'+表名substring(5, 16)+'_'+6为随机数+'_'+后缀
				Log4jUtil.info("在oracle数据库中，解析关系表[{}]时，因关系表名长度超过30个字符，系统自动处理",  tableName);
				this.tableName = ResourceNameConstants.DATALINK_TABLENAME_PREFIX + this.tableName.substring(5, 16) + "_" + new Random().nextInt(100000) + ResourceNameConstants.DATALINK_TABLENAME_SUFFIX;
				Log4jUtil.info("自动处理的新表名为：{}",  tableName);
			}
		}
		return result;
	}
	
	public ComSysResource turnToResource() {
		analysisResourceProp();
		ComSysResource resource = super.turnToResource();
		resource.setRefResourceId(id);
		resource.setResourceType(TABLE);
		resource.setResourceName(resourceName);
		if(isBuiltin == 1){
			resource.setValidDate(DateUtil.parseDate("2099-12-31 23:59:59"));
		}
		return resource;
	}
}
