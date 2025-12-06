package dream.flying.flower.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.stream.Collectors;

import dream.flying.flower.ConstString;
import dream.flying.flower.lang.AssertHelper;

/**
 * Collection工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-23 11:08:44
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CollectionHelper {

	/**
	 * 判断集合是否为null或长度为0
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @return true当集合为null或长度为0
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * 判断集合是否不为null或长度大于0
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @return true当集合不为null或长度大于0
	 */
	public static <T> boolean isNotEmpty(Collection<T> collection) {
		return !isEmpty(collection);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 *
	 * @param separator 间隔符
	 * @param iterable 可迭代对象
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final Iterable<?> iterable) {
		return join(separator, iterable.iterator());
	}

	/**
	 * 获得Set中的最后一个元素
	 * 
	 * @param set 待查找的Set
	 * @return 最后一个元素,可能为null
	 */
	public static <T> T lastElement(Set<T> set) {
		if (isEmpty(set)) {
			return null;
		}
		if (set instanceof SortedSet) {
			return ((SortedSet<T>) set).last();
		}

		Iterator<T> it = set.iterator();
		T last = null;
		while (it.hasNext()) {
			last = it.next();
		}
		return last;
	}

	/**
	 * 获得List中的最后一个元素
	 * 
	 * @param list 待查找的List
	 * @return 最后一个元素,可能为null
	 */
	public static <T> T lastElement(List<T> list) {
		if (isEmpty(list)) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	/**
	 * 使用指定间隔符将数组中的元素拼接成字符串
	 *
	 * @param separator 间隔符
	 * @param iterator 可迭代对象
	 * @return 拼接后的字符串
	 */
	public static String join(final String separator, final Iterator<?> iterator) {
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return ConstString.EMPTY;
		}
		final Object first = iterator.next();
		if (!iterator.hasNext()) {
			return Objects.toString(first, "");
		}
		final StringBuilder buf = new StringBuilder(256);
		if (first != null) {
			buf.append(first);
		}
		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			final Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}
		return buf.toString();
	}

	/**
	 * 获得集合中的最大值
	 * 
	 * @param <T> 泛型,必须实现Comparable接口
	 * @param collection 集合
	 * @return 最大值
	 */
	public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> collection) {
		Iterator<? extends T> i = collection.iterator();
		T t = i.next();
		while (i.hasNext()) {
			T next = i.next();
			if (next.compareTo(t) > 0) {
				t = next;
			}
		}
		return t;
	}

	/**
	 * 获得集合中的最大值
	 * 
	 * @param <T> 泛型,必须实现Comparator接口
	 * @param collection 集合
	 * @param comparator Comparator接口实现类
	 * @return 最大值
	 */
	public static <T> T max(Collection<? extends T> collection, Comparator<? super T> comparator) {
		if (isEmpty(collection)) {
			return null;
		}
		Iterator<? extends T> i = collection.iterator();
		T t = i.next();
		if (comparator == null && !(t instanceof Comparator)) {
			return null;
		}
		while (i.hasNext()) {
			T next = i.next();
			if (comparator.compare(next, t) > 0) {
				t = next;
			}
		}
		return t;
	}

	/**
	 * 将多个集合合并和一个集合
	 * 
	 * @param <T> 泛型
	 * @param collections 集合的集合
	 * @return 新集合
	 */
	public static <T> Collection<T> merge(Collection<? extends Collection<T>> collections) {
		if (isEmpty(collections)) {
			return null;
		}
		Collection<T> result = null;
		for (Collection<T> collection : collections) {
			if (isEmpty(collection)) {
				continue;
			}
			if (isEmpty(result)) {
				result = collection;
			} else {
				result.addAll(collection);
			}
		}
		return result;
	}

	/**
	 * 获得集合中的最小值
	 * 
	 * @param <T> 泛型,必须实现Comparable接口
	 * @param collection 集合
	 * @return 最小值
	 */
	public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> collection) {
		Iterator<? extends T> i = collection.iterator();
		T candidate = i.next();
		while (i.hasNext()) {
			T next = i.next();
			if (next.compareTo(candidate) < 0)
				candidate = next;
		}
		return candidate;
	}

	/**
	 * 获得集合中的最小值
	 * 
	 * @param <T> 泛型,必须实现Comparator接口
	 * @param collection 集合
	 * @param comparator Comparator接口实现类
	 * @return 最小值
	 */
	public static <T> T min(Collection<? extends T> collection, Comparator<? super T> comparator) {
		if (isEmpty(collection)) {
			return null;
		}
		Iterator<? extends T> i = collection.iterator();
		T t = i.next();
		if (comparator == null && !(t instanceof Comparator)) {
			return null;
		}
		while (i.hasNext()) {
			T next = i.next();
			if (comparator.compare(next, t) < 0)
				t = next;
		}
		return t;
	}

	/**
	 * 新建一个ArrayList,指定容量
	 * 
	 * @param <T> 泛型
	 * @param capacity 容量,大于0
	 * @return ArrayList
	 */
	public static <T> ArrayList<T> newArrayList(int capacity) {
		AssertHelper.isTrue(capacity >= 0, "initialArraySize");
		return new ArrayList<>(capacity);
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 泛型
	 * @param ts 数据
	 * @return ArrayList<T>
	 */
	@SafeVarargs
	public static <T> ArrayList<T> newArrayList(T... ts) {
		if (null == ts || ts.length == 0) {
			return new ArrayList<>(0);
		}
		ArrayList<T> result = new ArrayList<>(ts.length);
		Collections.addAll(result, ts);
		return result;
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 泛型
	 * @param elements 迭代类型数据
	 * @return ArrayList<T>
	 */
	public static <T> ArrayList<T> newArrayList(Iterable<? extends T> elements) {
		AssertHelper.notNull(elements);
		return (elements instanceof Collection)
				? new ArrayList<>((Collection<? extends T>) elements) : newArrayList(elements);
	}

	/**
	 * 新建一个ArrayList
	 * 
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return ArrayList<T>
	 */
	public static <T> ArrayList<T> newArrayList(Iterator<? extends T> elements) {
		ArrayList<T> list = newArrayList();
		IterableHelper.addAll(list, elements);
		return list;
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T> 泛型
	 * @param ts 元素数组
	 * @return HashSet
	 */
	@SafeVarargs
	public static <T> HashSet<T> newHashSet(T... ts) {
		return (HashSet<T>) newSet(false, ts);
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return HashSet
	 */
	public static <T> HashSet<T> newHashSet(Iterable<? extends T> elements) {
		HashSet<T> hashSet = newHashSet();
		IterableHelper.addAll(hashSet, elements);
		return hashSet;
	}

	/**
	 * 新建一个HashSet
	 *
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return HashSet
	 */
	public static <T> HashSet<T> newHashSet(Iterator<? extends T> elements) {
		HashSet<T> hashSet = newHashSet();
		IterableHelper.addAll(hashSet, elements);
		return hashSet;
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 泛型
	 * @param ts 元素数组
	 * @return LinkedHashSet
	 */
	@SafeVarargs
	public static <T> LinkedHashSet<T> newLinkedHashSet(T... ts) {
		if (null == ts || ts.length == 0) {
			return new LinkedHashSet<>(0);
		}
		LinkedHashSet<T> linkedHashSet = new LinkedHashSet<>(ts.length);
		Collections.addAll(linkedHashSet, ts);
		return linkedHashSet;
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return LinkedHashSet
	 */
	public static <T> LinkedHashSet<T> newLinkedHashSet(Iterable<? extends T> elements) {
		LinkedHashSet<T> newLinkedHashSet = newLinkedHashSet();
		IterableHelper.addAll(newLinkedHashSet, elements);
		return newLinkedHashSet;
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return LinkedHashSet
	 */
	public static <T> LinkedHashSet<T> newLinkedHashSet(Iterator<? extends T> elements) {
		LinkedHashSet<T> newLinkedHashSet = newLinkedHashSet();
		IterableHelper.addAll(newLinkedHashSet, elements);
		return newLinkedHashSet;
	}

	/**
	 * 新建一个LinkedList
	 * 
	 * @param <T> 泛型
	 * @param ts 数据
	 * @return LinkedList
	 */
	@SafeVarargs
	public static <T> LinkedList<T> newLinkedList(T... ts) {
		LinkedList<T> linkedList = new LinkedList<>();
		Collections.addAll(linkedList, ts);
		return linkedList;
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 泛型
	 * @param elements 迭代元素
	 * @return LinkedHashSet
	 */
	public static <T> LinkedList<T> newLinkedList(Iterable<? extends T> elements) {
		LinkedList<T> list = newLinkedList();
		IterableHelper.addAll(list, elements);
		return list;
	}

	/**
	 * 新建一个LinkedHashSet
	 *
	 * @param <T> 泛型
	 * @param elements 迭代元素
	 * @return LinkedHashSet
	 */
	public static <T> LinkedList<T> newLinkedList(Iterator<? extends T> elements) {
		LinkedList<T> list = newLinkedList();
		IterableHelper.addAll(list, elements);
		return list;
	}

	/**
	 * 新建一个线程安全的ArrayList,并添加数据
	 * 
	 * {@link CopyOnWriteArrayList}:在多线程下查询性能优于ArrayList,但更新消耗更大
	 * 
	 * @param <T> 泛型
	 * @return CopyOnWriteArrayList
	 */
	@SafeVarargs
	public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(T... ts) {
		return new CopyOnWriteArrayList<>(ts);
	}

	/**
	 * 新建一个线程安全的ArrayList,并添加数据
	 * 
	 * {@link CopyOnWriteArrayList}:在多线程下查询性能优于ArrayList,但更新消耗更大
	 * 
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return CopyOnWriteArrayList
	 */
	public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(Iterable<? extends T> elements) {
		Collection<? extends T> elementsCollection =
				(elements instanceof Collection) ? (Collection<? extends T>) elements : newArrayList(elements);
		return new CopyOnWriteArrayList<>(elementsCollection);
	}

	/**
	 * 新建一个线程安全的ArrayList,并添加数据
	 * 
	 * {@link CopyOnWriteArrayList}:在多线程下查询性能优于ArrayList,但更新消耗更大
	 * 
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return CopyOnWriteArrayList
	 */
	public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(Iterator<? extends T> elements) {
		return new CopyOnWriteArrayList<>(newArrayList(elements));
	}

	/**
	 * 新建一个线程安全的HashSet,并添加数据
	 * 
	 * {@link CopyOnWriteArraySet}:在多线程下查询性能优于HashSet,但更新消耗更大
	 * 
	 * @param <T> 泛型
	 * @param ts 数据
	 * @return CopyOnWriteArraySet
	 */
	@SafeVarargs
	public static <T> CopyOnWriteArraySet<T> newCopyOnWriteArraySet(T... ts) {
		return new CopyOnWriteArraySet<>(newArrayList(ts));
	}

	/**
	 * 新建一个线程安全的HashSet,并添加数据
	 * 
	 * {@link CopyOnWriteArraySet}:在多线程下查询性能优于HashSet,但更新消耗更大
	 * 
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return CopyOnWriteArraySet
	 */
	public static <T> CopyOnWriteArraySet<T> newCopyOnWriteArraySet(Iterable<? extends T> elements) {
		return new CopyOnWriteArraySet<>(newHashSet(elements));
	}

	/**
	 * 新建一个线程安全的HashSet,并添加数据
	 * 
	 * {@link CopyOnWriteArraySet}:在多线程下查询性能优于HashSet,但更新消耗更大
	 * 
	 * @param <T> 泛型
	 * @param elements 迭代数据
	 * @return CopyOnWriteArraySet
	 */
	public static <T> CopyOnWriteArraySet<T> newCopyOnWriteArraySet(Iterator<? extends T> elements) {
		return new CopyOnWriteArraySet<>(newHashSet(elements));
	}

	/**
	 * 新建一个Set
	 *
	 * @param <T> 泛型
	 * @param sort 是否有序,true->返回 {@link LinkedHashSet},false->返回 {@link HashSet}
	 * @param ts 元素数组
	 * @return Set
	 */
	@SafeVarargs
	public static <T> Set<T> newSet(boolean sort, T... ts) {
		if (null == ts || ts.length == 0) {
			return sort ? new LinkedHashSet<>() : new HashSet<>();
		}
		int initialCapacity = Math.max((int) (ts.length / .75f) + 1, 16);
		final Set<T> set = sort ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
		Collections.addAll(set, ts);
		return set;
	}

	/**
	 * 将集合中的元素根据指定方法转换Set<V>
	 * 
	 * @param <T> 源集合中元素类型
	 * @param <V> 转换后Set中的元素类型
	 * @param datas 集合
	 * @param valueFunction 将源集合元素转换为V的方法
	 * @return valueFunction转换后的值为元素的Set
	 */
	static <T, V> Set<V> toSet(Collection<T> datas, Function<T, V> valueFunction) {
		Objects.requireNonNull(valueFunction);

		return Optional.ofNullable(datas)
				.orElse(Collections.emptyList())
				.stream()
				.filter(Objects::nonNull)
				.map(valueFunction)
				.collect(Collectors.toSet());
	}
}