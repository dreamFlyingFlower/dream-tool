package com.dream.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有此注解标识该方法或字段在某个事件中除外,比如说写入某个excel文件时不写入字段
 *
 * @author 飞花梦影
 * @date 2021-04-06 15:10:23
 * @git {@link <a href="https://github.com/dreamFlyingFlower">...</a>}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface Except {

}