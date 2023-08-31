package com.wy.primitive;

import java.util.Optional;

/**
 * Boolean工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-24 14:24:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class BooleanHelper {

	/**
	 * 判断给定对象是否是一个boolean
	 * 
	 * @param object 给定对象
	 * @return true->是boolean或boolean子类
	 */
	public static boolean isBoolean(Object object) {
		Optional<Object> optional = Optional.ofNullable(object);
		return optional.isPresent()
				? boolean.class.isAssignableFrom(object.getClass()) || Boolean.class.isAssignableFrom(object.getClass())
				: false;
	}

	/**
	 * 将对象转为boolean,除了true和"true"之外全部返回false
	 * 
	 * @param object 对象
	 * @return boolean值
	 */
	public static boolean toBoolean(Object object) {
		Optional<Object> optional = Optional.ofNullable(object);
		return optional.isPresent() ? Boolean.valueOf(optional.get().toString()) : false;
	}

	/**
	 * 将对象转换为boolean,null转默认值
	 * 
	 * @param object 对象
	 * @param defaultValue 默认值
	 * @return boolean值
	 */
	public static Boolean toBool(Object object, boolean defaultValue) {
		return Optional.ofNullable(object).isPresent() ? Boolean.valueOf(String.valueOf(object)) : defaultValue;
	}

	/**
	 * 将boolean转换为0或1,false对应0,true对应1
	 * 
	 * @param bool 待转换的值
	 * @return 转换后的值
	 */
	public static int toInt(Boolean bool) {
		return Optional.ofNullable(bool).isPresent() ? bool.booleanValue() ? 1 : 0 : 0;
	}

	/**
	 * 将boolean转换为特定字符串
	 *
	 * @param bool 待转换的值
	 * @param trueValue 当bool为true时返回的字符串
	 * @param falseValue 当bool为false时返回的字符串
	 * @return 转换后的值
	 */
	public static String toString(Boolean bool, String trueValue, String falseValue) {
		return Optional.ofNullable(bool).isPresent() ? bool.booleanValue() ? trueValue : falseValue : falseValue;
	}

	/**
	 * 将boolean转换为是否
	 * 
	 * @param bool 待转换的值
	 * @return 转换后的值
	 */
	public static String toChinese(Boolean bool) {
		return toString(bool, "是", "否");
	}

	/**
	 * 将boolean转换为on,off
	 * 
	 * @param bool 待转换的值
	 * @return 转换后的值
	 */
	public static String toOnOff(Boolean bool) {
		return toString(bool, "ON", "OFF");
	}

	/**
	 * 将boolean转换为yes,no
	 * 
	 * @param bool 待转换的值
	 * @return 转换后的值
	 */
	public static String toYesNo(Boolean bool) {
		return toString(bool, "YES", "NO");
	}
}