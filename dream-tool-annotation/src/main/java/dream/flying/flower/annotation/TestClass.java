package dream.flying.flower.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试类上添加注解
 * 
 * @author 飞花梦影
 * @date 2022-01-10 20:55:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface TestClass {

	String value() default "";
}