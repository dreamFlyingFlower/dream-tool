package com.dream.binary;

import java.nio.charset.Charset;
import java.util.Base64;

import com.dream.ConstArray;
import com.dream.ConstLang;
import com.dream.helper.CharsetHelper;
import com.dream.lang.StrHelper;

/**
 * Base64工具类,{@link Base64}
 * 
 * @author 飞花梦影
 * @date 2021-03-06 19:25:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Base64Helper {

	/**
	 * 对字节数组进行Base64解码
	 * 
	 * @param src 需要解码的字节数组
	 * @return 解码后的字节数组
	 */
	public static byte[] decode(byte[] src) {
		if (src == null || src.length == 0) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getDecoder().decode(src);
	}

	/**
	 * 将字符串按照UTF8转换为字节数组后再进行Base64解码
	 * 
	 * @param src 需要Base64解码的UTF8字符串
	 * @return 原字节数组
	 */
	public static byte[] decode(String src) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return decode(src.getBytes(ConstLang.DEFAULT_CHARSET));
	}

	/**
	 * 将字符串按照指定编码集转换为字节数组后再进行Base64解码
	 * 
	 * @param src 需要Base64解码的字符串
	 * @param charset 字符串转换为字节数值时的编码集
	 * @return 原字节数组
	 */
	public static byte[] decode(String src, Charset charset) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return decode(src.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * 将字符串按照指定编码集转换为字节数组后再进行Base64解码
	 * 
	 * @param src 需要Base64解码的字符串
	 * @param encoding 字符串转换为字节数值时的编码集字符串
	 * @return 原字节数组
	 */
	public static byte[] decode(String src, String encoding) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return decode(src.getBytes(CharsetHelper.defaultCharset(encoding)));
	}

	/**
	 * 对URL字节数组进行解码,使用RFC 4648文档
	 * 
	 * @param src 编码后的字节数组
	 * @return 原始字节数组
	 */
	public static byte[] decodeUrl(byte[] src) {
		if (src == null || src.length == 0) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlDecoder().decode(src);
	}

	/**
	 * 将URL按照UTF8转换为字节数组后再进行Base64解码,使用RFC 4648文档
	 * 
	 * @param src UTF8字符串
	 * @return 原字节数组
	 */
	public static byte[] decodeUrl(String src) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlDecoder().decode(src.getBytes(ConstLang.DEFAULT_CHARSET));
	}

	/**
	 * 对URL转换为指定编码字节数组进行Base64解码,使用RFC 4648文档
	 * 
	 * @param src 源字符串
	 * @param charset 转换字符串的编码集
	 * @return 编码后的字节数组
	 */
	public static byte[] decodeUrl(String src, Charset charset) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlDecoder().decode(src.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * 对URL转换为指定编码字节数组进行Base64解码,使用RFC 4648文档
	 * 
	 * @param src 源字符串
	 * @param encoding 转换字符串的编码集字符串
	 * @return 编码后的字节数组
	 */
	public static byte[] decodeUrl(String src, String encoding) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlDecoder().decode(src.getBytes(CharsetHelper.defaultCharset(encoding)));
	}

	/**
	 * 对字节数组进行Base64编码
	 * 
	 * @param src 源字节数组
	 * @return 编码后的字节数组
	 */
	public static byte[] encode(byte[] src) {
		if (src == null || src.length == 0) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getEncoder().encode(src);
	}

	/**
	 * 将字符串转换为UTF8字节数组,再进行Base64编码
	 * 
	 * @param src 源字符串
	 * @return 编码后的字节数组
	 */
	public static byte[] encode(String src) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getEncoder().encode(src.getBytes(ConstLang.DEFAULT_CHARSET));
	}

	/**
	 * 将字符串转换指定编码的字节数组,再进行Base64编码
	 * 
	 * @param src 源字符串
	 * @param charset 转换字符串的编码集
	 * @return 编码后的字节数组
	 */
	public static byte[] encode(String src, Charset charset) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getEncoder().encode(src.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * 将字符串转换指定编码的字节数组,再进行Base64编码
	 * 
	 * @param src 源字符串
	 * @param encoding 转换字符串的编码集字符串
	 * @return 编码后的字节数组
	 */
	public static byte[] encode(String src, String encoding) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getEncoder().encode(src.getBytes(CharsetHelper.defaultCharset(encoding)));
	}

	/**
	 * 对URL字节数组进行编码,使用RFC 4648文档
	 * 
	 * @param src 源字节数组
	 * @return 编码后的字节数组
	 */
	public static byte[] encodeUrl(byte[] src) {
		if (src == null || src.length == 0) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlEncoder().encode(src);
	}

	/**
	 * 对URL转换为UTF8字节数组进行Base64编码,使用RFC 4648文档
	 * 
	 * @param src 源字符串
	 * @return 编码后的字节数组
	 */
	public static byte[] encodeUrl(String src) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlEncoder().encode(src.getBytes(ConstLang.DEFAULT_CHARSET));
	}

	/**
	 * 对URL转换为UTF8字节数组进行Base64编码,使用RFC 4648文档
	 * 
	 * @param src 源字符串
	 * @param charset 转换字符串的编码集
	 * @return 编码后的字节数组
	 */
	public static byte[] encodeUrl(String src, Charset charset) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlEncoder().encode(src.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * 对URL转换为UTF8字节数组进行Base64编码,使用RFC 4648文档
	 * 
	 * @param src 源字符串
	 * @param encoding 转换字符串的编码集字符串
	 * @return 编码后的字节数组
	 */
	public static byte[] encodeUrl(String src, String encoding) {
		if (StrHelper.isBlank(src)) {
			return ConstArray.EMPTY_BYTE;
		}
		return Base64.getUrlEncoder().encode(src.getBytes(CharsetHelper.defaultCharset(encoding)));
	}

	/**
	 * 将字节数组进行Base64编码后,转换为UTF8字符串
	 * 
	 * @param src 源字节数组
	 * @return Base64编码并UTF8编码后的字符串
	 */
	public static String encodeString(byte[] src) {
		if (src == null || src.length == 0) {
			return ConstLang.STR_EMPTY;
		}
		return new String(encode(src), ConstLang.DEFAULT_CHARSET);
	}

	/**
	 * 将URL字节数组进行Base64编码后,再用UTF8编码,转换为字符串
	 * 
	 * @param src 源字节数组
	 * @return 编码后的URL字符串
	 */
	public static String encodeUrlString(byte[] src) {
		if (src == null || src.length == 0) {
			return ConstLang.STR_EMPTY;
		}
		return new String(encodeUrl(src), ConstLang.DEFAULT_CHARSET);
	}
}