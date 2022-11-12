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
	MANAGE("后台用户"),
	GENERAL("普通用户");

	private String msg;

	private OperatorType(String msg) {
		this.msg = msg;
	}

	@Override
	public Integer getCode() {
		return this.ordinal();
	}

	@Override
	public String getMsg() {
		return this.msg;
	}

	@Override
	public Object getValue() {
		return this.name();
	}
}