package com.dream.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个标记注解,表示被标记的对象可为null
 * 
 * @author 飞花梦影
 * @date 2021-03-11 11:25:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target({ElementType.TYPE_USE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Nullable {

}