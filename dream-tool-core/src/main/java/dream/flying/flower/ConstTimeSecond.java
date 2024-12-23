package dream.flying.flower;

/**
 * 时间单位,以秒为基准
 *
 * @author 飞花梦影
 * @date 2024-09-14 00:24:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstTimeSecond {

	/** 1秒 */
	int ONE_SECOND = 1;

	/** 30秒 */
	int HALF_MINUTE = 30;

	/** 1分钟 */
	int ONE_MINUTE = 60 * ONE_SECOND;

	/** 30分钟 */
	int HALF_HOUR = 30 * ONE_MINUTE;

	/** 1小时 */
	int ONE_HOUR = 60 * ONE_MINUTE;

	/** 12小时 */
	int HALF_DAY = 12 * ONE_HOUR;

	/** 1天 */
	int ONE_DAY = 24 * ONE_HOUR;

	/** 7天 */
	int ONE_WEEK = 7 * ONE_DAY;

	/** 14天 */
	int TWO_WEEK = 14 * ONE_DAY;

	/** 30天 */
	int ONE_MONTH = 30 * ONE_DAY;

	/** 60天 */
	int TWO_MONTH = 60 * ONE_DAY;

	/** 1季度 */
	int ONE_SEASON = 4 * 30 * ONE_DAY;

	/** 365天 */
	int ONE_YEAR = 365 * ONE_DAY;
}