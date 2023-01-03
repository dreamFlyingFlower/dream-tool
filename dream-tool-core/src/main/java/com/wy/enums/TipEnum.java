package com.wy.enums;

import com.wy.ConstI18n;
import com.wy.common.Internation;
import com.wy.common.StatusMsg;

/**
 * 各种提示信息
 * 
 * @author 飞花梦影
 * @date 2021-03-11 09:51:42
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum TipEnum implements StatusMsg {

	/** 请求失败 */
	TIP_REQUEST_FAIL(0, ConstI18n.MSG_FAIL),
	/** 请求成功 */
	TIP_REQUEST_SUCCESS(1, ConstI18n.MSG_SUCCESS),

	/** 登录成功 */
	TIP_LOGIN_SUCCESS_LOGIN(1, ConstI18n.MSG_LOGIN_SUCCESS_LOGIN),
	/** 登出成功 */
	TIP_LOGIN_SUCCESS_LOGOUT(1, ConstI18n.MSG_LOGIN_SUCCESS_LOGOUT),
	/** 登录失败 */
	TIP_LOGIN_FAIL(1000, ConstI18n.MSG_LOGIN_FAIL),
	/** 登录失败,用户名错误 */
	TIP_LOGIN_FAIL_USERNAME(1001, ConstI18n.MSG_LOGIN_FAIL_USERNAME),
	/** 登录失败,密码错误 */
	TIP_LOGIN_FAIL_PASSWORD(1002, ConstI18n.MSG_LOGIN_FAIL_PASSWORD),
	/** 登录失败,连接超时 */
	TIP_LOGIN_FAIL_TIMEOUT(1003, ConstI18n.MSG_LOGIN_FAIL_TIMEOUT),
	/** 用为未登录 */
	TIP_LOGIN_FAIL_NOT_LOGIN(1004, ConstI18n.MSG_LOGIN_FAIL_NOT_LOGIN),
	/** 验证码错误 */
	TIP_LOGIN_CAPTCHA_ERROR(1005, ConstI18n.MSG_LOGIN_CAPTCHA_ERROR),
	/** 验证码为空 */
	TIP_LOGIN_CAPTCHA_EMPTY(1006, ConstI18n.MSG_LOGIN_CAPTCHA_EMPTY),
	/** 验证码不存在 */
	TIP_LOGIN_CAPTCHA_NOT_EXIST(1007, ConstI18n.MSG_LOGIN_CAPTCHA_NOT_EXIST),
	/** 验证码不合法 */
	TIP_LOGIN_CAPTCHA_ILLEGAL(1008, ConstI18n.MSG_LOGIN_CAPTCHA_ILLEGAL),

	/** 用户不存在 */
	TIP_USER_NOT_EXIST(1100, ConstI18n.MSG_USER_NOT_EXIST),
	/** 用户未分配权限 */
	TIP_USER_NOT_DISTRIBUTE_ROLE(1101, ConstI18n.MSG_USER_NOT_DISTRIBUTE_ROLE),
	/** 用户不可用 */
	TIP_USER_NOT_AVALIABLE(1102, ConstI18n.MSG_USER_NOT_AVALIABLE),
	/** 用户为黑名单用户 */
	TIP_USER_IS_BLACK(1103, ConstI18n.MSG_USER_IS_BLACK),
	/** 用户被锁定 */
	TIP_USER_LOCK(1104, ConstI18n.MSG_USER_LOCK),
	/** 用户原始密码错误 */
	TIP_USER_PWD_ORIGINAL_ERROR(1105, ConstI18n.MSG_USER_PWD_ORIGINAL_ERROR),
	/** 用户新密码不能和旧密码相同 */
	TIP_USER_PWD_OLD_NOT_SAME_NEW(1106, ConstI18n.MSG_USER_PWD_OLD_NOT_SAME_NEW),

	/** 权限校验时间戳为null */
	TIP_AUTH_TIME_EMPTY(1200, ConstI18n.MSG_TIMESTAMP_NULL),
	/** 权限校验时间戳错误 */
	TIP_AUTH_TIME_ERROR(1201, ConstI18n.MSG_TIMESTAMP_ERROR),
	/** 权限校验信息为null */
	TIP_AUTH_EMPTY(1202, ConstI18n.MSG_AUTH_EMPTY),
	/** 权限校验失败 */
	TIP_AUTH_FAIL(1203, ConstI18n.MSG_AUTH_FAIL),
	/** 权限校验拒绝 */
	TIP_AUTH_DENIED(1204, ConstI18n.MSG_AUTH_DENIED),
	/** 权限校验token错误 */
	TIP_AUTH_TOKEN_ERROR(1205, ConstI18n.MSG_AUTH_TOKEN_ERROR),
	/** 权限校验token为空 */
	TIP_AUTH_TOKEN_EMPTY(1206, ConstI18n.MSG_AUTH_TOKEN_EMPTY),
	/** 权限校验token不存在 */
	TIP_AUTH_TOKEN_NOT_EXIST(1207, ConstI18n.MSG_AUTH_TOKEN_NOT_EXIST),
	/** 权限校验token不合法 */
	TIP_AUTH_TOKEN_ILLEGAL(1208, ConstI18n.MSG_AUTH_TOKEN_ILLEGAL),

	/** 角色错误 */
	TIP_ROLE_ERROR(1300, ConstI18n.MSG_ROLE_NOT_EXIST),
	/** 角色不可操作Admin */
	TIP_ROLE_NOT_OPERATE_ADMIN(1301, ConstI18n.MSG_ROLE_NOT_OPERATE_ADMIN),
	/** 角色权限不足 */
	TIP_ROLE_PERMISSION_DENIED(1302, ConstI18n.MSG_ROLE_PERMISSION_DENIED),
	/** 角色未分配资源 */
	TIP_ROLE_UNASSIGNED_RESOURCE(1303, ConstI18n.MSG_ROLE_UNASSIGNED_RESOURCE),

	/** 参数错误 */
	TIP_PARAM(1400, ConstI18n.MSG_PARAM_ERROR),
	/** 参数不可为null */
	TIP_PARAM_NOT_NULL(1401, ConstI18n.MSG_PARAM_NOT_NULL),
	/** 参数校验错误 */
	TIP_PARAM_INVALID(1402, ConstI18n.MSG_PARAM_INVALID),
	/** 必传参数为空 */
	TIP_PARAM_REQUIRED_IS_NULL(1403, ConstI18n.MSG_PARAM_REQUIRED_IS_NULL),

	/** 数据库错误 */
	TIP_DB_ERROR(1500, ConstI18n.MSG_DB_ERROR),
	/** 数据新增失败 */
	TIP_DB_CREATE(1501, ConstI18n.MSG_DB_CREATE_FAIL),
	/** 数据删除失败 */
	TIP_DB_DELETE(1502, ConstI18n.MSG_DB_DELETE_FAIL),
	/** 数据修改失败 */
	TIP_DB_MODIFY(1503, ConstI18n.MSG_DB_MODIFY_FAIL),
	/** 数据查询失败 */
	TIP_DB_QUERY(1504, ConstI18n.MSG_DB_QUERY_FAIL),
	/** 数据库主键重复 */
	TIP_DB_DUPLICATE_KEY_ERROR(1505, ConstI18n.MSG_DB_DUPLICATE_KEY_ERROR),

	/** 文件不存在 */
	TIP_FILE_NOT_EXIST(1600, ConstI18n.MSG_FILE_NOT_EXIST),
	/** 目录不存在 */
	TIP_FILE_DIRECTORY_NOT_EXIST(1601, ConstI18n.MSG_FILE_DIRECTORY_NOT_EXIST),
	/** 文件不可为null */
	TIP_FILE_NOT_NULL(1602, ConstI18n.MSG_FILE_NOT_NULL),
	/** 目录不可为null */
	TIP_FILE_DIRECTORY_NOT_NULL(1603, ConstI18n.MSG_FILE_DIRECTORY_NOT_NULL),
	/** 文件上传失败 */
	TIP_FILE_UPLOAD_FAILED(1604, ConstI18n.MSG_FILE_UPLOAD_FAILED),
	/** 文件下载失败 */
	TIP_FILE_DOWNLOAD_FAILED(1605, ConstI18n.MSG_FILE_DOWNLOAD_FAILED),

	/** 发送邮件的用户名不能为空 */
	TIP_MAIL_USERNAME_NULL(1700, ConstI18n.MSG_MAIL_USERNAME_NULL),
	/** 发送邮件的密码不能为空 */
	TIP_MAIL_PASSWORD_NULL(1701, ConstI18n.MSG_MAIL_PASSWORD_NULL),
	/** 邮件发送人不能为空 */
	TIP_MAIL_FROM_NULL(1702, ConstI18n.MSG_MAIL_FROM_NULL),
	/** 邮件收件人不能为空 */
	TIP_MAIL_TO_NULL(1703, ConstI18n.MSG_MAIL_TO_NULL),
	/** 邮件内容不能为空 */
	TIP_MAIL_CONTENT_NULL(1704, ConstI18n.MSG_MAIL_CONTENT_NULL),

	/** 系统错误 */
	TIP_SYS_ERROR(10000, ConstI18n.MSG_SYS_ERROR),
	/** 系统维护中 */
	TIP_SYS_MAINTAIN(10001, ConstI18n.MSG_SYS_MAINTAIN),
	/** 系统繁忙 */
	TIP_SYS_BUSY(10002, ConstI18n.MSG_SYS_BUSY),

	/** 请求URL不存在 */
	TIP_REQUEST_URL_NOT_EXIST(11000, ConstI18n.MSG_REQUEST_URL_NOT_EXIST),
	/** 请求方式不支持 */
	TIP_REQUEST_HTTP_METHOD_NOT_SUPPORTED(11001, ConstI18n.MSG_REQUEST_HTTP_METHOD_NOT_SUPPORTED),
	/** 请求媒体类型不支持 */
	TIP_REQUEST_MEDIA_TYPE_NOT_SUPPORTED(11002, ConstI18n.MSG_REQUEST_MEDIA_TYPE_NOT_SUPPORTED),
	/** 请求超时 */
	TIP_REQUEST_TIME_OUT(11003, ConstI18n.MSG_REQUEST_TIME_OUT);

	private Integer code;

	private String key;

	TipEnum(Integer code, String key) {
		this.code = code;
		this.key = key;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return Internation.getStr(key);
	}
}