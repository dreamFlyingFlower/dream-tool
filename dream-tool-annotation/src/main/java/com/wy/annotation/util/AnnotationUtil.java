package com.wy.annotation.util;

import java.util.Set;

import javax.lang.model.element.Modifier;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.List;

/**
 * 注解工具类
 *
 * @author 飞花梦影
 * @date 2021-12-17 17:25:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AnnotationUtil {

	/**
	 * 判断方法是否构造函数
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean constructor(JCTree.JCMethodDecl jcMethodDecl) {
		String name = jcMethodDecl.name.toString();
		if ("<init>".equals(name)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断方法是否为默认public无参构造
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean constrcutorDefault(JCMethodDecl jcMethodDecl) {
		if (AnnotationUtil.constructor(jcMethodDecl) && AnnotationUtil.methodNoArgs(jcMethodDecl)
				&& AnnotationUtil.methodPublic(jcMethodDecl)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断方法是否无参
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean methodNoArgs(JCMethodDecl jcMethodDecl) {
		List<JCTree.JCVariableDecl> jcVariableDeclList = jcMethodDecl.getParameters();
		if (jcVariableDeclList == null || jcVariableDeclList.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断方法修饰符是否为public
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean methodPublic(JCMethodDecl jcMethodDecl) {
		JCTree.JCModifiers jcModifiers = jcMethodDecl.getModifiers();
		Set<Modifier> modifiers = jcModifiers.getFlags();
		if (modifiers.contains(Modifier.PUBLIC)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断方法修饰符是否为private
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean methodPrivate(JCMethodDecl jcMethodDecl) {
		JCTree.JCModifiers jcModifiers = jcMethodDecl.getModifiers();
		Set<Modifier> modifiers = jcModifiers.getFlags();
		if (modifiers.contains(Modifier.PRIVATE)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断方法修饰符是否为final
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean methodFinal(JCMethodDecl jcMethodDecl) {
		JCTree.JCModifiers jcModifiers = jcMethodDecl.getModifiers();
		Set<Modifier> modifiers = jcModifiers.getFlags();
		if (modifiers.contains(Modifier.FINAL)) {
			return true;
		}
		return false;
	}
}