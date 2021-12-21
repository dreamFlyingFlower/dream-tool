package com.wy.collection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.wy.ConstantLang;
import com.wy.reflect.ReflectTool;

/**
 * List工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-23 10:54:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ListTool extends CollectionTool {

	/**
	 * 检查list,若为null返回一个空list
	 * 
	 * @param <T> 泛型
	 * @param list List
	 * @return 源List或空List
	 */
	public static <T> List<T> getEmpty(final List<T> list) {
		return list == null ? Collections.<T>emptyList() : list;
	}

	/**
	 * 判断list,若为null或长度为0,返回默认List
	 *
	 * @param <T> 泛型
	 * @param list 需要检查的List
	 * @param defaultList 默认List
	 * @return 源List或默认List
	 */
	public static <T> List<T> getDefault(final List<T> list, final List<T> defaultList) {
		return list == null || list.size() == 0 ? defaultList : list;
	}

	/**
	 * 获得2个List的交集,2个List都有的元素
	 *
	 * @param <T> 泛型
	 * @param list1 List1
	 * @param list2 List2
	 * @return 2个List都有的元素集合
	 */
	public static <T> List<T> getIntersection(final List<? extends T> list1, final List<? extends T> list2) {
		List<? extends T> smaller = list1;
		List<? extends T> larger = list2;
		if (list1.size() > list2.size()) {
			smaller = list2;
			larger = list1;
		}
		final List<T> result = new ArrayList<>();
		final HashSet<T> hashSet = new HashSet<>(smaller);
		for (final T e : larger) {
			if (hashSet.contains(e)) {
				result.add(e);
				hashSet.remove(e);
			}
		}
		return result;
	}

	/**
	 * 从List1中去除List1和List2都有的元素,只保留List1有的元素
	 *
	 * @param <T> 泛型
	 * @param list1 求差集的集合
	 * @param list2 差集
	 * @return 一个新的List,只包含List1中有,List2中没有的元素
	 */
	public static <T> List<T> getSubtract(final List<T> list1, final List<? extends T> list2) {
		final ArrayList<T> result = new ArrayList<>();
		list1.removeAll(getIntersection(list1, list2));
		result.addAll(list1);
		return result;
	}

	/**
	 * 获得2个集合的所有元素,同时去重
	 *
	 * @param <T> 泛型
	 * @param list1 List1
	 * @param list2 List2
	 * @return 一个新的List,包含所有的元素,去重
	 */
	public static <T> List<T> getSum(final List<? extends T> list1, final List<? extends T> list2) {
		return getSubtract(getUnion(list1, list2), getIntersection(list1, list2));
	}

	/**
	 * 合并2个List,返回一个新的List.注意,是返回一个新的List
	 *
	 * @param <T> 泛型
	 * @param list1 List1
	 * @param list2 List2
	 * @return 一个新的List,包含List1和List2
	 */
	public static <T> List<T> getUnion(final List<? extends T> list1, final List<? extends T> list2) {
		final ArrayList<T> result = new ArrayList<>(list1.size() + list2.size());
		if (isNotEmpty(list1)) {
			result.addAll(list1);
		}
		if (isNotEmpty(list2)) {
			result.addAll(list2);
		}
		return result;
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 *
	 * @param separator 间隔符
	 * @param list 集合
	 * @param start 起始索引
	 * @param end 结束索引
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final List<?> list, final int start, final int end) {
		if (list == null) {
			return null;
		}
		final int noOfItems = end - start;
		if (noOfItems <= 0) {
			return ConstantLang.STR_EMPTY;
		}
		final List<?> subList = list.subList(start, end);
		return join(separator, subList.iterator());
	}

	/**
	 * 将List转换为Map.
	 * 
	 * 本方法返回的Map中的key是Object,可参照该方法修改
	 * 
	 * @param <T>
	 * @param lists 需要转换的List
	 * @param key T中的字段,值用来作为Map的key
	 * @return Map
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static <T> Map<Object, List<T>> toMapList(List<T> lists, String key)
			throws NoSuchFieldException, SecurityException {
		HashMap<Object, List<T>> newHashMap = MapTool.newHashMap();
		Class<?> clazz = lists.get(0).getClass();
		Field declaredField = clazz.getDeclaredField(key);
		ReflectTool.fixAccessible(declaredField);
		lists.forEach(t -> {
			try {
				newHashMap.merge(declaredField.get(t), new ArrayList<>(Arrays.asList(t)), (m, n) -> {
					m.addAll(n);
					return m;
				});
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return newHashMap;
	}

	public static <T> ListBuilder<T> builder() {
		return new ListBuilder<T>();
	}

	public static <T> ListBuilder<T> builder(T val) {
		return new ListBuilder<T>(val);
	}

	public static class ListBuilder<T> {

		private List<T> list = new ArrayList<>(16);

		public ListBuilder() {
		}

		public ListBuilder(T val) {
			list.add(val);
		}

		public ListBuilder<T> add(T val) {
			list.add(val);
			return this;
		}

		public List<T> build() {
			return list;
		}
	}
}