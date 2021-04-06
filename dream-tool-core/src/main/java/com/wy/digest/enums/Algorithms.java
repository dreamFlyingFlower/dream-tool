package com.wy.digest.enums;

import java.security.MessageDigest;

/**
 * {@link MessageDigest}算法名,需要注意JDK版本
 * 
 * @author 飞花梦影
 * @date 2021-04-06 17:00:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum Algorithms {

	/**
	 * The MD2 message digest algorithm defined in RFC 1319.
	 */
	MD2("MD2"),

	/**
	 * The MD5 message digest algorithm defined in RFC 1321.
	 */
	MD5("MD5"),

	/**
	 * The SHA-1 hash algorithm defined in the FIPS PUB 180-2.
	 */
	SHA_1("SHA-1"),

	/**
	 * The SHA-224 hash algorithm defined in the FIPS PUB 180-3.需要JDK8以上
	 */
	SHA_224("SHA-224"),

	/**
	 * The SHA-256 hash algorithm defined in the FIPS PUB 180-2.
	 */
	SHA_256("SHA-256"),

	/**
	 * The SHA-384 hash algorithm defined in the FIPS PUB 180-2.
	 */
	SHA_384("SHA-384"),

	/**
	 * The SHA-512 hash algorithm defined in the FIPS PUB 180-2.
	 */
	SHA_512("SHA-512"),

	/**
	 * The SHA-512 hash algorithm defined in the FIPS PUB 180-4.需要JDK9以上
	 */
	SHA_512_224("SHA-512/224"),

	/**
	 * The SHA-512 hash algorithm defined in the FIPS PUB 180-4.需要JDK9以上
	 */
	SHA_512_256("SHA-512/256"),

	/**
	 * The SHA3-224 hash algorithm defined in the FIPS PUB 202.需要JDK9以上
	 */
	SHA3_224("SHA3-224"),

	/**
	 * The SHA3-256 hash algorithm defined in the FIPS PUB 202.需要JDK9以上
	 */
	SHA3_256("SHA3-256"),

	/**
	 * The SHA3-384 hash algorithm defined in the FIPS PUB 202.需要JDK9以上
	 */
	SHA3_384("SHA3-384"),

	/**
	 * The SHA3-512 hash algorithm defined in the FIPS PUB 202.需要JDK9以上
	 */
	SHA3_512("SHA3-512");

	private String name;

	private Algorithms(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}