package test;

import java.util.HashMap;
import java.util.Map;

import com.king.tooth.web.entity.resulttype.ResponseBody;

public class JsonTest {
	public static void main(String[] args) {
		
		Map<String, Object> m = new HashMap<String, Object>();
		
		ResponseBody r = new ResponseBody(m);
		System.out.println(r);   
	}
}
