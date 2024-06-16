package dream.flying.flower.enums;

import dream.flying.flower.common.PropConverter;
import dream.flying.flower.common.StatusMsg;

/**
 * 任务状态
 *
 * @author 飞花梦影
 * @date 2022-11-27 12:03:45
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public enum TaskStatus implements StatusMsg, PropConverter {

	STARTING(0, "开始中"),
	STARTED(1, "已开始"),
	STOPPING(2, "停止中"),
	STOPPED(3, "已停止"),
	FAILED(4, "失败"),
	COMPLETED(5, "已完成"),
	ABANDONED(6, "放弃");

	private Integer code;

	private String msg;

	private TaskStatus(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public Object getValue() {
		return this.ordinal();
	}
}