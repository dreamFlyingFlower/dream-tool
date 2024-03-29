package com.dream.function;

/**
 * 除了多个对象成另外一个对的
 * 
 * @author 飞花梦影
 * @date 2021-03-12 14:18:19
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@FunctionalInterface
public interface FunctionHandlerArgs<T> {

	T born(Object... args);
}