package com.simple.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import com.alibaba.fastjson.JSON;
import com.simple.model.Columninfo;
import com.simple.model.Tableinfo;
import com.simple.properties.ConfigProperties;
import com.wy.collection.ListTool;
import com.wy.collection.MapTool;
import com.wy.lang.StrTool;
import com.wy.third.json.JsonTools;
import com.wy.util.DateTool;

/**
 * 代码生成器 工具类
 * 
 * @author ParadiseWY
 * @date 2020-12-11 15:20:04
 * @git {@link https://github.com/mygodness100}
 */
public class GenerateUtils {

	/**
	 * 生成代码
	 * 
	 * @param table 单表对应关系
	 * @param columns 所有表中属性对应关系
	 * @param config 自定义配置信息
	 * @param localOrRemote 本地生成或远程传输,true本地生成,false远程传输
	 */
	public static byte[] generateCode(Tableinfo tableinfo, ConfigProperties config, boolean localOrRemote,
			DataSourceProperties dataSourceProperties) {
		// 封装模板数据
		Map<String,
				Object> map = MapTool.builder("tableinfo", tableinfo).put("tableName", tableinfo.getTableName())
						.put("datasource", dataSourceProperties).put("primaryKey", tableinfo.getPrimaryKey())
						.put("columns", tableinfo.getColumns()).put("common", config.getCommon())
						.put("datetime", DateTool.formatDateTime()).build();
		return VelocityUtils.generateFiles(map, tableinfo, getTemplates(config), localOrRemote);
	}

	/**
	 * 生成表信息
	 * 
	 * @param table 数据库表信息
	 * @param config 配置
	 */
	public static void generateTableinfoMap(Map<String, Object> table, List<Map<String, String>> columns,
			ConfigProperties config) {
		// 表名转换成Java类名
		String className = TableinfoUtils.tableToJava(String.valueOf(table.get("tableName")),
				config.getDatabase().getTablePrefix(), true);
		table.put("className", className);
		table.put("objectName", StrTool.firstLower(className));
		// 将处理后的字段信息添加到表信息中
		for (Map<String, String> column : columns) {
			// 判断是否为主键
			if ("PRI".equalsIgnoreCase(column.get("columnKey"))) {
				table.put("primaryKey", column);
				return;
			}
		}
	}

	/**
	 * 生成表信息
	 * 
	 * @param table 数据库表信息
	 * @param config 配置
	 */
	public static Tableinfo generateTableinfo(Map<String, Object> table, List<Map<String, String>> columns,
			ConfigProperties config, DataSourceProperties dataSourceProperties) {
		// 表名转换成Java类名
		String className = TableinfoUtils.tableToJava(String.valueOf(table.get("tableName")),
				config.getDatabase().getTablePrefix(), true);
		Tableinfo tableinfo = Tableinfo.builder().tableName(String.valueOf(table.get("tableName")))
				.comments(String.valueOf(table.get("tableComment"))).className(className)
				.objectName(StrTool.firstLower(className)).build();
		// 将处理后的字段信息添加到表信息中
		handlerColumninfo(tableinfo, columns, config);
		return tableinfo;
	}

	/**
	 * 处理表中的字段信息
	 * 
	 * @param tableinfo 表信息
	 * @param columns 字段信息
	 * @param config 配置信息
	 */
	public static void handlerColumninfo(Tableinfo tableinfo, List<Map<String, String>> columns,
			ConfigProperties config) {
		List<Columninfo> columsList = new ArrayList<>();
		for (Map<String, String> column : columns) {
			Columninfo columninfo =
					Columninfo.builder().columnName(column.get("columnName")).dataType(column.get("dataType"))
							.comments(column.get("columnComment")).columnKey(column.get("columnKey"))
							.extra(StrTool.getDefault(column.get("extra"), column.get("EXTRA"))).build();
			// 列名转换成Java属性名
			String attrName = StrTool.snake2Hump(columninfo.getColumnName());
			columninfo.setAttrNameUpper(attrName);
			columninfo.setAttrNameLower(StrTool.firstLower(attrName));

			// 列的数据类型,转换成Java类型
			String attrType =
					StrTool.getDefault(config.getDatabase().getDb2JavaType().get(columninfo.getDataType()), "String");
			columninfo.setAttrType(attrType);

			// 判断是否为主键
			if ("PRI".equalsIgnoreCase(column.get("columnKey"))) {
				tableinfo.setPrimaryKey(columninfo);
			}

			// 判断是否为唯一字段
			if (!tableinfo.isHasUnique() && "UNI".equalsIgnoreCase(String.valueOf(column.get("columnKey")))) {
				columninfo.setUnique(true);
				tableinfo.setHasUnique(true);
			}

			// 判断非空
			if ("NO".equalsIgnoreCase(column.get("nullable"))) {
				columninfo.setNullable(false);
			}

			// 判断是否为排序字段
			if (!tableinfo.isHasSort()
					&& config.getDatabase().getSortColumn().equalsIgnoreCase(column.get("columnName"))) {
				columninfo.setSortColumn(true);
				tableinfo.setHasSort(true);
			}

			if (!tableinfo.isHasBigDecimal() && attrType.equals("BigDecimal")) {
				tableinfo.setHasBigDecimal(true);
			}
			if (!tableinfo.isHasDate() && (attrType.equalsIgnoreCase("timestamp"))
					|| attrType.equalsIgnoreCase("datetime") || attrType.equalsIgnoreCase("date")) {
				tableinfo.setHasDate(true);
			}
			if (!tableinfo.isHasArray() && "array".equalsIgnoreCase(columninfo.getExtra())) {
				tableinfo.setHasArray(true);
			}
			columsList.add(columninfo);
		}
		tableinfo.setColumns(columsList);
	}

	public static List<String> getTemplates(ConfigProperties config) {
		List<String> templates = config.getTemplate().getDefaults();
		if (ListTool.isNotEmpty(config.getTemplate().getExtras())) {
			if (config.getTemplate().isAddDefaults()) {
				templates.addAll(config.getTemplate().getExtras());
			} else {
				templates = config.getTemplate().getExtras();
			}
		}
		// templates.add("template/menu.sql.vm");
		// templates.add("template/index.vue.vm");
		// templates.add("template/add-or-update.vue.vm");
		return templates.stream().filter(t -> {
			if (!config.getCommon().isGenerateModel() && t.toLowerCase().contains("model.java.vm")) {
				return false;
			}
			if (config.getCommon().isGenerateMapper() && t.toLowerCase().contains("mapper.java.vm")) {
				return false;
			}
			if (!config.getCommon().isGenerateService() && t.toLowerCase().contains("service.java.vm")) {
				return false;
			}
			if (!config.getCommon().isGenerateServiceImpl() && t.toLowerCase().contains("serviceimpl.java.vm")) {
				return false;
			}
			if (!config.getCommon().isGenerateCrl() && (t.toLowerCase().contains("crl.java.vm"))
					|| t.toLowerCase().contains("controller.java.vm")) {
				return false;
			}
			return true;
		}).collect(Collectors.toList());
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(ConfigProperties config, String template, String className) {
		String pathMain = config.getCommon().getPathMain();
		if (StrTool.isNotBlank(config.getCommon().getPathPackage())) {
			pathMain += config.getCommon().getPathPackage().replaceAll("\\.", Matcher.quoteReplacement(File.separator))
					+ File.separator;
		}
		if (StrTool.isNotBlank(config.getCommon().getModuleName())) {
			pathMain += config.getCommon().getModuleName() + File.separator;
		}
		if (template.contains("Model.java.vm")) {
			return pathMain + "entity" + File.separator + className + ".java";
		}
		if (template.contains("ModelDTO.java.vm")) {
			return pathMain + "dto" + File.separator + className + "DTO" + ".java";
		}
		if (template.contains("ModelQuery.java.vm")) {
			return pathMain + "query" + File.separator + className + "Query" + ".java";
		}
		if (template.contains("Convert.java.vm")) {
			return pathMain + "convert" + File.separator + className + "Convert" + ".java";
		}
		if (template.contains("Mapper.java.vm")) {
			return pathMain + "mapper" + File.separator + className + "Mapper.java";
		}
		if (template.contains("Service.java.vm")) {
			return pathMain + "service" + File.separator + className + "Service.java";
		}
		if (template.contains("ServiceImpl.java.vm")) {
			return pathMain + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}
		if (template.contains("Crl.java.vm")) {
			return pathMain + "crl" + File.separator + className + "Crl.java";
		} else if (template.contains("Controller.java.vm")) {
			return pathMain + "Controller" + File.separator + className + "Controller.java";
		}
		if (template.contains("Mapper.xml.vm")) {
			return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator
					+ config.getCommon().getModuleName() + File.separator + className + "Dao.xml";
		}
		if (template.contains("menu.sql.vm")) {
			return className.toLowerCase() + "_menu.sql";
		}
		if (template.contains("index.vue.vm")) {
			return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views"
					+ File.separator + "modules" + File.separator + config.getCommon().getModuleName() + File.separator
					+ className.toLowerCase() + ".vue";
		}
		if (template.contains("add-or-update.vue.vm")) {
			return "main" + File.separator + "resources" + File.separator + "src" + File.separator + "views"
					+ File.separator + "modules" + File.separator + config.getCommon().getModuleName() + File.separator
					+ className.toLowerCase() + "-add-or-update.vue";
		}
		return null;
	}

	/**
	 * 生成mybatis的generatorConfig.xml模板,只添加表信息到模板中
	 * 
	 * @param tableinfos 需要添加的表信息
	 * @param config 全局通用配置信息
	 * @param localOrRemote 本地生成代码或远程生成代码
	 */
	public static void generateMapperXml(List<Map<String, Object>> tableinfos, ConfigProperties config,
			boolean localOrRemote, DataSourceProperties dataSourceProperties) {
		// 封装模板数据
		Map<String, Object> map = MapTool.builder("tableinfos", tableinfos).put("datasource", dataSourceProperties)
				.put("common", JsonTools.parseMap(JSON.toJSONString(config.getCommon()))).build();
		VelocityUtils.generateFiles(map, localOrRemote);
	}

	/**
	 * 处理字段信息
	 * 
	 * @param columns 所有字段
	 * @return 所有字段与表对应关系
	 */
	public static Map<String, List<Map<String, String>>> handlerColumns(List<Map<String, String>> columns) {
		if (ListTool.isEmpty(columns)) {
			return null;
		}
		Map<String, List<Map<String, String>>> result = new HashMap<String, List<Map<String, String>>>();
		for (Map<String, String> column : columns) {
			List<Map<String, String>> tempColumns = result.get(column.get("tableName"));
			if (ListTool.isEmpty(tempColumns)) {
				tempColumns = new ArrayList<Map<String, String>>();
				result.put(column.get("tableName"), tempColumns);
			}
			tempColumns.add(column);
		}
		return result;
	}
}