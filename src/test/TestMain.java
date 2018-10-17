package test;

import com.king.tooth.sys.entity.cfg.ComSqlScript;
import com.king.tooth.util.JsonUtil;



public class TestMain {
	public static void main(String[] args) {
		String a = "{\"isCoverSqlObject\":true}";
		ComSqlScript s = JsonUtil.parseObject(a, ComSqlScript.class);
		System.out.println(s.getIsCoverSqlObject());
		
		
		System.out.println(JsonUtil.toJsonString(s, true));
	}
}
