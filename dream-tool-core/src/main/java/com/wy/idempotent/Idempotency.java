package com.wy.idempotent;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解防止重复提交,提升接口幂等性.事例查看dream-study-java-common/com.wy.redis.idempotent
 *
 * @author 飞花梦影
 * @date 2021-02-03 17:32:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotency {

	Class<? extends Idempotent> idempotency() default Idempotent.class;
}