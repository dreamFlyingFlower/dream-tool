package com.dream.lang;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import com.dream.collection.CollectionHelper;
import com.dream.collection.MapHelper;
import com.dream.helper.ArrayHelper;

/**
 * Assert工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 23:54:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AssertHelper {

	/**
	 * 判断superType是subType的父类或上级接口
	 * 
	 * @param superType 父类或上级接口
	 * @param subType 子类,可以和父类相同
	 * @throws IllegalArgumentException 当superType不是subType的父类或上级接口
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	/**
	 * 判断superType是subType的父类或上级接口
	 * 
	 * @param superType 父类或上级接口
	 * @param subType 子类,可以和父类相同
	 * @param message 提示信息,根据结尾不同而不同 {@link #endsWithSeparator(String)}
	 * @throws IllegalArgumentException 当superType不是subType的父类或上级接口
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
		notNull(superType, "Super type must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, message);
		}
	}

	/**
	 * 判断superType是subType的父类或上级接口
	 * 
	 * @param superType 父类或上级接口
	 * @param subType 子类,可以和父类相同
	 * @param messageSupplier 提示信息
	 * @throws IllegalArgumentException 当superType不是subType的父类或上级接口
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, Supplier<String> messageSupplier) {
		notNull(superType, "Super type must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, nullSafeGet(messageSupplier));
		}
	}

	/**
	 * 判断superType是subType的父类或上级接口
	 * 
	 * @param superType 父类或上级接口
	 * @param subType 子类,可以和父类相同
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @throws IllegalArgumentException 当superType不是subType的父类或上级接口
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType, String format, Object... args) {
		notNull(superType, "Super type must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			assignableCheckFailed(superType, subType, String.format(format, args));
		}
	}

	/**
	 * 组装失败信息
	 * 
	 * @param superType 父类或上级接口
	 * @param subType 子类
	 * @param msg 提示信息
	 */
	private static void assignableCheckFailed(Class<?> superType, Class<?> subType, String msg) {
		String result = "";
		boolean defaultMessage = true;
		if (StrHelper.hasLength(msg)) {
			if (endsWithSeparator(msg)) {
				result = msg + " ";
			} else {
				result = messageWithTypeName(msg, subType);
				defaultMessage = false;
			}
		}
		if (defaultMessage) {
			result = result + (subType + " is not assignable to " + superType);
		}
		throw new IllegalArgumentException(result);
	}

	/**
	 * 判断字符串以冒号,分号,逗号,点结尾
	 * 
	 * @param msg 字符串
	 * @return true->当字符串以冒号,分号,逗号,点结尾
	 */
	private static boolean endsWithSeparator(String msg) {
		return (msg.endsWith(":") || msg.endsWith(";") || msg.endsWith(",") || msg.endsWith("."));
	}

	/**
	 * 当msg以单个空格结尾时返回{@code msg+typeName},否则返回{@code msg+: +typeName}
	 * 
	 * @param msg 字符串
	 * @param typeName 提示信息字节码
	 * @return 提示信息
	 */
	private static String messageWithTypeName(String msg, Object typeName) {
		return msg + (msg.endsWith(" ") ? "" : ": ") + typeName;
	}

	/**
	 * 判断字符序列为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @throws IllegalArgumentException 当字符序列不为null,"",换行符,制表符," "
	 */
	public static void isBlank(CharSequence charSequence) {
		if (StrHelper.isNotBlank(charSequence)) {
			throw new IllegalArgumentException("[Assertion failed] - the parameter should by blank");
		}
	}

	/**
	 * 判断字符序列为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @param message 提示信息
	 * @throws IllegalArgumentException 当字符序列不为null,"",换行符,制表符," "
	 */
	public static void isBlank(CharSequence charSequence, String message) {
		if (StrHelper.isNotBlank(charSequence)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 判断字符序列为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @param messageSupplier 提示信息
	 * @throws IllegalArgumentException 当字符序列不为null,"",换行符,制表符," "
	 */
	public static void isBlank(CharSequence charSequence, Supplier<String> messageSupplier) {
		if (StrHelper.isNotBlank(charSequence)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * 判断字符序列为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @throws IllegalArgumentException 当字符序列不为null,"",换行符,制表符," "
	 */
	public static void isBlank(CharSequence charSequence, String format, Object... args) {
		if (StrHelper.isNotBlank(charSequence)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
	}

	/**
	 * 判断字符序列为null,""
	 * 
	 * @param charSequence 字符序列
	 * @throws IllegalArgumentException 当字符序列不为null,""
	 */
	public static void isEmpty(CharSequence charSequence) {
		if (StrHelper.isNotEmpty(charSequence)) {
			throw new IllegalArgumentException("[Assertion failed] - charsequence should be null or empty");
		}
	}

	/**
	 * 判断字符序列为null,""
	 * 
	 * @param charSequence 字符序列
	 * @param message 提示信息
	 * @throws IllegalArgumentException 当字符序列不为null,""
	 */
	public static void isEmpty(CharSequence charSequence, String message) {
		if (StrHelper.isNotEmpty(charSequence)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 判断字符序列为null,""
	 * 
	 * @param charSequence 字符序列
	 * @param messageSupplier 提示信息
	 * @throws IllegalArgumentException 当字符序列不为null,""
	 */
	public static void isEmpty(CharSequence charSequence, Supplier<String> messageSupplier) {
		if (StrHelper.isNotEmpty(charSequence)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * 判断字符序列为null,""
	 * 
	 * @param charSequence 字符序列
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @throws IllegalArgumentException 当字符序列不为null,""
	 */
	public static void isEmpty(CharSequence charSequence, String format, Object... args) {
		if (StrHelper.isNotEmpty(charSequence)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
	}

	/**
	 * 判断数组为null或长度为0,返回空对象数组
	 * 
	 * @param array 数组
	 * @return 空对象数组
	 * @throws IllegalArgumentException 当数组不为null或长度大于0
	 */
	public static Object[] isEmpty(Object[] array) {
		if (ArrayHelper.isNotEmpty(array)) {
			throw new IllegalArgumentException("[Assertion failed] - the arrays should be empty");
		}
		return new Object[0];
	}

	/**
	 * 判断数组为null或长度为0,返回空对象数组
	 * 
	 * @param array 数组
	 * @param message 提示信息
	 * @return 空对象数组
	 * @throws IllegalArgumentException 当数组不为null或长度大于0
	 */
	public static Object[] isEmpty(Object[] array, String message) {
		if (ArrayHelper.isNotEmpty(array)) {
			throw new IllegalArgumentException(message);
		}
		return new Object[0];
	}

	/**
	 * 判断数组为null或长度为0,返回空对象数组
	 * 
	 * @param array 数组
	 * @param messageSupplier 提示信息
	 * @return 空对象数组
	 * @throws IllegalArgumentException 当数组不为null或长度大于0
	 */
	public static Object[] isEmpty(Object[] array, Supplier<String> messageSupplier) {
		if (ArrayHelper.isNotEmpty(array)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return new Object[0];
	}

	/**
	 * 判断数组为null或长度为0,返回空对象数组
	 * 
	 * @param array 数组
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 空对象数组
	 * @throws IllegalArgumentException 当数组不为null或长度大于0
	 */
	public static Object[] isEmpty(Object[] array, String format, Object... args) {
		if (ArrayHelper.isNotEmpty(array)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return new Object[0];
	}

	/**
	 * 判断对象为null
	 * 
	 * @param object 对象
	 * @throws IllegalArgumentException 当对象不为null
	 */
	public static void isNull(Object object) {
		if (object != null) {
			throw new IllegalArgumentException("[Assertion failed] - the parameter should be null");
		}
	}

	/**
	 * 判断对象为null
	 * 
	 * @param object 对象
	 * @param message 提示信息
	 * @throws IllegalArgumentException 当对象不为null
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 判断对象为null
	 * 
	 * @param object 对象
	 * @param messageSupplier 提示信息
	 * @throws IllegalArgumentException 当对象不为null
	 */
	public static void isNull(Object object, Supplier<String> messageSupplier) {
		if (object != null) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * 判断对象为null
	 * 
	 * @param object 对象
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @throws IllegalArgumentException 当对象不为null
	 */
	public static void isNull(Object object, String format, Object... args) {
		if (object != null) {
			throw new IllegalArgumentException(String.format(format, args));
		}
	}

	/**
	 * 判断对象是一个数,返回该数
	 * 
	 * @param object 对象
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isNumber(Object object) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException("[Assertion failed] - the is not a number");
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个数,返回该数
	 * 
	 * @param object 对象
	 * @param message 提示信息
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isNumber(Object object, String message) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(message);
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个数,返回该数
	 * 
	 * @param object 对象
	 * @param messageSupplier 提示信息
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isNumber(Object object, Supplier<String> messageSupplier) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个数,返回该数
	 * 
	 * @param object 对象
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isNumber(Object object, String format, Object... args) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是类的实例
	 * 
	 * @param clazz 类
	 * @param obj 对象
	 * @return true->是,false->否
	 * @throws IllegalArgumentException 当对象不是类的实例
	 */
	public static boolean isInstanceOf(Class<?> clazz, Object obj) {
		return isInstanceOf(clazz, obj, "");
	}

	/**
	 * 判断对象是类的实例
	 * 
	 * @param clazz 类
	 * @param obj 对象
	 * @param message 提示信息,根据结尾不同而不同 {@link #endsWithSeparator(String)}
	 * @return true->是,false->否
	 * @throws IllegalArgumentException 当对象不是类的实例
	 */
	public static boolean isInstanceOf(Class<?> clazz, Object obj, String message) {
		notNull(clazz, "Class to check must not be null");
		if (!clazz.isInstance(obj)) {
			instanceCheckFailed(clazz, obj, message);
		}
		return true;
	}

	/**
	 * 判断对象是类的实例
	 * 
	 * @param clazz 类
	 * @param obj 对象
	 * @param messageSupplier 提示信息
	 * @return true->是,false->否
	 * @throws IllegalArgumentException 当对象不是类的实例
	 */
	public static boolean isInstanceOf(Class<?> clazz, Object obj, Supplier<String> messageSupplier) {
		notNull(clazz, "Class to check must not be null");
		if (!clazz.isInstance(obj)) {
			instanceCheckFailed(clazz, obj, nullSafeGet(messageSupplier));
		}
		return true;
	}

	/**
	 * 判断对象是类的实例
	 * 
	 * @param clazz 类
	 * @param obj 对象
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return true->是,false->否
	 * @throws IllegalArgumentException 当对象不是类的实例
	 */
	public static boolean isInstanceOf(Class<?> clazz, Object obj, String format, Object... args) {
		notNull(clazz, "Class to check must not be null");
		if (!clazz.isInstance(obj)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return true;
	}

	private static void instanceCheckFailed(Class<?> clazz, Object obj, String msg) {
		String className = (obj != null ? obj.getClass().getName() : "null");
		String result = "";
		boolean defaultMessage = true;
		if (StrHelper.hasLength(msg)) {
			if (endsWithSeparator(msg)) {
				result = msg + " ";
			} else {
				result = messageWithTypeName(msg, className);
				defaultMessage = false;
			}
		}
		if (defaultMessage) {
			result = result + ("Object of class [" + className + "] must be an instance of " + clazz);
		}
		throw new IllegalArgumentException(result);
	}

	/**
	 * 判断对象是一个正数,返回该数
	 * 
	 * @param object 对象
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveNumber(Object object) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException("[Assertion failed] - the object is not a number");
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException("the object is not a positive number");
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正数,返回该数
	 * 
	 * @param object 对象
	 * @param message 提示信息
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveNumber(Object object, String message) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(message);
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException(message);
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正数,返回该数
	 * 
	 * @param object 对象
	 * @param messageSupplier 提示信息
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveNumber(Object object, Supplier<String> messageSupplier) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正数,返回该数
	 * 
	 * @param object 对象
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveNumber(Object object, String format, Object... args) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正整数,返回该数
	 * 
	 * @param object 对象
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveInteger(Object object) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException("[Assertion failed] - the object is not a number");
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException("the object is not a positive number");
		}
		if (!NumberHelper.isDigits(object)) {
			throw new IllegalArgumentException("the object is not a integral number");
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正整数,返回该数
	 * 
	 * @param object 对象
	 * @param message 提示信息
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveInteger(Object object, String message) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(message);
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException(message);
		}
		if (!NumberHelper.isDigits(object)) {
			throw new IllegalArgumentException(message);
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正整数,返回该数
	 * 
	 * @param object 对象
	 * @param messageSupplier 提示信息
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveInteger(Object object, Supplier<String> messageSupplier) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		if (!NumberHelper.isDigits(object)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断对象是一个正整数,返回该数
	 * 
	 * @param object 对象
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return Number
	 * @throws IllegalArgumentException 当对象不是一个数
	 */
	public static Number isPositiveInteger(Object object, String format, Object... args) {
		if (!NumberHelper.isNumber(notNull(object))) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		if (Double.parseDouble(object.toString()) <= 0) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		if (!NumberHelper.isDigits(object)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return NumberHelper.toNumber(object);
	}

	/**
	 * 判断表达式为true
	 * 
	 * @param expression boolean表达式
	 * @return true->true
	 * @throws IllegalArgumentException 当boolean表达式为false
	 */
	public static boolean isTrue(boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException("[Assertion failed] - this expression must be true");
		}
		return true;
	}

	/**
	 * 判断表达式为true
	 * 
	 * @param expression boolean表达式
	 * @param message 提示信息
	 * @return true->true
	 * @throws IllegalArgumentException 当boolean表达式为false
	 */
	public static boolean isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
		return true;
	}

	/**
	 * 判断表达式为true
	 * 
	 * @param expression boolean表达式
	 * @param messageSupplier 提示信息
	 * @return true->true
	 * @throws IllegalArgumentException 当boolean表达式为false
	 */
	public static boolean isTrue(boolean expression, Supplier<String> messageSupplier) {
		if (!expression) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return true;
	}

	/**
	 * 判断表达式为true
	 * 
	 * @param expression boolean表达式
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return true->true
	 * @throws IllegalArgumentException 当boolean表达式为false
	 */
	public static boolean isTrue(boolean expression, String format, Object... args) {
		if (!expression) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return true;
	}

	/**
	 * 判断字符序列不为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,"",换行符,制表符," "
	 */
	public static CharSequence notBlank(CharSequence charSequence) {
		if (StrHelper.isBlank(charSequence)) {
			throw new IllegalArgumentException("[Assertion failed] - the parameter can't be blank");
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @param message 提示信息
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,"",换行符,制表符," "
	 */
	public static CharSequence notBlank(CharSequence charSequence, String message) {
		if (StrHelper.isBlank(charSequence)) {
			throw new IllegalArgumentException(message);
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @param messageSupplier 提示信息
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,"",换行符,制表符," "
	 */
	public static CharSequence notBlank(CharSequence charSequence, Supplier<String> messageSupplier) {
		if (StrHelper.isBlank(charSequence)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,"",换行符,制表符," "
	 * 
	 * @param charSequence 字符序列
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,"",换行符,制表符," "
	 */
	public static CharSequence notBlank(CharSequence charSequence, String format, Object... args) {
		if (StrHelper.isBlank(charSequence)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,""
	 * 
	 * @param charSequence 字符序列
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,""
	 */
	public static CharSequence notEmpty(CharSequence charSequence) {
		if (StrHelper.isEmpty(charSequence)) {
			throw new IllegalArgumentException("[Assertion failed] - charSequence can't be null or empty");
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,""
	 * 
	 * @param charSequence 字符序列
	 * @param message 提示信息
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,""
	 */
	public static CharSequence notEmpty(CharSequence charSequence, String message) {
		if (StrHelper.isEmpty(charSequence)) {
			throw new IllegalArgumentException(message);
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,""
	 * 
	 * @param charSequence 字符序列
	 * @param messageSupplier 提示信息
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,""
	 */
	public static CharSequence notEmpty(CharSequence charSequence, Supplier<String> messageSupplier) {
		if (StrHelper.isEmpty(charSequence)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return charSequence;
	}

	/**
	 * 判断字符序列不为null,""
	 * 
	 * @param charSequence 字符序列
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源字符序列
	 * @throws IllegalArgumentException 当字符序列为null,""
	 */
	public static CharSequence notEmpty(CharSequence charSequence, String format, Object... args) {
		if (StrHelper.isEmpty(charSequence)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return charSequence;
	}

	/**
	 * 判断集合不为null或长度为0
	 * 
	 * @param collection 集合
	 * @return 源集合
	 * @throws IllegalArgumentException 当字符序列为null或长度为0
	 */
	public static Collection<?> notEmpty(Collection<?> collection) {
		if (CollectionHelper.isEmpty(collection)) {
			throw new IllegalArgumentException("[Assertion failed] - the collection can't be null or empty");
		}
		return collection;
	}

	/**
	 * 判断集合不为null或长度为0
	 * 
	 * @param collection 集合
	 * @param message 提示信息
	 * @return 源集合
	 * @throws IllegalArgumentException 当字符序列为null或长度为0
	 */
	public static Collection<?> notEmpty(Collection<?> collection, String message) {
		if (CollectionHelper.isEmpty(collection)) {
			throw new IllegalArgumentException(message);
		}
		return collection;
	}

	/**
	 * 判断集合不为null或长度为0
	 * 
	 * @param collection 集合
	 * @param messageSupplier 提示信息
	 * @return 源集合
	 * @throws IllegalArgumentException 当字符序列为null或长度为0
	 */
	public static Collection<?> notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
		if (CollectionHelper.isEmpty(collection)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return collection;
	}

	/**
	 * 判断集合不为null或长度为0
	 * 
	 * @param collection 集合
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源集合
	 * @throws IllegalArgumentException 当字符序列为null或长度为0
	 */
	public static Collection<?> notEmpty(Collection<?> collection, String format, Object... args) {
		if (CollectionHelper.isEmpty(collection)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return collection;
	}

	/**
	 * 判断Map不为null或长度为0
	 * 
	 * @param object 对象
	 * @return 源Map
	 * @throws IllegalArgumentException 当Map为null或长度为0
	 */
	public static Map<?, ?> notEmpty(Map<?, ?> map) {
		if (MapHelper.isEmpty(map)) {
			throw new IllegalArgumentException("[Assertion failed] - the Map can't be null or empty");
		}
		return map;
	}

	/**
	 * 判断Map不为null或长度为0
	 * 
	 * @param map Map实例
	 * @param message 提示信息
	 * @return 源Map
	 * @throws IllegalArgumentException 当Map为null或长度为0
	 */
	public static Map<?, ?> notEmpty(Map<?, ?> map, String message) {
		if (MapHelper.isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}
		return map;
	}

	/**
	 * 判断Map不为null或长度为0
	 * 
	 * @param map Map实例
	 * @param messageSupplier 提示信息
	 * @return 源Map
	 * @throws IllegalArgumentException 当Map为null或长度为0
	 */
	public static Map<?, ?> notEmpty(Map<?, ?> map, Supplier<String> messageSupplier) {
		if (MapHelper.isEmpty(map)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return map;
	}

	/**
	 * 判断Map不为null或长度为0
	 * 
	 * @param map Map实例
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源Map
	 * @throws IllegalArgumentException 当Map为null或长度为0
	 */
	public static Map<?, ?> notEmpty(Map<?, ?> map, String format, Object... args) {
		if (MapHelper.isEmpty(map)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return map;
	}

	/**
	 * 判断数组不为null或长度为0
	 * 
	 * @param arrays 数组
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组为null或长度为0
	 */
	public static Object[] notEmpty(Object[] arrays) {
		if (ArrayHelper.isEmpty(arrays)) {
			throw new IllegalArgumentException("[Assertion failed] - the array can't be null or empty");
		}
		return arrays;
	}

	/**
	 * 判断数组不为null或长度为0
	 * 
	 * @param arrays 数组
	 * @param message 提示信息
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组为null或长度为0
	 */
	public static Object[] notEmpty(Object[] arrays, String message) {
		if (ArrayHelper.isEmpty(arrays)) {
			throw new IllegalArgumentException(message);
		}
		return arrays;
	}

	/**
	 * 判断数组不为null或长度为0
	 * 
	 * @param arrays 数组
	 * @param messageSupplier 提示信息
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组为null或长度为0
	 */
	public static Object[] notEmpty(Object[] arrays, Supplier<String> messageSupplier) {
		if (ArrayHelper.isEmpty(arrays)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return arrays;
	}

	/**
	 * 判断数组不为null或长度为0
	 * 
	 * @param arrays 数组
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组为null或长度为0
	 */
	public static Object[] notEmpty(Object[] arrays, String format, Object... args) {
		if (ArrayHelper.isEmpty(arrays)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return arrays;
	}

	/**
	 * 判断对象不为null
	 * 
	 * @param <T> 泛型
	 * @param t 对象
	 * @return 源对象
	 * @throws IllegalArgumentException 当对象为null
	 */
	public static <T> T notNull(T t) {
		if (t == null) {
			throw new IllegalArgumentException("[Assertion failed] - the parameter can't be null");
		}
		return t;
	}

	/**
	 * 判断对象不为null
	 * 
	 * @param <T> 泛型
	 * @param t 对象
	 * @param message 提示信息
	 * @return 源对象
	 * @throws IllegalArgumentException 当对象为null
	 */
	public static <T> T notNull(T t, String message) {
		if (t == null) {
			throw new IllegalArgumentException(message);
		}
		return t;
	}

	/**
	 * 判断对象不为null
	 * 
	 * @param <T> 泛型
	 * @param t 对象
	 * @param messageSupplier 提示信息
	 * @return 源对象
	 * @throws IllegalArgumentException 当对象为null
	 */
	public static <T> T notNull(T t, Supplier<String> messageSupplier) {
		if (t == null) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
		return t;
	}

	/**
	 * 判断对象不为null
	 * 
	 * @param <T> 泛型
	 * @param t 对象
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源对象
	 * @throws IllegalArgumentException 当对象为null
	 */
	public static <T> T notNull(T t, String format, Object... args) {
		if (t == null) {
			throw new IllegalArgumentException(String.format(format, args));
		}
		return t;
	}

	/**
	 * 判断数组中无null元素
	 * 
	 * @param array 数组
	 * @param message 提示信息
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组中有null元素
	 */
	public static Object[] noNullElements(Object[] array) {
		notEmpty(array);
		for (Object element : array) {
			if (element == null) {
				throw new IllegalArgumentException("[Assertion failed] - the array has null elementl");
			}
		}
		return array;
	}

	/**
	 * 判断数组中无null元素
	 * 
	 * @param array 数组
	 * @param message 提示信息
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组中有null元素
	 */
	public static Object[] noNullElements(Object[] array, String message) {
		notEmpty(array);
		for (Object element : array) {
			if (element == null) {
				throw new IllegalArgumentException(message);
			}
		}
		return array;
	}

	/**
	 * 判断数组中无null元素
	 * 
	 * @param array 数组
	 * @param messageSupplier 提示信息
	 * @return 源数组
	 * @throws IllegalArgumentException 当数组中有null元素
	 */
	public static Object[] noNullElements(Object[] array, Supplier<String> messageSupplier) {
		notEmpty(array);
		for (Object element : array) {
			if (element == null) {
				throw new IllegalArgumentException(nullSafeGet(messageSupplier));
			}
		}
		return array;
	}

	/**
	 * 判断数组中无null元素
	 * 
	 * @param array 数组
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源数组
	 * @throws IllegalArgumentException 当对象为null
	 */
	public static Object[] noNullElements(Object[] array, String format, Object... args) {
		notEmpty(array);
		for (Object element : array) {
			if (element == null) {
				throw new IllegalArgumentException(String.format(format, args));
			}
		}
		return array;
	}

	/**
	 * 判断集合中无null元素
	 * 
	 * @param collection 集合
	 * @return 源集合
	 * @throws IllegalArgumentException 当集合中有null元素
	 */
	public static Collection<?> noNullElements(Collection<?> collection) {
		notEmpty(collection);
		for (Object element : collection) {
			if (element == null) {
				throw new IllegalArgumentException("[Assertion failed] - the collection has null element");
			}
		}
		return collection;
	}

	/**
	 * 判断集合中无null元素
	 * 
	 * @param collection 集合
	 * @param message 提示信息
	 * @return 源集合
	 * @throws IllegalArgumentException 当集合中有null元素
	 */
	public static Collection<?> noNullElements(Collection<?> collection, String message) {
		notEmpty(collection);
		for (Object element : collection) {
			if (element == null) {
				throw new IllegalArgumentException(message);
			}
		}
		return collection;
	}

	/**
	 * 判断集合中无null元素
	 * 
	 * @param collection 集合
	 * @param messageSupplier 提示信息
	 * @return 源集合
	 * @throws IllegalArgumentException 当集合中有null元素
	 */
	public static Collection<?> noNullElements(Collection<?> collection, Supplier<String> messageSupplier) {
		notEmpty(collection);
		for (Object element : collection) {
			if (element == null) {
				throw new IllegalArgumentException(nullSafeGet(messageSupplier));
			}
		}
		return collection;
	}

	/**
	 * 判断集合中无null元素
	 * 
	 * @param collection 集合
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @return 源集合
	 * @throws IllegalArgumentException 当集合中有null元素
	 */
	public static Collection<?> noNullElements(Collection<?> collection, String format, Object... args) {
		notEmpty(collection);
		for (Object element : collection) {
			if (element == null) {
				throw new IllegalArgumentException(String.format(format, args));
			}
		}
		return collection;
	}

	/**
	 * 判断str不包含searchStr
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @throws IllegalArgumentException 当str包含searchStr
	 */
	public static void notContain(String str, String searchStr) {
		if (StrHelper.isNotEmpty(str) && StrHelper.isNotEmpty(searchStr) && str.contains(searchStr)) {
			throw new IllegalArgumentException("[Assertion failed] - the str contain the serarchStr");
		}
	}

	/**
	 * 判断str不包含searchStr
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param message 提示信息
	 * @throws IllegalArgumentException 当str包含searchStr
	 */
	public static void notContain(String str, String searchStr, String message) {
		if (StrHelper.isNotEmpty(str) && StrHelper.isNotEmpty(searchStr) && str.contains(searchStr)) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 判断str不包含searchStr
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param messageSupplier 提示信息
	 * @throws IllegalArgumentException 当str包含searchStr
	 */
	public static void notContain(String str, String searchStr, Supplier<String> messageSupplier) {
		if (StrHelper.isNotEmpty(str) && StrHelper.isNotEmpty(searchStr) && str.contains(searchStr)) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * 判断str不包含searchStr
	 * 
	 * @param str 源字符串
	 * @param searchStr 待查找字符串
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @throws IllegalArgumentException 当str包含searchStr
	 */
	public static void notContain(String str, String searchStr, String format, Object... args) {
		if (StrHelper.isNotEmpty(str) && StrHelper.isNotEmpty(searchStr) && str.contains(searchStr)) {
			throw new IllegalArgumentException(String.format(format, args));
		}
	}

	/**
	 * 判断表达式为true,主要判断某些特定状态
	 * 
	 * @param expression boolean表达式
	 * @throws IllegalStateException 当boolean表达式为false
	 */
	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}

	/**
	 * 判断表达式为true,主要判断某些特定状态
	 * 
	 * @param expression boolean表达式
	 * @param message 状态错误提示信息
	 * @throws IllegalStateException 当boolean表达式为false
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * 判断表达式为true,主要判断某些特定状态
	 * 
	 * @param expression boolean表达式
	 * @param messageSupplier 状态错误提示信息
	 * @throws IllegalStateException 当boolean表达式为false
	 */
	public static void state(boolean expression, Supplier<String> messageSupplier) {
		if (!expression) {
			throw new IllegalStateException(nullSafeGet(messageSupplier));
		}
	}

	/**
	 * 判断表达式为true,主要判断某些特定状态
	 * 
	 * @param expression boolean表达式
	 * @param format 格式化信息
	 * @param args 格式化信息参数
	 * @throws IllegalStateException 当boolean表达式为false
	 */
	public static void state(boolean expression, String format, Object... args) {
		if (!expression) {
			throw new IllegalStateException(String.format(format, args));
		}
	}

	/**
	 * 返回Supplier自定义的提示信息
	 * 
	 * @param messageSupplier 接口实例
	 * @return 提示信息
	 */
	private static String nullSafeGet(Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}
}