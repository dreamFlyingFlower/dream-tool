package com.wy.thread;

/**
 * 线程工具类
 *
 * @author 飞花梦影
 * @date 2021-12-30 14:43:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class ThreadTool {

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}