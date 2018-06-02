package test;


@SuppressWarnings("unused")
public class DBTest {
	public static void main(String[] args) {
		String a = "JFAFEIOWJ";
		System.out.println(a.charAt(5));
		
		
//		dynamicCreateTable();
//		dynamicDropTable();
	}

	private static void dynamicDropTable() {
//		DBTableHandler dto = new DBTableHandler("jdbc.properties");
//		List<CfgTabledata> tabledatas = new ArrayList<CfgTabledata>();
//		CfgTabledata table1 = new CfgTabledata();
////		table1.setTableName("Parent");
//		CfgTabledata table2 = new CfgTabledata();
////		table2.setTableName("Sub");
//		tabledatas.add(table1);
//		tabledatas.add(table2);
//		table2.setTableType(DynamicDataConstants.PARENT_SUB_TABLE);
//		table2.setParentTableName("Parent");
//		dto.dropTable(tabledatas );
	}

	private static void dynamicCreateTable() {
//		DBTableHandler dto = new DBTableHandler("jdbc.properties");
//		List<CfgTabledata> tabledatas = new ArrayList<CfgTabledata>();
//		CfgTabledata table = new CfgTabledata();
//		List<CfgColumndata> columns = new ArrayList<CfgColumndata>();
//		table.setColumns(columns);
//		table.setComments("表注释表注释表注释");
////		table.setTableName("Parent");
//		tabledatas.add(table);
//		
//		CfgTabledata table2 = new CfgTabledata();
//		table2.setColumns(columns);
////		table2.setTableName("Sub");
//		table2.setTableType(DynamicDataConstants.PARENT_SUB_TABLE);
//		table2.setParentTableName("Parent");
//		tabledatas.add(table2);
//		
//		
//		CfgColumndata c1 = new CfgColumndata();
////		c1.setColumnName("Id");
//		c1.setColumnType("string");
//		c1.setLength(32);
//		c1.setIsKey(1);
//		
//		CfgColumndata c2 = new CfgColumndata();
////		c2.setColumnName("age");
//		c2.setColumnType("integer");
//		c2.setLength(2);
//		c2.setIsUnique(1);
//		c2.setComments("列注释注释");
//		
//		CfgColumndata c3 = new CfgColumndata();
////		c3.setColumnName("price");
//		c3.setColumnType("double");
//		c3.setLength(12);
//		c3.setPrecision(2);
//		c3.setDefaultValue("22.2");
//		
//		CfgColumndata c4 = new CfgColumndata();
////		c4.setColumnName("birthday");
//		c4.setColumnType("date");
//		c4.setIsNullabled(0);
//		
//		CfgColumndata c5 = new CfgColumndata();
////		c5.setColumnName("content");
//		c5.setColumnType("clob");
//		
//		CfgColumndata c6 = new CfgColumndata();
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
