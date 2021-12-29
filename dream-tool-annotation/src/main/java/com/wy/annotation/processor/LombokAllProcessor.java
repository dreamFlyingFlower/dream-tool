package com.wy.annotation.processor;

import java.util.Set;
import java.util.logging.Logger;

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
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import com.wy.annotation.LombokAll;
import com.wy.annotation.TestSource;
import com.wy.annotation.util.AnnotationUtil;

/**
 * 处理{@link LombokAll}注解
 *
 * @author 飞花梦影
 * @date 2021-12-29 17:42:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SupportedAnnotationTypes("com.wy.annotation.LombokAll")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class LombokAllProcessor extends AbstractProcessor {

	private static final Logger logger = Logger.getLogger(MyTestSourceProcessor.class.getName());

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
			// 获得所有的方法元素
			// List<? extends Element> enclosedElements = eleStr.getEnclosedElements();
			// 获得语法树
			JCTree jcTree = trees.getTree(eleStr);
			this.messager.printMessage(Diagnostic.Kind.NOTE, jcTree.toString());
			// 修改jcTree的方式,可以修改类的定义
			jcTree.accept(new TreeTranslator() {

				@Override
				public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
					addClassAnnotation(eleStr);
				}
			});
		}
		return true;
	}

	/**
	 * 该方法最后会给类新增一个@Getter注解
	 * 
	 * @param element
	 */
	public void addClassAnnotation(Element element) {
		JCTree jcTree = trees.getTree(element);
		jcTree.accept(new TreeTranslator() {

			// 遍历所有类定义
			@Override
			public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
				// 创建一个value的赋值语句,作为注解的参数
				JCTree.JCExpression arg = makeArg("value", "");
				// 创建注解对象
				JCTree.JCAnnotation jcAnnotation =
						AnnotationUtil.makeAnnotation(treeMaker, names, "lombok.Getter", List.of(arg));
				logger.info("class Annotation add:" + jcAnnotation.toString());
				// 在原有类定义中append新的注解对象
				jcClassDecl.mods.annotations = jcClassDecl.mods.annotations.append(jcAnnotation);
				jcClassDecl.mods.annotations.forEach(e -> {
					logger.info("class Annotation list:" + e.toString());
				});
				super.visitClassDef(jcClassDecl);
			}
		});
	}

	public JCTree.JCExpression makeArg(String key, String value) {
		// 注解需要的参数是表达式,这里的实际实现为等式对象,Ident是值,Literal是value,最后结果为a=b
		return treeMaker.Assign(treeMaker.Ident(names.fromString(key)), treeMaker.Literal(value));
	}
}