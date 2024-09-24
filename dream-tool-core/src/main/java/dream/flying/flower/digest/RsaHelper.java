package dream.flying.flower.digest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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

	public static void main(String[] args) {
		// System.out.println(generateKey());

		String publicKey =
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkdpLBeKc0a+r6AWIlvzw7KVpzAiiA3JMdmDEWcWT8biv04CvzWlvgWI6t3nvMt003mozHFJmlOFNjQzmks9Rt+MK/D0iQq5V6lOnn58FE5TlKKXjiQK+pjdfUt748ua5l3PdO5MaHPsV9pHNjy5Xo06vSSelMt0e9UJ9f6nw6sS8KZTXBFVAWnJeTCnl5tKBDqPOBIdH6RLAVmH/wd1rh9gVLEJySmekx2R8TqGcanwebG+QuZcRMdp5ux5FPtf9Oh+0MlYS0ktHZQZg0By7xBLCtSJpPrdm59QD6QoPNJ202hXEvVWiaasm2tEsxHne2eDTFsd/666Kqie0i8pTPwIDAQAB";
		String privateKey =
				"MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCR2ksF4pzRr6voBYiW/PDspWnMCKIDckx2YMRZxZPxuK/TgK/NaW+BYjq3ee8y3TTeajMcUmaU4U2NDOaSz1G34wr8PSJCrlXqU6efnwUTlOUopeOJAr6mN19S3vjy5rmXc907kxoc+xX2kc2PLlejTq9JJ6Uy3R71Qn1/qfDqxLwplNcEVUBacl5MKeXm0oEOo84Eh0fpEsBWYf/B3WuH2BUsQnJKZ6THZHxOoZxqfB5sb5C5lxEx2nm7HkU+1/06H7QyVhLSS0dlBmDQHLvEEsK1Imk+t2bn1APpCg80nbTaFcS9VaJpqyba0SzEed7Z4NMWx3/rroqqJ7SLylM/AgMBAAECggEAbQVJjMWdyJLSIWV5erro70qZcTyGtaLLCrU8L4ojEsylCKgpQJZuFb0x/JE1PcThTtr9B0+EhAaA7EzXUKeNArHANVkQ8dzUNYqAQi0gI0npnN+a23/kt+Ot6gjm9tGFqnKdzHkhTMZV3W+QwlS2h0g6d6kdr45fJJ6lpjaF8UUk/L8KhmyckruSXsLbzFqy6j8q35OJNwh2OyRjj6lBm3rc9ViKKFBwOsDFAcY5k66vXwWJryKa5EaroENN+jVHj6pN2nybm8XX4Q/92jafmrQm/0DEhmMlK9dG31NEbLwkI0McxNFI1AB7aL2+Qa04JgzNFQXaTrPo/RAeSRy8eQKBgQDpfZV5X49/RT7voxOUX4ZCnoAC9jSBwgv9x9tXaoqYlWgKlEMDWlGSHV4PueX/O11NeJA2Nidm08Yy/n58blaAkX6hVWTxP8+wAFfYYU6+SStEZn1yickNABrDWj3AIxdHpyY3IyDqOhr1pD+ACyIk15uLTE0x5z1FszbhQIvsIwKBgQCf6dsnfvO8mjmOc/cHOdOry4Y3eoHdkQ5PXA51cNkAkKi4DMh3BzvWZKQhw7ZYL/MuDERwcWJu9dn0v5ZscxziguYpi2EW/jeSvM5v9tZCKX3alr3nGfr2qnVWk5WRsXHE8yRlo/UHQczIvHyOlDXZIKd1Uda3qSfc9ZjckZLQNQKBgF6Y+s5J4SF3NKByOaSLbyHehY6toWhQ7frL1hn8mFppbdWba+sX1KSWMV29u2lE6/BPQF42YpBOjKZsZWKUcYd1axCCQHtdlw9/lBeelQCTNGxp+blGCaayvCUZkpsdnHjzR5BfMx6ym+mZugBugk7a67HsH/pv7WsnyLxKImZxAoGBAIiC0Cz1OQjF92BeKoEmokXDpj3x/K6yR68DfOxQe076Lq1/bBoAONqaa2rPcffyZvvmSP5cLJVglsFSVA0cb4oGCIlqjGVeev5u4yO9acZtORzDFjYUfaLYfPZ9Ym9PNTnV/jouLtwVFcY+8W2dpCJIt+t1A4EhjAAaQHX6Z4QJAoGBAN/9J8lqGQd6WIvSo7yXbgbOT+u1lMhfWazrRTAcQiSDlQihU8JpYJgwW1zOvregUeLHGgrN38O+14Xf4qGxc1MhjQh5qjZkQsmrq/Pqa0Oq+JISe85URkt5q3Na+MZj/KvFV70eGQRMwKK8IvYAky/T6opMIFtIA2fk6wHjvvbx";

		String content =
				"fdsrwerewtgGERWGRWTRWFEWTGRWgdsgrew罡风等各位以太网同一人委托人武rtewytwywtrwtrwetrwtrwetewrttr" + "讽德诵功热骇人听闻特意认为他we";

		String encryptPrivate = encryptPrivate(privateKey, content);
		System.out.println(encryptPrivate);

		System.out.println(decryptPublic(publicKey, encryptPrivate));
	}

	/**
	 * 加解密
	 * 
	 * @param cipher 密码管理器
	 * @param keyLength 密钥长度
	 * @param content 加密或解密内容
	 * @param cipherType 密钥模式和填充模式
	 * @param encrypt 是否为加密
	 * @return 加解密后的内容
	 */
	private static byte[] crypt(Cipher cipher, int keyLength, byte[] content, CipherType cipherType, boolean encrypt) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			int blockSize = encrypt ? cipherType.encryptLength(keyLength) : cipherType.decryptLength(keyLength);
			int length = content.length;
			int offset = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加解密
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
			out.flush();
			return out.toByteArray();
		} catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(PrivateKey privateKey, byte[] content) {
		return decrypt(privateKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param content 未经Base64解码的加密数据
	 * @param cipherType 加密模式以及填充模式
	 * @return 解密后字符串
	 */
	public static String decrypt(PrivateKey privateKey, byte[] content, CipherType cipherType) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			// 解密数据
			Cipher cipher = Cipher.getInstance(cipherType.getType());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength();
			return new String(crypt(cipher, keyLength, content, cipherType, false), CharsetHelper.defaultCharset());
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(PrivateKey privateKey, String content) {
		return decrypt(privateKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKey 私钥
	 * @param content 未经Base64解码的加密数据
	 * @param cipherType 加密模式以及填充模式
	 * @return 解密后字符串
	 */
	public static String decrypt(PrivateKey privateKey, String content, CipherType cipherType) {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		byte[] encryptedData = Base64.getDecoder().decode(content.replaceAll("\r|\n", ""));
		return decrypt(privateKey, encryptedData, cipherType);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, byte[] content) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return decrypt(privateKey, content);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, byte[] content, CipherType cipherType) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return decrypt(privateKey, content, cipherType);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, String content) {
		return decrypt(privateKeyStr, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA);
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @param cipherType 加密模式以及填充模式
	 * @return 解密后字符串
	 */
	public static String decrypt(String privateKeyStr, String content, CipherType cipherType) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return decrypt(privateKey, content, cipherType);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKey 公钥
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decryptPublic(PublicKey publicKey, byte[] content) {
		return decryptPublic(publicKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKey 公钥
	 * @param content 未经Base64解码的加密数据
	 * @param cipherType 加密模式以及填充模式
	 * @return 解密后字符串
	 */
	public static String decryptPublic(PublicKey publicKey, byte[] content, CipherType cipherType) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			// 解密数据
			Cipher cipher = Cipher.getInstance(cipherType.getType());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength();
			return new String(crypt(cipher, keyLength, content, cipherType, false), CharsetHelper.defaultCharset());
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKey 公钥
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decryptPublic(PublicKey publicKey, String content) {
		return decryptPublic(publicKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKey 公钥
	 * @param content 未经Base64解码的加密数据
	 * @param cipherType 加密模式以及填充模式
	 * @return 解密后字符串
	 */
	public static String decryptPublic(PublicKey publicKey, String content, CipherType cipherType) {
		// base64编码规定一行字符串不能超过76个,超过换行,换行符会导致编码失败
		byte[] encryptedData = Base64.getDecoder().decode(content.replaceAll("\r|\n", ""));
		return decryptPublic(publicKey, encryptedData, cipherType);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decryptPublic(String publicKeyStr, byte[] content) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return decryptPublic(publicKey, content);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decryptPublic(String publicKeyStr, byte[] content, CipherType cipherType) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return decryptPublic(publicKey, content, cipherType);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @return 解密后字符串
	 */
	public static String decryptPublic(String publicKeyStr, String content) {
		return decryptPublic(publicKeyStr, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 未经Base64解码的加密数据
	 * @param cipherType 加密模式以及填充模式
	 * @return 解密后字符串
	 */
	public static String decryptPublic(String publicKeyStr, String content, CipherType cipherType) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return decryptPublic(publicKey, content, cipherType);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, byte[] content) {
		return encrypt(publicKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, byte[] content, CipherType cipherType) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(cipherType.getType());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength();
			return Base64.getEncoder().encodeToString(crypt(cipher, keyLength, content, cipherType, true));
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content) {
		return encrypt(publicKey, content, CharsetHelper.defaultCharset());
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content, Charset charset) {
		return encrypt(publicKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA, charset);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content, CipherType cipherType, Charset charset) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset(charset));
		return encrypt(publicKey, plainText, cipherType);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKey 公钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(PublicKey publicKey, String content, String charset) {
		return encrypt(publicKey, content, CharsetHelper.defaultCharset(charset));
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, byte[] content) {
		return encrypt(publicKeyStr, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, byte[] content, CipherType cipherType) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return encrypt(publicKey, content);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, String content) {
		return encrypt(publicKeyStr, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param publicKeyStr 公钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @return Base64编码后的的加密字符串
	 */
	public static String encrypt(String publicKeyStr, String content, CipherType cipherType) {
		RSAPublicKey publicKey = publicKey(publicKeyStr);
		return encrypt(publicKey, content, cipherType, CharsetHelper.defaultCharset());
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKey 私钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(PrivateKey privateKey, byte[] content) {
		return encryptPrivate(privateKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKey 私钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(PrivateKey privateKey, byte[] content, CipherType cipherType) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			Cipher cipher = Cipher.getInstance(cipherType.getType());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength();
			return Base64.getEncoder().encodeToString(crypt(cipher, keyLength, content, cipherType, true));
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
			throw new ResultException(e.getMessage());
		}
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKey 私钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(PrivateKey privateKey, String content) {
		return encryptPrivate(privateKey, content, CharsetHelper.defaultCharset());
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKey 私钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(PrivateKey privateKey, String content, Charset charset) {
		return encryptPrivate(privateKey, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE, charset);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKey 私钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(PrivateKey privateKey, String content, CipherType cipherType, Charset charset) {
		byte[] plainText = content.getBytes(CharsetHelper.defaultCharset(charset));
		return encryptPrivate(privateKey, plainText, cipherType);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKey 私钥
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param charset 字符集
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(PrivateKey privateKey, String content, String charset) {
		return encryptPrivate(privateKey, content, CharsetHelper.defaultCharset(charset));
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(String privateKeyStr, byte[] content) {
		return encryptPrivate(privateKeyStr, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(String privateKeyStr, byte[] content, CipherType cipherType) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return encryptPrivate(privateKey, content);
	}

	/**
	 * RSA公钥解密,私钥加密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(String privateKeyStr, String content) {
		return encryptPrivate(privateKeyStr, content, ConstDigest.DEFAULT_CIPHER_TYPE_RSA_PRIVATE);
	}

	/**
	 * RSA公钥加密,私钥解密
	 * 
	 * @param privateKeyStr 私钥字符串
	 * @param content 加密内容长度受秘钥长度限制,若加密内容超过特定值, 则需要分段加密
	 * @param cipherType 加密模式以及填充模式
	 * @return Base64编码后的的加密字符串
	 */
	public static String encryptPrivate(String privateKeyStr, String content, CipherType cipherType) {
		RSAPrivateKey privateKey = privateKey(privateKeyStr);
		return encryptPrivate(privateKey, content, cipherType, CharsetHelper.defaultCharset());
	}

	/**
	 * RSA生成公私钥,默认密钥长度为2048
	 * 
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<CryptKeyType, String> generateKey() {
		return generateKey(ConstDigest.KEY_SIZE_2048);
	}

	/**
	 * RSA生成公私钥,默认密钥长度为2048
	 * 
	 * @param encodeType 编码类型,默认base64
	 * @return 公私钥键值对
	 */
	public static Map<CryptKeyType, String> generateKey(EncodeType encodeType) {
		return generateKey(ConstDigest.KEY_SIZE_2048, encodeType);
	}

	/**
	 * RSA生成指定长度公私钥,长度是64倍数
	 * 
	 * @param length 密钥长度
	 * @return 公私钥键值对,公私钥都已经经过base64编码
	 */
	public static Map<CryptKeyType, String> generateKey(int length) {
		return generateKey(length, EncodeType.BASE64);
	}

	/**
	 * RSA生成指定长度公私钥,长度是64倍数
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
		if (length % ConstDigest.KEY_SIZE_MULTIPLE != 0 || length < ConstDigest.KEY_SIZE_MIN
				|| length > ConstDigest.KEY_SIZE_MAX) {
			throw new ResultException("密钥长度错误,必须是64的倍数,且长度在512至65535之间");
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
	 * RSA私钥签名,公钥验签
	 * 
	 * @param content 签名数据
	 * @param privateKey 私钥
	 * @return 签名后的字节数组
	 */
	public static byte[] sign(byte[] content, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance(ConstDigest.DEFAULT_SIGNATURE_RSA.getType());
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
	 * @return 签名后的字节数组
	 */
	public static byte[] sign(String content, String privateKeyStr) {
		return sign(content, privateKeyStr, CharsetHelper.defaultCharset());
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
	 * RSA私钥签名,公钥验签,结果默认经过base64编码
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
	 * @param charset 字符集
	 * @return Base64编码后的签名字符串
	 */
	public static String signString(String content, String privateKeyStr, Charset charset) {
		return signString(content.getBytes(CharsetHelper.defaultCharset(charset)), privateKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param data 原数据字节数组
	 * @param sign 签名后经过Base64解码的字节数组
	 * @param publicKey 公钥
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance(ConstDigest.DEFAULT_SIGNATURE_RSA.getType());
			signature.initVerify(publicKey);
			signature.update(content);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 签名数据
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verify(byte[] content, byte[] sign, String publicKeyStr) {
		PublicKey publicKey = publicKey(publicKeyStr);
		return verify(content, sign, publicKey);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 未经Base64解码的签名数据
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verify(byte[] content, String sign, String publicKeyStr) {
		return verify(content, Base64.getDecoder().decode(sign), publicKeyStr);
	}

	/**
	 * RSA公钥验签
	 * 
	 * @param content 原数据
	 * @param sign 签名后的字符串
	 * @param publicKeyStr 公钥字符串
	 * @return 验证是否正确.true->正确,false->错误
	 */
	public static boolean verify(String content, String sign, String publicKeyStr) {
		return verify(content.getBytes(), Base64.getDecoder().decode(sign), publicKeyStr);
	}
}