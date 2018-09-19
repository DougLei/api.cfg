package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;

public class TestOracleProcedure {
	public static void main(String[] args) {
		String sql = getSql();
		
		EDbVendor dbDialect = EDbVendor.dbvoracle;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		
		TStatementList sqlList = parser.sqlstatements;
		TPlsqlCreateProcedure procedureSqlStatement = (TPlsqlCreateProcedure)sqlList.get(0);
		
		
		if(procedureSqlStatement.getParameterDeclarations() != null && procedureSqlStatement.getParameterDeclarations().size() > 0){
			int len = procedureSqlStatement.getParameterDeclarations().size();
		
			TParameterDeclaration param = null;
			for(int i=0;i<len;i++){
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				
				 
				if(param.getDefaultValue() != null){
					String defaultValue = param.getDefaultValue().toString();
					if(defaultValue.startsWith("'")){
						defaultValue = defaultValue.substring(1, defaultValue.length()-1);
					}
					System.out.println(defaultValue);
				}
			}
		}
		
		
	}

	private static String getSql() {
		String sqls = "";
		sqls += "  create or replace procedure test(age number, name varchar2 := '   呵呵啊')\n"; 
		sqls += "  as \n"; 
		sqls += "  begin\n"; 
		sqls += "   dbms_output.put_line(name || '+' || age);\n"; 
		sqls += "  end;\n"; 












		return sqls;
	}
}
