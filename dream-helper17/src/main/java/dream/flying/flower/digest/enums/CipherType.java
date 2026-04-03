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
public enum CipherType {

	/**
	 * 未确定分段最大长度,AES CBC加密和填充模式
	 */
	AES_CBC7_CIPHER("AES/CBC/PKCS7Padding") {

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
	 * 未确定分段最大长度
	 */
	AES_CIPHER("AES/ECB/PKCS5Padding") {

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
	 * 未确定分段最大长度,AES GCM默认加密和填充模式,暂不支持其他GCM模式
	 */
	AES_GCM_CIPHER("AES/GCM/NoPadding") {

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
	 * 未确定分段最大长度
	 */
	DES_CIPHER("DES/ECB/PKCS5Padding") {

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
	 * Java默认模式
	 * 
	 * 原文每行最大数据= 密钥位数(bit)长度/8 - 11
	 */
	RSA_CIPHER("RSA/ECB/PKCS1Padding") {

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
	RSA_ECB_OAEP_SHA1("RSA/ECB/OAEPWithSHA-1AndMGF1Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return (keyLength / 8) - 20 * 2 - 2;
		}

		@Override
		public int decryptLength(int keyLength) {
			return keyLength / 8;
		}
	},

	/**
	 * 推荐使用,更安全
	 * 
	 * 原文每行最大数据= 密钥位数(bit)长度/8 - 经过SHA256摘要运算后的字符长度(固定为32字节,UTF8下为64个字符) - 2
	 */
	RSA_ECB_OAEP_SHA256("RSA/ECB/OAEPWithSHA-256AndMGF1Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return (keyLength / 8) - 32 * 2 - 2;
		}

		@Override
		public int decryptLength(int keyLength) {
			return keyLength / 8;
		}
	},

	/**
	 * 原文每行最大数据= 密钥位数(bit)长度/8 - 经过SHA384摘要运算后的字符长度(固定为48字节,UTF8下为96个字符) - 2
	 */
	RSA_ECB_OAEP_SHA384("RSA/ECB/OAEPWithSHA-384AndMGF1Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return (keyLength / 8) - 48 * 2 - 2;
		}

		@Override
		public int decryptLength(int keyLength) {
			return keyLength / 8;
		}
	},

	/**
	 * 注意:当密钥长度小于2048时,此模式无法正确加解密
	 * 
	 * 原文每行最大数据= 密钥位数(bit)长度/8 - 经过SHA512摘要运算后的字符长度(固定为64字节,UTF8下为128个字符) - 2
	 */
	RSA_ECB_OAEP_SHA512("RSA/ECB/OAEPWithSha-512AndMGF1Padding") {

		@Override
		public int encryptLength(int keyLength) {
			return (keyLength / 8) - 64 * 2 - 2;
		}

		@Override
		public int decryptLength(int keyLength) {
			return keyLength / 8;
		}
	};

	private String type;

	private CipherType(String type) {
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