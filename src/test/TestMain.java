package test;

import java.util.ArrayList;
import java.util.List;

public class TestMain {
	public static void main(String[] args) throws CloneNotSupportedException {
		
		Parent p1 = new Parent();
		p1.setName("p1");
		List<String> tels = new ArrayList<String>();
		tels.add("11111");
		tels.add("22222");
		p1.setTels(tels);
		
		
		Parent p2 = (Parent) p1.clone();
		p2.getTels().remove(1);
		
		
		System.out.println(p1.getTels());
		System.out.println(p2.getTels());
	}
}
