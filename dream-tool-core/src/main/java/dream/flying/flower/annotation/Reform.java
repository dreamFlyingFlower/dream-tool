package dream.flying.flower.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个标记注解,表示被标记的类已改造完毕
 *
 * @author 飞花梦影
 * @date 2021-03-11 23:23:31
 * @git {@link <a href="https://github.com/dreamFlyingFlower">...</a>}
 */
@Documented
@Target({ ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Reform {

}