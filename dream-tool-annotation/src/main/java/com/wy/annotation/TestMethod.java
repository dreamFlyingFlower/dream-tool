package com.wy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试方法上添加注解
 * 
 * @author 飞花梦影
 * @date 2022-01-10 22:19:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestMethod {

	String value() default "";
}