package dream.flying.flower.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dream.flying.flower.db.enums.QueryType;

/**
 * 查询字段注解
 *
 * @author 飞花梦影
 * @date 2024-03-29 20:10:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Inherited
@Documented
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

	/**
	 * java属性名
	 * 
	 * @return java属性名
	 */
	String javaField() default "";

	/**
	 * mapper中的数据库字段名
	 * 
	 * @return 数据库字段名
	 */
	String tableColumn() default "";

	/**
	 * 查询类型
	 * 
	 * @return 查询类型
	 */
	QueryType type() default QueryType.EQ;
}