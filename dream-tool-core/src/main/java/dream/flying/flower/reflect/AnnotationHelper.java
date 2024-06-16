package dream.flying.flower.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import dream.flying.flower.lang.AssertHelper;

/**
 * Annotation工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-05 15:47:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class AnnotationHelper {

	/**
	 * 获得给定方法上的指定注解,会递归查找父类以及接口
	 *
	 * @param <A> 注解泛型
	 * @param method 给定方法
	 * @param annotationCls 注解类
	 * @param searchSupers 是否查找当前类的所有父类以及接口,true查询
	 * @param ignoreAccess 是否查询所有方法,true是,false只查询可访问方法
	 * @return 第一个匹配的注解
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static <A extends Annotation> A getAnnotation(final Method method, final Class<A> annotationCls,
			final boolean searchSupers, final boolean ignoreAccess) throws NoSuchMethodException, SecurityException {
		AssertHelper.notNull(method, "The method must not be null");
		AssertHelper.notNull(annotationCls, "The annotation class must not be null");
		if (!ignoreAccess && !ReflectHelper.isAccessible(method)) {
			return null;
		}
		A annotation = method.getAnnotation(annotationCls);
		if (annotation == null && searchSupers) {
			final Class<?> mcls = method.getDeclaringClass();
			final List<Class<?>> classes = ClassHelper.getSuperclassesAndInterfaces(mcls);
			for (final Class<?> acls : classes) {
				final Method equivalentMethod = ReflectHelper.getMethod(acls, method.getName(),
						method.getParameterTypes());
				if (equivalentMethod != null) {
					ReflectHelper.fixAccessible(equivalentMethod, ignoreAccess);
					annotation = equivalentMethod.getAnnotation(annotationCls);
					if (annotation != null) {
						break;
					}
				}
			}
		}
		return annotation;
	}
}