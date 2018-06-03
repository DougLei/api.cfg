package test.thread;

public class MyThread1 implements Runnable{

	public void run() {
		ThreadLocalTest.setContent("11111111111111");
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
		System.out.println(Thread.currentThread().getName() + ":" + ThreadLocalTest.getContent());
	}
}
