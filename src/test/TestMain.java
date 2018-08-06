package test;

import java.util.ArrayList;
import java.util.List;



public class TestMain {
	public static void main(String[] args) {
		List<List<Object>> o = new ArrayList<List<Object>>(10);
		System.out.println(o.size());
		
		System.out.println(o.get(0));
	}
	
}
