package com.wy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试编译时在类上添加注解是否生效
 *
 * @author 飞花梦影
 * @date 2021-12-17 14:27:12
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface TestSource {

	String value() default "success";
}