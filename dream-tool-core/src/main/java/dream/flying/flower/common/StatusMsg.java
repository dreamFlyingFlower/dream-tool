package dream.flying.flower.common;

import java.io.Serializable;

/**
 * 自定义各种状态码以及状态信息
 * 
 * @author 飞花梦影
 * @date 2020-02-20 15:14:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface StatusMsg<ID extends Serializable> {

	/**
	 * 状态码
	 * 
	 * @return 状态码
	 */
	ID getCode();

	/**
	 * 状态信息
	 * 
	 * @return 状态信息
	 */
	String getMsg();

	/**
	 * 格式化状态信息,{@link String#format(String, Object...)}
	 * 
	 * @param format 格式化字符串
	 * @param args 格式化参数
	 * @return 状态信息
	 */
	default String getMsg(String format, Object... args) {
		return String.format(format, args);
	}
}