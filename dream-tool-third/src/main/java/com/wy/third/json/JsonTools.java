package com.wy.third.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;
import com.wy.util.CharsetTool;

/**
 * FastJson工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-11 22:20:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JsonTools {

	/** 序列化特性 */
	private static final SerializerFeature[] DEFAULT_SERIALIZER_FEATURES = { SerializerFeature.NotWriteRootClassName,
			// 将key输出为字符串
			SerializerFeature.WriteNonStringKeyAsString,
			// 无默认值
			SerializerFeature.NotWriteDefaultValue,
			// 禁止循环引用检测
			SerializerFeature.DisableCircularReferenceDetect };

	/** 格式化输出 */
	private static final SerializerFeature[] FORMAT_SERIALIZER_FEATURES = { SerializerFeature.NotWriteRootClassName,
			// 格式化输出
			SerializerFeature.PrettyFormat,
			// 将key输出为字符串
			SerializerFeature.WriteNonStringKeyAsString,
			// 无默认值
			SerializerFeature.NotWriteDefaultValue,
			// 禁止循环引用检测
			SerializerFeature.DisableCircularReferenceDetect };

	/**
	 * JSON将对象序列化为字符串
	 * 
	 * @param object 需要转化的对象
	 * @return json字符串
	 */
	public static String toJson(Object object) {
		AssertTool.notNull(object, "The data must not be null");
		return JSON.toJSONString(object, DEFAULT_SERIALIZER_FEATURES);
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
		return JSON.toJSONString(object, filter, DEFAULT_SERIALIZER_FEATURES);
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
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(
				filterNames.toArray(new String[filterNames.size()]));
		return JSON.toJSONString(object, filter, DEFAULT_SERIALIZER_FEATURES);
	}

	/**
	 * 将json字符串转换为List<T>
	 * 
	 * @param <T> 类泛型
	 * @param json json字符串
	 * @param clazz 需要转换的类
	 * @return List<T>
	 */
	public static <T> List<T> parseList(String json, Class<T> clazz) {
		AssertTool.notNull(clazz, "The class must not be null");
		return JSON.parseArray(AssertTool.notBlank(json).toString(), clazz);
	}

	/**
	 * 将json字符串转换为List<Map<String, Object>>
	 * 
	 * @param json json字符串
	 * @return List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> parseListMap(String json) {
		return JSON.parseObject(AssertTool.notBlank(json).toString(), new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * 将json字符串转换为List<Map<K, V>>
	 * 
	 * @param <K> key泛型
	 * @param <V> value泛型
	 * @param json json字符串
	 * @param empty 一个空的Map<K,V>,为了确定转出的泛型正确
	 * @return List<Map<K, V>>
	 */
	public static <K, V> List<Map<K, V>> parseListMap(String json, Map<K, V> empty) {
		return JSON.parseObject(AssertTool.notBlank(json).toString(), new TypeReference<List<Map<K, V>>>() {
		});
	}

	/**
	 * JSON将序列化字符串转换为Map<String, Object>
	 * 
	 * @param json json字符串
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> parseMap(String json) {
		return JSON.parseObject(AssertTool.notBlank(json).toString(), new TypeReference<Map<String, Object>>() {
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
	public static <K, V> Map<K, V> parseMap(String json, Map<K, V> empty) {
		return JSON.parseObject(AssertTool.notBlank(json).toString(), new TypeReference<Map<K, V>>() {
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
		write(os, object, CharsetTool.defaultCharset(), false);
	}

	/**
	 * 将json数据写入到输出流中,需要调用者关闭流
	 * 
	 * @param os 字节输出流
	 * @param object json数据
	 * @param charset 字符编码
	 * @throws IOException
	 */
	public static void write(OutputStream os, Object object, Charset charset) throws IOException {
		write(os, object, CharsetTool.defaultCharset(charset), false);
	}

	/**
	 * 将json数据写入到输出流中,需要调用者关闭流
	 * 
	 * @param os 字节输出流
	 * @param object json数据
	 * @param charsetName 字符编码名
	 * @throws IOException
	 */
	public static void write(OutputStream os, Object object, String charsetName) throws IOException {
		write(os, object, CharsetTool.defaultCharset(charsetName), false);
	}

	/**
	 * 将json数据写入到输出流中,需要调用者关闭流
	 * 
	 * @param os 字节输出流
	 * @param object json数据
	 * @param charset 字符编码
	 * @throws IOException
	 */
	public static void write(OutputStream os, Object object, Charset charset, boolean pretty) throws IOException {
		AssertTool.notNull(object, "The data must not be null");
		if (pretty) {
			JSON.writeJSONString(os, CharsetTool.defaultCharset(charset), object, FORMAT_SERIALIZER_FEATURES);
		} else {
			JSON.writeJSONString(os, CharsetTool.defaultCharset(charset), object, DEFAULT_SERIALIZER_FEATURES);
		}
	}

	/**
	 * 将json数据写入到输出流中,需要调用者关闭流
	 * 
	 * @param os 字节输出流
	 * @param object json数据
	 * @param charset 字符编码
	 * @throws IOException
	 */
	public static void write(OutputStream os, Object object, String charsetName, boolean pretty) throws IOException {
		write(os, object, CharsetTool.defaultCharset(charsetName), pretty);
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

	/**
	 * 将json数据以字符形式写入到输出流中,流自动关闭
	 * 
	 * @param file 文件
	 * @param object json数据
	 * @throws IOException
	 */
	public static void writeString(File file, Object object) throws IOException {
		try (BufferedWriter bufferedWriter = FileTool.newBufferedWriter(file);) {
			writeString(bufferedWriter, object, CharsetTool.defaultCharset());
		}
	}

	/**
	 * 将json数据以字符形式写入到输出流中,需要调用者关闭流
	 * 
	 * @param writer 字符输出流
	 * @param object json数据
	 * @throws IOException
	 */
	public static void writeString(Writer writer, Object object) throws IOException {
		writeString(writer, object, CharsetTool.defaultCharset());
	}

	/**
	 * 将json数据以字符形式写入到输出流中,需要调用者关闭流
	 * 
	 * @param writer 字符输出流
	 * @param object json数据
	 * @param charset 字符编码
	 * @throws IOException
	 */
	public static void writeString(Writer writer, Object object, Charset charset) throws IOException {
		AssertTool.notNull(object, "The data must not be null");
		JSON.writeJSONString(writer, object, DEFAULT_SERIALIZER_FEATURES);
	}
}