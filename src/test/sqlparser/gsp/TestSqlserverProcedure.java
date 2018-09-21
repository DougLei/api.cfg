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
			String defaultValue = null;
			String parameterName;
			String dataType;
			String[] lengthArr = null;
			String length;
			String precision;
			for(int i=0;i<len;i++){
				length = null;
				precision = null;
				
				param = procedureSqlStatement.getParameterDeclarations().getParameterDeclarationItem(i);
				if(param.getDefaultValue() != null){
					defaultValue = param.getDefaultValue().toString();
					if(defaultValue.startsWith("'")){
						defaultValue = defaultValue.substring(1, defaultValue.length()-1);
					}
				}
				
				parameterName = param.getParameterName().toString();
				if(parameterName.startsWith("@")){
					parameterName = parameterName.substring(1);
				}
				
				dataType = param.getDataType().toString().toLowerCase();
				if(dataType.indexOf("(") != -1){
					lengthArr = dataType.substring(dataType.indexOf("(")+1, dataType.indexOf(")")).split(",");
					length = lengthArr[0];
					if(lengthArr.length == 2){
						precision = lengthArr[1];
					}else if(lengthArr.length > 2){
						throw new IllegalArgumentException("系统在解析存储过程参数时出现异常，请联系后台系统开发人员");
					}
					dataType = dataType.substring(0, dataType.indexOf("("));
				}
				
				System.out.println("|"+parameterName+"|"+dataType +"\t\t" + length + "\t\t" + precision);
			}
		}
		
		
	}

	private static String getSql() {
		String sqls = "";
		sqls += "  create procedure test\n"; 
		sqls += "  	    @name varchar(12) = '哈哈',\n"; 
		sqls += "  	@resultName varchar out,\n"; 
		sqls += "  	@numbers decimal(20, 2) out\n"; 
		sqls += "  as \n"; 
		sqls += "  begin\n"; 
		sqls += "    set @resultName=@name+'-zd';\n"; 
		sqls += "  end\n"; 










		return sqls;
	}
}
