package test;

import java.util.ArrayList;
import java.util.List;


public class TestMain {
	public static void main(String[] args) {
		
		List<Object> a = new ArrayList<Object>();
		a.add("4c024427c47c46be953efdb564e87cf1");
		a.add("6d2b6fa4167844ae8596211900c7a924");
		a.add("0ee3724673ca4db781c83c433b7bcb53");
		a.add("113b755c7b094421bccc1d9062b687e5");
		a.add("041c89eca5d64d419a958d8ea15b12a2");
		System.out.println(a);
		
		List<Object> b = new ArrayList<Object>();
		
		String[] ar = {"6d2b6fa4167844ae8596211900c7a924", "113b755c7b094421bccc1d9062b687e5"};
		for (String t : ar) {
			if(a.contains(t)){
				b.add(t);
				a.remove(t);
			}
		}
		
		if(a.size() > 0){
			System.out.println(a.size());
			b.addAll(a);
			a.clear();
		}
		System.out.println(b);
		System.out.println(a);
		
	}
}
