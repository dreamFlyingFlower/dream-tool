package dream.flying.flower.enums;

import java.util.stream.Stream;

import dream.flying.flower.common.CodeMsg;

/**
 * 身份标识类型
 * 
 * @author 飞花梦影
 * @date 2024-07-30 21:34:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum IdCardType implements CodeMsg {

	/** 未知 */
	UNKNOWN,
	/** 身份证 */
	ID_CARD,
	/** 护照 */
	PASSPORT,
	/** 学生证 */
	STUDENT_CARD,
	/** 军人证 */
	MILITARY_CARD;

	@Override
	public String getMsg() {
		return name();
	}

	@Override
	public Integer getValue() {
		return ordinal();
	}

	public static IdCardType get(int value) {
		return Stream.of(values()).filter(t -> (t.ordinal() + 1) == value).findFirst().orElse(null);
	}

	public static IdCardType get(String value) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}