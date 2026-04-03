package dream.flying.flower.db.annotation;

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
	 * 重复时的提示信息
	 * 
	 * @return 提示信息
	 */
	String value() default "";

	/**
	 * 在同一个类中若存在多个unique时,每个字段是否单独校验
	 * 
	 * @return true->单独校验;false->所有false的字段同时检验
	 */
	boolean single() default false;

	/**
	 * 待校验的字段名,若为空,默认为Java属性名
	 * 
	 * @return 待校验的字段名
	 */
	String columnName() default "";

	/**
	 * 待校验的字段名是否需要转换为下划线
	 * 
	 * @return 默认true->转下划线,false->不变
	 */
	boolean columnUnderLine() default true;

	/**
	 * 检查唯一字段时用来判断值是否重复的字段
	 * 
	 * @return 判断是否重复的字段
	 */
	String[] identifyName() default { "id" };

	/**
	 * identifyName是否转为下划线,默认全部转,identifyName()不为空时有效
	 * 
	 * @return true->下划线;false->不变
	 */
	boolean identifyUnderLine() default true;
}