package dream.flying.flower.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dream.flying.flower.ConstArray;
import dream.flying.flower.ConstClass;
import dream.flying.flower.ConstSymbol;
import dream.flying.flower.collection.ListHelper;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.result.ResultException;

/**
 * Class工具类,不涉及构造,方法,字段 FIXME
 *
 * @author 飞花梦影
 * @date 2021-03-14 13:22:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ClassHelper {

	/**
	 * 将类的全路径类名地址转换为class
	 *
	 * @param classNames 全路径类名
	 * @return class集合
	 * @throws ClassCastException
	 */
	public static List<Class<?>> convertClassNamesToClasses(final List<String> classNames) {
		if (classNames == null) {
			return null;
		}
		final List<Class<?>> classes = new ArrayList<>(classNames.size());
		for (final String className : classNames) {
			try {
				classes.add(Class.forName(className));
			} catch (final Exception ex) {
				classes.add(null);
			}
		}
		return classes;
	}

	/**
	 * 获得class的全路径类名
	 *
	 * @param classes class的集合
	 * @return class的全路径类名集合
	 * @throws ClassCastException
	 */
	public static List<String> convertClassesToClassNames(final List<Class<?>> classes) {
		if (ListHelper.isEmpty(classes)) {
			return null;
		}
		final List<String> classNames = new ArrayList<>(classes.size());
		for (final Class<?> cls : classes) {
			if (cls != null) {
				classNames.add(cls.getName());
			}
		}
		return classNames;
	}

	/**
	 * 返回用默认加载器加载的指定className的class
	 * 
	 * @param className 类名
	 * @return className对应的class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(final String className) throws ClassNotFoundException {
		return getClass(className, true);
	}

	/**
	 * 返回用默认加载器加载的指定className的class
	 * 
	 * @param className 类名
	 * @param initialize 是否初始化
	 * @return className对应的class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(final String className, final boolean initialize) throws ClassNotFoundException {
		final ClassLoader loader = getClassLoader();
		return getClass(loader, className, initialize);
	}

	/**
	 * 返回用指定加载器加载的指定className的class
	 * 
	 * @param classLoader 类加载器
	 * @param className 类名
	 * @return className对应的class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(final ClassLoader classLoader, final String className)
			throws ClassNotFoundException {
		return getClass(classLoader, className, true);
	}

	/**
	 * 返回用指定加载器加载的指定className的class
	 * 
	 * @param classLoader 类加载器
	 * @param className 类名
	 * @param initialize 是否初始化
	 * @return className对应的class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClass(final ClassLoader classLoader, final String className, final boolean initialize)
			throws ClassNotFoundException {
		try {
			Class<?> clazz;
			if (ConstClass.NAME_PRIMITIVE_MAP.containsKey(className)) {
				clazz = ConstClass.NAME_PRIMITIVE_MAP.get(className);
			} else {
				clazz = Class.forName(getCanonicalName(className), initialize, classLoader);
			}
			return clazz;
		} catch (final ClassNotFoundException ex) {
			// 允许点(.)作为内部类的分隔符
			final int lastDotIndex = className.lastIndexOf(ConstSymbol.CHAR_DOT);
			if (lastDotIndex != -1) {
				try {
					return getClass(classLoader, className.substring(0, lastDotIndex)
							+ ConstClass.INNER_CLASS_SEPARATOR_CHAR + className.substring(lastDotIndex + 1),
							initialize);
				} catch (final ClassNotFoundException ex2) {
					ex2.printStackTrace();
				}
			}
			throw ex;
		}
	}

	/**
	 * 将类名转换为内存中的类名,主要是数组
	 *
	 * @param className 类名
	 * @return 内存中的类名
	 */
	private static String getCanonicalName(String className) {
		if (className.endsWith("[]")) {
			final StringBuilder classNameBuffer = new StringBuilder();
			while (className.endsWith("[]")) {
				className = className.substring(0, className.length() - 2);
				classNameBuffer.append("[");
			}
			final String abbreviation = ConstClass.ABBREVIATION_MAP.get(className);
			if (abbreviation != null) {
				classNameBuffer.append(abbreviation);
			} else {
				classNameBuffer.append("L").append(className).append(";");
			}
			className = classNameBuffer.toString();
		}
		return className;
	}

	/**
	 * 获得默认的类加载器
	 * 
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// 没有权限获取线程的上下文
		}
		if (cl == null) {
			// 使用当前类的类加载器
			cl = ClassHelper.class.getClassLoader();
			if (cl == null) {
				// 当前类加载器获取不到则使用bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// 没有权限获得系统的类加载器,狗带
				}
			}
		}
		return cl;
	}

	/**
	 * 获得类中第一个泛型参数类型,注意泛型必须定义在父类,且父类带泛型参数.如无法找到,返回null
	 * 
	 * @param clazz 需要进行判断的类字节码,必须继承或实现了某个带泛型的父类
	 * @return 类的第一个泛型参数类型
	 */
	public static <T> Class<?> getGenricType(Class<T> clazz) {
		return getGenricType(clazz, 0);
	}

	/**
	 * 获得类中指定位置泛型参数类型,注意泛型必须定义在父类,且父类带泛型参数.如无法找到,返回null
	 * 
	 * @param clazz 需要进行判断的类字节码
	 * @return 类的指定位置泛型参数类型
	 */
	public static <T> Class<?> getGenricType(Class<T> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			throw new ResultException(clazz.getSimpleName() + "'s superclass not ParameterizedType");
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			throw new ResultException("Index: " + index + ", Size of " + clazz.getSimpleName()
					+ "'s Parameterized Type: " + params.length);
		}
		if (!(params[index] instanceof Class)) {
			throw new ResultException(
					clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
		}
		return (Class<?>) params[index];
	}

	/**
	 * 递归获得给定对象实现的所有接口
	 *
	 * @param obj 要查找对象
	 * @return 所有实现接口的set
	 */
	public static List<Class<?>> getInterfaces(Object obj) {
		return null == obj ? null : getInterfaces(obj.getClass());
	}

	/**
	 * 递归获得给定类实现的所有接口
	 *
	 * @param clazz 要查找的类
	 * @return 所有实现接口的set
	 */
	public static List<Class<?>> getInterfaces(final Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		final List<Class<?>> interfacesFound = new ArrayList<>();
		getInterfaces(clazz, interfacesFound);
		return new ArrayList<>(interfacesFound);
	}

	/**
	 * 递归获得给定类实现的所有接口
	 *
	 * @param cls 要查找的类
	 * @return 所有实现接口的set
	 */
	private static void getInterfaces(Class<?> cls, final List<Class<?>> interfacesFound) {
		while (cls != null) {
			final Class<?>[] interfaces = cls.getInterfaces();
			for (final Class<?> i : interfaces) {
				if (interfacesFound.add(i)) {
					getInterfaces(i, interfacesFound);
				}
			}
			cls = cls.getSuperclass();
		}
	}

	/**
	 * 获得给定类的包的名称
	 * 
	 * @param clazz 要获取包名的类
	 * @return 包名,可能是""
	 */
	public static String getPackageName(Class<?> clazz) {
		AssertHelper.notNull(clazz, "Class must not be null");
		return getPackageName(clazz.getName());
	}

	/**
	 * 获得给定类的包的名称
	 * 
	 * @param className 类的全路径名称
	 * @return 包名,可能是""
	 */
	public static String getPackageName(String className) {
		AssertHelper.notNull(className, "Class name must not be null");
		int lastDotIndex = className.lastIndexOf(ConstSymbol.DOT);
		return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
	}

	/**
	 * 获得没有包名的类名,包括经过jdk代理和cglib代理之后的类名
	 * 
	 * @param className 要获取其短名称的类名
	 * @return 没有包名或空字符串的类的类名
	 */
	public static String getShortName(final Class<?> clazz) {
		AssertHelper.notNull(clazz, "Class must not be null");
		return getShortName(clazz.getTypeName());
	}

	/**
	 * 获得没有包名的类名,包括经过jdk代理和cglib代理之后的类名
	 *
	 * @param className 要获取其短名称的类名
	 * @return 没有包名或空字符串的类的类名
	 */
	public static String getShortName(String className) {
		AssertHelper.notBlank(className, "Class name must not be empty");
		int lastDotIndex = className.lastIndexOf(ConstSymbol.DOT);
		int nameEndIndex = className.indexOf(ConstClass.CGLIB_CLASS_SEPARATOR);
		if (nameEndIndex == -1) {
			nameEndIndex = className.length();
		}
		String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
		shortName = shortName.replace(ConstClass.INNER_CLASS_SEPARATOR_CHAR, ConstSymbol.CHAR_DOT);
		return shortName;
	}

	/**
	 * 递归获得给定类的所有父类
	 *
	 * @param clazz 要查找的类
	 * @return 所有父类的set
	 */
	public static List<Class<?>> getSuperclasses(final Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		final List<Class<?>> classes = new ArrayList<>();
		Class<?> superclass = clazz.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return classes;
	}

	/**
	 * 递归获得给定类的所有父类以及接口
	 *
	 * @param clazz 要查找的类
	 * @return 给定类的所有父类以及接口集合
	 */
	public static List<Class<?>> getSuperclassesAndInterfaces(final Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<>();
		final List<Class<?>> allSuperclasses = ClassHelper.getSuperclasses(clazz);
		int superClassIndex = 0;
		final List<Class<?>> allInterfaces = ClassHelper.getInterfaces(clazz);
		int interfaceIndex = 0;
		while (interfaceIndex < allInterfaces.size() || superClassIndex < allSuperclasses.size()) {
			Class<?> acls;
			if (interfaceIndex >= allInterfaces.size()) {
				acls = allSuperclasses.get(superClassIndex++);
			} else if ((superClassIndex >= allSuperclasses.size()) || (interfaceIndex < superClassIndex)
					|| !(superClassIndex < interfaceIndex)) {
				acls = allInterfaces.get(interfaceIndex++);
			} else {
				acls = allSuperclasses.get(superClassIndex++);
			}
			allSuperClassesAndInterfaces.add(acls);
		}
		return allSuperClassesAndInterfaces;
	}

	/**
	 * class初始化
	 * 
	 * @param classes 需要初始化的类
	 * @throws ExceptionInInitializerError
	 */
	public static void initialize(Class<?>... classes) {
		for (Class<?> clazz : classes) {
			try {
				Class.forName(clazz.getName(), true, clazz.getClassLoader());
			} catch (ClassNotFoundException e) {
				throw new AssertionError(e);
			}
		}
	}

	/**
	 * 检查superType是否是subType的父类
	 *
	 * @param subType 子类
	 * @param superType 父类
	 * @return true当superType是subType的父类或同类,包装类
	 */
	public static boolean isAssignable(Class<?> subType, final Class<?> superType) {
		return isAssignable(subType, superType, true);
	}

	/**
	 * 检查superType是否是subType的父类
	 *
	 * @param subType 子类
	 * @param superType 父类
	 * @param autoBox 基类是否自动装箱:true是
	 * @return true当superType是subType的父类或同类,包装类
	 */
	public static boolean isAssignable(Class<?> subType, final Class<?> superType, boolean autoBox) {
		AssertHelper.notNull(subType, "subType class must not be null");
		AssertHelper.notNull(superType, "superType class must not be null");
		if (superType.isAssignableFrom(subType)) {
			return true;
		}
		if (autoBox) {
			if (subType.isPrimitive()) {
				Class<?> resolvedPrimitive = ConstClass.WRAPPER_PRIMITIVE_MAP.get(superType);
				return subType == resolvedPrimitive;
			} else {
				Class<?> resolvedWrapper = ConstClass.PRIMITIVE_WRAPPER_MAP.get(superType);
				return (resolvedWrapper != null && subType.isAssignableFrom(resolvedWrapper));
			}
		}
		return false;
	}

	/**
	 * 检查superType是否是subType的父类
	 *
	 * @param subType 子类
	 * @param superType 父类
	 * @return true当superType是subType的父类或同类,包装类
	 */
	public static boolean isAssignable(Class<?>[] subType, Class<?>[] superType) {
		return isAssignable(subType, superType, true);
	}

	/**
	 * 检查superType是否是subType的父类
	 *
	 * @param subType 子类
	 * @param superType 父类
	 * @param autoBox 基类是否自动装箱:true是
	 * @return true当superType是subType的父类或同类,包装类
	 */
	public static boolean isAssignable(Class<?>[] subType, Class<?>[] superType, final boolean autoBox) {
		if (null == subType || null == superType) {
			return false;
		}
		if (subType.length != superType.length) {
			return false;
		}
		for (int i = 0; i < subType.length; i++) {
			if (!isAssignable(subType[i], superType[i], autoBox)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查给定的类是一个可实例化的类,不是接口和抽象类
	 * 
	 * @param cls 待检查的类
	 * @return true当给定的类是一个可实例化的类
	 */
	public static boolean isConcrete(Class<?> cls) {
		int mod = cls.getModifiers();
		return (mod & (Modifier.INTERFACE | Modifier.ABSTRACT)) == 0;
	}

	/**
	 * 判断给定的类是一个枚举
	 *
	 * @param cls 给定的类
	 * @return true当给定的类是一个枚举
	 */
	public static boolean isEnumType(Class<?> cls) {
		return Enum.class.isAssignableFrom(cls);
	}

	/**
	 * 判断cls是否是一个内部类,可以是静态的
	 *
	 * @param cls 需要判断的类
	 * @return true当cls是一个内部类
	 */
	public static boolean isInnerClass(final Class<?> cls) {
		return cls != null && cls.getEnclosingClass() != null;
	}

	/**
	 * 判断是否为一个整数:byte,short,int,long
	 * 
	 * @param clazz 类字节码文件
	 * @return true->是一个整数类型,false->不是一个整数类型
	 */
	public static boolean isIntegral(Class<?> clazz) {
		if (byte.class == clazz || Byte.class == clazz || short.class == clazz || Short.class == clazz
				|| int.class == clazz || Integer.class == clazz || long.class == clazz || Long.class == clazz) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是一个数字类型
	 * 
	 * @param clazz 类字节码文件
	 * @return true->是一个数字类型,false->不是一个数字类型
	 */
	public static boolean isNumber(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			Class<?> wrapper = primitiveToWrapper(clazz);
			return ClassHelper.isAssignable(wrapper, Number.class);
		} else if (isPrimitiveWrapper(clazz)) {
			return ClassHelper.isAssignable(clazz, Number.class);
		}
		return false;
	}

	/**
	 * 判断class是否为基类数组
	 * 
	 * @param clazz 需要判断的类
	 * @return true当class是基类数组
	 */
	public static boolean isPrimitiveArray(Class<?> clazz) {
		AssertHelper.notNull(clazz, "Class must not be null");
		return (clazz.isArray() && clazz.getComponentType().isPrimitive());
	}

	/**
	 * 判断class是否为基类包装类数组
	 * 
	 * @param clazz 需要判断的类
	 * @return true当class是基类包装类数组
	 */
	public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
		AssertHelper.notNull(clazz, "Class must not be null");
		return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
	}

	/**
	 * 判断class是否为基本类型或基本类型包装类
	 *
	 * @param cls 需要判断的class
	 * @return true当class为基本类型或基本类型包装类
	 */
	public static boolean isPrimitiveOrWrapper(final Class<?> cls) {
		return cls == null ? false : (cls.isPrimitive() || isPrimitiveWrapper(cls));
	}

	/**
	 * 判断是否为基础类型和字符串,字符串不属于基本类型
	 * 
	 * @param clazz 需要判断的类
	 * @return true当class是基类或字符串时
	 */
	public static boolean isPrimitiveStr(Class<?> clazz) {
		return null == clazz ? false : (clazz.isPrimitive() || String.class == clazz);
	}

	/**
	 * 判断class是否为基本类型包装类
	 *
	 * @param cls 需要判断的class
	 * @return true当class为基本类型包装类
	 */
	public static boolean isPrimitiveWrapper(final Class<?> cls) {
		return ConstClass.WRAPPER_PRIMITIVE_MAP.containsKey(cls);
	}

	/**
	 * 判断class是否为基本类型包装类
	 * 
	 * 每个基本类型的包装类都有一个TYPE字段,且都是static final,Field.get(null)表示获得静态变量的值
	 * 
	 * @param cls 需要判断的class
	 * @return true当class为基本类型包装类
	 */
	public static boolean isPrimitiveWrapperClass(Class<?> cls) {
		try {
			return ((Class<?>) (cls.getField("TYPE").get(null))).isPrimitive();
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 判断class是否无返回值
	 * 
	 * @param type 字节码文件
	 * @return true当class无返回值
	 */
	public static boolean isVoid(Class<?> type) {
		if (type == Void.class || Void.TYPE == type) {
			return true;
		}
		if (type.getName().equals("kotlin.Unit")) {
			return true;
		}
		return false;
	}

	/**
	 * 获得一个接口的代理实例对象
	 *
	 * @param interfaceType 接口class
	 * @param handler 代理方法
	 * @throws IllegalArgumentException
	 */
	public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
		AssertHelper.notNull(handler);
		AssertHelper.isTrue(interfaceType.isInterface(), interfaceType + " is not an interface");
		Object object =
				Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[] { interfaceType }, handler);
		return interfaceType.cast(object);
	}

	/**
	 * 将基本类型转换为对应的包装类
	 *
	 * @param cls 基本类型
	 * @return 包装类,可能返回null
	 */
	public static Class<?> primitiveToWrapper(final Class<?> cls) {
		Class<?> convertedClass = cls;
		if (cls != null && cls.isPrimitive()) {
			convertedClass = ConstClass.PRIMITIVE_WRAPPER_MAP.get(cls);
		}
		return convertedClass;
	}

	/**
	 * 获得参数列表的class
	 * 
	 * @param array 数组参数
	 * @return class数组
	 */
	public static Class<?>[] toClass(final Object... array) {
		if (null == array || array.length == 0) {
			return ConstArray.EMPTY_CLASS;
		}
		final Class<?>[] classes = new Class[array.length];
		for (int i = 0; i < array.length; i++) {
			classes[i] = array[i] == null ? null : array[i].getClass();
		}
		return classes;
	}

	/**
	 *
	 * 将基本类型包装类转换为基本类型
	 * 
	 * @param cls 基本类型包装类
	 * @return 基本类型,可能为null
	 */
	public static Class<?> wrapperToPrimitive(final Class<?> cls) {
		return ConstClass.WRAPPER_PRIMITIVE_MAP.get(cls);
	}

	/**
	 * 获得包装类型的对应的基本类型
	 * 
	 * @param clazz 类型
	 * @return 基类或null
	 */
	public static Class<?> wrapperToPrimitiveClass(Class<?> clazz) {
		if (isPrimitiveWrapperClass(clazz)) {
			try {
				return (Class<?>) (clazz.getField("TYPE").get(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return clazz;
	}
}