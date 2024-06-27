package dream.flying.flower.collection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import dream.flying.flower.annotation.Example;
import dream.flying.flower.example.HeavenSong;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.NumberHelper;
import dream.flying.flower.lang.ObjectHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.primitive.BooleanHelper;
import dream.flying.flower.primitive.CharHelper;

/**
 * Map工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-02 10:10:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MapHelper {

	/**
	 * 默认增长因子,当Map的size达到 容量*增长因子时,开始扩充Map,见{@link HashMap}
	 */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	public static String createParams(String url, Map<String, Object> params) {
		return createParams(url, params, StandardCharsets.UTF_8.displayName());
	}

	public static String createParams(String url, Map<String, Object> params, String charset) {
		if (isEmpty(params)) {
			return url;
		}
		return MessageFormat.format("{0}?{1}", url, createParams(params, charset));
	}

	public static String createParams(Map<String, Object> params, String charset) {
		List<String> list = new ArrayList<>();
		for (String key : params.keySet()) {
			try {
				list.add(key + "=" + URLEncoder.encode(Objects.toString(params.get(key)), charset));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return String.join("&", list);
	}

	/**
	 * 行转列,合并相同的键,值合并为列表.该方法没有指定特定key,可能多个key的value相同,慎用
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param datas Map列表
	 * @return Map
	 */
	public static <K, V> Map<K, List<V>> convert(Collection<? extends Map<K, V>> datas) {
		final HashMap<K, List<V>> resultMap = newHashMap();
		if (CollectionHelper.isEmpty(datas)) {
			return resultMap;
		}
		for (Map<K, V> map : datas) {
			K key;
			List<V> valueList;
			for (Entry<K, V> entry : map.entrySet()) {
				key = entry.getKey();
				valueList = resultMap.get(key);
				if (null == valueList) {
					valueList = new ArrayList<>();
					resultMap.put(key, valueList);
				}
				valueList.add(entry.getValue());
			}
		}
		return resultMap;
	}

	/**
	 * 行转列,合并指定key的相同value,值根据value合并为list,需指定key
	 * 
	 * @param datas 数据集
	 * @param key 进行的分类的key
	 * @return 结果集
	 */
	public static <K, V> Map<V, List<Map<K, V>>> convert(Collection<? extends Map<K, V>> datas, K key) {
		Map<V, List<Map<K, V>>> result = newHashMap(datas.size());
		if (CollectionHelper.isEmpty(datas)) {
			return result;
		}
		List<Map<K, V>> tempData;
		for (Map<K, V> data : datas) {
			tempData = result.get(data.get(key));
			if (ListHelper.isEmpty(tempData)) {
				tempData = new ArrayList<>();
				result.put(data.get(key), tempData);
			}
			tempData.add(data);
		}
		return result;
	}

	/**
	 * 当map为null时,返回一个空的Map
	 * 
	 * @param <K> 泛型key
	 * @param <V> 泛型value
	 * @param map map
	 * @return 源map或空map
	 */
	public static <K, V> Map<K, V> emptyIfNull(final Map<K, V> map) {
		return map == null ? Collections.<K, V>emptyMap() : map;
	}

	/**
	 * 拦截map中的entry,返回一个新的map.同Java8中的{@link Stream#map(Function)}
	 * 
	 * @param <K> 泛型key
	 * @param <V> 泛型value
	 * @param map Map数据
	 * @param function 拦截方法
	 * @return 新map
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Function<Map.Entry<K, V>, Map.Entry<K, V>> function) {
		if (isEmpty(map)) {
			return map;
		}
		Map<K, V> result = ObjectHelper.clone(map);
		for (Map.Entry<K, V> entry : map.entrySet()) {
			Entry<K, V> apply = function.apply(entry);
			result.put(apply.getKey(), apply.getValue());
		}
		return result;
	}

	/**
	 * 拦截map中的entry,返回一个新的map.同Java8中的{@link Stream#filter(Predicate)}
	 * 
	 * @param <K> 泛型key
	 * @param <V> 泛型value
	 * @param map Map数据
	 * @param predicate 拦截方法
	 * @return 新map
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
		if (isEmpty(map)) {
			return map;
		}
		Map<K, V> result = ObjectHelper.clone(map);
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (predicate.test(entry)) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	/**
	 * 根据key获得value,若为null,返回defaultValue
	 * 
	 * @param <K> 泛型key
	 * @param <V> 泛型value
	 * @param map map
	 * @param k key
	 * @param defaultValue 默认值
	 * @return value或defaultValue
	 */
	public static <K, V> V get(Map<K, V> map, K k, V defaultValue) {
		return isEmpty(map) ? defaultValue : null == map.get(k) ? defaultValue : map.get(k);
	}

	/**
	 * 获取Map指定key的值,并转换为Bool,若值为null,返回false
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Boolean getBool(Map<?, ?> map, Object key) {
		return getBool(map, key, false);
	}

	/**
	 * 获取Map指定key的值,并转换为Bool
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Boolean getBool(Map<?, ?> map, Object key, Boolean defaultValue) {
		return isEmpty(map) ? defaultValue : BooleanHelper.toBool(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为Character,若值为null,返回0的char值
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Character getChar(Map<?, ?> map, Object key) {
		return getChar(map, key, (char) 0);
	}

	/**
	 * 获取Map指定key的值，并转换为Character
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Character getChar(Map<?, ?> map, Object key, Character defaultValue) {
		return isEmpty(map) ? defaultValue : CharHelper.toChar(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为Double,若值为null,返回0
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Double getDouble(Map<?, ?> map, Object key) {
		return getDouble(map, key, 0.0);
	}

	/**
	 * 获取Map指定key的值,并转换为Double
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Double getDouble(Map<?, ?> map, Object key, Double defaultValue) {
		return isEmpty(map) ? defaultValue : NumberHelper.toDouble(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为Float,若值为null,返回0
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Float getFloat(Map<?, ?> map, Object key) {
		return getFloat(map, key, 0f);
	}

	/**
	 * 获取Map指定key的值,并转换为Float
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Float getFloat(Map<?, ?> map, Object key, Float defaultValue) {
		return isEmpty(map) ? defaultValue : NumberHelper.toFloat(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为Integer,若值为null,返回0
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Integer getInt(Map<?, ?> map, Object key) {
		return getInt(map, key, 0);
	}

	/**
	 * 获取Map指定key的值,并转换为Integer
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Integer getInt(Map<?, ?> map, Object key, Integer defaultValue) {
		return isEmpty(map) ? defaultValue : NumberHelper.toInt(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为Long,若值为null,返回0
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Long getLong(Map<?, ?> map, Object key) {
		return getLong(map, key, 0l);
	}

	/**
	 * 获取Map指定key的值,并转换为Long
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Long getLong(Map<?, ?> map, Object key, Long defaultValue) {
		return isEmpty(map) ? defaultValue : NumberHelper.toLong(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为Short,若值为null,返回0
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static Short getShort(Map<?, ?> map, Object key) {
		return getShort(map, key, (short) 0);
	}

	/**
	 * 获取Map指定key的值,并转换为Short
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static Short getShort(Map<?, ?> map, Object key, Short defaultValue) {
		return isEmpty(map) ? defaultValue : NumberHelper.toShort(map.get(key), defaultValue);
	}

	/**
	 * 获取Map指定key的值,并转换为字符串
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 */
	public static String getStr(Map<?, ?> map, Object key) {
		return getStr(map, key);
	}

	/**
	 * 获取Map指定key的值,并转换为字符串
	 *
	 * @param map Map
	 * @param key 键
	 * @param defaultValue 默认值
	 * @return 值
	 */
	public static String getStr(Map<?, ?> map, Object key, String defaultValue) {
		return isEmpty(map) ? defaultValue : StrHelper.toString(map.get(key), defaultValue);
	}

	/**
	 * 判断map为null和size为0
	 * 
	 * @param <K> 泛型
	 * @param <V> 泛型
	 * @param map map
	 * @return true当map为null或size为0
	 */
	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return null == map || map.isEmpty();
	}

	/**
	 * 判断map不为null和size不为0
	 * 
	 * @param <K> 泛型
	 * @param <V> 泛型
	 * @param map map
	 * @return true当map不为null和size不为0
	 */
	public static <K, V> boolean isNotEmpty(Map<K, V> map) {
		return !isEmpty(map);
	}

	/**
	 * 新建一个线程安全的key,value都非null的ConcurrentHashMap
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<>(16);
	}

	/**
	 * 新建一个线程安全的key,value都非null的ConcurrentHashMap
	 *
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param size 初始大小,由于默认负载因子0.75,传入的size会实际初始大小为size / 0.75 + 1
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int size) {
		return new ConcurrentHashMap<>((int) (size / DEFAULT_LOAD_FACTOR) + 1);
	}

	/**
	 * 新建一个线程安全的TreeMap
	 * 
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @return ConcurrentSkipListMap
	 */
	public static <K, V> ConcurrentSkipListMap<K, V> newConcurrentSkipListMap() {
		return new ConcurrentSkipListMap<>();
	}

	/**
	 * 新建一个线程安全的TreeMap
	 * 
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param comparator key比较器
	 * @return ConcurrentSkipListMap
	 */
	public static <K, V> ConcurrentSkipListMap<K, V> newConcurrentSkipListMap(Comparator<? super K> comparator) {
		return new ConcurrentSkipListMap<>(comparator);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>(16);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小,由于默认负载因子0.75,传入的size会实际初始大小为size / 0.75 + 1
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return new HashMap<>((int) (size / DEFAULT_LOAD_FACTOR) + 1);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param k 任意的k值
	 * @param v 任意的v值
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(K k, V v) {
		return new HashMap<K, V>(16);
	}

	/**
	 * 新建一个LinkedHashMap
	 *
	 * @param <K> key类型
	 * @param <V> value类型
	 * @return LinkedHashMap
	 */
	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<>(16);
	}

	/**
	 * 新建一个LinkedHashMap
	 * 
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param size 初始大小,由于默认负载因子0.75,传入的size会实际初始大小为size / 0.75 + 1
	 * @return LinkedHashMap
	 */
	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(int size) {
		return new LinkedHashMap<>((int) (size / DEFAULT_LOAD_FACTOR) + 1);
	}

	/**
	 * 新建TreeMap,Key有序的Map,默认key升序
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return TreeMap
	 */
	public static <K extends Comparable<?>, V> TreeMap<K, V> newTreeMap() {
		return new TreeMap<>();
	}

	/**
	 * 新建TreeMap,Key有序的Map,默认key升序
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param comparator Key比较器
	 * @return TreeMap
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
		return new TreeMap<>(comparator);
	}

	/**
	 * 将单一键值对转换为Map
	 *
	 * @param <K> key类型
	 * @param <V> 值类型
	 * @param key 键
	 * @param value 值
	 * @return {@link HashMap}
	 */
	public static <K, V> HashMap<K, V> of(K key, V value) {
		final HashMap<K, V> map = newHashMap();
		map.put(key, value);
		return map;
	}

	/**
	 * 将单一键值对转换为Map
	 * 
	 * @param <K> key类型
	 * @param <V> 值类型
	 * @param key1 键
	 * @param value1 值
	 * @param key2 键
	 * @param value2 值
	 * @return {@link HashMap}
	 */
	public static <K, V> HashMap<K, V> of(K key1, V value1, K key2, V value2) {
		final HashMap<K, V> map = newHashMap();
		map.put(key1, value1);
		map.put(key2, value2);
		return map;
	}

	/**
	 * 将Map中的键值进行交换
	 * 
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param map Map
	 * @return {@link HashMap}
	 */
	public static <K, V> Map<V, K> reverse(final Map<K, V> map) {
		if (null != map) {
			final Map<V, K> out = new HashMap<>(map.size());
			for (final Entry<K, V> entry : map.entrySet()) {
				out.put(entry.getValue(), entry.getKey());
			}
		}
		return null;
	}

	/**
	 * 给Map排序,一般用于对value进行排序,若对key排序,可直接使用TreeMap
	 * 
	 * @param <K> 泛型
	 * @param <V> 泛型
	 * @param map 需要排序的Map
	 * @param comparator 排序算法
	 * @return LinkedHashMap
	 */
	public static <K, V> LinkedHashMap<K, V> sort(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
		AssertHelper.notEmpty(map);
		AssertHelper.notNull(comparator);
		List<Map.Entry<K, V>> entrys = new ArrayList<>(map.entrySet());
		Collections.sort(entrys, comparator);
		LinkedHashMap<K, V> linkedHashMap = newLinkedHashMap();
		for (Entry<K, V> entry : entrys) {
			linkedHashMap.put(entry.getKey(), entry.getValue());
		}
		return linkedHashMap;
	}

	/**
	 * 将{@link Properties}转化为Map
	 * 
	 * @param properties properties
	 * @return Map
	 */
	public static Map<String, String> toMap(Properties properties) {
		HashMap<String, String> hashMap = newHashMap();
		for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			hashMap.put(key, properties.getProperty(key));
		}
		return hashMap;
	}

	/**
	 * 迭代器的所有元素变成Map的key,而value则通过key计算得来
	 * 
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param keys key集合
	 * @param valueFunction value生成接口
	 * @return {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
		return toMap(keys.iterator(), valueFunction);
	}

	/**
	 * 迭代器的所有元素变成Map的key,而value则通过key计算得来
	 * 
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param keys key集合
	 * @param valueFunction value生成接口
	 * @return {@link LinkedHashMap}
	 */
	public static <K, V> Map<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
		Objects.requireNonNull(valueFunction);
		Map<K, V> builder = new LinkedHashMap<>();
		while (keys.hasNext()) {
			K key = keys.next();
			builder.put(key, valueFunction.apply(key));
		}
		return builder;
	}

	/**
	 * 将{@link ResourceBundle}转换为Map
	 *
	 * @param resourceBundle 需要转换的资源
	 * @return {@link HashMap}
	 */
	public static Map<String, Object> toMap(final ResourceBundle resourceBundle) {
		if (null != resourceBundle) {
			final Enumeration<String> enumeration = resourceBundle.getKeys();
			final Map<String, Object> map = new HashMap<>();
			while (enumeration.hasMoreElements()) {
				final String key = enumeration.nextElement();
				final Object value = resourceBundle.getObject(key);
				map.put(key, value);
			}
		}
		return null;
	}

	/**
	 * 将Map转换为{@link Properties}
	 * 
	 * @param <K> key类型
	 * @param <V> value类型
	 * @param map Map
	 * @return {@link Properties}
	 */
	public static <K, V> Properties toProperties(final Map<K, V> map) {
		final Properties properties = new Properties();
		if (map != null) {
			for (final Entry<K, V> entry2 : map.entrySet()) {
				final Map.Entry<?, ?> entry = entry2;
				final Object key = entry.getKey();
				final Object value = entry.getValue();
				properties.put(key, value);
			}
		}
		return properties;
	}

	public static MapBuilder builder() {
		return new MapBuilder();
	}

	public static MapBuilder builder(String key, Object val) {
		return new MapBuilder(key, val);
	}

	public static <K, V> MapBuilderKV<K, V> builderKV() {
		return new MapBuilderKV<K, V>();
	}

	public static <K, V> MapBuilderKV<K, V> builderKV(K key, V val) {
		return new MapBuilderKV<K, V>();
	}

	public static MapConcurrentBuilder builderConcurrent() {
		return new MapConcurrentBuilder();
	}

	public static MapConcurrentBuilder builderConcurrent(String key, Object val) {
		return new MapConcurrentBuilder(key, val);
	}

	public static class MapBuilder {

		private Map<String, Object> map = new HashMap<>();

		public MapBuilder() {

		}

		public MapBuilder(String key, Object val) {
			map.put(key, val);
		}

		public MapBuilder put(String key, Object val) {
			map.put(key, val);
			return this;
		}

		public Map<String, Object> build() {
			return map;
		}
	}

	public static <K, V> MapBuilderKV<K, V> builder(K key, V val) {
		return new MapBuilderKV<K, V>(key, val);
	}

	public static class MapBuilderKV<K, V> {

		private Map<K, V> map = new HashMap<>();

		public MapBuilderKV() {

		}

		public MapBuilderKV(K key, V val) {
			map.put(key, val);
		}

		public MapBuilderKV<K, V> put(K key, V val) {
			map.put(key, val);
			return this;
		}

		public Map<K, V> build() {
			return map;
		}
	}

	public static class MapConcurrentBuilder {

		private Map<String, Object> map = new ConcurrentHashMap<>();

		public MapConcurrentBuilder() {

		}

		public MapConcurrentBuilder(String key, Object val) {
			AssertHelper.notBlank(key);
			AssertHelper.notNull(val);
			map.put(key, val);
		}

		public MapConcurrentBuilder put(String key, Object val) {
			AssertHelper.notBlank(key);
			AssertHelper.notNull(val);
			map.put(key, val);
			return this;
		}

		public Map<String, Object> build() {
			return map;
		}
	}

	public static <K, V> MapGenericBuilder<K, V> builderGeneric(K key, V val) {
		return new MapGenericBuilder<K, V>(key, val);
	}

	public static class MapGenericBuilder<K, V> {

		private Map<K, V> map = new HashMap<>();

		public MapGenericBuilder(K key, V val) {
			map.put(key, val);
		}

		public MapGenericBuilder<K, V> put(K key, V val) {
			map.put(key, val);
			return this;
		}

		public Map<K, V> build() {
			return map;
		}
	}
}

class Examples {

	/**
	 * 将一个List转换为Map
	 * 
	 * {@link Map#merge}:merge源码,2个参数的前一个参数是Map中的旧值,第二个是新值
	 * 
	 * @param entitys 需要进行转换的集合
	 * @return 转换后的Map
	 */
	@Example
	public static Map<Long, HeavenSong> merge(List<HeavenSong> entitys) {
		HeavenSong entity1 = new HeavenSong();
		entity1.setUserId(1l);
		entity1.setSalary(1000.0);
		entitys.add(entity1);
		HeavenSong entity2 = new HeavenSong();
		entity2.setUserId(2l);
		entity2.setSalary(2000.0);
		entitys.add(entity2);
		HashMap<Long, HeavenSong> newHashMap = MapHelper.newHashMap();
		// merge源码参见{@link Map#merge}
		// 第一个参数是需要在newHashMap中查找的的key
		// 第二个参数是要放入到newHashMap中的新的value
		// 第三个参数是当newHashMap中的旧值存在时,旧值和新值的处理方法:m是newHashMap中的旧值,n是新值,
		entitys.stream().forEach(t -> newHashMap.merge(t.getUserId(), t, (m, n) -> null));
		System.out.println(newHashMap);
		return newHashMap;
	}

	@Example
	public static Map<Long, List<HeavenSong>> mergeList(List<HeavenSong> entitys) {
		HeavenSong entity1 = new HeavenSong();
		entity1.setUserId(1l);
		entity1.setSalary(1000.0);
		entitys.add(entity1);
		HeavenSong entity2 = new HeavenSong();
		entity2.setUserId(2l);
		entity2.setSalary(2000.0);
		entitys.add(entity2);
		HeavenSong entity3 = new HeavenSong();
		entity3.setUserId(1l);
		entity3.setSalary(3000.0);
		entitys.add(entity3);
		HashMap<Long, List<HeavenSong>> newHashMap = MapHelper.newHashMap();
		// merge源码参见{@link Map#merge}
		// 第一个参数是需要在newHashMap中查找的的key
		// 第二个参数是要放入到newHashMap中的新的value
		// 第三个参数是当newHashMap中的旧值存在时,旧值和新值的处理方法:m是newHashMap中的旧值,n是新值,
		entitys.forEach(t -> newHashMap.merge(t.getUserId(), new ArrayList<>(Arrays.asList(t)), (m, n) -> {
			m.addAll(n);
			return m;
		}));
		System.out.println(newHashMap);
		return newHashMap;
	}
}