package test.sqlparser.druid;

import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Name;



public class Test1 {
	public static void main(String[] args) {
		String sql = returnSql();
		
		SQLStatementParser parser = new OracleStatementParser(sql);
		List<SQLStatement> ast = parser.parseStatementList();
		System.err.println(ast.size());
		
		for (SQLStatement a : ast) {
			OracleSchemaStatVisitor visitor= new OracleSchemaStatVisitor();
			a.accept(visitor);
			
			Map<Name, TableStat> ts = visitor.getTables();
			System.out.println(ts);
			
			
			System.out.println("\n---------------------------\n");
		}
	}

	private static String returnSql() {
		String sql = "";
		sql += "with t as(select name from Student where classId = (select id from classes where name='一年级') and name=1\n" +
				"union all\n" +
				"select gname from groupStudent)\n" +
				"select * from t;";
		sql += " (select id from classes where name='二年级');";
		
		
//		sql += "  with Temp as\n"; 
//		sql += "  (  \n"; 
//		sql += "    SELECT\n"; 
//		sql += "      ID, PARENT_ID, KIND,\n"; 
//		sql += "      Convert(VarChar(500), Name) AS NAME,\n"; 
//		sql += "      Convert(VarChar(500), SHORT_NAME) AS SHORT_NAME,\n"; 
//		sql += "      CODE, PLAT_CUSTOMER_ID, CREATE_TIME\n"; 
//		sql += "    FROM\n"; 
//		sql += "      ORG_NODE\n"; 
//		sql += "    WHERE\n"; 
//		sql += "      PARENT_ID = '~_PlatCustomerId~'\n"; 
//		sql += "  \n"; 
//		sql += "    UNION ALL\n"; 
//		sql += "  \n"; 
//		sql += "    SELECT\n"; 
//		sql += "      B.ID, B.PARENT_ID, B.KIND,\n"; 
//		sql += "      Convert(VarChar(500), A.NAME + '|' + B.NAME) AS NAME,\n"; 
//		sql += "      Convert(VarChar(500), A.SHORT_NAME + '|' + B.SHORT_NAME) AS SHORT_NAME,\n"; 
//		sql += "      B.CODE, B.PLAT_CUSTOMER_ID, B.CREATE_TIME\n"; 
//		sql += "    FROM\n"; 
//		sql += "      Temp A inner join ORG_NODE B on A.ID = B.PARENT_ID\n"; 
//		sql += "  )  \n"; 
//		sql += "  select * from Temp where name='';\n";
//		sql +=" select '第二条sql' from Student where name='1'";


		return sql;
	}
}
