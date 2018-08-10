package test;

import java.util.Date;

import com.king.tooth.util.DateUtil;

public class TestMain {
	public static void main(String[] args) {
	
		
		Object firstDay = DateUtil.formatDate(new Date(), "d");
		System.out.println(firstDay);
		System.out.println("01".equals(firstDay));
		System.out.println("1".equals(firstDay));
		
		
	}
}
