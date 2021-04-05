package com.wy.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.wy.Constant;
import com.wy.annotation.Nullable;
import com.wy.lang.AssertTool;
import com.wy.reflect.ReflectTool;

/**
 * Iterable可迭代对象工具类,
 * 
 * <pre>
 * org.apache.commons.collections4.IterableUtils
 * com.google.common.collect.Iterables
 * com.google.common.collect.Iterators
 * org.apache.commons.collections4.IteratorUtils
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-03-12 15:08:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class IterableTool {

	/**
	 * 将迭代中的数据全部添加到集合中
	 * 
	 * @param <T> 泛型
	 * @param collection 目标集合
	 * @param iterable 迭代数据
	 * @return true->collection被修改
	 */
	public static <T> boolean addAll(final Collection<T> collection, final Iterable<? extends T> iterable) {
		AssertTool.notNull(collection);
		return addAll(collection, iterable.iterator());
	}

	/**
	 * 将迭代中的数据全部添加到集合中
	 * 
	 * @param <T> 泛型
	 * @param collection 目标集合
	 * @param iterator 迭代数据
	 * @return true->collection被修改
	 */
	public static <T> boolean addAll(final Collection<T> collection, final Iterator<? extends T> iterator) {
		AssertTool.notNull(collection);
		AssertTool.notNull(iterator);
		boolean wasModified = false;
		while (iterator.hasNext()) {
			wasModified |= collection.add(iterator.next());
		}
		return wasModified;
	}

	/**
	 * 判断迭代中是否包含某个元素
	 * 
	 * @param iterable 迭代数据
	 * @param element 待判断的元素
	 * @return true->包含,false->不包含
	 */
	public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
		return contains(AssertTool.notNull(iterable).iterator(), element);
	}

	/**
	 * 判断迭代中是否包含某个元素
	 * 
	 * @param iterator 迭代数据
	 * @param element 待判断的元素
	 * @return true->包含,false->不包含
	 */
	public static boolean contains(Iterator<?> iterator, @Nullable Object element) {
		if (element == null) {
			while (iterator.hasNext()) {
				if (iterator.next() == null) {
					return true;
				}
			}
		} else {
			while (iterator.hasNext()) {
				if (element.equals(iterator.next())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 元素计数,类似大数据的Map->Count,元素作为key,该元素个数作为value
	 * 
	 * @param <T> 泛型
	 * @param iter {@link Iterator},如果为null返回一个空的Map
	 * @return {@link Map}
	 */
	public static <T> Map<T, Integer> countMap(Iterator<T> iter) {
		final HashMap<T, Integer> countMap = new HashMap<>();
		if (null != iter) {
			Integer count;
			T t;
			while (iter.hasNext()) {
				t = iter.next();
				count = countMap.get(t);
				if (null == count) {
					countMap.put(t, 1);
				} else {
					countMap.put(t, count + 1);
				}
			}
		}
		return countMap;
	}

	/**
	 * 处理迭代中的每一元素
	 *
	 * @param <T> 泛型
	 * @param iterable 迭代数据
	 * @param consumer 处理方法
	 */
	public static <T> void filter(Iterable<T> iterable, Consumer<T> consumer) {
		filter(AssertTool.notNull(iterable).iterator(), consumer);
	}

	/**
	 * 处理迭代中的每一元素
	 *
	 * @param <T> 泛型
	 * @param iterator 迭代数据
	 * @param consumer 处理方法
	 */
	public static <T> void filter(Iterator<T> iterator, Consumer<T> consumer) {
		AssertTool.notNull(consumer);
		if (iterator != null) {
			while (iterator.hasNext()) {
				final T element = iterator.next();
				consumer.accept(element);
			}
		}
	}

	/**
	 * 过滤集合
	 *
	 * @param <T> 泛型
	 * @param iterable 迭代数据
	 * @param predicate 过滤器
	 */
	public static <T> void filter(Iterable<T> iterable, Predicate<T> predicate) {
		filter(AssertTool.notNull(iterable).iterator(), predicate);
	}

	/**
	 * 过滤集合
	 *
	 * @param <T> 泛型
	 * @param iterator 迭代数据
	 * @param predicate 过滤器
	 */
	public static <T> void filter(Iterator<T> iterator, Predicate<T> predicate) {
		if (null == iterator || null == predicate) {
			return;
		}
		while (iterator.hasNext()) {
			if (!predicate.test(iterator.next())) {
				iterator.remove();
			}
		}
	}

	/**
	 * 查找迭代中第一个符合条件的元素
	 *
	 * @param <T> 泛型
	 * @param iterable 迭代数据
	 * @param predicate 过滤器
	 */
	public static <T> T find(final Iterable<T> iterable, final Predicate<? super T> predicate) {
		return find(AssertTool.notNull(iterable).iterator(), predicate);
	}

	/**
	 * 过滤集合
	 *
	 * @param <T> 泛型
	 * @param iterator 迭代数据
	 * @param predicate 过滤器
	 */
	public static <T> T find(final Iterator<T> iterator, final Predicate<? super T> predicate) {
		AssertTool.notNull(predicate);
		if (iterator != null) {
			while (iterator.hasNext()) {
				final T element = iterator.next();
				if (predicate.test(element)) {
					return element;
				}
			}
		}
		return null;
	}

	/**
	 * 判断指定元素在迭代中出现的次数
	 * 
	 * @param iterable 迭代数据
	 * @param element 待判断元素
	 * @return 出现次数
	 */
	public static int frequency(Iterable<?> iterable, @Nullable Object element) {
		AssertTool.notNull(iterable);
		if (iterable instanceof Set<?>) {
			return ((Set<?>) iterable).contains(element) ? 1 : 0;
		}
		return frequency(iterable.iterator(), element);
	}

	/**
	 * 判断指定元素在迭代中出现的次数
	 * 
	 * @param iterator 迭代数据
	 * @param element 待判断元素
	 * @return 出现次数
	 */
	public static int frequency(Iterator<?> iterator, @Nullable Object element) {
		int count = 0;
		while (contains(iterator, element)) {
			count++;
		}
		return count;
	}

	/**
	 * 获得迭代对象的泛型class
	 * 
	 * @param <T> 泛型
	 * @param iterable Iterable
	 * @return Class<?>.当列表为空或元素全部为null时,返回null
	 */
	public static <T> Class<?> getElementType(Iterable<T> iterable) {
		if (null != iterable) {
			final Iterator<?> iterator = iterable.iterator();
			return getElementType(iterator);
		}
		return null;
	}

	/**
	 * 获得迭代对象的泛型class
	 * 
	 * @param <T> 泛型
	 * @param iterator Iterator
	 * @return Class<?>.当列表为空或元素全部为null时,返回null
	 */
	public static <T> Class<?> getElementType(Iterator<T> iterator) {
		if (iterator.hasNext()) {
			final T t = iterator.next();
			if (null != t) {
				return t.getClass();
			}
		}
		return null;
	}

	/**
	 * 检查Iterator是否为null,若为null返回一个空的Iterator,否则返回本身
	 *
	 * @param <T> 泛型
	 * @return 空Iterator或源Iterator
	 */
	public static <T> Iterator<T> getEmpty(Iterator<T> iterator) {
		return null == iterator ? Collections.emptyIterator() : iterator;
	}

	/**
	 * 获取Bean迭代中指定字段的值,并组装成一个List
	 *
	 * @param <T> 泛型
	 * @param iterable Iterable
	 * @param fieldName 字段名
	 * @return 指定字段的值的List
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> List<Object> getFieldList(Iterable<T> iterable, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		return getFieldList(null == iterable ? null : iterable.iterator(), fieldName);
	}

	/**
	 * 获取Bean迭代中指定字段的值,并组装成一个List
	 *
	 * @param <T> 泛型
	 * @param iterator Iterator
	 * @param fieldName 字段名
	 * @return 指定字段的值的List
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static <T> List<Object> getFieldList(Iterator<T> iterator, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		final List<Object> result = new ArrayList<>();
		if (null != iterator) {
			T value;
			while (iterator.hasNext()) {
				value = iterator.next();
				result.add(ReflectTool.readField(value, fieldName));
			}
		}
		return result;
	}

	/**
	 * 获取迭代的第一个元素
	 *
	 * @param <T> 泛型
	 * @param iterable {@link Iterable}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterable<T> iterable) {
		return getFirst(AssertTool.notNull(iterable).iterator());
	}

	/**
	 * 获取迭代的第一个元素
	 *
	 * @param <T> 泛型
	 * @param iterator {@link Iterator}
	 * @return 第一个元素
	 */
	public static <T> T getFirst(Iterator<T> iterator) {
		if (null != iterator && iterator.hasNext()) {
			return iterator.next();
		}
		return null;
	}

	/**
	 * 检查Iterable包含null元素
	 *
	 * @param iter 被检查的{@link Iterable}对象,如果为null,返回true
	 * @return true->包含null,false->不包含
	 */
	public static boolean hasNull(Iterable<?> iter) {
		return hasNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 检查Iterator包含null元素
	 *
	 * @param iter 被检查的{@link Iterator}对象,如果为null,返回true
	 * @return true->包含null,false->不包含
	 */
	public static boolean hasNull(Iterator<?> iter) {
		if (null == iter) {
			return true;
		}
		while (iter.hasNext()) {
			if (null == iter.next()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查Iterable全部为null
	 *
	 * @param iter iter 被检查的{@link Iterable}对象,如果为null,返回true
	 * @return true->全部null
	 */
	public static boolean isAllNull(Iterable<?> iter) {
		return isAllNull(null == iter ? null : iter.iterator());
	}

	/**
	 * 检查Iterator全部为null
	 *
	 * @param iter iter 被检查的{@link Iterator}对象,如果为null,返回true
	 * @return true->全部null
	 */
	public static boolean isAllNull(Iterator<?> iter) {
		if (null == iter) {
			return true;
		}
		while (iter.hasNext()) {
			if (null != iter.next()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断Iterable为空
	 *
	 * @param iterable Iterable
	 * @return true->空,false->非空
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return null == iterable || isEmpty(iterable.iterator());
	}

	/**
	 * 判断Iterator为空
	 *
	 * @param iterator Iterator
	 * @return true->空,false->非空
	 */
	public static boolean isEmpty(Iterator<?> iterator) {
		return null == iterator || !iterator.hasNext();
	}

	/**
	 * 判断Iterable不为空
	 *
	 * @param iterable Iterable
	 * @return true->空,false->非空
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return null != iterable && isNotEmpty(iterable.iterator());
	}

	/**
	 * 判断Iterator不为空
	 *
	 * @param iterator Iterator
	 * @return true->空,false->非空
	 */
	public static boolean isNotEmpty(Iterator<?> iterator) {
		return null != iterator && iterator.hasNext();
	}

	/**
	 * 通过指定分隔符将迭代中的元素拼接成字符串,迭代中的元素简单的调用toString()获得字符串
	 *
	 * @param <T> 泛型
	 * @param iterator Iterator
	 * @param delimiter 分隔符
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence delimiter) {
		return join(iterator, delimiter, null, null, null);
	}

	/**
	 * 通过指定分隔符将迭代中的元素拼接成字符串
	 *
	 * @param <T> 泛型
	 * @param iterator Iterator
	 * @param delimiter 分隔符
	 * @param function 提供泛型转换为字符串的方法,若为null,直接调用toString()
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence delimiter, Function<T, String> function) {
		return join(iterator, delimiter, function, null, null);
	}

	/**
	 * 通过指定分隔符将迭代中的元素拼接成字符串,迭代中的元素简单的调用toString()获得字符串
	 *
	 * @param <T> 泛型
	 * @param iterator Iterator
	 * @param delimiter 分隔符
	 * @param prefix 每个元素添加的前缀,null表示不添加
	 * @param suffix 每个元素添加的后缀,null表示不添加
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence delimiter, String prefix, String suffix) {
		return join(iterator, delimiter, null, prefix, suffix);
	}

	/**
	 * 通过指定分隔符将迭代中的元素拼接成字符串
	 *
	 * @param <T> 泛型
	 * @param iterator Iterator
	 * @param delimiter 分隔符
	 * @param function 提供泛型转换为字符串的方法,若为null,直接调用toString()
	 * @param prefix 每个元素添加的前缀,null表示不添加
	 * @param suffix 每个元素添加的后缀,null表示不添加
	 * @return 连接后的字符串
	 */
	public static <T> String join(Iterator<T> iterator, CharSequence delimiter, Function<T, String> function,
			String prefix, String suffix) {
		if (null == iterator) {
			return Constant.Langes.STR_EMPTY;
		}
		StringJoiner joiner = new StringJoiner(delimiter, prefix, suffix);
		while (iterator.hasNext()) {
			joiner.add(null == function ? String.valueOf(iterator.next()) : function.apply(iterator.next()));
		}
		return joiner.toString();
	}

	/**
	 * 返回Iterable对象的元素数量,注意,该方法使用之后,参数需要重新获得迭代器
	 *
	 * @param iterable Iterable对象
	 * @return Iterable对象的元素数量
	 */
	public static int size(final Iterable<?> iterable) {
		if (null == iterable) {
			return 0;
		}
		if (iterable instanceof Collection<?>) {
			return ((Collection<?>) iterable).size();
		} else {
			return size(iterable.iterator());
		}
	}

	/**
	 * 返回Iterator对象的元素数量,注意,该方法使用之后,参数需要重新获得迭代器
	 *
	 * @param iterator Iterator对象
	 * @return Iterator对象的元素数量
	 */
	public static int size(final Iterator<?> iterator) {
		int size = 0;
		if (iterator != null) {
			while (iterator.hasNext()) {
				iterator.next();
				size++;
			}
		}
		return size;
	}

	/**
	 * {@link Enumeration}转换为{@link Iterator}
	 *
	 * @param <T> 集合元素类型
	 * @param e {@link Enumeration}
	 * @return {@link Iterator}
	 */
	public static <T> Iterator<T> toIterator(Enumeration<T> e) {
		return new EnumerationIterator<>(e);
	}

	/**
	 * {@link Iterator}转为{@link Iterable}
	 *
	 * @param <T> 泛型
	 * @param iter {@link Iterator}
	 * @return {@link Iterable}
	 */
	public static <T> Iterable<T> toIterable(final Iterator<T> iterator) {
		return () -> iterator;
	}

	/**
	 * Iterator转ArrayList
	 *
	 * @param <T> 泛型
	 * @param iter {@link Iterator}
	 * @return ArrayList
	 */
	public static <T> ArrayList<T> toList(Iterable<T> iter) {
		if (null == iter) {
			return new ArrayList<>(0);
		}
		if (iter instanceof Collection) {
			return new ArrayList<>((Collection<T>) iter);
		}
		return toList(iter.iterator());
	}

	/**
	 * Iterator转List
	 *
	 * @param <T> 泛型
	 * @param iter {@link Iterator}
	 * @return ArrayList
	 */
	public static <T> ArrayList<T> toList(Iterator<T> iter) {
		if (null == iter) {
			return new ArrayList<>(0);
		}
		final ArrayList<T> list = new ArrayList<>();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}

	/**
	 * 将迭代数据按照指定方式转成HashMap<K,List<V>>,key自定义,值为迭代元素
	 *
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param iterable 迭代数据
	 * @param keyFunction key生成方式
	 * @return HashMap<K,List<V>>
	 */
	public static <K, V> Map<K, List<V>> toListMap(Iterable<V> iterable, Function<V, K> keyFunction) {
		return toListMap(iterable, keyFunction, v -> v);
	}

	/**
	 * 将迭代数据按照指定方式转成HashMap<K,List<V>>
	 *
	 * @param <T> 迭代值类型
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param iterable 迭代数据
	 * @param keyFunction key生成方式
	 * @param valueFunction value生成方式
	 * @return HashMap<K,List<V>>
	 */
	public static <T, K, V> Map<K, List<V>> toListMap(Iterable<T> iterable, Function<T, K> keyFunction,
			Function<T, V> valueFunction) {
		return toListMap(iterable, MapTool.newHashMap(), keyFunction, valueFunction);
	}

	/**
	 * 将迭代数据按照指定方式转成HashMap<K,List<V>>
	 *
	 * @param <T> 迭代值类型
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param iterable 迭代数据
	 * @param result 结果Map,可自定义结果Map类型
	 * @param keyFunction key生成方式
	 * @param valueFunction value生成方式
	 * @return HashMap<K,List<V>>
	 */
	public static <T, K, V> Map<K, List<V>> toListMap(Iterable<T> iterable, Map<K, List<V>> result,
			Function<T, K> keyFunction, Function<T, V> valueFunction) {
		if (null == result) {
			result = MapTool.newHashMap();
		}
		if (null == iterable) {
			return result;
		}
		for (T value : iterable) {
			result.computeIfAbsent(keyFunction.apply(value), k -> new ArrayList<>()).add(valueFunction.apply(value));
		}
		return result;
	}

	/**
	 * 将key迭代和value迭代组装成一个Map,以key长度为Map长度,value可为null
	 *
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param keys 键迭代
	 * @param values 值迭代
	 * @return Map
	 */
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values) {
		return toMap(keys, values, false);
	}

	/**
	 * 将key迭代和value迭代组装成一个Map,以key长度为Map长度,value可为null
	 *
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param keys 键迭代
	 * @param values 值迭代
	 * @param order 是否有序,true->有序,false->无序
	 * @return Map
	 */
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values, boolean order) {
		return toMap(null == keys ? null : keys.iterator(), null == values ? null : values.iterator(), order);
	}

	/**
	 * 将迭代转成key为keyFunction指定的值,value为迭代元素本身的HashMap
	 *
	 * @param <K> key类型
	 * @param <V> value类型,元素类型
	 * @param iterable Iterable
	 * @param map 返回值,确定泛型
	 * @param keyFunction key生成方式
	 * @return HashMap
	 */
	public static <K, V> Map<K, V> toMap(Iterable<V> iterable, Map<K, V> map, Function<V, K> keyFunction) {
		return toMap(iterable, map, keyFunction, v -> v);
	}

	/**
	 * 将迭代按照特定的方法转成HashMap
	 *
	 * @param <T> 迭代元素类型
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param iterable 迭代数据
	 * @param result 结果Map,确定泛型类型
	 * @param keyFunction key生成方式
	 * @param valueFunction value生成方式
	 * @return HashMap
	 */
	public static <T, K, V> Map<K, V> toMap(Iterable<T> iterable, Map<K, V> result, Function<T, K> keyFunction,
			Function<T, V> valueFunction) {
		if (null == result) {
			result = MapTool.newHashMap();
		}
		if (null == iterable) {
			return result;
		}
		for (T value : iterable) {
			result.put(keyFunction.apply(value), valueFunction.apply(value));
		}
		return result;
	}

	/**
	 * 将key迭代和value迭代组装成一个Map,以key长度为Map长度,value可为null
	 *
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param keys 键迭代
	 * @param values 值迭代
	 * @return Map
	 */
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values) {
		return toMap(keys, values, false);
	}

	/**
	 * 将key迭代和value迭代组装成一个Map,以key长度为Map长度,value可为null
	 *
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param keys 键迭代
	 * @param values 值迭代
	 * @param order 是否有序,true->有序,false->无序
	 * @return Map
	 */
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values, boolean order) {
		final Map<K, V> result = order ? MapTool.newLinkedHashMap() : MapTool.newHashMap();
		if (isNotEmpty(keys)) {
			while (keys.hasNext()) {
				result.put(keys.next(), (null != values && values.hasNext()) ? values.next() : null);
			}
		}
		return result;
	}

	/**
	 * 将迭代转成key为keyFunction指定的值,value为迭代元素本身的HashMap
	 *
	 * @param <K> key类型
	 * @param <V> value类型,元素类型
	 * @param iterator Iterator
	 * @param map 返回值,确定泛型
	 * @param keyFunction key生成方式
	 * @return HashMap
	 */
	public static <K, V> Map<K, V> toMap(Iterator<V> iterator, Map<K, V> map, Function<V, K> keyFunction) {
		return toMap(iterator, map, keyFunction, (value) -> value);
	}

	/**
	 * 将迭代按照特定的方法转成HashMap
	 *
	 * @param <T> 迭代元素类型
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param iterator 迭代数据
	 * @param result 结果Map,确定泛型类型
	 * @param keyFunction key生成方式
	 * @param valueFunction value生成方式
	 * @return HashMap
	 */
	public static <K, V, T> Map<K, V> toMap(Iterator<T> iterator, Map<K, V> result, Function<T, K> keyFunction,
			Function<T, V> valueFunction) {
		if (null == result) {
			result = MapTool.newHashMap();
		}
		if (null == iterator) {
			return result;
		}
		T element;
		while (iterator.hasNext()) {
			element = iterator.next();
			result.put(keyFunction.apply(element), valueFunction.apply(element));
		}
		return result;
	}

	/**
	 * 按照给定函数,转换{@link Iterator}为另一种类型的{@link Iterator}
	 *
	 * @param <F> 源元素类型
	 * @param <T> 目标元素类型
	 * @param iterator 源{@link Iterator}
	 * @param function 转换函数
	 * @return 转换后的{@link Iterator}
	 */
	public static <F, T> Iterator<T> transform(Iterator<F> iterator, Function<? super F, ? extends T> function) {
		return new TransformerIterator<>(iterator, function);
	}
}