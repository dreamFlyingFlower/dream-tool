//package com.wy.annotation.processor;
//
//import java.util.Set;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.Messager;
//import javax.annotation.processing.ProcessingEnvironment;
//import javax.annotation.processing.Processor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.annotation.processing.SupportedSourceVersion;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//
//import com.google.auto.service.AutoService;
//import com.sun.source.tree.Tree;
//import com.sun.tools.javac.api.JavacTrees;
//import com.sun.tools.javac.code.Flags;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.JCTree.JCBlock;
//import com.sun.tools.javac.tree.JCTree.JCClassDecl;
//import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
//import com.sun.tools.javac.tree.JCTree.JCIdent;
//import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
//import com.sun.tools.javac.tree.JCTree.JCModifiers;
//import com.sun.tools.javac.tree.JCTree.JCNewClass;
//import com.sun.tools.javac.tree.JCTree.JCReturn;
//import com.sun.tools.javac.tree.JCTree.JCStatement;
//import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.tree.TreeTranslator;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.List;
//import com.sun.tools.javac.util.ListBuffer;
//import com.sun.tools.javac.util.Names;
//import com.wy.annotation.Singleton;
//import com.wy.annotation.util.AnnotationUtil;
//
///**
// * 单例注解处理类
// *
// * @author 飞花梦影
// * @date 2021-12-17 16:23:55
// * @git {@link https://github.com/dreamFlyingFlower }
// */
//@SupportedAnnotationTypes("com.wy.annotation.Singleton")
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
//@AutoService(Processor.class)
//public class SingletonProcessor extends AbstractProcessor {
//
//	// 用于编译时的输出
//	private Messager messager;
//
//	// AST 树
//	private JavacTrees trees;
//
//	// 封装了定义方法,变量,类等等的方法
//	private TreeMaker treeMaker;
//
//	// 用于创建标识符
//	private Names names;
//
//	@Override
//	public synchronized void init(ProcessingEnvironment processingEnv) {
//		super.init(processingEnv);
//		this.messager = processingEnv.getMessager();
//		this.trees = JavacTrees.instance(processingEnv);
//		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
//		this.treeMaker = TreeMaker.instance(context);
//		this.names = Names.instance(context);
//	}
//
//	@Override
//	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//		// 获得所有被Singleton标注的类的集合
//		Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Singleton.class);
//		this.messager.printMessage(Diagnostic.Kind.NOTE, set.size() + "");
//		set.forEach(element -> {
//			JCTree jcTree = trees.getTree(element);
//			jcTree.accept(new TreeTranslator() {
//
//				@Override
//				public void visitClassDef(JCClassDecl jcClassDecl) {
//					createPrivateConstructor(jcClassDecl);
//					JCClassDecl innerClass = createInnerClass(jcClassDecl);
//					createReturnInstance(jcClassDecl, innerClass);
//				}
//			});
//		});
//		return true;
//	}
//
//	/**
//	 * 去掉默认公有的无参数的构造函数,添加一个私有的无参数构造函数
//	 * 
//	 * @param singletonClass 被注解标注的类的包装类
//	 */
//	private void createPrivateConstructor(JCClassDecl singletonClass) {
//		// 定义修饰符
//		JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE);
//		// 定义代码块.参数:行数,代码
//		JCBlock block = treeMaker.Block(0L, List.nil());
//		// 定义方法.参数:修饰符;函数名;方法返回的类型;泛型参数;参数;throw;函数代码块,这里是空代码块;默认值
//		JCMethodDecl constructor = treeMaker.MethodDef(modifiers, names.fromString("<init>"), null, List.nil(),
//				List.nil(), List.nil(), block, null);
//		// 去掉默认的构造函数
//		ListBuffer<JCTree> out = new ListBuffer<>();
//		for (JCTree tree : singletonClass.defs) {
//			// 是否公有无参数的构造函数
//			if (constrcutorDefault(tree)) {
//				continue;
//			}
//			out.add(tree);
//		}
//		out.add(constructor);
//		singletonClass.defs = out.toList();
//	}
//
//	private boolean constrcutorDefault(JCTree jcTree) {
//		if (jcTree.getKind() == Tree.Kind.METHOD) {
//			JCMethodDecl jcMethodDecl = (JCMethodDecl) jcTree;
//			return AnnotationUtil.constrcutorDefault(jcMethodDecl);
//		}
//		return false;
//	}
//
//	// 创建静态内联类,效果如下
//	// private static class UserInner {
//	// private static User instance = new User();
//	// }
//	private JCClassDecl createInnerClass(JCClassDecl singletonClass) {
//		// 参数:修饰符;类名;泛型参数;extending;inplemneting;类定义的详细语句,包括字段,方法定义等
//		JCClassDecl innerClass = treeMaker.ClassDef(treeMaker.Modifiers(Flags.PRIVATE | Flags.STATIC),
//				names.fromString(singletonClass.name + "Inner"), List.nil(), null, List.nil(), List.nil());
//		// 给类添加添加 instance变量
//		addInstanceVar(innerClass, singletonClass);
//		singletonClass.defs = singletonClass.defs.append(innerClass);
//		return innerClass;
//	}
//
//	private void addInstanceVar(JCClassDecl innerClass, JCClassDecl singletonClass) {
//		// 获取注解的类型
//		JCIdent singletonClassType = treeMaker.Ident(singletonClass.name);
//		// new User() 的语句
//		// 参数:encl,enclosingExpression lambda 箭头吗？不太清楚,参数类型列表,待创建对象的类型,参数类,类定义
//		JCNewClass newKeyword = treeMaker.NewClass(null, List.nil(), singletonClassType, List.nil(), null);
//		JCModifiers fieldMod = treeMaker.Modifiers(Flags.PRIVATE | Flags.STATIC | Flags.FINAL);
//		// 定义变量:修饰符,变量名,类型,赋值语句
//		JCVariableDecl instanceVar =
//				treeMaker.VarDef(fieldMod, names.fromString("instance"), singletonClassType, newKeyword);
//		innerClass.defs = innerClass.defs.prepend(instanceVar);
//	}
//
//	// 创建一个成员函数,返回内联类中的 instance 变量
//	// public static User getInstance() {
//	// return UserInner.instance;
//	// }
//	private void createReturnInstance(JCClassDecl singletonClass, JCClassDecl innerClass) {
//		JCIdent singletonClassType = treeMaker.Ident(singletonClass.name);
//		// 获取 return 语句块
//		JCBlock body = addReturnBlock(innerClass);
//		// 创建方法
//		JCMethodDecl methodDec = treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC),
//				this.names.fromString("getInstance"), singletonClassType, List.nil(), List.nil(), List.nil(), body,
//				null);
//		singletonClass.defs = singletonClass.defs.prepend(methodDec);
//	}
//
//	private JCTree.JCBlock addReturnBlock(JCClassDecl holderInnerClass) {
//		JCIdent holderInnerClassType = treeMaker.Ident(holderInnerClass.name);
//		// 获取内联类中的静态变量
//		JCFieldAccess instanceVarAccess = treeMaker.Select(holderInnerClassType, names.fromString("instance"));
//		// 创建 return 语句
//		JCReturn returnValue = treeMaker.Return(instanceVarAccess);
//		ListBuffer<JCStatement> statements = new ListBuffer<>();
//		// 添加返回语句
//		statements.append(returnValue);
//		return treeMaker.Block(0L, statements.toList());
//	}
//}