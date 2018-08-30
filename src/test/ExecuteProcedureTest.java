package test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

public class ExecuteProcedureTest extends Parent{
	
	public static void main(String[] args)  {
//		inTable();
		outTable();
	}

	@SuppressWarnings("unused")
	private static void inTable() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=SmartOneCfg", "sa", "root");
			
			CallableStatement cs = conn.prepareCall("{call inTable(?)}");
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> m1 = new HashMap<String, Object>();
			m1.put("id", "111");
			m1.put("name", "账户1");
			list.add(m1);
			
			Map<String, Object> m2 = new HashMap<String, Object>();
			m2.put("id", "222");
			m2.put("name", "账户2");
			list.add(m2);

			cs.setArray(1, null);
			cs.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
			
			Integer val = cs.getInt(2);
			System.out.println("存储过程的输出参数的值为："+val);
			
			
			ResultSet rs = (ResultSet) cs.getObject(3);
			while(rs.next()){
				System.out.println(rs.getString(3));
			}
			System.out.println("-----------------------------------");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
