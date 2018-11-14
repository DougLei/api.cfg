package test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;

public class TestMain {
	public static void main(String[] args) {
		
		JSONArray jr =new JSONArray();
		jr.add(new JSONObject());
		jr.getJSONObject(0).put("1", "1");
		jr.add(new JSONObject());
		jr.getJSONObject(1).put("2", "2");
		
		d(jr);
		System.out.println(jr);
		
		System.out.println(IJsonUtil.getIJson("dfdfadsf"));
		
	}
	
	public static void d(JSONArray jr){
		JSONObject j = jr.getJSONObject(0);
		JSONObject jj = new JSONObject();
		jj.put("333", 333);
		j = jj;
		System.out.println(j);
	}
}
