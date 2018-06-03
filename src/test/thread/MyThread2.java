package test.thread;

public class MyThread2 implements Runnable{

	public void run() {
		ThreadLocalTest.setContent("2222222222222222");
//		try {
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
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
