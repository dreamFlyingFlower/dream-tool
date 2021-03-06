package com.wy.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.wy.collection.MapTool;
import com.wy.lang.StrTool;

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
		if (checkIp(ip)) {
			ip = ip.split(",")[0];
		}
		// apache http请求可能会有该值
		if (!checkIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		// weblogic请求可能会有该值
		if (!checkIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		// 某些代理可能会有该值
		if (!checkIp(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		// nginx可能会该值
		if (!checkIp(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (!checkIp(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	private static boolean checkIp(String ip) {
		if (StrTool.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断指定的IP地址是否在IP段里面。
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
}