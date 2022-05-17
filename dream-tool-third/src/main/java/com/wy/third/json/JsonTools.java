package com.wy.third.json;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;

/**
 * FastJson工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-11 22:20:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JsonTools {

	/** 格式化输出 */
	private static final JSONWriter.Feature[] FORMAT_SERIALIZER_FEATURES = { JSONWriter.Feature.NotWriteRootClassName,
	        // 格式化输出
	        JSONWriter.Feature.PrettyFormat,
	        // 无默认值
	        JSONWriter.Feature.NotWriteDefaultValue };

	/**
	 * JSON将对象序列化为字符串
	 * 
	 * @param object 需要转化的对象
	 * @return json字符串
	 */
	public static String toJson(Object object) {
		AssertTool.notNull(object, "The data must not be null");
		return JSON.toJSONString(object, FORMAT_SERIALIZER_FEATURES);
	}

	/**
	 * JSON将对象序列化为字符串
	 * 
	 * @param object 需要转化的对象
	 * @param filterNames 不进行序列化的字段
	 * @return json字符串
	 */
	public static String toJson(Object object, String[] filterNames) {
		AssertTool.notNull(object, "The data must not be null");
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(filterNames);
		return JSON.toJSONString(object, filter, FORMAT_SERIALIZER_FEATURES);
	}

	/**
	 * JSON将对象序列化为字符串
	 * 
	 * @param object 需要转化的对象
	 * @param filterNames 不进行序列化的字段
	 * @return json字符串
	 */
	public static String toJson(Object object, List<String> filterNames) {
		AssertTool.notNull(object, "The data must not be null");
		SimplePropertyPreFilter filter =
		        new SimplePropertyPreFilter(filterNames.toArray(new String[filterNames.size()]));
		return JSON.toJSONString(object, filter, FORMAT_SERIALIZER_FEATURES);
	}

	/**
	 * 将json对象转换为List<T>
	 * 
	 * @param <T> 类泛型
	 * @param json json对象
	 * @param clazz 需要转换的类
	 * @return List<T>
	 */
	public static <T> List<T> parseList(Object json, Class<T> clazz) {
		if (Objects.isNull(json)) {
			return Collections.emptyList();
		}
		return String.class == json.getClass() ? JSON.parseArray((String) json, clazz)
		        : JSON.parseArray(JSON.toJSONString(json), clazz);
	}

	/**
	 * 将json对象转换为List<Map<String, Object>>
	 * 
	 * @param json json对象
	 * @return List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> parseListMap(Object json) {
		if (Objects.isNull(json)) {
			return Collections.emptyList();
		}
		return String.class == json.getClass()
		        ? JSON.parseObject((String) json, new TypeReference<List<Map<String, Object>>>() {
		        })
		        : JSON.parseObject(JSON.toJSONString(json), new TypeReference<List<Map<String, Object>>>() {
		        });
	}

	/**
	 * 将json对象转换为List<Map<K, V>>
	 * 
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param json json对象
	 * @param empty 一个空的Map<K,V>,为了确定转出的泛型正确
	 * @return List<Map<K, V>>
	 */
	public static <K, V> List<Map<K, V>> parseListMap(Object json, Map<K, V> empty) {
		if (Objects.isNull(json)) {
			return Collections.emptyList();
		}
		return String.class == json.getClass() ? JSON.parseObject((String) json, new TypeReference<List<Map<K, V>>>() {
		}) : JSON.parseObject(JSON.toJSONString(json), new TypeReference<List<Map<K, V>>>() {
		});
	}

	/**
	 * 将json对象转换为List<String>
	 * 
	 * @param json json对象
	 * @param clazz 需要转换的类
	 * @return List<String>
	 */
	public static List<String> parseListStr(Object json) {
		if (Objects.isNull(json)) {
			return Collections.emptyList();
		}
		return String.class == json.getClass() ? JSON.parseArray((String) json, String.class)
		        : JSON.parseArray(JSON.toJSONString(json), String.class);
	}

	/**
	 * JSON将序列化字符串转换为Map<String, Object>
	 * 
	 * @param json json字符串
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> parseMap(Object json) {
		if (Objects.isNull(json)) {
			return Collections.emptyMap();
		}
		return String.class == json.getClass()
		        ? JSON.parseObject((String) json, new TypeReference<Map<String, Object>>() {
		        })
		        : JSON.parseObject(JSON.toJSONString(json), new TypeReference<Map<String, Object>>() {
		        });
	}

	/**
	 * JSON将序列化字符串转换为Map<K,V>
	 * 
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param json json字符串
	 * @param empty 一个空的Map<K,V>,为了确定转出的泛型正确
	 * @return Map<K, V>
	 */
	public static <K, V> Map<K, V> parseMap(Object json, Map<K, V> empty) {
		if (Objects.isNull(json)) {
			return Collections.emptyMap();
		}
		return String.class == json.getClass() ? JSON.parseObject((String) json, new TypeReference<Map<K, V>>() {
		}) : JSON.parseObject(JSON.toJSONString(json), new TypeReference<Map<K, V>>() {
		});
	}

	/**
	 * 将json数据写入到输出流中,流自动关闭
	 * 
	 * @param file 文件
	 * @param object json数据
	 * @throws IOException
	 */
	public static void write(File file, Object object) throws IOException {
		try (OutputStream os = FileTool.newOutputStream(file);) {
			write(os, object);
		}
	}

	/**
	 * 将json数据写入到输出流中,需要调用者关闭流
	 * 
	 * @param os 字节输出流
	 * @param object json数据
	 * @throws IOException
	 */
	public static void write(OutputStream os, Object object) throws IOException {
		AssertTool.notNull(object, "The data must not be null");
		JSON.writeTo(os, object, FORMAT_SERIALIZER_FEATURES);
	}

	/**
	 * 将json数据写入到输出流中,流自动关闭
	 * 
	 * @param fullPath 文件地址绝对路径
	 * @param object json数据
	 * @throws IOException
	 */
	public static void write(String fullPath, Object object) throws IOException {
		write(FileTool.checkFile(fullPath), object);
	}
}