package dream.flying.flower.digest.enums;

/**
 * 加密类型,如AES,DES,RSA等
 * 
 * @auther 飞花梦影
 * @date 2021-06-19 20:32:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum CryptType {

	/** 对称加密 */
	AES("AES"),
	DES("DES"),
	/** 非对称加密 */
	DH("DH"),
	RSA("RSA"),

	/** 签名算法类型 */
	MD5_WITH_RSA("MD5withRSA"),
	SHA1_WITH_DSA("SHA1withDSA"),
	SHA1_WITH_RSA("SHA1withRSA"),
	SHA224_WITH_RSA("SHA224withRSA"),
	SHA256_WITH_RSA("SHA256withRSA"),
	SHA384_WITH_RSA("SHA384withRSA"),
	SHA512_WITH_RSA("SHA512withRSA");

	private String type;

	private CryptType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}