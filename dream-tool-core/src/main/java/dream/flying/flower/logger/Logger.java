package dream.flying.flower.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dream.flying.flower.common.NameMsg;

/**
 * 自定义操作日志记录注解
 * 
 * @author 飞花梦影
 * @date 2021-02-03 16:05:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logger {

	/**
	 * 日志模块
	 * 
	 * @param 日志所属模块
	 */
	String value() default "";

	/**
	 * 是否记录日志,配置其他注解或切面使用
	 * 
	 * @return 默认记录
	 */
	boolean enabled() default true;

	/**
	 * 是否将日志输出到日志文件
	 * 
	 * @return 默认输出
	 */
	boolean outputFile() default true;

	/**
	 * 是否将日志存储到数据库
	 * 
	 * @return 默认存储
	 */
	boolean storeDb() default true;

	/**
	 * 日志类型,当logOtherType不等于StatusMsg.class时,使用logOtherType方法的值
	 * 
	 * @return 日志类型
	 */
	BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 操作描述
	 */
	String description() default "";

	/**
	 * 扩展功能描述,只有当该字节码不等于StatusMsg.class时才生效,此时忽略logType,实现类必须有无参构造
	 * 
	 * @return 自定义功能描述
	 */
	Class<? extends NameMsg> logOtherType() default NameMsg.class;

	/**
	 * 操作人类别
	 */
	OperatorType operatorType() default OperatorType.MANAGE;

	/**
	 * 扩展操作人类别,只有当该字节码不等于StatusMsg.class时才生效,此时忽略operatorType,实现类必须有无参构造
	 */
	Class<? extends NameMsg> operatorOtherType() default NameMsg.class;

	/**
	 * 是否保存请求的参数
	 */
	boolean saveRequest() default true;

	/**
	 * 是否保存响应的参数
	 */
	boolean saveResponse() default true;
}