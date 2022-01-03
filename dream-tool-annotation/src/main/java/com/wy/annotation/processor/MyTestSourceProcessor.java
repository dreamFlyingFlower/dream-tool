package com.wy.annotation.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.wy.annotation.TestSource;
import com.wy.annotation.util.AnnotationUtil;

/**
 * 处理编译时注解{@link TestSource}.注意,编译时注解还没有字节码文件,所以用底层反射会抛异常
 * 
 * {@link Element}:在逻辑上代表语言元素,比如包,类,方法等,是一个接口
 * 
 * <pre>
 * {@link Element#getEnclosingElement}:返回封装此元素的最里层元素,即最近的外层元素
 * 		如果此元素的声明在词法上直接封装在另一个元素的声明中,则返回那个封装元素;
 * 		如果此元素是顶层类型,则返回它的包;如果此元素是一个包,则返回 null; 如果此元素是一个泛型参数,则返回 null
 * 		比如一个类里面有方法,成员变量等,这个类相对于方法跟成员变量就是外层元素,而成员变量和方法相对于类就是内层元素
 * {@link Element#getEnclosedElements}:获得所有的内层元素,比如方法,属性,注解等
 * {@link Element#asType}:返回此元素定义的类型,实际上就是Java类型
 * {@link Element#getKind}:返回此元素的种类:包,类,接口,方法,字段…方法返回一个枚举值ElementKind
 * {@link Element#getModifiers}:返回元素的修饰符,如public,static等
 * {@link Element#getSimpleName}:返回此元素的简单名称,比如activity名,变量就是变量名
 * {@link Element#getAnnotation}:返回此元素指定类型的注解,否则返回null.注解可以是继承的,也可以是直接存在于此元素上的
 * {@link PackageElement}:一个包程序元素
 * {@link TypeElement}:一个类或接口程序元素,相当于当前注解所在的class对象
 * 		typeElement.getQualifiedName().toString():类的绝对路径
 * 		typeElement.getSimpleName().toString():类名
 * 		processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString():包名
 * 		variableElement.getAnnotation(TestSource.class):获取注解
 * 		variableElement.getSimpleName().toString():参数名
 * {@link TypeElement#getNestingKind()}:返回此类型元素的嵌套种类
 * {@link TypeElement#getQualifiedName()}:返回此类型元素的完全限定名称,即规范名称.
 * 		对于没有规范名称的局部类和匿名类,返回一个空名称.譬如 Activity 就是包名+类名
 * {@link TypeElement#getSuperclass()}:返回此类型元素的直接超类.
 * 		如果此类型元素表示一个接口或者类 java.lang.Object,则返回一个种类为 NONE 的 NoType
 * {@link TypeElement#getInterfaces()}:返回直接由此类实现或直接由此接口扩展的接口类型
 * {@link TypeElement#getTypeParameters()}:按照声明顺序返回此类型元素的形式类型参数
 * {@link TypeParameterElement}:一般类,接口,方法或构造方法元素的泛型参数
 * {@link ExecutableElement}:某个类或接口的方法,构造方法或初始化程序(静态或实例),包括注解类型元素
 * {@link VariableElement}:一个字段,enum 常量,方法或构造方法参数,局部变量或异常参数
 * {@link VariableElement#getConstantValue}:变量初始化的值
 * {@link VariableElement#getEnclosingElement}:获取相关类信息
 * </pre>
 * 
 * {@link TypeMirror}:包含自身信息,TypeMirror类型是DeclaredType或者TypeVariable时候可以转化成Element
 * {@link TypeMirror#getKind()}:返回元素类型:Byte,Long,Int等
 * 
 * 编译时主要处理类:
 * 
 * <pre>
 * {@link TreeMaker}:TreeMaker创建语法树节点的所有方法,创建时会为创建出来的JCTree设置pos字段
 * {@link TreeMaker#Modifiers}:创建访问标志语法树节点{@link JCModifiers}
 *		flags:访问标志,使用{@link Flags}枚举,且可以相加
 *		annotations:注解列表
 * {@link TreeMaker#ClassDef}:创建类定义语法树节点{@link JCClassDecl}
 * 		mods:访问标志
 * 		name:类名
 * 		typarams:泛型参数列表
 * 		extending:父类
 * 		implementing:接口列表
 * 		defs:类定义的详细语句,包括字段,方法定义等
 * {@link TreeMaker#MethodDef}:创建方法定义节点{@link JCMethodDecl}
 * 		mods:访问标志
 * 		name:方法名
 * 		restype:返回类型
 * 		typarams:泛型参数列表
 * 		params:参数列表
 * 		thrown:异常声明列表
 * 		body:方法体
 * 		defaultValue:默认方法,可能是interface中的那个default
 * 		m:方法符号
 * 		mtype:方法类型.包含多种类型,泛型参数类型,方法参数类型,异常参数类型,返回参数类型
 * {@link TreeMaker#VarDef:}创建字段/变量定义语法树节点{@link JCVariableDecl}
 * 		mods:访问标志
 * 		vartype:类型
 * 		init:初始化语句
 * 		v:变量符号
 * {@link TreeMaker#Ident(JCVariableDecl)}:创建标识语法树节点{@link JCIdent}
 * {@link TreeMaker#Return}:用于创建return语句语法树节点{@link JCReturn}
 * {@link TreeMaker#NewClass}:创建new语句语法树节点{@link JCNewClass}
 * 		encl:未知
 * 		typeargs:参数类型列表
 * 		clazz:待创建对象的类型
 * 		args:参数列表
 * 		def:类定义
 * {@link TreeMaker#Select}:创建域/方法访问.当是方法访问时,可以和TreeMaker.Apply一起使用{@link JCFieldAccess}
 * 		selected:.运算符左边的表达式
 * 		selector:.运算符右边的名字
 * {@link TreeMaker#Apply}:创建方法调用语法树节点{@link JCMethodInvocation}
 * 		typeargs:参数类型列表
 * 		fn:调用语句
 * 		args:参数列表
 * {@link TreeMaker#Assign}:创建赋值语句语法树节点{@link JCAssign}
 * 		lhs:赋值语句左边表达式
 * 		rhs:赋值语句右边表达式
 * {@link TreeMaker#Exec}:创建可执行语句语法树节点{@link JCExpressionStatement}
 * {@link TreeMaker#Block}:创建组合语句语法树节点{@link JCBlock}
 * 		flags:访问标志
 * 		stats:语句列表
 * {@link JCExpression}:表达式
 * {@link TreeMaker#Annotation}:创建注解{@link JCAnnotation}
 * 		jcTree:元素
 * 		list:参数
 * </pre>
 *
 * @author 飞花梦影
 * @date 2021-12-17 14:28:35
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SupportedAnnotationTypes("com.wy.annotation.TestSource")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class MyTestSourceProcessor extends AbstractProcessor {

	/** 用于编译时的输出 */
	private Messager messager;

	/** AST 树 */
	private JavacTrees trees;

	/** 封装了定义方法,变量,类等等的方法 */
	private TreeMaker treeMaker;

	/** 用于创建标识符 */
	private Names names;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.processingEnv = processingEnv;
		this.messager = processingEnv.getMessager();
		this.trees = JavacTrees.instance(processingEnv);
		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
		this.treeMaker = TreeMaker.instance(context);
		this.names = Names.instance(context);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// 得到使用了TestSource注解的所有元素
		Set<? extends Element> eleStrSet = roundEnv.getElementsAnnotatedWith(TestSource.class);
		for (Element eleStr : eleStrSet) {
			// 获得语法树
			JCTree jcTree = trees.getTree(eleStr);
			this.messager.printMessage(Diagnostic.Kind.NOTE, jcTree.toString());
			// 修改jcTree的方式,可以修改类的定义
			jcTree.accept(new TreeTranslator() {

				@Override
				public void visitClassDef(JCClassDecl jcClassDecl) {
					List<JCVariableDecl> jcVariableDeclList = List.nil();

					for (JCTree tree : jcClassDecl.defs) {
						if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
							JCVariableDecl jcVariableDecl = (JCVariableDecl) tree;
							jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
						}
					}
					jcVariableDeclList.forEach(jcVariableDecl -> {
						jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
					});
					super.visitClassDef(jcClassDecl);
				}
			});
		}
		return true;
	}

	private JCMethodDecl makeGetterMethodDecl(JCVariableDecl jcVariableDecl) {
		ListBuffer<JCStatement> statements = new ListBuffer<>();
		statements.append(treeMaker
				.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
		JCBlock body = treeMaker.Block(0, statements.toList());
		return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()),
				jcVariableDecl.vartype, List.nil(), List.nil(), List.nil(), body, null);
	}

	/**
	 * 获取新方法名,get + 将第一个字母大写 + 后续部分, 例如 value 变为 getValue
	 * 
	 * @param name
	 * @return
	 */
	private Name getNewMethodName(Name name) {
		String s = name.toString();
		return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
	}

	private Map<String, JCAssign> consumerSourceAnnotationValue = new HashMap<>();

	/**
	 * 获取源注解的参数
	 * 
	 * @param jcTree
	 */
	public void test(JCTree jcTree) {
		jcTree.accept(new TreeTranslator() {

			@Override
			public void visitAnnotation(JCAnnotation jcAnnotation) {
				JCIdent jcIdent = (JCIdent) jcAnnotation.getAnnotationType();
				if (jcIdent.name.contentEquals("MobCloudConsumer")) {
					messager.printMessage(Diagnostic.Kind.NOTE,
							"class Annotation arg process:" + jcAnnotation.toString());
					jcAnnotation.args.forEach(e -> {
						JCAssign jcAssign = (JCAssign) e;
						JCIdent value = treeMaker.Ident(names.fromString("value"));
						JCAssign targetArg = treeMaker.Assign(value, jcAssign.rhs);
						consumerSourceAnnotationValue.put(jcAssign.lhs.toString(), targetArg);
					});
				}
				messager.printMessage(Diagnostic.Kind.NOTE, "获取参数如下:" + consumerSourceAnnotationValue);
				super.visitAnnotation(jcAnnotation);
			}
		});
	}

	/**
	 * 该方法最后会给类新增一个@FeignClient(value="")的注解
	 * 
	 * @param element
	 */
	public void addClassAnnotation(Element element) {
		JCTree jcTree = trees.getTree(element);
		jcTree.accept(new TreeTranslator() {

			// 遍历所有类定义
			@Override
			public void visitClassDef(JCClassDecl jcClassDecl) {
				// 创建一个value的赋值语句,作为注解的参数
				JCExpression arg = AnnotationUtil.makeArg(treeMaker, names, "value", "");
				// 创建注解对象
				JCAnnotation jcAnnotation =
						AnnotationUtil.makeAnnotation(treeMaker, names, "lombok.Getter", List.of(arg));
				messager.printMessage(Diagnostic.Kind.NOTE, "class Annotation add:" + jcAnnotation.toString());
				// 在原有类定义中append新的注解对象
				jcClassDecl.mods.annotations = jcClassDecl.mods.annotations.append(jcAnnotation);
				jcClassDecl.mods.annotations.forEach(e -> {
					messager.printMessage(Diagnostic.Kind.NOTE, "class Annotation list:" + e.toString());
				});
				super.visitClassDef(jcClassDecl);
			}
		});
	}

	/**
	 * 给类增加import语句
	 * 
	 * @param element
	 * @param packageSupportEnums
	 */
	public void addImport(Element element, PackageEnum... packageSupportEnums) {
		TreePath treePath = trees.getPath(element);
		JCCompilationUnit jccu = (JCCompilationUnit) treePath.getCompilationUnit();
		java.util.List<JCTree> trees = new ArrayList<>();
		trees.addAll(jccu.defs);
		java.util.List<JCTree> sourceImportList = new ArrayList<>();
		trees.forEach(e -> {
			if (e.getKind().equals(Tree.Kind.IMPORT)) {
				sourceImportList.add(e);
			}
		});
		java.util.List<JCImport> needImportList = buildImportList(packageSupportEnums);
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

	private java.util.List<JCImport> buildImportList(PackageEnum... packageSupportEnums) {
		java.util.List<JCImport> targetImportList = new ArrayList<>();
		if (packageSupportEnums.length > 0) {
			for (int i = 0; i < packageSupportEnums.length; i++) {
				JCImport needImport =
						buildImport(packageSupportEnums[i].getPackageName(), packageSupportEnums[i].getClassName());
				targetImportList.add(needImport);
			}
		}
		return targetImportList;
	}

	private JCImport buildImport(String packageName, String className) {
		JCIdent ident = treeMaker.Ident(names.fromString(packageName));
		return treeMaker.Import(treeMaker.Select(ident, names.fromString(className)), false);
	}
}