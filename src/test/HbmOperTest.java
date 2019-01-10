package test;

import java.util.ArrayList;
import java.util.List;

import com.api.sys.entity.cfg.CfgColumn;
import com.api.sys.entity.cfg.CfgTable;

public class HbmOperTest {
	public static void main(String[] args) {
		List<CfgTable> tabledatas = new ArrayList<CfgTable>();
		CfgTable table = new CfgTable();
		List<CfgColumn> columns = new ArrayList<CfgColumn>();
		table.setId("12312");
		table.setColumns(columns);
		table.setRemark("表注释表注释表注释");
//		table.setTableName("Parent");
		tabledatas.add(table);
		
		CfgTable table2 = new CfgTable();
		table2.setId("123121111");
		table2.setColumns(columns);
//		table2.setTableName("DatabaseTest");
		tabledatas.add(table2);
		
		
		CfgColumn c1 = new CfgColumn();
//		c1.setColumnName("Id");
		c1.setColumnType("string");
		c1.setLength(32);
//		c1.setIsKey(1);
		
		CfgColumn c2 = new CfgColumn();
//		c2.setColumnName("age_stu");
		c2.setColumnType("integer");
		c2.setLength(2);
		c2.setIsUnique(1);
		c2.setComments("列注释注释");
		
		CfgColumn c3 = new CfgColumn();
//		c3.setColumnName("price");
		c3.setColumnType("double");
		c3.setLength(12);
		c3.setPrecision(2);
		c3.setDefaultValue("22.2");
		
		CfgColumn c4 = new CfgColumn();
//		c4.setColumnName("birthday");
		c4.setColumnType("date");
		c4.setIsNullabled(0);
		
		CfgColumn c5 = new CfgColumn();
//		c5.setColumnName("content");
		c5.setColumnType("clob");
		
		CfgColumn c6 = new CfgColumn();
//		c6.setColumnName("image");
		c6.setColumnType("blob");
		
		columns.add(c1);
		columns.add(c2);
		columns.add(c3);
		columns.add(c4);
		columns.add(c5);
		columns.add(c6);
		
		
		
		
		
		
//		HibernateHbmUtil hbm = new HibernateHbmUtil();
//		hbm.createHbmMappingContent(tabledatas );
//		hbm.dropHbmMappingContent(tabledatas );
	}
}
