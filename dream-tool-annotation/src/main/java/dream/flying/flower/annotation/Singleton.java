package dream.flying.flower.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单例模式注解
 *
 * @author 飞花梦影
 * @date 2021-12-17 16:22:40
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Singleton {

}