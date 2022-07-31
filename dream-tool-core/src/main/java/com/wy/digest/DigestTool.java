package com.wy.digest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import com.wy.binary.HexTool;
import com.wy.collection.MapTool;
import com.wy.digest.enums.CryptType;
import com.wy.digest.enums.MessageDigestType;
import com.wy.lang.StrTool;
import com.wy.result.ResultException;
import com.wy.util.CharsetTool;

/**
 * Digest加密工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-06 19:29:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DigestTool {

	/** 加解密生成公私钥Map的公钥key */
	public final static String PUBLIC_KEY = "publicKey";

	/** 加解密生成公私钥Map的私钥key */
	public final static String PRIVATE_KEY = "privateKey";

	/** RSA最大加密明文大小 */
	private final static int MAX_ENCRYPT_BLOCK = 117;

	/** RSA最大解密密文大小 */
	private final static int MAX_DECRYPT_BLOCK = 128;

	/**
	 * AES解密
	 * 
	 * @param secret 密钥,必须是16的倍数
	 * @param content 需解密16进制字符串
	 * @return 解密后的字符串
	 */
	public static String AESDecrypt(String secret, String content) {
		AESCheck(secret, content);
		// 若是将此解密方法放到linux机器报错,则使用以下方法生成随机源
		// SecretKeySpec key2 = null;
		// SecureRandom random =
		// SecureRandom.getInstance("SHA1PRNG");
		// random.setSeed(secret.getBytes());
		// keygen.init(128, random);
		byte[] byte_decode = AESSimpleCrypt(secret.getBytes(CharsetTool.defaultCharset()), HexTool.decode(content),
				Cipher.DECRYPT_MODE);
		return new String(byte_decode, CharsetTool.defaultCharset());
	}

	/**
	 * AES加密,若使用des加密,可将密钥生成器的随机源改为56
	 * 
	 * @param secret 密钥,必须是16的倍数
	 * @param content 加密内容
	 * @return 加密转换为16进制之后的大写字符串
	 */
	public static String AESEncrypt(String secret, String content) {
		AESCheck(secret, content);
		return HexTool.encodeHexString(AESSimpleCrypt(secret.getBytes(CharsetTool.defaultCharset()),
				content.getBytes(CharsetTool.defaultCharset()), Cipher.ENCRYPT_MODE));
	}

	/**
	 * 检查AES密钥以及加密内容
	 * 
	 * @param secret 密钥
	 * @param content 加密内容
	 */
	private static void AESCheck(String secret, String content) {
		if (StrTool.isAnyBlank(secret, content)) {
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
	public static String AESSimpleCrypt(String secret, String content, boolean flag) {
		AESCheck(secret, content);
		return flag
				? HexTool.encodeHexString(AES(content.getBytes(CharsetTool.defaultCharset()),
						secret.getBytes(CharsetTool.defaultCharset()), Cipher.ENCRYPT_MODE))
				: new String(AES(HexTool.decode(content), secret.getBytes(CharsetTool.defaultCharset()),
						Cipher.DECRYPT_MODE), CharsetTool.defaultCharset());
	}

	/**
	 * AES加解密
	 * 
	 * @param secret 密钥
	 * @param content 佳节密内容
	 * @param mode 加解密模式
	 * @return 加解密后的字符串
	 */
	public static byte[] AESSimpleCrypt(byte[] secret, byte[] content, int mode) {
		try {
			// 构造密钥生成器,指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance(CryptType.AES.getType());
			// 根据secret规则初始化密钥生成器,生成一个128位的随机源
			keygen.init(128, new SecureRandom(secret));
			// 产生原始对称密钥
			SecretKey originalKey = keygen.generateKey();
			// 获得原始对称密钥的字节数组
			byte[] raw = originalKey.getEncoded();
			return AES(content, raw, mode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	private static final byte[] AES(byte[] content, byte[] raw, int mode) {
		// 根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, CryptType.AES.getType());
		try {
			// 根据指定算法AES生成密码器
			Cipher cip = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 初始化密码器,第一个参数为加密(Encrypt_mode)或解密(Decrypt_mode),第二个参数为使用的KEY
			cip.init(mode, key);
			// 根据密码器的初始化方式--加密/解密
			return cip.doFinal(content);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * des加密
	 * 
	 * @param encryptString 待加密字符串
	 * @param encryptKey 加密key
	 * @return 加密后的base64字符串
	 */
	public static String DESEncrypt(String encryptString, String encryptKey) {
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), CryptType.DES.getType());
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.getEncoder().encodeToString(cipher.doFinal(encryptString.getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * des解密
	 * 
	 * @param decryptString 待解密base64字符串
	 * @param decryptKey 解密key
	 * @return 加密后的字符串
	 */
	public static String DESDecrypt(String decryptString, String decryptKey) {
		byte[] byteMi = Base64.getDecoder().decode(decryptString);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), CryptType.DES.getType());
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(cipher.doFinal(byteMi));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
				| InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
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
	 * @param content 需要加密的字符串
	 * @return 加密后的原始字节数组
	 */
	public static byte[] digest(MessageDigestType messageDigestType, String content) {
		return digest(messageDigestType, content.getBytes(CharsetTool.defaultCharset()));
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
		return digest(messageDigestType, content.getBytes(CharsetTool.defaultCharset(charset)));
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
		return digest(messageDigestType, content.getBytes(CharsetTool.defaultCharset(charset)));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param messageDigestType 加密算法
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的16进制大写字符串
	 */
	public static String digestHex(MessageDigestType messageDigestType, byte[] bytes) {
		return HexTool.encodeHexString(digest(messageDigestType, bytes));
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
		return HexTool.encodeHexString(digest(messageDigestType, content.getBytes(CharsetTool.defaultCharset())));
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
		return HexTool
				.encodeHexString(digest(messageDigestType, content.getBytes(CharsetTool.defaultCharset(charset))));
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
		return HexTool
				.encodeHexString(digest(messageDigestType, content.getBytes(CharsetTool.defaultCharset(charset))));
	}

	/**
	 * MD5加密,不可逆,无解密,任意长度变等长
	 * 
	 * @param bytes 需要进行加密的字节数组
	 * @return MD5加密后的原始字节数组
	 */
	public static byte[] MD5(byte[] bytes) {
		return digest(MessageDigestType.MD5, bytes);
	}

	/**
	 * MD5加密,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @return MD5加密后的原始字节数组
	 */
	public static byte[] MD5(String content) {
		return digest(MessageDigestType.MD5, content.getBytes(CharsetTool.defaultCharset()));
	}

	/**
	 * MD5加密,进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param bytes 需要进行加密的字节数组
	 * @return 加密后的16进制大写字符串
	 */
	public static String MD5Hex(byte[] bytes) {
		return digestHex(MessageDigestType.MD5, bytes);
	}

	/**
	 * MD5加密,再进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @return 加密后的16进制大写字符串
	 */
	public static String MD5Hex(String content) {
		return MD5Hex(content.getBytes(CharsetTool.defaultCharset()));
	}

	/**
	 * MD5加密,再进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @param charset 字符编码
	 * @return 加密后的16进制大写字符串
	 */
	public static String MD5Hex(String content, Charset charset) {
		return MD5Hex(content.getBytes(CharsetTool.defaultCharset(charset)));
	}

	/**
	 * MD5加密,再进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param content 需要进行加密的字符串
	 * @param charset 字符编码字符串
	 * @return 加密后的16进制大写字符串
	 */
	public static String MD5Hex(String content, String charset) {
		return MD5Hex(content.getBytes(CharsetTool.defaultCharset(charset)));
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 若是分段加密,则需要分段解密
	 * @return 加密后字符串
	 */
	public static String RSADecrypt(String privateKeyStr, String content) {
		RSAPrivateKey rsaPrivateKey = RSAPrivateKey(privateKeyStr);
		return RSADecrypt(rsaPrivateKey, content);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param content 若是分段加密,则需要分段解密
	 * @return 加密后字符串
	 */
	public static String RSADecrypt(PrivateKey privateKey, String content) {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		content = content.replaceAll("\r|\n", "");
		byte[] textb = Base64.getDecoder().decode(content);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(CryptType.RSA.getType());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int inputLen = textb.length;
			int offSet = 0;
			for (int i = 0; inputLen - offSet > 0; offSet = i * MAX_DECRYPT_BLOCK) {
				byte[] cache;
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(textb, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(textb, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				++i;
			}
			return new String(out.toByteArray(), CharsetTool.defaultCharset());
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA公钥加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 */
	public static String RSAEncrypt(String publicKeyStr, String content) {
		RSAPublicKey rsaPublicKey = RSAPublicKey(publicKeyStr);
		return RSAEncrypt(rsaPublicKey, content);
	}

	/**
	 * RSA公钥加密
	 * 
	 * @param publicKey
	 * @param content 加密内容长度受秘钥长度限制,若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 */
	public static String RSAEncrypt(PublicKey publicKey, String content) {
		byte[] plainText = content.getBytes(CharsetTool.defaultCharset());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(CryptType.RSA.getType());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int inputLen = plainText.length;
			int offSet = 0;
			byte[] cache;
			for (int i = 0; inputLen - offSet > 0; offSet = i * MAX_ENCRYPT_BLOCK) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(plainText, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(plainText, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				++i;
			}
			return Base64.getEncoder().encodeToString(out.toByteArray());
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<String, String> RSAGenerateKey() {
		return RSAGenerateKey(1024);
	}

	/**
	 * RSA生成公私钥
	 * 
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<String, String> RSAGenerateKey(int length) {
		if (length % 512 != 0) {
			throw new ResultException("密钥长度错误,必须是512的倍数");
		}
		try {
			KeyPairGenerator key = KeyPairGenerator.getInstance(CryptType.RSA.getType());
			key.initialize(length);
			KeyPair keyPair = key.generateKeyPair();
			return MapTool
					.builderGeneric(PUBLIC_KEY, Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
					.put(PRIVATE_KEY, Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())).build();
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
	public static final RSAPublicKey RSAPublicKey(String publicKeyStr) {
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
	public static RSAPrivateKey RSAPrivateKey(String privateKeyStr) {
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
	 * RSA私钥签名,和加密正好相反
	 * 
	 * @param data 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return 签名后的字节数组
	 */
	public static byte[] RSASign(byte[] data, String privateKeyStr) {
		try {
			PrivateKey privateKey = RSAPrivateKey(privateKeyStr.replaceAll("\r|\n", ""));
			Signature signature = Signature.getInstance(CryptType.SHA1_WITH_RSA.getType());
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA私钥签名,和加密正好相反
	 * 
	 * @param data 签名数据
	 * @param privateKeyStr 私钥字符串
	 * @return 签名后的Base64字符串
	 */
	public static String RSASignString(byte[] data, String privateKeyStr) {
		try {
			PrivateKey privateKey = RSAPrivateKey(privateKeyStr.replaceAll("\r|\n", ""));
			Signature signature = Signature.getInstance(CryptType.SHA1_WITH_RSA.getType());
			signature.initSign(privateKey);
			signature.update(data);
			return Base64.getEncoder().encodeToString(signature.sign());
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA公钥验证
	 * 
	 * @param data 原数据
	 * @param sign 签名后的字符串
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean RSAVerify(String data, String sign, String publicKeyStr) {
		return RSAVerify(data.getBytes(), Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验证
	 * 
	 * @param data 原数据字节数组
	 * @param sign 签名后的字符串
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean RSAVerify(byte[] data, String sign, String publicKeyStr) {
		return RSAVerify(data, Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验证
	 * 
	 * @param data 原数据字节数组
	 * @param sign 签名后经过Base64解码的字节数组
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean RSAVerify(byte[] data, byte[] sign, String publicKeyStr) {
		PublicKey publicKey = RSAPublicKey(publicKeyStr);
		try {
			Signature signature = Signature.getInstance(CryptType.SHA1_WITH_RSA.getType());
			signature.initVerify(publicKey);
			signature.update(data);
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