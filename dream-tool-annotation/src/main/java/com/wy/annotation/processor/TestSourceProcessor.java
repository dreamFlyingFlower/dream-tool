package com.wy.annotation.processor;

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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.wy.annotation.TestSource;

/**
 * 处理编译时注解{@link TestSource}
 *
 * @author 飞花梦影
 * @date 2021-12-17 14:28:35
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SupportedAnnotationTypes("com.wy.annotation.TestSource")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class TestSourceProcessor extends AbstractProcessor {

	// 用于编译时的输出
	private Messager messager;

	// AST 树
	private JavacTrees trees;

	// 封装了定义方法,变量,类等等的方法
	private TreeMaker treeMaker;

	// 用于创建标识符
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
		// 得到使用了TestSource注解的元素
		Set<? extends Element> eleStrSet = roundEnv.getElementsAnnotatedWith(TestSource.class);
		for (Element eleStr : eleStrSet) {
			// 获得所有的方法元素
			// List<? extends Element> enclosedElements = eleStr.getEnclosedElements();
			JCTree jcTree = trees.getTree(eleStr);
			this.messager.printMessage(Diagnostic.Kind.NOTE, jcTree.toString());
			// 修改jcTree的方式,可以修改类的定义
			jcTree.accept(new TreeTranslator() {

				@Override
				public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
					List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();

					for (JCTree tree : jcClassDecl.defs) {
						if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
							JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
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

	private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
		ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
		statements.append(treeMaker
				.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
		JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
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
}