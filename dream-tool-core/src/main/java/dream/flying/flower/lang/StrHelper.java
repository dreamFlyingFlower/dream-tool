package dream.flying.flower.lang;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dream.flying.flower.ConstArray;
import dream.flying.flower.ConstString;
import dream.flying.flower.ConstSymbol;
import dream.flying.flower.annotation.Example;
import dream.flying.flower.enums.RegexEnum;
import dream.flying.flower.helper.ArrayHelper;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.helper.ConvertHepler;

/**
 * String工具类,参照{@link org.apache.commons.lang3.StringUtils},{@link org.springframework.util.StringUtils}
 * 
 * 正则表达式的分组: @ (),每一个对括号表示一个分组,分组的顺序是从左括号出现的顺序, @
 * $后面加一个数字表示对应某一个分组,替代分组中不可直接表达的字符串
 * 
 * 快快乐乐 去掉叠词为快乐,pattern((.)\\1+,$1):
 * 第一个括号表示第一个分组中的任意字符;\\1表示第一个分组,\\2表示第2个分组,需要紧跟在分组之后
 * +表示可出现多个相同的任意字符,$1表示将分组中的.所代表的任意字符串替换到$1,$2则表示替换第二个分组
 * 
 * @author 飞花梦影
 * @date 2021-02-19 09:18:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class StrHelper extends CharSequenceHelper {

	private static final Pattern PATTERN_LINE = Pattern.compile("_(\\w)");

	private static final Pattern PATTERN_HUMP = Pattern.compile("[A-Z]");

	/**
	 * 将源字符串超过总长度的部分转换为...;若总长度刚好等于元字符长度,返回源字符串
	 *
	 * @param str 源字符串
	 * @param maxWidth 最大长度,不能小于4,且转换后的...也要算到该值中
	 * @return 带...的字符串
	 */
	public static String abbreviate(final String str, final int maxWidth) {
		return abbreviate(str, "...", maxWidth);
	}

	/**
	 * 将源字符串超过总长度的部分转换为指定省略符.若总长度刚好等于源字符串长度,返回源字符串
	 *
	 * @param str 源字符串
	 * @param abbrevMarker 指定省略符
	 * @param maxWidth 最大长度,不能小于省略符长度+1,且转换后的省略符长度也要算到该值中
	 * @return 带指定省略符的字符串
	 */
	public static String abbreviate(final String str, final String abbrevMarker, final int maxWidth) {
		if (isNotEmpty(str) && ConstString.STR_EMPTY.equals(abbrevMarker) && maxWidth > 0) {
			return str.substring(0, maxWidth);
		} else if (isAnyEmpty(str, abbrevMarker)) {
			return str;
		}
		final int minAbbrevWidth = abbrevMarker.length() + 1;
		if (maxWidth < minAbbrevWidth) {
			throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", minAbbrevWidth));
		}
		if (str.length() <= maxWidth) {
			return str;
		}
		return str.substring(0, maxWidth - abbrevMarker.length()) + abbrevMarker;
	}

	/**
	 * 判断源字符串是否有指定后缀或其他后缀.若有,返回源字符串;没有则添加指定后缀
	 *
	 * @param str 源字符串
	 * @param suffix 指定后缀
	 * @param ignoreCase true大小写不敏感;false大小写敏感
	 * @param suffixes 其他后缀
	 * @return 新字符串
	 */
	private static String appendIfMissing(final String str, final CharSequence suffix, final boolean ignoreCase,
			final CharSequence... suffixes) {
		if (str == null || isEmpty(suffix) || endsWith(str, suffix, ignoreCase)) {
			return str;
		}
		if (ArrayHelper.isNotEmpty(suffixes)) {
			for (final CharSequence s : suffixes) {
				if (endsWith(str, s, ignoreCase)) {
					return str;
				}
			}
		}
		return str + suffix.toString();
	}

	/**
	 * 判断源字符串是否有指定后缀或其他后缀.若有,返回源字符串;没有则添加指定后缀,大小写敏感
	 *
	 * @param str 源字符串
	 * @param suffix 指定后缀
	 * @param suffixes 其他后缀
	 * @return 新字符串
	 */
	public static String appendIfMissing(final String str, final CharSequence suffix, final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}

	/**
	 * 判断源字符串是否有指定后缀或其他后缀.若有,返回源字符串;没有则添加指定后缀,大小写不敏感
	 *
	 * @param str 源字符串
	 * @param suffix 指定后缀
	 * @param suffixes 其他后缀
	 * @return 新字符串
	 */
	public static String appendIfMissingIgnoreCase(final String str, final CharSequence suffix,
			final CharSequence... suffixes) {
		return appendIfMissing(str, suffix, true, suffixes);
	}

	/**
	 * 合法性检查
	 * 
	 * @param des 目标字符串
	 * @param type 检查类型,手机号,qq号等
	 * @return true合法
	 */
	public static boolean checkValidity(String des, RegexEnum type) {
		if (type == null) {
			return false;
		}
		return checkValidity(des, type.toString());
	}

	/**
	 * 合法性检查
	 * 
	 * @param des 目标字符串
	 * @param pattern 检查类型
	 * @return true合法
	 */
	public static final boolean checkValidity(String des, String pattern) {
		if (isBlank(des) || isBlank(pattern)) {
			return false;
		}
		return des.matches(pattern);
	}

	/**
	 * 删除源字符串末尾的换行符:\r,\n,\r\n;只去除一次,若末尾有多个换行符,只会去除最后一个
	 * 
	 * <pre>
	 * StrTool.chomp("abc\r\n\r\n") = "abc\r\n"
	 * StrTool.chomp("abc\n\r")     = "abc\n"
	 * StrTool.chomp("abc\n\rabc")  = "abc\n\rabc"
	 * </pre>
	 *
	 * @param str 源字符串
	 * @return 末尾无换行符的字符串
	 */
	public static String chomp(final String str) {
		if (isEmpty(str)) {
			return str;
		}
		if (str.length() == 1) {
			final char ch = str.charAt(0);
			if (ch == ConstSymbol.CHAR_CR || ch == ConstSymbol.CHAR_UNIX_LF) {
				return ConstString.STR_EMPTY;
			}
			return str;
		}
		int lastIdx = str.length() - 1;
		final char last = str.charAt(lastIdx);
		if (last == ConstSymbol.CHAR_UNIX_LF) {
			if (str.charAt(lastIdx - 1) == ConstSymbol.CHAR_CR) {
				lastIdx--;
			}
		} else if (last != ConstSymbol.CHAR_CR) {
			lastIdx++;
		}
		return str.substring(0, lastIdx);
	}

	/**
	 * 删除原字符串末尾的最后一个字符,若是换行符,则删除整个换行符,\r\n也会删除
	 *
	 * @param str 源字符串
	 * @return 删除了最后一个字符的新字符串
	 */
	public static String chop(final String str) {
		if (str == null) {
			return null;
		}
		final int strLen = str.length();
		if (strLen < 2) {
			return ConstString.STR_EMPTY;
		}
		final int lastIdx = strLen - 1;
		final String ret = str.substring(0, lastIdx);
		final char last = str.charAt(lastIdx);
		if (last == ConstSymbol.CHAR_UNIX_LF && ret.charAt(lastIdx - 1) == ConstSymbol.CHAR_CR) {
			return ret.substring(0, lastIdx - 1);
		}
		return ret;
	}

	/**
	 * 判断源字符串中是否包含字符串数组中任意一个元素
	 *
	 * @param str 源字符串
	 * @param searchStrs 字符串数组
	 * @return true当源字符串中包含任意一个字符串数组中的元素
	 */
	public static boolean containsAny(final String str, final String... searchStrs) {
		if (isEmpty(str) || ArrayHelper.isEmpty(searchStrs)) {
			return false;
		}
		for (final String searchStr : searchStrs) {
			if (str.contains(searchStr)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算指定字符串中的小写字母数量
	 * 
	 * @param str 源字符串
	 * @return 字母数量
	 */
	public static int countLower(String str) {
		int count = 0;
		char[] charArray = str.toCharArray();
		for (char ch : charArray) {
			if (containsLower(String.valueOf(ch))) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 计算源字符串中出现待查找字符串的次数
	 *
	 * @param str 源字符串
	 * @param sub 待查找字符串
	 * @return 源字符串中出现待查找字符串的次数
	 */
	public static int countMatches(final String str, final String sub) {
		if (isEmpty(str) || isEmpty(sub)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) != ConstArray.INDEX_NOT_FOUND) {
			count++;
			idx += sub.length();
		}
		return count;
	}

	/**
	 * 计算指定字符串中的数字数量
	 * 
	 * @param str 源字符串
	 * @return 字母数量
	 */
	public static int countNumber(String str) {
		int count = 0;
		char[] charArray = str.toCharArray();
		for (char ch : charArray) {
			if (NumberHelper.isSimpleNumber(String.valueOf(ch))) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 计算指定字符串中的特殊字符串数量
	 * 
	 * @param str 源字符串
	 * @return 特殊字符串数量
	 */
	public static int countSpecial(String str) {
		int count = 0;
		for (int i = 0; i < str.toCharArray().length; i++) {
			if (containsSpecial(String.valueOf(str.charAt(i)))) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 返回字符串中包含的大写字母数量
	 * 
	 * @param str 源字符串
	 * @return 大写字母数量
	 */
	public static int countUpper(String str) {
		int count = 0;
		char[] charArray = str.toCharArray();
		for (char ch : charArray) {
			if (containsUpper(String.valueOf(ch))) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 比较str1和str2相同索引的字符,返回str2中第一个不同字符索引到末尾字符的全部字符串
	 *
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return str2中第一个不同字符索引到末尾字符的全部字符串
	 */
	public static String difference(final String str1, final String str2) {
		if (str1 == null) {
			return str2;
		}
		if (str2 == null) {
			return str1;
		}
		final int at = indexOfDifference(str1, str2);
		if (at == ConstArray.INDEX_NOT_FOUND) {
			return ConstString.STR_EMPTY;
		}
		return str2.substring(at);
	}

	/**
	 * 判断指定字符串是否以指定后缀结尾,忽略大小写
	 * 
	 * @param str 待检查字符串
	 * @param suffix 指定后缀
	 * @return true->是;false->否
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		return (str != null && suffix != null && str.length() >= suffix.length()
				&& str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
	}

	/**
	 * 将字符串的首字母变成大写或小写
	 * 
	 * @param src 需要进行转换的字符串
	 * @param capitalize true大写,false小写
	 * @return 转换后的字符串
	 */
	private static String firstChange(String src, boolean capitalize) {
		if (isBlank(src)) {
			return src;
		}
		char baseChar = src.charAt(0);
		char updatedChar = capitalize ? Character.toUpperCase(baseChar) : Character.toLowerCase(baseChar);
		if (baseChar == updatedChar) {
			return src;
		}
		char[] chars = src.toCharArray();
		chars[0] = updatedChar;
		return new String(chars, 0, chars.length);
	}

	/**
	 * 将字符串的首字母变成小写
	 * 
	 * @param str 需要进行转换的字符串
	 * @return 转换后的字符串
	 */
	public static String firstLower(String str) {
		return firstChange(str, false);
	}

	/**
	 * 将字符串的首字母变成大写
	 * 
	 * @param str 需要进行转换的字符串
	 * @return 转换后的字符串
	 */
	public static String firstUpper(String str) {
		return firstChange(str, true);
	}

	/**
	 * 格式化字符串,该方法非JDK使用%s,%d进行格式化的方法,使用的是{}
	 * 
	 * 此方法只是简单将占位符{}按照顺序替换为参数,若要输出{},使用\\转义;输出{}之前的\使用双转义符\\\\
	 * 
	 * <pre>
	 * 	通常使用:format("this is {} for {}", "a", "b") -> this is a for b<br>
	 * 	转义{}:format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
	 * 	转义\: format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
	 * </pre>
	 * 
	 * @param template 字符串模板
	 * @param args 参数列表
	 * @return 结果 FIXME
	 */
	public static String format(final String template, final Object... args) {
		if (isBlank(template) || null == args || args.length == 0) {
			return template;
		}
		final int strPatternLength = template.length();
		// 初始化定义好的长度以获得更好的性能
		StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
		int handledPosition = 0;
		int delimIndex;// 占位符所在位置
		for (int argIndex = 0; argIndex < args.length; argIndex++) {
			delimIndex = template.indexOf(ConstString.EMPTY_JSON, handledPosition);
			if (delimIndex == -1) {
				if (handledPosition == 0) {
					return template;
				} else {
					// 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
					sbuf.append(template, handledPosition, strPatternLength);
					return sbuf.toString();
				}
			} else {
				if (delimIndex > 0 && template.charAt(delimIndex - 1) == ConstSymbol.CHAR_BACKSLASH) {
					if (delimIndex > 1 && template.charAt(delimIndex - 2) == ConstSymbol.CHAR_BACKSLASH) {
						// 转义符之前还有一个转义符，占位符依旧有效
						sbuf.append(template, handledPosition, delimIndex - 1);
						sbuf.append(ConvertHepler.toStr(args[argIndex]));
						handledPosition = delimIndex + 2;
					} else {
						// 占位符被转义
						argIndex--;
						sbuf.append(template, handledPosition, delimIndex - 1);
						sbuf.append(ConstSymbol.BRACE_START);
						handledPosition = delimIndex + 1;
					}
				} else {
					// 正常占位符
					sbuf.append(template, handledPosition, delimIndex);
					sbuf.append(ConvertHepler.toStr(args[argIndex]));
					handledPosition = delimIndex + 2;
				}
			}
		}
		// 加入最后一个占位符后所有的字符
		sbuf.append(template, handledPosition, template.length());
		return sbuf.toString();
	}

	/**
	 * 返回数组中所有字符串相同的前缀字符串,必须索引和字符都相同.若没有,返回""
	 *
	 * @param strs 字符串数组
	 * @return 相同前缀字符串
	 */
	public static String getCommonPrefix(final String... strs) {
		if (ArrayHelper.isEmpty(strs)) {
			return ConstString.STR_EMPTY;
		}
		final int smallestIndexOfDiff = indexOfDifference(strs);
		if (smallestIndexOfDiff == ConstArray.INDEX_NOT_FOUND) {
			if (strs[0] == null) {
				return ConstString.STR_EMPTY;
			}
			return strs[0];
		} else if (smallestIndexOfDiff == 0) {
			return ConstString.STR_EMPTY;
		} else {
			return strs[0].substring(0, smallestIndexOfDiff);
		}
	}

	/**
	 * 判断源字符串中是否只包含unicode表示的数字或纯数字,不包括小数点,非数字将删除;
	 * 若是unicode表示的数字,直接返回unicode形式的字符串;数字直接返回
	 *
	 * @param str 源字符串
	 * @return 只包含unicode表示的数字或纯数字字符串
	 */
	public static String getDigits(final String str) {
		if (isEmpty(str)) {
			return str;
		}
		final int sz = str.length();
		final StringBuilder strDigits = new StringBuilder(sz);
		for (int i = 0; i < sz; i++) {
			final char tempChar = str.charAt(i);
			if (Character.isDigit(tempChar)) {
				strDigits.append(tempChar);
			}
		}
		return strDigits.toString();
	}

	/**
	 * 将驼峰转成蛇底式
	 * 
	 * @param str 需要进行转换的字符串
	 * @return 转换后的字符串
	 */
	public static String hump2Underline(String str) {
		return hump2Underline(str, true);
	}

	/**
	 * 将驼峰转成蛇底式
	 * 
	 * @param str 需要进行转换的字符串
	 * @param lowerFirst 第一个字符串是否小写,true小写,false不管
	 * @return 转换后的字符串
	 */
	public static String hump2Underline(String str, boolean lowerFirst) {
		str = lowerFirst ? firstLower(str) : str;
		Matcher matcher = PATTERN_HUMP.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 从String数组起始位置开始搜索待查找字符串第一次出现的索引
	 * 
	 * @param array 数组
	 * @param searchStr 待查找字符串
	 * @return 索引,未找到返回-1
	 */
	public static int indexOf(String[] array, final String searchStr) {
		if (ArrayHelper.isNotEmpty(array) && isNotBlank(searchStr)) {
			for (int i = 0; i < array.length; i++) {
				if (searchStr.equals(array[i])) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 从源字符串中查找不属于待查找字符串中字符的第一个字符索引,若全部都有,返回-1
	 *
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @return 第一个不属于待查找字符串字符的索引,若全部都有,返回-1
	 */
	public static int indexOfAnyBut(final String str, final String searchStr) {
		if (isEmpty(str) || isEmpty(searchStr)) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		final int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			final char ch = str.charAt(i);
			final boolean chFound = searchStr.indexOf(ch, 0) >= 0;
			if (i + 1 < strLen && Character.isHighSurrogate(ch)) {
				final char ch2 = str.charAt(i + 1);
				if (chFound && searchStr.indexOf(ch2, 0) < 0) {
					return i;
				}
			} else {
				if (!chFound) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 将多个参数连接起来
	 * 
	 * @param msgs 参数
	 * @return 最终的字符串
	 */
	public static String joinBuffer(Object... msgs) {
		StringBuffer builder = new StringBuffer();
		for (Object msg : msgs) {
			builder.append(msg);
		}
		return builder.toString();
	}

	/**
	 * 用同一个分隔符将参数连接起来
	 * 
	 * @param delimiter 分隔符
	 * @param msgs 参数
	 * @return 最终的字符串
	 */
	public static String joinBuffer(String delimiter, Object... msgs) {
		StringBuffer builder = new StringBuffer();
		if (msgs.length == 0) {
			return "";
		}
		if (msgs.length == 1) {
			return builder.append(msgs[0]).toString();
		}
		builder.append(msgs[0]);
		for (int i = 1; i < msgs.length;) {
			builder.append(delimiter).append(msgs[i]);
		}
		return builder.toString();
	}

	/**
	 * 将多个参数连接起来
	 * 
	 * @param msgs 参数
	 * @return 最终的字符串
	 */
	public static String joinBuilder(Object... msgs) {
		StringBuilder builder = new StringBuilder();
		for (Object msg : msgs) {
			builder.append(msg);
		}
		return builder.toString();
	}

	/**
	 * 用同一个分隔符将参数连接起来
	 * 
	 * @param delimiter 分隔符
	 * @param msgs 参数
	 * @return 最终的字符串
	 */
	public static String joinBuilder(String delimiter, Object... msgs) {
		StringBuilder builder = new StringBuilder();
		if (msgs.length == 0) {
			return "";
		}
		if (msgs.length == 1) {
			return builder.append(msgs[0]).toString();
		}
		builder.append(msgs[0]);
		for (int i = 1; i < msgs.length;) {
			builder.append(delimiter).append(msgs[i]);
		}
		return builder.toString();
	}

	/**
	 * 从String数组末尾位置开始搜索待查找字符串第一次出现的索引
	 * 
	 * @param array 数组
	 * @param searchStr 待查找字符串
	 * @return 索引,未找到返回-1
	 */
	public static int lastIndexOf(String[] array, final String searchStr) {
		if (ArrayHelper.isNotEmpty(array) && isNotBlank(searchStr)) {
			for (int i = array.length - 1; i >= 0; i--) {
				if (searchStr.equals(array[i])) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 从源字符序列末尾开始查找指定字符序列在源字符序列第N次出现的索引,找不到返回-1
	 *
	 * @param str 字符序列
	 * @param searchStr 需要查找的字符序列
	 * @param ordinal 第N次出现
	 * @return 第N次出现的索引
	 */
	public static int lastOrdinalIndexOf(final String str, final String searchStr, final int ordinal) {
		return ordinalIndexOf(str, searchStr, ordinal, true);
	}

	/**
	 * 从源字符串左边开始截取指定长度的字符串,若长度超过源字符串长度,截取全部
	 * 
	 * @param str 源字符串
	 * @param len 截取指定长度
	 * @return 最大长度字符串
	 */
	public static String left(final String str, final int len) {
		if (isBlank(str) || len < 0) {
			return null;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * 在源字符串左边填充一定数量的空格使结果字符串长度达到指定长度,最终长度大于源字符串长度才填充
	 *
	 * @param str 被填充空格的源字符串
	 * @param size 被填充空格后结果字符串的总长度
	 * @return 被填充空格后的新字符串
	 */
	public static String leftPad(final String str, final int size) {
		return leftPad(str, size, ' ');
	}

	/**
	 * 在源字符串左边填充一定数量的指定字符使结果字符串长度达到指定长度,指定长度大于源字符串长度才填充
	 *
	 * @param str 被填充指定字符的源字符串
	 * @param size 被填充指定字符后结果字符串的总长度
	 * @param padChar 填充到源字符串的字符
	 * @return 被填充指定字符后的新字符串
	 */
	public static String leftPad(final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			return str;
		}
		if (pads > ConstString.PAD_LIMIT) {
			return leftPad(str, size, String.valueOf(padChar));
		}
		return repeat(padChar, pads).concat(str);
	}

	/**
	 * 在源字符串左边填充一定数量的指定字符串使结果字符串长度达到指定长度,指定长度大于源字符串长度才填充
	 *
	 * <pre>
	 * leftPad("", 3, "z")      = "zzz"
	 * leftPad("bat", 1, "yz")  = "bat"
	 * leftPad("bat", 5, "yz")  = "yzbat"
	 * leftPad("bat", 8, "yz")  = "yzyzybat"
	 * leftPad("bat", -1, "yz") = "bat"
	 * leftPad("bat", 5, null)  = "  bat"
	 * </pre>
	 *
	 * @param str 被填充指定字符串的源字符串
	 * @param size 被填充指定字符串后结果字符串的总长度
	 * @param padStr 填充到源字符串的字符串
	 * @return 被填充指定字符串后的新字符串
	 */
	public static String leftPad(final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = ConstString.STR_EMPTY;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		if (padLen == 1 && pads <= ConstString.PAD_LIMIT) {
			return leftPad(str, size, padStr.charAt(0));
		}
		if (pads == padLen) {
			return padStr.concat(str);
		} else if (pads < padLen) {
			return padStr.substring(0, pads).concat(str);
		} else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return new String(padding).concat(str);
		}
	}

	/**
	 * 将2个字符串数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的字符串数组
	 */
	public static String[] merge(String[]... arrays) {
		int length = 0;
		for (String[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		String[] result = new String[length];
		int pos = 0;
		for (String[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 从源字符序列起始开始查找指定字符序列在源字符序列第N次出现的索引,找不到返回-1
	 *
	 * @param str 字符序列
	 * @param searchStr 需要查找的字符序列
	 * @param ordinal 第N次出现
	 * @return 第N次出现的索引
	 */
	public static int ordinalIndexOf(final String str, final String searchStr, final int ordinal) {
		return ordinalIndexOf(str, searchStr, ordinal, false);
	}

	/**
	 * 查找指定字符序列在源字符序列第N次出现的索引,找不到返回-1
	 *
	 * @param str 字符序列
	 * @param searchStr 需要查找的字符序列
	 * @param ordinal 第N次出现
	 * @param lastIndex 是否从末尾开始查找,true是,false否
	 * @return 第N次出现的索引
	 */
	private static int ordinalIndexOf(final String str, final String searchStr, final int ordinal,
			final boolean lastIndex) {
		if (ObjectHelper.isNull(str, searchStr) || ordinal <= 0) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return lastIndex ? str.length() : 0;
		}
		int found = 0;
		int index = lastIndex ? str.length() : ConstArray.INDEX_NOT_FOUND;
		do {
			if (lastIndex) {
				index = str.lastIndexOf(searchStr, index - 1);
			} else {
				index = str.indexOf(searchStr, index + 1);
			}
			if (index < 0) {
				return index;
			}
			found++;
		} while (found < ordinal);
		return index;
	}

	/**
	 * 使用新字符串覆盖源字符串中指定索引之间的字符
	 *
	 * @param str 源字符串
	 * @param overlay 新字符串,若为null或"",相当于删除索引之间的字符串
	 * @param start 开始覆盖的索引
	 * @param end 结束覆盖的索引.若start大于end,则它们的值互换;若start和end都小于等于0,返回overlay
	 * @return 覆盖后的新字符串
	 */
	public static String overlay(final String str, String overlay, int start, int end) {
		if (str == null) {
			return null;
		}
		if (overlay == null) {
			overlay = ConstString.STR_EMPTY;
		}
		final int len = str.length();
		start = start < 0 ? 0 : start;
		start = start > len ? len : start;
		end = end < 0 ? 0 : end;
		end = end > len ? len : end;
		if (start > end) {
			final int temp = start;
			start = end;
			end = temp;
		}
		return str.substring(0, start) + overlay + str.substring(end);
	}

	/**
	 * 判断源字符串是否有指定前缀或其他前缀.若有,返回源字符串;没有则添加指定前缀
	 *
	 * @param str 源字符串
	 * @param prefix 指定前缀
	 * @param ignoreCase true大小写不敏感;false大小写敏感
	 * @param prefixes 其他前缀
	 * @return 新字符串
	 */
	private static String prependIfMissing(final String str, final CharSequence prefix, final boolean ignoreCase,
			final CharSequence... prefixes) {
		if (str == null || isEmpty(prefix) || startsWith(str, prefix, ignoreCase)) {
			return str;
		}
		if (ArrayHelper.isNotEmpty(prefixes)) {
			for (final CharSequence p : prefixes) {
				if (startsWith(str, p, ignoreCase)) {
					return str;
				}
			}
		}
		return prefix.toString() + str;
	}

	/**
	 * 判断源字符串是否有指定前缀或其他前缀.若有,返回源字符串;没有则添加指定前缀,大小写敏感
	 *
	 * @param str 源字符串
	 * @param prefix 指定前缀
	 * @param prefixes 其他前缀
	 * @return 新字符串
	 */
	public static String prependIfMissing(final String str, final CharSequence prefix, final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, false, prefixes);
	}

	/**
	 * 判断源字符串是否有指定前缀或其他前缀.若有,返回源字符串;没有则添加指定前缀,大小写不敏感
	 *
	 * @param str 源字符串
	 * @param prefix 指定前缀
	 * @param prefixes 其他前缀
	 * @return 新字符串
	 */
	public static String prependIfMissingIgnoreCase(final String str, final CharSequence prefix,
			final CharSequence... prefixes) {
		return prependIfMissing(str, prefix, true, prefixes);
	}

	/**
	 * 给源字符串添加单引号
	 * 
	 * @param str 源字符串
	 * @return 添加单引号都的字符串
	 */
	public static String quote(String str) {
		return (isNotBlank(str) ? "'" + str + "'" : null);
	}

	/**
	 * 删除源字符串末尾指定字符串,返回删除末尾字符串后的新字符串,大小写敏感
	 *
	 * @param str 源字符串
	 * @param remove 待删除的末尾字符串
	 * @return 新字符串
	 */
	public static String removeEnd(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.endsWith(remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * 删除源字符串末尾指定字符串,返回删除末尾字符串后的新字符串,大小写不敏感
	 *
	 * @param 源字符串
	 * @param 待删除的末尾字符串
	 * @return 新字符串
	 */
	public static String removeEndIgnoreCase(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (endsWithIgnoreCase(str, remove)) {
			return str.substring(0, str.length() - remove.length());
		}
		return str;
	}

	/**
	 * 删除源字符串起始指定字符串,返回删除起始字符串后的新字符串,大小写敏感
	 *
	 * @param str 源字符串
	 * @param remove 待删除的起始字符串
	 * @return 新字符串
	 */
	public static String removeStart(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * 删除源字符串起始指定字符串,返回删除起始字符串后的新字符串,大小写不敏感
	 *
	 * @param str 源字符串
	 * @param remove 待删除的起始字符串
	 * @return 新字符串
	 */
	public static String removeStartIgnoreCase(final String str, final String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (startsWithIgnoreCase(str, remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * 复制源字符指定次数,次数小于或等于0返回""
	 *
	 * @param ch 源字符
	 * @param repeat 复制次数,小于或等于0返回""
	 * @return 复制后的字符串
	 */
	public static String repeat(final char ch, final int repeat) {
		if (repeat <= 0) {
			return ConstString.STR_EMPTY;
		}
		final char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * 将源字符串复制指定次数,次数小于等于0返回""
	 *
	 * @param str 源字符串
	 * @param repeat 复制次数,小于等于0返回""
	 * @return 新字符串
	 */
	public static String repeat(final String str, final int repeat) {
		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return ConstString.STR_EMPTY;
		}
		final int inputLength = str.length();
		if (repeat == 1 || inputLength == 0) {
			return str;
		}
		if (inputLength == 1 && repeat <= ConstString.PAD_LIMIT) {
			return repeat(str.charAt(0), repeat);
		}
		final int outputLength = inputLength * repeat;
		switch (inputLength) {
		case 1:
			return repeat(str.charAt(0), repeat);
		case 2:
			final char ch0 = str.charAt(0);
			final char ch1 = str.charAt(1);
			final char[] output2 = new char[outputLength];
			for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
				output2[i] = ch0;
				output2[i + 1] = ch1;
			}
			return new String(output2);
		default:
			final StringBuilder buf = new StringBuilder(outputLength);
			for (int i = 0; i < repeat; i++) {
				buf.append(str);
			}
			return buf.toString();
		}
	}

	/**
	 * 将源字符串复制N次,并以指定间隔符拼接起来,N小于等于0返回""
	 *
	 * @param str 源字符串
	 * @param separator 间隔符
	 * @param repeat 复制次数
	 * @return 新字符串
	 */
	public static String repeat(final String str, final String separator, final int repeat) {
		if (str == null || separator == null) {
			return repeat(str, repeat);
		}
		final String result = repeat(str + separator, repeat);
		return removeEnd(result, separator);
	}

	/**
	 * 将源字符串中待查找字符串全部替换,大小写敏感
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param replacement 替换字符串
	 * @return 替换后的新字符串
	 */
	public static String replace(final String str, final String searchStr, final String replacement) {
		return replace(str, searchStr, replacement, -1);
	}

	/**
	 * 将源字符串中待查找字符串替换,大小写敏感
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param replacement 替换字符串
	 * @param max 替换的最大次数,-1表示全部替换
	 * @return 替换后的新字符串
	 */
	public static String replace(final String str, final String searchStr, final String replacement, final int max) {
		return replace(str, searchStr, replacement, max, false);
	}

	/**
	 * 将源字符串中待查找字符串替换
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param replacement 替换字符串
	 * @param max 替换的最大次数,-1表示全部替换
	 * @param ignoreCase true忽略大小写,false不忽略大小写
	 * @return 替换后的新字符串
	 */
	private static String replace(final String str, String searchStr, final String replacement, int max,
			final boolean ignoreCase) {
		if (isEmpty(str) || isEmpty(searchStr) || replacement == null || max == 0) {
			return str;
		}
		if (ignoreCase) {
			searchStr = searchStr.toLowerCase();
		}
		int start = 0;
		int end = ignoreCase ? indexOfIgnoreCase(str, searchStr, start) : str.indexOf(searchStr, start);
		if (end == ConstArray.INDEX_NOT_FOUND) {
			return str;
		}
		final int replLength = searchStr.length();
		int increase = Math.max(replacement.length() - replLength, 0);
		increase *= max < 0 ? 16 : Math.min(max, 64);
		final StringBuilder buf = new StringBuilder(str.length() + increase);
		while (end != ConstArray.INDEX_NOT_FOUND) {
			buf.append(str, start, end).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = ignoreCase ? indexOfIgnoreCase(str, searchStr, start) : str.indexOf(searchStr, start);
		}
		buf.append(str, start, str.length());
		return buf.toString();
	}

	/**
	 * 将源字符串中待查找字符串替换,大小写不敏感
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param replacement 替换字符串
	 * @return 替换后的新字符串
	 */
	public static String replaceIgnoreCase(final String str, final String searchStr, final String replacement) {
		return replaceIgnoreCase(str, searchStr, replacement, -1);
	}

	/**
	 * 将源字符串中待查找字符串替换,大小写不敏感
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param replacement 替换字符串
	 * @param max 替换的最大次数,-1表示全部替换
	 * @return 替换后的新字符串
	 */
	public static String replaceIgnoreCase(final String str, final String searchStr, final String replacement,
			final int max) {
		return replace(str, searchStr, replacement, max, true);
	}

	/**
	 * 将字符串中重复的字符去除,只留下单个
	 * 
	 * <pre>
	 * fdfdssssfdsffff -> fdfdsfdsf
	 * </pre>
	 * 
	 * @param str 源字符串
	 * @return 替换后的新字符串
	 */
	public static String replaceRepeat(final String str) {
		return str.replaceAll("(.)\\1+", "$1");
	}

	/**
	 * 在源字符串右边填充一定数量的空格字符使结果字符串长度达到指定长度,指定长度大于源字符串长度才填充
	 *
	 * @param str 被填充指定字符的源字符串
	 * @param size 被填充指定字符后结果字符串的总长度
	 * @return 被填充指定字符后的新字符串
	 */
	public static String rightPad(final String str, final int size) {
		return rightPad(str, size, ' ');
	}

	/**
	 * 在源字符串右边填充一定数量的指定字符使结果字符串长度达到指定长度,指定长度大于源字符串长度才填充
	 *
	 * @param str 被填充指定字符的源字符串
	 * @param size 被填充指定字符后结果字符串的总长度
	 * @param padChar 填充到源字符串的字符
	 * @return 被填充指定字符后的新字符串
	 */
	public static String rightPad(final String str, final int size, final char padChar) {
		if (str == null) {
			return null;
		}
		final int pads = size - str.length();
		if (pads <= 0) {
			return str;
		}
		if (pads > ConstString.PAD_LIMIT) {
			return rightPad(str, size, String.valueOf(padChar));
		}
		return str.concat(repeat(padChar, pads));
	}

	/**
	 * 在源字符串左边填充一定数量的指定字符使结果字符串长度达到指定长度,指定长度大于源字符串长度才填充
	 *
	 * <pre>
	 * rightPad("", 3, "z")      = "zzz"
	 * rightPad("bat", 3, "yz")  = "bat"
	 * rightPad("bat", 5, "yz")  = "batyz"
	 * rightPad("bat", 8, "yz")  = "batyzyzy"
	 * rightPad("bat", 1, "yz")  = "bat"
	 * rightPad("bat", -1, "yz") = "bat"
	 * rightPad("bat", 5, null)  = "bat  "
	 * rightPad("bat", 5, "")    = "bat  "
	 * </pre>
	 *
	 * @param str 被填充指定字符的源字符串
	 * @param size 被填充指定字符后结果字符串的总长度
	 * @param padStr 填充到源字符串的字符串
	 * @return 被填充指定字符后的新字符串
	 */
	public static String rightPad(final String str, final int size, String padStr) {
		if (str == null) {
			return null;
		}
		if (isEmpty(padStr)) {
			padStr = ConstString.STR_SPACE;
		}
		final int padLen = padStr.length();
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str;
		}
		if (padLen == 1 && pads <= ConstString.PAD_LIMIT) {
			return rightPad(str, size, padStr.charAt(0));
		}

		if (pads == padLen) {
			return str.concat(padStr);
		} else if (pads < padLen) {
			return str.concat(padStr.substring(0, pads));
		} else {
			final char[] padding = new char[pads];
			final char[] padChars = padStr.toCharArray();
			for (int i = 0; i < pads; i++) {
				padding[i] = padChars[i % padLen];
			}
			return str.concat(new String(padding));
		}
	}

	/**
	 * 将指定字符串根据指定分割符进行拆分,null或空字符串都将转换为空字符串进行拆分.多个分割符相邻时当作单个分割符
	 *
	 * @param str 待分割字符串,可能为null
	 * @param separatorChar 分割符
	 * @return 分割后的数组,可能为null
	 */
	public static String[] split(String str, char separatorChar) {
		return splitWorker(str, separatorChar, false);
	}

	/**
	 * 将指定字符串根据指定分割符进行拆分,null或空字符串都将转换为空字符串进行拆分.多个分割符相邻时当作单个分割符
	 *
	 * @param str 待分割字符串,可能为null
	 * @param separatorChar 分割符
	 * @return 分割后的数组,可能为null
	 */
	public static String[] split(String str, String separatorChars) {
		return splitWorker(str, separatorChars, -1, false);
	}

	/**
	 * 将指定字符串根据指定分割符进行拆分,null或空字符串都将转换为空字符串进行拆分.多个分割符相邻时当作单个分割符
	 *
	 * @param str 待分割字符串,可能为null
	 * @param separatorChar 分割符
	 * @param preserveAllTokens true->相邻的分割符拆分为空字符串;false->相邻的分割符当作单个分割符
	 * @return 分割后的数组,可能为null
	 */
	private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return ConstArray.EMPTY_STRING;
		}
		List<String> list = new ArrayList<String>();
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		while (i < len) {
			if (str.charAt(i) == separatorChar) {
				if (match || preserveAllTokens) {
					list.add(str.substring(start, i));
					match = false;
					lastMatch = true;
				}
				start = ++i;
				continue;
			}
			lastMatch = false;
			match = true;
			i++;
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * Performs the logic for the <code>split</code> and
	 * <code>splitPreserveAllTokens</code> methods that return a maximum array
	 * length.
	 *
	 * @param str 待分割字符串,可能为null
	 * @param separatorChar 分割符
	 * @param max 分割后数组的最大容量,可为任意整数
	 * @param preserveAllTokens true->相邻的分割符拆分为空字符串;false->相邻的分割符当作单个分割符
	 * @return 分割后的数组,可能为null
	 */
	private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
		if (str == null) {
			return null;
		}
		int len = str.length();
		if (len == 0) {
			return ConstArray.EMPTY_STRING;
		}
		List<String> list = new ArrayList<String>();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(str.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			// Optimise 1 character case
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (str.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(str.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * 判断字符串是否以指定前缀开头,忽略大小写
	 * 
	 * @param str 待检查字符串
	 * @param prefix 前缀
	 * @return true->是;false->否
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		return (str != null && prefix != null && str.length() >= prefix.length()
				&& str.regionMatches(true, 0, prefix, 0, prefix.length()));
	}

	/**
	 * 只要源字符串前后的字符被包含在待删除字符串中即可删除,不需要完全匹配,只要被包含即可删除
	 * 
	 * <pre>
	 * strip("  abc", null)    = "abc"
	 * strip("  abcyx", "xyz") = "  abc"
	 * strip("  xxxxabcyx", "xyz") = "  abc"
	 * </pre>
	 * 
	 * @param str 源字符串
	 * @param stripChars 待删除字符串
	 * @return 删除了被包含在待删除字符串中的新字符串
	 */
	public static String strip(String str, final String stripChars) {
		if (isEmpty(str)) {
			return str;
		}
		str = stripStart(str, stripChars);
		return stripEnd(str, stripChars);
	}

	/**
	 * 只要源字符串后的字符被包含在待删除字符串中即可删除,不需要完全匹配,只要被包含即可删除
	 *
	 * <pre>
	 * stripEnd(" abc ", null)    = " abc"
	 * stripEnd("  abcyx", "xyz") = "  abc"
	 * </pre>
	 *
	 * @param str 源字符串
	 * @param stripChars 待删除字符串
	 * @return 删除了后面被包含在待删除字符串中的新字符串
	 */
	public static String stripEnd(final String str, final String stripChars) {
		int end = length(str);
		if (end == 0) {
			return str;
		}
		if (stripChars == null) {
			while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else if (stripChars.isEmpty()) {
			return str;
		} else {
			while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != ConstArray.INDEX_NOT_FOUND) {
				end--;
			}
		}
		return str.substring(0, end);
	}

	/**
	 * 删除源字符串前后的空白
	 * 
	 * @param strs 源字符串数组
	 * @return 删除了字符串前后空白的新字符串
	 */
	public static String[] strips(final String... strs) {
		return strips(strs, null);
	}

	/**
	 * 只要源字符串前后的字符被包含在待删除字符串中即可删除,不需要完全匹配,只要被包含即可删除
	 * 
	 * @param strs 源字符串数组
	 * @param stripChars 待删除字符串
	 * @return 删除了被包含在待删除字符串中的新字符串
	 */
	public static String[] strips(final String[] strs, final String stripChars) {
		final int strsLen = ArrayHelper.length(strs);
		if (strsLen == 0) {
			return strs;
		}
		final String[] newArr = new String[strsLen];
		for (int i = 0; i < strsLen; i++) {
			newArr[i] = strip(strs[i], stripChars);
		}
		return newArr;
	}

	/**
	 * 只要源字符串前的字符被包含在待删除字符串中即可删除,不需要完全匹配,只要被包含即可删除
	 * 
	 * <pre>
	 * stripStart(" abc ", null)    = "abc "
	 * stripStart("yxabc  ", "xyz") = "abc  "
	 * </pre>
	 *
	 * @param str 源字符串
	 * @param stripChars 待删除字符串
	 * @return 删除了前面被包含在待删除字符串中的新字符串
	 */
	public static String stripStart(final String str, final String stripChars) {
		final int strLen = length(str);
		if (strLen == 0) {
			return str;
		}
		int start = 0;
		if (stripChars == null) {
			while (start != strLen && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
		} else if (stripChars.isEmpty()) {
			return str;
		} else {
			while (start != strLen && stripChars.indexOf(str.charAt(start)) != ConstArray.INDEX_NOT_FOUND) {
				start++;
			}
		}
		return str.substring(start);
	}

	/**
	 * 从起始使用间隔符截取源字符串间隔符索引之后的字符串
	 *
	 * @param str 源字符串
	 * @param separator 间隔符
	 * @return 第一次间隔符索引之后的字符串
	 */
	public static String subAfter(final String str, final String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return ConstString.STR_EMPTY;
		}
		final int pos = str.indexOf(separator);
		if (pos == ConstArray.INDEX_NOT_FOUND) {
			return ConstString.STR_EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 从末尾使用间隔符截取源字符串间隔符索引之后的字符串
	 *
	 * @param str 源字符串
	 * @param separator 间隔符
	 * @return 第一次间隔符索引之后的字符串
	 */
	public static String subAfterLast(final String str, final String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return ConstString.STR_EMPTY;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == ConstArray.INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
			return ConstString.STR_EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 从起始使用间隔符截取源字符串间隔符索引之前的字符串
	 *
	 * @param str 源字符串
	 * @param separator 间隔符
	 * @return 第一次间隔符索引之前的字符串
	 */
	public static String subBefore(final String str, final String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.isEmpty()) {
			return ConstString.STR_EMPTY;
		}
		final int pos = str.indexOf(separator);
		if (pos == ConstArray.INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 从末尾使用间隔符截取源字符串间隔符索引之前的字符串
	 *
	 * @param str 源字符串
	 * @param separator 间隔符
	 * @return 第一次间隔符索引之前的字符串
	 */
	public static String subBeforeLast(final String str, final String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == ConstArray.INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 从源字符串中查找被包裹在两个相同的指定字符串中间的字符串,只查找一次
	 *
	 * @param str 源字符串
	 * @param tag 指定字符串
	 * @return 指定字符串中间的字符串
	 */
	public static String subBetween(final String str, final String tag) {
		return subBetween(str, tag, tag);
	}

	/**
	 * 从源字符串中查找被包裹在两个指定字符串中间的字符串,只查找一次
	 *
	 * @param str 源字符串
	 * @param open 起始字符串
	 * @param close 结束字符串
	 * @return 指定字符串中间的字符串
	 */
	public static String subBetween(final String str, final String open, final String close) {
		if (!ObjectHelper.nonNull(str, open, close)) {
			return null;
		}
		final int start = str.indexOf(open);
		if (start != ConstArray.INDEX_NOT_FOUND) {
			final int end = str.indexOf(close, start + open.length());
			if (end != ConstArray.INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	/**
	 * 从源字符串中查找被包裹在两个指定字符串中间的字符串,查找所有符合的
	 *
	 * @param str 源字符串
	 * @param open 起始字符串
	 * @param close 结束字符串
	 * @return 所有符合条件的字符串组成的数组
	 */
	public static String[] subsBetween(final String str, final String open, final String close) {
		if (str == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == 0) {
			return new String[0];
		}
		final int closeLen = close.length();
		final int openLen = open.length();
		final List<String> list = new ArrayList<>();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			final int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.toArray(new String[0]);
	}

	/**
	 * 从指定字符串中截取部分字符串
	 * 
	 * @param str 待截取的字符串
	 * @param start 截取的开始索引
	 * @param end 截取的结束索引
	 * @return 截取后的字符串
	 */
	public static String substring(final String str, int start, int end) {
		if (str == null) {
			return null;
		}

		// handle negatives
		if (end < 0) {
			end = str.length() + end; // remember end is negative
		}
		if (start < 0) {
			start = str.length() + start; // remember start is negative
		}

		// check length next
		if (end > str.length()) {
			end = str.length();
		}

		// if start is greater than end, return ""
		if (start > end) {
			return ConstString.STR_EMPTY;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}
		return str.substring(start, end);
	}

	/**
	 * 将源字符串中的大小写逆转,大写转小写,小写转大写
	 *
	 * @param str 源字符串
	 * @return 逆转后的字符串
	 */
	public static String swapCase(final String str) {
		if (isEmpty(str)) {
			return str;
		}
		final int strLen = str.length();
		final int[] newCodePoints = new int[strLen];
		int outOffset = 0;
		for (int i = 0; i < strLen;) {
			final int oldCodepoint = str.codePointAt(i);
			final int newCodePoint;
			if (Character.isUpperCase(oldCodepoint) || Character.isTitleCase(oldCodepoint)) {
				newCodePoint = Character.toLowerCase(oldCodepoint);
			} else if (Character.isLowerCase(oldCodepoint)) {
				newCodePoint = Character.toUpperCase(oldCodepoint);
			} else {
				newCodePoint = oldCodepoint;
			}
			newCodePoints[outOffset++] = newCodePoint;
			i += Character.charCount(newCodePoint);
		}
		return new String(newCodePoints, 0, outOffset);
	}

	/**
	 * 将字节数组转换默认编码集(utf8)的字符串
	 *
	 * @param bytes 待转换的字节数组
	 * @return 字符串
	 */
	public static String toString(final byte[] bytes) {
		return new String(bytes, Charset.defaultCharset());
	}

	/**
	 * 将字节数组转换指定编码集的字符串
	 *
	 * @param bytes 待转换的字节数组
	 * @param charset 编码集
	 * @return 字符串
	 */
	public static String toString(final byte[] bytes, final Charset charset) {
		return new String(bytes, CharsetHelper.defaultCharset(charset));
	}

	/**
	 * 若对象为null,则返回"";若不为空,返回源字符串
	 * 
	 * @param str 源字符串
	 * @return 源字符串或""
	 */
	public static String toString(final Object str) {
		return toString(str, ConstString.STR_EMPTY);
	}

	/**
	 * 若对象为null,则返回默认值;若不为空,返回源字符串
	 * 
	 * @param str 源字符串
	 * @param defaultValue 默认值
	 * @return 源字符串或""
	 */
	public static String toString(final Object str, String defaultValue) {
		return null == str ? defaultValue : String.valueOf(str);
	}

	/**
	 * 将字符串按照指定编码转为字节数组后再转换为utf8字符串
	 *
	 * @param str 待转换的字符串
	 * @param charset 编码集
	 * @return 字符串
	 */
	public static String toString(String str, final Charset charset) {
		return new String(str.getBytes(charset), ConstString.DEFAULT_CHARSET);
	}

	/**
	 * 将字符串按照指定编码转为字节数组后再转换为另外一种编码集的字符串
	 *
	 * @param str 待转换的字符串
	 * @param sourceCharset 源字符串编码集
	 * @param charset 编码集
	 * @return 字符串
	 */
	public static String toString(String str, final Charset sourceCharset, final Charset charset) {
		return new String(str.getBytes(sourceCharset), CharsetHelper.defaultCharset(charset));
	}

	/**
	 * 删除源字符串中所有空白,null,制表符,换行符,包括字符串中间
	 *
	 * @param str 源字符串
	 * @return 删除空白后的新字符串
	 */
	public static String trimAll(final String str) {
		if (isEmpty(str)) {
			return str;
		}
		final int sz = str.length();
		final char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		return new String(chs, 0, count);
	}

	/**
	 * 删除源字符串头部所有空白,null,制表符,换行符,包括字符串中间
	 * 
	 * @param str 源字符串
	 * @return 删除空白后的新字符串
	 */
	public static String trimLeading(String str) {
		if (!hasLength(str)) {
			return str;
		}

		int beginIdx = 0;
		while (beginIdx < str.length() && Character.isWhitespace(str.charAt(beginIdx))) {
			beginIdx++;
		}
		return str.substring(beginIdx);
	}

	/**
	 * 删除源字符串头部指定字符串
	 * 
	 * @param str 源字符串
	 * @param leadingCharacter 待删除字符串
	 * @return 删除指定字符串后的新字符串
	 */
	public static String trimLeading(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}

		int beginIdx = 0;
		while (beginIdx < str.length() && leadingCharacter == str.charAt(beginIdx)) {
			beginIdx++;
		}
		return str.substring(beginIdx);
	}

	/**
	 * 删除源字符串末尾所有空白,null,制表符,换行符,包括字符串中间
	 * 
	 * @param str 源字符串
	 * @return 删除空白后的新字符串
	 */
	public static String trimTrailing(String str) {
		if (!hasLength(str)) {
			return str;
		}

		int endIdx = str.length() - 1;
		while (endIdx >= 0 && Character.isWhitespace(str.charAt(endIdx))) {
			endIdx--;
		}
		return str.substring(0, endIdx + 1);
	}

	/**
	 * 删除源字符串末尾指定字符串
	 * 
	 * @param str 源字符串
	 * @param trailingCharacter 待删除字符串
	 * @return 删除指定字符串后的新字符串
	 */
	public static String trimTrailing(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}

		int endIdx = str.length() - 1;
		while (endIdx >= 0 && trailingCharacter == str.charAt(endIdx)) {
			endIdx--;
		}
		return str.substring(0, endIdx + 1);
	}

	/**
	 * 将带下划线的字段名(蛇底式)变成小驼峰
	 * 
	 * @param str 需要进行转换的字符串
	 * @return 转换后的字符串
	 */
	public static String underline2Hump(String str) {
		return underline2Hump(str, false);
	}

	/**
	 * 将带下划线的字段名(蛇底式)变成驼峰
	 * 
	 * @param str 需要进行转换的字符串
	 * @param flag true大驼峰,false小驼峰
	 * @return 转换后的字符串
	 */
	public static String underline2Hump(String str, boolean flag) {
		str = str.toLowerCase();
		Matcher matcher = PATTERN_LINE.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return firstChange(sb.toString(), flag);
	}

	/**
	 * 以 . 作为分隔符,获取指定字符串最后一部分
	 * 
	 * @param str 指定字符串
	 * @return 最后一部分字符串
	 */
	public static String unqualify(String str) {
		return unqualify(str, '.');
	}

	/**
	 * 以指定字符作为分隔符,获取指定字符串最后一部分
	 * 
	 * @param str 指定字符串
	 * @param separator 分隔符
	 * @return 最后一部分字符串
	 */
	public static String unqualify(String str, char separator) {
		return str.substring(str.lastIndexOf(separator) + 1);
	}

	/**
	 * 展开源字符串,删除起始和末尾的指定字符.注意,起始和末尾必须都有才删除
	 *
	 * @param str 源字符串
	 * @param wrapChar 用来包裹的字符
	 * @return 删除起始和末尾包裹的字符之后的新字符串
	 */
	public static String unwrap(final String str, final char wrapChar) {
		if (isEmpty(str) || wrapChar == ConstSymbol.CHAR_NUL || str.length() == 1) {
			return str;
		}
		if (str.charAt(0) == wrapChar && str.charAt(str.length() - 1) == wrapChar) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}

	/**
	 * 展开源字符串,删除起始和末尾的指定字符串.注意,起始和末尾必须都有才删除
	 *
	 * @param str 源字符串
	 * @param wrapStr 用来包裹的字符串
	 * @return 删除起始和末尾包裹的字符串之后的新字符串
	 */
	public static String unwrap(final String str, final String wrapStr) {
		if (isEmpty(str) || isEmpty(wrapStr) || str.length() == 1) {
			return str;
		}
		if (startsWith(str, wrapStr) && endsWith(str, wrapStr)) {
			int startIndex = str.indexOf(wrapStr);
			int endIndex = str.lastIndexOf(wrapStr);
			if (startIndex != -1 && endIndex != -1) {
				return str.substring(startIndex + wrapStr.length(), endIndex);
			}
		}
		return str;
	}

	/**
	 * 包裹源字符串,起始和末尾同时添加指定字符
	 *
	 * @param str 源字符串
	 * @param wrapChar 用来包裹的字符
	 * @return 起始和末尾同时添加指定字符之后的新字符串
	 */
	public static String wrap(final String str, final char wrapChar) {
		if (isEmpty(str) || wrapChar == ConstSymbol.CHAR_NUL) {
			return str;
		}
		return wrapChar + str + wrapChar;
	}

	/**
	 * 包裹源字符串,起始和末尾同时添加指定字符串
	 *
	 * @param str 源字符串
	 * @param wrapStr 用来包裹的字符串
	 * @return 起始和末尾同时添加指定字符串之后的新字符串
	 */
	public static String wrap(final String str, final String wrapStr) {
		if (isEmpty(str) || isEmpty(wrapStr)) {
			return str;
		}
		return wrapStr.concat(str).concat(wrapStr);
	}

	/**
	 * 包裹源字符串,若起始或末尾没有待添加字符,则添加;有则不变
	 *
	 * @param str 源字符串
	 * @param wrapChar 用来包裹的字符
	 * @return 起始和末尾同时添加指定字符之后的新字符串
	 */
	public static String wrapIfMissing(final String str, final char wrapChar) {
		if (isEmpty(str) || wrapChar == ConstSymbol.CHAR_NUL) {
			return str;
		}
		final boolean wrapStart = str.charAt(0) != wrapChar;
		final boolean wrapEnd = str.charAt(str.length() - 1) != wrapChar;
		if (!wrapStart && !wrapEnd) {
			return str;
		}
		final StringBuilder builder = new StringBuilder(str.length() + 2);
		if (wrapStart) {
			builder.append(wrapChar);
		}
		builder.append(str);
		if (wrapEnd) {
			builder.append(wrapChar);
		}
		return builder.toString();
	}

	/**
	 * 包裹源字符串,若起始或末尾没有待添加字符串,则添加;有则不变
	 *
	 * @param str 源字符串
	 * @param wrapStr 用来包裹的字符串
	 * @return 起始和末尾同时添加指定字符串之后的新字符串
	 */
	public static String wrapIfMissing(final String str, final String wrapStr) {
		if (isEmpty(str) || isEmpty(wrapStr)) {
			return str;
		}
		final boolean wrapStart = !str.startsWith(wrapStr);
		final boolean wrapEnd = !str.endsWith(wrapStr);
		if (!wrapStart && !wrapEnd) {
			return str;
		}
		final StringBuilder builder = new StringBuilder(str.length() + wrapStr.length() + wrapStr.length());
		if (wrapStart) {
			builder.append(wrapStr);
		}
		builder.append(str);
		if (wrapEnd) {
			builder.append(wrapStr);
		}
		return builder.toString();
	}
}

@Example
class StrToolExample {

	/**
	 * 使用 {@link MessageFormat}格式化.该方法只是一个例子
	 * 
	 * 该方法格式化单引号时,需要双单引号才能输出,双引号直接转义即可.单引号表示原样输出
	 * 
	 * <pre>
	 * MessageFormat.format("飞{0}梦影","花");->飞花梦影
	 * MessageFormat.format("飞'{0}'梦影","花");->飞{0}梦影
	 * MessageFormat.format("飞''{0}''梦影","花");->飞'花'梦影
	 * MessageFormat.format("飞\"{0}\"梦影","花");->飞"花"梦影
	 * </pre>
	 * 
	 * @param format 模板,其中的参数需要用{0},{1}...
	 * @param args 参数,下标和format中的数字对应.若少于format的数字,则直接输出{n},超出的丢弃
	 * @return
	 */
	public static void formatMessage(String format, Object... args) {

	}
}