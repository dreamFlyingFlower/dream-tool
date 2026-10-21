package dream.flying.flower.helper;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dream.flying.flower.annotation.Example;
import dream.flying.flower.enums.DateEnum;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * {@link LocalDateTime}等工具类,jdk1.8的时间类工具类,和DateHelper差不多
 * 
 * <pre>
 * 1.当直接输出LocalTime.toString()时,类似XX:00:00将不会输出秒,只会输出XX:00,不要直接输出或使用toString()
 * 2.toInstant():类似方法始终都获得的是从1970到现在的时间戳,与时区无关,只有Instant.now()才和时区相关,获得的是UTC时间
 * 3.{@link Date},{@link LocalDateTime}: 无时区时间,即获取到的时间就是本地时间,不会加减时区
 * 4.{@link ZonedDateTime}: 绑定时区的时间,转换时区时间和无时区时间
 * 5.时区互转的方法,都会先转成Instant,然后再加上时区
 * </pre>
 * 
 * 
 * 时区:
 * 
 * <pre>
 * UTC: 标准时间,所有时区的基准点
 * 时区时间: 以UTC为基准的偏移时间,如Asia/Shanghai时区就是在UTC时间上+8小时,即UTC时间是4点,上海12点
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2019-03-20 16:56:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface DateTimeHelper {

	/**
	 * UTC时区
	 */
	ZoneId ZONE_UTC = ZoneOffset.UTC;

	/**
	 * 默认本地时区
	 */
	ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

	/**
	 * 默认格式化时间字符串HH:mm:ss,系统自带的{@link DateTimeFormatter#ISO_LOCAL_TIME}默认字符串带毫秒
	 */
	DateTimeFormatter DEFAULT_FORMATTER_TIME = new DateTimeFormatterBuilder().appendValue(HOUR_OF_DAY, 2)
			.appendLiteral(':')
			.appendValue(MINUTE_OF_HOUR, 2)
			.optionalStart()
			.appendLiteral(':')
			.appendValue(SECOND_OF_MINUTE, 2)
			.optionalStart()
			.parseStrict()
			.toFormatter();

	/**
	 * 默认格式化年月日时分秒字符串yyyy-MM-dd HH:mm:ss,系统自带的默认格式化字符串中间带T
	 */
	DateTimeFormatter DEFAULT_FORMATTER = new DateTimeFormatterBuilder().parseCaseInsensitive()
			.append(DateTimeFormatter.ISO_LOCAL_DATE)
			.appendLiteral(' ')
			.append(DEFAULT_FORMATTER_TIME)
			.optionalStart()
			.parseStrict()
			.toFormatter();

	/**
	 * 定时时间格式化字符串:yyyy-MM-dd HH:mm:ss,等同于DEFAULT_FORMATTER
	 */
	DateTimeFormatter DEFAULT_FORMATTER_DATETIME = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR)
			.appendLiteral("-")
			.appendValue(ChronoField.MONTH_OF_YEAR)
			.appendLiteral("-")
			.appendValue(ChronoField.DAY_OF_MONTH)
			.appendLiteral(" ")
			.appendValue(ChronoField.HOUR_OF_DAY)
			.appendLiteral(":")
			.appendValue(ChronoField.MINUTE_OF_HOUR)
			.appendLiteral(":")
			.appendValue(ChronoField.SECOND_OF_MINUTE)
			.toFormatter();

	/**
	 * 将Date转换为LocalDateTime
	 * 
	 * @param date 需要进行转换的时间
	 * @return 转换后的时间,和date时间相同
	 */
	public static LocalDateTime toLocal(Date date) {
		AssertHelper.notNull(date);
		// date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * 判断指定时间是否在2个时间之间
	 * 
	 * @param nowTime 待判断时间
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return true->是;false->否
	 */
	static Boolean betweenDate(Instant nowTime, Instant beginTime, Instant endTime) {
		return nowTime.isAfter(beginTime) && nowTime.isBefore(endTime);
	}

	/**
	 * 将Date转换成指定格式字符串
	 * 
	 * @param date 时间
	 * @param pattern 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(Date date, DateEnum pattern) {
		return format(date, DateTimeFormatter.ofPattern(pattern.getPattern()));
	}

	/**
	 * 将Date转换成指定格式字符串
	 * 
	 * @param date 时间
	 * @param dateTimeFormatter 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(Date date, DateTimeFormatter dateTimeFormatter) {
		return format(toLocal(date), dateTimeFormatter);
	}

	/**
	 * 将Date转换成指定格式字符串
	 * 
	 * @param date 时间
	 * @param pattern 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(Date date, String pattern) {
		return format(date, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将LocalDateTime转换成指定格式字符串
	 * 
	 * @param localDateTime 时间
	 * @param pattern 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(LocalDateTime localDateTime, DateEnum pattern) {
		return format(localDateTime, DateTimeFormatter.ofPattern(pattern.getPattern()));
	}

	/**
	 * 将LocalDateTime转换成指定格式字符串
	 * 
	 * @param localDateTime 时间
	 * @param dateTimeFormatter 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		return dateTimeFormatter.format(localDateTime);
	}

	/**
	 * 将LocalDateTime转换成指定格式字符串
	 * 
	 * @param localDateTime 时间
	 * @param pattern 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(LocalDateTime localDateTime, String pattern) {
		return format(localDateTime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将当前时间转换为指定格式
	 * 
	 * @param pattern 时间格式
	 * @return 时间格式字符串
	 */
	public static String format(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
	}

	/**
	 * 将当前时间转换为yyyy-MM-dd格式
	 * 
	 * @return 时间字符串
	 */
	public static String formatDate() {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
	}

	/**
	 * 将Date转换为yyyy-MM-dd格式
	 * 
	 * @param date 需要进行转换的时间
	 * @return 日期字符串
	 */
	public static String formatDate(Date date) {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(toLocal(date));
	}

	/**
	 * 将LocalDate转换为yyyy-MM-dd格式
	 * 
	 * @param localDate 需要进行转换的时间
	 * @return 日期字符串
	 */
	public static String formatDate(LocalDate localDate) {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(localDate);
	}

	/**
	 * 将LocalDateTime转换为yyyy-MM-dd格式
	 * 
	 * @param localDate 需要进行转换的时间
	 * @return 日期字符串
	 */
	public static String formatDate(LocalDateTime localDateTime) {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(localDateTime);
	}

	/**
	 * 将当前时间转换为指定年月日格式
	 * 
	 * @param pattern 日期格式
	 * @return 日期字符串
	 */
	public static String formatDate(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(LocalDate.now());
	}

	/**
	 * 获得当前时间的yyyy-MM-dd HH:mm:ss格式字符串
	 * 
	 * @return 时间字符串
	 */
	public static String formatDateTime() {
		return formatDateTime(LocalDateTime.now());
	}

	/**
	 * 将Date转换为yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param date 需要转换的时间
	 * @return 时间字符串
	 */
	public static String formatDateTime(Date date) {
		return formatDateTime(toLocal(date));
	}

	/**
	 * 将LocalDateTime转换为yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param localDateTime 需要转换的时间
	 * @return 时间字符串
	 */
	public static String formatDateTime(LocalDateTime localDateTime) {
		return format(localDateTime, DateEnum.DATETIME.getPattern());
	}

	/**
	 * 将当前时间转换为指定的年月日时分秒格式
	 * 
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String formatDateTime(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
	}

	/**
	 * 将LocalDateTime转换为HH:mm:ss格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime() {
		return DEFAULT_FORMATTER_TIME.format(LocalDateTime.now());
	}

	/**
	 * 将LocalDateTime转换为HH:mm:ss格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime(Date date) {
		return DEFAULT_FORMATTER_TIME.format(toLocal(date));
	}

	/**
	 * 将LocalDateTime转换为HH:mm:ss格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime(LocalDateTime localDateTime) {
		return DEFAULT_FORMATTER_TIME.format(localDateTime);
	}

	/**
	 * 将LocalTime转换为HH:mm:ss格式
	 * 
	 * @param localTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime(LocalTime localTime) {
		return DEFAULT_FORMATTER_TIME.format(localTime);
	}

	/**
	 * 将当前时间格式化为UTC时间,注意,与时区有关
	 * 
	 * @return 格式化后的UTC时间
	 */
	public static String formatUtcDateTime() {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
		return zonedDateTime.format(DEFAULT_FORMATTER_DATETIME);
	}

	/**
	 * 将指定时间格式化为UTC时间,注意,与时区有关
	 * 
	 * @param date 指定时间
	 * @return 格式化后的UTC时间
	 */
	public static String formatUtcDateTime(Date date) {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
		return zonedDateTime.format(DEFAULT_FORMATTER_DATETIME);
	}

	/**
	 * 将指定时间格式化为UTC时间,注意,与时区有关
	 * 
	 * @param localDateTime 指定时间
	 * @return 格式化后的UTC时间
	 */
	public static String formatUtcDateTime(LocalDateTime localDateTime) {
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.systemDefault());
		return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).format(DEFAULT_FORMATTER_DATETIME);
	}

	/**
	 * 将指定时间格式化为UTC时间,注意,与时区有关
	 * 
	 * @param date 指定时间
	 * @return 格式化后的UTC时间
	 */
	public static String formatUtcDateTime(long date) {
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(new Date(date).toInstant(), ZoneOffset.UTC);
		return zonedDateTime.format(DEFAULT_FORMATTER_DATETIME);
	}

	/**
	 * 获得当前时间所在日期的零点时间,但是toString()不会显示秒和纳秒,需要强制格式化为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 当前时间所在日期零点时间
	 */
	public static LocalDateTime getDayBegin() {
		return LocalDate.now().atStartOfDay();
	}

	/**
	 * 获得指定时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期零点时间
	 */
	public static LocalDateTime getDayBegin(Date date) {
		return toLocal(date).toLocalDate().atStartOfDay();
	}

	/**
	 * 获得指定时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在日期零点时间
	 */
	public static LocalDateTime getDayBegin(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().atStartOfDay();
	}

	/**
	 * 获得当前时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr() {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
		return localDateTime.format(DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(Date date) {
		LocalDateTime date2Local = toLocal(date);
		LocalDateTime localDateTime = LocalDateTime.of(date2Local.toLocalDate(), LocalTime.MIN);
		return DEFAULT_FORMATTER.format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的零点时间指定格式,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param date 指定时间
	 * @param dateFormat 时间格式
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(Date date, DateEnum dateFormat) {
		LocalDateTime localDateTime = LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MIN);
		return dateFormat == null
				? DEFAULT_FORMATTER.format(localDateTime)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的零点时间指定格式,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param date 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(Date date, DateTimeFormatter dateTimeFormatter) {
		LocalDateTime localDateTime = LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MIN);
		return dateTimeFormatter == null
				? DEFAULT_FORMATTER.format(localDateTime) : dateTimeFormatter.format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的零点时间指定格式,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param date 指定时间
	 * @param format 时间格式
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(Date date, String format) {
		LocalDateTime localDateTime = LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MIN);
		return StrHelper.isBlank(format)
				? DEFAULT_FORMATTER.format(localDateTime) : DateTimeFormatter.ofPattern(format).format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(LocalDateTime localDateTime) {
		return DEFAULT_FORMATTER.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得指定时间所在日期的零点时间指定格式,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param dateFormat 时间格式
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(LocalDateTime localDateTime, DateEnum dateFormat) {
		return dateFormat == null
				? DEFAULT_FORMATTER.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN))
				: DateTimeFormatter.ofPattern(dateFormat.getPattern())
						.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得指定时间所在日期的零点时间指定格式,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		return dateTimeFormatter == null
				? DEFAULT_FORMATTER.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN))
				: dateTimeFormatter.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得指定时间所在日期的零点时间指定格式,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param format 时间格式
	 * @return 当前时间所在日期零点时间字符串
	 */
	public static String getDayBeginStr(LocalDateTime localDateTime, String format) {
		return StrHelper.isBlank(format)
				? DEFAULT_FORMATTER.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN))
				: DateTimeFormatter.ofPattern(format)
						.format(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得当前时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss.ZZ,如2020-12-12 23:59:59.999999999
	 * 
	 * @return 当前时间所在日期结束时间
	 */
	public static LocalDateTime getDayEnd() {
		return LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss.ZZ,如2020-12-12 23:59:59.999999999
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期结束时间
	 */
	public static LocalDateTime getDayEnd(Date date) {
		LocalDateTime localDateTime = toLocal(date);
		return localDateTime.toLocalDate().atTime(LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss.ZZ,如2020-12-12 23:59:59.999999999
	 * 
	 * @param localDate 指定时间
	 * @return 当前时间所在日期结束时间
	 */
	public static LocalDateTime getDayEnd(LocalDate localDate) {
		return localDate.atTime(LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss.ZZ,如2020-12-12 23:59:59.999999999
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在日期结束时间
	 */
	public static LocalDateTime getDayEnd(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().atTime(LocalTime.MAX);
	}

	/**
	 * 获得当前时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @return 当前时间所在日期结束时间字符串
	 */
	public static String getDayEndStr() {
		return LocalDateTime.of(LocalDate.now(), LocalTime.MAX).format(DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期结束时间字符串
	 */
	public static String getDayEndStr(Date date) {
		return getDayEndStr(date, DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在日期的指定格式的结束时间,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @param dateFormat 时间格式
	 * @return 当前时间所在日期结束时间指定格式字符串
	 */
	public static String getDayEndStr(Date date, DateEnum dateFormat) {
		AssertHelper.notNull(date);
		LocalDateTime localDateTime = LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MAX);
		return dateFormat == null
				? DEFAULT_FORMATTER.format(localDateTime)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的指定格式的结束时间,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 当前时间所在日期结束时间指定格式字符串
	 */
	public static String getDayEndStr(Date date, DateTimeFormatter dateTimeFormatter) {
		AssertHelper.notNull(date);
		LocalDateTime localDateTime = LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MAX);
		return dateTimeFormatter == null
				? DEFAULT_FORMATTER.format(localDateTime) : dateTimeFormatter.format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的指定格式的结束时间,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @param format 时间格式
	 * @return 当前时间所在日期结束时间指定格式字符串
	 */
	public static String getDayEndStr(Date date, String format) {
		AssertHelper.notNull(date);
		LocalDateTime localDateTime = LocalDateTime.of(toLocal(date).toLocalDate(), LocalTime.MAX);
		return StrHelper.isBlank(format)
				? DEFAULT_FORMATTER.format(localDateTime) : DateTimeFormatter.ofPattern(format).format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在日期结束时间字符串
	 */
	public static String getDayEndStr(LocalDateTime localDateTime) {
		return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX).format(DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在日期的指定格式的结束时间,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param dateFormat 时间格式
	 * @return 当前时间所在日期结束时间指定格式字符串
	 */
	public static String getDayEndStr(LocalDateTime localDateTime, DateEnum dateFormat) {
		AssertHelper.notNull(localDateTime);
		localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return dateFormat == null
				? DEFAULT_FORMATTER.format(localDateTime)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的指定格式的结束时间,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 当前时间所在日期结束时间指定格式字符串
	 */
	public static String getDayEndStr(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		AssertHelper.notNull(localDateTime);
		localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return dateTimeFormatter == null
				? DEFAULT_FORMATTER.format(localDateTime) : dateTimeFormatter.format(localDateTime);
	}

	/**
	 * 获得指定时间所在日期的指定格式的结束时间,默认格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param format 时间格式
	 * @return 当前时间所在日期结束时间指定格式字符串
	 */
	public static String getDayEndStr(LocalDateTime localDateTime, String format) {
		AssertHelper.notNull(localDateTime);
		localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return StrHelper.isBlank(format)
				? DEFAULT_FORMATTER.format(localDateTime) : DateTimeFormatter.ofPattern(format).format(localDateTime);
	}

	/**
	 * 获得当前时间所在月份的第一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @return 当前时间所在月第一天的时间
	 */
	@Example
	public static LocalDateTime getFirstDayOfMonth() {
		// 得到上一个周六
		TemporalAdjusters.previous(DayOfWeek.SATURDAY);
		// 得到本月最后一个
		TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY);
		return LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 获得指定时间所在月份的第一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月第一天的时间
	 */
	public static LocalDateTime getFirstDayOfMonth(Date date) {
		return toLocal(date).with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 获得指定时间所在月份的第一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月第一天的时间
	 */
	public static LocalDateTime getFirstDayOfMonth(LocalDateTime localDateTime) {
		return localDateTime.with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 获得当前时间所在月份的最后一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @return 当前时间所在月最后一天的时间
	 */
	public static LocalDateTime getLastDayOfMonth() {
		return LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 获得指定时间所在月份的最后一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月最后一天的时间
	 */
	public static LocalDateTime getLastDayOfMonth(Date date) {
		return toLocal(date).with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 获得指定时间所在月份的最后一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月最后一天的时间
	 */
	public static LocalDateTime getLastDayOfMonth(LocalDateTime localDateTime) {
		return localDateTime.with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 获得当前时间所在月的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-01 00:00:00
	 * 
	 * @return 当前时间所在月的开始时间
	 */
	public static Date getMonthBegin() {
		return toDate(LocalDateTime.of(LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(),
				LocalTime.MIN));
	}

	/**
	 * 获得当前时间所在月的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的开始时间
	 */
	public static Date getMonthBegin(Date date) {
		return toDate(
				LocalDateTime.of(toLocal(date).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得当前时间所在月的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的开始时间
	 */
	public static Date getMonthBegin(LocalDateTime localDateTime) {
		return toDate(
				LocalDateTime.of(localDateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得当前时间所在月的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-01 00:00:00
	 * 
	 * @return 当前时间所在月的开始时间
	 */
	public static LocalDateTime getMonthBeginLocal() {
		return LocalDateTime.of(LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(),
				LocalTime.MIN);
	}

	/**
	 * 获得指定时间所在月的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的开始时间
	 */
	public static LocalDateTime getMonthBeginLocal(Date date) {
		return LocalDateTime.of(toLocal(date).with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN);
	}

	/**
	 * 获得指定时间所在月的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的开始时间
	 */
	public static LocalDateTime getMonthBeginLocal(LocalDateTime localDateTime) {
		return LocalDateTime.of(localDateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate(), LocalTime.MIN);
	}

	/**
	 * 获得当前时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @return 当前时间所在月的结束时间
	 */
	public static Date getMonthEnd() {
		return toDate(getMonthEndLocal());
	}

	/**
	 * 获得指定时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的结束时间
	 */
	public static Date getMonthEnd(Date date) {
		return toDate(getMonthEndLocal(date));
	}

	/**
	 * 获得指定时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的结束时间
	 */
	public static Date getMonthEnd(LocalDateTime localDateTime) {
		return toDate(getMonthEndLocal(localDateTime));
	}

	/**
	 * 获得当前时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @return 当前时间所在月的结束时间
	 */
	public static LocalDateTime getMonthEndLocal() {
		return LocalDateTime.of(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(),
				LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的结束时间
	 */
	public static LocalDateTime getMonthEndLocal(Date date) {
		AssertHelper.notNull(date);
		return LocalDateTime.of(toLocal(date).with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的结束时间
	 */
	public static LocalDateTime getMonthEndLocal(LocalDateTime localDateTime) {
		AssertHelper.notNull(localDateTime);
		return LocalDateTime.of(localDateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 获得当前时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @return 当前时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr() {
		return DEFAULT_FORMATTER.format(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
	}

	/**
	 * 获得指定时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr(Date date) {
		return getMonthEndStr(date, DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @param dateFormat 时间格式
	 * @return 指定时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr(Date date, DateEnum dateFormat) {
		return getMonthEndStr(toLocal(date), dateFormat);
	}

	/**
	 * 获得指定时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 指定时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr(Date date, DateTimeFormatter dateTimeFormatter) {
		return getMonthEndStr(toLocal(date), dateTimeFormatter);
	}

	/**
	 * 获得指定时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr(Date date, String pattern) {
		return getMonthEndStr(toLocal(date), pattern);
	}

	/**
	 * 获得指定时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的结束时间
	 */
	public static String getMonthEndStr(LocalDateTime localDateTime) {
		return getMonthEndStr(localDateTime, DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param dateFormat 时间格式
	 * @return 指定时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr(LocalDateTime localDateTime, DateEnum dateFormat) {
		AssertHelper.notNull(localDateTime);
		localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return dateFormat == null
				? DEFAULT_FORMATTER.format(localDateTime)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(localDateTime);
	}

	/**
	 * 获得指定时间所在月的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 指定时间所在月的结束时间
	 */
	public static String getMonthEndStr(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		AssertHelper.notNull(localDateTime);
		localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return dateTimeFormatter == null
				? DEFAULT_FORMATTER.format(localDateTime) : dateTimeFormatter.format(localDateTime);
	}

	/**
	 * 获得指定时间所在月的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在月的结束时间字符串
	 */
	public static String getMonthEndStr(LocalDateTime localDateTime, String pattern) {
		AssertHelper.notNull(localDateTime);
		localDateTime = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
		return StrHelper.isBlank(pattern)
				? DEFAULT_FORMATTER.format(localDateTime) : DateTimeFormatter.ofPattern(pattern).format(localDateTime);
	}

	/**
	 * 获取季度第一天
	 */
	public static LocalDate getQuarterStart(LocalDate date) {
		int q = getQuarter(date);
		int startMonth = (q - 1) * 3 + 1;
		return LocalDate.of(date.getYear(), startMonth, 1);
	}

	/**
	 * 获取季度最后一天
	 */
	public static LocalDate getQuarterEnd(LocalDate date) {
		int q = getQuarter(date);
		int endMonth = q * 3;
		return LocalDate.of(date.getYear(), endMonth, 1).plusMonths(1).minusDays(1);
	}

	/**
	 * 判断指定时间所属季度
	 * 
	 * @param date 指定时间
	 * @return 1->第一季度;2->第二季度;3->第三季度;4->第四季度
	 */
	public static int getQuarter(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		if (month >= 0 && month < 3) {
			return 1;
		} else if (month >= 3 && month < 6) {
			return 2;
		} else if (month >= 6 && month < 9) {
			return 3;
		} else if (month >= 9 && month < 12) {
			return 4;
		}
		return 0;
	}

	/**
	 * 判断指定时间所属季度
	 * 
	 * @param localDate 指定时间
	 * @return 1->第一季度;2->第二季度;3->第三季度;4->第四季度
	 */
	public static int getQuarter(LocalDate localDate) {
		int m = localDate.getMonthValue();
		return (m - 1) / 3 + 1;
	}

	/**
	 * 判断指定时间所属季度
	 * 
	 * @param localDateTime 指定时间
	 * @return 1->第一季度;2->第二季度;3->第三季度;4->第四季度
	 */
	public static int getQuarter(LocalDateTime localDateTime) {
		int m = localDateTime.getMonthValue();
		return (m - 1) / 3 + 1;
	}

	/**
	 * 获得当前时间所在月的所属季度的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @return 当前时间所在月的所属季度的开始时间
	 */
	public static Date getSeasonBegin() {
		return getSeasonBegin(LocalDateTime.now());
	}

	/**
	 * 获得指定时间所在月的所属季度的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的所属季度的开始时间
	 */
	public static Date getSeasonBegin(Date date) {
		int beginMonth = (getQuarter(date) - 1) * 3;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, beginMonth);
		return getMonthBegin(c.getTime());
	}

	/**
	 * 获得指定时间所在月的所属季度的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的所属季度的开始时间
	 */
	public static Date getSeasonBegin(LocalDateTime localDateTime) {
		int beginMonth = (getQuarter(localDateTime) - 1) * 3 + 1;
		return getMonthBegin(LocalDateTime.of(localDateTime.getYear(), beginMonth, 1, 0, 0));
	}

	/**
	 * 获得当前时间所在月的所属季度的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @return 当前时间所在月的所属季度的结束时间
	 */
	public static Date getSeasonEnd() {
		return getSeasonEnd(LocalDateTime.now());
	}

	/**
	 * 获得指定时间所在月的所属季度的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的所属季度的结束时间
	 */
	public static Date getSeasonEnd(Date date) {
		int beginMonth = getQuarter(date) * 3 - 1;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, beginMonth);
		return getMonthEnd(c.getTime());
	}

	/**
	 * 获得指定时间所在月的所属季度的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的所属季度的结束时间
	 */
	public static Date getSeasonEnd(LocalDateTime localDateTime) {
		int beginMonth = getQuarter(localDateTime) * 3;
		return getMonthEnd(LocalDateTime.of(localDateTime.getYear(), beginMonth, 1, 0, 0));
	}

	/**
	 * 获得当前时间所在月的所属季度的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @return 当前时间所在月的所属季度的结束时间
	 */
	public static LocalDateTime getSeasonEndLocal() {
		return getSeasonEndLocal(LocalDateTime.now());
	}

	/**
	 * 获得指定时间所在月的所属季度的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在月的所属季度的结束时间
	 */
	public static LocalDateTime getSeasonEndLocal(Date date) {
		return getSeasonEndLocal(toLocal(date));
	}

	/**
	 * 获得指定时间所在月的所属季度的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月的所属季度的结束时间
	 */
	public static LocalDateTime getSeasonEndLocal(LocalDateTime localDateTime) {
		int beginMonth = getQuarter(localDateTime) * 3;
		return getMonthEndLocal(LocalDateTime.of(localDateTime.getYear(), beginMonth, 1, 0, 0));
	}

	/**
	 * 获得当前时间所属周的周一开始时间
	 * 
	 * @return 当前时间所属周的周一的开始时间
	 */
	public static Date getWeekBegin() {
		return toDate(getWeekBeginLocal(LocalDateTime.now()));
	}

	/**
	 * 获得指定时间所属周的周一开始时间
	 * 
	 * @param date 指定时间
	 * @return 指定时间所属周的周一的开始时间
	 */
	public static Date getWeekBegin(Date date) {
		return toDate(getWeekBeginLocal(toLocal(date)));
	}

	/**
	 * 获得指定时间所属周的周一开始时间
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所属周的周一的开始时间
	 */
	public static Date getWeekBegin(LocalDateTime localDateTime) {
		LocalDateTime with = localDateTime.with(ChronoField.DAY_OF_WEEK, 1);
		return toDate(getDayBegin(with));
	}

	/**
	 * 获得当前时间所属周的周一开始时间
	 * 
	 * @return 当前时间所属周的周一的开始时间
	 */
	public static LocalDateTime getWeekBeginLocal() {
		return getWeekBeginLocal(LocalDateTime.now());
	}

	/**
	 * 获得指定时间所属周的周一开始时间
	 * 
	 * @param date 指定时间
	 * @return 指定时间所属周的周一的开始时间
	 */
	public static LocalDateTime getWeekBeginLocal(Date date) {
		return getWeekBeginLocal(toLocal(date));
	}

	/**
	 * 获得指定时间所属周的周一开始时间
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所属周的周一的开始时间
	 */
	public static LocalDateTime getWeekBeginLocal(LocalDateTime localDateTime) {
		LocalDateTime with = localDateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.getValue());
		return getDayBegin(with);
	}

	/**
	 * 获得指定时间所属周的周天结束时间
	 * 
	 * @param date 指定时间
	 * @return 指定时间所属周的周天结束时间
	 */
	public static Date getWeekEnd(Date date) {
		return toDate(getWeekEndLocal(toLocal(date)));
	}

	/**
	 * 获得指定时间所属周的周天结束时间
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所属周的周天结束时间
	 */
	public static Date getWeekEnd(LocalDateTime localDateTime) {
		LocalDateTime with = localDateTime.with(ChronoField.DAY_OF_WEEK, 7);
		return toDate(getDayEnd(with));
	}

	/**
	 * 获得当前时间所属周的周天结束时间
	 * 
	 * @return 当前时间所属周的周天结束时间
	 */
	public static Date getWeekEndl() {
		return toDate(getWeekEndLocal(LocalDateTime.now()));
	}

	/**
	 * 获得当前时间所属周的周天结束时间
	 * 
	 * @return 当前时间所属周的周天结束时间
	 */
	public static LocalDateTime getWeekEndLocal() {
		return getWeekEndLocal(LocalDateTime.now());
	}

	/**
	 * 获得指定时间所属周的周天结束时间
	 * 
	 * @param date 指定时间
	 * @return 指定时间所属周的周天结束时间
	 */
	public static LocalDateTime getWeekEndLocal(Date date) {
		return getWeekEndLocal(toLocal(date));
	}

	/**
	 * 获得指定时间所属周的周天结束时间
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所属周的周天结束时间
	 */
	public static LocalDateTime getWeekEndLocal(LocalDateTime localDateTime) {
		LocalDateTime with = localDateTime.with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.getValue());
		return getDayEnd(with);
	}

	/**
	 * 获得当前时间所在年的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @return 当前时间所在年的开始时间
	 */
	public static Date getYearBegin() {
		return toDate(getYearBeginLocal());
	}

	/**
	 * 获得指定时间所在年的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在年的开始时间
	 */
	public static Date getYearBegin(Date date) {
		return toDate(getYearBeginLocal(toLocal(date)));
	}

	/**
	 * 获得指定时间所在年的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在年的开始时间
	 */
	public static Date getYearBegin(LocalDateTime localDateTime) {
		return toDate(getYearBeginLocal(localDateTime));
	}

	/**
	 * 获得当前时间所在年的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @return 当前时间所在年的开始时间
	 */
	public static LocalDateTime getYearBeginLocal() {
		return LocalDateTime.of(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).toLocalDate(),
				LocalTime.MIN);
	}

	/**
	 * 获得指定时间所在年的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在年的开始时间
	 */
	public static LocalDateTime getYearBeginLocal(Date date) {
		AssertHelper.notNull(date);
		return LocalDateTime.of(toLocal(date).with(TemporalAdjusters.firstDayOfYear()).toLocalDate(), LocalTime.MIN);
	}

	/**
	 * 获得指定时间所在年的开始时间,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在年的开始时间
	 */
	public static LocalDateTime getYearBeginLocal(LocalDateTime localDateTime) {
		AssertHelper.notNull(localDateTime);
		return LocalDateTime.of(localDateTime.with(TemporalAdjusters.firstDayOfYear()).toLocalDate(), LocalTime.MIN);
	}

	/**
	 * 获得当前时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @return 当前时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr() {
		return getYearBeginStr(LocalDateTime.now());
	}

	/**
	 * 获得当前时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(Date date) {
		return getYearBeginStr(toLocal(date), DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @param dateFormat 时间格式
	 * @return 指定时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(Date date, DateEnum dateFormat) {
		LocalDateTime yearBeginLocal = getYearBeginLocal(toLocal(date));
		return null == dateFormat
				? DEFAULT_FORMATTER.format(yearBeginLocal)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(yearBeginLocal);
	}

	/**
	 * 获得指定时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 指定时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(Date date, DateTimeFormatter dateTimeFormatter) {
		LocalDateTime yearBeginLocal = getYearBeginLocal(toLocal(date));
		return null == dateTimeFormatter
				? DEFAULT_FORMATTER.format(yearBeginLocal) : dateTimeFormatter.format(yearBeginLocal);
	}

	/**
	 * 获得指定时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param date 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(Date date, String pattern) {
		LocalDateTime yearBeginLocal = getYearBeginLocal(toLocal(date));
		return StrHelper.isBlank(pattern)
				? DEFAULT_FORMATTER.format(yearBeginLocal)
				: DateTimeFormatter.ofPattern(pattern).format(yearBeginLocal);
	}

	/**
	 * 获得当前时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(LocalDateTime localDateTime) {
		return getYearBeginStr(localDateTime, DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param dateFormat 时间格式
	 * @return 指定时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(LocalDateTime localDateTime, DateEnum dateFormat) {
		LocalDateTime yearBeginLocal = getYearBeginLocal(localDateTime);
		return null == dateFormat
				? DEFAULT_FORMATTER.format(yearBeginLocal)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(yearBeginLocal);
	}

	/**
	 * 获得指定时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 指定时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		LocalDateTime yearBeginLocal = getYearBeginLocal(localDateTime);
		return null == dateTimeFormatter
				? DEFAULT_FORMATTER.format(yearBeginLocal) : dateTimeFormatter.format(yearBeginLocal);
	}

	/**
	 * 获得指定时间所在年的开始时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-01-01 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在年的开始时间字符串
	 */
	public static String getYearBeginStr(LocalDateTime localDateTime, String pattern) {
		LocalDateTime yearBeginLocal = getYearBeginLocal(localDateTime);
		return StrHelper.isBlank(pattern)
				? DEFAULT_FORMATTER.format(yearBeginLocal)
				: DateTimeFormatter.ofPattern(pattern).format(yearBeginLocal);
	}

	/**
	 * 获得当前时间所在年的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @return 当前时间所在年的结束时间
	 */
	public static Date getYearEnd() {
		return toDate(getYearEndLocal());
	}

	/**
	 * 获得指定时间所在年的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在年的结束时间
	 */
	public static Date getYearEnd(Date date) {
		return toDate(getYearEndLocal(toLocal(date)));
	}

	/**
	 * 获得指定时间所在年的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在年的结束时间
	 */
	public static Date getYearEnd(LocalDateTime localDateTime) {
		return toDate(getYearEndLocal(localDateTime));
	}

	/**
	 * 获得当前时间所在年的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @return 当前时间所在年的结束时间
	 */
	public static LocalDateTime getYearEndLocal() {
		return LocalDateTime.of(LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).toLocalDate(),
				LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在年的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 指定时间所在年的结束时间
	 */
	public static LocalDateTime getYearEndLocal(Date date) {
		AssertHelper.notNull(date);
		return LocalDateTime.of(toLocal(date).with(TemporalAdjusters.lastDayOfYear()).toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 获得指定时间所在年的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @return 指定时间所在年的结束时间
	 */
	public static LocalDateTime getYearEndLocal(LocalDateTime localDateTime) {
		AssertHelper.notNull(localDateTime);
		return LocalDateTime.of(localDateTime.with(TemporalAdjusters.lastDayOfYear()).toLocalDate(), LocalTime.MAX);
	}

	/**
	 * 获得当前时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @return 当前时间所在年的结束时间字符串
	 */
	public static String getYearEndStr() {
		return getYearEndStr(LocalDateTime.now());
	}

	/**
	 * 获得当前时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(Date date) {
		return getYearEndStr(toLocal(date), DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @param dateFormat 时间格式
	 * @return 指定时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(Date date, DateEnum dateFormat) {
		LocalDateTime yearEndLocal = getYearEndLocal(toLocal(date));
		return null == dateFormat
				? DEFAULT_FORMATTER.format(yearEndLocal)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(yearEndLocal);
	}

	/**
	 * 获得指定时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 指定时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(Date date, DateTimeFormatter dateTimeFormatter) {
		LocalDateTime yearEndLocal = getYearEndLocal(toLocal(date));
		return null == dateTimeFormatter
				? DEFAULT_FORMATTER.format(yearEndLocal) : dateTimeFormatter.format(yearEndLocal);
	}

	/**
	 * 获得指定时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param date 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(Date date, String pattern) {
		LocalDateTime yearEndLocal = getYearEndLocal(toLocal(date));
		return StrHelper.isBlank(pattern)
				? DEFAULT_FORMATTER.format(yearEndLocal) : DateTimeFormatter.ofPattern(pattern).format(yearEndLocal);
	}

	/**
	 * 获得当前时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(LocalDateTime localDateTime) {
		return getYearEndStr(localDateTime, DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param dateFormat 时间格式
	 * @return 指定时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(LocalDateTime localDateTime, DateEnum dateFormat) {
		LocalDateTime yearEndLocal = getYearEndLocal(localDateTime);
		return null == dateFormat
				? DEFAULT_FORMATTER.format(yearEndLocal)
				: DateTimeFormatter.ofPattern(dateFormat.getPattern()).format(yearEndLocal);
	}

	/**
	 * 获得指定时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param dateTimeFormatter 时间格式
	 * @return 指定时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
		LocalDateTime yearEndLocal = getYearEndLocal(localDateTime);
		return null == dateTimeFormatter
				? DEFAULT_FORMATTER.format(yearEndLocal) : dateTimeFormatter.format(yearEndLocal);
	}

	/**
	 * 获得指定时间所在年的结束时间字符串,格式为yyyy-MM-dd HH:mm:ss,如2020-12-31 23:59:59
	 * 
	 * @param localDateTime 指定时间
	 * @param pattern 时间格式
	 * @return 指定时间所在年的结束时间字符串
	 */
	public static String getYearEndStr(LocalDateTime localDateTime, String pattern) {
		LocalDateTime yearEndLocal = getYearEndLocal(localDateTime);
		return StrHelper.isBlank(pattern)
				? DEFAULT_FORMATTER.format(yearEndLocal) : DateTimeFormatter.ofPattern(pattern).format(yearEndLocal);
	}

	/**
	 * 获得当前时间的前1天,就是昨天,默认格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 昨天
	 */
	public static String getYesterdayStr() {
		return formatDate(plusDay(-1));
	}

	/**
	 * 获得指定时间的前1天,就是昨天,默认格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date 指定时间
	 * @return 昨天
	 */
	public static String getYesterdayStr(Date date) {
		return formatDate(plusDay(date, -1));
	}

	/**
	 * 判断date1是否在当前时间之后,即date1大于等于当前时间
	 * 
	 * @param date1 时间1
	 * @return true->date1在当前时间之后或date1和当前时间相同,false->date1在当前时间之前
	 */
	public static boolean isAfter(Date date1) {
		AssertHelper.notNull(date1);
		LocalDateTime now = LocalDateTime.now();
		return toLocal(date1).isAfter(now) || toLocal(date1).isEqual(now);
	}

	/**
	 * 判断date1是否在date2之后,即date1大于等于date2
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true->date1在date2之后或date1等于date2,false->date1在date2之前
	 */
	public static boolean isAfter(Date date1, Date date2) {
		AssertHelper.notNull(date1);
		AssertHelper.notNull(date2);
		return toLocal(date1).isAfter(toLocal(date2)) || toLocal(date1).isEqual(toLocal(date2));
	}

	/**
	 * 判断date1是否在当前时间之前,即date1小于等于当前时间
	 * 
	 * @param date1 时间1
	 * @return true->date1在当前时间之前或date1等于当前时间,false->date1在当前时间之后
	 */
	public static boolean isBefore(Date date1) {
		AssertHelper.notNull(date1);
		LocalDateTime now = LocalDateTime.now();
		return toLocal(date1).isBefore(now) || toLocal(date1).isEqual(now);
	}

	/**
	 * 判断date1是否在date2之前,即date1小于等于date2
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true->date1在date2之前或date1等于date2,false->date1在date2之后
	 */
	public static boolean isBefore(Date date1, Date date2) {
		AssertHelper.notNull(date1);
		AssertHelper.notNull(date2);
		return toLocal(date1).isBefore(toLocal(date2)) || toLocal(date1).isEqual(toLocal(date2));
	}

	/**
	 * 根据出生日期判断今天是否为生日
	 * 
	 * @param birthday 出生日期
	 * @return true->是;false->否
	 */
	public static boolean isBirthday(Date birthday) {
		return isBirthday(toLocal(birthday).toLocalDate());
	}

	/**
	 * 根据出生日期判断今天是否为生日
	 * 
	 * @param birthday 出生日期
	 * @return true->是;false->否
	 */
	public static boolean isBirthday(LocalDate birthday) {
		return MonthDay.from(LocalDate.now()).equals(MonthDay.of(birthday.getMonth(), birthday.getDayOfMonth()));
	}

	/**
	 * 将LocalDateTime类型转换为Date
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 转换后的时间,和localDateTime时间相同
	 */
	static Date toDate(LocalDateTime localDateTime) {
		AssertHelper.notNull(localDateTime);
		return toDate(localDateTime.atZone(DEFAULT_ZONE));
	}

	/**
	 * ZonedDateTime 转 Date
	 * 
	 * @param zonedDateTime 带时区的时间
	 * @return 转换后时间
	 */
	static Date toDate(ZonedDateTime zonedDateTime) {
		AssertHelper.notNull(zonedDateTime);
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * 将LocalDateTime类型转换为Date UTC时间
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 转换后的时间,根据时区不同,和localDateTime不同
	 */
	static Date toDateUtc(LocalDateTime localDateTime) {
		AssertHelper.notNull(localDateTime);
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss或yyyy-MM-ddTHH:mm:ss字符串格式化,返回的localdatetime中间必然有T
	 * 
	 * @param localDateTime 时间字符串,若不符合指定格式将报错
	 * @return 格式化后时间
	 */
	public static LocalDateTime parse(String localDateTime) {
		if (localDateTime.contains("T")) {
			return LocalDateTime.parse(localDateTime.toUpperCase());
		} else {
			return LocalDateTime.parse(localDateTime, DEFAULT_FORMATTER);
		}
	}

	/**
	 * 将指定时间字符串格式化格式化,返回的localdatetime中间必然有T
	 * 
	 * @param localDateTime 时间字符串,若不符合指定传入格式将报错
	 * @param pattern 字符串时间的格式
	 * @return 格式化后时间
	 */
	public static LocalDateTime parse(String localDateTime, DateEnum pattern) {
		if (localDateTime.contains("T")) {
			return LocalDateTime.parse(localDateTime.toUpperCase(), DateTimeFormatter.ofPattern(pattern.getPattern()));
		} else {
			return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern(pattern.getPattern()));
		}
	}

	/**
	 * 将指定时间字符串格式化格式化,返回的localdatetime中间必然有T
	 * 
	 * @param localDateTime 时间字符串,若不符合指定传入格式将报错
	 * @param pattern 字符串时间的格式
	 * @return 格式化后时间
	 */
	public static LocalDateTime parse(String localDateTime, String pattern) {
		if (localDateTime.contains("T")) {
			return LocalDateTime.parse(localDateTime.toUpperCase(), DateTimeFormatter.ofPattern(pattern));
		} else {
			return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern(pattern));
		}
	}

	/**
	 * 将格式为yyyy-MM-dd的时间字符串转为Date类型,时分秒都为0
	 * 
	 * @param date 时间字符串
	 * @return date对象
	 */
	public static Date parseDate(String date) {
		return toDate(parse(date + " 00:00:00"));
	}

	/**
	 * 将指定时间字符串格式化格式化
	 * 
	 * @param dateTime 时间字符串,若不符合指定格式将报错
	 * @param pattern 字符串时间的格式
	 * @return date对象
	 */
	public static Date parseDate(String dateTime, DateEnum pattern) {
		return toDate(parse(dateTime, pattern.getPattern()));
	}

	/**
	 * 将指定时间字符串格式化格式化
	 * 
	 * @param dateTime 时间字符串,若不符合指定格式将报错
	 * @param pattern 字符串时间的格式
	 * @return date对象
	 */
	public static Date parseDate(String dateTime, String pattern) {
		return toDate(parse(dateTime, pattern));
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss或yyyy-MM-ddTHH:mm:ss时间格式为Date类型
	 * 
	 * @param dateTime 时间字符串,若不符合指定格式将报错
	 * @return date对象
	 */
	public static Date parseDateTime(String dateTime) {
		return toDate(parse(dateTime));
	}

	/**
	 * 在指定时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param date 指定时间
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static Date plusDate(Date date, long amount, TemporalUnit temporalUnit) {
		return plusDate(toLocal(date), amount, temporalUnit);
	}

	/**
	 * 在指定时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param localDateTime 指定时间
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static Date plusDate(LocalDateTime localDateTime, long amount, TemporalUnit temporalUnit) {
		AssertHelper.notNull(localDateTime);
		AssertHelper.notNull(temporalUnit);
		LocalDateTime newDateTime = localDateTime.plus(amount, temporalUnit);
		return toDate(newDateTime);
	}

	/**
	 * 在当前时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static Date plusDate(long amount, TemporalUnit temporalUnit) {
		return plusDate(LocalDateTime.now(), amount, temporalUnit);
	}

	/**
	 * 在指定时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param date 指定时间
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static LocalDateTime plusDateTime(Date date, long amount, TemporalUnit temporalUnit) {
		return plusDateTime(toLocal(date), amount, temporalUnit);
	}

	/**
	 * 在指定时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param localDateTime 指定时间
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static LocalDateTime plusDateTime(LocalDateTime localDateTime, long amount, TemporalUnit temporalUnit) {
		AssertHelper.notNull(localDateTime);
		AssertHelper.notNull(temporalUnit);
		return localDateTime.plus(amount, temporalUnit);
	}

	/**
	 * 在当前时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static LocalDateTime plusDateTime(long amount, TemporalUnit temporalUnit) {
		return plusDateTime(LocalDateTime.now(), amount, temporalUnit);
	}

	/**
	 * 在当前时间基础上增加或减少amount天,若超过当前最大天数,会自动转到相应月数
	 * 
	 * @param amount 增加或较少的天数
	 * @return 新的时间
	 */
	public static Date plusDay(Date date1, long amount) {
		return toDate(toLocal(date1).plusDays(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount天,若超过当前最大天数,会自动转到相应月数
	 * 
	 * @param amount 增加或较少的天数
	 * @return 新的时间
	 */
	public static Date plusDay(long amount) {
		return toDate(toLocal(new Date()).plusDays(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount小时,若超过24小时,会自动转到相应天数
	 * 
	 * @param amount 增加或较少的小时
	 * @return 新的时间
	 */
	public static Date plusHour(Date date1, long amount) {
		return toDate(toLocal(date1).plusHours(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount小时,若超过24小时,会自动转到相应天数
	 * 
	 * @param amount 增加或较少的小时
	 * @return 新的时间
	 */
	public static Date plusHour(long amount) {
		return toDate(toLocal(new Date()).plusHours(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount个月,若超过12个月,会自动转到相应年
	 * 
	 * @param amount 增加或较少的月数
	 * @return 新的时间
	 */
	public static Date plusMonth(Date date1, long amount) {
		return toDate(toLocal(date1).plusMonths(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount个月,若超过12个月,会自动转到相应年
	 * 
	 * @param amount 增加或较少的月数
	 * @return 新的时间
	 */
	public static Date plusMonth(long amount) {
		return toDate(toLocal(new Date()).plusMonths(amount));
	}

	/**
	 * 在指定时间基础上增加或减少amount个周
	 * 
	 * @param amount 增加或较少的周数
	 * @return 新的时间
	 */
	public static Date plusWeek(Date date1, long amount) {
		return toDate(toLocal(date1).plusWeeks(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount个周
	 * 
	 * @param amount 增加或较少的周数
	 * @return 新的时间
	 */
	public static Date plusWeek(long amount) {
		return toDate(toLocal(new Date()).plusWeeks(amount));
	}

	/**
	 * 判断指定时间与当前时间是否同月,但可能不同年,不同日
	 * 
	 * @param date 指定时间
	 * @return true是,false否
	 */
	public static boolean sameMonth(Date date) {
		return toLocal(date).get(ChronoField.MONTH_OF_YEAR) == LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断指定时间与当前时间是否同月,但可能不同年,不同日
	 * 
	 * @param date 指定时间
	 * @return true是,false否
	 */
	public static boolean sameMonth(Date date1, Date date2) {
		return toLocal(date1).get(ChronoField.MONTH_OF_YEAR) == toLocal(date2).get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断2个时间否同月同日
	 * 
	 * @param temporalAccessor1 时间1
	 * @param temporalAccessor2 时间2
	 * @return true是,false否
	 */
	public static boolean sameMonth(TemporalAccessor temporalAccessor1, TemporalAccessor temporalAccessor2) {
		return (null == temporalAccessor1 || null == temporalAccessor2)
				? false
				: temporalAccessor1.get(ChronoField.MONTH_OF_YEAR) == temporalAccessor2.get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断指定时间与当前时间是否同月同日
	 * 
	 * @param date 指定时间
	 * @return true是,false否
	 */
	public static boolean sameMonthDay(Date date) {
		return toLocal(date).get(ChronoField.MONTH_OF_YEAR) == LocalDate.now().get(ChronoField.MONTH_OF_YEAR)
				&& toLocal(date).get(ChronoField.DAY_OF_MONTH) == LocalDate.now().get(ChronoField.DAY_OF_MONTH);
	}

	/**
	 * 判断2个时间否同月同日
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true是,false否
	 */
	public static boolean sameMonthDay(Date date1, Date date2) {
		return toLocal(date1).get(ChronoField.MONTH_OF_YEAR) == toLocal(date2).get(ChronoField.MONTH_OF_YEAR)
				&& toLocal(date1).get(ChronoField.DAY_OF_MONTH) == toLocal(date2).get(ChronoField.DAY_OF_MONTH);
	}

	/**
	 * 判断2个时间否同月同日
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true是,false否
	 */
	public static boolean sameMonthDay(MonthDay date1, MonthDay date2) {
		return (null == date1 || null == date2) ? false : date1.equals(date2);
	}

	/**
	 * 判断2个时间否同月同日
	 * 
	 * @param temporalAccessor1 时间1
	 * @param temporalAccessor2 时间2
	 * @return true是,false否
	 */
	public static boolean sameMonthDay(TemporalAccessor temporalAccessor1, TemporalAccessor temporalAccessor2) {
		return (null == temporalAccessor1 || null == temporalAccessor2)
				? false
				: temporalAccessor1.get(ChronoField.MONTH_OF_YEAR) == temporalAccessor2.get(ChronoField.MONTH_OF_YEAR)
						&& temporalAccessor1.get(ChronoField.DAY_OF_MONTH) == temporalAccessor2
								.get(ChronoField.DAY_OF_MONTH);
	}

	/**
	 * 判断2个时间是否为同一年
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true同一年,false不是
	 */
	public static boolean sameYear(Date date1, Date date2) {
		return toLocal(date1).get(ChronoField.YEAR) == toLocal(date2).get(ChronoField.YEAR);
	}

	/**
	 * 判断2个时间是否为同一年
	 * 
	 * @param temporalAccessor1 时间1
	 * @param temporalAccessor2 时间2
	 * @return true同一年,false不是
	 */
	public static boolean sameYear(TemporalAccessor temporalAccessor1, TemporalAccessor temporalAccessor2) {
		if (null == temporalAccessor1 || null == temporalAccessor2) {
			return false;
		}
		return temporalAccessor1.get(ChronoField.YEAR) == temporalAccessor2.get(ChronoField.YEAR);
	}

	/**
	 * 判断2个时间是否为同年同月
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true同年同月,false不是
	 */
	public static boolean sameYearMonth(Date date1, Date date2) {
		return sameYear(date1, date2)
				&& toLocal(date1).get(ChronoField.MONTH_OF_YEAR) == toLocal(date2).get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断2个时间是否为同年同月
	 * 
	 * @param temporalAccessor1 时间1
	 * @param temporalAccessor2 时间2
	 * @return true同年同月,false不是
	 */
	public static boolean sameYearMonth(TemporalAccessor temporalAccessor1, TemporalAccessor temporalAccessor2) {
		return sameYear(temporalAccessor1, temporalAccessor2)
				&& temporalAccessor1.get(ChronoField.MONTH_OF_YEAR) == temporalAccessor2.get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断2个时间是否为同年同月
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true同年同月,false不是
	 */
	public static boolean sameYearMonth(YearMonth date1, YearMonth date2) {
		return (null == date1 || null == date2) ? false : date1.equals(date2);
	}

	/**
	 * 判断2个时间是否为同年同月同日
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true同年同月同日,false不是
	 */
	public static boolean sameYearMonthDay(Date date1, Date date2) {
		return sameYearMonth(date1, date2)
				&& toLocal(date1).get(ChronoField.DAY_OF_MONTH) == toLocal(date2).get(ChronoField.DAY_OF_MONTH);
	}

	/**
	 * 判断2个时间是否为同年同月同日
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true同年同月同日,false不是
	 */
	public static boolean sameYearMonthDay(LocalDate date1, LocalDate date2) {
		if (null == date1 || null == date2) {
			return false;
		}
		return date1.equals(date2);
	}

	/**
	 * 判断2个时间是否为同年同月同日
	 * 
	 * @param temporalAccessor1 时间1
	 * @param temporalAccessor2 时间2
	 * @return true同年同月同日,false不是
	 */
	public static boolean sameYearMonthDay(TemporalAccessor temporalAccessor1, TemporalAccessor temporalAccessor2) {
		return sameYearMonth(temporalAccessor1, temporalAccessor2)
				&& temporalAccessor1.get(ChronoField.DAY_OF_MONTH) == temporalAccessor2.get(ChronoField.DAY_OF_MONTH);
	}

	/**
	 * 设置月份
	 * 
	 * @param localDateTime 指定时间
	 * @param month 指定月份,只能是1到12,否则报错
	 * @return 新的时间
	 */
	static LocalDateTime setMonth(LocalDateTime localDateTime, int month) {
		return localDateTime.withMonth(month);
	}

	/**
	 * 设置年份
	 * 
	 * @param localDateTime 指定时间
	 * @param year 指定年份
	 * @return 新的时间
	 */
	static LocalDateTime setYear(LocalDateTime localDateTime, int year) {
		return localDateTime.withYear(year);
	}

	/**
	 * 计算两个时间之间的间隔,和Peroid不同,会直接转换为相同单位后计算
	 *
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param unit 单位
	 * @return 差值,end - start,负数代表end早于start
	 */
	static long between(Temporal start, Temporal end, ChronoUnit unit) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(end);
		Objects.requireNonNull(unit);
		return unit.between(start, end);
	}

	/**
	 * 计算相差天数
	 *
	 * @param start 开始
	 * @param end 结束
	 * @return 天数差值
	 */
	public static long betweenDays(LocalDateTime start, LocalDateTime end) {
		return between(start, end, ChronoUnit.DAYS);
	}

	/**
	 * 计算相差小时
	 *
	 * @param start 开始
	 * @param end 结束
	 * @return 小时差值
	 */
	public static long betweenHours(LocalDateTime start, LocalDateTime end) {
		return between(start, end, ChronoUnit.HOURS);
	}

	/**
	 * 计算相差毫秒
	 *
	 * @param start 开始
	 * @param end 结束
	 * @return 毫秒差值
	 */
	public static long betweenMilli(LocalDateTime start, LocalDateTime end) {
		Objects.requireNonNull(start);
		Objects.requireNonNull(end);
		return between(start, end, ChronoUnit.MILLIS);
	}

	/**
	 * 计算相差分钟
	 *
	 * @param start 开始
	 * @param end 结束
	 * @return 分钟差值
	 */
	public static long betweenMinutes(LocalDateTime start, LocalDateTime end) {
		return between(start, end, ChronoUnit.MINUTES);
	}

	/**
	 * 计算相差秒
	 *
	 * @param start 开始
	 * @param end 结束
	 * @return 秒差值
	 */
	public static long betweenSeconds(LocalDateTime start, LocalDateTime end) {
		return between(start, end, ChronoUnit.SECONDS);
	}

	/**
	 * 获得当前时间和指定时间之间相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date 指定时间,可以是未来的,也可以是过去的.
	 * @return 天数相差数
	 */
	static int spanDay(Date date) {
		return Period.between(LocalDate.now(), toLocal(date).toLocalDate()).getDays();
	}

	/**
	 * 获得date1和date2相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 天数相差数
	 */
	static int spanDay(Date date1, Date date2) {
		return Period.between(toLocal(date1).toLocalDate(), toLocal(date2).toLocalDate()).getDays();
	}

	/**
	 * 获得当前时间和指定时间之间相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date 指定时间,可以是未来的,也可以是过去的
	 * @return 天数相差数
	 */
	static int spanDay(LocalDate date) {
		return Period.between(LocalDate.now(), date).getDays();
	}

	/**
	 * 获得date1和date2相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 天数相差数
	 */
	static int spanDay(LocalDate date1, LocalDate date2) {
		return Period.between(date1, date2).getDays();
	}

	/**
	 * 获得date1和date2相差的天数
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 天数相差数
	 */
	public static long spanDays(Temporal date1, Temporal date2) {
		return ChronoUnit.DAYS.between(date1, date2);
	}

	/**
	 * 获得当前时间和指定时间之间相差的月数 {@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date 指定时间,可以是未来的,也可以是过去的.
	 * @return 月份相差数
	 */
	public static int spanMonth(Date date) {
		return Period.between(LocalDate.now(), toLocal(date).toLocalDate()).getMonths();
	}

	/**
	 * 获得时间1和时间2之间相差的月数 {@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 月份相差数
	 */
	public static int spanMonth(Date date1, Date date2) {
		return Period.between(toLocal(date1).toLocalDate(), toLocal(date2).toLocalDate()).getMonths();
	}

	/**
	 * 获得当前时间和指定时间之间相差的月数 {@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date 指定时间,可以是未来的,也可以是过去的
	 * @return 月份相差数
	 */
	public static int spanMonth(LocalDate date) {
		return Period.between(LocalDate.now(), date).getMonths();
	}

	/**
	 * 获得date1到date2相差的月数,只有需要完整的年月日才使用,否则容易出现错误
	 * 
	 * date2的年月大于date1,得到的是正数,当date2的年月小于date1时,取大于时的负数即可
	 * 
	 * 并不会单纯的将时间都换算成年/月/日计算,而是分开计算,如:
	 * 
	 * <pre>
	 * spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 1))		=0;同月
	 * spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 5))		=0;同月
	 * spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 5, 5))		=2;日和月都够
	 * 	spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 5, 1))		=1;月够2,但是日不够1整个月,只能为1
	 * 	spanMonth(LocalDate.of(2000, 7, 5), LocalDate.of(2021, 5, 8))		=10;如果看年,就不为10,应该是200多
	 * 	spanMonth(LocalDate.of(2000, 7, 5), LocalDate.of(2021, 5, 1))		=9
	 * 	spanMonth(LocalDate.of(2000, 3, 5), LocalDate.of(2021, 5, 5))		=2
	 * 	spanMonth(LocalDate.of(2000, 3, 5), LocalDate.of(2021, 5, 1))		=1
	 * </pre>
	 * 
	 * @param date1 时间1,可以是未来的,也可以是过去的
	 * @param date2 时间2,可以是未来的,也可以是过去的
	 * @return 月份相差数
	 */
	public static int spanMonth(LocalDate date1, LocalDate date2) {
		return Period.between(date1, date2).getMonths();
	}

	public static void main(String[] args) {
		System.out.println(Period.between(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 1, 5)).getMonths());
		System.out.println(Period.between(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 5, 1)).getMonths());
	}

	/**
	 * 获得date1到date2相差的月数,年份,月份,天数都需要根据情况比较,情况复杂
	 * 
	 * <pre>
	 * LocalDate.of(2021, 11, 11), LocalDate.of(2001, 11, 11) = -240
	 * LocalDate.of(2021, 11, 10), LocalDate.of(2001, 11, 11) = -239
	 * </pre>
	 * 
	 * @param date1 时间1,可以是未来的,也可以是过去的
	 * @param date2 时间2,可以是未来的,也可以是过去的
	 * @return 月份相差数
	 */
	public static long spanMonths(LocalDate date1, LocalDate date2) {
		return ChronoUnit.MONTHS.between(date1, date2);
	}

	/**
	 * 获得指定时间到当前时间相差的年份
	 * 
	 * @param date1 指定时间
	 * @return 年份相差数.大于0表示当前时间大于date1
	 */
	public static int spanYear(Date date1) {
		return LocalDate.now().get(ChronoField.YEAR) - toLocal(date1).get(ChronoField.YEAR);
	}

	/**
	 * 获得指定时间到当前时间相差的年份
	 * 
	 * @param date1 指定时间
	 * @param date2 时间2
	 * @return 年份相差数.大于0表示date2大于date1
	 */
	public static int spanYear(Date date1, Date date2) {
		return toLocal(date2).get(ChronoField.YEAR) - toLocal(date1).get(ChronoField.YEAR);
	}

	/**
	 * 获得指定时间到当前时间相差的年份
	 * 
	 * @param date1 指定时间
	 * @return 年份相差数.大于0表示当前时间大于date1
	 */
	public static int spanYear(TemporalAccessor date1) {
		return LocalDate.now().get(ChronoField.YEAR) - date1.get(ChronoField.YEAR);
	}

	/**
	 * 获得date1到date2相差的年份
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 年份相差数.大于0表示date2大于date1
	 */
	public static int spanYear(TemporalAccessor date1, TemporalAccessor date2) {
		return date2.get(ChronoField.YEAR) - date1.get(ChronoField.YEAR);
	}

	/**
	 * 获得date1到date2相差的年份
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 年份相差数.大于0表示date2大于date1
	 */
	public static long spanYears(Temporal date1, Temporal date2) {
		return ChronoUnit.YEARS.between(date1, date2);
	}

	/**
	 * 将TimeUnit单位转换为ChronoUnit单位,JDK9自带该方法
	 * 
	 * @param timeUnit TimeUnit
	 * @return ChronoUnit
	 */
	public static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
		switch (timeUnit) {
		case NANOSECONDS:
			return ChronoUnit.NANOS;
		case MICROSECONDS:
			return ChronoUnit.MICROS;
		case MILLISECONDS:
			return ChronoUnit.MILLIS;
		case SECONDS:
			return ChronoUnit.SECONDS;
		case MINUTES:
			return ChronoUnit.MINUTES;
		case HOURS:
			return ChronoUnit.HOURS;
		case DAYS:
			return ChronoUnit.DAYS;
		default:
			throw new AssertionError();
		}
	}

	/**
	 * 将ChronoUnit单位转换为TimeUnit单位,JDK9自带该方法
	 * 
	 * @param chronoUnit ChronoUnit
	 * @return TimeUnit
	 */
	public static TimeUnit toTimeUnit(ChronoUnit chronoUnit) {
		switch (Objects.requireNonNull(chronoUnit, "chronoUnit")) {
		case NANOS:
			return TimeUnit.NANOSECONDS;
		case MICROS:
			return TimeUnit.MICROSECONDS;
		case MILLIS:
			return TimeUnit.MILLISECONDS;
		case SECONDS:
			return TimeUnit.SECONDS;
		case MINUTES:
			return TimeUnit.MINUTES;
		case HOURS:
			return TimeUnit.HOURS;
		case DAYS:
			return TimeUnit.DAYS;
		default:
			throw new IllegalArgumentException("No TimeUnit equivalent for " + chronoUnit);
		}
	}

	/**
	 * 获取当前UTC时间
	 * 
	 * @return Instant UTC时间
	 */
	public static Instant toInstant() {
		return Instant.now();
	}

	/**
	 * 获取当前UTC时间
	 * 
	 * @param date 指定时间
	 * @return ZonedDateTime 带时区的时间
	 */
	static ZonedDateTime toUtc(Date date) {
		return date.toInstant().atZone(ZONE_UTC);
	}

	/**
	 * 获取当前UTC时间
	 * 
	 * @return LocalDate UTC时间
	 */
	public static LocalDate toUtcDate() {
		return LocalDateTime.now(ZoneOffset.UTC).toLocalDate();
	}

	/**
	 * 获取指定时间的UTC时间
	 * 
	 * @param localDateTime 指定时间
	 * @return LocalDate UTC时间
	 */
	public static LocalDate toUtcDate(LocalDateTime localDateTime) {
		return toUtcDateTime(localDateTime).toLocalDate();
	}

	/**
	 * 将指定时间转换为指定时区时间后再转为UTC时间,注意LocalDateTime不带时区
	 * 
	 * @param localDateTime 指定时间
	 * @param zoneOffset 时区
	 * @return LocalDate UTC时间
	 */
	public static LocalDate toUtcDate(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
		return toUtcDateTime(localDateTime, zoneOffset).toLocalDate();
	}

	/**
	 * 将带时区的指定时间转换为UTC时间
	 * 
	 * @param zonedDateTime 时区时间
	 * @return LocalDate UTC时间
	 */
	public static LocalDate toUtcDate(ZonedDateTime zonedDateTime) {
		return toUtcDateTime(zonedDateTime).toLocalDate();
	}

	/**
	 * 获取当前UTC时间
	 * 
	 * @return LocalDateTime UTC时间
	 */
	public static LocalDateTime toUtcDateTime() {
		return LocalDateTime.now(ZONE_UTC);
	}

	/**
	 * 将指定时间转换本地时区时间后再转为UTC时间,注意LocalDateTime不带时区
	 * 
	 * @param localDateTime 指定时间
	 * @return LocalDateTime UTC时间
	 */
	public static LocalDateTime toUtcDateTime(LocalDateTime localDateTime) {
		return toUtcDateTime(localDateTime, DEFAULT_ZONE);
	}

	/**
	 * 将指定时间转换为指定时区时间后再转为UTC时间,注意LocalDateTime不带时区
	 * 
	 * @param localDateTime 指定时间
	 * @param zoneOffset 时区
	 * @return LocalDateTime UTC时间
	 */
	public static LocalDateTime toUtcDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
		// ZonedDateTime zdt = ZonedDateTime.of(localDateTime, DEFAULT_ZONE);
		// return zdt.withZoneSameInstant(ZONE_UTC).toLocalDateTime();

		return toUtcDateTime(localDateTime.atZone(zoneId));
	}

	/**
	 * 将指定时间转换本地时区时间后再转为UTC时间,注意LocalDateTime不带时区
	 * 
	 * @param localDateTime 指定时间
	 * @return LocalDateTime UTC时间
	 */
	public static LocalDateTime toUtcDateTime(String localDateTime) {
		return toUtcDateTime(parse(localDateTime));
	}

	/**
	 * 将带时区的指定时间转换为UTC时间
	 * 
	 * @param zonedDateTime 时区时间
	 * @return LocalDateTime UTC时间
	 */
	public static LocalDateTime toUtcDateTime(ZonedDateTime zonedDateTime) {
		return LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZONE_UTC);
	}

	/**
	 * 获取当前UTC时间
	 * 
	 * @return LocalTime UTC时间
	 */
	static LocalTime toUtcTime() {
		return LocalDateTime.now(ZoneOffset.UTC).toLocalTime();
	}

	/**
	 * 获取当前UTC时间
	 * 
	 * @param localDateTime 指定时间
	 * @return LocalTime UTC时间
	 */
	static LocalTime toUtcTime(LocalDateTime localDateTime) {
		return toUtcDateTime(localDateTime).toLocalTime();
	}

	/**
	 * 将指定时间转换为指定时区时间后再转为UTC时间,注意LocalDateTime不带时区
	 * 
	 * @param localDateTime 指定时间
	 * @param zoneOffset 时区
	 * @return LocalTime UTC时间
	 */
	static LocalTime toUtcTime(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
		return toUtcDateTime(localDateTime, zoneOffset).toLocalTime();
	}

	/**
	 * 将带时区的指定时间转换为UTC时间
	 * 
	 * @param zonedDateTime 时区时间
	 * @return LocalTime UTC时间
	 */
	static LocalTime toUtcTime(ZonedDateTime zonedDateTime) {
		return toUtcDateTime(zonedDateTime).toLocalTime();
	}

	/**
	 * 时区转换.支持的时区通过ZoneId.getAvailableZoneIds()查看
	 * 
	 * @param zoneId 时区
	 * @return 转换后的时区
	 */
	static ZonedDateTime toZone(String zoneId) {
		return toZone(ZoneId.of(zoneId));
	}

	/**
	 * 时区转换.支持的时区通过ZoneId.getAvailableZoneIds()查看
	 * 
	 * @param zoneId 时区
	 * @return 转换后的时区
	 */
	static ZonedDateTime toZone(ZoneId zoneId) {
		return ZonedDateTime.now(zoneId);
	}

	/**
	 * 将时间在2个时区之间互转
	 * 
	 * @param localDateTime 时间,不带时区
	 * @param sourceZone 来源时区
	 * @param targetZone 目标时区
	 * @return 转换后时间
	 */
	static LocalDateTime toZone(LocalDateTime localDateTime, ZoneId sourceZone, ZoneId targetZone) {
		if (localDateTime == null || sourceZone == null || targetZone == null) {
			return null;
		}
		return ZonedDateTime.of(localDateTime, sourceZone).withZoneSameInstant(targetZone).toLocalDateTime();
	}

	/**
	 * 将UTC时间转换为本地时间
	 * 
	 * @param localDateTime 时间,不带时区
	 * @return 转换后时间
	 */
	static LocalDateTime utc2local(LocalDateTime localDateTime) {
		AssertHelper.notNull(localDateTime);
		return toZone(localDateTime, ZONE_UTC, DEFAULT_ZONE);
	}
}