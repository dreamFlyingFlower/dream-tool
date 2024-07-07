package dream.flying.flower.annotation.processor;

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
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import dream.flying.flower.annotation.ControllerMapping;
import dream.flying.flower.annotation.enums.SpringEnum;
import dream.flying.flower.annotation.util.AnnotationUtil;

/**
 * 处理{@link ControllerMapping}
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
		messager = processingEnv.getMessager();
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
			addAnnotation(eleStr);
		}
		return true;
	}

	/**
	 * 该方法最后会给类新增RestController或Controller注解
	 * 
	 * @param element
	 */
	public void addAnnotation(Element element) {
		// 获得语法树
		JCTree jcTree = trees.getTree(element);
		ControllerMappingProcessor.messager.printMessage(Diagnostic.Kind.NOTE, jcTree.toString());
		// 修改类的定义
		jcTree.accept(new TreeTranslator() {

			// 遍历类定义
			@Override
			public void visitClassDef(JCClassDecl jcClassDecl) {
				ControllerMapping annotation = element.getAnnotation(ControllerMapping.class);
				AnnotationUtil.addAnnotation(treeMaker, names, jcClassDecl,
						annotation.rest() ? SpringEnum.RestController.toString() : SpringEnum.Controller.toString());
				jcClassDecl.mods.annotations.forEach(e -> {
					messager.printMessage(Diagnostic.Kind.NOTE, "class Annotation list:" + e.toString());
				});
				super.visitClassDef(jcClassDecl);
			}

			@Override
			public void visitMethodDef(JCMethodDecl jcMethodDecl) {

				List<JCAnnotation> annotations = jcMethodDecl.mods.getAnnotations();
				for (JCAnnotation jcAnnotation : annotations) {
					// 获得注解类型
					// jcAnnotation.annotationType;
					// 获得注解类型简称
					// jcAnnotation.annotationType.toString();
					// 获得注解类型的全路径名
					// jcAnnotation.annotationType.type.toString();
					if (jcAnnotation.annotationType.type.toString().equals("com.wy.annotation.TestAdd")) {
						AnnotationUtil.addAnnotation(treeMaker, names, jcMethodDecl, "com.wy.annotation.TestMethod",
								List.of(AnnotationUtil.makeArg(treeMaker, names, "value", "testettt")));
						List<JCExpression> args = jcAnnotation.args;
						for (JCExpression jcExpression : args) {
							System.out.println(jcExpression);
							JCAssign jcAssign = (JCAssign) jcExpression;
							// 获得表达式左边的值,此处是value
							JCExpression lhs = jcAssign.lhs;
							System.out.println(lhs);
							// 获得表达式右边的值
							JCExpression rhs = jcAssign.rhs;
							System.out.println(rhs);
							// 修改表达式右边的值
							jcAssign.rhs = treeMaker.Literal("测试我的11");
						}
					}
				}
				super.visitMethodDef(jcMethodDecl);
			}
		});
	}
}