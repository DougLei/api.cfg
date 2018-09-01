package test;

import com.alibaba.fastjson.JSONObject;
import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;

public class Test {
	public static void main(String[] args) throws CloneNotSupportedException {
		Object o = "  [{\"doubleVal\":12.1,\"name\":\"哈哈\",\"age\":22,\"time\":\"2018-01-01\",\"time2\":\"2018-2-2 12:12:12\"},{\"doubleVal\":12.2,\"name\":\"呵呵\",\"age\":33,\"time\":\"2018-03-03\",\"time2\":\"2018-4-4 12:12:12\"}]";
		IJson ijson = IJsonUtil.getIJson(o.toString());
		
		
		for (int i=0;i<ijson.size();i++) {
			JSONObject json = ijson.get(i);
			System.out.println(json);
			
			System.out.println(json.get("a"));
		}
		
	}

}
 