package dream.flying.flower.digest.enums;

/**
 * 加密模式以及填充模式,如PKCS5Padding,MGF1Padding等
 * 
 * 运算模式:CBC,默认ECB
 * 
 * 填充模式:NoPadding,默认PKCS1Padding(填充长度11),PKCS5Padding,MGF1Padding(填充长度2)
 * 
 * @auther 飞花梦影
 * @date 2021-06-19 20:32:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum CipherPaddingType {

	/**
	 * 无填充
	 */
	NO_PADDING("NoPadding") {

		@Override
		public int encryptLength(int keyLength) {
			return 0;
		}

		@Override
		public int decryptLength(int keyLength) {
			return 0;
		}
	},

	/**
	 * PKCS1Padding填充模式
	 */
	PKCS1_Padding("PKCS1Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return 11;
		}

		@Override
		public int decryptLength(int keyLength) {
			return 0;
		}
	},

	/**
	 * PKCS5Padding填充模式
	 */
	PKCS5_PADDING("PKCS5Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return 0;
		}

		@Override
		public int decryptLength(int keyLength) {
			return 0;
		}
	},

	/**
	 * PKCS7Padding填充模式
	 */
	PKCS7_PADDING("PKCS7Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return (keyLength / 8) - 11;
		}

		@Override
		public int decryptLength(int keyLength) {
			return keyLength / 8;
		}
	},

	/**
	 * 原文每行最大数据= 密钥位数(bit)长度/8 - 经过SHA1摘要运算后的字符长度(固定为20字节,UTF8下为40个字符) - 2
	 */
	RSA_CIPHER1("MGF1Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return (keyLength / 8) - 20 * 2 - 2;
		}

		@Override
		public int decryptLength(int keyLength) {
			return keyLength / 8;
		}
	};

	private String type;

	private CipherPaddingType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	/**
	 * 加密时原文每行最大数据
	 * 
	 * @param keyLength 密钥位数(bit)
	 * @return 原文每行最大数据
	 */
	public abstract int encryptLength(int keyLength);

	/**
	 * 解密时原文每行最大数据
	 * 
	 * @param keyLength 密钥位数(bit)
	 * @return 原文每行最大数据
	 */
	public abstract int decryptLength(int keyLength);
}