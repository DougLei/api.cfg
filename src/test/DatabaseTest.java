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
		sqls += "  DECLARE @NEWID VARCHAR(50) \n"; 
		sqls += "  DECLARE @MATERIALSTOCKID VARCHAR(50)\n"; 
		sqls += "  DECLARE @MANAGEMETHOD INT\n"; 
		sqls += "  SET @NEWID=NEWID()\n"; 
		sqls += "  SET @MANAGEMETHOD=0\n"; 
		sqls += "  SET @MATERIALSTOCKID=''\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  insert into Wms_Materialoutstorage (STORAGEAREAID, id) values('149597e0d07d4402be04c5298fb2096d', 'zs')\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  /*找到库存ID，插入出库小号货位数据*/\n"; 
		sqls += "  select @MANAGEMETHOD = MANAGEMETHOD from MDM_MATERIALINFO where ID = 'ea1d12040bd84b8ebafff3e6553dace0'  --0a145782df67490b8c8c52070613f8fc null\n"; 
		sqls += "  select @MATERIALSTOCKID = ID from WMS_MATERIALSTOCK														 --	ea1d12040bd84b8ebafff3e6553dace0  1\n"; 
		sqls += "  where STORAGEAREAID = (select STORAGEAREAID from WMS_MATERIALOUTSTORAGE where ID = 'zs')\n"; 
		sqls += "  and MATERIALID = 'ea1d12040bd84b8ebafff3e6553dace0'\n"; 
		sqls += "  and BARCODE = 'CP1-001'\n"; 
		sqls += "  and STOCKNUMBER>0\n"; 
		sqls += "  and RECORDSTATUS>1\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  /*插入出库细表数据*/\n"; 
		sqls += "  INSERT INTO WMS_MATERIALOUTSTORAGEDETAILS\n"; 
		sqls += "  (OUTSTORAGERECORDID,MATERIALID,BARCODE,SERIALNUMBER,BATCHNUMBER,\n"; 
		sqls += "  SIZE,OUTSTORAGENUMBER,REMARK,SORTCODE,DELETEFLAG,\n"; 
		sqls += "  ID,CUSTOMER_ID, PROJECT_ID,CREATE_DATE, LAST_UPDATE_DATE,\n"; 
		sqls += "  CREATE_USER_ID, LAST_UPDATE_USER_ID)\n"; 
		sqls += "  VALUES \n"; 
		sqls += "  (1,'ea1d12040bd84b8ebafff3e6553dace0','CP1-001',null,null,\n"; 
		sqls += "  null,1,null,0,1,\n"; 
		sqls += "  @NEWID,'unknow','unknow',GETDATE(),GETDATE(),\n"; 
		sqls += "  'unknow','unknow');\n"; 
		sqls += "  \n"; 
		sqls += "  select @MANAGEMETHOD\n"; 
		sqls += "  \n"; 
		sqls += "  /*单个管理插入对应的库位数据，@MANAGEMETHOD=1单个管理，2批次管理*/\n"; 
		sqls += "  IF(@MANAGEMETHOD = 1)\n"; 
		sqls += "  BEGIN\n"; 
		sqls += "  	insert into WMS_OUTMATERIALPLACEDETAILS\n"; 
		sqls += "  	(MATERIALOUTSTORAGEID,OUTSTORAGEDETAILSID,GOODSPLACEID,SINGLECODE,BARCODE,NUMBER,\n"; 
		sqls += "  	ID,CUSTOMER_ID, PROJECT_ID,CREATE_DATE, LAST_UPDATE_DATE,\n"; 
		sqls += "  	CREATE_USER_ID, LAST_UPDATE_USER_ID)\n"; 
		sqls += "  	select  1, @NEWID,GOODSPLACEID,SINGLECODE,BARCODE,1,\n"; 
		sqls += "  	NEWID(),'unknow','unknow',GETDATE(),GETDATE(),\n"; 
		sqls += "  	'unknow','unknow'\n"; 
		sqls += "  	from WMS_STOCKMATERIALPLACEDETAILS \n"; 
		sqls += "  	where MATERIALSTOCKID = @MATERIALSTOCKID ;\n"; 
		sqls += "  END\n"; 


		
		return sqls;
	}
}
