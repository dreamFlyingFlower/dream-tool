package dream.flying.flower.binary;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import dream.flying.flower.ConstArray;
import dream.flying.flower.ConstCharset;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.result.ResultException;

/**
 * Hex(16进制)工具类,默认所有涉及字符串编码的都是UTF-8
 *
 * @author 飞花梦影
 * @date 2021-02-21 10:51:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HexHelper {

	/**
	 * 将16进制字节数组转换为UTF8字符串之后再通过16进制解码为源字节数组,即字符串编码转换
	 *
	 * @param data 需要转换的16进制字节数组
	 * @return 转换编码后解码的字节数组
	 */
	public static byte[] decode(final byte[] data) {
		return decode(new String(data, ConstCharset.DEFAULT_CHARSET).toCharArray());
	}

	/**
	 * 将16进制字节数组转换为指定编码集字符串之后再通过16进制解码为源字节数组,即字符串编码转换
	 *
	 * @param data 需要转换的16进制字节数组
	 * @param charset data需要转换的字符串编码集
	 * @return 转换编码后解码的字节数组
	 */
	public static byte[] decode(final byte[] data, Charset charset) {
		return decode(new String(data, CharsetHelper.defaultCharset(charset)).toCharArray());
	}

	/**
	 * 将16进制字节数组转换为指定编码集字符串之后再通过16进制解码为源字节数组,即字符串编码转换
	 *
	 * @param data 需要转换的16进制字节数组
	 * @param encoding data需要转换的字符串编码集字符串
	 * @return 转换编码后解码的字节数组
	 */
	public static byte[] decode(final byte[] data, String encoding) {
		return decode(new String(data, CharsetHelper.defaultCharset(encoding)).toCharArray());
	}

	/**
	 * 将16进制字节缓冲对象转换为UTF8字符串之后再通过16进制解码为源字节数组,即字符串编码转换
	 * 
	 * @param buffer 字节缓冲对象
	 * @return 转换编码后解码的字节数组
	 */
	public static byte[] decode(final ByteBuffer buffer) {
		return decode(new String(toByteArray(buffer), ConstCharset.DEFAULT_CHARSET).toCharArray());
	}

	/**
	 * 将16进制字符数组转换为字节数组,返回源字节数组
	 *
	 * @param data 需要转换的只含有16进制字符的数组
	 * @return 16进制解码的字节数组
	 */
	public static byte[] decode(final char[] data) {
		final byte[] out = new byte[data.length >> 1];
		decode(data, out, 0);
		return out;
	}

	/**
	 * 将16进制字符数组转换为字节数组,返回字节数组长度
	 *
	 * @param data 需要转换的只含有16进制字符的数组
	 * @param out 将16进制字符转换后的字节数组
	 * @param outOffset 字节数组填充字节的起始索引
	 * @return 字符数组长度的1/2
	 */
	public static int decode(final char[] data, final byte[] out, final int outOffset) {
		final int len = data.length;
		if ((len & 0x01) != 0) {
			throw new ResultException("Odd number of characters.");
		}
		final int outLen = len >> 1;
		if (out.length - outOffset < outLen) {
			throw new ResultException("Output array is not large enough to accommodate decoded data.");
		}
		// two characters form the hex value.
		for (int i = outOffset, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}
		return outLen;
	}

	/**
	 * 将对象转换为字节数组,对象只能是String,byte[],char[],ByteBuffer
	 *
	 * @param object 需要转换的只含有16进制字符的对象
	 * @return 16进制解码的源字节数组
	 */
	public static byte[] decode(final Object object) {
		if (object instanceof String) {
			return decode(((String) object).toCharArray());
		} else if (object instanceof byte[]) {
			return decode((byte[]) object);
		} else if (object instanceof ByteBuffer) {
			return decode((ByteBuffer) object);
		} else {
			try {
				return decode((char[]) object);
			} catch (final ClassCastException e) {
				e.printStackTrace();
				throw new ResultException(e.getMessage(), e);
			}
		}
	}

	/**
	 * 将16进制字符串转换为字节数组
	 *
	 * @param data 需要转换的只含有16进制字符串
	 * @return 字节数组
	 */
	public static byte[] decode(final String data) {
		return decode(data.toCharArray());
	}

	/**
	 * 将Hex颜色值转为{@link Color}
	 *
	 * @param hexColor 16进制颜色值,可以以#,0x开头
	 * @return {@link Color}
	 */
	public static Color decodeColor(String hexColor) {
		return Color.decode(hexColor);
	}

	/**
	 * 将16进制字符串转换为字符串
	 *
	 * @param data 需要转换的只含有16进制字符串
	 * @return 转换后的字符串
	 */
	public static String decodeString(final String data) {
		return new String(decode(data.toCharArray()));
	}

	/**
	 * 将16进制字符串转换为字符串
	 *
	 * @param data 需要转换的只含有16进制字符串
	 * @param charset 字符集
	 * @return 转换后的字符串
	 */
	public static String decodeString(final String data, Charset charset) {
		return new String(decode(data.toCharArray()), charset);
	}

	/**
	 * 将字节数组转换为16进制字符串之后再转换为UTF8字节数组
	 *
	 * @param data 源字节数组
	 * @return 转换为16进制字符串之后再转换为UTF8编码的字节数组
	 */
	public static byte[] encode(final byte[] array) {
		return encodeHexString(array).getBytes(ConstCharset.DEFAULT_CHARSET);
	}

	/**
	 * 将字节缓冲数组转换为16进制字符串之后再进行UTF8编码转换为字节数组
	 *
	 * @param data 源字节缓冲对象
	 * @return 转换为16进制字符串之后再进行UTF8编码的字节数组,长度是源字节数组的2倍
	 */
	public static byte[] encode(final ByteBuffer data) {
		return encodeHexString(data).getBytes(ConstCharset.DEFAULT_CHARSET);
	}

	/**
	 * 将对象转换为用16进制表示的字符数组,字符全大写
	 * 
	 * @param object 对象
	 * @return 结果字符数组,长度是原字节数组的2倍
	 */
	public static char[] encode(final Object object) {
		byte[] byteArray;
		if (object instanceof String) {
			byteArray = ((String) object).getBytes(ConstCharset.DEFAULT_CHARSET);
		} else if (object instanceof ByteBuffer) {
			byteArray = toByteArray((ByteBuffer) object);
		} else {
			try {
				byteArray = (byte[]) object;
			} catch (final ClassCastException e) {
				throw new ResultException(e.getMessage(), e);
			}
		}
		return encodeHex(byteArray);
	}

	/**
	 * 将{@link Color}编码为Hex形式
	 *
	 * @param color {@link Color}
	 * @return Hex字符串
	 */
	public static String encodeColor(Color color) {
		return encodeColor(color, "#");
	}

	/**
	 * 将{@link Color}编码为Hex形式
	 *
	 * @param color {@link Color}
	 * @param prefix 前缀字符串,可以是#,0x等
	 * @return Hex字符串
	 */
	public static String encodeColor(Color color, String prefix) {
		final StringBuilder builder = new StringBuilder(prefix);
		String colorHex;
		colorHex = Integer.toHexString(color.getRed());
		if (1 == colorHex.length()) {
			builder.append('0');
		}
		builder.append(colorHex);
		colorHex = Integer.toHexString(color.getGreen());
		if (1 == colorHex.length()) {
			builder.append('0');
		}
		builder.append(colorHex);
		colorHex = Integer.toHexString(color.getBlue());
		if (1 == colorHex.length()) {
			builder.append('0');
		}
		builder.append(colorHex);
		return builder.toString();
	}

	/**
	 * 将字节数组转变为用16进制表示的字符数组,字符全大写
	 *
	 * @param data 字节数组
	 * @return 结果字符数组,长度是原字节数组的2倍
	 */
	public static char[] encodeHex(final byte[] data) {
		return encodeHex(data, false);
	}

	/**
	 * 将字节数组转变为用16进制表示的字符数组
	 *
	 * @param data 字节数组
	 * @param toLowerCase 字符是否小写.true->小写,false->大写
	 * @return 结果字符数组,长度是原字节数组的2倍
	 */
	public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? ConstArray.DIGITS_LOWER : ConstArray.DIGITS_UPPER);
	}

	/**
	 * 将字节数组转变为用16进制表示的字符数组
	 *
	 * @param data 字节数组
	 * @param toDigits 16进制中的字符,必须包含16个字符
	 * @return 结果字符数组,长度是原字节数组的2倍
	 */
	public static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		encodeHex(data, 0, data.length, toDigits, out, 0);
		return out;
	}

	/**
	 * 将字节数组转变为用16进制表示的字符数组
	 *
	 * @param data 字节数组
	 * @param byteStart 字节数组开始转换的索引
	 * @param dataLen 从start开始转换的字节数组长度
	 * @param toLowerCase 字符是否小写.true->小写,false->大写
	 * @return 结果字符数组,长度是原字节数组的2倍
	 */
	public static char[] encodeHex(final byte[] data, final int byteStart, final int dataLen,
			final boolean toLowerCase) {
		final char[] out = new char[dataLen << 1];
		encodeHex(data, byteStart, dataLen, toLowerCase ? ConstArray.DIGITS_LOWER : ConstArray.DIGITS_UPPER, out, 0);
		return out;
	}

	/**
	 * 将字节数组转变为用16进制表示的字符数组
	 *
	 * @param data 字节数组
	 * @param byteStart 字节数组开始转换的索引
	 * @param dataLen 从start开始转换的字节数组长度
	 * @param toLowerCase 字符是否小写.true->小写,false->大写
	 * @param charOut 结果字符数组
	 * @param charStart 结果字符数组填充字符的开始索引
	 */
	public static void encodeHex(final byte[] data, final int byteStart, final int dataLen, final boolean toLowerCase,
			final char[] charOut, final int charStart) {
		encodeHex(data, byteStart, dataLen, toLowerCase ? ConstArray.DIGITS_LOWER : ConstArray.DIGITS_UPPER, charOut,
				charStart);
	}

	/**
	 * 将字节数组转变为用16进制表示的字符数组
	 *
	 * @param data 字节数组
	 * @param byteStart 字节数组开始转换的索引
	 * @param dataLen 从start开始转换的字节数组长度
	 * @param toDigits 16进制中的字符,必须包含16个字符
	 * @param charOut 结果字符数组
	 * @param charStart 结果字符数组填充字符的开始索引
	 */
	private static void encodeHex(final byte[] data, final int start, final int dataLen, final char[] toDigits,
			final char[] charOut, final int charStart) {
		// two characters form the hex value.
		for (int i = start, j = charStart; i < start + dataLen; i++) {
			// 高位
			charOut[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			// 低位
			charOut[j++] = toDigits[0x0F & data[i]];
		}
	}

	/**
	 * 将字节缓冲数组转换为16进制字符数组
	 *
	 * @param data 源字节缓冲对象
	 * @return 转换为16进制之后的字符数组,字符全大写,长度是源字节数组的2倍
	 */
	public static char[] encodeHex(final ByteBuffer data) {
		return encodeHex(data, false);
	}

	/**
	 * 将字节缓冲数组转换为16进制字符数组
	 *
	 * @param data 源字节缓冲对象
	 * @param toLowerCase 字符串是否转换为小写.true->小写,false->大写
	 * @return 转换为16进制之后的字符数组,长度是源字节数组的2倍
	 */
	public static char[] encodeHex(final ByteBuffer data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? ConstArray.DIGITS_LOWER : ConstArray.DIGITS_UPPER);
	}

	/**
	 * 将字节缓冲数组转换为16进制字符数组
	 *
	 * @param data 源字节缓冲对象
	 * @param toDigits 16进制中的字符,必须包含16个字符
	 * @return 转换为16进制之后的字符数组,长度是源字节数组的2倍
	 */
	public static char[] encodeHex(final ByteBuffer byteBuffer, final char[] toDigits) {
		return encodeHex(toByteArray(byteBuffer), toDigits);
	}

	/**
	 * 将字节数组转换为16进制字符串
	 *
	 * @param data 源字节缓冲对象
	 * @return 转换为16进制之后的字符串,长度是源字节数组的2倍,字符串默认大写
	 */
	public static String encodeHexString(final byte[] data) {
		return new String(encodeHex(data));
	}

	/**
	 * 将字节数组转换为16进制字符串
	 *
	 * @param data 源字节缓冲对象
	 * @param toLowerCase 字符串是否转换为小写.true->小写,false->大写
	 * @return 转换为16进制之后的字符串,长度是源字节数组的2倍
	 */
	public static String encodeHexString(final byte[] data, final boolean toLowerCase) {
		return new String(encodeHex(data, toLowerCase));
	}

	/**
	 * 将字节缓冲数组转换为16进制字符串
	 *
	 * @param data 源字节缓冲对象
	 * @return 转换为16进制之后的字符串,长度是源字节数组的2倍,字符串默认大写
	 */
	public static String encodeHexString(final ByteBuffer data) {
		return new String(encodeHex(data));
	}

	/**
	 * 获得字节缓冲数组中的字符串
	 * 
	 * @param data 源字节缓冲对象
	 * @param toLowerCase 字符串是否转换为小写.true->小写,false->大写
	 * @return 转换为16进制之后的字符串,长度是源字节数组的2倍
	 */
	public static String encodeHexString(final ByteBuffer data, final boolean toLowerCase) {
		return new String(encodeHex(data, toLowerCase));
	}

	/**
	 * 获得字节缓冲数组中的字节数组
	 *
	 * @param byteBuffer 源字节缓冲对象
	 * @return 字节缓冲对象中的字节数组
	 */
	public static byte[] toByteArray(final ByteBuffer byteBuffer) {
		final int remaining = byteBuffer.remaining();
		// 若存在字节数组,直接返回,并将position移动到字节数组的位置
		if (byteBuffer.hasArray()) {
			final byte[] byteArray = byteBuffer.array();
			if (remaining == byteArray.length) {
				byteBuffer.position(remaining);
				return byteArray;
			}
		}
		// 复制字节数组并返回
		final byte[] byteArray = new byte[remaining];
		byteBuffer.get(byteArray);
		return byteArray;
	}

	/**
	 * 将16进制字符转换为Integer
	 * 
	 * @param ch 待转换的字符
	 * @param index 字符索引
	 * @return int
	 */
	public static int toDigit(final char ch, final int index) {
		final int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new ResultException("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

	/**
	 * 将int转为16进制字符串
	 *
	 * @param value int值
	 * @return 16进制字符串
	 */
	public static String toHex(int value) {
		return Integer.toHexString(value);
	}

	/**
	 * 转为16进制字符串
	 *
	 * @param value int值
	 * @return 16进制字符串
	 */
	public static String toHex(long value) {
		return Long.toHexString(value);
	}

	/**
	 * 将指定int值转换为Unicode字符串形式,常用于特殊字符,如汉字转Unicode形式
	 *
	 * @param value int值,也可以是char
	 * @return Unicode表现形式
	 */
	public static String toUnicodeHex(int value) {
		final StringBuilder builder = new StringBuilder(6);
		builder.append("\\u");
		String hex = toHex(value);
		int len = hex.length();
		if (len < 4) {
			builder.append("0000", 0, 4 - len);
		}
		builder.append(hex);
		return builder.toString();
	}

	/**
	 * 将指定char值转换为Unicode字符串形式,常用于特殊字符,如汉字转Unicode形式
	 *
	 * @param ch char值
	 * @return Unicode表现形式
	 */
	public static String toUnicodeHex(char ch) {
		return "\\u" + ConstArray.DIGITS_LOWER[(ch >> 12) & 15] + ConstArray.DIGITS_LOWER[(ch >> 8) & 15]
				+ ConstArray.DIGITS_LOWER[(ch >> 4) & 15] + ConstArray.DIGITS_LOWER[(ch) & 15];
	}
}