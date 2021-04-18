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
import com.wy.digest.enums.Algorithms;
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

	/**
	 * 按照UUID算法生成一个字符串
	 */
	public static String UUID() {
		return UUID(true);
	}

	/**
	 * 按照UUID算法生成一个字符串,是否做处理,true是
	 */
	public static String UUID(boolean flag) {
		return flag ? UUID.randomUUID().toString().replaceAll("-", "") : UUID.randomUUID().toString();
	}

	/**
	 * 将传入的消息进行md5加密,进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param message 需要进行加密的字符串
	 * @return 加密后的字符串
	 */
	public static String MD5(String message) {
		return MD5(message.getBytes(CharsetTool.defaultCharset()));
	}

	/**
	 * 将传入的消息进行md5加密,进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param message 需要进行加密的字符串
	 * @param charset 字符编码
	 * @return 加密后的字符串
	 */
	public static String MD5(String message, Charset charset) {
		return MD5(message.getBytes(CharsetTool.defaultCharset(charset)));
	}

	/**
	 * 将传入的消息进行md5加密,进行16进制编码,不可逆,无解密,任意长度变等长
	 * 
	 * @param bytes 需要进行加密的字节数组
	 * @return 加密后的字符串
	 */
	public static String MD5(byte[] bytes) {
		return digest(Algorithms.MD5, bytes);
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param algorithms 加密算法
	 * @param message 需要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String digest(Algorithms algorithms, String message) {
		return digest(algorithms, message.getBytes(CharsetTool.defaultCharset()));
	}

	/**
	 * 字符串加密,再进行16进制编码
	 * 
	 * @param algorithms 加密算法
	 * @param bytes 需要加密的字节数组
	 * @return 加密后的字符串
	 */
	public static String digest(Algorithms algorithms, byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithms.name());
			byte[] output = digest.digest(bytes);
			return HexTool.encodeHexString(output);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES简单加密,密钥长度必须是16的倍数位
	 * 
	 * @param encodeRules 密钥,必须是16的倍数位
	 * @param content 需要加解密的内容
	 * @param flag true为加密,false解密
	 * @return 加解密转换为16进制之后的大写字符串
	 */
	public static String AESSimpleCrypt(String encodeRules, String content, boolean flag) {
		if (StrTool.isAnyBlank(encodeRules, content)) {
			throw new ResultException("加密内容或密钥不能为空");
		}
		if (encodeRules.length() % 16 != 0) {
			throw new ResultException("密钥长度必须是16的倍数位");
		}
		return flag
				? HexTool.encodeHexString(AES(content.getBytes(CharsetTool.defaultCharset()),
						encodeRules.getBytes(CharsetTool.defaultCharset()), Cipher.ENCRYPT_MODE))
				: new String(AES(HexTool.decode(content), encodeRules.getBytes(CharsetTool.defaultCharset()),
						Cipher.DECRYPT_MODE), CharsetTool.defaultCharset());
	}

	/**
	 * aes加密,若使用des加密,可将密钥生成器的随机源改为56
	 * 
	 * @param encodeRules 加密规则
	 * @param content 加密内容
	 * @return 返回一个16进制字符串
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public static String AESEncrypt(String encodeRules, String content) {
		if (StrTool.isAnyBlank(encodeRules, content)) {
			return "加密参数不能为空";
		}
		return HexTool.encodeHexString(aesCrypto(encodeRules.getBytes(CharsetTool.defaultCharset()),
				content.getBytes(CharsetTool.defaultCharset()), Cipher.ENCRYPT_MODE));
	}

	/**
	 * AES解密
	 * 
	 * @param encodeRules 解密规则
	 * @param content 需解密16进制字符串
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public static String AESDecrypt(String encodeRules, String content) {
		if (StrTool.isAnyBlank(encodeRules, content)) {
			return "加密参数不能为空";
		}
		// 若是将此解密方法放到linux机器报错,则使用以下方法生成随机源
		// SecretKeySpec key2 = null;
		// SecureRandom random =
		// SecureRandom.getInstance("SHA1PRNG");
		// random.setSeed(encodeRules.getBytes());
		// keygen.init(128, random);
		byte[] byte_decode = aesCrypto(encodeRules.getBytes(CharsetTool.defaultCharset()), HexTool.decode(content),
				Cipher.DECRYPT_MODE);
		return new String(byte_decode, CharsetTool.defaultCharset());
	}

	private static byte[] aesCrypto(byte[] encodeRules, byte[] content, int mode) {
		try {
			// 1.构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			// 2.根据ecnodeRules规则初始化密钥生成器
			// 生成一个128位的随机源,根据传入的字节数组
			keygen.init(128, new SecureRandom(encodeRules));
			// 3.产生原始对称密钥
			SecretKey original_key = keygen.generateKey();
			// 4.获得原始对称密钥的字节数组
			byte[] raw = original_key.getEncoded();
			return AES(content, raw, mode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final byte[] AES(byte[] content, byte[] raw, int mode) {
		// 5.根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, "AES");
		// 6.根据指定算法AES生成密码器
		try {
			Cipher cip = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// 7.初始化密码器,第一个参数为加密(Encrypt_mode)或解密(Decrypt_mode),第二个参数为使用的KEY
			cip.init(mode, key);
			// 不要使用base64加密,会在前端传输中少字符数
			// 9.根据密码器的初始化方式--加密/解密
			return cip.doFinal(content);
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA生成公私钥
	 */
	public static Map<String, Object> createKey() {
		try {
			KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");
			key.initialize(1024);
			KeyPair keyPair = key.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
			return MapTool.builder("publicKey", Base64.getEncoder().encode(publicKey.getEncoded()))
					.put("privateKey", Base64.getEncoder().encode(privateKey.getEncoded())).build();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKey 公钥字符串
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static final RSAPublicKey loadPublicKey(String publicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] buffer = Base64.getDecoder().decode(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}

	/**
	 * 从字符串中加载私钥
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static RSAPrivateKey loadPrivateKey(String privateKeyStr)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] buffer = Base64.getDecoder().decode(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}

	/**
	 * RSA公钥加密
	 * 
	 * @param key
	 * @param xmlstr 加密内容长度受秘钥长度限制，若加密内容长度大于(秘钥长度(1024)/8-11=117), 则需要分段加密
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static String RsaEncrypt(PublicKey key, String xmlstr) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		byte[] plainText = xmlstr.getBytes(CharsetTool.defaultCharset());
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			int inputLen = plainText.length;
			int offSet = 0;
			for (int i = 0; inputLen - offSet > 0; offSet = i * 116) {
				byte[] cache;
				if (inputLen - offSet > 116) {
					cache = cipher.doFinal(plainText, offSet, 116);
				} else {
					cache = cipher.doFinal(plainText, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				++i;
			}
			return Base64.getEncoder().encodeToString(out.toByteArray());
		}
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param key 私钥
	 * @param encodedText 若是分段加密，则需要分段解密
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static String RsaDecrypt(PrivateKey key, String encodedText) throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		encodedText = encodedText.replaceAll("\r|\n", "");
		byte[] textb = Base64.getDecoder().decode(encodedText);
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			int inputLen = textb.length;
			int offSet = 0;
			for (int i = 0; inputLen - offSet > 0; offSet = i * 128) {
				byte[] cache;
				if (inputLen - offSet > 128) {
					cache = cipher.doFinal(textb, offSet, 128);
				} else {
					cache = cipher.doFinal(textb, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				++i;
			}
			return new String(out.toByteArray(), CharsetTool.defaultCharset());
		}
	}
}