package com.wy.enums;

import com.wy.common.PropConverter;
import com.wy.common.StatusMsg;

/**
 * 响应枚举
 *
 * @author 飞花梦影
 * @date 2022-04-26 09:45:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum ResponseEnum implements StatusMsg, PropConverter {

	SUCCESS(1, "成功"),
	FAIL(0, "失败"),
	UNKNOWN(-1, "未知");

	private Integer code;

	private String msg;

	private ResponseEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Object getValue() {
		return this.code;
	}
}