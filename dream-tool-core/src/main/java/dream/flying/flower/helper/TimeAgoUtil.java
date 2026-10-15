package dream.flying.flower.helper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 友好相对时间格式化：刚刚、xx分钟前、xx小时前、3天2小时前
 *
 * @author 飞花梦影
 * @date 2026-10-15 17:30:58
 */
public class TimeAgoUtil {

	public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

	/**
	 * 格式化，精简模式（微博/评论常用：刚刚、5分钟前、2小时前、3天前）
	 */
	public static String formatSimple(LocalDateTime target) {
		if (target == null)
			return "";
		LocalDateTime now = LocalDateTime.now(DEFAULT_ZONE);
		Duration duration = Duration.between(target, now);
		long sec = duration.getSeconds();
		if (sec < 0) {
			return "未来时间";
		}
		if (sec < 60) {
			return "刚刚";
		}
		long min = sec / 60;
		if (min < 60) {
			return min + " 分钟前";
		}
		long hour = min / 60;
		if (hour < 24) {
			return hour + " 小时前";
		}
		long day = hour / 24;
		if (day < 7) {
			return day + " 天前";
		}
		return target.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * 精细模式：3天2小时前、1天5分钟前
	 */
	public static String formatDetail(LocalDateTime target) {
		if (target == null)
			return "";
		LocalDateTime now = LocalDateTime.now(DEFAULT_ZONE);
		Duration duration = Duration.between(target, now);
		long sec = duration.getSeconds();
		if (sec < 0) {
			return "未来时间";
		}
		if (sec < 60) {
			return "刚刚";
		}
		long totalMin = sec / 60;
		long d = totalMin / (60 * 24);
		long h = (totalMin % (60 * 24)) / 60;
		long m = totalMin % 60;

		StringBuilder sb = new StringBuilder();
		if (d > 0)
			sb.append(d).append("天");
		if (h > 0)
			sb.append(h).append("小时");
		if (m > 0 && d == 0)
			sb.append(m).append("分钟");
		sb.append("前");
		return sb.toString();
	}

	// Date重载
	public static String formatSimple(Date date) {
		if (date == null)
			return "";
		LocalDateTime dt = date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime();
		return formatSimple(dt);
	}

	public static String formatDetail(Date date) {
		if (date == null)
			return "";
		LocalDateTime dt = date.toInstant().atZone(DEFAULT_ZONE).toLocalDateTime();
		return formatDetail(dt);
	}
}