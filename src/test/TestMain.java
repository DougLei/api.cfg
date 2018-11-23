package test;


public class TestMain {
	public static void main(String[] args) throws CloneNotSupportedException {
		String[] a = ",abc".split(",");
		System.out.println(a[0].equals(""));
		System.out.println(a[1]);
	}
}
