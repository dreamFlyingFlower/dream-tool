package com.dream.enums;

import java.text.MessageFormat;

/**
 * 日志格式化
 *
 * @author 飞花梦影
 * @date 2020-03-23 16:04:06
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum LogEnum {

	ERROR("###::{0}::###"),
	INFO("@@@::{0}::@@@");

	private String format;

	LogEnum(String format) {
		this.format = format;
	}

	private String format() {
		return format;
	}

	public String getMsg(String msg) {
		return MessageFormat.format(format(), msg);
	}
}