package dream.flying.flower.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Predicate;

import dream.flying.flower.common.CodeMsg;
import dream.flying.flower.common.NameMsg;
import dream.flying.flower.common.StatusMsg;

/**
 * 枚举状态工具类
 *
 * @author 飞花梦影
 * @date 2022-03-17 16:49:41
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CodeMsgHelper {

	/**
	 * 根据枚举获得值
	 * 
	 * @param <E> 枚举类型
	 * @param code 枚举code值
	 * @param enumClass 枚举字节码
	 * @return 枚举或null
	 */
	public static <E extends Enum<E> & CodeMsg> E getEnum(int code, Class<E> enumClass) {
		List<E> list = toList(enumClass);
		for (E e : list) {
			if (e.getValue().intValue() == code) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据枚举获得值
	 * 
	 * @param <E> 枚举类型
	 * @param code 枚举code值
	 * @param enumClass 枚举字节码
	 * @return 枚举或null
	 */
	public static <E extends Enum<E> & StatusMsg<String>> E getEnum(String code, Class<E> enumClass) {
		List<E> list = toList(enumClass);
		for (E e : list) {
			if (code.equals(e.getValue())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 根据枚举获得msg
	 * 
	 * @param <E> 枚举类型
	 * @param code 枚举code值
	 * @param enumClass 枚举字节码
	 * @return msg或null
	 */
	public static <E extends Enum<E> & CodeMsg> String getMsg(int code, Class<E> enumClass) {
		List<E> list = toList(enumClass);
		for (E e : list) {
			if (e.getValue().intValue() == code) {
				return e.getMsg();
			}
		}
		return null;
	}

	/**
	 * 根据枚举获得name()
	 * 
	 * @param <E> 枚举类型
	 * @param code 枚举code值
	 * @param enumClass 枚举字节码
	 * @return name()或null
	 */
	public static <E extends Enum<E> & CodeMsg> String getName(int code, Class<E> enumClass) {
		List<E> list = toList(enumClass);
		for (E e : list) {
			if (e.getValue().intValue() == code) {
				return e.name();
			}
		}
		return null;
	}

	/**
	 * 将枚举转换为json字符串
	 * 
	 * @param <E> 枚举类型
	 * @param enumClass 枚举字节码
	 * @return json字符串
	 */
	public static <E extends Enum<E> & NameMsg> String toJsonStr(Class<E> enumClass) {
		List<E> list = toList(enumClass);
		StringJoiner sj = new StringJoiner(",", "[", "]");
		list.forEach(t -> {
			sj.add("{id:'")
					.add(t.toString())
					.add("',msg:'")
					.add(t.getMsg())
					.add("',code:'")
					.add(t.getValue() + "")
					.add("',name:'")
					.add(t.name())
					.add("'}");
		});
		return sj.toString();
	}

	/**
	 * 将枚举转换为List,每个枚举项是一个元素
	 * 
	 * @param <E> 枚举类型
	 * @param enumClass 枚举字节码
	 * @return List
	 */
	public static <E extends Enum<E> & StatusMsg<?>> List<E> toList(Class<E> enumClass) {
		return enumClass.isEnum()
				? new ArrayList<>(Arrays.asList(enumClass.getEnumConstants())) : Collections.emptyList();
	}

	/**
	 * 将枚举转换为List<Map>,每个枚举项都是一个Map
	 * 
	 * @param <E> 枚举类型
	 * @param enumClass 枚举字节码
	 * @return List<Map>
	 */
	public static <E extends Enum<E> & StatusMsg<?>> List<Map<String, Object>> toListMap(Class<E> enumClass) {
		List<E> list = toList(enumClass);
		List<Map<String, Object>> ret = new ArrayList<>();
		list.forEach(t -> {
			Map<String, Object> map = new HashMap<>();
			map.put("code", t.getValue());
			map.put("msg", t.getMsg());
			map.put("name", t.name());
			ret.add(map);
		});
		return ret;
	}

	/**
	 * 将枚举转换为List<Map>,每个枚举项都是一个Map
	 * 
	 * @param <E> 枚举类型
	 * @param enumClass 枚举字节码
	 * @param predicate 过滤条件
	 * @return List<Map>
	 */
	public static <E extends Enum<E> & NameMsg> List<Map<String, Object>> toListMap(Class<E> enumClass,
			Predicate<E> predicate) {
		List<E> list = toList(enumClass);
		List<Map<String, Object>> ret = new ArrayList<>();
		list.stream().filter(predicate).forEach(t -> {
			Map<String, Object> map = new HashMap<>();
			map.put("code", t.getValue());
			map.put("msg", t.getMsg());
			map.put("name", t.name());
			ret.add(map);
		});
		return ret;
	}

	/**
	 * 将枚举转换为Map,其中key为枚举的code,code可能重复
	 * 
	 * @param <E> 枚举类型
	 * @param enumClass 枚举字节码
	 * @return Map
	 */
	public static <E extends Enum<E> & NameMsg> Map<String, Map<String, Object>> toMapCode(Class<E> enumClass) {
		List<E> list = toList(enumClass);
		Map<String, Map<String, Object>> enumMap = new HashMap<>();
		list.forEach(t -> {
			Map<String, Object> map = new HashMap<>();
			String key = String.valueOf(t.getValue());
			map.put("code", String.valueOf(t.getValue()));
			map.put("msg", t.getMsg());
			map.put("name", t.name());
			enumMap.put(key, map);
		});
		return enumMap;
	}

	/**
	 * 将枚举转换为Map,其中key为枚举的name()
	 * 
	 * @param <E> 枚举类型
	 * @param enumClass 枚举字节码
	 * @return Map
	 */
	public static <E extends Enum<E> & NameMsg> Map<String, Map<String, Object>> toMapName(Class<E> enumClass) {
		List<E> list = toList(enumClass);
		Map<String, Map<String, Object>> enumMap = new HashMap<>();
		list.forEach(t -> {
			Map<String, Object> map = new HashMap<>();
			String key = String.valueOf(t.name());
			map.put("code", String.valueOf(t.getValue()));
			map.put("msg", t.getMsg());
			map.put("name", t.name());
			enumMap.put(key, map);
		});
		return enumMap;
	}
}