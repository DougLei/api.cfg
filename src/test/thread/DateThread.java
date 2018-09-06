package test.thread;

import java.util.concurrent.ThreadLocalRandom;

public class DateThread extends Thread{

	public void run() {
		doRan();
	}

	private synchronized void doRan() {
//		Random r = new Random(10000l);
//		System.out.println(r.nextInt());
		
		System.out.println(Math.abs(ThreadLocalRandom.current().nextInt()));
		
	}
	
	
	
}
