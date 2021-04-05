package com.wy.limit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 访问限制
 * 
 * @author 飞花梦影
 * @date 2021-02-16 11:01:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LimitAccess {

	/**
	 * 限制访问的时间,默认1秒
	 * 
	 * @return 时间
	 */
	long value() default 1l;

	/**
	 * 限制访问的时间单位,默认秒
	 * 
	 * @return 时间单位
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 限制访问的最大次数
	 * 
	 * @return 最大访问次数
	 */
	int count() default 10;

	/**
	 * 是否需要登录,默认不需要
	 * 
	 * @return true需要,false不需要
	 */
	boolean login() default false;

	/**
	 * 限制访问处理类
	 * 
	 * @return 限制访问处理类
	 */
	Class<? extends LimitAccesser> handler() default LimitAccesser.class;
}