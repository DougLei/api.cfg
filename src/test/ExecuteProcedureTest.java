package test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import oracle.jdbc.OracleTypes;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

public class ExecuteProcedureTest extends Parent{
	
	public static void main(String[] args)  {
		inSqlServerTable();
//		outTable();
	}

	private static void inSqlServerTable() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=SmartOneCfg", "sa", "root");
			
			CallableStatement cs = conn.prepareCall("{call inTable(?)}");
			
			// 组装sqlserver表对象
			SQLServerDataTable table = new SQLServerDataTable();
			table.addColumnMetadata("id", Types.VARCHAR);
			table.addColumnMetadata("birthday", Types.TIMESTAMP);
			table.addColumnMetadata("age", Types.INTEGER);
			table.addColumnMetadata("score", Types.DECIMAL);
			
			
			
			table.addRow("1", new Timestamp(new Date().getTime()), 20, 22.3);
			
			SQLServerCallableStatement scs = (SQLServerCallableStatement) cs;
			scs.setStructured(1, "AccOuntTYpE", table);
			
			scs.execute();
			
			ResultSet rs = scs.getResultSet();
			while(rs.next()){
				System.out.println(rs.getObject(1)+"   "+rs.getObject(2)+"   "+rs.getObject(3)+"   "+rs.getObject(4));
				System.out.println(rs.getObject(1).getClass()+"   "+rs.getObject(2).getClass()+"   "+rs.getObject(3).getClass()+"   "+rs.getObject(4).getClass());
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void outTable() {
		try {
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=SmartOneCfg", "sa", "root");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:V6OA", "pt6", "cape");
			
			CallableStatement cs = conn.prepareCall("{call outTable(?,?,?)}");
			cs.setInt(1, 10);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, OracleTypes.CURSOR);

			cs.execute();
			
			// 要先输出结果，再输出存储过程的输出值，反过来会获得不到数据集
			System.out.println("存储过程的返回数据集为：");
			
//			ResultSet rs = cs.getResultSet();
//			while(rs != null){
//				while(rs.next()){
//					System.out.println(rs.getString(1));
//				}
//				System.out.println("-----------------------------------");
//				cs.getMoreResults();
//				rs = cs.getResultSet();
//			}
			
			ResultSet rs = (ResultSet) cs.getObject(3);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int len = rsmd.getColumnCount();
			for(int i=1;i<=len;i++){
				System.out.println(rsmd.getColumnName(i));
			}
			
			while(rs.next()){
				System.out.println(rs.getString(3));
			}
			System.out.println("-----------------------------------");
			
			
			Integer val = cs.getInt(2);
			System.out.println("存储过程的输出参数的值为："+val);
			
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
