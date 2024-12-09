package dream.flying.flower.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dream.flying.flower.db.enums.SortType;

/**
 * 在业务层检测是否有排序字段
 * 
 * @auther 飞花梦影
 * @date 2021-05-05 23:54:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sort {

	/**
	 * 默认排序字段为实体类字段名.当value有值时,直接使用该值进行数据库查询,不进行转下划线判断
	 * 
	 * @return 排序的Java属性名
	 */
	String value() default "";

	/**
	 * 当value为""时,取实体类字段的名称,实体类是否需要转换为下划线
	 * 
	 * @return true->转下划线,false->不转下划线
	 */
	boolean transfer() default true;

	/**
	 * 排序升序或降序,默认升序
	 * 
	 * @return 排序类型
	 */
	SortType sortType() default SortType.ASC;
}