package com.king.tooth.plugins.jdbc.table;

import java.util.Arrays;
import java.util.List;

import com.king.tooth.plugins.jdbc.DBLink;
import com.king.tooth.plugins.jdbc.util.DynamicBasicDataColumnUtil;
import com.king.tooth.plugins.jdbc.util.DynamicDataLinkTableUtil;
import com.king.tooth.sys.entity.cfg.CfgTabledata;
import com.king.tooth.sys.entity.common.ComDatabase;
import com.king.tooth.util.Log4jUtil;
import com.king.tooth.util.ReflectUtil;
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
	 * @param tabledatas
	 */
	public void createTable(List<CfgTabledata> tabledatas){
		String tmpSql = getCreateTableSql(tabledatas);
		if(StrUtils.notEmpty(tmpSql)){
			String[] ddlSqlArr = tmpSql.split(";");
			executeDDL(ddlSqlArr, tabledatas);
		}
	}
	
	/**
	 * 删除表
	 * @param tabledatas
	 */
	public void dropTable(List<CfgTabledata> tabledatas){
		String tmpSql = getDropTableSql(tabledatas);
		if(StrUtils.notEmpty(tmpSql)){
			String[] ddlSqlArr = tmpSql.split(";");
			executeDDL(ddlSqlArr, tabledatas);
		}
	}
	
	
	/**
	 * 获得创建表的sql
	 * @param tabledatas
	 * @return
	 */
	public String getCreateTableSql(List<CfgTabledata> tabledatas){
		if(tableOper == null){
			Log4jUtil.debug("[DBTableHandler.createSql]操作表的对象AbstractTableOper tableOper为null");
			return null;
		}
		if(tabledatas != null && tabledatas.size() > 0){
			// 处理主子表，如果有主子表数据，则要添加对应的关系表tabledata实例
			DynamicDataLinkTableUtil.processParentSubTable(tabledatas);
			
			StringBuilder createSql = new StringBuilder();
			for (CfgTabledata tabledata : tabledatas) {
				DynamicBasicDataColumnUtil.initBasicColumnToTable(tabledata);
				
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
	public String getDropTableSql(List<CfgTabledata> tabledatas){
		if(tabledatas != null && tabledatas.size() > 0){
			StringBuilder dropSql = new StringBuilder();

			// 处理主子表，如果有主子表数据，则要添加对应的关系表tabledata实例
			DynamicDataLinkTableUtil.processParentSubTable(tabledatas);
			
			for (CfgTabledata tabledata : tabledatas) {
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
	private void executeDDL(String[] ddlSqlArr, List<CfgTabledata> tabledatas){
		String result = dblink.executeDDL(ddlSqlArr);
		if(result == null){
			Log4jUtil.debug("[DBTableHandler.executeDDL]操作数据表成功：{}", tabledatas);
		}else{
			Log4jUtil.debug("[DBTableHandler.executeDDL]操作数据表失败，异常信息为：{}", result);
		}
	}
}
