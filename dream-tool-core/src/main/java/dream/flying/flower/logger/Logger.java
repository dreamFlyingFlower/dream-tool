package dream.flying.flower.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dream.flying.flower.common.StatusMsg;

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
	 */
	String value() default "";

	/**
	 * 默认功能描述,当logOtherType不等于StatusMsg.class时,使用logOtherType方法的值
	 * 
	 * @return 默认功能描述
	 */
	BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 扩展功能描述,只有当该字节码不等于StatusMsg.class时才生效,此时忽略logType,实现类必须有无参构造
	 * 
	 * @return 自定义功能描述
	 */
	Class<? extends StatusMsg> logOtherType() default StatusMsg.class;

	/**
	 * 操作人类别
	 */
	OperatorType operatorType() default OperatorType.MANAGE;

	/**
	 * 扩展操作人类别,只有当该字节码不等于StatusMsg.class时才生效,此时忽略operatorType,实现类必须有无参构造
	 */
	Class<? extends StatusMsg> operatorOtherType() default StatusMsg.class;

	/**
	 * 是否保存请求的参数
	 */
	boolean isSaveRequestParams() default true;

	/**
	 * 是否保存响应的参数
	 */
	boolean isSaveResponseResult() default true;
}