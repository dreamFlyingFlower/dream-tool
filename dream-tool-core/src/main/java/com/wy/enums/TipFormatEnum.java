package com.wy.enums;

import java.text.MessageFormat;

import com.wy.ConstI18nFormat;
import com.wy.common.StatusMsg;

/**
 * 提示或信息类,传入的信息不要带有双引号或单引号
 *
 * @author 飞花梦影
 * @date 2023-02-01 14:03:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum TipFormatEnum implements StatusMsg {

	TIP_LOG_ERROR(0, "@@@:{0}"),
	TIP_LOG_INFO(0, "###:{0}"),

	/** 必传参数为空 */
	TIP_PARAM_REQUIRED_IS_NULL(TipEnum.TIP_PARAM_REQUIRED_IS_NULL
			.getCode(), ConstI18nFormat.MSG_FORMAT_PARAM_REQUIRED_IS_NULL),

	/** 请求URL不存在 */
	TIP_REQUEST_URL_NOT_EXIST(TipEnum.TIP_REQUEST_URL_NOT_EXIST
			.getCode(), ConstI18nFormat.MSG_FORMAT_REQUEST_URL_NOT_EXIST),
	/** 请求方式不支持 */
	TIP_REQUEST_HTTP_METHOD_NOT_SUPPORTED(TipEnum.TIP_REQUEST_HTTP_METHOD_NOT_SUPPORTED
			.getCode(), ConstI18nFormat.MSG_FORMAT_REQUEST_HTTP_METHOD_NOT_SUPPORTED),
	/** 请求媒体类型不支持 */
	TIP_REQUEST_MEDIA_TYPE_NOT_SUPPORTED(TipEnum.TIP_REQUEST_MEDIA_TYPE_NOT_SUPPORTED
			.getCode(), ConstI18nFormat.MSG_FORMAT_REQUEST_MEDIA_TYPE_NOT_SUPPORTED);

	private Integer code;

	private String msg;

	private TipFormatEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return msg;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}

	public String getMsg(Object... msgs) {
		return MessageFormat.format(this.msg, msgs);
	}

	@Override
	public String getMsg(String format, Object... msgs) {
		return MessageFormat.format(this.msg, msgs);
	}
}