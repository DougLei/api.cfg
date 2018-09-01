package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;
import java.util.ArrayList;
import java.util.List;
import com.king.tooth.sys.entity.cfg.sql.SqlScriptParameterNameRecord;

public class TestProcedure {
	public static void main(String[] args) {
		String sql = getSql();
		
		EDbVendor dbDialect = EDbVendor.dbvoracle;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		
		TStatementList sqlList = parser.sqlstatements;
		System.out.println(sqlList.size());
		
		TPlsqlCreateProcedure procedureSqlStatement = (TPlsqlCreateProcedure)sqlList.get(0); 		
		
		// 解析出存储过程名
				List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>(1);
				SqlScriptParameterNameRecord parameterNameRecord = new SqlScriptParameterNameRecord(0);
				parameterNameRecordList.add(parameterNameRecord);
				
				// 解析参数
				if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
					int len = procedureSqlStatement.getParameterDeclarations().size();
					
					TParameterDeclaration param = null;
					String dataType;
					for(int i=0;i<len;i++){
						param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
						dataType = param.getDataType().toString().toLowerCase();
						System.out.println(dataType);
					}
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
