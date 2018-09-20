package test.sqlparser.gsp;

import com.king.tooth.util.StrUtils;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TResultColumnList;
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
		
		sql += "SELECT\n"; 
		sql += "      C.*, *,B.ID, B.PARENT_ID, [B].[ORDER], B.KIND,\n"; 
		sql += "      Convert(VarChar(500), A.NAME + '|' + B.NAME),\n"; 
		sql += "      Convert(VarChar(500), A.SHORT_NAME + '|' + B.SHORT_NAME) AS SHORT_NAME,\n"; 
		sql += "      B.CODE, B.LEVEL, B.COMMENT, B.PLAT_CUSTOMER_ID, B.CREATE_TIME\n"; 
		sql += "    FROM\n"; 
		sql += "      C A inner join ORG_NODE B on A.ID = B.PARENT_ID\n"; 
//		sql += "with Temp as (select name from b union select * from c) select age from temp where name=2 union select sex from sys_user" ;
//		sql +="with Temp as (select name from b union select * from c)  select sex z, sex from user union select name, name from user union select zzz, zzz from user";
//		sql +="select sex z, sex from user";
		
		
		EDbVendor dbDialect = EDbVendor.dbvmssql;
//		EDbVendor dbDialect = EDbVendor.dbvoracle;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		
		TSelectSqlStatement selectSqlStatement = (TSelectSqlStatement) parser.sqlstatements.get(0);
		
		if (selectSqlStatement.isCombinedQuery()) {
			selectSqlStatement = selectSqlStatement.getLeftStmt();
			
			if (selectSqlStatement.isCombinedQuery()) {
				selectSqlStatement = selectSqlStatement.getLeftStmt();
			}
			
			TResultColumnList columns = selectSqlStatement.getResultColumnList();
			for(int i=0;i<columns.size();i++){
				TResultColumn column = columns.getResultColumn(i);
				String columnName = column.getAliasClause() == null ? column.getColumnNameOnly() : column.getColumnAlias();
				System.out.println(columnName);
			}
		}else{
			TResultColumnList columns = selectSqlStatement.getResultColumnList();
			for(int i=0;i<columns.size();i++){
				TResultColumn column = columns.getResultColumn(i);
				String columnName = column.getAliasClause() == null ? column.getColumnNameOnly() : column.getColumnAlias();
				
				if(columnName == null){
					System.out.println("columnName == null:" + column);
				}
				if(StrUtils.isEmpty(columnName)){
					System.out.println("columnName is empty:" + column);
				}
				System.out.println(columnName);
			}
		}
		
		
		
	}
}
