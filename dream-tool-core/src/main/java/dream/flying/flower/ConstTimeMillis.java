package dream.flying.flower;

/**
 * 时间单位,以毫秒为基准
 *
 * @author 飞花梦影
 * @date 2024-09-14 00:24:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstTimeMillis {

	/** 1毫秒 */
	long ONE_MILLISECOND = 1;

	/** 1秒 */
	long ONE_SECOND = 1000 * ONE_MILLISECOND;

	/** 30秒 */
	long HALF_MINUTE = 30 * ONE_SECOND;

	/** 1分钟 */
	long ONE_MINUTE = 60 * ONE_SECOND;

	/** 30分钟 */
	long HALF_HOUR = 30 * ONE_MINUTE;

	/** 1小时 */
	long ONE_HOUR = 60 * ONE_MINUTE;

	/** 12小时 */
	long HALF_DAY = 12 * ONE_HOUR;

	/** 1天 */
	long ONE_DAY = 24 * ONE_HOUR;

	/** 7天 */
	long ONE_WEEK = 7 * ONE_DAY;

	/** 14天 */
	long TWO_WEEK = 14 * ONE_DAY;

	/** 30天 */
	long ONE_MONTH = 30 * ONE_DAY;

	/** 60天 */
	long TWO_MONTH = 60 * ONE_DAY;

	/** 1季度 */
	long ONE_SEASON = 4 * 30 * ONE_DAY;

	/** 365天 */
	long ONE_YEAR = 365 * ONE_DAY;
}