package test;

public class TestMain {
	public static void main(String[] args) {
		
		Integer i = new Integer(1);
		System.out.println(i);
		increment(i);
		System.out.println(i);
		
		System.out.println("PK_SYS_ACCOUNT_ONLINE_STATUS_ID".length());
		
		
	}
	
	public static void increment(Integer i){
		i = i+1;
	}
}
