package test;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;


public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		int[] i = {1,2,3};
		System.out.println(i);
		String a = "[1,2,3]";
		
		
		List<Integer> arr = JSONArray.parseArray(a, int.class);
		Integer[] b = new Integer[arr.size()];
		arr.toArray(b);
		arr.clear();
		System.out.println(Arrays.toString(b));
	}
}
 