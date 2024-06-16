package dream.flying.flower.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.reflect.ReflectHelper;

/**
 * Bean工具类 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-10 16:27:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class BeanHelper {

	/**
	 * 实体类转Map,实体类必须是严格按照java的set,get方式生成的,否则报错
	 * 
	 * @param <T> 泛型
	 * @param t 对象实例
	 * @return Map
	 */
	public static <T> Map<String, Object> beanToMap(T t) {
		try {
			Class<?> clazz = t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			Map<String, Object> map = new HashMap<>();
			for (Field field : fields) {
				ReflectHelper.fixAccessible(field);
				String key = field.getName();
				map.put(key, field.get(t));
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 实体类转Map,实体类必须是严格按照java的set,get方式生成的,否则报错
	 * 
	 * @param <T> 泛型
	 * @param t 对象实例
	 * @param targetMap 结果
	 * @param isToUnderlineCase 是否转下划线,true->是,false->否
	 * @param ignoreNullValue 是否忽略null,true->是,false->否
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T t, Map<String, Object> targetMap, final boolean isToUnderlineCase,
			boolean ignoreNullValue) {
		if (t == null) {
			return null;
		}
		try {
			Class<?> clazz = t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			Map<String, Object> map = new HashMap<>();
			for (Field field : fields) {
				ReflectHelper.fixAccessible(field);
				String key = field.getName();
				Object object = field.get(t);
				if (ignoreNullValue && null == object) {
					continue;
				}
				if (isToUnderlineCase) {
					map.put(StrHelper.hump2Underline(key), object);
				} else {
					map.put(key, object);
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断是一个bean,含有getter,setter
	 * 
	 * @param clazz 类
	 * @return ture->是,false->否
	 */
	public static boolean isBean(Class<?> clazz) {
		final Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Map转换成实体类
	 * 
	 * @param map 待转换的Map
	 * @param clazz 转换后的class
	 * @return T 转换后的实体
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static <T> T mapToBean(Object map, Class<T> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (null == map) {
			return null;
		}
		if (!(map instanceof Map)) {
			throw new ClassCastException("this map can't be cast to map");
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> res = (Map<String, Object>) map;
		Field[] fields = clazz.getDeclaredFields();
		T t = clazz.getDeclaredConstructor().newInstance();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(t, res.get(field.getName()));
		}
		return t;
	}

	/**
	 * Map转换成实体类
	 * 
	 * @param map 待转换的Map
	 * @param clazz 转换后的class
	 * @return T 转换后的实体
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		if (MapHelper.isEmpty(map)) {
			return null;
		}
		Field[] fields = clazz.getDeclaredFields();
		T t = clazz.getDeclaredConstructor().newInstance();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(t, map.get(field.getName()));
		}
		return t;
	}
}