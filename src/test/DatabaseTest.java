package test;

import java.sql.Connection;
import java.sql.DriverManager;
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
			
			
			
			
			System.out.println("1");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.1.111:1433;DatabaseName=SmartOneCfg", "sa", "123_abc");
			System.out.println("2");
			System.out.println(conn);
			
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=SmartOneCfg", "sa", "root");
//			System.out.println(conn);
			
//			executeSqlTest();
			
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
		}  catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	private static void executeSqlTest() throws ClassNotFoundException, SQLException{
//		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.1.111:1433;DatabaseName=SmartOneCfg", "sa", "123_abc");
//		CallableStatement cs = conn.prepareCall(getSql());
//		cs.setString(1, "a14eb744147346a1b46cc0174504548a");
//		cs.setString(2, "1");
//		
//		cs.execute();
//		System.out.println(111);
//		
//	}
	
//	private static String getSql(){
//		String sqls = "{call UpdatePPElement(?,?)}";
//		return sqls;
//	}
}
