package dream.flying.flower.common;

import java.util.Objects;

import dream.flying.flower.ConstString;

/**
 * 配置枚举使用,当枚举有字段有多个可能值时,根据具体的值进行数据转换
 * 
 * @author ParadiseWY
 * @date 2020-11-23 15:46:18
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface PropConverter {

	/**
	 * 根据指定参数得到枚举中指定的值,枚举无法通过字节码实例化
	 * 
	 * @param args 枚举参数,通过接口.getEnumConstants()方法获得
	 * @param arg 需要进行判断的值
	 * @return 指定的值
	 */
	public static Object getMember(PropConverter[] args, Object arg) {
		if (Objects.isNull(args) || Objects.isNull(arg)) {
			return ConstString.UNKNOWN;
		}
		for (PropConverter converter : args) {
			if (Objects.equals(converter.toString(), arg.toString())) {
				return converter.getValue();
			}
		}
		return ConstString.UNKNOWN;
	}

	/**
	 * 最终返回的值
	 * 
	 * @return 返回的值
	 */
	Object getValue();
}