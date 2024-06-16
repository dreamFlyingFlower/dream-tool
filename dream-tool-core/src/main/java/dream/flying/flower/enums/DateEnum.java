package dream.flying.flower.enums;

import dream.flying.flower.ConstDate;

/**
 * 时间格式化枚举
 * 
 * @author 飞花梦影
 * @date 2020-11-25 09:26:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum DateEnum {

	DATE(ConstDate.DATE),
	DATE_SLASH(ConstDate.DATE_SLASH),
	DATE_NONE(ConstDate.DATE_NONE),

	DATEWEEK(ConstDate.DATEWEEK),
	DATEWEEK_SLASH(ConstDate.DATEWEEK_SLASH),
	DATEWEEK_NONE(ConstDate.DATEWEEK_NONE),

	DATETIME(ConstDate.DATETIME),
	DATETIME_SLASH(ConstDate.DATETIME_SLASH),
	DATETIME_NONE(ConstDate.DATETIME_NONE),

	DATETIMEWEEK(ConstDate.DATETIMEWEEK),
	DATETIMEWEEK_SLASH(ConstDate.DATETIMEWEEK_SLASH),
	DATETIMEWEEK_NONE(ConstDate.DATETIMEWEEK_NONE),

	DATEMILSEC(ConstDate.DATEMILSEC),
	DATEMILSEC_SLASH(ConstDate.DATEMILSEC_SLASH),
	DATEMILSEC_NONE(ConstDate.DATEMILSEC_NONE),

	DATEMILSECWEEK(ConstDate.DATEMILSECWEEK),
	DATEMILSECWEEK_SLASH(ConstDate.DATEMILSECWEEK_SLASH),
	DATEMILSECWEEK_NONE(ConstDate.DATEMILSECWEEK_NONE),

	TIME(ConstDate.TIME),
	TIMEWEEK(ConstDate.TIMEWEEK),

	WEEK(ConstDate.WEEK);

	private String pattern;

	DateEnum(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}
}