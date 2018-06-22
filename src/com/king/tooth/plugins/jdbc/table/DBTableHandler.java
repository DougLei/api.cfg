package com.king.tooth.plugins.jdbc.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.plugins.jdbc.util.DynamicDataLinkTableUtil;
import com.king.tooth.sys.entity.cfg.ComTabledata;
import com.king.tooth.sys.entity.common.ComDatabase;
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
	public DBTableHandler(ComDatabase database){
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
	 * @param isNeedInitBasicColumns 是否需要给table中加入基础列信息，比如id字段等【当建表和创建hbm文件两个功能同时执行时，这个字段会用到】
	 * @param tabledatas
	 */
	public void createTable(List<ComTabledata> tabledatas, boolean isNeedInitBasicColumns){
		String tmpSql = getCreateTableSql(tabledatas, isNeedInitBasicColumns);
		if(StrUtils.notEmpty(tmpSql)){
			String[] ddlSqlArr = tmpSql.split(";");
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
		String tmpSql = getDropTableSql(tabledatas);
		if(StrUtils.notEmpty(tmpSql)){
			String[] ddlSqlArr = tmpSql.split(";");
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
			StringBuilder createSql = new StringBuilder();
			for (ComTabledata tabledata : tabledatas) {
				if(isNeedInitBasicColumns){
					DynamicBasicDataColumnUtil.initBasicColumnToTable(tabledata);
				}
				tableOper.installCreateTableSql(tabledata);
				createSql.append(tableOper.getCreateTableSql()).append(";")
						 .append(tableOper.getCreateCommentSql()).append(";");
			}
			createSql.setLength(createSql.length() - 1);
			return createSql.toString();
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
			StringBuilder dropSql = new StringBuilder();
			// 处理主子表，如果有主子表数据，则要添加对应的关系表tabledata实例
			DynamicDataLinkTableUtil.processParentSubTable(tabledatas, false);
			for (ComTabledata tabledata : tabledatas) {
				dropSql.append(" drop table ").append(tabledata.getTableName()).append(";");
			}
			dropSql.setLength(dropSql.length() - 1);
			return dropSql.toString();
		}
		return null;
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
			Log4jUtil.debug("[DBTableHandler.executeDDL]操作数据表失败，异常信息为：{}", result);
		}
	}
}
