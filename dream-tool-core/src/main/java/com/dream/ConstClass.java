package com.dream;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Class常量
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:02:12
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstClass {

	/** CGLIB代理的class的分隔符. */
	String CGLIB_CLASS_SEPARATOR = "$$";

	/** 内部类分隔符 */
	char INNER_CLASS_SEPARATOR_CHAR = '$';

	/** 将基本类型名称映射到数组类名中使用的相应缩写 */
	Map<String, String> ABBREVIATION_MAP = new IdentityHashMap<String, String>() {

		private static final long serialVersionUID = 1L;
		{
			put("int", "I");
			put("boolean", "Z");
			put("float", "F");
			put("long", "J");
			put("short", "S");
			put("byte", "B");
			put("double", "D");
			put("char", "C");
		}
	};

	/** 基本类型字符串和基本类型class对应Map */
	Map<String, Class<?>> NAME_PRIMITIVE_MAP = new IdentityHashMap<String, Class<?>>() {

		private static final long serialVersionUID = 1L;
		{
			put("boolean", Boolean.TYPE);
			put("byte", Byte.TYPE);
			put("char", Character.TYPE);
			put("short", Short.TYPE);
			put("int", Integer.TYPE);
			put("long", Long.TYPE);
			put("double", Double.TYPE);
			put("float", Float.TYPE);
			put("void", Void.TYPE);
		}
	};

	/** 基本类型和对应的包装类型的Map */
	Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new IdentityHashMap<Class<?>, Class<?>>() {

		private static final long serialVersionUID = 1L;
		{
			put(Boolean.TYPE, Boolean.class);
			put(Byte.TYPE, Byte.class);
			put(Character.TYPE, Character.class);
			put(Short.TYPE, Short.class);
			put(Integer.TYPE, Integer.class);
			put(Long.TYPE, Long.class);
			put(Double.TYPE, Double.class);
			put(Float.TYPE, Float.class);
			put(Void.TYPE, Void.TYPE);
		}
	};

	/** 包装类和对应的基本类型的Map */
	Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = new IdentityHashMap<Class<?>, Class<?>>(12) {

		private static final long serialVersionUID = 1L;
		{
			for (final Map.Entry<Class<?>, Class<?>> entry : ConstClass.PRIMITIVE_WRAPPER_MAP.entrySet()) {
				put(entry.getValue(), entry.getKey());
			}
		}
	};
}