package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在需要进行递归查询的表中,对进行递归的中间字段进行标识
 * 
 * @author 飞花梦影
 * @date 2021-01-13 23:37:09
 * @git {@link https://github.com/mygodness100}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pid {

	/**
	 * 对应的数据库字段,若为空,则根据Java属性转下划线后使用
	 * 
	 * @return 数据库字段
	 */
	String value() default "";

	/**
	 * Java属性是否转下划线,默认转
	 * 
	 * @return Java属性是否转下划线.true->转,false->不转
	 */
	boolean hump2Snake() default true;
}