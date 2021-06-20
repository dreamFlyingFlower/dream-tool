package com.wy.digest.enums;

/**
 * 加密类型,如AES,DES,RSA等
 * 
 * @auther 飞花梦影
 * @date 2021-06-19 20:32:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum CryptType {

	AES("AES"),
	DES("DES"),
	RSA("RSA");

	private String type;

	private CryptType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}