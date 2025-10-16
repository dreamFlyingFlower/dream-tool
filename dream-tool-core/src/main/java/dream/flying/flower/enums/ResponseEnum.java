package dream.flying.flower.enums;

import dream.flying.flower.common.NumberMsg;
import dream.flying.flower.common.PropConverter;

/**
 * 响应枚举
 *
 * @author 飞花梦影
 * @date 2022-04-26 09:45:05
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum ResponseEnum implements NumberMsg, PropConverter {

	SUCCESS(1, "成功"),
	FAIL(0, "失败"),
	UNKNOWN(-1, "未知");

	private Integer code;

	private String msg;

	private ResponseEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public Integer getValue() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}