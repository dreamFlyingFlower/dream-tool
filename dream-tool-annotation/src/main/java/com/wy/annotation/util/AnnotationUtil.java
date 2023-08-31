package com.wy.annotation.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import com.wy.annotation.enums.AnnotationEnum;
import com.wy.collection.MapHelper;

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
	public static boolean isConstructor(JCMethodDecl jcMethodDecl) {
		String name = jcMethodDecl.name.toString();
		return Optional.of(jcMethodDecl).map(t -> "<init>".equals(name)).get();
	}

	/**
	 * 判断方法是否为默认public无参构造
	 * 
	 * @param jcMethodDecl 方法定义
	 * @return true->是,false->否
	 */
	public static boolean constrcutorDefault(JCMethodDecl jcMethodDecl) {
		if (AnnotationUtil.isConstructor(jcMethodDecl) && AnnotationUtil.methodNoArgs(jcMethodDecl)
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
		List<JCVariableDecl> jcVariableDeclList = jcMethodDecl.getParameters();
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
		JCModifiers jcModifiers = jcMethodDecl.getModifiers();
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
		JCModifiers jcModifiers = jcMethodDecl.getModifiers();
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
		JCModifiers jcModifiers = jcMethodDecl.getModifiers();
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
	public static JCAnnotation makeAnnotation(TreeMaker treeMaker, Names names, String annotaionName,
			List<JCExpression> args) {
		JCExpression expression = AnnotationUtil.chainDots(treeMaker, names, annotaionName);
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
	public static JCExpression makeArg(TreeMaker treeMaker, Names names, String key, Object value) {
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
		JCExpression e = null;
		String[] elems = element.split("\\.");
		for (int i = 0; i < elems.length; i++) {
			e = e == null ? treeMaker.Ident(names.fromString(elems[i]))
					: treeMaker.Select(e, names.fromString(elems[i]));
		}
		return e;
	}

	/**
	 * 添加无参注解
	 * 
	 * @param treeMaker AST树操作工具类
	 * @param names 命名工具类
	 * @param jcClassDecl 类定义
	 * @param annotationName 需要添加的注解全路径
	 */
	public static void addAnnotation(TreeMaker treeMaker, Names names, JCClassDecl jcClassDecl, String annotationName) {
		addAnnotation(treeMaker, names, jcClassDecl, annotationName, List.nil());
	}

	/**
	 * 添加有参数注解
	 * 
	 * @param treeMaker AST树操作工具类
	 * @param names 命名工具类
	 * @param jcClassDecl 类定义
	 * @param annotationName 需要添加的注解全路径
	 * @param args 注解参数
	 */
	public static void addAnnotation(TreeMaker treeMaker, Names names, JCClassDecl jcClassDecl, String annotationName,
			List<JCExpression> args) {
		JCAnnotation jcAnnotation = makeAnnotation(treeMaker, names, annotationName, args);
		jcClassDecl.mods.annotations = jcClassDecl.mods.annotations.append(jcAnnotation);
	}

	/**
	 * 添加单个参数注解
	 * 
	 * @param treeMaker AST树操作工具类
	 * @param names 命名工具类
	 * @param jcClassDecl 类定义
	 * @param annotationName 需要添加的注解全路径
	 * @param key 注解参数key
	 * @param value 注解参数key的值
	 */
	public static void addAnnotation(TreeMaker treeMaker, Names names, JCClassDecl jcClassDecl, String annotationName,
			String key, Object value) {
		addAnnotation(treeMaker, names, jcClassDecl, annotationName, List.of(makeArg(treeMaker, names, key, value)));
	}

	/**
	 * 添加多参数注解
	 * 
	 * @param treeMaker AST树操作工具类
	 * @param names 命名工具类
	 * @param jcClassDecl 类定义
	 * @param annotationName 需要添加的注解全路径
	 * @param args 注解参数
	 */
	public static void addAnnotation(TreeMaker treeMaker, Names names, JCClassDecl jcClassDecl, String annotationName,
			Map<String, Object> args) {
		List<JCExpression> jcExpressions = List.nil();
		if (MapHelper.isNotEmpty(args)) {
			for (Map.Entry<String, Object> entry : args.entrySet()) {
				jcExpressions.add(makeArg(treeMaker, names, entry.getKey(), entry.getValue()));
			}
		}
		addAnnotation(treeMaker, names, jcClassDecl, annotationName, jcExpressions);
	}

	/**
	 * 给普通方法添加注解
	 * 
	 * @param treeMaker AST树操作工具类
	 * @param names 命名工具类
	 * @param jcMethodDecl 方法定义
	 * @param annotationName 需要添加的注解全路径
	 */
	public static void addAnnotation(TreeMaker treeMaker, Names names, JCMethodDecl jcMethodDecl,
			String annotationName) {
		addAnnotation(treeMaker, names, jcMethodDecl, annotationName, List.nil());
	}

	/**
	 * 给普通方法添加注解
	 * 
	 * @param treeMaker AST树操作工具类
	 * @param names 命名工具类
	 * @param jcMethodDecl 方法定义
	 * @param annotationName 需要添加的注解全路径
	 * @param args 注解参数
	 */
	public static void addAnnotation(TreeMaker treeMaker, Names names, JCMethodDecl jcMethodDecl, String annotationName,
			List<JCExpression> args) {
		// 构造方法也属于方法,但是普通的方法注解不能放到构造上,此处要排除构造方法
		if ("<init>".equals(jcMethodDecl.name.toString())) {
			return;
		}
		List<JCAnnotation> jcAnnotations = jcMethodDecl.mods.annotations
				.append(AnnotationUtil.makeAnnotation(treeMaker, names, annotationName, args));
		jcMethodDecl.mods.annotations = jcAnnotations;
	}

	/**
	 * 给类增加import语句
	 * 
	 * @param element
	 * @param packageSupportEnums
	 */
	public static void addImport(TreePath treePath, Element element, TreeMaker treeMaker, Names names,
			AnnotationEnum... annotationEnums) {
		JCCompilationUnit jccu = (JCCompilationUnit) treePath.getCompilationUnit();
		java.util.List<JCTree> trees = new ArrayList<>();
		trees.addAll(jccu.defs);
		java.util.List<JCTree> sourceImportList = new ArrayList<>();
		trees.forEach(e -> {
			if (e.getKind().equals(Tree.Kind.IMPORT)) {
				sourceImportList.add(e);
			}
		});
		java.util.List<JCImport> needImportList = buildImportList(treeMaker, names, annotationEnums);
		for (int i = 0; i < needImportList.size(); i++) {
			boolean importExist = false;
			for (int j = 0; j < sourceImportList.size(); j++) {
				if (sourceImportList.get(j).toString().equals(needImportList.get(i).toString())) {
					importExist = true;
				}
			}
			if (!importExist) {
				trees.add(0, needImportList.get(i));
			}
		}
		jccu.defs = List.from(trees);
	}

	private static java.util.List<JCImport> buildImportList(TreeMaker treeMaker, Names names,
			AnnotationEnum... annotationEnums) {
		java.util.List<JCImport> targetImportList = new ArrayList<>();
		if (annotationEnums.length > 0) {
			for (int i = 0; i < annotationEnums.length; i++) {
				JCImport needImport = buildImport(treeMaker, names, annotationEnums[i].packageName(),
						annotationEnums[i].simpleName());
				targetImportList.add(needImport);
			}
		}
		return targetImportList;
	}

	private static JCImport buildImport(TreeMaker treeMaker, Names names, String packageName, String className) {
		JCIdent ident = treeMaker.Ident(names.fromString(packageName));
		return treeMaker.Import(treeMaker.Select(ident, names.fromString(className)), false);
	}
}