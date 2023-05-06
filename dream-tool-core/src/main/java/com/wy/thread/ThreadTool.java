package com.wy.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程工具类
 *
 * @author 飞花梦影
 * @date 2021-12-30 14:43:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ThreadTool {

	private static final int DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

	private static final Long DEFAULT_KEEP_ALIVE_TIME = 60L;

	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

	private static final int DEFAULT_QUEUE_SIZE = 100;

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static ExecutorService newFixedThreadPool() {
		return newFixedThreadPool(DEFAULT_CORE_POOL_SIZE);
	}

	public static ExecutorService newFixedThreadPool(int corePoolSize) {
		return newFixedThreadPool(corePoolSize, corePoolSize);
	}

	public static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize) {
		return newFixedThreadPool(corePoolSize, maximumPoolSize, DEFAULT_QUEUE_SIZE);
	}

	public static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize, int queueSize) {
		return newFixedThreadPool(corePoolSize, maximumPoolSize, queueSize, DEFAULT_KEEP_ALIVE_TIME);
	}

	public static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize, int queueSize,
			long keepAliveTime) {
		return newFixedThreadPool(corePoolSize, maximumPoolSize, queueSize, keepAliveTime, DEFAULT_TIME_UNIT);
	}

	public static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize, int queueSize,
			long keepAliveTime, TimeUnit timeUnit) {
		corePoolSize = corePoolSize <= 0 ? DEFAULT_CORE_POOL_SIZE : corePoolSize;
		maximumPoolSize = maximumPoolSize <= 0 ? DEFAULT_CORE_POOL_SIZE : maximumPoolSize;
		maximumPoolSize = Math.max(corePoolSize, maximumPoolSize);
		queueSize = queueSize <= 0 ? DEFAULT_QUEUE_SIZE : queueSize;
		keepAliveTime = keepAliveTime <= 0 ? DEFAULT_KEEP_ALIVE_TIME : keepAliveTime;
		timeUnit = timeUnit == null ? DEFAULT_TIME_UNIT : timeUnit;
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
				new LinkedBlockingQueue<Runnable>(queueSize));
	}
}