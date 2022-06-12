package com.wy.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮箱服务器验证,qq邮箱开启验证需要的是开启smtp服务时的加密密钥,其他邮箱是登录密码
 * 
 * @author 飞花梦影
 * @date 2021-04-01 12:19:05
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MailAuthenticator extends Authenticator {

	private String username;

	private String password;

	public MailAuthenticator() {

	}

	public MailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}