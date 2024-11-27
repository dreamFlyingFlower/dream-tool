package dream.flying.flower.io.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import dream.flying.flower.ConstIO;
import dream.flying.flower.ConstString;

/**
 * 该类为字节输出流,写数据进入字节数组的基类,写入数据时字节数组会自动扩容,该类使用close()无效
 * 
 * 这是{@link java.io.ByteArrayOutputStream}的替代实现基础类,初始字节数组为1024,而不是32.
 * 与原来相比不会重新分配内存块,但是会分配额外的缓冲区.这种不会被GC回收,内容也不会复制到新的缓冲区.
 * 
 * FIXME
 * 
 * @author 飞花梦影
 * @date 2021-02-19 08:55:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class AbstractByteArrayOutputStream extends OutputStream {

	/** 字节数组的默认长度 */
	static final int DEFAULT_SIZE = 1024;

	/** 当前存储字节的数组 */
	private byte[] buf;

	/** 缓冲区列表,所有使用过的字节数组都会在里面,不能删除,它不断增长,不会减少 */
	private List<byte[]> buffers = new ArrayList<>();

	/** 从开始写数据到字节数组中直到reset()之间总共写入的字节数,包含当前字节数组的长度 */
	protected int count;

	/** 字节数组缓冲列表索引,即buffers中正在使用的字节数组索引 */
	private int currentIndex;

	/** 所有已经装满字节的字节数组的总长度,不包含当前字节数组的长度 */
	private int filledBufferSum;

	/** 字节数组重置后是否可重用,默认true可重用 */
	private boolean reuse = true;

	/**
	 * JDK的同方法中close()也是空方法,字节数组无需关闭
	 */
	@Override
	public void close() throws IOException {
	}

	/**
	 * 创建一个新的字节数据或循环使用已经存在的字节数组
	 *
	 * @param 使用字节数组的大小
	 */
	protected void grow(final int newcount) {
		if (currentIndex < buffers.size() - 1) {
			// 调用reset()之后,从buffers的第一个重新开始使用旧的字符数组
			filledBufferSum += buf.length;
			currentIndex++;
			buf = buffers.get(currentIndex);
		} else {
			// 创建一个新的字节数组
			int newBufferSize;
			if (buf == null) {
				// 第一次创建字节数组
				newBufferSize = newcount;
				filledBufferSum = 0;
			} else {
				// 对buf扩容时,buf不为null,扩容为原buf容量的2倍或是新的字节长度减去已经装满字节的字节数组长度
				newBufferSize = Math.max(buf.length << 1, newcount - filledBufferSum);
				// 将已经装满字节的所有字节数组长度相加
				filledBufferSum += buf.length;
			}
			buf = new byte[newBufferSize];
			currentIndex++;
			buffers.add(buf);
		}
	}

	/**
	 * 重置参数,buffers中已经存在的字节数组仍然保留,但是从index=0的位置重新开始使用其中的buf,可重写本方法
	 * 
	 * @see java.io.ByteArrayOutputStream#reset()
	 */
	protected void reset() {
		count = 0;
		filledBufferSum = 0;
		currentIndex = 0;
		if (reuse) {
			// 从buffers中的第一个buf开始使用,不需要重新创建
			buf = buffers.get(currentIndex);
		} else {
			// 重置字节输入流
			buf = null;
			final int size = buffers.get(0).length;
			buffers.clear();
			grow(size);
			reuse = true;
		}
	}

	/**
	 * 返回字节数组已经使用的长度,可重写本方法
	 *
	 * @return 字节数组已经使用的长度
	 * @see java.io.ByteArrayOutputStream#size()
	 */
	public int size() {
		return count;
	};

	/**
	 * 获得当前已经使用的字节并复制到新的字节数组中返回,可重写本方法
	 * 
	 * @return 当前字节数组
	 * @see java.io.ByteArrayOutputStream#toByteArray()
	 */
	public byte[] toByteArray() {
		int remaining = count;
		if (remaining == 0) {
			return new byte[0];
		}
		final byte[] newbuf = new byte[remaining];
		int pos = 0;
		for (final byte[] buf : buffers) {
			final int c = Math.min(buf.length, remaining);
			System.arraycopy(buf, 0, newbuf, pos, c);
			pos += c;
			remaining -= c;
			if (remaining == 0) {
				break;
			}
		}
		return newbuf;
	}

	/**
	 * 将字符数组转成字符串输出
	 *
	 * @return 由字节数组转成的字符串
	 */
	@Override
	public String toString() {
		return new String(toByteArray(), ConstString.DEFAULT_CHARSET);
	}

	/**
	 * 将字符数组转成字符串输出
	 *
	 * @param charset 将字节数组encode时的编码集字符串
	 * @return 由字节数组转成的字符串
	 */
	public String toString(final String charsetName) throws UnsupportedEncodingException {
		return new String(toByteArray(), charsetName);
	}

	/**
	 * 将字符数组转成字符串输出
	 *
	 * @param charset 将字节数组encode时的编码集
	 * @return 由字节数组转成的字符串
	 */
	public String toString(final Charset charset) {
		return new String(toByteArray(), charset);
	}

	/**
	 * 向字节数组中添加字节,可重写本方法
	 * 
	 * @param b 需要写入的字节
	 * @see java.io.ByteArrayOutputStream#write(int)
	 */
	@Override
	public void write(final int b) {
		int inBufferPos = count - filledBufferSum;
		if (inBufferPos == buf.length) {
			grow(count + 1);
			inBufferPos = 0;
		}
		buf[inBufferPos] = (byte) b;
		count++;
	}

	/**
	 * 从字节输入流中读取字节写入到字节数组中
	 *
	 * @param input 字节输入流
	 * @return 从字节输入流中读取的总字节数量
	 */
	public int write(final InputStream in) throws IOException {
		int readCount = 0;
		int inBufferPos = count - filledBufferSum;
		int n = in.read(buf, inBufferPos, buf.length - inBufferPos);
		while (n != ConstIO.EOF) {
			readCount += n;
			inBufferPos += n;
			count += n;
			if (inBufferPos == buf.length) {
				grow(buf.length);
				inBufferPos = 0;
			}
			n = in.read(buf, inBufferPos, buf.length - inBufferPos);
		}
		return readCount;
	}

	/**
	 * 从字节数组指定索引开始写入指定长度的额外字节数组,可重写本方法
	 * 
	 * @param b 需要写入的字节数组
	 * @param off 开始写入的索引
	 * @param len 写入的字节数组长度
	 * @see java.io.ByteArrayOutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(final byte[] b, final int off, final int len) {
		// 需要写入的总长度
		final int newcount = count + len;
		// 仍然需要写入的长度
		int remaining = len;
		// 当前字节数组已经写入的长度
		int inBufferPos = count - filledBufferSum;
		// 循环写入字节,每次判断是否能全部写入当前buf,或者需要对buf进行扩容
		while (remaining > 0) {
			// 比较当前buf的剩余容量和remaining大小:若remaining小,只用写一次即可,否则buf需要扩容
			final int part = Math.min(remaining, buf.length - inBufferPos);
			System.arraycopy(b, off + len - remaining, buf, inBufferPos, part);
			remaining -= part;
			// 当前buf剩余容量不够,需要扩容,之后向新的buf的开始位置写入字节
			if (remaining > 0) {
				grow(newcount);
				inBufferPos = 0;
			}
		}
		count = newcount;
	}

	/**
	 * 从字节数组中读取字节到另外的出入流中,可重写本方法
	 *
	 * @param output 输出流
	 * @see java.io.ByteArrayOutputStream#writeTo(OutputStream)
	 */
	public void writeTo(final OutputStream out) throws IOException {
		int remaining = count;
		for (final byte[] buf : buffers) {
			final int c = Math.min(buf.length, remaining);
			out.write(buf, 0, c);
			remaining -= c;
			if (remaining == 0) {
				break;
			}
		}
	}
}