package test;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class DBTest {
	public static void main(String[] args) {
//		String a = "JFAFEIOWJ";
//		System.out.println(a.charAt(5));
		
		List<String> a = new ArrayList<String>(100);
		System.out.println(a.size());;
//		dynamicCreateTable();
//		dynamicDropTable();
	}

	private static void dynamicDropTable() {
//		DBTableHandler dto = new DBTableHandler("jdbc.properties");
//		List<CfgTable> tabledatas = new ArrayList<CfgTable>();
//		CfgTable table1 = new CfgTable();
////		table1.setTableName("Parent");
//		CfgTable table2 = new CfgTable();
////		table2.setTableName("DatabaseTest");
//		tabledatas.add(table1);
//		tabledatas.add(table2);
//		table2.setTableType(BuiltinDatabaseData.PARENT_SUB_TABLE);
//		table2.setParentTableName("Parent");
//		dto.dropTable(tabledatas );
	}

	private static void dynamicCreateTable() {
//		DBTableHandler dto = new DBTableHandler("jdbc.properties");
//		List<CfgTable> tabledatas = new ArrayList<CfgTable>();
//		CfgTable table = new CfgTable();
//		List<CfgColumn> columns = new ArrayList<CfgColumn>();
//		table.setColumns(columns);
//		table.setComments("表注释表注释表注释");
////		table.setTableName("Parent");
//		tabledatas.add(table);
//		
//		CfgTable table2 = new CfgTable();
//		table2.setColumns(columns);
////		table2.setTableName("DatabaseTest");
//		table2.setTableType(BuiltinDatabaseData.PARENT_SUB_TABLE);
//		table2.setParentTableName("Parent");
//		tabledatas.add(table2);
//		
//		
//		CfgColumn c1 = new CfgColumn();
////		c1.setColumnName("Id");
//		c1.setColumnType("string");
//		c1.setLength(32);
//		c1.setIsKey(1);
//		
//		CfgColumn c2 = new CfgColumn();
////		c2.setColumnName("age");
//		c2.setColumnType("integer");
//		c2.setLength(2);
//		c2.setIsUnique(1);
//		c2.setComments("列注释注释");
//		
//		CfgColumn c3 = new CfgColumn();
////		c3.setColumnName("price");
//		c3.setColumnType("double");
//		c3.setLength(12);
//		c3.setPrecision(2);
//		c3.setDefaultValue("22.2");
//		
//		CfgColumn c4 = new CfgColumn();
////		c4.setColumnName("birthday");
//		c4.setColumnType("date");
//		c4.setIsNullabled(0);
//		
//		CfgColumn c5 = new CfgColumn();
////		c5.setColumnName("content");
//		c5.setColumnType("clob");
//		
//		CfgColumn c6 = new CfgColumn();
////		c6.setColumnName("image");
//		c6.setColumnType("blob");
//		
//		columns.add(c1);
//		columns.add(c2);
//		columns.add(c3);
//		columns.add(c4);
//		columns.add(c5);
//		columns.add(c6);
//		
//		dto.createTable(tabledatas);
	}
}
