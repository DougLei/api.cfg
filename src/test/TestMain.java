package test;




public class TestMain {
	public static void main(String[] args) {
		StringBuilder a = new StringBuilder("123");
		a.insert(0, "aa").append("bb");
		System.out.println(a);
	}
	
}
