package com.wy.logger;

import com.wy.common.PropConverter;
import com.wy.common.StatusMsg;

/**
 * 日志类型
 * 
 * @author 飞花梦影
 * @date 2021-02-03 16:09:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum LogType implements StatusMsg, PropConverter {

	OTHER("其他"),
	INSERT("新增"),
	DELETE("删除"),
	UPDATE("修改"),
	SELECT("查询"),
	GRANT("授权"),
	EXPORT("导出"),
	IMPORT("导入"),
	CLEAR("清空数据");

	private int code;

	private String msg;

	private LogType(String msg) {
		this.code = this.ordinal();
		this.msg = msg;
	}

	private LogType(int code, String msg) {
		this.code = code;
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