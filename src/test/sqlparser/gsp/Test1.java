package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;

public class Test1 {
	// update
	public static void main(String[] args) {
		EDbVendor dbDialect = EDbVendor.dbvmssql;
		TGSqlParser sqlParser = new TGSqlParser(dbDialect);
		sqlParser.sqltext = returnSql();
		sqlParser.parse();
		
		System.out.println(sqlParser.sqlstatements.get(0).sqlstatementtype);
	}
	private static String returnSql() {
		String sqls = "";sqls += "  declare @var_a int = 10\n"; 
		sqls += "  while( @var_a > 0)\n"; 
		sqls += "  begin\n"; 
		sqls += "      select @var_a\n"; 
		sqls += "      set @var_a=@var_a-1\n"; 
		sqls += "  end\n"; 
		return sqls;
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
