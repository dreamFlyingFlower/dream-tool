package com.wy.db.annotation;

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
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
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
	boolean validSingle() default true;

	/**
	 * 当value为""时,取实体类Java属性名,实体类是否需要转换为蛇形,即下划线模式
	 * 
	 * @return 默认true->转下划线,false->不转下划线
	 */
	boolean hump2Snake() default true;

	/**
	 * 定义接收原始值的Java属性名
	 * 
	 * 当唯一字段进行更新时,需要将注解属性更新前的原始值传入到另外一个指定的Java属性中,另外的Java属性接收原始值
	 * 
	 * <pre>
	 * 1.当该方法不为""时,根据反射取方法返回的Java属性名在实体类中查找.若该Java属性在实体类中没有,抛异常
	 * 2.当该方法为""时,将在注解Java属性前添加ori并变成驼峰从实体类中查找,如username将变成oriUsername查找.若该Java属性在实体类中没有,抛异常
	 * 3.接收原始值的Java属性名必须在类中真实存在,否则抛异常
	 * </pre>
	 * 
	 * @return 修改时接收原始值的Java属性名
	 */
	String oriName() default "";
}