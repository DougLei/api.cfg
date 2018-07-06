package test;

import com.king.tooth.plugins.thread.CurrentThreadContext;
import com.king.tooth.util.hibernate.HibernateUtil;

public class HibernateTest {

	public static void main(String[] args) {
		SpringApplication.getApplicationContext();
		CurrentThreadContext.setDatabaseId("447f1ef0272847018f9df0e9edc48d37");
		HibernateUtil.executeListQueryByHql("100", 2+"", "select hbmContent from ComHibernateHbm where isEnabled = 1", null);
		
		
		
//		String sql = "select count(id) from ComTabledata";
//		System.out.println(HibernateUtil.executeUniqueQueryByHql(sql, null));;
		
		
//		ComTabledata table = new ComTabledata("COM_SYS_RESOURCE");
//		JSONObject json = table.toEntityJson();
		
		
//		HibernateUtil.saveObject(table.getEntityName(), json, null);
//		
//		HibernateUtil.commitTransaction();
//		HibernateUtil.closeCurrentThreadSession();
		
//		List<ProcedureSqlScriptParameter> pssp = new ArrayList<ProcedureSqlScriptParameter>();
//		ProcedureSqlScriptParameter p1 = new ProcedureSqlScriptParameter(
//				BuiltinDatabaseData.DB_TYPE_SQLSERVER, 1, "mynameIn", "varchar", 0);
//		p1.setActualValue("哈哈");
//		ProcedureSqlScriptParameter p2 = new ProcedureSqlScriptParameter(
//				BuiltinDatabaseData.DB_TYPE_SQLSERVER, 2, "myageOut", "varchar", 4);
//		pssp.add(p1);
//		pssp.add(p2);
//		
//		for (ProcedureSqlScriptParameter p : pssp) {
//			System.out.println(p.getParameterName() +":" + p.getOutValue());
//		}
//		HibernateUtil.executeProcedure("myprocedures", pssp);
//		System.out.println("----------------------------");
//		for (ProcedureSqlScriptParameter p : pssp) {
//			System.out.println(p.getParameterName() +":" + p.getOutValue());
//		}
		
		
//		List<ProcedureSqlScriptParameter> pssp = new ArrayList<ProcedureSqlScriptParameter>();
//		ProcedureSqlScriptParameter p1 = new ProcedureSqlScriptParameter(
//				BuiltinDatabaseData.DB_TYPE_ORACLE, 1, "c", "varchar2", 1);
//		p1.setActualValue("哈哈");
//		ProcedureSqlScriptParameter p2 = new ProcedureSqlScriptParameter(
//				BuiltinDatabaseData.DB_TYPE_ORACLE, 2, "b", "varchar2", 2);
//		ProcedureSqlScriptParameter p3 = new ProcedureSqlScriptParameter(
//				BuiltinDatabaseData.DB_TYPE_ORACLE, 3, "d", "varchar2", 3);
//		p3.setActualValue("我把只给了p2");
//		pssp.add(p1);
//		pssp.add(p2);
//		pssp.add(p3);
//		for (ProcedureSqlScriptParameter p : pssp) {
//			System.out.println(p.getParameterName() +":" + p.getOutValue());
//		}
//		Map<String, Object> data = HibernateUtil.executeProcedure("myprocedure", pssp);
//		System.out.println("----------------------------");
//		Set<String> keys = data.keySet();
//		for (String k : keys) {
//			System.out.println(k +":" +data.get(k));
//		}
	}
}
