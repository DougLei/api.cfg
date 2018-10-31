package test.thread.future;

import java.util.concurrent.Callable;

public class ACallAble implements Callable<Integer>{

	public Integer call() throws Exception {
		return 1;
	}
}
