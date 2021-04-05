package com.wy.enums.http;

import java.util.HashMap;
import java.util.Map;

import com.wy.lang.StrTool;

/**
 * Http请求方式
 * 
 * @author 飞花梦影
 * @date 2020-04-08 12:24:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum HttpMethod {

	GET,
	HEAD,
	POST,
	PUT,
	PATCH,
	DELETE,
	OPTIONS,
	TRACE;

	/** 请求方式缓存 */
	private static final Map<String, HttpMethod> MAPPINGS = new HashMap<>(24);

	static {
		for (HttpMethod httpMethod : values()) {
			MAPPINGS.put(httpMethod.name(), httpMethod);
			MAPPINGS.put(httpMethod.name().toLowerCase(), httpMethod);
		}
	}

	public static HttpMethod resolve(String method) {
		return (StrTool.isNotBlank(method) ? MAPPINGS.get(method) : null);
	}

	public boolean matches(String method) {
		return (this == resolve(method));
	}
}