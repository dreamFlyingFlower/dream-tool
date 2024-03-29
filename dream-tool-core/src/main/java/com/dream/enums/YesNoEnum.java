package com.dream.enums;

import java.util.List;
import java.util.Map;

import com.dream.common.PropConverter;
import com.dream.common.StatusMsg;
import com.dream.helper.EnumStatusMsgHelper;

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
		return EnumStatusMsgHelper.toListMap(YesNoEnum.class);
	}
}