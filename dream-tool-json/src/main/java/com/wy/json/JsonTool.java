package com.wy.json;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Json工具类,参考org.json修改
 * 
 * @author 飞花梦影
 * @date 2021-03-12 15:00:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JsonTool {

	/**
	 * 将Properties转换为JSONObject
	 * 
	 * @param properties {@link java.util.Properties}
	 * @return JSONObject
	 */
	public static JSONObject toJSONObject(Properties properties) {
		JSONObject jsonObject = new JSONObject();
		if (properties != null && !properties.isEmpty()) {
			Enumeration<?> enumProperties = properties.propertyNames();
			while (enumProperties.hasMoreElements()) {
				String name = (String) enumProperties.nextElement();
				jsonObject.put(name, properties.getProperty(name));
			}
		}
		return jsonObject;
	}

	/**
	 * 将JSONObject转换为Properties
	 * 
	 * @param jsonObject JSONObject
	 * @return {@link java.util.Properties}
	 */
	public static Properties toProperties(JSONObject jsonObject) {
		Properties properties = new Properties();
		if (jsonObject != null) {
			for (final String key : jsonObject.keySet()) {
				Object value = jsonObject.opt(key);
				if (!JSONObject.NULL.equals(value)) {
					properties.put(key, value.toString());
				}
			}
		}
		return properties;
	}
}