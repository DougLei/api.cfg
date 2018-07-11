package test;

import java.util.ArrayList;
import java.util.List;

public class Sub extends Parent{
	private String name;
	public String getName(){
		return name;
	}
	
	public static void main(String[] args) {
		
		List<String> a = new ArrayList<String>();
		a.add("a_1111");
		List<String> b = new ArrayList<String>(a);
		System.out.println(a);
		b.clear();
		System.out.println(a);
		System.out.println(b);
	}
}
