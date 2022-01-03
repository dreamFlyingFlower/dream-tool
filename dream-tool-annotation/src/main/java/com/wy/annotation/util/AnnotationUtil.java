package com.wy.annotation.util;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
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
	 * 判断参数类型是否为基本类型
	 * 
	 * @param decl 变量定义
	 * @return true->是基本类型,false->不是基本类型
	 */
	public static boolean isPrimitive(JCVariableDecl decl) {
		return decl.type.isPrimitive();
	}

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
	 * 获得当前元素的直接父元素,若为null表示无父元素
	 * 
	 * @param processingEnv
	 * @param typeElement
	 * @return
	 */
	public static TypeElement findSuperElement(ProcessingEnvironment processingEnv, TypeElement typeElement) {
		Types typeUtils = processingEnv.getTypeUtils();
		while (!typeElement.toString().equals(Object.class.getName())) {
			TypeMirror typeMirror = typeElement.getSuperclass();
			return (TypeElement) typeUtils.asElement(typeMirror);
		}
		return null;
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
	 * 创建赋值语句
	 * 
	 * @param treeMaker
	 * @param names
	 * @param key
	 * @param value
	 * @return
	 */
	public static JCTree.JCExpression makeArg(TreeMaker treeMaker, Names names, String key, String value) {
		// 注解需要的参数是表达式,这里的实际实现为等式对象,Ident是值,Literal是value,最后结果为a=b
		return treeMaker.Assign(treeMaker.Ident(names.fromString(key)), treeMaker.Literal(value));
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