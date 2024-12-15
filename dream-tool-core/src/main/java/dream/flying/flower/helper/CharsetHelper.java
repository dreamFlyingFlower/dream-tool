package dream.flying.flower.helper;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import dream.flying.flower.ConstCharset;
import dream.flying.flower.lang.StrHelper;

/**
 * Charset字符集工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 16:08:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CharsetHelper {

	/**
	 * 返回手动指定的默认字符集:UTF-8
	 * 
	 * @return UTF-8
	 */
	public static Charset defaultCharset() {
		return ConstCharset.DEFAULT_CHARSET;
	}

	/**
	 * 返回指定字符集,若参数为null,返回utf8
	 *
	 * @param 字符集
	 * @return 字符集
	 */
	public static Charset defaultCharset(final Charset charset) {
		return charset == null ? ConstCharset.DEFAULT_CHARSET : charset;
	}

	/**
	 * 根据字符串返回字符集,若为空或不支持的字符集,返回utf8
	 *
	 * @param 字符集名称
	 * @return 字符集
	 */
	public static Charset defaultCharset(final String charsetName) {
		if (StrHelper.isNotBlank(charsetName) && Charset.isSupported(charsetName)) {
			return Charset.forName(charsetName);
		} else {
			return ConstCharset.DEFAULT_CHARSET;
		}
	}

	/**
	 * 返回指定字符集,若参数为null,返回系统默认字符集
	 *
	 * @param 字符集
	 * @return 字符集
	 */
	public static Charset systemCharset(final Charset charset) {
		return charset == null ? Charset.defaultCharset() : charset;
	}

	/**
	 * 根据字符串返回字符集,若为空或不支持的字符集,返回系统默认字符集
	 *
	 * @param 字符集名称
	 * @return 字符集
	 */
	public static Charset systemCharset(final String charsetName) {
		if (StrHelper.isNotBlank(charsetName) && Charset.isSupported(charsetName)) {
			return Charset.forName(charsetName);
		} else {
			return Charset.defaultCharset();
		}
	}

	/**
	 * 根据给定字符串返回系统字符集,若为空则返回系统默认字符集
	 *
	 * @param charsetName 字符集字符串
	 * @return 给定字符集字符串的charset的name
	 */
	public static String systemCharsetName(final String charsetName) {
		return StrHelper.isBlank(charsetName) ? Charset.defaultCharset().name() : charsetName;
	}

	/**
	 * 转换字符串的字符集编码
	 * 
	 * @param source 源字符串
	 * @param srcCharset 源字符集,默认ISO-8859-1
	 * @param destCharset 目标字符集,默认UTF-8
	 * @return 转换后的字符
	 */
	public static String convert(String source, String srcCharset, String destCharset) {
		return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
	}

	/**
	 * 转换字符串的字符集编码
	 * 
	 * @param source 源字符串
	 * @param srcCharset 源字符集,默认ISO-8859-1
	 * @param destCharset 目标字符集,默认UTF-8
	 * @return 转换后的字符
	 */
	public static String convert(String source, Charset srcCharset, Charset destCharset) {
		if (null == srcCharset) {
			srcCharset = StandardCharsets.ISO_8859_1;
		}

		if (null == destCharset) {
			destCharset = StandardCharsets.UTF_8;
		}
		if (StrHelper.isBlank(source) || srcCharset.equals(destCharset)) {
			return source;
		}
		return new String(source.getBytes(srcCharset), destCharset);
	}
}