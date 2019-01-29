package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class DatabaseTest extends Parent{
	private String name;
	public String getName(){
		return name;
	}
	
	public static void main(String[] args)  {
		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "SmartOneCfg", "root");
//			PreparedStatement pst = conn.prepareStatement(" select count(1) from user_tables where table_name = ?");
//			pst.setString(1, "Com_sQL_sCript".toUpperCase());
//			ResultSet rs = pst.executeQuery();
//			if(rs.next() && (rs.getInt(1) > 0)){
//				System.out.println("存在");
//				return;
//			}
//			System.out.println("不存在！！！！！");
			
			
			
			
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=SmartOneCfg", "sa", "root");
//			PreparedStatement pst = conn.prepareStatement("select count(1) from  sysobjects where id = object_id(?) and type = 'U'");
//			pst.setString(1, "Com_sQL_sCript".toLowerCase());
//			ResultSet rs = pst.executeQuery();
//			if(rs.next() && (rs.getInt(1) > 0)){
//				System.out.println("存在");
//				return;
//			}
//			System.out.println("不存在！！！！！");
			
			
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=SmartOneCfg", "sa", "root");
//			System.out.println(conn);
			
			executeSqlTest();
			
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.1.252:1433;DatabaseName=SmartOneCfg", "sa", "root");
//			Statement st = conn.createStatement();
//			st.executeUpdate("drop view a");
//			st.executeUpdate("drop view b");
//			st.executeUpdate("create view a as select * from COM_COLUMNDATA");
//			st.executeUpdate("create view b as select * from COM_COLUMNDATA");
//			st.close();
//			conn.close();
//			System.out.println("ok");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void executeSqlTest() throws ClassNotFoundException, SQLException{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=DProcess", "sa", "root");
		Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.1.111:1433;DatabaseName=SmartOneCfg", "sa", "123_abc");
		PreparedStatement ps = conn.prepareStatement(getSql());
		
//		int index = 1;
//		ps.setString(index++, "a661e25ac5c040bba127ca08d4941f85");
//		ps.setString(index++, "485e20eda7bf4068bf9aafbbb3b55090");
//		ps.setString(index++, "923ff78943d1413db5f3a2850b18f1cf");
//		ps.setString(index++, "923ff78943d1413db5f3a2850b18f1cf");
//		ps.setString(index++, "58bb8239609649c490889b68d07f7fea");
//		ps.setString(index++, "58bb8239609649c490889b68d07f7fea");
//		ps.setString(index++, "10");
//		ps.setString(index++, "2019-01-01");
//		ps.setString(index++, "2019-01-07");
//		ps.setString(index++, "8");
//		ps.setString(index++, "9d00696c3245463698fc1e9c61da44db");
		
		
		int count = ps.executeUpdate();
		System.out.println(count);
	}
	
	private static String getSql(){
		String sqls = "";
		sqls += "  DECLARE\n"; 
		sqls += "  @RESOURCESBARCODE VARCHAR(500),\n"; 
		sqls += "  @RESOURCESBARCODE1 VARCHAR(500)\n"; 
		sqls += "  \n"; 
		sqls += "  SET @RESOURCESBARCODE = '12345'\n"; 
		sqls += "     SELECT @RESOURCESBARCODE1 = \n"; 
		sqls += "     STUFF(\n"; 
		sqls += "     (SELECT ','+ A.ID from\n"; 
		sqls += "     ( SELECT DISTINCT ID FROM(\n"; 
		sqls += "         SELECT \n"; 
		sqls += "         value  AS ID\n"; 
		sqls += "         FROM F_Split((SELECT  MAX(MEASUREBARCODE)  FROM MES_CHECKPOSITIONDATA WHERE ID IN (SELECT  ID FROM MES_CHECKPOSITIONDATA WHERE CHECKPOSITIONDETAILSID = '6DE1B5D1-3FAE-4803-B5BF-4E9C0D98D93C' AND CHECKELEMENTID in\n"; 
		sqls += "        (SELECT ID FROM MES_CHECKDATA WHERE PROCESSDETAILSID in (SELECT ID FROM  MES_PROCESSDETAILS WHERE WORKORDERSINGLECODEID IN \n"; 
		sqls += "        (SELECT ID FROM MES_WORKORDERSINGLECODE WHERE ID IN(SELECT VALUE FROM F_Split\n"; 
		sqls += "        ('7AB5348C-A6D0-43BC-829C-C16D514C6301,88301F29-2E57-45D3-9463-B8E89DEDAD90',',')\n"; 
		sqls += "         )\n"; 
		sqls += "         )\n"; 
		sqls += "         ) \n"; 
		sqls += "         )\n"; 
		sqls += "         )\n"; 
		sqls += "         ),',')\n"; 
		sqls += "         UNION\n"; 
		sqls += "         SELECT\n"; 
		sqls += "         ID\n"; 
		sqls += "         FROM WMS_RESOURCESSTOCK WHERE RESOURCESBARCODE = @RESOURCESBARCODE\n"; 
		sqls += "         )B )A\n"; 
		sqls += "         FOR  XML PATH('') ),1,1,'')\n"; 
		sqls += "  	   SELECT @RESOURCESBARCODE1\n"; 
		sqls += "  \n"; 
		sqls += "        UPDATE MES_CHECKPOSITIONDATA\n"; 
		sqls += "        SET MEASUREBARCODE = @RESOURCESBARCODE1\n"; 
		sqls += "        WHERE ID IN (SELECT  ID FROM MES_CHECKPOSITIONDATA WHERE CHECKPOSITIONDETAILSID = '6DE1B5D1-3FAE-4803-B5BF-4E9C0D98D93C' AND CHECKELEMENTID IN\n"; 
		sqls += "        (SELECT ID FROM MES_CHECKDATA WHERE PROCESSDETAILSID IN (SELECT ID FROM  MES_PROCESSDETAILS WHERE WORKORDERSINGLECODEID IN \n"; 
		sqls += "        (SELECT ID FROM MES_WORKORDERSINGLECODE WHERE ID IN(SELECT VALUE FROM F_Split\n"; 
		sqls += "        ('7AB5348C-A6D0-43BC-829C-C16D514C6301,88301F29-2E57-45D3-9463-B8E89DEDAD90',',')\n"; 
		sqls += "        )\n"; 
		sqls += "        )\n"; 
		sqls += "        ) \n"; 
		sqls += "        )\n"; 
		sqls += "       )\n"; 
		sqls += "  	  UPDATE MES_CHECKLISTDATA\n"; 
		sqls += "        SET MEASUREBARCODE = @RESOURCESBARCODE1\n"; 
		sqls += "        WHERE ID IN (SELECT  ID FROM MES_CHECKPOSITIONDATA WHERE CHECKLISTDETAILSID = '6DE1B5D1-3FAE-4803-B5BF-4E9C0D98D93C' AND CHECKELEMENTID IN\n"; 
		sqls += "        (SELECT ID FROM MES_CHECKDATA WHERE PROCESSDETAILSID IN (SELECT ID FROM  MES_PROCESSDETAILS WHERE WORKORDERSINGLECODEID IN \n"; 
		sqls += "        (SELECT ID FROM MES_WORKORDERSINGLECODE WHERE ID IN(SELECT VALUE FROM F_Split\n"; 
		sqls += "        ('7AB5348C-A6D0-43BC-829C-C16D514C6301,88301F29-2E57-45D3-9463-B8E89DEDAD90',',')\n"; 
		sqls += "        )\n"; 
		sqls += "        )\n"; 
		sqls += "        ) \n"; 
		sqls += "        )\n"; 
		sqls += "       )\n"; 


		
		return sqls;
	}
}
