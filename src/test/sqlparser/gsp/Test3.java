package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class Test3 {
	public static void main(String[] args) {
		EDbVendor dbDialect = EDbVendor.dbvoracle;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = "   ddd";
		parser.parse();
		System.out.println(parser.getErrormessage());
		
		TStatementList list = parser.sqlstatements;
		System.out.println(list.size());
		while(list.hasNext()){
			TCustomSqlStatement sqlstatement = list.next();
			TSelectSqlStatement selectSql = (TSelectSqlStatement) sqlstatement;
			System.out.println(selectSql.getTargetTable());
			
			System.out.println(selectSql.isCombinedQuery());
			
			System.out.println(selectSql.getLeftStmt());;
			System.out.println(selectSql.getResultColumnList());
		}
		
		
	}
}
