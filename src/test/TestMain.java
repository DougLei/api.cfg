package test;

import com.king.tooth.util.ResourceHandlerUtil;


public class TestMain {
	public static void main(String[] args) throws CloneNotSupportedException {
		Object o = ResourceHandlerUtil.getRandom(1000);
		System.out.println(o.toString());
	}
}
