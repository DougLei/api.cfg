package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;

public class Test1 {
	// update
	public static void main(String[] args) {
		EDbVendor oracle = EDbVendor.dbvmssql;
		TGSqlParser sqlParser = new TGSqlParser(oracle);
		sqlParser.sqltext = returnSql();
		sqlParser.parse();
		
		TStatementList tl = sqlParser.sqlstatements;
		TCustomSqlStatement sql = tl.get(0);
		
		TUpdateSqlStatement update = (TUpdateSqlStatement) sql;
		System.out.println(update.getResultColumnList());
		System.out.println(update.getWhereClause().getCondition().getSubQuery());
		
	}
	private static String returnSql() {
		String sql = "";
		sql += "update Student set name = '1', age = $age$ where id = $id$ and addr = (select addr from address where id = $aid$)"; 
		return sql;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// insert
//	public static void main(String[] args) {
//		EDbVendor oracle = EDbVendor.dbvmssql;
//		TGSqlParser sqlParser = new TGSqlParser(oracle);
//		sqlParser.sqltext = returnSql();
//		sqlParser.parse();
//		
//		TStatementList tl = sqlParser.sqlstatements;
//		TCustomSqlStatement sql = tl.get(0);
//		
//		TInsertSqlStatement insert = (TInsertSqlStatement) sql;
//		TMultiTargetList values = insert.getValues();
//		
//		TResultColumn trc = values.getMultiTarget(0).getColumnList().getResultColumn(3);
//		
//	}
//	private static String returnSql() {
//		String sql = "";
//		sql += "insert into student values($name$,$age$,$sex$,(select addr from address where add = $add$))"; 
//		return sql;
//	}
}
