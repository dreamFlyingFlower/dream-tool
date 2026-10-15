package dream.flying.flower.helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 时区转换工具：UTC、Asia/Shanghai、GMT+0、GMT+8.
 * 
 * 核心原则：withZoneSameInstant = 同一个时刻，换时区视图（不会乱偏移）
 *
 * @author 飞花梦影
 * @date 2026-10-15 17:31:53
 */
public class ZoneConvertUtil {

	public static final ZoneId ZONE_UTC = ZoneId.of("UTC");

	public static final ZoneId ZONE_CN = ZoneId.of("Asia/Shanghai");

	/**
	 * 北京时间 LocalDateTime → UTC LocalDateTime
	 */
	public static LocalDateTime cnToUtc(LocalDateTime cnTime) {
		if (cnTime == null)
			return null;
		ZonedDateTime zdt = ZonedDateTime.of(cnTime, ZONE_CN);
		return zdt.withZoneSameInstant(ZONE_UTC).toLocalDateTime();
	}

	/**
	 * UTC LocalDateTime → 北京时间 LocalDateTime
	 */
	public static LocalDateTime utcToCn(LocalDateTime utcTime) {
		if (utcTime == null)
			return null;
		ZonedDateTime zdt = ZonedDateTime.of(utcTime, ZONE_UTC);
		return zdt.withZoneSameInstant(ZONE_CN).toLocalDateTime();
	}

	/**
	 * 任意时区A → 时区B
	 */
	public static LocalDateTime convert(LocalDateTime source, ZoneId sourceZone, ZoneId targetZone) {
		if (source == null || sourceZone == null || targetZone == null)
			return null;
		return ZonedDateTime.of(source, sourceZone).withZoneSameInstant(targetZone).toLocalDateTime();
	}

	/**
	 * Date 转 ZonedDateTime（UTC）
	 */
	public static ZonedDateTime dateToUtcZoned(Date date) {
		if (date == null)
			return null;
		return date.toInstant().atZone(ZONE_UTC);
	}

	/**
	 * ZonedDateTime 转 Date
	 */
	public static Date zonedToDate(ZonedDateTime zonedDateTime) {
		if (zonedDateTime == null)
			return null;
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * 获取当前UTC时间
	 */
	public static LocalDateTime nowUtc() {
		return LocalDateTime.now(ZONE_UTC);
	}

	/**
	 * 获取当前北京时间
	 */
	public static LocalDateTime nowCn() {
		return LocalDateTime.now(ZONE_CN);
	}
}