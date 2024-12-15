package dream.flying.flower.common;

/**
 * 自定义各种状态码以及状态信息
 * 
 * @author 飞花梦影
 * @date 2020-02-20 15:14:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface CodeMsg extends StatusMsg<Integer> {

	/**
	 * 状态码
	 * 
	 * @return 数字状态码
	 */
	@Override
	default Integer getValue() {
		if (Enum.class.isAssignableFrom(this.getClass())) {
			return ((Enum<?>) this).ordinal() + 1;
		}
		return null;
	}

	/**
	 * 信息
	 * 
	 * @return 信息
	 */
	@Override
	default String getMsg() {
		if (Enum.class.isAssignableFrom(this.getClass())) {
			return ((Enum<?>) this).toString();
		}
		return null;
	}

	/**
	 * name
	 * 
	 * @return name
	 */
	default String getName() {
		if (Enum.class.isAssignableFrom(this.getClass())) {
			return ((Enum<?>) this).name();
		}
		return null;
	}
}