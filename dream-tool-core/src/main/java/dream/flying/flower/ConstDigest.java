package dream.flying.flower;

/**
 * 加密相关
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:00:55
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstDigest {

	/** RSA最大加密明文大小 */
	int MAX_ENCRYPT_BLOCK = 117;

	/** RSA最大解密密文大小 */
	int MAX_DECRYPT_BLOCK = 128;

	/** RSA密钥长度 */
	int KEY_SIZE_1024 = 1024;

	/** RSA密钥长度 */
	int KEY_SIZE_2048 = 2048;

	/** 生成PEM格式时每行字符串个数 */
	int PEM_LINE_SIZE = 64;

	String PUBLIC_KEY = "publicKey";

	String PRIVATE_KEY = "privateKey";

	/**
	 * Java密钥,密钥库
	 */
	String KEY_STORE = "JKS";

	String X509 = "X.509";
}