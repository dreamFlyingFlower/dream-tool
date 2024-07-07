package dream.flying.flower.annotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 处理自定义的注解Model,编译时使用
 * 
 * @author 飞花梦影
 * @date 2020-12-21 20:05:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ModelProcesser extends AbstractProcessor {

	/**
	 * 每个注解处理器都必须有一个空构造方法,父类已实现.该方法会被构造器调用并传入ProcessingEnvironment参数,
	 * 该参数提供了很多工具类:如Elements,Filer,Messager,Types
	 */
	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);
	}

	/**
	 * 该方法相当于注解处理器的入口main(),在编译时对注解进行的处理,
	 * 比如对注解的扫描,评估和处理,以及后续的其他操作,比如生成其他java代码文件,都是在这里发生的
	 * 
	 * 参数RoundEnvironment可以查出包含特定注解的被注解元素
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		return false;
	}

	/**
	 * 获取通过注解@SupportedOptions设置的可支持的输入选项值
	 */
	@Override
	public Set<String> getSupportedOptions() {
		return super.getSupportedOptions();
	}

	/**
	 * 该方法是指定要处理哪些注解,若想处理自定义的注解,则必须重写该方法,且要在这里指明需要处理的注解全称
	 * 
	 * 返回值是一个字符串的集合,包含着本处理器想要处理的注解类型的合法全称
	 */
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return super.getSupportedAnnotationTypes();
	}

	/**
	 * 指明支持的java版本,一般使用SourceVersion.latestSupported()就可以
	 */
	@Override
	public SourceVersion getSupportedSourceVersion() {
		return super.getSupportedSourceVersion();
	}
}