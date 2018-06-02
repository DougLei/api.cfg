package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class Test4 {
	public static void main(String[] args) {
		String sql = "";
//		sql += "  with Temp as\n"; 
//		sql += "  (  \n"; 
//		sql += "    SELECT\n"; 
//		sql += "      ID, PARENT_ID, [ORDER], KIND,\n"; 
//		sql += "      Convert(VarChar(500), Name) AS NAME,\n"; 
//		sql += "      Convert(VarChar(500), SHORT_NAME) AS SHORT_NAME,\n"; 
//		sql += "      CODE, [LEVEL], [COMMENT], PLAT_CUSTOMER_ID, CREATE_TIME\n"; 
//		sql += "    FROM\n"; 
//		sql += "      ORG_NODE\n"; 
//		sql += "    WHERE\n"; 
//		sql += "      PARENT_ID = '~_PlatCustomerId~'\n"; 
//		sql += "  \n"; 
//		sql += "    UNION ALL\n"; 
//		sql += "  \n"; 
//		sql += "    SELECT\n"; 
//		sql += "      B.ID, B.PARENT_ID, [B].[ORDER], B.KIND,\n"; 
//		sql += "      Convert(VarChar(500), A.NAME + '|' + B.NAME) AS NAME,\n"; 
//		sql += "      Convert(VarChar(500), A.SHORT_NAME + '|' + B.SHORT_NAME) AS SHORT_NAME,\n"; 
//		sql += "      B.CODE, B.LEVEL, B.COMMENT, B.PLAT_CUSTOMER_ID, B.CREATE_TIME\n"; 
//		sql += "    FROM\n"; 
//		sql += "      Temp A inner join ORG_NODE B on A.ID = B.PARENT_ID\n"; 
//		sql += "  )  \n"; 
//		sql += "  select * from Temp\n"; 
		
		sql += "with Temp as (select name from b union select * from c) select * from temp where name=2" ;
//		sql +="select name from user";
		
		
		EDbVendor dbDialect = EDbVendor.dbvmssql;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		TStatementList list = parser.sqlstatements;
		while(list.hasNext()){
			TCustomSqlStatement sqlstatement = list.next();
			TSelectSqlStatement selectSql = (TSelectSqlStatement) sqlstatement;
			
			System.out.println(selectSql.toString());
			selectSql.addCondition("sex=? and age=?");
//			selectSql.addCondition("age=?");
			System.out.println(selectSql.toString());
		}
		
		
	}
}
