package test;

import com.alibaba.fastjson.JSONObject;



public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		JSONObject j = new JSONObject();
		j.put("string", "我是字符串");
		j.put("int", 22);
		j.put("float", 2.232);
		
		System.out.println(j.get("string").getClass());
		System.out.println(j.get("int").getClass());
		System.out.println(j.get("x") == null);
		
	}
}
 