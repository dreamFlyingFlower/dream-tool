package dream.flying.flower.digest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import dream.flying.flower.ConstDigest;
import dream.flying.flower.binary.HexHelper;
import dream.flying.flower.binary.enums.EncodeType;
import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.digest.enums.CipherType;
import dream.flying.flower.digest.enums.CryptKeyType;
import dream.flying.flower.digest.enums.CryptType;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.result.ResultException;

/**
 * RSA工具类
 *
 * @author 飞花梦影
 * @date 2024-09-23 14:00:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class RsaHelper {

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, byte[] encryptedContent) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return decrypt(privateKey, encryptedContent);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, String encryptedContent) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return decrypt(privateKey, encryptedContent);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, String encryptedContent, CipherType cipherType) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return decrypt(privateKey, encryptedContent, cipherType);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 * @throws InvalidKeySpecException
	 * @throws InvalidAlgorithmParameterException
	 */
	public static String decrypt(PrivateKey privateKey, byte[] encryptedContent) {
		return decrypt(privateKey, encryptedContent, CipherType.RSA_CIPHER256);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 * @throws InvalidKeySpecException
	 * @throws InvalidAlgorithmParameterException
	 */
	public static String decrypt(PrivateKey privateKey, byte[] encryptedContent, CipherType cipherType) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			// 解密数据
			Cipher cipher = Cipher.getInstance(cipherType.getType());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength();
			int blockSize = cipherType.decryptLength(keyLength);
			int length = encryptedContent.length;
			int offset = 0;
			int i = 0;
			byte[] cache;
			// 对数据分段解密
			for (; length - offset > 0; offset = i * blockSize) {
				if (length - offset > blockSize) {
					cache = cipher.doFinal(encryptedContent, offset, blockSize);
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
	public static String decrypt(PrivateKey privateKey, String encryptedContent) {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		byte[] encryptedData = Base64.getDecoder().decode(encryptedContent.replaceAll("\r|\n", ""));
		return decrypt(privateKey, encryptedData);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param encryptedContent 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(PrivateKey privateKey, String encryptedContent, CipherType cipherType) {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		byte[] encryptedData = Base64.getDecoder().decode(encryptedContent.replaceAll("\r|\n", ""));
		return decrypt(privateKey, encryptedData, cipherType);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, byte[] content) {
		return encrypt(publicKey, content, CipherType.RSA_CIPHER256);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, byte[] content, CipherType cipherType) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(cipherType.getType());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength();
			int blockSize = cipherType.encryptLength(keyLength);
			int length = content.length;
			int offset = 0;
			byte[] cache;
			int i = 0;
			while (length - offset > 0) {
				if (length - offset > blockSize) {
					cache = cipher.doFinal(content, offset, blockSize);
				} else {
					cache = cipher.doFinal(content, offset, length - offset);
				}
				out.write(cache, 0, cache.length);
				i++;
				offset = i * blockSize;
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
	public static String encrypt(PublicKey publicKey, String content) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset());
		return encrypt(publicKey, plainText);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content, CipherType cipherType) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset());
		return encrypt(publicKey, plainText, cipherType);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content, Charset charset) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset(charset));
		return encrypt(publicKey, plainText);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content, String charset) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset(charset));
		return encrypt(publicKey, plainText);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, byte[] content) {
		RSAPublicKey rsaPublicKey = publicKey(publicKeyStr);
		return encrypt(rsaPublicKey, content);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, String content) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return encrypt(publicKey, content);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, String content, CipherType cipherType) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return encrypt(publicKey, content, cipherType);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<CryptKeyType, String> generateKey() {
		return generateKey(ConstDigest.KEY_SIZE_2048);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @param encodeType 编码类型
	 * @return 公私钥键值对
	 */
	public static Map<CryptKeyType, String> generateKey(EncodeType encodeType) {
		return generateKey(ConstDigest.KEY_SIZE_2048, encodeType);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @param length 密钥长度
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<CryptKeyType, String> generateKey(int length) {
		return generateKey(length, EncodeType.BASE64);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @param length 密钥长度
	 * @param encodeType 编码类型
	 * @return 公私钥键值对,公私钥都已经经过hex或base64编码
	 */
	public static Map<CryptKeyType, String> generateKey(int length, EncodeType encodeType) {
		KeyPair keyPair = generateKeyPair(length);
		switch (encodeType) {
		case HEX:
			return MapHelper
					.builderGeneric(CryptKeyType.PUBLIC_KEY,
							HexHelper.encodeHexString(keyPair.getPublic().getEncoded()))
					.put(CryptKeyType.PRIVATE_KEY, HexHelper.encodeHexString(keyPair.getPrivate().getEncoded()))
					.build();
		default:
			return MapHelper
					.builderGeneric(CryptKeyType.PUBLIC_KEY,
							Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
					.put(CryptKeyType.PRIVATE_KEY,
							Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
					.build();
		}
	}

	/**
	 * RSA生成公私钥,默认密钥长度2048
	 * 
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		return generateKeyPair(ConstDigest.KEY_SIZE_2048);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @param length 密钥长度
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair(int length) {
		if (length % 512 != 0) {
			throw new ResultException("密钥长度错误,必须是512的倍数");
		}
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CryptType.RSA.getType());
			SecureRandom secureRandom = new SecureRandom();
			keyPairGenerator.initialize(length, secureRandom);
			return keyPairGenerator.generateKeyPair();
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
	public static final RSAPublicKey publicKey(String publicKeyStr) {
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
	public static RSAPrivateKey privateKey(String privateKeyStr) {
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
	public static byte[] sign(String content, String privateKeyStr) {
		return sign(content.getBytes(), privateKeyStr);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return 签名后的字节数组
	 */
	public static byte[] sign(byte[] content, String privateKeyStr) {
		PrivateKey privateKey = privateKey(privateKeyStr);
		return sign(content, privateKey);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @param charset 字符集
	 * @return 签名后的字节数组
	 */
	public static byte[] sign(String content, String privateKeyStr, Charset charset) {
		return sign(content.getBytes(CharsetHelper.defaultCharset(charset)), privateKeyStr);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKey 私钥
	 * @return 签名后的字节数组
	 */
	public static byte[] sign(byte[] content, PrivateKey privateKey) {
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
	public static String signString(String content, String privateKeyStr) {
		return signString(content.getBytes(), privateKeyStr);
	}

	/**
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return Base64编码后的签名字符串
	 */
	public static String signString(byte[] content, String privateKeyStr) {
		byte[] byteSign = sign(content, privateKeyStr);
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
	public static String signString(String content, String privateKeyStr, Charset charset) {
		return signString(content.getBytes(CharsetHelper.defaultCharset(charset)), privateKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 签名后的字符串
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verifySign(String content, String sign, String publicKeyStr) {
		return verifySign(content.getBytes(), Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 未经Base64解码的签名数据
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verifySign(byte[] content, String sign, String publicKeyStr) {
		return verifySign(content, Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 签名数据
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verifySign(byte[] content, byte[] sign, String publicKeyStr) {
		PublicKey publicKey = publicKey(publicKeyStr);
		return verifySign(content, sign, publicKey);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param data 原数据字节数组
	 * @param sign 签名后经过Base64解码的字节数组
	 * @param publicKey 公钥
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verifySign(byte[] content, byte[] sign, PublicKey publicKey) {
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
}