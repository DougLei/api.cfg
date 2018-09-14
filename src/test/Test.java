package test;

import com.alibaba.fastjson.JSONObject;


public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		int[] i = {1,2,3};
		System.out.println(JSONObject.toJSON(i));
	}
}
 