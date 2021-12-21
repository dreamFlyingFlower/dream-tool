package com.wy.util;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import com.wy.enums.DateEnum;
import com.wy.lang.AssertTool;

/**
 * {@link LocalDateTime}等工具类,jdk1.8的时间类工具类,和DateTool差不多
 * 
 * @author ParadiseWY
 * @date 2019-03-20 16:56:06
 * @git {@link https://github.com/mygodness100}
 */
public class DateTimeTool {

	/**
	 * 默认格式化时间字符串HH:mm:ss,系统自带的{@link DateTimeFormatter#ISO_LOCAL_TIME}默认字符串带毫秒
	 */
	private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendValue(HOUR_OF_DAY, 2).appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).optionalStart()
			.appendLiteral(':').appendValue(SECOND_OF_MINUTE, 2).optionalStart().parseStrict().toFormatter();

	/**
	 * 默认格式化年月日时分秒字符串yyyy-MM-dd HH:mm:ss,系统自带的默认格式化字符串中间带T
	 */
	private static final DateTimeFormatter DEFAULT_FORMATTER =
			new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ISO_LOCAL_DATE)
					.appendLiteral(' ').append(DEFAULT_TIME_FORMATTER).optionalStart().parseStrict().toFormatter();

	/**
	 * 定时时间格式化字符串:yyyy-MM-dd HH:mm:ss,等同于DEFAULT_FORMATTER
	 */
	public static final DateTimeFormatter FORMATTER_DATETIME = new DateTimeFormatterBuilder()
			.appendValue(ChronoField.YEAR).appendLiteral("-").appendValue(ChronoField.MONTH_OF_YEAR).appendLiteral("-")
			.appendValue(ChronoField.DAY_OF_MONTH).appendLiteral(" ").appendValue(ChronoField.HOUR_OF_DAY)
			.appendLiteral(":").appendValue(ChronoField.MINUTE_OF_HOUR).appendLiteral(":")
			.appendValue(ChronoField.SECOND_OF_MINUTE).toFormatter();

	/**
	 * 将Date类型转换为LocalDateTime类型
	 * 
	 * @param date 需要进行转换的时间
	 * @return 转换后的时间
	 */
	public static LocalDateTime date2Local(Date date) {
		AssertTool.notNull(date);
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	/**
	 * 将LocalDateTime转换为HH:mm:ss格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime() {
		return DEFAULT_TIME_FORMATTER.format(LocalDateTime.now());
	}

	/**
	 * 将LocalDateTime转换为HH:mm:ss格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime(Date date) {
		return DEFAULT_TIME_FORMATTER.format(date2Local(date));
	}

	/**
	 * 将LocalTime转换为HH:mm:ss格式
	 * 
	 * @param localTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime(LocalTime localTime) {
		return DEFAULT_TIME_FORMATTER.format(localTime);
	}

	/**
	 * 将LocalDateTime转换为HH:mm:ss格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatTime(LocalDateTime localDateTime) {
		return DEFAULT_TIME_FORMATTER.format(localDateTime);
	}

	/**
	 * 将当前时间转换为yyyy-MM-dd格式
	 * 
	 * @return 时间字符串
	 */
	public static String formatDate() {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.now());
	}

	/**
	 * 将Date转换为yyyy-MM-dd格式
	 * 
	 * @param date 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatDate(Date date) {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(date2Local(date));
	}

	/**
	 * 将LocalDate转换为yyyy-MM-dd格式
	 * 
	 * @param localDate 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatDate(LocalDate localDate) {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(localDate);
	}

	/**
	 * 将LocalDateTime转换为yyyy-MM-dd格式
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 时间字符串
	 */
	public static String formatDate(LocalDateTime localDateTime) {
		return DateTimeFormatter.ISO_LOCAL_DATE.format(localDateTime);
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
		return formatDateTime(date2Local(date));
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
	 * 将Date转换为指定格式
	 * 
	 * @param date 需要转换的时间
	 * @param pattern 需要转换的格式
	 * @return 时间字符串
	 */
	public static String formatDateTime(Date date, DateEnum pattern) {
		return format(date, pattern.getPattern());
	}

	/**
	 * 将Date转换为指定格式
	 * 
	 * @param date 需要转换的时间
	 * @param pattern 需要转换的格式
	 * @return 时间字符串
	 */
	public static String formatDateTime(Date date, String pattern) {
		return format(date, pattern);
	}

	/**
	 * 将LocalDateTime转换为指定格式
	 * 
	 * @param localDateTime 需要转换的时间
	 * @param pattern 需要转换的格式
	 * @return 时间字符串
	 */
	public static String formatDateTime(LocalDateTime localDateTime, DateEnum pattern) {
		return format(localDateTime, pattern.getPattern());
	}

	/**
	 * 将LocalDateTime转换为指定格式
	 * 
	 * @param localDateTime 需要转换的时间
	 * @param pattern 需要转换的格式
	 * @return 时间字符串
	 */
	public static String formatDateTime(LocalDateTime localDateTime, String pattern) {
		return format(localDateTime, pattern);
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
	 * @param pattern 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(Date date, String pattern) {
		return format(date, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 将Date转换成指定格式字符串
	 * 
	 * @param date 时间
	 * @param dateTimeFormatter 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(Date date, DateTimeFormatter dateTimeFormatter) {
		return format(date2Local(date), dateTimeFormatter);
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
	 * @param pattern 格式化样式
	 * @return 格式化后字符串
	 */
	public static String format(LocalDateTime localDateTime, String pattern) {
		return format(localDateTime, DateTimeFormatter.ofPattern(pattern));
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
	 * 获得当前时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @return 当前时间所在日期零点时间
	 */
	public static Date getDayBegin() {
		return local2Date(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
	}

	/**
	 * 获得指定时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期零点时间
	 */
	public static Date getDayBegin(Date date) {
		LocalDateTime date2Local = date2Local(date);
		return local2Date(LocalDateTime.of(date2Local.toLocalDate(), LocalTime.MIN));
	}

	/**
	 * 获得指定时间所在日期的零点时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 00:00:00
	 * 
	 * @param localDateTime 指定时间
	 * @return 当前时间所在日期零点时间
	 */
	public static Date getDayBegin(LocalDateTime localDateTime) {
		return local2Date(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
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
		LocalDateTime date2Local = date2Local(date);
		LocalDateTime localDateTime = LocalDateTime.of(date2Local.toLocalDate(), LocalTime.MIN);
		return DEFAULT_FORMATTER.format(localDateTime);
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
	 * 获得当前时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @return 当前时间所在日期结束时间
	 */
	public static Date getDayEnd() {
		return local2Date(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期结束时间
	 */
	public static Date getDayEnd(Date date) {
		LocalDateTime date2Local = date2Local(date);
		return local2Date(LocalDateTime.of(date2Local.toLocalDate(), LocalTime.MAX));
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期结束时间
	 */
	public static Date getDayEnd(LocalDateTime localDateTime) {
		return local2Date(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX));
	}

	/**
	 * 获得当前时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @return 当前时间所在日期结束时间字符串
	 */
	public static String getDayEndStr() {
		LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
		return localDateTime.format(DEFAULT_FORMATTER);
	}

	/**
	 * 获得指定时间所在日期的结束时间,格式为yyyy-MM-dd HH:mm:ss,如2020-12-12 23:59:59
	 * 
	 * @param date 指定时间
	 * @return 当前时间所在日期结束时间字符串
	 */
	public static String getDayEndStr(Date date) {
		LocalDateTime date2Local = date2Local(date);
		LocalDateTime localDateTime = LocalDateTime.of(date2Local.toLocalDate(), LocalTime.MAX);
		return localDateTime.format(DEFAULT_FORMATTER);
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
	 * 获得当前时间所在月份的第一天时间,注意,只有年月日是第一天的,时分秒同当前时间
	 * 
	 * @return 当前时间所在月第一天的时间
	 */
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
	 * @param localDateTime 指定时间
	 * @return 指定时间所在月最后一天的时间
	 */
	public static LocalDateTime getLastDayOfMonth(LocalDateTime localDateTime) {
		return localDateTime.with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 判断date1是否在当前时间之后,即date1大于等于当前时间
	 * 
	 * @param date1 时间1
	 * @return true->date1在当前时间之后或date1和当前时间相同,false->date1在当前时间之前
	 */
	public static boolean isAfter(Date date1) {
		AssertTool.notNull(date1);
		LocalDateTime now = LocalDateTime.now();
		return date2Local(date1).isAfter(now) || date2Local(date1).isEqual(now);
	}

	/**
	 * 判断date1是否在date2之后,即date1大于等于date2
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true->date1在date2之后或date1等于date2,false->date1在date2之前
	 */
	public static boolean isAfter(Date date1, Date date2) {
		AssertTool.notNull(date1);
		AssertTool.notNull(date2);
		return date2Local(date1).isAfter(date2Local(date2)) || date2Local(date1).isEqual(date2Local(date2));
	}

	/**
	 * 判断date1是否在当前时间之前,即date1小于等于当前时间
	 * 
	 * @param date1 时间1
	 * @return true->date1在当前时间之前或date1等于当前时间,false->date1在当前时间之后
	 */
	public static boolean isBefore(Date date1) {
		AssertTool.notNull(date1);
		LocalDateTime now = LocalDateTime.now();
		return date2Local(date1).isBefore(now) || date2Local(date1).isEqual(now);
	}

	/**
	 * 判断date1是否在date2之前,即date1小于等于date2
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true->date1在date2之前或date1等于date2,false->date1在date2之后
	 */
	public static boolean isBefore(Date date1, Date date2) {
		AssertTool.notNull(date1);
		AssertTool.notNull(date2);
		return date2Local(date1).isBefore(date2Local(date2)) || date2Local(date1).isEqual(date2Local(date2));
	}

	/**
	 * 将LocalDateTime类型转换为Date类型
	 * 
	 * @param localDateTime 需要进行转换的时间
	 * @return 转换后的时间
	 */
	public static Date local2Date(LocalDateTime localDateTime) {
		AssertTool.notNull(localDateTime);
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * 将格式为yyyy-MM-dd的时间字符串转为Date类型,时分秒都为0
	 * 
	 * @param date 时间字符串
	 * @return date对象
	 */
	public static Date parseDate(String date) {
		return local2Date(parse(date + " 00:00:00"));
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss或yyyy-MM-ddTHH:mm:ss时间格式为Date类型
	 * 
	 * @param dateTime 时间字符串,若不符合指定格式将报错
	 * @return date对象
	 */
	public static Date parseDateTime(String dateTime) {
		return local2Date(parse(dateTime));
	}

	/**
	 * 将指定时间字符串格式化格式化
	 * 
	 * @param dateTime 时间字符串,若不符合指定格式将报错
	 * @param pattern 字符串时间的格式
	 * @return date对象
	 */
	public static Date parseDateTime(String dateTime, DateEnum pattern) {
		return local2Date(parse(dateTime, pattern.getPattern()));
	}

	/**
	 * 将指定时间字符串格式化格式化
	 * 
	 * @param dateTime 时间字符串,若不符合指定格式将报错
	 * @param pattern 字符串时间的格式
	 * @return date对象
	 */
	public static Date parseDateTime(String dateTime, String pattern) {
		return local2Date(parse(dateTime, pattern));
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
	public static Date plusDate(Date date, long amount, TemporalUnit temporalUnit) {
		return plusDate(date2Local(date), amount, temporalUnit);
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
		AssertTool.notNull(localDateTime);
		AssertTool.notNull(temporalUnit);
		LocalDateTime newDateTime = localDateTime.plus(amount, temporalUnit);
		return local2Date(newDateTime);
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
	 * 在指定时间上加上或减去指定时间,可以是年月日,时分秒任意
	 * 
	 * @param date 指定时间
	 * @param amount 添加的数量,可以为负数
	 * @param temporalUnit 单位,使用 @see ChronoUnit 枚举
	 * @return 修改后的时间
	 */
	public static LocalDateTime plusDateTime(Date date, long amount, TemporalUnit temporalUnit) {
		return plusDateTime(date2Local(date), amount, temporalUnit);
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
		AssertTool.notNull(localDateTime);
		AssertTool.notNull(temporalUnit);
		return localDateTime.plus(amount, temporalUnit);
	}

	/**
	 * 在当前时间基础上增加或减少amount天,若超过当前最大天数,会自动转到相应月数
	 * 
	 * @param amount 增加或较少的天数
	 * @return 新的时间
	 */
	public static Date plusDay(long amount) {
		return local2Date(date2Local(new Date()).plusDays(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount天,若超过当前最大天数,会自动转到相应月数
	 * 
	 * @param amount 增加或较少的天数
	 * @return 新的时间
	 */
	public static Date plusDay(Date date1, long amount) {
		return local2Date(date2Local(date1).plusDays(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount小时,若超过24小时,会自动转到相应天数
	 * 
	 * @param amount 增加或较少的小时
	 * @return 新的时间
	 */
	public static Date plusHour(long amount) {
		return local2Date(date2Local(new Date()).plusHours(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount小时,若超过24小时,会自动转到相应天数
	 * 
	 * @param amount 增加或较少的小时
	 * @return 新的时间
	 */
	public static Date plusHour(Date date1, long amount) {
		return local2Date(date2Local(date1).plusHours(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount个月,若超过12个月,会自动转到相应年
	 * 
	 * @param amount 增加或较少的月数
	 * @return 新的时间
	 */
	public static Date plusMonth(long amount) {
		return local2Date(date2Local(new Date()).plusMonths(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount个月,若超过12个月,会自动转到相应年
	 * 
	 * @param amount 增加或较少的月数
	 * @return 新的时间
	 */
	public static Date plusMonth(Date date1, long amount) {
		return local2Date(date2Local(date1).plusMonths(amount));
	}

	/**
	 * 在当前时间基础上增加或减少amount个周
	 * 
	 * @param amount 增加或较少的周数
	 * @return 新的时间
	 */
	public static Date plusWeek(long amount) {
		return local2Date(date2Local(new Date()).plusWeeks(amount));
	}

	/**
	 * 在指定时间基础上增加或减少amount个周
	 * 
	 * @param amount 增加或较少的周数
	 * @return 新的时间
	 */
	public static Date plusWeek(Date date1, long amount) {
		return local2Date(date2Local(date1).plusWeeks(amount));
	}

	/**
	 * 判断指定时间与当前时间是否同月,但可能不同年,不同日
	 * 
	 * @param date 指定时间
	 * @return true是,false否
	 */
	public static boolean sameMonth(Date date) {
		return date2Local(date).get(ChronoField.MONTH_OF_YEAR) == LocalDate.now().get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断指定时间与当前时间是否同月,但可能不同年,不同日
	 * 
	 * @param date 指定时间
	 * @return true是,false否
	 */
	public static boolean sameMonth(Date date1, Date date2) {
		return date2Local(date1).get(ChronoField.MONTH_OF_YEAR) == date2Local(date2).get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断2个时间否同月同日
	 * 
	 * @param temporalAccessor1 时间1
	 * @param temporalAccessor2 时间2
	 * @return true是,false否
	 */
	public static boolean sameMonth(TemporalAccessor temporalAccessor1, TemporalAccessor temporalAccessor2) {
		return (null == temporalAccessor1 || null == temporalAccessor2) ? false
				: temporalAccessor1.get(ChronoField.MONTH_OF_YEAR) == temporalAccessor2.get(ChronoField.MONTH_OF_YEAR);
	}

	/**
	 * 判断指定时间与当前时间是否同月同日
	 * 
	 * @param date 指定时间
	 * @return true是,false否
	 */
	public static boolean sameMonthDay(Date date) {
		return date2Local(date).get(ChronoField.MONTH_OF_YEAR) == LocalDate.now().get(ChronoField.MONTH_OF_YEAR)
				&& date2Local(date).get(ChronoField.DAY_OF_MONTH) == LocalDate.now().get(ChronoField.DAY_OF_MONTH);
	}

	/**
	 * 判断2个时间否同月同日
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true是,false否
	 */
	public static boolean sameMonthDay(Date date1, Date date2) {
		return date2Local(date1).get(ChronoField.MONTH_OF_YEAR) == date2Local(date2).get(ChronoField.MONTH_OF_YEAR)
				&& date2Local(date1).get(ChronoField.DAY_OF_MONTH) == date2Local(date2).get(ChronoField.DAY_OF_MONTH);
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
		return (null == temporalAccessor1 || null == temporalAccessor2) ? false
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
		return date2Local(date1).get(ChronoField.YEAR) == date2Local(date2).get(ChronoField.YEAR);
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
				&& date2Local(date1).get(ChronoField.MONTH_OF_YEAR) == date2Local(date2).get(ChronoField.MONTH_OF_YEAR);
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
				&& date2Local(date1).get(ChronoField.DAY_OF_MONTH) == date2Local(date2).get(ChronoField.DAY_OF_MONTH);
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
	 * 获得当前时间和指定时间之间相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date 指定时间,可以是未来的,也可以是过去的.
	 * @return 天数相差数
	 */
	public static int spanDay(Date date) {
		return Period.between(LocalDate.now(), date2Local(date).toLocalDate()).getDays();
	}

	/**
	 * 获得date1和date2相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 天数相差数
	 */
	public static int spanDay(Date date1, Date date2) {
		return Period.between(date2Local(date1).toLocalDate(), date2Local(date2).toLocalDate()).getDays();
	}

	/**
	 * 获得当前时间和指定时间之间相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date 指定时间,可以是未来的,也可以是过去的
	 * @return 天数相差数
	 */
	public static int spanDay(LocalDate date) {
		return Period.between(LocalDate.now(), date).getDays();
	}

	/**
	 * 获得date1和date2相差的天数{@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 天数相差数
	 */
	public static int spanDay(LocalDate date1, LocalDate date2) {
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
		return Period.between(LocalDate.now(), date2Local(date).toLocalDate()).getMonths();
	}

	/**
	 * 获得时间1和时间2之间相差的月数 {@link #spanMonth(LocalDate, LocalDate)}
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return 月份相差数
	 */
	public static int spanMonth(Date date1, Date date2) {
		return Period.between(date2Local(date1).toLocalDate(), date2Local(date2).toLocalDate()).getMonths();
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
	 * 获得date1到date2相差的月数,年份,月份,天数都需要根据情况比较,情况复杂,慎用
	 * 
	 * <pre>
	 * date2的年月大于date1,得到的是正数,当date2的年月小于date1时,取大于时的负数即可
	 * spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 1))		=0
	 * spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 3, 5))		=0
	 * spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 5, 5))		=2
	 * 	spanMonth(LocalDate.of(2021, 3, 3), LocalDate.of(2021, 5, 1))		=1
	 * 	spanMonth(LocalDate.of(2000, 7, 5), LocalDate.of(2021, 5, 8))	=10
	 * 	spanMonth(LocalDate.of(2000, 7, 5), LocalDate.of(2021, 5, 1))		=9
	 * 	spanMonth(LocalDate.of(2000, 3, 5), LocalDate.of(2021, 5, 5))	=2
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
		return LocalDate.now().get(ChronoField.YEAR) - date2Local(date1).get(ChronoField.YEAR);
	}

	/**
	 * 获得指定时间到当前时间相差的年份
	 * 
	 * @param date1 指定时间
	 * @param date2 时间2
	 * @return 年份相差数.大于0表示date2大于date1
	 */
	public static int spanYear(Date date1, Date date2) {
		return date2Local(date2).get(ChronoField.YEAR) - date2Local(date1).get(ChronoField.YEAR);
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
}