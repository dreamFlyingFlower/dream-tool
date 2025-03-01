package dream.flying.flower.logger;

import dream.flying.flower.common.CodeMsg;
import dream.flying.flower.common.PropConverter;

/**
 * 日志类型
 * 
 * @author 飞花梦影
 * @date 2021-02-03 16:09:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum BusinessType implements CodeMsg, PropConverter {

	OTHER("其他"),
	LOGIN("登录"),
	INSERT("新增"),
	DELETE("删除"),
	UPDATE("修改"),
	SELECT("查询"),
	GRANT("授权"),
	EXPORT("导出"),
	IMPORT("导入"),
	FORCE("强退"),
	CLEAR("清空数据");

	private String msg;

	private BusinessType(String msg) {
		this.msg = msg;
	}

	@Override
	public Integer getValue() {
		return this.ordinal() + 1;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}
}