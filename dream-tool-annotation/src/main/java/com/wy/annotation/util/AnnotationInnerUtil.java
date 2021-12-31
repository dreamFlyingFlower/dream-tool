package com.wy.annotation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.JCDiagnostic.DiagnosticFlag;

/**
 * 构建内部类
 *
 * @author 飞花梦影
 * @date 2021-12-30 17:51:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AnnotationInnerUtil {

	private static Map<String, JCTree.JCAssign> providerSourceAnnotationValue = new HashMap<>();

	/**
	 * 构建一个内部类
	 * 
	 * @param treeMaker
	 * @param names
	 * @param messager
	 * @param sourceClassDecl
	 * @param methodDecls
	 * @return
	 */
	public static JCClassDecl buildInnerClass(TreeMaker treeMaker, Names names, Messager messager,
			JCClassDecl sourceClassDecl, java.util.List<JCMethodDecl> methodDecls) {
		java.util.List<JCVariableDecl> jcVariableDeclList =
				buildInnerClassVar(treeMaker, names, messager, sourceClassDecl);
		String lowerClassName = sourceClassDecl.getSimpleName().toString();
		lowerClassName = lowerClassName.substring(0, 1).toLowerCase().concat(lowerClassName.substring(1));
		java.util.List<JCMethodDecl> jcMethodDecls =
				buildInnerClassMethods(treeMaker, names, messager, methodDecls, lowerClassName);
		java.util.List<JCTree> jcTrees = new ArrayList<>();
		jcTrees.addAll(jcVariableDeclList);
		jcTrees.addAll(jcMethodDecls);
		JCClassDecl targetClassDecl = treeMaker.ClassDef(buildInnerClassAnnotation(treeMaker, names),
				names.fromString(sourceClassDecl.name.toString().concat("InnerController")), List.nil(), null,
				List.nil(), List.from(jcTrees));
		return targetClassDecl;
	}

	private static java.util.List<JCVariableDecl> buildInnerClassVar(TreeMaker treeMaker, Names names,
			Messager messager, JCClassDecl jcClassDecl) {
		String parentClassName = jcClassDecl.getSimpleName().toString();
		messager.printMessage(Diagnostic.Kind.NOTE, "simpleClassName:" + parentClassName);
		java.util.List<JCVariableDecl> jcVariableDeclList = new ArrayList<>();
		java.util.List<JCAnnotation> jcAnnotations = new ArrayList<>();
		JCAnnotation jcAnnotation = AnnotationUtil.makeAnnotation(Autowired.toString(), List.nil());
		jcAnnotations.add(jcAnnotation);
		JCVariableDecl jcVariableDecl = treeMaker.VarDef(treeMaker.Modifiers(1, from(jcAnnotations)),
				names.fromString(parentClassName.substring(0, 1).toLowerCase().concat(parentClassName.substring(1))),
				treeMaker.Ident(names.fromString(parentClassName)), null);
		jcVariableDeclList.add(jcVariableDecl);
		return jcVariableDeclList;
	}

	private static JCModifiers buildInnerClassAnnotation(TreeMaker treeMaker, Names names) {
		JCExpression jcAssign = AnnotationUtil.makeArg(treeMaker, names, "value",
				providerSourceAnnotationValue.get("feignClientPrefix").rhs.toString().replace("\"", ""));
		JCAnnotation jcAnnotation = AnnotationUtil.makeAnnotation(RequestMapping.toString(), List.of(jcAssign));
		JCAnnotation restController = AnnotationUtil.makeAnnotation(RestController.toString(), List.nil());
		JCModifiers mods =
				treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC, List.of(jcAnnotation).append(restController));
		return mods;
	}

	/**
	 * 深度拷贝内部类方法
	 * 
	 * @param treeMaker
	 * @param names
	 * @param methodDecls
	 * @param serviceName
	 * @return
	 */
	private static java.util.List<JCMethodDecl> buildInnerClassMethods(TreeMaker treeMaker, Names names,
			Messager messager, java.util.List<JCMethodDecl> methodDecls, String serviceName) {
		java.util.List<JCMethodDecl> target = new ArrayList<>();
		methodDecls.forEach(e -> {
			if (!e.name.contentEquals("<init>")) {
				java.util.List<JCVariableDecl> targetParams = new ArrayList<>();
				e.params.forEach(param -> {
					JCVariableDecl newParam =
							treeMaker.VarDef((JCModifiers) param.mods.clone(), param.name, param.vartype, param.init);
					messager.printMessage(Diagnostic.Kind.NOTE, "copy of param:{}" + newParam);
					targetParams.add(newParam);
				});
				JCMethodDecl methodDecl = treeMaker.MethodDef((JCModifiers) e.mods.clone(), e.name,
						(JCExpression) e.restype.clone(), e.typarams, e.recvparam, List.from(targetParams), e.thrown,
						treeMaker.Block(0L, List.nil()), e.defaultValue);
				target.add(methodDecl);
			}
		});
		target.forEach(e -> {
			if (e.params.size() > 0) {
				for (int i = 0; i < e.params.size(); i++) {
					JCVariableDecl jcVariableDecl = e.params.get(i);
					if (i == 0) {
						// 第一个参数加requestbody注解,其他参数加requestparam注解,否则会报错
						if (!isBaseVarType(jcVariableDecl.vartype.toString())) {
							jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations
									.append(AnnotationUtil.makeAnnotation(RequestBody.toString(), List.nil()));
						} else {
							JCAnnotation requestParam = AnnotationUtil.makeAnnotation(RequestParam.toString(), List.of(
									AnnotationUtil.makeArg(treeMaker, names, "value", jcVariableDecl.name.toString())));
							jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations.append(requestParam);
						}
					} else {
						JCAnnotation requestParam = makeAnnotation(AnnotationUtil.RequestParam.toString(), List
								.of(AnnotationUtil.makeArg(treeMaker, names, "value", jcVariableDecl.name.toString())));
						jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations.append(requestParam);
					}

				}
			}
			messager.printMessage(Diagnostic.Kind.NOTE, "sourceMethods: {}" + e);
			// value
			JCExpression jcAssign = AnnotationUtil.makeArg(treeMaker, names, "value", "/" + e.name.toString());

			JCAnnotation jcAnnotation = makeAnnotation(PostMapping.toString(), List.of(jcAssign));
			messager.printMessage(Diagnostic.Kind.NOTE, "annotation: {}" + jcAnnotation);
			e.mods.annotations = e.mods.annotations.append(jcAnnotation);
			JCExpressionStatement exec =
					getMethodInvocationStat(treeMaker, names, messager, serviceName, e.name.toString(), e.params);
			if (!e.restype.toString().contains("void")) {
				JCReturn jcReturn = treeMaker.Return(exec.getExpression());
				e.body.stats = e.body.stats.append(jcReturn);
			} else {
				e.body.stats = e.body.stats.append(exec);
			}

		});
		return List.from(target);
	}

	/**
	 * 创建方法调用,如String.format()这种
	 * 
	 * @param treeMaker
	 * @param names
	 * @param invokeFrom
	 * @param invokeMethod
	 * @param args
	 * @return
	 */
	private static JCExpressionStatement getMethodInvocationStat(TreeMaker treeMaker, Names names, Messager messager,
			String invokeFrom, String invokeMethod, List<JCVariableDecl> args) {
		java.util.List<JCIdent> params = new ArrayList<>();
		args.forEach(e -> {
			params.add(treeMaker.Ident(e.name));
		});
		JCIdent invocationFrom = treeMaker.Ident(names.fromString(invokeFrom));
		JCFieldAccess jcFieldAccess1 = treeMaker.Select(invocationFrom, names.fromString(invokeMethod));
		JCMethodInvocation apply = treeMaker.Apply(List.nil(), jcFieldAccess1, List.from(params));
		JCExpressionStatement exec = treeMaker.Exec(apply);
		messager.printMessage(Diagnostic.Kind.NOTE, "method invoke:" + exec);
		return exec;
	}
}