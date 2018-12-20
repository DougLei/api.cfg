package test;

import java.util.HashMap;
import java.util.Map;


public class TestMain {
	public static void main(String[] args) throws CloneNotSupportedException {
		Map<String, String> s = new HashMap<String, String>();
		s.put("1", "1111111");
		
		
		Map<String, Object> s1 = new HashMap<String, Object>(s);
		
		System.out.println(s);
		System.out.println(s1);
		s.clear();
		System.out.println(s);
		System.out.println(s1);
		
	}
}
