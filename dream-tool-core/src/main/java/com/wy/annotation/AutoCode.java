package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动生成Code,需要使用redis
 *
 * @author 飞花梦影
 * @date 2023-06-07 09:39:19
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoCode {

	/**
	 * code前缀,默认使用实体类中所有大写字母拼接
	 * 
	 * @return code前缀
	 */
	String value() default "";

	/**
	 * 是否添加日期,默认添加日期
	 * 
	 * @return true->添加日期时间,false->不添加日期时间
	 */
	boolean containDate() default true;

	/**
	 * 序号长度
	 * 
	 * @return 6
	 */
	int length() default 6;

	/**
	 * 数据更新时是否更新该值
	 * 
	 * @return true->更新,false->不更新
	 */
	boolean update() default false;
}