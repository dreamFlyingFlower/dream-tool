package com.wy.function;

import java.util.function.Function;

/**
 * 处理传入的参数,同时返回同样类型的参数,{@link Function}的简化版
 * 
 * @author 飞花梦影
 * @date 2021-03-10 23:28:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@FunctionalInterface
public interface FunctionHandler<T> {

	T handler(T t);
}