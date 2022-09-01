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

	/** 不从数据库映射到Entity的字段 */
	private String[] excludeEntityColumns = new String[] { "create_time", "create_user", "update_time", "update_user" };

	/** 不从数据库映射到EntityDTO的字段 */
	private String[] excludeEntityDtoColumns = new String[] {};

	/** 不从数据库映射到Qyert的字段 */
	private String[] excludeQueryColumns = new String[] {};

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
			put("float", "BigDecimal");
			put("FLOAT", "BigDecimal");
			put("BINARY_FLOAT", "BigDecimal");
			put("double", "BigDecimal");
			put("decimal", "BigDecimal");
			put("DECIMAL", "BigDecimal");
			put("DOUBLE", "BigDecimal");
			put("BINARY_DOUBLE", "BigDecimal");
			put("numeric", "BigDecimal");
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
			put("bit", "Integer");
			put("date", "LocalDate");
			put("datetime", "LocalDateTime");
			put("timestamp", "LocalDateTime");
			put("DATE", "LocalDateTime");
			put("DATETIME", "LocalDateTime");
			put("TIMESTAMP", "LocalDateTime");
			put("TIMESTAMP(6)", "LocalDateTime");
		}
	};
}