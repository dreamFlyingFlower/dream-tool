package dream.flying.flower.enums;

import java.util.List;
import java.util.Map;

import dream.flying.flower.common.PropConverter;
import dream.flying.flower.common.StatusMsg;
import dream.flying.flower.helper.EnumStatusMsgHelper;

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

	public static List<Map<String, Object>> toList() {
		return EnumStatusMsgHelper.toListMap(YesNoEnum.class);
	}

	public static boolean isYes(String msg) {
		return YES.name().equalsIgnoreCase(msg);
	}

	@Override
	public Object getValue() {
		return this.msg;
	}
}