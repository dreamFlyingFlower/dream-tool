package dream.flying.flower.enums;

/**
 * 区间开闭枚举
 *
 * @author 飞花梦影
 * @date 2022-04-11 09:17:21
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum RegionEnum {

	/** 区间左右开放,即[x],x大于等于左边界,小于等于右边界 */
	LEFT_OPEN_RIGHT_OPEN,
	/** 区间左关右开,即(x],x大于左边界,小于等于右边界 */
	LEFT_CLOSE_RIGHT_OPEN,
	/** 区间左开右关,即[x),x大于等于左边界,小于右边界 */
	LEFT_OPEN_RIGHT_CLOSE,
	/** 区间左右关闭,即(x),x大于左边界,小于右边界 */
	LEFT_CLOSE_RIGHT_CLOSE;
}