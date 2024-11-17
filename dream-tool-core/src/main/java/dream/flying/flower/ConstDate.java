package dream.flying.flower;

/**
 * 时间格式化参数
 *
 * @author 飞花梦影
 * @date 2022-11-08 09:53:06
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface ConstDate {

	String DATE = "yyyy-MM-dd";

	String DATE_SLASH = "yyyy/MM/dd";

	String DATE_NONE = "yyyyMMdd";

	String DATEWEEK = "yyyy-MM-dd EEE";

	String DATEWEEK_SLASH = "yyyy/MM/dd EEE";

	String DATEWEEK_NONE = "yyyyMMdd EEE";

	String DATETIME = "yyyy-MM-dd HH:mm:ss";

	String DATETIME_SLASH = "yyyy/MM/dd HH:mm:ss";

	String DATETIME_NONE = "yyyyMMdd HH:mm:ss";

	String DATETIMEWEEK = "yyyy-MM-dd HH:mm:ss EEE";

	String DATETIMEWEEK_SLASH = "yyyy/MM/dd HH:mm:ss EEE";

	String DATETIMEWEEK_NONE = "yyyyMMdd HH:mm:ss EEE";

	String DATEMILSEC = "yyyy-MM-dd HH:mm:ss.SSS";

	String DATEMILSEC_SLASH = "yyyy/MM/dd HH:mm:ss.SSS";

	String DATEMILSEC_NONE = "yyyyMMddHHmmssSSS";

	String DATEMILSECWEEK = "yyyy-MM-dd HH:mm:ss.SSS EEE";

	String DATEMILSECWEEK_SLASH = "yyyy/MM/dd HH:mm:ss.SSS EEE";

	String DATEMILSECWEEK_NONE = "yyyyMMdd HHmmssSSS EEE";

	String DATETIME_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	String DATETIME_ISO_SLASH = "yyyy/MM/dd'T'HH:mm:ss.SSS'Z'";

	String DATETIME_ISO_NONE = "yyyyMMdd'T'HH:mm:ss.SSS'Z'";

	String TIME = "HH:mm:ss";

	String TIMEWEEK = "HH:mm:ss EEE";

	String WEEK = "EEE";

	/** 秒与毫秒换算 */
	int TIME_MILLSECONDS = 1000;

	/** 秒与微秒换算 */
	int TIME_MICROSECONDS = 1000000;

	/** 秒与纳秒换算 */
	int TIME_NANOSECONDS = 1000000000;
}