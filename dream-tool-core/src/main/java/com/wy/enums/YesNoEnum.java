package com.wy.enums;

import java.util.List;
import java.util.Map;

import com.wy.common.PropConverter;
import com.wy.common.StatusMsg;
import com.wy.util.EnumStatusMsgTool;

/**
 * 是否枚举
 *
 * @author 飞花梦影
 * @date 2022-04-28 09:16:03
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum YesNoEnum implements StatusMsg, PropConverter {

	NO("否"),
	YES("是");

	private String msg;

	private YesNoEnum(String msg) {
		this.msg = msg;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Integer getCode() {
		return this.ordinal();
	}

	@Override
	public Object getValue() {
		return this.name();
	}

	public static List<Map<String, Object>> toList() {
		return EnumStatusMsgTool.toListMap(YesNoEnum.class);
	}
}