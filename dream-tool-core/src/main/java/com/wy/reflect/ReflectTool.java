package com.wy.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import com.wy.collection.ListTool;
import com.wy.collection.ListTool.ListBuilder;
import com.wy.lang.AssertTool;
import com.wy.result.ResultException;
import com.wy.util.ArrayTool;

/**
 * Reflect反射工具类 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-03 14:13:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ReflectTool {

	/** 方法缓存,{@link org.springframework.util.ReflectionUtils} */
	private static final Map<Class<?>, Method[]> DECLARED_METHODS_CACHE = new ConcurrentHashMap<>(256);

	/** 字段缓存,{@link org.springframework.util.ReflectionUtils} */
	private static final Map<Class<?>, Field[]> DECLARED_FIELDS_CACHE = new ConcurrentHashMap<>(256);

	private ReflectTool() {}

	/**
	 * 强制修改访问类中的成员权限
	 * 
	 * @param member 类中成员
	 */
	public static void fixAccessible(Member member) {
		fixAccessible(member, true);
	}

	/**
	 * 修改访问类中的成员权限
	 * 
	 * @param member 需要访问的成员
	 * @param force 是否强制修改,true强制
	 * @param ignoreStatic 是否忽略static变量,true忽略
	 * @param ignoreFinal 是否忽略final常量,true忽略
	 */
	public static void fixAccessible(Member member, boolean forceAccess) {
		AssertTool.notNull(member);
		AccessibleObject ao = (AccessibleObject) member;
		try {
			if (ao.isAccessible()) {
				return;
			}
			if (forceAccess && (!Modifier.isPublic(member.getModifiers())
					|| !Modifier.isPublic(member.getDeclaringClass().getModifiers()))) {
				ao.setAccessible(true);
			}
		} catch (SecurityException se) {
			if (!ao.isAccessible()) {
				Class<?> declClass = member.getDeclaringClass();
				throw new IllegalArgumentException("Cannot access " + member + " (from class " + declClass.getName()
						+ "; failed to set access: " + se.getMessage());
			}
		}
	}

	/**
	 * 根据参数类型列表强制获得类中的指定无参构造函数
	 * 
	 * @param type 待查找的类
	 * @return 构造函数
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> Constructor<T> getConstructor(Class<T> type) throws NoSuchMethodException, SecurityException {
		return getConstructor(type, true);
	}

	/**
	 * 根据参数类型列表获得类中的指定无参构造函数
	 * 
	 * @param type 待查找的类
	 * @param forceAccess 是否强制访问所有构造,true是,false只访问public
	 * @return 构造函数
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> Constructor<T> getConstructor(Class<T> type, boolean forceAccess)
			throws NoSuchMethodException, SecurityException {
		AssertTool.notNull(type, "class cannot be null");
		return forceAccess ? type.getDeclaredConstructor(ArrayTool.ARRAY_EMPTY_CLASS)
				: type.getConstructor(ArrayTool.ARRAY_EMPTY_CLASS);
	}

	/**
	 * 根据参数类型列表强制获得类中的指定构造函数
	 * 
	 * @param type 待查找的类
	 * @param parameterTypes 构造函数参数类型列表
	 * @return 构造函数
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> Constructor<T> getConstructor(Class<T> type, Class<?>[] parameterTypes)
			throws NoSuchMethodException, SecurityException {
		return getConstructor(type, true, parameterTypes);
	}

	/**
	 * 根据参数类型列表获得类中的指定构造函数
	 * 
	 * @param type 待查找的类
	 * @param parameterTypes 构造函数参数类型列表
	 * @param forceAccess 是否强制访问所有构造,true是,false只访问public
	 * @return 构造函数
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <T> Constructor<T> getConstructor(Class<T> type, boolean forceAccess, Class<?>[] parameterTypes)
			throws NoSuchMethodException, SecurityException {
		AssertTool.notNull(type, "class cannot be null");
		return forceAccess ? type.getDeclaredConstructor(parameterTypes) : type.getConstructor(parameterTypes);
	}

	/**
	 * 从对象实例中查找指定字段,直到Object,若不找到,返回null
	 * 
	 * @param obj 对象实例
	 * @param fieldName 需要查找的字段铭
	 */
	public static <T> Field getField(T obj, String fieldName) {
		AssertTool.notNull(obj, "Object must not be null");
		return getField(obj.getClass(), fieldName);
	}

	/**
	 * 根据属性名获得类中的属性Field
	 * 
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @return {@link Field}
	 */
	public static Field getField(Class<?> clazz, String fieldName) {
		AssertTool.notNull(clazz, "Class must not be null");
		AssertTool.notBlank(fieldName, "The name of the field must be specified");
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if (fieldName == null || fieldName.equals(field.getName())) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * 递归获得当前类以及父类中所有属性名
	 * 
	 * @param clazz 字节码
	 * @return 属性名集合
	 */
	public static <T> List<String> getFieldNames(Class<T> clazz) {
		return getFieldNames(clazz, true);
	}

	/**
	 * 获得类中所有属性名,可控制是否递归获得父类的Field Name
	 * 
	 * @param clazz 字节码
	 * @param superClass 是否递归查找父类中的属性Field,true递归查找
	 * @return 属性名集合
	 */
	public static <T> List<String> getFieldNames(Class<T> clazz, boolean superClass) {
		return getFieldNames(clazz, superClass, false, false);
	}

	/**
	 * 递归获得当前类以及父类中所有属性名
	 * 
	 * @param clazz 字节码
	 * @param ignoreStatic 是否忽略static变量,true是
	 * @param ignoreFinal 是否忽略final常量,true是
	 * @return 属性名集合
	 */
	public static <T> List<String> getFieldNames(Class<T> clazz, boolean ignoreStatic, boolean ignoreFinal) {
		return getFieldNames(clazz, true, ignoreStatic, ignoreFinal);
	}

	/**
	 * 获得类中所有属性名
	 * 
	 * @param clazz 字节码
	 * @param superClass 是否递归查找父类中的属性Field,true递归查找
	 * @param ignoreStatic 是否忽略static变量,true忽略
	 * @param ignoreFinal 是否忽略final常量,true忽略
	 * @return 属性名集合
	 */
	public static <T> List<String> getFieldNames(Class<T> clazz, boolean superClass, boolean ignoreStatic,
			boolean ignoreFinal) {
		Field[] fields = getDeclaredFields(clazz);
		ListBuilder<String> builder = ListTool.builder();
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			for (Field field : fields) {
				fixAccessible(field);
				if (ignoreStatic && Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (ignoreFinal && Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				builder = builder.add(field.getName());
			}
			searchType = superClass ? searchType.getSuperclass() : null;
		}
		return builder.build();
	}

	/**
	 * 递归获得当前类以及父类中所有属性名,忽略static和final修饰的属性
	 * 
	 * @param clazz 字节码
	 * @return 属性名集合
	 */
	public static <T> List<String> getFieldNamesIgnoreStaticFinal(Class<T> clazz) {
		return getFieldNames(clazz, true, true, true);
	}

	/**
	 * 获得类中所有属性名,忽略static和final修饰的属性
	 * 
	 * @param clazz 字节码
	 * @return 属性名集合
	 */
	public static <T> List<String> getFieldNamesSelfIgnoreStaticFinal(Class<T> clazz) {
		return getFieldNames(clazz, false, true, true);
	}

	/**
	 * 获得类中所有属性Field,递归查找父类中的Field属性
	 * 
	 * @param clazz 字节码
	 * @return 属性集合
	 */
	public static <T> List<Field> getFields(Class<T> clazz) {
		return getFields(clazz, true, false, false);
	}

	/**
	 * 获得类中所有属性Field,可控制是否查询父类
	 * 
	 * @param clazz 字节码
	 * @param superClass 是否递归查找父类中的属性Field,true递归查找
	 * @return 属性集合
	 */
	public static <T> List<Field> getFields(Class<T> clazz, boolean superClass) {
		return getFields(clazz, superClass, false, false);
	}

	/**
	 * 获得类中所有属性Field,递归查找父类中的Field属性
	 * 
	 * @param clazz 字节码
	 * @param ignoreStatic 是否忽略static变量
	 * @param ignoreFinal 是否忽略final常量
	 * @return 属性集合
	 */
	public static <T> List<Field> getFields(Class<T> clazz, boolean ignoreStatic, boolean ignoreFinal) {
		return getFields(clazz, true, ignoreStatic, ignoreFinal);
	}

	/**
	 * 获得类中所有属性Field
	 * 
	 * @param clazz 字节码
	 * @param superClass 是否递归查找父类中的属性Field,true递归查找
	 * @param ignoreStatic 是否忽略static变量,true忽略
	 * @param ignoreFinal 是否忽略final常量,true忽略
	 * @return 属性集合
	 */
	public static <T> List<Field> getFields(Class<T> clazz, boolean superClass, boolean ignoreStatic,
			boolean ignoreFinal) {
		AssertTool.notNull(clazz, "Class must not be null");
		ListBuilder<Field> builder = ListTool.builder();
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if (ignoreStatic && Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (ignoreFinal && Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				builder.add(field);
			}
			searchType = superClass ? searchType.getSuperclass() : null;
		}
		return builder.build();
	}

	/**
	 * 递归获得当前类以及父类中所有属性Field,忽略static和final修饰的属性
	 * 
	 * @param clazz 字节码
	 * @return List<Field>
	 */
	public static <T> List<Field> getFieldsIgnoreStaticFinal(Class<T> clazz) {
		return getFields(clazz, true, true, true);
	}

	/**
	 * 获得当前类中所有Field,忽略static和final修饰的属性
	 * 
	 * @param <T> 泛型
	 * @param clazz 类
	 * @return List<Field>
	 */
	public static <T> List<Field> getFieldsSelfIgnoreStaticFinal(Class<T> clazz) {
		return getFields(clazz, false, true, true);
	}

	/**
	 * 获得类中所有含有给定注解的Field
	 * 
	 * @param clazz 待查找的类
	 * @param annotationCls 注解类
	 * @return 符合条件的集合,可能为null或[]
	 * @throws IllegalArgumentException
	 */
	public static List<Field> getFieldsListByAnnotation(final Class<?> clazz,
			final Class<? extends Annotation> annotationCls) {
		AssertTool.notNull(annotationCls, "The annotation class must not be null");
		final List<Field> allFields = getFields(clazz);
		final List<Field> annotatedFields = new ArrayList<>();
		for (final Field field : allFields) {
			if (field.getAnnotation(annotationCls) != null) {
				annotatedFields.add(field);
			}
		}
		return annotatedFields;
	}

	/**
	 * 获得类中所有Field,同时进行过滤
	 * 
	 * @param <T> 泛型
	 * @param clazz 类
	 * @param predicate 过滤内部类
	 * @return 过滤后的 List<Field>
	 */
	public static <T> List<Field> getFieldsFilter(Class<T> clazz, Predicate<Field> predicate) {
		return getFieldsFilter(clazz, true, predicate);
	}

	/**
	 * 获得类中所有Field,同时进行过滤
	 * 
	 * @param <T> 泛型
	 * @param clazz 类
	 * @param superClass 是否递归查找父类中的属性Field,true递归查找
	 * @param predicate 过滤内部类
	 * @return 过滤后的 List<Field>
	 */
	public static <T> List<Field> getFieldsFilter(Class<T> clazz, boolean superClass, Predicate<Field> predicate) {
		AssertTool.notNull(clazz, "Class must not be null");
		ListBuilder<Field> builder = ListTool.builder();
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if (predicate.test(field)) {
					builder.add(field);
				}
			}
			searchType = superClass ? searchType.getSuperclass() : null;
		}
		return builder.build();
	}

	/**
	 * 在接口中查找非abstract修饰的方法,只查找接口,且只查找一层,不递归查找
	 * 
	 * @param clazz 待查找的类
	 * @return 已经实现的方法
	 */
	public static List<Method> getConcreteMethodsOnInterface(Class<?> clazz) {
		List<Method> result = null;
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method ifcMethod : ifc.getMethods()) {
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new ArrayList<>();
					}
					result.add(ifcMethod);
				}
			}
		}
		return result;
	}

	/**
	 * 获得class中所有字段,包括private,protected,public,默认
	 * 
	 * @param clazz 类
	 * @return 所有Field数组
	 */
	public static Field[] getDeclaredFields(Class<?> clazz) {
		AssertTool.notNull(clazz, "Class must not be null");
		Field[] result = DECLARED_FIELDS_CACHE.get(clazz);
		if (result == null) {
			try {
				result = clazz.getDeclaredFields();
				DECLARED_FIELDS_CACHE.put(clazz, (result.length == 0 ? ArrayTool.ARRAY_EMPTY_FIELD : result));
			} catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName()
						+ "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return result;
	}

	/**
	 * 使用本地缓存处理类中方法
	 * 
	 * @param clazz 待处理的类
	 * @return 待处理的类中方法数组
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		return getDeclaredMethods(clazz, true);
	}

	/**
	 * 使用本地缓存处理类中方法
	 * 
	 * @param clazz 待处理的类
	 * @param defensive true返回结果的clone(),false返回结果本身
	 * @return 待处理的类中方法数组
	 * @throws IllegalStateException
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
		AssertTool.notNull(clazz, "Class must not be null");
		Method[] result = DECLARED_METHODS_CACHE.get(clazz);
		if (result == null || result.length == 0) {
			try {
				Method[] declaredMethods = clazz.getDeclaredMethods();
				List<Method> defaultMethods = getConcreteMethodsOnInterface(clazz);
				if (defaultMethods != null) {
					result = new Method[declaredMethods.length + defaultMethods.size()];
					System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
					int index = declaredMethods.length;
					for (Method defaultMethod : defaultMethods) {
						result[index] = defaultMethod;
						index++;
					}
				} else {
					result = declaredMethods;
				}
				DECLARED_METHODS_CACHE.put(clazz, (result.length == 0 ? ArrayTool.ARRAY_EMPTY_METHOD : result));
			} catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName()
						+ "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return (result.length == 0 || !defensive) ? result : result.clone();
	}

	/**
	 * 尝试在待查找的类中根据方法名查找无参方法
	 * 
	 * @param clazz 待查找的类
	 * @param name 待查找的方法名,无参方法
	 * @return Method 或null
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> clazz, String name) throws NoSuchMethodException, SecurityException {
		return getMethod(clazz, name, true, true, ArrayTool.ARRAY_EMPTY_CLASS);
	}

	/**
	 * 尝试在待查找的类中根据方法名查找方法,父类会递归查找
	 * 
	 * @param clazz 待查找的类
	 * @param name 待查找的方法名
	 * @param paramTypes 待查找的方法参数列表类
	 * @return Method 或null
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes)
			throws NoSuchMethodException, SecurityException {
		return getMethod(clazz, name, true, true, paramTypes);
	}

	/**
	 * 尝试在待查找的类中根据方法名查找方法,父类会递归查找
	 * 
	 * @param clazz 待查找的类
	 * @param name 待查找的方法名
	 * @param args 待查找的方法参数列表
	 * @return Method 或null
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> clazz, String name, Object[] args)
			throws NoSuchMethodException, SecurityException {
		return getMethod(clazz, name, true, true, ClassTool.toClass(args));
	}

	/**
	 * 尝试在待查找的类中根据方法名查找方法
	 * 
	 * @param clazz 待查找的类
	 * @param name 待查找的方法名
	 * @param forceAccess 是否强制访问所有方法,true是,false只访问public
	 * @param superClass 是否递归查找父类,true查找
	 * @param paramTypes 待查找的方法参数列表
	 * @return Method 或null
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getMethod(Class<?> clazz, String name, boolean forceAccess, boolean superClass,
			Class<?>... paramTypes) throws NoSuchMethodException, SecurityException {
		AssertTool.notNull(clazz, "Class must not be null");
		AssertTool.notNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		// 从本类或父类中查找
		while (searchType != null) {
			Method method = forceAccess ? searchType.getDeclaredMethod(name, paramTypes)
					: searchType.getMethod(name, paramTypes);
			if (null != method) {
				return method;
			}
			searchType = superClass ? searchType.getSuperclass() : null;
		}
		return null;
	}

	/**
	 * 从当前类以及当前类的父类,接口中递归查找指定方法名的方法数量,忽略参数,包括public和非public的
	 * 
	 * @param clazz 指定类
	 * @param methodName 方法名
	 * @return 指定方法名的方法数量,包括public和非public
	 */
	public static int getMethodCount(Class<?> clazz, String methodName) {
		return getMethodCount(clazz, methodName, true, true);
	}

	/**
	 * 从类中查找指定方法名的方法数量,忽略参数,包括public和非public的
	 * 
	 * @param clazz 指定类
	 * @param methodName 方法名
	 * @param superClass 是否递归查找父类中的方法,true是
	 * @return 指定方法名的方法数量,包括public和非public
	 */
	public static int getMethodCount(Class<?> clazz, String methodName, boolean superClass) {
		return getMethodCount(clazz, methodName, superClass, true);
	}

	/**
	 * 从类中查找指定方法名的方法数量,忽略参数,包括public和非public的
	 * 
	 * @param clazz 指定类
	 * @param methodName 方法名
	 * @param superClass 是否递归查找父类中的方法,true是
	 * @param superInterface 是否递归查找上级接口中的方法,true是
	 * @return 指定方法名的方法数量,包括public和非public
	 */
	public static int getMethodCount(Class<?> clazz, String methodName, boolean superClass, boolean superInterface) {
		AssertTool.notNull(clazz, "Class must not be null");
		AssertTool.notNull(methodName, "Method name must not be null");
		int count = 0;
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (methodName.equals(method.getName())) {
				count++;
			}
		}
		if (superInterface) {
			Class<?>[] ifcs = clazz.getInterfaces();
			for (Class<?> ifc : ifcs) {
				count += getMethodCount(ifc, methodName, superClass, superInterface);
			}
		}
		if (superClass && clazz.getSuperclass() != null) {
			count += getMethodCount(clazz.getSuperclass(), methodName, superClass, superInterface);
		}
		return count;
	}

	/**
	 * 获得类中所有含有给定注解的方法
	 * 
	 * @param clazz 待查询的类
	 * @param annotationCls 注解类
	 * @return 符合条件的集合,可能为null或[]
	 * @throws IllegalArgumentException
	 */
	public static List<Method> getMethodsByAnnotation(final Class<?> clazz,
			final Class<? extends Annotation> annotationCls) {
		return getMethodsByAnnotation(clazz, annotationCls, false, false);
	}

	/**
	 * 获得类中所有含有给定注解的方法
	 * 
	 * @param clazz 待查询的类
	 * @param annotationCls 注解类
	 * @param searchSupers 是否查找当前类的所有父类以及接口,true查询
	 * @param ignoreAccess 是否查询所有方法,true是,false只查询可访问方法
	 * @return 符合条件的集合,可能为null或[]
	 * @throws NullPointerException
	 */
	public static List<Method> getMethodsByAnnotation(final Class<?> clazz,
			final Class<? extends Annotation> annotationCls, final boolean searchSupers, final boolean ignoreAccess) {
		AssertTool.notNull(clazz, "The class must not be null");
		AssertTool.notNull(annotationCls, "The annotation class must not be null");
		final List<Class<?>> classes = (searchSupers ? ClassTool.getSuperclassesAndInterfaces(clazz)
				: new ArrayList<>());
		classes.add(0, clazz);
		final List<Method> annotatedMethods = new ArrayList<>();
		for (final Class<?> acls : classes) {
			final Method[] methods = (ignoreAccess ? acls.getDeclaredMethods() : acls.getMethods());
			for (final Method method : methods) {
				if (method.getAnnotation(annotationCls) != null) {
					annotatedMethods.add(method);
				}
			}
		}
		return annotatedMethods;
	}

	public static Method[] getPropertyMethods(PropertyDescriptor[] properties, boolean read, boolean write) {
		Set<Method> methods = new HashSet<>();
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor pd = properties[i];
			if (read) {
				methods.add(pd.getReadMethod());
			}
			if (write) {
				methods.add(pd.getWriteMethod());
			}
		}
		methods.remove(null);
		return (Method[]) methods.toArray(new Method[methods.size()]);
	}

	public static PropertyDescriptor[] getBeanProperties(Class<?> type) throws IntrospectionException {
		return getPropertiesHelper(type, true, true);
	}

	public static PropertyDescriptor[] getBeanGetters(Class<?> type) throws IntrospectionException {
		return getPropertiesHelper(type, true, false);
	}

	public static PropertyDescriptor[] getBeanSetters(Class<?> type) throws IntrospectionException {
		return getPropertiesHelper(type, false, true);
	}

	private static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write)
			throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(type, Object.class);
		PropertyDescriptor[] all = info.getPropertyDescriptors();
		if (read && write) {
			return all;
		}
		List<PropertyDescriptor> properties = new ArrayList<>(all.length);
		for (int i = 0; i < all.length; i++) {
			PropertyDescriptor pd = all[i];
			if ((read && pd.getReadMethod() != null) || (write && pd.getWriteMethod() != null)) {
				properties.add(pd);
			}
		}
		return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[properties.size()]);
	}

	/**
	 * 获得指定接口的代理类
	 * 
	 * @param <T> 泛型
	 * @param interfaceType 接口class
	 * @param handler 代理方法
	 * @param T 代理类
	 * @throws IllegalArgumentException
	 */
	public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
		AssertTool.notNull(handler);
		AssertTool.notNull(interfaceType.isInterface(), "%s is not an interface", interfaceType);
		Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[] { interfaceType },
				handler);
		return interfaceType.cast(object);
	}

	/**
	 * 判断指定的class中有public的指定类型的无参构造
	 * 
	 * @param clazz 指定的class
	 * @return true当class中有指定参数的public构造
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static boolean hasConstructor(Class<?> clazz) throws NoSuchMethodException, SecurityException {
		return (getConstructor(clazz, false) != null);
	}

	/**
	 * 判断指定的class中有public的指定类型的有参构造
	 * 
	 * @param clazz 指定的class
	 * @param paramTypes 构造参数类型
	 * @return true当class中有指定参数的public构造
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes)
			throws NoSuchMethodException, SecurityException {
		return (getConstructor(clazz, false, paramTypes) != null);
	}

	/**
	 * 判断方法的参数个数和给定的方法类型个数是否相同
	 * 
	 * @param method 方法
	 * @param paramTypes 参数类数组
	 * @return true相同,false不同
	 */
	public static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
		AssertTool.notNull(method);
		return (paramTypes.length == method.getParameterCount()
				&& Arrays.equals(paramTypes, method.getParameterTypes()));
	}

	/**
	 * 强制新建一个对象
	 * 
	 * @param constructor 构造函数
	 * @param args 构造函数的实参
	 * @return 新对象
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Object invokeConstructor(final Constructor<?> constructor, final Object[] args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		fixAccessible(constructor);
		return constructor.newInstance(null == args ? new Object[0] : args);
	}

	/**
	 * 新建一个对象
	 * 
	 * @param constructor 构造函数
	 * @param forceAccess 是否强制访问,true强制,false只能访问public
	 * @param args 构造函数的实参
	 * @return 新对象
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Object invokeConstructor(final Constructor<?> constructor, boolean forceAccess, final Object[] args)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		fixAccessible(constructor, forceAccess);
		return constructor.newInstance(null == args ? new Object[0] : args);
	}

	/**
	 * 新建一个对象
	 *
	 * @param <T> 泛型
	 * @param cls 待新建对象的class
	 * @param args 构造函数的参数列表
	 * @return 新对象
	 * @throws NullPointerException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> T invokeConstructor(final Class<T> cls, Object... args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		final Class<?>[] parameterTypes = ClassTool.toClass(args);
		return invokeConstructor(cls, true, args, parameterTypes);
	}

	/**
	 * 新建一个对象
	 *
	 * @param <T> 泛型
	 * @param clazz 待新建对象的class
	 * @param forceAccess 是否强制访问,true是,false否
	 * @param args 构造函数的参数列表
	 * @param parameterTypes 构造函数参数列表class列表
	 * @return 新对象
	 * @throws NullPointerException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> T invokeConstructor(final Class<T> clazz, boolean forceAccess, Object[] args,
			Class<?>[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		final Constructor<T> constructor = getConstructor(clazz, parameterTypes);
		if (constructor == null) {
			throw new NoSuchMethodException("No such accessible constructor on object: " + clazz.getName());
		}
		fixAccessible(constructor, forceAccess);
		return constructor.newInstance(null == args ? new Object[0] : args);
	}

	/**
	 * 强制执行一个无参的方法
	 * 
	 * @param object 执行方法的对象
	 * @param methodName 方法名
	 * @return 结果
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(final Object object, final String methodName)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(object, methodName, ArrayTool.ARRAY_EMPTY_OBJECT, null);
	}

	/**
	 * 执行一个无参的方法
	 * 
	 * @param object 执行方法的对象
	 * @param forceAccess true强制执行,false只要public方法可执行
	 * @param methodName 方法名
	 * 
	 * @return 结果
	 * 
	 * @throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	 */
	public static Object invokeMethod(final Object object, final boolean forceAccess, final String methodName)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(object, forceAccess, methodName, ArrayTool.ARRAY_EMPTY_OBJECT, null);
	}

	/**
	 * 强制执行一个有参数的方法
	 * 
	 * @param object 执行方法的对象
	 * @param methodName 方法名
	 * @param args 方法参数列表
	 * 
	 * @return 结果
	 * 
	 * @throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	 */
	public static Object invokeMethod(final Object object, final String methodName, Object... args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?>[] parameterTypes = ClassTool.toClass(args);
		return invokeMethod(object, methodName, args, parameterTypes);
	}

	/**
	 * 执行一个有参数的方法
	 * 
	 * @param object 执行方法的对象
	 * @param forceAccess true强制执行,false只要public方法可执行
	 * @param methodName 方法名
	 * @param args 方法参数列表
	 * 
	 * @return 结果
	 * 
	 * @throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	 */
	public static Object invokeMethod(final Object object, final boolean forceAccess, final String methodName,
			Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?>[] parameterTypes = ClassTool.toClass(args);
		return invokeMethod(object, forceAccess, methodName, args, parameterTypes);
	}

	/**
	 * 强制执行方法
	 * 
	 * @param object 执行方法的对象
	 * @param methodName 方法名
	 * @param args 方法参数列表
	 * @param parameterTypes 方法参数类型class列表
	 * 
	 * @return 结果
	 * 
	 * @throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	 */
	public static Object invokeMethod(final Object object, final String methodName, final Object[] args,
			final Class<?>[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(object, true, methodName, args, parameterTypes);
	}

	/**
	 * 执行方法
	 * 
	 * @param object 执行方法的对象
	 * @param forceAccess true强制执行,false只要public方法可执行
	 * @param methodName 方法名
	 * @param args 方法参数列表
	 * @param parameterTypes 方法参数类型class列表
	 * 
	 * @return 结果
	 * 
	 * @throws NoSuchMethodException,IllegalAccessException,InvocationTargetException
	 */
	public static Object invokeMethod(final Object object, final boolean forceAccess, final String methodName,
			Object[] args, Class<?>[] parameterTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final String messagePrefix;
		Method method = null;
		if (forceAccess) {
			messagePrefix = "No such method: ";
			method = getMethod(object.getClass(), methodName, parameterTypes);
			if (null != method) {
				fixAccessible(method);
			}
		} else {
			messagePrefix = "No such accessible method: ";
			method = getMethod(object.getClass(), methodName, parameterTypes);
		}
		if (method == null) {
			throw new NoSuchMethodException(
					messagePrefix + methodName + "() on object: " + object.getClass().getName());
		}
		return method.invoke(object, args);
	}

	/**
	 * 在目标实例或类上执行方法
	 * 
	 * @param method 需要执行的方法
	 * @param target 需要执行方法的实例或类
	 * @return 执行结果
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethod(Method method, Object target)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return invokeMethod(method, target, true, ArrayTool.ARRAY_EMPTY_OBJECT);
	}

	/**
	 * 在目标实例或类上执行方法
	 * 
	 * @param method 需要执行的方法
	 * @param target 需要执行方法的实例或类
	 * @param args 执行方法时的参数,需要自行调整和方法的参数位置类型,否则报错
	 * @return 执行结果
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethod(Method method, Object target, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return invokeMethod(method, target, true, args);
	}

	/**
	 * 在目标实例或类上执行方法
	 * 
	 * @param method 需要执行的方法
	 * @param target 需要执行方法的实例或类
	 * @param forceAccess 是否强制访问,true强制访问
	 * @return 执行结果
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethod(Method method, Object target, boolean forceAccess)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		fixAccessible(method, forceAccess);
		return method.invoke(target, ArrayTool.ARRAY_EMPTY_OBJECT);
	}

	/**
	 * 在目标实例或类上执行方法
	 * 
	 * @param method 需要执行的方法
	 * @param target 需要执行方法的实例或类
	 * @param forceAccess 是否强制访问,true强制访问
	 * @param args 执行方法时的参数,需要自行调整和方法的参数位置类型,否则报错
	 * @return 执行结果
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethod(Method method, Object target, boolean forceAccess, Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		fixAccessible(method, forceAccess);
		return method.invoke(target, args);
	}

	/**
	 * 在目标实例或类上执行无参静态方法
	 *
	 * @param cls 执行静态方法的类
	 * @param methodName 静态方法名
	 * @return 执行后的结果
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethodStatic(final Class<?> cls, final String methodName)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethodStatic(cls, methodName, ArrayTool.ARRAY_EMPTY_OBJECT, ArrayTool.ARRAY_EMPTY_CLASS);
	}

	/**
	 * 在目标实例或类上执行静态方法
	 *
	 * @param cls 执行静态方法的类
	 * @param methodName 静态方法名
	 * @param args 静态方法参数列表
	 * @return 执行后的结果
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethodStatic(final Class<?> cls, final String methodName, Object... args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Class<?>[] parameterTypes = ClassTool.toClass(args);
		return invokeMethodStatic(cls, methodName, args, parameterTypes);
	}

	/**
	 * 在目标实例或类上执行静态方法
	 *
	 * @param cls 执行静态方法的类
	 * @param methodName 静态方法名
	 * @param args 静态方法参数列表
	 * @param parameterTypes 静态方法参数classs列表
	 * @return 执行后的结果
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static Object invokeMethodStatic(final Class<?> cls, final String methodName, Object[] args,
			Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		final Method method = getMethod(cls, methodName, parameterTypes);
		if (method == null) {
			throw new NoSuchMethodException(
					"No such accessible method: " + methodName + "() on class: " + cls.getName());
		}
		return method.invoke(null, args);
	}

	/**
	 * 判断成员是否可访问
	 * 
	 * @param member 需要访问的成员
	 * @return true当可访问
	 */
	public static boolean isAccessible(final Member member) {
		AssertTool.notNull(member);
		if (!(member instanceof AccessibleObject)) {
			return false;
		}
		return ((AccessibleObject) member).isAccessible()
				|| (Modifier.isPublic(member.getModifiers()) && !member.isSynthetic());
	}

	/**
	 * 判断给定的方法是否是equals()
	 * 
	 * @return true当是equals()
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null) {
			return false;
		}
		if (method.getParameterCount() != 1) {
			return false;
		}
		if (!method.getName().equals("equals")) {
			return false;
		}
		return method.getParameterTypes()[0] == Object.class;
	}

	/**
	 * 判断给定的方法是否是hashCode()
	 * 
	 * @return true当是hashCode()
	 */
	public static boolean isHashCodeMethod(Method method) {
		return method != null && method.getParameterCount() == 0 && method.getName().equals("hashCode");
	}

	/**
	 * 判断给定的方法是否是toString()
	 * 
	 * @return true当是toString()
	 */
	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getParameterCount() == 0 && method.getName().equals("toString"));
	}

	/**
	 * 强制读取对象属性值
	 * 
	 * @param obj 实例对象
	 * @param field 属性
	 * @return 实例对象中属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readField(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
		return readField(obj, field, true);
	}

	/**
	 * 读取对象属性值
	 * 
	 * @param obj 实例对象
	 * @param field 属性
	 * @param forceAccess 是否强制访问,true是,false只访问public
	 * @return 实例对象中属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readField(Object obj, Field field, boolean forceAccess)
			throws IllegalArgumentException, IllegalAccessException {
		AssertTool.notNull(field);
		fixAccessible(field, forceAccess);
		return field.get(obj);
	}

	/**
	 * 直接读取对象属性值
	 * 
	 * @param obj 实例对象
	 * @param fieldName 属性名
	 * @return 实例对象中属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readField(Object obj, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		return readField(obj, fieldName, true);
	}

	/**
	 * 直接读取对象属性值
	 * 
	 * @param obj 实例对象
	 * @param fieldName 属性名
	 * @param forceAccess 是否强制访问,true是,false只访问public
	 * @return 实例对象中属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readField(Object obj, final String fieldName, boolean forceAccess)
			throws IllegalArgumentException, IllegalAccessException {
		AssertTool.notNull(obj, "Object can't be null");
		AssertTool.notBlank(fieldName, "FieldName can't be blank");
		Field field = getField(obj, fieldName);
		if (null == field) {
			throw new ResultException("在[%s]中没有找到%s字段", obj.getClass(), fieldName);
		}
		fixAccessible(field, forceAccess);
		return field.get(obj);
	}

	/**
	 * 强制读取静态Field属性值
	 * 
	 * @param field 属性
	 * @return 属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readFieldStatic(Field field) throws IllegalArgumentException, IllegalAccessException {
		return readFieldStatic(field, true);
	}

	/**
	 * 读取静态Field属性值
	 * 
	 * @param field 属性
	 * @param forceAccess 是否强制访问,true是,false只访问public
	 * @return 属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readFieldStatic(Field field, boolean forceAccess)
			throws IllegalArgumentException, IllegalAccessException {
		AssertTool.notNull(field, "The field can't be null");
		AssertTool.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
		return readField(null, field, forceAccess);
	}

	/**
	 * 强制读取静态Field属性值
	 * 
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @return 属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readFieldStatic(Class<?> clazz, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		return readFieldStatic(clazz, fieldName, true);
	}

	/**
	 * 读取静态Field属性值
	 * 
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @param forceAccess 是否强制访问,true是,false只访问public
	 * @return 属性的值
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Object readFieldStatic(Class<?> clazz, final String fieldName, boolean forceAccess)
			throws IllegalArgumentException, IllegalAccessException {
		AssertTool.notNull(clazz, "Class can't be null");
		AssertTool.notBlank(fieldName, "FieldName can't be blank");
		Field field = getField(clazz, fieldName);
		if (null == field) {
			throw new ResultException("在[%s]中没有找到%s字段", clazz, fieldName);
		}
		AssertTool.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
		fixAccessible(field, forceAccess);
		return field.get(null);
	}

	/**
	 * 强制设置对象属性值
	 *
	 * @param field 待写入值的属性
	 * @param target 对象实例
	 * @param value 待写入的值
	 * @throws IllegalAccessException
	 */
	public static void writeField(final Field field, final Object target, final Object value)
			throws IllegalAccessException {
		writeField(field, target, value, true);
	}

	/**
	 * 设置对象属性值
	 *
	 * @param field 待写入值的属性
	 * @param target 对象实例
	 * @param value 待写入的值
	 * @param forceAccess 是否强制访问属性,true是,false否
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void writeField(final Field field, final Object target, final Object value, final boolean forceAccess)
			throws IllegalAccessException {
		AssertTool.notNull(field, "The field must not be null");
		fixAccessible(field, forceAccess);
		field.set(target, value);
	}

	/**
	 * 强制设置对象属性值
	 *
	 * @param fieldName 属性名
	 * @param target 对象实例
	 * @param value 待写入的值
	 * @throws IllegalAccessException
	 */
	public static void writeField(final String fieldName, final Object target, final Object value)
			throws IllegalAccessException {
		writeField(fieldName, target, value, true);
	}

	/**
	 * 设置对象属性值
	 *
	 * @param fieldName 属性名
	 * @param target 对象实例
	 * @param value 待写入的值
	 * @param forceAccess 是否强制访问属性,true是,false否,只能访问public
	 * @throws IllegalAccessException
	 */
	public static void writeField(final String fieldName, final Object target, final Object value,
			final boolean forceAccess) throws IllegalAccessException {
		AssertTool.notNull(target, "target object must not be null");
		final Field field = getField(target.getClass(), fieldName);
		AssertTool.notNull(field, "Cannot locate declared field %s.%s", target.getClass().getName(), fieldName);
		fixAccessible(field, forceAccess);
		writeField(field, target, value, forceAccess);
	}

	/**
	 * 强制修改静态变量的值
	 *
	 * @param field 待写入值的属性
	 * @param value 待写入的值
	 * @throws IllegalAccessException
	 */
	public static void writeFieldStatic(final Field field, final Object value) throws IllegalAccessException {
		writeFieldStatic(field, value, false);
	}

	/**
	 * 修改静态变量的值
	 *
	 * @param field 待写入值的属性
	 * @param value 待写入的值
	 * @param forceAccess 是否强制访问属性,true是,false否,只能访问public
	 * @throws IllegalAccessException
	 */
	public static void writeFieldStatic(final Field field, final Object value, final boolean forceAccess)
			throws IllegalAccessException {
		AssertTool.notNull(field, "The field must not be null");
		AssertTool.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static",
				field.getDeclaringClass().getName(), field.getName());
		writeField(field, null, value, forceAccess);
	}

	/**
	 * 强制修改静态变量的值
	 *
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @param value 待写入的值
	 * @throws IllegalAccessException
	 */
	public static void writeFieldStatic(final Class<?> clazz, final String fieldName, final Object value)
			throws IllegalAccessException {
		writeFieldStatic(clazz, fieldName, value, true);
	}

	/**
	 * 修改静态变量的值
	 *
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @param value 待写入的值
	 * @param forceAccess 是否强制访问属性,true是,false否,只能访问public
	 * @throws IllegalAccessException
	 */
	public static void writeFieldStatic(final Class<?> clazz, final String fieldName, final Object value,
			final boolean forceAccess) throws IllegalAccessException {
		final Field field = getField(clazz, fieldName);
		AssertTool.notNull(field, "Cannot locate field %s on %s", fieldName, clazz);
		fixAccessible(field, forceAccess);
		writeFieldStatic(field, value, forceAccess);
	}
}