package com.wy.core.enums;

import java.util.stream.Stream;

import com.wy.common.CodeMsg;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ${comment} 枚举
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ${className} implements CodeMsg<String> {


	private String code;

	private String msg;

	public static ${className} getByCode(String code) {
		return Stream.of(${className}.values()).filter(t -> t.code.equals(code)).findFirst().orElse(null);
	}
}