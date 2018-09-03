package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;

public class TestProcedure {
	public static void main(String[] args) {
		String sql = getSql();
		
		EDbVendor dbDialect = EDbVendor.dbvmssql;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		
		TStatementList sqlList = parser.sqlstatements;
		System.out.println(sqlList.get(0).sqlstatementtype);
		
	}

	private static String getSql() {
		String sqls = "";
		sqls += "  create type accountType as table(\n"; 
		sqls += "  	id varchar(32),\n"; 
		sqls += "  	birthday datetime,\n"; 
		sqls += "  	age int\n"; 
		sqls += "  )\n"; 








		return sqls;
	}
}
