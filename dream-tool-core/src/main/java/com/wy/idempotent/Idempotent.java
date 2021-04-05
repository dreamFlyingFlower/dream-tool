package com.wy.idempotent;

/**
 * 判断是否幂等的方法
 * 
 * @author 飞花梦影
 * @date 2021-02-03 17:30:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface Idempotent {

	boolean isIdempotent();
}