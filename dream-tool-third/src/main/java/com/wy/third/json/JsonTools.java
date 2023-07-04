package com.wy.third.json;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;

import lombok.SneakyThrows;

/**
 * Jackson工具类
 *
 * @author 飞花梦影
 * @date 2023-07-01 22:41:27
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class JsonTools {

	private static final ObjectMapper DEFAULT_OBJECT_MAPPER = JsonMapper.builder()
			// 序列化为字符串时的属性
			// JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
			// JsonInclude.Include.ALWAYS 所有属性,对象属性中有null时,输出null
			// JsonInclude.Include.NON_EMPTY 属性为 空字符串 或者为 NULL 都不序列化
			// JsonInclude.Include.NON_NULL 属性为NULL 不序列化
			.serializationInclusion(JsonInclude.Include.ALWAYS)
			// 反序列化时,遇到未知属性会不会报错:true-遇到没有的属性就报错;false-没有的属性不会管,不会报错
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			// 强制JSON 空字符串("")转换为null对象值
			// .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

			// 转换为格式化的json,美化json
			.enable(SerializationFeature.INDENT_OUTPUT)
			// 序列化空对象的时候,不抛异常
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			// 忽略 transient 修饰的属性
			.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)

			// 修改序列化后日期格式,取消时间戳格式,指定默认序列化格式
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))

			// 处理不同的时区偏移格式
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

			// 允许没有引号的字段名(非标准)出现
			.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
			// 允许单引号(非标准)
			.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
			// 强制转义非ASCII字符
			.configure(JsonWriteFeature.ESCAPE_NON_ASCII, false)

			.build();

	// DEFAULT_OBJECT_MAPPER.registerModule(new JavaTimeModule());

	private static final String SERIALIZE_FILTER_ID = "SERIALIZE_FILTER_ID";

	/**
	 * JSON将对象序列化为字符串
	 * 
	 * @param object 需要转化的对象
	 * @return json字符串
	 */
	@SneakyThrows
	public static String toJson(Object object) {
		AssertTool.notNull(object, "The data must not be null");
		return DEFAULT_OBJECT_MAPPER.writeValueAsString(object);
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
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept(filterNames);
		filterProvider.addFilter(SERIALIZE_FILTER_ID, simpleBeanPropertyFilter);
		DEFAULT_OBJECT_MAPPER.setFilterProvider(filterProvider);
		String result = JSON.toJSONString(object);
		filterProvider.removeFilter(SERIALIZE_FILTER_ID);
		return result;
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
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		SimpleBeanPropertyFilter simpleBeanPropertyFilter =
				SimpleBeanPropertyFilter.serializeAllExcept(new HashSet<String>(filterNames));
		filterProvider.addFilter(SERIALIZE_FILTER_ID, simpleBeanPropertyFilter);
		DEFAULT_OBJECT_MAPPER.setFilterProvider(filterProvider);
		String result = JSON.toJSONString(object);
		filterProvider.removeFilter(SERIALIZE_FILTER_ID);
		return result;
	}

	@SneakyThrows
	public static <T> T parse(Object json, Class<T> clazz) {
		return DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				clazz);
	}

	@SneakyThrows
	public static <T> T parse(Object json, Class<T> clazz, String[] filterNames) {
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept(filterNames);
		filterProvider.addFilter(SERIALIZE_FILTER_ID, simpleBeanPropertyFilter);
		DEFAULT_OBJECT_MAPPER.setFilterProvider(filterProvider);
		T result = DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				clazz);
		filterProvider.removeFilter(SERIALIZE_FILTER_ID);
		return result;
	}

	/**
	 * 将json字符串反序列化为List<T>
	 * 
	 * @param <T> 类泛型
	 * @param json json对象
	 * @param clazz 需要转换的类
	 * @return List<T>
	 */
	@SneakyThrows
	public static <T> List<T> parseList(Object json, Class<T> clazz) {
		return Objects.nonNull(json) ? DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				new TypeReference<List<T>>() {
				}) : Collections.emptyList();
	}

	/**
	 * 将json对象转换为List<Map<String, Object>>
	 * 
	 * @param json json对象
	 * @return List<Map<String, Object>>
	 */
	@SneakyThrows
	public static List<Map<String, Object>> parseListMap(Object json) {
		return Objects.nonNull(json) ? DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				new TypeReference<List<Map<String, Object>>>() {
				}) : Collections.emptyList();
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
	@SneakyThrows
	public static <K, V> List<Map<K, V>> parseListMap(Object json, Map<K, V> empty) {
		return Objects.nonNull(json) ? DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				new TypeReference<List<Map<K, V>>>() {
				}) : Collections.emptyList();
	}

	/**
	 * 将json对象转换为List<String>
	 * 
	 * @param json json对象
	 * @param clazz 需要转换的类
	 * @return List<String>
	 */
	@SneakyThrows
	public static List<String> parseListStr(Object json) {
		return Objects.nonNull(json) ? DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				new TypeReference<List<String>>() {
				}) : Collections.emptyList();
	}

	/**
	 * JSON将序列化字符串转换为Map<String, Object>
	 * 
	 * @param json json字符串
	 * @return Map<String, Object>
	 */
	@SneakyThrows
	public static Map<String, Object> parseMap(Object json) {
		return Objects.nonNull(json) ? DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				new TypeReference<Map<String, Object>>() {
				}) : Collections.emptyMap();
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
	@SneakyThrows
	public static <K, V> Map<K, V> parseMap(Object json, Map<K, V> empty) {
		return Objects.nonNull(json) ? DEFAULT_OBJECT_MAPPER.readValue(
				String.class == json.getClass() ? (String) json : DEFAULT_OBJECT_MAPPER.writeValueAsString(json),
				new TypeReference<Map<K, V>>() {
				}) : Collections.emptyMap();
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
		DEFAULT_OBJECT_MAPPER.writeValue(os, object);
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

	@SneakyThrows
	public static void writeTree(String treeStr, String nodeName) {
		// 将Json串以树状结构读入内存
		JsonNode node = DEFAULT_OBJECT_MAPPER.readTree(treeStr);
		// 得到指定节点下的信息
		JsonNode contents = node.get(nodeName);
		// 遍历nodeName下的信息,size()函数可以得节点所包含的的信息的个数,类似于数组的长度
		for (int i = 0; i < contents.size(); i++) {
			// 读取节点下的某个子节点的值
			System.out.println(contents.get(i).get("objectID").intValue());
			JsonNode geoNumber = contents.get(i).get("geoPoints");
			// 循环遍历子节点下的信息
			for (int j = 0; j < geoNumber.size(); j++) {
				System.out.println(
						geoNumber.get(j).get("x").doubleValue() + "  " + geoNumber.get(j).get("y").doubleValue());
			}
		}
	}
}