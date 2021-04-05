package com.wy.io.output;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 将数据写入/dev/null,即将数据全部丢掉 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-02-19 09:08:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NullOutputStream extends OutputStream {

	private NullOutputStream() {
		super();
	}

	public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();

	/**
	 * 丢掉数据
	 *
	 * @param b 写入的字节数组
	 */
	@Override
	public void write(final byte[] b) throws IOException {
		// To /dev/null
	}

	/**
	 * 丢掉数据
	 *
	 * @param b 写入的字节数组
	 */
	@Override
	public void write(final int b) {
		// To /dev/null
	}

	/**
	 * 丢掉数据
	 *
	 * @param b 写入的字节数组
	 * @param off 开始写入的索引
	 * @param len 写入的长度
	 */
	@Override
	public void write(final byte[] b, final int off, final int len) {
		// To /dev/null
	}
}