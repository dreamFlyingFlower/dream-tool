package com.wy.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-02 20:43:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class EnumTool {

	private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";

	private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

	/**
	 * 判断Enum数组含有和toString()相等的待查找值,大小写不敏感
	 * 
	 * @param enums 枚举数组
	 * @param str 待查找的枚举中的toString()的值
	 * @return true当枚举中含有待查找的str
	 */
	public static boolean contains(Enum<?>[] enums, String str) {
		return contains(enums, str, false);
	}

	/**
	 * 判断Enum数组含有和toString()相等的待查找值
	 * 
	 * @param enums 枚举数组
	 * @param str 待查找的枚举中的toString()的值
	 * @param caseSensitive 大小写敏感,true敏感
	 * @return true当枚举中含有待查找的str
	 */
	public static boolean contains(Enum<?>[] enums, String str, boolean caseSensitive) {
		for (Enum<?> candidate : enums) {
			if (caseSensitive ? candidate.toString().equals(str) : candidate.toString().equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断Enum含有和toString()相等的待查找值,大小写不敏感
	 * 
	 * @param enumClass 枚举class
	 * @param str 待查找的枚举中的toString()的值
	 * @return true当枚举中含有待查找的str
	 */
	public static boolean contains(Class<? extends Enum<?>> enumClass, String str) {
		return contains(enumClass, str, false);
	}

	/**
	 * 判断Enum含有和toString()相等的待查找值
	 * 
	 * @param enumClass 枚举class
	 * @param str 待查找的枚举中的toString()的值
	 * @param caseSensitive 大小写敏感,true敏感
	 * @return true当枚举中含有待查找的str
	 */
	public static boolean contains(Class<? extends Enum<?>> enumClass, String str, boolean caseSensitive) {
		for (Enum<?> enumValue : enumClass.getEnumConstants()) {
			if (caseSensitive ? enumValue.toString().equals(str) : enumValue.toString().equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断Enum数组含有和name()相等的待查找值,大小写不敏感
	 * 
	 * @param enums 枚举数组
	 * @param name 待查找的枚举中的name()的值
	 * @return true当枚举中含有待查找的name
	 */
	public static boolean containsName(Enum<?>[] enums, String name) {
		return containsName(enums, name, false);
	}

	/**
	 * 判断Enum数组含有和name()相等的待查找值
	 * 
	 * @param enums 枚举数组
	 * @param name 待查找的枚举中的name()的值
	 * @param caseSensitive 大小写敏感,true敏感
	 * @return true当枚举中含有待查找的name
	 */
	public static boolean containsName(Enum<?>[] enums, String name, boolean caseSensitive) {
		for (Enum<?> candidate : enums) {
			if (caseSensitive ? candidate.name().equals(name) : candidate.name().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断Enum含有和name()相等的待查找值,大小写不敏感
	 * 
	 * @param enumClass 枚举class
	 * @param name 待查找的枚举中的toString()的值
	 * @return true当枚举中含有待查找的str
	 */
	public static boolean containsName(Class<? extends Enum<?>> enumClass, String name) {
		return containsName(enumClass, name, false);
	}

	/**
	 * 判断Enum含有和name()相等的待查找值
	 * 
	 * @param enumClass 枚举class
	 * @param name 待查找的枚举中的name()的值
	 * @param caseSensitive 大小写敏感,true敏感
	 * @return true当枚举中含有待查找的str
	 */
	public static boolean containsName(Class<? extends Enum<?>> enumClass, String name, boolean caseSensitive) {
		for (Enum<?> enumValue : enumClass.getEnumConstants()) {
			if (caseSensitive ? enumValue.name().equals(name) : enumValue.name().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回Enum中和name()相等的待查找值,大小写不敏感
	 * 
	 * @param enumClass 枚举class
	 * @param name 待查找的枚举中的name()的值
	 * @return Enum中和name()相等的值,若没有,返回null
	 */
	public static <E extends Enum<E>> E getByName(final Class<E> enumClass, String enumName) {
		return getByName(enumClass, enumName, false);
	}

	/**
	 * 返回Enum中和name()相等的待查找值
	 * 
	 * @param enumClass 枚举class
	 * @param name 待查找的枚举中的name()的值
	 * @param caseSensitive 大小写敏感,true敏感
	 * @return Enum中和name()相等的值,若没有,返回null
	 */
	public static <E extends Enum<E>> E getByName(Class<E> enumClass, String name, boolean caseSensitive) {
		for (E enumValue : enumClass.getEnumConstants()) {
			if (caseSensitive && enumValue.name().equals(name)) {
				return enumValue;
			} else if (!caseSensitive && enumValue.name().equalsIgnoreCase(name)) {
				return enumValue;
			}
		}
		return null;
	}

	/**
	 * 将枚举中的值转换为列表
	 * 
	 * @param <E>
	 * @param enumClass 枚举类
	 * @return 枚举列表
	 */
	public static <E extends Enum<E>> List<E> toList(final Class<E> enumClass) {
		return enumClass.isEnum() ? new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()))
				: Collections.emptyList();
	}

	/**
	 * 将枚举转换为Map,其中name()为key,枚举项为value
	 * 
	 * @param <E> 枚举
	 * @param enumClass 枚举类
	 * @return 枚举Map
	 */
	public static <E extends Enum<E>> Map<String, E> toMap(final Class<E> enumClass) {
		AssertTool.isTrue(enumClass.isEnum(), "the class is not a enum");
		final Map<String, E> map = new LinkedHashMap<>();
		for (final E e : enumClass.getEnumConstants()) {
			map.put(e.name(), e);
		}
		return map;
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