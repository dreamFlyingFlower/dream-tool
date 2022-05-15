package com.wy.lang;

import java.util.Objects;

import com.wy.ConstLang;
import com.wy.primitive.CharTool;
import com.wy.util.ArrayTool;

/**
 * CharSequence相关工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 22:16:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CharSequenceTool {

	/**
	 * 计算源字符序列中出现待查找字符的次数
	 *
	 * @param str 源字符序列
	 * @param ch 待查找字符
	 * @return 源字符序列中出现待查找字符的次数
	 */
	public static int countMatches(final CharSequence str, final char ch) {
		if (isEmpty(str)) {
			return 0;
		}
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (ch == str.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 判断源字符序列中是否含有指定字符序列中至少一个字符,大小写敏感
	 *
	 * @param cs 字符序列
	 * @param searchChars 指定字符序列
	 * @return true当源字符序列中含有至少一个指定字符序列中的字符
	 */
	public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
		if (cs == null || searchChars == null) {
			return false;
		}
		return containsAny(cs, searchChars.toString().toCharArray());
	}

	/**
	 * 判断源字符序列中是否含有字符数组中至少一个元素,大小写敏感
	 *
	 * @param cs 字符序列
	 * @param searchChars 字符数组
	 * @return true当源字符序列中含有至少一个字符数组中的元素
	 */
	public static boolean containsAny(final CharSequence cs, final char... searchChars) {
		if (isEmpty(cs) || ArrayTool.isEmpty(searchChars)) {
			return false;
		}
		final int csLength = cs.length();
		final int searchLength = searchChars.length;
		final int csLast = csLength - 1;
		final int searchLast = searchLength - 1;
		for (int i = 0; i < csLength; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return true;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return true;
						}
					} else {
						// ch is in the Basic Multilingual Plane
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断源字符序列中是否含有待查找序列,大小写不敏感
	 *
	 * @param str 源字符序列
	 * @param searchStr 待查找字符序列
	 * @return true当源字符序列中含有待查找字符序列
	 */
	public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		final int len = searchStr.length();
		final int max = str.length() - len;
		for (int i = 0; i <= max; i++) {
			if (regionMatches(str, true, i, searchStr, 0, len)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断源字符序列是否不包含待查找字符串中任意字符
	 *
	 * @param cs 源字符序列
	 * @param invalidChars 待查找字符串
	 * @return true当源字符序列是否不包含待查找字符串中任意字符
	 */
	public static boolean containsNone(final CharSequence cs, final String invalidChars) {
		if (cs == null || invalidChars == null) {
			return true;
		}
		return containsNone(cs, invalidChars.toCharArray());
	}

	/**
	 * 判断源字符序列是否不包含待查找字符串中任意字符
	 *
	 * @param cs 源字符序列
	 * @param searchChars 待查找字符数组
	 * @return true当源字符序列不包含待查找字符数组中任意字符
	 */
	public static boolean containsNone(final CharSequence cs, final char... searchChars) {
		if (cs == null || searchChars == null) {
			return true;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return false;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return false;
						}
					} else {
						// ch is in the Basic Multilingual Plane
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 判断源字符序列是否只包含待查找字符串中的字符
	 *
	 * @param cs 源字符序列
	 * @param validChars 待查找字符串
	 * @return true当源字符序列只包含待查找字符串中的字符
	 */
	public static boolean containsOnly(final CharSequence cs, final String validChars) {
		if (cs == null || validChars == null) {
			return false;
		}
		return containsOnly(cs, validChars.toCharArray());
	}

	/**
	 * 判断源字符序列是否只包含待查找字符数组中的字符
	 *
	 * @param cs 源字符序列
	 * @param valid 待查找字符数组
	 * @return true当源字符序列只包含待查找字符数组中的字符
	 */
	public static boolean containsOnly(final CharSequence cs, final char... valid) {
		if (cs.length() == 0) {
			return true;
		}
		if (valid.length == 0) {
			return false;
		}
		return indexOfAnyBut(cs, valid) == ConstLang.INDEX_NOT_FOUND;
	}

	/**
	 * 判断源字符序列是否以待查找字符数组中任意元素结束,大小写敏感
	 *
	 * @param cs 源字符序列
	 * @param searchStrs 待查找字符序列数组
	 * @return true当源字符序列以待查找字符数组中任意元素结束
	 */
	public static boolean endsWithAny(final CharSequence cs, final CharSequence... searchStrs) {
		if (isEmpty(cs) || ArrayTool.isEmpty(searchStrs)) {
			return false;
		}
		for (final CharSequence searchStr : searchStrs) {
			if (endsWith(cs, searchStr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断源字符序列是否以待查找字符数组中任意元素结束,大小写不敏感
	 *
	 * @param cs 源字符序列
	 * @param searchStrs 待查找字符序列数组
	 * @return true当源字符序列以待查找字符数组中任意元素结束
	 */
	public static boolean endsWithAnyIgnoreCase(final CharSequence cs, final CharSequence... searchStrs) {
		if (isEmpty(cs) || ArrayTool.isEmpty(searchStrs)) {
			return false;
		}
		for (final CharSequence searchStr : searchStrs) {
			if (endsWith(cs, searchStr, true)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断源字符序列是否以待查找字符序列结束,大小写不敏感
	 *
	 * @param cs 源字符序列
	 * @param suffix 待查找字符序列
	 * @return true当源字符序列以待查找字符序列结束
	 */
	public static boolean endsWithIgnoreCase(final CharSequence cs, final CharSequence suffix) {
		return endsWith(cs, suffix, true);
	}

	/**
	 * 判断源字符序列是否以待查找字符序列结束,大小写敏感
	 *
	 * @param str 源字符序列
	 * @param suffix 待查找字符序列
	 * @return true当源字符序列以待查找字符序列结束
	 */
	public static boolean endsWith(final CharSequence str, final CharSequence suffix) {
		return endsWith(str, suffix, false);
	}

	/**
	 * 判断源字符序列是否以待查找字符序列结束,大小写敏感
	 * 
	 * @param cs 源字符序列
	 * @param suffix 待查找字符序列
	 * @param ignoreCase true大小写不敏感;false大小写敏感
	 * @return true当源字符序列以待查找字符序列结束
	 */
	public static boolean endsWith(final CharSequence cs, final CharSequence suffix, final boolean ignoreCase) {
		if (cs == null || suffix == null) {
			return cs == suffix;
		}
		if (suffix.length() > cs.length()) {
			return false;
		}
		final int strOffset = cs.length() - suffix.length();
		return regionMatches(cs, ignoreCase, strOffset, suffix, 0, suffix.length());
	}

	/**
	 * 判断源字符序列是否和待查找字符序列数组中任意元素equals,大小写敏感
	 * 
	 * @param cs 源字符序列
	 * @param searchStrs 待查找的字符序列数组
	 * @return true当源字符序列和待查找字符序列数组中任意元素equals
	 */
	public static boolean equalsAny(final CharSequence cs, final CharSequence... searchStrs) {
		if (ArrayTool.isNotEmpty(searchStrs)) {
			for (final CharSequence searchStr : searchStrs) {
				if (Objects.equals(cs, searchStr)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断源字符序列是否和待查找字符序列数组中任意元素equals,大小写不敏感
	 *
	 * @param cs 源字符序列
	 * @param searchStrs 待查找的字符序列数组
	 * @return true当源字符序列和待查找字符序列数组中任意元素equals
	 */
	public static boolean equalsAnyIgnoreCase(final CharSequence cs, final CharSequence... searchStrs) {
		if (ArrayTool.isNotEmpty(searchStrs)) {
			for (final CharSequence searchStr : searchStrs) {
				if (equalsIgnoreCase(cs, searchStr)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断2个字符序列是否equals,大小写不敏感
	 *
	 * @param cs1 字符序列1
	 * @param cs2 字符序列2
	 * @return true当2个字符序列equals
	 */
	public static boolean equalsIgnoreCase(final CharSequence cs1, final CharSequence cs2) {
		if (cs1 == cs2) {
			return true;
		}
		if (cs1 == null || cs2 == null) {
			return false;
		}
		if (cs1.length() != cs2.length()) {
			return false;
		}
		return regionMatches(cs1, true, 0, cs2, 0, cs1.length());
	}

	/**
	 * 获得一个序列数组中第一个非空字符序列,若数组为空,返回null
	 *
	 * @param <T> 字符类型
	 * @param values 字符序列数组
	 * @return 第一个非空值
	 */
	@SafeVarargs
	public static <T extends CharSequence> T firstNonBlank(final T... values) {
		if (ArrayTool.isEmpty(values)) {
			return null;
		}
		for (final T val : values) {
			if (isNotBlank(val)) {
				return val;
			}
		}
		return null;
	}

	/**
	 * 获得一个序列数组中第一个非null或""的字符序列,若数组为空,返回null
	 *
	 * @param <T> 字符类型
	 * @param values 字符序列数组
	 * @return 第一个非null或""值
	 */
	@SafeVarargs
	public static <T extends CharSequence> T firstNonEmpty(final T... values) {
		if (ArrayTool.isEmpty(values)) {
			return null;
		}
		if (values != null) {
			for (final T val : values) {
				if (isNotEmpty(val)) {
					return val;
				}
			}
		}
		return null;
	}

	/**
	 * 若源字符串空,则返回"";若不为空,返回源字符串
	 *
	 * @param str 源字符串
	 * @return 源字符串或""
	 */
	public static String getDefault(final String str) {
		return isBlank(str) ? "" : str;
	}

	/**
	 * 若源字符串空,则返回默认字符串;若不为空,返回源字符串
	 *
	 * @param <T> 泛型
	 * @param str 源字符串
	 * @param defaultStr 源字符串为空时返回的默认字符串
	 * @return 源字符串或默认字符串
	 */
	public static <T extends CharSequence> T getDefault(final T str, final T defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	/**
	 * 若源字符串null或"",则返回默认字符串;若不为null或"",返回源字符串
	 *
	 * @param <T> 泛型
	 * @param str 源字符串
	 * @param defaultStr源字符串为空时返回的默认字符串
	 * @return 源字符串或默认字符串
	 */
	public static <T extends CharSequence> T getIfEmpty(final T str, final T defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	/**
	 * 检查源字符串是否为null或长度为0,不会对字符串进行trim()操作
	 * 
	 * @param str 源字符串
	 * @return true当源字符串为null或长度为0
	 */
	public static boolean hasLength(final CharSequence cs) {
		return length(cs) > 0;
	}

	/**
	 * 从源字符序列中查找不属于待查找字符数组中元素的第一个字符索引,若全部都有,返回-1
	 *
	 * @param cs 源字符序列
	 * @param searchChars 待查找字符数组
	 * @return 第一个不属于待查找字符数组元素的索引,若全部都有,返回-1
	 */
	public static int indexOfAnyBut(final CharSequence cs, final char... searchChars) {
		if (isEmpty(cs) || ArrayTool.isEmpty(searchChars)) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		final int csLen = cs.length();
		final int csLast = csLen - 1;
		final int searchLen = searchChars.length;
		final int searchLast = searchLen - 1;
		outer: for (int i = 0; i < csLen; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLen; j++) {
				if (searchChars[j] == ch) {
					if (i < csLast && j < searchLast && Character.isHighSurrogate(ch)) {
						if (searchChars[j + 1] == cs.charAt(i + 1)) {
							continue outer;
						}
					} else {
						continue outer;
					}
				}
			}
			return i;
		}
		return ConstLang.INDEX_NOT_FOUND;
	}

	/**
	 * 从源字符序列起始指定位置开始查找指定字符序列第一次出现的索引,忽略大小写<br>
	 * 若指定位置大于源字符序列长度,返回-1;若指定位置小于0,默认从起始开始查找
	 *
	 * @param str 源字符序列
	 * @param searchStr 要查找到的字符序列
	 * @return 第一次查找的索引
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * 从源字符序列起始指定位置开始查找指定字符序列第一次出现的索引,忽略大小写<br>
	 * 若指定位置大于源字符序列长度,返回-1;若指定位置小于0,默认从起始开始查找
	 *
	 * @param str 源字符序列
	 * @param searchStr 要查找到的字符序列
	 * @param start 起始索引
	 * @return 第一次查找的索引
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int start) {
		if (str == null || searchStr == null) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return start;
		}
		start = start < 0 ? 0 : start;
		int endLimit = str.length() - searchStr.length() + 1;
		if (start > endLimit) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		for (int i = start; i < endLimit; i++) {
			if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return ConstLang.INDEX_NOT_FOUND;
	}

	/**
	 * 比较2个字符序列中的字符,返回第一个不同字符的索引
	 * 
	 * @param cs1 字符序列1
	 * @param cs2 字符序列2
	 * @return 第一个不同字符的索引,若都相同,返回-1
	 */
	public static int indexOfDifference(final CharSequence cs1, final CharSequence cs2) {
		if (cs1 == cs2) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		if (cs1 == null || cs2 == null) {
			return 0;
		}
		int i;
		for (i = 0; i < cs1.length() && i < cs2.length(); ++i) {
			if (cs1.charAt(i) != cs2.charAt(i)) {
				break;
			}
		}
		if (i < cs2.length() || i < cs1.length()) {
			return i;
		}
		return ConstLang.INDEX_NOT_FOUND;
	}

	/**
	 * 比较所有字符序列中的字符,返回第一个不同字符的索引
	 *
	 * @param css 字符序列数组
	 * @return 第一个不同字符的索引,若都相同,返回-1
	 */
	public static int indexOfDifference(final CharSequence... css) {
		if (ArrayTool.length(css) <= 1) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		boolean anyStringNull = false;
		boolean allStringsNull = true;
		final int arrayLen = css.length;
		int shortestStrLen = Integer.MAX_VALUE;
		int longestStrLen = 0;
		// 只需要找出最短和最长的字符序列即可
		for (final CharSequence cs : css) {
			if (cs == null) {
				anyStringNull = true;
				shortestStrLen = 0;
			} else {
				allStringsNull = false;
				shortestStrLen = Math.min(cs.length(), shortestStrLen);
				longestStrLen = Math.max(cs.length(), longestStrLen);
			}
		}
		// 处理都是null和部分""
		if (allStringsNull || longestStrLen == 0 && !anyStringNull) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		// 处理都是""
		if (shortestStrLen == 0) {
			return 0;
		}
		// 循环比较找到第一个不一样的字符
		int firstDiff = -1;
		for (int stringPos = 0; stringPos < shortestStrLen; stringPos++) {
			final char comparisonChar = css[0].charAt(stringPos);
			for (int arrayPos = 1; arrayPos < arrayLen; arrayPos++) {
				if (css[arrayPos].charAt(stringPos) != comparisonChar) {
					firstDiff = stringPos;
					break;
				}
			}
			if (firstDiff != -1) {
				break;
			}
		}
		// 所有的字符串都和最短字符串前缀相同,返回最短字符串的长度
		if (firstDiff == -1 && shortestStrLen != longestStrLen) {
			return shortestStrLen;
		}
		return firstDiff;
	}

	/**
	 * 判断字符序列中所有字符都是小写,只判断a-z,若含有其他字符或空,返回false
	 *
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都为小写
	 */
	public static boolean isAllLowerCase(final CharSequence cs) {
		if (isBlank(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isLowerCase(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中所有字符都是大写,只判断a-z,若含有其他字符或空,返回false
	 * 
	 * @param cs 字符序列
	 * @return true当所有字符都为大写
	 */
	public static boolean isAllUpperCase(final CharSequence cs) {
		if (isBlank(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isUpperCase(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中所有字符都是unicode字母,不包含数字
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都为unicode字母
	 */
	public static boolean isAlpha(final CharSequence cs) {
		if (isBlank(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isLetter(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中所有字符都是unicode字母或数字
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都为unicode字母或数字
	 */
	public static boolean isAlphanumeric(final CharSequence cs) {
		if (isBlank(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isLetterOrDigit(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中所有字符都是unicode字母或数字或空白
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都为unicode字母或数字或空白
	 */
	public static boolean isAlphanumericSpace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中所有字符都是unicode字母或空白
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都为unicode字母或空白
	 */
	public static boolean isAlphaSpace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中所有字符都是可打印的ASCII字符,null返回false
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都为可打印的ASCII字符
	 */
	public static boolean isAsciiPrintable(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!CharTool.isAsciiPrintable(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断数组中是否有至少一个空字符序列
	 * 
	 * @param css 字符序列数组
	 * @return true当有数组中至少一个空字符序列
	 */
	public static boolean isAnyBlank(final CharSequence... css) {
		if (ArrayTool.isEmpty(css)) {
			return false;
		}
		for (final CharSequence cs : css) {
			if (isBlank(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断数组中至少有一个字符序列是否为null或"",其他不判断
	 *
	 * @param css 字符序列数组
	 * @return true当数组中至少有一个字符序列为null或""
	 */
	public static boolean isAnyEmpty(final CharSequence... css) {
		if (ArrayTool.isEmpty(css)) {
			return false;
		}
		for (final CharSequence cs : css) {
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符序列是否为null,""," ",空白字符串,制表符,换行符
	 *
	 * @param cs 字符序列
	 * @return true为null,""," "等
	 */
	public static boolean isBlank(final CharSequence cs) {
		final int strLen = length(cs);
		if (strLen == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断所有字符序列是否为null,""," ",空白字符串,制表符,换行符;空数组返回true
	 *
	 * @param css 字符序列数组
	 * @return true为空数组或null,""," "等
	 */
	public static boolean isBlank(final CharSequence... css) {
		if (ArrayTool.isEmpty(css)) {
			return true;
		}
		for (final CharSequence cs : css) {
			if (isNotBlank(cs)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否为中文字符,包括标点等
	 * 
	 * CJK:是“Chinese，Japanese，Korea”的简写,实际上就是指中日韩三国的象形文字的Unicode编码<br>
	 * CJK_UNIFIED_IDEOGRAPHS:4E00-9FBF:CJK 统一表意符号<br>
	 * CJK_COMPATIBILITY_IDEOGRAPHS:F900-FAFF:CJK 兼容象形文字<br>
	 * CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A:3400-4DBF:CJK 统一表意符号扩展A<br>
	 * CJK_SYMBOLS_AND_PUNCTUATION:3000-303F:CJK 符号和标点<br>
	 * HALFWIDTH_AND_FULLWIDTH_FORMS:FF00-FFEF:半角及全角形式<br>
	 * GENERAL_PUNCTUATION:2000-206F:常用标点
	 * 
	 * @param c 字符
	 * @return true当字符是中文时
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符序列中是否全是中文,包括中文标点符号等
	 * 
	 * @param cs 需要检查的字符串
	 * @return true:是,false:不是
	 */
	public static boolean isChinese(CharSequence cs) {
		if (isBlank(cs)) {
			return false;
		}
		char[] array = cs.toString().toCharArray();
		for (char c : array) {
			if (!isChinese(c)) {
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断字符序列是否为null或"",其他不判断
	 *
	 * @param cs 字符序列
	 * @return true当字符序列为null或""
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return length(cs) == 0;
	}

	/**
	 * 判断所有字符序列是否为null或"",其他不判断;空数组返回true
	 *
	 * @param css 字符序列数组
	 * @return true当字符序列为空数组或null,""
	 */
	public static boolean isEmpty(final CharSequence... css) {
		if (ArrayTool.isEmpty(css)) {
			return true;
		}
		for (final CharSequence cs : css) {
			if (isNotEmpty(cs)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符序列中同时包含大小写字符
	 *
	 * @param cs 字符序列
	 * @return true当字符同时包含大小写
	 */
	public static boolean isMixedCase(final CharSequence cs) {
		if (isEmpty(cs) || cs.length() == 1) {
			return false;
		}
		boolean containsUppercase = false;
		boolean containsLowercase = false;
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (containsUppercase && containsLowercase) {
				return true;
			} else if (Character.isUpperCase(cs.charAt(i))) {
				containsUppercase = true;
			} else if (Character.isLowerCase(cs.charAt(i))) {
				containsLowercase = true;
			}
		}
		return containsUppercase && containsLowercase;
	}

	/**
	 * 判断字符序列是否为null,""," ",空白字符串,制表符,换行符
	 *
	 * @param cs 字符序列
	 * @return true不为null,""," "等
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * 判断所有字符序列都不为空
	 * 
	 * @param css 字符序列数组
	 * @return true当所有字符序列都不为空
	 */
	public static boolean isNotBlank(final CharSequence... css) {
		return !isAnyBlank(css);
	}

	/**
	 * 判断字符序列是否为null或"",其他不判断
	 *
	 * @param cs 字符序列
	 * @return true当字符序列不为null或""
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * 判断数组中所有字符序列都不为null或"",其他不判断
	 *
	 * @param css 字符序列数组
	 * @return true当数组中所有字符序列都不为null或""
	 */
	public static boolean isNotEmpty(final CharSequence... css) {
		return !isAnyEmpty(css);
	}

	/**
	 * 判断字符序列是否只包含unicode数字.小数点,+,-不属于unicode数字,返回false
	 *
	 * @param cs 字符序列
	 * @return true当所有字符都只包含unicode数字
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (isBlank(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 从源字符序列末尾指定位置开始查找指定字符序列第一次出现的索引,忽略大小写<br>
	 * 若指定位置大于源字符序列长度,默认从末尾开始查找; 若指定位置小于0,返回-1
	 *
	 * @param str 源字符序列
	 * @param searchStr 要查找到的字符序列
	 * @return 第一次查找的索引
	 */
	public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return lastIndexOfIgnoreCase(str, searchStr, str.length());
	}

	/**
	 * 从源字符序列末尾指定位置开始查找指定字符序列第一次出现的索引,忽略大小写<br>
	 * 若指定位置大于源字符序列长度,默认从末尾开始查找; 若指定位置小于0,返回-1
	 *
	 * @param str 源字符序列
	 * @param searchStr 要查找到的字符序列
	 * @param start 起始索引
	 * @return 第一次查找的索引
	 */
	public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int start) {
		if (str == null || searchStr == null) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return start;
		}
		start = start > str.length() - searchStr.length() ? str.length() - searchStr.length() : start;
		if (start < 0) {
			return ConstLang.INDEX_NOT_FOUND;
		}
		for (int i = start; i >= 0; i--) {
			if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return ConstLang.INDEX_NOT_FOUND;
	}

	/**
	 * 获得一个字符序列的长度.若为空,返回0;若为空白字符串,仍返回空白字符串长度
	 *
	 * @param cs 字符序列
	 * @return 字符序列的长度
	 */
	public static int length(final CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 字符串匹配
	 *
	 * @param cs 源字符序列
	 * @param ignoreCase true大小写不敏感;false大小写敏感
	 * @param thisStart 源字符序列开始索引
	 * @param searchStr 待查找字符序列
	 * @param start 待查找字符序列开始索引
	 * @param length 待查找的字符串长度
	 * @return true匹配
	 */
	static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
			final CharSequence searchStr, final int start, final int length) {
		if (cs instanceof String && searchStr instanceof String) {
			return ((String) cs).regionMatches(ignoreCase, thisStart, (String) searchStr, start, length);
		}
		int index1 = thisStart;
		int index2 = start;
		int tmpLen = length;
		final int srcLen = cs.length() - thisStart;
		final int otherLen = searchStr.length() - start;
		if (thisStart < 0 || start < 0 || length < 0) {
			return false;
		}
		if (srcLen < length || otherLen < length) {
			return false;
		}
		while (tmpLen-- > 0) {
			final char c1 = cs.charAt(index1++);
			final char c2 = searchStr.charAt(index2++);
			if (c1 == c2) {
				continue;
			}
			if (!ignoreCase) {
				return false;
			}
			final char u1 = Character.toUpperCase(c1);
			final char u2 = Character.toUpperCase(c2);
			if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断源字符序列是否以待查找字符序列起始,大小写敏感
	 * 
	 * @param cs 源字符序列
	 * @param prefix 待查找字符序列
	 * @return true当源字符序列以待查找字符序列起始
	 */
	public static boolean startsWith(final CharSequence cs, final CharSequence prefix) {
		return startsWith(cs, prefix, false);
	}

	/**
	 * 判断源字符序列是否以待查找字符序列起始
	 * 
	 * @param cs 源字符序列
	 * @param prefix 待查找字符序列
	 * @param ignoreCase true大小写不敏感;false大小写敏感
	 * @return true当源字符序列以待查找字符序列结束
	 */
	public static boolean startsWith(final CharSequence cs, final CharSequence prefix, final boolean ignoreCase) {
		if (cs == null || prefix == null) {
			return cs == prefix;
		}
		if (prefix.length() > cs.length()) {
			return false;
		}
		return regionMatches(cs, ignoreCase, 0, prefix, 0, prefix.length());
	}

	/**
	 * 判断源字符序列是否以待查找字符序列起始,大小写敏感
	 * 
	 * @param cs 源字符序列
	 * @param searchStrs 待查找字符序列数组
	 * @return true当源字符序列以待查找字符序列起始
	 */
	public static boolean startsWithAny(final CharSequence cs, final CharSequence... searchStrs) {
		if (isEmpty(cs) || ArrayTool.isEmpty(searchStrs)) {
			return false;
		}
		for (final CharSequence searchStr : searchStrs) {
			if (startsWith(cs, searchStr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断源字符序列是否以待查找字符序列起始,大小写不敏感
	 * 
	 * @param cs 源字符序列
	 * @param prefix 待查找字符序列
	 * @return true当源字符序列以待查找字符序列起始
	 */
	public static boolean startsWithIgnoreCase(final CharSequence cs, final CharSequence prefix) {
		return startsWith(cs, prefix, true);
	}
}