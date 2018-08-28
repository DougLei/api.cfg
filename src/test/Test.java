package test;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;



public class Test {
	public static void main(String[] args) throws CloneNotSupportedException {
		List<ComSqlScriptParameter> origin = new ArrayList<ComSqlScriptParameter>();
		origin.add(new ComSqlScriptParameter());
		origin.add(new ComSqlScriptParameter());
		
		System.out.println(origin);
		
		
		List<ComSqlScriptParameter> to = new ArrayList<ComSqlScriptParameter>();
		for (ComSqlScriptParameter p : origin) {
			to.add((ComSqlScriptParameter) p.clone());
		}
		
		System.out.println(to);
	}

}
 