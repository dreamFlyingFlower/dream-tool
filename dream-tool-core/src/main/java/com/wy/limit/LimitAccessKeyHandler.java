package com.wy.limit;

/**
 * 限制访问自定义key生成方式接口
 *
 * @author 飞花梦影
 * @date 2023-04-17 14:55:01
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@FunctionalInterface
public interface LimitAccessKeyHandler {

	/**
	 * 生成key的方式
	 * 
	 * @return key
	 */
	Object generateKey();
}