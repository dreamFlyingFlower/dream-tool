package dream.flying.flower.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import dream.flying.flower.lang.AssertHelper;

/**
 * Modifier工具类,{@link Modifier}
 * 
 * @author 飞花梦影
 * @date 2021-03-11 09:17:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ModifierHelper {

	/**
	 * 判断是否是抽象类
	 * 
	 * @param clazz 类
	 * @return true->是,false->不是
	 */
	public static boolean isAbstract(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The class can't be null");
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 判断是否是抽象方法
	 * 
	 * @param method 方法
	 * @return true->是,false->不是
	 */
	public static boolean isAbstract(Method method) {
		return Modifier.isAbstract(method.getModifiers());
	}

	/**
	 * 判断是否是常量
	 * 
	 * @return true->是,false->不是
	 */
	public static boolean isConstant(Field field) {
		return Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers());
	}

	/**
	 * 判断是否是final类
	 * 
	 * @param clazz 类
	 * @return true->是,false->不是
	 */
	public static boolean isFinal(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The class can't be null");
		return Modifier.isFinal(clazz.getModifiers());
	}

	/**
	 * 判断是否是final字段,方法
	 * 
	 * @param member 类中成员
	 * @return true->是,false->不是
	 */
	public static boolean isFinal(Member member) {
		AssertHelper.notNull(member);
		return Modifier.isFinal(member.getModifiers());
	}

	/**
	 * 判断是否是接口
	 * 
	 * @param clazz 类
	 * @return true->是,false->不是
	 */
	public static boolean isInterface(Class<?> clazz) {
		return Modifier.isInterface(clazz.getModifiers());
	}

	/**
	 * 判断是否是public类
	 * 
	 * @param clazz 类
	 * @return true->是,false->不是
	 */
	public static boolean isPublic(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The class can't be null");
		return Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 判断是否是public字段,方法,构造
	 * 
	 * @param member 类中成员
	 * @return true->是,false->不是
	 */
	public static boolean isPublic(Member member) {
		AssertHelper.notNull(member);
		return Modifier.isPublic(member.getModifiers());
	}

	/**
	 * 判断是否是static类
	 * 
	 * @param clazz 类
	 * @return 是否是static
	 */
	public static boolean isStatic(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The class can't be null");
		return Modifier.isStatic(clazz.getModifiers());
	}

	/**
	 * 判断是否是static字段,方法
	 * 
	 * @param member 类中成员
	 * @return true->是,false->不是
	 */
	public static boolean isStatic(Member member) {
		AssertHelper.notNull(member);
		return Modifier.isStatic(member.getModifiers());
	}

	/**
	 * 判断是否是static或final类
	 * 
	 * @return true->是,false->不是
	 */
	public static boolean isStaticOrFinal(Class<?> clazz) {
		AssertHelper.notNull(clazz, "The class can't be null");
		return isStatic(clazz) || isFinal(clazz);
	}

	/**
	 * 判断是否是static或final字段,方法
	 * 
	 * @return true->是,false->不是
	 */
	public static boolean isStaticOrFinal(Member member) {
		AssertHelper.notNull(member);
		return isStatic(member) || isFinal(member);
	}
}