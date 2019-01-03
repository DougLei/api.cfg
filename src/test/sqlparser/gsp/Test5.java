package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ETableSource;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TCTE;
import gudusoft.gsqlparser.nodes.TCTEList;
import gudusoft.gsqlparser.nodes.TOrderBy;
import gudusoft.gsqlparser.nodes.TOrderByItemList;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.nodes.TTableList;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class Test5 {
	public static void main(String[] args) {
		String sql = "select * from user u";
		EDbVendor dbDialect = EDbVendor.dbvmssql;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		System.err.println(parser.getErrormessage());
		
		TStatementList sqlStatementList = parser.sqlstatements;
		if(sqlStatementList != null){
			TSelectSqlStatement selectSqlStatement = (TSelectSqlStatement) sqlStatementList.get(0);
			TCTEList cteList = selectSqlStatement.getCteList();
			if(cteList != null){
				for(int i=0;i<cteList.size();i++){
					TCTE tcte = cteList.getCTE(i);
					TSelectSqlStatement s = (TSelectSqlStatement) tcte.getPreparableStmt();
					
					TOrderBy orderBy = s.getOrderbyClause();
					TOrderByItemList iteams = orderBy.getItems();
					for (int j=0;j<iteams.size();j++) {
						
						System.out.println("d  . name".equals(iteams.getOrderByItem(j).getSortKey().toString()));
						System.out.println(iteams.getOrderByItem(j).getSortKey());
						System.out.println(iteams.getOrderByItem(j).getSortOrder());
						System.out.println("---------------------------------------------------");
						
					}
					
				}
			}
			
			System.out.println(selectSqlStatement.getTables() == null);
			System.out.println(selectSqlStatement.getTables() );
			
			TTableList tables = selectSqlStatement.getTables();
			TTable table = null;
			for(int i=0;i<tables.size();i++){
				table = tables.getTable(i);
				if(table.getTableType() == ETableSource.subquery){
					System.out.println(table.getSubquery().getTables());
				}
			}
			
			
		}
		
		
	}
}
