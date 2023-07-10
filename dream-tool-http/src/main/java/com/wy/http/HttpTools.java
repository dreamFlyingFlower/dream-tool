package com.wy.http;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.wy.collection.MapTool;
import com.wy.lang.StrTool;
import com.wy.util.ArrayTool;

/**
 * Http工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 23:44:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HttpTools {

	private static final Pattern PATTERN_CIDR =
			Pattern.compile("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})$");

	/**
	 * 简单转换httpservletrequest请求中的参数为hashmap,数组会转成list
	 * 
	 * @param request 请求
	 * @return 转换后的数据
	 */
	public static Map<String, Object> transReq(HttpServletRequest request) {
		Map<String, String[]> parameters = request.getParameterMap();
		if (MapTool.isEmpty(parameters)) {
			return new HashMap<>();
		}
		Map<String, Object> result = new HashMap<>();
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			if (1 == entry.getValue().length) {
				result.put(entry.getKey(), entry.getValue()[0]);
			} else {
				result.put(entry.getKey(), Arrays.asList(entry.getValue()));
			}
		}
		return result;
	}

	/**
	 * 获取用户真实IP地址.用户有可能使用了代理避免真实IP地址.
	 * 
	 * @param request 请求
	 * @return ip
	 */
	public static String getIp(HttpServletRequest request) {
		// 如果通过了多级反向代理,X-Forwarded-For的值并不止一个,而是一串IP值.取第一个非unknown的有效IP字符串
		String ip = request.getHeader("x-forwarded-for");
		if (!checkIp(ip)) {
			ip = ip.split(",")[0];
		}
		// apache http请求可能会有该值
		if (checkIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		// weblogic请求可能会有该值
		if (checkIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		// 某些代理可能会有该值
		if (checkIp(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (checkIp(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// nginx可能会该值
		if (checkIp(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (checkIp(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
	}

	/**
	 * 检测给定字符串是否为未知,多用于检测HTTP请求相关
	 *
	 * @param checkString 被检测的字符串
	 * @return 是否未知
	 */
	private static boolean checkIp(String ip) {
		if (StrTool.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			return true;
		}
		return false;
	}

	/**
	 * 从多级反向代理中获得第一个非unknown IP地址
	 *
	 * @param ip 获得的IP地址
	 * @return 第一个非unknown IP地址
	 */
	public static String getMultistageReverseProxyIp(String ip) {
		// 多级反向代理检测
		if (ip != null && ip.indexOf(",") > 0) {
			final String[] ips = ip.trim().split(",");
			for (String subIp : ips) {
				if (!checkIp(subIp)) {
					ip = subIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 检查是否为内部IP地址
	 * 
	 * @param ip IP地址
	 * @return 结果
	 */
	public static boolean internalIp(String ip) {
		byte[] addr = textToNumericFormatV4(ip);
		return internalIp(addr) || "127.0.0.1".equals(ip);
	}

	/**
	 * 检查是否为内部IP地址
	 * 
	 * @param addr byte地址
	 * @return 结果
	 */
	private static boolean internalIp(byte[] addr) {
		if (ArrayTool.isEmpty(addr) || addr.length < 2) {
			return true;
		}
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
		case SECTION_1:
			return true;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4) {
				return true;
			}
		case SECTION_5:
			switch (b1) {
			case SECTION_6:
				return true;
			}
		default:
			return false;
		}
	}

	/**
	 * 将IPv4地址转换成字节
	 * 
	 * @param text IPv4地址
	 * @return byte 字节
	 */
	public static byte[] textToNumericFormatV4(String text) {
		if (text.length() == 0) {
			return null;
		}

		byte[] bytes = new byte[4];
		String[] elements = text.split("\\.", -1);
		try {
			long l;
			int i;
			switch (elements.length) {
			case 1:
				l = Long.parseLong(elements[0]);
				if ((l < 0L) || (l > 4294967295L)) {
					return null;
				}
				bytes[0] = (byte) (int) (l >> 24 & 0xFF);
				bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
				bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 2:
				l = Integer.parseInt(elements[0]);
				if ((l < 0L) || (l > 255L)) {
					return null;
				}
				bytes[0] = (byte) (int) (l & 0xFF);
				l = Integer.parseInt(elements[1]);
				if ((l < 0L) || (l > 16777215L)) {
					return null;
				}
				bytes[1] = (byte) (int) (l >> 16 & 0xFF);
				bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 3:
				for (i = 0; i < 2; ++i) {
					l = Integer.parseInt(elements[i]);
					if ((l < 0L) || (l > 255L)) {
						return null;
					}
					bytes[i] = (byte) (int) (l & 0xFF);
				}
				l = Integer.parseInt(elements[2]);
				if ((l < 0L) || (l > 65535L)) {
					return null;
				}
				bytes[2] = (byte) (int) (l >> 8 & 0xFF);
				bytes[3] = (byte) (int) (l & 0xFF);
				break;
			case 4:
				for (i = 0; i < 4; ++i) {
					l = Integer.parseInt(elements[i]);
					if ((l < 0L) || (l > 255L)) {
						return null;
					}
					bytes[i] = (byte) (int) (l & 0xFF);
				}
				break;
			default:
				return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return bytes;
	}

	/**
	 * 获取IP地址
	 * 
	 * @return 本地IP地址
	 */
	public static String getHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
		return "127.0.0.1";
	}

	/**
	 * 获取主机名
	 * 
	 * @return 本地主机名
	 */
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return "未知";
	}

	/**
	 * 判断指定的IP地址是否在IP段里面
	 * 
	 * @param ipAddr IP地址
	 * @param cidrAddr 用CIDR表示法的IP段信息
	 * @return true->在,false->不在
	 */
	public static boolean isIpInRange(String ipAddr, String cidrAddr) {
		Matcher matcher = PATTERN_CIDR.matcher(cidrAddr);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid CIDR address: " + cidrAddr);
		}

		int[] minIpParts = new int[4];
		int[] maxIpParts = new int[4];
		String[] ipParts = matcher.group(1).split("\\.");
		int intMask = Integer.parseInt(matcher.group(2));

		for (int i = 0; i < ipParts.length; i++) {
			int ipPart = Integer.parseInt(ipParts[i]);
			if (intMask >= 8) {
				minIpParts[i] = ipPart;
				maxIpParts[i] = ipPart;
				intMask -= 8;
			} else if (intMask > 0) {
				minIpParts[i] = ipPart >> intMask;
				maxIpParts[i] = ipPart | (0xFF >> intMask);
				intMask = 0;
			} else {
				minIpParts[i] = 1;
				maxIpParts[i] = 0xFF - 1;
			}
		}

		String[] realIpParts = ipAddr.split("\\.");
		for (int i = 0; i < realIpParts.length; i++) {
			int realIp = Integer.parseInt(realIpParts[i]);
			if (realIp < minIpParts[i] || realIp > maxIpParts[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取mac地址
	 * 
	 * @return Mac地址
	 */
	public static String getMacAddress() {
		StringBuilder sb = new StringBuilder();
		try {
			// 取mac地址
			byte[] macAddressBytes = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
			for (int i = 0; i < macAddressBytes.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				// mac[i] & 0xFF 是为了把byte转化为正整数
				String s = Integer.toHexString(macAddressBytes[i] & 0xFF);
				sb.append(s.length() == 1 ? 0 + s : s);
			}
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		return sb.toString().trim().toUpperCase();
	}
}