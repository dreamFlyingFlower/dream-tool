package com.wy.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 字符流的行迭代
 * 
 * @author 飞花梦影
 * @date 2021-02-19 09:10:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ReaderLineIterator implements Iterator<String>, Closeable {

	/**
	 * 待读取的字符流
	 */
	private final BufferedReader bufferedReader;

	/**
	 * 当前行
	 */
	private String cachedLine;

	/**
	 * 字符流是否读取完毕
	 */
	private boolean finished = false;

	/**
	 * 构造函数
	 *
	 * @param reader 非null字符流
	 */
	public ReaderLineIterator(final Reader reader) {
		Objects.requireNonNull(reader, "Reader must not be null");
		if (reader instanceof BufferedReader) {
			bufferedReader = (BufferedReader) reader;
		} else {
			bufferedReader = new BufferedReader(reader);
		}
	}

	/**
	 * 关闭字符输入流.当只读取一个大文件的第一行时效率很高
	 */
	@Override
	public void close() throws IOException {
		finished = true;
		cachedLine = null;
		IOHelper.close(bufferedReader);
	}

	/**
	 * 判断是否还有更多行.
	 *
	 * @return true当字符流有更多行
	 */
	@Override
	public boolean hasNext() {
		if (cachedLine != null) {
			return true;
		} else if (finished) {
			return false;
		} else {
			try {
				while (true) {
					final String line = bufferedReader.readLine();
					if (line == null) {
						finished = true;
						return false;
					} else if (filterLine(line)) {
						cachedLine = line;
						return true;
					}
				}
			} catch (final IOException ioe) {
				IOHelper.closeQuietly(this, e -> ioe.addSuppressed(e));
				throw new IllegalStateException(ioe);
			}
		}
	}

	/**
	 * 校验下一行是否符合条件,可以用做过滤器
	 * 
	 * @param line 待校验的行
	 * @return true当校验通过
	 */
	protected boolean filterLine(final String line) {
		return true;
	}

	/**
	 * 返回下一行字符
	 *
	 * @return 返回下一行字符
	 */
	@Override
	public String next() {
		return nextLine();
	}

	/**
	 * 返回下一行字符
	 *
	 * @return 返回下一行字符
	 */
	public String nextLine() {
		if (!hasNext()) {
			throw new NoSuchElementException("No more lines");
		}
		final String currentLine = cachedLine;
		cachedLine = null;
		return currentLine;
	}

	/**
	 * 不可删除行
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Remove unsupported on LineIterator");
	}
}