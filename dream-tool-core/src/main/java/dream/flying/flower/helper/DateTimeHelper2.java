package dream.flying.flower.helper;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dream.flying.flower.lang.StrHelper;

/**
 * 时间工具类，统一封装 Date / LocalDateTime / LocalDate 转换、区间、加减、比较、时间戳、格式化解析
 */
public class DateTimeHelper2 {

	/**
	 * 默认业务时区：东八区
	 */
	public static final ZoneId DEFAULT_BUSINESS_ZONE = ZoneId.systemDefault();

	// ===================== 时间格式常量 =====================
	public static final DateTimeFormatter FORMAT_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static final DateTimeFormatter FORMAT_YYYY_MM_DD_HH_MM_SS =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public static final DateTimeFormatter FORMAT_YYYY_MM_DD_HH_MM_SS_SSS =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public static final DateTimeFormatter FORMAT_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

	public static final DateTimeFormatter FORMAT_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public static final DateTimeFormatter FORMAT_HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");

	// ===================== 转换基础方法 =====================

	/**
	 * LocalDate -> LocalDateTime 23:59:59.999999999
	 */
	public static LocalDateTime endOfDay(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return LocalDateTime.of(localDate, LocalTime.MAX);
	}

	/**
	 * 获取当月几号
	 *
	 * @return 日期
	 */
	public static int getDay() {
		return LocalDate.now().getDayOfMonth();
	}

	/**
	 * 获取时间是当月几号
	 *
	 * @param localDateTime 时间
	 * @return 日期
	 */
	public static int getDay(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getDayOfMonth();
	}

	/**
	 * 获取今天起始 Date
	 */
	public static Date getDayBegin() {
		return toDate(getDayBeginLocal(), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取指定Date当天起始 Date
	 */
	public static Date getDayBegin(Date date) {
		return toDate(getDayBeginLocal(date), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取今天起始时间（00:00:00）LocalDateTime
	 */
	public static LocalDateTime getDayBeginLocal() {
		return startOfDay(LocalDate.now());
	}

	/**
	 * 获取指定Date当天起始时间 LocalDateTime
	 */
	public static LocalDateTime getDayBeginLocal(Date date) {
		return startOfDay(toLocal(date, DEFAULT_BUSINESS_ZONE).toLocalDate());
	}

	// ===================== 当天、本周、本月起止 =====================

	/**
	 * 获取指定LocalDateTime当天起始时间 LocalDateTime
	 */
	public static LocalDateTime getDayBeginLocal(LocalDateTime localDateTime) {
		return startOfDay(localDateTime.toLocalDate());
	}

	/**
	 * 获取今天结束 Date
	 */
	public static Date getDayEnd() {
		return toDate(getDayEndLocal(), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取指定Date当天结束 Date
	 */
	public static Date getDayEnd(Date date) {
		return toDate(getDayEndLocal(date), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取今天结束时间（23:59:59.999999999）LocalDateTime
	 */
	public static LocalDateTime getDayEndLocal() {
		return endOfDay(LocalDate.now());
	}

	/**
	 * 获取指定Date当天结束 LocalDateTime
	 */
	public static LocalDateTime getDayEndLocal(Date date) {
		return endOfDay(toLocal(date, DEFAULT_BUSINESS_ZONE).toLocalDate());
	}

	/**
	 * 获取指定LocalDateTime当天结束 LocalDateTime
	 */
	public static LocalDateTime getDayEndLocal(LocalDateTime localDateTime) {
		return endOfDay(localDateTime.toLocalDate());
	}

	/**
	 * 获取小时 0~23
	 *
	 * @param localDateTime 时间
	 * @return 小时
	 */
	public static int getHour(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getHour();
	}

	/**
	 * 获取分钟 0~59
	 *
	 * @param localDateTime 时间
	 * @return 分钟
	 */
	public static int getMinute(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getMinute();
	}

	/**
	 * 获取当前月 1~12
	 *
	 * @return 月份
	 */
	public static int getMonth() {
		return LocalDate.now().getMonthValue();
	}

	/**
	 * 获取时间的月 1~12
	 *
	 * @param localDateTime 时间
	 * @return 月份
	 */
	public static int getMonth(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getMonthValue();
	}

	/**
	 * 获取本月起始 Date
	 */
	public static Date getMonthBegin() {
		return toDate(getMonthBeginLocal(), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取本月起始 LocalDateTime
	 */
	public static LocalDateTime getMonthBeginLocal() {
		return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
	}

	/**
	 * 获取指定Date所在月起始 LocalDateTime
	 */
	public static LocalDateTime getMonthBeginLocal(Date date) {
		return getMonthBeginLocal(toLocal(date, DEFAULT_BUSINESS_ZONE));
	}

	/**
	 * 获取指定LocalDateTime所在月起始 LocalDateTime
	 */
	public static LocalDateTime getMonthBeginLocal(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
	}

	/**
	 * 获取指定年月的当月总天数
	 *
	 * @param year 年
	 * @param month 月
	 * @return 当月天数
	 */
	public static int getMonthDays(int year, int month) {
		return YearMonth.of(year, month).lengthOfMonth();
	}

	/**
	 * 获取本月结束 Date
	 */
	public static Date getMonthEnd() {
		return toDate(getMonthEndLocal(), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取本月结束 LocalDateTime
	 */
	public static LocalDateTime getMonthEndLocal() {
		return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
	}

	/**
	 * 获取指定Date所在月结束 LocalDateTime
	 */
	public static LocalDateTime getMonthEndLocal(Date date) {
		return getMonthEndLocal(toLocal(date, DEFAULT_BUSINESS_ZONE));
	}

	/**
	 * 获取指定LocalDateTime所在月结束 LocalDateTime
	 */
	public static LocalDateTime getMonthEndLocal(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
	}

	/**
	 * 获取秒 0~59
	 *
	 * @param localDateTime 时间
	 * @return 秒
	 */
	public static int getSecond(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getSecond();
	}

	/**
	 * 获取星期，周一=1，周日=7
	 *
	 * @return 星期数字
	 */
	public static int getWeek() {
		return LocalDate.now().getDayOfWeek().getValue();
	}

	/**
	 * 获取时间对应的星期，周一=1，周日=7
	 *
	 * @param localDateTime 时间
	 * @return 星期数字
	 */
	public static int getWeek(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getDayOfWeek().getValue();
	}

	// ===================== 年份起止 =====================

	/**
	 * 获取本周起始（周一 00:00:00）LocalDateTime
	 */
	public static LocalDateTime getWeekBeginLocal() {
		return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
	}

	/**
	 * 获取指定时间所在周起始（周一）LocalDateTime
	 */
	public static LocalDateTime getWeekBeginLocal(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
	}

	/**
	 * 获取本周结束（周日 23:59:59.999999999）LocalDateTime
	 */
	public static LocalDateTime getWeekEndLocal() {
		return LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
	}

	/**
	 * 获取指定时间所在周结束（周日）LocalDateTime
	 */
	public static LocalDateTime getWeekEndLocal(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
	}

	/**
	 * 获取当前年
	 *
	 * @return 年
	 */
	public static int getYear() {
		return LocalDate.now().getYear();
	}

	/**
	 * 获取时间的年
	 *
	 * @param localDateTime 时间
	 * @return 年
	 */
	public static int getYear(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.getYear();
	}

	/**
	 * 获取当年起始 LocalDateTime
	 */
	public static LocalDateTime getYearBeginLocal() {
		return LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()), LocalTime.MIN);
	}

	/**
	 * 获取指定Date所在年份起始时间，返回LocalDateTime
	 *
	 * @param date Date对象
	 * @return LocalDateTime
	 */
	public static LocalDateTime getYearBeginLocal(Date date) {
		return getYearBeginLocal(toLocal(date, DEFAULT_BUSINESS_ZONE));
	}

	/**
	 * 获取指定LocalDateTime所在年份起始时间（当年1月1日 00:00:00），返回LocalDateTime
	 *
	 * @param localDateTime 时间
	 * @return LocalDateTime
	 */
	public static LocalDateTime getYearBeginLocal(LocalDateTime localDateTime) {
		return LocalDateTime.of(localDateTime.with(TemporalAdjusters.firstDayOfYear()).toLocalDate(), LocalTime.MIN);
	}

	// ===================== 时间加减 =====================

	/**
	 * 获取当年结束时间（12月31日 23:59:59.999999999），返回Date
	 *
	 * @return Date
	 */
	public static Date getYearEnd() {
		return toDate(getYearEndLocal(), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取指定Date所在年份结束时间，返回Date
	 *
	 * @param date Date对象
	 * @return Date
	 */
	public static Date getYearEnd(Date date) {
		return toDate(getYearEndLocal(toLocal(date, DEFAULT_BUSINESS_ZONE)), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取指定LocalDateTime所在年份结束时间，返回Date
	 *
	 * @param localDateTime 时间
	 * @return Date
	 */
	public static Date getYearEnd(LocalDateTime localDateTime) {
		return toDate(getYearEndLocal(localDateTime), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * 获取当年结束时间，返回LocalDateTime
	 *
	 * @return LocalDateTime
	 */
	public static LocalDateTime getYearEndLocal() {
		return LocalDateTime.of(LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).toLocalDate(),
				LocalTime.MAX);
	}

	/**
	 * 获取指定Date所在年份结束时间，返回LocalDateTime
	 *
	 * @param date Date对象
	 * @return LocalDateTime
	 */
	public static LocalDateTime getYearEndLocal(Date date) {
		return getYearEndLocal(toLocal(date, DEFAULT_BUSINESS_ZONE));
	}

	/**
	 * 获取指定LocalDateTime所在年份结束时间（当年12月31日 23:59:59.999999999），返回LocalDateTime
	 *
	 * @param localDateTime 时间
	 * @return LocalDateTime
	 */
	public static LocalDateTime getYearEndLocal(LocalDateTime localDateTime) {
		return LocalDateTime.of(localDateTime.with(TemporalAdjusters.lastDayOfYear()).toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 判断time1 是否在 time2 之后
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return true time1 > time2
	 */
	public static boolean isAfter(LocalDateTime time1, LocalDateTime time2) {
		if (Objects.isNull(time1) || Objects.isNull(time2)) {
			return false;
		}
		return time1.isAfter(time2);
	}

	/**
	 * 判断time1 是否在 time2 之前
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return true time1 < time2
	 */
	public static boolean isBefore(LocalDateTime time1, LocalDateTime time2) {
		if (Objects.isNull(time1) || Objects.isNull(time2)) {
			return false;
		}
		return time1.isBefore(time2);
	}

	/**
	 * 判断目标时间是否在 [start, end] 闭区间内
	 *
	 * @param target 待判断时间
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return true 在区间内
	 */
	public static boolean isBetween(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
		if (Objects.isNull(target) || Objects.isNull(start) || Objects.isNull(end)) {
			return false;
		}
		return (!target.isBefore(start)) && (!target.isAfter(end));
	}

	/**
	 * 判断两个时间是否相等
	 *
	 * @param time1 时间1
	 * @param time2 时间2
	 * @return true 相等
	 */
	public static boolean isEqual(LocalDateTime time1, LocalDateTime time2) {
		return Objects.equals(time1, time2);
	}

	/**
	 * 判断是否为闰年
	 *
	 * @param year 年份
	 * @return true 闰年
	 */
	public static boolean isLeapYear(int year) {
		return LocalDate.of(year, 1, 1).isLeapYear();
	}

	/**
	 * 判断两个Date是否同一天（东八区）
	 *
	 * @param d1 Date1
	 * @param d2 Date2
	 * @return true 同一天
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		if (Objects.isNull(d1) || Objects.isNull(d2)) {
			return false;
		}
		LocalDateTime t1 = toLocal(d1, DEFAULT_BUSINESS_ZONE);
		LocalDateTime t2 = toLocal(d2, DEFAULT_BUSINESS_ZONE);
		return t1.toLocalDate().isEqual(t2.toLocalDate());
	}

	/**
	 * 判断是否为同一天（只比较年月日）
	 *
	 * @param t1 时间1
	 * @param t2 时间2
	 * @return true 同一天
	 */
	public static boolean isSameDay(LocalDateTime t1, LocalDateTime t2) {
		if (Objects.isNull(t1) || Objects.isNull(t2)) {
			return false;
		}
		return t1.toLocalDate().isEqual(t2.toLocalDate());
	}

	/**
	 * 判断今天是否是目标日期（月日，忽略年份，生日、纪念日场景）
	 *
	 * @param monthDay 月日
	 * @return true 匹配
	 */
	public static boolean isToday(MonthDay monthDay) {
		return MonthDay.now().equals(monthDay);
	}

	/**
	 * 时间减少指定数量单位
	 *
	 * @param temporal 源时间对象 LocalDateTime/LocalDate等
	 * @param amount 减少数量
	 * @param unit 时间单位
	 * @param <T> 时间泛型
	 * @return 计算后的时间
	 */
	public static <T extends Temporal> T minus(T temporal, long amount, TemporalUnit unit) {
		Objects.requireNonNull(temporal);
		Objects.requireNonNull(unit);
		return (T) temporal.minus(amount, unit);
	}

	/**
	 * Date减少天数
	 *
	 * @param date 源Date
	 * @param days 减少天数
	 * @return 计算后的Date
	 */
	public static Date minusDays(Date date, long days) {
		Objects.requireNonNull(date);
		LocalDateTime local = toLocal(date, DEFAULT_BUSINESS_ZONE);
		return toDate(local.minusDays(days), DEFAULT_BUSINESS_ZONE);
	}

	// ===================== 时间比较、判断 =====================

	/**
	 * LocalDateTime减少天数
	 *
	 * @param localDateTime 源时间
	 * @param days 减少天数
	 * @return 计算后的时间
	 */
	public static LocalDateTime minusDays(LocalDateTime localDateTime, long days) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.minusDays(days);
	}

	/**
	 * LocalDateTime减少小时
	 *
	 * @param localDateTime 源时间
	 * @param hours 减少小时
	 * @return 计算后的时间
	 */
	public static LocalDateTime minusHours(LocalDateTime localDateTime, long hours) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.minusHours(hours);
	}

	/**
	 * LocalDateTime减少分钟
	 *
	 * @param localDateTime 源时间
	 * @param minutes 减少分钟
	 * @return 计算后的时间
	 */
	public static LocalDateTime minusMinutes(LocalDateTime localDateTime, long minutes) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.minusMinutes(minutes);
	}

	/**
	 * LocalDateTime减少月
	 *
	 * @param localDateTime 源时间
	 * @param months 减少月
	 * @return 计算后的时间
	 */
	public static LocalDateTime minusMonths(LocalDateTime localDateTime, long months) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.minusMonths(months);
	}

	/**
	 * LocalDateTime减少秒
	 *
	 * @param localDateTime 源时间
	 * @param seconds 减少秒
	 * @return 计算后的时间
	 */
	public static LocalDateTime minusSeconds(LocalDateTime localDateTime, long seconds) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.minusSeconds(seconds);
	}

	/**
	 * LocalDateTime减少年
	 *
	 * @param localDateTime 源时间
	 * @param years 减少年
	 * @return 计算后的时间
	 */
	public static LocalDateTime minusYears(LocalDateTime localDateTime, long years) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.minusYears(years);
	}

	public static Date parseDate(String text, DateTimeFormatter formatter) {
		LocalDateTime dt = parseLocalDateTime(text, formatter);
		return toDate(dt);
	}

	// ===================== 字符串解析 =====================
	public static LocalDate parseLocalDate(String text, DateTimeFormatter formatter) {
		if (StrHelper.isBlank(text)) {
			return null;
		}
		return LocalDate.parse(text, formatter);
	}

	// ===================== 时间差计算 =====================

	public static LocalDateTime parseLocalDateTime(String text, DateTimeFormatter formatter) {
		if (StrHelper.isBlank(text)) {
			return null;
		}
		return LocalDateTime.parse(text, formatter);
	}

	public static LocalDate parseYmd(String text) {
		return parseLocalDate(text, FORMAT_YYYY_MM_DD);
	}

	public static LocalDateTime parseYmdHms(String text) {
		return parseLocalDateTime(text, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取两个日期之间的Period(年、月、日)，日历差值，不是物理时长
	 *
	 * @param start 起始日期
	 * @param end 结束日期
	 * @return Period
	 */
	public static Period periodBetween(LocalDate start, LocalDate end) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(end);
		return Period.between(start, end);
	}

	/**
	 * 时间增加指定数量单位
	 *
	 * @param temporal 源时间对象 LocalDateTime/LocalDate等
	 * @param amount 增加数量，负数代表减少
	 * @param unit 时间单位 ChronoUnit.DAYS、ChronoUnit.MONTHS等
	 * @param <T> 时间泛型
	 * @return 计算后的时间
	 */
	public static <T extends Temporal> T plus(T temporal, long amount, TemporalUnit unit) {
		Objects.requireNonNull(temporal);
		Objects.requireNonNull(unit);
		return (T) temporal.plus(amount, unit);
	}

	/**
	 * Date增加天数
	 *
	 * @param date 源Date
	 * @param days 增加天数，负数往前推
	 * @return 计算后的Date
	 */
	public static Date plusDays(Date date, long days) {
		Objects.requireNonNull(date);
		LocalDateTime local = toLocal(date, DEFAULT_BUSINESS_ZONE);
		return toDate(local.plusDays(days), DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * LocalDateTime增加天数
	 *
	 * @param localDateTime 源时间
	 * @param days 增加天数，负数为往前推
	 * @return 计算后的时间
	 */
	public static LocalDateTime plusDays(LocalDateTime localDateTime, long days) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.plusDays(days);
	}

	// ===================== 快速获取时间分量 =====================

	/**
	 * LocalDateTime增加小时
	 *
	 * @param localDateTime 源时间
	 * @param hours 增加小时，负数为往前推
	 * @return 计算后的时间
	 */
	public static LocalDateTime plusHours(LocalDateTime localDateTime, long hours) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.plusHours(hours);
	}

	/**
	 * LocalDateTime增加分钟
	 *
	 * @param localDateTime 源时间
	 * @param minutes 增加分钟，负数为往前推
	 * @return 计算后的时间
	 */
	public static LocalDateTime plusMinutes(LocalDateTime localDateTime, long minutes) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.plusMinutes(minutes);
	}

	/**
	 * LocalDateTime增加月
	 *
	 * @param localDateTime 源时间
	 * @param months 增加月，负数为往前推
	 * @return 计算后的时间
	 */
	public static LocalDateTime plusMonths(LocalDateTime localDateTime, long months) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.plusMonths(months);
	}

	/**
	 * LocalDateTime增加秒
	 *
	 * @param localDateTime 源时间
	 * @param seconds 增加秒，负数为往前推
	 * @return 计算后的时间
	 */
	public static LocalDateTime plusSeconds(LocalDateTime localDateTime, long seconds) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.plusSeconds(seconds);
	}

	/**
	 * LocalDateTime增加年
	 *
	 * @param localDateTime 源时间
	 * @param years 增加年，负数为往前推
	 * @return 计算后的时间
	 */
	public static LocalDateTime plusYears(LocalDateTime localDateTime, long years) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.plusYears(years);
	}

	/**
	 * 休眠，包装TimeUnit.sleep，捕获InterruptedException并复位中断标记
	 *
	 * @param time 时长
	 * @param unit 单位
	 */
	public static void sleep(long time, TimeUnit unit) {
		try {
			unit.sleep(time);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * LocalDate -> LocalDateTime 00:00:00
	 */
	public static LocalDateTime startOfDay(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return LocalDateTime.of(localDate, LocalTime.MIN);
	}

	/**
	 * LocalDateTime -> Date（默认东八区）
	 */
	public static Date toDate(LocalDateTime localDateTime) {
		return toDate(localDateTime, DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * LocalDateTime -> Date 指定时区
	 */
	public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
		if (localDateTime == null) {
			return null;
		}
		return Date.from(localDateTime.atZone(zoneId).toInstant());
	}

	/**
	 * Date -> LocalDateTime（默认东八区）
	 */
	public static LocalDateTime toLocal(Date date) {
		return toLocal(date, DEFAULT_BUSINESS_ZONE);
	}

	/**
	 * Date -> LocalDateTime 指定时区
	 */
	public static LocalDateTime toLocal(Date date, ZoneId zoneId) {
		if (date == null) {
			return null;
		}
		return date.toInstant().atZone(zoneId).toLocalDateTime();
	}

	// ===================== 其他辅助 =====================

	// ===================== 时间戳转换 =====================
	/**
	 * 毫秒时间戳 -> LocalDateTime（东八区）
	 */
	public static LocalDateTime toLocal(long milli) {
		return Instant.ofEpochMilli(milli).atZone(DEFAULT_BUSINESS_ZONE).toLocalDateTime();
	}

	/**
	 * 秒时间戳 -> LocalDateTime（东八区）
	 */
	public static LocalDateTime toLocalBySecond(long second) {
		return Instant.ofEpochSecond(second).atZone(DEFAULT_BUSINESS_ZONE).toLocalDateTime();
	}

	/**
	 * LocalDateTime -> 毫秒时间戳
	 */
	public static long toMilli(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.atZone(DEFAULT_BUSINESS_ZONE).toInstant().toEpochMilli();
	}

	/**
	 * LocalDateTime -> 秒时间戳
	 */
	public static long toSecond(LocalDateTime localDateTime) {
		Objects.requireNonNull(localDateTime);
		return localDateTime.atZone(DEFAULT_BUSINESS_ZONE).toInstant().getEpochSecond();
	}

}