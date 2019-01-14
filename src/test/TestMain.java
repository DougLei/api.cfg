package test;

import com.expression.ExpressionResolver;


public class TestMain {
	public static void main(String[] args) throws Exception {

		Object a = null;
		System.out.println(a+"");
		
		System.out.println('\0');
		
		
		System.out.println(ExpressionResolver.resolve("1+2"));
	}
}
