package test;

import com.king.tooth.util.CryptographyUtil;


public class Test {
	
	public static void main(String[] args) throws CloneNotSupportedException {
		
		int i = 0;
		System.out.println(i++);
		System.out.println(i++);
		
		System.out.println(99/100);
		System.out.println(99%100);
		
		System.out.println(CryptographyUtil.encodeMd5("smartone", "1QaZ2wSx,."));
	}
}
 