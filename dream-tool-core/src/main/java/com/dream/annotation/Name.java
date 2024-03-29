package com.dream.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对应字符串主键,默认生成不带-的32位uuid
 * 
 * @auther 飞花梦影
 * @date 2018-11-19 00:58:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Name {

	String value() default "";

	boolean hump2Snake() default true;
}