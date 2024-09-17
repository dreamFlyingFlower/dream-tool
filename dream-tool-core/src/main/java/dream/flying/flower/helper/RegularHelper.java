package dream.flying.flower.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dream.flying.flower.ConstLang;
import dream.flying.flower.ConstSymbol;
import dream.flying.flower.enums.RegexEnum;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * Regular正则工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 23:03:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class RegularHelper {

	/**
	 * 是否包含中文
	 * 
	 * @param cs 字符序列
	 * @return true->是;false->否
	 */
	public static Boolean containsChinese(final CharSequence cs) {
		String regEx = "[\u2E80-\u9FFF]+$";
		return Pattern.compile(regEx).matcher(cs).find();
	}

	/**
	 * 检查字符串是否包含小写
	 * 
	 * @param str 待检查字符串
	 * @return true->是;false->否
	 */
	public static Boolean containsLower(String str) {
		return Pattern.compile("[a-z]").matcher(str).find();
	}

	/**
	 * 字符串是否包含特殊字
	 * 
	 * @param cs 源字符序列
	 * @return true->包含;false->不包含
	 */
	public static Boolean containSpecial(CharSequence cs) {
		return Pattern.compile(ConstSymbol.SPECIAL).matcher(cs).find();
	}

	/**
	 * 检查字符串是否包含大写
	 * 
	 * @param str 待检查字符串
	 * @return true->是;false->否
	 */
	public static Boolean containsUpper(String str) {
		return Pattern.compile("[A-Z]").matcher(str).find();
	}

	/**
	 * 从待查找字符串中查找所有符合条件的字符串
	 * 
	 * @param str 待查找字符串
	 * @param pattern 正则表达式
	 * @return 待查找字符串中所有符合正则的字符串
	 */
	public static List<String> findAll(String str, Pattern pattern) {
		AssertHelper.notNull(pattern);
		List<String> result = new ArrayList<>();
		if (StrHelper.isNotBlank(str)) {
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				result.add(matcher.group());
			}
		}
		return result;
	}

	/**
	 * 从待查找字符串中查找所有符合条件的字符串
	 * 
	 * @param str 待查找字符串
	 * @param regex 正则表达式
	 * @return 待查找字符串中所有符合正则的字符串
	 */
	public static List<String> findAll(String str, String regex) {
		AssertHelper.notBlank(regex);
		List<String> result = new ArrayList<>();
		if (StrHelper.isNotBlank(str)) {
			Matcher matcher = Pattern.compile(regex).matcher(str);
			while (matcher.find()) {
				result.add(matcher.group());
			}
		}
		return result;
	}

	/**
	 * 从待查找字符串中查找第一个符合条件的字符串
	 * 
	 * @param str 待查找字符串
	 * @param pattern 正则表达式
	 * @return 待查找字符串中第一个符合正则的字符串
	 */
	public static String findFirst(String str, Pattern pattern) {
		AssertHelper.notNull(pattern);
		if (StrHelper.isNotBlank(str)) {
			Matcher matcher = pattern.matcher(str);
			return matcher.find() ? matcher.group() : null;
		}
		return null;
	}

	/**
	 * 从待查找字符串中查找第一个符合条件的字符串
	 * 
	 * @param str 待查找字符串
	 * @param regex 正则表达式
	 * @return 待查找字符串中第一个符合正则的字符串
	 */
	public static String findFirst(String str, String regex) {
		AssertHelper.notBlank(regex);
		if (StrHelper.isNotBlank(str)) {
			Matcher matcher = Pattern.compile(regex).matcher(str);
			return matcher.find() ? matcher.group() : null;
		}
		return null;
	}

	/**
	 * 验证是否为正确的邮箱号
	 *
	 * @param email email地址
	 * @return true->是;false->否
	 */
	public static boolean isEmail(String email) {
		return isMatch(RegexEnum.REGEX_EMAIL, email);
	}

	/**
	 * 验证字符串是否符合指定的正则
	 * 
	 * @param regexEnum 正则
	 * @param str 待检查字符串
	 * @return true->是;false->否
	 */
	public static boolean isMatch(RegexEnum regexEnum, String str) {
		Pattern pattern = Pattern.compile(regexEnum.toString());
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 验证是否为手机号
	 *
	 * @param phoneNo 手机号
	 * @return true->是;false->否
	 */
	public static boolean isPhoneNo(String phoneNo) {
		return isMatch(RegexEnum.REGEX_PHONE, phoneNo);
	}

	/**
	 * 删除字符串中匹配给定正则表达式的所有字符串
	 *
	 * @param text 待删除字符串
	 * @param regex 正则表达式
	 * @return 删除匹配正则之后的字符串
	 */
	public static String removeAll(final String text, final Pattern regex) {
		return replaceAll(text, regex, ConstLang.STR_EMPTY);
	}

	/**
	 * 删除字符串中匹配给定正则表达式的所有字符串
	 *
	 * @param text 待删除字符串
	 * @param regex 正则表达式
	 * @return 删除匹配正则之后的字符串
	 */
	public static String removeAll(final String text, final String regex) {
		return replaceAll(text, regex, ConstLang.STR_EMPTY);
	}

	/**
	 * 删除字符串中匹配给定正则表达式的第一个子字符串
	 *
	 * @param text 待删除字符串
	 * @param regex 正则表达式
	 * @return 删除匹配正则之后的字符串
	 */
	public static String removeFirst(final String text, final Pattern regex) {
		return replaceFirst(text, regex, ConstLang.STR_EMPTY);
	}

	/**
	 * 删除字符串中匹配给定正则表达式的第一个子字符串
	 *
	 * @param text 待删除字符串
	 * @param regex 正则表达式
	 * @return 删除匹配正则之后的字符串
	 */
	public static String removeFirst(final String text, final String regex) {
		return replaceFirst(text, regex, ConstLang.STR_EMPTY);
	}

	/**
	 * 删除字符串中匹配给定正则表达式的所有字符串,该方法中点(.)匹配任何字符串,包括换行符
	 *
	 * @param text 待删除字符串
	 * @param regex 正则表达式
	 * @return The resulting {@code String}
	 */
	public static String removePattern(final String text, final String regex) {
		return replacePattern(text, regex, ConstLang.STR_EMPTY);
	}

	/**
	 * 替换源字符串中匹配给定正则表达式的所有子字符串
	 *
	 * @param text 源字符串
	 * @param regex 正则表达式
	 * @param replacement 需要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceAll(final String text, final Pattern regex, final String replacement) {
		if (text == null || regex == null || replacement == null) {
			return text;
		}
		return regex.matcher(text).replaceAll(replacement);
	}

	/**
	 * 替换源字符串中匹配给定正则表达式的所有子字符串
	 *
	 * @param text 源字符串
	 * @param regex 正则表达式
	 * @param replacement 需要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceAll(final String text, final String regex, final String replacement) {
		if (text == null || regex == null || replacement == null) {
			return text;
		}
		return text.replaceAll(regex, replacement);
	}

	/**
	 * 替换源字符串中匹配给定正则表达式的第一个子字符串
	 *
	 * @param text 源字符串
	 * @param regex 正则表达式
	 * @param replacement 需要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
		if (text == null || regex == null || replacement == null) {
			return text;
		}
		return regex.matcher(text).replaceFirst(replacement);
	}

	/**
	 * 替换源字符串中匹配给定正则表达式的第一个子字符串
	 *
	 * @param text 源字符串
	 * @param regex 正则表达式
	 * @param replacement 需要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replaceFirst(final String text, final String regex, final String replacement) {
		if (text == null || regex == null || replacement == null) {
			return text;
		}
		return text.replaceFirst(regex, replacement);
	}

	/**
	 * 替换字符串中匹配给定正则表达式的所有子字符串,该方法中点(.)匹配任何字符串,包括换行符
	 *
	 * @param text 源字符串
	 * @param regex 正则表达式
	 * @param replacement 需要替换的字符串
	 * @return 替换后的字符串
	 */
	public static String replacePattern(final String text, final String regex, final String replacement) {
		if (text == null || regex == null || replacement == null) {
			return text;
		}
		return Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement);
	}

	/**
	 * 检查字符串是否以大写开头
	 * 
	 * @param str 待检查字符串
	 * @return true->是;false->否
	 */
	public static Boolean startWithUpper(String str) {
		return Pattern.compile("^[A-Z]").matcher(str).find();
	}

	/**
	 * 检查字符串是否以小写开头
	 * 
	 * @param str 待检查字符串
	 * @return true->是;false->否
	 */
	public static Boolean startWithLower(String str) {
		return Pattern.compile("^[a-z]").matcher(str).find();
	}

	/**
	 * 检查字符串是否以数字开头
	 * 
	 * @param str 待检查字符串
	 * @return true->是;false->否
	 */
	public static Boolean startWithNumber(String str) {
		return Pattern.compile("^[0-9]").matcher(str).find();
	}
}