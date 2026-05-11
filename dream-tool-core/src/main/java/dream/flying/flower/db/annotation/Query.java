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
	 * 数据库字段名
	 * 
	 * @return 数据库字段名
	 */
	String tableColumn() default "";

	/**
	 * 是否忽略字段,默认 false
	 * 
	 * @return true->忽略;false->不忽略
	 */
	boolean ignore() default false;

	/**
	 * 是否忽略空值,默认 true
	 *
	 * @return true-忽略空值,false-不忽略
	 */
	boolean ignoreNull() default true;

	/**
	 * 查询类型
	 * 
	 * @return 查询类型
	 */
	QueryType type() default QueryType.EQ;

	/**
	 * 表别名,默认为空则使用主表别名
	 *
	 * @return 表别名
	 */
	String alias() default "";

	/**
	 * 自定义 SQL 片段,用于复杂查询
	 *
	 * @return SQL 片段
	 */
	String sql() default "";

	/**
	 * 自定义查询查询类型,当customizeType()有值时使用,无值时使用type(),需通过 QueryHandlerRegistry 注册
	 *
	 * @return 查询类型
	 */
	String customizeType() default "EQ";
}