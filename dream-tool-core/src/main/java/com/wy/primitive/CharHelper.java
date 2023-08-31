package com.wy.primitive;

import com.wy.lang.NumberHelper;

/**
 * Char工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 22:47:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CharHelper {

	/**
	 * 判断源字符是否ASCII 7位字母
	 *
	 * @param ch 源字符
	 * @return true当ch值在65到90或97到122之间
	 */
	public static boolean isAsciiAlpha(final char ch) {
		return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
	}

	/**
	 * 判断源字符是否ASCII 7位大写字母
	 *
	 * @param ch 源字符
	 * @return true当ch值在65到90之间
	 */
	public static boolean isAsciiAlphaUpper(final char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * 判断源字符是否ASCII 7位小写字母
	 * 
	 * @param ch 源字符
	 * @return true当ch值在97到122之间
	 */
	public static boolean isAsciiAlphaLower(final char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	/**
	 * 判断字符是否为ASCII 7位数字
	 *
	 * @param ch 源字符
	 * @return true当字符值在48到57之间
	 */
	public static boolean isAsciiNumeric(final char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * 检查字符是否为ASCII 7位可打印字符
	 * 
	 * <pre>
	 * isAsciiPrintable('\n') = false 
	 * isAsciiPrintable('&copy;') = false
	 * </pre>
	 *
	 * @param ch 源字符
	 * @return true当ch值在32到127之间
	 */
	public static boolean isAsciiPrintable(final char ch) {
		return ch >= 32 && ch < 127;
	}

	/**
	 * 判断是否是一个char字符
	 * 
	 * @param clazz 类
	 * @return true->是,false->否
	 */
	public static boolean isChar(Class<?> clazz) {
		return clazz == char.class || clazz == Character.class || char.class.isAssignableFrom(clazz)
				|| Character.class.isAssignableFrom(clazz);
	}

	/**
	 * 判断对象是否为null,null返回默认字符,非null直接返回
	 *
	 * @param ch 对象
	 * @param defaultValue 源字符为null时的默认字符
	 * @return 源字符或默认字符
	 */
	public static char toChar(Object ch, final char defaultValue) {
		return ch == null ? defaultValue : Character.valueOf((char) NumberHelper.toInt(ch).intValue());
	}

	/**
	 * 判断源字符是否为null,null返回默认字符,非null直接返回
	 *
	 * @param ch 源字符
	 * @param defaultValue 源字符为null时的默认字符
	 * @return 源字符或默认字符
	 */
	public static char toChar(final Character ch, final char defaultValue) {
		return ch == null ? defaultValue : ch.charValue();
	}
}