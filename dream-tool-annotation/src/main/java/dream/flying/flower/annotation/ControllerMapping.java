package dream.flying.flower.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dream.flying.flower.http.enums.HttpMethod;

/**
 * FIXME
 * 自动给Spring的Controller加上RestController,Controller,RequestMapping,PostMapping,GetMapping等
 *
 * @author 飞花梦影
 * @date 2022-01-04 09:13:10
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ControllerMapping {

	/**
	 * RequestMapping的value值,默认为类名去掉Controller之后首字母小写
	 * 
	 * @return RequestMapping#value()
	 */
	String value() default "";

	/**
	 * RestController或Controller实例名,默认不写
	 * 
	 * @return Bean实例名
	 */
	String beanName() default "";

	/**
	 * 默认添加RestController
	 * 
	 * @return 添加RestContoller
	 */
	boolean rest() default true;

	/**
	 * 指定前缀的新增方法将使用PostMapping方式调用
	 * 
	 * @return 新增方法前缀
	 */
	String[] create() default { "add", "create", "save", "insert" };

	/**
	 * 新增方法的默认使用方式
	 * 
	 * @return PostMapping
	 */
	HttpMethod createMethod() default HttpMethod.POST;

	/**
	 * 指定前缀的删除方法将使用DeleteMapping方式调用
	 * 
	 * @return 新增方法前缀
	 */
	String[] delete() default { "delete", "remove" };

	/**
	 * 删除方法的默认使用方式
	 * 
	 * @return DeleteMapping
	 */
	HttpMethod deleteMethod() default HttpMethod.DELETE;

	/**
	 * 指定前缀的修改方法将使用PostMapping方式调用
	 * 
	 * @return 新增方法前缀
	 */
	String[] update() default { "update", "edit", "modify" };

	/**
	 * 修改方法的默认使用方式
	 * 
	 * @return PostMapping
	 */
	HttpMethod updateMethod() default HttpMethod.POST;

	/**
	 * 指定前缀的查询方法将使用GetMapping方式调用
	 * 
	 * @return 新增方法前缀
	 */
	String[] get() default { "get", "select", "query" };

	/**
	 * 查询方法的默认使用方式
	 * 
	 * @return GetMapping
	 */
	HttpMethod getMethod() default HttpMethod.GET;

	/**
	 * 除增删改查之外的方法调用使用post方式
	 * 
	 * @return 增删改查之外的方法调用方式
	 */
	HttpMethod otherMethod() default HttpMethod.POST;
}