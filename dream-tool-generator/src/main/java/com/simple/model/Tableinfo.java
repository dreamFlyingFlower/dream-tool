package com.simple.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 表相关属性
 * 
 * @author ParadiseWY
 * @date 2020-12-11 17:01:04
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tableinfo {

	/**
	 * 表的名称
	 */
	private String tableName;

	/**
	 * 表引擎类型
	 */
	private String engine;

	/**
	 * 表的备注
	 */
	private String comments;

	/**
	 * 类名,第一个字母大写,如:sys_user->SysUser
	 */
	private String className;

	/**
	 * 实例名,第一个字母小写,如:sys_user->sysUser
	 */
	private String objectName;

	/**
	 * 表的列名(不包含主键)
	 */
	private List<Columninfo> columns;

	/**
	 * 主键字段,可为null,多主键暂时不考虑
	 */
	private Columninfo primaryKey;

	/**
	 * 判断是否有BigDecimal类型
	 */
	private boolean entityHasBigDecimal;

	/**
	 * 判断是否有java.util.Date类型
	 */
	private boolean entityHasDate;

	/**
	 * 判断是否有java.time.LocalDate和java.time.LocalDateTime;
	 */
	private boolean entityHasLocalDate;

	private boolean entityHasLocalDateTime;

	/**
	 * 判断是否有BigDecimal类型
	 */
	private boolean entityDTOHasBigDecimal;

	/**
	 * 判断是否有java.util.Date类型
	 */
	private boolean entityDTOHasDate;

	/**
	 * 判断是否有java.time.LocalDate和java.time.LocalDateTime;
	 */
	private boolean entityDTOHasLocalDate;

	private boolean entityDTOHasLocalDateTime;

	/**
	 * 判断是否有BigDecimal类型
	 */
	private boolean queryHasBigDecimal;

	/**
	 * 判断是否有java.util.Date类型
	 */
	private boolean queryHasDate;

	/**
	 * 判断是否有java.time.LocalDate和java.time.LocalDateTime;
	 */
	private boolean queryHasLocalDate;

	private boolean queryHasLocalDateTime;

	/**
	 * 判断是否有数组类型
	 */
	private boolean hasArray;

	/**
	 * 判断表中是否有唯一字段
	 */
	private boolean hasUnique;

	/**
	 * 判断表中时候有排序字段
	 */
	private boolean hasSort;
}