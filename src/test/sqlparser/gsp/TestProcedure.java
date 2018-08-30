package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;

public class TestProcedure {
	public static void main(String[] args) {
		String sql = getSql();
		
		EDbVendor dbDialect = EDbVendor.dbvoracle;
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
		sqls += "  create or replace procedure outTable(inage in number, age out number, dataset out sys_refcursor)\n"; 
		sqls += "  as\n"; 
		sqls += "  begin\n"; 
		sqls += "      age:=inage+10;\n"; 
		sqls += "      open dataset for\n"; 
		sqls += "           select * from sys_user where id = '1';\n"; 
		sqls += "  end;\n"; 


		return sqls;
	}
}
