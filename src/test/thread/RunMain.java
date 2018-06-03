package test.thread;

public class RunMain {
	public static void main(String[] args) {
		Thread t1 = new Thread(new MyThread1());
		Thread t2 = new Thread(new MyThread2());
		t2.start();
		t1.start();
	}
}
