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
}