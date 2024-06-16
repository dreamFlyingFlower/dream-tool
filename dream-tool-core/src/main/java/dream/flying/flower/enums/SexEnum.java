package dream.flying.flower.enums;

import dream.flying.flower.common.PropConverter;

/**
 * 两种方式重写getValue():直接在枚举属性中实现抽象类;在枚举类中统一实现抽象类
 * 
 * @author ParadiseWY
 * @date 2020-11-23 15:48:40
 * @git {@link https://github.com/mygodness100}
 */
public enum SexEnum implements PropConverter {

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
	},

	UNKNOWN("未知") {

		@Override
		public Object getValue() {
			return "中性";
		}
	};

	private String msg;

	private SexEnum(String msg) {
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
}