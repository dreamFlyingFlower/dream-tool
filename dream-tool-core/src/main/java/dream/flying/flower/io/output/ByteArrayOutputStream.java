package dream.flying.flower.io.output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 安全同步的字节数组实现类{@link java.io.ByteArrayOutputStream} FIXME
 * 
 * @author 飞花梦影
 * @date 2021-02-19 08:58:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ByteArrayOutputStream extends AbstractByteArrayOutputStream {

	/**
	 * 创建一个默认大小的字节数组
	 */
	public ByteArrayOutputStream() {
		this(DEFAULT_SIZE);
	}

	/**
	 * 创建一个指定大小的字节数组输出流,size不能小于0
	 *
	 * @param size 初始字节大小,不能小于0
	 */
	public ByteArrayOutputStream(final int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		synchronized (this) {
			grow(size);
		}
	}

	/**
	 * @see java.io.ByteArrayOutputStream#reset()
	 */
	@Override
	public synchronized void reset() {
		super.reset();
	}

	@Override
	public synchronized int size() {
		return super.size();
	}

	@Override
	public synchronized byte[] toByteArray() {
		return super.toByteArray();
	}

	@Override
	public synchronized void write(final int b) {
		super.write(b);
	}

	@Override
	public synchronized int write(final InputStream in) throws IOException {
		return super.write(in);
	}

	@Override
	public void write(final byte[] b, final int off, final int len) {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		synchronized (this) {
			super.write(b, off, len);
		}
	}

	@Override
	public synchronized void writeTo(final OutputStream out) throws IOException {
		super.writeTo(out);
	}
}