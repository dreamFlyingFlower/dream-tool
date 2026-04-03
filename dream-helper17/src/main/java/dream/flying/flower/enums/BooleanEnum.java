package dream.flying.flower.enums;

import java.util.stream.Stream;

import dream.flying.flower.ConstString;
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

	public static BooleanEnum get(Integer code) {
		return Stream.of(values()).filter(t -> t.getValue().intValue() == code).findFirst().orElse(null);
	}

	public static boolean isFalse(BooleanEnum booleanEnum) {
		return FALSE == booleanEnum;
	}

	public static boolean isFalse(int code) {
		return FALSE.ordinal() == code;
	}

	/**
	 * 判断值是否为false.null不为false,包括false,no,0,off
	 * 
	 * @param value 待判断的值
	 * @return true->是false;false->不是false
	 */
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
		valueStr = valueStr.trim().toUpperCase();
		switch (valueStr) {
		case ConstString.FALSE:
		case ConstString.NO:
		case ConstString.ZERO:
		case ConstString.OFF:
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

	/**
	 * 判断值是否为true,包括true,yes,ok,1,on
	 * 
	 * @param value 待判断的值
	 * @return true->是true;false->不是true
	 */
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
		valueStr = valueStr.trim().toUpperCase();
		switch (valueStr) {
		case ConstString.TRUE:
		case ConstString.YES:
		case ConstString.OK:
		case ConstString.ONE:
		case ConstString.ON:
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
	public Integer getValue() {
		return this.ordinal();
	}

	public static BooleanEnum get(int value) {
		return Stream.of(values()).filter(t -> (t.ordinal() + 1) == value).findFirst().orElse(null);
	}

	public static BooleanEnum get(String value) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}