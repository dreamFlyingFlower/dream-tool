package dream.flying.flower.digest.enums;

import java.util.stream.Stream;

import dream.flying.flower.ConstDigest;

/**
 * Hmac算法名,需要注意JDK版本
 * 
 * @see https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#messagedigest-algorithms
 * @see https://docs.oracle.com/javase/10/docs/specs/security/standard-names.html#messagedigest-algorithms
 * @see https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#messagedigest-algorithms
 * @see https://docs.oracle.com/en/java/javase/12/docs/specs/security/standard-names.html#messagedigest-algorithms
 * @see https://docs.oracle.com/en/java/javase/13/docs/specs/security/standard-names.html#messagedigest-algorithms
 * 
 * @author 飞花梦影
 * @date 2021-04-06 17:00:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum HmacAlgorithmType {

	/**
	 * The HmacMD5 Message Authentication Code (MAC) algorithm specified in RFC 2104
	 * and RFC 1321.
	 * <p>
	 * Every implementation of the Java platform is required to support this
	 * standard MAC algorithm.
	 * </p>
	 */
	HMAC_MD5("HmacMD5"),

	/**
	 * The HmacSHA1 Message Authentication Code (MAC) algorithm specified in RFC
	 * 2104 and FIPS PUB 180-2.
	 * <p>
	 * Every implementation of the Java platform is required to support this
	 * standard MAC algorithm.
	 * </p>
	 */
	HMAC_SHA_1("HmacSHA1"),

	/**
	 * The HmacSHA224 Message Authentication Code (MAC) algorithm specified in RFC
	 * 2104 and FIPS PUB 180-2.
	 * <p>
	 * Every implementation of the Java 8+ platform is required to support this
	 * standard MAC algorithm.
	 * </p>
	 * 
	 * @since 1.11
	 */
	HMAC_SHA_224("HmacSHA224"),

	/**
	 * The HmacSHA256 Message Authentication Code (MAC) algorithm specified in RFC
	 * 2104 and FIPS PUB 180-2.
	 * <p>
	 * Every implementation of the Java platform is required to support this
	 * standard MAC algorithm.
	 * </p>
	 */
	HMAC_SHA_256("HmacSHA256"),

	/**
	 * The HmacSHA384 Message Authentication Code (MAC) algorithm specified in RFC
	 * 2104 and FIPS PUB 180-2.
	 * <p>
	 * This MAC algorithm is <em>optional</em>; not all implementations support it.
	 * </p>
	 */
	HMAC_SHA_384("HmacSHA384"),

	/**
	 * The HmacSHA512 Message Authentication Code (MAC) algorithm specified in RFC
	 * 2104 and FIPS PUB 180-2.
	 * <p>
	 * This MAC algorithm is <em>optional</em>; not all implementations support it.
	 * </p>
	 */
	HMAC_SHA_512("HmacSHA512");

	private final String name;

	HmacAlgorithmType(final String algorithm) {
		this.name = algorithm;
	}

	/**
	 * Gets the algorithm name.
	 *
	 * @return the algorithm name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The algorithm name.
	 *
	 * @see <a href=
	 *      "https://docs.oracle.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider">
	 *      Java 6 Cryptography Architecture Sun Providers Documentation</a>
	 * @see <a href=
	 *      "https://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html#SunJCEProvider">
	 *      Java 7 Cryptography Architecture Sun Providers Documentation</a>
	 * @see <a href=
	 *      "https://docs.oracle.com/javase/8/docs/technotes/guides/security/SunProviders.html#SunJCEProvider">
	 *      Java 8 Cryptography Architecture Sun Providers Documentation</a>
	 * @see <a href=
	 *      "https://docs.oracle.com/javase/9/security/oracleproviders.htm#JSSEC-GUID-A47B1249-593C-4C38-A0D0-68FA7681E0A7">
	 *      Java 9 Cryptography Architecture Sun Providers Documentation</a>
	 * @return The algorithm name ("HmacSHA512" for example)
	 */
	@Override
	public String toString() {
		return name;
	}

	public static HmacAlgorithmType of(String type) {
		return Stream.of(HmacAlgorithmType.values())
				.filter(t -> t.name.equalsIgnoreCase(type))
				.findFirst()
				.orElse(null);
	}

	/**
	 * 根据算法类型获取随机密钥长度
	 * 
	 * @param hmacAlgorithmType 算法类型
	 * @return 随机密钥长度
	 */
	public static int length(HmacAlgorithmType hmacAlgorithmType) {
		switch (hmacAlgorithmType) {
		case HMAC_SHA_1:
			return ConstDigest.HMAC_SHA1_SIZE;
		case HMAC_SHA_224:
		case HMAC_SHA_256:
			return ConstDigest.HMAC_SHA256_SIZE;
		case HMAC_MD5:
		case HMAC_SHA_384:
		case HMAC_SHA_512:
			return ConstDigest.HMAC_SHA512_SIZE;
		default:
			return ConstDigest.HMAC_SHA512_SIZE;
		}
	}
}