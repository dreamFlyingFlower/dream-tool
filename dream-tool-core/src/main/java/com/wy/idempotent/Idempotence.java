package com.wy.idempotent;

import com.wy.digest.DigestHelper;

/**
 * 判断是否幂等的方法
 * 
 * @author 飞花梦影
 * @date 2021-02-03 17:30:16
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface Idempotence {

	/**
	 * 生成唯一幂等编码,默认32位UUID
	 * 
	 * @return 32位UUID
	 */
	default String generateCode() {
		return DigestHelper.uuid();
	}

	/**
	 * 检查是否存在幂等编码
	 * 
	 * @param idempotentCode 幂等编码
	 * @return 是否存在
	 */
	boolean check(String idempotentCode);

	/**
	 * 记录幂等编码
	 * 
	 * @param idempotentCode 幂等编码
	 */
	void record(String idempotentCode);

	/**
	 * 记录幂等编码
	 * 
	 * @param idempotentCode 幂等编码
	 * @param time 过期时间
	 */
	void record(String idempotentCode, Long time);

	/**
	 * 删除幂等编码
	 * 
	 * @param idempotentCode 幂等编码
	 */
	void delete(String idempotentCode);
}