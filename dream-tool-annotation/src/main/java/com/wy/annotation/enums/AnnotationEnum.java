package com.wy.annotation.enums;

import java.util.List;

/**
 * 其他注解包名和简单类名
 *
 * @author 飞花梦影
 * @date 2021-12-29 16:45:01
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface AnnotationEnum {

	/**
	 * 类所属包名
	 * 
	 * @return 类所属包名
	 */
	String packageName();

	/**
	 * 类名
	 * 
	 * @return 类名
	 */
	String simpleName();

	/**
	 * 获得枚举中所有值
	 * 
	 * @return
	 */
	List<? extends AnnotationEnum> value();
}