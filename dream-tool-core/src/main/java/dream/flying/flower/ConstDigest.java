package dream.flying.flower;

import dream.flying.flower.digest.enums.CipherType;
import dream.flying.flower.digest.enums.CryptType;

/**
 * 加密相关
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:00:55
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstDigest {

	/** 密钥倍数 */
	int KEY_SIZE_MULTIPLE = 64;

	/** 密钥最小长度 */
	int KEY_SIZE_MIN = 512;

	/** 密钥最大长度 */
	int KEY_SIZE_MAX = 65535;

	/** 密钥长度 */
	int KEY_SIZE_256 = 256;

	/** 密钥长度 */
	int KEY_SIZE_1024 = 1024;

	/** 密钥长度 */
	int KEY_SIZE_2048 = 2048;

	/** 生成PEM格式时每行字符串个数 */
	int PEM_LINE_SIZE = 64;

	/** Java密钥,密钥库 */
	String KEY_STORE = "JKS";

	String X509 = "X.509";

	/** RSA默认加解密模式以及填充模式 */
	CipherType DEFAULT_CIPHER_TYPE_RSA = CipherType.RSA_CIPHER256;

	/** RSA默认签名算法 */
	CryptType DEFAULT_SIGNATURE_RSA = CryptType.SHA256_WITH_RSA;
}