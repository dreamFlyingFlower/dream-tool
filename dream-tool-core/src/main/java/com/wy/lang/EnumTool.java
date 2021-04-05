package com.wy.lang;

/**
 * Enum工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-02 20:43:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class EnumTool {

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
	public static Enum<?> getByName(Class<? extends Enum<?>> enumClass, String name) {
		return getByName(enumClass, name, false);
	}

	/**
	 * 返回Enum中和name()相等的待查找值
	 * 
	 * @param enumClass 枚举class
	 * @param name 待查找的枚举中的name()的值
	 * @param caseSensitive 大小写敏感,true敏感
	 * @return Enum中和name()相等的值,若没有,返回null
	 */
	public static Enum<?> getByName(Class<? extends Enum<?>> enumClass, String name, boolean caseSensitive) {
		for (Enum<?> enumValue : enumClass.getEnumConstants()) {
			return caseSensitive ? enumValue.name().equals(name) ? enumValue : null
					: enumValue.name().equalsIgnoreCase(name) ? enumValue : null;
		}
		return null;
	}
}