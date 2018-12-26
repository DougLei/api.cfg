package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class Test5 {
	public static void main(String[] args) {
		String sql = "with     a    as (select * from student),    b    as(select * from b  )   select *,name from Student,DevToolController s left join classes c on s.id=c.id and c.sex = $sex$ and d=1 right join classesr c on (s.id=c.id and c.sex = $sex$) inner join classesr c on (s.id=c.id and c.sex = $sex$) where name = $name$ and test.id = student.id union select * from student";
		EDbVendor dbDialect = EDbVendor.dbvmssql;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		System.err.println(parser.getErrormessage());
		
		TStatementList sqlStatementList = parser.sqlstatements;
		if(sqlStatementList != null){
			TSelectSqlStatement selectSqlStatement = (TSelectSqlStatement) sqlStatementList.get(0);
			if(selectSqlStatement.getCteList() != null){
				String with = selectSqlStatement.getCteList().toString();
				
				System.out.println(sql.indexOf(with) + with.length());
				System.out.println(sql.substring(0, sql.indexOf(with) + with.length()));
				System.out.println(sql.substring(sql.indexOf(with) + with.length()));
//				sql.substring(sql.indexOf(with), endIndex)
				
			}
		}
		
		
	}
}
