package test;

import java.util.ArrayList;
import java.util.List;

public class Parent implements Cloneable{
	private String name;
	private List<String> tels;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getTels() {
		return tels;
	}
	public void setTels(List<String> tels) {
		this.tels = tels;
	}
	
	public Object clone() throws CloneNotSupportedException {
		Parent p = (Parent) super.clone();
		p.tels = new ArrayList<String>();
		for (String tel : tels) {
			p.tels.add(tel);
		}
		return p;
	}
}
