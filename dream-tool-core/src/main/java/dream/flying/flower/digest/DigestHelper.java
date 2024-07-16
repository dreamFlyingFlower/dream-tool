package dream.flying.flower.digest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dream.flying.flower.ConstDigest;
import dream.flying.flower.binary.HexHelper;
import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.digest.enums.CryptKeyType;
import dream.flying.flower.digest.enums.CryptType;
import dream.flying.flower.digest.enums.MessageDigestType;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;

/**
 * Digest加密工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-06 19:29:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DigestHelper {

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
			Cipher cipher = Cipher.getInstance(CryptType.AES_CIPHER.getType());
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
			Cipher cipher = Cipher.getInstance(CryptType.DES_CIPHER.getType());
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
			Cipher cipher = Cipher.getInstance(CryptType.DES_CIPHER.getType());
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
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String rsaDecrypt(String privateKeyStr, byte[] encryptedContent) {
		RSAPrivateKey rsaPrivateKey = rsaPrivateKey(privateKeyStr);
		return rsaDecrypt(rsaPrivateKey, encryptedContent);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String rsaDecrypt(String privateKeyStr, String encryptedContent) {
		RSAPrivateKey rsaPrivateKey = rsaPrivateKey(privateKeyStr);
		return rsaDecrypt(rsaPrivateKey, encryptedContent);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String rsaDecrypt(PrivateKey privateKey, byte[] encryptedContent) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(CryptType.RSA.getType());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int length = encryptedContent.length;
			int offset = 0;
			int i = 0;
			byte[] cache;
			// 对数据分段解密
			for (; length - offset > 0; offset = i * ConstDigest.MAX_DECRYPT_BLOCK) {
				if (length - offset > ConstDigest.MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedContent, offset, ConstDigest.MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedContent, offset, length - offset);
				}
				out.write(cache, 0, cache.length);
				i++;
			}
			return new String(out.toByteArray(), CharsetHelper.defaultCharset());
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String rsaDecrypt(PrivateKey privateKey, String encryptedContent) {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		byte[] encryptedData = Base64.getDecoder().decode(encryptedContent.replaceAll("\r|\n", ""));
		return rsaDecrypt(privateKey, encryptedData);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String rsaEncrypt(PublicKey publicKey, byte[] content) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(CryptType.RSA.getType());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int length = content.length;
			int offset = 0;
			byte[] cache;
			int i = 0;
			for (; length - offset > 0; offset = i * ConstDigest.MAX_ENCRYPT_BLOCK) {
				if (length - offset > ConstDigest.MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(content, offset, ConstDigest.MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(content, offset, length - offset);
				}
				out.write(cache, 0, cache.length);
				i++;
			}
			return Base64.getEncoder().encodeToString(out.toByteArray());
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String rsaEncrypt(PublicKey publicKey, String content) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset());
		return rsaEncrypt(publicKey, plainText);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String rsaEncrypt(PublicKey publicKey, String content, Charset charset) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset(charset));
		return rsaEncrypt(publicKey, plainText);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String rsaEncrypt(PublicKey publicKey, String content, String charset) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset(charset));
		return rsaEncrypt(publicKey, plainText);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String rsaEncrypt(String publicKeyStr, byte[] content) {
		RSAPublicKey rsaPublicKey = rsaPublicKey(publicKeyStr);
		return rsaEncrypt(rsaPublicKey, content);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String rsaEncrypt(String publicKeyStr, String content) {
		RSAPublicKey rsaPublicKey = rsaPublicKey(publicKeyStr);
		return rsaEncrypt(rsaPublicKey, content);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<CryptKeyType, String> rsaGenerateKey() {
		return rsaGenerateKey(ConstDigest.KEY_SIZE_1024);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @param length 密钥长度
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<CryptKeyType, String> rsaGenerateKey(int length) {
		if (length % 512 != 0) {
			throw new ResultException("密钥长度错误,必须是512的倍数");
		}
		try {
			KeyPairGenerator key = KeyPairGenerator.getInstance(CryptType.RSA.getType());
			key.initialize(length);
			KeyPair keyPair = key.generateKeyPair();
			return MapHelper
					.builderGeneric(CryptKeyType.PUBLIC_KEY,
							Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
					.put(CryptKeyType.PRIVATE_KEY,
							Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
					.build();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * 生成RSA公钥
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @return RSA公钥
	 */
	public static final RSAPublicKey rsaPublicKey(String publicKeyStr) {
		X509EncodedKeySpec keySpec =
				new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr.replaceAll("\r|\n", "")));
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(CryptType.RSA.getType());
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * 生成RSA私钥
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @return RSA私钥
	 */
	public static RSAPrivateKey rsaPrivateKey(String privateKeyStr) {
		PKCS8EncodedKeySpec keySpec =
				new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr.replaceAll("\r|\n", "")));
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(CryptType.RSA.getType());
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return 签名后的字节数组
	 */
	public static byte[] rsaSign(String content, String privateKeyStr) {
		return rsaSign(content.getBytes(), privateKeyStr);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return 签名后的字节数组
	 */
	public static byte[] rsaSign(byte[] content, String privateKeyStr) {
		PrivateKey privateKey = rsaPrivateKey(privateKeyStr);
		return rsaSign(content, privateKey);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @param charset 字符集
	 * @return 签名后的字节数组
	 */
	public static byte[] rsaSign(String content, String privateKeyStr, Charset charset) {
		return rsaSign(content.getBytes(CharsetHelper.defaultCharset(charset)), privateKeyStr);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKey 私钥
	 * @return 签名后的字节数组
	 */
	public static byte[] rsaSign(byte[] content, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance(CryptType.SHA1_WITH_RSA.getType());
			signature.initSign(privateKey);
			signature.update(content);
			return signature.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return Base64编码后的签名字符串
	 */
	public static String rsaSignString(String content, String privateKeyStr) {
		return rsaSignString(content.getBytes(), privateKeyStr);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return Base64编码后的签名字符串
	 */
	public static String rsaSignString(byte[] content, String privateKeyStr) {
		byte[] byteSign = rsaSign(content, privateKeyStr);
		return Base64.getEncoder().encodeToString(byteSign);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @param charset 字符集
	 * @return Base64编码后的签名字符串
	 */
	public static String rsaSignString(String content, String privateKeyStr, Charset charset) {
		return rsaSignString(content.getBytes(CharsetHelper.defaultCharset(charset)), privateKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 签名后的字符串
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean rsaVerifySign(String content, String sign, String publicKeyStr) {
		return rsaVerifySign(content.getBytes(), Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 未经Base64解码的签名数据
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean rsaVerifySign(byte[] content, String sign, String publicKeyStr) {
		return rsaVerifySign(content, Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 签名数据
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean rsaVerifySign(byte[] content, byte[] sign, String publicKeyStr) {
		PublicKey publicKey = rsaPublicKey(publicKeyStr);
		return rsaVerifySign(content, sign, publicKey);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param data 原数据字节数组
	 * @param sign 签名后经过Base64解码的字节数组
	 * @param publicKey 公钥
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean rsaVerifySign(byte[] content, byte[] sign, PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance(CryptType.SHA1_WITH_RSA.getType());
			signature.initVerify(publicKey);
			signature.update(content);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return false;
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
}