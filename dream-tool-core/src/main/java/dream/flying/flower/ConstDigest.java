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

	/** -1 */
	int NEGATIVE_ONE = -1;

	/** 0 */
	int ZERO = 0;

	/** 1 */
	int POSITIVE_ONE = 1;

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

	/** RSA默认公钥加密,私钥解密模式以及填充模式,非系统默认,当前模式更安全 */
	CipherType DEFAULT_CIPHER_TYPE_RSA = CipherType.RSA_ECB_OAEP_SHA256;

	/** RSA默认公钥解密,私钥加密模式以及填充模式,OAEP不可用于当前公私钥解加密模式 */
	CipherType DEFAULT_CIPHER_TYPE_RSA_PRIVATE = CipherType.RSA_CIPHER;

	/** RSA默认签名算法 */
	CryptType DEFAULT_SIGNATURE_RSA = CryptType.SHA256_WITH_RSA;

	/** PEM格式Certificate开头 */
	String PEM_CERTIFICATE_BEGIN = "-----BEGIN CERTIFICATE-----";

	/** PEM格式Certificate结尾 */
	String PEM_CERTIFICATE_END = "-----END CERTIFICATE-----";

	/** PEM格式RSA私钥开头 */
	String PEM_PRIVATE_KEY_BEGIN = "-----BEGIN RSA PRIVATE KEY-----";

	/** PEM格式RSA私钥结尾 */
	String PEM_PRIVATE_KEY_END = "-----END RSA PRIVATE KEY-----";

	/** PEM格式RSA公钥开头 */
	String PEM_PUBLIC_KEY_BEGIN = "-----BEGIN PUBLIC KEY-----";

	/** PEM格式RSA公钥结尾 */
	String PEM_PUBLIC_KEY_END = "-----END PUBLIC KEY-----";
}