package dream.flying.flower.helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Stream;

import dream.flying.flower.ConstArray;
import dream.flying.flower.ConstString;
import dream.flying.flower.collection.ListHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * Array工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 22:39:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ArrayHelper {

	private ArrayHelper() {
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static boolean[] append(final boolean[] array, final boolean element) {
		boolean[] result = new boolean[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static byte[] append(final byte[] array, final byte element) {
		byte[] result = new byte[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static char[] append(final char[] array, final char element) {
		char[] result = new char[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static double[] append(final double[] array, final double element) {
		double[] result = new double[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static float[] append(final float[] array, final float element) {
		float[] result = new float[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static int[] append(final int[] array, final int element) {
		int[] result = new int[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static long[] append(final long[] array, final long element) {
		long[] result = new long[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param array 原数组
	 * @param element 新的元素
	 * @return 新的数组
	 */
	public static short[] append(final short[] array, final short element) {
		short[] result = new short[array.length + 1];
		System.arraycopy(array, 0, result, 0, array.length);
		result[result.length - 1] = element;
		return result;
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param <T> 泛型
	 * @param array 原数组
	 * @param element 新元素
	 * @return 新的数组
	 */
	public static <T> T[] append(T[] array, T element) {
		return appendAssignable(array, element);
	}

	/**
	 * 在新数组末尾添加一个元素,原数组不改变,返回一个新的数组
	 * 
	 * @param <P> 父类泛型
	 * @param <C> 子类泛型
	 * @param array 原数组
	 * @param element 新元素
	 * @return 新的数组
	 */
	public static <P, C extends P> P[] appendAssignable(P[] array, C element) {
		if (element == null) {
			return array;
		}
		Class<?> compType = null;
		if (array != null) {
			compType = array.getClass().getComponentType();
		} else {
			compType = element.getClass();
		}
		int newArrLength = (array != null ? array.length + 1 : 1);
		@SuppressWarnings("unchecked")
		P[] newArr = (P[]) Array.newInstance(compType, newArrLength);
		if (array != null) {
			System.arraycopy(array, 0, newArr, 0, array.length);
		}
		newArr[newArr.length - 1] = element;
		return newArr;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Boolean> asList(boolean... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Boolean> list = new ArrayList<>();
		for (boolean i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Byte> asList(byte... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Byte> list = new ArrayList<>();
		for (byte i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Character> asList(char... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Character> list = new ArrayList<>();
		for (char i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Float> asList(float... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Float> list = new ArrayList<>();
		for (float i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Double> asList(double... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Double> list = new ArrayList<>();
		for (double i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Integer> asList(int... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Integer> list = new ArrayList<>();
		for (int i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Long> asList(long... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Long> list = new ArrayList<>();
		for (long i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 将数组转换为包装类集合
	 * 
	 * @param array 数组
	 * @return 集合
	 */
	public static List<Short> asList(short... array) {
		if (array.length == 0) {
			return Collections.emptyList();
		}
		List<Short> list = new ArrayList<>();
		for (short i : array) {
			list.add(i);
		}
		return list;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(boolean[] array, boolean target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(byte[] array, byte target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(char[] array, char target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素.慎用,超过一定精准度将出现误差
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(final double[] array, final double target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素.慎用,超过一定精准度将出现误差
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(final float[] array, final float target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(int[] array, int target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(long[] array, long target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(Object[] array, Object target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断数组中是否包含某个元素
	 * 
	 * @param array 源数组
	 * @param target 待查找元素
	 * @return true当源数组中存在待查找元素
	 */
	public static boolean contains(short[] array, short target) {
		return indexOf(array, target) > ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(boolean[] array, boolean value) {
		return indexOf(array, value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(boolean[] array, boolean value, int start) {
		if (isNotEmpty(array)) {
			start = start < 0 ? 0 : start;
			for (int i = start; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(byte[] array, byte value) {
		return indexOf(array, (byte) value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(byte[] array, byte value, int start) {
		if (isNotEmpty(array)) {
			start = start < 0 ? 0 : start;
			for (int i = start; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(char[] array, char value) {
		return indexOf(array, value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(char[] array, char value, int start) {
		if (isNotEmpty(array)) {
			start = start < 0 ? 0 : start;
			for (int i = start; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final float[] array, final float value) {
		return indexOf(array, value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final float[] array, final float value, int start) {
		if (isEmpty(array)) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < array.length; i++) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final double[] array, final double value) {
		return indexOf(array, value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final double[] array, final double value, int start) {
		if (isEmpty(array)) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			start = 0;
		}
		for (int i = start; i < array.length; i++) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(int[] array, int value) {
		if (isNotEmpty(array)) {
			for (int i = 0; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final int[] array, final int value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		start = start < 0 ? 0 : start;
		for (int i = start; i < array.length; i++) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(long[] array, long value) {
		return indexOf(array, value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final long[] array, final long value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		start = start < 0 ? 0 : start;
		for (int i = start; i < array.length; i++) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final Object[] array, final Object value) {
		return indexOf(array, value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final Object[] array, final Object value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		start = start < 0 ? 0 : start;
		if (value == null) {
			for (int i = start; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		} else {
			for (int i = start; i < array.length; i++) {
				if (value.equals(array[i])) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(short[] array, short value) {
		return indexOf(array, (short) value, 0);
	}

	/**
	 * 返回数组中指定元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从起始位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int indexOf(final short[] array, final short value, int start) {
		if (isNotEmpty(array)) {
			start = start < 0 ? 0 : start;
			for (int i = start; i < array.length; i++) {
				if (value == array[i]) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 判断对象是数组
	 * 
	 * @param obj 对象
	 * @return true当对象是数组
	 */
	public static boolean isArray(Object obj) {
		return (obj != null && obj.getClass().isArray());
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final boolean[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final byte[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final char[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final double[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final float[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final int[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final long[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final Object[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组为null或长度为0
	 */
	public static boolean isEmpty(final short[] array) {
		return length(array) == 0;
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @returntrue当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final char[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final double[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final float[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final int[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final long[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static boolean isNotEmpty(final short[] array) {
		return !isEmpty(array);
	}

	/**
	 * 检查数组是否为null或长度为0,不判断数组中的元素
	 *
	 * @param <T> 泛型
	 * @param array 数组
	 * @return true当数组不为null或长度大于0
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return !isEmpty(array);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final byte[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final byte[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final char[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final char[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final double[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final double[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator).append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final float[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final float[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return null;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator).append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final int[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final int[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator).append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final long[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final long[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator).append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final Object[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 *
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(String separator, final Object[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		separator = StrHelper.toString(separator);
		final int noOfItems = end - start;
		if (noOfItems <= 0 || start >= array.length) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		if (array[start] != null) {
			buf.append(array[start]);
		}
		for (int i = start + 1; i < end; i++) {
			buf.append(separator);
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final short[] array) {
		return join(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final short[] array, final int start, final int end) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		final StringBuilder buf = new StringBuilder(noOfItems * 16);
		buf.append(array[start]);
		for (int i = start + 1; i < end; i++) {
			buf.append(separator);
			buf.append(array[i]);
		}
		return buf.toString();
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @return 拼接后的字符串
	 * @since JDK1.8
	 */
	public static String join8(final String separator, final double[] array) {
		return join8(separator, array, 0, array.length);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 * @since JDK1.8
	 */
	public static String join8(final String separator, final double[] array, final int start, final int end) {
		return join8(separator, array, start, end, ConstString.EMPTY, ConstString.EMPTY);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 * 
	 * @param separator 间隔符
	 * @param array 数组
	 * @param start 起始索引
	 * @param end 结束索引
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 拼接后的字符串
	 * @since JDK1.8
	 */
	public static String join8(final String separator, final double[] array, final int start, final int end,
			String prefix, String suffix) {
		if (isEmpty(array)) {
			return ConstString.EMPTY;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstString.EMPTY;
		}
		StringJoiner stringJoiner = new StringJoiner(separator, prefix, suffix);
		for (int i = start; i < end; i++) {
			stringJoiner.add(String.valueOf(array[i]));
		}
		return stringJoiner.toString();
	}

	/**
	 * 返回数组长度,null返回0
	 *
	 * @param array 数组
	 * @return 数组长度
	 * @throws IllegalArgumentException 若是非数组,抛异常
	 */
	public static int length(final Object array) {
		return array == null ? 0 : Array.getLength(array);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final boolean[] array, final boolean value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final boolean[] array, final boolean value, int start) {
		if (isEmpty(array)) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final byte[] array, final byte value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final byte[] array, final byte value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final char[] array, final char valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final char[] array, final char value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final double[] array, final double value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final double[] array, final double value, int start) {
		if (isEmpty(array)) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final float[] array, final float value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1.慎用,超过一定精准度将出现误差
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final float[] array, final float value, int start) {
		if (isEmpty(array)) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final int[] array, final int value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final int[] array, final int value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final long[] array, final long valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final long[] array, final long value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final Object[] array, final Object value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final Object[] array, final Object value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		if (value == null) {
			for (int i = start; i >= 0; i--) {
				if (array[i] == null) {
					return i;
				}
			}
		} else if (array.getClass().getComponentType().isInstance(value)) {
			for (int i = start; i >= 0; i--) {
				if (value.equals(array[i])) {
					return i;
				}
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final short[] array, final short value) {
		return lastIndexOf(array, value, Integer.MAX_VALUE);
	}

	/**
	 * 返回数组末尾开始搜索待查找元素第一次出现的索引,未找到返回-1
	 *
	 * @param array 数组
	 * @param value 待查找的元素
	 * @param start 从末尾位置开始查找的索引,默认从0
	 * @return 数组中指定元素所在位置,未找到返回-1
	 */
	public static int lastIndexOf(final short[] array, final short value, int start) {
		if (array == null) {
			return ConstArray.INDEX_NOT_FOUND;
		}
		if (start < 0) {
			return ConstArray.INDEX_NOT_FOUND;
		} else if (start >= array.length) {
			start = array.length - 1;
		}
		for (int i = start; i >= 0; i--) {
			if (value == array[i]) {
				return i;
			}
		}
		return ConstArray.INDEX_NOT_FOUND;
	}

	/**
	 * 将2个boolean数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的boolean数组
	 */
	public static boolean[] merge(boolean[]... arrays) {
		int length = 0;
		for (boolean[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		boolean[] result = new boolean[length];
		int pos = 0;
		for (boolean[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个byte数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的byte数组
	 */
	public static byte[] merge(byte[]... arrays) {
		int length = 0;
		for (byte[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		byte[] result = new byte[length];
		int pos = 0;
		for (byte[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个char数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的char数组
	 */
	public static char[] merge(char[]... arrays) {
		int length = 0;
		for (char[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		char[] result = new char[length];
		int pos = 0;
		for (char[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个double数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的double数组
	 */
	public static double[] merge(double[]... arrays) {
		int length = 0;
		for (double[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		double[] result = new double[length];
		int pos = 0;
		for (double[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个float数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的float数组
	 */
	public static float[] merge(float[]... arrays) {
		int length = 0;
		for (float[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		float[] result = new float[length];
		int pos = 0;
		for (float[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个int数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的int数组
	 */
	public static int[] merge(int[]... arrays) {
		int length = 0;
		for (int[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		int[] result = new int[length];
		int pos = 0;
		for (int[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个long数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的long数组
	 */
	public static long[] merge(long[]... arrays) {
		int length = 0;
		for (long[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		long[] result = new long[length];
		int pos = 0;
		for (long[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个short数组进行合并
	 * 
	 * @param arrays 数组集合
	 * @return 合并后的short数组
	 */
	public static short[] merge(short[]... arrays) {
		int length = 0;
		for (short[] array : arrays) {
			if (isNotEmpty(array)) {
				length += array.length;
			}
		}
		short[] result = new short[length];
		int pos = 0;
		for (short[] array : arrays) {
			if (isNotEmpty(array)) {
				System.arraycopy(array, 0, result, pos, array.length);
				pos += array.length;
			}
		}
		return result;
	}

	/**
	 * 将2个对象数组进行合并
	 * 
	 * @param array1 数组1
	 * @param array2 数组2
	 * @return 合并后的对象数组
	 */
	public static <T> T[] merge(T[] array1, T[] array2) {
		if (array1 == null) {
			return array2;
		} else if (array2 == null) {
			return array1;
		}
		final Class<?> type1 = array1.getClass().getComponentType();
		@SuppressWarnings("unchecked")
		final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try {
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		} catch (final ArrayStoreException ase) {
			final Class<?> type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2)) {
				throw new IllegalArgumentException(
						"Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
			}
			throw ase;
		}
		return joinedArray;
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static boolean[] remove(final boolean[] array, final int index) {
		return (boolean[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static boolean[] remove(final boolean[] array, final boolean element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static byte[] remove(final byte[] array, final int index) {
		return (byte[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static byte[] remove(final byte[] array, final byte element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static char[] remove(final char[] array, final int index) {
		return (char[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static char[] remove(final char[] array, final char element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static double[] remove(final double[] array, final int index) {
		return (double[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static double[] remove(final double[] array, final double element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static float[] remove(final float[] array, final int index) {
		return (float[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static float[] remove(final float[] array, final float element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static int[] remove(final int[] array, final int index) {
		return (int[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static int[] remove(final int element, final int[] array) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static long[] remove(final long[] array, final int index) {
		return (long[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static long[] remove(final long[] array, final long element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	public static short[] remove(final short[] array, final int index) {
		return (short[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中待删除元素
	 * 
	 * @param array 数组
	 * @param element 待删除元素
	 * @return 删除元素后的新数组
	 */
	public static short[] remove(final short[] array, final short element) {
		return remove(array, indexOf(array, element));
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param <T> 泛型
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(final T[] array, final int index) {
		return (T[]) remove((Object) array, index);
	}

	/**
	 * 删除数组中指定索引下的元素
	 * 
	 * @param array 数组
	 * @param index 索引
	 * @return 删除元素后的新数组
	 */
	private static Object remove(final Object array, final int index) {
		if (null == array) {
			return null;
		}
		final int length = length(array);
		if (index < 0 || index >= length) {
			return array;
		}
		final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}
		return result;
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final boolean[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final boolean[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		boolean tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final byte[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final byte[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final char[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final char[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		char tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final double[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final double[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		double tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final float[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final float[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		float tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final int[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final int[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final long[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final long[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		long tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final Object[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final Object[] array, final int startInclusive, final int endExclusive) {
		if (array == null) {
			return;
		}
		int i = Math.max(startInclusive, 0);
		int j = Math.min(array.length, endExclusive) - 1;
		Object tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 */
	public static void reverse(final short[] array) {
		reverse(array, 0, array.length);
	}

	/**
	 * 反转数组中的元素顺序
	 *
	 * @param array 数组
	 * @param start 起始索引.若小于0,默认从0开始;若大于数组长度,不做任何处理
	 * @param end 结束索引.若小于起始值,不做任何处理;若大于数组长度,默认为数组长度
	 */
	public static void reverse(final short[] array, final int start, final int end) {
		if (array == null) {
			return;
		}
		int i = Math.max(start, 0);
		int j = Math.min(array.length, end) - 1;
		short tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final boolean[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final boolean[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final byte[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final byte[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final char[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final char[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final double[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final double[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final float[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final float[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final int[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final int[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final long[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final long[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final Object[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final Object[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 */
	public static void shuffle(final short[] array) {
		shuffle(array, new Random());
	}

	/**
	 * 重排数组中元素的顺序
	 * 
	 * @param array 数组
	 * @param random 随机数
	 */
	public static void shuffle(final short[] array, final Random random) {
		for (int i = array.length; i > 1; i--) {
			swap(array, i - 1, random.nextInt(i), 1);
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final boolean[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final boolean[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final boolean aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final byte[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final byte[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final byte aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final char[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final char[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final char aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final double[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final double[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final double aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final float[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final float[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final float aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}

	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final int[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final int[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final int aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final long[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final long[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final long aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final Object[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final Object[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final Object aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 */
	public static void swap(final short[] array, final int offset1, final int offset2) {
		swap(array, offset1, offset2, 1);
	}

	/**
	 * 将数组中两个元素的位置交换
	 *
	 * @param array 数组
	 * @param offset1 第一个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param offset2 第二个交换的元素索引,小于0默认为0,超过数组长度不进行任何操作
	 * @param len 从各自索引开始递增交换的次数,根据数组长度,offset1,offset2,len取最小值
	 */
	public static void swap(final short[] array, int offset1, int offset2, int len) {
		if (array == null || array.length == 0 || offset1 >= array.length || offset2 >= array.length) {
			return;
		}
		if (offset1 < 0) {
			offset1 = 0;
		}
		if (offset2 < 0) {
			offset2 = 0;
		}
		if (offset1 == offset2) {
			return;
		}
		len = Math.min(Math.min(len, array.length - offset1), array.length - offset2);
		for (int i = 0; i < len; i++, offset1++, offset2++) {
			final short aux = array[offset1];
			array[offset1] = array[offset2];
			array[offset2] = aux;
		}
	}

	/**
	 * 将对象转化成Object数组,若是基类数组,将转化成包装类数组
	 * 
	 * @param object 对象
	 * @return Object[]
	 */
	public static Object[] toArray(Object object) {
		if (object instanceof Object[]) {
			return (Object[]) object;
		}
		if (object == null) {
			return new Object[] {};
		}
		if (!object.getClass().isArray()) {
			throw new IllegalArgumentException("object is not an array: " + object);
		}
		int length = Array.getLength(object);
		if (length == 0) {
			return new Object[] {};
		}
		Class<?> wrapperType = Array.get(object, 0).getClass();
		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(object, i);
		}
		return newArray;
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Boolean[] toBoxed(boolean[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_BOOLEAN_BOXED;
		}
		final Boolean[] result = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = (array[i] ? Boolean.TRUE : Boolean.FALSE);
		}
		return result;
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Byte[] toBoxed(byte[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_BYTE_BOXED;
		}
		final Byte[] result = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Byte.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Character[] toBoxed(char[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_CHAR_BOXED;
		}
		final Character[] result = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Character.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Double[] toBoxed(double[] array) {
		return Arrays.stream(array).boxed().toArray(Double[]::new);
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Float[] toBoxed(float[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_FLOAT_BOXED;
		}
		final Float[] result = new Float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Float.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Integer[] toBoxed(int[] array) {
		return Arrays.stream(array).boxed().toArray(Integer[]::new);
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Long[] toBoxed(long[] array) {
		return Arrays.stream(array).boxed().toArray(Long[]::new);
	}

	/**
	 * 将基础类型数组转换为包装类型数组
	 * 
	 * @param array 基本类型数组
	 * @return 包装类型数组
	 */
	public static Short[] toBoxed(short[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_SHORT_BOXED;
		}
		final Short[] result = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Short.valueOf(array[i]);
		}
		return result;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static byte[] toByte(Collection<Byte> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new byte[0];
		}
		int len = 0;
		for (Byte integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new byte[0];
		}
		Byte[] array = collection.toArray(new Byte[len]);
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].byteValue();
			}
		}
		return result;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static char[] toChar(Collection<Character> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new char[0];
		}
		int len = 0;
		for (Character integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new char[0];
		}
		Character[] array = collection.toArray(new Character[len]);
		char[] result = new char[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].charValue();
			}
		}
		return result;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static double[] toDouble(Collection<Double> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new double[0];
		}
		int len = 0;
		for (Double integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new double[0];
		}
		Double[] array = collection.toArray(new Double[len]);
		double[] result = new double[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].doubleValue();
			}
		}
		return result;
	}

	/**
	 * 将Collection<Double>转换为double数组,需要至少jdk8以上,且效率低于{@link #toArrayDouble}
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static double[] toDouble8(Collection<Double> collection) {
		if (ListHelper.isNotEmpty(collection)) {
			return Arrays.stream(collection.toArray(new Double[collection.size()]))
					.mapToDouble(Double::valueOf)
					.toArray();
		}
		return null;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static float[] toFloat(Collection<Float> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new float[0];
		}
		int len = 0;
		for (Float integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new float[0];
		}
		Float[] array = collection.toArray(new Float[len]);
		float[] result = new float[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].floatValue();
			}
		}
		return result;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static int[] toInt(Collection<Integer> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new int[0];
		}
		int len = 0;
		for (Integer integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new int[0];
		}
		Integer[] array = collection.toArray(new Integer[len]);
		int[] result = new int[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].intValue();
			}
		}
		return result;
	}

	/**
	 * 将Collection<Integer>转换为int数组,需要至少jdk8以上,且效率低于{@link #toArrayInt(Collection)}
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static int[] toInt8(Collection<Integer> collection) {
		if (ListHelper.isNotEmpty(collection)) {
			return Arrays.stream(collection.toArray(new Integer[collection.size()]))
					.mapToInt(Integer::valueOf)
					.toArray();
		}
		return null;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static long[] toLong(Collection<Long> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new long[0];
		}
		int len = 0;
		for (Long integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new long[0];
		}
		Long[] array = collection.toArray(new Long[len]);
		long[] result = new long[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].longValue();
			}
		}
		return result;
	}

	/**
	 * 将Collection<Long>转换为long数组,需要至少jdk8以上,且效率低于{@link #toArrayLong}
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static long[] toLong8(Collection<Long> collection) {
		if (ListHelper.isNotEmpty(collection)) {
			return Arrays.stream(collection.toArray(new Long[collection.size()])).mapToLong(Long::valueOf).toArray();
		}
		return null;
	}

	/**
	 * 将集合转换为数组
	 * 
	 * @param collection 集合
	 * @return 转换后的数组
	 */
	public static short[] toShort(Collection<Short> collection) {
		if (ListHelper.isEmpty(collection)) {
			return new short[0];
		}
		int len = 0;
		for (Short integer : collection) {
			if (integer != null) {
				len++;
			}
		}
		if (len == 0) {
			return new short[0];
		}
		Short[] array = collection.toArray(new Short[len]);
		short[] result = new short[len];
		for (int i = 0; i < len; i++) {
			if (array[i] != null) {
				result[i] = array[i].shortValue();
			}
		}
		return result;
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static boolean[] toPrimitive(final Boolean[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_BOOLEAN;
		}
		final boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].booleanValue();
		}
		return result;
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static byte[] toPrimitive(final Byte[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_BYTE;
		}
		final byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].byteValue();
		}
		return result;
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static char[] toPrimitive(final Character[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_CHAR;
		}
		final char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].charValue();
		}
		return result;
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static double[] toPrimitive(final Double[] array) {
		return Stream.of(Optional.ofNullable(array).orElse(new Double[0])).mapToDouble(t -> t.doubleValue()).toArray();
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static float[] toPrimitive(final Float[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_FLOAT;
		}
		final float[] result = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].floatValue();
		}
		return result;
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static int[] toPrimitive(final Integer[] array) {
		return Stream.of(Optional.ofNullable(array).orElse(new Integer[0])).mapToInt(t -> t.intValue()).toArray();
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static long[] toPrimitive(final Long[] array) {
		return Stream.of(Optional.ofNullable(array).orElse(new Long[0])).mapToLong(t -> t.longValue()).toArray();
	}

	/**
	 * 将包装类型转换为基类
	 * 
	 * @param array 包装类型数组
	 * @return 基类数组
	 */
	public static short[] toPrimitive(final Short[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return ConstArray.EMPTY_SHORT;
		}
		final short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].shortValue();
		}
		return result;
	}
}