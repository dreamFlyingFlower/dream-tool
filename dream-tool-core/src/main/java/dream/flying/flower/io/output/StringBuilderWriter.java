package dream.flying.flower.io.output;

import java.io.Serializable;
import java.io.Writer;

/**
 * 非线程安全的StringBuilder字符输出流,效率高.线程安全的当使用{@link java.io.StringWriter}
 * 
 * @author 飞花梦影 FIXME
 * @date 2021-02-19 09:12:03
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StringBuilderWriter extends Writer implements Serializable {

	private static final long serialVersionUID = -146927496096066153L;

	private final StringBuilder builder;

	public StringBuilderWriter() {
		this.builder = new StringBuilder();
	}

	public StringBuilderWriter(final int capacity) {
		this.builder = new StringBuilder(capacity);
	}

	public StringBuilderWriter(final StringBuilder builder) {
		this.builder = builder != null ? builder : new StringBuilder();
	}

	/**
	 * 在输出流末尾添加一个字符
	 *
	 * @param value 待添加的字符
	 * @return 当前实例
	 */
	@Override
	public Writer append(final char value) {
		builder.append(value);
		return this;
	}

	/**
	 * 在输出流末尾添加一个字符序列
	 *
	 * @param value 待添加的字符序列
	 * @return 当前实例
	 */
	@Override
	public Writer append(final CharSequence value) {
		builder.append(value);
		return this;
	}

	/**
	 * 在输入流末尾添加另外一个子字符序列的部分序列
	 *
	 * @param value 子字符序列
	 * @param start 添加到输出流的子字符序列的起始索引
	 * @param end 添加到输出流的子字符序列的结束索引
	 * @return 当前实例
	 */
	@Override
	public Writer append(final CharSequence value, final int start, final int end) {
		builder.append(value, start, end);
		return this;
	}

	/**
	 * Closing this writer has no effect.
	 */
	@Override
	public void close() {
		// no-op
	}

	/**
	 * Flushing this writer has no effect.
	 */
	@Override
	public void flush() {
		// no-op
	}

	/**
	 * 返回当前实例
	 *
	 * @return 当前实例
	 */
	public StringBuilder getBuilder() {
		return builder;
	}

	/**
	 * 返回当前实例字符串
	 *
	 * @return 当前实例字符串
	 */
	@Override
	public String toString() {
		return builder.toString();
	}

	/**
	 * 向字符输出流末尾添加字符串
	 *
	 * @param value 待添加字符串
	 */
	@Override
	public void write(final String value) {
		if (value != null) {
			builder.append(value);
		}
	}

	/**
	 * 在输入流末尾添加另外一个子字符数组的部分序列
	 *
	 * @param value 子字符数组
	 * @param start 添加到输出流的子字符数组的起始索引
	 * @param end 添加到输出流的子字符数组的结束索引
	 */
	@Override
	public void write(final char[] value, final int offset, final int length) {
		if (value != null) {
			builder.append(value, offset, length);
		}
	}
}