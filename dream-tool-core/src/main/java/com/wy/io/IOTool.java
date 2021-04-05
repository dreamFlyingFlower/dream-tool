package com.wy.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.wy.Constant;
import com.wy.ConstantIO;
import com.wy.ConstantLang;
import com.wy.io.output.ByteArrayOutputStream;
import com.wy.io.output.NullOutputStream;
import com.wy.io.output.StringBuilderWriter;
import com.wy.lang.AssertTool;
import com.wy.result.ResultException;
import com.wy.util.ArrayTool;
import com.wy.util.CharsetTool;

/**
 * IO工具类,该类主要集中于InputStream,OutputStream,Reader和Writer FIXME
 *
 * @author 飞花梦影
 * @date 2021-02-19 00:18:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class IOTool {

	/**
	 * 默认字节缓冲大小
	 */
	public static final int DEFAULT_BUFFER_SIZE = 8192;

	/**
	 * 默认字节缓冲数组,8k
	 */
	public static final byte[] DEFAULT_BUFFER_BYTE = new byte[DEFAULT_BUFFER_SIZE];

	/**
	 * 默认字节缓冲数组 {@link #skip(InputStream, long)}
	 */
	private static final byte[] SKIP_BYTE_BUFFER = new byte[DEFAULT_BUFFER_SIZE];

	/**
	 * 默认字符缓冲数组
	 */
	private static char[] SKIP_CHAR_BUFFER;

	/**
	 * 关闭一个{@link Closeable}流
	 *
	 * @param closeable 待关闭的流
	 * @throws IOException
	 */
	public static void close(final Closeable closeable) throws IOException {
		if (closeable != null) {
			closeable.close();
		}
	}

	/**
	 * 关闭多个{@link Closeable}流
	 *
	 * @param closeables 待关闭的流
	 * @throws IOException
	 */
	public static void close(final Closeable... closeables) throws IOException {
		if (closeables != null) {
			for (final Closeable closeable : closeables) {
				close(closeable);
			}
		}
	}

	/**
	 * 关闭一个{@link URLConnection}
	 *
	 * @param conn 待关闭的URLConnection连接
	 */
	public static void close(final URLConnection conn) {
		if (conn instanceof HttpURLConnection) {
			((HttpURLConnection) conn).disconnect();
		}
	}

	/**
	 * 关闭一个流,捕获异常
	 * 
	 * @param closeable 待关闭的流
	 * @param consumer 发生异常时的操作
	 */
	public static void closeQuietly(final Closeable closeable, final Consumer<IOException> consumer) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (final IOException e) {
				if (consumer != null) {
					consumer.accept(e);
				}
			}
		}
	}

	/**
	 * 将字节输入流中的字节全部抛弃
	 *
	 * @param input 输入流,读数据
	 * @return 输入流的字节长度
	 */
	public static long consume(final InputStream input) throws IOException {
		return copy(input, NullOutputStream.NULL_OUTPUT_STREAM, SKIP_BYTE_BUFFER);
	}

	/**
	 * 判断2个字节流的内容是否相同
	 *
	 * @param input1 字节流1
	 * @param input2 字节流2
	 * @return true->字节流相同,false->不相同
	 * @throws NullPointerException if either input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEquals(final InputStream input1, final InputStream input2) {
		if (input1 == input2) {
			return true;
		}
		if (input1 == null ^ input2 == null) {
			return false;
		}
		final BufferedInputStream bufferedInput1 = toBufferedInputStream(input1);
		final BufferedInputStream bufferedInput2 = toBufferedInputStream(input2);
		try {
			int ch = bufferedInput1.read();
			while (Constant.IO.EOF != ch) {
				final int ch2 = bufferedInput2.read();
				if (ch != ch2) {
					return false;
				}
				ch = bufferedInput1.read();
			}
			return bufferedInput2.read() == Constant.IO.EOF;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(e);
		}
	}

	/**
	 * 判断2个字符流是否相同
	 *
	 * @param input1 字符流1
	 * @param input2 字符流2
	 * @return true->字符流相同,false->不相同
	 * @throws NullPointerException if either input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEquals(final Reader input1, final Reader input2) {
		if (input1 == input2) {
			return true;
		}
		if (input1 == null ^ input2 == null) {
			return false;
		}
		final BufferedReader bufferedInput1 = toBufferedReader(input1);
		final BufferedReader bufferedInput2 = toBufferedReader(input2);
		try {
			int ch = bufferedInput1.read();
			while (Constant.IO.EOF != ch) {
				final int ch2 = bufferedInput2.read();
				if (ch != ch2) {
					return false;
				}
				ch = bufferedInput1.read();
			}
			return bufferedInput2.read() == Constant.IO.EOF;
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(e);
		}
	}

	/**
	 * 判断2个字符流是否相同,一直读,直到流关闭
	 *
	 * @param input1 字符流1
	 * @param input2 字符流2
	 * @return true->字符流相同,false->不相同
	 * @throws NullPointerException if either input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEqualsIgnoreEOL(final Reader input1, final Reader input2) throws IOException {
		if (input1 == input2) {
			return true;
		}
		if (input1 == null ^ input2 == null) {
			return false;
		}
		final BufferedReader br1 = toBufferedReader(input1);
		final BufferedReader br2 = toBufferedReader(input2);
		String line1 = br1.readLine();
		String line2 = br2.readLine();
		while (line1 != null && line1.equals(line2)) {
			line1 = br1.readLine();
			line2 = br2.readLine();
		}
		return Objects.equals(line1, line2);
	}

	/**
	 * 复制一个输入流中的字节到一个输出流中
	 *
	 * @param input 输入流,读数据
	 * @param output 输出流,写数据
	 * @return 最终从输入流读取的字节长度
	 */
	public static long copy(final InputStream input, final OutputStream output) {
		return copy(input, output, DEFAULT_BUFFER_BYTE);
	}

	/**
	 * 复制一个输入流中的字节到一个输出流中
	 *
	 * @param input 输入流,读数据
	 * @param output 输出流,写数据
	 * @param bufferSize 缓存区字节大小
	 * @return 最终从输入流读取的字节长度
	 */
	public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
			throws IOException {
		return copy(input, output, new byte[bufferSize]);
	}

	/**
	 * 复制一个超过2GB的输入流中的字节到一个输出流中
	 *
	 * @param input 输入流,读数据
	 * @param output 输出流,写数据
	 * @param buffer 缓存区字节数组
	 * @return 最终从输入流读取的字节长度
	 */
	public static long copy(final InputStream input, final OutputStream output, final byte[] buffer) {
		Objects.requireNonNull(input, "inputstream can't by null");
		Objects.requireNonNull(output, "outputstream can't by null");
		long count = 0;
		int n;
		try {
			while (ConstantIO.EOF != (n = input.read(buffer))) {
				output.write(buffer, 0, n);
				count += n;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(e);
		}
		return count;
	}

	/**
	 * 复制部分或全部的字节输入流中的数据到一个输出流中
	 *
	 * @param input 输入流,读数据
	 * @param output 输出流,写数据
	 * @param inputOffset 输入流中从开始位置到需要舍弃位置的索引
	 * @param length 从舍弃索引开始需要复制的字节长度
	 * @return 实际复制的字节长度
	 */
	public static long copy(final InputStream input, final OutputStream output, final long inputOffset,
			final long length) throws IOException {
		return copy(input, output, inputOffset, length, DEFAULT_BUFFER_BYTE);
	}

	/**
	 * 复制部分或全部的字节输入流中的数据到一个输出流中
	 *
	 * @param input 输入流,读数据
	 * @param output 输出流,写数据
	 * @param inputOffset 输入流中从开始位置到需要舍弃位置的索引
	 * @param length 从舍弃索引开始需要复制的字节长度
	 * @param buffer 缓冲数组
	 * @return 实际复制的字节长度
	 */
	public static long copy(final InputStream input, final OutputStream output, final long inputOffset,
			final long length, final byte[] buffer) throws IOException {
		Objects.requireNonNull(input, "inputstream can't by null");
		Objects.requireNonNull(output, "outputstream can't by null");
		if (inputOffset > 0) {
			skipFully(input, inputOffset);
		}
		if (length == 0) {
			return 0;
		}
		final int bufferLength = buffer.length;
		int bytesToRead = bufferLength;
		if (length > 0 && length < bufferLength) {
			bytesToRead = (int) length;
		}
		int read;
		long totalRead = 0;
		while (bytesToRead > 0 && ConstantIO.EOF != (read = input.read(buffer, 0, bytesToRead))) {
			output.write(buffer, 0, read);
			totalRead += read;
			// 防止length并不是读到字节末尾
			if (length > 0) {
				bytesToRead = (int) Math.min(length - totalRead, bufferLength);
			}
		}
		return totalRead;
	}

	/**
	 * 复制一个字节输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字节输入流,读数据
	 * @param output 字符输出流,写数据
	 */
	public static void copy(final InputStream input, final Writer output) throws IOException {
		copy(input, output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 复制一个字节输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字节输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param inputCharset 将字节输入流包装成字符输入流时的字符集编码
	 */
	public static void copy(final InputStream input, final Writer output, final Charset inputCharset)
			throws IOException {
		Objects.requireNonNull(input, "inputstream can't by null");
		final InputStreamReader in = new InputStreamReader(input, CharsetTool.defaultCharset(inputCharset));
		copy(in, output);
	}

	/**
	 * 复制一个字节输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字节输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param inputCharsetName 将字节输入流包装成字符输入流时的字符集编码字符串
	 */
	public static void copy(final InputStream input, final Writer output, final String inputCharsetName)
			throws IOException {
		copy(input, output, CharsetTool.defaultCharset(inputCharsetName));
	}

	/**
	 * 复制一个字符输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字符输入流,读数据
	 * @param output 字节输出流,写数据
	 */
	public static void copy(final Reader input, final OutputStream output) throws IOException {
		copy(input, output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 复制一个字符输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字符输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param outputCharset 将输出流包装成字符流时的字符集,默认UTF8
	 */
	public static void copy(final Reader input, final OutputStream output, final Charset outputCharset)
			throws IOException {
		Objects.requireNonNull(output, "outputstream can't by null");
		final OutputStreamWriter out = new OutputStreamWriter(output, CharsetTool.defaultCharset(outputCharset));
		copy(input, out);
		// 若不需要对out进行重写,则直接在此处刷新数据
		out.flush();
	}

	/**
	 * 复制一个字符输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字符输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param outputCharset 将输出流包装成字符流时的字符集,默认UTF8
	 */
	public static void copy(final Reader input, final OutputStream output, final String outputCharsetName)
			throws IOException {
		copy(input, output, CharsetTool.defaultCharset(outputCharsetName));
	}

	/**
	 * 复制一个字符输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字符输入流,读数据
	 * @param output 字符输出流,写数据
	 * @return 复制的字符大小
	 */
	public static long copy(final Reader input, final Writer output) throws IOException {
		return copy(input, output, new char[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * 复制一个字符输入流中的数据到一个字符输出流中
	 * 
	 * @param input 字符输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param buffer 缓冲字符数组
	 * @return 复制的字符大小
	 */
	public static long copy(final Reader input, final Writer output, final char[] buffer) throws IOException {
		Objects.requireNonNull(input, "inputstream can't by null");
		Objects.requireNonNull(output, "outputstream can't by null");
		long count = 0;
		int n;
		while (ConstantIO.EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * 复制部分或全部的字节输入流中的数据到一个输出流中
	 *
	 * @param input 字符输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param inputOffset 字符输入流中从开始位置到需要舍弃位置的索引
	 * @param length 从舍弃索引开始需要复制的字符长度
	 * @return 实际复制的字符长度
	 */
	public static long copy(final Reader input, final Writer output, final long inputOffset, final long length)
			throws IOException {
		return copy(input, output, inputOffset, length, new char[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * 复制部分或全部的字节输入流中的数据到一个输出流中
	 *
	 * @param input 字符输入流,读数据
	 * @param output 字符输出流,写数据
	 * @param inputOffset 字符输入流中从开始位置到需要舍弃位置的索引
	 * @param length 从舍弃索引开始需要复制的字符长度
	 * @param buffer 字符缓冲数组
	 * @return 实际复制的字符长度
	 */
	public static long copy(final Reader input, final Writer output, final long inputOffset, final long length,
			final char[] buffer) throws IOException {
		Objects.requireNonNull(input, "inputstream can't by null");
		Objects.requireNonNull(output, "outputstream can't by null");
		if (inputOffset > 0) {
			skipFully(input, inputOffset);
		}
		if (length == 0) {
			return 0;
		}
		int bytesToRead = buffer.length;
		if (length > 0 && length < buffer.length) {
			bytesToRead = (int) length;
		}
		int read;
		long totalRead = 0;
		while (bytesToRead > 0 && ConstantIO.EOF != (read = input.read(buffer, 0, bytesToRead))) {
			output.write(buffer, 0, read);
			totalRead += read;
			// 防止length并不是读到字节末尾
			if (length > 0) {
				bytesToRead = (int) Math.min(length - totalRead, buffer.length);
			}
		}
		return totalRead;
	}

	/**
	 * 将字节输入流转换为字符流并返回一个行迭代器,默认UTF8
	 *
	 * @param input 字节输入流,读数据
	 * @return 字符行迭代器
	 */
	public static ReaderLineIterator lineIterator(final InputStream input) throws IOException {
		return new ReaderLineIterator(new InputStreamReader(input, ConstantLang.DEFAULT_CHARSET));
	}

	/**
	 * 将字节输入流转换为字符流并返回一个行迭代器
	 *
	 * @param input 字节输入流,读数据
	 * @param charset 包装字节流的字符集
	 * @return 字符行迭代器
	 */
	public static ReaderLineIterator lineIterator(final InputStream input, final Charset charset) throws IOException {
		return new ReaderLineIterator(new InputStreamReader(input, CharsetTool.defaultCharset(charset)));
	}

	/**
	 * 将字节输入流转换为字符流并返回一个行迭代器
	 *
	 * @param input 字节输入流,读数据
	 * @param charsetName 包装字节流的字符集字符串
	 * @return 字符行迭代器
	 */
	public static ReaderLineIterator lineIterator(final InputStream input, final String charsetName)
			throws IOException {
		return lineIterator(input, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将字符输入流转换为一个行迭代器
	 *
	 * @param reader 字符输入流,读数据
	 * @return 字符行迭代器
	 */
	public static ReaderLineIterator lineIterator(final Reader reader) {
		return new ReaderLineIterator(reader);
	}

	/**
	 * 读取字节输入流.buffer长度不够,不能读取全部流;流长度不够,buffer有空字节,需调用者关闭流
	 * 
	 * @param input 字节输入流,读数据
	 * @param buffer 存放从字节输入流中读取的字节
	 * @return 最终从字节输入流读取的字节长度,可能小于字节输入流的长度
	 */
	public static int read(final InputStream input, final byte[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	/**
	 * 读取字节输入流.buffer长度不够,不能读取全部流;流长度不够,buffer有空字节,需调用者关闭流
	 * 
	 * @param input 字节输入流,读数据
	 * @param buffer 存放从字节输入流中读取的字节,前面的字节可能为0
	 * @param offset 从字节输入流中开始读取数据的索引
	 * @param length 从字节输入路中读取字节的长度
	 * @return 最终从字节输入流读取的字节长度,可能小于字节输入流的长度
	 */
	public static int read(final InputStream input, final byte[] buffer, final int offset, final int length)
			throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			final int location = length - remaining;
			final int count = input.read(buffer, offset + location, remaining);
			if (ConstantIO.EOF == count) {
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	/**
	 * 从可读的字节通道中读取字节.buffer长度不够,不能读取全部流;流长度不够,buffer有空字节
	 * 
	 * @param input 字节通道输入流,读数据
	 * @param buffer 存放从字节通道中读取的字节,前面的字节可能为0
	 * @return 最终从字节通道读取的字节长度,可能小于字节输入流的长度
	 */
	public static int read(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
		final int length = buffer.remaining();
		while (buffer.remaining() > 0) {
			final int count = input.read(buffer);
			if (ConstantIO.EOF == count) {
				break;
			}
		}
		return length - buffer.remaining();
	}

	/**
	 * 读取字符输入流.buffer长度不够,不能读取全部流;流长度不够,buffer有空字符,需调用者关闭流
	 * 
	 * @param input 字符输入流,读数据
	 * @param buffer 存放从字符输入流中读取的字符
	 * @return 最终从字符输入流读取的字符长度,可能小于字符输入流的长度
	 */
	public static int read(final Reader input, final char[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	/**
	 * 读取字符输入流.buffer长度不够,不能读取全部流;流长度不够,buffer有空字符,需调用者关闭流
	 * 
	 * @param reader 字符输入流,读数据
	 * @param buffer 存放从字符输入流中读取的字符,前面的字节可能为0
	 * @param offset 从字符输入流中开始读取数据的索引
	 * @param length 从字符输入路中读取字节的长度
	 * @return 最终从字符输入流读取的字符长度,可能小于字符输入流的长度
	 */
	public static int read(final Reader reader, final char[] buffer, final int offset, final int length)
			throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			final int location = length - remaining;
			final int count = reader.read(buffer, offset + location, remaining);
			if (ConstantIO.EOF == count) {
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	/**
	 * 以UTF8按行读取字节输入流中的字节,需调用者关闭流
	 *
	 * @param input 字节输入流,读数据
	 * @return 字节流中所有的行
	 */
	public static List<String> readLines(final InputStream input) throws IOException {
		return readLines(input, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 以指定字符集按行读取字节输入流中的字节,需调用者关闭流
	 *
	 * @param input 字节输入流,读数据
	 * @param charset 指定字符集
	 * @return 字节流中所有的行
	 */
	public static List<String> readLines(final InputStream input, final Charset charset) throws IOException {
		Objects.requireNonNull(input, "inputstream can't by null");
		final InputStreamReader reader = new InputStreamReader(input, CharsetTool.defaultCharset(charset));
		return readLines(reader);
	}

	/**
	 * 以指定字符集按行读取字节输入流中的字节,需调用者关闭流
	 *
	 * @param input 字节输入流,读数据
	 * @param charset 指定字符集字符串
	 * @return 字节流中所有的行
	 */
	public static List<String> readLines(final InputStream input, final String charsetName) throws IOException {
		return readLines(input, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * /** 以指定字符集按行读取字节输入流中的字节,需调用者关闭流
	 *
	 * @param input 字符输入流,读数据
	 * @return 字节流中所有的行
	 */
	public static List<String> readLines(final Reader input) throws IOException {
		final BufferedReader reader = toBufferedReader(Objects.requireNonNull(input, "Reader can't be null"));
		final List<String> list = new ArrayList<>();
		String line;
		while ((line = reader.readLine()) != null) {
			list.add(line);
		}
		return list;
	}

	/**
	 * 获得类路径下的资源并返回字节数组,路径必须是全路径,以/开头
	 *
	 * @param name 资源完整路径
	 * @return 资源字节数组
	 */
	public static byte[] resourceToBytes(final String name) throws IOException {
		return resourceToBytes(name, null);
	}

	/**
	 * 获得类路径下的资源并返回字节数组,路径必须是全路径,以/开头
	 *
	 * @param name 资源完整路径
	 * @param classLoader 类加载器,默认为当前类
	 * @return 资源字节数组
	 */
	public static byte[] resourceToBytes(final String name, final ClassLoader classLoader) throws IOException {
		return toByteArray(resourceToURL(name, classLoader));
	}

	/**
	 * 获得类路径下的资源并以指定字符集返回文件中的字符串内容,路径必须是全路径,以/开头
	 *
	 * @param name 资源完整路径
	 * @param charset 字符集
	 * @return 资源字符串
	 */
	public static String resourceToString(final String name, final Charset charset) throws IOException {
		return resourceToString(name, charset, null);
	}

	/**
	 * 获得类路径下的资源并以指定字符集返回文件中的字符串内容,路径必须是全路径,以/开头
	 *
	 * @param name 资源完整路径
	 * @param charset 字符集
	 * @param classLoader 类加载器,默认为当前工具类
	 * @return 资源字符串
	 */
	public static String resourceToString(final String name, final Charset charset, final ClassLoader classLoader)
			throws IOException {
		return toString(resourceToURL(name, classLoader), charset);
	}

	/**
	 * 根据资源路径获得URL
	 *
	 * @param name 资源完整路径
	 * @return 资源URL
	 */
	public static URL resourceToURL(final String name) throws IOException {
		return resourceToURL(name, null);
	}

	/**
	 * 根据资源路径获得URL
	 *
	 * @param name 资源完整路径
	 * @param classLoader 类加载器,默认为当前工具类
	 * @return 资源URL
	 */
	public static URL resourceToURL(final String name, final ClassLoader classLoader) throws IOException {
		final URL resource = classLoader == null ? IOTool.class.getResource(name) : classLoader.getResource(name);
		if (resource == null) {
			throw new IOException("Resource not found: " + name);
		}
		return resource;
	}

	/**
	 * 忽略一个字节数组指定长度的字节数,只能从字节数组的起始位置开始忽略
	 *
	 * @param input 字节输入流
	 * @param toSkip 待忽略的字节数组长度
	 * @return 实际忽略的字节数组长度
	 */
	public static long skip(final InputStream input, final long toSkip) throws IOException {
		if (toSkip <= 0) {
			return 0;
		}
		long remain = toSkip;
		while (remain > 0) {
			final long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, SKIP_BYTE_BUFFER.length));
			if (n < 0) {
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	/**
	 * 忽略一个可读字节通道指定长度的字节数,只能从字节数组的起始位置开始忽略
	 *
	 * @param input 可读的字节通道
	 * @param toSkip 待忽略的字节数组长度
	 * @return 实际忽略的字节数组长度
	 */
	public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
		if (toSkip <= 0) {
			return 0;
		}
		final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, SKIP_BYTE_BUFFER.length));
		long remain = toSkip;
		while (remain > 0) {
			skipByteBuffer.position(0);
			skipByteBuffer.limit((int) Math.min(remain, SKIP_BYTE_BUFFER.length));
			final int n = input.read(skipByteBuffer);
			if (n == ConstantIO.EOF) {
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	/**
	 * 忽略一个字符输入流中指定长度的字符数,只能从字符输入流的起始位置开始忽略
	 *
	 * @param input 字符输入流
	 * @param toSkip 待忽略的字符数组长度
	 * @return 实际忽略的字符数组长度
	 */
	public static long skip(final Reader input, final long toSkip) throws IOException {
		if (toSkip <= 0) {
			return 0;
		}
		if (SKIP_CHAR_BUFFER == null) {
			SKIP_CHAR_BUFFER = new char[DEFAULT_BUFFER_SIZE];
		}
		long remain = toSkip;
		while (remain > 0) {
			final long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, DEFAULT_BUFFER_SIZE));
			if (n < 0) {
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	/**
	 * 完全忽略字节输入流中的字节数组
	 *
	 * @param input 字节输入流
	 * @param toSkip 待忽略的字节数组长度
	 */
	public static void skipFully(final InputStream input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
		}
		final long skipped = skip(input, toSkip);
		if (skipped != toSkip) {
			throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
		}
	}

	/**
	 * 完全忽略一个可读字节通道中的字节数组
	 *
	 * @param input 可读的字节通道
	 * @param toSkip 待忽略的字符数组长度
	 */
	public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
		}
		final long skipped = skip(input, toSkip);
		if (skipped != toSkip) {
			throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
		}
	}

	/**
	 * 完全忽略字符输入流中的字符数组
	 *
	 * @param input 字符输入流
	 * @param toSkip 待忽略的字符数组长度
	 */
	public static void skipFully(final Reader input, final long toSkip) throws IOException {
		final long skipped = skip(input, toSkip);
		if (skipped != toSkip) {
			throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
		}
	}

	/**
	 * 将字节流包装成字节缓冲流
	 *
	 * @param inputStream 字节流
	 * @return 包装后的字节缓冲流
	 * @throws NullPointerException if the input parameter is null
	 */
	public static BufferedInputStream toBufferedInputStream(final InputStream inputStream) {
		Objects.requireNonNull(inputStream, "inputStream can't be null");
		return inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream
				: new BufferedInputStream(inputStream);
	}

	/**
	 * 将字符流包装成字符缓冲流
	 *
	 * @param reader 字符流
	 * @return 字符缓冲流
	 */
	public static BufferedReader toBufferedReader(final Reader reader) {
		Objects.requireNonNull(reader, "Reader can't be null");
		return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
	}

	/**
	 * 将字节输入流中的字节转到字节输出流中并返回字节数组,同步方法,慎用
	 *
	 * @param input 字节输入流,从中读取字节
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final InputStream input) throws IOException {
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			copy(input, output);
			return output.toByteArray();
		}
	}

	/**
	 * 直接取出字节输入流中的字节数组.若缓存设置的太小,耗时比{@link #toByteArray(InputStream)}更长
	 *
	 * @param input 字节输入流,从中读取字节
	 * @param size 缓存字节数组大小,默认1024
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final InputStream input, int size) throws IOException {
		size = size <= 0 ? DEFAULT_BUFFER_SIZE : size;
		byte[] temp = new byte[size];
		List<byte[]> buffers = new ArrayList<>();
		int read;
		byte[] buf;
		while ((read = input.read(temp)) != ConstantIO.EOF) {
			buf = new byte[read];
			System.arraycopy(temp, 0, buf, 0, read);
			buffers.add(buf);
		}
		return ArrayTool.merge(buffers.toArray(new byte[buffers.size()][]));
	}

	/**
	 * 获得字符输入流中的字节数组,需要先从输入流转到输出流,且为同步方法,慎用
	 * 
	 * @param input 字符输入流
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final Reader input) throws IOException {
		return toByteArray(input, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 获得字符输入流中的字节数组,需要先从输入流转到输出流,且为同步方法,慎用
	 * 
	 * @param input 字符输入流
	 * @param charset 将字符流解析为字节流时decode的编码集
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final Reader input, final Charset charset) throws IOException {
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			copy(input, output, charset);
			return output.toByteArray();
		}
	}

	/**
	 * 获得字符输入流中的字节数组,需要先从输入流转到输出流,且为同步方法,慎用
	 * 
	 * @param input 字符输入流
	 * @param charset 将字符流解析为字节流时decode的编码集字符串
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final Reader input, final String charsetName) throws IOException {
		return toByteArray(input, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 获得URI中的字节输入流中,并转移到字节输出流中并返回字节数组,同步方法,慎用
	 *
	 * @param uri uri资源
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final URI uri) throws IOException {
		return toByteArray(uri.toURL());
	}

	/**
	 * 获得URL中的字节输入流中,并转移到字节输出流中并返回字节数组,同步方法,慎用
	 *
	 * @param url URL资源
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final URL url) throws IOException {
		if (url == null) {
			return null;
		}
		final URLConnection conn = url.openConnection();
		try {
			return toByteArray(conn);
		} finally {
			close(conn);
		}
	}

	/**
	 * 获得URLConnection中的字节输入流中,并转移到字节输出流中并返回字节数组,同步方法,慎用
	 *
	 * @param urlConn URL连接资源
	 * @return 字节数组
	 */
	public static byte[] toByteArray(final URLConnection urlConn) throws IOException {
		Objects.requireNonNull(urlConn, "url connection can't be null");
		try (InputStream inputStream = urlConn.getInputStream()) {
			return toByteArray(inputStream);
		}
	}

	/**
	 * 将字节输入流中读取出来并转换为字符数组
	 * 
	 * @param input 字节输入流,从中读取数据
	 * @return 字节输入流中的字符数组
	 */
	public static char[] toCharArray(final InputStream is) throws IOException {
		return toCharArray(is, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将字节输入流中读取出来并转换为字符数组
	 * 
	 * @param input 字节输入流,从中读取数据
	 * @param charset 将字节输入流包装成字符输入流时的编码集
	 * @return 字节输入流中的字符数组
	 */
	public static char[] toCharArray(final InputStream input, final Charset charset) throws IOException {
		final CharArrayWriter output = new CharArrayWriter();
		copy(input, output, charset);
		return output.toCharArray();
	}

	/**
	 * 将字节输入流中读取出来并转换为字符数组
	 * 
	 * @param input 字节输入流,从中读取数据
	 * @param charset 将字节输入流包装成字符输入流时的编码集字符串
	 * @return 字节输入流中的字符数组
	 */
	public static char[] toCharArray(final InputStream is, final String charsetName) throws IOException {
		return toCharArray(is, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将字符输入流中读取出来并转换为字符数组
	 * 
	 * @param input 字符输入流,从中读取数据
	 * @return 字节输入流中的字符数组
	 */
	public static char[] toCharArray(final Reader input) throws IOException {
		final CharArrayWriter sw = new CharArrayWriter();
		copy(input, sw);
		return sw.toCharArray();
	}

	/**
	 * 将字符串转换为字节输入流
	 *
	 * @param input 待转换的字符序列
	 * @return 字节数组输入流
	 */
	public static InputStream toInputStream(final CharSequence input) {
		return toInputStream(input, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将字符串转换为字节输入流
	 *
	 * @param input 待转换的字符序列
	 * @param charset 将字符串转成字节数组时encode的编码集
	 * @return 字节数组输入流
	 */
	public static InputStream toInputStream(final CharSequence input, final Charset charset) {
		AssertTool.notEmpty(input);
		return new ByteArrayInputStream(input.toString().getBytes(CharsetTool.defaultCharset(charset)));
	}

	/**
	 * 将字符序列转换为字节输入流
	 *
	 * @param input 待转换的字符串
	 * @param charsetName 将字符串转成字节数组时encode的编码集字符串
	 * @return 字节数组输入流
	 */
	public static InputStream toInputStream(final CharSequence input, final String charsetName) {
		return toInputStream(input, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将字节输入流中的数据读取出现并转成字符串
	 *
	 * @param input 字节输入流,从其中读数据
	 * @return 字符串
	 */
	public static String toString(final InputStream input) throws IOException {
		return toString(input, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将字节输入流中的数据读取出现并转成字符串
	 *
	 * @param input 字节输入流,从其中读数据
	 * @param charset 将字节输入流包装成字符输入流时的字符集
	 * @return 字符串
	 */
	public static String toString(final InputStream input, final Charset charset) throws IOException {
		Objects.requireNonNull(input, "inputstream can't by null");
		try (final StringBuilderWriter sw = new StringBuilderWriter()) {
			copy(input, sw, charset);
			return sw.toString();
		}
	}

	/**
	 * 将字节输入流中的数据读取出现并转成字符串
	 *
	 * @param input 字节输入流,从其中读数据
	 * @param charset 将字节输入流包装成字符输入流时的字符集字符串
	 * @return 字符串
	 */
	public static String toString(final InputStream input, final String charsetName) throws IOException {
		return toString(input, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将字符输入流中的数据读取出来转成字符串
	 *
	 * @param input 字符输入流,从中读取数据
	 * @return 输入流中的字符串
	 */
	public static String toString(final Reader input) throws IOException {
		try (final StringBuilderWriter sw = new StringBuilderWriter()) {
			copy(input, sw);
			return sw.toString();
		}
	}

	/**
	 * 将URI中输入流的数据读取出现并转成字符串
	 *
	 * @param uri uri资源
	 * @return URI资源中的字符串
	 */
	public static String toString(final URI uri) throws IOException {
		return toString(uri, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将URI中输入流的数据读取出现并转成字符串
	 *
	 * @param uri uri资源
	 * @param charset 将字节输入流包装成字符输入流时的字符集
	 * @return URI资源中的字符串
	 */
	public static String toString(final URI uri, final Charset charset) throws IOException {
		Objects.requireNonNull(uri, "uri can't by null");
		return toString(uri.toURL(), CharsetTool.defaultCharset(charset));
	}

	/**
	 * 将URI中输入流的数据读取出现并转成字符串
	 *
	 * @param uri uri资源
	 * @param charset 将字节输入流包装成字符输入流时的字符集字符串
	 * @return URI资源中的字符串
	 */
	public static String toString(final URI uri, final String charsetName) throws IOException {
		return toString(uri, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将URL中输入流的数据读取出现并转成字符串
	 *
	 * @param url url资源,也可以是本地资源文件地址
	 * @return 字符串
	 */
	public static String toString(final URL url) throws IOException {
		return toString(url, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将URL中输入流的数据读取出现并转成字符串
	 *
	 * @param url url地址,也可以是本地资源文件地址
	 * @param charset 将字节输入流包装成字符输入流时的字符集
	 * @return 字符串
	 */
	public static String toString(final URL url, final Charset charset) throws IOException {
		Objects.requireNonNull(url, "url can't be null");
		try (InputStream inputStream = url.openStream()) {
			return toString(inputStream, charset);
		}
	}

	/**
	 * 将URL中输入流的数据读取出现并转成字符串
	 *
	 * @param url url资源,也可以是本地资源文件地址
	 * @param charsetName 将字节输入流包装成字符输入流时的字符集字符串
	 * @return 字符串
	 */
	public static String toString(final URL url, final String charsetName) throws IOException {
		return toString(url, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 从字节数组中读取字节到字节输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字节数组
	 * @param output 字节输出流,往流中写数据
	 */
	public static void write(final byte[] data, final OutputStream output) throws IOException {
		if (data != null && data.length > 0) {
			output.write(data);
		}
	}

	/**
	 * 从字节数组中读取字节到字符输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字节数组
	 * @param output 字符输出流,往流中写数据
	 */
	public static void write(final byte[] data, final Writer output) throws IOException {
		write(data, output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 从字节数组中读取字节到字符输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字节数组
	 * @param output 字符输出流,往流中写数据
	 * @param charset 将字节流包装成字符流时的编码集
	 */
	public static void write(final byte[] data, final Writer output, final Charset charset) throws IOException {
		if (data != null) {
			output.write(new String(data, CharsetTool.defaultCharset(charset)));
		}
	}

	/**
	 * 从字节数组中读取字节到字符输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字节数组
	 * @param output 字符输出流,往流中写数据
	 * @param charset 将字节流包装成字符流时的编码集字符串
	 */
	public static void write(final byte[] data, final Writer output, final String charsetName) throws IOException {
		write(data, output, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 从字符数组中读取字符到字节输出流中
	 *
	 * @param data 待读取的字符数组
	 * @param output 字节输出流,往流中写数据
	 */
	public static void write(final char[] data, final OutputStream output) throws IOException {
		write(data, output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 从字符数组中读取字节到字节输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字符数组
	 * @param output 字节输出流,往流中写数据
	 * @param charset 将字节流包装成字符流时的编码集
	 */
	public static void write(final char[] data, final OutputStream output, final Charset charset) throws IOException {
		if (data != null) {
			output.write(new String(data).getBytes(CharsetTool.defaultCharset(charset)));
		}
	}

	/**
	 * 从字符数组中读取字节到字节输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字符数组
	 * @param output 字节输出流,往流中写数据
	 * @param charset 将字节流包装成字符流时的编码集字符串
	 */
	public static void write(final char[] data, final OutputStream output, final String charsetName)
			throws IOException {
		write(data, output, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 从字符数组中读取字符到字符输出流中,需调用者关闭流 FIXME
	 *
	 * @param data 待读取的字符数组
	 * @param output 字符输出流,往流中写数据
	 */
	public static void write(final char[] data, final Writer output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * 将字符序列写入到字节输出流中,需调用者关闭流
	 *
	 * @param data 待读取的字符序列
	 * @param output 字节输出流,往流中写数据
	 */
	public static void write(final CharSequence data, final OutputStream output) throws IOException {
		write(data, output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将字符序列写入到字节输出流中,需调用者关闭流 FIXME
	 *
	 * @param data 待读取的字符序列
	 * @param output 字节输出流,往流中写数据
	 * @param charset 将字符序列转成字节数组时的编码集
	 */
	public static void write(final CharSequence data, final OutputStream output, final Charset charset)
			throws IOException {
		if (data != null) {
			write(data.toString(), output, charset);
		}
	}

	/**
	 * 将字符序列写入到字节输出流中,需调用者关闭流 FIXME
	 *
	 * @param data 待读取的字符序列
	 * @param output 字节输出流,往流中写数据
	 * @param charset 将字符序列转成字节数组时的编码集字符串
	 */
	public static void write(final CharSequence data, final OutputStream output, final String charsetName)
			throws IOException {
		write(data, output, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将字符序列写入到字符输出流中,需调用者关闭流 FIXME
	 *
	 * @param data 待读取的字符序列
	 * @param output 字符输出流,往流中写数据
	 */
	public static void write(final CharSequence data, final Writer output) throws IOException {
		if (data != null) {
			output.write(data.toString());
		}
	}

	/**
	 * 从字节数组中分段读取字节到字节输出流中,默认每次读取8192字节,需调用者关闭流
	 *
	 * @param data 待读取的字节数组
	 * @param output 字节输出流,往流中写数据
	 */
	public static void writeChunked(final byte[] data, final OutputStream output) throws IOException {
		writeChunked(data, output, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 从字节数组中分段读取字节到字节输出流中,默认每次读取8192字节,需调用者关闭流
	 *
	 * @param data 待读取的字节数组
	 * @param output 字节输出流,往流中写数据
	 * @param length 每次分段读取的字节长度
	 */
	public static void writeChunked(final byte[] data, final OutputStream output, int length) throws IOException {
		length = length <= 0 ? DEFAULT_BUFFER_SIZE : length;
		if (data != null) {
			int bytes = data.length;
			int offset = 0;
			while (bytes > 0) {
				final int chunk = Math.min(bytes, length);
				output.write(data, offset, chunk);
				bytes -= chunk;
				offset += chunk;
			}
		}
	}

	/**
	 * 从字符数组中分段读取字符到字符输出流中,默认每次读取8192字符,需调用者关闭流
	 *
	 * @param data 待读取的字符数组
	 * @param output 字符输出流,往流中写数据
	 */
	public static void writeChunked(final char[] data, final Writer output) throws IOException {
		writeChunked(data, output, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 从字符数组中分段读取字符到字符输出流中,默认每次读取8192字符,需调用者关闭流
	 *
	 * @param data 待读取的字符数组
	 * @param output 字符输出流,往流中写数据
	 * @param length 每次分段读取的字符度
	 */
	public static void writeChunked(final char[] data, final Writer output, int length) throws IOException {
		length = length <= 0 ? DEFAULT_BUFFER_SIZE : length;
		if (data != null) {
			int bytes = data.length;
			int offset = 0;
			while (bytes > 0) {
				final int chunk = Math.min(bytes, length);
				output.write(data, offset, chunk);
				bytes -= chunk;
				offset += chunk;
			}
		}
	}

	/**
	 * 将对象集合中的元素调用toString()方法后写入到输出流中,需调用者关闭流
	 *
	 * @param lines 需要写入到输出流中的数据
	 * @param output 输出流
	 */
	public static void writeLines(final Collection<?> lines, final OutputStream output) throws IOException {
		writeLines(lines, System.lineSeparator(), output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将对象集合中的元素调用toString()方法后写入到输出流中,需调用者关闭流
	 *
	 * @param lines 需要写入到输出流中的数据
	 * @param lineEnding 换行符,若不指定,根据系统默认指定
	 * @param output 输出流
	 */
	public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output)
			throws IOException {
		writeLines(lines, lineEnding, output, ConstantLang.DEFAULT_CHARSET);
	}

	/**
	 * 将对象集合中的元素调用toString()方法后写入到输出流中,需调用者关闭流
	 *
	 * @param lines 需要写入到输出流中的数据
	 * @param lineEnding 换行符,若不指定,根据系统默认指定
	 * @param output 输出流
	 * @param charset 将字符串转换为字节数组时的编码集
	 */
	public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
			final Charset charset) throws IOException {
		Objects.requireNonNull(output, "outputstream can't by null");
		if (lines == null) {
			return;
		}
		lineEnding = lineEnding == null ? System.lineSeparator() : lineEnding;
		final Charset cs = CharsetTool.defaultCharset(charset);
		for (final Object line : lines) {
			if (line != null) {
				output.write(line.toString().getBytes(cs));
			}
			output.write(lineEnding.getBytes(cs));
		}
	}

	/**
	 * 将对象集合中的元素调用toString()方法后写入到输出流中,需调用者关闭流
	 *
	 * @param lines 需要写入到输出流中的数据
	 * @param lineEnding 换行符,若不指定,根据系统默认指定
	 * @param output 输出流
	 * @param charsetName 将字符串转换为字节数组时的编码集字符串
	 */
	public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output,
			final String charsetName) throws IOException {
		writeLines(lines, lineEnding, output, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将对象集合中的元素调用toString()方法后写入到输出流中,需调用者关闭流
	 *
	 * @param lines 需要写入到输出流中的数据
	 * @param output 输出流
	 */
	public static void writeLines(final Collection<?> lines, final Writer output) throws IOException {
		writeLines(lines, System.lineSeparator(), output);
	}

	/**
	 * 将对象集合中的元素调用toString()方法后写入到输出流中,需调用者关闭流
	 *
	 * @param lines 需要写入到输出流中的数据
	 * @param lineEnding 换行符,若不指定,根据系统默认指定
	 * @param output 输出流
	 */
	public static void writeLines(final Collection<?> lines, String lineEnding, final Writer output)
			throws IOException {
		Objects.requireNonNull(output, "Writer can't by null");
		if (lines == null) {
			return;
		}
		lineEnding = lineEnding == null ? System.lineSeparator() : lineEnding;
		for (final Object line : lines) {
			if (line != null) {
				output.write(line.toString());
			}
			output.write(lineEnding);
		}
	}
}