package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给类上添加lombok的Getter,Setter,ToString,NoArgConstructor,AllArgsConstructor,Builder
 *
 * @author 飞花梦影
 * @date 2021-12-29 17:42:16
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface LombokAll {

}