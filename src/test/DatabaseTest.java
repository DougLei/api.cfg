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
//		String sqls = "";
//		sqls += "  DECLARE @PRODUCTID VARCHAR(50), @PRODUCTNAME VARCHAR(50), @PARTSID VARCHAR(50), @PARTSNAME VARCHAR(50), @PRODUCTCODEID VARCHAR(50), @PRODUCTCODE VARCHAR(50), @STAGEID VARCHAR(50), @STAGE VARCHAR(50), @SERIALNUMBER VARCHAR(50), @FINGERCODE VARCHAR(50), @PARTSFINGERCODE VARCHAR(50), @PROCESSID VARCHAR(50), @PROCESSNAME VARCHAR(50), @WORKNUMBER INT, @PLANBEGINDATE date, @PLANENDDATE date, @PERIODDAY VARCHAR(50), @ID VARCHAR(50), @ID1 VARCHAR(50), @I INT, @PROCESSFILEID VARCHAR(50), @ORDERVOUCHERID VARCHAR(500), @CREATOR VARCHAR(50), @PLANWEEK VARCHAR(50), @PRODUCTID1 VARCHAR(50), @PARTSID1 VARCHAR(50), @WORKCENTER VARCHAR(50)     SELECT @PRODUCTID = ID FROM MDM_MATERIALINFO WHERE ID = (SELECT PRODUCTID FROM MPM_PRODUCT WHERE  ID= ?) SELECT @PARTSID =  ID FROM MDM_MATERIALINFO WHERE ID = (SELECT PARTSID FROM( SELECT  C.ID, C.PARTSID AS PARTSID FROM MPM_PRODUCTDETAILS C LEFT  JOIN MDM_MATERIALINFO D ON C.PARTSID = D.ID UNION ALL SELECT  A.ID, A.PRODUCTID AS PARTSID FROM MPM_PRODUCT A LEFT  JOIN MDM_MATERIALINFO B ON A.PRODUCTID = B.ID ) E WHERE E.ID = ?)   SELECT PARTSNAME  = MATERIALNAME FROM  MDM_MATERIALINFO WHERE ID = @PARTSID   SELECT @PRODUCTCODEID =  PRODUCTCODEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID SELECT @PRODUCTCODE = VALUESNAME FROM MDM_DATADICTIONARYDETAILS WHERE ID=(SELECT PRODUCTCODEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID) SELECT @STAGEID = STAGEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID SELECT @STAGE = VALUESNAME FROM MDM_DATADICTIONARYDETAILS WHERE ID=(SELECT STAGEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID) SELECT @SERIALNUMBER = SERIALNUMBER FROM MPM_MPF WHERE ID = ? SELECT @FINGERCODE =  FIGURENUMBER  FROM MDM_MATERIALINFO WHERE ID = (SELECT PRODUCTID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID) SELECT @PARTSFINGERCODE =  FIGURENUMBER FROM MDM_MATERIALINFO WHERE ID = (SELECT PARTSID FROM( SELECT  C.ID, C.PARTSID AS PARTSID FROM MPM_PRODUCTDETAILS C LEFT  JOIN MDM_MATERIALINFO D ON C.PARTSID = D.ID UNION ALL SELECT  A.ID, A.PRODUCTID AS PARTSID FROM MPM_PRODUCT A LEFT  JOIN MDM_MATERIALINFO B ON A.PRODUCTID = B.ID ) E WHERE E.ID = @PARTSID)    SET @PROCESSFILEID = ? SELECT @PROCESSID  =  ID FROM  MPM_PF WHERE MPFID = @PROCESSFILEID AND BASEPROCESSID = ? SELECT @PROCESSNAME =  PROCESSNAME  FROM MDM_PROCESS WHERE ID = ?     SET @WORKNUMBER = ? SET @WORKNUMBER = CAST(@WORKNUMBER AS INT)  SET @PLANBEGINDATE = ? SET @PLANWEEK = CAST(DATEPART(YEAR,@PLANBEGINDATE )AS VARCHAR(50))+'-'+CAST(DATEPART(WEEK,@PLANBEGINDATE)AS VARCHAR(50)) SET @PLANENDDATE = ? SET @PERIODDAY = ? SET @ID = LEFT(NEWID(),50) SET @ID1 = LEFT(NEWID(),50) SET @I  = 1 SET @CREATOR = ?  IF(@WORKNUMBER = '') BEGIN  SET @WORKNUMBER = ISNULL(@WORKNUMBER,0) END  INSERT INTO MES_WORKORDER (ID,PRODUCTID,TASKNAME,PLANWEEK,PRODUCTNAME,PARTSID,PARTSNAME,FINGERCODE,PARTSFINGERCODE,PRODUCTCODEID,PRODUCTCODE,STAGEID,STAGE,SERIALNUMBER,NODETYPE,CREATOR,CREATE_DATE) values (@ID,@PRODUCTID,@PARTSNAME,@PLANWEEK,@PRODUCTNAME,@PARTSID,@PARTSNAME,@FINGERCODE,@PARTSFINGERCODE,@PRODUCTCODEID,@PRODUCTCODE,@STAGEID,@STAGE,@SERIALNUMBER,1,@CREATOR,GETDATE ()) INSERT INTO MES_WORKORDER (ID,PROCESSID,TASKNAME,PROCESSNAME,WORKNUMBER,PLANBEGINDATE,PLANENDDATE,PERIODDAY,PARENT_ID,ORDERSTATUS,ORDERVOUCHERID,NODETYPE,CREATOR,PROCESSFILEID) VALUES (@ID1,@PROCESSID,@PROCESSNAME,@PROCESSNAME,@WORKNUMBER,@PLANBEGINDATE,@PLANENDDATE,@PERIODDAY,@ID,0,'20181111',2,@CREATOR,@PROCESSFILEID)   WHILE(@I <= @WORKNUMBER) BEGIN  INSERT INTO MES_WORKORDERSINGLECODE  (ID,WORKORDERID,PRODUCTSTATUS,CHECKSTATUS)  VALUES  (LEFT(NEWID(),50),@ID1,1,1)  SET @I=@I+1 END  SELECT @WORKCENTER = PARENT_ID FROM SYS_DEPT WHERE ID = (SELECT DEPT_ID FROM SYS_USER WHERE ID = @CREATOR)  INSERT INTO MES_WORKINPROCESS (ID,TASKCODE,PARTSID,PRODUCTID,PRODUCTTYPE,DEPARTMENTID,TASKID,PRODUCTCODEID,STAGEID,SERIALNUMBER,TOTALQUANTITY,PLANEDQUANTITY,UNPLANEDQUANTITY,FINISHEDQUANTITY,UNFINISHEDQUANTITY) VALUES (LEFT(NEWID(),50),'BZZZ-2019-01-07',@PARTSID,@PRODUCTID,'91416aeb6e7845c0ae21421432d4e30d',@WORKCENTER,@ID,@PRODUCTCODEID,@STAGEID,@SERIALNUMBER,@WORKNUMBER,@WORKNUMBER,0,0,10)\n";
		
		String sqls = "";
		sqls += "  DECLARE\n"; 
		sqls += "  @PRODUCTID VARCHAR(50),\n"; 
		sqls += "  @PRODUCTNAME VARCHAR(50),\n"; 
		sqls += "  @PARTSID VARCHAR(50),\n"; 
		sqls += "  @PARTSNAME VARCHAR(50),\n"; 
		sqls += "  @PRODUCTCODEID VARCHAR(50),\n"; 
		sqls += "  @PRODUCTCODE VARCHAR(50),\n"; 
		sqls += "  @STAGEID VARCHAR(50),\n"; 
		sqls += "  @STAGE VARCHAR(50),\n"; 
		sqls += "  @SERIALNUMBER VARCHAR(50),\n"; 
		sqls += "  @FINGERCODE VARCHAR(50),\n"; 
		sqls += "  @PARTSFINGERCODE VARCHAR(50),\n"; 
		sqls += "  @PROCESSID VARCHAR(50),\n"; 
		sqls += "  @PROCESSNAME VARCHAR(50),\n"; 
		sqls += "  @WORKNUMBER INT,\n"; 
		sqls += "  @PLANBEGINDATE date,\n"; 
		sqls += "  @PLANENDDATE date,\n"; 
		sqls += "  @PERIODDAY VARCHAR(50),\n"; 
		sqls += "  @ID VARCHAR(50),\n"; 
		sqls += "  @ID1 VARCHAR(50),\n"; 
		sqls += "  @I INT,\n"; 
		sqls += "  @PROCESSFILEID VARCHAR(50),\n"; 
		sqls += "  @ORDERVOUCHERID VARCHAR(500),\n"; 
		sqls += "  @CREATOR VARCHAR(50),\n"; 
		sqls += "  @PLANWEEK VARCHAR(50),\n"; 
		sqls += "  @PRODUCTID1 VARCHAR(50),\n"; 
		sqls += "  @PARTSID1 VARCHAR(50),\n"; 
		sqls += "  @WORKCENTER VARCHAR(50)\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  SELECT @PRODUCTID = ID FROM MDM_MATERIALINFO WHERE ID = (SELECT PRODUCTID FROM MPM_PRODUCT WHERE  ID= 'a661e25ac5c040bba127ca08d4941f85')\n"; 
		sqls += "  SELECT @PARTSID =  ID FROM MDM_MATERIALINFO WHERE ID = (SELECT PARTSID FROM(\n"; 
		sqls += "  SELECT \n"; 
		sqls += "  C.ID,\n"; 
		sqls += "  C.PARTSID AS PARTSID\n"; 
		sqls += "  FROM MPM_PRODUCTDETAILS C\n"; 
		sqls += "  LEFT  JOIN MDM_MATERIALINFO D\n"; 
		sqls += "  ON C.PARTSID = D.ID\n"; 
		sqls += "  UNION ALL\n"; 
		sqls += "  SELECT \n"; 
		sqls += "  A.ID,\n"; 
		sqls += "  A.PRODUCTID AS PARTSID\n"; 
		sqls += "  FROM MPM_PRODUCT A\n"; 
		sqls += "  LEFT  JOIN MDM_MATERIALINFO B\n"; 
		sqls += "  ON A.PRODUCTID = B.ID\n"; 
		sqls += "  ) E WHERE E.ID = '485e20eda7bf4068bf9aafbbb3b55090')\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
		sqls += "  SELECT @PARTSNAME  = MATERIALNAME FROM  MDM_MATERIALINFO WHERE ID = @PARTSID\n"; 
		sqls += "  \n"; 
		sqls += "  \n"; 
//		sqls += "  SELECT @PRODUCTCODEID =  PRODUCTCODEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID\n"; 
//		sqls += "  SELECT @PRODUCTCODE = VALUESNAME FROM MDM_DATADICTIONARYDETAILS WHERE ID=(SELECT PRODUCTCODEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID)\n"; 
//		sqls += "  SELECT @STAGEID = STAGEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID\n"; 
//		sqls += "  SELECT @STAGE = VALUESNAME FROM MDM_DATADICTIONARYDETAILS WHERE ID=(SELECT STAGEID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID)\n"; 
//		sqls += "  SELECT @SERIALNUMBER = SERIALNUMBER FROM MPM_MPF WHERE ID = '923ff78943d1413db5f3a2850b18f1cf'\n"; 
//		sqls += "  SELECT @FINGERCODE =  FIGURENUMBER  FROM MDM_MATERIALINFO WHERE ID = (SELECT PRODUCTID FROM MPM_PRODUCT WHERE  ID= @PRODUCTID)\n"; 
//		sqls += "  SELECT @PARTSFINGERCODE =  FIGURENUMBER FROM MDM_MATERIALINFO WHERE ID = (SELECT PARTSID FROM(\n"; 
//		sqls += "  SELECT \n"; 
//		sqls += "  C.ID,\n"; 
//		sqls += "  C.PARTSID AS PARTSID\n"; 
//		sqls += "  FROM MPM_PRODUCTDETAILS C\n"; 
//		sqls += "  LEFT  JOIN MDM_MATERIALINFO D\n"; 
//		sqls += "  ON C.PARTSID = D.ID\n"; 
//		sqls += "  UNION ALL\n"; 
//		sqls += "  SELECT \n"; 
//		sqls += "  A.ID,\n"; 
//		sqls += "  A.PRODUCTID AS PARTSID\n"; 
//		sqls += "  FROM MPM_PRODUCT A\n"; 
//		sqls += "  LEFT  JOIN MDM_MATERIALINFO B\n"; 
//		sqls += "  ON A.PRODUCTID = B.ID\n"; 
//		sqls += "  ) E WHERE E.ID = @PARTSID)\n"; 
//		sqls += "  \n"; 
//		sqls += "  \n"; 
//		sqls += "  \n"; 
//		sqls += "  SET @PROCESSFILEID = '923ff78943d1413db5f3a2850b18f1cf'\n"; 
//		sqls += "  SELECT @PROCESSID  =  ID FROM  MPM_PF WHERE MPFID = @PROCESSFILEID AND BASEPROCESSID = 'a661e25ac5c040bba127ca08d4941f85'\n"; 
//		sqls += "  SELECT @PROCESSNAME =  PROCESSNAME  FROM MDM_PROCESS WHERE ID = 'a661e25ac5c040bba127ca08d4941f85'\n"; 
//		sqls += "  \n"; 
//		sqls += "  \n"; 
//		sqls += "  \n"; 
//		sqls += "  \n"; 
//		sqls += "  SET @WORKNUMBER = '10'\n"; 
//		sqls += "  SET @WORKNUMBER = CAST(@WORKNUMBER AS INT) \n"; 
//		sqls += "  SET @PLANBEGINDATE = '2019-01-01'\n"; 
//		sqls += "  SET @PLANWEEK = CAST(DATEPART(YEAR,@PLANBEGINDATE )AS VARCHAR(50))+'-'+CAST(DATEPART(WEEK,@PLANBEGINDATE)AS VARCHAR(50))\n"; 
//		sqls += "  SET @PLANENDDATE = '2019-01-07'\n"; 
//		sqls += "  SET @PERIODDAY = '8'\n"; 
//		sqls += "  SET @ID = LEFT(NEWID(),50)\n"; 
//		sqls += "  SET @ID1 = LEFT(NEWID(),50)\n"; 
//		sqls += "  SET @I  = 1\n"; 
//		sqls += "  SET @CREATOR = '9d00696c3245463698fc1e9c61da44db'\n"; 
//		sqls += "  \n"; 
//		sqls += "  IF(@WORKNUMBER = '')\n"; 
//		sqls += "  BEGIN\n"; 
//		sqls += "   SET @WORKNUMBER = ISNULL(@WORKNUMBER,0)\n"; 
//		sqls += "  END\n"; 
//		sqls += "  \n"; 
//		sqls += "  INSERT INTO MES_WORKORDER\n"; 
//		sqls += "  (ID,PRODUCTID,TASKNAME,PLANWEEK,PRODUCTNAME,PARTSID,PARTSNAME,FINGERCODE,PARTSFINGERCODE,PRODUCTCODEID,PRODUCTCODE,STAGEID,STAGE,SERIALNUMBER,NODETYPE,CREATOR,CREATE_DATE)\n"; 
//		sqls += "  values\n"; 
//		sqls += "  (@ID,@PRODUCTID,@PARTSNAME,@PLANWEEK,@PRODUCTNAME,@PARTSID,@PARTSNAME,@FINGERCODE,@PARTSFINGERCODE,@PRODUCTCODEID,@PRODUCTCODE,@STAGEID,@STAGE,@SERIALNUMBER,1,@CREATOR,GETDATE ())\n"; 
//		sqls += "  INSERT INTO MES_WORKORDER\n"; 
//		sqls += "  (ID,PROCESSID,TASKNAME,PROCESSNAME,WORKNUMBER,PLANBEGINDATE,PLANENDDATE,PERIODDAY,PARENT_ID,ORDERSTATUS,ORDERVOUCHERID,NODETYPE,CREATOR,PROCESSFILEID)\n"; 
//		sqls += "  VALUES\n"; 
//		sqls += "  (@ID1,@PROCESSID,@PROCESSNAME,@PROCESSNAME,@WORKNUMBER,@PLANBEGINDATE,@PLANENDDATE,@PERIODDAY,@ID,0,'20181111',2,@CREATOR,@PROCESSFILEID)\n"; 
//		sqls += "  \n"; 
//		sqls += "  \n"; 
//		sqls += "  WHILE(@I <= @WORKNUMBER)\n"; 
//		sqls += "  BEGIN\n"; 
//		sqls += "   INSERT INTO MES_WORKORDERSINGLECODE\n"; 
//		sqls += "   (ID,WORKORDERID,PRODUCTSTATUS,CHECKSTATUS)\n"; 
//		sqls += "   VALUES\n"; 
//		sqls += "   (LEFT(NEWID(),50),@ID1,1,1)\n"; 
//		sqls += "   SET @I=@I+1\n"; 
//		sqls += "  END\n"; 
//		sqls += "  \n"; 
//		sqls += "  SELECT @WORKCENTER = PARENT_ID FROM SYS_DEPT WHERE ID = (SELECT DEPT_ID FROM SYS_USER WHERE ID = @CREATOR)\n"; 
//		sqls += "  \n"; 
//		sqls += "  INSERT INTO MES_WORKINPROCESS\n"; 
//		sqls += "  (ID,TASKCODE,PARTSID,PRODUCTID,PRODUCTTYPE,DEPARTMENTID,TASKID,PRODUCTCODEID,STAGEID,SERIALNUMBER,TOTALQUANTITY,PLANEDQUANTITY,UNPLANEDQUANTITY,FINISHEDQUANTITY,UNFINISHEDQUANTITY)\n"; 
//		sqls += "  VALUES\n"; 
//		sqls += "  (LEFT(NEWID(),50),'BZZZ-2018-12-20',@PARTSID,@PRODUCTID,'91416aeb6e7845c0ae21421432d4e30d',@WORKCENTER,@ID,@PRODUCTCODEID,@STAGEID,@SERIALNUMBER,@WORKNUMBER,@WORKNUMBER,0,0,10)\n"; 


		
		return sqls;
	}
}
