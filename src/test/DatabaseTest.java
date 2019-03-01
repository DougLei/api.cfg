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
		sqls += "  \n"; 
		sqls += "  @barcode VARCHAR(50),\n"; 
		sqls += "  \n"; 
		sqls += "  @workorderid VARCHAR(50),\n"; 
		sqls += "  \n"; 
		sqls += "  @barcode1 VARCHAR(50),\n"; 
		sqls += "  \n"; 
		sqls += "  @firststring VARCHAR(50),\n"; 
		sqls += "  \n"; 
		sqls += "  @stringlength INT\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  SET @barcode = 'F-F-117T0X-1'\n"; 
		sqls += "  \n"; 
		sqls += "  SET @workorderid = '38B65541-74A3-42B6-BCD3-4518B5018A38'\n"; 
		sqls += "  \n"; 
		sqls += "  SET @stringlength = LEN(@barcode)\n"; 
		sqls += "  \n"; 
		sqls += "  SET @firststring = SUBSTRING(@barcode,1,1)\n"; 
		sqls += "  \n"; 
		sqls += "  SET @barcode1 = SUBSTRING(@barcode,3,@stringlength)\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  IF(@firststring = 'B')\n"; 
		sqls += "  \n"; 
		sqls += "  BEGIN\n"; 
		sqls += "  \n"; 
		sqls += "  UPDATE MES_WORKORDERSINGLECODE\n"; 
		sqls += "  \n"; 
		sqls += "  SET \n"; 
		sqls += "  \n"; 
		sqls += "  PRODUCTSTATUS = 2,\n"; 
		sqls += "  \n"; 
		sqls += "  ACTUALBEGINTIME = GETDATE()\n"; 
		sqls += "  \n"; 
		sqls += "  WHERE ID = (SELECT ID FROM MES_WORKORDERSINGLECODE WHERE WORKSERIALSINGLECODEID = (SELECT ID FROM MES_SINGLECODE WHERE BARCODE = @barcode1) )\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  UPDATE MES_SINGLECODE \n"; 
		sqls += "  \n"; 
		sqls += "  SET STATUS = 2\n"; 
		sqls += "  \n"; 
		sqls += "  WHERE BARCODE = @barcode1\n"; 
		sqls += "  \n"; 
		sqls += "  END\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  ELSE IF(@firststring = 'F')\n"; 
		sqls += "  \n"; 
		sqls += "  BEGIN\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "   UPDATE MES_WORKORDERSINGLECODE\n"; 
		sqls += "  \n"; 
		sqls += "  SET \n"; 
		sqls += "  \n"; 
		sqls += "  PRODUCTSTATUS = 4,\n"; 
		sqls += "  \n"; 
		sqls += "  ACTUALFINISHTIME = GETDATE()\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  WHERE ID = (SELECT ID FROM MES_WORKORDERSINGLECODE WHERE WORKSERIALSINGLECODEID = (SELECT ID FROM MES_SINGLECODE WHERE BARCODE = @barcode1) )\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  UPDATE MES_SINGLECODE \n"; 
		sqls += "  \n"; 
		sqls += "  SET STATUS = 4\n"; 
		sqls += "  \n"; 
		sqls += "  WHERE BARCODE = @barcode1\n"; 
		sqls += "  \n"; 
		sqls += "  BEGIN\n"; 
		sqls += "  \n"; 
		sqls += "  DECLARE \n"; 
		sqls += "  \n"; 
		sqls += "   @ACTUALBEGINTIME DATE,\n"; 
		sqls += "  \n"; 
		sqls += "   @ACTUALFINISHTIME DATE\n"; 
		sqls += "  \n"; 
		sqls += "   SELECT @ACTUALBEGINTIME =  ACTUALBEGINTIME FROM MES_WORKORDERSINGLECODE \n"; 
		sqls += "  \n"; 
		sqls += "   WHERE WORKSERIALSINGLECODEID = (SELECT ID FROM MES_SINGLECODE WHERE BARCODE = @barcode1)\n"; 
		sqls += "  \n"; 
		sqls += "   SELECT @ACTUALFINISHTIME =  ACTUALFINISHTIME FROM MES_WORKORDERSINGLECODE \n"; 
		sqls += "  \n"; 
		sqls += "   WHERE WORKSERIALSINGLECODEID = (SELECT ID FROM MES_SINGLECODE WHERE BARCODE = @barcode1)\n"; 
		sqls += "  \n"; 
		sqls += "   UPDATE MES_WORKORDERSINGLECODE\n"; 
		sqls += "  \n"; 
		sqls += "   SET \n"; 
		sqls += "  \n"; 
		sqls += "   WORKTIME = (select (datediff( Minute, '2019-02-27 17:42:29', '2019-03-01 17:39:55' )/60.0))\n"; 
		sqls += "  \n"; 
		sqls += "  WHERE ID = (SELECT ID FROM MES_WORKORDERSINGLECODE WHERE WORKSERIALSINGLECODEID = (SELECT ID FROM MES_SINGLECODE WHERE BARCODE = @barcode1) )\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  END\n"; 
		sqls += "  \n"; 
		sqls += "   \n"; 
		sqls += "  \n"; 
		sqls += "  END\n"; 


		
		return sqls;
	}
}
