package test;

import com.king.tooth.plugins.alibaba.json.extend.string.IJson;
import com.king.tooth.plugins.alibaba.json.extend.string.IJsonUtil;

public class TestMain {
	public static void main(String[] args) {
		
		String a = "[{'name':'哈哈'},[{'age':22},{'age':27}]]";
		IJson ijson = IJsonUtil.getIJson(a);
		
		IJson ijson1 = ijson.getIJson(0);
		System.out.println(ijson1);
		System.out.println(ijson1.isArray());
		System.out.println(ijson1.isObject());
		
		
	}
}
