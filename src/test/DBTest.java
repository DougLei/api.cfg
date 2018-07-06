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
//		List<ComTabledata> tabledatas = new ArrayList<ComTabledata>();
//		ComTabledata table1 = new ComTabledata();
////		table1.setTableName("Parent");
//		ComTabledata table2 = new ComTabledata();
////		table2.setTableName("Sub");
//		tabledatas.add(table1);
//		tabledatas.add(table2);
//		table2.setTableType(BuiltinDatabaseData.PARENT_SUB_TABLE);
//		table2.setParentTableName("Parent");
//		dto.dropTable(tabledatas );
	}

	private static void dynamicCreateTable() {
//		DBTableHandler dto = new DBTableHandler("jdbc.properties");
//		List<ComTabledata> tabledatas = new ArrayList<ComTabledata>();
//		ComTabledata table = new ComTabledata();
//		List<ComColumndata> columns = new ArrayList<ComColumndata>();
//		table.setColumns(columns);
//		table.setComments("表注释表注释表注释");
////		table.setTableName("Parent");
//		tabledatas.add(table);
//		
//		ComTabledata table2 = new ComTabledata();
//		table2.setColumns(columns);
////		table2.setTableName("Sub");
//		table2.setTableType(BuiltinDatabaseData.PARENT_SUB_TABLE);
//		table2.setParentTableName("Parent");
//		tabledatas.add(table2);
//		
//		
//		ComColumndata c1 = new ComColumndata();
////		c1.setColumnName("Id");
//		c1.setColumnType("string");
//		c1.setLength(32);
//		c1.setIsKey(1);
//		
//		ComColumndata c2 = new ComColumndata();
////		c2.setColumnName("age");
//		c2.setColumnType("integer");
//		c2.setLength(2);
//		c2.setIsUnique(1);
//		c2.setComments("列注释注释");
//		
//		ComColumndata c3 = new ComColumndata();
////		c3.setColumnName("price");
//		c3.setColumnType("double");
//		c3.setLength(12);
//		c3.setPrecision(2);
//		c3.setDefaultValue("22.2");
//		
//		ComColumndata c4 = new ComColumndata();
////		c4.setColumnName("birthday");
//		c4.setColumnType("date");
//		c4.setIsNullabled(0);
//		
//		ComColumndata c5 = new ComColumndata();
////		c5.setColumnName("content");
//		c5.setColumnType("clob");
//		
//		ComColumndata c6 = new ComColumndata();
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
