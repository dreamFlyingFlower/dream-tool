package dream.flying.flower.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import dream.flying.flower.enums.DateEnum;
import dream.flying.flower.enums.RegexEnum;
import dream.flying.flower.enums.RegionEnum;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * Calender的set方法和add方法都可以重新设定时间,但是set方法设置时间后会重新计算
 * 
 * 可查看commons-lang的DateUtils以及LocalDateUtils FIXME
 *
 * @author 飞花梦影
 * @date 2021-03-14 13:23:41
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class DateHelper {

	public static final long ONE_DAY = 1000 * 60 * 60 * 24;

	public static final String DEFAULT_PATTERN = DateEnum.DATETIME.getPattern();

	/** 比较日期的模式 --只比较日期,不比较时间 */
	public static final int COMP_MODEL_DATE = 1;

	/** 比较日期的模式 --只比较时间,不比较日期 */
	public static final int COMP_MODEL_TIME = 2;

	/** 比较日期的模式 --比较日期,也比较时间 */
	public final static int COMP_MODEL_DATETIME = 3;

	private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {

		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(DEFAULT_PATTERN);
		}
	};

	private DateHelper() {
	}

	/**
	 * 返回指定时间是周几
	 */
	public static String getWeek(Date date) {
		return format(date, DateEnum.WEEK);
	}

	public static String getWeek(long date) {
		return format(new Date(date), DateEnum.WEEK);
	}

	/**
	 * 返回当前时间年月日字符串
	 */
	public static String formatDate() {
		return format(new Date(), DateEnum.DATE);
	}

	/**
	 * 返回指定时间年月日字符串
	 */
	public static String formatDate(long date) {
		return format(new Date(date), DateEnum.DATE);
	}

	/**
	 * 返回指定时间年月日字符串
	 */
	public static String formatDate(Date date) {
		return format(date, DateEnum.DATE);
	}

	/**
	 * 返回当前时间的时分秒
	 */
	public static String formatTime() {
		return format(new Date(), DateEnum.TIME);
	}

	/**
	 * 返回指定时间时分秒字符串
	 */
	public static String formatTime(long date) {
		return format(date, DateEnum.TIME);
	}

	/**
	 * 返回指定时间时分秒字符串
	 */
	public static String formatTime(Date date) {
		return format(date, DateEnum.TIME);
	}

	/**
	 * 返回当前时间的年月日时分秒字符串
	 */
	public static String formatDateTime() {
		return format(new Date(), DEFAULT_PATTERN);
	}

	/**
	 * 返回指定时间转换为年月日时分秒字符串
	 */
	public static String formatDateTime(long date) {
		return format(date, DEFAULT_PATTERN);
	}

	/**
	 * 返回指定时间转换为年月日时分秒字符串
	 */
	public static String formatDateTime(Date date) {
		return format(date, DEFAULT_PATTERN);
	}

	/**
	 * 获取当前时间的指定格式字符串
	 * 
	 * @return 当前时间的指定格式字符串
	 */
	public static String format(DateEnum pattern) {
		return format(new Date(), pattern);
	}

	/**
	 * 返回指定时间转换为年月日时分秒字符串
	 */
	public static String format(long date, DateEnum pattern) {
		return format(date, pattern.getPattern());
	}

	/**
	 * 返回指定时间转换为年月日时分秒字符串
	 */
	public static String format(Date date, DateEnum pattern) {
		return format(date, pattern.getPattern());
	}

	/**
	 * 返回指定时间,指定格式的自定义年月日时分秒字符串
	 */
	public static String format(long date, String pattern) {
		date = date < 0 ? 0 : date;
		return format(new Date(date), pattern);
	}

	/**
	 * 返回指定时间,指定格式的自定义年月日时分秒字符串
	 */
	public static String format(Date date, String pattern) {
		date = date == null ? new Date() : date;
		if (StrHelper.isBlank(pattern)) {
			return threadLocal.get().format(date);
		}
		return get(pattern).format(date);
	}

	private static SimpleDateFormat get(String pattern) {
		SimpleDateFormat sdf = threadLocal.get();
		sdf.applyPattern(pattern);
		return sdf;
	}

	/**
	 * 根据时间格式将时间字符串转换为对应格式,年月日的分隔符只支持/,-和无,若有时分秒,必须是:
	 * 
	 * @param date 需要被转换的时间
	 * @return 转换后的时间
	 */
	public static Date parse(String date) {
		if (StrHelper.checkValidity(date, RegexEnum.REGEX_DATE)) {
			return parse(date, DateEnum.DATE);
		}
		if (StrHelper.checkValidity(date, RegexEnum.REGEX_DATE_NONE)) {
			return parse(date, DateEnum.DATE_NONE);
		}
		if (StrHelper.checkValidity(date, RegexEnum.REGEX_DATE_SLASH)) {
			return parse(date, DateEnum.DATE_SLASH);
		}
		if (StrHelper.checkValidity(date, RegexEnum.REGEX_DATETIME)) {
			return parse(date, DateEnum.DATETIME);
		}
		if (StrHelper.checkValidity(date, RegexEnum.REGEX_DATETIME_NONE)) {
			return parse(date, DateEnum.DATETIME_NONE);
		}
		if (StrHelper.checkValidity(date, RegexEnum.REGEX_DATETIME_SLASH)) {
			return parse(date, DateEnum.DATETIME_SLASH);
		}
		return parse(date, DEFAULT_PATTERN);
	}

	/**
	 * 将时间字符串转换为指定格式的date
	 */
	public static Date parse(String date, DateEnum pattern) {
		return parse(date, pattern.getPattern());
	}

	/**
	 * 将时间字符串转换为指定格式的date
	 */
	public static Date parse(String date, String pattern) {
		try {
			if (StrHelper.isAnyBlank(pattern, date)) {
				return null;
			}
			return get(pattern).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将yyyy-MM-dd'T'HH:mm:ss.SSS'Z'格式的时间转换为UTC
	 * 
	 * @param text 时间字符串,格式支持两种:不包含毫秒值,如"2023-03-03T08:26:15Z";
	 *        支持任意位数的毫秒值:2023-03-03T08:26:15.503162206Z; 转换出来的Date精度到毫秒
	 * @return Date
	 */
	public static Date parseUtc(String text) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (text.contains(".")) {
			String prefix = text.substring(0, text.indexOf("."));
			String suffix = text.substring(text.indexOf("."));
			if (suffix.length() >= 5) {
				suffix = suffix.substring(0, 4) + "Z";
			} else {
				int len = 5 - suffix.length();
				StringBuilder temp = new StringBuilder();
				temp.append(suffix, 0, suffix.length() - 1);
				for (int i = 0; i < len; i++) {
					temp.append("0");
				}
				suffix = temp + "Z";
			}
			text = prefix + suffix;
		} else {
			text = text.substring(0, text.length() - 1) + ".000Z";
		}
		try {
			return sdf.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 当前时间的指定时间位的计算
	 * 
	 * @param field Calendar的日历字段,Calender.MONTH,Calender.DAY等
	 */
	public static final Date dateAdd(int field, int amount) {
		return dateAdd(new Date(), field, amount);
	}

	/**
	 * 指定时间的指定时间位的运算
	 */
	public static final Date dateAdd(Date date, int field, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(field, amount);
		return c.getTime();
	}

	/**
	 * 返回当前时间指定时间之后的字符串值
	 */
	public static final String strAdd(int field, int amount) {
		return formatDateTime(dateAdd(field, amount));
	}

	/**
	 * 给指定的时间字段设置指定的值
	 * 
	 * @param field Calendar的日历字段,Calender.MONTH,Calender.DAY等
	 */
	public static Date dateCustom(Date date, int field, int value) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(field, value);
		return c.getTime();
	}

	/**
	 * 设置一个指定年月日,当前系统时间时分秒的时间
	 */
	public static Date dateCustom(int year, int month, int day) {
		if (month < 1) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, day);
		return c.getTime();
	}

	/**
	 * 设置一个指定年月日时分的时间
	 */
	public static Date dateCustom(int year, int month, int date, int hourOfDay, int minute) {
		return dateCustom(year, month, date, hourOfDay, minute, 0);
	}

	/**
	 * 设置一个指定年月日时分秒的时间
	 */
	public static Date dateCustom(int year, int month, int date, int hourOfDay, int minute, int second) {
		if (month < 1) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, date, hourOfDay, minute, second);
		return c.getTime();
	}

	/**
	 * 判断时间是否在指定时间区间内
	 * 
	 * @param now 目标时间
	 * @param start 时间区间开始
	 * @param end 时间区间结束
	 * @param model 区间模式
	 * @return 是否在区间内
	 */
	public static boolean isBetween(Date now, Date start, Date end, int model) {
		return isBetween(now, start, end, RegionEnum.LEFT_OPEN_RIGHT_OPEN, model);
	}

	/**
	 * 判断时间是否在指定的时间段之类
	 * 
	 * @param date 需要判断的时间
	 * @param start 时间段的起始时间
	 * @param end 时间段的截止时间
	 * @param interModel 区间的模式
	 * @param compModel 比较的模式<br>
	 *        COMP_MODEL_DATE:只比较日期,不比较时间<br>
	 *        COMP_MODEL_TIME:只比较时间,不比较日期<br>
	 *        COMP_MODEL_DATETIME:比较日期,也比较时间
	 * @return
	 */
	public static boolean isBetween(Date date, Date start, Date end, RegionEnum interModel, int compModel) {
		AssertHelper.notNull(date);
		AssertHelper.notNull(start);
		AssertHelper.notNull(end);
		DateEnum dateFormat = null;
		switch (compModel) {
		case COMP_MODEL_DATE: {
			dateFormat = DateEnum.DATE;
			break;
		}
		case COMP_MODEL_TIME: {
			dateFormat = DateEnum.TIME;
			break;
		}
		case COMP_MODEL_DATETIME: {
			dateFormat = DateEnum.DATETIME;
			break;
		}
		default: {
			throw new IllegalArgumentException(String.format("日期的比较模式[%d]有误", compModel));
		}
		}
		long dateNumber = DateTimeHelper.parseDate(DateTimeHelper.format(date, dateFormat)).getTime();
		long startNumber = DateTimeHelper.parseDate(DateTimeHelper.format(start, dateFormat)).getTime();
		long endNumber = DateTimeHelper.parseDate(DateTimeHelper.format(end, dateFormat)).getTime();
		switch (interModel) {
		case LEFT_OPEN_RIGHT_OPEN: {
			if (dateNumber <= startNumber || dateNumber >= endNumber) {
				return false;
			} else {
				return true;
			}
		}
		case LEFT_CLOSE_RIGHT_OPEN: {
			if (dateNumber < startNumber || dateNumber >= endNumber) {
				return false;
			} else {
				return true;
			}
		}
		case LEFT_OPEN_RIGHT_CLOSE: {
			if (dateNumber <= startNumber || dateNumber > endNumber) {
				return false;
			} else {
				return true;
			}
		}
		case LEFT_CLOSE_RIGHT_CLOSE: {
			if (dateNumber < startNumber || dateNumber > endNumber) {
				return false;
			} else {
				return true;
			}
		}
		default: {
			throw new IllegalArgumentException(String.format("日期的区间模式[%d]有误", interModel));
		}
		}
	}

	/**
	 * 判断是否为闰年
	 */
	public static boolean isLeapYear(int yyyy) {
		if ((yyyy % 4 == 0 && yyyy % 100 != 0) || yyyy % 400 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获得当前时间当日开始时间
	 */
	public static long getDayBegin() {
		return getDayBegin(new Date());
	}

	/**
	 * 获得某个时间当日开始时间
	 */
	public static long getDayBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	/**
	 * 获得当前时间当日开始时间
	 */
	public static long getDayEnd() {
		return getDayEnd(new Date());
	}

	/**
	 * 获得某个时间当日开始时间
	 */
	public static long getDayEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTimeInMillis();
	}

	/**
	 * 获得当前时间的昨天开始时间
	 */
	public static Date getYesterdayBegin() {
		return getYesterdayBegin(new Date());
	}

	/**
	 * 获取某个时间昨天的开始时间
	 */
	public static Date getYesterdayBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(getDayBegin(date)));
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	/**
	 * 获得当前时间的昨天结束时间
	 */
	public static Date getYesterdayEnd() {
		return getYesterdayEnd(new Date());
	}

	/**
	 * 获取某个时间昨天的开始时间
	 */
	public static Date getYesterdayEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(getDayEnd(date)));
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.getTime();
	}

	/**
	 * 获得当前明天的开始时间
	 */
	public static Date getTomorrowBegin() {
		return getTomorrowBegin(new Date());
	}

	/**
	 * 获得某个指定时间的明天开始时间
	 */
	public static Date getTomorrowBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(getDayBegin(date)));
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 获得当天时间明天的结束时间
	 */
	public static Date getTomorrowEnd() {
		return getTomorrowEnd(new Date());
	}

	/**
	 * 获得某个指定时间的明天结束时间
	 */
	public static Date getTomorrowEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(getDayEnd(date)));
		c.add(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 获得本周的开始时间
	 */
	public static Date getWeekBegin() {
		return getWeekBegin(new Date());
	}

	/**
	 * 获得某个时间所在周的开始时间
	 */
	public static Date getWeekBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			dayOfWeek += 7;
		}
		c.add(Calendar.DATE, 2 - dayOfWeek);
		return new Date(getDayBegin(c.getTime()));
	}

	/**
	 * 获得本周的结束时间
	 */
	public static Date getWeekEnd() {
		return getWeekEnd(new Date());
	}

	/**
	 * 获得某个时间所在周的结束时间
	 */
	public static Date getWeekEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(getWeekBegin(date));
		c.add(Calendar.DAY_OF_WEEK, 6);
		return new Date(getDayEnd(c.getTime()));
	}

	/**
	 * 获得某个月的最大天数
	 * 
	 * @return 天数
	 */
	public static int getMonthDays(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得当前时间所在月的第一天
	 */
	public static Date getMonthBegin() {
		return getMonthBegin(new Date());
	}

	/**
	 * 获得某个时间所在月的第一天
	 */
	public static Date getMonthBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
		return new Date(getDayBegin(c.getTime()));
	}

	/**
	 * 获得当前时间所在月的最后一天
	 */
	public static Date getMonthEnd() {
		return getMonthEnd(new Date());
	}

	/**
	 * 获得某个时间所在月的最后一天
	 */
	public static Date getMonthEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new Date(getDayEnd(c.getTime()));
	}

	/**
	 * 获得当前时间所在季度的开始时间
	 */
	public static Date getSeasonBegin() {
		return getSeasonBegin(new Date());
	}

	/**
	 * 获得当前时间所在季度的开始时间
	 */
	public static Date getSeasonBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		int season = month / 3;
		c.set(c.get(Calendar.YEAR), season * 3, 1);
		return new Date(getDayBegin(c.getTime()));
	}

	/**
	 * 获得当前时间所在季度的结束时间
	 */
	public static Date getSeasonEnd() {
		return getSeasonEnd(new Date());
	}

	/**
	 * 获得当前时间所在季度的结束时间
	 */
	public static Date getSeasonEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		int season = month / 3;
		c.set(Calendar.MONTH, (season + 1) * 3 - 1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return new Date(getDayEnd(c.getTime()));
	}

	/**
	 * 获得当前时间年份,不会格式化
	 * 
	 * @return 年份
	 */
	public static String getYear() {
		return getYear(new Date());
	}

	/**
	 * 获得指定时间年份,不会格式化
	 * 
	 * @param date 指定时间
	 * @return 年份
	 */
	public static String getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR) + "";
	}

	/**
	 * 获得当前时间月份,格式为2位字符
	 * 
	 * @return 月份
	 */
	public static String getMonth() {
		return getMonth(new Date());
	}

	/**
	 * 获得指定时间月份,格式为2位字符
	 * 
	 * @return 月份
	 */
	public static String getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		return month > 9 ? month + "" : "0" + month;
	}

	/**
	 * 获得当前时间日期,格式为2位字符
	 * 
	 * @return 日期
	 */
	public static String getDay() {
		return getDay(new Date());
	}

	/**
	 * 获得指定时间月份,格式为2位字符
	 * 
	 * @return 日期
	 */
	public static String getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day > 9 ? day + "" : "0" + day;
	}

	/**
	 * 获得当前时间所在年的开始时间
	 */
	public static Date getYearBegin() {
		return getYearBegin(new Date());
	}

	/**
	 * 获得某个时间所在年的开始时间
	 */
	public static Date getYearBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), Calendar.JANUARY, 1);
		return new Date(getDayBegin(c.getTime()));
	}

	/**
	 * 获得当前时间所在年的结束时间
	 */
	public static Date getYearEnd() {
		return getYearEnd(new Date());
	}

	/**
	 * 获得某个时间所在年的结束时间
	 */
	public static Date getYearEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), Calendar.DECEMBER, 31);
		return new Date(getDayEnd(c.getTime()));
	}

	/**
	 * 获得2个时间中大的一个
	 */
	public static Date getMaxDate(Date date1, Date date2) {
		if (date1 == null) {
			return date2;
		}
		if (date2 == null) {
			return date1;
		}
		if (date1.after(date2)) {
			return date1;
		}
		return date2;
	}

	/**
	 * 获得2个时间中小的一个
	 */
	public static Date getMinDate(Date date1, Date date2) {
		if (date1 == null) {
			return date2;
		}
		if (date2 == null) {
			return date1;
		}
		if (date1.after(date2)) {
			return date2;
		}
		return date1;
	}

	/**
	 * 比较2个日期是否为同一天
	 * 
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return true是,false否
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DATE) == c2.get(Calendar.DATE)) {
			return true;
		}
		return false;
	}

	public static long span2Date(String date1Str, String date2Str) {
		Date date1 = DateHelper.parse(date1Str);
		Date date2 = DateHelper.parse(date2Str);
		return date2.getTime() - date1.getTime();
	}

	public static long span2Date(String date1Str, Date date2) {
		Date date1 = DateHelper.parse(date1Str);
		return date2.getTime() - date1.getTime();
	}

	/**
	 * 计算2个时间yyyy-MM-dd HH:mm:ss之间的毫秒数,后减前
	 * 
	 * @param date1 开始时间
	 * @param date2 结束时间
	 * @return 相差毫秒数,可小于0
	 */
	public static long span2Date(Date date1, Date date2) {
		return date2.getTime() - date1.getTime();
	}

	/**
	 * 获得2个yyyy-MM-dd或yyyy-MM-dd HH:mm:ss之间相隔的年数
	 * 
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @return 间隔年数.可小于0
	 */
	public static int span2Year(String begintime, String endtime) {
		Date date1 = parse(begintime, DateEnum.DATE);
		Date date2 = parse(endtime, DateEnum.DATE);
		return span2Year(date1, date2);
	}

	public static int span2Year(Date begintime, Date endtime) {
		Calendar c = Calendar.getInstance();
		c.setTime(begintime);
		int m1 = c.get(Calendar.YEAR);
		c.setTime(endtime);
		int m2 = c.get(Calendar.YEAR);
		return m2 - m1;
	}

	/**
	 * 获得2个yyyy-MM-dd或yyyy-MM-dd HH:mm:ss之间相隔的月数
	 * 
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @return 相隔月数.可小于0
	 */
	public static int span2Month(String begintime, String endtime) {
		Date date1 = parse(begintime, DateEnum.DATE);
		Date date2 = parse(endtime, DateEnum.DATE);
		return span2Month(date1, date2);
	}

	public static int span2Month(Date begintime, Date endtime) {
		Calendar c = Calendar.getInstance();
		c.setTime(begintime);
		int year1 = c.get(Calendar.YEAR);
		int m1 = c.get(Calendar.MONTH);
		c.setTime(endtime);
		int year2 = c.get(Calendar.YEAR);
		int m2 = c.get(Calendar.MONTH);
		return (year2 - year1) * 12 + (m2 - m1);
	}

	/**
	 * 获得2个yyyy-MM-dd或yyyy-MM-dd HH:mm:ss之间相隔的天数
	 * 
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @return 相隔天数.可小于0
	 */
	public static int span2Day(String begintime, String endtime) {
		Date date1 = parse(begintime, DateEnum.DATE);
		Date date2 = parse(endtime, DateEnum.DATE);
		return span2Day(date1, date2);
	}

	public static int span2Day(Date begintime, Date endtime) {
		return (int) ((endtime.getTime() - begintime.getTime()) / ONE_DAY);
	}
}