package com.wy.annotation.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

/**
 * 构建内部类
 *
 * @author 飞花梦影
 * @date 2021-12-30 17:51:49
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class AnnotationInnerUtil {

	private static final Logger logger = Logger.getLogger(AnnotationInnerUtil.class.getName());

	private static Map<String, JCTree.JCAssign> providerSourceAnnotationValue = new HashMap<>();

	public static JCTree.JCClassDecl buildInnerClass(TreeMaker treeMaker, Names names,
			JCTree.JCClassDecl sourceClassDecl, java.util.List<JCTree.JCMethodDecl> methodDecls) {
		java.util.List<JCTree.JCVariableDecl> jcVariableDeclList =
				buildInnerClassVar(treeMaker, names, sourceClassDecl);
		String lowerClassName = sourceClassDecl.getSimpleName().toString();
		lowerClassName = lowerClassName.substring(0, 1).toLowerCase().concat(lowerClassName.substring(1));
		java.util.List<JCTree.JCMethodDecl> jcMethodDecls =
				buildInnerClassMethods(treeMaker, names, methodDecls, lowerClassName);
		java.util.List<JCTree> jcTrees = new ArrayList<>();
		jcTrees.addAll(jcVariableDeclList);
		jcTrees.addAll(jcMethodDecls);
		JCTree.JCClassDecl targetClassDecl = treeMaker.ClassDef(buildInnerClassAnnotation(treeMaker,names),
				names.fromString(sourceClassDecl.name.toString().concat("InnerController")), List.nil(), null,
				List.nil(), List.from(jcTrees));
		return targetClassDecl;
	}

	private static java.util.List<JCTree.JCVariableDecl> buildInnerClassVar(TreeMaker treeMaker, Names names,
			JCTree.JCClassDecl jcClassDecl) {
		String parentClassName = jcClassDecl.getSimpleName().toString();
		logger.info("simpleClassName:" + parentClassName);
		java.util.List<JCTree.JCVariableDecl> jcVariableDeclList = new ArrayList<>();
		java.util.List<JCTree.JCAnnotation> jcAnnotations = new ArrayList<>();
		JCTree.JCAnnotation jcAnnotation = makeAnnotation(PackageSupportEnum.Autowired.toString(), List.nil());
		jcAnnotations.add(jcAnnotation);
		JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(treeMaker.Modifiers(1, from(jcAnnotations)),
				names.fromString(parentClassName.substring(0, 1).toLowerCase().concat(parentClassName.substring(1))),
				treeMaker.Ident(names.fromString(parentClassName)), null);
		jcVariableDeclList.add(jcVariableDecl);
		return jcVariableDeclList;
	}

	private static JCTree.JCModifiers buildInnerClassAnnotation(TreeMaker treeMaker, Names names) {
		JCTree.JCExpression jcAssign = AnnotationUtil.makeArg(treeMaker, names, "value",
				providerSourceAnnotationValue.get("feignClientPrefix").rhs.toString().replace("\"", ""));
		JCTree.JCAnnotation jcAnnotation =
				makeAnnotation(PackageSupportEnum.RequestMapping.toString(), List.of(jcAssign));
		JCTree.JCAnnotation restController = makeAnnotation(PackageSupportEnum.RestController.toString(), List.nil());
		JCTree.JCModifiers mods =
				treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC, List.of(jcAnnotation).append(restController));
		return mods;
	}

	// 深度拷贝内部类方法
	private static java.util.List<JCTree.JCMethodDecl> buildInnerClassMethods(TreeMaker treeMaker, Names names,
			java.util.List<JCTree.JCMethodDecl> methodDecls, String serviceName) {
		java.util.List<JCTree.JCMethodDecl> target = new ArrayList<>();
		methodDecls.forEach(e -> {
			if (!e.name.contentEquals("<init>")) {
				java.util.List<JCTree.JCVariableDecl> targetParams = new ArrayList<>();
				e.params.forEach(param -> {
					JCTree.JCVariableDecl newParam = treeMaker.VarDef((JCTree.JCModifiers) param.mods.clone(),
							param.name, param.vartype, param.init);
					logger.info("copy of param:{}" + newParam);
					targetParams.add(newParam);
				});
				JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef((JCTree.JCModifiers) e.mods.clone(), e.name,
						(JCTree.JCExpression) e.restype.clone(), e.typarams, e.recvparam, List.from(targetParams),
						e.thrown, treeMaker.Block(0L, List.nil()), e.defaultValue);
				target.add(methodDecl);
			}
		});
		target.forEach(e -> {
			if (e.params.size() > 0) {
				for (int i = 0; i < e.params.size(); i++) {
					JCTree.JCVariableDecl jcVariableDecl = e.params.get(i);
					if (i == 0) {
						// 第一个参数加requestbody注解,其他参数加requestparam注解,否则会报错
						if (!isBaseVarType(jcVariableDecl.vartype.toString())) {
							jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations
									.append(makeAnnotation(PackageSupportEnum.RequestBody.toString(), List.nil()));
						} else {
							JCTree.JCAnnotation requestParam =
									makeAnnotation(PackageSupportEnum.RequestParam.toString(), List.of(AnnotationUtil
											.makeArg(treeMaker, names, "value", jcVariableDecl.name.toString())));
							jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations.append(requestParam);
						}
					} else {
						JCTree.JCAnnotation requestParam =
								makeAnnotation(PackageSupportEnum.RequestParam.toString(), List.of(AnnotationUtil
										.makeArg(treeMaker, names, "value", jcVariableDecl.name.toString())));
						jcVariableDecl.mods.annotations = jcVariableDecl.mods.annotations.append(requestParam);
					}

				}
			}
			logger.info("sourceMethods: {}" + e);
			// value
			JCTree.JCExpression jcAssign = AnnotationUtil.makeArg(treeMaker, names, "value", "/" + e.name.toString());

			JCTree.JCAnnotation jcAnnotation =
					makeAnnotation(PackageSupportEnum.PostMapping.toString(), List.of(jcAssign));
			logger.info("annotation: {}" + jcAnnotation);
			e.mods.annotations = e.mods.annotations.append(jcAnnotation);
			JCTree.JCExpressionStatement exec =
					getMethodInvocationStat(treeMaker, names, serviceName, e.name.toString(), e.params);
			if (!e.restype.toString().contains("void")) {
				JCTree.JCReturn jcReturn = treeMaker.Return(exec.getExpression());
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
	private static JCTree.JCExpressionStatement getMethodInvocationStat(TreeMaker treeMaker, Names names,
			String invokeFrom, String invokeMethod, List<JCTree.JCVariableDecl> args) {
		java.util.List<JCTree.JCIdent> params = new ArrayList<>();
		args.forEach(e -> {
			params.add(treeMaker.Ident(e.name));
		});
		JCTree.JCIdent invocationFrom = treeMaker.Ident(names.fromString(invokeFrom));
		JCTree.JCFieldAccess jcFieldAccess1 = treeMaker.Select(invocationFrom, names.fromString(invokeMethod));
		JCTree.JCMethodInvocation apply = treeMaker.Apply(List.nil(), jcFieldAccess1, List.from(params));
		JCTree.JCExpressionStatement exec = treeMaker.Exec(apply);
		logger.info("method invoke:" + exec);
		return exec;
	}
}