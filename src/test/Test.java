package test;

import java.util.ArrayList;
import java.util.List;

import com.king.tooth.sys.entity.cfg.ComSqlScriptParameter;



public class Test {
	public static void main(String[] args) throws CloneNotSupportedException {
		List<ComSqlScriptParameter> sqlParams1 = new ArrayList<ComSqlScriptParameter>(1);
		sqlParams1.add(new ComSqlScriptParameter());
		List<ComSqlScriptParameter> sqlParams = new ArrayList<ComSqlScriptParameter>(0);
		
		for (ComSqlScriptParameter p1 : sqlParams1) {
			System.out.println(p1);
			for (ComSqlScriptParameter p2 : sqlParams) {
				System.out.println(p1.equals(p2));
			}
		}
		
		
	}

}
 