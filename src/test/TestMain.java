package test;


public class TestMain {
	public static void main(String[] args) {
	
		
		String a = "sysManager";
		String b = "sysManager_user";
		
		System.out.println(b.startsWith(a));
		System.out.println(b.startsWith(b));
		System.out.println(b.equals(b));
	}
}
