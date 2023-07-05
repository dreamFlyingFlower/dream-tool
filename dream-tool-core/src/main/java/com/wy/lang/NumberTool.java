package com.wy.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import com.wy.util.ArrayTool;

/**
 * Number工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-22 17:30:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class NumberTool {

	/**
	 * 小数默认保留的小数位
	 */
	private static final int DEFAULT_SCALE = 2;

	/**
	 * 数字类型避免精度缺失的加法运算,但仍会有精度丢失
	 * 
	 * @param array 数组
	 * @return 加法运算的结果
	 */
	public static BigDecimal add(Number... numbers) {
		if (ArrayTool.isEmpty(numbers)) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = new BigDecimal((null == numbers[0] ? BigDecimal.ZERO : numbers[0]).toString());
		for (int i = 1; i < numbers.length; i++) {
			result = null == numbers[i] ? result : result.add(new BigDecimal(numbers[i].toString()));
		}
		return result;
	}

	/**
	 * 比较2个数的大小
	 * 
	 * @param number1 数字1
	 * @param number2 数字2
	 * @return 大于0时数组1大于数字2,反之则小于,等于0则相等
	 */
	public static int compare(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2);
	}

	/**
	 * 判断指定值是否为null,若为null,则赋值0
	 * 
	 * @param number 待判断的值
	 * @return 返回值
	 */
	public static BigDecimal defaultIfNull(BigDecimal number) {
		return defaultIfNull(number, BigDecimal.ZERO);
	}

	/**
	 * 判断指定值是否为null,若为null,返回默认值
	 * 
	 * @param number 待判断的值
	 * @param defaultValue 默认值
	 * @return 返回值
	 */
	public static BigDecimal defaultIfNull(BigDecimal number, BigDecimal defaultValue) {
		AssertTool.notNull(defaultValue);
		return null == number ? defaultValue : number;
	}

	/**
	 * 精确的除法运算.当发生除不尽的情况时,默认精确到小数点以后2位,并且四舍五入
	 *
	 * @param arg1 被除数
	 * @param arg2 除数
	 * @return arg1除以arg2的商
	 */
	public static BigDecimal div(double arg1, double arg2) {
		return div(new BigDecimal(arg1), new BigDecimal(arg2), DEFAULT_SCALE);
	}

	/**
	 * 精确的除法运算.当发生除不尽的情况时,scale指定精度,并且四舍五入
	 *
	 * @param arg1 被除数
	 * @param arg2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 * @return arg1除以arg2的商
	 */
	public static BigDecimal div(double arg1, double arg2, int scale) {
		return div(new BigDecimal(arg1), new BigDecimal(arg2), scale);
	}

	/**
	 * 精确的除法运算.当发生除不尽的情况时,scale指定精度,并且四舍五入
	 *
	 * @param arg1 被除数
	 * @param arg2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 * @param roundingMode 小数处理方式
	 * @return arg1除以arg2的商
	 */
	public static BigDecimal div(double arg1, double arg2, int scale, RoundingMode roundingMode) {
		return div(new BigDecimal(arg1), new BigDecimal(arg2), scale, roundingMode);
	}

	/**
	 * 精确的除法运算.当发生除不尽的情况时,scale指定精度,并且四舍五入
	 *
	 * @param arg1 被除数
	 * @param arg2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 * @return arg1除以arg2的商
	 */
	public static BigDecimal div(Number arg1, Number arg2, int scale) {
		return div(arg1, arg2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 精确的除法运算.当发生除不尽的情况时,scale指定精度
	 *
	 * @param arg1 被除数
	 * @param arg2 除数
	 * @param scale 表示表示需要精确到小数点以后几位
	 * @param roundingMode 小数处理方式
	 * @return arg1除以arg2的商
	 */
	public static BigDecimal div(Number arg1, Number arg2, int scale, RoundingMode roundingMode) {
		scale = scale < 0 ? 0 : scale;
		BigDecimal b1 = new BigDecimal(arg1 == null ? "0" : arg1.toString());
		BigDecimal b2 = new BigDecimal(arg2 == null ? "0" : arg2.toString());
		if (b1.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		if (b2.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("除数不可为0");
		}
		return b1.divide(b2, scale, roundingMode);
	}

	/**
	 * 判断 a 与 b 是否相等,并不精确
	 * 
	 * @param a 数字a
	 * @param b 数字b
	 * @return a==b 返回true, a!=b 返回false
	 */
	public static boolean equal(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		if (v1.compareTo(v2) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 阶乘,如5!,阶乘不得超过64,会超出long的最大值
	 * 
	 * @return 阶乘结果,可能会抛数字过大异常
	 */
	public static long factorial(int n) {
		if (n <= 0 || n > 63) {
			return 0;
		}
		long l = 1;
		for (int i = 1; i <= n; i++) {
			l *= i;
		}
		return l;
	}

	/**
	 * 将数字转化为指定的字符串格式
	 * 
	 * <pre>
	 * 1l->0.000%格式化后为1.000%,#.###kg为1%
	 * 1l->0.000格式化为1.000,#.###为1
	 * 1l->.000格式化为1.000,#.###为1
	 * 1l->000.0000格式化为001.0000,###.####格式化为1
	 * </pre>
	 * 
	 * @param number 待格式化的数字
	 * @param format 需要进行转换的格式.<br>
	 *        0占位符,格式固定,num超出的整数部分全部显示,不足补0;超出的小数部分为四舍五入,不足补0
	 *        #占位符,格式不固定,num超出的整数部分全部显示,不足不补0;超出的小数部分四舍五入,不足不补0
	 * @return 格式化后的字符串
	 */
	public static String format(long number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 将数字转化为指定的字符串格式
	 * 
	 * <pre>
	 * 3.4555->0.000%格式化后为3.456kg,#.###%为3.456%;
	 * 34.4555->0.000格式化为34.456,#.###为34.456;
	 * 34.455->000.0000格式化为034.4550,###.####格式化为34.455
	 * </pre>
	 * 
	 * @param number 待格式化的数字
	 * @param format 需要进行转换的格式.<br>
	 *        0占位符,格式固定,num超出的整数部分全部显示,不足补0;超出的小数部分四舍五入,不足补0
	 *        #占位符,格式不固定,num超出的整数部分全部显示,不足不补0;超出的小数部分四舍五入,不足不补0
	 * @return 格式化后的字符串
	 */
	public static String format(double number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 将数字转化为指定的字符串格式
	 * 
	 * <pre>
	 * 3.4555->0.000%格式化后为3.456%,#.###kg为3.456%;
	 * 34.4555->0.000格式化为34.456,#.###为34.456;
	 * 34.455->000.0000格式化为034.4550,###.####格式化为34.455
	 * </pre>
	 * 
	 * @param number 待格式化的数字
	 * @param format 需要进行转换的格式.<br>
	 *        0占位符,格式固定,num超出的整数部分全部显示,不足补0;超出的小数部分四舍五入,不足补0
	 *        #占位符,格式不固定,num超出的整数部分全部显示,不足不补0;超出的小数部分四舍五入,不足不补0
	 * @return 格式化后的字符串
	 */
	public static String format(Number number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 将数值转换为当前系统默认的货币形式
	 * 
	 * @param number 数字
	 * @return 本地货币形式
	 */
	public static String formatCurrency(Number number) {
		return formatCurrency(number, Locale.getDefault());
	}

	/**
	 * 将数值转换为指定格式的货币形式
	 * 
	 * @param number 数字
	 * @return 货币形式
	 */
	public static String formatCurrency(Number number, Locale locale) {
		NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
		return nf.format(number);
	}

	/**
	 * 格式化百分比,小数采用四舍五入方式,默认保留2位小数
	 *
	 * @param number 数字
	 * @return 百分比
	 */
	public static String formatPercent(double number) {
		return formatPercent(number, DEFAULT_SCALE);
	}

	/**
	 * 格式化百分比,小数采用四舍五入方式
	 *
	 * @param number 数字
	 * @param scale 保留小数位数
	 * @return 百分比
	 */
	public static String formatPercent(double number, int scale) {
		NumberFormat format = NumberFormat.getPercentInstance();
		format.setMaximumFractionDigits(scale);
		return format.format(number);
	}

	/**
	 * 判断 a 是否大于等于 b
	 * 
	 * @param a 数字a
	 * @param b 数字b
	 * @return a>=b 返回true, a<b 返回false
	 */
	public static boolean ge(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		return v1.compareTo(v2) >= 0;
	}

	/**
	 * 判断 a 是否大于 b
	 * 
	 * @param a 数字a
	 * @param b 数字b
	 * @return a>b 返回true, a<=b 返回 false
	 */
	public static boolean gt(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		return v1.compareTo(v2) == 1;
	}

	/**
	 * 对象是否能强转为一个整数,不包括小数,科学计数法,+-等.更快的方法是直接判空
	 * 
	 * @param obj 对象
	 * @return true是,false不是
	 */
	public static boolean isDigits(Object obj) {
		Optional<Object> optional = Optional.ofNullable(obj);
		return optional.isPresent() ? StrTool.isNumeric(optional.get().toString()) : false;
	}

	/**
	 * 判断对象是否能强转为一个小数,但不一定是整数,可能最后一位是点.不包括科学计数法,+-等
	 * 
	 * @param obj 对象
	 * @return true是,false不是
	 */
	public static boolean isDecimal(Object obj) {
		Optional<Object> optional = Optional.ofNullable(obj);
		if (!optional.isPresent()) {
			return false;
		}
		String str = optional.get().toString();
		// 只有一个点
		if (str.length() == 1 && str.charAt(0) == '.') {
			return false;
		}
		// 判断结尾是否有f,F,d,D
		if (str.length() > 1) {
			if (str.charAt(str.length() - 1) == 'f' || str.charAt(str.length() - 1) == 'F'
					|| str.charAt(str.length() - 1) == 'd' || str.charAt(str.length() - 1) == 'D') {
				str = str.substring(0, str.length() - 1);
			}
		}
		return checkDecimal(str);
	}

	private static boolean checkDecimal(final String str) {
		int decimalPoints = 0;
		for (int i = 0; i < str.length(); i++) {
			final boolean isDecimalPoint = str.charAt(i) == '.';
			if (isDecimalPoint) {
				decimalPoints++;
			}
			if (decimalPoints > 1) {
				return false;
			}
			if (!isDecimalPoint && !Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为数字,包括整数,小数,科学计数,8进制,16进制等 FIXME
	 * 
	 * <pre>
	 * isNumber("00019") = false;
	 * isNumber("0001E2") = false;
	 * </pre>
	 * 
	 * @apiNote "00019"会被认为是8进制的17,而不是10进制19,同时该字符串会被判断为不是一个数字
	 * @return true当是一个java支持的数
	 */
	public static boolean isNumber(Object obj) {
		if (null == obj) {
			return false;
		}
		if (obj instanceof Number) {
			return true;
		}
		String str = String.valueOf(obj);
		if (StrTool.isBlank(str)) {
			return false;
		}
		final char[] chars = str.toCharArray();
		int sz = chars.length;
		// 处理最前面的+,-号
		final int start = chars[0] == '-' || chars[0] == '+' ? 1 : 0;
		// 若是整数,处理前面的0
		if (sz > start + 1 && chars[start] == '0' && !str.contains(".")) {
			if (chars[start + 1] == 'x' || chars[start + 1] == 'X') {
				// 判断是否为16进制数字
				int i = start + 2;
				if (i == sz) {
					// str == "0x"
					return false;
				}
				// 16进制只含有0-9,a-f,A-F
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			} else if (Character.isDigit(chars[start + 1])) {
				// 判断是否为8进制数字
				int i = start + 1;
				for (; i < chars.length; i++) {
					if (chars[i] < '0' || chars[i] > '7') {
						return false;
					}
				}
				return true;
			}
		}
		// 不循环到最后一个字符,在后面检查类型限定符,如l,f,d,e等
		sz--;
		int i = start;
		// 是否有科学计数法的e
		boolean hasExp = false;
		// 是否有小数点
		boolean hasDecPoint = false;
		// 是否有+,-号
		boolean allowSigns = false;
		// 是否有数字
		boolean foundDigit = false;
		// 是否为科学计数
		while (i < sz || i < sz + 1 && allowSigns && !foundDigit) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// 是数字
				foundDigit = true;
				// 数字的下一个不可以是+,-
				allowSigns = false;
			} else if (chars[i] == '.') {
				// 小数点只能有一个
				if (hasDecPoint || hasExp) {
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// 科学计数法,只能有一个e或E
				if (hasExp) {
					return false;
				}
				// e前面不能没有数字
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				// 下一个可能是+,-,也可能是数字,正数情况下,+可以省略
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				// 科学计数法后需要紧跟一个+或-或数字,正数情况下,+可以省略
				if (!allowSigns) {
					return false;
				}
				// 下一个只能是数字
				allowSigns = false;
				// e后面可能跟一个数字或+数字或-数字
				foundDigit = false;
			} else {
				return false;
			}
			i++;
		}
		// 若前面都检查正常,此时检查最后一个字符
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				return true;
			}
			// 最后一个不能是e
			if (chars[i] == 'e' || chars[i] == 'E') {
				return false;
			}
			// 不能有多个点,但可以是单个点结尾
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					return false;
				}
				return foundDigit;
			}
			// 不能以+,-结尾,但可以以f,d结尾
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			// 最后以l结尾,最后的数只能是数字,且前面的数不能有e和小数点
			if (chars[i] == 'l' || chars[i] == 'L') {
				return foundDigit && !hasExp && !hasDecPoint;
			}
			// 最后一个字符不合法
			return false;
		}
		// 最后一个不能是+,-,且必须是数字
		return !allowSigns && foundDigit;
	}

	/**
	 * 判断 a 是否小于等于 b
	 * 
	 * @param a 数字a
	 * @param b 数字b
	 * @return a<=b 返回true, a>b 返回 false
	 */
	public static boolean le(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		return v1.compareTo(v2) <= 0;
	}

	/**
	 * 判断 a 是否小于 b
	 * 
	 * @param a 数字a
	 * @param b 数字b
	 * @return a<b 返回true, a>=b 返回 false
	 */
	public static boolean lt(double a, double b) {
		BigDecimal v1 = BigDecimal.valueOf(a);
		BigDecimal v2 = BigDecimal.valueOf(b);
		return v1.compareTo(v2) == -1;
	}

	/**
	 * 数组中的最小值,number中可以存放任何数字类型包装类,Byte,Short,Integer等
	 * 
	 * @param array 数组
	 * @return 数组中最小值
	 */
	public static Number min(Number[] array) {
		Arrays.sort(array);
		return array[0];
	}

	/**
	 * 查找数组中最小的值
	 * 
	 * @param array 数组
	 * @return 数组中的最小值
	 */
	public static byte min(final byte... array) {
		AssertTool.isNull(array, "this array can not be empty");
		byte min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	/**
	 * 查找数组中最小的值
	 * 
	 * @param array 数组
	 * @return 数组中的最小值
	 */
	public static short min(final short... array) {
		AssertTool.isNull(array, "this array can not be empty");
		short min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	/**
	 * 查找数组中最小的值
	 * 
	 * @param array 数组
	 * @return 数组中的最小值
	 */
	public static int min(final int... array) {
		AssertTool.isNull(array, "this array can not be empty");
		int min = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] < min) {
				min = array[j];
			}
		}
		return min;
	}

	/**
	 * 查找数组中最小的值
	 * 
	 * @param array 数组
	 * @return 数组中的最小值
	 */
	public static long min(final long... array) {
		AssertTool.isNull(array, "this array can not be empty");
		long min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	/**
	 * 查找数组中最小的值
	 * 
	 * @param array 数组
	 * @return 数组中的最小值
	 */
	public static float min(final float... array) {
		AssertTool.isNull(array, "this array can not be empty");
		float min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (Float.isNaN(array[i])) {
				continue;
			}
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	/**
	 * 查找数组中最小的值
	 * 
	 * @param array 数组
	 * @return 数组中的最小值
	 */
	public static double min(final double... array) {
		AssertTool.isNull(array, "this array can not be empty");
		double min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (Double.isNaN(array[i])) {
				continue;
			}
			if (array[i] < min) {
				min = array[i];
			}
		}
		return min;
	}

	/**
	 * 数组中的最大值,number中可以存放任何数字类型包装类,Byte,Short,Integer等
	 * 
	 * @param array 数组
	 * @return 数组中最小值
	 */
	public static Number max(Number[] array) {
		Arrays.sort(array);
		return array[array.length - 1];
	}

	/**
	 * 查找数组中最大的值
	 * 
	 * @param array 数组
	 * @return 数组中的最大值
	 */
	public static byte max(final byte... array) {
		AssertTool.isNull(array, "this array can not be empty");
		byte max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}

	/**
	 * 查找数组中最大的值
	 * 
	 * @param array 数组
	 * @return 数组中的最大值
	 */
	public static short max(final short... array) {
		AssertTool.isNull(array, "this array can not be empty");
		short max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}

	/**
	 * 查找数组中最大的值
	 * 
	 * @param array 数组
	 * @return 数组中的最大值
	 */
	public static int max(final int... array) {
		AssertTool.isNull(array, "this array can not be empty");
		int max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}
		return max;
	}

	/**
	 * 查找数组中最大的值
	 * 
	 * @param array 数组
	 * @return 数组中的最大值
	 */
	public static long max(final long... array) {
		AssertTool.isNull(array, "this array can not be empty");
		long max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}
		return max;
	}

	/**
	 * 查找数组中最大的值
	 * 
	 * @param array 数组
	 * @return 数组中的最大值
	 */
	public static float max(final float... array) {
		AssertTool.isNull(array, "this array can not be empty");
		float max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (Float.isNaN(array[j])) {
				continue;
			}
			if (array[j] > max) {
				max = array[j];
			}
		}
		return max;
	}

	/**
	 * 查找数组中最大的值
	 * 
	 * @param array 数组
	 * @return 数组中的最大值
	 */
	public static double max(final double... array) {
		AssertTool.isNull(array, "this array can not be empty");
		double max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (Double.isNaN(array[j])) {
				continue;
			}
			if (array[j] > max) {
				max = array[j];
			}
		}
		return max;
	}

	/**
	 * 精准的乘法运算
	 * 
	 * @param arg1 被乘数
	 * @param arg2 乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal multiply(double arg1, double arg2) {
		return new BigDecimal(arg1).multiply(new BigDecimal(arg2));
	}

	/**
	 * 多个数字相乘
	 * 
	 * @param numbers 数字数组
	 * @return 乘积结果
	 */
	public static BigDecimal multiply(Number... numbers) {
		if (ArrayTool.isEmpty(numbers)) {
			return BigDecimal.ZERO;
		}
		BigDecimal result = new BigDecimal(1);
		for (Number number : numbers) {
			if (number == null || new BigDecimal(number.toString()).compareTo(BigDecimal.ZERO) == 0) {
				return BigDecimal.ZERO;
			}
			result = result.multiply(new BigDecimal(number.toString()));
		}
		return result;
	}

	/**
	 * 获得范围内的随机数,包括开头和结尾
	 * 
	 * @param a 最小数
	 * @param b 最大数,不能比最小数小,否则返回-1
	 * @return a和b之间的随机整数
	 */
	public static int random(int a, int b) {
		if (a > b) {
			return -1;
		}
		return a + (int) ((b + 1 - a) * Math.random());
	}

	/**
	 * 获得范围内的随机数,包括开头和结尾
	 * 
	 * @param a 最小数
	 * @param b 最大数,不能比最小数小,否则返回-1
	 * @return a和b之间的随机整数
	 */
	public static long random(long a, long b) {
		if (a > b) {
			return -1;
		}
		return a + (long) ((b + 1 - a) * Math.random());
	}

	/**
	 * 保留2位小数,默认四舍五入
	 * 
	 * @param num 数字
	 */
	public static float round(float num) {
		return round(num, DEFAULT_SCALE);
	}

	/**
	 * 保留2位小数,默认四舍五入
	 * 
	 * @param num 数字
	 */
	public static double round(double num) {
		return round(num, DEFAULT_SCALE);
	}

	/**
	 * 保留多位小数,默认四舍五入
	 * 
	 * @param num 要保留小数的原始值
	 * @param scale 需要保留几位小数
	 */
	public static double round(Number num) {
		return round(num, DEFAULT_SCALE);
	}

	/**
	 * 保留多位小数,默认四舍五入
	 * 
	 * @param num 要保留小数的原始值
	 * @param scale 需要保留几位小数
	 */
	public static float round(float num, int scale) {
		scale = scale < 0 ? 0 : scale;
		BigDecimal big = new BigDecimal(num);
		return big.setScale(scale, RoundingMode.HALF_UP).floatValue();
	}

	/**
	 * 保留多位小数,默认四舍五入
	 * 
	 * @param num 要保留小数的原始值
	 * @param scale 需要保留几位小数
	 */
	public static double round(double num, int scale) {
		scale = scale < 0 ? 0 : scale;
		BigDecimal big = new BigDecimal(num);
		return big.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 保留多位小数,默认四舍五入
	 * 
	 * @param num 要保留小数的原始值
	 * @param scale 需要保留几位小数
	 */
	public static double round(Number num, int scale) {
		scale = scale < 0 ? 0 : scale;
		BigDecimal big = new BigDecimal(num == null ? "0" : num.toString());
		return big.setScale(scale, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 选择排序,默认升序
	 * 
	 * @param array 需要排序的数组
	 */
	public static void sortSelect(int[] array) {
		sortSelect(array, true);
	}

	/**
	 * 选择排序
	 * 
	 * @param array 需要排序的数组
	 * @param asc 升序还是降序,默认true升序
	 */
	public static void sortSelect(int[] array, boolean asc) {
		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (asc) {
					if (array[i] > array[j]) {
						ArrayTool.swap(array, i, j);
					}
				} else {
					if (array[i] < array[j]) {
						ArrayTool.swap(array, i, j);
					}
				}
			}
		}
	}

	/**
	 * 冒泡排序,默认升序
	 * 
	 * @param array 数组
	 */
	public static void sortBubble(int[] array) {
		sortBubble(array, true);
	}

	/**
	 * 冒泡排序
	 * 
	 * @param array 数组
	 */
	public static void sortBubble(int[] array, boolean asc) {
		for (int i = 1; i < array.length; i++) {
			for (int j = 0; j < array.length - i; j++) {
				if (asc) {
					if (array[j] > array[j + 1]) {
						ArrayTool.swap(array, j, j + 1);
					}
				} else {
					if (array[j] < array[j + 1]) {
						ArrayTool.swap(array, j, j + 1);
					}
				}
			}
		}
	}

	/**
	 * 精确的减法运算
	 * 
	 * @param arg1 被减数
	 * @param arg2 减数
	 * @return arg1减去arg2的差
	 */
	public static BigDecimal subtract(double arg1, double arg2) {
		return new BigDecimal(arg1).subtract(new BigDecimal(arg2));
	}

	/**
	 * 数字类型避免精度缺失的减法运算,但仍会有精度丢失,第一个值一直减后面的值
	 * 
	 * @param numbers 数组
	 * @return 减法运算的结果
	 */
	public static BigDecimal subtract(Number... numbers) {
		if (ArrayTool.isEmpty(numbers)) {
			return BigDecimal.ZERO;
		}
		Number value = numbers[0];
		BigDecimal result = null == value ? BigDecimal.ZERO : new BigDecimal(value.toString());
		for (int i = 1; i < numbers.length; i++) {
			result = null == numbers[i] ? result : result.subtract(new BigDecimal(numbers[i].toString()));
		}
		return result;
	}

	/**
	 * Object对象转BigDecimal.如果Object为空或不是数值型对象,返回0;为数值型,转为BigDecimal
	 * 
	 * @param obj 要转换的Object对象
	 * @return BigDecimal
	 */
	public static BigDecimal toBigDecimal(Object obj) {
		return toBigDecimal(obj, BigDecimal.ZERO);
	}

	/**
	 * Object对象转BigDecimal.如果Object为空或不是数值型对象,抛异常;为数值型,转为BigDecimal
	 * 
	 * @param obj 要转换的Object对象
	 * @return BigDecimal
	 */
	public static BigDecimal toBigDecimal(Object obj, BigDecimal defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return new BigDecimal(obj.toString());
	}

	/**
	 * 将对象强行转换为byte,该对象必须是一个整数字符串,null返回0
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static byte toByte(Object obj) {
		return toByte(obj, (byte) 0);
	}

	/**
	 * 将对象强行转换为byte,若该对象不可转为整数,则返回默认值
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static byte toByte(Object obj, byte defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return Byte.parseByte(Optional.ofNullable(obj).get().toString());
	}

	/**
	 * 将对象强行转换为double,该对象必须是一个整数或小数字符串,null返回0
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Double toDouble(Object obj) {
		return toDouble(obj, 0);
	}

	/**
	 * 将一个对象强行转换为Double类型,若该对象不可转为整数或小数,则返回默认值
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Double toDouble(Object obj, double defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return Double.parseDouble(Optional.ofNullable(obj).get().toString());
	}

	/**
	 * 将对象强行转换为float类型,该对象必须是一个整数或小数字符串,null返回0
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Float toFloat(Object obj) {
		return toFloat(obj, 0f);
	}

	/**
	 * 将对象强行转换为float,若该对象不可转为整数或小数,则返回默认值
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Float toFloat(Object obj, float defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return Float.parseFloat(Optional.ofNullable(obj).get().toString());
	}

	/**
	 * 将一个对象强行转换为int类型,该对象必须是一个整数字符串,null返回0
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Integer toInt(Object obj) {
		return toInt(obj, 0);
	}

	/**
	 * 将一个对象强行转换为int类型,若该对象不可转为整数,则返回默认值
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Integer toInt(Object obj, int defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return Integer.parseInt(Optional.ofNullable(obj).get().toString());
	}

	/**
	 * 将一个对象强行转换为long类型,该对象必须是一个整数字符串,null返回0
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的long类型值
	 */
	public static Long toLong(Object obj) {
		return toLong(obj, 0l);
	}

	/**
	 * 将一个对象强行转换为long类型,若该对象不可转为整数,则返回默认值
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的long类型值
	 */
	public static Long toLong(Object obj, long defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return Long.parseLong(Optional.ofNullable(obj).get().toString());
	}

	/**
	 * 将对象转换为Number
	 * 
	 * @param obj 对象实例
	 * @return {@link Number}
	 */
	public static Number toNumber(Object obj) {
		if (obj != null) {
			if (obj instanceof Number) {
				return (Number) obj;
			}
			if (obj instanceof String) {
				try {
					return NumberFormat.getInstance().parse((String) obj);
				} catch (final ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 将对象强行转换为short,该对象必须是一个整数字符串,null返回0
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Short toShort(Object obj) {
		return toShort(obj, (short) 0);
	}

	/**
	 * 将对象强行转换为short,若该对象不可转为整数,则返回默认值
	 * 
	 * @param obj 需要进行强转的对象,可以是object,string等
	 * @return 强转后的值
	 */
	public static Short toShort(Object obj, short defaultValue) {
		if (!isDigits(obj)) {
			return defaultValue;
		}
		return Short.parseShort(Optional.ofNullable(obj).get().toString());
	}
}