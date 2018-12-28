package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TCTE;
import gudusoft.gsqlparser.nodes.TCTEList;
import gudusoft.gsqlparser.nodes.TOrderBy;
import gudusoft.gsqlparser.nodes.TOrderByItemList;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class Test5 {
	public static void main(String[] args) {
		String sql = "with  a   as (select top 1 * from student order by d  . name desc, size),    b    as(select * from b  order by d asc)   select *,name from Student,DevToolController s left join classes c on s.id=c.id and c.sex = $sex$ and d=1 right join classesr c on (s.id=c.id and c.sex = $sex$) inner join classesr c on (s.id=c.id and c.sex = $sex$) where name = $name$ and test.id = student.id order by test desc";
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
				System.out.println(cteList);
			}
			System.out.println("************************************************************");
			
			System.out.println(selectSqlStatement.getOrderbyClause());
//			System.out.println(selectSqlStatement.getLeftStmt().getResultColumnList());
		}
		
		
	}
}
