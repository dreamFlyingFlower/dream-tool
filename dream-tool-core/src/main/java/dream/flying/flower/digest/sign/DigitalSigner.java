package dream.flying.flower.digest.sign;

import java.security.Signature;
import java.util.Map;

import dream.flying.flower.ConstDigest;
import dream.flying.flower.digest.enums.CryptKeyType;

/**
 * 私钥签名,公钥验签 {@link Signature}
 * 
 * 签名并不保证数据的私密性,主要是防篡改
 * 
 * <pre>
 * 1.generateKey():生成公私钥
 * 2.A将公钥交给B,自己保留私钥
 * 3.A用私钥将数据data签名得到签名数据signedData
 * 4.A将data和signedData都发送给B,此时data和signedData都可能被截获
 * 5.B收到A发送的data和signedData,通过公钥对data进行加密并和A发送的signedData比对
 * 6.在保证私钥不泄漏的前提下(公钥是都可见的),只要data被篡改,那么signedData就会验签失败
 * </pre>
 *
 * @author 飞花梦影
 * @date 2024-09-25 23:21:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface DigitalSigner {

	/**
	 * 生成长度为1024的密钥对
	 * 
	 * @return 公私钥
	 */
	default Map<CryptKeyType, byte[]> generateKey() {
		return generateKey(ConstDigest.KEY_SIZE_1024);
	}

	/**
	 * 生成指定长度公私钥
	 * 
	 * @param length 密钥长度,安全起见,不要低于1024
	 * @return 公私钥
	 */
	Map<CryptKeyType, byte[]> generateKey(int length);

	/**
	 * 私钥签名
	 * 
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * @return 签名后数据
	 */
	byte[] sign(byte[] data, byte[] privateKey);

	/**
	 * 私钥签名
	 * 
	 * @param data 待签名数据
	 * @param privateKey 私钥
	 * @return 签名后数据
	 */
	String sign(String data, String privateKey);

	/**
	 * 公钥验签
	 * 
	 * @param data 待签名数据
	 * @param publicKey 公钥
	 * @param sign 签名数据
	 * @return true->验签成功;false->验签失败
	 */
	boolean verify(byte[] data, byte[] publicKey, byte[] sign);

	/**
	 * 公钥验签
	 * 
	 * @param data 经过base64编码的待签名原始数据
	 * @param publicKey 经过base64编码的公钥字符串
	 * @param sign 经过base64编码的签名数据
	 * @return true->验签成功;false->验签失败
	 */
	boolean verify(String data, String publicKey, String sign);
}