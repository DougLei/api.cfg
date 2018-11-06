package com.king.tooth.thread.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * @author DougLei
 */
public class ThreadPool {
	/**
	 * 线程池
	 */
	private static final ThreadPoolExecutor threadPoolExecutor;
	static{
		threadPoolExecutor = new ThreadPoolExecutor(30, 100, 10l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * 运行线程
	 * @param runnable
	 */
	public static void execute(Runnable runnable){
		threadPoolExecutor.execute(runnable);
	}
}
