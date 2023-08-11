package com.wy.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Object工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 23:00:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ObjectTool {

	/**
	 * 克隆对象
	 * 
	 * @param <T> 泛型
	 * @param obj 泛型实例
	 * @return 新泛型实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(T obj) {
		if (obj instanceof Cloneable) {
			final Object result;
			if (obj.getClass().isArray()) {
				final Class<?> componentType = obj.getClass().getComponentType();
				if (componentType.isPrimitive()) {
					int length = Array.getLength(obj);
					result = Array.newInstance(componentType, length);
					while (length-- > 0) {
						Array.set(result, length, Array.get(obj, length));
					}
				} else {
					result = ((Object[]) obj).clone();
				}
			} else {
				try {
					Method clone = obj.getClass().getMethod("clone");
					result = clone.invoke(obj);
					return (T) result;
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 比较2个对象是否==或equals
	 * 
	 * @param o1 对象1
	 * @param o2 对象2
	 * @return true当2个对象==或equals
	 */
	public static boolean equals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		if (o1.equals(o2)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取对象 HashCode(十六进制形式字符串).参数为 null 时,返回 0
	 * 
	 * @param obj 参数
	 * @return 对象 HashCode
	 */
	public static String getIdentityHexString(Object obj) {
		return Integer.toHexString(System.identityHashCode(obj));
	}

	/**
	 * 当t为null时返回defaultValue
	 * 
	 * @param <T> 泛型
	 * @param t 需要进行判断的参数
	 * @param defaultValue 默认值
	 * @return 结果
	 */
	public static <T> T getNullDefault(T t, T defaultValue) {
		return Optional.ofNullable(t).orElse(defaultValue);
	}

	/**
	 * 当t为null时返回defaultValue;若默认值为null,抛异常
	 * 
	 * @param <T> 泛型
	 * @param t 需要判断的值
	 * @param defaultValue 默认值
	 * @return 返回的值或异常
	 * @throws NullPointerException
	 */
	public static <T> T getNullDefaultException(T t, T defaultValue) {
		return Optional.ofNullable(t)
				.orElseGet(() -> Optional.ofNullable(defaultValue).orElseThrow(() -> new NullPointerException()));
	}

	/**
	 * 获得参数值,若参数不为null,直接返回;若为null,抛异常
	 * 
	 * @param <T> 泛型
	 * @param t 参数
	 * @return 有值返回,若为null,抛空指针异常
	 * @throws NullPointerException
	 */
	public static <T> T getNullException(T t) {
		return Optional.ofNullable(t).orElseThrow(() -> new NullPointerException());
	}

	/**
	 * 判断对象为null或其他为null的情况
	 * 
	 * @param obj 对象
	 * @return true当对象为null或其他为null的情况
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof Optional) {
			return !((Optional<?>) obj).isPresent();
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length() == 0;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).isEmpty();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		return false;
	}

	/**
	 * 判断对象不为null或其他不为null的情况
	 * 
	 * @param obj 对象
	 * @return true当对象不为null或其他不为null的情况
	 */
	public static boolean isNotEmpty(final Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 判断参数是否为null
	 * 
	 * @param <T> 泛型
	 * @param t 参数
	 * @return 若为null,返回true
	 */
	public static <T> boolean isNull(T t) {
		return !Optional.ofNullable(t).isPresent();
	}

	/**
	 * 判断数组中所有的值都为null
	 * 
	 * @param arrays 数组
	 * @return true当所有值都为null
	 */
	public static boolean isNull(Object... arrays) {
		for (Object array : arrays) {
			if (null != array) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断对象不为null
	 * 
	 * @param obj 对象
	 * @return true当对象不为null
	 */
	public static boolean nonNull(Object obj) {
		return !isNull(obj);
	}

	/**
	 * 判断数组中所有的值都不是null
	 * 
	 * @param values 数组
	 * @return true当数组中所有值都不为null
	 */
	public static boolean nonNull(Object... values) {
		if (values == null || values.length == 0) {
			return false;
		}
		for (final Object val : values) {
			if (val == null) {
				return false;
			}
		}
		return true;
	}
}