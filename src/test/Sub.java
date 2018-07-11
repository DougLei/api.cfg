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
		a.add("a_22222");
		System.out.println(a.toString().replace("[", "").replace("]", ""));
	}
}
