package com.wy.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import com.wy.ConstantIO;
import com.wy.ConstantLang;
import com.wy.io.output.ByteArrayOutputStream;
import com.wy.lang.AssertTool;
import com.wy.util.CharsetTool;

/**
 * NIO工具类,主要针对Channel读写,拷贝等 FIXME
 *
 * @author 飞花梦影
 * @date 2021-02-27 15:19:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NioTool {

	/**
	 * 默认缓存大小 8192
	 */
	public static final int DEFAULT_BUFFER_SIZE = 8192;

	/**
	 * 默认中等缓存大小 16384
	 */
	public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 16384;

	/**
	 * 默认大缓存大小 32768
	 */
	public static final int DEFAULT_LARGE_BUFFER_SIZE = 23768;

	/**
	 * 拷贝流 thanks to:
	 * https://github.com/venusdrogon/feilong-io/blob/master/src/main/java/com/feilong/io/IOWriteUtil.java<br>
	 * 本方法不会关闭流
	 *
	 * @param input 输入流
	 * @param output 输出流
	 * @param bufferSize 缓存大小
	 * @param channelProgress 进度条
	 * @return 传输的byte数
	 */
	public static long copyByNIO(InputStream input, OutputStream output, int bufferSize,
			ChannelProgress channelProgress) throws IOException {
		return copy(Channels.newChannel(input), Channels.newChannel(output), bufferSize, channelProgress);
	}

	/**
	 * 拷贝文件Channel,使用NIO,拷贝后不会关闭channel
	 *
	 * @param inChannel {@link FileChannel}
	 * @param outChannel {@link FileChannel}
	 * @return 拷贝的字节数
	 */
	public static long copy(FileChannel inChannel, FileChannel outChannel) throws IOException {
		AssertTool.notNull(inChannel, "Input channel is null!");
		AssertTool.notNull(outChannel, "Output channel is null!");
		return inChannel.transferTo(0, inChannel.size(), outChannel);
	}

	/**
	 * 拷贝流,使用NIO,不会关闭channel
	 *
	 * @param input {@link ReadableByteChannel}
	 * @param output {@link WritableByteChannel}
	 * @return 拷贝的字节数
	 */
	public static long copy(ReadableByteChannel input, WritableByteChannel output) throws IOException {
		return copy(input, output, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 拷贝流,使用NIO,不会关闭channel
	 *
	 * @param in {@link ReadableByteChannel}
	 * @param output {@link WritableByteChannel}
	 * @param bufferSize 缓冲大小,如果小于等于0,使用默认
	 * @return 拷贝的字节数
	 */
	public static long copy(ReadableByteChannel input, WritableByteChannel output, int bufferSize) throws IOException {
		return copy(input, output, bufferSize, null);
	}

	/**
	 * 拷贝流,使用NIO,不会关闭channel
	 *
	 * @param input {@link ReadableByteChannel}
	 * @param output {@link WritableByteChannel}
	 * @param bufferSize 缓冲大小,如果小于等于0,使用默认
	 * @param channelProgress 进度处理器
	 * @return 拷贝的字节数
	 */
	public static long copy(ReadableByteChannel input, WritableByteChannel output, int bufferSize,
			ChannelProgress channelProgress) throws IOException {
		AssertTool.notNull(input, "InputStream is null !");
		AssertTool.notNull(output, "OutputStream is null !");
		ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize <= 0 ? DEFAULT_BUFFER_SIZE : bufferSize);
		long size = 0;
		if (null != channelProgress) {
			channelProgress.start();
		}
		while (input.read(byteBuffer) != ConstantIO.EOF) {
			byteBuffer.flip();
			size += output.write(byteBuffer);
			byteBuffer.clear();
			if (null != channelProgress) {
				channelProgress.progress(size);
			}
		}
		if (null != channelProgress) {
			channelProgress.finish();
		}
		return size;
	}

	/**
	 * 从流中读取内容,读取完毕后并不关闭流
	 *
	 * @param channel 可读通道,读取完毕后并不关闭通道
	 * @param charset 字符集
	 * @return 内容
	 */
	public static String read(ReadableByteChannel channel, Charset charset) throws IOException {
		ByteArrayOutputStream out = read(channel);
		return null == charset ? out.toString() : out.toString(charset);
	}

	/**
	 * 从流中读取内容,读到输出流中
	 *
	 * @param channel 可读通道,读取完毕后并不关闭通道
	 * @return 输出流
	 */
	public static ByteArrayOutputStream read(ReadableByteChannel channel) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(channel, Channels.newChannel(out));
		return out;
	}

	/**
	 * 从FileChannel中读取UTF-8编码内容
	 *
	 * @param fileChannel 文件管道
	 * @return 内容
	 */
	public static String readUtf8(FileChannel fileChannel) throws IOException {
		return read(fileChannel, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 从FileChannel中读取内容,读取完毕后并不关闭Channel
	 *
	 * @param fileChannel 文件管道
	 * @param charsetName 字符集字符串
	 * @return 内容
	 */
	public static String read(FileChannel fileChannel, String charsetName) throws IOException {
		return read(fileChannel, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 从FileChannel中读取内容,读取完毕后并不关闭Channel
	 *
	 * @param fileChannel 文件管道
	 * @param charset 字符集
	 * @return 内容
	 */
	public static String read(FileChannel fileChannel, Charset charset) throws IOException {
		MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
		return CharsetTool.defaultCharset(charset).decode(buffer).toString();
	}
}