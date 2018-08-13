package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.stmt.TCreateViewSqlStatement;

public class Test1 {
	// update
	public static void main(String[] args) {
		EDbVendor dbDialect = EDbVendor.dbvoracle;
		TGSqlParser sqlParser = new TGSqlParser(dbDialect);
		sqlParser.sqltext = "create or replace view viewNames as select * from COM_COLUMNDATA ";
		sqlParser.parse();
		
		System.out.println(sqlParser.sqlstatements.get(0).sqlstatementtype);
		
		
		TCreateViewSqlStatement tvss = (TCreateViewSqlStatement) sqlParser.sqlstatements.get(0);
		System.out.println(tvss.getViewName().toString());
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
