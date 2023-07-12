package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键,只考虑单主键
 * 
 * @author 飞花梦影
 * @date 2021-01-13 17:10:28
 * @git {@link https://github.com/mygodness100}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pri {

}