package com.wy.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 结合GUAUA的RateLimiter限流.可查看dream-study-java-web/com.wy.interceptor/RateLimitInterceptor
 *
 * @author 飞花梦影
 * @date 2021-12-21 09:29:27
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRate {

	/**
	 * 限流时key值.若不写,默认为请求URI地址
	 * 
	 * @return key
	 */
	String value();

	/**
	 * 限流每秒流速
	 * 
	 * @return 默认10
	 */
	double limit() default 10;
}