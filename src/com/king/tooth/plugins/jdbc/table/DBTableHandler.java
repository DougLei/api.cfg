package com.king.tooth.plugins.jdbc.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.table.impl.AbstractTableHandler;
import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.plugins.jdbc.util.DynamicDataLinkTableUtil;
import com.king.tooth.sys.builtin.data.BuiltinDatabaseData;
import com.king.tooth.sys.entity.cfg.CfgDatabase;
import com.king.tooth.sys.entity.cfg.ComColumndata;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.util.CloseUtil;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ReflectUtil;
import com.king.tooth.util.ResourceHandlerUtil;
import com.king.tooth.util.StrUtils;

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
	private AbstractTableHandler tableOper;
	
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
	
	
	/**
	 * 创建表
	 * @param tabledata
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return
	 */
	public List<ComTabledata> createTable(ComTabledata tabledata, boolean isNeedInitBasicColumns){
		List<ComTabledata> tabledatas = new ArrayList<ComTabledata>(2);
		tabledatas.add(tabledata);
		createTable(tabledatas, isNeedInitBasicColumns);
		return tabledatas;
	}
	
	/**
	 * 创建表
	 * @param tabledatas
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 */
	public void createTable(List<ComTabledata> tabledatas, boolean isNeedInitBasicColumns){
		String createTableSql = getCreateTableSql(tabledatas, isNeedInitBasicColumns);
		if(StrUtils.notEmpty(createTableSql)){
			String[] ddlSqlArr = createTableSql.split(";");
			executeDDL(ddlSqlArr, tabledatas);
		}
	}
	
	/**
	 * 删除表
	 * @param tabledata
	 * @return 返回被删除表的资源名，多个用,分割
	 */
	public String dropTable(ComTabledata tabledata){
		List<ComTabledata> tabledatas = new ArrayList<ComTabledata>(2);
		tabledatas.add(tabledata);
		String deleteTableResourceNames = dropTable(tabledatas);
		ResourceHandlerUtil.clearTables(tabledatas);
		return deleteTableResourceNames;
	}
	
	/**
	 * 删除表
	 * @param tabledatas
	 * @return 返回被删除表的资源名，多个用,分割
	 */
	public String dropTable(List<ComTabledata> tabledatas){
		String dropTableSql = getDropTableSql(tabledatas);
		if(StrUtils.notEmpty(dropTableSql)){
			String[] ddlSqlArr = dropTableSql.split(";");
			executeDDL(ddlSqlArr, tabledatas);
		}
		
		StringBuilder deleteTableResourceNames = new StringBuilder();
		for (ComTabledata table : tabledatas) {
			deleteTableResourceNames.append(table.getResourceName()).append(",");
		}
		deleteTableResourceNames.setLength(deleteTableResourceNames.length()-1);
		return deleteTableResourceNames.toString();
	}
	
	/**
	 * 执行操作数据表的ddlsql语句
	 * @param ddlSqlArr
	 */
	private void executeDDL(String[] ddlSqlArr, List<ComTabledata> tabledatas){
		String result = dblink.executeDDL(ddlSqlArr);
		if(result == null){
			Log4jUtil.debug("[DBTableHandler.executeDDL]操作数据表成功：{}", tabledatas);
		}else{
			Log4jUtil.warn("[DBTableHandler.executeDDL]操作数据表失败，异常信息为：{}", result);
		}
	}
	
	/**
	 * 获得创建表的sql
	 * @param tabledatas
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @return
	 */
	public String getCreateTableSql(List<ComTabledata> tabledatas, boolean isNeedInitBasicColumns){
		if(tableOper == null){
			Log4jUtil.debug("[DBTableHandler.createSql]操作表的对象AbstractTableOper tableOper为null");
			return null;
		}
		if(tabledatas != null && tabledatas.size() > 0){
			// 处理主子表，如果有主子表数据，则要添加对应的关系表tabledata实例
			DynamicDataLinkTableUtil.processParentSubTable(tabledatas, true);
			StringBuilder createTableSql = new StringBuilder();
			for (ComTabledata tabledata : tabledatas) {
				if(isNeedInitBasicColumns){
					DynamicBasicDataColumnUtil.initBasicColumnToTable(tabledata);
				}
				tableOper.installCreateTableSql(tabledata);
				createTableSql.append(tableOper.getCreateTableSql()).append(";")
						 	  .append(tableOper.getCreateCommentSql()).append(";")
						 	  .append(tableOper.getOperColumnSql()).append(";");
			}
			createTableSql.setLength(createTableSql.length() - 1);
			return createTableSql.toString();
		}
		return null;
	}
	
	/**
	 * 获得删除表的sql
	 * @param tableNames
	 * @return
	 */
	public String getDropTableSql(List<ComTabledata> tabledatas){
		if(tabledatas != null && tabledatas.size() > 0){
			StringBuilder dropTableSql = new StringBuilder();
			// 处理主子表，如果有主子表数据，则要添加对应的关系表tabledata实例
			DynamicDataLinkTableUtil.processParentSubTable(tabledatas, false);
			List<String> tableNames = filterTableIsExists(tabledatas);
			if(tableNames!= null && tableNames.size() > 0){
				for (String tn: tableNames) {
					dropTableSql.append(" drop table ").append(tn).append(";");
				}
				dropTableSql.setLength(dropTableSql.length() - 1);
			}
			return dropTableSql.toString();
		}
		return null;
	}
	/**
	 * 筛选出存在的表
	 * <p>目前这个方法配合getDropTableSql(List<ComTabledata> tabledatas)方法使用，防止删除不存在的表</p>
	 * @param tabledatas
	 * @return tableNames
	 */
	private List<String> filterTableIsExists(List<ComTabledata> tabledatas) {
		List<String> tableNames = new ArrayList<String>(tabledatas.size());
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
			for (ComTabledata table : tabledatas) {
				pst.setString(1, table.getTableName());
				rs = pst.executeQuery();
				if(rs.next() && (rs.getInt(1) > 0)){
					tableNames.add(table.getTableName());
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}finally{
			CloseUtil.closeDBConn(rs, pst, conn);
		}
		return tableNames;
	}
	
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

	/**
	 * 修改列
	 * @param tableName
	 * @param columns
	 * @param removeDeleteColumn 是否从集合中移除被删除的列
	 */
	public void modifyColumn(String tableName, List<ComColumndata> columns, boolean removeDeleteColumn){
		ComColumndata column;
		for (int i = 0; i < columns.size(); i++) {
			column = columns.get(i);
			if(column.getOperStatus() == ComColumndata.UN_CREATED){
				tableOper.installCreateColumnSql(tableName, column);
			}else if(column.getOperStatus() == ComColumndata.MODIFIED){
				tableOper.installModifyColumnSql(tableName, column);
			}else if(column.getOperStatus() == ComColumndata.DELETED){
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
}
