package dream.flying.flower.logger;

import java.util.stream.Stream;

import dream.flying.flower.common.CodeMsg;
import dream.flying.flower.common.PropConverter;
import dream.flying.flower.lang.StrHelper;

/**
 * 日志类型
 * 
 * @author 飞花梦影
 * @date 2021-02-03 16:09:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum BusinessType implements CodeMsg, PropConverter {

	OTHER("其他,other"),
	LOGIN("登录,login"),
	INSERT("新增,add,create,insert,save"),
	DELETE("删除,delete,remove,clear"),
	UPDATE("修改,update,edit,modify,change"),
	SELECT("查询,get,list,query,select"),
	GRANT("授权,grant"),
	EXPORT("导出,export"),
	IMPORT("导入,import"),
	FORCE("强退,force"),
	CLEAR("清空数据,clear");

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

	public static BusinessType getByMsg(String msg) {
		if (StrHelper.isBlank(msg)) {
			return BusinessType.OTHER;
		}

		return Stream.of(BusinessType.values())
				.filter(t -> Stream.of(t.getMsg().split(",")).filter(msg::startsWith).findFirst().isPresent())
				.findFirst()
				.orElse(OTHER);
	}
}