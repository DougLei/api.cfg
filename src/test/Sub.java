package test;

import com.alibaba.fastjson.JSONArray;

public class Sub extends Parent{
	private String name;
	public String getName(){
		return name;
	}
	
	public static void main(String[] args) {
		String sqlScriptParameterJson = "[]";
		JSONArray sqlScriptParametersJsonArray = JSONArray.parseArray(sqlScriptParameterJson );
		System.out.println(sqlScriptParametersJsonArray);
	}
}
