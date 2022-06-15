package com.wy.util;

/**
 * IP转换工具类
 *
 * @author 飞花梦影
 * @date 2022-06-15 09:38:30
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class IpTool {

	/**
	 * 将字符串IP转换成long,再存入到MySQL中.在MySQL中使用INET_NTOA()转换IPV4,INET6_NTOA()转换IPV6
	 * 
	 * @param ipStr 字符串IP
	 * @return IP对应的long值
	 */
	public static long ip2Long(String ipStr) {
		String[] ip = ipStr.split("\\.");
		return (Long.valueOf(ip[0]) << 24) + (Long.valueOf(ip[1]) << 16) + (Long.valueOf(ip[2]) << 8)
		        + Long.valueOf(ip[3]);
	}

	/**
	 * 将IP的long值转换成字符串.在MySQL中使用INET_NTOA()转换IPV4,INET6_NTOA()转换IPV6
	 * 
	 * @param ipLong IP的long值
	 * @return long值对应的字符串
	 */
	public static String long2Ip(long ipLong) {
		StringBuilder ip = new StringBuilder();
		ip.append(ipLong >>> 24).append(".");
		ip.append((ipLong >>> 16) & 0xFF).append(".");
		ip.append((ipLong >>> 8) & 0xFF).append(".");
		ip.append(ipLong & 0xFF);
		return ip.toString();
	}
}