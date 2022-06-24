package com.wy.limit;

import com.wy.limit.annotation.LimitAccess;

/**
 * 限制访问自定义处理接口
 * 
 * @author 飞花梦影
 * @date 2021-02-16 11:30:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@FunctionalInterface
public interface LimitAccessHandler {

	boolean handler(LimitAccess limitAccess);
}