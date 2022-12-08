package com.wy.core.enums;

import java.util.stream.Stream;

import com.wy.common.CodeMsg;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用是否状态 枚举
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CommonBooleanStatus implements CodeMsg<String> {

	COMMON_BOOLEAN_STATUS_1("1", "是"),
	COMMON_BOOLEAN_STATUS_2("2", "否");

	private String code;

	private String msg;

	public static CommonBooleanStatus getByCode(String code) {
		return Stream.of(CommonBooleanStatus.values()).filter(t -> t.code.equals(code)).findFirst().orElse(null);
	}
}