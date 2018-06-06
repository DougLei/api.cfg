package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.ISysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.JsonUtil;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [配置系统]表数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CfgTabledata extends AbstractSysResource implements ITable, IEntity{
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
	private int tableType;
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
	private int isHavaDatalink;
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
	private int version;
	/**
	 * 注释
	 */
	private String comments;
	/**
	 * 是否是关系表，默认是0
	 */
	private int isDatalinkTable;
	
	//-----------------------------------------------------------------------
	/**
	 * 列集合
	 */
	private List<CfgColumndata> columns;
	/**
	 * 数据库类型
	 * 在createTable时用到
	 */
	private String dbType;
	
	
	public CfgTabledata() {
		this.version = 1;
	}
	public CfgTabledata(String dbType, String tableName) {
		this();
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType) && tableName.length() > 30){// 判断：如果是oracle数据库表名是否过长
			throw new IllegalAccessError("oracle数据库的表名长度不能超过30个字符");
		}
		doSetTableName(dbType, tableName, 0);
	}
	
	/**
	 * 目前，只让DynamicDataLinkTableUtil.getDataLinkTabledata()方法使用到这个构造函数
	 * 和上面的构造函数区分，如果是创建关系表，则不需要判断：如果是oracle数据库表名是否过长
	 * @param dbType
	 * @param tableName
	 * @param isDatalinkTable 标识是关系表，只有这里会将这个属性的值置为1，其他地方都必须是0
	 */
	public CfgTabledata(String dbType, String tableName, int isDatalinkTable) {
		this();
		this.isDatalinkTable = isDatalinkTable;
		doSetTableName(dbType, tableName, 1);
	}
	
	private void doSetTableName(String dbType, String tableName, int isDatalinkTable) {
		this.dbType = dbType;
		this.tableName = tableName.trim();
		analysisResourceName(this.tableName);
		
		if(isDatalinkTable == 1){
			// oracle的表名长度不能超过30个字符，这里对关系表的表名做处理：前缀+后缀
			if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType) && this.tableName.length() > 30){
				this.tableName = ResourceNameConstants.DATALINK_TABLENAME_PREFIX + this.tableName.substring(5, 16) + "_" + new Random().nextInt(100000) + ResourceNameConstants.DATALINK_TABLENAME_SUFFIX;
			}
		}
	}
	
	/**
	 * 解析出资源名
	 * @param tableName
	 */
	private void analysisResourceName(String tableName) {
		this.resourceName = NamingTurnUtil.tableNameTurnClassName(tableName);
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
		if(StrUtils.isEmpty(dbType)){
			dbType = HibernateUtil.getCurrentDatabaseType();
		}
		return dbType;
	}
	public String getTableName() {
		return tableName;
	}
	public List<CfgColumndata> getColumns() {
		return columns;
	}
	public void setColumns(List<CfgColumndata> columns) {
		this.columns = columns;
	}
	public int getTableType() {
		return tableType;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setLastUpdatedUserId(String lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public void setTableType(int tableType) {
		this.tableType = tableType;
	}
	public String getParentTableId() {
		return parentTableId;
	}
	public void setParentTableId(String parentTableId) {
		this.parentTableId = parentTableId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getComments() {
		return comments;
	}
	public String getId() {
		return id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getResourceName() {
		if(StrUtils.isEmpty(resourceName)){
			analysisResourceName(tableName);
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
	public int getIsHavaDatalink() {
		return isHavaDatalink;
	}
	public String getSubRefParentColumnId() {
		return subRefParentColumnId;
	}
	public void setSubRefParentColumnId(String subRefParentColumnId) {
		this.subRefParentColumnId = subRefParentColumnId;
	}
	public void setIsHavaDatalink(int isHavaDatalink) {
		this.isHavaDatalink = isHavaDatalink;
	}
	public int getIsDeploymentApp() {
		return isDeploymentApp;
	}
	public void setIsDeploymentApp(int isDeploymentApp) {
		this.isDeploymentApp = isDeploymentApp;
	}
	public int getIsBuiltin() {
		return isBuiltin;
	}
	public void setIsBuiltin(int isBuiltin) {
		this.isBuiltin = isBuiltin;
	}
	public int getPlatformType() {
		return platformType;
	}
	public void setPlatformType(int platformType) {
		this.platformType = platformType;
	}
	public int getIsCreatedResource() {
		return isCreatedResource;
	}
	public void setIsCreatedResource(int isCreatedResource) {
		this.isCreatedResource = isCreatedResource;
	}
	public void setReqResourceMethod(String reqResourceMethod) {
		this.reqResourceMethod = reqResourceMethod;
	}
	public String getReqResourceMethod() {
		return super.getReqResourceMethod();
	}
	public String getSubRefParentColumnName() {
		return subRefParentColumnName;
	}
	public void setSubRefParentColumnName(String subRefParentColumnName) {
		this.subRefParentColumnName = subRefParentColumnName;
	}
	public void setTableName(String tableName) {
		if(StrUtils.isEmpty(tableName)){
			throw new NullPointerException("表名不能为空！");
		}
		this.tableName = tableName;
	}
	
	
	public void clear(){
		if(columns != null && columns.size()>0){
			columns.clear();
		}
	}
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "CFG_TABLEDATA");
		table.setName("[配置系统]表数据信息资源对象表");
		table.setComments("[配置系统]表数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(20);
		
		CfgColumndata nameColumn = new CfgColumndata("name");
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		nameColumn.setColumnType(DataTypeConstants.STRING);
		nameColumn.setLength(100);
		nameColumn.setOrderCode(1);
		columns.add(nameColumn);
		
		CfgColumndata tableNameColumn = new CfgColumndata("table_name");
		tableNameColumn.setName("表名");
		tableNameColumn.setComments("表名");
		tableNameColumn.setColumnType(DataTypeConstants.STRING);
		tableNameColumn.setLength(50);
		tableNameColumn.setOrderCode(2);
		columns.add(tableNameColumn);
		
		CfgColumndata resourceNameColumn = new CfgColumndata("resource_name");
		resourceNameColumn.setName("资源名");
		resourceNameColumn.setComments("资源名");
		resourceNameColumn.setColumnType(DataTypeConstants.STRING);
		resourceNameColumn.setLength(50);
		resourceNameColumn.setOrderCode(3);
		columns.add(resourceNameColumn);
		
		CfgColumndata tableTypeColumn = new CfgColumndata("table_type");
		tableTypeColumn.setName("表类型");
		tableTypeColumn.setComments("表类型：1:单表、2:树表、3:父子关系表");
		tableTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		tableTypeColumn.setLength(1);
		tableTypeColumn.setOrderCode(4);
		columns.add(tableTypeColumn);
		
		CfgColumndata parentTableIdColumn = new CfgColumndata("parent_table_id");
		parentTableIdColumn.setName("父表id");
		parentTableIdColumn.setComments("父表id，只有Table_Type=3的时候，才有效，主表该字段没有值，子表字段存储的是其父表的id，子表才会配置该字段的值");
		parentTableIdColumn.setColumnType(DataTypeConstants.STRING);
		parentTableIdColumn.setLength(32);
		parentTableIdColumn.setOrderCode(5);
		columns.add(parentTableIdColumn);
		
		CfgColumndata parentTableNameColumn = new CfgColumndata("parent_table_name");
		parentTableNameColumn.setName("父表名(冗余字段)");
		parentTableNameColumn.setComments("父表名，只有Table_Type=3的时候，才有效，冗余字段，在create table的时候，根据这个值，自动创建一张关系表，表名需要用到这个值，关系表的表名规则是：[父表名+\"_\"+子表名+\"_\"+Link]，先提取父子表名第一个\"_\"前的值，如果这个值长度还大于5，则只提取前5个，子表才会配置该字段的值");
		parentTableNameColumn.setColumnType(DataTypeConstants.STRING);
		parentTableNameColumn.setLength(50);
		parentTableNameColumn.setOrderCode(6);
		columns.add(parentTableNameColumn);
		
		CfgColumndata isHavaDatalinkColumn = new CfgColumndata("is_hava_datalink");
		isHavaDatalinkColumn.setName("是否是关系表");
		isHavaDatalinkColumn.setComments("是否是关系表，默认是0");
		isHavaDatalinkColumn.setColumnType(DataTypeConstants.INTEGER);
		isHavaDatalinkColumn.setLength(1);
		isHavaDatalinkColumn.setOrderCode(7);
		columns.add(isHavaDatalinkColumn);
		
		CfgColumndata subRefParentColumnIdColumn = new CfgColumndata("sub_ref_parent_column_id");
		subRefParentColumnIdColumn.setName("子表指向父表的(子表)字段编号");
		subRefParentColumnIdColumn.setComments("子表指向父表的(子表)字段编号，存储的是子表的字段编号");
		subRefParentColumnIdColumn.setColumnType(DataTypeConstants.STRING);
		subRefParentColumnIdColumn.setLength(32);
		subRefParentColumnIdColumn.setOrderCode(8);
		columns.add(subRefParentColumnIdColumn);
		
		CfgColumndata subRefParentColumnNameColumn = new CfgColumndata("sub_ref_parent_column_name");
		subRefParentColumnNameColumn.setName("子表指向父表的(子表)字段名(冗余字段)");
		subRefParentColumnNameColumn.setComments("子表指向父表的(子表)字段名，冗余字段，配置的是子表的字段名，例如parentId，只有Table_Type=3，isHavaDatalink=0的时候，才有效，子表才会配置该字段的值");
		subRefParentColumnNameColumn.setColumnType(DataTypeConstants.STRING);
		subRefParentColumnNameColumn.setLength(50);
		subRefParentColumnNameColumn.setOrderCode(9);
		columns.add(subRefParentColumnNameColumn);
		
		CfgColumndata commentsColumn = new CfgColumndata("comments");
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		commentsColumn.setColumnType(DataTypeConstants.STRING);
		commentsColumn.setLength(200);
		commentsColumn.setOrderCode(10);
		columns.add(commentsColumn);
		
		CfgColumndata versionColumn = new CfgColumndata("version");
		versionColumn.setName("版本");
		versionColumn.setComments("版本");
		versionColumn.setColumnType(DataTypeConstants.INTEGER);
		versionColumn.setLength(3);
		versionColumn.setOrderCode(11);
		columns.add(versionColumn);
		
		CfgColumndata isDeploymentAppColumn = new CfgColumndata("is_deployment_app");
		isDeploymentAppColumn.setName("是否部署到正式环境");
		isDeploymentAppColumn.setComments("是否部署到正式环境");
		isDeploymentAppColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentAppColumn.setLength(1);
		isDeploymentAppColumn.setOrderCode(11);
		columns.add(isDeploymentAppColumn);
		
		CfgColumndata reqResourceMethodColumn = new CfgColumndata("req_resource_method");
		reqResourceMethodColumn.setName("请求资源的方法");
		reqResourceMethodColumn.setComments("请求资源的方法:get/put/post/delete/all/none，多个可用,隔开；all表示支持全部，none标识都不支持");
		reqResourceMethodColumn.setColumnType(DataTypeConstants.STRING);
		reqResourceMethodColumn.setLength(20);
		reqResourceMethodColumn.setOrderCode(12);
		columns.add(reqResourceMethodColumn);

		CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
		isBuiltinColumn.setName("是否内置");
		isBuiltinColumn.setComments("是否内置:如果不是内置，则需要发布出去；如果是内置，且platformType=2或3，则也需要发布出去；如果是内置，且platformType=1，则不需要发布出去");
		isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
		isBuiltinColumn.setLength(1);
		isBuiltinColumn.setOrderCode(13);
		columns.add(isBuiltinColumn);
		
		CfgColumndata platformTypeColumn = new CfgColumndata("platform_type");
		platformTypeColumn.setName("所属于的平台类型");
		platformTypeColumn.setComments("所属于的平台类型:1:配置平台、2:运行平台、3:公用");
		platformTypeColumn.setColumnType(DataTypeConstants.INTEGER);
		platformTypeColumn.setLength(1);
		platformTypeColumn.setOrderCode(14);
		columns.add(platformTypeColumn);
		
		CfgColumndata isCreatedResourceColumn = new CfgColumndata("is_created_resource");
		isCreatedResourceColumn.setName("是否已经创建资源");
		isCreatedResourceColumn.setComments("是否已经创建资源");
		isCreatedResourceColumn.setColumnType(DataTypeConstants.INTEGER);
		isCreatedResourceColumn.setLength(1);
		isCreatedResourceColumn.setOrderCode(15);
		columns.add(isCreatedResourceColumn);
		
		table.setColumns(columns);
		table.setReqResourceMethod(ISysResource.GET);
		table.setIsBuiltin(1);
		table.setPlatformType(IS_CFG_PLATFORM_TYPE);
		table.setIsCreatedResource(1);
		return table;
	}

	public String toDropTable() {
		return "CFG_TABLEDATA";
	}
	public int getResourceType() {
		return TABLE;
	}
	public String getResourceId() {
		return getId();
	}
	
	public String getEntityName() {
		return "CfgTabledata";
	}
	public JSONObject toEntity() {
		JSONObject json = JsonUtil.toJsonObject(this);
		json.put("tableType", tableType+"");
		json.put("isHavaDatalink", isHavaDatalink+"");
		json.put("version", version+"");
		json.put("isDatalinkTable", isDatalinkTable+"");
		json.put("isDeploymentApp", isDeploymentApp+"");
		json.put("isBuiltin", isBuiltin+"");
		json.put("platformType", platformType+"");
		json.put("isCreatedResource", isCreatedResource+"");
		if(this.createTime != null){
			json.put(ResourceNameConstants.CREATE_TIME, this.createTime);
		}
		return json;
	}
}
