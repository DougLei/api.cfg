package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



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
		String[] sqls = getSql().split(";");
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection("jdbc:sqlserver://192.168.1.111:1433;DatabaseName=SmartOneCfg", "sa", "123_abc");

		Statement s = conn.createStatement();
		System.out.println("1222");
		
		for(String sql : sqls){
			System.out.println(s.executeUpdate(sql));
		}
	}

	private static String getSql() {
		String sqls = "";
		sqls += "  DECLARE @layouttemple TABLE\n"; 
		sqls += "  (\n"; 
		sqls += "   ID varchar(32)\n"; 
		sqls += "  );\n"; 
		sqls += "  WITH cte(ID,NAME,PARENT_ID)\n"; 
		sqls += "  AS (\n"; 
		sqls += "  SELECT \n"; 
		sqls += "   R1.ID,\n"; 
		sqls += "   R1.NAME,\n"; 
		sqls += "   R1.PARENT_ID\n"; 
		sqls += "  FROM dbo.SYS_ROWS_COLS AS R1\n"; 
		sqls += "  WHERE R1.BS='rows'\n"; 
		sqls += "  UNION ALL\n"; 
		sqls += "  SELECT \n"; 
		sqls += "   R2.ID,\n"; 
		sqls += "   R2.NAME,\n"; 
		sqls += "   R2.PARENT_ID\n"; 
		sqls += "  FROM dbo.SYS_ROWS_COLS AS R2\n"; 
		sqls += "  WHERE R2.BS='row'\n"; 
		sqls += "  UNION ALL\n"; 
		sqls += "  SELECT \n"; 
		sqls += "   R3.ID,\n"; 
		sqls += "   R3.NAME,\n"; 
		sqls += "   R3.PARENT_ID\n"; 
		sqls += "  FROM dbo.SYS_ROWS_COLS AS R3\n"; 
		sqls += "  WHERE R3.BS='columns'\n"; 
		sqls += "  UNION ALL\n"; 
		sqls += "  SELECT \n"; 
		sqls += "   R4.ID,\n"; 
		sqls += "   R4.NAME,\n"; 
		sqls += "   R4.PARENT_ID\n"; 
		sqls += "  FROM dbo.SYS_ROWS_COLS AS R4\n"; 
		sqls += "  WHERE R4.BS='column'\n"; 
		sqls += "  UNION ALL\n"; 
		sqls += "  SELECT \n"; 
		sqls += "   C.ID,\n"; 
		sqls += "   C.TITLE AS NAME,\n"; 
		sqls += "   C.COLUMN_ID AS PARENT_ID\n"; 
		sqls += "  FROM dbo.SYS_COLS_DETAILS AS C),\n"; 
		sqls += "   A AS (\n"; 
		sqls += "  SELECT ID,NAME,PARENT_ID FROM cte WHERE ID= '1'\n"; 
		sqls += "  UNION ALL\n"; 
		sqls += "  SELECT cte.ID, cte.NAME, cte.PARENT_ID FROM cte\n"; 
		sqls += "  INNER JOIN A ON cte.PARENT_ID = A.ID\n"; 
		sqls += "  )\n"; 
		sqls += "  INSERT INTO @layouttemple\n"; 
		sqls += "          ( ID ) \n"; 
		sqls += "  SELECT ID FROM A\n"; 
		sqls += "  DELETE FROM dbo.SYS_COLS_DETAILS WHERE\n"; 
		sqls += "  ID IN(SELECT ID FROM @layouttemple)\n"; 
		sqls += "  DELETE FROM dbo.SYS_ROWS_COLS WHERE\n"; 
		sqls += "  ID IN(SELECT ID FROM @layouttemple)\n"; 
		return sqls;
	}
}
