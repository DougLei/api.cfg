package com.king.tooth.sys.entity.cfg;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.king.tooth.annotation.Table;
import com.king.tooth.constants.DataTypeConstants;
import com.king.tooth.constants.ResourceInfoConstants;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.builtin.data.BuiltinObjectInstance;
import com.king.tooth.sys.entity.IEntity;
import com.king.tooth.sys.entity.IEntityPropAnalysis;
import com.king.tooth.sys.entity.sys.SysResource;
import com.king.tooth.util.NamingProcessUtil;
import com.king.tooth.util.StrUtils;

/**
 * 表信息表
 * @author DougLei
 */
@SuppressWarnings("serial")
@Table
public class CfgTable extends ASysResource implements IEntityPropAnalysis, IEntity{
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
	 * 表类型：1:单表、2、表类型/游标类型
	 * <p>默认值:1</p>
	 */
	private Integer type;
	/**
	 * 注释
	 */
	private String comments;
	/**
	 * 是否建模
	 * <p>默认值为0</p>
	 * <p>该字段在建模时，值改为1，后续修改字段信息等，该值改为0，用来标识是否建模，是否需要alter table xxx</p>
	 */
	private Integer isBuildModel;
	
	//-----------------------------------------------------------------------
	
	/**
	 * 是否内置
	 * <p>代码开发的都为内置，其余的都不是</p>
	 */
	@JSONField(serialize = false)
	private int isBuildIn;
	
	/**
	 * 列集合
	 */
	@JSONField(serialize = false)
	private List<CfgColumn> columns;
	/**
	 * 数据库类型
	 * 目前主要是判断，如果是oracle数据库时，要判断表名长度不能超过30个字符
	 */
	@JSONField(serialize = false)
	private String dbType;

	public CfgTable() {
	}
	public CfgTable(String tableName) {
		this.tableName = tableName.trim().toUpperCase();
		this.resourceName = NamingProcessUtil.tableNameTurnClassName(tableName);
		this.isCreated = 1;
		this.isBuildModel = 1;
		this.isBuildIn = 1;
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
	public List<CfgColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<CfgColumn> columns) {
		this.columns = columns;
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
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getIsBuildModel() {
		return isBuildModel;
	}
	public void setIsBuildModel(Integer isBuildModel) {
		this.isBuildModel = isBuildModel;
	}
	public int getIsBuildIn() {
		return isBuildIn;
	}
	
	/**
	 * 清空columns
	 */
	public void clear(){
		if(columns != null && columns.size()>0){
			columns.clear();
		}
	}
	
	@JSONField(serialize = false)
	public List<CfgColumn> getColumnList() {
		List<CfgColumn> columns = new ArrayList<CfgColumn>(10+7);
		
		CfgColumn nameColumn = new CfgColumn("name", DataTypeConstants.STRING, 100);
		nameColumn.setName("显示的汉字名称");
		nameColumn.setComments("显示的汉字名称");
		columns.add(nameColumn);
		
		CfgColumn tableNameColumn = new CfgColumn("table_name", DataTypeConstants.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			tableNameColumn.setLength(30);
		}
		tableNameColumn.setName("表名");
		tableNameColumn.setIsNullabled(0);
		tableNameColumn.setComments("表名");
		columns.add(tableNameColumn);
		
		CfgColumn oldTableNameColumn = new CfgColumn("old_table_name", DataTypeConstants.STRING, 80);
		if(BuiltinDatabaseData.DB_TYPE_ORACLE.equals(dbType)){
			oldTableNameColumn.setLength(30);
		}
		oldTableNameColumn.setName("旧表名");
		oldTableNameColumn.setComments("如果修改了表名，这里记录之前的表名");
		columns.add(oldTableNameColumn);
		
		columns.add(BuiltinObjectInstance.resourceNameColumn);
		
		CfgColumn typeColumn = new CfgColumn("type", DataTypeConstants.INTEGER, 1);
		typeColumn.setName("表类型");
		typeColumn.setComments("1:单表、2、表类型/游标类型");
		typeColumn.setDefaultValue("1");
		columns.add(typeColumn);
		
		CfgColumn commentsColumn = new CfgColumn("comments", DataTypeConstants.STRING, 200);
		commentsColumn.setName("注释");
		commentsColumn.setComments("注释");
		columns.add(commentsColumn);
		
		columns.add(BuiltinObjectInstance.isCreatedColumn);
		
		CfgColumn isBuildModelColumn = new CfgColumn("is_build_model", DataTypeConstants.INTEGER, 1);
		isBuildModelColumn.setName("是否建模");
		isBuildModelColumn.setComments("默认值为0，该字段在建模时，值改为1，后续修改字段信息等，该值改为0，用来标识是否建模，是否需要alter table xxx");
		isBuildModelColumn.setDefaultValue("0");
		columns.add(isBuildModelColumn);
		
		columns.add(BuiltinObjectInstance.isEnabledColumn);
		columns.add(BuiltinObjectInstance.requestMethodColumn);
		
		return columns;
	}
	
	public CfgTable toCreateTable() {
		CfgTable table = new CfgTable(toDropTable());
		table.setName("表信息表");
		table.setComments("表信息表");
		
		table.setColumns(getColumnList());
		return table;
	}

	public String toDropTable() {
		return "CFG_TABLE";
	}
	
	@JSONField(serialize = false)
	public String getEntityName() {
		return "CfgTable";
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
			if(StrUtils.isEmpty(requestMethod)){
				requestMethod = ResourceInfoConstants.ALL;
			}
		}
		return result;
	}
	
	public SysResource turnToResource() {
		SysResource resource = super.turnToResource();
		resource.setResourceType(ResourceInfoConstants.TABLE);
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
