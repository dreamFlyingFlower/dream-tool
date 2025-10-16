package dream.flying.flower.enums;

import java.util.stream.Stream;

import dream.flying.flower.common.NumberMsg;
import dream.flying.flower.common.PropConverter;

/**
 * 任务状态
 *
 * @author 飞花梦影
 * @date 2022-11-27 12:03:45
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public enum TaskStatus implements NumberMsg, PropConverter {

	PREPAER("准备中"),
	RUNNING("运行中"),
	SUSPEND("挂起"),
	COMPLETED("已完成"),
	FAILED("失败"),
	ABANDONED("放弃");

	private String msg;

	private TaskStatus(String msg) {
		this.msg = msg;
	}

	@Override
	public Integer getValue() {
		return this.ordinal() + 1;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public static TaskStatus get(int value) {
		return Stream.of(values()).filter(t -> (t.ordinal() + 1) == value).findFirst().orElse(null);
	}

	public static TaskStatus get(String value) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}