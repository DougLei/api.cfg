package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Entity;
import com.king.tooth.sys.builtin.data.BuiltinDataType;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.sys.entity.dm.DmPublishInfo;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Entity
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
	/**
	 * 是否建模
	 */
	private Integer isBuildModel;
	
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
			resourceName = NamingProcessUtil.tableNameTurnClassName(tableName);
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
	public Integer getIsBuildModel() {
		return isBuildModel;
	}
	public void setIsBuildModel(Integer isBuildModel) {
		this.isBuildModel = isBuildModel;
	}
	
	public ComTabledata toCreateTable() {
		ComTabledata table = new ComTabledata("COM_TABLEDATA", 0);
		table.setName("表信息表");
		table.setComments("表信息表");
		table.setIsResource(1);
		table.setIsBuiltin(1);
		table.setIsNeedDeploy(0);
		table.setIsCreated(1);
		table.setBelongPlatformType(ISysResource.COMMON_PLATFORM);
		
		List<ComColumndata> columns = new ArrayList<ComColumndata>(28);
		
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
		tableTypeColumn.setComments("表类型：1:单表、2:树表、3:父子关系表");
		tableTypeColumn.setDefaultValue("1");
		tableTypeColumn.setOrderCode(4);
		columns.add(tableTypeColumn);
		
		ComColumndata parentTableIdColumn = new ComColumndata("parent_table_id", BuiltinDataType.STRING, 32);
		parentTableIdColumn.setName("父表id");
		parentTableIdColumn.setComments("父表id，只有Table_Type=3的时候，才有效，主表该字段没有值，子表字段存储的是其父表的id，子表才会配置该字段的值");
		parentTableIdColumn.setOrderCode(5);
		columns.add(parentTableIdColumn);
		
		ComColumndata parentTableNameColumn = new ComColumndata("parent_table_name", BuiltinDataType.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			parentTableNameColumn.setLength(30);
		}
		parentTableNameColumn.setName("父表名(冗余字段)");
		parentTableNameColumn.setComments("父表名，只有Table_Type=3的时候，才有效，冗余字段，在create table的时候，根据这个值，自动创建一张关系表，表名需要用到这个值，关系表的表名规则是：[父表名+\"_\"+子表名+\"_\"+Link]，先提取父子表名第一个\"_\"前的值，如果这个值长度还大于5，则只提取前5个，子表才会配置该字段的值");
		parentTableNameColumn.setOrderCode(6);
		columns.add(parentTableNameColumn);
		
		ComColumndata isHavaDatalinkColumn = new ComColumndata("is_hava_datalink", BuiltinDataType.INTEGER, 1);
		isHavaDatalinkColumn.setName("是否是关系表");
		isHavaDatalinkColumn.setComments("是否是关系表，默认是0");
		isHavaDatalinkColumn.setDefaultValue("0");
		isHavaDatalinkColumn.setOrderCode(7);
		columns.add(isHavaDatalinkColumn);
		
		ComColumndata subRefParentColumnIdColumn = new ComColumndata("sub_ref_parent_column_id", BuiltinDataType.STRING, 32);
		subRefParentColumnIdColumn.setName("子表指向父表的(子表)字段编号");
		subRefParentColumnIdColumn.setComments("子表指向父表的(子表)字段编号，存储的是子表的字段编号");
		subRefParentColumnIdColumn.setOrderCode(8);
		columns.add(subRefParentColumnIdColumn);
		
		ComColumndata subRefParentColumnNameColumn = new ComColumndata("sub_ref_parent_column_name", BuiltinDataType.STRING, 40);
		subRefParentColumnNameColumn.setName("子表指向父表的(子表)字段名(冗余字段)");
		subRefParentColumnNameColumn.setComments("子表指向父表的(子表)字段名，冗余字段，配置的是子表的字段名，例如parentId，只有Table_Type=3，isHavaDatalink=0的时候，才有效，子表才会配置该字段的值");
		subRefParentColumnNameColumn.setOrderCode(9);
		columns.add(subRefParentColumnNameColumn);
		
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
		isBuildModelColumn.setComments("是否建模");
		isBuildModelColumn.setDefaultValue("0");
		columns.add(isBuildModelColumn);
		
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
			this.tableName = tableName.trim().toUpperCase();
			this.resourceName = NamingProcessUtil.tableNameTurnClassName(tableName);
			
			if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType) && isDatalinkTable == 1 && this.tableName.length() > 30){
				// oracle的表名长度不能超过30个字符，所以这里对关系表的表名做处理：前缀+'_'+表名substring(5, 16)+'_'+后缀
				Log4jUtil.debug("在oracle数据库中，解析关系表[{}]时，因关系表名长度超过30个字符，系统自动处理",  tableName);
				this.tableName = "DL_" + NamingProcessUtil.extractDbObjName(tableName) + "_LINKS";
				Log4jUtil.debug("自动处理的新表名为：{}",  tableName);
			}
		}
		return result;
	}
	
	public SysResource turnToResource() {
		SysResource resource = super.turnToResource();
		resource.setResourceType(TABLE);
		resource.setResourceName(resourceName);
		if(isDatalinkTable == 1){
			resource.setRefResourceId(parentTableId);
		}
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
