package dream.flying.flower.digest;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import dream.flying.flower.digest.enums.CryptKeyType;
import dream.flying.flower.digest.enums.CryptType;

/**
 * DH工具类
 * 
 * DH算法加解密流程
 * 
 * <pre>
 * 1.甲方构建密钥对,将公钥公布给乙方,将私钥保留;双方约定数据加密算法;乙方通过甲方公钥构建密钥对,将公钥公布给甲方,私钥保留
 * 2.甲方使用私钥、乙方公钥、约定数据加密算法构建本地密钥,然后通过本地密钥加密数据,发送给乙方加密后的数据
 * 3.乙方使用私钥、甲方公钥、约定数据加密算法构建本地密钥,然后通过本地密钥对数据解密
 * 4.乙方使用私钥、甲方公钥、约定数据加密算法构建本地密钥,然后通过本地密钥加密数据,发送给甲方加密后的数据
 * 5.甲方使用私钥、乙方公钥、约定数据加密算法构建本地密钥,然后通过本地密钥对数据解密
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-09-23 14:00:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DhHelper {

	/**
	 * 生成密钥对
	 */
	public static Map<CryptKeyType, Object> generateKey() throws Exception {
		// 实例化秘钥对生成器
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CryptType.DH.getType());
		// 初始化秘钥生成器,默认是1024 512-1024& 64的倍数
		keyPairGenerator.initialize(1024);
		// 生成秘钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 得到甲方的公钥
		DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
		// 得到甲方的私钥
		DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

		Map<CryptKeyType, Object> keyMap = new HashMap<>();
		keyMap.put(CryptKeyType.PUBLIC_KEY, publicKey);
		keyMap.put(CryptKeyType.PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 乙方根据甲方公钥初始化并返回秘钥对
	 */
	public static Map<CryptKeyType, Object> generateKey(byte[] key) throws Exception {
		// 将甲方公钥从字节数组中转换成PublicKey
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
		// 实例化秘钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(CryptType.DH.getType());
		// 产生甲方的公钥
		DHPublicKey dhPublicKey = (DHPublicKey) keyFactory.generatePublic(keySpec);
		// 剖析甲方的公钥,得到其参数
		DHParameterSpec dhParameterSpec = dhPublicKey.getParams();
		// 实例化秘钥对生成器
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CryptType.DH.getType());
		// 用甲方秘钥生成秘钥生成器
		keyPairGenerator.initialize(dhParameterSpec);
		// 产生密钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 得到乙方公钥
		DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
		// 得到乙方私钥
		DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

		// 将公约和私钥封装在Map中,方便以后使用
		Map<CryptKeyType, Object> keyMap = new HashMap<>();
		keyMap.put(CryptKeyType.PUBLIC_KEY, publicKey);
		keyMap.put(CryptKeyType.PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * 根据对方的公钥和自己私钥生成对称密码本地秘钥
	 * 
	 * @param publicKey 甲方公钥
	 * @param privateKey 乙方私钥
	 */
	public static byte[] getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
		// 实例化秘钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(CryptType.DH.getType());
		// 将公约从字节数组转化为PublicKey
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		// 将私钥从字节数组转化为PrivateKey
		PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(privateKey);
		PrivateKey priKey = keyFactory.generatePrivate(priKeySpec);

		// 准备以上公钥和私钥生成本地秘钥
		// 先实例化KeyAgreement
		KeyAgreement keyAgreement = KeyAgreement.getInstance(CryptType.DH.getType());
		// 用自己的私钥初始化KeyAgreement
		keyAgreement.init(priKey);
		// 结合对方公钥进行运算
		keyAgreement.doPhase(pubKey, true);
		// 生成本地秘钥SecretKey秘钥算法为对称密码算法,如DES AES 3DES
		SecretKey secretKey = keyAgreement.generateSecret(CryptType.DES.getType());
		return secretKey.getEncoded();
	}

	/**
	 * 从Map中取得公钥
	 */
	public static byte[] getPublicKey(Map<CryptKeyType, Object> keyMap) {
		DHPublicKey key = (DHPublicKey) keyMap.get(CryptKeyType.PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 从Map中取得私钥
	 */
	public static byte[] getPrivateKey(Map<CryptKeyType, Object> keyMap) {
		DHPrivateKey key = (DHPrivateKey) keyMap.get(CryptKeyType.PRIVATE_KEY);
		return key.getEncoded();
	}
}