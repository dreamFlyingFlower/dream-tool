package com.wy.annotation.util;

import java.util.Set;

import javax.lang.model.element.Modifier;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

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

	/**
	 * 创建一个注解
	 * 
	 * @param treeMaker 封装了定义方法,变量,类等等的方法
	 * @param names 用于创建标识符
	 * @param annotaionName 注解全路径名
	 * @param args 注解参数
	 * @return 注解表达式
	 */
	public static JCTree.JCAnnotation makeAnnotation(TreeMaker treeMaker, Names names, String annotaionName,
			List<JCTree.JCExpression> args) {
		JCTree.JCExpression expression = AnnotationUtil.chainDots(treeMaker, names, annotaionName);
		return treeMaker.Annotation(expression, args);
	}

	/**
	 * 创建域/方法的多级访问,方法的标识只能是最后一个
	 * 
	 * @param treeMaker 封装了定义方法,变量,类等等的方法
	 * @param names 用于创建标识符
	 * @param element 类全路径
	 * @return 类访问表达式
	 */
	public static JCExpression chainDots(TreeMaker treeMaker, Names names, String element) {
		JCTree.JCExpression e = null;
		String[] elems = element.split("\\.");
		for (int i = 0; i < elems.length; i++) {
			e = e == null ? treeMaker.Ident(names.fromString(elems[i]))
					: treeMaker.Select(e, names.fromString(elems[i]));
		}
		return e;
	}
}