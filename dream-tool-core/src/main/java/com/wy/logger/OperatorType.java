package com.wy.logger;

import com.wy.common.PropConverter;
import com.wy.common.StatusMsg;

/**
 * 操作日志类型
 * 
 * @author 飞花梦影
 * @date 2021-01-13 10:18:54
 * @git {@link https://github.com/mygodness100}
 */
public enum OperatorType implements StatusMsg, PropConverter {

	OTHER("其他"),
	ADMIN("超级管理员"),
	GENERAL("普通用户");

	private int code;

	private String msg;

	private OperatorType(String msg) {
		this.code = this.ordinal();
		this.msg = msg;
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}

	@Override
	public Object getValue() {
		return this.msg;
	}
}