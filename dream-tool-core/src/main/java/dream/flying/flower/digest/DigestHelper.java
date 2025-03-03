package dream.flying.flower.digest;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import dream.flying.flower.ConstDigest;
import dream.flying.flower.binary.HexHelper;
import dream.flying.flower.binary.enums.EncodeType;
import dream.flying.flower.digest.enums.CipherType;
import dream.flying.flower.digest.enums.CryptType;
import dream.flying.flower.digest.enums.MessageDigestType;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;

/**
 * Digest加密工具类
 * 
 * CCM(Counter with CBC-MAC Mode)和GCM(Galois/Counter Mode):
 * AES加密算法的两种操作模式,都是AEAD(Authenticated Encryption with Associated Data)模式,
 * 用于同时提供数据加密和认证服务,比ECB和CBC模式更安全,效率更高
 * 
 * GCM模式需要指定标签长度(通常是96或128位),CCM模式则不需要指定,因为CCM自带标签生成机制.
 * 加密完成后,输出的是加密文本和认证标签的合并,这是因为GCM和CCM都是AEAD模式
 * 
 * @author 飞花梦影
 * @date 2021-03-06 19:29:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DigestHelper {

	/**
	 * 输出当前JDK版本支持的加密模式
	 */
	public static void printSecurityProvider() {
		for (Provider provider : Security.getProviders()) {
			for (Map.Entry<Object, Object> entry : provider.entrySet()) {
				System.out.printf("key: [%s]  value: [%s]%n", entry.getKey(), entry.getValue());
			}
		}
	}

	public static void main(String[] args) {
		printSecurityProvider("GCM");
	}

	/**
	 * 输出当前JDK版本支持的指定加密模式
	 * 
	 * @param cipherType 加密模式
	 */
	public static void printSecurityProvider(String cipherType) {
		for (Provider provider : Security.getProviders()) {
			for (Map.Entry<Object, Object> entry : provider.entrySet()) {
				if (((String) entry.getValue()).contains(cipherType)) {
					System.out.printf("key: [%s]  value: [%s]%n", entry.getKey(), entry.getValue());
				}
			}
		}
	}

	/**
	 * AES解密
	 * 
	 * @param secret 密钥
	 * @param content 需解密16进制字符串
	 * @return 解密后的字符串
	 */
	public static String aesDecrypt(byte[] secret, String content) {
		AssertHelper.notNull(secret, "密钥不能为空!");
		AssertHelper.notBlank(content, "加密内容不能为空!");
		// 若是将此解密方法放到linux机器报错,则使用以下方法生成随机源
		// SecretKeySpec key2 = null;
		// SecureRandom random =
		// SecureRandom.getInstance("SHA1PRNG");
		// random.setSeed(secret.getBytes());
		// keygen.init(128, random);
		byte[] decodeBytes = aesSimpleCrypt(secret, HexHelper.decode(content), Cipher.DECRYPT_MODE);
		return new String(decodeBytes, CharsetHelper.defaultCharset());
	}

	/**
	 * AES解密
	 * 
	 * @param secret 密钥
	 * @param content 16进制解码后的字节数组
	 * @return 解密后的字符串
	 */
	public static String aesDecrypt(byte[] secret, byte[] content) {
		AssertHelper.notNull(secret, "密钥不能为空!");
		AssertHelper.notNull(content, "加密内容不能为空!");
		// 若是将此解密方法放到linux机器报错,则使用以下方法生成随机源
		// SecretKeySpec key2 = null;
		// SecureRandom random =
		// SecureRandom.getInstance("SHA1PRNG");
		// random.setSeed(secret.getBytes());
		// keygen.init(128, random);
		byte[] decodeBytes = aesSimpleCrypt(secret, content, Cipher.DECRYPT_MODE);
		return new String(decodeBytes, CharsetHelper.defaultCharset());
	}

	/**
	 * AES解密
	 * 
	 * @param secret 密钥,必须是16的倍数
	 * @param content 需解密16进制字符串
	 * @return 解密后的字符串
	 */
	public static String aesDecrypt(String secret, String content) {
		aesCheck(secret, content);
		// 若是将此解密方法放到linux机器报错,则使用以下方法生成随机源
		// SecretKeySpec key2 = null;
		// SecureRandom random =
		// SecureRandom.getInstance("SHA1PRNG");
		// random.setSeed(secret.getBytes());
		// keygen.init(128, random);
		byte[] decodeBytes = aesSimpleCrypt(secret.getBytes(CharsetHelper.defaultCharset()), HexHelper.decode(content),
				Cipher.DECRYPT_MODE);
		return new String(decodeBytes, CharsetHelper.defaultCharset());
	}

	/**
	 * AES加密,若使用des加密,可将密钥生成器的随机源改为56
	 * 
	 * @param secret 密钥字节数组
	 * @param content 加密内容
	 * @return 加密转换为16进制之后的大写字符串
	 */
	public static String aesEncrypt(byte[] secret, String content) {
		AssertHelper.notNull(secret, "密钥不能为空!");
		AssertHelper.notBlank(content, "加密内容不能为空!");
		return HexHelper.encodeHexString(
				aesSimpleCrypt(secret, content.getBytes(CharsetHelper.defaultCharset()), Cipher.ENCRYPT_MODE));
	}

	/**
	 * AES加密,若使用des加密,可将密钥生成器的随机源改为56
	 * 
	 * @param secret 密钥
	 * @param content 加密内容
	 * @return 加密转换为16进制之后的大写字符串
	 */
	public static String aesEncrypt(byte[] secret, byte[] content) {
		AssertHelper.notNull(secret, "密钥不能为空!");
		AssertHelper.notNull(content, "加密内容不能为空!");
		return HexHelper.encodeHexString(aesSimpleCrypt(secret, content, Cipher.ENCRYPT_MODE));
	}

	/**
	 * AES加密,若使用des加密,可将密钥生成器的随机源改为56
	 * 
	 * @param secret 密钥,必须是16的倍数
	 * @param content 加密内容
	 * @return 加密转换为16进制之后的大写字符串
	 */
	public static String aesEncrypt(String secret, String content) {
		aesCheck(secret, content);
		return HexHelper.encodeHexString(aesSimpleCrypt(secret.getBytes(CharsetHelper.defaultCharset()),
				content.getBytes(CharsetHelper.defaultCharset()), Cipher.ENCRYPT_MODE));
	}

	/**
	 * 检查AES密钥以及加密内容
	 * 
	 * @param secret 密钥
	 * @param content 加密内容
	 */
	private static void aesCheck(String secret, String content) {
		if (StrHelper.isAnyBlank(secret, content)) {
			throw new ResultException("加密内容或密钥不能为空");
		}
		if (secret.length() % 16 != 0) {
			throw new ResultException("密钥长度必须是16的倍数位");
		}
	}

	/**
	 * AES简单加密,密钥长度必须是16的倍数
	 * 
	 * @param secret 密钥,必须是16的倍数
	 * @param content 需要加解密的内容
	 * @param flag 加解密.true->加密,false->解密
	 * @return 加解密转换为16进制之后的大写字符串
	 */
	public static String aesSimpleCrypt(String secret, String content, boolean flag) {
		aesCheck(secret, content);
		return flag
				? HexHelper.encodeHexString(aes(content.getBytes(CharsetHelper.defaultCharset()),
						secret.getBytes(CharsetHelper.defaultCharset()), Cipher.ENCRYPT_MODE))
				: new String(aes(HexHelper.decode(content), secret.getBytes(CharsetHelper.defaultCharset()),
						Cipher.DECRYPT_MODE), CharsetHelper.defaultCharset());
	}

	/**
	 * AES加解密
	 * 
	 * @param secret 密钥
	 * @param content 佳节密内容
	 * @param mode 加解密模式
	 * @return 加解密后的字符串
	 */
	public static byte[] aesSimpleCrypt(byte[] secret, byte[] content, int mode) {
		try {
			// 构造密钥生成器,指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance(CryptType.AES.getType());

			// 根据secret规则初始化密钥生成器,生成一个128位的随机源
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secret);
			keygen.init(128, secureRandom);

			// 产生原始对称密钥
			SecretKey originalKey = keygen.generateKey();
			// 获得原始对称密钥的字节数组
			byte[] raw = originalKey.getEncoded();
			return aes(content, raw, mode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	private static final byte[] aes(byte[] content, byte[] raw, int mode) {
		// 根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, CryptType.AES.getType());
		try {
			// 根据指定算法AES生成密码器
			Cipher cipher = Cipher.getInstance(CryptType.AES.getType());
			// 初始化密码器,第一个参数为加密(Encrypt_mode)或解密(Decrypt_mode),第二个参数为使用的KEY
			cipher.init(mode, key);
			// 根据密码器的初始化方式--加密/解密
			return cipher.doFinal(content);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * des加密
	 * 
	 * @param secretKey 加密key
	 * @param content 待加密字符串
	 * @return 加密后的base64字符串
	 */
	public static String desEncrypt(byte[] secretKey, String content) {
		SecretKeySpec key = new SecretKeySpec(secretKey, CryptType.DES.getType());
		try {
			Cipher cipher = Cipher.getInstance(CryptType.DES.getType());
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * des加密
	 * 
	 * @param secretKey 加密key
	 * @param content 待加密字符串
	 * @return 加密后的base64字符串
	 */
	public static String desEncrypt(String secretKey, String content) {
		return desEncrypt(secretKey.getBytes(), content);
	}

	/**
	 * des解密
	 * 
	 * @param secretKey 解密key
	 * @param content 待解密base64字符串
	 * @return 加密后的字符串
	 */
	public static String desDecrypt(byte[] secretKey, String content) {
		byte[] byteMi = Base64.getDecoder().decode(content);
		SecretKeySpec key = new SecretKeySpec(secretKey, CryptType.DES.getType());
		try {
			Cipher cipher = Cipher.getInstance(CryptType.DES.getType());
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(byteMi));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
				| InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * des解密
	 * 
	 * @param secretKey 解密key
	 * @param content 待解密base64字符串
	 * @return 加密后的字符串
	 */
	public static String desDecrypt(String secretKey, String content) {
		return desDecrypt(secretKey.getBytes(), content);
	}

	/**
	 * 字符串加密
	 * 
	 * @param messageDigestType 加密算法
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的原始字节数组
	 */
	public static byte[] digest(MessageDigestType messageDigestType, byte[] bytes) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(messageDigestType.getType());
			return messageDigest.digest(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * 字符串加密
	 * 
	 * @param messageDigestType 加密算法
	 * @param byteBuffer 需要加密的字节数组缓存
	 * @return 加密后的原始字节数组
	 */
	public static byte[] digest(MessageDigestType messageDigestType, ByteBuffer byteBuffer) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(messageDigestType.getType());
			messageDigest.update(byteBuffer);
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * 字符串加密
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @return 加密后的原始字节数组
	 */
	public static byte[] digest(MessageDigestType messageDigestType, String content) {
		return digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset()));
	}

	/**
	 * 字符串加密
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param charset 字符编码
	 * @return 加密后的原始字节数组
	 */
	public static byte[] digest(MessageDigestType messageDigestType, String content, Charset charset) {
		return digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * 字符串加密
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param charset 字符编码字符串
	 * @return 加密后的原始字节数组
	 */
	public static byte[] digest(MessageDigestType messageDigestType, String content, String charset) {
		return digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * 字符串加密,再进行Base64编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的Base64编码大写字符串
	 */
	public static String digestBase64(MessageDigestType messageDigestType, byte[] bytes) {
		return Base64.getEncoder().encodeToString(digest(messageDigestType, bytes));
	}

	/**
	 * 字符串加密,再进行Base64编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param byteBuffer 需要加密的字节数组缓存
	 * @return 加密后的Base64编码大写字符串
	 */
	public static String digestBase64(MessageDigestType messageDigestType, ByteBuffer byteBuffer) {
		return Base64.getEncoder().encodeToString(digest(messageDigestType, byteBuffer));
	}

	/**
	 * 字符串加密,再进行Base64编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的Base64编码大写字符串
	 */
	public static String digestBase64(MessageDigestType messageDigestType, String content) {
		return Base64.getEncoder()
				.encodeToString(digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset())));
	}

	/**
	 * 字符串加密,再进行Base64编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param charset 字符编码
	 * @return 加密后的Base64编码大写字符串
	 */
	public static String digestBase64(MessageDigestType messageDigestType, String content, Charset charset) {
		return Base64.getEncoder()
				.encodeToString(digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset(charset))));
	}

	/**
	 * 字符串加密,再进行Base64编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param charset 字符编码字符串
	 * @return 加密后的Base64编码大写字符串
	 */
	public static String digestBase64(MessageDigestType messageDigestType, String content, String charset) {
		return Base64.getEncoder()
				.encodeToString(digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset(charset))));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的16进制大写字符串
	 */
	public static String digestHex(MessageDigestType messageDigestType, byte[] bytes) {
		return HexHelper.encodeHexString(digest(messageDigestType, bytes));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param byteBuffer 需要加密的字节数组缓存
	 * @return 加密后的16进制大写字符串
	 */
	public static String digestHex(MessageDigestType messageDigestType, ByteBuffer byteBuffer) {
		return HexHelper.encodeHexString(digest(messageDigestType, byteBuffer));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的16进制大写字符串
	 */
	public static String digestHex(MessageDigestType messageDigestType, String content) {
		return HexHelper.encodeHexString(digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset())));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param charset 字符编码
	 * @return 加密后的16进制大写字符串
	 */
	public static String digestHex(MessageDigestType messageDigestType, String content, Charset charset) {
		return HexHelper
				.encodeHexString(digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset(charset))));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param content 需要加密的字符串
	 * @param charset 字符编码字符串
	 * @return 加密后的16进制大写字符串
	 */
	public static String digestHex(MessageDigestType messageDigestType, String content, String charset) {
		return HexHelper
				.encodeHexString(digest(messageDigestType, content.getBytes(CharsetHelper.defaultCharset(charset))));
	}

	/**
	 * MD5加密,不可逆,无解密,任意长度变等长
	 * 
	 * @param bytes 需要进行加密的字节数组
	 * @return MD5加密后的原始字节数组
	 */
	public static byte[] md5(byte[] bytes) {
		return digest(MessageDigestType.MD5, bytes);
	}

	/**
	 * MD5加密,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @return MD5加密后的原始字节数组
	 */
	public static byte[] md5(String content) {
		return digest(MessageDigestType.MD5, content.getBytes(CharsetHelper.defaultCharset()));
	}

	/**
	 * MD5加密,进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param bytes 需要进行加密的字节数组
	 * @return 加密后的16进制大写字符串
	 */
	public static String md5Hex(byte[] bytes) {
		return digestHex(MessageDigestType.MD5, bytes);
	}

	/**
	 * MD5加密,进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param byteBuffer 需要进行加密的字节数组缓存
	 * @return 加密后的16进制大写字符串
	 */
	public static String md5Hex(ByteBuffer byteBuffer) {
		return digestHex(MessageDigestType.MD5, byteBuffer);
	}

	/**
	 * MD5加密,再进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @return 加密后的16进制大写字符串
	 */
	public static String md5Hex(String content) {
		return md5Hex(content.getBytes(CharsetHelper.defaultCharset()));
	}

	/**
	 * MD5加密,再进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @param charset 字符编码
	 * @return 加密后的16进制大写字符串
	 */
	public static String md5Hex(String content, Charset charset) {
		return md5Hex(content.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * MD5加密,再进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @param charset 字符编码字符串
	 * @return 加密后的16进制大写字符串
	 */
	public static String md5Hex(String content, String charset) {
		return md5Hex(content.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * UUID算法生成一个随机字符串,无-
	 * 
	 * @return 32位uuid字符串
	 */
	public static String uuid() {
		return uuid(true);
	}

	/**
	 * UUID算法生成一个字符串
	 * 
	 * @param flag 是否去掉-.true->去掉,false->不去掉
	 * @return 36位uuid字符串
	 */
	public static String uuid(boolean flag) {
		return flag ? UUID.randomUUID().toString().replaceAll("-", "") : UUID.randomUUID().toString();
	}

	/**
	 * 生成对称密钥
	 * 
	 * @return 密钥
	 */
	public static SecretKey secretKey() {
		return secretKey(ConstDigest.KEY_SIZE_256);
	}

	/**
	 * 生成对称密钥
	 * 
	 * @param length 密钥长度
	 * @return 密钥
	 */
	public static SecretKey secretKey(int length) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptType.AES.getType());
			keyGenerator.init(length);
			return keyGenerator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成对称密钥
	 * 
	 * @return 密钥
	 */
	public static String generateKey() {
		return generateKey(EncodeType.BASE64);
	}

	/**
	 * 生成对称密钥
	 * 
	 * @param encodeType 密钥编码方式
	 * @return 密钥
	 */
	public static String generateKey(EncodeType encodeType) {
		return generateKey(ConstDigest.KEY_SIZE_256, encodeType);
	}

	/**
	 * 生成对称密钥
	 * 
	 * @param length 密钥长度
	 * @param encodeType 密钥编码方式
	 * @return 密钥
	 */
	public static String generateKey(int length, EncodeType encodeType) {
		byte[] secretKey = secretKey(length).getEncoded();
		switch (encodeType) {
		case HEX:
			return HexHelper.encodeHexString(secretKey);
		default:
			return Base64.getEncoder().encodeToString(secretKey);
		}
	}

	/**
	 * 获得密钥的pem格式
	 * 
	 * @param bytes 原始密钥,未经过base64或其他算法编码
	 * @return base64编码后的pem格式密钥
	 */
	public static String getPemBase64(byte[] bytes) {
		return Base64.getMimeEncoder(ConstDigest.PEM_LINE_SIZE, System.lineSeparator().getBytes())
				.encodeToString(bytes);
	}

	/**
	 * 获取Certificate的PEM格式
	 * 
	 * @param encoded 字节数据
	 * @return PEM格式公钥
	 */
	public static String getPemCertificate(byte[] encoded) {
		StringBuffer base64String = new StringBuffer("");
		base64String.append(ConstDigest.PEM_CERTIFICATE_BEGIN).append(System.lineSeparator());
		base64String.append(getPemBase64(encoded)).append(System.lineSeparator());
		base64String.append(ConstDigest.PEM_CERTIFICATE_END).append(System.lineSeparator());
		return base64String.toString();
	}

	/**
	 * 获取私钥的PEM格式
	 * 
	 * @param encoded 私钥
	 * @return PEM格式私钥
	 */
	public static String getPemPrivateKey(byte[] encoded) {
		StringBuffer base64String = new StringBuffer("");
		base64String.append(ConstDigest.PEM_PRIVATE_KEY_BEGIN).append(System.lineSeparator());
		base64String.append(getPemBase64(encoded)).append(System.lineSeparator());
		base64String.append(ConstDigest.PEM_PRIVATE_KEY_END).append(System.lineSeparator());
		return base64String.toString();
	}

	/**
	 * 获取公钥的PEM格式
	 *
	 * @param encoded 公钥
	 * @return PEM格式公钥
	 */
	public static String getPemPublicKey(byte[] encoded) {
		StringBuffer base64String = new StringBuffer("");
		base64String.append(ConstDigest.PEM_PUBLIC_KEY_BEGIN).append(System.lineSeparator());
		base64String.append(getPemBase64(encoded)).append(System.lineSeparator());
		base64String.append(ConstDigest.PEM_PUBLIC_KEY_END).append(System.lineSeparator());
		return base64String.toString();
	}

	/**
	 * AES的GCM模式加密
	 *
	 * @param content 加密内容
	 * @return hex编码后的加密结果和标签
	 */
	public static String aesGcmEncrypt(String content) {
		// 128比特的密钥
		byte[] keyBytes = new byte[16];
		// 初始化向量
		byte[] iv = new byte[16];
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, CryptType.AES.getType());
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
		try {
			// JDK1.8只支持当前模式,其他模式不支持
			Cipher cipher = Cipher.getInstance(CipherType.AES_GCM_CIPHER.getType());
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
			// 包含加密文本和认证标签
			byte[] authenticationTag = cipher.doFinal(content.getBytes());
			// 输出加密结果和认证标签
			return HexHelper.encodeHexString(authenticationTag);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES的GCM模式加密
	 *
	 * @param content 经过hex的加密内容
	 * @return 原文
	 */
	public static String aesGcmDecrypt(String content) {
		// 128比特的密钥
		byte[] keyBytes = new byte[16];
		// 初始化向量
		byte[] iv = new byte[16];
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, CryptType.AES.getType());
		GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
		try {
			// JDK1.8只支持当前模式,其他模式不支持
			Cipher cipher = Cipher.getInstance(CipherType.AES_GCM_CIPHER.getType());
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
			// 包含加密文本和认证标签
			byte[] authenticationTag = cipher.doFinal(HexHelper.decode(content));
			return new String(authenticationTag);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}