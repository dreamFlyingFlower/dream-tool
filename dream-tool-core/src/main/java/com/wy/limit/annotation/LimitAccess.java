package com.wy.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.wy.limit.LimitAccessHandler;

/**
 * 访问限制,可查看dream-study-java-common/com.wy.access/AccessLimitInterceptor
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
	 * 是否使用自定义的拦截器实现,默认false
	 * 
	 * @return true->是,false->否
	 */
	boolean custom() default false;

	/**
	 * 限制访问处理类
	 * 
	 * @return 限制访问处理类
	 */
	Class<? extends LimitAccessHandler> handler() default LimitAccessHandler.class;
}