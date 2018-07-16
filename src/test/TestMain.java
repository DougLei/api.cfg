package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.tooth.sys.entity.common.sqlscript.SqlScriptParameterNameRecord;


public class TestMain {
	public static void main(String[] args) {
		
		List<SqlScriptParameterNameRecord> parameterNameRecordList = new ArrayList<SqlScriptParameterNameRecord>();
		parameterNameRecordList.add(new SqlScriptParameterNameRecord(1));
		parameterNameRecordList.add(new SqlScriptParameterNameRecord(2));
		
		parameterNameRecordList.get(0).addParameterName("11111");
		parameterNameRecordList.get(0).addParameterName("222");
		
		SqlScriptParameterNameRecord p = parameterNameRecordList.get(0);
		System.out.println(p.getParameterNames());
		
		
		Map<Integer, List<String>> parameterNameRecordMap = new HashMap<Integer, List<String>>(10);
		
		for (SqlScriptParameterNameRecord pnr : parameterNameRecordList) {
			parameterNameRecordMap.put(pnr.getSqlIndex(), pnr.getParameterNames());
			pnr.getParameterNames().clear();
		}
		parameterNameRecordList.clear();
		
		System.out.println(p.getParameterNames());
		System.out.println(parameterNameRecordMap);
	}
}
