package com.wy.enums;

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

	/** 响应失败 */
	TIP_RESPONSE_FAIL(0, "msg_fail"),
	/** 响应成功 */
	TIP_RESPONSE_SUCCESS(1, "msg_success"),
	/** 未知错误 */
	TIP_RESPONSE_UNKNOWN(-1, "msg_unknown"),

	/** 登录成功 */
	TIP_LOGIN_SUCCESS_LOGIN(1, "msg_login_success_login"),
	/** 登出成功 */
	TIP_LOGIN_SUCCESS_LOGOUT(1, "msg_login_success_logout"),
	/** 登录失败 */
	TIP_LOGIN_FAIL(1000, "msg_login_fail"),
	/** 登录失败,用户名错误 */
	TIP_LOGIN_FAIL_USERNAME(1001, "msg_login_fail_username"),
	/** 登录失败,密码错误 */
	TIP_LOGIN_FAIL_PASSWORD(1002, "msg_login_fail_password"),
	/** 登录失败,连接超时 */
	TIP_LOGIN_FAIL_TIMEOUT(1003, "msg_login_fail_timeout"),
	/** 用为未登录 */
	TIP_LOGIN_FAIL_NOT_LOGIN(1004, "msg_login_fail_not_login"),
	/** 验证码错误 */
	TIP_LOGIN_CAPTCHA_ERROR(1005, "msg_login_captcha_error"),
	/** 验证码为空 */
	TIP_LOGIN_CAPTCHA_EMPTY(1006, "msg_login_captcha_empty"),
	/** 验证码不存在 */
	TIP_LOGIN_CAPTCHA_NOT_EXIST(1007, "msg_login_captcha_not_exist"),
	/** 验证码不合法 */
	TIP_LOGIN_CAPTCHA_ILLEGAL(1008, "msg_login_captcha_illegal"),

	/** 用户不存在 */
	TIP_USER_NOT_EXIST(1100, "msg_user_not_exist"),
	/** 用户未分配权限 */
	TIP_USER_NOT_DISTRIBUTE_ROLE(1101, "msg_user_not_distribute_role"),
	/** 用户不可用 */
	TIP_USER_NOT_AVALIABLE(1102, "msg_user_not_avaliable"),
	/** 用户为黑名单用户 */
	TIP_USER_IS_BLACK(1103, "msg_user_is_black"),
	/** 用户被锁定 */
	TIP_USER_LOCK(1104, "msg_user_lock"),
	/** 用户原始密码错误 */
	TIP_USER_PWD_ORIGINAL_ERROR(1105, "msg_user_original_pwd_error"),
	/** 用户新密码不能和旧密码相同 */
	TIP_USER_PWD_OLD_NOT_SAME_NEW(1106, "msg_user_pwd_old_not_same_new"),

	/** 权限校验时间戳为null */
	TIP_AUTH_TIME_EMPTY(1200, "msg_timestamp_null"),
	/** 权限校验时间戳错误 */
	TIP_AUTH_TIME_ERROR(1201, "msg_timestamp_error"),
	/** 权限校验信息为null */
	TIP_AUTH_EMPTY(1202, "msg_auth_empty"),
	/** 权限校验失败 */
	TIP_AUTH_FAIL(1203, "msg_auth_fail"),
	/** 权限校验拒绝 */
	TIP_AUTH_DENIED(1204, "msg_auth_denied"),
	/** 权限校验token错误 */
	TIP_AUTH_TOKEN_ERROR(1205, "msg_auth_token_error"),
	/** 权限校验token为空 */
	TIP_AUTH_TOKEN_EMPTY(1206, "msg_auth_token_empty"),
	/** 权限校验token不存在 */
	TIP_AUTH_TOKEN_NOT_EXIST(1207, "msg_auth_token_not_exist"),
	/** 权限校验token不合法 */
	TIP_AUTH_TOKEN_ILLEGAL(1208, "msg_auth_token_illegal"),

	/** 角色错误 */
	TIP_ROLE_ERROR(1300, "msg_role_not_exist"),
	/** 角色不可操作Admin */
	TIP_ROLE_NOT_OPERATE_ADMIN(1301, "msg_role_not_operate_admin"),
	/** 角色权限不足 */
	TIP_ROLE_PERMISSION_DENIED(1302, "msg_role_permission_denied"),
	/** 角色未分配资源 */
	TIP_ROLE_UNASSIGNED_RESOURCE(1303, "msg_role_unassigned_resource"),

	/** 参数错误 */
	TIP_PARAM(1400, "msg_param_error"),
	/** 参数不可为null */
	TIP_PARAM_NOT_NULL(1401, "msg_param_not_null"),
	/** 参数校验错误 */
	TIP_PARAM_INVALID(1402, "msg_param_invalid"),

	/** 数据库错误 */
	TIP_DB_ERROR(1500, "msg_db_error"),
	/** 数据新增失败 */
	TIP_DB_CREATE(1501, "msg_db_create_fail"),
	/** 数据删除失败 */
	TIP_DB_DELETE(1502, "msg_db_delete_fail"),
	/** 数据修改失败 */
	TIP_DB_MODIFY(1503, "msg_db_modify_fail"),
	/** 数据查询失败 */
	TIP_DB_QUERY(1504, "msg_db_query_fail"),
	/** 数据库主键重复 */
	TIP_DB_DUPLICATE_KEY_ERROR(1505, "msg_db_duplicate_key_error"),

	/** 文件不存在 */
	TIP_FILE_NOT_EXIST(1600, "msg_file_not_exist"),
	/** 目录不存在 */
	TIP_FILE_DIRECTORY_NOT_EXIST(1601, "msg_file_directory_not_exist"),
	/** 文件不可为null */
	TIP_FILE_NOT_NULL(1602, "msg_file_not_null"),
	/** 目录不可为null */
	TIP_FILE_DIRECTORY_NOT_NULL(1603, "msg_file_directory_not_null"),
	/** 文件上传失败 */
	TIP_FILE_UPLOAD_FAILED(1604, "msg_file_upload_failed"),
	/** 文件下载失败 */
	TIP_FILE_DOWNLOAD_FAILED(1605, "msg_file_download_failed"),

	/** 发送邮件的用户名不能为空 */
	TIP_MAIL_USERNAME_NULL(1700, "msg_mail_username_null"),
	/** 发送邮件的密码不能为空 */
	TIP_MAIL_PASSWORD_NULL(1701, "msg_mail_password_null"),
	/** 邮件发送人不能为空 */
	TIP_MAIL_FROM_NULL(1702, "msg_mail_from_null"),
	/** 邮件收件人不能为空 */
	TIP_MAIL_TO_NULL(1703, "msg_mail_to_null"),
	/** 邮件内容不能为空 */
	TIP_MAIL_CONTENT_NULL(1704, "msg_mail_content_null"),

	/** 系统错误 */
	TIP_SYS_ERROR(10000, "msg_sys_error"),
	/** 系统维护中 */
	TIP_SYS_MAINTAIN(10001, "msg_sys_maintain"),
	/** 系统繁忙 */
	TIP_SYS_BUSY(10002, "msg_sys_busy");

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