package dream.flying.flower;

import com.google.auto.service.AutoService;

/**
 * 自定义编译时注解
 * 
 * auto-services:该JAR包会在项目打成jar包时自动生成/META-INF/services/javax.annotation.processing.Processor
 * javac会检测JAR中javax.annotation.processing.Processor文件,编译时就会调用到.
 * 
 * {@link AutoService}:需要在处理编译时注解的了上添加,会自动生成该类
 * 
 * @author 飞花梦影
 * @date 2021-03-07 16:42:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstantAnnotation {

}