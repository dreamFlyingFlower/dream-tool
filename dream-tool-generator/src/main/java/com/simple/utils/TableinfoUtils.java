package com.simple.utils;

import java.util.Map;

import com.simple.model.Tableinfo;
import com.simple.properties.ConfigProperties;

import dream.flying.flower.lang.StrHelper;

/**
 * 表信息工具类
 * 
 * @author ParadiseWY
 * @date 2020-12-13 22:08:08
 * @git {@link https://github.com/mygodness100}
 */
public class TableinfoUtils {

	private static ConfigProperties config = SpringContextUtils.getBean(ConfigProperties.class);

	/**
	 * Mybatis结果转换成Tableinfo
	 * 
	 * @param map 表信息map
	 * @return 转换后的结果
	 */
	public static Tableinfo generateTableinfo(Map<String, Object> map) {
		return Tableinfo.builder().tableName(String.valueOf(map.get("tableName"))).engine(String.valueOf("engine"))
				.className(
						tableToJava(String.valueOf(map.get("tableName")), config.getDatabase().getTablePrefix(), true))
				.objectName(
						tableToJava(String.valueOf(map.get("tableName")), config.getDatabase().getTablePrefix(), false))
				.comments(String.valueOf(map.get("tableComment"))).build();
	}

	/**
	 * Mybatis结果转换成Tableinfo的map
	 * 
	 * @param map 表信息map
	 * @return 添加其他信息之后的结果
	 */
	public static void generateTableinfoMap(Map<String, Object> map) {
		map.put("className",
				tableToJava(String.valueOf(map.get("tableName")), config.getDatabase().getTablePrefix(), true));
		map.put("objectName",
				tableToJava(String.valueOf(map.get("tableName")), config.getDatabase().getTablePrefix(), false));
	}

	/**
	 * 表名转换成Java类名
	 * 
	 * @param tableName 表名
	 * @param tablePrefixs 表名前缀
	 * @param clsOrObj 生成类名或实例名,true类名,false实例名
	 * @return java类名或实例名
	 */
	public static String tableToJava(String tableName, String tablePrefixs, boolean clsOrObj) {
		if (StrHelper.isBlank(tableName)) {
			return null;
		}
		if (StrHelper.isNotBlank(tablePrefixs)) {
			for (String tablePrefix : tablePrefixs.split(",")) {
				if (tableName.startsWith(tablePrefix)) {
					tableName = tableName.replaceFirst(tablePrefix, "");
					break;
				}
			}
		}
		return StrHelper.underline2Hump(tableName, clsOrObj);
	}
}