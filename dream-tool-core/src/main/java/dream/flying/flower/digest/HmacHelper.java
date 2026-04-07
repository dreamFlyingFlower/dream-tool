package dream.flying.flower.digest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dream.flying.flower.binary.HexHelper;
import dream.flying.flower.digest.enums.HmacAlgorithmType;
import dream.flying.flower.lang.StrHelper;

/**
 * HMAC签名工具类
 *
 * @author 飞花梦影
 * @date 2026-04-07 18:45:07
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HmacHelper {

	/**
	 * 根据密钥对数据签名,生成指定位数数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @param hmacAlgorithmType 算法
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha(byte[] key, byte[] data, HmacAlgorithmType hmacAlgorithmType)
			throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance(hmacAlgorithmType.getName());
		SecretKeySpec secretKey = new SecretKeySpec(key, hmacAlgorithmType.getName());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	/**
	 * 根据密钥对数据签名,生成256位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha256(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
		return hmacSha(key, data, HmacAlgorithmType.HMAC_SHA_256);
	}

	/**
	 * 根据密钥对数据签名,生成256位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha256(String key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
		return hmacSha(StrHelper.toBytes(key), data, HmacAlgorithmType.HMAC_SHA_256);
	}

	/**
	 * 根据密钥对数据签名,生成256位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha256(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
		return hmacSha(StrHelper.toBytes(key), StrHelper.toBytes(data), HmacAlgorithmType.HMAC_SHA_256);
	}

	/**
	 * 根据密钥对数据签名,生成256位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后进行Base64的数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String hmacSha256Base64(String key, String data)
			throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] bytes = hmacSha256(StrHelper.toBytes(key), StrHelper.toBytes(data));
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * 根据密钥对数据签名,生成512位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha512(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
		return hmacSha(key, data, HmacAlgorithmType.HMAC_SHA_512);
	}

	/**
	 * 根据密钥对数据签名,生成512位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha512(String key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
		return hmacSha(StrHelper.toBytes(key), data, HmacAlgorithmType.HMAC_SHA_512);
	}

	/**
	 * 根据密钥对数据签名,生成512位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] hmacSha512(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
		return hmacSha(StrHelper.toBytes(key), StrHelper.toBytes(data), HmacAlgorithmType.HMAC_SHA_512);
	}

	/**
	 * 根据密钥对数据签名,生成512位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后进行Base64的数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String hmacSha512Base64(String key, String data)
			throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] bytes = hmacSha512(StrHelper.toBytes(key), StrHelper.toBytes(data));
		return Base64.getEncoder().encodeToString(bytes);
	}

	/**
	 * 根据密钥对数据签名,生成512位数据
	 * 
	 * @param key 密钥
	 * @param data 待签名数据
	 * @return 签名后进行Base64的数据
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String hmacSha512Hex(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] bytes = hmacSha512(StrHelper.toBytes(key), StrHelper.toBytes(data));
		return HexHelper.encodeHexString(bytes);
	}
}