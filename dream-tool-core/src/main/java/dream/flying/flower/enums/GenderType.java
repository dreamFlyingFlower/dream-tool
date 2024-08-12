package dream.flying.flower.enums;

import java.util.stream.Stream;

import dream.flying.flower.common.PropConverter;

/**
 * 两种方式重写getValue():直接在枚举属性中实现抽象类;在枚举类中统一实现抽象类
 * 
 * @author 飞花梦影
 * @date 2020-11-23 15:48:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum GenderType implements PropConverter {

	UNKNOWN("未知") {

		@Override
		public Object getValue() {
			return "未知";
		}
	},

	MALE("男") {

		@Override
		public Object getValue() {
			return "男";
		}
	},

	FEMALE("女") {

		@Override
		public Object getValue() {
			return "女";
		}
	};

	private String msg;

	private GenderType(String msg) {
		this.msg = msg;
	}

	@Override
	public Object getValue() {
		return this.toString();
	}

	@Override
	public String toString() {
		return this.msg;
	}

	public static GenderType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static GenderType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}
}