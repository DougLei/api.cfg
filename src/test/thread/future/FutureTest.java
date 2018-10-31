package test.thread.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {
	public static void main(String[] args) {
		
		ACallAble aCallAble = new ACallAble();
		FutureTask<Integer> futureTask = new FutureTask<Integer>(aCallAble);
		Thread t = new Thread(futureTask);
		t.start();
		
		try {
			System.out.println(futureTask.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}
