package com.wy.common;

/**
 * 自定义各种状态码以及状态信息
 * 
 * @author 飞花梦影
 * @date 2020-02-20 15:14:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface StatusMsg {

	/**
	 * 状态码,默认为成功状态码1
	 * 
	 * @return 数字状态码
	 */
	default int getCode() {
		return 1;
	}

	/**
	 * 状态信息,默认为响应成功提示信息
	 * 
	 * @return 状态信息
	 */
	default String getMsg() {
		return Internation.getStr("msg_success");
	}

	/**
	 * 格式化状态信息,{@link String#format(String, Object...)}
	 * 
	 * @param format 格式化字符串
	 * @param args 格式化参数
	 * @return 状态信息
	 */
	default String getMsg(String format, Object... args) {
		return String.format(format, args);
	}
}