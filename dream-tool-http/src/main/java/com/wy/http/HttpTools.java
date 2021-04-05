package com.wy.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.wy.collection.MapTool;

/**
 * Http工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-18 23:44:20
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HttpTools {

	/**
	 * 简单转换httpservletrequest请求中的参数为hashmap,数组会转成list
	 * 
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
}