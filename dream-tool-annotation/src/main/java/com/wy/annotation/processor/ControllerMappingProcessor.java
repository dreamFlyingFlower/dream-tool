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
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import com.wy.annotation.ControllerMapping;
import com.wy.annotation.enums.SpringEnum;
import com.wy.annotation.util.AnnotationUtil;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2022-01-04 10:24:57
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@SupportedAnnotationTypes("com.wy.annotation.ControllerMapping")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@AutoService(Processor.class)
public class ControllerMappingProcessor extends AbstractProcessor {

	/** 用于编译时的输出 */
	private static Messager messager;

	/** AST 树 */
	private JavacTrees trees;

	/** 封装了定义方法,变量,类等等的方法 */
	private TreeMaker treeMaker;

	/** 用于创建标识符 */
	private Names names;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		ControllerMappingProcessor.messager = processingEnv.getMessager();
		this.trees = JavacTrees.instance(processingEnv);
		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
		this.treeMaker = TreeMaker.instance(context);
		this.names = Names.instance(context);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// 得到使用了LombokAll的所有元素
		Set<? extends Element> eleStrSet = roundEnv.getElementsAnnotatedWith(ControllerMapping.class);
		for (Element eleStr : eleStrSet) {
			addClassAnnotation(eleStr);
		}
		return true;
	}

	/**
	 * 该方法最后会给类新增Getter,Setter,ToString,NoArgConstructor,AllArgsConstructor,Builder注解
	 * 
	 * @param element
	 */
	public void addClassAnnotation(Element element) {
		// 获得语法树
		JCTree jcTree = trees.getTree(element);
		ControllerMappingProcessor.messager.printMessage(Diagnostic.Kind.NOTE, jcTree.toString());
		// 修改类的定义
		jcTree.accept(new TreeTranslator() {

			// 遍历类定义
			@Override
			public void visitClassDef(JCClassDecl jcClassDecl) {
				// 创建一个value的赋值语句,作为注解的参数
				// JCExpression arg = AnnotationUtil.makeArg(treeMaker, names, "value", "");
				ControllerMapping annotation = element.getAnnotation(ControllerMapping.class);
				boolean rest = annotation.rest();
				// 创建注解对象
				JCAnnotation jcAnnotation = null;
				if (rest) {
					jcAnnotation = AnnotationUtil.makeAnnotation(treeMaker, names, SpringEnum.RestController.toString(),
							List.nil());
				} else {
					jcAnnotation = AnnotationUtil.makeAnnotation(treeMaker, names, SpringEnum.Controller.toString(),
							List.nil());
				}
				ControllerMappingProcessor.messager.printMessage(Diagnostic.Kind.NOTE,
						"class Annotation add:" + jcAnnotation.toString());
				// 在原有类定义中append新的注解对象
				jcClassDecl.mods.annotations = jcClassDecl.mods.annotations.append(jcAnnotation);
				jcClassDecl.mods.annotations.forEach(e -> {
					ControllerMappingProcessor.messager.printMessage(Diagnostic.Kind.NOTE,
							"class Annotation list:" + e.toString());
				});
				super.visitClassDef(jcClassDecl);
			}
		});
	}
}