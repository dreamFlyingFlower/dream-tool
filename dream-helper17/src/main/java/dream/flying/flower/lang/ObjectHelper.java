package dream.flying.flower.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Supplier;

import dream.flying.flower.ConstArray;
import dream.flying.flower.helper.ArrayHelper;

/**
 * Object工具类,可使用 Objects
 * 
 * @author 飞花梦影
 * @date 2021-02-18 23:00:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ObjectHelper {

	/**
	 * 将给定对象追加到给定数组,返回由输入数组内容和给定对象组成的新数组
	 * 
	 * @param array 要追加到的数组,可能为null
	 * @param obj 追加的对象
	 * @return 新数组
	 */
	public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
		Class<?> compType = Object.class;
		if (array != null) {
			compType = array.getClass().getComponentType();
		} else if (obj != null) {
			compType = obj.getClass();
		}
		int newArrLength = (array != null ? array.length + 1 : 1);
		@SuppressWarnings("unchecked")
		A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
		if (array != null) {
			System.arraycopy(array, 0, newArr, 0, array.length);
		}
		newArr[newArr.length - 1] = obj;
		return newArr;
	}

	/**
	 * 将给定的数组进行比较,使用 Arrays.equals()进行比较,根据数组元素而不是数组引用执行相等性检查
	 * 
	 * @param o1 数组
	 * @param o2 数组
	 * @return true->equals;false->不equals
	 */
	private static boolean arrayEquals(Object o1, Object o2) {
		if (o1 instanceof Object[] && o2 instanceof Object[]) {
			return Arrays.equals((Object[]) o1, (Object[]) o2);
		}
		if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
			return Arrays.equals((boolean[]) o1, (boolean[]) o2);
		}
		if (o1 instanceof byte[] && o2 instanceof byte[]) {
			return Arrays.equals((byte[]) o1, (byte[]) o2);
		}
		if (o1 instanceof char[] && o2 instanceof char[]) {
			return Arrays.equals((char[]) o1, (char[]) o2);
		}
		if (o1 instanceof double[] && o2 instanceof double[]) {
			return Arrays.equals((double[]) o1, (double[]) o2);
		}
		if (o1 instanceof float[] && o2 instanceof float[]) {
			return Arrays.equals((float[]) o1, (float[]) o2);
		}
		if (o1 instanceof int[] && o2 instanceof int[]) {
			return Arrays.equals((int[]) o1, (int[]) o2);
		}
		if (o1 instanceof long[] && o2 instanceof long[]) {
			return Arrays.equals((long[]) o1, (long[]) o2);
		}
		if (o1 instanceof short[] && o2 instanceof short[]) {
			return Arrays.equals((short[]) o1, (short[]) o2);
		}
		return false;
	}

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
	 * 可比null的安全比较
	 *
	 * @param <T> 泛型
	 * @param c1 需要比较的对象,可为null
	 * @param c2 需要比较的对象,可为null
	 * @param nullGreater null是否更大.如果为true,null比非null更大;false->null比非null小
	 * @return c1小于c2返回负数;相等返回0;c1大于c2返回正数
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

	/**
	 * 检查给定的枚举常量数组是否包含具有给定名称的常量,在确定匹配时忽略大小写
	 *
	 * @param enumValues 要检查的枚举值
	 * @param constant 要查找的常量名称,不得为 null 或空字符串
	 * @return 是否在给定枚举中找到常量
	 */
	public static boolean contains(Enum<?>[] enumValues, String constant) {
		return contains(enumValues, constant, false);
	}

	/**
	 * 检查给定的枚举常量数组是否包含具有给定名称的常量
	 * 
	 * @param enumValues 要检查的枚举值
	 * @param constant 要查找的常量名称,不得为 null 或空字符串
	 * @param caseSensitive 是否忽略大小写
	 * @return 是否在给定枚举中找到常量
	 */
	public static boolean contains(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
		for (Enum<?> candidate : enumValues) {
			if (caseSensitive ? candidate.toString().equals(constant)
					: candidate.toString().equalsIgnoreCase(constant)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查给定数组是否包含给定元素
	 * 
	 * @param array 要检查的数组.如果是null,返回false
	 * @param element 待检查的元素
	 * @return 是否已在给定数组中找到该元素
	 */
	public static boolean contains(Object[] array, Object element) {
		if (array == null) {
			return false;
		}
		for (Object arrayEle : array) {
			if (equals(arrayEle, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果对象非空直接返回,否则调用Supplier
	 *
	 * @param <T> 泛型
	 * @param object 待检查的对象
	 * @param defaultSupplier 默认Supplier
	 * @return 原对象或其他默认值
	 */
	public static <T> T defaultIfNull(final T object, final Supplier<T> defaultSupplier) {
		return object != null ? object : defaultSupplier == null ? null : defaultSupplier.get();
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
	 * 确定给定的对象是否相等.如果两者都是null返回true,只有一个为null返回false
	 * 
	 * @param o1 待比较的对象
	 * @param o2 待比较的对象
	 * @return 给定对象是否相等
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
		if (o1.getClass().isArray() && o2.getClass().isArray()) {
			return arrayEquals(o1, o2);
		}
		return false;
	}

	/**
	 * 返回数组中第一个不是null的值.如果所有值均为null或数组为null或为空,则返回null
	 *
	 * @param <T> 泛型
	 * @param values 待查找的数组,可能为null
	 * @return 第一个非null的值,可能为null
	 */
	@SafeVarargs
	public static <T> T firstNonNull(final T... values) {
		if (values != null) {
			for (final T val : values) {
				if (val != null) {
					return val;
				}
			}
		}
		return null;
	}

	/**
	 * 按顺序执行Supplier并返回第一个返回非null的值,则其他Supplier不再执行.如果全部返回null或未提供Supplier返回空
	 *
	 * @param <T> 泛型
	 * @param suppliers 待调用的Supplier数组
	 * @return 第一个非null的值
	 */
	@SafeVarargs
	public static <T> T getFirstNonNull(final Supplier<T>... suppliers) {
		if (suppliers != null) {
			for (final Supplier<T> supplier : suppliers) {
				if (supplier != null) {
					final T value = supplier.get();
					if (value != null) {
						return value;
					}
				}
			}
		}
		return null;
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
	 * 判断对象是否为数组,null返回false
	 * 
	 * @param obj 待检查对象
	 * @return true->是数组;false->不是数组
	 */
	public static boolean isArray(Object obj) {
		return (obj != null && obj.getClass().isArray());
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
	 * 获得最大值
	 *
	 * @param <T> 泛型
	 * @param values 待比较的数组,可能为null
	 * @return 如果所有对象非null且不等,则返回最大对象;如果所有对象都非空且相等,则为第一个;
	 *         如果存在null,则返回非null中的最大对象;如果都为null,则返回null
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
	 * 在可比对象中找到最佳猜测中间值,如果总值为偶数,则将返回两个中间值中较小的一个
	 * 
	 * @param <T> 泛型
	 * @param comparator 比较器
	 * @param items 待比较的数组或集合
	 * @return 中间位置的元素
	 * @throws NullPointerException if items or comparator is {@code null}
	 * @throws IllegalArgumentException if items is empty or contains {@code null}
	 *         values
	 */
	@SafeVarargs
	public static <T> T median(final Comparator<T> comparator, final T... items) {
		AssertHelper.notEmpty(items, "null/empty items");
		AssertHelper.noNullElements(items);
		AssertHelper.notNull(comparator, "comparator");
		final TreeSet<T> sort = new TreeSet<>(comparator);
		Collections.addAll(sort, items);
		@SuppressWarnings("unchecked")
		final T result = (T) sort.toArray()[(sort.size() - 1) / 2];
		return result;
	}

	/**
	 * 在可比对象中找到最佳猜测中间值,如果总值为偶数,则将返回两个中间值中较小的一个
	 * 
	 * @param <T> 泛型
	 * @param items 待比较的数组或集合
	 * @return 中间位置的元素
	 * @throws NullPointerException if items is {@code null}
	 * @throws IllegalArgumentException if items is empty or contains {@code null}
	 *         values
	 */
	@SafeVarargs
	public static <T extends Comparable<? super T>> T median(final T... items) {
		AssertHelper.notEmpty(items);
		AssertHelper.noNullElements(items);
		final TreeSet<T> sort = new TreeSet<>();
		Collections.addAll(sort, items);
		@SuppressWarnings("unchecked")
		final T result = (T) sort.toArray()[(sort.size() - 1) / 2];
		return result;
	}

	/**
	 * 获得最小值
	 *
	 * @param <T> 泛型
	 * @param values 待比较的数组,可能为null
	 * @return 如果所有对象非null且不等,则返回最小对象;如果所有对象都非空且相等,则为第一个;
	 *         如果存在null,则返回非null中的的最小对象;都为null,则返回空值
	 */
	@SafeVarargs
	public static <T extends Comparable<? super T>> T min(final T... values) {
		T result = null;
		if (values != null) {
			for (final T value : values) {
				if (compare(value, result, true) < 0) {
					result = value;
				}
			}
		}
		return result;
	}

	/**
	 * 查找最常出现的元素
	 *
	 * @param <T> 泛型
	 * @param items 待检查的数组
	 * @return 填充最多的元素,可能为null
	 */
	@SafeVarargs
	public static <T> T mode(final T... items) {
		if (ArrayHelper.isNotEmpty(items)) {
			final HashMap<T, Integer> occurrences = new HashMap<>(items.length);
			for (final T t : items) {
				Integer count = occurrences.get(t);
				if (count == null) {
					occurrences.put(t, 1);
				} else {
					count++;
				}
			}
			T result = null;
			int max = 0;
			for (final Map.Entry<T, Integer> e : occurrences.entrySet()) {
				final int cmp = e.getValue().intValue();
				if (cmp == max) {
					result = null;
				} else if (cmp > max) {
					max = cmp;
					result = e.getKey();
				}
			}
			return result;
		}
		return null;
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
	 * 将给定的数组(可能是基类数组)转换为对象数组.null被转为空对象数组
	 * 
	 * @param source 数组,非数组抛异常
	 * @return 相应的对象数组
	 * @throws IllegalArgumentException if the parameter is not an array
	 */
	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[]) source;
		}
		if (source == null) {
			return ConstArray.EMPTY_OBJECT;
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return ConstArray.EMPTY_OBJECT;
		}
		Class<?> wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}

	/**
	 * 获取对象的toString,如果对象为null,则返回指定的字符串
	 * 
	 * 不带参数和带默认值的可调用{@link Objects#toString()},{@link Objects#toString(Object, String)}
	 *
	 * @param obj 对象,可能为null
	 * @param supplier 当obj为null时提供的方法
	 * @return 字符串结果
	 */
	public static String toString(final Object obj, final Supplier<String> supplier) {
		return obj == null ? supplier == null ? null : supplier.get() : obj.toString();
	}
}