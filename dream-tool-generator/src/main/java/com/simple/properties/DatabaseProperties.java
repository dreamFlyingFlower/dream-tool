package com.simple.properties;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库相关配置
 * 
 * @author ParadiseWY
 * @date 2020-12-11 16:39:26
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class DatabaseProperties {

	/** 默认数据库类型 */
	private String databaseType = "mysql";

	/** 表前缀,通过表名生成实体类时需要去除的前缀 */
	private String tablePrefix = "ti_,tb_,ts_,tr_,td_";

	/** 是否自动生成主键,mysql可用:若存在auto_increment,则不变;若是字符串,则自动生成32位不带-的uuid */
	private boolean autoPrimaryKey = true;

	/** 排序字段,当该字段存在时,会在实体类上添加相应排序注解 */
	private String sortColumn = "sort_index";

	/** 各种数据库与Java对照类型 */
	private Map<String, String> Db2JavaType = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;
		{
			put("tinyint", "Integer");
			put("smallint", "Integer");
			put("mediumint", "Integer");
			put("int", "Integer");
			put("integer", "Integer");
			put("NUMBER", "Integer");
			put("INT", "Integer");
			put("INTEGER", "Integer");
			put("BINARY_INTEGER", "Integer");
			put("int4", "Integer");
			put("int2", "Integer");
			put("bigint", "Long");
			put("int8", "Long");
			put("LONG", "Long");
			put("float", "Float");
			put("FLOAT", "Float");
			put("BINARY_FLOAT", "Float");
			put("double", "Double");
			put("decimal", "Double");
			put("DECIMAL", "Double");
			put("DOUBLE", "Double");
			put("BINARY_DOUBLE", "Double");
			put("char", "String");
			put("varchar", "String");
			put("tinytext", "String");
			put("mediumtext", "String");
			put("text", "String");
			put("longtext", "String");
			put("CHAR", "String");
			put("VARCHAR", "String");
			put("VARCHAR2", "String");
			put("NVARCHAR", "String");
			put("NVARCHAR2", "String");
			put("CLOB", "String");
			put("BLOB", "String");
			put("nvarchar", "String");
			put("numeric", "BigDecimal");
			put("bit", "Boolean");
			put("date", "Date");
			put("datetime", "Date");
			put("timestamp", "Date");
			put("DATE", "Date");
			put("DATETIME", "Date");
			put("TIMESTAMP", "Date");
			put("TIMESTAMP(6)", "Date");
		}
	};
}