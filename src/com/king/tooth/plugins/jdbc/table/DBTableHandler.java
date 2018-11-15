package com.king.tooth.plugins.jdbc.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.table.impl.ATableHandler;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.CfgColumn;
import com.king.tooth.sys.entity.cfg.CfgTable;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ReflectUtil;
import com.king.tooth.util.StrUtils;
import com.king.tooth.util.build.model.DynamicBasicColumnUtil;

/**
 * 对数据库表的操作
 * 创建/删除
 * @author DougLei
 */
public class DBTableHandler {
	
	/**
	 * 数据库链接对象
	 */
	private DBLink dblink;
	/**
	 * 数据表的操作对象
	 * 创建/删除
	 */
	private ATableHandler tableOper;
	
	/**
	 * 构造函数
	 * @param database 数据库对象 
	 */
	public DBTableHandler(CfgDatabase database){
		dblink = new DBLink(database);
		newAbstractCreateTableInstance();
	}
	
	/**
	 * 创建AbstractTable的实例
	 * @return
	 */
	private void newAbstractCreateTableInstance() {
		tableOper = ReflectUtil.newInstance(basePackage + dblink.getDBType() + implClassname);
		if(tableOper == null){
			Log4jUtil.debug("创建表时，配置文件配置的dbtype错误，请检查值是否与系统要求的值完全一致：[{}]", Arrays.toString(dblink.getDatabaseTypes()));
		}
	}
	private static final String basePackage = "com.king.tooth.plugins.jdbc.table.impl.";
	private static final String implClassname = ".TableImpl";
	
	// ----------------------------------------------------------------------------
	/**
	 * 创建表
	 * @param tabledata
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return
	 */
	public void createTable(CfgTable tabledata, boolean isNeedInitBasicColumns){
		String createTableSql = getCreateTableSql(tabledata, isNeedInitBasicColumns);
		if(StrUtils.notEmpty(createTableSql)){
			executeDDL(createTableSql);
		}
	}
	
	/**
	 * 批量创建表
	 * @param tabledatas
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 */
	public void batchCreateTable(List<CfgTable> tabledatas, boolean isNeedInitBasicColumns){
		String createTableSql = getBatchCreateTableSql(tabledatas, isNeedInitBasicColumns);
		if(StrUtils.notEmpty(createTableSql)){
			String[] ddlSqlArr = createTableSql.split(";");
			executeDDL(ddlSqlArr);
		}
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * 删除表
	 * @param table
	 * @return 返回被删除表的资源名
	 */
	public String dropTable(CfgTable table){
		String dropTableSql = getDropTableSql(table);
		if(StrUtils.notEmpty(dropTableSql)){
			executeDDL(dropTableSql);
		}
		return table.getTableName();
	}
	
	/**
	 * 批量删除表
	 * @param tables
	 * @return 返回被删除表的资源名，多个用,分割
	 */
	public String batchDropTable(List<CfgTable> tables){
		String dropTableSql = getBatchDropTableSql(tables);
		if(StrUtils.notEmpty(dropTableSql)){
			String[] ddlSqlArr = dropTableSql.split(";");
			executeDDL(ddlSqlArr);
		}
		
		StringBuilder deleteTableResourceNames = new StringBuilder();
		for (CfgTable table : tables) {
			deleteTableResourceNames.append(table.getResourceName()).append(",");
		}
		deleteTableResourceNames.setLength(deleteTableResourceNames.length()-1);
		return deleteTableResourceNames.toString();
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * 执行操作数据表的ddlsql语句
	 * @param ddlSqlArr
	 */
	private void executeDDL(String... ddlSqlArr){
		String result = dblink.executeDDL(ddlSqlArr);
		if(result == null){
			Log4jUtil.debug("[DBTableHandler.executeDDL]操作数据表成功");
		}else{
			Log4jUtil.warn("[DBTableHandler.executeDDL]操作数据表失败，异常信息为：{}", result);
		}
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * 获得创建表的sql
	 * @param table
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return
	 */
	private String getCreateTableSql(CfgTable table, boolean isNeedInitBasicColumns){
		if(tableOper == null){
			Log4jUtil.debug("[DBTableHandler.createSql]操作表的对象ATableOper tableOper为null");
			return null;
		}
		if(isNeedInitBasicColumns){
			DynamicBasicColumnUtil.initBasicColumnToTable(table);
		}
		tableOper.installCreateTableSql(table);
		return tableOper.getCreateTableSql() + ";" + tableOper.getCreateCommentSql() + ";" + tableOper.getOperColumnSql();
	}
	
	/**
	 * 获得批量创建表的sql
	 * @param tables
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return
	 */
	private String getBatchCreateTableSql(List<CfgTable> tables, boolean isNeedInitBasicColumns){
		if(tableOper == null){
			Log4jUtil.debug("[DBTableHandler.createSql]操作表的对象ATableOper tableOper为null");
			return null;
		}
		if(tables != null && tables.size() > 0){
			StringBuilder createTableSql = new StringBuilder();
			for (CfgTable table : tables) {
				createTableSql.append(getCreateTableSql(table, isNeedInitBasicColumns)).append(";");
			}
			createTableSql.setLength(createTableSql.length() - 1);
			return createTableSql.toString();
		}
		return null;
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * 获得删除表的sql
	 * @param table
	 * @return
	 */
	public String getDropTableSql(CfgTable table){
		List<CfgTable> tables = new ArrayList<CfgTable>(1);
		tables.add(table);
		
		List<String> tableNames = filterTableIsExists(tables);
		tables.clear();
		
		if(tableNames!= null && tableNames.size() > 0){
			tableNames.clear();
			return "drop table " + table.getTableName();
		}
		return null;
	}
	/**
	 * 获得批量删除表的sql
	 * @param tables
	 * @return
	 */
	public String getBatchDropTableSql(List<CfgTable> tables){
		if(tables != null && tables.size() > 0){
			StringBuilder dropTableSql = new StringBuilder();
			List<String> tableNames = filterTableIsExists(tables);
			if(tableNames!= null && tableNames.size() > 0){
				for (String tn: tableNames) {
					dropTableSql.append(" drop table ").append(tn).append(";");
				}
				dropTableSql.setLength(dropTableSql.length() - 1);
				tableNames.clear();
			}
			return dropTableSql.toString();
		}
		return null;
	}
	/**
	 * 筛选出存在的表
	 * <p>目前这个方法配合getDropTableSql(List<CfgTable> tables)方法使用，防止删除不存在的表</p>
	 * @param tables
	 * @return tableNames
	 */
	private List<String> filterTableIsExists(List<CfgTable> tables) {
		int size = tables.size();
		String[] tableNameArr = new String[size];
		for(int i=0;i<size;i++){
			tableNameArr[i] = tables.get(i).getTableName();
		}
		return filterTable(true, tableNameArr);
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * 筛选表在库中是否存在
	 * @param isExists true:筛选参数tableNameArr传入的表名数组，在数据库中已经存在的都有哪些，返回回来、false:反之
	 * @param tableNameArr
	 * @return 表名集合
	 */
	public List<String> filterTable(boolean isExists, String... tableNameArr) {
		if(tableNameArr == null || tableNameArr.length == 0){
			return null;
		}
		
		List<String> tableNames = new ArrayList<String>(tableNameArr.length);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = dblink.getConnection();
			if(dblink.isOracle()){
				pst = conn.prepareStatement(BuiltinDatabaseData.oracle_queryObjectIsExistsSql);
				pst.setString(2, "TABLE");
			}else if(dblink.isSqlServer()){
				pst = conn.prepareStatement(BuiltinDatabaseData.sqlserver_queryObjectIsExistsSql);
				pst.setString(2, "U");
			}
			for (String tableName : tableNameArr) {
				pst.setString(1, tableName);
				rs = pst.executeQuery();
				if(isExists){
					if(rs.next() && (rs.getInt(1) > 0)){
						tableNames.add(tableName);
					}
				}else{
					if(!rs.next() || (rs.getInt(1) == 0)){
						tableNames.add(tableName);
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}finally{
			CloseUtil.closeDBConn(rs, pst, conn);
		}
		return tableNames;
	}

	// ----------------------------------------------------------------------------
	/**
	 * 修改表名
	 * @param tableName
	 * @param oldTableName
	 */
	public void reTableName(String tableName, String oldTableName) {
		String reTableNameSql = tableOper.getReTableNameSql(tableName, oldTableName);
		if(StrUtils.notEmpty(reTableNameSql)){
			String[] sqlArr = {reTableNameSql};
			dblink.executeDDL(sqlArr);
		}
	}
	
	/**
	 * 修改列
	 * @param tableName
	 * @param columns
	 */
	public void modifyColumn(String tableName, CfgColumn column){
		if(column.getOperStatus() == CfgColumn.UN_CREATED){
			tableOper.installCreateColumnSql(tableName, column);
		}else if(column.getOperStatus() == CfgColumn.MODIFIED){
			tableOper.installModifyColumnSql(tableName, column);
		}else if(column.getOperStatus() == CfgColumn.DELETED){
			tableOper.installDeleteColumnSql(tableName, column);
		}
		
		String operColumnSql = tableOper.getOperColumnSql();
		if(StrUtils.notEmpty(operColumnSql)){
			String[] ddlSqlArr = operColumnSql.split(";");
			dblink.executeDDL(ddlSqlArr);
		}
	}
	
	/**
	 * 修改列
	 * @param tableName
	 * @param columns
	 * @param removeDeleteColumn 是否从集合中移除被删除的列
	 */
	public void modifyColumns(String tableName, List<CfgColumn> columns, boolean removeDeleteColumn){
		CfgColumn column;
		for (int i = 0; i < columns.size(); i++) {
			column = columns.get(i);
			if(column.getOperStatus() == CfgColumn.UN_CREATED){
				tableOper.installCreateColumnSql(tableName, column);
			}else if(column.getOperStatus() == CfgColumn.MODIFIED){
				tableOper.installModifyColumnSql(tableName, column);
			}else if(column.getOperStatus() == CfgColumn.DELETED){
				tableOper.installDeleteColumnSql(tableName, column);
				if(removeDeleteColumn){
					columns.remove(i);
					i--;
				}
			}
		}
		
		String operColumnSql = tableOper.getOperColumnSql();
		if(StrUtils.notEmpty(operColumnSql)){
			String[] ddlSqlArr = operColumnSql.split(";");
			dblink.executeDDL(ddlSqlArr);
		}
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * 创建表类型对象
	 * @param table
	 */
	public void createTableDataType(CfgTable table) {
		String createTableDataTypeSql = getCreateTableDataTypeSql(table);
		if(StrUtils.notEmpty(createTableDataTypeSql)){
			String[] sqlArr = {createTableDataTypeSql};
			dblink.executeDDL(sqlArr);
		}
	}

	/**
	 * 获取创建表类型的sql
	 * @param table
	 * @return
	 */
	private String getCreateTableDataTypeSql(CfgTable table){
		if(tableOper == null){
			Log4jUtil.debug("[DBTableHandler.getCreateTableDataTypeSql]操作表的对象ATableOper tableOper为null");
			return null;
		}
		tableOper.installCreateTableDataTypeSql(table);
		return tableOper.getOperTableDataTypeSql();
	}
	
	// ----------------------------------------------------------------------------
	
	/**
	 * 删除表类型对象
	 * @param table
	 */
	public void dropTableDataType(CfgTable table) {
		String dropTableDataTypeSql = getDropTableDataTypeSql(table);
		if(StrUtils.notEmpty(dropTableDataTypeSql)){
			String[] sqlArr = {dropTableDataTypeSql};
			dblink.executeDDL(sqlArr);
		}
	}
	
	/**
	 * 获取删除表类型的sql
	 * @param table
	 * @return
	 */
	private String getDropTableDataTypeSql(CfgTable table){
		if(tableOper == null){
			Log4jUtil.debug("[DBTableHandler.getDropTableDataTypeSql]操作表的对象ATableOper tableOper为null");
			return null;
		}
		tableOper.installDropTableDataTypeSql(table);
		return tableOper.getOperTableDataTypeSql();
	}
}
