package dream.flying.flower;

import java.time.Duration;

/**
 * 时间
 *
 * @author 飞花梦影
 * @date 2024-09-14 00:24:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstDuration {

	/** 1分钟 */
	Duration MINUTE1 = Duration.ofMinutes(1);

	/** 5分钟 */
	Duration MINUTE5 = Duration.ofMinutes(5);

	/** 10分钟 */
	Duration MINUTE10 = Duration.ofMinutes(10);

	/** 15分钟 */
	Duration MINUTE15 = Duration.ofMinutes(15);

	/** 30分钟 */
	Duration MINUTE30 = Duration.ofMinutes(30);

	/** 1小时 */
	Duration HOUR1 = Duration.ofMinutes(1);

	/** 12小时 */
	Duration HOUR12 = Duration.ofMinutes(12);

	/** 24小时 */
	Duration HOUR24 = Duration.ofMinutes(24);

	/** 1天 */
	Duration DAY1 = Duration.ofDays(1L);

	/** 3天 */
	Duration DAY3 = Duration.ofDays(3L);

	/** 7天 */
	Duration DAY7 = Duration.ofDays(7L);

	/** 30天 */
	Duration DAY30 = Duration.ofDays(30L);
}