package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个标记注解,表示被标记的对象是一个事例,并不能作为工具使用
 * 
 * @author 飞花梦影
 * @date 2021-03-11 23:23:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Example {

}