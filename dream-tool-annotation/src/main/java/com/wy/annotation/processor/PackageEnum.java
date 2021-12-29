package com.wy.annotation.processor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2021-12-29 16:45:01
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface PackageEnum {

	/**
	 * 类所属包名
	 * 
	 * @return
	 */
	String getPackageName();

	/**
	 * 类名
	 * 
	 * @return
	 */
	String getClassName();
}