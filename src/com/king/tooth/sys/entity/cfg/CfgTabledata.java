package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.DynamicDataConstants;
import com.king.tooth.constants.ResourceNameConstants;
import com.king.tooth.sys.entity.AbstractSysResource;
import com.king.tooth.sys.entity.ITable;
import com.king.tooth.util.NamingTurnUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.hibernate.HibernateUtil;

/**
 * [配置系统]表数据信息资源对象
 * @author DougLei
 */
@SuppressWarnings("serial")
public class CfgTabledata extends AbstractSysResource implements ITable{
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
	/**
	 * 是否内置
	 */
	private int isBuiltin;
	
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
		if(DynamicDataConstants.DB_TYPE_ORACLE.equals(dbType) && tableName.length() > 30){
			throw new IllegalAccessError("oracle数据库的表名长度不能超过30个字符");
		}
		doSetTableName(dbType, tableName, 0);
	}
	
	/**
	 * 目前，只让DynamicDataLinkTableUtil.getDataLinkTabledata()方法使用到这个构造函数
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
			// oracle的表名长度不能超过30个字符
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
		if(StrUtils.notEmpty(tableName)){
			this.resourceName = NamingTurnUtil.tableNameTurnClassName(tableName);
		}
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
	public String getSubRefParentColumnName() {
		return subRefParentColumnName;
	}
	public void setSubRefParentColumnName(String subRefParentColumnName) {
		this.subRefParentColumnName = subRefParentColumnName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getIsDeploymentTest() {
		return isDeploymentTest;
	}
	public void setIsDeploymentTest(int isDeploymentTest) {
		this.isDeploymentTest = isDeploymentTest;
	}
	public int getIsDeploymentRun() {
		return isDeploymentRun;
	}
	public void setIsDeploymentRun(int isDeploymentRun) {
		this.isDeploymentRun = isDeploymentRun;
	}
	public int getIsBuiltin() {
		return isBuiltin;
	}
	public void setIsBuiltin(int isBuiltin) {
		this.isBuiltin = isBuiltin;
	}
	
	
	public CfgTabledata toCreateTable(String dbType) {
		CfgTabledata table = new CfgTabledata(dbType, "CFG_TABLEDATA");
		table.setName("[配置系统]表数据信息资源对象表");
		table.setComments("[配置系统]表数据信息资源对象表");
		
		List<CfgColumndata> columns = new ArrayList<CfgColumndata>(19);
		
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
		
		CfgColumndata isDeploymentTestColumn = new CfgColumndata("is_deployment_test");
		isDeploymentTestColumn.setName("是否部署到测试环境");
		isDeploymentTestColumn.setComments("是否部署到测试环境");
		isDeploymentTestColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentTestColumn.setLength(1);
		isDeploymentTestColumn.setOrderCode(12);
		columns.add(isDeploymentTestColumn);

		CfgColumndata isDeploymentRunColumn = new CfgColumndata("is_deployment_run");
		isDeploymentRunColumn.setName("是否部署到正式环境");
		isDeploymentRunColumn.setComments("是否部署到正式环境");
		isDeploymentRunColumn.setColumnType(DataTypeConstants.INTEGER);
		isDeploymentRunColumn.setLength(1);
		isDeploymentRunColumn.setOrderCode(13);
		columns.add(isDeploymentRunColumn);
		
		CfgColumndata isBuiltinColumn = new CfgColumndata("is_builtin");
		isBuiltinColumn.setName("是否内置");
		isBuiltinColumn.setComments("是否内置");
		isBuiltinColumn.setColumnType(DataTypeConstants.INTEGER);
		isBuiltinColumn.setLength(1);
		isBuiltinColumn.setOrderCode(14);
		columns.add(isBuiltinColumn);
		
		table.setColumns(columns);
		return table;
	}

	public String toDropTable() {
		return "CFG_TABLEDATA";
	}
	
	public int getResourceType() {
		return TABLE;
	}
}
