package com.wy.util;

/**
 * 监视一个线程,在本线程到达指定的时间时中断它
 * 
 * @author 飞花梦影
 * @date 2021-03-08 09:56:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ThreadMonitor implements Runnable {

	/** 需要监控的线程 */
	private final Thread thread;

	/** 超时时间,达到该时间后打断监控的线程 */
	private final long timeout;

	/**
	 * 构造
	 *
	 * @param thread 需要监控的线程
	 * @param timeout 超时时间,毫秒
	 */
	private ThreadMonitor(Thread thread, long timeout) {
		this.thread = thread;
		this.timeout = timeout;
	}

	/**
	 * 开始监控当前线程
	 *
	 * @param timeout 监控当前线程时间,小于等于0不监控
	 * @return 当前监控线程{@link ThreadMonitor},若timeout小于等于0,当前监控线程为null
	 */
	public static Thread start(long timeout) {
		return start(Thread.currentThread(), timeout);
	}

	/**
	 * 开始监控指定线程
	 *
	 * @param thread 需要监控的指定线程
	 * @param timeout 监控当前线程时间,小于等于0不监控
	 * @return 当前监控线程{@link ThreadMonitor},若timeout小于等于0,当前监控线程为null
	 */
	public static Thread start(Thread thread, long timeout) {
		Thread monitor = null;
		if (timeout > 0) {
			ThreadMonitor timout = new ThreadMonitor(thread, timeout);
			monitor = new Thread(timout, ThreadMonitor.class.getSimpleName());
			monitor.setDaemon(true);
			monitor.start();
		}
		return monitor;
	}

	/**
	 * 打断指定线程
	 *
	 * @param thread 指定的线程
	 */
	public static void stop(Thread thread) {
		if (thread != null) {
			thread.interrupt();
		}
	}

	/**
	 * 睡眠线程直到指定的时间,之后打断被监控的线程
	 */
	@Override
	public void run() {
		try {
			Thread.sleep(timeout);
			if (!thread.isInterrupted()) {
				thread.interrupt();
			}
		} catch (InterruptedException e) {
			// timeout not reached
		}
	}
}