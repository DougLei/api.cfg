package test;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;



public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		String a = "{\"float2\":2.23z,\"float1\":2.z,\"int\":22.2,\"string\":\"我是字符串\",\"bool\": true  }";
		JSONObject j = JSONObject.parseObject(a );
		
		System.out.println(j.get("string")+"==>"+j.get("string").getClass());
		System.out.println(j.get("int")+"==>"+j.get("int").getClass());
		System.out.println(j.get("float1")+"==>"+j.get("float1").getClass());
		System.out.println(j.get("float2")+"==>"+j.get("float2").getClass());
		System.out.println(j.get("bool")+"==>"+j.get("bool").getClass());

		
		System.out.println(j.getString("float2").length());
		System.out.println(((BigDecimal)j.get("float2")).precision());
		
		String dataValueStr = "22.22";
		System.out.println(dataValueStr.substring(dataValueStr.indexOf(".")+1).length());
	}
}
 