package dream.flying.flower.enums;

import dream.flying.flower.ConstI18nFormat;
import dream.flying.flower.common.NumberMsg;

/**
 * 提示或信息类,传入的信息不要带有双引号或单引号
 *
 * @author 飞花梦影
 * @date 2023-02-01 14:03:39
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum TipFormatEnum implements NumberMsg {

	/** 必传参数为空 */
	TIP_PARAM_REQUIRED_IS_NULL(TipEnum.TIP_PARAM_REQUIRED_IS_NULL
			.getValue(), ConstI18nFormat.MSG_FORMAT_PARAM_REQUIRED_IS_NULL),

	/** 请求URL不存在 */
	TIP_REQUEST_URL_NOT_EXIST(TipEnum.TIP_REQUEST_URL_NOT_EXIST
			.getValue(), ConstI18nFormat.MSG_FORMAT_REQUEST_URL_NOT_EXIST),
	/** 请求方式不支持 */
	TIP_REQUEST_HTTP_METHOD_NOT_SUPPORTED(TipEnum.TIP_REQUEST_HTTP_METHOD_NOT_SUPPORTED
			.getValue(), ConstI18nFormat.MSG_FORMAT_REQUEST_HTTP_METHOD_NOT_SUPPORTED),
	/** 请求媒体类型不支持 */
	TIP_REQUEST_MEDIA_TYPE_NOT_SUPPORTED(TipEnum.TIP_REQUEST_MEDIA_TYPE_NOT_SUPPORTED
			.getValue(), ConstI18nFormat.MSG_FORMAT_REQUEST_MEDIA_TYPE_NOT_SUPPORTED);

	private Integer code;

	private String key;

	private TipFormatEnum(Integer code, String key) {
		this.code = code;
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public Integer getValue() {
		return code;
	}

	@Override
	public String getMsg() {
		return key;
	}

	@Override
	public String toString() {
		return super.toString().replaceFirst("TIP", "");
	}
}