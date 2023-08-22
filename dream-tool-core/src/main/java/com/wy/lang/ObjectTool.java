package com.wy.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
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
		return Objects.equals(o1, o2);
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
	public static <T> T defaultIfNull(T t, T defaultValue) {
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
	public static <T> T defaultIfNullException(T t, T defaultValue) {
		return Optional.ofNullable(t)
				.orElseGet(() -> Optional.ofNullable(defaultValue).orElseThrow(() -> new NullPointerException()));
	}

	/**
	 * 若参数为null,抛出异常,否则直接返回
	 * 
	 * @param <T> 泛型
	 * @param t 参数
	 * @return 有值返回,若为null,抛空指针异常
	 * @throws NullPointerException
	 */
	public static <T> T requireNonNull(T t) {
		return Objects.requireNonNull(t);
	}

	/**
	 * 若参数为null,抛出自定义异常消息,否则直接返回
	 * 
	 * @param <T> 泛型
	 * @param t 参数
	 * @param message 异常消息
	 * @return 有值返回,若为null,抛空指针异常
	 * @throws NullPointerException
	 */
	public static <T> T requireNonNull(T t, String message) {
		if (t == null) {
			throw new NullPointerException(message);
		}
		return t;
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

	/**
	 * <p>
	 * Null safe comparison of Comparables.
	 * </p>
	 *
	 * @param <T> type of the values processed by this method
	 * @param values the set of comparable values, may be null
	 * @return
	 *         <ul>
	 *         <li>If any objects are non-null and unequal, the greater object.
	 *         <li>If all objects are non-null and equal, the first.
	 *         <li>If any of the comparables are null, the greater of the non-null
	 *         objects.
	 *         <li>If all the comparables are null, null is returned.
	 *         </ul>
	 */
	@SafeVarargs
	public static <T extends Comparable<? super T>> T max(final T... values) {
		T result = null;
		if (values != null) {
			for (final T value : values) {
				if (compare(value, result, false) > 0) {
					result = value;
				}
			}
		}
		return result;
	}

	/**
	 * <p>
	 * Null safe comparison of Comparables.
	 * </p>
	 *
	 * @param <T> type of the values processed by this method
	 * @param c1 the first comparable, may be null
	 * @param c2 the second comparable, may be null
	 * @param nullGreater if true {@code null} is considered greater than a
	 *        non-{@code null} value or if false {@code null} is considered less
	 *        than a Non-{@code null} value
	 * @return a negative value if c1 &lt; c2, zero if c1 = c2 and a positive value
	 *         if c1 &gt; c2
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(final T c1, final T c2, final boolean nullGreater) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return nullGreater ? 1 : -1;
		} else if (c2 == null) {
			return nullGreater ? -1 : 1;
		}
		return c1.compareTo(c2);
	}
}