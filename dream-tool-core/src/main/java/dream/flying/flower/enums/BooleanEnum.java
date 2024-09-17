package dream.flying.flower.enums;

import java.util.stream.Stream;

import dream.flying.flower.common.CodeMsg;
import dream.flying.flower.common.PropConverter;
import dream.flying.flower.lang.StrHelper;

/**
 * 通用是否状态 枚举
 * 
 * @author 飞花梦影
 * @date 2022-09-05 11:24:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum BooleanEnum implements CodeMsg, PropConverter {

	FALSE("否"),
	TRUE("是");

	private String msg;

	private BooleanEnum(String msg) {
		this.msg = msg;
	}

	public static BooleanEnum getByCode(Integer code) {
		return Stream.of(BooleanEnum.values()).filter(t -> t.getCode().intValue() == code).findFirst().orElse(null);
	}

	public static boolean isFalse(BooleanEnum booleanEnum) {
		return FALSE == booleanEnum;
	}

	public static boolean isFalse(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof Boolean) {
			return !(Boolean) value;
		}
		String valueStr = value.toString();
		if (StrHelper.isBlank(valueStr)) {
			return false;
		}
		valueStr = valueStr.trim().toLowerCase();
		switch (valueStr) {
		case "false":
		case "no":
		case "0":
			return true;
		default:
			return false;
		}
	}

	public static boolean isTrue(BooleanEnum booleanEnum) {
		return TRUE == booleanEnum;
	}

	public static boolean isTrue(int code) {
		return TRUE.ordinal() == code;
	}

	public static boolean isTrue(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		String valueStr = value.toString();
		if (StrHelper.isBlank(valueStr)) {
			return false;
		}
		valueStr = valueStr.trim().toLowerCase();
		switch (valueStr) {
		case "true":
		case "yes":
		case "ok":
		case "1":
			return true;
		default:
			return false;
		}
	}

	@Override
	public String getMsg() {
		return this.msg;
	}

	@Override
	public Integer getCode() {
		return this.ordinal();
	}

	@Override
	public Object getValue() {
		return this.getMsg();
	}
}