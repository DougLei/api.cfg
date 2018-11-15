package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestMain {
	public static void main(String[] args) {
		String[] a= ",ddd".split(",");
		System.out.println(a[0] == null);
		System.out.println(a[1]);
		
	}
	
	public static void d(JSONArray jr){
		JSONObject j = jr.getJSONObject(0);
		JSONObject jj = new JSONObject();
		jj.put("333", 333);
		j = jj;
		System.out.println(j);
	}
}
