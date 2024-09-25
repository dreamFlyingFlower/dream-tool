package dream.flying.flower.digest.sign;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.digest.RsaHelper;
import dream.flying.flower.digest.enums.CryptKeyType;
import dream.flying.flower.digest.enums.CryptType;
import dream.flying.flower.digest.enums.KeyPairType;

/**
 * RSA数字签名.默认签名算法为SHA1,默认密钥大小为1024,签名器支持MD5和SHA1
 *
 * @author 飞花梦影
 * @date 2024-07-20 12:21:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class RsaDigitalSigner implements DigitalSigner {

	public static final KeyPairType KEY_ALGORTHM = KeyPairType.RSA;

	private static final String SIGNATURE_ALGORITHM = CryptType.SHA1_WITH_RSA.getType();

	public byte[] sign(byte[] dataBytes, byte[] privateKeyBytes, String algorithm) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM.name());
			PrivateKey signPrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(signPrivateKey);
			signature.update(dataBytes);
			return signature.sign();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean verify(byte[] dataBytes, byte[] publicKeyBytes, byte[] signBytes, String algorithm) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM.name());
			PublicKey verifyPublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(verifyPublicKey);
			signature.update(dataBytes);
			return signature.verify(signBytes);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Map<CryptKeyType, byte[]> generateKey(int length) {
		return RsaHelper.generateKey(length);
	}

	@Override
	public byte[] sign(byte[] dataBytes, byte[] privateKeyBytes) {
		return sign(dataBytes, privateKeyBytes, SIGNATURE_ALGORITHM);
	}

	@Override
	public String sign(String data, String privateKey) {
		byte[] keyBytes = Base64Helper.decode(privateKey);
		byte[] dataBytes = data.getBytes();
		byte[] signature = sign(dataBytes, keyBytes);
		return Base64Helper.encodeString(signature);
	}

	@Override
	public boolean verify(byte[] dataBytes, byte[] publicKeyBytes, byte[] signBytes) {
		return verify(dataBytes, publicKeyBytes, signBytes, SIGNATURE_ALGORITHM);
	}

	@Override
	public boolean verify(String data, String publicKey, String sign) {
		byte[] keyBytes = Base64Helper.decode(publicKey);
		byte[] dataBytes = data.getBytes();
		byte[] signBytes = Base64Helper.decode(sign);
		return verify(dataBytes, keyBytes, signBytes);
	}
}