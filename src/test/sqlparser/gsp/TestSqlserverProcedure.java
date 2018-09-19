package test.sqlparser.gsp;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateProcedure;

public class TestSqlserverProcedure {
	public static void main(String[] args) {
		String sql = getSql();
		
		EDbVendor dbDialect = EDbVendor.dbvmssql;
		TGSqlParser parser = new TGSqlParser(dbDialect);
		parser.sqltext = sql;
		parser.parse();
		
		TStatementList sqlList = parser.sqlstatements;
		TMssqlCreateProcedure procedureSqlStatement = (TMssqlCreateProcedure)sqlList.get(0);
		
		
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
		sqls += "  create procedure test\n"; 
		sqls += "  	@name varchar(20) = '哈哈',\n"; 
		sqls += "  	@resultName varchar(20) out\n"; 
		sqls += "  as \n"; 
		sqls += "  begin\n"; 
		sqls += "    set @resultName=@name+'-zd';\n"; 
		sqls += "  end\n"; 










		return sqls;
	}
}
