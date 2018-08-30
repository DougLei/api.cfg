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
		System.out.println(sqlList.size());
		
		for(int i=0;i<sqlList.size();i++){
			System.out.println(sqlList.get(i).sqlstatementtype);
		}
		
		
	}

	private static String getSql() {
		String sqls = "";
		sqls += "  create type accountType as table(\n"; 
		sqls += "  	id varchar(32),\n"; 
		sqls += "  	name varchar(30)\n"; 
		sqls += "  )\n"; 
		sqls += "  go\n"; 
		sqls += "  \n"; 
		sqls += "  create proc inTable(@accountTable accountType readonly)\n"; 
		sqls += "  as\n"; 
		sqls += "   select * from @accountTable;\n"; 
		sqls += "  go\n"; 
		return sqls;
	}
}
