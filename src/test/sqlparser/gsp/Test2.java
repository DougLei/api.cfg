package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;

public class Test2 {
	public static void main(String[] args) {
		EDbVendor dbDialect = EDbVendor.dbvoracle;
		TGSqlParser parser = new TGSqlParser(dbDialect);
//		parser.sqltext = "select  t.*  as b from app_user where id in (select id from student where name = 1) and name = 1";
		parser.sqltext = "delete Student where id = 1";
		parser.parse();
		System.out.println(parser.getErrormessage());
		TStatementList list = parser.sqlstatements;
		while(list.hasNext()){
			
			System.out.println(list.next().sqlstatementtype);
			
			
//			TResultColumnList cl = selectSql.getResultColumnList();
//			System.out.println(cl.getResultColumn(0).getAliasClause());
//			System.out.println(cl.getResultColumn(0).getColumnNameOnly().equals("*"));
			
			
		}
		
		
	}
}
