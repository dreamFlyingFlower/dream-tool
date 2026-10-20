package dream.flying.flower.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 时区转换工具：UTC、Asia/Shanghai、GMT+0、GMT+8.
 * 
 * 核心原则：withZoneSameInstant = 同一个时刻，换时区视图（不会乱偏移）
 *
 * @author 飞花梦影
 * @date 2026-10-15 17:31:53
 */
public class ZoneConvertUtil {

	public static final ZoneId ZONE_UTC = ZoneOffset.UTC;

	public static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

	/**
	 * UTC LocalDateTime → 北京时间 LocalDateTime
	 */
	public static LocalDateTime utcToCn(LocalDateTime utcTime) {
		if (utcTime == null)
			return null;
		ZonedDateTime zdt = ZonedDateTime.of(utcTime, ZONE_UTC);

		return zdt.withZoneSameInstant(DEFAULT_ZONE).toLocalDateTime();
	}
}