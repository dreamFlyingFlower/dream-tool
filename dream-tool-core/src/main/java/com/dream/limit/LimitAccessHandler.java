package com.dream.limit;

import com.dream.limit.annotation.LimitAccess;

/**
 * 限制访问自定义处理接口
 * 
 * @author 飞花梦影
 * @date 2021-02-16 11:30:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface LimitAccessHandler {

	/**
	 * 自定义限流处理
	 * 
	 * @param limitAccess 限流注解
	 * @return 是否限流.true->限流;false->不限流
	 */
	boolean handler(LimitAccess limitAccess);
}