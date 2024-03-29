package com.dream.nio;

/**
 * Channel进度条 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-02-27 15:35:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ChannelProgress {

	/**
	 * 开始
	 */
	void start();

	/**
	 * 进行中
	 * 
	 * @param progressSize 已经进行的大小
	 */
	void progress(long progressSize);

	/**
	 * 结束
	 */
	void finish();
}