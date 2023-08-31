package com.wy.util;

import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wy.collection.CollectionHelper;

/**
 * Stream工具类
 *
 * @author 飞花梦影
 * @date 2023-08-31 09:58:27
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class StreamHelper {

	/**
	 * 将集合中的每个元素转换为流之后再将所有流合并为一个流
	 * 
	 * @param <T> 目标泛型
	 * @param <R> 结果泛型
	 * @param collection 集合
	 * @param function 将集合中的每个元素转换为Stream的方法
	 * @return 新的List
	 */
	public static <T, R> List<R> flatMap2List(Collection<T> collection, Function<T, Stream<R>> function) {
		return collection.stream().flatMap(function).collect(Collectors.toList());
	}

	/**
	 * 将集合中的每个元素转换为流之后再将所有流合并为一个流
	 * 
	 * @param <T> 目标泛型
	 * @param <R> 结果泛型
	 * @param collection 集合
	 * @param function 将集合中的每个元素转换为Stream的方法
	 * @return 新的Set
	 */
	public static <T, R> Set<R> flatMap2Set(Collection<T> collection, Function<T, Stream<R>> function) {
		return collection.stream().flatMap(function).collect(Collectors.toSet());
	}

	/**
	 * 根据function分组
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 第一个分组条件
	 * @return Map<R, List<T>>
	 */
	public static <T, R> Map<R, List<T>> group(Collection<T> collection, Function<T, R> function) {
		return collection.stream().collect(Collectors.groupingBy(function));
	}

	/**
	 * 根据function分组,分组后的结果再根据innerFunction进行分组
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 第一个分组条件
	 * @param innerFunction 第二个分组条件
	 * @return Map<R, Map<G, List<T>>>
	 */
	public static <T, R, G> Map<R, Map<G, List<T>>> group(Collection<T> collection, Function<T, R> function,
			Function<T, G> innerFunction) {
		return collection.stream().collect(Collectors.groupingBy(function, Collectors.groupingBy(innerFunction)));
	}

	/**
	 * 根据true/false进行分组
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param predicate 分组条件
	 * @return Map<Boolean, List<T>>
	 */
	public static <T> Map<Boolean, List<T>> partition(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().collect(Collectors.partitioningBy(predicate));
	}

	/**
	 * 求和
	 * 
	 * @param collection 集合
	 * @return 和
	 */
	public static int sumInt(Collection<Integer> collection) {
		return CollectionHelper.isEmpty(collection) ? 0 : collection.stream().reduce(Integer::sum).get().intValue();
	}

	/**
	 * 求和
	 * 
	 * @param collection 集合
	 * @return 和
	 */
	public static long sumLong(Collection<Long> collection) {
		return CollectionHelper.isEmpty(collection) ? 0L : collection.stream().reduce(Long::sum).get().longValue();
	}

	/**
	 * 求Double集合的最大值,最小值,平均值,总和
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 返回Double的函数方法
	 * @return 统计对象
	 */
	public static <T> DoubleSummaryStatistics summaryDouble(Collection<T> collection, Function<T, Double> function) {
		return CollectionHelper.isEmpty(collection) ? new DoubleSummaryStatistics()
				: collection.stream().collect(Collectors.summarizingDouble(function::apply));
	}

	/**
	 * 求Double集合的最大值,最小值,平均值,总和
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 返回Double的函数方法
	 * @return 统计对象
	 */
	public static <T> DoubleSummaryStatistics summaryDouble(Collection<T> collection, ToDoubleFunction<T> function) {
		return CollectionHelper.isEmpty(collection) ? new DoubleSummaryStatistics()
				: collection.stream().collect(Collectors.summarizingDouble(function));
	}

	/**
	 * 求int集合的最大值,最小值,平均值,总和
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 返回Integer的函数方法
	 * @return 统计对象
	 */
	public static <T> IntSummaryStatistics summaryInt(Collection<T> collection, Function<T, Integer> function) {
		return CollectionHelper.isEmpty(collection) ? new IntSummaryStatistics()
				: collection.stream().collect(Collectors.summarizingInt(function::apply));
	}

	/**
	 * 求int集合的最大值,最小值,平均值,总和
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 返回Integer的函数方法
	 * @return 统计对象
	 */
	public static <T> IntSummaryStatistics summaryInt(Collection<T> collection, ToIntFunction<T> function) {
		return CollectionHelper.isEmpty(collection) ? new IntSummaryStatistics()
				: collection.stream().collect(Collectors.summarizingInt(function));
	}

	/**
	 * 求long集合的最大值,最小值,平均值,总和
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 返回Long的函数方法
	 * @return 统计对象
	 */
	public static <T> LongSummaryStatistics summaryLong(Collection<T> collection, Function<T, Long> function) {
		return CollectionHelper.isEmpty(collection) ? new LongSummaryStatistics()
				: collection.stream().collect(Collectors.summarizingLong(function::apply));
	}

	/**
	 * 求long集合的最大值,最小值,平均值,总和
	 * 
	 * @param <T> 泛型
	 * @param collection 集合
	 * @param function 返回Long的函数方法
	 * @return 统计对象
	 */
	public static <T> LongSummaryStatistics summaryLong(Collection<T> collection, ToLongFunction<T> function) {
		return CollectionHelper.isEmpty(collection) ? new LongSummaryStatistics()
				: collection.stream().collect(Collectors.summarizingLong(function));
	}

	/**
	 * 将集合中的元素经过function转换成一个新的集合
	 * 
	 * @param <T> 待转换泛型
	 * @param <R> 结果泛型
	 * @param collection 原始集合
	 * @param function 转换方法
	 * @return 新的集合
	 */
	public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> function) {
		return collection.stream().map(function).collect(Collectors.toList());
	}

	/**
	 * 将集合中的元素根据某个值转换为Map,其中key需要指定,value为原始元素,若同key,后面的元素覆盖前面的元素
	 * 
	 * @param <T> 待转换泛型
	 * @param <K> 转换后Map的key类型
	 * @param collection 原始集合
	 * @param functionKey Map中key的转换方法
	 * @return Map<K, T>
	 */
	public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> functionKey) {
		return collection.stream().collect(Collectors.toMap(functionKey, t -> t, (p, n) -> null == n ? p : n));
	}

	/**
	 * 将集合中的元素根据某个值转换为Map,其中key和value的转换方式都需要指定
	 * 
	 * @param <T> 待转换泛型
	 * @param <K> 转换后Map的K类型
	 * @param <V> 转换后Map的value类型
	 * @param collection 原始集合
	 * @param functionKey Map中key的转换方法
	 * @param functionValue Map中的value的转换方法
	 * @return Map<K, V>
	 */
	public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<T, K> functionKey,
			Function<T, V> functionValue) {
		return collection.stream().collect(Collectors.toMap(functionKey, functionValue, (p, n) -> null == n ? p : n));
	}

	/**
	 * 将集合中的元素经过function转换成一个新的集合
	 * 
	 * @param <T> 待转换泛型
	 * @param <R> 结果泛型
	 * @param collection 原始集合
	 * @param function 转换方法
	 * @return 新的集合
	 */
	public static <T, R> Set<R> toSet(Collection<T> collection, Function<T, R> function) {
		return collection.stream().map(function).collect(Collectors.toSet());
	}
}