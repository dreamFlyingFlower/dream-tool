package com.wy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 当字段上有该注解时,表示字段的值在数据库中唯一
 *
 * @author 飞花梦影
 * @date 2019-07-30 17:21:07
 * @git {@link <a href="https://github.com/dreamFlyingFlower">...</a>}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Unique {

	/**
	 * 数据库字段的名称,若为空,默认为Java属性名
	 * 
	 * @return 数据库字段名
	 */
	String value() default "";

	/**
	 * 在同一个类中若存在多个unique时,每个字段是否单独校验
	 * 
	 * @return true->单独校验;false->所有false的字段同时检验
	 */
	boolean validSingle() default false;

	/**
	 * 实体类是否需要转换为下划线模式,当value为""时,取实体类Java属性名
	 * 
	 * @return 默认true->转下划线,false->不转下划线
	 */
	boolean hump2Snake() default true;

	/**
	 * 检查唯一字段时用来判断值是否重复的字段
	 * 
	 * @return 判断是否重复的字段
	 */
	String[] identifys() default { "id" };
}