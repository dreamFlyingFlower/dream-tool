package com.wy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wy.lang.AssertTool;

/**
 * Enum工具类,参照{@link org.apache.commons.lang3.EnumUtils}
 *
 * @author 飞花梦影
 * @date 2022-01-27 09:30:32
 */
public class EnumTool {

	private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";

	private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

	public EnumTool() {
	}

	/**
	 * 获得枚举中所有属性的集合
	 *
	 * @param <E> 泛型
	 * @param enumClass 枚举类型,非空
	 * @return 枚举属性集合
	 */
	public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumClass) {
		return new ArrayList<>(Arrays.asList(isEnum(enumClass).getEnumConstants()));
	}

	/**
	 * 将枚举转换为Map:k->name(),value->enum
	 *
	 * @param <E> 泛型
	 * @param enumClass 枚举类型,非空
	 * @return 枚举name-enum的map
	 */
	public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass) {
		isEnum(enumClass);
		final Map<String, E> map = new LinkedHashMap<>();
		for (final E e : enumClass.getEnumConstants()) {
			map.put(e.name(), e);
		}
		return map;
	}

	/**
	 * 获得枚举中所有属性name集合
	 *
	 * @param <E> 泛型
	 * @param enumClass 枚举类型,非空
	 * @return 枚举属性集合
	 */
	public static <E extends Enum<E>> List<String> getEnumNameList(final Class<E> enumClass) {
		return Stream.of(isEnum(enumClass).getEnumConstants()).map(t -> t.name()).collect(Collectors.toList());
	}

	/**
	 * 校验指定name是否是枚举中的属性,不同于原生的valueOf,该方法不抛异常
	 *
	 * @param <E> 泛型
	 * @param enumClass 枚举类型,非空
	 * @param enumName 枚举name值
	 * @return true->name值是该枚举属性;false->该name不是枚举属性
	 */
	public static <E extends Enum<E>> boolean isValidName(final Class<E> enumClass, final String enumName) {
		if (enumName == null) {
			return false;
		}
		try {
			Enum.valueOf(enumClass, enumName);
			return true;
		} catch (final IllegalArgumentException ex) {
			return false;
		}
	}

	/**
	 * 根据name属性查找枚举值,未找到返回null,不抛异常
	 *
	 * @param <E> 泛型
	 * @param enumClass 枚举类型,非空
	 * @param enumName 枚举name值
	 * @return 枚举值
	 */
	public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName) {
		if (enumName == null) {
			return null;
		}
		try {
			return Enum.valueOf(enumClass, enumName);
		} catch (final IllegalArgumentException ex) {
			return null;
		}
	}

	/**
	 * 校验是否为枚举class
	 * 
	 * @param <E> 泛型
	 * @param enumClass 枚举类型,非空
	 * @return 枚举class
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	private static <E extends Enum<E>> Class<E> isEnum(final Class<E> enumClass) {
		AssertTool.notNull(enumClass, ENUM_CLASS_MUST_BE_DEFINED);
		AssertTool.isTrue(enumClass.isEnum(), S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE, enumClass);
		return enumClass;
	}
}