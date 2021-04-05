package com.wy.primitive;

/**
 * Byte工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-23 00:28:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ByteTool {

	/**
	 * 将int转换为字节数组
	 * 
	 * @param num int
	 * @return 字节数组
	 */
	public static byte[] int2Bytes(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) num;
		bytes[1] = (byte) (num >> 8);
		bytes[2] = (byte) (num >> 16);
		bytes[3] = (byte) (num >> 24);
		return bytes;
	}

	/**
	 * 将字节数组转换为int
	 * 
	 * @param bytes 字节数组
	 * @return int
	 */
	public static int bytes2Int(byte[] bytes) {
		int b0 = bytes[0] & 0xFF;
		int b1 = (bytes[1] & 0xFF) << 8;
		int b2 = (bytes[2] & 0xFF) << (2 * 8);
		int b3 = (bytes[3] & 0xFF) << (3 * 8);
		return b0 | b1 | b2 | b3;
	}
}